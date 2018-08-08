
package com.wnc.qqnews.demo;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.wnc.basic.BasicFileUtil;
import com.wnc.tools.FileOp;

public class QqArticleManager
{
    private static Set<Long> articleSet = Collections
            .synchronizedSet( new HashSet<Long>() );
    private static String articlesFile = QqConsts.ARTICLES_TXT;
    static
    {
        initArticles();
    }

    public static void initArticles()
    {
        if ( BasicFileUtil.isExistFile( articlesFile ) )
        {
            List<String> lines = FileOp.readFrom( articlesFile, "UTF-8" );
            for ( String line : lines )
            {
                long aid = JSONObject.parseObject( line )
                        .getLongValue( "targetid" );
                articleSet.add( aid );
            }
        }
    }

    public static synchronized void addAndWriteArticle( JSONObject jsonObject )
    {
        try
        {
            long aid = jsonObject.getLongValue( "targetid" );
            if ( articleSet.add( aid ) )
            {
                BasicFileUtil.writeFileString( articlesFile,
                        jsonObject.toJSONString() + "\r\n", null, true );
            } else
            {
                System.out.println( aid + " 文章已经存在!" );
            }
        } catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    public static void main( String[] args )
    {
        initArticles();
    }
}
