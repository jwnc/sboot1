
package com.wnc.qqnews;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.PropertyConfigurator;

import com.wnc.basic.BasicFileUtil;
import com.wnc.qqnews.demo.QqConsts;
import com.wnc.qqnews.demo.QqUserTask;
import com.wnc.qqnews.user.UserStat;
import com.wnc.qqnews.user.UserStatFileUtil;
import com.wnc.tools.FileOp;
import com.wnc.wynews.utils.ProxyUtil;

public class QqUserTest
{
    public static void main( String[] args )
            throws IOException, InterruptedException
    {
        // Set<NewsModule> newsModules = QqNewsUtil.getNewsModules();
        // System.out.println( newsModules );
        new ProxyUtil().initProxyPool();
        PropertyConfigurator.configure( "log4j.properties" );
        int id = 20304315;
        id = 337065534;
        UserStat userStat = new UserStat( id ).setPos( 0 );
        userStat = UserStatFileUtil.read( 0 );
        new QqUserTask( userStat ).run();
    }

    public static void uniqueUsers()
    {
        List<String> readFrom = FileOp.readFrom( QqConsts.USERS_TXT );
        Set<String> set = new HashSet<String>( readFrom );
        for ( String s : set )
        {
            BasicFileUtil.writeFileString( QqConsts.USERS_TXT + ".bak",
                    s + "\r\n", null, true );
        }
    }
}
