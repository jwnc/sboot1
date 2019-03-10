package com.wnc.qqnews.jpa.entity;

import com.wnc.wynews.jpa.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class QqNewsImg extends BaseEntity {
    @Id
    @Column(length = 95) // 主键最大长度767个字节
    private String src;
    private int width;
    private int height;
    private int pos = 1;

    @JoinColumn(name = "news_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private QqNews qqNews;

    // 图片呈现位置

    public QqNewsImg() {
    }

    public QqNewsImg(int width, int height, String src) {
        this.width = width;
        this.height = height;
        this.src = src;
    }

    public QqNewsImg(int width, int height, String src, int pos) {
        this.width = width;
        this.height = height;
        this.src = src;
        this.pos = pos;
    }

    public QqNewsImg setNews(QqNews qqNews) {
        this.qqNews = qqNews;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public QqNews getQqNews() {
        return qqNews;
    }

    public void setQqNews(QqNews qqNews) {
        this.qqNews = qqNews;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
