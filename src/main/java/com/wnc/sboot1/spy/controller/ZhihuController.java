
package com.wnc.sboot1.spy.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wnc.sboot1.spy.service.ZhihuActivityService;
import com.wnc.sboot1.spy.util.SpiderUtils;

@Controller
@RequestMapping( "/spyapi/zhihu" )
public class ZhihuController
{
    @Autowired
    private ZhihuActivityService zhihuActivityService;
    private static Logger logger = Logger.getLogger( ZhihuController.class );

    @GetMapping( "aggre" )
    public String aggre( Model model, String dateStr )
    {
        if ( dateStr == null || !dateStr.matches( "\\d{4}\\-\\d{2}\\-\\d{2}" ) )
        {
            dateStr = SpiderUtils.getYesterDayStr();
        }
        try
        {
            List aggreData = zhihuActivityService.getAggreData( dateStr );
            model.addAttribute( "dateStr", dateStr );
            model.addAttribute( "data", aggreData );
        } catch ( Exception e )
        {
            e.printStackTrace();
            logger.error( e );
        }
        return "zhihu/aggre";
    }
}
