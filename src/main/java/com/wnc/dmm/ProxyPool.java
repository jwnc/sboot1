
package com.wnc.dmm;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.crawl.proxy.entity.Proxy;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.string.PatternUtil;

/**
 * 自定义的日本ProxyPool
 * 
 * @author nengcai.wang
 */
public class ProxyPool
{
    public final static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    public final static Set<String> proxySet = Collections
            .synchronizedSet( new HashSet<String>() );
    public final static DelayQueue<Proxy> proxyQueue = new DelayQueue();

    public synchronized static void addProxy( Proxy proxy )
    {
        if ( proxySet.add( proxy.getProxyStr() ) )
        {
            proxyQueue.add( proxy );
            System.out.println( proxy.getProxyStr() );
        }
    }

    public static void addProxy( String proxyStr )
    {
        String ip = PatternUtil.getFirstPattern( proxyStr,
                "\\d+.\\d+.\\d+.\\d+" );
        int port = BasicNumberUtil
                .getNumber( PatternUtil.getLastPattern( proxyStr, "\\d+" ) );
        Proxy e = new Proxy( ip, port, 1000 );
        addProxy( e );
    }
}