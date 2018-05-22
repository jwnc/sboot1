
package com.wnc.sboot1.small;


import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.wnc.basic.BasicDateUtil;
import com.wnc.sboot1.spy.util.SpiderUtils;
import com.wnc.string.PatternUtil;


public class DateUtil
{
    public static void main(String[] args)
    {
        int currentWeekDay = BasicDateUtil.getCurrentWeekDay();
        String today = BasicDateUtil.getCurrentDateString();
        String thisMonday = BasicDateUtil.getDateBeforeDayDateString(today, currentWeekDay - 1);
        String lastSunday = BasicDateUtil.getDateBeforeDayDateString(today, currentWeekDay);
        String lastMonday = BasicDateUtil.getDateBeforeDayDateString(lastSunday, 6);

        System.out.println(SpiderUtils.getMondayOfWeek(2018, 18));

        Date dateTimeFromString = BasicDateUtil.getDateTimeFromString("2018-05-16 18:05:00.000",
            "yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println(dateTimeFromString);
        System.out.println(dateTimeFromString.getTime());
        System.out.println(new Date(1525996741798L));

        String x = "{\"a\" :\"1\", \"b\" : \"2\"}";
        JSONObject parseObject = JSONObject.parseObject(x);
        System.out.println(parseObject.remove("a"));
        System.out.println(parseObject);
        System.out.println(new Date(1526874982000L));
        String tid = "234A23-2S3+add";
        System.out.println(PatternUtil.getFirstPatternGroup(tid, "(\\S+)\\+"));
    }
}
