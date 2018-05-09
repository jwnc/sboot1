
package com.wnc.sboot1.spy.task;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wnc.sboot1.spy.zhihu.ActivitySpy;

@Component
public class ZhihuActivityTask
{
    @Autowired
    private ActivitySpy activitySpy;
    private static volatile boolean flag = false;

    @Scheduled( cron = "${cronJob.fork_zhihu_activity}" )
    public void a()
    {
        sleep();
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

    private void sleep()
    {
        try
        {
            Thread.sleep( new Random().nextInt( 10000 ) );
        } catch ( InterruptedException e1 )
        {
            e1.printStackTrace();
        }
    }
}
