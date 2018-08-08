
package com.wnc.wynews.consts;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.wnc.basic.BasicFileUtil;
import com.wnc.tools.FileOp;
import com.wnc.wynews.model.User;

public class UserManager
{
    private static Set<Integer> userSet = Collections
            .synchronizedSet( new HashSet<Integer>() );
    private static String usersFile = WyConsts.USERS_TXT;
    static
    {
        initUsers();
    }

    public static void initUsers()
    {
        if ( BasicFileUtil.isExistFile( usersFile ) )
        {
            List<String> lines = FileOp.readFrom( usersFile, "UTF-8" );
            for ( String line : lines )
            {
                int uid = JSONObject.parseObject( line )
                        .getIntValue( "userId" );
                userSet.add( uid );
                System.out.println( uid );
            }
        }
    }

    public static synchronized void addAndWriteUser( User user )
    {
        if ( user.getIncentiveInfoList() == null
                || user.getIncentiveInfoList().length == 0 )
        {
            user.setIncentiveInfoList( null );
        }
        if ( user.getRedNameInfo() == null
                || user.getRedNameInfo().length == 0 )
        {
            user.setRedNameInfo( null );
        }
        if ( userSet.add( user.getUserId() ) )
        {
            BasicFileUtil.writeFileString( usersFile,
                    JSONObject.toJSONString( user ) + "\r\n", null, true );
        } else
        {
            System.out.println( user.getUserId() + " 已经存在!" );
        }
    }

    public static void main( String[] args )
    {
        initUsers();
    }
}
