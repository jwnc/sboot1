
package com.wnc.sboot1.cluster.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Autowired;

import com.crawl.core.util.HttpClientUtil;
import com.crawl.proxy.entity.Proxy;
import com.crawl.spider.entity.Page;
import com.wnc.sboot1.spy.util.ProxyProcess;
import com.wnc.string.PatternUtil;

public class ProxyUtil
{
    public static volatile boolean taskFlag1 = false;
    public static volatile boolean taskFlag2 = false;
    private static final String HTTPS_WWW_BAIDU_COM = "https://www.baidu.com";

    public static void main( String[] args ) throws IOException
    {
        long l = System.currentTimeMillis();
        if ( ProxyUtil.checkNetWork() )
        {
            long x = System.currentTimeMillis() - l;
            System.out.println( x );
            boolean checkAvailable = ProxyUtil
                    .checkAvailable( "159.65.184.229:3128" );
            System.out.println( checkAvailable );
        } else
        {
            System.out.println( "请检查网络" );
        }

    }

    public static boolean checkAvailable( String proxyStr ) throws IOException
    {
        HttpGet tempRequest = new HttpGet( HTTPS_WWW_BAIDU_COM );
        if ( StringUtils.isNotBlank( proxyStr ) )
        {
            String[] split = proxyStr.split( ":" );
            final Proxy currentProxy = new Proxy( split[0],
                    Integer.parseInt( split[1] ), 1000L );
            HttpHost proxy = new HttpHost( currentProxy.getIp(),
                    currentProxy.getPort() );
            tempRequest.setConfig( HttpClientUtil.getRequestConfigBuilder()
                    .setProxy( proxy ).build() );
        }

        Page page = PageUtil.getWebPage( tempRequest, "UTF-8" );
        if ( page.getStatusCode() == 200 )
        {
            return true;
        }
        return false;
    }

    public static boolean checkNetWork()
    {
        try
        {
            return checkAvailable( null );
        } catch ( IOException e )
        {
            e.printStackTrace();
        }
        return false;
    }

    final static int MAX_SIZE = 4000;

    @Autowired
    private static ProxyProcess proxyProcess;

    /**
     * 一次最多返回4000个, 不然校验过程太漫长
     * 
     * @return
     * @throws IOException
     */
    public static List<String> get61Proxies() throws IOException
    {
        HttpGet httpGet = new HttpGet( "http://www.66ip.cn/nmtq.php?getnum="
                + MAX_SIZE
                + "&isp=0&anonymoustype=0&start=&ports=&export=&ipaddress=&area=0&proxytype=2&api=66ip" );
        Page webPage = PageUtil.getWebPage( httpGet, "UTF-8" );
        String content = webPage.getHtml();
        List<String> list = PatternUtil.getAllPatternGroup( content,
                "\\d+.\\d+.\\d+.\\d+:\\d+" );
        List<String> retList = new ArrayList<String>();
        for ( String string : list )
        {
            retList.add( string );
        }
        return retList;
    }

}
