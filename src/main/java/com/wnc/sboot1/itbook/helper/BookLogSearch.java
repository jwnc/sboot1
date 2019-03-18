
package com.wnc.sboot1.itbook.helper;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.itbooktool.dao.DictionaryDao;
import com.wnc.sboot1.SpringContextUtils;
import com.wnc.sboot1.itbook.entity.BookLogCondition;
import com.wnc.sboot1.itbook.entity.BookLogVO;
import com.wnc.sboot1.readlog.ReadLogRepository;

import db.DbExecMgr;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BookLogSearch
{
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private ReadLogRepository readLogRepository;
	
    final static Logger logger = Logger.getLogger( BookLogSearch.class );
    private BookLogCondition condition;

    public BookLogSearch setCondition( BookLogCondition condition )
    {
        this.condition = condition;
        return this;
    }

    public List<BookLogVO> search( int page, int size )
    {
        String sql = getSql( page, size );
        logger.info( "itbook search:" + sql );

        List<BookLogVO> list = new ArrayList<>();
        BookLogVO blog;
        Query createNativeQuery = entityManager.createNativeQuery( sql );
        List resultList = createNativeQuery.getResultList();
		for (Object obj : resultList) {
			Object[] arr = (Object[])obj;
            blog = new BookLogVO();
            blog.setId( DictionaryDao.getArrInt(arr, 0 ) );
            blog.setContent( DictionaryDao.getArrStr(arr, 1 ) );
            blog.setDevice( DictionaryDao.getArrStr(arr, 2 ) );
            blog.setTopic( DictionaryDao.getArrStr(arr, 3 ) );
            blog.setTime( DictionaryDao.getArrStr(arr, 4 ) );
            blog.setType( DictionaryDao.getArrStr(arr, 5 ) );
            blog.setMean( DictionaryDao.getArrStr(arr, 6 ) );
            blog.setWeight( DictionaryDao.getArrInt(arr, 7 ) );
            blog.setDeviceCnName( DictionaryDao.getArrStr(arr, 8 ) );
            list.add( blog );
        }

        return list;
    }

    private String getSql( int page, int size )
    {
        return getSql()
                + String.format( " limit %d, %d", (page - 1) * size, size );
    }

    private String getSql()
    {
        String sql = "SELECT b.ID,b.CONTENT,b.DEVICE,b.DICT_ID,b.LOG_TIME,b.TYPE,d.mean_cn,d.weight,dv.cn_name DEVICECNNAME FROM ITBOOK_LOG b left join device dv on b.device=dv.name left join dictionary d on b.dict_id=d.id WHERE b.deleted = 0 ";
        if ( StringUtils.isNotBlank( condition.getDevice() ) )
        {
            sql += " AND b.DEVICE='" + condition.getDevice() + "'";
        }
        if ( StringUtils.isNotBlank( condition.getWord() ) )
        {
            sql += " AND upper(b.CONTENT) LIKE '%" + StringEscapeUtils
                    .escapeSql( condition.getWord().trim().toUpperCase() )
                    + "%'";
        }
        if ( StringUtils.isNotBlank( condition.getDayStart() ) )
        {
            sql += " AND b.LOG_TIME>='" + cvtDay( condition.getDayStart(), 0 )
                    + "'";
        }
        if ( StringUtils.isNotBlank( condition.getDayEnd() ) )
        {
            sql += " AND b.LOG_TIME<='" + cvtDay( condition.getDayEnd(), 1 )
                    + "'";
        }
        if ( StringUtils.isNotBlank( condition.getType() ) && StringUtils
                .containsAny( condition.getType(), '1', '2', '3' ) )
        {
            sql += " AND b.TYPE = " + condition.getType();
        }
        if ( condition.getWeightStart() != null )
        {
            sql += " AND d.weight >= " + condition.getWeightStart();
        }
        if ( condition.getWeightEnd() != null )
        {
            sql += " AND ifnull(d.weight,0) <= " + condition.getWeightEnd();// weight字段在数据库可能为空
        }
        if ( condition.getStar() != null && condition.getStar() == 1 )
        {
            sql += " AND b.content in (select word from word_questions wq,question q where wq.question_id = q.id and q.star = 1)";
        }
        sql += " order by log_time desc";

        return sql;

    }

    private String cvtDay( String day, int i )
    {
        if ( day.matches( "\\d{8}" ) )
        {
            return day.substring( 0, 4 ) + "-" + day.substring( 4, 6 ) + "-"
                    + day.substring( 6 ) + (i == 0 ? " 00:00:00" : " 23:59:59");
        }
        if ( day.matches( "\\d{4}-\\d{2}-\\d{2}" ) )
        {
            return day + (i == 0 ? " 00:00:00" : " 23:59:59");
        }
        return day;
    }

    public static void main( String[] args )
    {
        BookLogCondition condition = new BookLogCondition();
        condition.setDayStart( "20171110" );
        // condition.setDayEnd( "20171117" );
        condition.setDevice( "" );
        condition.setWord( "g" );
        BookLogSearch bookLogSearch = (BookLogSearch)SpringContextUtils.getContext().getBean("BookLogSearch");
        List<BookLogVO> search = bookLogSearch.setCondition( condition ).search( 3, 2 );
        System.out.println( JSONObject.toJSONString( search,
                SerializerFeature.PrettyFormat ) );
        System.out.println( search );

    }

    public long getTotalRows()
    {
    	String sql = "SELECT COUNT(1) CNT FROM (" + getSql() + ") t";
    	Query createNativeQuery = entityManager.createNativeQuery( sql );
        List resultList = createNativeQuery.getResultList();
        if(resultList != null && resultList.size() > 0){
        	return ((BigInteger)resultList.get(0)).longValue();
        }
        return 0;
    }
}