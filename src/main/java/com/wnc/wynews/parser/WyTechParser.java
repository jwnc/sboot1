package com.wnc.wynews.parser;

import com.wnc.basic.BasicNumberUtil;
import com.wnc.string.PatternUtil;
import com.wnc.wynews.model.NewsKeyWord;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

/**
 * @Description 网易科技(IT和互联网)
 * @Date 0:05 2018/7/27
 * @Param
 * @return
 */
public class WyTechParser extends WyParser {

    public WyTechParser(Document doc) {
        super(doc);
        this.listSelector = "#news-flow-content li";
    }

    public WyTechParser(String html) {
        super(html);
        this.listSelector = "#news-flow-content li";
    }

    @Override
    protected String getTitle(Element item) {
        return item.select("h3.bigsize > a").text();
    }

    @Override
    protected String getTime(Element item) {
        return PatternUtil.getLastPattern(item.select("p.sourceDate").text(), "[\\d-: ]+");
    }

    @Override
    protected String getLabel(Element item) {
        return null;
    }

    @Override
    protected String getDiget(Element item) {
        return item.select(".newsDigest").text();
    }

    @Override
    public String getDocUrl(Element item) {
        return item.select("h3.bigsize > a").attr("href");
    }

    @Override
    public String getImgUrl(Element item) {
        return item.select("a.newsList-img img").attr("src");
    }

    @Override
    public String getCmtUrl(Element item) {
        return item.select("a.commentCount").attr("href");
    }

    @Override
    public Integer getCmtCount(Element item) {
        return BasicNumberUtil.getNumber(item.select("a.commentCount").text().trim());
    }

    @Override
    public List<NewsKeyWord> getKeyWords(Element item) {
        return null;
    }
}
