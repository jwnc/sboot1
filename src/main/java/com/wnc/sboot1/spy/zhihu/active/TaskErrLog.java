
package com.wnc.sboot1.spy.zhihu.active;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "ZH_TASK_ERRLOG" )
public class TaskErrLog
{
    @Id
    @GeneratedValue
    private int id;
    private String uToken;
    private String url;
    private String msg;

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getuToken()
    {
        return uToken;
    }

    public void setuToken( String uToken )
    {
        this.uToken = uToken;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg( String msg )
    {
        this.msg = msg;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

}
