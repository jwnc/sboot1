
package com.wnc.sboot1.itbook.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.sboot1.common.exceptions.CheckException;
import com.wnc.sboot1.itbook.entity.WordExample;
import com.wnc.sboot1.itbook.helper.DbConverter;

import db.DbExecMgr;

public class StarQService
{
    final static Logger logger = Logger.getLogger( StarQService.class );

    public static List<WordExample> searchStarQList( int page, int size )
    {
        List<WordExample> list = new ArrayList<WordExample>();

        String sql = "SELECT * FROM  QUESTION q where  q.star=1 order by q.update_time desc limit "
                + (page - 1) * size + "," + size;
        logger.info( sql );
        Map selectAllSqlMap = DbExecMgr.getSelectAllSqlMap( sql );
        WordExample wordExample = null;
        Map fieldMap;
        for ( int i = 1; i <= selectAllSqlMap.size(); i++ )
        {
            wordExample = new WordExample();
            fieldMap = (Map)selectAllSqlMap.get( i );
            wordExample.setAnswers( DbConverter.cvtInt( fieldMap, "ANSWERS" ) );
            wordExample.setExcerpt( DbConverter.cvtStr( fieldMap, "EXCERPT" ) );
            wordExample.setHref( DbConverter.cvtStr( fieldMap, "URL" ) );
            wordExample.setQ( DbConverter.cvtStr( fieldMap, "TITLE" ) );
            wordExample.setVotes( DbConverter.cvtInt( fieldMap, "VOTES" ) );
            wordExample.setQid( DbConverter.cvtInt( fieldMap, "ID" ) );
            wordExample.setStar( DbConverter.cvtInt( fieldMap, "STAR" ) );
            list.add( wordExample );
        }

        return list;
    }

    public static int getStarQCount()
    {
        return BasicNumberUtil.getNumber( String.valueOf( DbExecMgr
                .getSelectSqlMap(
                        "SELECT COUNT(1) CNT FROM QUESTION q where  q.star=1" )
                .get( "CNT" ) ) );
    }

    public static void changeStarStatus( int qid, int status )
    {
        try
        {
            DbExecMgr
                    .execOnlyOneUpdate( "UPDATE QUESTION SET STAR=1,UPDATE_TIME='"
                            + BasicDateUtil.getCurrentDateTimeString()
                            + "' where id=" + qid );
        } catch ( Exception e )
        {
            throw new CheckException( e );
        }
    }
}