package com.bridgei2i.common.vo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.bridgei2i.vo.TemplateChartCategory;
import com.bridgei2i.vo.TemplateChartReportAssign;

@Entity
@Table(name="page_template")
public class PageTemplate {
	
	@Id
	@Column(name="id")
	@GeneratedValue
	private Integer id;
	
	@Column(name="pageTitle")
	private String pageTitle="Untitled";
	
	
	@Column(name="logicalName")
	private String logicalName;
	
	@Column(name="createdDate")
	private Date createdDate;
	
	@Column(name="updatedDate")
	private Date updatedDate;
	
	@OneToMany(fetch=FetchType.EAGER,mappedBy="pageTemplate",cascade=CascadeType.ALL)
	private List<PageTemplateCharts> pageTemplateCharts;
	
	@OneToMany(mappedBy="pageTemplate",cascade=CascadeType.ALL)
	@OrderBy("displayOrder")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<PageTemplateFilters> pageTemplateFilters;
	
	@Transient
	private List jsonStrList;
	
	@Transient
	private List filtersList;
	
	@Transient
	private List jsonTableList;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public String getLogicalName() {
		return logicalName;
	}

	public void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
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

	public List<PageTemplateCharts> getPageTemplateCharts() {
		return pageTemplateCharts;
	}

	public void setPageTemplateCharts(List<PageTemplateCharts> pageTemplateCharts) {
		this.pageTemplateCharts = pageTemplateCharts;
	}

	public List<PageTemplateFilters> getPageTemplateFilters() {
		return pageTemplateFilters;
	}

	public void setPageTemplateFilters(List<PageTemplateFilters> pageTemplateFilters) {
		this.pageTemplateFilters = pageTemplateFilters;
	}

	public List getJsonStrList() {
		return jsonStrList;
	}

	public void setJsonStrList(List jsonStrList) {
		this.jsonStrList = jsonStrList;
	}

	public List getFiltersList() {
		return filtersList;
	}

	public void setFiltersList(List filtersList) {
		this.filtersList = filtersList;
	}

	public List getJsonTableList() {
		return jsonTableList;
	}

	public void setJsonTableList(List jsonTableList) {
		this.jsonTableList = jsonTableList;
	}
	
	
}