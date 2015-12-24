package com.bridgei2i.vo;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="sku_list")
public class SkuList {

	@Id
    @Column(name="id")
    @GeneratedValue
    private Integer id;
	
	@Column(name="productId")
	private String productId;
	
	@Column(name="eolWeek")
	private String eolWeek;

	@Column(name="eolYear")
	private String eolYear;
	
	@Column(name="createdDate")
	private String createdDate;

	@Column(name="updatedDate")
	private String updatedDate;
	
		
	@Transient
	private String C9;
	
	@Transient
	private String C11;
	
	
	@Transient
	private String C28;
	
	@Transient
	private String C6;
	
	@Transient
	private String C7;
	
	@Transient
	private String C19;
	
	@Transient
	private String combinedEOL;
	
	@Transient
	private String toolTipProductDescription;
	
	
	
	public String getCombinedEOL() {
		return combinedEOL;
	}

	public void setCombinedEOL(String combinedEOL) {
		this.combinedEOL = combinedEOL;
	}

	public Integer getId() {
		return id;
	}

	public String getProductId() {
		return productId;
	}

	public String getEolWeek() {
		return eolWeek;
	}

	public String getEolYear() {
		return eolYear;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setEolWeek(String eolWeek) {
		this.eolWeek = eolWeek;
	}

	public void setEolYear(String eolYear) {
		this.eolYear = eolYear;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getC9() {
		return C9;
	}

	public void setC9(String c9) {
		C9 = c9;
	}

	public String getC11() {
		return C11;
	}

	public void setC11(String c11) {
		C11 = c11;
	}

	public String getC28() {
		return C28;
	}

	public void setC28(String c28) {
		C28 = c28;
	}

	public String getC6() {
		return C6;
	}

	public void setC6(String c6) {
		C6 = c6;
	}

	public String getC7() {
		return C7;
	}

	public void setC7(String c7) {
		C7 = c7;
	}

	public String getC19() {
		return C19;
	}

	public void setC19(String c19) {
		C19 = c19;
	}

	public String getToolTipProductDescription() {
		return toolTipProductDescription;
	}

	public void setToolTipProductDescription(String toolTipProductDescription) {
		this.toolTipProductDescription = toolTipProductDescription;
	}
	
	
}