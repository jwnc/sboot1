
package com.wnc.sboot1.small;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Test;

import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.sboot1.spy.util.SpiderUtils;

public class DateUtil2
{
    @Test
    public void a()
    {

        dayAggre();

        weekAggre();

        monthAggre();

        testLastMonthDay();
        System.out.println( BasicDateUtil.getCurrentDay() );
        System.out.println( BasicDateUtil.getCurrentWeekDay() );
    }

    private void testLastMonthDay()
    {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add( Calendar.MONTH, -1 );
        calendar1.set( Calendar.DAY_OF_MONTH, 1 );
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        String firstDay = sdf.format( calendar1.getTime() );
        // 获取前一个月最后一天
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set( Calendar.DAY_OF_MONTH, 0 );
        String lastDay = sdf.format( calendar2.getTime() );
        System.out.println( firstDay + "  " + lastDay );
    }

    private void monthAggre()
    {
        String lastday = "";
        String mStr = "";
        for ( int i = 1; i < 6; i++ )
        {
            lastday = SpiderUtils.getLastDayOfMonth( BasicNumberUtil
                    .getNumber( BasicDateUtil.getCurrentYearString() ), i );
            mStr = i < 10 ? "0" + i : "" + i;
            System.out.println( "2018-" + mStr + "-01" + "    " + lastday );
        }
    }

    private void weekAggre()
    {
        String monday = "2018-01-01";
        String sunday = "2018-01-07";
        while ( monday.compareTo( "2018-05-21" ) < 0 )
        {

            System.out.println( monday + "  " + sunday );
            monday = SpiderUtils
                    .wrapDayWithLine( BasicDateUtil.getDateBeforeDayDateString(
                            monday.replace( "-", "" ), -7 ) );
            sunday = SpiderUtils
                    .wrapDayWithLine( BasicDateUtil.getDateBeforeDayDateString(
                            sunday.replace( "-", "" ), -7 ) );
        }
    }

    private void dayAggre()
    {
        String day = "2018-01-01";
        while ( day.compareTo( "2018-05-20" ) < 0 )
        {
            System.out.println( day );
            day = SpiderUtils.wrapDayWithLine( BasicDateUtil
                    .getDateBeforeDayDateString( day.replace( "-", "" ), -1 ) );
        }
    }
}
