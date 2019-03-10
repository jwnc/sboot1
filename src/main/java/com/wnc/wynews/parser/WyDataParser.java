package com.wnc.wynews.parser;

import com.alibaba.fastjson.JSONObject;
import com.wnc.string.PatternUtil;
import com.wnc.wynews.model.DataNews;
import com.wnc.wynews.model.News;
import com.wnc.wynews.model.NewsKeyWord;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 网易数读
 * @Date 23:56 2018/7/26
 * @Param
 * @return
 */
public class WyDataParser extends WyParser {
    public WyDataParser(Document doc) {
        super(doc);
    }

    public WyDataParser(String html) {
        super(html);
    }

    @Override
    public List<News> getNewsList() {
        List<News> ret = new ArrayList<News>();
        String firstPatternGroup = PatternUtil.getFirstPatternGroup(doc.toString().replace("\n", ""), " ohnofuchlist=(\\[.*?\\]);");
        List<String> allPatternGroup = PatternUtil.getAllPatternGroup(firstPatternGroup, "(\\{.*?\\})");
        for (String s : allPatternGroup) {
            DataNews dataNews = JSONObject.parseObject(s.replaceAll("'", "\""), DataNews.class);
            ret.add(dataNews.cvt2News());
        }
        return ret;
    }

    @Override
    protected String getTitle(Element item) {
        return null;
    }

    @Override
    protected String getTime(Element item) {
        return null;
    }

    @Override
    protected String getLabel(Element item) {
        return null;
    }

    @Override
    protected String getDiget(Element item) {
        return null;
    }

    @Override
    public String getDocUrl(Element item) {
        return null;
    }

    @Override
    public String getImgUrl(Element item) {
        return null;
    }

    @Override
    public String getCmtUrl(Element item) {
        return null;
    }

    @Override
    public Integer getCmtCount(Element item) {
        return null;
    }

    @Override
    public List<NewsKeyWord> getKeyWords(Element item) {
        return null;
    }

}
