
package com.wnc.sboot1.spy.zhihu.active.target;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.wnc.sboot1.spy.zhihu.active.Actor;

@Entity
@Table(name = "ZH_QUESTION")
public class Question extends Target {
	private String comment_count;
	// @Transient
	@ElementCollection
	private List<String> bound_topic_ids;// 相关主题

	@Transient
	private Actor author;
	private String actor_id;

	private Long created;
	private String title;
	@Column(length = 4000)
	private String excerpt;
	private String answer_count;
	private String is_following;
	private String follower_count;

	public String getComment_count() {
		return comment_count;
	}

	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}

	public List<String> getBound_topic_ids() {
		return bound_topic_ids;
	}

	public void setBound_topic_ids(List<String> bound_topic_ids) {
		this.bound_topic_ids = bound_topic_ids;
	}

	public Actor getAuthor() {
		return author;
	}

	public void setAuthor(Actor author) {
		this.author = author;
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getExcerpt() {
		return excerpt;
	}

	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	public String getAnswer_count() {
		return answer_count;
	}

	public void setAnswer_count(String answer_count) {
		this.answer_count = answer_count;
	}

	public String getIs_following() {
		return is_following;
	}

	public void setIs_following(String is_following) {
		this.is_following = is_following;
	}

	public String getFollower_count() {
		return follower_count;
	}

	public void setFollower_count(String follower_count) {
		this.follower_count = follower_count;
	}

	public String getActor_id() {
		return actor_id;
	}

	public void setActor_id(String actor_id) {
		this.actor_id = actor_id;
	}

}
