package com.bridgei2i.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="sku_user_mapping")
public class SkuUserMapping {
	@Id
	@Column(name="id")
	@GeneratedValue
	private Integer id;
	
	@Column(name="skuListId")
	private Integer skuListId;
	
	@Column(name="userId")
	private Integer userId;
	
	@Column(name="createdDate")
	private Date createdDate;
	
	@Column(name="updatedDate")
	private Date updatedDate;

	@Column(name="business")
	private String business;
	
	public Integer getId() {
		return id;
	}

	public Integer getSkuListId() {
		return skuListId;
	}

	public Integer getUserId() {
		return userId;
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

	public void setSkuListId(Integer skuListId) {
		this.skuListId = skuListId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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
}