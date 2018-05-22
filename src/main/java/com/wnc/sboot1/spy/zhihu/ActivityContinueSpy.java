
package com.wnc.sboot1.spy.zhihu;


import java.util.Date;

import org.springframework.stereotype.Component;

import com.wnc.basic.BasicDateUtil;
import com.wnc.sboot1.spy.zhihu.active.UserV;


@Component
public class ActivityContinueSpy extends ActivitySpy
{
    @Override
    protected void spyByUsers()
    {
        Date taskBeginTime = BasicDateUtil.getDateTimeFromString("2018-05-22 9:30:00.000",
            "yyyy-MM-dd HH:mm:ss.SSS");
        userVList = userVService.getContinueUserVList();
        if (userVList.size() == 0)
        {
            userVList = userVService.getInitUserVList();
        }
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
