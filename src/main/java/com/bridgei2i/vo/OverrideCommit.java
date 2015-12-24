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
@Table(name="override_commit")
public class OverrideCommit {

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
	
	@Column(name="commitStatusId")
	private int commitStatusId ;

	@Column(name="business")
	private String business;
	
	
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

	public int getCommitStatusId() {
		return commitStatusId;
	}

	public void setCommitStatusId(int commitStatusId) {
		this.commitStatusId = commitStatusId;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}
}