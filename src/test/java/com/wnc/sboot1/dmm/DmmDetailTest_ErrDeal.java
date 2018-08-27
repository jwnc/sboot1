
package com.wnc.sboot1.dmm;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.wnc.dmm.DmmConsts;
import com.wnc.dmm.JpProxyUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

//@RunWith( SpringRunner.class )
//@SpringBootTest
public class DmmDetailTest_ErrDeal
{
    @Test
    public void testMovieDetail2() throws IOException, InterruptedException
    {
        new JpProxyUtil().initProxyPool();
        String url = "";
        String cid = "";
        List<String> readFrom = FileOp
                .readFrom( DmmConsts.DETAIL_DIR + "suc-mdetail-4.txt" );
        for ( String line : readFrom )
        {
            url = PatternUtil.getFirstPatternGroup( line, "(http.*?) " );
            cid = PatternUtil.getLastPatternGroup( url, "cid=(.+)/" );
            if ( url.contains( "/rental/" ) )
            {

                System.out.println( url + " begin..." );
                // DmmSpiderClient.getInstance()
                // .submitTask( new MovieDetailRentalTask( cid, url ) );
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