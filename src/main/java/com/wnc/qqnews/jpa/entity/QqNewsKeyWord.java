package com.wnc.qqnews.jpa.entity;

import com.wnc.wynews.jpa.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import java.util.List;
import java.util.Set;

@Entity
public class QqNewsKeyWord extends BaseEntity {
    @Id
    @Column(length = 95) // 主键最大长度767个字节
    private String name;

    @Transient
    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable( name = "QqNewsKeyWordRelation", joinColumns = {
            @JoinColumn( name = "kw_name" )}, inverseJoinColumns = {
            @JoinColumn( name = "news_id" )},uniqueConstraints = {@UniqueConstraint(columnNames={"news_id", "kw_name"})} )
    private Set<QqNews> qqNews;

    public QqNewsKeyWord() {
    }

    public QqNewsKeyWord(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQqNews(Set<QqNews> qqNews) {
        this.qqNews = qqNews;
    }


}
