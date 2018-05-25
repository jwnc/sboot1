
package com.wnc.sboot1.spy.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wnc.sboot1.spy.zhihu.ActivityLastTaskSpy;
import com.wnc.sboot1.spy.zhihu.ActivitySpy;

@Component
public class ZhihuActivityTask
{
    @Autowired
    private ActivitySpy activitySpy;

    @Autowired
    private ActivityLastTaskSpy activityLastTaskSpy;
    // @Autowired
    // private ActivityContinueSpy activityContinueSpy;

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
                    activityLastTaskSpy.spy();
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
