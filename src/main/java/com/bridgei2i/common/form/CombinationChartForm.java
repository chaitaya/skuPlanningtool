
package com.bridgei2i.common.form;

import java.util.Collection;

import com.bridgei2i.common.util.ApplicationUtil;
import com.bridgei2i.common.vo.GenericChartValueObject;

public class CombinationChartForm extends GenericChartValueObject{
	
	protected Collection _pYAxisDetailValueObjs;
	protected Collection _sYAxisDetailValueObjs;
	private String pYAxisName;
	private String sYAxisName;
	
	public String getpYAxisName() {
		return pYAxisName;
	}

	public void setpYAxisName(String pYAxisName) {
		this.pYAxisName = pYAxisName;
	}

	public String getsYAxisName() {
		return sYAxisName;
	}

	public void setsYAxisName(String sYAxisName) {
		this.sYAxisName = sYAxisName;
	}

	public Collection get_pYAxisDetailValueObjs() {
		return _pYAxisDetailValueObjs;
	}

	public void set_pYAxisDetailValueObjs(Collection _pYAxisDetailValueObjs) {
		this._pYAxisDetailValueObjs = _pYAxisDetailValueObjs;
	}

	public Collection get_sYAxisDetailValueObjs() {
		return _sYAxisDetailValueObjs;
	}

	public void set_sYAxisDetailValueObjs(Collection _sYAxisDetailValueObjs) {
		this._sYAxisDetailValueObjs = _sYAxisDetailValueObjs;
	}
	
	@Override
	public String getXml() {
		// TODO Auto-generated method stub
		String chartXmlData = "";
		if(_pYAxisDetailValueObjs != null && _pYAxisDetailValueObjs.size()>0 
				&& _sYAxisDetailValueObjs != null && _sYAxisDetailValueObjs.size()>0){
			if(validateRequiredFields()){
				chartXmlData = ApplicationUtil.getChartXmlData(_pYAxisDetailValueObjs, categoryField,false);
				chartXmlData = chartXmlData + ApplicationUtil.getChartXmlData(_sYAxisDetailValueObjs, categoryField,true);
			}
			setXml(chartXmlData);
		}
		return super.getXml();
	}
}