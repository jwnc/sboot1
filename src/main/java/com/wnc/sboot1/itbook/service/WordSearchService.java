
package com.wnc.sboot1.itbook.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.wnc.basic.BasicDateUtil;
import com.wnc.sboot1.common.exceptions.CheckException;
import com.wnc.sboot1.itbook.entity.WordExample;
import com.wnc.sboot1.itbook.entity.WordSearchDTO;
import com.wnc.sboot1.itbook.helper.DbConverter;

import db.DbExecMgr;
import db.DbField;
import db.DbFieldSqlUtil;

public class WordSearchService
{
    final static Logger logger = Logger.getLogger( WordSearchService.class );

    public static WordSearchDTO getWordSearch( String word )
    {
        WordSearchDTO wordSearch = null;
        String sql = "SELECT * FROM WORD_SEARCH WHERE WORD='" + word + "'";
        logger.info( sql );
        Map selectSqlMap = DbExecMgr.getSelectSqlMap( sql );
        if ( selectSqlMap.size() > 0 )
        {
            wordSearch = new WordSearchDTO();
            wordSearch.setDisplayListSize( DbConverter.cvtInt( selectSqlMap,
                    "DISPLAY_LIST_SIZE" ) );
            wordSearch
                    .setDictId( DbConverter.cvtInt( selectSqlMap, "DICT_ID" ) );
            wordSearch.setResultCount( DbConverter.cvtInt( selectSqlMap,
                    "RESULT_COUNT" ) );
            wordSearch.setSearchUrl( DbConverter.cvtStr( selectSqlMap,
                    "SEARCH_URL" ) );
            wordSearch.setPage( DbConverter
                    .cvtInt( selectSqlMap, "SEARCH_PAGE" ) );
            wordSearch.setWord( word );
        }

        return wordSearch;
    }

    public static List<WordExample> searchList( String word )
    {
        List<WordExample> list = null;
        if ( DbExecMgr.isExistData( "SELECT * FROM WORD_QUESTIONS WHERE WORD='"
                + word + "'" ) )
        {
            list = new ArrayList<WordExample>();

            String sql = "SELECT * FROM WORD_QUESTIONS w, QUESTION q where w.question_id = q.id and word = '"
                    + word + "' order by q.votes desc";
            logger.info( sql );
            Map selectAllSqlMap = DbExecMgr.getSelectAllSqlMap( sql );
            WordExample wordExample = null;
            Map fieldMap;
            for ( int i = 1; i <= selectAllSqlMap.size(); i++ )
            {
                wordExample = new WordExample();
                fieldMap = (Map)selectAllSqlMap.get( i );
                wordExample.setAnswers( DbConverter
                        .cvtInt( fieldMap, "ANSWERS" ) );
                wordExample.setExcerpt( DbConverter
                        .cvtStr( fieldMap, "EXCERPT" ) );
                wordExample.setHref( DbConverter.cvtStr( fieldMap, "URL" ) );
                wordExample.setQ( DbConverter.cvtStr( fieldMap, "TITLE" ) );
                wordExample.setVotes( DbConverter.cvtInt( fieldMap, "VOTES" ) );
                wordExample.setQid( DbConverter.cvtInt( fieldMap, "ID" ) );
                wordExample.setStar( DbConverter.cvtInt( fieldMap, "STAR" ) );
                wordExample.setWord( word );
                list.add( wordExample );
            }
        }

        return list;
    }

    public static WordExample getWordQuestion( int id )
    {
        String sql = "SELECT * FROM QUESTION WHERE ID=" + id;
        logger.info( sql );
        Map selectSqlMap = DbExecMgr.getSelectSqlMap( sql );
        WordExample wordExample = null;
        if ( selectSqlMap.size() > 0 )
        {
            wordExample = new WordExample();
            wordExample.setExcerpt( DbConverter
                    .cvtStr( selectSqlMap, "EXCERPT" ) );
            wordExample.setQ( DbConverter.cvtStr( selectSqlMap, "TITLE" ) );
        }

        return wordExample;
    }

    public static void ayncLogSearch( WordSearchDTO wordSearch,
            List<WordExample> wordExamples )
    {
        final WordSearchDTO wordSearchTmp = wordSearch;
        final List<WordExample> wordExamplesTmp = wordExamples;
        new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    WordSearchService
                            .logSearch( wordSearchTmp, wordExamplesTmp );
                } catch ( Exception e )
                {
                    logger.error( e );
                }
            }
        } ).start();
    }

    /**
     * 记录搜索结果
     * 
     * @param wordSearch
     * @param wordExamples
     * @throws SQLException
     */
    public static void logSearch( WordSearchDTO wordSearch,
            List<WordExample> wordExamples )
    {
        try
        {
            if ( !DbExecMgr
                    .isExistData( "SELECT * FROM WORD_SEARCH WHERE WORD = '"
                            + wordSearch.getWord() + "'" ) )
            {
                String sql = "INSERT INTO WORD_SEARCH(WORD,DICT_ID,SEARCH_URL,RESULT_COUNT,DISPLAY_LIST_SIZE,CREATE_TIME) VALUES('"
                        + wordSearch.getWord()
                        + "',"
                        + wordSearch.getDictId()
                        + ",'"
                        + wordSearch.getSearchUrl()
                        + "',"
                        + wordSearch.getResultCount()
                        + ","
                        + wordSearch.getDisplayListSize()
                        + ",'"
                        + BasicDateUtil.getCurrentDateTimeString() + "')";
                logger.info( sql );
                DbExecMgr.execOnlyOneUpdate( sql );
            } else
            {
                String sql = "UPDATE WORD_SEARCH SET SEARCH_PAGE="
                        + wordSearch.getPage() + ",DISPLAY_LIST_SIZE="
                        + wordSearch.getDisplayListSize() + ",RESULT_COUNT="
                        + wordSearch.getResultCount() + " WHERE WORD='"
                        + wordSearch.getWord() + "'";
                logger.info( sql );
                DbExecMgr.execOnlyOneUpdate( sql );
            }

            for ( WordExample wordExample : wordExamples )
            {
                if ( !DbExecMgr
                        .isExistData( "SELECT * FROM WORD_QUESTIONS WHERE WORD = '"
                                + wordSearch.getWord()
                                + "' AND QUESTION_ID="
                                + wordExample.getQid() ) )
                {
                    DbFieldSqlUtil util = new DbFieldSqlUtil( "WORD_QUESTIONS",
                            "" );
                    util.addInsertField( new DbField( "WORD", wordExample
                            .getWord() ) );
                    util.addInsertField( new DbField( "QUESTION_ID",
                            wordExample.getQid() + "" ) );
                    logger.info( util.getInsertSql() );
                    DbExecMgr.execOnlyOneUpdate( util.getInsertSql() );
                }
                if ( !existQuestion( wordExample.getQid() ) )
                {
                    DbFieldSqlUtil util = new DbFieldSqlUtil( "QUESTION", "" );
                    util.addInsertField( new DbField( "ID", wordExample
                            .getQid() + "" ) );
                    util.addInsertField( new DbField( "TITLE",
                            StringEscapeUtils.escapeSql( wordExample.getQ() ) ) );
                    util.addInsertField( new DbField( "URL", StringEscapeUtils
                            .escapeSql( wordExample.getHref() ) ) );
                    util.addInsertField( new DbField( "EXCERPT",
                            StringEscapeUtils.escapeSql( wordExample
                                    .getExcerpt() ) ) );
                    util.addInsertField( new DbField( "VOTES", wordExample
                            .getVotes() + "" ) );
                    util.addInsertField( new DbField( "ANSWERS", wordExample
                            .getAnswers() + "" ) );
                    logger.info( util.getInsertSql() );
                    DbExecMgr.execOnlyOneUpdate( util.getInsertSql() );
                } else
                {
                    DbFieldSqlUtil util = new DbFieldSqlUtil( "QUESTION", "" );
                    util.addUpdateField( new DbField( "TITLE",
                            StringEscapeUtils.escapeSql( wordExample.getQ() ) ) );
                    util.addUpdateField( new DbField( "URL", StringEscapeUtils
                            .escapeSql( wordExample.getHref() ) ) );
                    util.addUpdateField( new DbField( "EXCERPT",
                            StringEscapeUtils.escapeSql( wordExample
                                    .getExcerpt() ) ) );
                    util.addUpdateField( new DbField( "VOTES", wordExample
                            .getVotes() + "" ) );
                    util.addUpdateField( new DbField( "ANSWERS", wordExample
                            .getAnswers() + "" ) );
                    util.addWhereField( new DbField( "ID", wordExample.getQid()
                            + "" ) );
                    logger.info( util.getUpdateSql() );
                    DbExecMgr.execOnlyOneUpdate( util.getUpdateSql() );
                }
            }
        } catch ( Exception e )
        {
            throw new CheckException( e );
        }
    }

    public static boolean existQuestion( int id )
    {
        return DbExecMgr.isExistData( "SELECT * FROM QUESTION WHERE ID=" + id );
    }

}