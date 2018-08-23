
package com.wnc.dmm.task;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSONObject;
import com.crawl.core.httpclient.Constants;
import com.crawl.core.util.HttpClientUtil;
import com.crawl.proxy.entity.Direct;
import com.crawl.proxy.entity.Proxy;
import com.crawl.proxy.util.ProxyUtil;
import com.crawl.spider.SpiderHttpClient;
import com.crawl.spider.entity.Page;
import com.wnc.basic.BasicStringUtil;
import com.wnc.dmm.ProxyPool;

/**
 * page task 下载网页并解析，具体解析由子类实现 若使用代理，从ProxyPool中取 ##### 使用自定义的日本ProxyPool
 * 
 * @see ProxyPool
 */
public abstract class AbstractPageTask implements Runnable
{
    // 任务执行中的各种状态码
    public static final int COMPLETE_STATUS_SUCCESS = 0;
    public static final int COMPLETE_STATUS_FAIL_RETRY_OUT = 1;
    public static final int COMPLETE_STATUS_FAIL_404 = 2;
    public static final int COMPLETE_STATUS_FAIL_CANT_RETRY = 3;
    // 线程安全Map, 使用移除随机数后的url作为key
    public static Map<Class, Map<String, Integer>> retryMap = new ConcurrentHashMap<Class, Map<String, Integer>>();

    protected String url;
    protected HttpRequestBase request;
    protected boolean proxyFlag;// 是否通过代理下载
    protected Proxy currentProxy;// 当前线程使用的代理
    protected String pageEncoding = "UTF-8";
    protected SpiderHttpClient spiderHttpClient = SpiderHttpClient
            .getInstance();

    // 如果flag为flase, 则一次重试机会都没有
    protected boolean retryFlag = true;
    protected int MAX_RETRY_TIMES = 10;

    private static final String IO_EXCEPTION = "IOException";
    private static Logger logger = Logger.getLogger( AbstractPageTask.class );

    public AbstractPageTask()
    {

    }

    public AbstractPageTask( String url,boolean proxyFlag )
    {
        this.url = url;
        this.proxyFlag = proxyFlag;
    }

    public AbstractPageTask( String url,boolean proxyFlag,String pageEncoding )
    {
        this.url = url;
        this.proxyFlag = proxyFlag;
        this.pageEncoding = pageEncoding;
    }

    public AbstractPageTask( HttpRequestBase request,boolean proxyFlag )
    {
        this.request = request;
        this.proxyFlag = proxyFlag;
    }

    public void run()
    {
        long requestStartTime = 0l;
        HttpGet tempRequest = null;
        boolean connectProxy = true;
        try
        {
            // Thread.sleep(1000);
            Page page = null;
            if ( url != null )
            {
                if ( proxyFlag )
                {
                    tempRequest = new HttpGet( url );
                    currentProxy = ProxyPool.proxyQueue.take();
                    if ( !(currentProxy instanceof Direct) )
                    {
                        HttpHost proxy = new HttpHost( currentProxy.getIp(),
                                currentProxy.getPort() );
                        tempRequest.setConfig(
                                HttpClientUtil.getRequestConfigBuilder()
                                        .setProxy( proxy ).build() );
                    }
                    requestStartTime = System.currentTimeMillis();
                    page = spiderHttpClient.getWebPage( tempRequest,
                            pageEncoding );
                } else
                {
                    requestStartTime = System.currentTimeMillis();
                    page = spiderHttpClient.getWebPage( url, pageEncoding );
                }
            } else if ( request != null )
            {
                if ( proxyFlag )
                {
                    currentProxy = ProxyPool.proxyQueue.take();
                    if ( !(currentProxy instanceof Direct) )
                    {
                        HttpHost proxy = new HttpHost( currentProxy.getIp(),
                                currentProxy.getPort() );
                        request.setConfig(
                                HttpClientUtil.getRequestConfigBuilder()
                                        .setProxy( proxy ).build() );
                    }
                    requestStartTime = System.currentTimeMillis();
                    page = spiderHttpClient.getWebPage( request );
                } else
                {
                    requestStartTime = System.currentTimeMillis();
                    page = spiderHttpClient.getWebPage( request );
                }
            }
            long requestEndTime = System.currentTimeMillis();
            page.setProxy( currentProxy );
            int status = page.getStatusCode();
            String logStr = Thread.currentThread().getName() + " "
                    + page.getUrl() + " " + currentProxy + " executing request "
                    + " response statusCode:" + status + " request cost time:"
                    + (requestEndTime - requestStartTime) + "ms";
            if ( status == HttpStatus.SC_OK )
            {
                // if (page.getHtml().contains("douban")) {
                logger.debug( logStr );
                if ( currentProxy != null )
                {
                    currentProxy.setSuccessfulTimes(
                            currentProxy.getSuccessfulTimes() + 1 );
                    currentProxy.setSuccessfulTotalTime(
                            currentProxy.getSuccessfulTotalTime()
                                    + (requestEndTime - requestStartTime) );
                    double aTime = (currentProxy.getSuccessfulTotalTime() + 0.0)
                            / currentProxy.getSuccessfulTimes();
                    currentProxy.setSuccessfulAverageTime( aTime );
                    currentProxy.setLastSuccessfulTime(
                            System.currentTimeMillis() );
                }

                handle( page );
                complete( COMPLETE_STATUS_SUCCESS, "" );
            }
            /**
             * 401--不能通过验证
             */
            else if ( status == 404 )
            {
                logger.debug( logStr );
                errLog404( page );
                complete( COMPLETE_STATUS_FAIL_404, "404 no need retry!" );
            } else
            {
                logger.debug( logStr );
                errLogxxx( page );
                judgeToRetry( " pageStatus=" + status );
            }
        } catch ( InterruptedException e )
        {
            logger.error( "InterruptedException", e );
        } catch ( Exception e )
        {
            errLogExp( e );
            if ( e instanceof IOException )
            {
                if ( currentProxy != null )
                {
                    // e.getMessage() 在ClientProtocolException时可能为空,改用toString()
                    if ( e.toString().contains( currentProxy.getProxyStr() ) )
                    {
                        connectProxy = false;
                        logger.info( "丢弃当前代理" + currentProxy.getProxyStr() );
                    } else
                    {
                        currentProxy.setFailureTimes(
                                currentProxy.getFailureTimes() + 1 );
                    }
                }
                judgeToRetry( IO_EXCEPTION + "/"
                        + BasicStringUtil.subString( e.toString(), 0, 200 ) );
            } else
            {
                judgeToRetry(
                        BasicStringUtil.subString( e.toString(), 0, 200 ) );
            }
        } finally
        {
            if ( request != null )
            {
                request.releaseConnection();
            }
            if ( tempRequest != null )
            {
                tempRequest.releaseConnection();
            }
            if ( currentProxy != null && connectProxy
                    && !ProxyUtil.isDiscardProxy( currentProxy ) )
            {
                currentProxy.setTimeInterval( Constants.TIME_INTERVAL );
                ProxyPool.proxyQueue.put( currentProxy );
            }
        }
    }

    private void judgeToRetry( String msg )
    {
        boolean shutdown = spiderHttpClient.getNetPageThreadPool().isShutdown();
        if ( retryFlag && !shutdown )
        {
            retryMonitor( msg );
        } else
        {
            complete( COMPLETE_STATUS_FAIL_CANT_RETRY, msg + " 无法重试. retryFlag="
                    + retryFlag + " pool shutdown:" + shutdown );
        }
    }

    /**
     * 记录404,401,410状态码错误
     */
    protected void errLog404( Page page )
    {

    }

    /**
     * 记录其他状态码错误
     */
    protected void errLogxxx( Page page )
    {

    }

    /**
     * 记录异常日志
     */
    protected void errLogExp( Exception ex )
    {

    }

    /**
     * retry
     */
    protected abstract void retry();

    /**
     * 重试管理
     * 
     * @param msg
     */
    protected void retryMonitor( String msg )
    {
        Map<String, Integer> map = retryMap.get( this.getClass() );
        String u = removeRandom();
        if ( map.containsKey( u ) )
        {
            Integer times = map.get( u );
            if ( times >= MAX_RETRY_TIMES )
            {
                complete( COMPLETE_STATUS_FAIL_RETRY_OUT, msg );
                return;
            } else
            {
                if ( !isIOException( msg ) )
                {
                    map.put( u, times + 1 );
                }
            }
        } else
        {
            // 如果是IO异常, 则什么都不做, 相当于多一次重试次数
            if ( !isIOException( msg ) )
            {
                map.put( u, 1 );
            }
        }
        logger.debug(
                "准备重试第" + map.get( u ) + "次:" + url + " 重试原因:[" + msg + "]" );
        retry();
    }

    /**
     * key=0.1234123412341234的情况删掉, key中不包括?和&
     * 
     * @return
     */
    protected String removeRandom()
    {
        return url.replaceAll( "(\\?|\\&)[^\\&\\?]+=0\\.\\d{16}", "" );
    }

    protected void complete( int type, String msg )
    {
        if ( type == COMPLETE_STATUS_FAIL_RETRY_OUT )
        {
            logger.error( "重试(" + MAX_RETRY_TIMES + ")次数用完,仍然失败!" + url
                    + " 失败原因:" + msg );
        } else
        {
            if ( type != COMPLETE_STATUS_SUCCESS )
            {
                logger.error( url + " 失败原因:" + msg );
            }

            if ( retryMap.get( this.getClass() ).containsKey( removeRandom() ) )
            {
                retryMap.get( this.getClass() ).remove( removeRandom() );
                logger.debug( "重试成功, 移除" + removeRandom() );
            }
        }
    };

    /**
     * 子类实现page的处理
     * 
     * @param page
     */
    protected abstract void handle( Page page ) throws Exception;

    public Document getDoc( Page page )
    {
        return Jsoup.parse( page.getHtml() );
    }

    public JSONObject getJson( Page page )
    {
        return JSONObject.parseObject( page.getHtml() );
    }

    /**
     * 是否是IO异常信息
     * 
     * @param msg
     * @return
     */
    private boolean isIOException( String msg )
    {
        return msg.startsWith( IO_EXCEPTION );
    };
}
