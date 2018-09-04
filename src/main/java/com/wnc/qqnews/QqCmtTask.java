
package com.wnc.qqnews;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.wnc.qqnews.jpa.entity.QqUser;
import com.wnc.qqnews.service.QqDbService;
import com.wnc.sboot1.SpringContextUtils;
import com.wnc.sboot1.spy.zuqiu.TaskCompleteLog;
import com.wnc.sboot1.spy.zuqiu.rep.TaskCompleteLogRepository;
import com.wnc.sboot1.spy.zuqiu.rep.Zb8NewsRepository;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawl.spider.entity.Page;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicFileUtil;
import com.wnc.wynews.model.NewsModule;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 找出两个月之间的
 */
public class QqCmtTask extends AbstractPageTask
{
    private static final int PAGE_SIZE = 30;
    private static String CMT_URL_FORMAT = "http://coral.qq.com/article/%s/comment/v2?orinum="
            + PAGE_SIZE
            + "&oriorder=o&pageflag=1&cursor=%s&scorecursor=0&orirepnum=2&reporder=o&reppageflag=1&source=1&_=%d";
    private static Logger logger = Logger.getLogger( QqCmtTask.class );
    private String code;
    private String cursor;
    private NewsModule newsModule;
    private int cmtListSize;
    private QqDbService qqDbService = (QqDbService) SpringContextUtils.getContext().getBean("qqDbService");
    private boolean ignoreComp = false;
    static
    {
        retryMap.put( QqCmtTask.class,
                new ConcurrentHashMap<String, Integer>() );
    }

    public QqCmtTask( NewsModule newsModule,String code )
    {
        this( newsModule, code, "0" );
    }

    public QqCmtTask( NewsModule newsModule,String code,String cursor )
    {
        this.newsModule = newsModule;
        this.code = code;
        this.cursor = cursor;

        this.url = String.format( CMT_URL_FORMAT, this.code, this.cursor,
                System.currentTimeMillis() );
        this.proxyFlag = true;

        this.MAX_RETRY_TIMES = 20;

        request = new HttpGet( url );
    }

    public QqCmtTask setMaxRetryTimes( int tm )
    {
        this.MAX_RETRY_TIMES = tm;
        return this;
    }

    @Override
    public void run()
    {
        super.run();
    }

    @Override
    protected void retry()
    {
        QqSpiderClient.getInstance()
                .submitTask( new QqCmtTask( newsModule, code, cursor ) );
    }

    @Override
    protected void handle( Page page ) throws Exception
    {
        // TODO
        int errCode = 0;
        boolean hasNext = false;

        String res = page.getHtml();
        // res = PatternUtil.getFirstPatternGroup( res, ".*?(\\{.+)\\)$" );

        JSONObject parseObject = JSONObject.parseObject( res );
        errCode = parseObject.getIntValue( "errCode" );
        if ( errCode == 0 )
        {
            JSONObject dataObject = parseObject.getJSONObject( "data" );
            hasNext = dataObject.getBooleanValue( "hasnext" );

            int intValue = dataObject.getIntValue( "oriretnum" );
            if ( intValue > 0 )
            {
                outputCmtData( dataObject );
                cmtListSize = dataObject.getIntValue( "oritotal" );
            }
            if ( hasNext )
            {
                nextJob( dataObject.getString( "last" ) );
                ignoreComp = true;
            } else if ( "false".equals( dataObject.getString( "first" ) ) )
            {
                // 如果是首页, 且列表为空
                if ( intValue == 0 )
                {
                    // 无记录, 记作错误日志
                    complete( 99, "评论列表空白" );
                    ignoreComp = true;
                }
            }
        } else
        {
            complete( errCode + 10000, "出错:errCode=" + errCode );
            ignoreComp = true;
        }

    }

    @Override
    protected void errLogExp( Exception e )
    {
        e.printStackTrace();
    }

    /**
     * 重试时需要删除随机数
     */
    protected String removeRandom()
    {
        return url.replaceAll( "\\d{13}$", "" );
    }

    @Override
    protected void errLog404( Page page )
    {
        retryMonitor( "404 continue..." );
        ignoreComp = true;
    }

    private void outputCmtData( JSONObject dataObject )
    {
        // BasicFileUtil.writeFileString( "C:\\data\\spider\\qq-news\\dddd.txt",
        // JSONObject.toJSONString( dataObject ) + "\r\n", null, true );

        JSONArray commentArr = dataObject.getJSONArray( "oriCommList" );
        for ( int i = 0; i < commentArr.size(); i++ )
        {
            JSONObject cmtJO = commentArr.getJSONObject( i );
            // 输出cmt到curDateStr目录
            BasicFileUtil.writeFileString(
                    QqNewsUtil.getCommentLocation( newsModule, code ),
                    cmtJO.toJSONString() + "\r\n", null, true );
        }

        JSONObject usersoJO = dataObject.getJSONObject( "userList" );
        Set<String> keys = usersoJO.keySet();
        QqUser qqUser;
        for ( String key : keys )
        {
            JSONObject userJO = usersoJO.getJSONObject( key );
            // 输出user到user目录
//            System.out.println( userJO.getString( "nick" ) + " / "
//                    + userJO.getString( "userid" ) );
            // QqUserManager.addAndWriteUser( userJO );
            qqUser = userJO.toJavaObject(QqUser.class).splitRegion().computeCertId();
            qqDbService.singleUser(qqUser);
        }
    }

    private void nextJob( String nextCursor )
    {
        QqSpiderClient.getInstance()
                .submitTask( new QqCmtTask( newsModule, code, nextCursor ) );
    }

    // protected void errLog404( Page page )
    // {
    // ignoreComp=true;
    // retryMonitor( "404 continue..." );
    // logger.error("--404--"+url);
    // }

    @Override
    protected void complete( int type, String msg )
    {
        if ( ignoreComp )
        {
            return;
        }
        try
        {
            super.complete( type, msg );

            if ( type != COMPLETE_STATUS_SUCCESS )
            {
                logger.error( code + "在" + url + "失败, 失败原因:" + msg );
                QqNewsUtil.err( newsModule, code + "在" + url + "失败, 失败原因:" + msg
                        + " 应抓取总数:" + cmtListSize );
            } else
            {
                // 任务完成数加1
                QqSpiderClient.getInstance().counterTaskComp();
                logger.info( code + "在" + url + "成功, 评论数:" + cmtListSize );
                QqNewsUtil.log( newsModule.getName() + "\\" + code
                        + "成功!抓取评论总数:" + cmtListSize );
            }
        } catch ( Exception e )
        {
            e.printStackTrace();
        } finally
        {
            QqNewsUtil.recoverCmtFileAfterTask( this.newsModule, code );
        }
    }
}
