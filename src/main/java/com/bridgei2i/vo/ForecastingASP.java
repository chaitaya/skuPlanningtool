package com.bridgei2i.vo;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.bridgei2i.common.util.ApplicationUtil;

@Entity
@Table(name="forecasting_asp")

public class ForecastingASP implements Cloneable {
	
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
	
	@OneToMany(mappedBy = "forecastAspObj", fetch = FetchType.EAGER)
	private List<OverrideAspLog> overrideAsp;
	
	public List<OverrideAspLog> getOverrideAsp() {
		return overrideAsp;
	}

	public void setOverrideAsp(List<OverrideAspLog> overrideAsp) {
		this.overrideAsp = overrideAsp;
	}

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

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPlanningCycleId() {
		return planningCycleId;
	}

	public void setPlanningCycleId(Integer planningCycleId) {
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
		String pattern = "######.##";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		if(ApplicationUtil.isEmptyOrNull(forecastValue)){
			forecastValue="0.00";
		}
		return decimalFormat.format(Double.parseDouble(forecastValue));
	}

	public void setForecastValue(String forecastValue) {
		this.forecastValue = forecastValue;
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

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}