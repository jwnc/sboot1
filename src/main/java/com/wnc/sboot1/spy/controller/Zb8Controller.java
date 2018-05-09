
package com.wnc.sboot1.spy.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wnc.sboot1.itbook.helper.PageDataBean;
import com.wnc.sboot1.spy.service.HotCommentService;
import com.wnc.sboot1.spy.util.SpiderUtils;
import com.wnc.sboot1.spy.zuqiu.HotComment;
import com.wnc.sboot1.spy.zuqiu.Zb8News;

@Controller
@RequestMapping( "/spyapi/zb8" )
public class Zb8Controller
{
    @Autowired
    HotCommentService hotCommentService;
    private final int size = 30;

    /**
     * @param page
     * @param newsId
     * @param day
     *            评论日期 格式为2018-04-01
     * @param orderNews
     *            是否按新闻时间顺序排列(1:是, 0:否)
     * @return
     */
    @GetMapping( "comments" )
    public String comments( Model model, Integer page, String newsId,
            String day, Integer orderNews )
    {
        try
        {
            if ( StringUtils.isBlank( day ) )
            {
                day = SpiderUtils.getDayWithLine();
                page = 1;
                orderNews = 1;
            }
            model.addAttribute( "newsId", newsId );
            model.addAttribute( "day", day );
            model.addAttribute( "orderNews", orderNews );
            Page<HotComment> pagination = null;
            if ( StringUtils.isNotBlank( day ) )
            {
                pagination = hotCommentService.paginationByDay( page, size, day,
                        orderNews );
            } else if ( StringUtils.isNotBlank( newsId ) )
            {
                pagination = hotCommentService.paginationByNid( page, size,
                        newsId );
            }
            if ( pagination != null )
            {
                model.addAttribute( "pageData", new PageDataBean<HotComment>(
                        removeDuplicate( pagination ),
                        pagination.getNumber() + 1, pagination.getSize(),
                        (int)pagination.getTotalElements() ) );
            }

        } catch ( Exception e )
        {
            e.printStackTrace();
        }
        return "zb8/comments";
    }

    private List<HotComment> removeDuplicate( Page<HotComment> pagination )
    {
        List<HotComment> content = pagination.getContent();
        Zb8News lastNews = null;
        for ( HotComment hotComment : content )
        {
            Zb8News zb8News = hotComment.getZb8News();
            if ( lastNews != null
                    && zb8News.getPinglun().equals( lastNews.getPinglun() ) )
            {
                hotComment.setZb8News( null );
            } else
            {
                lastNews = zb8News;
                zb8News.setDescribe( "first" );
            }
        }
        return content;
    }

}
