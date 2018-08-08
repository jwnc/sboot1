
package com.wnc.wynews.parser;

import java.util.List;

import org.jsoup.nodes.Document;
import org.junit.Test;

import com.wnc.wynews.consts.ModuleIdsManager;
import com.wnc.wynews.model.News;
import com.wnc.wynews.utils.WyNewsUtil;

import translate.util.JsoupHelper;

public class ParserTest
{
    @Test
    public void data() throws Exception
    {
        Document documentResult = JsoupHelper
                .getDocumentResult( "http://data.163.com" );
        List<News> newsList = new WyDataParser( documentResult ).getNewsList();
        System.out.println( newsList );
    }

    @Test
    public void auto() throws Exception
    {
        Document documentResult = JsoupHelper
                .getDocumentResult( "http://auto.163.com/special/2016drive/" );
        List<News> newsList = new WyAutoParser( documentResult ).getNewsList();
        // System.out.println(newsList);
        System.out.println(
                newsList.size() + " / " + ModuleIdsManager.addModuleNews( "试驾",
                        WyNewsUtil.getNewsCode( newsList.get( 0 ) ) ) );
        for ( News news : newsList )
        {
            System.out.println( news.getTime() + " / " + news.getDocurl()
                    + " / " + ModuleIdsManager.addModuleNews( "试驾",
                            WyNewsUtil.getNewsCode( news ) ) );
        }
    }

    @Test
    public void tech() throws Exception
    {
        Document documentResult = JsoupHelper
                .getDocumentResult( "http://tech.163.com/special/it_2016/" );
        List newsList = new WyTechParser( documentResult ).getNewsList();
        System.out.println( newsList );
    }

    @Test
    public void json() throws Exception
    {
        // http://money.163.com/special/002557S5/newsdata_idx_licai
        String documentResult = JsoupHelper.getJsonResult(
                "http://sports.163.com/special/000587PK/newsdata_nba_index.js" );
        List newsList = new WyJsonParser( documentResult ).getNewsList();
        System.out.println( newsList );
    }
}
