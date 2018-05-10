
package com.wnc.sboot1.spy.zhihu.active.target;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 关注话题TOPIC_FOLLOW
 * 
 * @author Administrator
 */
@Entity
@Table( name = "ZH_TOPIC" )
public class Topic extends Target
{
    private String avatar_url;
    private String followers_count;
    private String name;
    @Column( length = 4000 )
    private String excerpt;
    private String introduction;

    public String getAvatar_url()
    {
        return avatar_url;
    }

    public void setAvatar_url( String avatar_url )
    {
        this.avatar_url = avatar_url;
    }

    public String getFollowers_count()
    {
        return followers_count;
    }

    public void setFollowers_count( String followers_count )
    {
        this.followers_count = followers_count;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getExcerpt()
    {
        return excerpt;
    }

    public void setExcerpt( String excerpt )
    {
        this.excerpt = excerpt;
    }

    public String getIntroduction()
    {
        return introduction;
    }

    public void setIntroduction( String introduction )
    {
        this.introduction = introduction;
    }

}
