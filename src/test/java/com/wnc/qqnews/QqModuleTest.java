
package com.wnc.qqnews;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.wnc.qqnews.demo.QqModuleIdsManager;
import com.wnc.qqnews.demo.QqNewsModuleTask;
import com.wnc.qqnews.demo.QqNewsUtil;
import com.wnc.qqnews.demo.QqSpiderClient;
import com.wnc.wynews.model.NewsModule;
import com.wnc.wynews.utils.ProxyUtil;

/**
 * 实时爬取所有模块下的新闻
 * 
 * @author nengcai.wang
 */
public class QqModuleTest
{
    private static Logger logger = Logger.getLogger( QqModuleTest.class );

    @Test
    public void d() throws IOException, InterruptedException
    {
        QqNewsUtil.log( "QqNewsSpy任务启动" );
        QqSpiderClient.getInstance().counterReset();
        long startTime = System.currentTimeMillis();

        new ProxyUtil().initProxyPool();

        for ( NewsModule newsModule : QqNewsUtil.getNewsModules() )
        {
            QqSpiderClient.getInstance()
                    .submitTask( new QqNewsModuleTask( newsModule ) );
        }

        while ( QqSpiderClient.getInstance().getNetPageThreadPool()
                .getActiveCount() > 0 )
        {
            Thread.sleep( 10000 );
        }

        QqModuleIdsManager.output();
        QqNewsUtil.log( "QqNewsSpy任务成功完成. 完成子任务数:" + QqSpiderClient.parseCount
                + "任务耗时:" + (System.currentTimeMillis() - startTime) / 1000
                + "秒." );
    }

}
