package com.bridgei2i.vo;

import java.util.ArrayList;

public class PlanningLog {
	
	private String category;
	private String model;
	private String categoryCheckFlag;
	private String modelCheckFlag;
	private String sku;
	private String status;
	private String productManagerOverride;
	private String categoryManagerOverride;
	private String productManagerOverrideModelLevel;
	private String categoryManagerOverrideModelLevel;
	private String productManagerOverrideCategoryLevel;
	private String categoryManagerOverrideCategoryLevel;
	private String categoryStatus;
	private String modelStatus;
	private String productDescription;
	private String productHierarchyII;
	private String planogram;
	private int commitFlag=1;
	private int commitModelFlag=1;
	private int commitcategoryFlag=1;
	private String userName;
	private String toolTipProductDescription;
	
	public String getCategory() {
		return category;
	}
	public String getModel() {
		return model;
	}
	public String getCategoryCheckFlag() {
		return categoryCheckFlag;
	}
	public String getModelCheckFlag() {
		return modelCheckFlag;
	}
	public String getSku() {
		return sku;
	}
	public String getStatus() {
		return status;
	}
	public String getProductManagerOverride() {
		return productManagerOverride;
	}
	public String getCategoryManagerOverride() {
		return categoryManagerOverride;
	}
	public String getProductManagerOverrideModelLevel() {
		return productManagerOverrideModelLevel;
	}
	public String getCategoryManagerOverrideModelLevel() {
		return categoryManagerOverrideModelLevel;
	}
	public String getProductManagerOverrideCategoryLevel() {
		return productManagerOverrideCategoryLevel;
	}
	public String getCategoryManagerOverrideCategoryLevel() {
		return categoryManagerOverrideCategoryLevel;
	}
	public String getCategoryStatus() {
		return categoryStatus;
	}
	public String getModelStatus() {
		return modelStatus;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public String getProductHierarchyII() {
		return productHierarchyII;
	}
	public String getPlanogram() {
		return planogram;
	}
	public int getCommitFlag() {
		return commitFlag;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public void setCategoryCheckFlag(String categoryCheckFlag) {
		this.categoryCheckFlag = categoryCheckFlag;
	}
	public void setModelCheckFlag(String modelCheckFlag) {
		this.modelCheckFlag = modelCheckFlag;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setProductManagerOverride(String productManagerOverride) {
		this.productManagerOverride = productManagerOverride;
	}
	public void setCategoryManagerOverride(String categoryManagerOverride) {
		this.categoryManagerOverride = categoryManagerOverride;
	}
	public void setProductManagerOverrideModelLevel(
			String productManagerOverrideModelLevel) {
		this.productManagerOverrideModelLevel = productManagerOverrideModelLevel;
	}
	public void setCategoryManagerOverrideModelLevel(
			String categoryManagerOverrideModelLevel) {
		this.categoryManagerOverrideModelLevel = categoryManagerOverrideModelLevel;
	}
	public void setProductManagerOverrideCategoryLevel(
			String productManagerOverrideCategoryLevel) {
		this.productManagerOverrideCategoryLevel = productManagerOverrideCategoryLevel;
	}
	public void setCategoryManagerOverrideCategoryLevel(
			String categoryManagerOverrideCategoryLevel) {
		this.categoryManagerOverrideCategoryLevel = categoryManagerOverrideCategoryLevel;
	}
	public void setCategoryStatus(String categoryStatus) {
		this.categoryStatus = categoryStatus;
	}
	public void setModelStatus(String modelStatus) {
		this.modelStatus = modelStatus;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public void setProductHierarchyII(String productHierarchyII) {
		this.productHierarchyII = productHierarchyII;
	}
	public void setPlanogram(String planogram) {
		this.planogram = planogram;
	}
	public void setCommitFlag(int commitFlag) {
		this.commitFlag = commitFlag;
	}
	public int getCommitModelFlag() {
		return commitModelFlag;
	}
	public void setCommitModelFlag(int commitModelFlag) {
		this.commitModelFlag = commitModelFlag;
	}
	public int getCommitcategoryFlag() {
		return commitcategoryFlag;
	}
	public void setCommitcategoryFlag(int commitcategoryFlag) {
		this.commitcategoryFlag = commitcategoryFlag;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getToolTipProductDescription() {
		return toolTipProductDescription;
	}
	public void setToolTipProductDescription(String toolTipProductDescription) {
		this.toolTipProductDescription = toolTipProductDescription;
	}
	
}