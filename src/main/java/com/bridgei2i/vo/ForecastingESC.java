package com.bridgei2i.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="forecasting_esc")
public class ForecastingESC {
	
	@Id
	@Column(name="id")
	@GeneratedValue
	private Integer id;
	
	@Column(name="planningCycleId")
	private Integer planningCycleId;
	
	@Column(name="modelId")
	private String modelId;
	
	@Column(name="productId")
	private String productId;
	
	@Column(name="business")
	private String business;
	
	@Column(name="forecastPeriod")
	private String forecastPeriod;
	
	@Column(name="forecastValue")
	private String forecastValue;
	
	@Column(name="createdDate")
	private Date createdDate;
	
	@Column(name="updatedDate")
	private Date updatedDate;

	public Integer getId() {
		return id;
	}

	public Integer getPlanningCycleId() {
		return planningCycleId;
	}

	public String getModelId() {
		return modelId;
	}

	public String getProductId() {
		return productId;
	}

	public String getBusiness() {
		return business;
	}

	public String getForecastPeriod() {
		return forecastPeriod;
	}

	public String getForecastValue() {
		return forecastValue;
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

	public void setPlanningCycleId(Integer planningCycleId) {
		this.planningCycleId = planningCycleId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public void setForecastPeriod(String forecastPeriod) {
		this.forecastPeriod = forecastPeriod;
	}

	public void setForecastValue(String forecastValue) {
		this.forecastValue = forecastValue;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	
}
