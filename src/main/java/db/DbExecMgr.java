
package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.wnc.basic.BasicStringUtil;
import com.wnc.sboot1.itbook.MyAppParams;

public class DbExecMgr
{
    static Connection con = DBconnectionMgr.getConnection();

    static
    {
        DbExecMgr.refreshCon( MyAppParams.getInstance().getSqliteName() );
        // DbExecMgr.refreshCon( "jdbc:sqlite:D:\\itbook\\itdict.db" );
    }

    public static void refreshCon( String source )
    {
        DBconnectionMgr.setJDBCName( source );
        con = DBconnectionMgr.getConnection();
    }

    public static Map getSelectSqlMap( String sql )
    {

        Statement statement = null;

        Map map = new HashMap<Integer, String>();
        if ( BasicStringUtil.isNullString( sql ) )
        {
            return map;
        }
        try
        {
            statement = con.createStatement();
            ResultSet set = statement.executeQuery( sql );
            ResultSetMetaData resultSetMetaData = set.getMetaData();
            while ( set.next() )
            {
                for ( int i3 = 1; i3 < resultSetMetaData.getColumnCount() + 1; ++i3 )
                {
                    map.put( resultSetMetaData.getColumnName( i3 )
                            .toUpperCase(), set.getString( i3 ) );
                }
            }
        } catch ( SQLException e1 )
        {
            System.out.println( e1.getMessage() );
            // System.out.println(e1.getStackTrace()[10].getFileName());
            e1.printStackTrace();
        } finally
        {
            try
            {
                statement.close();
            } catch ( SQLException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return map;
    }

    /**
     * 获取数据的map形式,key为int型从1开始
     * 
     * @param sql
     * @return
     */
    public static Map getSelectAllSqlMap( String sql )
    {
        Statement statement = null;

        Map<Integer, Map> map = new HashMap<Integer, Map>();
        if ( BasicStringUtil.isNullString( sql ) )
        {
            return map;
        }
        try
        {
            statement = con.createStatement();
            ResultSet set = statement.executeQuery( sql );
            ResultSetMetaData resultSetMetaData = set.getMetaData();

            int i = 1;
            while ( set.next() )
            {
                Map<String, String> fieldMap = new HashMap<String, String>();
                for ( int i3 = 1; i3 < resultSetMetaData.getColumnCount() + 1; ++i3 )
                {
                    fieldMap.put( resultSetMetaData.getColumnName( i3 )
                            .toUpperCase(), set.getString( i3 ) );
                }
                map.put( i, fieldMap );
                i++;
            }
            statement.close();
        } catch ( SQLException e1 )
        {
            System.out.println( e1.getMessage() );
            // System.out.println(e1.getStackTrace()[10].getFileName());
            e1.printStackTrace();
        } finally
        {
            try
            {
                statement.close();
            } catch ( SQLException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return map;
    }

    public static synchronized int execUpdate( Connection con, String sql )
            throws SQLException
    {
        Statement statement = null;
        int i = 0;
        if ( BasicStringUtil.isNullString( sql ) )
        {
            return 0;
        }
        statement = con.createStatement();
        i = statement.executeUpdate( sql );
        statement.close();
        return i;

    }

    public static boolean execOnlyOneUpdate( Connection con, String sql )
            throws SQLException
    {
        if ( execUpdate( con, sql ) == 1 )
        {
            return true;
        }
        return false;
    }

    public static boolean execOnlyOneUpdate( String sql ) throws SQLException
    {
        if ( execUpdate( con, sql ) == 1 )
        {
            return true;
        }
        return false;
    }

    public static boolean isExistData( String sql )
    {
        Map selectSqlMap = getSelectSqlMap( sql );
        if ( selectSqlMap.size() > 0 )
        {
            return true;
        }
        return false;
    }
}