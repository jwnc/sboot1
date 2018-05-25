
package com.wnc.sboot1.spy.zhihu;


import java.util.Date;

import org.springframework.stereotype.Component;

import com.wnc.basic.BasicDateUtil;
import com.wnc.sboot1.spy.zhihu.active.UserV;


/**
 * 初始化完成的时候，无限重试机会从最小created_time开始下载。
 * 
 * @author nengcai.wang
 */
@Component
public class ActivityContinueSpy extends ActivitySpy
{
    @Override
    protected void spyByUsers()
    {
        Date taskBeginTime = BasicDateUtil.getDateTimeFromString("2018-05-22 9:30:00.000",
            "yyyy-MM-dd HH:mm:ss.SSS");
        // taskBeginTime = new Date();
        userVList = userVService.getContinueUserVList();
        for (UserV userV : userVList)
        {
            doJobAndAccum(userV.getUrl(), userV, true, taskBeginTime);
        }
    }

    @Override
    public synchronized void doJob(String apiUrl, UserV userV, boolean proxyFlag,
                                   Date beginSpyDate)
    {
        netPageThreadPool.execute(
            new VUSerPageTask(apiUrl, userV, true, this, beginSpyDate).setMaxRetryTimes(
                Integer.MAX_VALUE));
    }
}
