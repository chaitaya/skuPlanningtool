package com.bridgei2i.vo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="override_units_log_comments")
public class OverrideUnitsLogComment {

	@Id
	@GenericGenerator(name="gen",strategy="increment")
	@GeneratedValue(generator="gen")
	@Column(name="id", unique = true, nullable = false)
	private Integer id;
	
	@OneToMany (fetch = FetchType.LAZY , mappedBy = "overrideUnitsLogObj")
	private Set<OverrideUnitsLog> overrideUnitsLogCommentObj = new HashSet<OverrideUnitsLog>(0);
	
	public Set<OverrideUnitsLog> getOverrideUnitsLogCommentObj() {
		return overrideUnitsLogCommentObj;
	}

	public void setOverrideUnitsLogCommentObj(Set<OverrideUnitsLog> overrideUnitsLogCommentObj) {
		this.overrideUnitsLogCommentObj = overrideUnitsLogCommentObj;
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
