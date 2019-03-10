package com.wnc.wynews.parser;

import com.alibaba.fastjson.JSONArray;
import com.wnc.string.PatternUtil;
import com.wnc.wynews.model.News;
import com.wnc.wynews.model.NewsKeyWord;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

/**
 * @Description 网易数读
 * @Date 23:56 2018/7/26
 * @Param
 * @return
 */
public class WyJsonParser extends WyParser {
    public WyJsonParser(Document doc) {
        super(doc);
    }

    public WyJsonParser(String html) {
        super(html);
    }

    @Override
    public List<News> getNewsList() {
        String listStr = PatternUtil.getFirstPatternGroup(html.replaceAll("\n", ""), "data_callback\\((.+)\\)$");
        return JSONArray.parseArray(listStr, News.class);
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
