package com.wnc.wynews.jpa.entity;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Set;

@Entity
public class WyNewsKeyword extends BaseEntity{
    @Id
    @Column(length = 95) // 主键最大长度767个字节
    private String name;
    private String link;

    @Transient
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "WyNewsKeywordRelation", joinColumns = {
            @JoinColumn(name = "kwName")}, inverseJoinColumns = {
            @JoinColumn(name = "newsCode")}, uniqueConstraints = {@UniqueConstraint(columnNames = {"newsCode", "kwName"})})
    private Set<WyNews> wyNews;

    public Set<WyNews> getWyNews() {
        return wyNews;
    }

    public void setWyNews(Set<WyNews> wyNews) {
        this.wyNews = wyNews;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public int hashCode() {
        if (StringUtils.isBlank(name)) {
            return -1;
        }
        return name.hashCode();
    }
}
