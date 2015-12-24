package com.bridgei2i.vo;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="override_category_level_commit")
public class OverrideCategoryLevelCommit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id",unique=true, nullable = false)
	private Integer id;
	
	
	@Column(name="planningCycleId")
	private int planningCycleId;
	
	
	@Column(name="userId")
	private int userId;
	
	@Column(name="productId")
	private String productId;
	
	@Column(name="commitFlag")
	private int commitFlag ;

	public Integer getId() {
		return id;
	}

	public int getPlanningCycleId() {
		return planningCycleId;
	}

	public int getUserId() {
		return userId;
	}

	public String getProductId() {
		return productId;
	}

	public int getCommitFlag() {
		return commitFlag;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPlanningCycleId(int planningCycleId) {
		this.planningCycleId = planningCycleId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setCommitFlag(int commitFlag) {
		this.commitFlag = commitFlag;
	}

	
}
