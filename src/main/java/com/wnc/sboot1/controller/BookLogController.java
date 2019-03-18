
package com.wnc.sboot1.controller;

import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wnc.basic.BasicDateUtil;
import com.wnc.sboot1.SpringContextUtils;
import com.wnc.sboot1.common.aop.LoggerManage;
import com.wnc.sboot1.itbook.entity.BookLogCondition;
import com.wnc.sboot1.itbook.entity.BookLogVO;
import com.wnc.sboot1.itbook.helper.BookLogSearch;
import com.wnc.sboot1.itbook.helper.PageDataBean;
import com.wnc.sboot1.itbook.helper.TimeUtils;
import com.wnc.sboot1.itbook.service.ITBookLogService;

@Controller
public class BookLogController
{
    final static Logger logger = Logger.getLogger( BookLogController.class );

    @RequestMapping( "/upload" )
    public String toForm( Model model )
    {
        return "upload";
    }

    @RequestMapping( "/booklog" )
    @LoggerManage( description = "ITBOOK日志列表" )
    public String list( Model model )
    {
        System.out.println( Thread.currentThread().getName() );
        BookLogCondition condition = new BookLogCondition();
        condition.setType( "1" );

        BookLogSearch bookLogSearch = (BookLogSearch)SpringContextUtils.getContext().getBean("BookLogSearch");
        bookLogSearch.setCondition( condition );
        model.addAttribute( "pageData",
                new PageDataBean<BookLogVO>( bookLogSearch.search( 1, 20 ), 1,
                        20, bookLogSearch.getTotalRows() ) );
        model.addAttribute( "condition", condition );
        // int a = 1 / 0;
        return "booklog/booklog";
    }

    @PostMapping( "/booklog" )
    @LoggerManage( description = "搜索ITBOOK日志列表" )
    public String sbl( Model model, BookLogCondition bookLogCondition, int page,
            int size )
    {
    	BookLogSearch bookLogSearch = (BookLogSearch)SpringContextUtils.getContext().getBean("BookLogSearch");
        bookLogSearch.setCondition( bookLogCondition );
        model.addAttribute( "pageData",
                new PageDataBean<BookLogVO>( bookLogSearch.search( page, size ),
                        page, size, bookLogSearch.getTotalRows() ) );
        model.addAttribute( "condition", bookLogCondition );
        return "booklog/booklog";
    }

    @PostMapping( "/booklog/deletelog" )
    @ResponseBody
    @LoggerManage( description = "ITBOOK日志删除" )
    public String deleteLog(
            @RequestParam( value = "id", defaultValue = "0" ) int id )
    {
        try
        {
            ITBookLogService.deleteLog( id );
        } catch ( SQLException e )
        {
            e.printStackTrace();
            return "删除失败!";
        }
        return "删除成功!";
    }

    @RequestMapping( "/kpi/booklog_kpi" )
    /**
     * @param model
     * @param dayStart
     * @param dayEnd
     *            起止时间可同时为空, 此时会去计算最近一周, 也可以起始时间为空, 此时会计算为结束时间前6天.
     * @return
     */
    public String kpiBooklog( Model model, String dayStart, String dayEnd )
    {
        if ( StringUtils.isBlank( dayEnd ) )
        {
            dayEnd = BasicDateUtil.getCurrentDateString();
            dayStart = TimeUtils.dealDay8to10(
                    BasicDateUtil.getDateBeforeDayDateString( dayEnd, 6 ) );
            dayEnd = TimeUtils.dealDay8to10( dayEnd );
        } else if ( StringUtils.isBlank( dayStart ) )
        {
            dayStart = TimeUtils
                    .dealDay8to10( BasicDateUtil.getDateBeforeDayDateString(
                            dayEnd.replace( "-", "" ), 6 ) );
        }
        model.addAttribute( "dayStart", dayStart );
        model.addAttribute( "dayEnd", dayEnd );
        return "/kpi/booklog_kpi";
    }

    @RequestMapping( "/kpi/booklog_week_kpi" )
    /**
     * @param model
     * @param dayStart
     * @param dayEnd
     *            起止时间可同时为空, 此时会去计算最近一周, 也可以起始时间为空, 此时会计算为结束时间前6天.
     * @return
     */
    public String kpiBooklogWeek( Model model, String dayEnd )
    {
        String dayStart = "";
        if ( StringUtils.isBlank( dayEnd ) )
        {
            dayEnd = BasicDateUtil.getCurrentDateString();
            dayEnd = TimeUtils.dealDay8to10( dayEnd );
        } else
        {
            dayStart = TimeUtils
                    .dealDay8to10( BasicDateUtil.getDateBeforeDayDateString(
                            dayEnd.replace( "-", "" ), 6 ) );
        }
        model.addAttribute( "dayStart", dayStart );
        model.addAttribute( "dayEnd", dayEnd );
        return "/kpi/booklog_kpi";
    }

    @RequestMapping( "/kpi/booklog_kpi_phone" )
    public String kpiBookPhonelog( Model model )
    {
        return "/kpi/booklog_kpi_phone";
    }
}