
package com.wnc.sboot1.spy.util;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawl.core.httpclient.Constants;
import com.crawl.proxy.ProxyPool;
import com.crawl.proxy.entity.Proxy;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.string.PatternUtil;

public class ProxyProcess
{
    private static ProxyProcess proxyProcess = new ProxyProcess();

    private ProxyProcess()
    {

    }

    public static ProxyProcess getInstance()
    {
        return proxyProcess;
    }

    private Set<Proxy> cache = Collections
            .synchronizedSet( new HashSet<Proxy>() );

    public void init() throws IOException
    {
        if ( ProxyPool.proxyQueue.size() < 50 )
        {
            fillQueue();
            detectBackground();
        }
    }

    private void fillQueue() throws IOException
    {
        String str = SpiderUtils.getJsonHtml(
                "http://118.126.116.16:8080/sboot1/proxy/fatest200" );
        System.out.println( str );
        JSONObject parseObject = JSONObject.parseObject( str );
        JSONArray jsonArray = parseObject.getJSONArray( "data" );
        System.out.println( jsonArray.size() );
        for ( int i = 0; i < jsonArray.size(); i++ )
        {
            String info = jsonArray.getString( i );
            Proxy p = new Proxy(
                    PatternUtil.getFirstPatternGroup( info, "(.*?):" ),
                    BasicNumberUtil.getNumber(
                            PatternUtil.getLastPattern( info, "\\d+" ) ),
                    Constants.TIME_INTERVAL );
            if ( cache.add( p ) )
            {
                ProxyPool.proxyQueue.add( p );
            }
        }
    }

    private void detectBackground()
    {
        new Thread( new Runnable()
        {

            @Override
            public void run()
            {
                try
                {
                    while ( true )
                    {
                        if ( ProxyPool.proxyQueue.size() < 50 )
                        {
                            try
                            {
                                fillQueue();
                                // 填充完毕, 至少再等10分钟再看
                                Thread.sleep( 1000 * 60 * 10 );
                            } catch ( IOException e )
                            {
                                e.printStackTrace();
                            }
                        }
                        Thread.sleep( 5000 );
                    }
                } catch ( InterruptedException e )
                {
                    e.printStackTrace();
                }
            }
        } ).start();
    }
}
