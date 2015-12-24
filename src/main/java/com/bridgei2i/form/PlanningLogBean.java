package com.bridgei2i.form;
import java.util.List;

import com.bridgei2i.common.form.ListForm;
import com.bridgei2i.common.form.SkuListForm;

public class PlanningLogBean extends ListForm {

	private String category;
	private String model;
	private String businessType;
	private String region;
	private String status;
	private String productManagerOverride;
	private String categoryManagerOverride;
	private String commitInput;
	private int planningCycleId;
	private SkuListForm skuListForm;
	private String selectedFilterIndex;
	private String productDescription;
	private String productHierarchyII;
	private String planogram;
	private int categoryDirectorReleaseFlag;
	private int categoryDirectorCommitFlag;
	private int categoryStatus;
	private List categorySummaryList;
	private List skuForecastList;
	private List forecastPeriodList;
	private String skuList;
	private int forecastMetric;
	private String isFromPlanningLog;
	private String selectedSkuList;
	private String isfilterSelected;
	
	public String getCategory() {
		return category;
	}
	public String getModel() {
		return model;
	}

	public String getBusinessType() {
		return businessType;
	}
	public String getRegion() {
		return region;
	}
	public String getStatus() {
		return status;
	}
	public String getProductManagerOverride() {
		return productManagerOverride;
	}
	public String getCategoryManagerOverride() {
		return categoryManagerOverride;
	}
	public String getCommitInput() {
		return commitInput;
	}
	public int getPlanningCycleId() {
		return planningCycleId;
	}
	public SkuListForm getSkuListForm() {
		return skuListForm;
	}
	public String getSelectedFilterIndex() {
		return selectedFilterIndex;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public String getProductHierarchyII() {
		return productHierarchyII;
	}
	public String getPlanogram() {
		return planogram;
	}
	public int getCategoryDirectorReleaseFlag() {
		return categoryDirectorReleaseFlag;
	}
	public int getCategoryDirectorCommitFlag() {
		return categoryDirectorCommitFlag;
	}
	public int getCategoryStatus() {
		return categoryStatus;
	}
	public List getCategorySummaryList() {
		return categorySummaryList;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setProductManagerOverride(String productManagerOverride) {
		this.productManagerOverride = productManagerOverride;
	}
	public void setCategoryManagerOverride(String categoryManagerOverride) {
		this.categoryManagerOverride = categoryManagerOverride;
	}
	public void setCommitInput(String commitInput) {
		this.commitInput = commitInput;
	}
	public void setPlanningCycleId(int planningCycleId) {
		this.planningCycleId = planningCycleId;
	}
	public void setSkuListForm(SkuListForm skuListForm) {
		this.skuListForm = skuListForm;
	}
	public void setSelectedFilterIndex(String selectedFilterIndex) {
		this.selectedFilterIndex = selectedFilterIndex;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public void setProductHierarchyII(String productHierarchyII) {
		this.productHierarchyII = productHierarchyII;
	}
	public void setPlanogram(String planogram) {
		this.planogram = planogram;
	}
	public void setCategoryDirectorReleaseFlag(int categoryDirectorReleaseFlag) {
		this.categoryDirectorReleaseFlag = categoryDirectorReleaseFlag;
	}
	public void setCategoryDirectorCommitFlag(int categoryDirectorCommitFlag) {
		this.categoryDirectorCommitFlag = categoryDirectorCommitFlag;
	}
	public void setCategoryStatus(int categoryStatus) {
		this.categoryStatus = categoryStatus;
	}
	public void setCategorySummaryList(List categorySummaryList) {
		this.categorySummaryList = categorySummaryList;
	}
	public List getSkuForecastList() {
		return skuForecastList;
	}
	public void setSkuForecastList(List skuForecastList) {
		this.skuForecastList = skuForecastList;
	}
	public List getForecastPeriodList() {
		return forecastPeriodList;
	}
	public void setForecastPeriodList(List forecastPeriodList) {
		this.forecastPeriodList = forecastPeriodList;
	}
	public String getSkuList() {
		return skuList;
	}
	public void setSkuList(String skuList) {
		this.skuList = skuList;
	}
	public int getForecastMetric() {
		return forecastMetric;
	}
	public void setForecastMetric(int forecastMetric) {
		this.forecastMetric = forecastMetric;
	}
	public String getIsFromPlanningLog() {
		return isFromPlanningLog;
	}
	public String getSelectedSkuList() {
		return selectedSkuList;
	}
	public void setSelectedSkuList(String selectedSkuList) {
		this.selectedSkuList = selectedSkuList;
	}
	public void setIsFromPlanningLog(String isFromPlanningLog) {
		this.isFromPlanningLog = isFromPlanningLog;
	}
	public String getIsfilterSelected() {
		return isfilterSelected;
	}
	public void setIsfilterSelected(String isfilterSelected) {
		this.isfilterSelected = isfilterSelected;
	}
	
}