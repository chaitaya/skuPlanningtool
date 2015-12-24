package com.bridgei2i.form;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bridgei2i.common.vo.SingleMultiSeriesChartValueObject;
import com.bridgei2i.vo.TemplateChart;
import com.bridgei2i.vo.TemplateChartCategory;
@Entity
@Table(name="templatechartinfo")
public class WorkspaceBean implements Cloneable,Serializable {

	@Id
	@Column(name="id")
	@GeneratedValue
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="templateChartCategoryId")
	private TemplateChartCategory templateChartCategory;
	
	@Column(name="chartTitle")
	private String chartTitle="Untitled";
	
	@Column(name="functionId")
	private Integer functionId;
	
	@Column(name="chartTypeId")
	private String chartTypeId;
	

	@Column(name="benchMark")
	private String benchMark;
	
	@Column(name="xAxis")
	private String xAxis;
	
	@Transient
	private String[] xAxisArray;
	
	@Column(name="yAxis")
	private String yAxis;
	
	@Transient
	private String[] yAxisArray;
	
	
	@Column(name="filter")
	private String filter;
	
	@Column(name="weightVariable")
	private String weightVariable;
	
	@Column(name="crossTabValue")
	private String crossTabValue;
	
	
	@Column(name="createdDate")
	private Date createdDate;
	
	@Column(name="updatedDate")
	private Date updatedDate;
	
	@Column(name="createdBy")
	private Long createdBy;
	
	@Column(name="updatedBy")
	private Long updatedBy;

	@Transient
	private boolean deleted;
	
	@Transient
	private SingleMultiSeriesChartValueObject singleMultiSeriesChartValueObject;
	
	@Transient
	private List chartTypesList;
	protected Collection _detailValueObjs;
	protected Collection _nodeDetailValueObjs;
	private List header;
	private String chartType;
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
	private String caption;
	private List bannerTableHeaderList;
	private List bannerTableDataList;
	private List bannerTableWeightList;
	private List bannerTablePercentageList;
	private List bannerTablePercentageWeightList;
	
	private int meanIncl;
	private int meanExcl;
	private int medianIncl;
	private int medianExcl;
	private int numberOfCharts;
	private String excelData;
	private String colorCode;
	
	@Transient
	private String isOverall;
	
	public String getIsOverall() {
		return isOverall;
	}

	public void setIsOverall(String isOverall) {
		this.isOverall = isOverall;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	private TemplateChart templateChart;

	public TemplateChart getTemplateChart() {
		return templateChart;
	}

	public void setTemplateChart(TemplateChart templateChart) {
		this.templateChart = templateChart;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public String getChartTitle() {
		return chartTitle;
	}

	public void setChartTitle(String chartTitle) {
		this.chartTitle = chartTitle;
	}

	public void setFunctionId(Integer functionId) {
		this.functionId = functionId;
	}

	public Integer getFunctionId() {
		return functionId;
	}

	public String getChartTypeId() {
		return chartTypeId;
	}

	public void setChartTypeId(String chartTypeId) {
		this.chartTypeId = chartTypeId;
	}

	public String getBenchMark() {
		return benchMark;
	}

	public void setBenchMark(String benchMark) {
		this.benchMark = benchMark;
	}

	public String getxAxis() {
		return xAxis;
	}

	public void setxAxis(String xAxis) {
		if(xAxis !=null){
			this.xAxisArray = xAxis.split(",");
		}
		this.xAxis = xAxis;
	}

	public String getyAxis() {
		return yAxis;
	}

	public void setyAxis(String yAxis) {
		if(yAxis !=null){
			this.yAxisArray = yAxis.split(",");
		}
		this.yAxis = yAxis;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
	
	public String getWeightVariable() {
		return weightVariable;
	}

	public void setWeightVariable(String weightVariable) {
		this.weightVariable = weightVariable;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public TemplateChartCategory getTemplateChartCategory() {
		return templateChartCategory;
	}

	public void setTemplateChartCategory(TemplateChartCategory templateChartCategory) {
		this.templateChartCategory = templateChartCategory;
	}

	public String[] getxAxisArray() {
		return xAxisArray;
	}

	public void setxAxisArray(String[] xAxisArray) {
		this.xAxisArray = xAxisArray;
		if(xAxisArray!=null){
			int len = xAxisArray.length;
			String str="";
			for(int i=0;i<len;i++){
				String val = xAxisArray[i];
				str=str+val;
				if(i+1<len){
					str=str+",";
				}
			}
			this.xAxis=str;
		}
	}

	public String[] getyAxisArray() {
		return yAxisArray;
	}

	public void setyAxisArray(String[] yAxisArray) {
		this.yAxisArray = yAxisArray;
		if(yAxisArray!=null){
			int len = yAxisArray.length;
			String str="";
			for(int i=0;i<len;i++){
				String val = yAxisArray[i];
				str=str+val;
				if(i+1<len){
					str=str+",";
				}
			}
			this.yAxis=str;
		}
	}

	public SingleMultiSeriesChartValueObject getSingleMultiSeriesChartValueObject() {
		return singleMultiSeriesChartValueObject;
	}

	public void setSingleMultiSeriesChartValueObject(
			SingleMultiSeriesChartValueObject singleMultiSeriesChartValueObject) {
		this.singleMultiSeriesChartValueObject = singleMultiSeriesChartValueObject;
	}

	public List getChartTypesList() {
		return chartTypesList;
	}

	public void setChartTypesList(List chartTypesList) {
		this.chartTypesList = chartTypesList;
	}

	public Collection get_detailValueObjs() {
		return _detailValueObjs;
	}

	public void set_detailValueObjs(Collection _detailValueObjs) {
		this._detailValueObjs = _detailValueObjs;
	}

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

	public String getFilterColumn() {
		return filterColumn;
	}

	public void setFilterColumn(String filterColumn) {
		this.filterColumn = filterColumn;
	}

	public String getTemplateChartInfoId() {
		return templateChartInfoId;
	}

	public void setTemplateChartInfoId(String templateChartInfoId) {
		this.templateChartInfoId = templateChartInfoId;
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

	public String getDivLineAlpha() {
		return divLineAlpha;
	}

	public void setDivLineAlpha(String divLineAlpha) {
		this.divLineAlpha = divLineAlpha;
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

	public List getBannerTableWeightList() {
		return bannerTableWeightList;
	}

	public void setBannerTableWeightList(List bannerTableWeightList) {
		this.bannerTableWeightList = bannerTableWeightList;
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

	public int getMeanIncl() {
		return meanIncl;
	}

	public void setMeanIncl(int meanIncl) {
		this.meanIncl = meanIncl;
	}

	public int getMeanExcl() {
		return meanExcl;
	}

	public void setMeanExcl(int meanExcl) {
		this.meanExcl = meanExcl;
	}

	public String getExcelData() {
		return excelData;
	}

	public void setExcelData(String excelData) {
		this.excelData = excelData;
	}

	public int getMedianIncl() {
		return medianIncl;
	}

	public void setMedianIncl(int medianIncl) {
		this.medianIncl = medianIncl;
	}

	public int getMedianExcl() {
		return medianExcl;
	}

	public void setMedianExcl(int medianExcl) {
		this.medianExcl = medianExcl;
	}

	public int getNumberOfCharts() {
		return numberOfCharts;
	}

	public void setNumberOfCharts(int numberOfCharts) {
		this.numberOfCharts = numberOfCharts;
	}

	public String getCrossTabValue() {
		return crossTabValue;
	}

	public void setCrossTabValue(String crossTabValue) {
		this.crossTabValue = crossTabValue;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	
}