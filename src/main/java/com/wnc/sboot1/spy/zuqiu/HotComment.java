
package com.wnc.sboot1.spy.zuqiu;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class HotComment
{
    @Id
    private int id;
    @Column( length = 4000 )
    private String content;
    private String createtime;
    private int down;
    private int up;
    private String userid;
    private int reply_count;
    // 关联新闻的pinglun
    // @Transient
    private String filename;

    private String device;
    private String figureurl;
    private String level;
    private String m_uid;
    private String parentid;
    private String status;
    private String u_verified;
    private String updatetime;
    private String username;

    private Date updateDate;
    @Column( updatable = false )
    private int favorite;

    public String getContent()
    {
        return content;
    }

    public void setContent( String content )
    {
        this.content = content;
    }

    public String getCreatetime()
    {
        return createtime;
    }

    @Override
    public String toString()
    {
        return "HotComment [content=" + content + "]";
    }

    public void setCreatetime( String createtime )
    {
        this.createtime = createtime;
    }

    public String getDevice()
    {
        return device;
    }

    public void setDevice( String device )
    {
        this.device = device;
    }

    public int getDown()
    {
        return down;
    }

    public void setDown( int down )
    {
        this.down = down;
    }

    public String getFigureurl()
    {
        return figureurl;
    }

    public void setFigureurl( String figureurl )
    {
        this.figureurl = figureurl;
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename( String filename )
    {
        this.filename = filename;
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getLevel()
    {
        return level;
    }

    public void setLevel( String level )
    {
        this.level = level;
    }

    public String getM_uid()
    {
        return m_uid;
    }

    public void setM_uid( String m_uid )
    {
        this.m_uid = m_uid;
    }

    public String getParentid()
    {
        return parentid;
    }

    public void setParentid( String parentid )
    {
        this.parentid = parentid;
    }

    public int getReply_count()
    {
        return reply_count;
    }

    public void setReply_count( int reply_count )
    {
        this.reply_count = reply_count;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus( String status )
    {
        this.status = status;
    }

    public String getU_verified()
    {
        return u_verified;
    }

    public void setU_verified( String u_verified )
    {
        this.u_verified = u_verified;
    }

    public int getUp()
    {
        return up;
    }

    public void setUp( int up )
    {
        this.up = up;
    }

    public String getUpdatetime()
    {
        return updatetime;
    }

    public void setUpdatetime( String updatetime )
    {
        this.updatetime = updatetime;
    }

    public String getUserid()
    {
        return userid;
    }

    public void setUserid( String userid )
    {
        this.userid = userid;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    @ManyToOne
    @JoinColumn( name = "filename", insertable = false, updatable = false )
    private Zb8News zb8News;

    public Zb8News getZb8News()
    {
        return zb8News;
    }

    public void setZb8News( Zb8News zb8News )
    {
        this.zb8News = zb8News;
    }

    public Date getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate( Date updateDate )
    {
        this.updateDate = updateDate;
    }

    public int getFavorite()
    {
        return favorite;
    }

    public void setFavorite( int favorite )
    {
        this.favorite = favorite;
    }

}
