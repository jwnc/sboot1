package com.wnc.wynews.jpa.entity;

import com.wnc.wynews.model.NewsKeyWord;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * @Description 从Html中解析出的新闻
 * @Author nengcai.wang
 * @Date 2018/7/26 11:34
*/
@Entity
public class WyNews  extends  BaseEntity{

    @Id
    @Column( length = 95 ) // 主键最大长度767个字节
    private String code;

    private String module;

    private String docurl;
    /***
     * 评论数量
    */
    private int cmtCount;

    @Transient
    private List<NewsKeyWord> keywords;

    private String commenturl;
    private String label;
    private String title;
    private String imgurl;
    private String digest;
    private String time;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable( name = "WyNewsKeywordRelation", joinColumns = {
            @JoinColumn( name = "newsCode" )}, inverseJoinColumns = {
            @JoinColumn( name = "kwName" )},uniqueConstraints = {@UniqueConstraint(columnNames={"newsCode", "kwName"})} )
    private Set<WyNewsKeyword> wyNewsKeywords;

    public Set<WyNewsKeyword> getWyNewsKeywords() {
        return wyNewsKeywords;
    }

    public void setWyNewsKeywords(Set<WyNewsKeyword> wyNewsKeywords) {
        this.wyNewsKeywords = wyNewsKeywords;
    }

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCmtCount() {
        return cmtCount;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setCmtCount(int cmtCount) {
        this.cmtCount = cmtCount;
    }
}
