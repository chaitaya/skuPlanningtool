package com.bridgei2i.form;

import java.util.List;

import javax.persistence.Transient;

import org.springframework.context.annotation.Scope;

import com.bridgei2i.common.form.ListForm;
import com.bridgei2i.common.vo.PageTemplate;

public class PlanningCycleBean extends ListForm {
	
	private PageTemplate pageTemplate;
	private String categoryIndex;
	private int planningCycleId;
	private String type;
	private String selectedTypeValue;
	private String selectedTypeVariable;
	private int isFromSaveOverride=0;
	private String planogram;
	private String productDescription;
	private String productHierarchyII;
	private String selectedFilterIndex;
	private String isFromCategoryLevelSummary;
	private String isFromCategoryLevelSummaryFreeze;
	private int enableOverride=1;
	private String selectedPeriod;
	private String business;
	private int overrideButtonFlag=1;
	
	@Transient
	private List jsonStrList;
	
	@Transient
	private List jsonTableList;
	
	
	private List overrideForecastUnitsList;
	private List overrideForecastASPList;
	
	private String overrideForecastUnitsComment;
	private String overrideForecastASPComment;
	
	public PageTemplate getPageTemplate() {
		return pageTemplate;
	}

	public void setPageTemplate(PageTemplate pageTemplate) {
		this.pageTemplate = pageTemplate;
	}

	public String getCategoryIndex() {
		return categoryIndex;
	}

	public void setCategoryIndex(String categoryIndex) {
		this.categoryIndex = categoryIndex;
	}

	public int getPlanningCycleId() {
		return planningCycleId;
	}

	public void setPlanningCycleId(int planningCycleId) {
		this.planningCycleId = planningCycleId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSelectedTypeValue() {
		return selectedTypeValue;
	}

	public String getPlanogram() {
		return planogram;
	}

	public void setPlanogram(String planogram) {
		this.planogram = planogram;
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

	public void setSelectedTypeValue(String selectedTypeValue) {
		this.selectedTypeValue = selectedTypeValue;
	}

	public List getJsonStrList() {
		return jsonStrList;
	}

	public void setJsonStrList(List jsonStrList) {
		this.jsonStrList = jsonStrList;
	}

	public List getJsonTableList() {
		return jsonTableList;
	}

	public void setJsonTableList(List jsonTableList) {
		this.jsonTableList = jsonTableList;
	}
	
	public List getOverrideForecastUnitsList() {
		return overrideForecastUnitsList;
	}

	public void setOverrideForecastUnitsList(List overrideForecastUnitsList) {
		this.overrideForecastUnitsList = overrideForecastUnitsList;
	}

	public List getOverrideForecastASPList() {
		return overrideForecastASPList;
	}

	public void setOverrideForecastASPList(List overrideForecastASPList) {
		this.overrideForecastASPList = overrideForecastASPList;
	}

	public String getOverrideForecastUnitsComment() {
		return overrideForecastUnitsComment;
	}

	public void setOverrideForecastUnitsComment(String overrideForecastUnitsComment) {
		this.overrideForecastUnitsComment = overrideForecastUnitsComment;
	}

	public String getOverrideForecastASPComment() {
		return overrideForecastASPComment;
	}

	public void setOverrideForecastASPComment(String overrideForecastASPComment) {
		this.overrideForecastASPComment = overrideForecastASPComment;
	}

	public String getSelectedFilterIndex() {
		return selectedFilterIndex;
	}

	public void setSelectedFilterIndex(String selectedFilterIndex) {
		this.selectedFilterIndex = selectedFilterIndex;
	}

	public int getIsFromSaveOverride() {
		return isFromSaveOverride;
	}

	public void setIsFromSaveOverride(int isFromSaveOverride) {
		this.isFromSaveOverride = isFromSaveOverride;
	}

	public String getIsFromCategoryLevelSummary() {
		return isFromCategoryLevelSummary;
	}

	public void setIsFromCategoryLevelSummary(String isFromCategoryLevelSummary) {
		this.isFromCategoryLevelSummary = isFromCategoryLevelSummary;
	}

	public String getIsFromCategoryLevelSummaryFreeze() {
		return isFromCategoryLevelSummaryFreeze;
	}

	public void setIsFromCategoryLevelSummaryFreeze(
			String isFromCategoryLevelSummaryFreeze) {
		this.isFromCategoryLevelSummaryFreeze = isFromCategoryLevelSummaryFreeze;
	}

	public int getEnableOverride() {
		return enableOverride;
	}

	public void setEnableOverride(int enableOverride) {
		this.enableOverride = enableOverride;
	}

	public String getSelectedTypeVariable() {
		return selectedTypeVariable;
	}

	public void setSelectedTypeVariable(String selectedTypeVariable) {
		this.selectedTypeVariable = selectedTypeVariable;
	}

	public String getSelectedPeriod() {
		return selectedPeriod;
	}

	public void setSelectedPeriod(String selectedPeriod) {
		this.selectedPeriod = selectedPeriod;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public int getOverrideButtonFlag() {
		return overrideButtonFlag;
	}

	public void setOverrideButtonFlag(int overrideButtonFlag) {
		this.overrideButtonFlag = overrideButtonFlag;
	}

	
	
	
}