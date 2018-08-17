
package com.wnc.sboot1.spy.qq;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.wnc.qqnews.QqCmtTask;
import com.wnc.qqnews.QqConsts;
import com.wnc.qqnews.QqNewsUtil;
import com.wnc.qqnews.QqSpiderClient;
import com.wnc.sboot1.spy.util.SpiderUtils;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;
import com.wnc.wynews.model.NewsModule;
import com.wnc.wynews.utils.ProxyUtil;

/**
 * 根据模块下的历史新闻进行评论的爬取
 * 
 * @author nengcai.wang
 */
@Component
public class QqCmtModuleDaySpy
{
    public void spy() throws IOException, InterruptedException
    {
        QqNewsUtil.log( "QqCmtModuleDaySpy任务启动" );
        QqSpiderClient.getInstance().counterReset();
        long startTime = System.currentTimeMillis();

        new ProxyUtil().initProxyPool();

        String yesterday = SpiderUtils.getYesterDayStr();
        List<String> readFrom = FileOp
                .readFrom( QqConsts.NEWS_DAY_DIR + yesterday + ".txt" );
        for ( String string : readFrom )
        {
            String code = PatternUtil.getFirstPatternGroup( string, "\\d+" );

            String moduleName = PatternUtil.getFirstPatternGroup( string,
                    "\\[(.*?)\\]" );
            NewsModule newsModule = new NewsModule( moduleName, new Date() );

            QqNewsUtil.backupCmtFileBeforeTask( newsModule, code );
            QqSpiderClient.getInstance()
                    .submitTask( new QqCmtTask( newsModule, code ) );
        }

        while ( QqSpiderClient.getInstance().getNetPageThreadPool()
                .getActiveCount() > 0 )
        {
            Thread.sleep( 10000 );
        }
        QqNewsUtil.log( "QqCmtModuleDaySpy任务成功完成. 完成子任务数:"
                + QqSpiderClient.parseCount + "任务耗时:"
                + (System.currentTimeMillis() - startTime) / 1000 + "秒." );

    }

}
