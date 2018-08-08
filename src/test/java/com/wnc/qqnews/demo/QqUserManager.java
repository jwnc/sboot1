
package com.wnc.qqnews.demo;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.wnc.basic.BasicFileUtil;
import com.wnc.tools.FileOp;

public class QqUserManager
{
    private static final String CERTINFO = "certinfo";
    private static Set<Long> userSet = Collections
            .synchronizedSet( new HashSet<Long>() );
    private static String usersFile = QqConsts.USERS_TXT;
    static
    {
        initUsers();
    }

    private static void initUsers()
    {
        if ( BasicFileUtil.isExistFile( usersFile ) )
        {
            List<String> lines = FileOp.readFrom( usersFile, "UTF-8" );
            for ( String line : lines )
            {
                long uid = JSONObject.parseObject( line )
                        .getLongValue( "userid" );
                userSet.add( uid );
                // System.out.println( uid );
            }
        }
    }

    public static synchronized void addAndWriteUser( JSONObject jsonObject )
    {
        if ( StringUtils.isBlank( jsonObject.getString( "head" ) ) )
        {
            jsonObject.remove( "head" );
        }
        try
        {
            if ( jsonObject.getJSONArray( CERTINFO ).size() == 0 )
            {
                jsonObject.remove( CERTINFO );
            }
        } catch ( Exception e )
        {
            e.printStackTrace();
            try
            {
                if ( jsonObject.getJSONObject( CERTINFO ).size() == 0 )
                {
                    jsonObject.remove( CERTINFO );
                }
            } catch ( Exception e2 )
            {
                e2.printStackTrace();
            }
        }

        long uid = jsonObject.getLongValue( "userid" );
        if ( uid == 0 )
        {
            return;
        }

        if ( userSet.add( uid ) )
        {
            BasicFileUtil.writeFileString( usersFile,
                    jsonObject.toJSONString() + "\r\n", null, true );
        } else
        {
            System.out.println( uid + " 用户已经存在!" );
        }
    }

    public static void main( String[] args )
    {
        initUsers();
    }
}
