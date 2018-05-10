
package com.wnc.sboot1.spy.zhihu.active.target;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 关注了圆桌 MEMBER_FOLLOW_ROUNDTABLE
 * 
 * @author Administrator
 */
@Entity
@Table( name = "ZH_ROUNDTABLE" )
public class RoundTable extends Target
{
    private String comment_count;
    private String start_tim;
    private String visits;
    private String followers;
    private String stop_time;
    private String name;
    private String banner;
    private String logo;
    @Column( length = 4000 )
    private String description;

    public String getComment_count()
    {
        return comment_count;
    }

    public void setComment_count( String comment_count )
    {
        this.comment_count = comment_count;
    }

    public String getStart_tim()
    {
        return start_tim;
    }

    public void setStart_tim( String start_tim )
    {
        this.start_tim = start_tim;
    }

    public String getVisits()
    {
        return visits;
    }

    public void setVisits( String visits )
    {
        this.visits = visits;
    }

    public String getFollowers()
    {
        return followers;
    }

    public void setFollowers( String followers )
    {
        this.followers = followers;
    }

    public String getStop_time()
    {
        return stop_time;
    }

    public void setStop_time( String stop_time )
    {
        this.stop_time = stop_time;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getBanner()
    {
        return banner;
    }

    public void setBanner( String banner )
    {
        this.banner = banner;
    }

    public String getLogo()
    {
        return logo;
    }

    public void setLogo( String logo )
    {
        this.logo = logo;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

}
