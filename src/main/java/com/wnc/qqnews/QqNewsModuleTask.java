
package com.wnc.qqnews;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import com.wnc.qqnews.service.QqDbService;
import com.wnc.sboot1.SpringContextUtils;
import org.apache.http.client.methods.HttpGet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawl.spider.entity.Page;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicFileUtil;
import com.wnc.wynews.model.NewsModule;
import com.wnc.wynews.utils.NewsPageUrlGenerator;

/**
 * 项目初始化, 拉取之前的新闻数据到本地 2018-07-26 12:00:00
 */
public class QqNewsModuleTask extends AbstractPageTask
{
    private NewsModule newsModule;
    private int pageIdx = 1;
    private QqDbService qqDbService = (QqDbService) SpringContextUtils.getContext().getBean("qqDbService");

    // 以首个任务开始请求接口时的时间为准, 而不是初始化提交到线程池时为准
    // 在重试和下一个任务时, beginSpyDate会一直流转下去
    private Date beginSpyDate;
    private boolean ignoreComp = false;
    static
    {
        retryMap.put( QqNewsModuleTask.class,
                new ConcurrentHashMap<String, Integer>() );
    }

    public QqNewsModuleTask( NewsModule newsModule )
    {
        this( newsModule, 0, null );
    }

    public QqNewsModuleTask( NewsModule newsModule,int pageIdx,
            Date beginSpyDate )
    {
        this.MAX_RETRY_TIMES = 20;
        pageEncoding = "GBK";

        this.newsModule = newsModule;
        this.pageIdx = pageIdx;
        if ( beginSpyDate != null )
        {
            this.beginSpyDate = beginSpyDate;
        }

        this.url = getUrl( newsModule, pageIdx );

        this.proxyFlag = true;

        request = new HttpGet( url );
    }

    @Override
    public void run()
    {
        if ( pageIdx == 0 )
        {
            this.beginSpyDate = new Date();
        }
        super.run();
    }

    public QqNewsModuleTask setMaxRetryTimes( int tm )
    {
        this.MAX_RETRY_TIMES = tm;
        return this;
    }

    @Override
    protected void retry()
    {
        QqSpiderClient.getInstance().submitTask( new QqNewsModuleTask(
                this.newsModule, pageIdx, beginSpyDate ) );
    }

    @Override
    protected void handle( Page page ) throws Exception
    {
        QqNewsUtil.log( newsModule.getName() + "任务完成-" + pageIdx );
        // System.out.println( page.getHtml() );
        JSONArray jsonArray = QqNewsUtil.getNewsList( page.getHtml() );
        boolean mustPullMore = judgeIfGetMore( jsonArray );
        // 一个模块最多抓20页新闻
        if ( !newsModule.isOnePageModule() && mustPullMore && pageIdx < 20 )
        {
            // 下一页任务
            nextJob();
            ignoreComp = true;
        }
        // 处理当前页的数据
        dealCurrentPageNews( jsonArray );
        // 如果不需要加载下一页, 则完成任务, 自动调用complete
        if ( !mustPullMore )
        {
            ignoreComp = false;
        }

    }

    // 404 可能还要继续
    protected void errLog404( Page page )
    {
        // 假的404. 和代理有关, 重试
        retryMonitor( page.getStatusCode() + " continue..." );
        ignoreComp = true;
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
    protected void complete( int type, String msg )
    {
        if ( ignoreComp )
        {
            return;
        }
        try
        {
            super.complete( type, msg );

            String name = newsModule.getName();

            if ( type != COMPLETE_STATUS_SUCCESS )
            {
                QqNewsUtil.err( newsModule,
                        name + "在" + url + "失败, 失败原因:" + msg );
            } else
            {
                // 任务完成数加1
                QqSpiderClient.getInstance().counterTaskComp();
                // 通过循环试探得知本页已是结束页, 取上次成功页
                String preUrl = getUrl( this.newsModule, pageIdx - 1 );
                QqNewsUtil.log( name + "在" + preUrl + "结束任务!" );
                // 写最后任务时间
                BasicFileUtil.writeFileString(
                        QqConsts.MODULE_HISTORY + newsModule.getName() + ".txt",
                        BasicDateUtil.getLocaleDate2String( this.beginSpyDate ),
                        null, false );

            }
        } catch ( Exception e )
        {
            e.printStackTrace();
        } finally
        {

        }
    }

    private String getUrl( NewsModule newsModule, int pageIdx )
    {
        if ( newsModule.getMore() == null )
        {
            return newsModule.getUrl();
        }
        if ( newsModule.isOnePageModule() )
        {
            return newsModule.getMore();
        }
        return NewsPageUrlGenerator.generatorPageUrl( newsModule.getMore(),
                pageIdx );
    }

    private void dealCurrentPageNews( JSONArray jsonArray )
    {
        if ( jsonArray != null && !jsonArray.isEmpty() )
        {
            for ( int i = 0; i < jsonArray.size(); i++ )
            {
                JSONObject news = jsonArray.getJSONObject( i );

                qqDbService.singleNews(news);

                // 采用评论id做code
                String code = news.getString( "comment_id" );
                if ( QqModuleIdsManager.addModuleNews( newsModule.getName(),
                        code ) )
                {
                    // news_list记录当前新闻信息, 如果list中重复出现多次,那么说明该新闻一直没抓到评论或者评论本身为空
                    BasicFileUtil.writeFileString(
                            QqConsts.NEWS_LIST_DIR + newsModule.getName()
                                    + ".txt",
                            JSONObject.toJSONString( news ) + "\r\n", null,
                            true );
                    // 将新闻进行日期分类, 如果日期文件中重复出现多次,那么说明该新闻一直没抓到评论或者评论本身为空
                    String dateStr = QqNewsUtil.getFormatTimeStr(
                            news.getString( "publish_time" ) );
                    String day = dateStr.substring( 0, 10 );
                    BasicFileUtil.writeFileString(
                            QqConsts.NEWS_DAY_DIR + day + ".txt",
                            code + "[" + newsModule.getName() + "]" + dateStr
                                    + "\r\n",
                            null, true );
                    if ( news.getIntValue( "comment_num" ) > 0
                            && QqNewsUtil.isValidCode( code ) )
                    {
                        System.out.println(
                                newsModule.getName() + " 开始抓评论" + code );
                        QqNewsUtil.backupCmtFileBeforeTask( this.newsModule,
                                code );
                        QqSpiderClient.getInstance().submitTask(
                                new QqCmtTask( newsModule, code ) );
                    }
                }
            }

        }
    }

    /***
     * @Description 腾讯新闻无序, 每次重新查所有页吧
     * @Date 2018/7/28 0:16
     * @Param list
     * @Return boolean
     */
    private boolean judgeIfGetMore( JSONArray jsonArray )
    {
        if ( jsonArray == null || jsonArray.isEmpty() )
        {
            return false;
        } else
        {
            return true;
        }
    }

    private void nextJob()
    {
        QqSpiderClient.getInstance().submitTask( new QqNewsModuleTask(
                this.newsModule, ++pageIdx, beginSpyDate ) );
    }

}
