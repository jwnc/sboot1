
package com.wnc.sboot1.spy.helper;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wnc.sboot1.spy.rep.HotCommentRepository;
import com.wnc.sboot1.spy.rep.TaskCompleteLogRepository;
import com.wnc.sboot1.spy.rep.Zb8NewsRepository;
import com.wnc.sboot1.spy.zuqiu.HotComment;
import com.wnc.sboot1.spy.zuqiu.TaskCompleteLog;
import com.wnc.sboot1.spy.zuqiu.Zb8News;

@Component
public class FunnyCmtHelper
{
    private static Logger logger = Logger.getLogger( FunnyCmtHelper.class );
    @Autowired
    private Zb8NewsRepository zb8NewsRepository;
    @Autowired
    private HotCommentRepository hotCommentRepository;
    @Autowired
    private TaskCompleteLogRepository taskCompleteLogRepository;

    // @Transactional( propagation = Propagation.REQUIRES_NEW )
    public void singleNews( Zb8News zb8News, List<HotComment> list )
    {
        try
        {
            zb8NewsRepository.save( zb8News );
            for ( HotComment hotComment : list )
            {
                hotCommentRepository.save( hotComment );
            }
        } catch ( Exception e )
        {
            System.err.println( zb8News.getTitle() );
            e.printStackTrace();
        }
    }

    public void saveCompleteLog( Zb8News zb8News )
    {
        try
        {
            TaskCompleteLog taskCompleteLog;
            taskCompleteLog = new TaskCompleteLog();
            taskCompleteLog.setTitle( zb8News.getTitle() );
            taskCompleteLog
                    .setUrl( "https://news.zhibo8.cc" + zb8News.getUrl() );
            taskCompleteLog.setSite( "zb8" );
            taskCompleteLog.setModule( "news" );
            taskCompleteLogRepository.save( taskCompleteLog );
        } catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}
