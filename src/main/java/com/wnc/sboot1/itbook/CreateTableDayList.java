package com.wnc.sboot1.itbook;

import com.wnc.basic.BasicDateUtil;

import db.DbExecMgr;

public class CreateTableDayList
{
    public static void main( String[] args )
    {
        try
        {
            String day = "20171110";
            for ( int i = 0; i < 1000; i++ )
            {
                day = dealDay( day, 1 );
                System.out.println( day );
                DbExecMgr
                        .execOnlyOneUpdate( "INSERT INTO DAYLIST(DAY) VALUES('"
                                + day + "')" );
                day = day.replace( "-", "" );
            }
        } catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    private static String dealDay( String day, int i )
    {
        String ret = BasicDateUtil.getDateBeforeDayDateString( day, -i );
        return ret.substring( 0, 4 ) + "-" + ret.substring( 4, 6 ) + "-"
                + ret.substring( 6 );
    }
}