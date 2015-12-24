
package com.bridgei2i.common.form;

import com.bridgei2i.common.vo.GenericValueObj;

public class DetailForm extends GenericForm{
	
	protected GenericValueObj detailValueObj;	
	
	public GenericValueObj getValueObject() {
		return detailValueObj;
	}
	
	public void setValueObject(GenericValueObj iplsGenericValueObj) {
		detailValueObj = iplsGenericValueObj;
	}
}
