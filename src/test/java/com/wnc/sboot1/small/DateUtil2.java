
package com.wnc.sboot1.small;

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
    }

    private void testLastMonthDay()
    {
        String day = "20180501";
        String lastDay = BasicDateUtil.getDateBeforeDayDateString( day, 1 );
        String yearOfLastDay = lastDay.substring( 0, 4 );
        String monOfLastDay = lastDay.substring( 4, 6 );

        String lastDayOfMonth = SpiderUtils.getLastDayOfMonth(
                BasicNumberUtil.getNumber( yearOfLastDay ),
                BasicNumberUtil.getNumber( monOfLastDay ) );

        System.out.println( lastDayOfMonth );
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
