
package com.wnc.sboot1.security.sevice;

import com.wnc.sboot1.security.entity.UserInfo;

public interface UserInfoService
{
    /** 通过username查找用户信息; */
    public UserInfo findByUsername( String username );
}