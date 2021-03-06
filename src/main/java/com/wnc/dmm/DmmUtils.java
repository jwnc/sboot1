
package com.wnc.dmm;

import org.apache.commons.lang3.StringUtils;

import com.wnc.basic.BasicStringUtil;
import com.wnc.string.PatternUtil;

public class DmmUtils
{
    public static String getMovieUrl( String cid )
    {
        return String.format( DmmConsts.MOVIE_PARAMS_URL, cid );
    }

    public static String getDetailUrl( String cid )
    {
        return String.format( DmmConsts.DETAIL_PAGE_URL, cid );
    }

    /**
     * 非常规地址
     * 
     * @param cid
     * @param retryMode
     *            1: mono, 2:videoc 3: 补全juy001 --> juy00001
     * @return
     */
    public static String getDetailRetryUrl( String cid, int retryMode )
    {
        if ( retryMode == 0 )
        {
            return String.format( DmmConsts.DETAIL_PAGE_URL, cid );
        } else if ( retryMode == 1 )
        {
            return String.format( DmmConsts.DETAIL_PAGE_MONO_URL, cid );
        } else if ( retryMode == 2 )
        {
            return String.format( DmmConsts.DETAIL_PAGE_C_URL, cid );
        } else if ( retryMode == 3 )
        {
            String pre = PatternUtil.getFirstPatternGroup( cid,
                    "(.+[^\\d])\\d+\\S*$" );
            String num = PatternUtil.getLastPatternGroup( cid, "(\\d+)" );
            String newCid = pre + BasicStringUtil.fillLeftString( num, 5, "0" );
            return String.format( DmmConsts.DETAIL_PAGE_URL, newCid );
        } else
        {
            return String.format( DmmConsts.DETAIL_PAGE_URL, cid );
        }
    }

    public static String getHotPageUrl( int page )
    {
        return String.format( DmmConsts.HOT_PAGE_URL, page );
    }

    public static String dealPicUrl( String imgUrl )
    {
        if ( imgUrl.matches( ".+(p[a-z])\\.jpg.*?" ) )
        {
            return imgUrl.replaceFirst( "(p[a-z])(\\.jpg.*?)", "pl$2" );
        } else if ( imgUrl.contains( "consumer_game" ) )
        {
            return imgUrl.replace( "js-", "-" );
        } else if ( imgUrl.matches( ".+js\\-([0-9]+)\\.jpg$" ) )
        {
            return imgUrl.replace( "js-", "jp-" );
        } else if ( imgUrl.matches( ".+ts\\-([0-9]+)\\.jpg$" ) )
        {
            return imgUrl.replace( "ts-", "tl-" );
        } else if ( imgUrl.matches( ".+(\\-[0-9]+\\.)jpg$" ) )
        {
            return imgUrl.replaceAll( "(\\-[0-9]+\\.)(jpg$)", "jp$1$2" );
        } else
        {
            return imgUrl.replace( "-", "jp-" );
        }
    }

    public static String getMovieDetailLocation( String cid )
    {
        String folder = "";
        String id = PatternUtil.getLastPattern( cid, "\\d+" );
        if ( StringUtils.isBlank( id ) )
        {
            folder = DmmConsts.DETAIL_DIR + "special\\" + cid + "\\";
        } else
        {
            id = id.length() < 3 ? BasicStringUtil.fillLeftString( id, 3, "0" )
                    : id.substring( id.length() - 3 );
            folder = DmmConsts.DETAIL_DIR + id + "\\" + cid + "\\";
        }
        return folder;
    }

    public static String getMovieDetailPicsLocation( String cid )
    {
        return getMovieDetailLocation( cid ) + "pics\\";
    }

    public static void main( String[] args )
    {
        String cid = "h_455maguro056r";
        String detailRetryUrl = getDetailRetryUrl( cid, 3 );
        System.out.println( detailRetryUrl );
        String dealPicUrl = dealPicUrl(
                "http://pics.dmm.co.jp/digital/video/cjod00113/cjod00113-1.jpg" );
        System.out.println( dealPicUrl );

        dealPicUrl = dealPicUrl(
                "http://pics.dmm.co.jp/digital/video/cjod00113/cjod00113ps.jpg" );
        System.out.println( dealPicUrl );
        String movieDetailLocation = getMovieDetailLocation( "cjod00113" );
        System.out.println( movieDetailLocation );
    }

}
