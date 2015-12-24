
package com.bridgei2i.common.form;

import java.io.Serializable;

public class GenericForm implements Cloneable,Serializable{
	
	private String navigationPage;
	private String logicalName;
	
	public String getNavigationPage() {
		return navigationPage;
	}

	public void setNavigationPage(String navigationPage) {
		this.navigationPage = navigationPage;
	}

	public String getLogicalName() {
		return logicalName;
	}

	public void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
	}
}
