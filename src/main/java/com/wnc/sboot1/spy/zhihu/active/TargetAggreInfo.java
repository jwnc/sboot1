
package com.wnc.sboot1.spy.zhihu.active;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

public @Entity class TargetAggreInfo {
	@Id
	@Column(length = 64)
	private String tid;
	private String type;
	private String title;
	private String description;
	private String url;
	private int cnt;
	/**
	 * 1:日报 2:周报 3:月报
	 */
	private int aggreCode;

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "TargetAggreInfo [tid=" + tid + ", title=" + title + ", description=" + description + ", type=" + type
				+ ", cnt=" + cnt + "]";
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getAggreCode() {
		return aggreCode;
	}

	public void setAggreCode(int aggreCode) {
		this.aggreCode = aggreCode;
	}

}
