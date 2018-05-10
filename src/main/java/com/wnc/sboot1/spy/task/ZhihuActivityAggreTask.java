
package com.wnc.sboot1.spy.task;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wnc.sboot1.spy.service.ZhihuActivityService;

@Component
public class ZhihuActivityAggreTask
{
    @Autowired
    private ZhihuActivityService zhihuActivityService;
    private static volatile boolean flag = false;

    @Scheduled( cron = "${cronJob.fork_zhihu_activity_aggre}" )
    public void yesterday()
    {
        sleep();
        if ( flag )
        {
            return;
        }
        flag = true;
        try
        {
            zhihuActivityService.aggreYesterday();
        } catch ( Exception e )
        {
            e.printStackTrace();
        } finally
        {
            flag = false;
        }
    }

    @Scheduled( cron = "${cronJob.fork_zhihu_activity_aggre_week}" )
    public void week()
    {
        sleep();
        if ( flag )
        {
            return;
        }
        flag = true;
        try
        {
            zhihuActivityService.aggreLastWeek();
        } catch ( Exception e )
        {
            e.printStackTrace();
        } finally
        {
            flag = false;
        }
    }

    @Scheduled( cron = "${cronJob.fork_zhihu_activity_aggre_month}" )
    public void month()
    {
        sleep();
        if ( flag )
        {
            return;
        }
        flag = true;
        try
        {
            zhihuActivityService.aggreMonth();
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
