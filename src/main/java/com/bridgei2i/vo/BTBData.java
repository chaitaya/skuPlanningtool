package com.bridgei2i.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="btbdata")
public class BTBData {
	
	@Id
	@Column(name="id")
	@GeneratedValue
	private Integer id;
	
	@Column(name="category")
	private String category;
	
	@Column(name="btbValue")
	private String btbValue;
	
	@Column(name="targetValue")
	private String targetValue;
	
	@Column(name="business")
	private String business;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getBtbValue() {
		return btbValue;
	}

	public void setBtbValue(String btbValue) {
		this.btbValue = btbValue;
	}

	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}
	
	

}
