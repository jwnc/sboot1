
package com.wnc.dmm;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.wnc.basic.BasicFileUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

public class DmmToolTest
{
    // @Test
    public void uniqueCid()
    {
        Set<String> set = new HashSet<String>();
        List<String> cids = FileOp.readFrom( DmmConsts.CID_LOG );
        String cid = null;
        for ( String line : cids )
        {
            cid = PatternUtil.getLastPatternGroup( line, "cid=(.+)/" );
            if ( set.add( cid ) )
            {
                BasicFileUtil.writeFileString( DmmConsts.CID_LOG + ".tmp",
                        line + "\r\n", null, true );
            }
        }
    }

    @Test
    public void uniqueCid2()
    {
        Set<String> set = new HashSet<String>();
        File[] listFiles = new File( "C:\\data\\spider\\dmm\\tags\\cids\\" )
                .listFiles();
        int sum = 0;
        for ( File file : listFiles )
        {
            List<String> cids = FileOp.readFrom( file.getAbsolutePath() );
            String cid = null;
            for ( String line : cids )
            {
                cid = PatternUtil.getLastPatternGroup( line, "cid=(.+)/" );
                if ( set.add( cid ) )
                {
                    BasicFileUtil.writeFileString(
                            DmmConsts.APP_DIR + "cid-all.txt", line + "\r\n",
                            null, true );
                    sum++;
                }
            }
        }
        System.out.println( sum );
    }

}
