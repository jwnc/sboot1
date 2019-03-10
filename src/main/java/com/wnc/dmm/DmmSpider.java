
package com.wnc.dmm;

import java.io.IOException;
import java.util.List;

import com.wnc.dmm.task.DmmHotPageTask;
import com.wnc.dmm.task.MovieParamTask;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

public class DmmSpider
{

    public static void main( String[] args )
            throws IOException, InterruptedException
    {

        List<String> logs = FileOp.readFrom( DmmConsts.SUC_LOG );
        new JpProxyUtil().initProxyPool();

        crawlPageData( logs );

        // 爬存量cid
        // crawlHistoryCid( logs );
    }

    private static void crawlPageData( List<String> logs )
    {
        for ( int i = 1; i <= 417; i++ )
        {
            if ( logs.contains( i + "页  HotPage 结束" ) )
            {
                // System.out.println( "已经完成..." + i );
                continue;
            }
            System.out.println( "待执行.." + i );
            DmmSpiderClient.getInstance().submitTask( new DmmHotPageTask( i ) );
        }
    }

    private static void crawlHistoryCid( List<String> logs )
    {
        // 加载之前没有爬的cid
        List<String> cids = FileOp.readFrom( DmmConsts.CID_LOG );
        String cid = null;
        for ( String line : cids )
        {
            cid = PatternUtil.getLastPatternGroup( line, "cid=(.+)/" );
            if ( logs.contains( cid + " getMp4Url 结束" ) )
            {
                continue;
            }
            System.out.println( "没有完成getMp4Url..." + cid );
            DmmSpiderClient.getInstance()
                    .submitTask( new MovieParamTask( cid ) );
        }
    }
}
