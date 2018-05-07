
package com.wnc.sboot1.spy.zuqiu;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Zb8News {
	@Id
	@Column(length = 95) // 主键最大长度767个字节
	private String pinglun;

	private String url;
	private String createtime;
	private String from_name;
	private String from_url;
	private String thumbnail;
	private String title;
	@Column(length = 1000)
	private String lable;

	private String filename;
	private String color;
	@Transient
	private String describe;
	private String indextitle;
	// private String"2018_04_06-news-zuqiu-5ac7979acb456"
	private String porder;
	private String position;
	private String saishiid;
	private String shortTitle;
	private String type;
	private String updatetime;
	private String way;
	private Date updateDate;

	@Override
	public String toString() {
		return "Zb8News [title=" + title + ", url=" + url + "]";
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFrom_name() {
		return from_name;
	}

	public void setFrom_name(String from_name) {
		this.from_name = from_name;
	}

	public String getFrom_url() {
		return from_url;
	}

	public void setFrom_url(String from_url) {
		this.from_url = from_url;
	}

	public String getIndextitle() {
		return indextitle;
	}

	public void setIndextitle(String indextitle) {
		this.indextitle = indextitle;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public String getPinglun() {
		return pinglun;
	}

	public void setPinglun(String pinglun) {
		this.pinglun = pinglun;
	}

	public String getPorder() {
		return porder;
	}

	public void setPorder(String porder) {
		this.porder = porder;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getSaishiid() {
		return saishiid;
	}

	public void setSaishiid(String saishiid) {
		this.saishiid = saishiid;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWay() {
		return way;
	}

	public void setWay(String way) {
		this.way = way;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}
