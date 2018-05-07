
package com.wnc.sboot1.itbook.helper;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class GithubLoginHelper
{
    public static void main( String[] args ) throws Exception
    {
        // login( getAuth() );
        Response execute = Jsoup.connect( "https://github.com/login" )
                .proxy( "openproxy.huawei.com", 8080 ).execute();
        Document parse = execute.parse();
        Map<String, String> cookies = execute.cookies();
        String auth = parse.select( "input[name='authenticity_token']" )
                .first().val();

        String githubLoginUrl = "https://github.com/session";
        Response execute2 = Jsoup.connect( githubLoginUrl ).cookies( cookies )
                .proxy( "openproxy.huawei.com", 8080 )
                .data( "commit", "Sign in" ).data( "utf8", "%E2%9C%93" )
                .data( "authenticity", auth )
                .data( "login", "529801034@qq.com" )
                .data( "password", "w2101024" ).method( Method.POST ).execute();
        System.out.println( execute2 );

    }

    private static String getAuth() throws IOException
    {
        Document document = Jsoup.connect( "https://github.com/login" )
                .proxy( "openproxy.huawei.com", 8080 ).get();
        return document.select( "input[name='authenticity_token']" ).first()
                .val();
    }

    private static void login( String auth )
    {
        String githubLoginUrl = "https://github.com/session";
        try
        {
            String post = Jsoup.connect( githubLoginUrl )
                    .proxy( "openproxy.huawei.com", 8080 )
                    .data( "commit", "Sign in" ).data( "utf8", "%E2%9C%93" )
                    .data( "authenticity", auth )
                    .data( "login", "5259801034@qq.com" )
                    .data( "password", "w2101024" ).method( Method.POST )
                    .ignoreContentType( true ).execute().body();
            System.out.println( post );
        } catch (

        Exception e )
        {
            e.printStackTrace();
        }
    }
}