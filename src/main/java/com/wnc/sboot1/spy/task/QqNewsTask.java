
package com.wnc.sboot1.spy.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wnc.sboot1.spy.qq.QqCmtModuleDaySpy;
import com.wnc.sboot1.spy.qq.QqModuleSpy;

@Component
public class QqNewsTask
{
    @Autowired
    private QqModuleSpy qqModuleSpy;
    @Autowired
    private QqCmtModuleDaySpy qqCmtModuleDaySpy;
    // 同时只能执行一次任务, 上次没执行完, 这次不能执行
    private static volatile boolean flag = false;

    @Scheduled( cron = "${cronJob.fork_qq_news}" )
    public void a()
    {
        try
        {
            if ( flag )
            {
                return;
            }
            flag = true;
            qqModuleSpy.spy();
        } catch ( Exception e )
        {
            e.printStackTrace();
        } finally
        {
            flag = false;
        }
    }

    @Scheduled( cron = "${cronJob.fork_qq_news_yesterday}" )
    public void b()
    {
        try
        {
            flag = true;
            qqCmtModuleDaySpy.spy();
        } catch ( Exception e )
        {
            e.printStackTrace();
        } finally
        {
            flag = false;
        }
    }
}
