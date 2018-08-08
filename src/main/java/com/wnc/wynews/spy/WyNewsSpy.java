
package com.wnc.wynews.spy;

import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicFileUtil;
import com.wnc.tools.FileOp;
import com.wnc.wynews.consts.ModuleIdsManager;
import com.wnc.wynews.consts.WyConsts;
import com.wnc.wynews.helper.WySpiderClient;
import com.wnc.wynews.model.NewsModule;
import com.wnc.wynews.utils.ProxyUtil;
import com.wnc.wynews.utils.WyNewsUtil;

public class WyNewsSpy
{
    public static void main( String[] args )
            throws IOException, InterruptedException
    {
        new WyNewsSpy().spy();
    }

    public void spy() throws IOException, InterruptedException
    {
        WyNewsUtil.log( "WyNewsSpy任务启动" );
        WySpiderClient.getInstance().counterReset();
        long startTime = System.currentTimeMillis();
        AbstractPageTask.retryMap.put( WyNewsModuleTask.class,
                new ConcurrentHashMap<String, Integer>() );
        AbstractPageTask.retryMap.put( WyCmtTask.class,
                new ConcurrentHashMap<String, Integer>() );

        new ProxyUtil().initProxyPool();

        Set<NewsModule> newsModules = WyNewsUtil.getNewsModules();
        for ( NewsModule newsModule : newsModules )
        {
            String lastTime = getLastTime( newsModule );
            newsModule.setLastSpyDate( lastTime );
            System.out.println( newsModule );
            WySpiderClient.getInstance().submitTask(
                    new WyNewsModuleTask( newsModule, 1, new Date() ) );
        }

        while ( WySpiderClient.getInstance().getNetPageThreadPool()
                .getActiveCount() > 0 )
        {
            Thread.sleep( 10000 );
        }

        ModuleIdsManager.output();
        WyNewsUtil.log( "WyNewsSpy任务成功完成. 完成子任务数:" + WySpiderClient.parseCount
                + "任务耗时:" + (System.currentTimeMillis() - startTime) / 1000
                + "秒." );

    }

    private String getLastTime( NewsModule newsModule )
    {
        String txt = WyConsts.MODULE_HISTORY + newsModule.getName() + ".txt";
        if ( BasicFileUtil.isExistFile( txt ) )
        {
            return FileOp.readFrom( txt ).get( 0 );
        }
        return "2000-01-01 00:00:00";
    }

}
