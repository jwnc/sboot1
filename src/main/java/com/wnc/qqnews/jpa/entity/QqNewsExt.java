package com.wnc.qqnews.jpa.entity;

public class QqNewsExt {
    private int tonality_score;
    private int news_score;
    private int img_type;

    public int getTonality_score() {
        return tonality_score;
    }

    public void setTonality_score(int tonality_score) {
        this.tonality_score = tonality_score;
    }

    public int getNews_score() {
        return news_score;
    }

    public void setNews_score(int news_score) {
        this.news_score = news_score;
    }

    public int getImg_type() {
        return img_type;
    }

    public void setImg_type(int img_type) {
        this.img_type = img_type;
    }
}