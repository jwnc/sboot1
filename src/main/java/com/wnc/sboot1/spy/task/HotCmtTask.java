
package com.wnc.sboot1.spy.task;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wnc.sboot1.spy.util.SpiderUtils;
import com.wnc.sboot1.spy.zuqiu.FunnyCommetSpy;

@Component
public class HotCmtTask
{
    @Autowired
    private FunnyCommetSpy funnyCommetSpy;
    // 同时只能执行一次任务, 上次没执行完, 这次不能执行
    private static volatile boolean flag = false;

    @Scheduled( cron = "${cronJob.fork_zb8_comment}" )
    public void a()
    {
        try
        {
            if ( flag )
            {
                return;
            }
            flag = true;
            funnyCommetSpy.setSpyDay( SpiderUtils.getDayWithLine() ).spy();
            int hour = Calendar.getInstance().get( Calendar.HOUR_OF_DAY );
            // 同时去找昨天的新闻
            if ( hour < 10 )
            {
                funnyCommetSpy.setSpyDay( SpiderUtils.getYesterDayStr() ).spy();
            }
        } catch ( Exception e )
        {
            e.printStackTrace();
        } finally
        {
            flag = false;
        }
    }

}
