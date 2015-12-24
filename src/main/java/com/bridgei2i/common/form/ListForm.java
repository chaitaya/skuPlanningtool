package com.bridgei2i.common.form;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class ListForm extends GenericForm{
	
	protected Collection _detailValueObjs;
	public String selectedFilterIndex;
	
	public Collection getDetailValueObjs() {
		return _detailValueObjs;
	}

	public void setDetailValueObjs(Collection c) {
		_detailValueObjs = c;
	}

	protected String[] _editFlagArray;

	public void setEditFlagArray(String[] s) {
		_editFlagArray = s;
	}

	public String[] getEditFlagArray() {
		return _editFlagArray;
	}

	public String getSelectedFilterIndex() {
		return selectedFilterIndex;
	}

	public void setSelectedFilterIndex(String selectedFilterIndex) {
		this.selectedFilterIndex = selectedFilterIndex;
	}

	
	
}