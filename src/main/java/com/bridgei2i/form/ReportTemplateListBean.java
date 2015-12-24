package com.bridgei2i.form;

import java.util.List;

import com.bridgei2i.common.form.ListForm;

public class ReportTemplateListBean extends ListForm{
	
	private String editReportIndex;
	private String comments;
	private String index;
	private String editFlagArray[];
	private String isFromPreview;
	private String reportTitle;
	private String crossTabIndex;
	public String[] getEditFlagArray() {
		return editFlagArray;
	}

	public void setEditFlagArray(String[] editFlagArray) {
		this.editFlagArray = editFlagArray;
	}

	public String getEditReportIndex() {
		return editReportIndex;
	}

	public void setEditReportIndex(String editReportIndex) {
		this.editReportIndex = editReportIndex;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getIsFromPreview() {
		return isFromPreview;
	}

	public void setIsFromPreview(String isFromPreview) {
		this.isFromPreview = isFromPreview;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getCrossTabIndex() {
		return crossTabIndex;
	}

	public void setCrossTabIndex(String crossTabIndex) {
		this.crossTabIndex = crossTabIndex;
	}
	
}
