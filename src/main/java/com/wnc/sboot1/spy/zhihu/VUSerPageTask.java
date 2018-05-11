
package com.wnc.sboot1.spy.zhihu;

import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawl.spider.entity.Page;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.sboot1.spy.zhihu.active.Activity;

/**
 * 找出两个月之间的
 */
public class VUSerPageTask extends AbstractPageTask
{
    private static Logger logger = Logger.getLogger( VUSerPageTask.class );
    private String utoken;
    private String apiUrl;

    private ActivitySpy activitySpy;

    public VUSerPageTask( String apiUrl,String utoken,boolean b,
            ActivitySpy activitySpy )
    {
        this.apiUrl = apiUrl;
        this.utoken = utoken;
        this.proxyFlag = b;
        this.activitySpy = activitySpy;
        request = new HttpGet( apiUrl );
        request.setHeader( "authorization",
                "oauth " + TT2.initAuthorization() );
    }

    @Override
    protected void retry()
    {
        spiderHttpClient.getNetPageThreadPool().execute(
                new VUSerPageTask( apiUrl, utoken, proxyFlag, activitySpy ) );
    }

    @Override
    protected void handle( Page page )
    {
        System.out.println( "进行:" + utoken );
        JSONObject parseObject = JSONObject.parseObject( page.getHtml() );

        JSONArray jsonArray = parseObject.getJSONArray( "data" );
        int size = jsonArray.size();
        if ( size > 0 )
        {
            if ( !isNewActivity( jsonArray.getJSONObject( 0 ) ) )
            {
                taskLog( utoken + "无最新动态" );
                return;
            }

            // 消费本次动态列表
            List<Activity> parseArray = jsonArray.toJavaList( Activity.class );
            activitySpy.save( parseArray );

            JSONObject jsonObject = jsonArray.getJSONObject( size - 1 );
            if ( isNewActivity( jsonObject ) )
            {
                nextJob( parseObject );
            } else
            {
                taskLog( utoken + "已经到了尽头" );
            }
        } else
        {
            taskLog( utoken + "无动态" );
        }
    }

    private boolean isNewActivity( JSONObject jsonObject )
    {
        return jsonObject.getLong( "created_time" )
                * 1000 > ActivitySpy.LAST_SEEK_TIME;
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
                activitySpy.nextJob( nextUrl, utoken, proxyFlag );
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
            activitySpy.callBackComplete( type, msg );
        }
    }
}
