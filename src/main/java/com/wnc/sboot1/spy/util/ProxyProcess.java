
package com.wnc.sboot1.spy.util;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawl.core.httpclient.Constants;
import com.crawl.proxy.ProxyPool;
import com.crawl.proxy.entity.Proxy;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.sboot1.cluster.service.ProxyListService;
import com.wnc.string.PatternUtil;

@Component
public class ProxyProcess
{
    private static final int _200 = 200;

    private static Set<Proxy> cache = Collections
            .synchronizedSet( new HashSet<Proxy>() );

    private static boolean detectOpen = false;

    @Autowired
    ProxyListService proxyListService;

    public void init() throws IOException
    {
        if ( detectOpen )
        {
            return;
        }
        detectOpen = true;
        detectBackground();
    }

    private void fillQueue() throws IOException
    {
        List<String> fatestProxies = proxyListService.getFatestProxies();
        for ( int i = 0; i < fatestProxies.size(); i++ )
        {
            String info = fatestProxies.get( i );
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
                while ( true )
                {
                    if ( ProxyPool.proxyQueue.size() < _200 )
                    {
                        try
                        {
                            fillQueue();
                            // 填充完毕, 至少再等10分钟再看
                            Thread.sleep( 1000 * 60 * 10 );
                        } catch ( Exception e )
                        {
                            e.printStackTrace();
                        }
                    }
                    try
                    {
                        Thread.sleep( 5000 );
                    } catch ( InterruptedException e )
                    {
                        e.printStackTrace();
                    }
                }
            }
        } ).start();
    }
}
