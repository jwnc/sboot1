
package com.wnc.sboot1.itbook.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.sboot1.itbook.helper.TimeUtils;

import db.DbExecMgr;

@Component
public class BookKpiService
{

    public List column( String dayStart, String dayEnd )
    {
        dayStart = dealDayStart( dayStart );
        dayEnd = dealDayEnd( dayEnd );

        List<Map> list = new ArrayList<Map>();

        String sql = "SELECT d.cn_name device, ifnull(t.cnt,0) count FROM DEVICE d left join (select device,count(1) cnt from itbook_log where substr(log_time,0,11)>= '"
                + dayStart + "' and substr(log_time,0,11)<= '" + dayEnd
                + "' group by device ) t on d.name=t.device";
        // day="2017-11-12", device1=100,***
        System.out.println( "Column:" + sql );
        Map selectAllSqlMap = DbExecMgr.getSelectAllSqlMap( sql );
        Map rowMap = null;
        for ( int i = 1; i <= selectAllSqlMap.size(); i++ )
        {
            rowMap = (Map)selectAllSqlMap.get( i );
            rowMap.put( "COUNT", BasicNumberUtil
                    .getNumber( rowMap.get( "COUNT" ).toString() ) );
            list.add( rowMap );
        }
        return list;
    }

    public List line( String dayStart, String dayEnd )
    {
        dayStart = dealDayStart( dayStart );
        dayEnd = dealDayEnd( dayEnd );

        List<Map> list = new ArrayList<Map>();

        Map selectAllSqlMap = getLineData( dayStart, dayEnd );
        Map daydataMap = null;
        Map rowMap = null;
        String lastDay = null;
        for ( int i = 1; i <= selectAllSqlMap.size(); i++ )
        {
            rowMap = (Map)selectAllSqlMap.get( i );
            if ( !rowMap.get( "DAY" ).toString().equals( lastDay ) )
            {
                daydataMap = new HashMap<>();
                lastDay = rowMap.get( "DAY" ).toString();
                daydataMap.put( "date", lastDay );
                daydataMap.put( "day", BasicDateUtil
                        .getGBWeekString( lastDay.replace( "-", "" ) ) );
                list.add( daydataMap );
            }
            daydataMap.put( rowMap.get( "NAME" ),
                    rowMap.get( "CNT" ) == null ? 0
                            : BasicNumberUtil.getNumber(
                                    rowMap.get( "CNT" ).toString() ) );
        }
        return list;
    }

    public List line_phone( String dayStart, String dayEnd )
    {
        dayStart = dealDayStart( dayStart );
        dayEnd = dealDayEnd( dayEnd );

        List<Map> list = new ArrayList<Map>();

        Map selectAllSqlMap = getLineData( dayStart, dayEnd );
        Map rowMap = null;
        for ( int i = 1; i <= selectAllSqlMap.size(); i++ )
        {
            rowMap = (Map)selectAllSqlMap.get( i );
            rowMap.put( "CNT", BasicNumberUtil
                    .getNumber( rowMap.get( "CNT" ).toString() ) );
            list.add( rowMap );
        }
        return list;
    }

    private Map getLineData( String dayStart, String dayEnd )
    {
        String sql = "select x.cn_name name, x.day day,ifnull(t.cnt,0) cnt from (select * from device join (select day from daylist where day >= '"
                + dayStart + "' and day <= '" + dayEnd
                + "')) x left join (select device, substr(log_time,0,11) day,count(*) cnt from itbook_log  group by device,substr(log_time,0,11) )t on x.name=t.device and x.day = t.day order by x.day asc";
        // day="2017-11-12", device1=100,***
        System.out.println( "getLineData:" + sql );
        Map selectAllSqlMap = DbExecMgr.getSelectAllSqlMap( sql );
        return selectAllSqlMap;
    }

    private String dealDayEnd( String dayEnd )
    {
        if ( StringUtils.isBlank( dayEnd ) )
        {
            dayEnd = BasicDateUtil.getCurrentDateTimeString().substring( 0,
                    10 );
        }
        dayEnd = TimeUtils.dealDay8to10( dayEnd );
        return dayEnd;
    }

    private String dealDayStart( String dayStart )
    {
        if ( StringUtils.isBlank( dayStart ) )
        {
            dayStart = "2017-11-15";
        }
        dayStart = TimeUtils.dealDay8to10( dayStart );
        return dayStart;
    }

    public List lineWeek()
    {
        String today = BasicDateUtil.getCurrentDateString();
        String weekBeforeToday = BasicDateUtil
                .getDateBeforeDayDateString( today, 6 );
        return line( weekBeforeToday, today );
    }

    public List lineWeek4Mail()
    {
        String today = BasicDateUtil.getCurrentDateString();
        if ( BasicDateUtil.getCurrentHour() <= 8 )
        {
            today = BasicDateUtil.getDateBeforeDayDateString( today, 1 );
        }
        String weekBeforeToday = BasicDateUtil
                .getDateBeforeDayDateString( today, 6 );
        return line( weekBeforeToday, today );
    }

    public List line_phoneWeek()
    {
        String today = BasicDateUtil.getCurrentDateString();
        String weekBeforeToday = BasicDateUtil
                .getDateBeforeDayDateString( today, 6 );
        return line_phone( weekBeforeToday, today );
    }
}