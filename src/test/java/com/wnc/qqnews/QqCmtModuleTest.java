
package com.wnc.qqnews;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.wnc.qqnews.demo.QqCmtTask;
import com.wnc.qqnews.demo.QqConsts;
import com.wnc.qqnews.demo.QqNewsUtil;
import com.wnc.qqnews.demo.QqSpiderClient;
import com.wnc.tools.FileOp;
import com.wnc.wynews.model.NewsModule;
import com.wnc.wynews.utils.ProxyUtil;

/**
 * 根据模块下的历史新闻进行评论的爬取
 * 
 * @author nengcai.wang
 */
public class QqCmtModuleTest
{

    @Test
    public void d() throws IOException, InterruptedException
    {
        new ProxyUtil().initProxyPool();
        PropertyConfigurator.configure( "log4j.properties" );
        List<String> readFrom = FileOp
                .readFrom( QqConsts.NEWS_LIST_DIR + "要闻.txt" );
        NewsModule newsModule = new NewsModule();
        newsModule.setName( "要闻" );
        for ( String string : readFrom )
        {
            String code = JSONObject.parseObject( string )
                    .getString( "comment_id" );
            System.out.println( code );
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
