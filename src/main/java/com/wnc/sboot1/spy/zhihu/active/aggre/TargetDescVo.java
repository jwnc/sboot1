
package com.wnc.sboot1.spy.zhihu.active.aggre;

import javax.persistence.Entity;

@Entity
public class TargetDescVo extends TargetDesc
{

    public int cnt;

    public int getCnt()
    {
        return cnt;
    }

    public void setCnt( int cnt )
    {
        this.cnt = cnt;
    }

}
