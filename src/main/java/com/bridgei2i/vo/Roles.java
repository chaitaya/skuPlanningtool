package com.bridgei2i.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="roles")
public class Roles {
	@Id
	@Column(name="ROLE_ID")
	@GeneratedValue
	private Integer ROLE_ID;
	
	@Column(name="LOGIN_ID")
	private int LOGIN_ID;

	@Column(name="ROLE")
	private String ROLE;

	@Column(name="createdDate")
	private String createdDate;

	@Column(name="updatedDate")
	private String updatedDate;

	public Integer getROLE_ID() {
		return ROLE_ID;
	}

	public int getLOGIN_ID() {
		return LOGIN_ID;
	}

	public String getROLE() {
		return ROLE;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setROLE_ID(Integer rOLE_ID) {
		ROLE_ID = rOLE_ID;
	}

	public void setLOGIN_ID(int lOGIN_ID) {
		LOGIN_ID = lOGIN_ID;
	}

	public void setROLE(String rOLE) {
		ROLE = rOLE;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	

	
	
}
