
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
import com.wnc.dmm.DmmSpiderClient;
import com.wnc.dmm.DmmUtils;
import com.wnc.dmm.JpProxyUtil;
import com.wnc.dmm.task.MovieDetailTask;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

@RunWith( SpringRunner.class )
@SpringBootTest
public class DmmDetailTest
{
    @Test
    public void a() throws IOException, InterruptedException
    {
        new JpProxyUtil().initProxyPool();
        List<String> readFrom = FileOp
                .readFrom( DmmConsts.APP_DIR + "cid-all.txt" );
        String cid;
        for ( String s : readFrom )
        {
            cid = PatternUtil.getLastPatternGroup( s, "cid=(.+)/" );
            String movieDetailLocation = DmmUtils.getMovieDetailLocation( cid );
            if ( BasicFileUtil.isExistFolder( movieDetailLocation )
                    && new File( movieDetailLocation ).listFiles().length == 3 )
            {
                // 1dvdes00592
                System.out.println( "已爬过完整内容" );
                continue;
            }
            DmmSpiderClient.getInstance()
                    .submitTask( new MovieDetailTask( cid ) );
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
