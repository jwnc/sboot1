
package com.wnc.sboot1.spy.zhihu.active;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "user_v" )
public class UserV
{
    @Id
    private int id;
    private String userToken;
    private String location;
    private String business;
    private String sex;
    private String employment;
    private String education;
    private String username;
    private String url;
    private int agrees;
    private int thanks;
    private int asks;
    private int answers;
    private int posts;
    private int followees;
    private int followers;
    @Column( name = "hashId" )
    private String hashId;
    private int status;
    private Date lastSpyTime;

    public UserV( String userToken,Date lastSpyTime )
    {
        this.userToken = userToken;
        this.lastSpyTime = lastSpyTime;
    }

    public UserV( String userToken,String url,Date lastSpyTime )
    {
        this.userToken = userToken;
        this.url = url;
        this.lastSpyTime = lastSpyTime;
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getUserToken()
    {
        return userToken;
    }

    public void setUserToken( String userToken )
    {
        this.userToken = userToken;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation( String location )
    {
        this.location = location;
    }

    public String getBusiness()
    {
        return business;
    }

    public void setBusiness( String business )
    {
        this.business = business;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex( String sex )
    {
        this.sex = sex;
    }

    public String getEmployment()
    {
        return employment;
    }

    public void setEmployment( String employment )
    {
        this.employment = employment;
    }

    public String getEducation()
    {
        return education;
    }

    public void setEducation( String education )
    {
        this.education = education;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    public int getAgrees()
    {
        return agrees;
    }

    public void setAgrees( int agrees )
    {
        this.agrees = agrees;
    }

    public int getThanks()
    {
        return thanks;
    }

    public void setThanks( int thanks )
    {
        this.thanks = thanks;
    }

    public int getAsks()
    {
        return asks;
    }

    public void setAsks( int asks )
    {
        this.asks = asks;
    }

    public int getAnswers()
    {
        return answers;
    }

    public void setAnswers( int answers )
    {
        this.answers = answers;
    }

    public int getPosts()
    {
        return posts;
    }

    public void setPosts( int posts )
    {
        this.posts = posts;
    }

    public int getFollowees()
    {
        return followees;
    }

    public void setFollowees( int followees )
    {
        this.followees = followees;
    }

    public int getFollowers()
    {
        return followers;
    }

    public void setFollowers( int followers )
    {
        this.followers = followers;
    }

    public String getHashId()
    {
        return hashId;
    }

    public void setHashId( String hashId )
    {
        this.hashId = hashId;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus( int status )
    {
        this.status = status;
    }

    public Date getLastSpyTime()
    {
        return lastSpyTime;
    }

    public void setLastSpyTime( Date lastSpyTime )
    {
        this.lastSpyTime = lastSpyTime;
    }

}
