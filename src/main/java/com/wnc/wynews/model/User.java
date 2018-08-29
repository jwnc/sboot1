package com.wnc.wynews.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.wnc.wynews.jpa.entity.WyIncentiveInfo;
import com.wnc.wynews.jpa.entity.WyRedNameInfo;

import java.util.Set;

public class User {
    private String nickname;
    private Integer userId;
    private String location;

    private String avatar;

    @JSONField(name="incentiveInfoList")
    private Set<WyIncentiveInfo> wyIncentiveInfoList;
    @JSONField(name="redNameInfo")
    private Set<WyRedNameInfo> wyRedNameInfo;

    public Set<WyIncentiveInfo> getWyIncentiveInfoList() {
        return wyIncentiveInfoList;
    }

    public void setWyIncentiveInfoList(Set<WyIncentiveInfo> wyIncentiveInfoList) {
        this.wyIncentiveInfoList = wyIncentiveInfoList;
    }

    public Set<WyRedNameInfo> getWyRedNameInfo() {
        return wyRedNameInfo;
    }

    public void setWyRedNameInfo(Set<WyRedNameInfo> wyRedNameInfo) {
        this.wyRedNameInfo = wyRedNameInfo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
