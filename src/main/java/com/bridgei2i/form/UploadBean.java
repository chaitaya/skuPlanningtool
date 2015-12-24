package com.bridgei2i.form;

import org.springframework.web.multipart.MultipartFile;

import com.bridgei2i.common.form.ListForm;


public class UploadBean extends ListForm{

	private MultipartFile surveyData;
	private MultipartFile codeBookData;
	public MultipartFile getSurveyData() {
		return surveyData;
	}
	public void setSurveyData(MultipartFile surveyData) {
		this.surveyData = surveyData;
	}
	public MultipartFile getCodeBookData() {
		return codeBookData;
	}
	public void setCodeBookData(MultipartFile codeBookData) {
		this.codeBookData = codeBookData;
	}

	
	
}
