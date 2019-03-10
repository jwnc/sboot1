
package com.wnc.qqnews;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;

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
public class QqCmtModuleDayTest
{

    @SuppressWarnings( "unused" )
    @Test
    public void d() throws IOException, InterruptedException
    {
        new ProxyUtil().initProxyPool();
        PropertyConfigurator.configure( "log4j.properties" );
        String yesterday = SpiderUtils.getYesterDayStr();
        List<String> readFrom = FileOp
                .readFrom( QqConsts.NEWS_DAY_DIR + yesterday + ".txt" );
        for ( String string : readFrom )
        {
            String code = PatternUtil.getFirstPatternGroup( string, "\\d+" );
            System.out.println( code );

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
    }

}
