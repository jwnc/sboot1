
package com.wnc.dmm;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.crawl.proxy.entity.Proxy;

public class JsoupHelper
{

    public static String getJsonResult( String url ) throws Exception
    {
        return Jsoup.connect( url ).timeout( 60000 ).header( "User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36" )
                .ignoreContentType( true ).execute().body();
    }

    public static Document getDocumentResult( String url, Proxy currentProxy )
            throws Exception
    {
        return Jsoup.connect( url )
                .proxy( currentProxy.getIp(), currentProxy.getPort() )
                .timeout( 60000 )
                .header( "User-Agent",
                        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36" )
                .get();
    }
}
