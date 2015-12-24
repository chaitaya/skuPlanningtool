package com.bridgei2i.vo;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="target_data")
public class TargetData {

	@Id
    @Column(name="id")
    @GeneratedValue
    private Integer id;
	
	@Column(name="Biz")
	private String biz;
	
	@Column(name="GBU")
	private String gbu;

	@Column(name="Metric")
	private String metric;
	
	@Column(name="Total_Store_Deals")
	private String totalStoreDeals;
	
	
	@Column(name="week")
	private String week;
	
	@Column(name="value")
	private BigDecimal value;

	@Column(name="seasonalityIndex")
	private String seasonalityIndex;

	
	public String getSeasonalityIndex() {
		return seasonalityIndex;
	}

	public void setSeasonalityIndex(String seasonalityIndex) {
		this.seasonalityIndex = seasonalityIndex;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBiz() {
		return biz;
	}

	public void setBiz(String biz) {
		this.biz = biz;
	}

	public String getGbu() {
		return gbu;
	}

	public void setGbu(String gbu) {
		this.gbu = gbu;
	}

	public String getMetric() {
		return metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	public String getTotalStoreDeals() {
		return totalStoreDeals;
	}

	public void setTotalStoreDeals(String totalStoreDeals) {
		this.totalStoreDeals = totalStoreDeals;
	}
	
	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
}
