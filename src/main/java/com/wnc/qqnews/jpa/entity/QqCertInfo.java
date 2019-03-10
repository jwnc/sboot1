package com.wnc.qqnews.jpa.entity;

import com.wnc.wynews.jpa.entity.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class QqCertInfo extends BaseEntity {
    @Id
    @Column(length = 95)
    private String certid;
    private String certnick;
    private String certhead;
    private String certinfo;
    private String certidentityid;
    private String certappids;


    public String getCertid() {
        return certid;
    }

    public void setCertid(String certid) {
        this.certid = certid;
    }

    public String getCerthead() {
        return certhead;
    }

    public void setCerthead(String certhead) {
        this.certhead = certhead;
    }

    public String getCertinfo() {
        return certinfo;
    }

    public void setCertinfo(String certinfo) {
        this.certinfo = certinfo;
    }

    public String getCertnick() {
        return certnick;
    }

    public void setCertnick(String certnick) {
        this.certnick = certnick;
    }

    public String getCertidentityid() {
        return certidentityid;
    }

    public void setCertidentityid(String certidentityid) {
        this.certidentityid = certidentityid;
    }

    public String getCertappids() {
        return certappids;
    }

    public void setCertappids(String certappids) {
        this.certappids = certappids;
    }
}
