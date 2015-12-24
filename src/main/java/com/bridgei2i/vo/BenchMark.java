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
@Table(name="master_distribution_type")
public class BenchMark {
	@Id
    @Column(name="id")
    @GeneratedValue
    private Integer id;
       
    @Column(name="masterBenchMarkId")
    private Integer masterBenchMarkId;
   
    @Column(name="templateChartInfoId")
    private Integer templateChartInfoId;
    
    @Column(name="value")
    private String value;
    
    @Column(name="reportmetadataId")
    private Integer reportmetadataId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMasterBenchMarkId() {
		return masterBenchMarkId;
	}

	public void setMasterBenchMarkId(Integer masterBenchMarkId) {
		this.masterBenchMarkId = masterBenchMarkId;
	}

	public Integer getTemplateChartInfoId() {
		return templateChartInfoId;
	}

	public void setTemplateChartInfoId(Integer templateChartInfoId) {
		this.templateChartInfoId = templateChartInfoId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getReportmetadataId() {
		return reportmetadataId;
	}

	public void setReportmetadataId(Integer reportmetadataId) {
		this.reportmetadataId = reportmetadataId;
	}
}