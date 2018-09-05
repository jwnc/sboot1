
package com.wnc.sboot1.spy.wy;

import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicFileUtil;
import com.wnc.tools.FileOp;
import com.wnc.wynews.consts.ModuleIdsManager;
import com.wnc.wynews.consts.WyConsts;
import com.wnc.wynews.helper.WySpiderClient;
import com.wnc.wynews.model.NewsModule;
import com.wnc.wynews.spy.WyCmtTask;
import com.wnc.wynews.spy.WyNewsModuleTask;
import com.wnc.wynews.spy.WyNewsModuleUpdateTask;
import com.wnc.wynews.utils.ProxyUtil;
import com.wnc.wynews.utils.WyNewsUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WyNewsUpdateSpy
{
    public static void main( String[] args )
            throws IOException, InterruptedException
    {
        new WyNewsUpdateSpy().spy();
    }

    public void spy() throws IOException, InterruptedException
    {
        WyNewsUtil.log( "WyNewsUpdateSpy任务启动" );
        WySpiderClient.getInstance().counterReset();
        long startTime = System.currentTimeMillis();
        AbstractPageTask.retryMap.put( WyNewsModuleUpdateTask.class,
                new ConcurrentHashMap<String, Integer>() );
        AbstractPageTask.retryMap.put( WyNewsModuleTask.class,
                new ConcurrentHashMap<String, Integer>() );
        AbstractPageTask.retryMap.put( WyCmtTask.class,
                new ConcurrentHashMap<String, Integer>() );

        new ProxyUtil().initProxyPool();

        Set<NewsModule> newsModules = WyNewsUtil.getNewsModules();
        for ( NewsModule newsModule : newsModules )
        {
            WySpiderClient.getInstance().submitTask(
                    new WyNewsModuleUpdateTask( newsModule, 1 ) );
        }

        while ( WySpiderClient.getInstance().getNetPageThreadPool()
                .getActiveCount() > 0 )
        {
            Thread.sleep( 10000 );
        }

        ModuleIdsManager.output();
        WyNewsUtil.log( "WyNewsUpdateSpy任务成功完成. 完成子任务数:" + WySpiderClient.parseCount
                + "任务耗时:" + (System.currentTimeMillis() - startTime) / 1000
                + "秒." );

    }
}
