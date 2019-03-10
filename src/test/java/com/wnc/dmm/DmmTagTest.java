
package com.wnc.dmm;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.crawl.spider.SpiderHttpClient;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.dmm.task.TagMoviesTask;
import com.wnc.string.PatternUtil;

public class DmmTagTest
{
    @Test
    public void a() throws Exception
    {
        new JpProxyUtil().initProxyPool();

        // singleTest();
        Document documentResult = getDocumentResult(
                "http://www.dmm.co.jp/litevideo/-/genre/" );
        Elements select = documentResult
                .select( ".d-sect > .d-item > ul > li > a" );
        for ( org.jsoup.nodes.Element element : select )
        {
            System.out.println( element.text() );
            String tid = PatternUtil
                    .getLastPatternGroup( element.attr( "href" ), "\\d+" );
            // BasicFileUtil.writeFileString( "grene.txt",
            // tid + " / " + element.text() + "\r\n",
            // null, true );
            SpiderHttpClient.getInstance().getNetPageThreadPool().execute(
                    new TagMoviesTask( BasicNumberUtil.getNumber( tid ), 1 ) );
        }

        Thread.sleep( 11111111111L );
    }

    private void singleTest() throws InterruptedException
    {
        SpiderHttpClient.getInstance().getNetPageThreadPool()
                .execute( new TagMoviesTask( 1027, 1 ) );
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
