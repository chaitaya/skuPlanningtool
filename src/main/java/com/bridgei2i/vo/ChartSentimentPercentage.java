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
@Table(name="chartsentimentpercentage")
public class ChartSentimentPercentage {
	@Id
    @Column(name="id")
    @GeneratedValue
    private Integer id;
    
    @Column(name="templateChartInfoId")
    private Integer templateChartInfoId;
    
    @Column(name="employeeId")
    private Integer employeeId;
       
    @Column(name="positive")
    private String positive;
    
    @Column(name="negative")
    private String negative;
    
    @Column(name="reportmetadataId")
    private Integer reportmetadataId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTemplateChartInfoId() {
		return templateChartInfoId;
	}

	public void setTemplateChartInfoId(Integer templateChartInfoId) {
		this.templateChartInfoId = templateChartInfoId;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getPositive() {
		return positive;
	}

	public void setPositive(String positive) {
		this.positive = positive;
	}

	public String getNegative() {
		return negative;
	}

	public void setNegative(String negative) {
		this.negative = negative;
	}

	public Integer getReportmetadataId() {
		return reportmetadataId;
	}

	public void setReportmetadataId(Integer reportmetadataId) {
		this.reportmetadataId = reportmetadataId;
	}
	
}