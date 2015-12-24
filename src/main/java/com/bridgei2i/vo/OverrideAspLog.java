package com.bridgei2i.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="override_asp_log")
public class OverrideAspLog implements Cloneable,Serializable {

	@Id
	@GenericGenerator(name="gen",strategy="increment")
	@GeneratedValue(generator="gen")
	@Column(name="id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="userId")
	private int userId;
	
	@Column(name="forecastingAspId")
	private int forecastingAspId;
	
	@Column(name="overrideValue")
	private String overrideValue;
	
	@Column(name="overrideLevel")
	private String overrideLevel;
	
	@ManyToOne
	@JoinColumn(name = "forecastingAspId", referencedColumnName = "id", insertable=false, updatable=false)
    private ForecastingASP forecastAspObj;
	
	public ForecastingASP getForecastAspObj() {
		return forecastAspObj;
	}

	public void setForecastAspObj(ForecastingASP forecastAspObj) {
		this.forecastAspObj = forecastAspObj;
	}



	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "overrideAspLogCommentId", nullable = true)
	private OverrideAspLogComment overrideAspLogObj;	
	
	

	public OverrideAspLogComment getOverrideAspLogObj() {
		return overrideAspLogObj;
	}

	public void setOverrideAspLogObj(OverrideAspLogComment overrideAspLogObj) {
		this.overrideAspLogObj = overrideAspLogObj;
	}



	@Column(name="createdDate")
	private String createdDate;

	public Integer getId() {
		return id;
	}

	public int getUserId() {
		return userId;
	}

	public int getForecastingAspId() {
		return forecastingAspId;
	}

	public String getOverrideValue() {
		return overrideValue;
	}



	public String getCreatedDate() {
		return createdDate;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setForecastingAspId(int forecastingAspId) {
		this.forecastingAspId = forecastingAspId;
	}

	public void setOverrideValue(String overrideValue) {
		this.overrideValue = overrideValue;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getOverrideLevel() {
		return overrideLevel;
	}

	public void setOverrideLevel(String overrideLevel) {
		this.overrideLevel = overrideLevel;
	}
}