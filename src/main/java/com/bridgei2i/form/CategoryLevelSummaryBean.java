package com.bridgei2i.form;

import java.util.List;

public class CategoryLevelSummaryBean extends ListForm{
	
	private String category;
	private String categoryManagerName;
	private String categoryStatus;
	private int categoryDirectorReleaseFlag;
	private int categoryDirectorCommitFlag;
	private int planningCycleId;
	private String workFlowStatus;
	public String getWorkFlowStatus() {
		return workFlowStatus;
	}
	public void setWorkFlowStatus(String workFlowStatus) {
		this.workFlowStatus = workFlowStatus;
	}
	private int commitLevel;
	private List categoryStatusList;
	private int categoryCommitFlag;
	public String getCategory() {
		return category;
	}
	public String getCategoryManagerName() {
		return categoryManagerName;
	}
	public String getCategoryStatus() {
		return categoryStatus;
	}
	public int getCategoryDirectorReleaseFlag() {
		return categoryDirectorReleaseFlag;
	}
	public int getCategoryDirectorCommitFlag() {
		return categoryDirectorCommitFlag;
	}
	public int getPlanningCycleId() {
		return planningCycleId;
	}
	public int getCommitLevel() {
		return commitLevel;
	}
	public List getCategoryStatusList() {
		return categoryStatusList;
	}
	public int getCategoryCommitFlag() {
		return categoryCommitFlag;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public void setCategoryManagerName(String categoryManagerName) {
		this.categoryManagerName = categoryManagerName;
	}
	public void setCategoryStatus(String categoryStatus) {
		this.categoryStatus = categoryStatus;
	}
	public void setCategoryDirectorReleaseFlag(int categoryDirectorReleaseFlag) {
		this.categoryDirectorReleaseFlag = categoryDirectorReleaseFlag;
	}
	public void setCategoryDirectorCommitFlag(int categoryDirectorCommitFlag) {
		this.categoryDirectorCommitFlag = categoryDirectorCommitFlag;
	}
	public void setPlanningCycleId(int planningCycleId) {
		this.planningCycleId = planningCycleId;
	}
	public void setCommitLevel(int commitLevel) {
		this.commitLevel = commitLevel;
	}
	public void setCategoryStatusList(List categoryStatusList) {
		this.categoryStatusList = categoryStatusList;
	}
	public void setCategoryCommitFlag(int categoryCommitFlag) {
		this.categoryCommitFlag = categoryCommitFlag;
	}
	
}
