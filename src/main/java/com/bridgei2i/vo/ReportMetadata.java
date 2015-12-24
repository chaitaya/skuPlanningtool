package com.bridgei2i.vo;
import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


@Entity
@Table(name="reportmetadata")
public class ReportMetadata implements Cloneable,Serializable {
	@Id
	@Column(name="ID")
	@GeneratedValue
	private Integer id;
	
	@Column(name="filterColumn1")
	private String filterColumn1;
	
	@Column(name="filterColumn2")
	private String filterColumn2;
	
	@Column(name="filterColumn3")
	private String filterColumn3;
	
	@Column(name="filterValue1")
	private String filterValue1;
	
	@Column(name="filterValue2")
	private String filterValue2;
	
	@Column(name="filterValue3")
	private String filterValue3;
	
	@Column(name="distributionList")
	private String distributionList;
	
	@Column(name="templatechartId")
	private Integer templatechartId;
	
	@Column(name="reportTitle")
	private String reportTitle;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFilterColumn1() {
		return filterColumn1;
	}

	public void setFilterColumn1(String filterColumn1) {
		this.filterColumn1 = filterColumn1;
	}

	public String getFilterColumn2() {
		return filterColumn2;
	}

	public void setFilterColumn2(String filterColumn2) {
		this.filterColumn2 = filterColumn2;
	}

	public String getFilterColumn3() {
		return filterColumn3;
	}

	public void setFilterColumn3(String filterColumn3) {
		this.filterColumn3 = filterColumn3;
	}

	public String getFilterValue1() {
		return filterValue1;
	}

	public void setFilterValue1(String filterValue1) {
		this.filterValue1 = filterValue1;
	}

	public String getFilterValue2() {
		return filterValue2;
	}

	public void setFilterValue2(String filterValue2) {
		this.filterValue2 = filterValue2;
	}

	public String getFilterValue3() {
		return filterValue3;
	}

	public void setFilterValue3(String filterValue3) {
		this.filterValue3 = filterValue3;
	}

	public String getDistributionList() {
		return distributionList;
	}

	public void setDistributionList(String distributionList) {
		this.distributionList = distributionList;
	}

	public Integer getTemplatechartId() {
		return templatechartId;
	}

	public void setTemplatechartId(Integer templatechartId) {
		this.templatechartId = templatechartId;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	
}
