package com.wnc.wynews.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.UniqueConstraint;
import java.util.List;

@Entity
public class WyIncentiveInfo  extends  BaseEntity{
    @Id
    @Column( length = 95 ) // 主键最大长度767个字节
    private String info;
    private String url;

    @ManyToMany( fetch = FetchType.EAGER )
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
