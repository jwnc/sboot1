
package com.wnc.dmm;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.crawl.core.httpclient.AbstractHttpClient;
import com.crawl.core.httpclient.Config;
import com.crawl.core.httpclient.IHttpClient;
import com.crawl.core.util.SimpleThreadPoolExecutor;
import com.crawl.core.util.ThreadPoolMonitor;
import com.crawl.spider.MySpiderAction;

public class DmmSpiderClient extends AbstractHttpClient implements IHttpClient
{
    private static volatile DmmSpiderClient instance;
    public static AtomicInteger parseCount = new AtomicInteger( 0 );
    private ThreadPoolExecutor localDataSaveThreadPool;
    private ThreadPoolExecutor netPageThreadPool;

    public static DmmSpiderClient getInstance()
    {
        if ( instance == null )
        {
            synchronized ( DmmSpiderClient.class )
            {
                if ( instance == null )
                {
                    instance = new DmmSpiderClient();
                }
            }
        }

        return instance;
    }

    private DmmSpiderClient()
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
                DmmSpiderClient.this.manageHttpClient();
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
            System.err.println( "DmmSpiderClient抓取总数：" + parseCount.get()
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
        DmmSpiderClient.parseCount.set( 0 );
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
