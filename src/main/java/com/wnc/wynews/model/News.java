package com.wnc.wynews.model;

import java.util.List;
/**
 * @Description 从Html中解析出的新闻
 * @Author nengcai.wang
 * @Date 2018/7/26 11:34
*/
public class News{
    /***
     * @Description 评论数量
     * @Date 2018/7/26 11:33
     * @Param null 
     * @Return 
    */
    private int cmtCount;

    private List<NewsKeyWord> keywords;
    private String commenturl;
    private String label;
    private String title;
    private String tlastid;
    private String imgurl;
    private String digest;
    private String docurl;
    private String tlink;
    private String time;
    private String add2;
    private String add1;
    private String tienum;
    private String newstype;
    private String add3;

    public List<NewsKeyWord> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<NewsKeyWord> keywords) {
        this.keywords = keywords;
    }

    public String getCommenturl() {
        return commenturl;
    }

    public void setCommenturl(String commenturl) {
        this.commenturl = commenturl;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTlastid() {
        return tlastid;
    }

    public void setTlastid(String tlastid) {
        this.tlastid = tlastid;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getDocurl() {
        return docurl;
    }

    public void setDocurl(String docurl) {
        this.docurl = docurl;
    }

    public String getTlink() {
        return tlink;
    }

    public void setTlink(String tlink) {
        this.tlink = tlink;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAdd2() {
        return add2;
    }

    public void setAdd2(String add2) {
        this.add2 = add2;
    }

    public String getAdd1() {
        return add1;
    }

    public void setAdd1(String add1) {
        this.add1 = add1;
    }

    public String getTienum() {
        return tienum;
    }

    public void setTienum(String tienum) {
        this.tienum = tienum;
    }

    public String getNewstype() {
        return newstype;
    }

    public void setNewstype(String newstype) {
        this.newstype = newstype;
    }

    public String getAdd3() {
        return add3;
    }

    public void setAdd3(String add3) {
        this.add3 = add3;
    }
    public int getCmtCount() {
        return cmtCount;
    }

    public void setCmtCount(int cmtCount) {
        this.cmtCount = cmtCount;
    }
}
