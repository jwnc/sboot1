
package com.wnc.sboot1.spy.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;

import com.crawl.core.util.HttpClientUtil;
import com.crawl.proxy.entity.Direct;
import com.crawl.proxy.entity.Proxy;
import com.crawl.spider.entity.Page;
import com.wnc.basic.BasicDateUtil;

public class SpiderUtils
{
    public static void main( String[] args )
    {
        System.out.println(
                "https://cache.zhibo8.cc/json/2018_04_16/news/nba/5ad4bc3c1499c_count.htm?key=0.1234567891234567"
                        .replaceAll( "(\\?|\\&)[^\\&\\?]+=0\\.\\d{16}", "" ) );
    }

    /**
     * 获取一周中的星期一
     * 
     * @param year
     * @param week
     * @return
     */
    public static String getMondayOfWeek( int year, int week )
    {
        Calendar cal = Calendar.getInstance();
        cal.set( Calendar.YEAR, year ); // year年
        cal.set( Calendar.WEEK_OF_YEAR, week ); // 设置为2018年的第week周
        cal.set( Calendar.DAY_OF_WEEK, 2 ); // 1表示周日，2表示周一，7表示周六
        Date date = cal.getTime();
        return wrapDayWithLine( BasicDateUtil.getDateString( date ) );
    }

    public static String getYesterDayStr()
    {
        String ret = BasicDateUtil.getDateBeforeDayDateString(
                BasicDateUtil.getCurrentDateString(), 1 );
        ret = wrapDayWithLine( ret );
        return ret;
    }

    public static String wrapDayWithLine( String ret )
    {
        if ( ret.matches( "\\d{8}" ) )
        {
            ret = ret.substring( 0, 4 ) + "-" + ret.substring( 4, 6 ) + "-"
                    + ret.substring( 6 );
        }
        return ret;
    }

    public static String getDayWithLine()
    {
        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );
        return df.format( new Date() );
    }

    public static String getLastDayOfMonth( int year, int month )
    {
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set( Calendar.YEAR, year );
        // 设置月份
        cal.set( Calendar.MONTH, month - 1 );
        // 获取某月最大天数
        int lastDay = cal.getActualMaximum( Calendar.DAY_OF_MONTH );
        // 设置日历中月份的最大天数
        cal.set( Calendar.DAY_OF_MONTH, lastDay );
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        String lastDayOfMonth = sdf.format( cal.getTime() );
        return lastDayOfMonth;
    }

    public static String getJsonHtml( String url ) throws IOException
    {
        return Jsoup.connect( url ).ignoreContentType( true ).timeout( 60000 )
                .execute().body();
    }

    public static String getHtmlByProxy( String url, Proxy currentProxy )
            throws Exception
    {
        HttpGet tempRequest = new HttpGet( url );
        if ( !(currentProxy instanceof Direct) )
        {
            HttpHost proxy = new HttpHost( currentProxy.getIp(),
                    currentProxy.getPort() );
            tempRequest.setConfig( HttpClientUtil.getRequestConfigBuilder()
                    .setProxy( proxy ).build() );
        }
        return getWebPage( tempRequest ).getHtml();
    }

    public static Page getWebPage( HttpRequestBase request ) throws IOException
    {
        CloseableHttpResponse response = HttpClientUtil.getResponse( request );
        Page page = new Page();
        page.setStatusCode( response.getStatusLine().getStatusCode() );
        HttpEntity entity = response.getEntity();
        String string = EntityUtils.toString( entity );
        page.setHtml( string );
        string = null;
        entity = null;
        page.setUrl( request.getURI().toString() );
        return page;
    }

    public static Page getWebPage( String url, String charset )
            throws IOException
    {
        Page page = new Page();
        CloseableHttpResponse response = null;
        response = HttpClientUtil.getResponse( url );
        page.setStatusCode( response.getStatusLine().getStatusCode() );
        page.setUrl( url );
        try
        {
            if ( page.getStatusCode() == 200 )
            {
                page.setHtml(
                        EntityUtils.toString( response.getEntity(), charset ) );
            }
        } catch ( IOException e )
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                response.close();
            } catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
        return page;
    }
}
