
package com.wnc.sboot1.jpa.zhihu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wnc.basic.BasicDateUtil;
import com.wnc.sboot1.spy.service.ZhihuActivityService;
import com.wnc.sboot1.spy.util.SpiderUtils;

/**
 * 按日期保存统计信息
 * 
 * @author wnc
 */
@RunWith( SpringRunner.class )
@SpringBootTest
public class TargetDescriptionDay
{
    @Autowired
    private ZhihuActivityService zhihuActivityService;

    // @Test
    public void n()
    {
        zhihuActivityService.aggre( "2018-05-22", "2018-05-22",
                ZhihuActivityService.AGGRE_DAY_CODE,
                ZhihuActivityService.FOLLOW_DAY_COUNT );
    }

    /**
     * 6月中下旬数据异常，手动处理
     */
    @Test
    public void a()
    {
        // weekAggre();
        dayAggre( "2018-07-18", "2018-07-28" );
        // weekAggre();
        // monthAggre();
        // yAggre();
    }

    private void monthAggre()
    {
        String lastday = "";
        String mStr = "";
        for ( int i = 1; i < 6; i++ )
        {
            lastday = SpiderUtils.getLastDayOfMonth( 2018, i );
            mStr = i < 10 ? "0" + i : "" + i;
            zhihuActivityService.aggre( "2018-" + mStr + "-01", lastday,
                    ZhihuActivityService.AGGRE_MONTH_CODE,
                    ZhihuActivityService.FOLLOW_MONTH_COUNT );
            System.out.println( "2018-" + mStr + "-01" + "    " + lastday );
        }
    }

    private void yAggre()
    {
        zhihuActivityService.aggreYear();
    }

    private void weekAggre()
    {
        String monday = "2018-06-04";
        String sunday = SpiderUtils.wrapDayWithLine( BasicDateUtil
                .getDateBeforeDayDateString( monday.replace( "-", "" ), -6 ) );
        while ( monday.compareTo( "2018-06-25" ) < 0 )
        {
            zhihuActivityService.aggre( monday, sunday,
                    ZhihuActivityService.AGGRE_WEEK_CODE,
                    ZhihuActivityService.FOLLOW_WEEK_COUNT );
            System.out.println( monday + "  " + sunday );
            monday = SpiderUtils
                    .wrapDayWithLine( BasicDateUtil.getDateBeforeDayDateString(
                            monday.replace( "-", "" ), -7 ) );
            sunday = SpiderUtils
                    .wrapDayWithLine( BasicDateUtil.getDateBeforeDayDateString(
                            sunday.replace( "-", "" ), -7 ) );
        }
    }

    // [startDay, endDay]范围之内的进行操作
    private void dayAggre( String startDay, String endDay )
    {
        String day = startDay;
        while ( day.compareTo( endDay ) < 0 )
        {
            zhihuActivityService.aggre( day, day,
                    ZhihuActivityService.AGGRE_DAY_CODE,
                    ZhihuActivityService.FOLLOW_DAY_COUNT );
            System.out.println( day );
            day = SpiderUtils.wrapDayWithLine( BasicDateUtil
                    .getDateBeforeDayDateString( day.replace( "-", "" ), -1 ) );
        }
    }
}
