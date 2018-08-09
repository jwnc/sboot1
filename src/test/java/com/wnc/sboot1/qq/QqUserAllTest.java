
package com.wnc.sboot1.qq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;
import com.wnc.basic.BasicFileUtil;
import com.wnc.qqnews.demo.QqConsts;
import com.wnc.qqnews.demo.QqModuleIdsManager;
import com.wnc.qqnews.demo.QqNewsUtil;
import com.wnc.qqnews.demo.QqSpiderClient;
import com.wnc.qqnews.demo.QqUserTask;
import com.wnc.qqnews.user.UserStat;
import com.wnc.qqnews.user.UserStatFileUtil;
import com.wnc.tools.FileOp;
import com.wnc.wynews.utils.ProxyUtil;

@RunWith( SpringRunner.class )
@SpringBootTest
public class QqUserAllTest
{

    @Test
    public void testWithBoot() throws IOException, InterruptedException
    {

        QqNewsUtil.log( "QqUserSpy任务启动" );
        QqSpiderClient.getInstance().counterReset();
        long startTime = System.currentTimeMillis();

        new ProxyUtil().initProxyPool();

        List<Integer> userIds = getUserIds();
        for ( int i = 320000; i < userIds.size() && i < 600000; i++ )
        {
            UserStat userStat = new UserStat( userIds.get( i ) ).setPos( i );
            int lastSpyTime = UserStatFileUtil.read( i ).getLastSpyTime();
            if ( lastSpyTime == 0 )
            {
                // System.out.println( lastSpyTime );
                QqSpiderClient.getInstance()
                        .submitTask( new QqUserTask( userStat ) );
            }
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

    private List<Integer> getUserIds()
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
