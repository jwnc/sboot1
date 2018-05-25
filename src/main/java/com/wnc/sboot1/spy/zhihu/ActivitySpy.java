
package com.wnc.sboot1.spy.zhihu;

import java.io.IOException;
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
import com.crawl.proxy.entity.Proxy;
import com.crawl.spider.SpiderHttpClient;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicFileUtil;
import com.wnc.sboot1.spy.Spy;
import com.wnc.sboot1.spy.helper.ZhihuActivityHelper;
import com.wnc.sboot1.spy.service.UserVService;
import com.wnc.sboot1.spy.util.ProxyProcess;
import com.wnc.sboot1.spy.zhihu.active.Activity;
import com.wnc.sboot1.spy.zhihu.active.ActivityCronLog;
import com.wnc.sboot1.spy.zhihu.active.TaskErrLog;
import com.wnc.sboot1.spy.zhihu.active.UserV;

@Component
public class ActivitySpy implements Spy
{
    private static final long SIX_HOURS = 1000 * 3600 * 500L;

    private static Logger logger = Logger.getLogger( ActivitySpy.class );

    private long startTime = 0L;

    protected int vCount = 0;

    private int cmtTopicCount = 0;

    // 记录各个用户的最后爬取日期
    private Set<String> spyRefreshTimeRecorder = Collections
            .synchronizedSet( new HashSet() );

    protected List<UserV> userVList;
    private boolean isSuper = true;

    // 线程池获取
    ThreadPoolExecutor netPageThreadPool = SpiderHttpClient.getInstance()
            .getNetPageThreadPool();

    @Autowired
    ProxyProcess proxyProcess;

    @Autowired
    protected ZhihuActivityHelper zhihuActivityHelper;

    @Autowired
    protected UserVService userVService;

    private ActivityCronLog activityCronLog;

    public void spy() throws Exception
    {
        this.isSuper = this.getClass().getName().endsWith( "ActivitySpy" );
        init();
        spyByUsers();

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
        logger.info( "任务结束用时:" + getSpyDuration() + " 完成任务数:" + vCount );

        if ( this.isSuper )
        {
            // 记录任务结束时间
            activityCronLog.setEndTime( new Date() );
            zhihuActivityHelper.cronLog( activityCronLog );
        }
    }

    /**
     * 添加下一页任务,计数
     * 
     * @param apiUrl
     *            地址
     * @param utoken
     *            用户token
     * @param proxyFlag
     *            是否使用代理
     * @param beginSpyDate
     *            任务开始时间
     */
    public synchronized void doJobAndAccum( String apiUrl, UserV userV,
            boolean proxyFlag, Date beginSpyDate )
    {
        vCount++;
        doJob( apiUrl, userV, proxyFlag, beginSpyDate );
    }

    public synchronized void doJob( String apiUrl, UserV userV,
            boolean proxyFlag, Date beginSpyDate )
    {
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
        System.out.println( "当前完成任务数目:" + cmtTopicCount + "/" + vCount );
    }

    /**
     * 记录错误日志
     * 
     * @param utoken
     * @param apiUrl
     * @param msg
     */
    public synchronized void errLog( String utoken, String apiUrl, String msg,
            Proxy proxy )
    {
        TaskErrLog taskErrLog = new TaskErrLog();
        taskErrLog.setMsg( msg );
        taskErrLog.setUrl( apiUrl );
        taskErrLog.setuToken( utoken );
        taskErrLog.setTaskBeginDate( new Date( startTime ) );
        if ( proxy != null )
        {
            taskErrLog.setProxyStr( proxy.getProxyStr() );
        }
        zhihuActivityHelper.errLog( taskErrLog );
    }

    /**
     * 保存记录
     * 
     * @param parseArray
     * @param beginSpyDate
     * @throws Exception
     *             严重异常, 如数据库失去连接, 表结构变更等
     */
    public synchronized void save( List<Activity> parseArray ) throws Exception
    {
        zhihuActivityHelper.save( parseArray );
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
        return cmtTopicCount >= vCount || getSpyDuration() >= SIX_HOURS;
    }

    public long getSpyDuration()
    {
        return System.currentTimeMillis() - startTime;
    }

    protected void spyByUsers()
    {
        userVList = userVService.getUserVList();
        for ( UserV userV : userVList )
        {
            doJobAndAccum(
                    "https://www.zhihu.com/api/v4/members/"
                            + userV.getUserToken() + "/activities",
                    userV, true, null );
        }
    }

    private void init() throws IOException
    {
        spyRefreshTimeRecorder.clear();
        cmtTopicCount = 0;
        vCount = 0;
        startTime = System.currentTimeMillis();

        activityCronLog = new ActivityCronLog();
        if ( this.isSuper )
        {
            activityCronLog.setStartTime( new Date() );
            zhihuActivityHelper.cronLog( activityCronLog );
        }
        // 删除本地ip，防止屏蔽本地ip
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
    }
}
