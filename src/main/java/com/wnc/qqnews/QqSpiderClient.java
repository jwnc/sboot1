
package com.wnc.qqnews;

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

public class QqSpiderClient extends AbstractHttpClient implements IHttpClient
{
    private static volatile QqSpiderClient instance;
    public static AtomicInteger parseCount = new AtomicInteger( 0 );
    private ThreadPoolExecutor localDataSaveThreadPool;
    private ThreadPoolExecutor netPageThreadPool;

    public static QqSpiderClient getInstance()
    {
        if ( instance == null )
        {
            synchronized ( QqSpiderClient.class )
            {
                if ( instance == null )
                {
                    instance = new QqSpiderClient();
                }
            }
        }

        return instance;
    }

    private QqSpiderClient()
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
                "QqSpiderClient-localDataSaveThreadPool" );
        this.netPageThreadPool = new SimpleThreadPoolExecutor(
                Config.downloadThreadSize, Config.downloadThreadSize, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue(),
                "QqSpiderClient-netPageThreadPool" );
        (new Thread( new ThreadPoolMonitor( this.netPageThreadPool,
                "QqSpiderClient-netPageThreadPool" ) )).start();
        (new Thread( new Runnable()
        {
            public void run()
            {
                QqSpiderClient.this.manageHttpClient();
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
            System.err.println( "QqSpiderClient抓取总数：" + parseCount.get()
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
        QqSpiderClient.parseCount.set( 0 );
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
