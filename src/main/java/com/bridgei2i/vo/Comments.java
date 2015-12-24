package com.bridgei2i.vo;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name="comments")
public class Comments implements Cloneable,Serializable{
	@Id
	@Column(name="ID")
	@GeneratedValue
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="templateChartId")
	private TemplateChart templateChart;

	@Column(name="comments")
	private String comments;
	
	@Column(name="userId")
	private Integer userId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TemplateChart getTemplateChart() {
		return templateChart;
	}

	public void setTemplateChart(TemplateChart templateChart) {
		this.templateChart = templateChart;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
