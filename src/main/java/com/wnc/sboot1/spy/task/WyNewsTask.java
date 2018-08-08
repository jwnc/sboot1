
package com.wnc.sboot1.spy.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wnc.wynews.spy.WyNewsDaySpy;
import com.wnc.wynews.spy.WyNewsSpy;

@Component
public class WyNewsTask
{
    private WyNewsSpy wyNewsSpy = new WyNewsSpy();
    private WyNewsDaySpy wyNewsDaySpy = new WyNewsDaySpy();
    // 同时只能执行一次任务, 上次没执行完, 这次不能执行
    private static volatile boolean flag = false;

    @Scheduled( cron = "${cronJob.fork_wy_news}" )
    public void a()
    {
        try
        {
            if ( flag )
            {
                return;
            }
            flag = true;
            wyNewsSpy.spy();
        } catch ( Exception e )
        {
            e.printStackTrace();
        } finally
        {
            flag = false;
        }
    }

    @Scheduled( cron = "${cronJob.fork_wy_news_yesterday}" )
    public void b()
    {
        try
        {
            if ( flag )
            {
                return;
            }
            flag = true;
            wyNewsDaySpy.spy();
        } catch ( Exception e )
        {
            e.printStackTrace();
        } finally
        {
            flag = false;
        }
    }
}
