package com.bridgei2i.vo;
import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
@Entity
@Table(name="templatechartreportassign")
public class TemplateChartReportAssign implements Cloneable,Serializable{
	@Id
	@Column(name="id")
	@GeneratedValue
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="templateChartId")
	private TemplateChart templateChart;
	
	@Column(name="masterDistId")
	private Integer masterDistId;
	
	@Column(name="createdDate")
	private Date createdDate;
	
	@Column(name="updatedDate")
	private Date updatedDate;
	
	@Column(name="createdBy")
	private Long createdBy;
	
	@Column(name="updatedBy")
	private Long updatedBy;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	public Integer getMasterDistId() {
		return masterDistId;
	}

	public void setMasterDistId(Integer masterDistId) {
		this.masterDistId = masterDistId;
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

	public TemplateChart getTemplateChart() {
		return templateChart;
	}

	public void setTemplateChart(TemplateChart templateChart) {
		this.templateChart = templateChart;
	}
}
