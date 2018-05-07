package com.wnc.sboot1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wnc.sboot1.common.beans.ResultBean;
import com.wnc.sboot1.itbook.service.BookKpiService;

@RestController
@RequestMapping( "/api" )
@SuppressWarnings( {"rawtypes", "unchecked"} )
public class BookKpiController
{
    @Autowired
    BookKpiService bookKpiService;

    @PostMapping( "booklogkpi/column" )
    public ResultBean<List> column( String dayStart, String dayEnd )
    {
        return new ResultBean<List>( bookKpiService.column( dayStart, dayEnd ) );
    }

    @PostMapping( "booklogkpi/line" )
    public ResultBean<List> line( String dayStart, String dayEnd )
    {
        return new ResultBean<List>( bookKpiService.line( dayStart, dayEnd ) );
    }

    @PostMapping( "booklogkpi/lineWeek" )
    public ResultBean<List> line()
    {
        return new ResultBean<List>( bookKpiService.lineWeek() );
    }

    @PostMapping( "booklogkpi/line_phoneWeek" )
    public ResultBean<List> line_phoneWeek()
    {
        return new ResultBean<List>( bookKpiService.line_phoneWeek() );
    }
}