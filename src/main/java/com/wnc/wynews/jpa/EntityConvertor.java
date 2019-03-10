package com.wnc.wynews.jpa;

import com.wnc.wynews.jpa.entity.WyNews;
import com.wnc.wynews.jpa.entity.WyNewsKeyword;
import com.wnc.wynews.jpa.entity.WyUser;
import com.wnc.wynews.model.News;
import com.wnc.wynews.model.NewsKeyWord;
import com.wnc.wynews.model.User;
import com.wnc.wynews.utils.WyNewsUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;

public class EntityConvertor {
    public static WyNews newsToEntity(String moduleName, News news){
        WyNews wyNews = new WyNews();
        wyNews.setCmtCount(news.getCmtCount());
        wyNews.setCommenturl(news.getCommenturl());
        wyNews.setDigest(news.getDigest());
        wyNews.setDocurl(news.getDocurl());
        wyNews.setImgurl(news.getImgurl());
        wyNews.setLabel(news.getLabel());
        wyNews.setTime(WyNewsUtil.getFormatTimeStr(news.getTime()));
        wyNews.setTitle(news.getTitle());
        wyNews.setCode(WyNewsUtil.getNewsCode(news));
        wyNews.setModule(moduleName);

        List<NewsKeyWord> keywords = news.getKeywords();
        if (keywords != null && keywords.size() > 0) {
            wyNews.setWyNewsKeywords(new HashSet<WyNewsKeyword>(3));
            for(NewsKeyWord newsKeyword : keywords){
                if(StringUtils.isBlank(newsKeyword.getKeyname())){
                    continue;
                }
                WyNewsKeyword wyNewsKeyword = new WyNewsKeyword();
                wyNewsKeyword.setLink(newsKeyword.getAkey_link());
                wyNewsKeyword.setName(newsKeyword.getKeyname());
                wyNews.getWyNewsKeywords().add(wyNewsKeyword);
            }
        }

        return wyNews;
    }

    public static WyUser userToEntity(User user){
        WyUser wyUser = new WyUser();
        wyUser.setAvatar(user.getAvatar());
        wyUser.setLocation(user.getLocation());
        wyUser.setNickname(user.getNickname());
        wyUser.setUserId(user.getUserId());

        if(!CollectionUtils.isEmpty(user.getWyIncentiveInfoList())) {
            wyUser.setWyIncentiveInfoList(user.getWyIncentiveInfoList());
        }
        if(!CollectionUtils.isEmpty(user.getWyRedNameInfo())) {
            wyUser.setWyRedNameInfo(user.getWyRedNameInfo());
        }
        return wyUser;
    }
}
