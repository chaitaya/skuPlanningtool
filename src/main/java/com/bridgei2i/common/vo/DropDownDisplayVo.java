package com.bridgei2i.common.vo;

import java.io.Serializable;

public class DropDownDisplayVo implements Cloneable,Serializable {

	private String displayName;
	private String value;
	private String overAll;
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getOverAll() {
		return overAll;
	}
	public void setOverAll(String overAll) {
		this.overAll = overAll;
	}
	
}
