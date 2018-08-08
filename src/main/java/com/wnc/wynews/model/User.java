package com.wnc.wynews.model;

public class User {
    private String nickname;
    private Integer userId;
    private String location;

    private String avatar;
    private String[] incentiveInfoList;
    private String[] redNameInfo;

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

    public String[] getIncentiveInfoList() {
        return incentiveInfoList;
    }

    public void setIncentiveInfoList(String[] incentiveInfoList) {
        this.incentiveInfoList = incentiveInfoList;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String[] getRedNameInfo() {
        return redNameInfo;
    }

    public void setRedNameInfo(String[] redNameInfo) {
        this.redNameInfo = redNameInfo;
    }
}
