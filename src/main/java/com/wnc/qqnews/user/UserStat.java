
package com.wnc.qqnews.user;

public class UserStat
{
    /**
     * 用户ID
     */
    private int id;
    /**
     * 最后活动时间, 单位:秒
     */
    private int lastActiveTime;
    /**
     * 上次爬取时间, 以秒为单位
     */
    private int lastSpyTime;
    /**
     * 评论数
     */
    private int orieffcommentnum;
    /**
     * 回复数
     */
    private int repeffcommentnum;
    /**
     * 获得点赞数
     */
    private int upnum;
    /**
     * 顺序位 0开始, 只读不写
     */
    private int pos;

    public UserStat( int id )
    {
        this.id = id;
    }

    public UserStat()
    {
    }

    public int getId()
    {
        return id;
    }

    public UserStat setId( int id )
    {
        this.id = id;
        return this;
    }

    public int getLastSpyTime()
    {
        return lastSpyTime;
    }

    public UserStat setLastSpyTime( int lastSpyTime )
    {
        this.lastSpyTime = lastSpyTime;
        return this;
    }

    public int getUpnum()
    {
        return upnum;
    }

    public UserStat setUpnum( int upnum )
    {
        this.upnum = upnum;
        return this;
    }

    public int getOrieffcommentnum()
    {
        return orieffcommentnum;
    }

    public UserStat setOrieffcommentnum( int orieffcommentnum )
    {
        this.orieffcommentnum = orieffcommentnum;
        return this;
    }

    public int getRepeffcommentnum()
    {
        return repeffcommentnum;
    }

    public UserStat setRepeffcommentnum( int repeffcommentnum )
    {
        this.repeffcommentnum = repeffcommentnum;
        return this;
    }

    public int getPos()
    {
        return pos;
    }

    public UserStat setPos( int pos )
    {
        this.pos = pos;
        return this;
    }

    public int getLastActiveTime()
    {
        return lastActiveTime;
    }

    public void setLastActiveTime( int lastActiveTime )
    {
        this.lastActiveTime = lastActiveTime;
    }
}
