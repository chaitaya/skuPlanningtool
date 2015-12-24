package com.bridgei2i.vo;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


@Entity
@Table(name="variablenames")
public class VariableNames {
	@Id
	@Column(name="ID")
	@GeneratedValue
	private Integer id;
	
	@Column(name="variableName")
	private String variableName;
	
	@Column(name="columnName")
	private String columnName;
	
	@Column(name="aggregateFormula")
	private String aggregateFormula;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="createdDate")
	private Date createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updatedDate")
	private Date updatedDate;

	public Integer getId() {
		return id;
	}

	public String getVariableName() {
		return variableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public String getAggregateFormula() {
		return aggregateFormula;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public void setAggregateFormula(String aggregateFormula) {
		this.aggregateFormula = aggregateFormula;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
}