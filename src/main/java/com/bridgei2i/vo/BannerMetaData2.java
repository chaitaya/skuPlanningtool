package com.bridgei2i.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="bannermetadata2")
public class BannerMetaData2 {
	@Id
    @Column(name="id")
    @GeneratedValue
    private Integer id;
    
    @Column(name="canadiantire")
    private String canadiantire;
	    
	@Column(name="walmart")
	private String walmart;
	
    @Column(name="homedepot")
    private String homedepot;
    
    @Column(name="target")
    private String target;
    
    @Column(name="costco")
    private String costco;
    
    @Column(name="dollarstore")
    private String dollarstore;

    @Column(name="columnTitle")
    private String columnTitle;
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCanadiantire() {
		return canadiantire;
	}

	public void setCanadiantire(String canadiantire) {
		this.canadiantire = canadiantire;
	}

	public String getWalmart() {
		return walmart;
	}

	public void setWalmart(String walmart) {
		this.walmart = walmart;
	}

	public String getHomedepot() {
		return homedepot;
	}

	public void setHomedepot(String homedepot) {
		this.homedepot = homedepot;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getCostco() {
		return costco;
	}

	public void setCostco(String costco) {
		this.costco = costco;
	}

	public String getDollarstore() {
		return dollarstore;
	}

	public void setDollarstore(String dollarstore) {
		this.dollarstore = dollarstore;
	}

	public String getColumnTitle() {
		return columnTitle;
	}

	public void setColumnTitle(String columnTitle) {
		this.columnTitle = columnTitle;
	}
}