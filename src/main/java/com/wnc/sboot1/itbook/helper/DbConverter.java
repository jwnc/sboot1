
package com.wnc.sboot1.itbook.helper;

import java.util.Map;

import com.wnc.basic.BasicNumberUtil;

public class DbConverter
{
    public static String cvtStr( Map fieldMap, String key )
    {
        Object obj = fieldMap.get( key );
        if ( obj == null )
        {
            return null;
        }
        return obj.toString();
    }

    public static int cvtInt( Map fieldMap, String key )
    {
        Object obj = fieldMap.get( key );
        if ( obj == null )
        {
            return 0;
        }
        return BasicNumberUtil.getNumber( String.valueOf( obj ) );
    }

}