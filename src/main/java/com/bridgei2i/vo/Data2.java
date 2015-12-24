package com.bridgei2i.vo;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


@Entity
@Table(name="data2")
public class Data2 {
	@Id
    @Column(name="id")
    @GeneratedValue
    private Integer id;
	
	@Column(name="planningCycleId")
	private int planningCycleId;
	
	@Column(name="C1")
	private String C1;
	
	@Column(name="C2")
	private String C2;
	
	@Column(name="C3")
	private String C3;

	@Column(name="C4")
	private String C4;
	
	@Column(name="C5")
	private String C5;
	
	@Column(name="C6")
	private String C6;
	
	@Column(name="C7")
	private String C7;
	
	@Column(name="C8")
	private String C8;
	
	@Column(name="C9")
	private String C9;
	
	@Column(name="C10")
	private String C10;
	
	@Column(name="C11")
	private String C11;
	
	@Column(name="C12")
	private String C12;
	
	@Column(name="C13")
	private String C13;
	
	@Column(name="C14")
	private String C14;
	
	@Column(name="C15")
	private String C15;
	
	@Column(name="C16")
	private String C16;
	
	@Column(name="C17")
	private String C17;
	
	@Column(name="C18")
	private String C18;
	
	@Column(name="C19")
	private String C19;
	
	@Column(name="C20")
	private String C20;
	
	@Column(name="C21")
	private String C21;
	
	@Column(name="C22")
	private String C22;
	
	@Column(name="C23")
	private String C23;
	
	@Column(name="C24")
	private String C24;
	
	@Column(name="C25")
	private String C25;
	
	@Column(name="C26")
	private String C26;
	
	@Column(name="C27")
	private String C27;
	
	@Column(name="C28")
	private String C28;
	
	@Column(name="C29")
	private String C29;
	
	@Column(name="C30")
	private String C30;
	
	@Column(name="C31")
	private String C31;
	
	@Column(name="derivedRegion")
	private String derivedRegion;
	
	@Column(name="derivedCategory")
	private String derivedCategory;
	
	@Column(name="derivedProductType")
	private String derivedProductType;
	


	public Integer getId() {
		return id;
	}

	public int getPlanningCycleId() {
		return planningCycleId;
	}

	public String getC1() {
		return C1;
	}

	public String getC2() {
		return C2;
	}

	public String getC3() {
		return C3;
	}

	public String getC4() {
		return C4;
	}

	public String getC5() {
		return C5;
	}

	public String getC6() {
		return C6;
	}

	public String getC7() {
		return C7;
	}

	public String getC8() {
		return C8;
	}

	public String getC9() {
		return C9;
	}

	public String getC10() {
		return C10;
	}

	public String getC11() {
		return C11;
	}

	public String getC12() {
		return C12;
	}

	public String getC13() {
		return C13;
	}

	public String getC14() {
		return C14;
	}

	public String getC15() {
		return C15;
	}

	public String getC16() {
		return C16;
	}

	public String getC17() {
		return C17;
	}

	public String getC18() {
		return C18;
	}

	public String getC19() {
		return C19;
	}

	public String getC20() {
		return C20;
	}

	public String getC21() {
		return C21;
	}

	public String getC22() {
		return C22;
	}

	public String getC23() {
		return C23;
	}

	public String getC24() {
		return C24;
	}

	public String getC25() {
		return C25;
	}

	public String getC26() {
		return C26;
	}

	public String getC27() {
		return C27;
	}

	public String getC28() {
		return C28;
	}

	public String getC29() {
		return C29;
	}

	public String getC30() {
		return C30;
	}

	public String getC31() {
		return C31;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPlanningCycleId(int planningCycleId) {
		this.planningCycleId = planningCycleId;
	}
	
	public void setC1(String c1) {
		C1 = c1;
	}

	public void setC2(String c2) {
		C2 = c2;
	}

	public void setC3(String c3) {
		C3 = c3;
	}

	public void setC4(String c4) {
		C4 = c4;
	}

	public void setC5(String c5) {
		C5 = c5;
	}

	public void setC6(String c6) {
		C6 = c6;
	}

	public void setC7(String c7) {
		C7 = c7;
	}

	public void setC8(String c8) {
		C8 = c8;
	}

	public void setC9(String c9) {
		C9 = c9;
	}

	public void setC10(String c10) {
		C10 = c10;
	}

	public void setC11(String c11) {
		C11 = c11;
	}

	public void setC12(String c12) {
		C12 = c12;
	}

	public void setC13(String c13) {
		C13 = c13;
	}

	public void setC14(String c14) {
		C14 = c14;
	}

	public void setC15(String c15) {
		C15 = c15;
	}

	public void setC16(String c16) {
		C16 = c16;
	}

	public void setC17(String c17) {
		C17 = c17;
	}

	public void setC18(String c18) {
		C18 = c18;
	}

	public void setC19(String c19) {
		C19 = c19;
	}

	public void setC20(String c20) {
		C20 = c20;
	}

	public void setC21(String c21) {
		C21 = c21;
	}

	public void setC22(String c22) {
		C22 = c22;
	}

	public void setC23(String c23) {
		C23 = c23;
	}

	public void setC24(String c24) {
		C24 = c24;
	}

	public void setC25(String c25) {
		C25 = c25;
	}

	public void setC26(String c26) {
		C26 = c26;
	}

	public void setC27(String c27) {
		C27 = c27;
	}

	public void setC28(String c28) {
		C28 = c28;
	}

	public void setC29(String c29) {
		C29 = c29;
	}

	public void setC30(String c30) {
		C30 = c30;
	}

	public void setC31(String c31) {
		C31 = c31;
	}
	
	public String getDerivedRegion() {
		return derivedRegion;
	}

	public String getDerivedCategory() {
		return derivedCategory;
	}

	public String getDerivedProductType() {
		return derivedProductType;
	}

	public void setDerivedRegion(String derivedRegion) {
		this.derivedRegion = derivedRegion;
	}

	public void setDerivedCategory(String derivedCategory) {
		this.derivedCategory = derivedCategory;
	}

	public void setDerivedProductType(String derivedProductType) {
		this.derivedProductType = derivedProductType;
	}

}