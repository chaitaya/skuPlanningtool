
package com.bridgei2i.common.vo;

import java.util.List;

import org.apache.log4j.Logger;

import com.bridgei2i.common.controller.LoginLogoutController;
import com.bridgei2i.common.form.GenericForm;
import com.bridgei2i.common.util.ApplicationUtil;

public class GenericChartValueObject extends GenericForm{
	
	private String xml;
	private String chartId = hashCode()+"";
	private String width = "900";
	private String height = "300";
	private String chartType;
	private String caption;
	private String tableName;
	private String xAxis;
	private String yAxis;
	private String segment;
	private String numberPrefix="$";
	private String formatNumberScale="1";
	private String rotateValues;
	private String placeValuesInside="1";
	private String decimalPrecision="2";
	private String stack100Percent;
	protected String categoryField;
	private String filterName1;
	private List filterValues1;
	private String filterLabel1;
	private String filterName2;
	private List filterValues2;
	private String filterLabel2;
	private String filterName3;
	private List filterValues3;
	private String filterLabel3;
	private String timeFilter;
	private List timeFilterValues;
	private String timeFilterLabel;
	private String category;
	private List categoryType;
	private List categoryLevel;
	private String modelComments;
	private String enableModelComments;
	private String xAxisName;
	private String yAxisName;
	private static Logger logger=Logger.getLogger(GenericChartValueObject.class.getName());
	public String getEnableModelComments() {
		return enableModelComments;
	}


	public void setEnableModelComments(String enableModelComments) {
		this.enableModelComments = enableModelComments;
	}


	public String getModelComments() {
		return modelComments;
	}


	public void setModelComments(String modelComments) {
		this.modelComments = modelComments;
	}


	public String getXml() {
		return xml;
	}


	public void setXml(String xml) {
		this.xml = xml;
	}


	public String getChartId() {
		return chartId;
	}


	public void setChartId(String chartId) {
		this.chartId = chartId;
	}


	public String getWidth() {
		return width;
	}


	public void setWidth(String width) {
		this.width = width;
	}


	public String getHeight() {
		return height;
	}


	public void setHeight(String height) {
		this.height = height;
	}


	public String getChartType() {
		return chartType;
	}


	public void setChartType(String chartType) {
		this.chartType = chartType;
	}


	public String getCaption() {
		return caption;
	}


	public void setCaption(String caption) {
		this.caption = caption;
	}


	public String getTableName() {
		return tableName;
	}


	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	public String getxAxis() {
		return xAxis;
	}


	public void setxAxis(String xAxis) {
		this.xAxis = xAxis;
	}


	public String getyAxis() {
		return yAxis;
	}


	public void setyAxis(String yAxis) {
		this.yAxis = yAxis;
	}


	public String getSegment() {
		return segment;
	}


	public void setSegment(String segment) {
		this.segment = segment;
	}


	public String getNumberPrefix() {
		return numberPrefix;
	}


	public void setNumberPrefix(String numberPrefix) {
		this.numberPrefix = numberPrefix;
	}


	public String getFormatNumberScale() {
		return formatNumberScale;
	}


	public void setFormatNumberScale(String formatNumberScale) {
		this.formatNumberScale = formatNumberScale;
	}


	public String getRotateValues() {
		return rotateValues;
	}


	public void setRotateValues(String rotateValues) {
		this.rotateValues = rotateValues;
	}


	public String getPlaceValuesInside() {
		return placeValuesInside;
	}


	public void setPlaceValuesInside(String placeValuesInside) {
		this.placeValuesInside = placeValuesInside;
	}


	public String getDecimalPrecision() {
		return decimalPrecision;
	}


	public void setDecimalPrecision(String decimalPrecision) {
		this.decimalPrecision = decimalPrecision;
	}


	public String getStack100Percent() {
		return stack100Percent;
	}


	public void setStack100Percent(String stack100Percent) {
		this.stack100Percent = stack100Percent;
	}


	public String getCategoryField() {
		return categoryField;
	}


	public void setCategoryField(String categoryField) {
		this.categoryField = categoryField;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public List getCategoryType() {
		return categoryType;
	}


	public void setCategoryType(List categoryType) {
		this.categoryType = categoryType;
	}


	public List getCategoryLevel() {
		return categoryLevel;
	}


	public void setCategoryLevel(List categoryLevel) {
		this.categoryLevel = categoryLevel;
	}
	
	

	public String getFilterName1() {
		return filterName1;
	}


	public void setFilterName1(String filterName1) {
		this.filterName1 = filterName1;
	}


	public List getFilterValues1() {
		return filterValues1;
	}


	public void setFilterValues1(List filterValues1) {
		this.filterValues1 = filterValues1;
	}


	public String getFilterLabel1() {
		return filterLabel1;
	}


	public void setFilterLabel1(String filterLabel1) {
		this.filterLabel1 = filterLabel1;
	}


	public String getFilterName2() {
		return filterName2;
	}


	public void setFilterName2(String filterName2) {
		this.filterName2 = filterName2;
	}


	public List getFilterValues2() {
		return filterValues2;
	}


	public void setFilterValues2(List filterValues2) {
		this.filterValues2 = filterValues2;
	}


	public String getFilterLabel2() {
		return filterLabel2;
	}


	public void setFilterLabel2(String filterLabel2) {
		this.filterLabel2 = filterLabel2;
	}


	public String getFilterName3() {
		return filterName3;
	}


	public void setFilterName3(String filterName3) {
		this.filterName3 = filterName3;
	}


	public List getFilterValues3() {
		return filterValues3;
	}


	public void setFilterValues3(List filterValues3) {
		this.filterValues3 = filterValues3;
	}


	public String getFilterLabel3() {
		return filterLabel3;
	}


	public void setFilterLabel3(String filterLabel3) {
		this.filterLabel3 = filterLabel3;
	}

	public String getTimeFilter() {
		return timeFilter;
	}


	public void setTimeFilter(String timeFilter) {
		this.timeFilter = timeFilter;
	}


	public String getTimeFilterLabel() {
		return timeFilterLabel;
	}


	public void setTimeFilterLabel(String timeFilterLabel) {
		this.timeFilterLabel = timeFilterLabel;
	}
	

	public List getTimeFilterValues() {
		return timeFilterValues;
	}


	public void setTimeFilterValues(List timeFilterValues) {
		this.timeFilterValues = timeFilterValues;
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


	public boolean validateRequiredFields(){
		logger.info("Entering Into validateRequiredFields Function");
		if(ApplicationUtil.isEmptyOrNull(chartId)){
			System.err.print("chartId can not be empty");
			return false;
		}else if(ApplicationUtil.isEmptyOrNull(chartType)){
			System.err.print("chartType can not be empty");
			return false;
		}else if(ApplicationUtil.isEmptyOrNull(categoryField)){
			System.err.print("categoryField can not be empty");
			return false;
		}
		logger.info("Exiting From validateRequiredFields Function");
		return true;
	}
}