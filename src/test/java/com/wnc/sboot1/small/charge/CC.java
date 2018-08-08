
package com.wnc.sboot1.small.charge;

import java.io.IOException;

import org.apache.http.client.methods.HttpGet;

import com.crawl.core.util.HttpClientUtil;
import com.wnc.basic.BasicFileUtil;

public class CC
{
    public static void main( String[] args ) throws IOException
    {
        String p = "http://www.bjev520.com/jsp/beiqi/pcmap/do/pcMap.jsp?chargingTypeId=&companyId=&chargingBrandId=&brandStatuId=&cityName=%E6%AD%A6%E6%B1%89%E5%B8%82";

        getDetail();

        getMap();

    }

    private static void getMap() throws IOException
    {
        HttpGet get = new HttpGet(
                "http://www.bjev520.com/jsp/beiqi/pcmap/pages/pcmap_Main.jsp" );
        get.setHeader( "Referer",
                "http://www.bjev520.com/jsp/beiqi/pcmap/do/pcMap.jsp?chargingTypeId=&companyId=&chargingBrandId=4&brandStatuId=&cityName=%E6%AD%A6%E6%B1%89%E5%B8%82" );
        get.setHeader( "Cookie",
                "JSESSIONID=7CA967D6495D1D0744380DEFC133E404" );
        String webPage = HttpClientUtil.getWebPage( get );
        BasicFileUtil.writeFileString( "C:\\个人工作\\项目\\充电桩\\测试准备\\cg-map.txt",
                webPage, null, false );
    }

    private static void getDetail() throws IOException
    {
        HttpGet get = new HttpGet(
                "http://www.bjev520.com/jsp/beiqi/pcmap/pages/pcmap_Left.jsp" );
        get.setHeader( "Referer",
                "http://www.bjev520.com/jsp/beiqi/pcmap/do/pcMap.jsp?chargingTypeId=&companyId=&chargingBrandId=4&brandStatuId=&cityName=%E6%AD%A6%E6%B1%89%E5%B8%82" );
        get.setHeader( "Cookie",
                "JSESSIONID=7CA967D6495D1D0744380DEFC133E404" );
        String webPage = HttpClientUtil.getWebPage( get );
        BasicFileUtil.writeFileString( "C:\\个人工作\\项目\\充电桩\\测试准备\\cg-detail.txt",
                webPage, null, false );
    }
}
