
package com.wnc.dmm.task;

import java.util.concurrent.ConcurrentHashMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.crawl.spider.SpiderHttpClient;
import com.crawl.spider.entity.Page;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicFileUtil;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.string.PatternUtil;

public class TagMoviesTask extends AbstractPageTask
{
    public static final String TAG_MOVIES_HEAD_FORMT = "http://www.dmm.co.jp/litevideo/-/list/=/article=keyword/id=%d/";
    public static final String TAG_MOVIES_FORMT = TAG_MOVIES_HEAD_FORMT
            + "page=%d/";
    private int tagId;
    private int curPage;
    boolean ignoreComplte = false;
    static
    {
        retryMap.put( TagMoviesTask.class,
                new ConcurrentHashMap<String, Integer>() );
    }

    public TagMoviesTask( int tagId,int curPage )
    {
        this.MAX_RETRY_TIMES = 20;
        this.tagId = tagId;
        this.curPage = curPage;
        this.proxyFlag = true;
        this.url = String.format(
                curPage == 1 ? TAG_MOVIES_HEAD_FORMT : TAG_MOVIES_FORMT, tagId,
                curPage );

    }

    @Override
    protected void retry()
    {
        SpiderHttpClient.getInstance().getNetPageThreadPool()
                .execute( new TagMoviesTask( tagId, curPage ) );

    }

    @Override
    protected void handle( Page page ) throws Exception
    {
        Document doc = getDoc( page );

        int maxPage = getMaxPage( doc );
        if ( maxPage == 0 )
        {
            // ProxyPool.proxyQueue.remove( currentProxy );
            BasicFileUtil.writeFileString( tagId + "err-log.txt",
                    doc.toString() + "\r\n", null, false );
            retryMonitor( this.url + "没有数据!" );
            ignoreComplte = true;
        } else
        {
            outputCids( doc );

            if ( curPage == 1 )
            {
                BasicFileUtil.writeFileString( "tag-log.txt",
                        "TagId:" + tagId + " Pages:" + maxPage + "\r\n", null,
                        true );

                if ( maxPage > 1 )
                {
                    for ( int i = 2; i <= maxPage; i++ )
                    {
                        SpiderHttpClient.getInstance().getNetPageThreadPool()
                                .execute( new TagMoviesTask( tagId, i ) );
                    }
                }
            }
        }
    }

    private void outputCids( Document doc )
    {
        Elements select = doc.select( "#list > li > div" );

        for ( Element element : select )
        {
            String url = element.select( "p > a" ).attr( "href" );
            String count = element.select( "div.value" ).text()
                    .replaceAll( "[,回再生]", "" );
            // String cid = PatternUtil.getLastPatternGroup( url, "cid=(.+)/" );
            BasicFileUtil.writeFileString( tagId + "-cids.txt",
                    url + " " + count + "\r\n", null, true );
        }

    }

    private int getMaxPage( Document doc )
    {
        Elements select = doc
                .select( ".list-boxcaptside.list-boxpagenation > ul > li > a" );
        if ( select.size() > 0 )
        {
            if ( select.last().text().contains( "最後へ" ) )
            {
                return BasicNumberUtil.getNumber( PatternUtil.getLastPattern(
                        select.last().attr( "href" ), "\\d+" ) );
            } else if ( select.last().text().contains( "次へ" ) )
            {
                return BasicNumberUtil.getNumber( PatternUtil.getLastPattern(
                        select.get( select.size() - 2 ).attr( "href" ),
                        "\\d+" ) );
            } else
            {
                return 1;
            }
        } else
        {
            Elements select2 = doc
                    .select( ".list-boxcaptside.list-boxpagenation" );
            if ( select2.size() > 0
                    && select2.first().select( " > ul > li" ).size() == 1 )
            {
                // 只有一页的时候, 没有a标签, 分页在页面上会有重复的情况
                // .list-boxcaptside.list-boxpagenation
                return 1;
            }
        }
        return 0;
    }

    @Override
    protected void complete( int type, String msg )
    {
        if ( ignoreComplte )
        {
            return;
        }
        super.complete( type, msg );
        if ( type != COMPLETE_STATUS_SUCCESS )
        {
            BasicFileUtil.writeFileString( "err.txt",
                    this.tagId + " 失败:" + msg + "\r\n", null, true );
        }
    }

    @Override
    protected void errLogExp( Exception ex )
    {
        super.errLogExp( ex );
        ex.printStackTrace();
    }
}
