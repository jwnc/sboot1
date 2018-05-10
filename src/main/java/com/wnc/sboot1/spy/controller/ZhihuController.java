
package com.wnc.sboot1.spy.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wnc.basic.BasicNumberUtil;
import com.wnc.sboot1.spy.service.ZhihuActivityService;
import com.wnc.sboot1.spy.util.SpiderUtils;
import com.wnc.string.PatternUtil;

@Controller
@RequestMapping( "/spyapi/zhihu" )
public class ZhihuController
{
    @Autowired
    private ZhihuActivityService zhihuActivityService;
    private static Logger logger = Logger.getLogger( ZhihuController.class );

    @GetMapping( "aggre" )
    public String aggre( Model model, String dateStr, Integer aggreCode )
    {
        if ( aggreCode == null )
        {
            aggreCode = ZhihuActivityService.AGGRE_DAY_CODE;
        }
        if ( dateStr == null )
        {
            dateStr = SpiderUtils.getYesterDayStr();
            aggreCode = ZhihuActivityService.AGGRE_DAY_CODE;
        }
        String dateStrCopy = dateStr;
        if ( dateStr.matches( "\\d{4}\\-W\\d{2}" ) )
        {
            dateStr = SpiderUtils.getMondayOfWeek(
                    BasicNumberUtil.getNumber(
                            PatternUtil.getFirstPattern( dateStr, "\\d+" ) ),
                    BasicNumberUtil.getNumber(
                            PatternUtil.getLastPattern( dateStr, "\\d+" ) ) );
            aggreCode = ZhihuActivityService.AGGRE_WEEK_CODE;
        }
        if ( dateStr.matches( "\\d{4}\\-\\d{2}" ) )
        {
            dateStr = dateStr + "-01";
            aggreCode = ZhihuActivityService.AGGRE_MONTH_CODE;
        }

        try
        {
            List aggreData = zhihuActivityService.getAggreData( dateStr,
                    aggreCode );
            model.addAttribute( "dateStr", dateStrCopy );
            model.addAttribute( "aggreCode", aggreCode );
            model.addAttribute( "data", aggreData );
        } catch ( Exception e )
        {
            e.printStackTrace();
            logger.error( e );
        }
        return "zhihu/aggre";
    }
}
