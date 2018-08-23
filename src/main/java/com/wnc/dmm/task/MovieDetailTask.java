
package com.wnc.dmm.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawl.spider.entity.Page;
import com.wnc.basic.BasicFileUtil;
import com.wnc.dmm.DmmConsts;
import com.wnc.dmm.DmmSpiderClient;
import com.wnc.dmm.DmmUtils;
import com.wnc.string.PatternUtil;

public class MovieDetailTask extends AbstractPageTask
{
    private String MOVIE_DETAIL_LOCATION;
    private String cid;
    boolean ignoreComplte = false;
    static
    {
        retryMap.put( MovieDetailTask.class,
                new ConcurrentHashMap<String, Integer>() );
    }

    public MovieDetailTask( String cid )
    {
        MAX_RETRY_TIMES = 20;
        this.cid = cid;
        this.url = DmmUtils.getDetailUrl( cid );
        this.proxyFlag = true;
        MOVIE_DETAIL_LOCATION = DmmUtils.getMovieDetailLocation( cid );
    }

    public void retry()
    {
        DmmSpiderClient.getInstance()
                .submitTask( new MovieDetailTask( this.cid ) );
    }

    @Override
    protected void handle( Page page ) throws Exception
    {
        Document documentResult = getDoc( page );
        if ( documentResult.select(
                "#mu > div > table > tbody > tr > td:nth-child(1) > table" )
                .size() > 0 )
        {
            BasicFileUtil.makeDirectory( MOVIE_DETAIL_LOCATION );

            grabBasicInfo( documentResult );
            grabPics( documentResult );
            grabCmt( documentResult );
        } else
        {
            retryMonitor( cid + " detail页面无内容!" );
            ignoreComplte = true;
        }
    }

    private void grabPics( Document documentResult )
    {
        JSONObject json = new JSONObject( true );
        json.put( "cid", cid );

        // #sample-image2 > img
        Elements links = documentResult.select( "#sample-image-block > a" );
        List<String> listS = new ArrayList<String>();
        List<String> listL = new ArrayList<String>();
        for ( Element link : links )
        {
            String smallPic = getAttr( link, "img", "src" );
            String dealPicUrl = DmmUtils.dealPicUrl( smallPic );
            listS.add( smallPic );
            listL.add( dealPicUrl );
        }

        String ps = getPsImg( documentResult );
        String pl = getPlImg( documentResult );
        json.put( "ps", ps );
        json.put( "pl", pl );
        json.put( "listS", listS );
        json.put( "listL", listL );

        BasicFileUtil.writeFileString(
                MOVIE_DETAIL_LOCATION + cid + "-pic-urls.txt",
                JSONObject.toJSONString( json, true ) + "\r\n", null, false );

    }

    //
    private String getPsImg( Document documentResult )
    {
        return getAttr( documentResult, "#package-src-" + this.cid, "src" );
    }

    private String getPlImg( Document documentResult )
    {
        return DmmUtils.dealPicUrl( getPsImg( documentResult ) );
    }

    private void grabBasicInfo( Document documentResult )
    {
        String title = getText( documentResult, "#title" );
        checkEmpty( "title", title );

        Element parse = documentResult.select(
                "#mu > div > table > tbody > tr > td:nth-child(1) > table" )
                .first();
        String publishDate = getMonoTExt( parse, "商品発売日：", "[/\\d]+" );
        String mvLength = getMonoTExt( parse, "収録時間：", "\\d+" );
        String mvCode = getMonoTExt( parse, "品番：", ".+" );

        List<String> performers = getLinkIds( parse, "出演者：" );
        List<String> director = getLinkIds( parse, "監督：" );
        List<String> series = getLinkIds( parse, "シリーズ：" );
        List<String> maker = getLinkIds( parse, "メーカー：" );
        List<String> operator = getLinkIds( parse, "レーベル：" );
        List<String> keywords = getLinkIds( parse, "ジャンル：" );
        checkBigSize( "director.size()  ", director.size(), 1 );
        checkBigSize( "series.size()  ", series.size(), 1 );
        checkBigSize( "maker.size()  ", maker.size(), 1 );
        checkBigSize( "operator.size()  ", operator.size(), 1 );

        String description = getDesc( documentResult );
        checkEmpty( "description", description );

        List<String> albums = new ArrayList<String>();
        Elements albumLinks = documentResult.select(
                "#mu > div > table > tbody > tr > td:nth-child(1) > div.mg-b20.lh4 > dl a" );
        for ( Element element : albumLinks )
        {
            String albumCode = PatternUtil.getLastPatternGroup(
                    element.attr( "href" ), "type=(.*?)/" );
            if ( StringUtils.isNotBlank( albumCode ) )
            {
                albums.add( albumCode );
            }
        }
        checkBigSize( "albums.size()  ", albums.size(), 1 );

        String score = getScore( documentResult );
        checkEmpty( "score", score );

        String cmtCounttext = getText( documentResult,
                "#review > div.d-review__container > div > div.d-review__ratings > div > p.d-review__evaluates" );

        String voteCount = pattern( cmtCounttext, "(\\d+)" );
        checkEmpty( "voteCount", voteCount );

        String cmtCount = pattern( cmtCounttext, "（(\\d+)件" );
        checkEmpty( "cmtCount", cmtCount );

        String loveCount = getText( documentResult,
                "#mu > div > table > tbody > tr > td:nth-child(1) > div.box-rank > p > span.tx-count" );
        checkEmpty( "txCount", loveCount );

        JSONObject json = new JSONObject( true );
        json.put( "title", title );
        json.put( "mvCode", mvCode );
        json.put( "publishDate", publishDate );
        json.put( "mvLength", mvLength );
        json.put( "score", score );
        json.put( "voteCount", voteCount );
        json.put( "cmtCount", cmtCount );
        json.put( "loveCount", loveCount );

        String ps = getPsImg( documentResult );
        String pl = getPlImg( documentResult );
        json.put( "ps", ps );
        json.put( "pl", pl );

        json.put( "performers", performers );
        json.put( "director", director );
        json.put( "series", series );
        json.put( "maker", maker );
        json.put( "operator", operator );
        json.put( "keywords", keywords );
        json.put( "albums", albums );
        json.put( "description", description );
        BasicFileUtil.writeFileString(
                MOVIE_DETAIL_LOCATION + cid + "-basic-info.txt",
                JSONObject.toJSONString( json, true ) + "\r\n", null, false );
    }

    private void grabCmt( Document documentResult )
    {
        Elements select = documentResult.select(
                "#review > div.d-review__container > div > div.d-review__with-comment > div.d-review__list > ul > li" );
        JSONObject commentsJson = new JSONObject( true );
        JSONArray jsonArr = new JSONArray();
        for ( Element element : select )
        {
            String title = getText( element, ".d-review__unit__title" );
            String score = getAttr( element, "span:nth-child(1)", "class" )
                    .replaceAll( "[^1-9]", "" );
            String reviewerId = PatternUtil.getFirstPatternGroup( element
                    .select( ".d-review__unit__reviewer > a" ).attr( "href" ),
                    "id=(\\d+)" );
            String cmtDate = PatternUtil.getLastPattern(
                    element.select( ".d-review__unit__postdate" ).text(),
                    "[/\\d]+" );
            String cmtText = element.select( ".fn-d-review__many-text" ).html();

            String voteText = element.select( ".d-review__unit__voted" ).text();
            String voteDesc = PatternUtil.getLastPattern( voteText, "\\d+" )
                    + "/" + PatternUtil.getFirstPattern( voteText, "\\d+" );

            JSONObject json = new JSONObject( true );
            json.put( "title", title );
            json.put( "score", score );
            json.put( "cmtDate", cmtDate );
            json.put( "reviewerId", reviewerId );
            json.put( "voteDesc", voteDesc );
            json.put( "cmtText", cmtText );

            jsonArr.add( json );

        }
        commentsJson.put( "cid", cid );
        commentsJson.put( "score", getScore( documentResult ) );
        commentsJson.put( "comments", jsonArr );
        BasicFileUtil.writeFileString(
                MOVIE_DETAIL_LOCATION + cid + "-comments.txt",
                JSONObject.toJSONString( commentsJson, true ) + "\r\n", null,
                false );
    }

    private String getDesc( Document documentResult )
    {
        Element first = documentResult.select(
                "#mu > div > table > tbody > tr > td:nth-child(1) > div.mg-b20.lh4" )
                .first();
        if ( first != null )
        {
            return first.ownText();
        }
        return "";
    }

    public String pattern( String value, String pattern )
    {
        return PatternUtil.getFirstPatternGroup( value, pattern );
    }

    public String getAttr( Element e, String select, String attr )
    {
        return e.select( select ).attr( attr );
    }

    public String getText( Element e, String select )
    {
        return e.select( select ).text();
    }

    @Override
    protected void complete( int type, String msg )
    {
        if ( this.ignoreComplte )
        {
            return;
        }

        super.complete( type, msg );
        DmmSpiderClient.getInstance().counterTaskComp();
        System.out.println( "任务完成:" + MOVIE_DETAIL_LOCATION );
        if ( type == COMPLETE_STATUS_SUCCESS )
        {
            BasicFileUtil.writeFileString(
                    DmmConsts.DETAIL_DIR + "suc-mdetail.txt",
                    cid + " MovieDetail - Suc! \r\n", null, true );
        } else
        {
            BasicFileUtil.writeFileString(
                    DmmConsts.DETAIL_DIR + "err-mdetail.txt",
                    cid + " MovieDetail - err:" + msg + "\r\n", null, true );
        }
    }

    @Override
    protected void errLogExp( Exception ex )
    {
        super.errLogExp( ex );
        ex.printStackTrace();
    }

    // 404还要继续
    protected void errLog404( Page page )
    {
        ignoreComplte = true;
        retryMonitor( "404 continue..." );
    }

    private String getScore( Document documentResult )
    {
        return pattern( getText( documentResult,
                "#review > div.d-review__container > div > div.d-review__ratings > div > p.d-review__average > strong" ),
                "([\\d\\.]+)" );
    }

    private List<String> getLinkIds( Element element, String filter )
    {
        List<String> ids = new ArrayList<String>();
        Elements trs = element.select( "tr" );
        for ( Element tr : trs )
        {
            Elements tds = tr.select( "td" );
            if ( tds.size() > 1 && filter.equals( tds.get( 0 ).text() ) )
            {
                tr.remove();
                Elements select = tds.get( 1 ).select( "a" );
                for ( Element element2 : select )
                {
                    ids.add( PatternUtil.getLastPatternGroup(
                            element2.attr( "href" ), "id=(\\d+)" ) );
                }
                break;
            }
        }
        return ids;
    }

    private String getMonoTExt( Element element, String filter, String pattern )
    {
        Elements trs = element.select( "tr" );
        for ( Element tr : trs )
        {
            Elements tds = tr.select( "td" );
            if ( tds.size() > 1 && filter.equals( tds.get( 0 ).text() ) )
            {
                tr.remove();
                return PatternUtil.getFirstPatternGroup( tds.get( 1 ).text(),
                        pattern );
            }
        }
        return "";
    }

    private void checkBigSize( String key, int size, int basicSize )
    {
        if ( size > basicSize )
        {
            check( key + " size is " + size + ", more than " + basicSize
                    + ".\r\n" );
        }
    }

    private void checkEmpty( String key, String value )
    {
        // System.out.println( key + ":" + value );
        if ( StringUtils.isBlank( value ) )
        {
            check( key + " is empty.\r\n" );
        }
    }

    private void check( String msg )
    {
        BasicFileUtil.writeFileString( DmmConsts.APP_DIR + "checkDetail.txt",
                cid + ": " + msg + "\r\n", null, true );
    }
}
