package com.bridgei2i.vo;
import java.sql.Blob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
@Entity
@Table(name="distributionlist")
public class DistributionList {
	@Id
	@Column(name="ID")
	@GeneratedValue
	private Integer id;
	
	@Column(name="emailId")
	private String emailId;

	@Column(name="masterDistributionTypeId")
	private Integer masterDistributionTypeId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Integer getMasterDistributionTypeId() {
		return masterDistributionTypeId;
	}

	public void setMasterDistributionTypeId(Integer masterDistributionTypeId) {
		this.masterDistributionTypeId = masterDistributionTypeId;
	}
	
}
