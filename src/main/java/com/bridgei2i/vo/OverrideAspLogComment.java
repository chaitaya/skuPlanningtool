package com.bridgei2i.vo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="override_asp_log_comments")
public class OverrideAspLogComment {

	@Id
	@GenericGenerator(name="gen",strategy="increment")
	@GeneratedValue(generator="gen")
	@Column(name="id", unique = true, nullable = false)
	private Integer id;
	
	@OneToMany (fetch = FetchType.LAZY , mappedBy = "overrideAspLogObj")
	private Set<OverrideAspLog> overrideAspLogCommentObj = new HashSet<OverrideAspLog>(0);
	
	public Set<OverrideAspLog> getOverrideAspLogCommentObj() {
		return overrideAspLogCommentObj;
	}

	public void setOverrideAspLogCommentObj(
			Set<OverrideAspLog> overrideAspLogCommentObj) {
		this.overrideAspLogCommentObj = overrideAspLogCommentObj;
	}

	@Column(name="comment")
	private String comment;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
