package com.wnc.qqnews.jpa.entity;

import com.wnc.string.PatternUtil;
import com.wnc.wynews.jpa.entity.BaseEntity;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class QqUser extends BaseEntity {
    @Id
    @Column(length = 95)
    private String userid;
    private String nick;
    private String head;

    @OneToOne
    @JoinColumn(name = "certid")
    private QqCertInfo certinfo;
    private String uidex;
    private String thirdlogin;
    private String gender;
    private String viptype;
    private String region;
    private String country;
    private String province;
    private String city;

    public QqUser splitRegion() {
        if (StringUtils.isNotBlank(region)) {
            this.country = PatternUtil.getFirstPatternGroup(region, "^(.*?):");
            this.province = PatternUtil.getFirstPatternGroup(region, ":(.*?):");
            this.city = PatternUtil.getLastPatternGroup(region, "([^:]+)$");

            this.country = this.country.length() == 0 ? null : this.country;
            this.province = this.province.length() == 0 ? null : this.province;
            this.city = this.city.length() == 0 ? null : this.city;
        }
        return this;
    }

    public QqUser computeCertId() {
        if (this.certinfo != null) {
            this.certinfo.setCertid(userid + this.certinfo.getCertnick());
        }
        return this;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public QqCertInfo getCertinfo() {
        return certinfo;
    }

    public void setCertinfo(QqCertInfo certinfo) {
        this.certinfo = certinfo;
    }

    public String getUidex() {
        return uidex;
    }

    public void setUidex(String uidex) {
        this.uidex = uidex;
    }

    public String getThirdlogin() {
        return thirdlogin;
    }

    public void setThirdlogin(String thirdlogin) {
        this.thirdlogin = thirdlogin;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getViptype() {
        return viptype;
    }

    public void setViptype(String viptype) {
        this.viptype = viptype;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
