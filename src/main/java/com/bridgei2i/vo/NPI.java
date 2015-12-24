package com.bridgei2i.vo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="npi")
public class NPI {

	@Id
    @Column(name="id")
    @GeneratedValue
    private Integer id;
	
	@Column(name="npiId")
	private String npiId;
	
	@Column(name="productNumber")
	private String productNumber;
	
	@Column(name="productDescription")
	private String productDescription;
	
	@Column(name="productLine")
	private String productLine;
	
	@Column(name="hierarchy1")
	private String hierarchy1;
	
	@Column(name="hierarchy2")
	private String hierarchy2;
	
	@Column(name="hierarchy4")
	private String hierarchy4;

	@Column(name="business")
	private String business;
	
	@Column(name="plClass")
	private String plClass;
	

	@Column(name="plScorecard")
	private String plScorecard;
	
	@Column(name="productManager")
	private String productManager;
	
	@Column(name="expectedASP")
	private BigDecimal expectedAsp;
	

	@Column(name="expectedESC")
	private BigDecimal expectedEsc;
	
	@Column(name="noMoreNPI")
	private String noMoreNPI="0";
	
	private Integer productManagerId;
 	
	public Integer getProductManagerId() {
		return productManagerId;
	}

	public void setProductManagerId(Integer productManagerId) {
		this.productManagerId = productManagerId;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Column(name="region")
	private String region;
	
	@Column(name="model")
	private String model;
	
	public String getNoMoreNPI() {
		return noMoreNPI;
	}

	public void setNoMoreNPI(String noMoreNPI) {
		this.noMoreNPI = noMoreNPI;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "createdDate")
	private Date createdDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "updateDate")
	private Date updateDate;

	public Integer getId() {
		return id;
	}

	public String getNpiId() {
		return npiId;
	}

	public String getProductNumber() {
		return productNumber;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public String getProductLine() {
		return productLine;
	}

	public String getHierarchy1() {
		return hierarchy1;
	}

	public String getHierarchy2() {
		return hierarchy2;
	}

	public String getHierarchy4() {
		return hierarchy4;
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

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNpiId(String npiId) {
		this.npiId = npiId;
	}

	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public void setProductLine(String productLine) {
		this.productLine = productLine;
	}

	public void setHierarchy1(String hierarchy1) {
		this.hierarchy1 = hierarchy1;
	}

	public void setHierarchy2(String hierarchy2) {
		this.hierarchy2 = hierarchy2;
	}

	public void setHierarchy4(String hierarchy4) {
		this.hierarchy4 = hierarchy4;
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

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public BigDecimal getExpectedAsp() {
		return expectedAsp;
	}

	public void setExpectedAsp(BigDecimal expectedAsp) {
		this.expectedAsp = expectedAsp;
	}

	public BigDecimal getExpectedEsc() {
		return expectedEsc;
	}

	public void setExpectedEsc(BigDecimal expectedEsc) {
		this.expectedEsc = expectedEsc;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
}