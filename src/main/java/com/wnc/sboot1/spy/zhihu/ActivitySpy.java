
package com.wnc.sboot1.spy.zhihu;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawl.proxy.ProxyPool;
import com.crawl.spider.SpiderHttpClient;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicFileUtil;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.sboot1.spy.helper.ZhihuActivityHelper;
import com.wnc.sboot1.spy.util.ProxyProcess;
import com.wnc.sboot1.spy.zhihu.active.Activity;
import com.wnc.sboot1.spy.zhihu.active.TaskErrLog;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

@Component
public class ActivitySpy
{
    private static final long FOUR_HOURS = 1000 * 3600 * 4L;
    private static final long TEN_DAYS = 1000 * 3600 * 24 * 10L;
    private static Logger logger = Logger.getLogger( ActivitySpy.class );
    private long startTime = 0L;
    // 任务成功结束, 就把任务启动时间写入数据库,以便下次赋予LAST_SEEK_TIME
    // 第一次取全量, 下次取增量, 从数据库取
    public static long LAST_SEEK_TIME = 0;
    private int vCount = 0;
    private volatile int cmtTopicCount = 0;
    // 线程池获取
    ThreadPoolExecutor netPageThreadPool = SpiderHttpClient.getInstance()
            .getNetPageThreadPool();

    @Autowired
    private ZhihuActivityHelper zhihuActivityHelper;

    public void spy() throws Exception
    {
        initLastSeekTime();
        cmtTopicCount = 0;
        vCount = 0;
        startTime = System.currentTimeMillis();

        // 防止屏蔽本地ip
        if ( ProxyPool.proxyQueue.size() == 1 )
        {
            ProxyPool.proxyQueue.clear();
        }
        // 初始化代理池
        ProxyProcess.getInstance().init();
        // ProxyPool.proxyQueue.add( new Direct( 1000 ) );

        List<String> readFrom = FileOp.readFrom( "c:/zhihu-user-token.txt" );
        vCount = readFrom.size();

        AbstractPageTask.retryMap.put( VUSerPageTask.class,
                new HashMap<String, Integer>() );
        AbstractPageTask.retryMap.put( GeneralPageTask.class,
                new HashMap<String, Integer>() );
        for ( String utoken : readFrom )
        {
            netPageThreadPool.execute(
                    new VUSerPageTask( "https://www.zhihu.com/api/v4/members/"
                            + utoken + "/activities", utoken, true, this ) );
        }

        while ( true )
        {
            if ( isTaskOver() )
            {
                break;
            }
            Thread.sleep( 3000 );
        }
        BasicFileUtil.writeFileString( "c:/zhihu-task.log", "开始于:" + startTime
                + " 结束于:" + System.currentTimeMillis() + "\r\n", null, true );
        logger.info( "任务结束用时:" + getSpyDuration() );

    }

    /**
     * 添加下一页任务,计数
     * 
     * @param nextUrl
     * @param utoken
     * @param proxyFlag
     */
    public synchronized void nextJob( String nextUrl, String utoken,
            boolean proxyFlag )
    {
        vCount++;
        netPageThreadPool.execute(
                new VUSerPageTask( nextUrl, utoken, proxyFlag, this ) );
    }

    /**
     * 任务完成之后的回调, 计数
     * 
     * @param type
     * @param msg
     */
    public synchronized void callBackComplete( int type, String msg )
    {
        cmtTopicCount++;
        logger.info( "当前完成任务数目:" + cmtTopicCount + "/" + vCount );
    }

    /**
     * 记录错误日志
     * 
     * @param utoken
     * @param apiUrl
     * @param msg
     */
    public synchronized void errLog( String utoken, String apiUrl, String msg )
    {
        TaskErrLog taskErrLog = new TaskErrLog();
        taskErrLog.setMsg( msg );
        taskErrLog.setUrl( apiUrl );
        taskErrLog.setuToken( utoken );

        zhihuActivityHelper.errLog( taskErrLog );
    }

    /**
     * 保存记录
     * 
     * @param parseArray
     */
    public synchronized void save( List<Activity> parseArray )
    {
        zhihuActivityHelper.save( parseArray );
    }

    /**
     * 需要严格控制两个计数值
     * 
     * @return
     */
    private boolean isTaskOver()
    {
        return cmtTopicCount >= vCount || getSpyDuration() >= FOUR_HOURS;
    }

    /**
     * 获得上次任务执行时间
     */
    private void initLastSeekTime()
    {
        try
        {
            List<String> readFrom = FileOp.readFrom( "c:/zhihu-task.log" );
            if ( readFrom.size() > 0 )
            {
                String string = readFrom.get( readFrom.size() - 1 );
                LAST_SEEK_TIME = BasicNumberUtil.getLongNumber(
                        PatternUtil.getFirstPattern( string, "\\d+" ) );
                logger.info( "Last Seek Time:" + LAST_SEEK_TIME );
            }
        } catch ( Exception e )
        {
            e.printStackTrace();
        }

        if ( LAST_SEEK_TIME < System.currentTimeMillis() - TEN_DAYS )
        {
            LAST_SEEK_TIME = System.currentTimeMillis() - TEN_DAYS;
        }
    }

    private long getSpyDuration()
    {
        return System.currentTimeMillis() - startTime;
    }
}
