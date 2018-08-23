
package com.wnc.dmm.task;

import java.util.concurrent.ConcurrentHashMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.crawl.spider.entity.Page;
import com.wnc.basic.BasicFileUtil;
import com.wnc.dmm.DmmConsts;
import com.wnc.dmm.DmmSpiderClient;
import com.wnc.dmm.DmmUtils;
import com.wnc.string.PatternUtil;

public class DmmHotPageTask extends AbstractPageTask
{
    private int curPage;
    boolean ignoreComplte = false;
    static
    {
        retryMap.put( DmmHotPageTask.class,
                new ConcurrentHashMap<String, Integer>() );
    }

    public DmmHotPageTask( int curPage )
    {
        this.curPage = curPage;
        this.url = DmmUtils.getHotPageUrl( curPage );
        this.proxyFlag = true;
    }

    @Override
    protected void retry()
    {
        DmmSpiderClient.getInstance()
                .submitTask( new DmmHotPageTask( this.curPage ) );
    }

    @Override
    protected void handle( Page page ) throws Exception
    {
        Document documentResult = getDoc( page );
        Elements select = documentResult.select( "#list > li > div" );
        String url = null;
        String count = null;
        String cid = null;
        for ( Element element : select )
        {
            url = element.select( "p > a" ).attr( "href" );
            count = element.select( "div.value" ).text().replaceAll( "[,回再生]",
                    "" );
            BasicFileUtil.writeFileString( DmmConsts.CID_LOG,
                    url + " " + count + "\r\n", null, true );

            // 抓视频信息
            cid = PatternUtil.getLastPatternGroup( url, "cid=(.+)/" );
            DmmSpiderClient.getInstance()
                    .submitTask( new MovieParamTask( cid ) );
        }
        if ( select.size() == 0 )
        {
            retryMonitor( "第" + curPage + "页沒有内容!" );
            ignoreComplte = true;
        }
    }

    @Override
    protected void complete( int type, String msg )
    {
        if ( this.ignoreComplte )
        {
            return;
        }

        super.complete( type, msg );

        if ( type == COMPLETE_STATUS_SUCCESS )
        {
            BasicFileUtil.writeFileString( DmmConsts.SUC_LOG,
                    curPage + "页  HotPage 结束" + "\r\n", null, true );
        } else
        {
            BasicFileUtil.writeFileString( DmmConsts.ERR_LOG,
                    curPage + "页  HotPage - err:" + msg + "\r\n", null, true );
        }
    }
}
