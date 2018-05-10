
package com.wnc.sboot1.small;

import com.wnc.basic.BasicDateUtil;
import com.wnc.sboot1.spy.util.SpiderUtils;

public class DateUtil
{
    public static void main( String[] args )
    {
        int currentWeekDay = BasicDateUtil.getCurrentWeekDay();
        String today = BasicDateUtil.getCurrentDateString();
        String lastSunday = BasicDateUtil.getDateBeforeDayDateString( today,
                currentWeekDay );
        String lastMonday = BasicDateUtil
                .getDateBeforeDayDateString( lastSunday, 6 );
        System.out.println( SpiderUtils.wrapDayWithLine( lastMonday ) );

        System.out.println( SpiderUtils.getMondayOfWeek( 2018, 18 ) );

    }
}
