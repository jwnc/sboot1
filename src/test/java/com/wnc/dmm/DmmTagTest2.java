
package com.wnc.dmm;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import com.crawl.spider.SpiderHttpClient;
import com.wnc.dmm.task.TagMoviesTask;

public class DmmTagTest2
{
    @Test
    public void a() throws Exception
    {
        new JpProxyUtil().initProxyPool();

        singleTest();

    }

    private void singleTest() throws InterruptedException
    {
        SpiderHttpClient.getInstance().getNetPageThreadPool()
                .execute( new TagMoviesTask( 5020, 1 ) );
        Thread.sleep( 11111111111L );
    }

    public static Document getDocumentResult( String url ) throws Exception
    {
        return Jsoup.connect( url ).timeout( 60000 )
                .proxy( "143.90.15.193", 8080 )
                .header( "User-Agent",
                        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36" )
                .get();
    }
}
