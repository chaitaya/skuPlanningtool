package com.bridgei2i.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="category_scorecard_rollup_mapping")
public class CategoryScorecardRollupMapping {
	@Id
	@Column(name="id")
	@GeneratedValue
	private Integer id;
	
	@Column(name="scorecardRollup")
	private String scorecardRollup;
	
	@Column(name="categoryName")
	private String categoryName;
	
	@Column(name="createdDate")
	private Date createdDate;
	
	@Column(name="updatedDate")
	private Date updatedDate;

	public Integer getId() {
		return id;
	}

	public String getScoreCardRollup() {
		return scorecardRollup;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setScoreCardRollup(String scoreCardRollup) {
		this.scorecardRollup = scoreCardRollup;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

}