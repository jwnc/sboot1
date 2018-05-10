
package com.wnc.sboot1.spy.zhihu.active.target;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.wnc.sboot1.spy.zhihu.active.Actor;
import com.wnc.sboot1.spy.zhihu.active.CanComment;

@Entity
@Table(name = "ZH_ANSWER")
public class Answer extends Target {
	@Transient
	Question question;
	private String question_id;

	@Transient
	private Actor author;
	private String actor_id;

	private String comment_count;
	private Long created_time;
	private String updated_time;
	private String thumbnail;
	@Transient
	private String preview_text;

	private String thanks_count;
	private String preview_type;
	private String is_copyable;
	private String comment_permission;
	private String reshipment_settings;

	@Transient
	private String excerpt;
	@Column(length = 4000)
	private String excerpt_new;

	private String voteup_count;
	@Transient // 长度太长, 屏蔽掉
	private String content;
	@Transient
	private CanComment can_comment;

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Actor getAuthor() {
		return author;
	}

	public void setAuthor(Actor author) {
		this.author = author;
	}

	public String getComment_count() {
		return comment_count;
	}

	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}

	public Long getCreated_time() {
		return created_time;
	}

	public void setCreated_time(Long created_time) {
		this.created_time = created_time;
	}

	public String getUpdated_time() {
		return updated_time;
	}

	public void setUpdated_time(String updated_time) {
		this.updated_time = updated_time;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getPreview_text() {
		return preview_text;
	}

	public void setPreview_text(String preview_text) {
		this.preview_text = preview_text;
	}

	public String getThanks_count() {
		return thanks_count;
	}

	public void setThanks_count(String thanks_count) {
		this.thanks_count = thanks_count;
	}

	public String getPreview_type() {
		return preview_type;
	}

	public void setPreview_type(String preview_type) {
		this.preview_type = preview_type;
	}

	public String getIs_copyable() {
		return is_copyable;
	}

	public void setIs_copyable(String is_copyable) {
		this.is_copyable = is_copyable;
	}

	public String getComment_permission() {
		return comment_permission;
	}

	public void setComment_permission(String comment_permission) {
		this.comment_permission = comment_permission;
	}

	public String getExcerpt() {
		return excerpt;
	}

	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public CanComment getCan_comment() {
		return can_comment;
	}

	public void setCan_comment(CanComment can_comment) {
		this.can_comment = can_comment;
	}

	public String getReshipment_settings() {
		return reshipment_settings;
	}

	public void setReshipment_settings(String reshipment_settings) {
		this.reshipment_settings = reshipment_settings;
	}

	public String getActor_id() {
		return actor_id;
	}

	public void setActor_id(String actor_id) {
		this.actor_id = actor_id;
	}

	public String getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(String question_id) {
		this.question_id = question_id;
	}

}
