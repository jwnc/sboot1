package com.wnc.wynews.jpa.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class WyRedNameInfo  extends  BaseEntity{
    @Id
    @Column(length = 95) // 主键最大长度767个字节
    private String titleId;
    private String titleName;
    private String image;
    private String url;
    private String info;

    @Transient
    @ManyToMany( fetch = FetchType.LAZY )
    @JoinTable( name = "WyUserRedNameRelation", joinColumns = {
            @JoinColumn( name = "titleId" )}, inverseJoinColumns = {
            @JoinColumn( name = "uid" )},uniqueConstraints = {@UniqueConstraint(columnNames={"uid", "titleId"})} )
    private List<WyUser> wyUserList;

    public List<WyUser> getWyUserList() {
        return wyUserList;
    }

    public void setWyUserList(List<WyUser> wyUserList) {
        this.wyUserList = wyUserList;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}