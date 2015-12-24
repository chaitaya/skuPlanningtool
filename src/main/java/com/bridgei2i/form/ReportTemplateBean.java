package com.bridgei2i.form;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.bridgei2i.common.form.ListForm;
import com.bridgei2i.vo.TemplateChart;
import com.bridgei2i.vo.TemplateChartInfo;

public class ReportTemplateBean extends ListForm{

	private String categoryIndex;
	private String templateChartInfoIndex;
	private String selectedFilterId;
	private List filterValues1;
	private List filterValues2;
	private List filterValues3;
	private int scrollLocation;
	private String excelData;
	private String distributionList;
	private String selectedDistributionListId;
	private Integer selectedReportMetaDataListId;
	private String reportStatusComment;
	private String tabName;
	private List distributionListObj;
	private MultipartFile metaData;
	private List selectedTemplateChartInfoList = null;
	public String getCategoryIndex() {
		return categoryIndex;
	}

	public void setCategoryIndex(String categoryIndex) {
		this.categoryIndex = categoryIndex;
	}

	private String renameCategory;
	private String categoryName;
	

	public String getRenameCategory() {
		return renameCategory;
	}

	public void setRenameCategory(String renameCategory) {
		this.renameCategory = renameCategory;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	private TemplateChart templateChart;

	public TemplateChart getTemplateChart() {
		return templateChart;
	}

	public void setTemplateChart(TemplateChart templateChart) {
		this.templateChart = templateChart;
	}
	
	private String selectedDistributionId[];

	public String[] getSelectedDistributionId() {
		return selectedDistributionId;
	}

	public void setSelectedDistributionId(String[] selectedDistributionId) {
		this.selectedDistributionId = selectedDistributionId;
	}
	
	private String selectedDistributionIdStr;

	public String getSelectedDistributionIdStr() {
		return selectedDistributionIdStr;
	}

	public void setSelectedDistributionIdStr(String selectedDistributionIdStr) {
		this.selectedDistributionIdStr = selectedDistributionIdStr;
	}

	public String getSelectedFilterId() {
		return selectedFilterId;
	}

	public void setSelectedFilterId(String selectedFilterId) {
		this.selectedFilterId = selectedFilterId;
	}

	public List getFilterValues1() {
		return filterValues1;
	}

	public void setFilterValues1(List filterValues1) {
		this.filterValues1 = filterValues1;
	}

	public List getFilterValues2() {
		return filterValues2;
	}

	public void setFilterValues2(List filterValues2) {
		this.filterValues2 = filterValues2;
	}

	public List getFilterValues3() {
		return filterValues3;
	}

	public void setFilterValues3(List filterValues3) {
		this.filterValues3 = filterValues3;
	}

	public int getScrollLocation() {
		return scrollLocation;
	}

	public void setScrollLocation(int scrollLocation) {
		this.scrollLocation = scrollLocation;
	}

	public String getTemplateChartInfoIndex() {
		return templateChartInfoIndex;
	}

	public void setTemplateChartInfoIndex(String templateChartInfoIndex) {
		this.templateChartInfoIndex = templateChartInfoIndex;
	}

	public String getExcelData() {
		return excelData;
	}

	public void setExcelData(String excelData) {
		this.excelData = excelData;
	}

	public String getDistributionList() {
		return distributionList;
	}

	public void setDistributionList(String distributionList) {
		this.distributionList = distributionList;
	}

	public String getSelectedDistributionListId() {
		return selectedDistributionListId;
	}

	public void setSelectedDistributionListId(String selectedDistributionListId) {
		this.selectedDistributionListId = selectedDistributionListId;
	}

	public String getReportStatusComment() {
		return reportStatusComment;
	}

	public void setReportStatusComment(String reportStatusComment) {
		this.reportStatusComment = reportStatusComment;
	}

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public List getDistributionListObj() {
		return distributionListObj;
	}

	public void setDistributionListObj(List distributionListObj) {
		this.distributionListObj = distributionListObj;
	}

	public MultipartFile getMetaData() {
		return metaData;
	}
	public void setMetaData(MultipartFile metaData) {
		this.metaData = metaData;
	}

	public Integer getSelectedReportMetaDataListId() {
		return selectedReportMetaDataListId;
	}

	public void setSelectedReportMetaDataListId(Integer selectedReportMetaDataListId) {
		this.selectedReportMetaDataListId = selectedReportMetaDataListId;
	}

	public List getSelectedTemplateChartInfoList() {
		return selectedTemplateChartInfoList;
	}

	public void setSelectedTemplateChartInfoList(List selectedTemplateChartInfoList) {
		this.selectedTemplateChartInfoList = selectedTemplateChartInfoList;
	}

}