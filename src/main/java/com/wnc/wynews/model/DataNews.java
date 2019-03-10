package com.wnc.wynews.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 从数读Html中解析出的新闻
 * @Author nengcai.wang
 * @Date 2018/7/26 11:34
 */
public class DataNews {
    private String url;
    private String title;
    private String img;
    private String time;
    private Integer comment;
    private String keyword;

    public News cvt2News() {
        News news = new News();
        news.setDocurl(url);
        news.setTitle(title);
        news.setImgurl(img);
        news.setTime(time);
        news.setCmtCount(comment);
        if (keyword != null) {
            List<NewsKeyWord> list = new ArrayList<NewsKeyWord>();
            for (String kw : keyword.split(",")) {
                NewsKeyWord nkw = new NewsKeyWord();
                nkw.setKeyname(kw.trim());
                list.add(nkw);
            }
            news.setKeywords(list);
        }
        return news;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
