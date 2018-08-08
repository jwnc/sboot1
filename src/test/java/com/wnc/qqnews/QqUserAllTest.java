
package com.wnc.qqnews;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.wnc.basic.BasicFileUtil;
import com.wnc.qqnews.demo.QqConsts;
import com.wnc.qqnews.demo.QqModuleIdsManager;
import com.wnc.qqnews.demo.QqNewsUtil;
import com.wnc.qqnews.demo.QqSpiderClient;
import com.wnc.qqnews.demo.QqUserTask;
import com.wnc.qqnews.user.UserStat;
import com.wnc.tools.FileOp;
import com.wnc.wynews.utils.ProxyUtil;

public class QqUserAllTest
{
    public static void main( String[] args )
            throws IOException, InterruptedException
    {
        QqNewsUtil.log( "QqUserSpy任务启动" );
        QqSpiderClient.getInstance().counterReset();
        long startTime = System.currentTimeMillis();

        new ProxyUtil().initProxyPool();

        List<Integer> userIds = getUserIds();
        for ( int i = 0; i < userIds.size(); i++ )
        {
            UserStat userStat = new UserStat( userIds.get( i ) ).setPos( i );
            QqSpiderClient.getInstance()
                    .submitTask( new QqUserTask( userStat ) );
        }

        while ( QqSpiderClient.getInstance().getNetPageThreadPool()
                .getActiveCount() > 0 )
        {
            Thread.sleep( 10000 );
        }

        QqModuleIdsManager.output();
        QqNewsUtil.log( "QqUserSpy任务成功完成. 完成子任务数:" + QqSpiderClient.parseCount
                + "任务耗时:" + (System.currentTimeMillis() - startTime) / 1000
                + "秒." );
    }

    public static void main2( String[] args )
            throws IOException, InterruptedException
    {
        getUserIds();
    }

    private static List<Integer> getUserIds()
    {
        Set<Integer> userSet = Collections
                .synchronizedSet( new HashSet<Integer>() );
        List<Integer> retByOrder = new ArrayList<>();

        String usersFile = QqConsts.USERS_TXT;
        if ( BasicFileUtil.isExistFile( usersFile ) )
        {
            List<String> lines = FileOp.readFrom( usersFile, "UTF-8" );
            System.out.println( "List:" + lines.size() );
            for ( String line : lines )
            {
                int uid = JSONObject.parseObject( line )
                        .getIntValue( "userid" );
                if ( userSet.add( uid ) )
                {
                    retByOrder.add( uid );
                } else
                {
                    System.out.println( "重复UID:" + uid );
                }
            }
        }
        System.out.println( "List:" + retByOrder.size() );
        return retByOrder;
    }
}
