package com.bridgei2i.vo;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@Entity
@Table(name="master_workflow_status")
public class MasterWorkflowStatus implements Cloneable,Serializable{
	

	@Id
	@Column(name="id")
	private Integer id;
	
	
	@Column(name="statusName")
	private String statusName;
	
	
	@Column(name="logicalName")
	private String logicalName;
	
	@Temporal(TemporalType.DATE)
	@Column(name="createdDate")
	private Date createdDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="updatedDate")
	private Date updatedDate;
	
	@OneToOne
	@JoinColumn(name = "id", referencedColumnName = "workflowStatusId", insertable=false, updatable=false)
    private PlanningCycle planningCycle;
	
	public PlanningCycle getPlanningCycle() {
		return planningCycle;
	}

	public void setPlanningCycle(PlanningCycle planningCycle) {
		this.planningCycle = planningCycle;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public MasterWorkflowStatus() {
		
	}

	public Integer getId() {
		return id;
	}

	public String getStatusName() {
		return statusName;
	}

	public String getLogicalName() {
		return logicalName;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	
	
	}
