package com.wnc.wynews.parser.data;

import com.alibaba.fastjson.JSONObject;
import com.wnc.string.PatternUtil;
import com.wnc.wynews.model.DataNews;
import com.wnc.wynews.model.News;
import org.jsoup.nodes.Document;
import org.junit.Test;
import translate.util.JsoupHelper;

import java.util.List;

public class TestDataHtml {
    @Test
    public void aa() throws Exception {
        String text = "{\"url\":\"http://data.163.com/18/0722/21/DNBLUMKS000181IU.html\",\"title\":\"中国的孩子为什么逃不过问题疫苗\",\"img\":\"http://cms-bucket.nosdn.127.net/2018/07/22/506dd0938d094046a0a2465639d7b1fe.png\",\"time\":\"2018-07-22 21:06:25\",\"comment\":'1662',\"keyword\":'民生,社会,国内,疫苗,狂犬病,流感,乙肝疫苗,接种'}".replaceAll("'", "\"");
        System.out.println(text);
        System.out.println(JSONObject.parseObject(text,DataNews.class));
        Document documentResult = JsoupHelper.getDocumentResult("http://data.163.com");
//        System.out.println(documentResult.toString());
        String firstPatternGroup = PatternUtil.getFirstPatternGroup(documentResult.toString().replace("\n",""), " ohnofuchlist=(\\[.*?\\]);");
//        System.out.println(firstPatternGroup);
        List<String> allPatternGroup = PatternUtil.getAllPatternGroup(firstPatternGroup, "(\\{.*?\\})");
        for(String s: allPatternGroup){
            DataNews dataNews = JSONObject.parseObject(s.replaceAll("'", "\""), DataNews.class);
            News news = dataNews.cvt2News();
            System.out.println(news.getTitle() +" / "+ news.getTime());
        }
    }
}
