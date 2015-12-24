package com.bridgei2i.form;

import org.springframework.web.multipart.MultipartFile;

import com.bridgei2i.common.form.ListForm;

public class UploadDataBean extends ListForm {
private MultipartFile data;
private MultipartFile codebook;
private String excelData;
private String index;
private String startWeek;
private String startYear;
private Integer planningCycleId ;
private Integer uploadSuccessFlag;

public MultipartFile getData() {
	return data;
}
public MultipartFile getCodebook() {
	return codebook;
}
public String getExcelData() {
	return excelData;
}
public String getIndex() {
	return index;
}
public String getStartWeek() {
	return startWeek;
}
public String getStartYear() {
	return startYear;
}
public Integer getPlanningCycleId() {
	return planningCycleId;
}
public Integer getUploadSuccessFlag() {
	return uploadSuccessFlag;
}
public void setData(MultipartFile data) {
	this.data = data;
}
public void setCodebook(MultipartFile codebook) {
	this.codebook = codebook;
}
public void setExcelData(String excelData) {
	this.excelData = excelData;
}
public void setIndex(String index) {
	this.index = index;
}
public void setStartWeek(String startWeek) {
	this.startWeek = startWeek;
}
public void setStartYear(String startYear) {
	this.startYear = startYear;
}
public void setPlanningCycleId(Integer planningCycleId) {
	this.planningCycleId = planningCycleId;
}
public void setUploadSuccessFlag(Integer uploadSuccessFlag) {
	this.uploadSuccessFlag = uploadSuccessFlag;
}


}
