package com.bridgei2i.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import com.bridgei2i.common.form.ListForm;

public class ReportBean extends ListForm {
	
	private String categoryIndex;
	private int planningCycleId;
	private String type;
	private String selectedTypeValue;
	private String selectedTypeVariable;
	private String selectedFilterIndex;
	private String isFromCategoryLevelSummary;
	private String isFromCategoryLevelSummaryFreeze;
	private String productId;
	private String startWeek;
	private String startYear;
	private String selectedPeriod;
	private String noOfWeeks;
	private List jsonStrList;
	private List historicalJsonStrList;
	private List jsonTableList;
	private String planogram;
	private String productDescription;
	private String productHierarchyII;
	private Integer tableColorRange;
	private String business;
	private String plClass;
	private String planningCycle;
	
	private int reportType;
	private String isFiltersSelected;
	public String getIsFiltersSelected() {
		return isFiltersSelected;
	}

	public void setIsFiltersSelected(String isFiltersSelected) {
		this.isFiltersSelected = isFiltersSelected;
	}

	public int getReportType() {
		return reportType;
	}

	public void setReportType(int reportType) {
		this.reportType = reportType;
	}

	public String getPlanogram() {
		return planogram;
	}

	public void setPlanogram(String planogram) {
		this.planogram = planogram;
	}

	public List getHistoricalJsonStrList() {
		return historicalJsonStrList;
	}

	public void setHistoricalJsonStrList(List historicalJsonStrList) {
		this.historicalJsonStrList = historicalJsonStrList;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getProductHierarchyII() {
		return productHierarchyII;
	}

	public void setProductHierarchyII(String productHierarchyII) {
		this.productHierarchyII = productHierarchyII;
	}
	@Transient
	private Map targetTableMap;
	
	
	private List<String> closedWeekList = new ArrayList<String>();
	
	private List planningCycleList = new ArrayList();
	
	private List overrideReportListObj=new ArrayList();
	
	
	public String getCategoryIndex() {
		return categoryIndex;
	}

	public int getPlanningCycleId() {
		return planningCycleId;
	}

	public String getType() {
		return type;
	}

	public String getSelectedTypeValue() {
		return selectedTypeValue;
	}

	public String getSelectedTypeVariable() {
		return selectedTypeVariable;
	}

	public String getSelectedFilterIndex() {
		return selectedFilterIndex;
	}

	public String getIsFromCategoryLevelSummary() {
		return isFromCategoryLevelSummary;
	}

	public String getIsFromCategoryLevelSummaryFreeze() {
		return isFromCategoryLevelSummaryFreeze;
	}

	public String getNoOfWeeks() {
		return noOfWeeks;
	}

	public List getJsonStrList() {
		return jsonStrList;
	}

	public List getJsonTableList() {
		return jsonTableList;
	}

	public Map getTargetTableMap() {
		return targetTableMap;
	}

	public void setCategoryIndex(String categoryIndex) {
		this.categoryIndex = categoryIndex;
	}

	public void setPlanningCycleId(int planningCycleId) {
		this.planningCycleId = planningCycleId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setSelectedTypeValue(String selectedTypeValue) {
		this.selectedTypeValue = selectedTypeValue;
	}

	public void setSelectedTypeVariable(String selectedTypeVariable) {
		this.selectedTypeVariable = selectedTypeVariable;
	}

	public void setSelectedFilterIndex(String selectedFilterIndex) {
		this.selectedFilterIndex = selectedFilterIndex;
	}

	public void setIsFromCategoryLevelSummary(String isFromCategoryLevelSummary) {
		this.isFromCategoryLevelSummary = isFromCategoryLevelSummary;
	}

	public void setIsFromCategoryLevelSummaryFreeze(
			String isFromCategoryLevelSummaryFreeze) {
		this.isFromCategoryLevelSummaryFreeze = isFromCategoryLevelSummaryFreeze;
	}

	public void setNoOfWeeks(String noOfWeeks) {
		this.noOfWeeks = noOfWeeks;
	}

	public void setJsonStrList(List jsonStrList) {
		this.jsonStrList = jsonStrList;
	}

	public void setJsonTableList(List jsonTableList) {
		this.jsonTableList = jsonTableList;
	}

	public void setTargetTableMap(Map targetTableMap) {
		this.targetTableMap = targetTableMap;
	}
	
	
	public List getOverrideReportListObj() {
		return overrideReportListObj;
	}
	public void setOverrideReportListObj(List overrideReportListObj) {
		this.overrideReportListObj = overrideReportListObj;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
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
	public List<String> getClosedWeekList() {
		return closedWeekList;
	}
	public void setClosedWeekList(List<String> closedWeekList) {
		this.closedWeekList = closedWeekList;
	}
	public List getPlanningCycleList() {
		return planningCycleList;
	}
	public void setPlanningCycleList(List planningCycleList) {
		this.planningCycleList = planningCycleList;
	}
	public String getSelectedPeriod() {
		return selectedPeriod;
	}
	public void setSelectedPeriod(String selectedPeriod) {
		this.selectedPeriod = selectedPeriod;
	}

	public Integer getTableColorRange() {
		return tableColorRange;
	}

	public void setTableColorRange(Integer tableColorRange) {
		this.tableColorRange = tableColorRange;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public String getPlClass() {
		return plClass;
	}

	public void setPlClass(String plClass) {
		this.plClass = plClass;
	}

	public String getPlanningCycle() {
		return planningCycle;
	}

	public void setPlanningCycle(String planningCycle) {
		this.planningCycle = planningCycle;
	}
	
	
	
}