package com.bridgei2i.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlanningCycleVO implements Serializable {

	private static final long serialVersionUID = 993116571668171610L;

	private Date closedDate;
	private String startWeek;
	private String startYear;	
	private Integer id;
	private String status;
	private String workFlowStatus;
	private List<String> activeWeekList = new ArrayList<String>();

	public List<String> getActiveWeekList() {
		return activeWeekList;
	}

	public void setActiveWeekList(List<String> activeWeekList) {
		this.activeWeekList = activeWeekList;
	}

	public PlanningCycleVO() {
		// TODO Auto-generated constructor stub
	}

	public Date getClosedDate() {
		return closedDate;
	}

	public void setClosedDate(Date closedDate) {
		this.closedDate = closedDate;
	}

	public String getStartWeek() {
		return startWeek;
	}

	public void setStartWeek(String startWeek) {
		this.startWeek = startWeek;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getWorkFlowStatus() {
		return workFlowStatus;
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

	public void setWorkFlowStatus(String workFlowStatus) {
		this.workFlowStatus = workFlowStatus;
	}

	/*@Override
	public String toString() {
		return "PlanningCycleVO [closedDate=" + closedDate + ", startWeek="
				+ startWeek + ", startYear=" + startYear + ", id=" + id
				+ ", status=" + status + ", workFlowStatus=" + workFlowStatus
				+ "]";
	}*/

	
	

}
