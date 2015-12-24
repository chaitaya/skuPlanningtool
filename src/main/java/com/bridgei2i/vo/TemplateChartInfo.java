package com.bridgei2i.vo;
import java.io.Serializable;
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
@Entity
@Table(name="templatechartinfo")
public class TemplateChartInfo implements Cloneable,Serializable {

	@Id
	@Column(name="id")
	@GeneratedValue
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="templateChartCategoryId")
	private TemplateChartCategory templateChartCategory;
	
	@Column(name="chartTitle")
	private String chartTitle="Untitled";
	
	@Transient
	private String myNote="";
	
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
	
	@Column(name="meanIncl")
	private Integer meanIncl=0;
	
	@Column(name="meanExcl")
	private Integer meanExcl=0;
	
	@Column(name="medianIncl")
	private Integer medianIncl=0;
	
	@Column(name="medianExcl")
	private Integer medianExcl=0;
	
	@Column(name="numberOfCharts")
	private Integer numberOfCharts;
	
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
	
	@Column(name="colorCode")
	private String colorCode;
	
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

	public Integer getMeanIncl() {
		return meanIncl;
	}

	public void setMeanIncl(Integer meanIncl) {
		this.meanIncl = meanIncl;
	}

	public Integer getMeanExcl() {
		return meanExcl;
	}

	public void setMeanExcl(Integer meanExcl) {
		this.meanExcl = meanExcl;
	}

	public Integer getMedianIncl() {
		return medianIncl;
	}

	public void setMedianIncl(Integer medianIncl) {
		this.medianIncl = medianIncl;
	}

	public Integer getMedianExcl() {
		return medianExcl;
	}

	public void setMedianExcl(Integer medianExcl) {
		this.medianExcl = medianExcl;
	}

	public String getMyNote() {
		return myNote;
	}

	public void setMyNote(String myNote) {
		this.myNote = myNote;
	}

	public Integer getNumberOfCharts() {
		return numberOfCharts;
	}

	public void setNumberOfCharts(Integer numberOfCharts) {
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