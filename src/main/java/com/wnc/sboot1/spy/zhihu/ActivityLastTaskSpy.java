
package com.wnc.sboot1.spy.zhihu;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wnc.sboot1.spy.service.ZhihuActivityService;
import com.wnc.sboot1.spy.zhihu.active.UserV;

/**
 * 找出上次的errTask，然后进行下载。 该方法可以具体到某一个批次
 * 
 * @author nengcai.wang
 */
@Component
public class ActivityLastTaskSpy extends ActivitySpy
{
    @Autowired
    private ZhihuActivityService zhihuActivityService;
    private static Logger logger = Logger
            .getLogger( ActivityLastTaskSpy.class );

    @Override
    protected void spyByUsers()
    {
        this.MAX_EXECUTE_TIME = this.MAX_EXECUTE_TIME / 2;
        Date lastTaskTime = zhihuActivityService.findLastTaskTime();
        // taskBeginTime = new Date();
        userVList = userVService.getLastTaskRetryUserVList( lastTaskTime );
        logger.info( "重试用户数:" + userVList.size() );
        for ( UserV userV : userVList )
        {
            doJobAndAccum( userV.getUrl(), userV, true, lastTaskTime );
        }
    }

    /**
     * 有时候可能是真的无动态， 所以不能设置为无限重试， 暂时控制时间为MAX_EXECUTE_TIME的一半
     */
    @Override
    public synchronized void doJob( String apiUrl, UserV userV,
            boolean proxyFlag, Date beginSpyDate )
    {
        netPageThreadPool.execute(
                new VUSerPageTask( apiUrl, userV, true, this, beginSpyDate )
                        .setMaxRetryTimes( Integer.MAX_VALUE ) );
    }
}
