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
@Table(name="codebook")
public class CodeBook {
	@Id
	@Column(name="ID")
	@GeneratedValue
	private Integer id;
	
	@Column(name="variableNames")
	private String variableNames;

	@Column(name="actualValue")
	private String actualValue;
	
	@Column(name="recodeValue")
	private String recodeValue;
	
	@Column(name="displayValue")
	private String displayValue;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVariableNames() {
		return variableNames;
	}

	public void setVariableNames(String variableNames) {
		this.variableNames = variableNames;
	}

	public String getActualValue() {
		return actualValue;
	}

	public void setActualValue(String actualValue) {
		this.actualValue = actualValue;
	}

	public String getRecodeValue() {
		return recodeValue;
	}

	public void setRecodeValue(String recodeValue) {
		this.recodeValue = recodeValue;
	}

	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}
	
}
