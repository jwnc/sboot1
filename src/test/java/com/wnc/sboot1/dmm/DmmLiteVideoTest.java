
package com.wnc.sboot1.dmm;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wnc.basic.BasicFileUtil;
import com.wnc.dmm.DmmConsts;
import com.wnc.dmm.DmmUtils;
import com.wnc.dmm.JpProxyUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

@RunWith( SpringRunner.class )
@SpringBootTest
public class DmmLiteVideoTest
{
    private static final String IN_FILE = DmmConsts.APP_DIR + "cid-all.txt";
    private static final String IN_FILE_TEST = DmmConsts.TEST_DIR
            + "detail-cid-test1.txt";

    @Test
    public void a() throws IOException, InterruptedException
    {
        new JpProxyUtil().initProxyPool();
        List<String> readFrom = FileOp.readFrom( IN_FILE );
        String cid;
        String url;
        for ( String s : readFrom )
        {
            url = PatternUtil.getFirstPatternGroup( s, "(.+) " );
            cid = PatternUtil.getLastPatternGroup( s, "cid=(.+)/" );
            String movieDetailLocation = DmmUtils.getMovieDetailLocation( cid );
            if ( BasicFileUtil.isExistFolder( movieDetailLocation )
                    && new File( movieDetailLocation ).listFiles().length == 3 )
            {
                // 1dvdes00592
                // System.out.println( "已爬过完整内容" );
                continue;
            } else
            {
                System.out.println( "未爬过完整内容" + url );
            }
            // DmmSpiderClient.getInstance()
            // .submitTask( new LiteVideoTask( url ) );
        }

        try
        {
            Thread.sleep( 1000000000L );
        } catch ( InterruptedException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
