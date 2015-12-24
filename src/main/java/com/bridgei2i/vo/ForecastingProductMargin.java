package com.bridgei2i.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="forecasting_product_margin")
public class ForecastingProductMargin {
	@Id
	@Column(name="id")
	@GeneratedValue
	private Integer id;
	
	@Column(name="planningCycleId")
	private String planningCycleId;

	@Column(name="modelId")
	private String modelId;

	@Column(name="productId")
	private String productId;

	@Column(name="forecastPeriod")
	private String forecastPeriod;
	
	@Column(name="forecastValue")
	private int forecastValue;
	
	@Temporal(TemporalType.DATE)
	@Column(name="createdDate")
	private Date createdDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="updatedDate")
	private Date updaedDate;

	public Integer getId() {
		return id;
	}

	public String getPlanningCycleId() {
		return planningCycleId;
	}

	public String getModelId() {
		return modelId;
	}

	public String getProductId() {
		return productId;
	}

	public String getForecastPeriod() {
		return forecastPeriod;
	}

	public int getForecastValue() {
		return forecastValue;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public Date getUpdaedDate() {
		return updaedDate;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPlanningCycleId(String planningCycleId) {
		this.planningCycleId = planningCycleId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setForecastPeriod(String forecastPeriod) {
		this.forecastPeriod = forecastPeriod;
	}

	public void setForecastValue(int forecastValue) {
		this.forecastValue = forecastValue;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setUpdaedDate(Date updaedDate) {
		this.updaedDate = updaedDate;
	}
	
	
}
