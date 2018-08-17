
package com.wnc.qqnews;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawl.spider.entity.Page;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.qqnews.user.UserStat;
import com.wnc.qqnews.user.UserStatFileUtil;

/**
 * 找出两个月之间的
 */
public class QqUserTask extends AbstractPageTask
{
    private static final int PAGE_SIZE = 30;
    private static String CMT_URL_FORMAT = "http://coral.qq.com/user/%d/comment/v2?pageflag=1&reqnum="
            + PAGE_SIZE + "&needparent=1&commentid=%s&_=%d";
    private static Logger logger = Logger.getLogger( QqUserTask.class );
    private String cursor;
    private int uid;
    private UserStat userStat;
    private int uCmtListSize;

    private boolean ignoreComp = false;
    /**
     * 需要大量的文章和用户数据的时候才开启
     */
    private boolean pullBigData = false;
    static
    {
        retryMap.put( QqUserTask.class,
                new ConcurrentHashMap<String, Integer>() );
    }

    public QqUserTask( UserStat userStat,boolean pullBigData )
    {
        this( userStat, "0", pullBigData );
    }

    public QqUserTask( UserStat userStat,String cursor,boolean pullBigData )
    {
        this.userStat = userStat;
        this.uid = userStat.getId();
        this.cursor = cursor;

        this.url = String.format( CMT_URL_FORMAT, this.uid, this.cursor,
                System.currentTimeMillis() );
        this.proxyFlag = true;

        this.MAX_RETRY_TIMES = 20;

        request = new HttpGet( url );
    }

    public QqUserTask setMaxRetryTimes( int tm )
    {
        this.MAX_RETRY_TIMES = tm;
        return this;
    }

    @Override
    public void run()
    {
        // 只有第一页运行的时候才设置lastSpayTime
        if ( isFirstPage() )
        {
            this.userStat
                    .setLastSpyTime( (int)(System.currentTimeMillis() / 1000) );
        }
        super.run();
    }

    @Override
    protected void retry()
    {
        QqSpiderClient.getInstance()
                .submitTask( new QqUserTask( userStat, cursor, pullBigData ) );
    }

    @Override
    protected void handle( Page page ) throws Exception
    {
        int errCode = 0;
        boolean hasNext = false;

        String res = page.getHtml();

        JSONObject parseObject = JSONObject.parseObject( res );
        errCode = parseObject.getIntValue( "errCode" );
        if ( errCode == 0 )
        {
            JSONObject dataObject = parseObject.getJSONObject( "data" );
            hasNext = dataObject.getBooleanValue( "hasnext" );

            // 不考虑评论为0的情况了
            // int intValue = dataObject.getIntValue( "retnum" );
            outputUserData( dataObject );
            uCmtListSize = dataObject.getIntValue( "total" );
            boolean mustPullMore = decideMore( dataObject );
            if ( pullBigData && hasNext && mustPullMore )
            {
                nextJob( dataObject.getString( "last" ) );
                ignoreComp = true;
            }
            // else if ( "false".equals( dataObject.getString( "first" ) ) )
            // {
            // // 如果是首页, 且列表为空
            // if ( intValue == 0 )
            // {
            // // 无记录, 记作错误日志
            // complete( 99, "用户评论列表空白" );
            // ignoreComp = true;
            // }
            // }
        } else
        {
            complete( errCode + 10000, "出错:errCode=" + errCode );
            ignoreComp = true;
        }

    }

    /**
     * 根据当前页评论时间判断是否下一页
     * 
     * @param dataObject
     * @return
     */
    private boolean decideMore( JSONObject dataObject )
    {
        JSONArray commentArr = dataObject.getJSONArray( "comments" );
        int size = commentArr.size();
        if ( size > 0 )
        {
            int pageLastTime = commentArr.getJSONObject( size - 1 )
                    .getIntValue( "time" );
            // 如果当前页的数据比较新, 则继续加载下一页
            if ( pageLastTime > userStat.getLastSpyTime() )
            {
                return true;
            }
        }
        return false;
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
                logger.error( "用户" + uid + "在" + url + "失败, 失败原因:" + msg );
                QqNewsUtil.errUser( "用户:" + uid + "在" + url + "失败, 失败原因:" + msg
                        + " 应抓取总数:" + uCmtListSize );
            } else
            {
                logger.info(
                        "用户" + uid + "在" + url + "成功, 评论数:" + uCmtListSize );
                QqNewsUtil.log( uid + "成功!抓取用户评论总数:" + uCmtListSize + "耗时:"
                        + (System.currentTimeMillis() / 1000
                                - userStat.getLastSpyTime()) );
            }
        } catch ( Exception e )
        {
            e.printStackTrace();
        } finally
        {
            // 任务完成数加1
            QqSpiderClient.getInstance().counterTaskComp();
            // 写UserStat数据
            try
            {
                UserStatFileUtil.write( userStat );
            } catch ( IOException e )
            {
                e.printStackTrace();
            }
        }

    }

    /**
     * 输出当页用户涉及到的数据
     * 
     * @param dataObject
     */
    private void outputUserData( JSONObject dataObject )
    {
        if ( isFirstPage() )
        {

            rebuildUserStat( dataObject );

        }
        // 需要爬大量文章和用户数据的时候才开启
        if ( pullBigData )
        {
            outputArticle( dataObject );
            outputUser( dataObject );
        }
    }

    /**
     * 输出评论设计的所有用户信息
     * 
     * @param dataObject
     */
    private void outputUser( JSONObject dataObject )
    {
        JSONObject usersJO = dataObject.getJSONObject( "users" );
        for ( String key : usersJO.keySet() )
        {
            JSONObject userJO = usersJO.getJSONObject( key );
            System.out.println( userJO.getString( "nick" ) + " / "
                    + userJO.getString( "userid" ) );
            // 输出user到user目录
            QqUserManager.addAndWriteUser( userJO );
        }
    }

    /**
     * 输出评论设计的所有文章信息
     * 
     * @param dataObject
     */
    private void outputArticle( JSONObject dataObject )
    {
        JSONObject articlesJO = dataObject.getJSONObject( "articles" );
        for ( String key : articlesJO.keySet() )
        {
            JSONObject articleJO = articlesJO.getJSONObject( key );
            System.out.println( articleJO.getString( "title" ) );
            // 输出文章到articles文件
            QqArticleManager.addAndWriteArticle( articleJO );
        }
    }

    private boolean isFirstPage()
    {
        return "0".equals( cursor );
    }

    /**
     * 设置UserStat的相关属性
     * 
     * @param umetaJO
     */
    private void rebuildUserStat( JSONObject dataObject )
    {
        JSONObject umetaJO = dataObject.getJSONObject( "usermeta" );
        QqUserMetaManager.addAndWriteUserMeta( umetaJO );
        this.userStat.setOrieffcommentnum(
                umetaJO.getIntValue( "orieffcommentnum" ) );
        this.userStat.setRepeffcommentnum(
                umetaJO.getIntValue( "repeffcommentnum" ) );
        this.userStat.setUpnum( umetaJO.getIntValue( "upnum" ) );
        // 设置最后的活动时间
        this.userStat.setLastActiveTime( getLastActiveTime( dataObject ) );
    }

    /**
     * 获取最后一条评论的时间
     * 
     * @param dataObject
     * @return
     */
    private int getLastActiveTime( JSONObject dataObject )
    {
        JSONArray commentArr = dataObject.getJSONArray( "comments" );
        if ( commentArr.size() > 0 )
        {
            return commentArr.getJSONObject( 0 ).getIntValue( "time" );
        }
        return 0;
    }

    private void nextJob( String nextCursor )
    {
        QqSpiderClient.getInstance().submitTask(
                new QqUserTask( userStat, nextCursor, pullBigData ) );
    }
}
