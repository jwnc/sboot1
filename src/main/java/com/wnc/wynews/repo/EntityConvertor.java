package com.wnc.wynews.repo;

import com.wnc.wynews.model.News;

public class EntityConvertor {
    public WyNews newsToEntity(News news){
        WyNews wyNews = new WyNews();
        wyNews.setCmtCount(news.getCmtCount());

        return wyNews;
    }
}
