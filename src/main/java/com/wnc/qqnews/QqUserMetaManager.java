
package com.wnc.qqnews;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.wnc.basic.BasicFileUtil;
import com.wnc.tools.FileOp;

public class QqUserMetaManager
{
    private static final String CERTINFO = "certinfo";
    private static Set<Long> userSet = Collections
            .synchronizedSet( new HashSet<Long>() );
    private static String usermetaFile = QqConsts.USERMETA_TXT;
    static
    {
        initUserMetas();
    }

    public static void initUserMetas()
    {
        if ( BasicFileUtil.isExistFile( usermetaFile ) )
        {
            List<String> lines = FileOp.readFrom( usermetaFile, "UTF-8" );
            for ( String line : lines )
            {
                long uid = JSONObject.parseObject( line )
                        .getLongValue( "userid" );
                userSet.add( uid );
                // System.out.println( uid );
            }
        }
    }

    public static synchronized void addAndWriteUserMeta( JSONObject jsonObject )
    {

        long uid = jsonObject.getLongValue( "userid" );
        if ( uid == 0 )
        {
            return;
        }

        if ( userSet.add( uid ) )
        {
            if ( jsonObject.containsKey( CERTINFO ) )
            {
                if ( StringUtils.isBlank( jsonObject.getJSONObject( CERTINFO )
                        .getString( "certnick" ) ) )
                {
                    jsonObject.remove( CERTINFO );
                }
            }
            BasicFileUtil.writeFileString( usermetaFile,
                    jsonObject.toJSONString() + "\r\n", null, true );
        } else
        {
            System.out.println( uid + " 用户已经存在!" );
        }
    }

}
