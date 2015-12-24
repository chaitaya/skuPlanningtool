package com.bridgei2i.vo;

import java.sql.Blob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
@Entity
@Table(name="bannermetadata")
public class BannerMetaData {
	@Id
    @Column(name="id")
    @GeneratedValue
    private Integer id;
    
    @Column(name="xAxis")
    private String xAxis;
	    
	@Column(name="yAxis")
	private String yAxis;
	
    @Column(name="weight")
    private String weight;
    
    @Column(name="mean")
    private String mean;
    
    @Column(name="median")
    private String median;
    
    @Column(name="std_deviation")
    private String std_deviation;
    
    @Column(name="std_error")
    private String std_error;
    
    @Column(name="InclExcl")
    private Integer InclExcl;

    @Column(name="type")
    private String type;
    
    @Column(name="title1")
    private String title1;
    
    @Column(name="title2")
    private String title2;
    
    @Column(name="title3")
    private String title3;
    
    @Column(name="xAxisRaw")
    private String xAxisRaw;

    @Column(name="whereClause")
    private String whereClause;
    
    @Column(name="levelOrder")
    private String levelOrder;
    
    @Column(name="displayLevels")
    private String displayLevels;
    
    @Column(name="totalFrequencyOrder")
    private String totalFrequencyOrder;
    
    @Column(name="totalFrequencyOrderVariableExclude")
    private String totalFrequencyOrderVariableExclude;
    
    
    @Column(name="totalFrequencyOrderLevelExclude")
    private String totalFrequencyOrderLevelExclude;
    
    @Column(name="includeMainVariableZero")
    private String includeMainVariableZero;
    
    @Column(name="mainVariableOrder")
    private String mainVariableOrder;
    
    @Column(name="netWeight")
    private String netWeight;
    
    
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getMean() {
		return mean;
	}

	public void setMean(String mean) {
		this.mean = mean;
	}

	public String getMedian() {
		return median;
	}

	public void setMedian(String median) {
		this.median = median;
	}

	public String getStd_deviation() {
		return std_deviation;
	}

	public void setStd_deviation(String std_deviation) {
		this.std_deviation = std_deviation;
	}

	public String getStd_error() {
		return std_error;
	}

	public void setStd_error(String std_error) {
		this.std_error = std_error;
	}

	public Integer getInclExcl() {
		return InclExcl;
	}

	public void setInclExcl(Integer inclExcl) {
		InclExcl = inclExcl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle1() {
		return title1;
	}

	public void setTitle1(String title1) {
		this.title1 = title1;
	}

	public String getTitle2() {
		return title2;
	}

	public void setTitle2(String title2) {
		this.title2 = title2;
	}

	public String getTitle3() {
		return title3;
	}

	public void setTitle3(String title3) {
		this.title3 = title3;
	}

	public String getxAxisRaw() {
		return xAxisRaw;
	}

	public void setxAxisRaw(String xAxisRaw) {
		this.xAxisRaw = xAxisRaw;
	}

	public String getWhereClause() {
		return whereClause;
	}

	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}

	public String getLevelOrder() {
		return levelOrder;
	}

	public void setLevelOrder(String levelOrder) {
		this.levelOrder = levelOrder;
	}

	public String getDisplayLevels() {
		return displayLevels;
	}

	public void setDisplayLevels(String displayLevels) {
		this.displayLevels = displayLevels;
	}

	public String getTotalFrequencyOrder() {
		return totalFrequencyOrder;
	}

	public void setTotalFrequencyOrder(String totalFrequencyOrder) {
		this.totalFrequencyOrder = totalFrequencyOrder;
	}
	

	public String getTotalFrequencyOrderVariableExclude() {
		return totalFrequencyOrderVariableExclude;
	}

	public void setTotalFrequencyOrderVariableExclude(
			String totalFrequencyOrderVariableExclude) {
		this.totalFrequencyOrderVariableExclude = totalFrequencyOrderVariableExclude;
	}

	public String getTotalFrequencyOrderLevelExclude() {
		return totalFrequencyOrderLevelExclude;
	}

	public void setTotalFrequencyOrderLevelExclude(
			String totalFrequencyOrderLevelExclude) {
		this.totalFrequencyOrderLevelExclude = totalFrequencyOrderLevelExclude;
	}

	public String getIncludeMainVariableZero() {
		return includeMainVariableZero;
	}

	public void setIncludeMainVariableZero(String includeMainVariableZero) {
		this.includeMainVariableZero = includeMainVariableZero;
	}

	public String getMainVariableOrder() {
		return mainVariableOrder;
	}

	public void setMainVariableOrder(String mainVariableOrder) {
		this.mainVariableOrder = mainVariableOrder;
	}

	public String getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(String netWeight) {
		this.netWeight = netWeight;
	}
	
	
}