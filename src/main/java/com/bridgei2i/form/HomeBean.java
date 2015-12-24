package com.bridgei2i.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bridgei2i.common.form.CombinationChartForm;
import com.bridgei2i.vo.PlanningCycleVO;


public class HomeBean extends CombinationChartForm implements Serializable {
	
	private static final long serialVersionUID = -1793212295578972622L;
	
	private int id;
	private String startWeek;
	private String startYear;
	private Date closedDate;
	private String workFlowStatus;
	private int planningStatusId;
	private Date createDate;
	private Date updateDate;	
	private String status;
	private String activWeekId;
	private String selectedActiveWeek;
	List<String> lists=new ArrayList();
	private List<String> activeWeekList = new ArrayList<String>();
	private List planningCycleList = new ArrayList();
	private Integer planningCycleId ;
	private String logicalName;
	private String selectedFilterIndex;
	
	
	public Integer getPlanningCycleId() {
		return planningCycleId;
	}

	public void setPlanningCycleId(Integer planningCycleId) {
		this.planningCycleId = planningCycleId;
	}

	public String getSelectedActiveWeek() {
		return selectedActiveWeek;
	}

	public void setSelectedActiveWeek(String selectedActiveWeek) {
		this.selectedActiveWeek = selectedActiveWeek;
	}
	
	public String getActivWeekId() {
		return activWeekId;
	}

	public void setActivWeekId(String activWeekId) {
		this.activWeekId = activWeekId;
	}


	public List<String> getActiveWeekList() {
		return activeWeekList;
	}

	public void setActiveWeekList(List<String> activeWeekList) {
		this.activeWeekList = activeWeekList;
	}

	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStartWeek() {
		return startWeek;
	}

	public void setStartWeek(String startWeek) {
		this.startWeek = startWeek;
	}

	public String getStartYear() {
		return startYear;
	}

	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}

	public Date getclosedDate() {
		return closedDate;
	}

	public void setclosedDate(Date closedDate) {
		this.closedDate = closedDate;
	}

	public String getWorkFlowStatus() {
		return workFlowStatus;
	}

	public void setWorkFlowStatus(String workFlowStatus) {
		this.workFlowStatus = workFlowStatus;
	}

	public int getPlanningStatusId() {
		return planningStatusId;
	}

	public void setPlanningStatusId(int planningStatusId) {
		this.planningStatusId = planningStatusId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	
	

	public Date getClosedDate() {
		return closedDate;
	}

	public void setClosedDate(Date closedDate) {
		this.closedDate = closedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

	public List<String> getLists() {
		return lists;
	}

	public void setLists(List<String> lists) {
		this.lists = lists;
	}

	public List getPlanningCycleList() {
		return planningCycleList;
	}

	public void setPlanningCycleList(List planningCycleList) {
		this.planningCycleList = planningCycleList;
	}

	public String getLogicalName() {
		return logicalName;
	}

	public void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
	}

	public String getSelectedFilterIndex() {
		return selectedFilterIndex;
	}

	public void setSelectedFilterIndex(String selectedFilterIndex) {
		this.selectedFilterIndex = selectedFilterIndex;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	

}
