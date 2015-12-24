package com.bridgei2i.vo;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="category_list")
public class CategoryList {

	@Id
    @Column(name="id")
    @GeneratedValue
    private Integer id;
	
	@Column(name="categoryName")
	private String categoryName;
	
	@Column(name="createdDate")
	private String createdDate;

	@Column(name="updatedDate")
	private String updatedDate;

	public Integer getId() {
		return id;
	}

	public String getCategoryName() {
		return categoryName;
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

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	
}