
package com.wnc.wynews.check;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.junit.Test;

import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

import translate.util.JsoupHelper;

public class CheckCmtDuplicate
{
    @Test
    public void a()
    {
        String folder = "C:\\data\\spider\\netease-news\\comments\\";
        Set<String> set = new HashSet<>();
        for ( File f : new File( folder ).listFiles() )
        {
            if ( f.isDirectory() )
            {
                for ( File f2 : f.listFiles() )
                {
                    List<String> list = FileOp.readFrom( f2.getAbsolutePath() );
                    set.addAll( list );
                    System.out.println( "Comment Size:" + set.size() );
                }
            }
        }
    }

    @Test
    public void b() throws Exception
    {
        Document documentResult = JsoupHelper
                .getDocumentResult( "http://news.ifeng.com/" );
        System.out.println( documentResult );
        String firstPatternGroup = PatternUtil.getFirstPatternGroup(
                documentResult.toString(), "var dataList=(.+\\]\\]);" );
        System.out.println( firstPatternGroup );
    }

}
