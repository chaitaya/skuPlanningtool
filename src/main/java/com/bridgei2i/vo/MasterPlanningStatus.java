package com.bridgei2i.vo;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@Entity
@Table(name="master_planning_status")
public class MasterPlanningStatus implements Cloneable,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7597395156909910135L;

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
	
	public MasterPlanningStatus() {
		
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

	/*@Override
	public String toString() {
		return "MasterPlanningStatus [id=" + id + ", statusName=" + statusName
				+ ", logicalName=" + logicalName + ", createdDate="
				+ createdDate + ", updatedDate=" + updatedDate + "]";
	}
	*/
	
	
	}
