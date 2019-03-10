
package com.wnc.dmm;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.methods.HttpGet;

import com.crawl.spider.entity.Page;
import com.wnc.basic.BasicFileUtil;
import com.wnc.sboot1.cluster.util.PageUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

public class JpProxyUtil
{

    public void initProxyPool() throws IOException, InterruptedException
    {
        if ( ProxyPool.proxyQueue.size() < 10 )
        {
            getAllProxy();
            new Thread( new Runnable()
            {
                @Override
                public void run()
                {
                    while ( true )
                    {
                        try
                        {
                            if ( ProxyPool.proxyQueue.size() < 30 )
                            {
                                int get66Proxy = get66Proxy();
                                if ( get66Proxy < 20 )
                                {
                                    getLocalProxy();
                                }
                            }
                            Thread.sleep( 10 * 1000 );
                        } catch ( Exception e )
                        {
                            e.printStackTrace();
                        }
                    }
                }

            } ).start();
        }
    }

    private void getAllProxy()
    {
        getLocalProxy();
        get66Proxy();
    }

    private int get66Proxy()
    {
        int sum = 0;
        try
        {
            HttpGet httpGet = new HttpGet(
                    "http://www.66ip.cn/mo.php?sxb=%C8%D5%B1%BE&tqsl=100&port=&export=&ktip=&sxa=&submit=%CC%E1++%C8%A1&textarea=" );
            Page webPage = PageUtil.getWebPage( httpGet, "UTF-8" );
            String content = webPage.getHtml();
            List<String> get61Proxies = PatternUtil.getAllPatternGroup( content,
                    "\\d+.\\d+.\\d+.\\d+:\\d+" );
            sum = get61Proxies.size();
            for ( String proxyStr : get61Proxies )
            {
                ProxyPool.addProxy( proxyStr );
            }
        } catch ( IOException e )
        {
            e.printStackTrace();
        }
        return sum;
    }

    private void getLocalProxy()
    {
        if ( !BasicFileUtil.isExistFile( DmmConsts.PROXY_FILE ) )
        {
            return;
        }
        List<String> proxies = FileOp.readFrom( DmmConsts.PROXY_FILE );
        for ( String string : proxies )
        {
            ProxyPool.addProxy( string );
        }
        System.out.println( "当前有效代理数量:" + ProxyPool.proxyQueue.size() );
    }

}
