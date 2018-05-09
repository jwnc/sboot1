
package com.wnc.sboot1.itbook.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.wnc.itbooktool.BookLog;
import com.wnc.tools.FileOp;

import db.DbExecMgr;
import db.DbField;
import db.DbFieldSqlUtil;

public class ITBookLogService
{
    private static final Logger logger = Logger
            .getLogger( ITBookLogService.class );

    public static void parseLogsToDb( final String path, final String device )
    {
        new Thread( new Runnable()
        {

            @Override
            public void run()
            {
                List<String> readFrom = FileOp.readFrom( path );
                for ( String string : readFrom )
                {
                    BookLog blog = JSONObject.parseObject( string,
                            BookLog.class );
                    DbFieldSqlUtil util = new DbFieldSqlUtil( "itbook_log",
                            "" );
                    util.addInsertField( new DbField( "device", device ) );
                    util.addInsertField(
                            new DbField( "dict_id", blog.getTopic() ) );
                    util.addInsertField(
                            new DbField( "content", StringEscapeUtils
                                    .escapeSql( blog.getContent() ) ) );
                    util.addInsertField(
                            new DbField( "log_time", blog.getTime() ) );
                    util.addInsertField(
                            new DbField( "type", blog.getType() ) );

                    try
                    {
                        if ( !DbExecMgr.isExistData(
                                "SELECT * FROM itbook_log WHERE LOG_TIME='"
                                        + blog.getTime() + "' AND DEVICE='"
                                        + device + "'" ) )
                            DbExecMgr.execOnlyOneUpdate( util.getInsertSql() );
                    } catch ( SQLException e )
                    {
                        e.printStackTrace();
                        logger.error( e );
                    }
                }
            }
        } ).start();
    }

    public static void deleteLog( int logId ) throws SQLException
    {
        String sql = "UPDATE ITBOOK_LOG SET DELETED=1 WHERE ID=" + logId;
        logger.info( sql );
        DbExecMgr.execOnlyOneUpdate( sql );
    }
}