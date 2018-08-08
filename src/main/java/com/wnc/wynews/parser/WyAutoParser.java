package com.wnc.wynews.parser;

import com.wnc.basic.BasicNumberUtil;
import com.wnc.wynews.model.NewsKeyWord;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 网易汽车
 * @Date 23:56 2018/7/26
 * @Param
 * @return
 */
public class WyAutoParser extends WyParser {
    public WyAutoParser(Document doc) {
        super(doc);
        listSelector = "#auto_pull_dataset .auto_news_item";
    }

    public WyAutoParser(String html) {
        super(html);
        listSelector = "#auto_pull_dataset .auto_news_item";
    }

    @Override
    protected String getTitle(Element item) {
        return item.select(".item-cont h3 > a").text();
    }

    @Override
    protected String getTime(Element item) {
        return item.select(".item-cont .item-info .item-time").text().trim();
    }

    @Override
    protected String getLabel(Element item) {
        return null;
    }

    @Override
    protected String getDiget(Element item) {
        return item.select(".item-cont p").text().trim().replace("版权声明：本文版权为网易汽车所有，转载请注明出处。", "");
    }

    @Override
    public String getDocUrl(Element item) {
        return item.select(".item-cont h3 > a").attr("href");
    }

    @Override
    public String getImgUrl(Element item) {
        return item.select("a.item-pic > img").attr("src");
    }

    @Override
    public String getCmtUrl(Element item) {
        return null;
    }

    @Override
    public Integer getCmtCount(Element item) {
        return BasicNumberUtil.getNumber(item.select(".item-info .item-comment").text().trim());
    }

    @Override
    public List<NewsKeyWord> getKeyWords(Element item) {
        List<NewsKeyWord> list = null;
        Elements tags = item.select(".item-cont .item-tag a");
        if (tags != null) {
            list = new ArrayList<NewsKeyWord>();
            for (Element tag : tags) {
                NewsKeyWord nkw = new NewsKeyWord();
                nkw.setName(tag.text().trim());
                nkw.setUrl("http://auto.163.com" + tag.attr("href"));
                list.add(nkw);
            }
            return list;
        }
        return list;
    }

}
