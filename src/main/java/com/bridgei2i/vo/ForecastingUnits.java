package com.bridgei2i.vo;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="forecasting_units")
public class ForecastingUnits implements Cloneable {
		
		@Id
		@Column(name="id")
		@GeneratedValue
		private Integer id;
		
		@Column(name="planningCycleId")
		private int planningCycleId;
		
		@Column(name="modelId")
		private String modelId;
		
		@Column(name="productId")
		private String productId;
		
		@Column(name="business")
		private String business;
		
		@OneToMany(mappedBy = "forecastUnitObj", fetch = FetchType.EAGER)
		private List<OverrideUnitsLog> overrideUnit;
		
		@Transient
		private String userName;
		
		@Transient
		private String comment;
		
		
		
		
		public List<OverrideUnitsLog> getOverrideUnit() {
			return overrideUnit;
		}

		public void setOverrideUnit(List<OverrideUnitsLog> overrideUnit) {
			this.overrideUnit = overrideUnit;
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

		public String getBusiness() {
			return business;
		}

		public void setBusiness(String business) {
			this.business = business;
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

		@Override
		public Object clone() throws CloneNotSupportedException {
			// TODO Auto-generated method stub
			return super.clone();
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}
		
		
}
