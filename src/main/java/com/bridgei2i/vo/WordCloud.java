package com.bridgei2i.vo;
import java.io.Serializable;
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
@Table(name="wordcloud")
public class WordCloud implements Cloneable,Serializable {
	@Id
    @Column(name="id")
    @GeneratedValue
    private Integer id;
    
    @Column(name="templateChartInfoId")
    private Integer templateChartInfoId;
    
    @Column(name="employeeId")
    private Integer employeeId;
       
    @Column(name="word")
    private String word;
    
    @Column(name="count")
    private int count;
    
    @Column(name="segmentName")
    private String segmentName;
    
    @Column(name="createdDate")
    private Date createdDate;
    
    @Column(name="updatedDate")
    private Date updatedDate;
    
    @Column(name="createdBy")
    private Long createdBy;
    
    @Column(name="updatedBy")
    private Long updatedBy;
    
    @Column(name="normalizedCount")
    private String normalizedCount;
    
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

	public String getSegmentName() {
		return segmentName;
	}

	public void setSegmentName(String segmentName) {
		this.segmentName = segmentName;
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

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getNormalizedCount() {
		return normalizedCount;
	}

	public void setNormalizedCount(String normalizedCount) {
		this.normalizedCount = normalizedCount;
	}

	public Integer getReportmetadataId() {
		return reportmetadataId;
	}

	public void setReportmetadataId(Integer reportmetadataId) {
		this.reportmetadataId = reportmetadataId;
	}

}