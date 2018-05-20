
package com.wnc.sboot1.spy.zhihu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.wnc.basic.BasicDateUtil;
import com.wnc.sboot1.spy.zhihu.active.UserV;

/**
 * 用于从任务失败列表中读取url重新计算, 一般在时间跨度比较大的任务失败后使用
 * 
 * @author wnc
 */
@Component
public class ActivitySpecifySpy extends ActivitySpy
{
    @Override
    protected void spyByUsers()
    {
        Date taskBeginTime = BasicDateUtil.getDateTimeFromString(
                "2018-05-16 18:05:00.000", "yyyy-MM-dd HH:mm:ss.SSS" );
        userVList = specifyUsers();
        for ( UserV userV : userVList )
        {
            doJobAndAccum( userV.getUrl(), userV, true, taskBeginTime );
        }
    }

    private List<UserV> specifyUsers()
    {
        Date dateTimeFromString = BasicDateUtil.getDateTimeFromString(
                "2018-01-01 00:00:00.000", "yyyy-MM-dd HH:mm:ss.SSS" );
        List<UserV> list = new ArrayList<>();
        list.add( new UserV( "jie-shi-bang-53",
                "https://www.zhihu.com/api/v4/members/jie-shi-bang-53/activities?limit=7&after_id=1515051401&desktop=True",
                dateTimeFromString ) );
        return list;

    }

    @Override
    public synchronized void doJob( String apiUrl, UserV userV,
            boolean proxyFlag, Date beginSpyDate )
    {
        netPageThreadPool.execute(
                new VUSerPageTask( apiUrl, userV, true, this, beginSpyDate )
                        .setMaxRetryTimes( Integer.MAX_VALUE ) );
    }
}
