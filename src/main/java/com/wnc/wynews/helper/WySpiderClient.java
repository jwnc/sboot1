
package com.wnc.wynews.helper;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.crawl.core.httpclient.AbstractHttpClient;
import com.crawl.core.httpclient.Config;
import com.crawl.core.httpclient.IHttpClient;
import com.crawl.core.util.SimpleThreadPoolExecutor;
import com.crawl.core.util.ThreadPoolMonitor;
import com.crawl.proxy.ProxyPool;
import com.crawl.spider.MySpiderAction;

public class WySpiderClient extends AbstractHttpClient implements IHttpClient
{
    private static volatile WySpiderClient instance;
    public static AtomicInteger parseCount = new AtomicInteger( 0 );
    public static volatile boolean isStop = false;
    private ThreadPoolExecutor localDataSaveThreadPool;
    private ThreadPoolExecutor netPageThreadPool;

    public static WySpiderClient getInstance()
    {
        if ( instance == null )
        {
            synchronized ( WySpiderClient.class )
            {
                if ( instance == null )
                {
                    instance = new WySpiderClient();
                }
            }
        }

        return instance;
    }

    private WySpiderClient()
    {
        this.initHttpClient();
        this.intiThreadPool();
    }

    public void initHttpClient()
    {
    }

    private void intiThreadPool()
    {
        this.localDataSaveThreadPool = new SimpleThreadPoolExecutor( 10, 10, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue(),
                "localDataSaveThreadPool" );
        this.netPageThreadPool = new SimpleThreadPoolExecutor(
                Config.downloadThreadSize, Config.downloadThreadSize, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue(),
                "netPageThreadPool" );
        (new Thread( new ThreadPoolMonitor( this.netPageThreadPool,
                "netPageThreadPool" ) )).start();
        (new Thread( new Runnable()
        {
            public void run()
            {
                WySpiderClient.this.manageHttpClient();
            }
        } )).start();
    }

    public void startCrawl( MySpiderAction myAction )
    {
        myAction.execute();
    }

    public void manageHttpClient()
    {
        while ( true )
        {
            System.err.println( "WySpiderClient抓取总数：" + parseCount.get()
                    + " 闲置代理数:" + ProxyPool.proxyQueue.size() );

            try
            {
                Thread.sleep( 10000L );
            } catch ( InterruptedException var6 )
            {
                var6.printStackTrace();
            }
        }
    }

    public void submitTask( Runnable runnable )
    {
        this.netPageThreadPool.execute( runnable );
    }

    public void counterTaskComp()
    {
        parseCount.getAndIncrement();
    }

    public void counterReset()
    {
        WySpiderClient.parseCount.set( 0 );
    }

    public ThreadPoolExecutor getNetPageThreadPool()
    {
        return this.netPageThreadPool;
    }

    public ThreadPoolExecutor getLocalDataSaveThreadPool()
    {
        return this.localDataSaveThreadPool;
    }
}
