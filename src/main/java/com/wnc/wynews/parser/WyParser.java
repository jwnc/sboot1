package com.wnc.wynews.parser;

import com.wnc.basic.BasicNumberUtil;
import com.wnc.wynews.model.News;
import com.wnc.wynews.model.NewsKeyWord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public abstract class WyParser {
    protected Document doc;
    protected String html;
    protected String listSelector;


    public WyParser(Document doc) {
        this.doc = doc;
        this.html = doc.toString();
    }

    public WyParser(String html) {
        this.html = html;
        this.doc = Jsoup.parse(html);
    }

    public String getAttr(Element e, String selector, String attr) {
        return e.select(selector).attr(attr);
    }

    public String getText(Element e, String selector) {
        return e.select(selector).text();
    }

    public int number(String str) {
        return BasicNumberUtil.getNumber(str);
    }

    public List<News> getNewsList() {
        List<News> ret = new ArrayList<News>();
        Elements select = doc.select(listSelector);
        News news = null;
        for (Element e : select) {
            news = new News();
            news.setCmtCount(getCmtCount(e));
            news.setCommenturl(getCmtUrl(e));
            news.setDigest(getDiget(e));
            news.setDocurl(getDocUrl(e));
            news.setImgurl(getImgUrl(e));
            news.setKeywords(getKeyWords(e));
            news.setLabel(getLabel(e));
            news.setTime(getTime(e));
            news.setTitle(getTitle(e));
            ret.add(news);
        }
        return ret;
    }

    protected abstract String getTitle(Element item);

    protected abstract String getTime(Element item);

    protected abstract String getLabel(Element item);

    protected abstract String getDiget(Element item);

    public abstract String getDocUrl(Element item);

    public abstract String getImgUrl(Element item);

    public abstract String getCmtUrl(Element item);

    public abstract Integer getCmtCount(Element item);

    public abstract List<NewsKeyWord> getKeyWords(Element item);

}
