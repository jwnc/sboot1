
package com.wnc.sboot1.spy.zhihu.active.aggre;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TargetDesc
{
    @Id
    @Column( length = 64 )
    private String tid;
    private String type;
    private String title;
    @Column( length = 4000 )
    private String description;
    private String url;

    @Override
    public String toString()
    {
        return "TargetDesc [tid=" + tid + ", type=" + type + ", title=" + title
                + ", description=" + description + ", url=" + url + "]";
    }

    public String getTid()
    {
        return tid;
    }

    public void setTid( String tid )
    {
        this.tid = tid;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle( String title )
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

}
