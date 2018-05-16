
package com.wnc.sboot1.spy.zhihu.active;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ActivityKey implements Serializable
{
    private static final long serialVersionUID = 3193767512098204462L;

    private Long created_time;
    @Column( length = 64 )
    private String actor_id;

    public Long getCreated_time()
    {
        return created_time;
    }

    public void setCreated_time( Long created_time )
    {
        this.created_time = created_time;
    }

    public String getActor_id()
    {
        return actor_id;
    }

    public void setActor_id( String actor_id )
    {
        this.actor_id = actor_id;
    }

}
