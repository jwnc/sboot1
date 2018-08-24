
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
import com.wnc.dmm.task.MovieDetailMonoTask;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

@RunWith( SpringRunner.class )
@SpringBootTest
public class DmmDetailTest_ErrDeal
{
    @Test
    public void testMovieDetail2() throws IOException, InterruptedException
    {
        new JpProxyUtil().initProxyPool();
        String cid = "";
        List<String> readFrom = FileOp
                .readFrom( DmmConsts.DETAIL_DIR + "err-mdetail-1.txt" );
        for ( String line : readFrom )
        {
            cid = PatternUtil.getFirstPatternGroup( line, "(.*?) " );
            String movieDetailLocation = DmmUtils.getMovieDetailLocation( cid );
            if ( BasicFileUtil.isExistFolder( movieDetailLocation )
                    && new File( movieDetailLocation ).listFiles().length == 3 )
            {
                System.out.println( "Ok" );
            } else
            {
                System.out.println( cid + " begin..." );
                DmmSpiderClient.getInstance()
                        .submitTask( new MovieDetailMonoTask( cid ) );
            }
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