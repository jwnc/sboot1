
package com.wnc.sboot1.spy.zhihu.active;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "ZH_ACTOR" )
public class Actor
{
    @Id
    @Column( length = 64 )
    private String id;
    private String is_org;
    private String gender;
    private String is_followed;
    private String url_token;
    private String type;
    private String url;
    // private List badge;
    private String user_type;
    private String avatar_url;
    private String name;
    private String headline;

    public String getIs_org()
    {
        return is_org;
    }

    public void setIs_org( String is_org )
    {
        this.is_org = is_org;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender( String gender )
    {
        this.gender = gender;
    }

    public String getIs_followed()
    {
        return is_followed;
    }

    public void setIs_followed( String is_followed )
    {
        this.is_followed = is_followed;
    }

    public String getUrl_token()
    {
        return url_token;
    }

    public void setUrl_token( String url_token )
    {
        this.url_token = url_token;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    public String getUser_type()
    {
        return user_type;
    }

    public void setUser_type( String user_type )
    {
        this.user_type = user_type;
    }

    public String getAvatar_url()
    {
        return avatar_url;
    }

    public void setAvatar_url( String avatar_url )
    {
        this.avatar_url = avatar_url;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public String getHeadline()
    {
        return headline;
    }

    public void setHeadline( String headline )
    {
        this.headline = headline;
    }

}
