
package com.wnc.wynews.spy;

import java.util.Date;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.crawl.spider.entity.Page;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicFileUtil;
import com.wnc.wynews.consts.ModuleIdsManager;
import com.wnc.wynews.consts.WyConsts;
import com.wnc.wynews.helper.WySpiderClient;
import com.wnc.wynews.model.News;
import com.wnc.wynews.model.NewsModule;
import com.wnc.wynews.parser.WyParser;
import com.wnc.wynews.utils.NewsPageUrlGenerator;
import com.wnc.wynews.utils.WyNewsUtil;

/**
 * 项目初始化, 拉取之前的新闻数据到本地 2018-07-26 12:00:00
 */
public class WyNewsModuleTask extends AbstractPageTask
{
    private static Logger logger = Logger.getLogger( WyNewsModuleTask.class );
    private NewsModule newsModule;
    private int pageIdx = 1;

    // 以首个任务开始请求接口时的时间为准, 而不是初始化提交到线程池时为准
    // 在重试和下一个任务时, beginSpyDate会一直流转下去
    private Date beginSpyDate;
    private boolean ignoreComp = false;
    // 结束分为循环试探得到404而结束, 和比较最后时间两种
    private boolean completeWithCompareTime = false;

    public WyNewsModuleTask( NewsModule newsModule,int pageIdx,
            Date beginSpyDate )
    {
        this.MAX_RETRY_TIMES = 20;
        pageEncoding = "GBK";

        this.newsModule = newsModule;
        this.pageIdx = pageIdx;

        this.url = getUrl( newsModule, pageIdx );

        if ( beginSpyDate == null )
        {
            this.beginSpyDate = new Date();
        } else
        {
            this.beginSpyDate = beginSpyDate;
        }
        this.proxyFlag = true;

        request = new HttpGet( url );
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

    public WyNewsModuleTask setMaxRetryTimes( int tm )
    {
        this.MAX_RETRY_TIMES = tm;
        return this;
    }

    @Override
    protected void retry()
    {
        WySpiderClient.getInstance().submitTask( new WyNewsModuleTask(
                this.newsModule, pageIdx, beginSpyDate ) );
    }

    @Override
    protected void handle( Page page ) throws Exception
    {
        if ( page.getHtml().contains( "<title>Index_Page</title>" ) )
        {
            // 重试
            retryMonitor( "Page Error ..." );
            ignoreComp = true;
        } else
        {
            // BasicFileUtil.writeFileString(
            // WyConsts.NEWS_MENU_DIR + "2018-07-28\\" + newsModule.getName() +
            // "-" + pageIdx + ".txt",
            // page.getHtml() + "\r\n", null, true);
            WyNewsUtil.log( newsModule.getName() + "任务完成-" + pageIdx );
            WyParser parser = WyNewsUtil.getNewsParser( this.newsModule,
                    page.getHtml() );
            List<News> list = parser.getNewsList();
            boolean mustPullMore = judgeIfGetMore( list );
            if ( !newsModule.isOnePageModule() && mustPullMore )
            {
                // 下一页任务
                nextJob();
                ignoreComp = true;
            }
            // 处理当前页的数据
            dealCurrentPageNews( list );
            // 如果不需要加载下一页, 则完成任务, 自动调用complete
            if ( !mustPullMore )
            {
                completeWithCompareTime = true;
                ignoreComp = false;
            }
        }

    }

    private void dealCurrentPageNews( List<News> list )
    {
        if ( !list.isEmpty() )
        {
            for ( News news : list )
            {
                // 判断本地是否已存在新闻, 已存在的新闻就不用处理了
                if ( ModuleIdsManager.addModuleNews( newsModule.getName(),
                        WyNewsUtil.getNewsCode( news ) ) )
                {
                    // news_list记录当前新闻信息, 如果list中重复出现多次,那么说明该新闻一直没抓到评论或者评论本身为空
                    BasicFileUtil.writeFileString(
                            WyConsts.NEWS_LIST_DIR + newsModule.getName()
                                    + ".txt",
                            JSONObject.toJSONString( news ) + "\r\n", null,
                            true );
                    // 将新闻进行日期分类, 如果日期中重复出现多次,那么说明该新闻一直没抓到评论或者评论本身为空
                    String dateStr = WyNewsUtil
                            .getFormatTimeStr( news.getTime() );
                    String day = dateStr.substring( 0, 10 );
                    String code = WyNewsUtil.getNewsCode( news );
                    if ( WyNewsUtil.isValidCode( code ) )
                    {
                        BasicFileUtil
                                .writeFileString(
                                        WyConsts.NEWS_DAY_DIR + day + ".txt",
                                        code + "[" + newsModule.getName() + "]"
                                                + dateStr + "\r\n",
                                        null, true );
                        System.out.println(
                                newsModule.getName() + " 开始抓评论" + code );
                        // WyNewsUtil.log(
                        // this.newsModule.getName() + " 开始抓评论" + code);
                        WyNewsUtil.backupCmtFileBeforeTask( this.newsModule,
                                code );
                        WySpiderClient.getInstance().submitTask(
                                new WyCmtTask( newsModule, code, 0 ) );
                    }
                }
            }

        }
    }

    /***
     * @Description 小于当前页的最后一条新闻, 则继续加载
     * @Date 2018/7/28 0:16
     * @Param list
     * @Return boolean
     */
    private boolean judgeIfGetMore( List<News> list )
    {
        if ( list.isEmpty() )
        {
            return false;
        }
        String time = WyNewsUtil
                .getFormatTimeStr( list.get( list.size() - 1 ).getTime() );
        if ( BasicDateUtil.getLocaleDate2String( newsModule.getLastSpyDate() )
                .compareTo( time ) <= 0 )
        {
            return true;
        }

        return false;
    }

    private void nextJob()
    {
        WySpiderClient.getInstance().submitTask( new WyNewsModuleTask(
                this.newsModule, ++pageIdx, beginSpyDate ) );
    }

    // 404 可能还要继续
    protected void errLog404( Page page )
    {
        if ( page.getHtml() != null && page.getHtml().contains( "一起帮孩子回家" ) )
        {
            // 真的404. 由网易返回. 表示已没有当前页
            complete( AbstractPageTask.COMPLETE_STATUS_SUCCESS, "" );
        } else
        {
            // 假的404. 和代理有关, 重试
            retryMonitor( "404 continue..." );
        }
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

            String name = newsModule.getName();

            if ( type != COMPLETE_STATUS_SUCCESS )
            {
                WyNewsUtil.err( newsModule,
                        name + "在" + url + "失败, 失败原因:" + msg );
            } else
            {
                // 任务完成数加1
                WySpiderClient.getInstance().counterTaskComp();
                if ( !completeWithCompareTime )
                {
                    // 通过循环试探得知本页已是结束页, 取上次成功页
                    String preUrl = getUrl( this.newsModule, pageIdx - 1 );
                    WyNewsUtil.log( name + "在" + preUrl + "结束任务!" );
                } else
                {
                    WyNewsUtil.log( name + "在" + url + "结束任务!" );
                }
                // 写最后任务时间
                BasicFileUtil.writeFileString(
                        WyConsts.MODULE_HISTORY + newsModule.getName() + ".txt",
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

}
