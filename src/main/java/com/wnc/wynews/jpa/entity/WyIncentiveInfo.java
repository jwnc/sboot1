package com.wnc.wynews.jpa.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class WyIncentiveInfo  extends  BaseEntity{
    @Id
    @Column( length = 95 ) // 主键最大长度767个字节
    private String info;
    private String url;

    @Transient
    @ManyToMany( fetch = FetchType.LAZY )
    @JoinTable( name = "WyUserIncentiveRelation", joinColumns = {
            @JoinColumn( name = "info" )}, inverseJoinColumns = {
            @JoinColumn( name = "uid" )},uniqueConstraints = {@UniqueConstraint(columnNames={"uid", "info"})} )
    private List<WyUser> wyUserList;

    public List<WyUser> getWyUserList() {
        return wyUserList;
    }

    public void setWyUserList(List<WyUser> wyUserList) {
        this.wyUserList = wyUserList;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
