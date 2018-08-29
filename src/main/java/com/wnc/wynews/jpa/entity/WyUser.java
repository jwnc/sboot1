package com.wnc.wynews.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.UniqueConstraint;
import java.util.Set;

@Entity
public class WyUser extends  BaseEntity{
    @Id
    private Integer userId;
    private String nickname;
    private String location;

    private String avatar;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable( name = "WyUserIncentiveRelation", joinColumns = {
            @JoinColumn( name = "uid" )}, inverseJoinColumns = {
            @JoinColumn( name = "info" )},uniqueConstraints = {@UniqueConstraint(columnNames={"uid", "info"})} )
    private Set<WyIncentiveInfo> wyIncentiveInfoList;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable( name = "WyUserRedNameRelation", joinColumns = {
            @JoinColumn( name = "uid" )}, inverseJoinColumns = {
            @JoinColumn( name = "titleId" )},uniqueConstraints = {@UniqueConstraint(columnNames={"uid", "titleId"})} )
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
