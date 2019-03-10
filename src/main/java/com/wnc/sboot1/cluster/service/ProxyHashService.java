
package com.wnc.sboot1.cluster.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.crawl.spider.entity.Page;
import com.wnc.sboot1.cluster.util.MapUtil;
import com.wnc.sboot1.cluster.util.MapUtil.KeyValue;
import com.wnc.sboot1.cluster.util.PageUtil;
import com.wnc.sboot1.cluster.util.ProxyUtil;
import com.wnc.string.PatternUtil;

@Component
public class ProxyHashService
{
    private static final int INVALID_PROXY_REPLYTIME = -1;
    private static final int DEFAULT_PROXY_REPLYTIME = 10000;// 默认10秒,
                                                             // 尽量让后面计算出的优秀代理排前面
    private static final int ENOUGH_COUNT = 1000;// 只有在不足的情况下, 才执行导入网络资源
    private final String redisHashKey = "proxyHtable";
    private Logger logger = Logger.getLogger( ProxyHashService.class );

    private StringRedisTemplate stringRedisTemplate;
    private RedisTemplate redisTemplate;
    private HashOperations opsForHash;
    ListOperations<String, String> opsForList;

    private ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)Executors
            .newFixedThreadPool( 30 );
    private volatile int checkCount = 0;

    // 构造函数使用@Autowired
    @Autowired
    public ProxyHashService( StringRedisTemplate stringRedisTemplate,
            RedisTemplate redisTemplate )
    {
        checkCount = 0;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisTemplate = redisTemplate;
        opsForHash = redisTemplate.opsForHash();
        opsForList = stringRedisTemplate.opsForList();
    }

    /**
     * 从66Ip批量导入代理, 可用于定时任务
     * 
     * @throws Exception
     */
    public int importProxies() throws Exception
    {
        // 用在正式
         List<String> get61Proxies = ProxyUtil.get61Proxies();
        int sum = 0;
        for ( String proxyStr : get61Proxies )
        {
            try
            {
                sum += addProxy( proxyStr, DEFAULT_PROXY_REPLYTIME ) ? 1 : 0;
            } catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
        return sum;
    }

    public List<String> getFatestProxies( int count )
    {
        Map<String, Integer> readAvailableProxy = readAvailableProxy();
        logger.info( "有效代理数目:" + readAvailableProxy.size() );
        List<KeyValue> map2List = MapUtil.map2List( readAvailableProxy );
        Collections.sort( map2List, new Comparator<KeyValue>()
        {
            @Override
            public int compare( KeyValue o1, KeyValue o2 )
            {
                if ( o1.getV() instanceof Integer
                        && o2.getV() instanceof Integer )
                {
                    return Integer.parseInt( o1.getV() + "" )
                            - Integer.parseInt( o2.getV() + "" );
                }
                return 0;
            }
        } );
        List<String> ret = new ArrayList<String>();
        List<KeyValue> subList = map2List.subList( 0,
                Math.min( count, map2List.size() ) );
        for ( KeyValue keyValue : subList )
        {
            ret.add( keyValue.getK() );
        }
        return ret;
    }

    /**
     * 采用相应时间做value
     * 
     * @param proxyStr
     * @param replyTime
     * @throws IOException
     */
    public boolean addProxy( String proxyStr, int replyTime ) throws IOException
    {
        if ( !opsForHash.hasKey( redisHashKey, proxyStr ) )
        {
            opsForHash.put( redisHashKey, proxyStr, replyTime );
            return true;
        }
        return false;
    }

    /**
     * 逻辑删除, 置为false
     * 
     * @param proxyStr
     * @throws IOException
     */
    public void removeProxy( String proxyStr ) throws IOException
    {
        Boolean hasKey = opsForHash.hasKey( redisHashKey, proxyStr );
        // logger.info("removeProxy: " + proxyStr);
        if ( hasKey )
        {
            opsForHash.delete( redisHashKey, proxyStr );
        }
    }

    public Map<String, Integer> readAllProxy()
    {
        return opsForHash.entries( redisHashKey );
    }

    public Map<String, Integer> readAvailableProxy()
    {
        Map<String, Integer> allProxies = readAllProxy();
        Map<String, Integer> availableProxies = new HashMap<String, Integer>();
        for ( Map.Entry<String, Integer> entry : allProxies.entrySet() )
        {
            if ( entry.getValue() > INVALID_PROXY_REPLYTIME )
            {
                availableProxies.put( entry.getKey(), entry.getValue() );
            }
        }
        return availableProxies;
    }

    /**
     * 讀取失效代理
     * 
     * @return
     * @throws IOException
     */
    public void removeDisableProxy() throws IOException
    {
        Map<String, Integer> allProxies = readAllProxy();
        for ( Map.Entry<String, Integer> entry : allProxies.entrySet() )
        {
            if ( entry.getValue() == INVALID_PROXY_REPLYTIME )
            {
                removeProxy( entry.getKey() );
            }
        }
        logger.info( "失效代理删除完毕." );
    }

    public boolean isEnough()
    {
        return readAvailableProxy().size() >= ENOUGH_COUNT;
    }

    /**
     * 检查代理有效性
     * 
     * @throws IOException
     */
    public void checkAll()
    {
        checkCount = 0;
        Map<String, Integer> availableProxies = readAvailableProxy();
        final int size = availableProxies.size();
        logger.info( "checkAll任务数:" + size );
        for ( final Map.Entry<String, Integer> entry : availableProxies
                .entrySet() )
        {
            threadPoolExecutor.execute( new Runnable()
            {
                @Override
                public void run()
                {
                    String proxyStr = entry.getKey();
                    // if ( !ProxyUtil.checkNetWork() )
                    // {
                    // return;
                    // }
                    boolean checkAvailable = false;

                    long t1 = System.currentTimeMillis();
                    try
                    {
                        checkAvailable = ProxyUtil.checkAvailable( proxyStr );
                    } catch ( IOException e )
                    {
                        System.out.println(
                                "代理失效:" + proxyStr + ">" + e.toString() );
                    } finally
                    {
                        checkCount++;
                        if ( !checkAvailable )
                        {
                            opsForHash.put( redisHashKey, proxyStr,
                                    INVALID_PROXY_REPLYTIME );
                        } else
                        {
                            System.out.println( "已check数目:" + checkCount + "/"
                                    + size + ", 代理有效:" + proxyStr );
                            opsForHash.put( redisHashKey, proxyStr,
                                    (int)(System.currentTimeMillis() - t1) );
                        }
                    }
                }
            } );
        }
        while ( threadPoolExecutor.getActiveCount() > 0 )
        {
            try
            {
                Thread.sleep( 1000 );
            } catch ( InterruptedException e )
            {
                e.printStackTrace();
            }
        }
        logger.info( "代理检验线程池顺利结束!" );
    }

    /**
     * 将最快的N个代理放入代理池List
     */
    public void saveFatestProxies()
    {
        Long size = opsForList.size( ProxyListService.redisListKey );

        List<String> fatestProxies = getFatestProxies(
                ProxyListService.POOL_SIZE );
        // 先塞进去
        opsForList.rightPushAll( ProxyListService.redisListKey, fatestProxies );

        logger.info( "加入缓存代理池内数目:" + fatestProxies.size() );

        logger.info( "清空缓存代理池内数目:" + size );
        // 再全部清空
        for ( int i = 0; i < size; i++ )
        {
            opsForList.leftPop( ProxyListService.redisListKey );
        }

    }
}
