package com.bridgei2i.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "planning_cycle")
public class PlanningCycle implements Cloneable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4086472929993932905L;

	@Id
	@Column(name = "id")
	@GeneratedValue
	private Integer id;

	@Column(name = "startWeek")
	private String startWeek;

	@Column(name = "startYear")
	private String startYear;

	@Column(name = "closedDate")
	private Date closedDate;
	
	@Transient
	private String weekYear;


	@Column(name = "workflowStatusId")
	private int workflowStatusId;
	
	@Transient
	private String workFlowStatusName;
	
	@Transient
	private String cycleStartDate;
	/*@OneToOne
	@JoinColumn(name = "planningStatusId")
	private MasterPlanningStatus masterPlanningStatus;*/
	
	public int getWorkflowStatusId() {
		return workflowStatusId;
	}

	public void setWorkflowStatusId(int workflowStatusId) {
		this.workflowStatusId = workflowStatusId;
	}

	@OneToOne(mappedBy = "planningCycle", fetch = FetchType.EAGER)
	MasterWorkflowStatus masterWorkflowStatus;
	

	@Column(name = "planningStatusId")
	private Integer planningStatusId;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "createDate")
	private Date createDate;

	@Transient
	private String statusName;
	
	@Transient
	private String logicalName;
	
	public PlanningCycle() {

	}

	public Integer getId() {
		return id;
	}

	public String getStartWeek() {
		return startWeek;
	}

	public Date getClosedDate() {
		return closedDate;
	}


	/*public MasterPlanningStatus getMasterPlanningStatus() {
		return masterPlanningStatus;
	}
	public void setMasterPlanningStatus(
			MasterPlanningStatus masterPlanningStatus) {
		this.masterPlanningStatus = masterPlanningStatus;
	}*/

	public void setId(Integer id) {
		this.id = id;
	}

	public void setStartWeek(String startWeek) {
		this.startWeek = startWeek;
	}

	public void setClosedDate(Date closedDate) {
		this.closedDate = closedDate;
	}	

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getStartYear() {
		return startYear;
	}

	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getLogicalName() {
		return logicalName;
	}

	public void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
	}

	public Integer getPlanningStatusId() {
		return planningStatusId;
	}

	public void setPlanningStatusId(Integer planningStatusId) {
		this.planningStatusId = planningStatusId;
	}

	public String getWorkFlowStatusName() {
		return workFlowStatusName;
	}

	public void setWorkFlowStatusName(String workFlowStatusName) {
		this.workFlowStatusName = workFlowStatusName;
	}

	public MasterWorkflowStatus getMasterWorkflowStatus() {
		return masterWorkflowStatus;
	}

	public void setMasterWorkflowStatus(MasterWorkflowStatus masterWorkflowStatus) {
		this.masterWorkflowStatus = masterWorkflowStatus;
	}

	public String getWeekYear() {
		return weekYear;
	}

	public void setWeekYear(String weekYear) {
		this.weekYear = weekYear;
	}

	public String getCycleStartDate() {
		return cycleStartDate;
	}

	public void setCycleStartDate(String cycleStartDate) {
		this.cycleStartDate = cycleStartDate;
	}
	
	
	/*@Override
	public String toString() {
		return "PlanningCycle [id=" + id + ", startWeek=" + startWeek
				+ ", startYear=" + startYear + ", closedDate=" + closedDate
				+ ", workFlowStatus=" + workFlowStatus
				+ ", masterPlanningStatus=" + masterPlanningStatus
				+ ", createDate=" + createDate + "]";
	}*/

}
