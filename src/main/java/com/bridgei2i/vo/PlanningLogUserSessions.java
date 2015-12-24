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
@Table(name="planning_log_user_sessions")
public class PlanningLogUserSessions {

	@Id
    @Column(name="id")
    @GeneratedValue
    private Integer id;
	
	@Column(name="productId")
	private String productId;
	
	@Column(name="userId")
	private String userId;
	
	public Integer getId() {
		return id;
	}

	public String getProductId() {
		return productId;
	}

	public String getUserId() {
		return userId;
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

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Column(name="createdDate")
	private String createdDate;

	@Column(name="updatedDate")
	private String updatedDate;
	
		
	
}
