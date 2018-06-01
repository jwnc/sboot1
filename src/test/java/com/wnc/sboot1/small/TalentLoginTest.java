
package com.wnc.sboot1.small;

import java.io.IOException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

import com.crawl.core.util.HttpClientUtil;

public class TalentLoginTest
{
    public static void main( String[] args ) throws IOException
    {
        HttpRequestBase request = new HttpGet(
                "http://localhost/ucar-talent/login/secret.do" );
        request.addHeader( "Cookie",
                "ukey=6dedc8cf-31d3-4bf9-a48e-f24d8482e0fc; JSESSIONID=4E88787B19F1E73FEE61A83722400979.114" );
        String webPage = HttpClientUtil.getWebPage( request );
        System.out.println( webPage );
    }
}
