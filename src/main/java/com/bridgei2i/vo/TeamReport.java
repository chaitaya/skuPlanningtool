package com.bridgei2i.vo;
import java.math.BigDecimal;
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
@Table(name="teamreport")
public class TeamReport {

	@Id
	@Column(name="ID")
	@GeneratedValue
	private Integer id;
	
	@Column(name="templatechartId")
	private Integer templateChartId;

	@Column(name="managerId")
	private String managerId;
	
	@Column(name="employeeId")
	private String employeeId;

	@Column(name="columnName")
	private String columnName;
	
	@Column(name="value")
	private BigDecimal value;
	
	@Column(name="totalValidRespondents")
	private BigDecimal totalValidRespondents;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTemplateChartId() {
		return templateChartId;
	}

	public void setTemplateChartId(Integer templateChartId) {
		this.templateChartId = templateChartId;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getTotalValidRespondents() {
		return totalValidRespondents;
	}

	public void setTotalValidRespondents(BigDecimal totalValidRespondents) {
		this.totalValidRespondents = totalValidRespondents;
	}
}