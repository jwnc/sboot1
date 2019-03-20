
package com.wnc.sboot1.itbook.task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import db.DBconnectionMgr;
import db.DbExecMgr;

public class ReadTimeAggrTask
{
    /**
     * 写一个历史的, 从11月15开始逐一判断日期, 再写一个当日的只管今天
     * 
     * @param args
     */
    public static void main( String[] args )
    {
        DBconnectionMgr.setJDBCName( "jdbc:sqlite:D:\\itbook\\itdict.db" );
        Map selectAllSqlMap = DbExecMgr.getSelectAllSqlMap(
                "SELECT ID,DEVICE,LOG_TIME FROM itbook_log WHERE LOG_TIME LIKE '2017-11-28%' ORDER BY LOG_TIME ASC" );
        Map fieldMap;
        int sum = 0;
        String time = null;
        String lastTime = null;
        for ( int i = 1; i < selectAllSqlMap.size(); i++ )
        {
            fieldMap = (Map)selectAllSqlMap.get( i );
            time = fieldMap.get( "LOG_TIME" ).toString();
            System.out.println( fieldMap );
            if ( lastTime != null )
            {
                int minutesInterval = secondsInterval( time, lastTime );
                if ( minutesInterval < 10 * 60 )
                {
                    sum += minutesInterval;
                }
            }
            lastTime = time;
            System.out.println( "阅读总计(秒):" + sum );
        }
        System.out.println( getFormatResultTime( sum ) );
    }

    private static String getFormatResultTime( int sum )
    {
        String ret = "%s:%s:%s";
        int hour = 0;
        int min = 0;
        int seconds = 0;
        if ( sum > 3600 )
        {
            hour = sum / 3600;
            min = (sum - hour * 3600) / 60;
            seconds = sum % 60;
        } else if ( sum > 60 )
        {
            min = sum / 60;
            seconds = sum % 60;
        }
        return String.format( ret, align( hour ), align( min ),
                align( seconds ) );
    }

    private static String align( int n )
    {
        return n < 10 ? "0" + n : "" + n;
    }

    /**
     * 计算时间的差值, 单位秒
     * 
     * @param time
     * @param lastTime
     * @return
     */
    private static int secondsInterval( String time, String lastTime )
    {
        DateFormat df = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        try
        {
            Date d1 = df.parse( time );
            Date d2 = df.parse( lastTime );
            long diff = d1.getTime() - d2.getTime();
            return (int)diff / (1000);
        } catch ( Exception e )
        {
        }
        return 0;
    }
}