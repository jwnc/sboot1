
package com.wnc.sboot1.spy.zhihu.active.aggre;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class TargetAggreInfo extends TargetAggreKey
{
    private static final long serialVersionUID = 8195407694303106539L;

    @EmbeddedId
    private TargetAggreKey id;

    @Transient
    private TargetDesc targetDesc;

    private int cnt;

    public TargetDesc getTargetDesc()
    {
        return targetDesc;
    }

    public void setTargetDesc( TargetDesc targetDesc )
    {
        this.targetDesc = targetDesc;
    }

    public int getCnt()
    {
        return cnt;
    }

    public void setCnt( int cnt )
    {
        this.cnt = cnt;
    }

    public TargetAggreKey getId()
    {
        return id;
    }

    public void setId( TargetAggreKey id )
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "TargetAggreInfo [id=" + id + ", targetDesc=" + targetDesc
                + ", cnt=" + cnt + "]";
    }

}
