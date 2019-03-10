
package com.wnc.dmm.task;

import java.lang.reflect.Constructor;
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
    protected int retryMode = 0; // -1:外部动态链接, 确定的. 0:videoa 1: mono, 2:videoc
                                 // 3:补全数字, 3+:videoa
                                 // 只有mono页的解析方式不一样
    protected boolean retry404ChangeMode = false;

    protected String MOVIE_DETAIL_LOCATION;
    protected String cid;
    boolean ignoreComplte = false;
    static
    {
        retryMap.put( MovieDetailTask.class,
                new ConcurrentHashMap<String, Integer>() );
    }

    public MovieDetailTask( String cid )
    {
        this( cid, 0 );
    }

    public MovieDetailTask( String cid,String url )
    {
        this( cid, -1 );
        this.url = url;
    }

    public MovieDetailTask( String cid,int retryMode )
    {
        this.cid = cid;
        this.retryMode = retryMode;
        MAX_RETRY_TIMES = 5;
        this.url = getUrl( cid, retryMode );
        this.proxyFlag = true;
        MOVIE_DETAIL_LOCATION = DmmUtils.getMovieDetailLocation( cid );
    }

    private String getUrl( String cid, int retryMode )
    {
        return DmmUtils.getDetailRetryUrl( cid, retryMode );
    }

    public void retry()
    {

        // -1表示动态的链接, 无法随机试探修改
        if ( retryMode == -1 )
        {
            try
            {
                DmmSpiderClient.getInstance().submitTask( getClone() );
            } catch ( Exception e )
            {
                e.printStackTrace();
            }
            return;
        }

        if ( retry404ChangeMode )
        {
            // 404情况换一种url重试
            retryMode++;
        }
        if ( retryMode == 1 )
        {
            DmmSpiderClient.getInstance()
                    .submitTask( new MovieDetailMonoTask( this.cid ) );
        } else
        {
            DmmSpiderClient.getInstance()
                    .submitTask( new MovieDetailTask( this.cid, retryMode ) );
        }
    }

    protected Runnable getClone() throws Exception
    {
        Class clazz = this.getClass();
        Constructor c = clazz.getDeclaredConstructor( String.class,
                String.class );// 获取有参构造
        return (Runnable)c.newInstance( cid, url ); // 通过有参构造创建对象
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
            if ( documentResult.title().contains( "地域からご利用" )
                    && currentProxy != null )
            {
                // remove invalid address proxy, 父类的finally就不会添加到代理池中
                currentProxy = null;
            }
            retryMonitor( cid + " 代理错误, 地址不支持DMM!" );
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
        return getAttr( documentResult, "img[id*=package-src-]", "src" );
    }

    private String getPlImg( Document documentResult )
    {
        return DmmUtils.dealPicUrl( getPsImg( documentResult ) );
    }

    private void grabBasicInfo( Document documentResult )
    {
        String title = getTitle( documentResult );
        checkEmpty( "title", title );

        Element parse = documentResult.select(
                "#mu > div > table > tbody > tr > td:nth-child(1) > table" )
                .first();
        String publishDate = getPublishDate( parse );
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

    protected String getTitle( Document documentResult )
    {
        return getText( documentResult, "#title" );
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

    protected String getPublishDate( Element element )
    {
        return getMonoTExt( element, "商品発売日：", "[/\\d]+" );
    }

    protected String getDesc( Document documentResult )
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
        String logHead = cid + " / " + retryMode + " / " + url
                + " MovieDetail - ";
        if ( type == COMPLETE_STATUS_SUCCESS )
        {
            System.out.println( "任务完成:" + MOVIE_DETAIL_LOCATION );
            BasicFileUtil.writeFileString(
                    DmmConsts.DETAIL_DIR + "suc-mdetail.txt",
                    logHead + "Suc!\r\n", null, true );
        } else
        {
            System.err
                    .println( "任务失败,retryMode:" + retryMode + " " + this.url );
            BasicFileUtil.writeFileString(
                    DmmConsts.DETAIL_DIR + "err-mdetail.txt",
                    logHead + "Err:" + msg + "\r\n", null, true );
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
        if ( page.getHtml().contains( "404 Not Found" ) )
        {
            // 又回到了正常模式,这种模式下直接结束
            if ( retryMode == 4 )
            {
                return;
            }
            retry404ChangeMode = true;
        }
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

    protected String getMonoTExt( Element element, String filter,
            String pattern )
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