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
@Table(name="chartfrequencies")
public class ChartFrequencies {
	@Id
    @Column(name="id")
    @GeneratedValue
    private Integer id;
    
    @Column(name="templateChartInfoId")
    private Integer templateChartInfoId;
    
    @Column(name="employeeId")
    private Integer employeeId;
       
    @Column(name="xAxis")
    private String xAxis;
    
    @Column(name="yAxis")
    private String yAxis;
    
    @Column(name="filter")
    private String filter;
    
    @Column(name="createdDate")
    private Date createdDate;
    
    @Column(name="updatedDate")
    private Date updatedDate;
    
    @Column(name="createdBy")
    private Long createdBy;
    
    @Column(name="updatedBy")
    private Long updatedBy;
    
    @Column(name="value")
    private Long value;
    
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

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
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

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	public Integer getReportmetadataId() {
		return reportmetadataId;
	}

	public void setReportmetadataId(Integer reportmetadataId) {
		this.reportmetadataId = reportmetadataId;
	}

}