
package com.wnc.sboot1.spy.zhihu;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawl.proxy.ProxyPool;
import com.crawl.spider.SpiderHttpClient;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicFileUtil;
import com.wnc.sboot1.spy.Spy;
import com.wnc.sboot1.spy.helper.ZhihuActivityHelper;
import com.wnc.sboot1.spy.service.UserVService;
import com.wnc.sboot1.spy.util.ProxyProcess;
import com.wnc.sboot1.spy.zhihu.active.Activity;
import com.wnc.sboot1.spy.zhihu.active.TaskErrLog;
import com.wnc.sboot1.spy.zhihu.active.UserV;

@Component
public class ActivitySpy implements Spy
{
    private static final long FOUR_HOURS = 1000 * 3600 * 4L;
    private static Logger logger = Logger.getLogger( ActivitySpy.class );
    private long startTime = 0L;
    private int vCount = 0;
    private volatile int cmtTopicCount = 0;

    // 记录各个用户的最后爬取日期
    private Set<String> spyRefreshTimeRecorder = Collections
            .synchronizedSet( new HashSet() );
    List<UserV> userVList;

    // 线程池获取
    ThreadPoolExecutor netPageThreadPool = SpiderHttpClient.getInstance()
            .getNetPageThreadPool();

    @Autowired
    ProxyProcess proxyProcess;
    @Autowired
    private ZhihuActivityHelper zhihuActivityHelper;
    @Autowired
    private UserVService userVService;

    public void spy() throws Exception
    {
        userVList = userVService.getUserVList();
        spyRefreshTimeRecorder.clear();
        cmtTopicCount = 0;
        vCount = 0;
        startTime = System.currentTimeMillis();

        // 防止屏蔽本地ip
        if ( ProxyPool.proxyQueue.size() == 1 )
        {
            ProxyPool.proxyQueue.clear();
        }
        // 初始化代理池
        proxyProcess.init();
        // ProxyPool.proxyQueue.add( new Direct( 1000 ) );

        AbstractPageTask.retryMap.put( VUSerPageTask.class,
                new HashMap<String, Integer>() );
        AbstractPageTask.retryMap.put( GeneralPageTask.class,
                new HashMap<String, Integer>() );
        for ( UserV userV : userVList )
        {
            doJob( "https://www.zhihu.com/api/v4/members/"
                    + userV.getUserToken() + "/activities", userV, true, null );
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
     * @param nextUrl
     * @param utoken
     * @param proxyFlag
     * @param beginSpyDate
     */
    public synchronized void doJob( String apiUrl, UserV userV,
            boolean proxyFlag, Date beginSpyDate )
    {
        vCount++;
        netPageThreadPool.execute(
                new VUSerPageTask( apiUrl, userV, true, this, beginSpyDate ) );
    }

    /**
     * 任务完成之后的回调, 计数
     * 
     * @param type
     * @param msg
     */
    public synchronized void callBackComplete( int type, String msg,
            Runnable task )
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
     * @param beginSpyDate
     */
    public synchronized void save( List<Activity> parseArray )
    {
        try
        {
            zhihuActivityHelper.save( parseArray );
        } catch ( Exception e )
        {
        }

    }

    public void updateLastTime( String userToken, Date beginSpyDate )
    {
        if ( spyRefreshTimeRecorder.add( userToken ) )
        {
            try
            {
                userVService.updateSpyTime( userToken, beginSpyDate );
            } catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 需要严格控制两个计数值
     * 
     * @return
     */
    public boolean isTaskOver()
    {
        return cmtTopicCount >= vCount || getSpyDuration() >= FOUR_HOURS;
    }

    public long getSpyDuration()
    {
        return System.currentTimeMillis() - startTime;
    }
}
