
package com.wnc.sboot1.spy.zhihu.active.target;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.wnc.sboot1.spy.zhihu.active.Actor;

/**
 * 用于发表了文章 MEMBER_CREATE_ARTICLE和 赞了文章MEMBER_VOTEUP_ARTICLE
 * 
 * @author Administrator
 */
@Entity
@Table(name = "ZH_ARTICLE")
public class Article extends Target {
	private String title;
	private String image_url;

	@Transient
	private Actor author;
	private String actor_id;

	@Transient
	private ZColumn column;
	private String column_id;
	@Transient
	private String content;
	@Transient
	private String excerpt;
	@Column(length = 4000)
	private String excerpt_new;
	private String excerpt_title;
	private String comment_count;
	private String voteup_count;
	private Long created;
	private Long updated;
	@Transient
	private String preview_text;
	private String preview_type;
	private String comment_permission;
	private String voting;

	public String getComment_count() {
		return comment_count;
	}

	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}

	public String getActor_id() {
		return actor_id;
	}

	public void setActor_id(String actor_id) {
		this.actor_id = actor_id;
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public Actor getAuthor() {
		return author;
	}

	public void setAuthor(Actor author) {
		this.author = author;
	}

	public String getExcerpt_new() {
		return excerpt_new;
	}

	public void setExcerpt_new(String excerpt_new) {
		this.excerpt_new = excerpt_new;
	}

	public String getVoteup_count() {
		return voteup_count;
	}

	public void setVoteup_count(String voteup_count) {
		this.voteup_count = voteup_count;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getExcerpt() {
		return excerpt;
	}

	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	public Long getUpdated() {
		return updated;
	}

	public void setUpdated(Long updated) {
		this.updated = updated;
	}

	public String getExcerpt_title() {
		return excerpt_title;
	}

	public void setExcerpt_title(String excerpt_title) {
		this.excerpt_title = excerpt_title;
	}

	public String getPreview_text() {
		return preview_text;
	}

	public void setPreview_text(String preview_text) {
		this.preview_text = preview_text;
	}

	public String getPreview_type() {
		return preview_type;
	}

	public void setPreview_type(String preview_type) {
		this.preview_type = preview_type;
	}

	public String getComment_permission() {
		return comment_permission;
	}

	public void setComment_permission(String comment_permission) {
		this.comment_permission = comment_permission;
	}

	public String getVoting() {
		return voting;
	}

	public void setVoting(String voting) {
		this.voting = voting;
	}

	public ZColumn getColumn() {
		return column;
	}

	public void setColumn(ZColumn column) {
		this.column = column;
	}

	public String getColumn_id() {
		return column_id;
	}

	public void setColumn_id(String column_id) {
		this.column_id = column_id;
	}

}
