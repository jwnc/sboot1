
package com.wnc.qqnews.service;

import com.wnc.qqnews.jpa.entity.QqUser;
import com.wnc.qqnews.jpa.repo.QQCertInfoRepository;
import com.wnc.qqnews.jpa.repo.QQUserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QqDbService {
    private static Logger logger = Logger.getLogger(QqDbService.class);

    @Autowired
    private QQUserRepository qqUserRepository;
    @Autowired
    private QQCertInfoRepository qqCertInfoRepository;


    public void singleUser(QqUser qqUser) {
        try {
            if (qqUser.getCertinfo() != null) {
                qqCertInfoRepository.save(qqUser.getCertinfo());
            }
            qqUserRepository.save(qqUser);
        }catch (Exception e){
            logger.error(qqUser.getUserid() + qqUser.getNick() + " 插入失败!", e);
        }
    }
}
