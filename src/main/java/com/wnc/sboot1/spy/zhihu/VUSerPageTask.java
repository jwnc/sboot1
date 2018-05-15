
package com.wnc.sboot1.spy.zhihu;

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
    private String utoken;
    private String apiUrl;
    // 以首个任务开始请求接口时的时间为准, 而不是初始化提交到线程池时为准
    // 在重试和下一个任务时, beginSpyDate会一直流转下去
    private Date beginSpyDate;
    private ActivitySpy activitySpy;
    private UserV userV;

    public VUSerPageTask( String apiUrl,UserV userV,boolean b,
            ActivitySpy activitySpy,Date beginSpyDate )
    {
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
        spiderHttpClient.getNetPageThreadPool().execute( new VUSerPageTask(
                apiUrl, userV, proxyFlag, activitySpy, this.beginSpyDate ) );
    }

    @Override
    protected void handle( Page page )
    {
        System.out.println( "进行:" + utoken );
        JSONObject restData = JSONObject.parseObject( page.getHtml() );

        JSONArray jsonArray = restData.getJSONArray( "data" );
        int size = jsonArray.size();
        if ( size > 0 )
        {
            if ( !isNewActivity( jsonArray.getJSONObject( 0 ) ) )
            {
                taskStop( "无最新动态" );
                return;
            }

            // 消费本次动态列表
            List<Activity> parseArray = jsonArray.toJavaList( Activity.class );
            activitySpy.save( parseArray );

            JSONObject lastActObj = jsonArray.getJSONObject( size - 1 );
            if ( isNewActivity( lastActObj ) )
            {
                nextJob( restData );
            } else
            {
                taskStop( "已经到了上次的截止期限" );
            }
        } else
        {
            taskStop( "无动态" );
        }
    }

    /**
     * 任务停止, 并更新最后时间
     * 
     * @param msg
     */
    private void taskStop( String msg )
    {
        activitySpy.updateLastTime( userV.getUserToken(), beginSpyDate );
        taskLog( utoken + msg );
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

    @Override
    protected void complete( int type, String msg )
    {

        try
        {
            super.complete( type, msg );

            if ( type != COMPLETE_STATUS_SUCCESS )
            {
                logger.error( utoken + "在" + apiUrl + "失败, 失败原因:" + msg );
                activitySpy.errLog( utoken, apiUrl, msg );
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
