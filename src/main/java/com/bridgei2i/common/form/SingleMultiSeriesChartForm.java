package com.bridgei2i.common.form;

import java.util.Collection;

import com.bridgei2i.common.util.ApplicationUtil;
import com.bridgei2i.common.vo.GenericChartValueObject;

public class SingleMultiSeriesChartForm extends GenericChartValueObject{
	
	protected Collection _detailValueObjs;
	private String xAxisName;
	private String yAxisName;
	
	public Collection getDetailValueObjs() {
		return _detailValueObjs;
	}

	public void setDetailValueObjs(Collection c) {
		_detailValueObjs = c;
	}

	public String getxAxisName() {
		return xAxisName;
	}

	public void setxAxisName(String xAxisName) {
		this.xAxisName = xAxisName;
	}

	public String getyAxisName() {
		return yAxisName;
	}

	public void setyAxisName(String yAxisName) {
		this.yAxisName = yAxisName;
	}

	@Override
	public String getXml() {
		// TODO Auto-generated method stub
		String chartXmlData = "";
		if(_detailValueObjs != null && _detailValueObjs.size()>0){
			if(validateRequiredFields()){
				chartXmlData = ApplicationUtil.getChartXmlData(_detailValueObjs, categoryField);
			}
			
		}
		setXml(chartXmlData);
		return super.getXml();
	}
}