
package com.wnc.sboot1.spy.zuqiu;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawl.core.util.HttpClientUtil;
import com.crawl.proxy.ProxyPool;
import com.crawl.proxy.entity.Direct;
import com.crawl.proxy.entity.Proxy;
import com.wnc.sboot1.spy.util.ProxyProcess;

@Component
public class Zb8CommentBatchUtils
{

    @Autowired
    ProxyProcess proxyProcess;
    ExecutorService newFixedThreadPool = Executors.newFixedThreadPool( 20 );

    String testUrl = "https:// www.zhibo8.cc/zuqiu/2018/0613-683ca39-svideo.htm";

    public Zb8CommentBatchUtils()
    {
    }

    public void init() throws IOException
    {
        proxyProcess.init();
    }

    public void downById( String url, int cid, int times )
    {
        operateComment( "down", url, cid, times );
    }

    public void upById( String url, int cid, int times )
    {
        operateComment( "up", url, cid, times );
    }

    volatile int ret = 0;

    private void operateComment( final String type, final String url, int cid,
            final int times )
    {
        while ( true )
        {
            for ( int i = 0; i < 20; i++ )
            {
                try
                {
                    Thread.sleep( new Random().nextInt( 900 ) + 100 );
                } catch ( InterruptedException e1 )
                {
                }
                final String curl = "https://pl.zhibo8.cc/code/" + type
                        + ".php?act=jsonp&cid=" + cid + "&callback=jsonp"
                        + System.currentTimeMillis();
                newFixedThreadPool.execute( new Runnable()
                {

                    @Override
                    public void run()
                    {
                        try
                        {
                            if ( ProxyPool.proxyQueue.size() == 0 )
                            {
                                return;
                            }
                            Proxy take = ProxyPool.proxyQueue.take();
                            if ( take instanceof Direct )
                            {
                                return;
                            }
                            HttpGet get = new HttpGet( curl );
                            get.setHeader( "referer", url );
                            get.setConfig( HttpClientUtil
                                    .getRequestConfigBuilder()
                                    .setProxy( new HttpHost( take.getIp(),
                                            take.getPort() ) )
                                    .build() );
                            get.setHeader( "cookie",
                                    "UM_distinctid=16385ba7906214-0951c50cb2c971-737356c-1fa400-16385ba7907ee0; room_id=1; Hm_lvt_3212511d67978fc36e99a8ba103a1cc8=1528078719,1528178684,1528424295,1528432344; Hm_lpvt_3212511d67978fc36e99a8ba103a1cc8=1528432344" );
                            String webPage = HttpClientUtil.getWebPage( get );
                            if ( webPage.contains( "\"status\":1" ) )
                            {
                                ret++;
                                System.out
                                        .println( type.equals( "up" ) ? "点赞成功！"
                                                : "点踩成功！" );
                                System.out.println( "当前进度：" + ret );
                            }

                        } catch ( Exception e )
                        {
                            // e.printStackTrace();
                        }
                    }
                } );
            }
            try
            {
                Thread.sleep( 30 * 1000L );
            } catch ( InterruptedException e )
            {
                e.printStackTrace();
            }
            System.out.println( "当前进度：" + ret );
            if ( ret >= times || ProxyPool.proxyQueue.isEmpty() )
            {
                System.exit( 0 );
                break;
            }

        }
    }
}
