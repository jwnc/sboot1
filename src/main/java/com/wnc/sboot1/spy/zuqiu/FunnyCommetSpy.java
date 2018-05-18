
package com.wnc.sboot1.spy.zuqiu;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.crawl.spider.task.AbstractPageTask;
import com.wnc.sboot1.spy.Spy;
import com.wnc.sboot1.spy.helper.FunnyCmtHelper;
import com.wnc.sboot1.spy.util.ProxyProcess;

@Component
public class FunnyCommetSpy implements Spy
{
    private int cmtTopicCount = 0;
    private int taskCount = 0;
    private long startTime = 0L;

    private int retryFaildCount = 0;
    private int status404Count = 0;

    @Value( "${spy.cmt_fork_rate}" )
    private double rate;

    private Logger logger = Logger.getLogger( FunnyCommetSpy.class );
    @Autowired
    ProxyProcess proxyProcess;
    @Autowired
    FunnyCmtHelper funnyCmtHelper;

    private static ThreadPoolExecutor pageThreadPool = (ThreadPoolExecutor)Executors
            .newFixedThreadPool( 8 );

    private String spyDay;

    public FunnyCommetSpy setSpyDay( String spyDay )
    {
        this.spyDay = spyDay;
        return this;
    }

    /**
     * 外部任务借口
     * 
     * @param day
     * @return
     * @throws Exception
     */
    public void spy() throws Exception
    {
        if ( this.spyDay == null )
        {
            throw new NullPointerException();
        }

        proxyProcess.init();
        reset();

        getFunnyNBA();
        getFunnyZuqiu();

        while ( true )
        {
            logger.info( getMonitorLog() );

            if ( isTaskOver() )
            {
                break;
            }
            Thread.sleep( 10000 );
        }
        logger.info(
                "任务结束用时:" + getSpyDuration() + " 抓取新闻总数:" + cmtTopicCount );

    }

    public ThreadPoolExecutor getPageThreadExecutor()
    {
        return this.pageThreadPool;
    }

    /**
     * 内部爬虫任务接口 - 完成任务之后的回调
     * 
     * @param type
     * @param msg
     * @param news
     */
    public synchronized void callBackComplete( int type, String msg,
            Runnable task )
    {

        cmtTopicCount++;
        switch ( type )
        {
            case AbstractPageTask.COMPLETE_STATUS_SUCCESS:
                // 会有大量重复的记录出现
                // funnyCmtHelper
                // .saveCompleteLog( ((FunnyCmtTask)task).getZb8News() );
                break;
            case AbstractPageTask.COMPLETE_STATUS_FAIL_RETRY_OUT:
                retryFaildCount++;
                break;
            case AbstractPageTask.COMPLETE_STATUS_FAIL_404:
                status404Count++;
                break;
        }

        System.out.println( "当前完成任务数目:" + cmtTopicCount );
    }

    /**
     * 任务全部结束或者时间超时, 最多rate秒一个
     * 
     * @param size
     * @param startTime
     * @return
     */
    public boolean isTaskOver()
    {
        if ( rate <= 0 )
        {
            rate = 1;
        }
        return cmtTopicCount >= taskCount || getSpyDuration() > Math
                .max( rate * taskCount * 1000, 300 * 1000 );
    }

    /**
     * 获取spy持续进行的时间
     * 
     * @param startTime
     * @return
     */
    public long getSpyDuration()
    {
        return System.currentTimeMillis() - startTime;
    }

    public synchronized void save( Zb8News news,
            List<HotComment> parseCommentList )
    {
        funnyCmtHelper.singleNews( news, parseCommentList );
    }

    /**
     * 拼接当前监控日志
     * 
     * @return
     */
    private String getMonitorLog()
    {
        StringBuilder accum = new StringBuilder( "" );
        accum.append( "当前进度:" + cmtTopicCount + "/" + taskCount );
        accum.append( " 重试失败:" + retryFaildCount );
        accum.append( " 重试中:"
                + (AbstractPageTask.retryMap.get( FunnyCmtTask.class ).size()
                        - retryFaildCount) );
        accum.append( " 404请求数:" + status404Count );
        accum.append( " 耗时秒数:" + getSpyDuration() / 1000 );
        return accum.toString();
    }

    private void getFunnyNBA() throws Exception
    {
        getFunnyComments( Zb8Const.NBA, this.spyDay );
    }

    private void getFunnyZuqiu() throws Exception
    {
        getFunnyComments( Zb8Const.ZUQIU, this.spyDay );
    }

    /**
     * @param day
     *            格式2018-04-16
     * @return
     * @throws IOException
     */
    private void getFunnyComments( String catelog, String day ) throws Exception
    {
        Set<String> set = new HashSet<String>();
        List<Zb8News> list = new Zb8NewsService().getNews( catelog, day );
        for ( Zb8News news : list )
        {
            if ( set.add( news.getPinglun() ) )
            {
                taskCount++;
                pageThreadPool.execute( new FunnyCmtTask( this, news ) );
            }
        }

    }

    /**
     * 重置各个类变量
     */
    private void reset()
    {
        startTime = System.currentTimeMillis();
        cmtTopicCount = 0;
        taskCount = 0;
        retryFaildCount = 0;
        status404Count = 0;
        if ( AbstractPageTask.retryMap.get( FunnyCmtTask.class ) != null )
        {
            AbstractPageTask.retryMap.get( FunnyCmtTask.class ).clear();
        } else
        {
            AbstractPageTask.retryMap.put( FunnyCmtTask.class,
                    new ConcurrentHashMap<String, Integer>() );
        }
    }

}
