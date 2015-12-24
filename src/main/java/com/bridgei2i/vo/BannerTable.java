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
@Table(name="bannertable")
public class BannerTable {
	@Id
    @Column(name="id")
    @GeneratedValue
    private Integer id;
    
    @Column(name="templateChartInfoId")
    private Integer templateChartInfoId;
    
    @Column(name="metaDataId")
    private Integer metaDataId;
    
    @Column(name="employeeId")
    private Integer employeeId;
    
    @Column(name="quesname")
    private String quesname;
    
    @Column(name="mainVariable")
    private String mainVariable;
    
    @Column(name="weight")
    private String weight;
    
    @Column(name="q1")
    private String q1;
   
    
    @Column(name="q2")
    private Integer q2;
    
    @Column(name="count")
    private String count;
    
    @Column(name="significance")
    private String significance;
    
    @Column(name="mean")
    private String mean;
    
    @Column(name="standard_deviation")
    private String standard_deviation;
    
    @Column(name="numberofcount")
    private String numberofcount;
    
    @Column(name="reportmetadataId")
    private Integer reportmetadataId;

    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTemplateChartInfoId() {
		return templateChartInfoId;
	}

	public void setTemplateChartInfoId(Integer templateChartInfoId) {
		this.templateChartInfoId = templateChartInfoId;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getQuesname() {
		return quesname;
	}

	public void setQuesname(String quesname) {
		this.quesname = quesname;
	}

	public String getQ1() {
		return q1;
	}

	public void setQ1(String q1) {
		this.q1 = q1;
	}

	public Integer getQ2() {
		return q2;
	}

	public void setQ2(Integer q2) {
		this.q2 = q2;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getMainVariable() {
		return mainVariable;
	}

	public void setMainVariable(String mainVariable) {
		this.mainVariable = mainVariable;
	}

	public Integer getMetaDataId() {
		return metaDataId;
	}

	public void setMetaDataId(Integer metaDataId) {
		this.metaDataId = metaDataId;
	}

	public String getSignificance() {
		return significance;
	}

	public void setSignificance(String significance) {
		this.significance = significance;
	}

	public String getMean() {
		return mean;
	}

	public void setMean(String mean) {
		this.mean = mean;
	}

	public String getStandard_deviation() {
		return standard_deviation;
	}

	public void setStandard_deviation(String standard_deviation) {
		this.standard_deviation = standard_deviation;
	}

	public String getNumberofcount() {
		return numberofcount;
	}

	public void setNumberofcount(String numberofcount) {
		this.numberofcount = numberofcount;
	}
    
    
}
