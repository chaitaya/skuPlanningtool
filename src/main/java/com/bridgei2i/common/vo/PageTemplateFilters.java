package com.bridgei2i.common.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="page_template_filters")
public class PageTemplateFilters {

	@Id
	@Column(name="id")
	@GeneratedValue
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="pageTemplateId")
	private PageTemplate pageTemplate;

	@Column(name="filterLabel")
	private String filterLabel;
	
	@Column(name="filterFieldName")
	private String filterFieldName;
	
	@Column(name="filterVariable")
	private String filterVariable;
	
	@Column(name="tableName")
	private String tableName;
	
	@Column(name="createdDate")
	private Date createdDate;

	@Column(name="defaultFilterValues")
	private int defaultFilterValues;
	
	@Column(name="displayOrder")
	private int displayOrder;

	@Column(name="historicalReportTableName")
	private String historicalReportTableName;
	
	public String getHistoricalReportTableName() {
		return historicalReportTableName;
	}

	public void setHistoricalReportTableName(String historicalReportTableName) {
		this.historicalReportTableName = historicalReportTableName;
	}

	public int getDashboardFilters() {
		return dashboardFilters;
	}

	public void setDashboardFilters(int dashboardFilters) {
		this.dashboardFilters = dashboardFilters;
	}

	@Column(name="dashboardFilters")
	private int dashboardFilters;
	
	@Column(name="historicalReportFilters")
		private int historicalReportFilters;
	public Integer getId() {
		return id;
	}

	public int getHistoricalReportFilters() {
		return historicalReportFilters;
	}

	public void setHistoricalReportFilters(int historicalReportFilters) {
		this.historicalReportFilters = historicalReportFilters;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PageTemplate getPageTemplate() {
		return pageTemplate;
	}

	public void setPageTemplate(PageTemplate pageTemplate) {
		this.pageTemplate = pageTemplate;
	}

	public String getFilterLabel() {
		return filterLabel;
	}

	public void setFilterLabel(String filterLabel) {
		this.filterLabel = filterLabel;
	}

	public String getFilterFieldName() {
		return filterFieldName;
	}

	public void setFilterFieldName(String filterFieldName) {
		this.filterFieldName = filterFieldName;
	}


	public String getFilterVariable() {
		return filterVariable;
	}

	public void setFilterVariable(String filterVariable) {
		this.filterVariable = filterVariable;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getDefaultFilterValues() {
		return defaultFilterValues;
	}

	public void setDefaultFilterValues(int defaultFilterValues) {
		this.defaultFilterValues = defaultFilterValues;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
}