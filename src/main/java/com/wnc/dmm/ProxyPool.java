
package com.wnc.dmm;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.crawl.proxy.entity.Proxy;

/**
 * 自定义的日本ProxyPool
 * 
 * @author nengcai.wang
 */
public class ProxyPool
{
    public final static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    public final static Set<Proxy> proxySet = new HashSet<Proxy>();
    public final static DelayQueue<Proxy> proxyQueue = new DelayQueue();

    public static void addProxy( Proxy proxy )
    {
        if ( proxySet.add( proxy ) )
        {
            proxyQueue.add( proxy );
        }
    }
}