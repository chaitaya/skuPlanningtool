package com.bridgei2i.vo;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.bridgei2i.common.dao.ApplicationDAO;
@Entity
@Table(name="templatechart")
public class TemplateChart implements Cloneable,Serializable {
	@Id
	@Column(name="id")
	@GeneratedValue
	private Integer id;
	
	@Column(name="reportTitle")
	private String reportTitle="Untitled";
	
	
	@Column(name="statusId")
	private Integer statusId;
	
	@Column(name="reportForOrganization")
	private String reportForOrganization;
	
	@Column(name="filter1")
	private String filter1;
	
	@Column(name="filter2")
	private String filter2;
	
	@Column(name="filter3")
	private String filter3;
	
	@Column(name="filterValues1")
	private String filterValues1;
	
	@Column(name="filterValues2")
	private String filterValues2;
	
	@Column(name="filterValues3")
	private String filterValues3;

	@Column(name="reportType")
	private String reportType="N";
	
	@Column(name="enablePreview")
	private String enablePreview;
	
	@Transient
	private String[] filterValuesArray1;
	
	@Transient
	private String[] filterValuesArray2;
	
	@Transient
	private String[] filterValuesArray3;

	
	@Column(name="createdDate")
	private Date createdDate;
	
	@Column(name="updatedDate")
	private Date updatedDate;
	
	@Column(name="createdBy")
	private Long createdBy;
	
	@Column(name="updatedBy")
	private Long updatedBy;

	@Transient
	private boolean deleted;
	
	@Transient
	private String statusName;
	
	@Transient
	private String assignReportNames;
	
	@Transient
	private List distributionList;
	
	@Transient
	private List reportMetaDataIdList;

	@Transient
	private List teamReport;
	
	@Transient
	private List teamReportHeaderList;
	
	@Transient
	private List summaryList;
	
	@Transient
	private List respondentsHeaderList;
	
	@Transient
	private List trendReportList;
	
	@Transient
	private String isOverall;
	
	@Transient
	private Integer reportMetaDataId;
	
	@Transient
	private Map reportMetaDataMapObj;
	
	@Transient
	private List managerByLocationList;
	
	@Transient
    private List segmentByLocationList;
	
	@OneToMany(fetch=FetchType.EAGER,mappedBy="templateChart",cascade=CascadeType.ALL)
	private List<TemplateChartCategory> templateChartCategories;
	
	@OneToMany(mappedBy="templateChart",cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<TemplateChartReportAssign> templateChartReportAssigns;
	
	@OneToMany(mappedBy="templateChart",cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Comments> comments;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getReportForOrganization() {
		return reportForOrganization;
	}

	public void setReportForOrganization(String reportForOrganization) {
		this.reportForOrganization = reportForOrganization;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public List<TemplateChartCategory> getTemplateChartCategories() {
		return templateChartCategories;
	}

	public void setTemplateChartCategories(
			List<TemplateChartCategory> templateChartCategories) {
		this.templateChartCategories = templateChartCategories;
	}

	public List<TemplateChartReportAssign> getTemplateChartReportAssigns() {
		return templateChartReportAssigns;
	}

	public void setTemplateChartReportAssigns(
			List<TemplateChartReportAssign> templateChartReportAssigns) {
		this.templateChartReportAssigns = templateChartReportAssigns;
	}

	public List<Comments> getComments() {
		return comments;
	}

	public void setComments(List<Comments> comments) {
		this.comments = comments;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getAssignReportNames() {
		return assignReportNames;
	}

	public void setAssignReportNames(String assignReportNames) {
		this.assignReportNames = assignReportNames;
	}

	public String getFilter1() {
		return filter1;
	}

	public void setFilter1(String filter1) {
		this.filter1 = filter1;
	}

	public String getFilter2() {
		return filter2;
	}

	public void setFilter2(String filter2) {
		this.filter2 = filter2;
	}

	public String getFilter3() {
		return filter3;
	}

	public void setFilter3(String filter3) {
		this.filter3 = filter3;
	}

	public String getFilterValues1() {
		return filterValues1;
	}

	public void setFilterValues1(String filterValues1) {
		this.filterValues1 = filterValues1;
		if(filterValues1 != null){
			this.filterValuesArray1 = filterValues1.split(",");
		}
	}

	public String getFilterValues2() {
		return filterValues2;
	}

	public void setFilterValues2(String filterValues2) {
		this.filterValues2 = filterValues2;
		if(filterValues2 != null){
			this.filterValuesArray2 = filterValues2.split(",");
		}
	}

	public String getFilterValues3() {
		return filterValues3;
	}

	public void setFilterValues3(String filterValues3) {
		this.filterValues3 = filterValues3;
		if(filterValues3 != null){
			this.filterValuesArray3 = filterValues3.split(",");
		}
	}

	public String[] getFilterValuesArray1() {
		return filterValuesArray1;
	}

	public void setFilterValuesArray1(String[] filterValuesArray1) {
		this.filterValuesArray1 = filterValuesArray1;
		if(filterValuesArray1!=null){
			String str="";
			int len = filterValuesArray1.length;
			for(int i=0;i<len;i++){
				str = str+filterValuesArray1[i];
				if(i+1<len){
					str = str+",";
				}
				filterValues1 = str;
			}
		}
	}

	public String[] getFilterValuesArray2() {
		return filterValuesArray2;
	}

	public void setFilterValuesArray2(String[] filterValuesArray2) {
		this.filterValuesArray2 = filterValuesArray2;
		if(filterValuesArray2!=null){
			String str="";
			int len = filterValuesArray2.length;
			for(int i=0;i<len;i++){
				str = str+filterValuesArray2[i];
				if(i+1<len){
					str = str+",";
				}
				filterValues2 = str;
			}
		}
	}

	public String[] getFilterValuesArray3() {
		return filterValuesArray3;
	}

	public void setFilterValuesArray3(String[] filterValuesArray3) {
		this.filterValuesArray3 = filterValuesArray3;
		if(filterValuesArray3!=null){
			String str="";
			int len = filterValuesArray3.length;
			for(int i=0;i<len;i++){
				str = str+filterValuesArray3[i];
				if(i+1<len){
					str = str+",";
				}
				filterValues3 = str;
			}
		}
	}
	
	public Object clone() 
	{
		try {
			ByteArrayOutputStream byteArr = new ByteArrayOutputStream();
			ObjectOutputStream objOut = new ObjectOutputStream(byteArr);
			objOut.writeObject(this);
			objOut.close();
			ByteArrayInputStream byteArrIn = new ByteArrayInputStream(byteArr.toByteArray());
			ObjectInputStream objIn = new ObjectInputStream(byteArrIn);
			Object obj = objIn.readObject();
			objIn.close();
			return obj;
			
		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	
	@Override
	public String toString() {
		return "TemplateChart [id=" + id + ", reportTitle=" + reportTitle
				+ ", statusId=" + statusId + ", reportForOrganization="
				+ reportForOrganization + ", filter1=" + filter1 + ", filter2="
				+ filter2 + ", filter3=" + filter3 + ", filterValues1="
				+ filterValues1 + ", filterValues2=" + filterValues2
				+ ", filterValues3=" + filterValues3 + ", filterValuesArray1="
				+ Arrays.toString(filterValuesArray1) + ", filterValuesArray2="
				+ Arrays.toString(filterValuesArray2) + ", filterValuesArray3="
				+ Arrays.toString(filterValuesArray3) + ", createdDate="
				+ createdDate + ", updatedDate=" + updatedDate + ", createdBy="
				+ createdBy + ", updatedBy=" + updatedBy + ", deleted="
				+ deleted + ", statusName=" + statusName
				+ ", assignReportNames=" + assignReportNames
				+ ", distributionList=" + distributionList + ", isOverall="
				+ isOverall + ", templateChartCategories="
				+ templateChartCategories + ", templateChartReportAssigns="
				+ templateChartReportAssigns + ", comments=" + comments
				+ ", ReportType=" + reportType + ", noOfResponse="
				+ noOfResponse + ", codeBookappliedFilter1Variable="
				+ codeBookappliedFilter1Variable
				+ ", codeBookappliedFilter2Variable="
				+ codeBookappliedFilter2Variable
				+ ", codeBookappliedFilter3Variable="
				+ codeBookappliedFilter3Variable
				+ ", codeBookappliedFilter1Value="
				+ codeBookappliedFilter1Value
				+ ", codeBookappliedFilter2Value="
				+ codeBookappliedFilter2Value
				+ ", codeBookappliedFilter3Value="
				+ codeBookappliedFilter3Value + "]";
	}

	public List getDistributionList() {
		return distributionList;
	}

	public void setDistributionList(List distributionList) {
		this.distributionList = distributionList;
	}

	public String getIsOverall() {
		return isOverall;
	}

	public void setIsOverall(String isOverall) {
		this.isOverall = isOverall;
	}

	public List getTeamReport() {
		return teamReport;
	}

	public void setTeamReport(List teamReport) {
		this.teamReport = teamReport;
	}

	public List getTeamReportHeaderList() {
		return teamReportHeaderList;
	}

	public void setTeamReportHeaderList(List teamReportHeaderList) {
		this.teamReportHeaderList = teamReportHeaderList;
	}

	@Transient
	private Integer noOfResponse;
	
	@Transient
	private Integer totalNoResponse;
	
	@Transient
	private String percentageOfResponse;
	

	public String getPercentageOfResponse() {
		return percentageOfResponse;
	}

	public void setPercentageOfResponse(String percentageOfResponse) {
		this.percentageOfResponse = percentageOfResponse;
	}

	public Integer getTotalNoResponse() {
		return totalNoResponse;
	}

	public void setTotalNoResponse(Integer totalNoResponse) {
		this.totalNoResponse = totalNoResponse;
	}

	@Transient
	private String codeBookappliedFilter1Variable;
	
	public String getCodeBookappliedFilter1Variable() {
		return codeBookappliedFilter1Variable;
	}

	public void setCodeBookappliedFilter1Variable(
			String codeBookappliedFilter1Variable) {
		this.codeBookappliedFilter1Variable = codeBookappliedFilter1Variable;
	}

	public String getCodeBookappliedFilter2Variable() {
		return codeBookappliedFilter2Variable;
	}

	public void setCodeBookappliedFilter2Variable(
			String codeBookappliedFilter2Variable) {
		this.codeBookappliedFilter2Variable = codeBookappliedFilter2Variable;
	}

	public String getCodeBookappliedFilter3Variable() {
		return codeBookappliedFilter3Variable;
	}

	public void setCodeBookappliedFilter3Variable(
			String codeBookappliedFilter3Variable) {
		this.codeBookappliedFilter3Variable = codeBookappliedFilter3Variable; 
	}

	public String getCodeBookappliedFilter1Value() {
		return codeBookappliedFilter1Value;
	}

	public void setCodeBookappliedFilter1Value(String codeBookappliedFilter1Value) {
		this.codeBookappliedFilter1Value = codeBookappliedFilter1Value;
	}

	public String getCodeBookappliedFilter2Value() {
		return codeBookappliedFilter2Value;
	}

	public void setCodeBookappliedFilter2Value(String codeBookappliedFilter2Value) {
		this.codeBookappliedFilter2Value = codeBookappliedFilter2Value;
	}

	public String getCodeBookappliedFilter3Value() {
		return codeBookappliedFilter3Value;
	}

	public void setCodeBookappliedFilter3Value(String codeBookappliedFilter3Value) {
		this.codeBookappliedFilter3Value = codeBookappliedFilter3Value;
	}

	@Transient
	private String codeBookappliedFilter2Variable;
	
	@Transient
	private String codeBookappliedFilter3Variable;
	
	@Transient
	private String codeBookappliedFilter1Value;
	
	@Transient
	private String codeBookappliedFilter2Value;
	
	@Transient
	private String codeBookappliedFilter3Value;
	
	public Integer getNoOfResponse() {
		return noOfResponse;
	}

	public void setNoOfResponse(Integer noOfResponse) {
		this.noOfResponse = noOfResponse;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public List getRespondentsHeaderList() {
		return respondentsHeaderList;
	}

	public void setRespondentsHeaderList(List respondentsHeaderList) {
		this.respondentsHeaderList = respondentsHeaderList;
	}

	public String getEnablePreview() {
		return enablePreview;
	}

	public void setEnablePreview(String enablePreview) {
		this.enablePreview = enablePreview;
	}

	public List getTrendReportList() {
		return trendReportList;
	}

	public void setTrendReportList(List trendReportList) {
		this.trendReportList = trendReportList;
	}

	public Integer getReportMetaDataId() {
		return reportMetaDataId;
	}

	public void setReportMetaDataId(Integer reportMetaDataId) {
		this.reportMetaDataId = reportMetaDataId;
	}

	public List getReportMetaDataIdList() {
		return reportMetaDataIdList;
	}

	public void setReportMetaDataIdList(List reportMetaDataIdList) {
		this.reportMetaDataIdList = reportMetaDataIdList;
	}

	public Map getReportMetaDataMapObj() {
		return reportMetaDataMapObj;
	}

	public void setReportMetaDataMapObj(Map reportMetaDataMapObj) {
		this.reportMetaDataMapObj = reportMetaDataMapObj;
	}

	public List getManagerByLocationList() {
		return managerByLocationList;
	}

	public void setManagerByLocationList(List managerByLocationList) {
		this.managerByLocationList = managerByLocationList;
	}

	public List getSummaryList() {
		return summaryList;
	}

	public void setSummaryList(List summaryList) {
		this.summaryList = summaryList;
	}

	public List getSegmentByLocationList() {
		return segmentByLocationList;
	}

	public void setSegmentByLocationList(List segmentByLocationList) {
		this.segmentByLocationList = segmentByLocationList;
	}
	
}