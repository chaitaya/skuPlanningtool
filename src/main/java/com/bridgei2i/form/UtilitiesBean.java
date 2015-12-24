package com.bridgei2i.form;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.bridgei2i.common.form.ListForm;

public class UtilitiesBean extends ListForm {

	private MultipartFile data;
	private String excelData;
	private Integer planningCycleId ;
	private String npiId;
	private String productNumber;
	private String modalProductNumber;
	private String productDescription;
	private String productLine;
	private String productHierarchyI;
	private String productHierarchyII;
	private String productHierarchyIV;
	private String business;
	private String plClass;
	private String plScorecard;
	private String productManager;
	private Date createdDate;
	private Date updatedDate;
	private List<Object[]> npiList;
	private Integer viewType;
	private String generatedNpiNumber;
	private List<Object[]> newSku;
	private List<Object[]> productManagerList;
	private String expectedAsp;
	private String expectedEsc;
	private String assignedType;
	private Integer noMoreNPI;
	private Integer productManagerId;
	private String region;
	private int selectedValues;
	private String model;
	public int getSelectedValues() {
		return selectedValues;
	}
	
	public void setSelectedValues(int selectedValues) {
		this.selectedValues = selectedValues;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Integer getProductManagerId() {
		return productManagerId;
	}

	public void setProductManagerId(Integer productManagerId) {
		this.productManagerId = productManagerId;
	}

	public Integer getNoMoreNPI() {
		return noMoreNPI;
	}

	public void setNoMoreNPI(Integer noMoreNPI) {
		this.noMoreNPI = noMoreNPI;
	}
	public List<Object[]> getProductManagerList() {
		return productManagerList;
	}
	public void setProductManagerList(List<Object[]> productManagerList) {
		this.productManagerList = productManagerList;
	}
	public List<Object[]> getNewSku() {
		return newSku;
	}
	public void setNewSku(List<Object[]> newSku) {
		this.newSku = newSku;
	}
	private Integer modalFlag;

	public Integer getModalFlag() {
		return modalFlag;
	}
	public void setModalFlag(Integer modalFlag) {
		this.modalFlag = modalFlag;
	}
	public MultipartFile getData() {
		return data;
	}
	public String getExcelData() {
		return excelData;
	}
	public Integer getPlanningCycleId() {
		return planningCycleId;
	}
	public String getNpiId() {
		return npiId;
	}
	public String getProductNumber() {
		return productNumber;
	}
	public String getModalProductNumber() {
		return modalProductNumber;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public String getProductLine() {
		return productLine;
	}
	public String getProductHierarchyI() {
		return productHierarchyI;
	}
	public String getProductHierarchyII() {
		return productHierarchyII;
	}
	public String getProductHierarchyIV() {
		return productHierarchyIV;
	}
	public String getBusiness() {
		return business;
	}
	public String getPlClass() {
		return plClass;
	}
	public String getPlScorecard() {
		return plScorecard;
	}
	public String getProductManager() {
		return productManager;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public List<Object[]> getNpiList() {
		return npiList;
	}
	public Integer getViewType() {
		return viewType;
	}
	public String getGeneratedNpiNumber() {
		return generatedNpiNumber;
	}
	public void setData(MultipartFile data) {
		this.data = data;
	}
	public void setExcelData(String excelData) {
		this.excelData = excelData;
	}
	public void setPlanningCycleId(Integer planningCycleId) {
		this.planningCycleId = planningCycleId;
	}
	public void setNpiId(String npiId) {
		this.npiId = npiId;
	}
	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}
	public void setModalProductNumber(String modalProductNumber) {
		this.modalProductNumber = modalProductNumber;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public void setProductLine(String productLine) {
		this.productLine = productLine;
	}
	public void setProductHierarchyI(String productHierarchyI) {
		this.productHierarchyI = productHierarchyI;
	}
	public void setProductHierarchyII(String productHierarchyII) {
		this.productHierarchyII = productHierarchyII;
	}
	public void setProductHierarchyIV(String productHierarchyIV) {
		this.productHierarchyIV = productHierarchyIV;
	}
	public void setBusiness(String business) {
		this.business = business;
	}
	public void setPlClass(String plClass) {
		this.plClass = plClass;
	}
	public void setPlScorecard(String plScorecard) {
		this.plScorecard = plScorecard;
	}
	public void setProductManager(String productManager) {
		this.productManager = productManager;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public void setNpiList(List<Object[]> npiList) {
		this.npiList = npiList;
	}
	public void setViewType(Integer viewType) {
		this.viewType = viewType;
	}
	public void setGeneratedNpiNumber(String generatedNpiNumber) {
		this.generatedNpiNumber = generatedNpiNumber;
	}
	public String getExpectedAsp() {
		return expectedAsp;
	}
	public void setExpectedAsp(String expectedAsp) {
		this.expectedAsp = expectedAsp;
	}
	public String getExpectedEsc() {
		return expectedEsc;
	}
	public void setExpectedEsc(String expectedEsc) {
		this.expectedEsc = expectedEsc;
	}
	public String getAssignedType() {
		return assignedType;
	}
	public void setAssignedType(String assignedType) {
		this.assignedType = assignedType;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
}
