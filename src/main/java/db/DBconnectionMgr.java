
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.wnc.basic.BasicStringUtil;

public final class DBconnectionMgr
{
    static final Logger _$1 = Logger.getLogger( DBconnectionMgr.class );

    private static Map<String, Vector> _$vectors = new HashMap<String, Vector>();
    private static Vector _$vector;
    private static boolean _$3;
    private static String _$4;
    private static String _$5;
    private static String _$6;
    private static String _$7;
    private static int _$8 = 10;
    private static int _$9;
    private static String _$10;
    private static String _$11;

    static
    {
        _$7 = "jdbc:sqlite:D:\\Users\\wnc\\Programs\\sts-bundle\\projects\\baicizhan\\src\\main\\resources\\dictionary.db";
        _$4 = "root";
        _$5 = "1988";
        _$6 = "org.sqlite.JDBC";
        _$9 = 10;
        _$3 = false;

        _$10 = null;
        _$11 = null;
    }

    private static Connection createConnection()
    {
        Connection localConnection = null;
        try
        {
            Class.forName( _$6 );
            localConnection = DriverManager.getConnection( _$7 );
        } catch ( ClassNotFoundException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch ( SQLException localSQLException )
        {
            _$1.error( "createConnection()  is SQLException:"
                    + localSQLException.getMessage() );

            return null;
        }

        return localConnection;
    }

    public static synchronized Connection getConnection()
    {
        if ( _$vector == null )
        {
            return null;
        }
        Connection localConnection1 = null;

        synchronized ( _$vectors )
        {
            Connection localConnection2;
            if ( !_$3 && (!initConnection()) )
            {
                _$1.error( "getConnection()  initConnection is Err." );
                localConnection2 = null;
                return localConnection2;
            }

            if ( _$vector.size() > 0 )
            {
                while ( _$vector.size() > _$9 )
                {
                    Connection localConnection3 = (Connection)_$vector
                            .remove( 0 );
                    try
                    {
                        if ( (localConnection3 != null)
                                && (!localConnection3.isClosed()) )
                        {
                            try
                            {
                                if ( !localConnection3.getAutoCommit() )
                                {
                                    localConnection3.rollback();
                                }
                            } catch ( Exception localException2 )
                            {
                            }
                            localConnection3.close();
                            localConnection3 = null;
                        }
                    } catch ( Exception localException1 )
                    {
                        _$1.error( "数据库异常！" + localException1.getMessage() );
                    }

                }

                if ( ((localConnection1 = (Connection)_$vector
                        .remove( 0 )) != null) )
                {
                }

                localConnection2 = localConnection1;
                return localConnection2;
            }

            if ( (localConnection1 = createConnection()) == null )
            {
                _$1.error( "数据库异常！getConnection()  initConnection is Empty." );
            }
            return localConnection1;
        }
    }

    public static boolean closeConnect( Connection paramConnection )
    {
        int i = 0;
        try
        {
            if ( (paramConnection != null) && (!paramConnection.isClosed()) )
            {
                try
                {
                    if ( !paramConnection.getAutoCommit() )
                    {
                        paramConnection.rollback();
                    }
                } catch ( Exception localException1 )
                {
                }
                paramConnection.close();
                paramConnection = null;
                i = 1;
            } else
            {
            }
        } catch ( Exception localException2 )
        {
            i = 0;

        }

        return i == 0 ? false : true;
    }

    public static boolean disAllConnection()
    {
        if ( _$vector == null )
        {
            return false;
        }
        synchronized ( _$vector )
        {
            databaseLogMgr.errorLog( "DBconnectionMgr", "disAllConnection",
                    "disAllConnection ConHandle." );

            while ( _$vector.size() > 0 )
            {
                Connection localConnection;
                closeConnect(
                        localConnection = (Connection)_$vector.remove( 0 ) );
            }

        }

        return true;
    }

    public static String getCheckConnSql()
    {
        return "";
    }

    public static int getConnectionCount()
    {
        int i = 0;

        if ( _$vector != null )
        {
            i = _$vector.size();
        }
        return i;
    }

    public static String getDbCharacter()
    {
        return _$10;
    }

    public static int getDbType()
    {
        return 0;
    }

    public static String getOutCharacter()
    {
        return _$11;
    }

    public static boolean getReadOnly()
    {
        return false;
    }

    public static String getUserName()
    {
        return _$4;
    }

    public static boolean initConnection()
    {
        if ( _$vector.isEmpty() )
        {
            for ( int i = 0; i < _$8; ++i )
            {
                Connection localConnection;
                if ( (localConnection = createConnection()) != null )
                {
                    _$vector.add( localConnection );
                } else
                {
                    return false;
                }
            }

        }

        return true;
    }

    public static boolean isCheckErrorMessage( String paramString )
    {
        int i = 0;

        if ( (paramString != null) && (paramString.indexOf( "死锁" ) > 0) )
        {
            _$1.error( "DBconnectionMgr isCheckErrorMessage:" + paramString );
            i = 1;
        }

        return i == 0 ? false : true;
    }

    public static Connection isConnectError( String paramString,
            Connection paramConnection )
    {
        Statement localStatement = null;
        boolean bool = false;

        String str = null;
        try
        {
            try
            {
                if ( (paramConnection != null)
                        && (!paramConnection.isClosed()) )
                {
                    localStatement = paramConnection.createStatement( 1003,
                            1007 );

                    if ( BasicStringUtil
                            .isNullString( str = getCheckConnSql() ) )
                    {

                        str = "select 1 from dual";

                    }
                    bool = localStatement.execute( str );
                } else
                {
                    closeConnect( paramConnection );
                    paramConnection = null;
                }
            } catch ( Exception localException1 )
            {
                try
                {
                    paramConnection = null;
                    localStatement = null;
                    disAllConnection();

                    _$1.error( "远程数据库的网络故障:" + localException1.getMessage()
                            + str );
                } catch ( Exception localException3 )
                {
                    _$1.error( "断开远程数据库 Exception:"
                            + localException3.getMessage() + str );
                }
            }
        } finally
        {
            try
            {
                if ( localStatement != null )
                {
                    localStatement.close();
                    localStatement = null;
                }
            } catch ( Exception localException2 )
            {
                _$1.error( "isConnectError finally Exception:"
                        + localException2.getMessage() );
            }
        }

        return paramConnection;
    }

    public static boolean isValidConnect( Connection paramConnection )
    {
        Statement localStatement = null;
        boolean bool = false;

        String str = null;
        try
        {
            try
            {
                if ( (paramConnection != null)
                        && (!paramConnection.isClosed()) )
                {
                    localStatement = paramConnection.createStatement( 1003,
                            1007 );

                    if ( BasicStringUtil
                            .isNullString( str = getCheckConnSql() ) )
                    {

                        str = "select 1 from dual";

                    }
                    bool = localStatement.execute( str );
                } else
                {
                    closeConnect( paramConnection );
                    paramConnection = null;
                }
            } catch ( Exception localException1 )
            {
                try
                {
                    localStatement = null;
                    paramConnection = null;
                    disAllConnection();

                    _$1.error( "远程数据库的网络故障:" + localException1.getMessage()
                            + str );
                } catch ( Exception localException3 )
                {
                    _$1.error( "断开远程数据库 Exception:"
                            + localException3.getMessage() + str );
                }
            }
        } finally
        {
            try
            {
                if ( localStatement != null )
                {
                    localStatement.close();
                    localStatement = null;
                }
            } catch ( Exception localException2 )
            {
                _$1.error( "isValidConnect finally Exception:"
                        + localException2.getMessage() );
            }
        }

        return bool;
    }

    public static String printDBconnectionMgr()
    {
        StringBuffer localStringBuffer = new StringBuffer();

        int i = getConnectionCount();
        localStringBuffer.append( "default=" + i + "<br>" );

        return localStringBuffer.toString();
    }

    public static synchronized void returnConnection(
            Connection paramConnection )
    {
        synchronized ( _$vector )
        {
            if ( paramConnection != null )
            {
                _$vector.add( paramConnection );
            }
            return;
        }
    }

    public static boolean returnValidConnect( Connection paramConnection )
    {
        int i = 0;

        if ( isValidConnect( paramConnection ) )
        {
            returnConnection( paramConnection );
        }

        return i == 0 ? false : true;
    }

    public static void setCheckConnSql( String paramString )
    {
        // BasicDbUtil.setCheckConnSql(paramString);
    }

    public static void setClassName( String paramString )
    {
        _$6 = paramString;
    }

    public static void setConnectionNumber( int paramInt )
    {
        _$8 = paramInt;
    }

    public static void setJDBCName( String paramString )
    {
        _$7 = paramString;
        if ( !_$vectors.containsKey( paramString ) )
            _$vectors.put( paramString, new Vector() );
        _$vector = _$vectors.get( paramString );
    }

    public static void setMaxIdelNumber( int paramInt )
    {
        _$9 = paramInt;
    }

    public static void setPassword( String paramString )
    {
        _$5 = paramString;
    }

    public static void setUserName( String paramString )
    {
        _$4 = paramString;
    }
}