
package com.wnc.sboot1.spy.task;

import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wnc.basic.BasicDateUtil;
import com.wnc.sboot1.spy.service.ZhihuActivityService;

@Component
public class ZhihuActivityAggreTask
{
    @Autowired
    private ZhihuActivityService zhihuActivityService;
    private static volatile boolean flag = false;
    private static Logger logger = Logger
            .getLogger( ZhihuActivityAggreTask.class );

    abstract class AbstractCronTask
    {
        public void start()
        {
            sleep();
            if ( flag )
            {
                return;
            }
            flag = true;
            try
            {
                task();
                logger.info( "任务成功结束!" );
            } catch ( Exception e )
            {
                e.printStackTrace();
            } finally
            {
                flag = false;
            }
        }

        protected abstract void task();

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

    @Scheduled( cron = "${cronJob.fork_zhihu_activity_aggre_today}" )
    public void today()
    {
        new AbstractCronTask()
        {

            @Override
            protected void task()
            {
                zhihuActivityService.aggreToday();
            }
        }.start();
    }

    @Scheduled( cron = "${cronJob.fork_zhihu_activity_aggre_yesterday}" )
    public void yesterday()
    {
        new AbstractCronTask()
        {

            @Override
            protected void task()
            {
                zhihuActivityService.aggreYesterday();
            }
        }.start();
    }

    @Scheduled( cron = "${cronJob.fork_zhihu_activity_aggre_week_now}" )
    public void week()
    {
        new AbstractCronTask()
        {

            @Override
            protected void task()
            {
                // 周三之前需要同步上星期数据
                if ( BasicDateUtil.getCurrentWeekDay() < 4 )
                {
                    zhihuActivityService.aggreLastWeek();
                }
                zhihuActivityService.aggreThisWeek();
            }
        }.start();
    }

    @Scheduled( cron = "${cronJob.fork_zhihu_activity_aggre_month}" )
    public void month()
    {
        new AbstractCronTask()
        {

            @Override
            protected void task()
            {
                // 如果是本月前三天, 同步上月数据
                if ( BasicDateUtil.getCurrentDay() < 4 )
                {
                    zhihuActivityService.aggreLastMonth();
                }
                zhihuActivityService.aggreMonth();
            }
        }.start();
    }

    @Scheduled( cron = "${cronJob.fork_zhihu_activity_aggre_year}" )
    public void year()
    {
        new AbstractCronTask()
        {
            @Override
            protected void task()
            {
                if ( BasicDateUtil.getCurrentMonth() == 1
                        && BasicDateUtil.getCurrentDay() < 4 )
                {
                    zhihuActivityService.aggreLastYear();
                }
                zhihuActivityService.aggreYear();
            }
        }.start();
    }

}
