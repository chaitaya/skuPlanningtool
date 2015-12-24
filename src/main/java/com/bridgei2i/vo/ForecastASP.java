package com.bridgei2i.vo;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="forecasting_asp", uniqueConstraints = {   
		@UniqueConstraint(columnNames = "id")}
     )
public class ForecastASP {	
	
	
	@Id
	@GenericGenerator(name="gen",strategy="increment")
	@GeneratedValue(generator="gen")
	@Column(name="id", unique = true, nullable = false)
    private int id;
	
	@Column(name="planningCycleId")
	private int planningCycleId;
	
	@Column(name="modelId")
	private String modelId;
	
	@Column(name="productId")
	private String productId;
	
	@Column(name="forecastPeriod")
	private String forecastPeriod;

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	@Column(name="forecastValue")
	private String forecastValue;
	
	@Column(name="business")
	private String business;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPlanningCycleId() {
		return planningCycleId;
	}

	public void setPlanningCycleId(int planningCycleId) {
		this.planningCycleId = planningCycleId;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getForecastPeriod() {
		return forecastPeriod;
	}

	public void setForecastPeriod(String forecastPeriod) {
		this.forecastPeriod = forecastPeriod;
	}

	public String getForecastValue() {
		return forecastValue;
	}

	public void setForecastValue(String forecastValue) {
		this.forecastValue = forecastValue;
	}

	
}
