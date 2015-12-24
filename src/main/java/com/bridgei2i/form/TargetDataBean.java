package com.bridgei2i.form;

import org.springframework.web.multipart.MultipartFile;

import com.bridgei2i.common.form.ListForm;

public class TargetDataBean extends ListForm  {
	private MultipartFile data;
	private MultipartFile btbData;
	private MultipartFile eventData;
	private Integer uploadSuccessFlag;
	private Integer planningCycleId ;

	public MultipartFile getData() {
		return data;
	}

	public void setData(MultipartFile data) {
		this.data = data;
	}

	public Integer getUploadSuccessFlag() {
		return uploadSuccessFlag;
	}

	public void setUploadSuccessFlag(Integer uploadSuccessFlag) {
		this.uploadSuccessFlag = uploadSuccessFlag;
	}

	public Integer getPlanningCycleId() {
		return planningCycleId;
	}

	public void setPlanningCycleId(Integer planningCycleId) {
		this.planningCycleId = planningCycleId;
	}

	public MultipartFile getBtbData() {
		return btbData;
	}

	public void setBtbData(MultipartFile btbData) {
		this.btbData = btbData;
	}

	public MultipartFile getEventData() {
		return eventData;
	}

	public void setEventData(MultipartFile eventData) {
		this.eventData = eventData;
	}

		
}
