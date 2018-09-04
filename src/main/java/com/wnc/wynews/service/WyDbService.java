
package com.wnc.wynews.service;

import com.wnc.wynews.jpa.entity.WyIncentiveInfo;
import com.wnc.wynews.jpa.repo.IncentiveInfoRepository;
import com.wnc.wynews.jpa.entity.WyRedNameInfo;
import com.wnc.wynews.jpa.repo.RedNameInfoRepository;
import com.wnc.wynews.jpa.entity.WyNews;
import com.wnc.wynews.jpa.entity.WyNewsKeyword;
import com.wnc.wynews.jpa.repo.WyNewsKeyWordRepository;
import com.wnc.wynews.jpa.repo.WyNewsRepository;
import com.wnc.wynews.jpa.entity.WyUser;
import com.wnc.wynews.jpa.repo.WyUserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class WyDbService {
    private static Logger logger = Logger.getLogger(WyDbService.class);
    @Autowired
    private WyNewsRepository wyNewsRepository;
    @Autowired
    private WyNewsKeyWordRepository wyNewsKeyWordRepository;
    @Autowired
    private WyUserRepository wyUserRepository;

    @Autowired
    private IncentiveInfoRepository incentiveInfoRepository;
    @Autowired
    private RedNameInfoRepository redNameInfoRepository;

    // @Transactional( propagation = Propagation.REQUIRES_NEW )
    public synchronized void singleNews(WyNews wyNews) {
        try {
            Set<WyNewsKeyword> wyNewsKeywords = wyNews.getWyNewsKeywords();
            if (wyNewsKeywords != null && wyNewsKeywords.size() > 0) {
                for(WyNewsKeyword wyNewsKeyword : wyNewsKeywords) {
                    if(wyNewsKeyWordRepository.findOne(wyNewsKeyword.getName()) == null) {
                        wyNewsKeyWordRepository.save(wyNewsKeyword);
                    }
                }
            }
            wyNewsRepository.save(wyNews);
        } catch (Exception e) {
            logger.error(wyNews.getTitle() + " 插入失败!", e);
        }
    }

    public synchronized void singleUser(WyUser wyUser) {
        try {
            Set<WyIncentiveInfo> wyIncentiveInfos = wyUser.getWyIncentiveInfoList();
            if (wyIncentiveInfos != null && wyIncentiveInfos.size() > 0) {
                incentiveInfoRepository.save(wyIncentiveInfos);
            }
            Set<WyRedNameInfo> wyRedNameInfos = wyUser.getWyRedNameInfo();
            if (wyRedNameInfos != null && wyRedNameInfos.size() > 0) {
                redNameInfoRepository.save(wyRedNameInfos);
            }

            wyUserRepository.save(wyUser);
        } catch (Exception e) {
            logger.error(wyUser.getUserId() + wyUser.getNickname() + " 插入失败!", e);
        }
    }
}
