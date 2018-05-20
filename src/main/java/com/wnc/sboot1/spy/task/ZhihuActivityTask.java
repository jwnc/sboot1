
package com.wnc.sboot1.spy.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wnc.sboot1.spy.zhihu.ActivityRetrySpy;
import com.wnc.sboot1.spy.zhihu.ActivitySpy;

@Component
public class ZhihuActivityTask
{
    @Autowired
    private ActivitySpy activitySpy;
    @Autowired
    private ActivityRetrySpy activityRetrySpy;
    private static volatile boolean flag = false;

    @Scheduled( cron = "${cronJob.fork_zhihu_activity}" )
    public void a()
    {
        new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                if ( flag )
                {
                    return;
                }
                flag = true;
                try
                {
                    activitySpy.spy();
                } catch ( Exception e )
                {
                    e.printStackTrace();
                } finally
                {
                    flag = false;
                }
            }
        } ).start();
    }

    /**
     * 改为线程独立执行, 防止占用quarz线程资源
     */
    @Scheduled( cron = "${cronJob.fork_zhihu_activity2}" )
    public void b()
    {
        new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                if ( flag )
                {
                    return;
                }
                flag = true;
                try
                {
                    activityRetrySpy.spy();
                } catch ( Exception e )
                {
                    e.printStackTrace();
                } finally
                {
                    flag = false;
                }
            }
        } ).start();

    }

}
