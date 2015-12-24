package com.bridgei2i.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.net.ntp.TimeStamp;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="override_units_log")
public class OverrideUnitsLog implements Cloneable,Serializable {

	@Id
	@GenericGenerator(name="gen",strategy="increment")
	@GeneratedValue(generator="gen")
	@Column(name="id", unique = true, nullable = false)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "overrideUnitsLogCommentId", nullable = true)
	private OverrideUnitsLogComment overrideUnitsLogObj;
	
	
	@Column(name="userId")
	private int userId;
	
	@Column(name="forecastingUnitsId")
	private int forecastingUnitsId;
	
	@Column(name="overrideValue")
	private int overrideValue;
	
	@Column(name="overrideLevel")
	private String overrideLevel;

	public OverrideUnitsLogComment getOverrideUnitsLogObj() {
		return overrideUnitsLogObj;
	}

	public void setOverrideUnitsLogObj(OverrideUnitsLogComment overrideUnitsLogObj) {
		this.overrideUnitsLogObj = overrideUnitsLogObj;
	}

	@ManyToOne
	@JoinColumn(name = "forecastingUnitsId", referencedColumnName = "id", insertable=false, updatable=false)
    private ForecastingUnits forecastUnitObj;
	
	
	public OverrideUnitsLogComment getOverrrideUnitsLogObj() {
		return overrideUnitsLogObj;
	}

	public void setOverrrideUnitsLogObj(OverrideUnitsLogComment overrrideUnitsLogObj) {
		overrideUnitsLogObj = overrrideUnitsLogObj;
	}

	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public ForecastingUnits getForecastUnitObj() {
		return forecastUnitObj;
	}

	public void setForecastUnitObj(ForecastingUnits forecastUnitObj) {
		this.forecastUnitObj = forecastUnitObj;
	}

	@Column(name="createdDate")
	private String createdDate;

	public Integer getId() {
		return id;
	}

	public int getUnits() {
		return userId;
	}

	public int getForecastingUnitsId() {
		return forecastingUnitsId;
	}

	public int getOverrideValue() {
		return overrideValue;
	}

	

	public String getCreatedDate() {
		return createdDate;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUnits(int units) {
		this.userId = units;
	}

	public void setForecastingUnitsId(int forecastingUnitsId) {
		this.forecastingUnitsId = forecastingUnitsId;
	}

	public void setOverrideValue(int overrideValue) {
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