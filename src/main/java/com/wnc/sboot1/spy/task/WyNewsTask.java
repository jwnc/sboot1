
package com.wnc.sboot1.spy.task;

import com.wnc.sboot1.spy.wy.WyNewsUpdateSpy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wnc.sboot1.spy.wy.WyNewsDaySpy;
import com.wnc.sboot1.spy.wy.WyNewsSpy;

@Component
public class WyNewsTask
{
    @Autowired
    private WyNewsSpy wyNewsSpy;
    @Autowired
    private WyNewsDaySpy wyNewsDaySpy;
    @Autowired
    private WyNewsUpdateSpy wyNewsDayUpdateSpy;

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

    @Scheduled( cron = "${cronJob.fork_wy_news_update}" )
    public void c()
    {
        try
        {
            flag = true;
            wyNewsDayUpdateSpy.spy();
        } catch ( Exception e )
        {
            e.printStackTrace();
        } finally
        {
            flag = false;
        }
    }
}
