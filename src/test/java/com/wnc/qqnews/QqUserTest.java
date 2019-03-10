
package com.wnc.qqnews;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.wnc.basic.BasicFileUtil;
import com.wnc.qqnews.user.UserStat;
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
        int id = 20304315;
        id = 337065534;
        UserStat userStat = new UserStat( id ).setPos( 0 );
        // userStat = UserStatFileUtil.read( 0 );
        new QqUserTask( userStat, false ).run();
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
