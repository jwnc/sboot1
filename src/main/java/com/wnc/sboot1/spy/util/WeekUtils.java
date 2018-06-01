
package com.wnc.sboot1.spy.util;

import com.wnc.basic.BasicDateUtil;

public class WeekUtils
{

    public static int yearTotalDays( int year )
    {
        return (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) ? 366
                : 365;
    }

    // 获取当前年的最大周数
    public static int getMaxWeekNumOfYear( int year )
    {
        int totalDays = yearTotalDays( year );
        String _1 = year + "0101";
        int weekDayFromDateString = BasicDateUtil
                .getWeekDayFromDateString( _1 );
        int addWeek = weekDayFromDateString > 4 ? 0 : 1; // 如果元旦是星期四以后，则落到这一周的这几天都删除，addWeek为0，
                                                         // 否则addWeek为1
        totalDays = totalDays - (8 - weekDayFromDateString);// 前几天参与addWeek之后，剩下多少天
        return addWeek
                + ((totalDays % 7 >= 4) ? totalDays / 7 + 1 : totalDays / 7);// 如果模7后剩余的天数是4天及以上，则进1，
                                                                             // 否则去尾

    }

    public static void main( String[] args )
    {
        test1();
    }

    private static void test1()
    {
        for ( int y = 2000; y < 2030; y++ )
        {
            int maxWeekNumOfYear = getMaxWeekNumOfYear( y );
            System.out.println( y + " " + maxWeekNumOfYear );
        }
    }

}
