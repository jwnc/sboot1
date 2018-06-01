
package com.wnc.sboot1.spy.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.sboot1.spy.service.ZhihuActivityService;
import com.wnc.sboot1.spy.util.SpiderUtils;
import com.wnc.sboot1.spy.util.WeekUtils;
import com.wnc.string.PatternUtil;

@Controller
@RequestMapping( "/spyapi/zhihu" )
public class ZhihuController
{
    @Autowired
    private ZhihuActivityService zhihuActivityService;
    private static Logger logger = Logger.getLogger( ZhihuController.class );

    /**
     * @param request
     * @param model
     * @param dateStr
     * @param weekStr
     * @param yearMonthStr
     * @param yearStr
     * @param aggreCode
     * @param action
     *            -1代表前一段， 1代表后一段
     * @return
     */
    @GetMapping( "aggre" )
    public String aggre( Model model, String dateStr, String weekStr,
            String yearMonthStr, String yearStr, Integer aggreCode,
            Integer action )
    {
        String searchDay = "";
        if ( dateStr == null )
        {
            dateStr = SpiderUtils.getYesterDayStr();
        }
        if ( aggreCode == null )
        {
            aggreCode = ZhihuActivityService.AGGRE_DAY_CODE;
        }

        switch ( aggreCode )
        {
            case 1:
                dateStr = getDateStr( dateStr, action );
                searchDay = dateStr;
                break;
            case 2:
                weekStr = getWeekStr( weekStr, action );
                searchDay = SpiderUtils.getMondayOfWeek(
                        BasicNumberUtil.getNumber( PatternUtil
                                .getFirstPattern( weekStr, "\\d+" ) ),
                        BasicNumberUtil.getNumber( PatternUtil
                                .getLastPattern( weekStr, "\\d+" ) ) );
                break;
            case 3:
                yearMonthStr = getYearMonthStr( yearMonthStr, action );
                searchDay = yearMonthStr + "-01";
                break;
            case 4:
                yearStr = getYearStr( yearStr, action );
                searchDay = yearStr + "-01-01";
                break;
        }

        try
        {
            List aggreData = zhihuActivityService.getAggreData( searchDay,
                    aggreCode );

            model.addAttribute( "dateStr", dateStr );
            model.addAttribute( "weekStr", weekStr );
            model.addAttribute( "yearMonthStr", yearMonthStr );
            model.addAttribute( "yearStr", yearStr );
            model.addAttribute( "aggreCode", aggreCode );
            model.addAttribute( "data", aggreData );
            model.addAttribute( "action", action );
        } catch ( Exception e )
        {
            e.printStackTrace();
            logger.error( e );
        }
        return "zhihu/aggre";
    }

    private String getYearStr( String yearStr, Integer action )
    {
        if ( action != null )
        {
            yearStr = "" + (BasicNumberUtil.getNumber( yearStr ) + action);
        }
        return yearStr;
    }

    private String getYearMonthStr( String yearMonthStr, Integer action )
    {
        if ( action == null )
        {
            return yearMonthStr;
        }
        int year = BasicNumberUtil.getNumber(
                PatternUtil.getFirstPattern( yearMonthStr, "\\d+" ) );
        int month = BasicNumberUtil.getNumber(
                PatternUtil.getLastPattern( yearMonthStr, "\\d+" ) );
        if ( action == -1 )
        {
            if ( month == 1 )
            {
                year--;
                month = 12;
            } else
            {
                month--;
            }
        } else if ( action == 1 )
        {
            if ( month == 12 )
            {
                year++;
                month = 1;
            } else
            {
                month++;
            }
        }
        return year + "-" + (month < 10 ? "0" + month : month);
    }

    private String getWeekStr( String weekStr, Integer action )
    {
        if ( action == null )
        {
            return weekStr;
        }
        int year = BasicNumberUtil
                .getNumber( PatternUtil.getFirstPattern( weekStr, "\\d+" ) );
        int week = BasicNumberUtil
                .getNumber( PatternUtil.getLastPattern( weekStr, "\\d+" ) );
        if ( action == -1 )
        {
            if ( week == 1 )
            {
                year--;
                week = WeekUtils.getMaxWeekNumOfYear( year );
            } else
            {
                week--;
            }
        } else if ( action == 1 )
        {
            if ( week == WeekUtils.getMaxWeekNumOfYear( year ) )
            {
                year++;
                week = 1;
            } else
            {
                week++;
            }
        }
        return year + "-W" + (week < 10 ? "0" + week : week);
    }

    private String getDateStr( String dateStr, Integer action )
    {
        String ret = dateStr;
        if ( action == null )
        {
            return ret;
        }
        switch ( action )
        {
            case -1:
            case 1:
                ret = SpiderUtils.wrapDayWithLine(
                        BasicDateUtil.getDateBeforeDayDateString(
                                dateStr.replace( "-", "" ), -action ) );
                break;
            default:
                break;
        }
        return ret;
    }
}
