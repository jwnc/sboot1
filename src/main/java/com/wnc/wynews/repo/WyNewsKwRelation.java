package com.wnc.wynews.repo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WyNewsKwRelation {
    private String akey_link;
    @Id
    @Column( length = 95 ) // 主键最大长度767个字节
    private String keyname;

    public String getAkey_link() {
        return akey_link;
    }

    public void setAkey_link(String akey_link) {
        this.akey_link = akey_link;
    }

    public String getKeyname() {
        return keyname;
    }

    public void setKeyname(String keyname) {
        this.keyname = keyname;
    }
}
