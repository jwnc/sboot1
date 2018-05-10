
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
    public String aggre( Model model, String dateStr, String weekStr,
            String monthStr, String yearStr, Integer aggreCode )
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
                searchDay = dateStr;
                break;
            case 2:
                searchDay = SpiderUtils.getMondayOfWeek(
                        BasicNumberUtil.getNumber( PatternUtil
                                .getFirstPattern( weekStr, "\\d+" ) ),
                        BasicNumberUtil.getNumber( PatternUtil
                                .getLastPattern( weekStr, "\\d+" ) ) );
                break;
            case 3:
                searchDay = monthStr + "-01";
                break;
            case 4:
                searchDay = yearStr + "-01-01";
                break;
        }

        try
        {
            List aggreData = zhihuActivityService.getAggreData( searchDay,
                    aggreCode );

            model.addAttribute( "dateStr", dateStr );
            model.addAttribute( "weekStr", weekStr );
            model.addAttribute( "monthStr", monthStr );
            model.addAttribute( "yearStr", yearStr );
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
