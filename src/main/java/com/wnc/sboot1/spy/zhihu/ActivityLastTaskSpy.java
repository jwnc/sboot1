
package com.wnc.sboot1.spy.zhihu;

import java.util.Date;

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

    @Override
    protected void spyByUsers()
    {
        Date lastTaskTime = zhihuActivityService.findLastTaskTime();
        // taskBeginTime = new Date();
        userVList = userVService.getLastTaskRetryUserVList( lastTaskTime );
        for ( UserV userV : userVList )
        {
            doJobAndAccum( userV.getUrl(), userV, true, lastTaskTime );
        }
    }

    /**
     * 有时候可能是真的无动态， 所以不能设置为无限重试， 暂定为200
     */
    @Override
    public synchronized void doJob( String apiUrl, UserV userV,
            boolean proxyFlag, Date beginSpyDate )
    {
        netPageThreadPool.execute(
                new VUSerPageTask( apiUrl, userV, true, this, beginSpyDate )
                        .setMaxRetryTimes( 200 ) );
    }
}
