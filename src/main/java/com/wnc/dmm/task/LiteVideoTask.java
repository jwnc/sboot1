
package com.wnc.dmm.task;

import java.util.concurrent.ConcurrentHashMap;

import org.jsoup.nodes.Document;

import com.crawl.spider.entity.Page;
import com.wnc.basic.BasicFileUtil;
import com.wnc.dmm.DmmConsts;
import com.wnc.dmm.DmmSpiderClient;
import com.wnc.string.PatternUtil;

public class LiteVideoTask extends AbstractPageTask
{
    static
    {
        retryMap.put( LiteVideoTask.class,
                new ConcurrentHashMap<String, Integer>() );
    }

    public LiteVideoTask( String url )
    {
        this.url = url;
        this.proxyFlag = true;
    }

    @Override
    protected void retry()
    {
        DmmSpiderClient.getInstance()
                .submitTask( new LiteVideoTask( this.url ) );
    }

    @Override
    protected void handle( Page page ) throws Exception
    {
        Document documentResult = getDoc( page );
        if ( documentResult.select( "#product > a" ).size() > 0 )
        {
            String href = documentResult.select( "#product > a" )
                    .attr( "href" );
            String cid = PatternUtil.getLastPatternGroup( href, "cid=(.+)/" );
            String urlFull = DmmConsts.DMM_DOMAIN + href;
            if ( href.startsWith( "/mono/" ) )
            {
                DmmSpiderClient.getInstance()
                        .submitTask( new MovieDetailMonoTask( cid, urlFull ) );
            } else if ( href.startsWith( "/digital/" ) )
            {
                // 此时可能是一些自定义的href, 不能用默认的url规则生成
                DmmSpiderClient.getInstance()
                        .submitTask( new MovieDetailTask( cid, urlFull ) );
            } else if ( href.startsWith( "/rental/" ) )
            {
                DmmSpiderClient.getInstance().submitTask(
                        new MovieDetailRentalTask( cid, urlFull ) );
            } else
            {
                BasicFileUtil.writeFileString(
                        DmmConsts.DETAIL_DIR + "err-mdetail-unknown-page.txt",
                        this.url + " 获取:[" + href + "]为未知的解析类型\r\n", null,
                        true );
            }
        } else
        {
            if ( documentResult.title().contains( "地域からご利用" )
                    && currentProxy != null )
            {
                // remove invalid address proxy, 父类的finally就不会添加到代理池中
                currentProxy = null;
            }
            retryMonitor( url + " 代理错误, 地址不支持DMM!" );
        }
    }

    @Override
    protected void errLogExp( Exception ex )
    {
        super.errLogExp( ex );
        String pStr = currentProxy == null ? "null"
                : currentProxy.getProxyStr();
        System.err.println( this.url + " / proxy:" + pStr );
        ex.printStackTrace();
    }

    // 404还要继续
    protected void errLog404( Page page )
    {
        retryMonitor( "404 continue..." );
    }

    @Override
    protected void complete( int type, String msg )
    {
        // Do Nothine
    }

}
