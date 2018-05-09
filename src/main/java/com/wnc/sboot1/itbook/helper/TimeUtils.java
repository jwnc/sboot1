
package com.wnc.sboot1.itbook.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.wnc.basic.BasicStringUtil;

public class TimeUtils
{
    public static String dealDay8to10( String day )
    {
        if ( day.matches( "\\d+{8}" ) )
        {
            return day.substring( 0, 4 ) + "-" + day.substring( 4, 6 ) + "-"
                    + day.substring( 6 );
        }
        return day;
    }

    public static String cvtToBjTime( String gistDate )
    {
        gistDate = gistDate.replace( "T", " " ).replace( "Z", "" );
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTime( getDateFromGistTime( gistDate ) );

        localCalendar.add( Calendar.HOUR, 8 );
        Date localDate = localCalendar.getTime();

        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss" );
        String format = localSimpleDateFormat.format( localDate );
        return format;
    }

    private static Date getDateFromGistTime( String paramString )
    {
        Date localDate = null;
        try
        {
            if ( !BasicStringUtil.isNullString( paramString ) )
            {
                SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss" );
                localDate = localSimpleDateFormat.parse( paramString );
            }
        } catch ( Exception localException )
        {
            localException.printStackTrace();
        }
        return localDate;
    }
}