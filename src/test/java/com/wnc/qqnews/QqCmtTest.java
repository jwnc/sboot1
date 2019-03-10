
package com.wnc.qqnews;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.wnc.wynews.model.NewsModule;
import com.wnc.wynews.utils.ProxyUtil;

/**
 * 指定模块指定评论id进行爬取
 * 
 * @author nengcai.wang
 */
public class QqCmtTest
{
    private static Logger logger = Logger.getLogger( QqCmtTest.class );

    public static void main( String[] args )
            throws IOException, InterruptedException
    {
        new ProxyUtil().initProxyPool();
        PropertyConfigurator.configure( "log4j.properties" );
        logger.info( "AAAA" );
        NewsModule newsModule = new NewsModule();
        newsModule.setName( "要闻" );
        // 2960170781菲律宾 2957998880当兵
        QqNewsUtil.backupCmtFileBeforeTask( newsModule, "2957998880" );
        new QqCmtTask( newsModule, "2957998880" ).run();
    }
}
