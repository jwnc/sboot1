
package com.wnc.sboot1.spy.zhihu;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.crawl.proxy.ProxyPool;
import com.crawl.spider.entity.Page;
import com.crawl.spider.task.AbstractPageTask;

public class TT2
{
    static String key;
    static
    {
        AbstractPageTask.retryMap.put( GeneralPageTask.class,
                new HashMap<String, Integer>() );
    }

    public static void main( String[] args ) throws IOException
    {
        AbstractPageTask.retryMap.put( VUSerPageTask.class,
                new HashMap<String, Integer>() );
        AbstractPageTask.retryMap.put( GeneralPageTask.class,
                new HashMap<String, Integer>() );
        System.out.println( initAuthorization2() );
    }

    public static String initAuthorization2()
    {
        if ( key != null )
        {
            return key;
        }
        String content = "";
        Page page = null;
        long t = System.currentTimeMillis();
        while ( System.currentTimeMillis() - t < 600 * 1000 )
        {
            GeneralPageTask generalPageTask = new GeneralPageTask(
                    "https://www.zhihu.com/people/long-nian-sheng-7/following",
                    true );
            generalPageTask.run();
            page = generalPageTask.getPage();
            if ( page.getStatusCode() == 200 )
            {
                content = page.getHtml();
                if ( getJs( content ) != null )
                {
                    break;
                } else
                {
                    ProxyPool.proxyQueue
                            .remove( generalPageTask.getWrongProxy() );
                }
            }
        }

        String jsSrc = getJs( content );
        if ( jsSrc == null )
        {
            throw new RuntimeException( "not find javascript url" );
        }

        String jsContent = null;
        GeneralPageTask jsPageTask = new GeneralPageTask( jsSrc, true );
        jsPageTask.run();
        jsContent = jsPageTask.getPage().getHtml();

        Pattern pattern = Pattern.compile( "oauth (([0-9]|[a-z])+)" );
        Matcher matcher = pattern.matcher( jsContent );
        if ( matcher.find() )
        {
            String a = matcher.group( 1 );
            System.out.println( "初始化authoriztion完成" );
            key = a;
            return a;
        } else
        {
            throw new RuntimeException( "not get authorization" );
        }
    }

    public static String getJs( String content )
    {
        Pattern pattern = Pattern.compile(
                "https://static\\.zhihu\\.com/heifetz/main\\.app\\.([0-9]|[a-z])*\\.js" );
        Matcher matcher = pattern.matcher( content );
        String jsSrc = null;
        if ( matcher.find() )
        {
            jsSrc = matcher.group( 0 );
        }
        return jsSrc;
    }
}
