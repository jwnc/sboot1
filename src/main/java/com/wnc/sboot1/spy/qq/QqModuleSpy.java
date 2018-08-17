
package com.wnc.sboot1.spy.qq;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.wnc.qqnews.QqModuleIdsManager;
import com.wnc.qqnews.QqNewsModuleTask;
import com.wnc.qqnews.QqNewsUtil;
import com.wnc.qqnews.QqSpiderClient;
import com.wnc.wynews.model.NewsModule;
import com.wnc.wynews.utils.ProxyUtil;

/**
 * 实时爬取所有模块下的新闻
 * 
 * @author nengcai.wang
 */
@Component
public class QqModuleSpy
{

    public void spy() throws IOException, InterruptedException
    {
        QqNewsUtil.log( "QqModuleSpy任务启动" );
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
