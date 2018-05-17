
package com.wnc.sboot1.spy.zhihu;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.wnc.basic.BasicDateUtil;
import com.wnc.sboot1.spy.zhihu.active.UserV;

/**
 * 用于从任务失败列表中读取url重新计算, 一般在时间跨度比较大的任务失败后使用
 * 
 * @author wnc
 */
@Component
public class ActivityRetrySpy extends ActivitySpy
{
    @Override
    protected void spyByUsers()
    {
        Date taskBeginTime = BasicDateUtil.getDateTimeFromString(
                "2018-05-16 18:05:00.000", "yyyy-MM-dd HH:mm:ss.SSS" );
        userVList = userVService.getRetryUserVList();
        for ( UserV userV : userVList )
        {
            doJob( userV.getUrl(), userV, true, taskBeginTime );
        }
    }
}
