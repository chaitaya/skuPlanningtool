package com.bridgei2i.common.vo;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.criteria.Order;

import com.bridgei2i.vo.OverrideUnitsLog;


/**
 * User domain
 */
@Entity
@Table(name="users")
public class Users {

	@Id
	@Column(name="login_id")
	@GeneratedValue
	private Integer login_id;
	private String userName;
	private String password;
	@OrderBy("firstName")
	private String firstName;
	private String lastName;
	private String emailId;
	private String managerId;
	
	@Transient
	private String newPassword;
	
	@Transient
	private String confirmNewPassword;
	
	
/*	@Transient
	private String employeeId;
	
	@Transient
	private String teamName;*/
	
	 @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Roles.class)
	    @JoinTable(name = "ROLES", joinColumns = { @JoinColumn(name = "LOGIN_ID") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
	private List<Roles> roles =null;
	 
	 @Transient
	 private List rolesList;
	 
	/* @OneToMany(mappedBy = "forecastUnitObj", fetch = FetchType.EAGER)
		private List<OverrideUnitsLog> overrideUnit;*/
	 
	public Integer getLogin_id() {
		return login_id;
	}

	public void setLogin_id(Integer login_id) {
		this.login_id = login_id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

/*	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	*/
	public List<Roles> getRoles() {
		return roles;
	}

	public void setRoles(List<Roles> roles) {
		this.roles = roles;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

/*	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
*/
	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

/*	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getAcceptedDate() {
		return acceptedDate;
	}

	public void setAcceptedDate(Timestamp acceptedDate) {
		this.acceptedDate = acceptedDate;
	}

	

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}*/
	public List getRolesList() {
		return rolesList;
	}

	public void setRolesList(List rolesList) {
		this.rolesList = rolesList;
	}

	
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}

	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}	
	
}