
package com.wnc.sboot1.small;

import java.util.Date;

import com.wnc.basic.BasicDateUtil;
import com.wnc.sboot1.spy.util.SpiderUtils;

public class DateUtil
{
    public static void main( String[] args )
    {
        int currentWeekDay = BasicDateUtil.getCurrentWeekDay();
        String today = BasicDateUtil.getCurrentDateString();
        String thisMonday = BasicDateUtil.getDateBeforeDayDateString( today,
                currentWeekDay - 1 );
        String lastSunday = BasicDateUtil.getDateBeforeDayDateString( today,
                currentWeekDay );
        String lastMonday = BasicDateUtil
                .getDateBeforeDayDateString( lastSunday, 6 );

        System.out.println( SpiderUtils.getMondayOfWeek( 2018, 18 ) );

        Date dateTimeFromString = BasicDateUtil.getDateTimeFromString(
                "2018-04-30 00:00:00.000", "yyyy-MM-dd HH:mm:ss.SSS" );
        System.out.println( dateTimeFromString );
        System.out.println( dateTimeFromString.getTime() );
        System.out.println( new Date( 1525996741798L ) );

    }
}
