
package com.wnc.dmm;

import java.util.List;

import org.junit.Test;

import com.crawl.spider.SpiderHttpClient;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.dmm.task.TagMoviesTask;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

public class DmmTagTest3
{
    @Test
    public void a() throws Exception
    {
        new JpProxyUtil().initProxyPool();

        List<String> readFrom = FileOp.readFrom( "err.txt" );

        for ( String line : readFrom )
        {
            String tid = PatternUtil.getFirstPattern( line, "\\d+" );
            System.out.println( tid );
            // BasicFileUtil.writeFileString( "grene.txt",
            // tid + " / " + element.text() + "\r\n",
            // null, true );
            SpiderHttpClient.getInstance().getNetPageThreadPool().execute(
                    new TagMoviesTask( BasicNumberUtil.getNumber( tid ), 1 ) );
        }

        Thread.sleep( 11111111111L );
    }

}
