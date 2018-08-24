
package com.wnc.dmm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.wnc.basic.BasicFileUtil;
import com.wnc.dmm.task.MovieDetailTask;
import com.wnc.dmm.task.MovieParamTask;
import com.wnc.string.PatternUtil;

public class DmmTest
{

    @Test
    public void testMovieDetail() throws IOException, InterruptedException
    {
        String cid = "juy00416";
        new JpProxyUtil().initProxyPool();

        String movieDetailLocation = DmmUtils.getMovieDetailLocation( cid );
        if ( BasicFileUtil.isExistFolder( movieDetailLocation )
                && new File( movieDetailLocation ).listFiles().length == 3 )
        {
            System.out.println( "Ok" );
        } else
        {
            DmmSpiderClient.getInstance()
                    .submitTask( new MovieDetailTask( cid ) );
        }
        Thread.sleep( 10000000L );
    }

    // @Test
    public void testMovieParam() throws IOException, InterruptedException
    {
        new JpProxyUtil().initProxyPool();
        DmmSpiderClient.getInstance()
                .submitTask( new MovieParamTask( "migd00629" ) );

        try
        {
            Thread.sleep( 100000000 );
        } catch ( InterruptedException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // @Test
    public void html()
    {
        String html = "<img src=\"https://pics.dmm.co.jp/mono/movie/adult/h_237nacr147/h_237nacr147ps.jpg\" alt=\"父と娘の近親セックス 酒癖が悪く、親離れも出来ない私はいつもお\r\n"
                + "父さんに迷惑を掛けています。そんなだからあの日も…。神宮寺ナオ\" id=\"package-src-h\\u005f237nacr147\" class=\"tdmm\">";

        Document parse = Jsoup.parse( html );
        Elements select = parse.select( "#package-src-h\\u005f237nacr147" );
        String ownText = select.first().ownText();
        System.out.println( ownText );
        for ( TextNode e : select.first().textNodes() )
        {
            System.out.println( e.text() );
        }
    }

    // @Test
    public void test2()
    {
        String html = "<table border=\"0\" cellpadding=\"2\" cellspacing=\"0\" class=\"mg-b20\">\r\n"
                + "\r\n" + "\r\n" + "\r\n" + "\r\n" + "<tbody><tr>\r\n" + "\r\n"
                + "<tr>\r\n"
                + "<td align=\"right\" valign=\"top\" class=\"nw\">配信開始日：</td>\r\n"
                + "<td>\r\n" + "2017/11/16</td>\r\n" + "</tr>\r\n" + "\r\n"
                + "<tr>\r\n"
                + "<td align=\"right\" valign=\"top\" class=\"nw\">商品発売日：</td>\r\n"
                + "<td>\r\n" + "2017/11/16</td>\r\n" + "</tr>\r\n" + "<tr>\r\n"
                + "<td align=\"right\" valign=\"top\" class=\"nw\">収録時間：</td>\r\n"
                + "<td>140分\r\n" + "（HD版：140分）\r\n" + "\r\n" + "\r\n"
                + "</td>\r\n" + "</tr>\r\n" + "\r\n" + "<tr>\r\n"
                + "<td align=\"right\" valign=\"top\" class=\"nw\">出演者：</td>\r\n"
                + "<td>\r\n" + "<span id=\"performer\">\r\n"
                + "<a href=\"/digital/videoa/-/list/=/article=actress/id=1012844/\">紗倉まな</a>\r\n"
                + "<a href=\"/digital/videoa/-/list/=/article=actress/id=1035021/\">戸田真琴</a>\r\n"
                + "</span></td>\r\n" + "</tr>\r\n" + "\r\n" + "<tr>\r\n"
                + "<td align=\"right\" valign=\"top\" class=\"nw\">監督：</td>\r\n"
                + "<td><a href=\"/digital/videoa/-/list/=/article=director/id=101123/\">キョウセイ</a></td>\r\n"
                + "</tr>\r\n" + "\r\n" + "<tr>\r\n"
                + "<td align=\"right\" valign=\"top\" class=\"nw\">シリーズ：</td>\r\n"
                + "<td><a href=\"/digital/videoa/-/list/=/article=series/id=210033/\">Wキャスト(SODクリエイト)</a></td>\r\n"
                + "</tr>\r\n" + "\r\n" + "\r\n" + "<tr>\r\n"
                + "<td align=\"right\" valign=\"top\" class=\"nw\">メーカー：</td>\r\n"
                + "<td><a href=\"/digital/videoa/-/list/=/article=maker/id=45276/\">SODクリエイト</a></td>\r\n"
                + "</tr>\r\n" + "<tr>\r\n"
                + "<td align=\"right\" valign=\"top\" class=\"nw\">レーベル：</td>\r\n"
                + "<td><a href=\"/digital/videoa/-/list/=/article=label/id=24154/\">SOD star</a></td>\r\n"
                + "</tr>\r\n" + "    <tr>\r\n"
                + "    <td align=\"right\" valign=\"top\" class=\"nw\">ジャンル：</td>\r\n"
                + "    <td>\r\n"
                + "    <a href=\"/digital/videoa/-/list/=/article=keyword/id=6533/\">ハイビジョン</a>&nbsp;&nbsp;<a href=\"/digital/videoa/-/list/=/article=keyword/id=5073/\">デカチン・巨根</a>&nbsp;&nbsp;<a href=\"/digital/videoa/-/list/=/article=keyword/id=25/\">拘束</a>&nbsp;&nbsp;<a href=\"/digital/videoa/-/list/=/article=keyword/id=5011/\">放尿</a>&nbsp;&nbsp;<a href=\"/digital/videoa/-/list/=/article=keyword/id=4013/\">レズ</a>&nbsp;&nbsp;<a href=\"/digital/videoa/-/list/=/article=keyword/id=4005/\">乱交</a>    </td>\r\n"
                + "    </tr>\r\n" + "    <tr>\r\n"
                + "    <td align=\"right\" valign=\"top\" class=\"nw\">品番：</td>\r\n"
                + "    <td>1star00844</td>\r\n" + "    </tr>\r\n" + "<tr>\r\n"
                + "\r\n" + "</tr>\r\n" + "<tr>\r\n" + "<td colspan=\"2\">\r\n"
                + "</td>\r\n" + "</tr>\r\n" + "\r\n" + "</tbody></table>";
        Document doc = Jsoup.parse( html );
        Element parse = doc.select( "table" ).first();
        String publishDate = getMonoTExt( parse, "商品発売日：", "[/\\d]+" );
        String mvLength = getMonoTExt( parse, "収録時間：", "\\d+" );
        String mvCode = getMonoTExt( parse, "品番：", ".+" );

        List<String> performers = getLinkIds( parse, "出演者：" );
        List<String> director = getLinkIds( parse, "監督：" );
        List<String> series = getLinkIds( parse, "シリーズ：" );
        List<String> maker = getLinkIds( parse, "メーカー：" );
        List<String> operator = getLinkIds( parse, "レーベル：" );
        List<String> keywords = getLinkIds( parse, "ジャンル：" );

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

}
