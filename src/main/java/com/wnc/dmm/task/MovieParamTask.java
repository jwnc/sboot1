
package com.wnc.dmm.task;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.jsoup.nodes.Document;

import com.crawl.spider.entity.Page;
import com.wnc.basic.BasicFileUtil;
import com.wnc.dmm.DmmConsts;
import com.wnc.dmm.DmmSpiderClient;
import com.wnc.dmm.DmmUtils;
import com.wnc.string.PatternUtil;

public class MovieParamTask extends AbstractPageTask
{
    private String cid;
    boolean ignoreComplte = false;
    static
    {
        retryMap.put( MovieParamTask.class,
                new ConcurrentHashMap<String, Integer>() );
    }

    public MovieParamTask( String cid )
    {
        this.cid = cid;
        this.url = DmmUtils.getMovieUrl( cid );
        this.proxyFlag = true;
    }

    public void retry()
    {
        DmmSpiderClient.getInstance()
                .submitTask( new MovieParamTask( this.cid ) );
    }

    @Override
    protected void handle( Page page ) throws Exception
    {
        Document documentResult = getDoc( page );
        String docStr = documentResult.toString();
        List<String> allPatternGroup = PatternUtil.getAllPatternGroup( docStr,
                "\"src\":\"(.*?)\"" );
        String last = null;
        // 保留所有视频源
        for ( String string : allPatternGroup )
        {
            // 简单去重
            if ( string.equals( last ) )
            {
                continue;
            }
            last = string;
            System.out.println( string.replace( "\\", "" ) );
            BasicFileUtil.writeFileString( DmmConsts.VIDEO_ALL_LOG,
                    string.replace( "\\", "" ) + "\r\n", null, true );
        }
        // 保留唯一副本
        if ( allPatternGroup.size() > 0 )
        {
            BasicFileUtil.writeFileString( DmmConsts.VIDEO_LOW_LOG,
                    allPatternGroup.get( 0 ).replace( "\\", "" ) + "\r\n", null,
                    true );
            BasicFileUtil.writeFileString( DmmConsts.VIDEO_HIGH_LOG,
                    allPatternGroup.get( allPatternGroup.size() - 1 )
                            .replace( "\\", "" ) + "\r\n",
                    null, true );
        } else
        {
            retryMonitor( cid + " getMp4Url 沒有视频!" );
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
                    cid + " getMp4Url 结束" + "\r\n", null, true );
        } else
        {
            BasicFileUtil.writeFileString( DmmConsts.ERR_LOG,
                    cid + " getMp4Url - err:" + msg + "\r\n", null, true );
        }
    }

    @Override
    protected void errLogExp( Exception ex )
    {
        super.errLogExp( ex );
        ex.printStackTrace();
    }
}
