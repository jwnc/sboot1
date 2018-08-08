
package com.wnc.sboot1.security.sevice.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wnc.sboot1.security.dao.UserInfoDao;
import com.wnc.sboot1.security.entity.UserInfo;
import com.wnc.sboot1.security.sevice.UserInfoService;

@Service
public class UserInfoServiceImpl implements UserInfoService
{
    @Resource
    private UserInfoDao userInfoDao;

    @Override
    public UserInfo findByUsername( String username )
    {
        System.out.println( "UserInfoServiceImpl.findByUsername()" );
        return userInfoDao.findByUsername( username );
    }
}