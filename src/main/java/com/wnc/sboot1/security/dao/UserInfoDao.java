
package com.wnc.sboot1.security.dao;

import org.springframework.data.repository.CrudRepository;

import com.wnc.sboot1.security.entity.UserInfo;

public interface UserInfoDao extends CrudRepository<UserInfo, Long>
{
    /** 通过username查找用户信息; */
    public UserInfo findByUsername( String username );
}