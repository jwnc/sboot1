
package com.wnc.sboot1.itbook.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.itbooktool.dao.DictionaryDao;
import com.wnc.sboot1.itbook.helper.TimeUtils;

@Component
public class BookKpiService
{

	@PersistenceContext
	private EntityManager entityManager;
	
    public List column( String dayStart, String dayEnd )
    {
        dayStart = dealDayStart( dayStart );
        dayEnd = dealDayEnd( dayEnd );

        List<Map> list = new ArrayList<Map>();

        String sql = "SELECT d.cn_name device, ifnull(t.cnt,0) count FROM DEVICE d left join (select device,count(1) cnt from itbook_log where date_format(log_time, '%Y-%m-%d')>= '"
                + dayStart + "' and date_format(log_time, '%Y-%m-%d')<= '" + dayEnd
                + "' group by device ) t on d.name=t.device";
        // day="2017-11-12", device1=100,***
        System.out.println( "Column:" + sql );
        
        Map selectAllSqlMap = new HashMap<>();
        Query createNativeQuery = entityManager.createNativeQuery(sql);
        List resultList = createNativeQuery.getResultList();
        String[] fieldArr = {"DEVICE", "COUNT"};
        int row = 1;
		for (Object obj : resultList) {
			Object[] arr = (Object[])obj;
			Map<String, String> fieldMap = new HashMap<String, String>();
			for (int i = 0; i < fieldArr.length; i++) {
				fieldMap.put(fieldArr[i], DictionaryDao.getArrStr(arr, i) );
			}
			selectAllSqlMap.put( row, fieldMap );
            row++;
		}
        
        
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
                + "') d) x left join (select device, date_format(log_time, '%Y-%m-%d') day,count(*) cnt from itbook_log  group by device,date_format(log_time, '%Y-%m-%d') )t on x.name=t.device and x.day = t.day order by x.day asc";
        // day="2017-11-12", device1=100,***
        System.out.println( "getLineData:" + sql );
        Map map = new HashMap<>();
        Query createNativeQuery = entityManager.createNativeQuery(sql);
        List resultList = createNativeQuery.getResultList();
        String[] fieldArr = {"NAME", "DAY", "CNT"};
        int row = 1;
		for (Object obj : resultList) {
			Object[] arr = (Object[])obj;
			Map<String, String> fieldMap = new HashMap<String, String>();
			for (int i = 0; i < fieldArr.length; i++) {
				fieldMap.put(fieldArr[i], DictionaryDao.getArrStr(arr, i) );
			}
            map.put( row, fieldMap );
            row++;
		}
        return map;
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