
package com.wnc.sboot1.spy.zhihu.active.target;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 关注收藏夹 MEMBER_FOLLOW_COLLECTION
 * 
 * @author Administrator
 */
@Entity
@Table( name = "ZH_COLLECTION" )
public class Collection extends Target
{
    private String is_public;
    private String title;
    private String answer_count;
    private String follower_count;

    public String getIs_public()
    {
        return is_public;
    }

    public void setIs_public( String is_public )
    {
        this.is_public = is_public;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle( String title )
    {
        this.title = title;
    }

    public String getAnswer_count()
    {
        return answer_count;
    }

    public void setAnswer_count( String answer_count )
    {
        this.answer_count = answer_count;
    }

    public String getFollower_count()
    {
        return follower_count;
    }

    public void setFollower_count( String follower_count )
    {
        this.follower_count = follower_count;
    }

}
