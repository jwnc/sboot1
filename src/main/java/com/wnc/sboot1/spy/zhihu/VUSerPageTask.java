
package com.wnc.sboot1.spy.zhihu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawl.spider.entity.Page;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.sboot1.spy.zhihu.active.Activity;
import com.wnc.sboot1.spy.zhihu.active.UserV;

/**
 * 找出两个月之间的
 */
public class VUSerPageTask extends AbstractPageTask
{
    private static Logger logger = Logger.getLogger( VUSerPageTask.class );

    public static final int COMPLETE_STATUS_SQL_ERROR = 4;

    private String utoken;

    private String apiUrl;

    // 以首个任务开始请求接口时的时间为准, 而不是初始化提交到线程池时为准
    // 在重试和下一个任务时, beginSpyDate会一直流转下去
    private Date beginSpyDate;

    private ActivitySpy activitySpy;

    private UserV userV;

    private boolean ignoreComplete = false;

    public VUSerPageTask( String apiUrl,UserV userV,boolean b,
            ActivitySpy activitySpy,Date beginSpyDate )
    {
        this.MAX_RETRY_TIMES = 30;
        this.userV = userV;
        this.utoken = userV.getUserToken();
        if ( beginSpyDate != null )
        {
            this.beginSpyDate = beginSpyDate;
        }

        this.apiUrl = apiUrl;
        this.proxyFlag = b;
        this.activitySpy = activitySpy;

        request = new HttpGet( apiUrl );
        request.setHeader( "authorization",
                "oauth " + TT2.initAuthorization() );
    }

    public VUSerPageTask setMaxRetryTimes( int tm )
    {
        this.MAX_RETRY_TIMES = tm;
        return this;
    }

    @Override
    public void run()
    {
        if ( beginSpyDate == null )
        {
            this.beginSpyDate = new Date();
        }
        super.run();
    }

    @Override
    protected void retry()
    {
        if ( !activitySpy.isTaskOver() )
        {
            activitySpy.doJob( apiUrl, userV, proxyFlag, beginSpyDate );
        }
    }

    @Override
    protected void handle( Page page ) throws Exception
    {
        System.out.println( "进行:" + utoken );
        JSONObject restData = JSONObject.parseObject( page.getHtml() );

        JSONArray jsonArray = restData.getJSONArray( "data" );
        int size = jsonArray.size();
        if ( size > 0 )
        {
            if ( !isNewActivity( jsonArray.getJSONObject( 0 ) ) )
            {
                taskSuccStop( "无最新动态" );
                return;
            }
            removeIdAttr( jsonArray );
            // 消费本次动态列表
            List<Activity> parseArray = jsonArray.toJavaList( Activity.class );
            try
            {
                activitySpy.save( getNewList( parseArray ) );
            } catch ( Exception e )
            {
                String msg = "数据库执行严重异常, 请检查! - " + e.getMessage();
                logger.error( utoken + "在" + apiUrl + "失败, 失败原因:" + msg );
                try
                {
                    activitySpy.errLog( utoken, apiUrl, msg, currentProxy );
                } catch ( Exception e1 )
                {
                    e1.printStackTrace();
                }
                activitySpy.callBackComplete( COMPLETE_STATUS_SQL_ERROR, msg,
                        this );
                ignoreComplete = true;
                return;
            }

            JSONObject lastActObj = jsonArray.getJSONObject( size - 1 );
            if ( isNewActivity( lastActObj ) )
            {
                nextJob( restData );
                ignoreComplete = true;
            } else
            {
                taskSuccStop( "已经到了上次的截止期限" );
            }
        } else
        {
            emptyRetry();
        }
    }

    /**
     * 知乎api可能不太稳定，明明有动态但是接口返回为空。 这种情况不能更新last_spy_time，需要后期手动对用户进行爬取
     */
    private void emptyRetry()
    {
        taskLog( utoken + "无动态, 将重试" );
        super.retryMonitor( "当前用户没有任何动态" );
    }

    /**
     * 删除Activity中原来自带的id
     * 
     * @param jsonArray
     */
    private void removeIdAttr( JSONArray jsonArray )
    {
        for ( int i = 0; i < jsonArray.size(); i++ )
        {
            jsonArray.getJSONObject( i ).remove( "id" );
        }
    }

    public void errLogExp( Exception e )
    {
        // 不显示io异常
        if ( e instanceof IOException )
        {
            return;
        }
        e.printStackTrace();
    }

    private List<Activity> getNewList( List<Activity> parseArray )
    {
        List<Activity> list = new ArrayList<Activity>();
        for ( Activity activity : parseArray )
        {
            if ( isNewActivity( activity ) )
            {
                list.add( activity );
            }
        }
        return list;
    }

    /**
     * 任务成功停止, 并更新最后时间
     * 
     * @param msg
     */
    private void taskSuccStop( String msg )
    {
        activitySpy.updateLastTime( userV.getUserToken(), beginSpyDate );
        taskLog( utoken + msg + " 进度:" + activitySpy.getProgress() );
    }

    private boolean isNewActivity( Activity activity )
    {
        return activity.getCreated_time() * 1000 > userV.getLastSpyTime()
                .getTime();
    }

    private boolean isNewActivity( JSONObject jsonObject )
    {
        return jsonObject.getLong( "created_time" ) * 1000 > userV
                .getLastSpyTime().getTime();
    }

    private void taskLog( String msg )
    {
        logger.info( msg );
    }

    private void nextJob( JSONObject parseObject )
    {
        String nextUrl = null;
        try
        {
            nextUrl = parseObject.getJSONObject( "paging" ).getString( "next" );
        } catch ( Exception e )
        {
            e.printStackTrace();
        } finally
        {
            if ( nextUrl != null )
            {
                activitySpy.doJob( nextUrl, userV, proxyFlag,
                        this.beginSpyDate );
            }
        }

    }

    @Override
    protected String removeRandom()
    {
        return apiUrl.replaceAll( "(\\?|\\&)[^\\&\\?]+=0\\.\\d{16}", "" );
    }

    /**
     * 其他地方可能有自己的complete逻辑，需要忽略
     */
    @Override
    protected void complete( int type, String msg )
    {
        if ( ignoreComplete )
        {
            return;
        }
        try
        {
            super.complete( type, msg );

            if ( type != COMPLETE_STATUS_SUCCESS )
            {
                logger.error( utoken + "在" + apiUrl + "失败, 失败原因:" + msg );
                activitySpy.errLog( utoken, apiUrl, msg, currentProxy );
            }
        } catch ( Exception e )
        {
            e.printStackTrace();
        } finally
        {
            activitySpy.callBackComplete( type, msg, this );
        }
    }
}
