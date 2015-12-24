package com.bridgei2i.common.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class SingleMultiSeriesChartValueObject extends GenericChartValueObject{

	@Id
	@Column(name="id")
	@GeneratedValue
	private Integer id;
	protected Collection _detailValueObjs;
	protected Collection _nodeDetailValueObjs;
	private List header;
	private String xAxisName;
	private String yAxisName;
	private String pYAxisName;
	private String sYAxisName;
	private String modelName;
	private String categoryName;
	private Integer modelCategoryChartId;
	private List filterValues;
	private String mainVariable="";
	private String splitByVariable="";
	private String filter="";
	private String benchMark="";
	private String benchMarkVariable=null;
	private String filterColumn;
	private String templateChartInfoId;
	private String filePath;
	private String segmentName;
	private String functionName;
	private List segmentValues;
	private String xAxisColumnName;
	private String yAxisColumnName;
	private String xAxisMaxValue;
	private String xAxisMinValue;
	private String yAxisMaxValue;
	private String yAxisMinValue;
	private String positiveSentiment;
	private String neutralSentiment;
	private String negativeSentiment;
	private String divLineAlpha;
	private String wordCloudStr;
	private List bannerTableHeaderList;
	private List bannerTableDataList;
	private List bannerTableWeightList;
	private List bannerTablePercentageList;
	private List bannerTablePercentageWeightList;
	private String tableView;
	private List tableViewList;
	private List kdaTableList;
	private Integer functionId;
	private double importanceTrendLine;
	private double performanceTrendLine;
	private String referenceGroupValues;
	private List wordCloudList=null;
	private List categoryRawCommentsList=null;
	List highStrengthList =null;
	List highNeutralList=null;
	List highWeaknessList=null;
	List mediumStrengthList =null;
	List mediumNeutralList=null;
	List mediumWeaknessList=null;
	List lowStrengthList =null;
	List lowNeutralList=null;
	List lowWeaknessList=null;
	private String reportMetaDataId=null;
	private List categoryCommentList;
	private List categoryList;
	
	public Collection get_nodeDetailValueObjs() {
		return _nodeDetailValueObjs;
	}
	public void set_nodeDetailValueObjs(Collection _nodeDetailValueObjs) {
		this._nodeDetailValueObjs = _nodeDetailValueObjs;
	}
	public List getHeader() {
		return header;
	}
	public void setHeader(List header) {
		this.header = header;
	}
	public Collection get_detailValueObjs() {
		return _detailValueObjs;
	}
	public void set_detailValueObjs(Collection _detailValueObjs) {
		this._detailValueObjs = _detailValueObjs;
	}
	public String getxAxisName() {
		return xAxisName;
	}
	public void setxAxisName(String xAxisName) {
		this.xAxisName = xAxisName;
	}
	public String getyAxisName() {
		return yAxisName;
	}
	public void setyAxisName(String yAxisName) {
		this.yAxisName = yAxisName;
	}
	
	public String getpYAxisName() {
		return pYAxisName;
	}
	public void setpYAxisName(String pYAxisName) {
		this.pYAxisName = pYAxisName;
	}
	public String getsYAxisName() {
		return sYAxisName;
	}
	public void setsYAxisName(String sYAxisName) {
		this.sYAxisName = sYAxisName;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Integer getModelCategoryChartId() {
		return modelCategoryChartId;
	}
	public void setModelCategoryChartId(Integer modelCategoryChartId) {
		this.modelCategoryChartId = modelCategoryChartId;
	}
	public List getFilterValues() {
		return filterValues;
	}
	public void setFilterValues(List filterValues) {
		this.filterValues = filterValues;
	}

	public String getMainVariable() {
		return mainVariable;
	}
	public void setMainVariable(String mainVariable) {
		this.mainVariable = mainVariable;
	}
	public String getSplitByVariable() {
		return splitByVariable;
	}
	public void setSplitByVariable(String splitByVariable) {
		this.splitByVariable = splitByVariable;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public String getBenchMark() {
		return benchMark;
	}
	public void setBenchMark(String benchMark) {
		this.benchMark = benchMark;
	}
	
	public String getTemplateChartInfoId() {
		return templateChartInfoId;
	}
	public void setTemplateChartInfoId(String templateChartInfoId) {
		this.templateChartInfoId = templateChartInfoId;
	}
	public String getFilterColumn() {
		return filterColumn;
	}
	public void setFilterColumn(String filterColumn) {
		this.filterColumn = filterColumn;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getSegmentName() {
		return segmentName;
	}
	public void setSegmentName(String segmentName) {
		this.segmentName = segmentName;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public List getSegmentValues() {
		return segmentValues;
	}
	public void setSegmentValues(List segmentValues) {
		this.segmentValues = segmentValues;
	}
	public String getPositiveSentiment() {
		return positiveSentiment;
	}
	public void setPositiveSentiment(String positiveSentiment) {
		this.positiveSentiment = positiveSentiment;
	}
	public String getNeutralSentiment() {
		return neutralSentiment;
	}
	public void setNeutralSentiment(String neutralSentiment) {
		this.neutralSentiment = neutralSentiment;
	}
	public String getNegativeSentiment() {
		return negativeSentiment;
	}
	public void setNegativeSentiment(String negativeSentiment) {
		this.negativeSentiment = negativeSentiment;
	}
	public String getxAxisColumnName() {
		return xAxisColumnName;
	}
	public void setxAxisColumnName(String xAxisColumnName) {
		this.xAxisColumnName = xAxisColumnName;
	}
	public String getyAxisColumnName() {
		return yAxisColumnName;
	}
	public void setyAxisColumnName(String yAxisColumnName) {
		this.yAxisColumnName = yAxisColumnName;
	}
	public String getxAxisMaxValue() {
		return xAxisMaxValue;
	}
	public void setxAxisMaxValue(String xAxisMaxValue) {
		this.xAxisMaxValue = xAxisMaxValue;
	}
	public String getxAxisMinValue() {
		return xAxisMinValue;
	}
	public void setxAxisMinValue(String xAxisMinValue) {
		this.xAxisMinValue = xAxisMinValue;
	}
	public String getyAxisMaxValue() {
		return yAxisMaxValue;
	}
	public void setyAxisMaxValue(String yAxisMaxValue) {
		this.yAxisMaxValue = yAxisMaxValue;
	}
	public String getyAxisMinValue() {
		return yAxisMinValue;
	}
	public void setyAxisMinValue(String yAxisMinValue) {
		this.yAxisMinValue = yAxisMinValue;
	}
	public String getDivLineAlpha() {
		return divLineAlpha;
	}
	public void setDivLineAlpha(String divLineAlpha) {
		this.divLineAlpha = divLineAlpha;
	}
	public List getBannerTableWeightList() {
		return bannerTableWeightList;
	}
	public void setBannerTableWeightList(List bannerTableWeightList) {
		this.bannerTableWeightList = bannerTableWeightList;
	}

	public String getWordCloudStr() {
		return wordCloudStr;
	}
	public void setWordCloudStr(String wordCloudStr) {
		this.wordCloudStr = wordCloudStr;
	}

	public List getBannerTableHeaderList() {
		return bannerTableHeaderList;
	}
	public void setBannerTableHeaderList(List bannerTableHeaderList) {
		this.bannerTableHeaderList = bannerTableHeaderList;
	}
	public List getBannerTableDataList() {
		return bannerTableDataList;
	}
	public void setBannerTableDataList(List bannerTableDataList) {
		this.bannerTableDataList = bannerTableDataList;
	}
	public List getBannerTablePercentageList() {
		return bannerTablePercentageList;
	}
	public void setBannerTablePercentageList(List bannerTablePercentageList) {
		this.bannerTablePercentageList = bannerTablePercentageList;
	}
	public List getBannerTablePercentageWeightList() {
		return bannerTablePercentageWeightList;
	}
	public void setBannerTablePercentageWeightList(
			List bannerTablePercentageWeightList) {
		this.bannerTablePercentageWeightList = bannerTablePercentageWeightList;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTableView() {
		return tableView;
	}
	public void setTableView(String tableView) {
		this.tableView = tableView;
	}
	public Integer getFunctionId() {
		return functionId;
	}
	public void setFunctionId(Integer functionId) {
		this.functionId = functionId;
	}
	public double getImportanceTrendLine() {
		return importanceTrendLine;
	}
	public void setImportanceTrendLine(double importanceTrendLine) {
		this.importanceTrendLine = importanceTrendLine;
	}
	public double getPerformanceTrendLine() {
		return performanceTrendLine;
	}
	public void setPerformanceTrendLine(double performanceTrendLine) {
		this.performanceTrendLine = performanceTrendLine;
	}
	public List getHighStrengthList() {
		return highStrengthList;
	}
	public void setHighStrengthList(List highStrengthList) {
		this.highStrengthList = highStrengthList;
	}
	public List getHighNeutralList() {
		return highNeutralList;
	}
	public void setHighNeutralList(List highNeutralList) {
		this.highNeutralList = highNeutralList;
	}
	public List getHighWeaknessList() {
		return highWeaknessList;
	}
	public void setHighWeaknessList(List highWeaknessList) {
		this.highWeaknessList = highWeaknessList;
	}
	public List getMediumStrengthList() {
		return mediumStrengthList;
	}
	public void setMediumStrengthList(List mediumStrengthList) {
		this.mediumStrengthList = mediumStrengthList;
	}
	public List getMediumNeutralList() {
		return mediumNeutralList;
	}
	public void setMediumNeutralList(List mediumNeutralList) {
		this.mediumNeutralList = mediumNeutralList;
	}
	public List getMediumWeaknessList() {
		return mediumWeaknessList;
	}
	public void setMediumWeaknessList(List mediumWeaknessList) {
		this.mediumWeaknessList = mediumWeaknessList;
	}
	public List getLowStrengthList() {
		return lowStrengthList;
	}
	public void setLowStrengthList(List lowStrengthList) {
		this.lowStrengthList = lowStrengthList;
	}
	public List getLowNeutralList() {
		return lowNeutralList;
	}
	public void setLowNeutralList(List lowNeutralList) {
		this.lowNeutralList = lowNeutralList;
	}
	public List getLowWeaknessList() {
		return lowWeaknessList;
	}
	public void setLowWeaknessList(List lowWeaknessList) {
		this.lowWeaknessList = lowWeaknessList;
	}
	public String getReferenceGroupValues() {
		return referenceGroupValues;
	}
	public void setReferenceGroupValues(String referenceGroupValues) {
		this.referenceGroupValues = referenceGroupValues;
	}
	public List getTableViewList() {
		return tableViewList;
	}
	public void setTableViewList(List tableViewList) {
		this.tableViewList = tableViewList;
	}
	public List getKdaTableList() {
		return kdaTableList;
	}
	public void setKdaTableList(List kdaTableList) {
		this.kdaTableList = kdaTableList;
	}
	public List getWordCloudList() {
		return wordCloudList;
	}
	public void setWordCloudList(List wordCloudList) {
		this.wordCloudList = wordCloudList;
	}
	public String getReportMetaDataId() {
		return reportMetaDataId;
	}
	public void setReportMetaDataId(String reportMetaDataId) {
		this.reportMetaDataId = reportMetaDataId;
	}
	public String getBenchMarkVariable() {
		return benchMarkVariable;
	}
	public void setBenchMarkVariable(String benchMarkVariable) {
		this.benchMarkVariable = benchMarkVariable;
	}
	public List getCategoryRawCommentsList() {
		return categoryRawCommentsList;
	}
	public void setCategoryRawCommentsList(List categoryRawCommentsList) {
		this.categoryRawCommentsList = categoryRawCommentsList;
	}
	public List getCategoryCommentList() {
		return categoryCommentList;
	}
	public void setCategoryCommentList(List categoryCommentList) {
		this.categoryCommentList = categoryCommentList;
	}
	public List getCategoryList() {
		return categoryList;
	}
	public void setCategoryList(List categoryList) {
		this.categoryList = categoryList;
	}
	
}