
package com.wnc.dmm;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.crawl.core.util.HttpClientUtil;
import com.crawl.spider.SpiderHttpClient;
import com.wnc.basic.BasicFileUtil;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

public class GimmeProxyUtil
{
    @Test
    public void testProxy() throws Exception
    {
        List<String> proxies = FileOp.readFrom( DmmConsts.PROXY_FILE );
        for ( String string : proxies )
        {

            String ip = PatternUtil.getFirstPattern( string,
                    "\\d+.\\d+.\\d+.\\d+" );
            int port = BasicNumberUtil
                    .getNumber( PatternUtil.getLastPattern( string, "\\d+" ) );
            System.out.println( ip + " / " + port );
            for ( int i = 0; i < 1; i++ )
            {
                proxyGet( ip, port );
            }
        }
    }

    private void proxyGet( String ip, int port ) throws IOException
    {
        try
        {
            String proxyUrl = "https://gimmeproxy.com/api/getProxy?country=JP&maxCheckPeriod=600";

            HttpHost proxy = new HttpHost( ip, port );
            HttpGet tempRequest = new HttpGet( proxyUrl );
            tempRequest.setConfig( HttpClientUtil.getRequestConfigBuilder()
                    .setProxy( proxy ).build() );

            String html = SpiderHttpClient.getInstance()
                    .getWebPage( tempRequest ).getHtml();
            JSONObject parseObject = JSONObject.parseObject( html );
            String proxyStr = parseObject.getString( "ip" ) + ":"
                    + parseObject.getString( "port" );
            System.out.println( proxyStr );
            BasicFileUtil.writeFileString(
                    DmmConsts.TEST_DIR + "gimmeproxy.txt", proxyStr + "\r\n",
                    null, true );
        } catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

}
