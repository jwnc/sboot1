
package com.wnc.sboot1.spy.zhihu.active.aggre;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TargetAggreKey implements Serializable
{
    private static final long serialVersionUID = 3031382136413022043L;

    /**
     * 1:日报 2:周报 3:月报
     */
    private int aggreCode;
    @Column( length = 24 )
    private String dateStr;
    @Column( length = 64 )
    private String tid;

    public int getAggreCode()
    {
        return aggreCode;
    }

    public void setAggreCode( int aggreCode )
    {
        this.aggreCode = aggreCode;
    }

    public String getDateStr()
    {
        return dateStr;
    }

    public void setDateStr( String dateStr )
    {
        this.dateStr = dateStr;
    }

    public String getTid()
    {
        return tid;
    }

    public void setTid( String tid )
    {
        this.tid = tid;
    }

}
