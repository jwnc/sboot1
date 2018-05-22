
package com.wnc.sboot1.spy.zhihu.active.target;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "ZH_TARGET")
@Inheritance(strategy = InheritanceType.JOINED)
public class Target
{
    @Id
    @Column(length = 64)
    private String tid;// tid为id+type的组合形式

    @Transient
    private String id; // json自带, 无需保存

    private String type;

    private String info;// 描述信息

    public String generateTid()
    {
        return this.tid = this.id + "+" + this.type;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

    public String getTid()
    {
        return tid;
    }

    public void setTid(String tid)
    {
        this.tid = tid;
    }

}
