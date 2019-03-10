package com.wnc.qqnews.jpa.entity;

import com.wnc.wynews.jpa.entity.BaseEntity;
import com.wnc.wynews.jpa.entity.WyNews;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import java.util.Set;

@Entity
public class QqNewsTagLabel  extends BaseEntity {
    @Id
    @Column(length = 95) // 主键最大长度767个字节
    private String id;
    private String name;

    @Transient
    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable( name = "QqNewsTagLabelRelation", joinColumns = {
            @JoinColumn( name = "tag_id" )}, inverseJoinColumns = {
            @JoinColumn( name = "news_id" )},uniqueConstraints = {@UniqueConstraint(columnNames={"news_id", "tag_id"})} )
    private Set<QqNews> qqNews;

    public QqNewsTagLabel() {
    }

    public QqNewsTagLabel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Set<QqNews> getQqNews() {
        return qqNews;
    }

    public void setQqNews(Set<QqNews> qqNews) {
        this.qqNews = qqNews;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}