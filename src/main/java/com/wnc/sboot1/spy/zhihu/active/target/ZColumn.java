
package com.wnc.sboot1.spy.zhihu.active.target;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.wnc.sboot1.spy.zhihu.active.Actor;


/**
 * 专栏
 * 
 * @author Administrator
 */
@Entity
@Table(name = "ZH_COLUMN")
public class ZColumn extends Target
{
    private String followers;

    @Transient
    private Actor author;

    private String actor_id;

    private String comment_permission;

    private String image_url;

    private String articles_count;

    private String intro;

    @Column(length = 4000)
    private String description;

    private String title;

    private Long updated;

    /**
     * 重写计算方法， 有时候type为null
     */
    @Override
    public String generateTid()
    {
        if (getType() == null)
        {
            setType("column");
        }
        return super.generateTid();
    }

    public String getFollowers()
    {
        return followers;
    }

    public void setFollowers(String followers)
    {
        this.followers = followers;
    }

    public Actor getAuthor()
    {
        return author;
    }

    public void setAuthor(Actor author)
    {
        this.author = author;
    }

    public String getComment_permission()
    {
        return comment_permission;
    }

    public void setComment_permission(String comment_permission)
    {
        this.comment_permission = comment_permission;
    }

    public String getImage_url()
    {
        return image_url;
    }

    public void setImage_url(String image_url)
    {
        this.image_url = image_url;
    }

    public String getArticles_count()
    {
        return articles_count;
    }

    public void setArticles_count(String articles_count)
    {
        this.articles_count = articles_count;
    }

    public String getIntro()
    {
        return intro;
    }

    public void setIntro(String intro)
    {
        this.intro = intro;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Long getUpdated()
    {
        return updated;
    }

    public void setUpdated(Long updated)
    {
        this.updated = updated;
    }

    public String getActor_id()
    {
        return actor_id;
    }

    public void setActor_id(String actor_id)
    {
        this.actor_id = actor_id;
    }

}
