
package com.wnc.dmm;

import java.io.IOException;
import java.util.List;

import com.crawl.proxy.entity.Proxy;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

public class JpProxyUtil
{

    public void initProxyPool() throws IOException, InterruptedException
    {
        if ( ProxyPool.proxyQueue.size() < 10 )
        {
            getProxy();
            new Thread( new Runnable()
            {
                @Override
                public void run()
                {
                    while ( true )
                    {
                        try
                        {
                            if ( ProxyPool.proxyQueue.size() < 10 )
                            {
                                getProxy();
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

    private void getProxy() throws IOException
    {
        List<String> proxies = FileOp.readFrom( DmmConsts.PROXY_FILE );
        for ( String string : proxies )
        {

            String ip = PatternUtil.getFirstPattern( string,
                    "\\d+.\\d+.\\d+.\\d+" );
            int port = BasicNumberUtil
                    .getNumber( PatternUtil.getLastPattern( string, "\\d+" ) );
            System.out.println( ip + " / " + port );
            Proxy e = new Proxy( ip, port, 1000 );
            ProxyPool.addProxy( e );
        }
        System.out.println( ProxyPool.proxyQueue.size() );
    }

}
