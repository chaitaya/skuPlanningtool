package com.bridgei2i.common.vo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

//import com.bridgei2i.vo.TemplateChart;

/**
 * User domain
 */
@Entity
@Table(name="roles")
public class Roles {

	@Id
	@Column(name="ROLE_ID")
	@GeneratedValue
	private Integer role_id;
	
	@ManyToOne
	@JoinColumn(name="LOGIN_ID")
	private Users users;

	@Column(name="ROLE")
	private String role;
	
	
	public Integer getRole_id() {
		return role_id;
	}

	public void setRole_id(Integer role_id) {
		this.role_id = role_id;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}