
package com.wnc.sboot1.spy.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.crawl.core.util.HttpClientUtil;
import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.sboot1.spy.util.SpiderUtils;
import com.wnc.sboot1.spy.zhihu.TT2;
import com.wnc.sboot1.spy.zhihu.active.aggre.TargetAggreInfo;
import com.wnc.sboot1.spy.zhihu.active.aggre.TargetAggreKey;
import com.wnc.sboot1.spy.zhihu.active.aggre.TargetDesc;
import com.wnc.sboot1.spy.zhihu.active.aggre.TargetDescVo;
import com.wnc.sboot1.spy.zhihu.active.target.Answer;
import com.wnc.sboot1.spy.zhihu.active.target.Article;
import com.wnc.sboot1.spy.zhihu.active.target.Question;
import com.wnc.sboot1.spy.zhihu.active.target.RoundTable;
import com.wnc.sboot1.spy.zhihu.active.target.ZColumn;
import com.wnc.sboot1.spy.zhihu.rep.AnswerRepository;
import com.wnc.sboot1.spy.zhihu.rep.ArticleRepository;
import com.wnc.sboot1.spy.zhihu.rep.CollectionRepository;
import com.wnc.sboot1.spy.zhihu.rep.ColumnRepository;
import com.wnc.sboot1.spy.zhihu.rep.QuestionRepository;
import com.wnc.sboot1.spy.zhihu.rep.RoundTableRepository;
import com.wnc.sboot1.spy.zhihu.rep.TargetAggreInfoRepository;
import com.wnc.sboot1.spy.zhihu.rep.TargetDescRepository;
import com.wnc.sboot1.spy.zhihu.rep.TopicRepository;

@Component
public class ZhihuActivityService
{
    private static Logger logger = Logger
            .getLogger( ZhihuActivityService.class );
    public static final int FOLLOW_DAY_COUNT = 15;
    public static final int FOLLOW_WEEK_COUNT = 40;
    public static final int FOLLOW_MONTH_COUNT = 90;
    public static final int FOLLOW_YEAR_COUNT = 300;

    public static final int AGGRE_DAY_CODE = 1;
    public static final int AGGRE_WEEK_CODE = 2;
    public static final int AGGRE_MONTH_CODE = 3;
    public static final int AGGRE_YEAR_CODE = 4;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    CollectionRepository collectionRepository;
    @Autowired
    ColumnRepository columnRepository;
    @Autowired
    RoundTableRepository roundTableRepository;
    @Autowired
    TopicRepository topicRepository;
    @Autowired
    TargetAggreInfoRepository targetAggreInfoRepository;
    @Autowired
    TargetDescRepository targetDescRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public void aggreYesterday()
    {
        String day1 = SpiderUtils.getYesterDayStr();
        aggre( day1, day1, AGGRE_DAY_CODE, FOLLOW_DAY_COUNT );
    }

    public void aggreLastWeek()
    {
        int currentWeekDay = BasicDateUtil.getCurrentWeekDay();
        String today = BasicDateUtil.getCurrentDateString();
        String lastSunday = BasicDateUtil.getDateBeforeDayDateString( today,
                currentWeekDay );
        String lastMonday = BasicDateUtil
                .getDateBeforeDayDateString( lastSunday, 6 );
        aggre( SpiderUtils.wrapDayWithLine( lastMonday ),
                SpiderUtils.wrapDayWithLine( lastSunday ), AGGRE_WEEK_CODE,
                FOLLOW_WEEK_COUNT );
    }

    public void aggreThisWeek()
    {
        int currentWeekDay = BasicDateUtil.getCurrentWeekDay();
        String today = BasicDateUtil.getCurrentDateString();
        String thisMonday = BasicDateUtil.getDateBeforeDayDateString( today,
                currentWeekDay - 1 );
        aggre( SpiderUtils.wrapDayWithLine( thisMonday ),
                SpiderUtils.wrapDayWithLine( today ), AGGRE_WEEK_CODE,
                FOLLOW_WEEK_COUNT );
    }

    public void aggreMonth()
    {
        String year = BasicDateUtil.getCurrentYearString();
        String month = BasicDateUtil.getCurrentMonthString();
        int currentMonthLastDay = BasicDateUtil.getCurrentMonthLastDay();

        aggre( SpiderUtils.wrapDayWithLine( year + month + "01" ),
                SpiderUtils
                        .wrapDayWithLine( year + month + currentMonthLastDay ),
                AGGRE_MONTH_CODE, FOLLOW_MONTH_COUNT );
    }

    public void aggreLastMonth()
    {
        String day = BasicDateUtil.getCurrentDateString();
        String last3Day = BasicDateUtil.getDateBeforeDayDateString( day, 3 );
        String yearOfLastDay = last3Day.substring( 0, 4 );
        String monOfLastDay = last3Day.substring( 4, 6 );

        String lastDayOfMonth = SpiderUtils.getLastDayOfMonth(
                BasicNumberUtil.getNumber( yearOfLastDay ),
                BasicNumberUtil.getNumber( monOfLastDay ) );

        aggre( SpiderUtils
                .wrapDayWithLine( yearOfLastDay + monOfLastDay + "01" ),
                lastDayOfMonth, AGGRE_MONTH_CODE, FOLLOW_MONTH_COUNT );
    }

    public void aggreLastYear()
    {
        String year = BasicDateUtil.getCurrentYearString();
        String lastYear = "" + (BasicNumberUtil.getNumber( year ) - 1);

        aggre( SpiderUtils.wrapDayWithLine( lastYear + "0101" ),
                SpiderUtils.wrapDayWithLine( lastYear + "1231" ),
                AGGRE_YEAR_CODE, FOLLOW_YEAR_COUNT );
    }

    public void aggreYear()
    {
        String year = BasicDateUtil.getCurrentYearString();

        aggre( SpiderUtils.wrapDayWithLine( year + "0101" ),
                SpiderUtils.wrapDayWithLine( year + "1231" ), AGGRE_YEAR_CODE,
                FOLLOW_YEAR_COUNT );
    }

    /**
     * 两个日期的闭区间的数据
     * 
     * @param day1
     * @param day2
     * @param aggreCode
     * @param count
     */
    public void aggre( final String day1, final String day2, int aggreCode,
            int count )
    {
        String sql = "select act.cnt,tar.tid tid,tar.info title,tar.type from (SELECT target_id, count(*) cnt FROM zh_activity WHERE FROM_UNIXTIME(created_time) >= '"
                + day1 + " 00:00:00' and FROM_UNIXTIME(created_time) <= '"
                + day2 + " 23:59:59' group by target_id having count(*) > "
                + count + ") act, zh_target tar where act.target_id=tar.tid";
        Query createNativeQuery = entityManager.createNativeQuery( sql );
        List resultList = createNativeQuery.getResultList();
        logger.info( String.format( "Day:%s AggreCode:%d  Size:%d SQL:%s", day1,
                aggreCode, resultList.size(), sql ) );
        for ( Object object : resultList )
        {
            Object[] arr = (Object[])object;
            TargetAggreInfo targetAggreInfo = new TargetAggreInfo();
            TargetDesc targetDesc = new TargetDesc();
            targetDesc.setTid( String.valueOf( arr[1] ) );
            targetDesc.setTitle( String.valueOf( arr[2] ) );
            targetDesc.setType( String.valueOf( arr[3] ) );

            TargetAggreKey key = new TargetAggreKey();
            key.setAggreCode( aggreCode );
            // 日期
            key.setDateStr( day1 );
            key.setTid( targetDesc.getTid() );

            targetAggreInfo.setId( key );
            targetAggreInfo.setCnt(
                    BasicNumberUtil.getNumber( String.valueOf( arr[0] ) ) );
            targetAggreInfo.setTargetDesc( targetDesc );

            find( targetDesc );

            try
            {
                targetDescRepository.save( targetDesc );
            } catch ( Exception e )
            {
            }
            try
            {
                targetAggreInfoRepository.save( targetAggreInfo );
            } catch ( Exception e )
            {
            }

        }
    }

    public List getAggreData( String dateStr, int aggreCode )
    {
        String sql = "select ti.cnt,td.description,td.tid,td.title,td.type,td.url from target_aggre_info ti, target_desc td where ti.tid = td.tid and aggre_code="
                + aggreCode + " and date_str = '" + dateStr
                + "' order by cnt desc";
        Query createNativeQuery = entityManager.createNativeQuery( sql );
        List<Object[]> aggreData = createNativeQuery.getResultList();
        List<TargetDescVo> list = new ArrayList<TargetDescVo>();
        TargetDescVo vo;
        for ( Object[] arr : aggreData )
        {
            vo = new TargetDescVo();
            vo.setCnt( BasicNumberUtil.getNumber( String.valueOf( arr[0] ) ) );
            vo.setDescription( String.valueOf( arr[1] ) );
            vo.setTid( String.valueOf( arr[2] ) );
            vo.setTitle( String.valueOf( arr[3] ) );
            vo.setType( String.valueOf( arr[4] ) );
            vo.setUrl( String.valueOf( arr[5] ) );
            list.add( vo );
        }
        return list;
    }

    private void find( TargetDesc targetAggreInfo )
    {
        String description = "";
        String url = "";
        try
        {
            String type = targetAggreInfo.getType();
            String tid = targetAggreInfo.getTid();
            switch ( type )
            {
                case "answer":
                    Answer findOne = answerRepository.findOne( tid );
                    String question_id = findOne.getQuestion_id();
                    Question findOne2 = questionRepository
                            .findOne( question_id );
                    String title = "";
                    if ( findOne2 == null )
                    {
                        HttpGet request = new HttpGet(
                                "https://api.zhihu.com/questions/"
                                        + question_id );
                        request.setHeader( "authorization",
                                "oauth " + TT2.initAuthorization() );
                        String webPage = HttpClientUtil.getWebPage( request );
                        title = JSONObject.parseObject( webPage )
                                .getString( "title" );
                    } else
                    {
                        title = findOne2.getTitle();
                    }

                    description = findOne.getExcerpt_new();
                    targetAggreInfo.setTitle( title );
                    url = "https://www.zhihu.com/question/" + question_id
                            + "/answer/" + tid;
                    break;
                case "article":
                    Article article = articleRepository.findOne( tid );
                    description = article.getExcerpt_new();
                    url = "https://zhuanlan.zhihu.com/p/" + tid;
                    break;
                case "question":
                    Question question = questionRepository.findOne( tid );
                    description = question.getExcerpt();
                    url = "https://www.zhihu.com/question/" + tid;
                    break;
                case "topic":
                    url = "https://www.zhihu.com/topic/" + tid + "/hot";
                    break;
                case "column":
                    ZColumn column = columnRepository.findOne( tid );
                    description = column.getIntro();
                    url = "https://zhuanlan.zhihu.com/" + tid;
                    break;
                case "collection":
                    url = "https://www.zhihu.com/collection/" + tid;
                    break;
                case "roundtable":
                    RoundTable roundTable = roundTableRepository.findOne( tid );
                    description = roundTable.getDescription();
                    url = "https://www.zhihu.com//" + tid;
                    break;
                default:
                    break;
            }
        } catch ( Exception e )
        {
            e.printStackTrace();
        }

        targetAggreInfo.setUrl( url );
        targetAggreInfo.setDescription( description );
    }
}
