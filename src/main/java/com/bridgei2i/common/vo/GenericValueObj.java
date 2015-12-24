package com.bridgei2i.common.vo; 

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;

import com.bridgei2i.common.util.ApplicationUtil;

public class GenericValueObj implements Serializable, Cloneable,Comparable
{   
	  private static final long serialVersionUID = 1L;
	
	  private boolean _isNew;
	  private boolean _isDeleted;
	  private boolean _isUpdated;
	  private boolean _isSelected;
  
  
	  public void setIsNew(boolean b) {
	    _isNew = b;
	  }
	  public void setIsDeleted(boolean b) {
	    _isDeleted = b;
	  }
	  public void setIsUpdated(boolean b) {
	    _isUpdated = b;
	  }
	  
	  public boolean getIsNew() {
	    return _isNew;
	  }
	  public boolean getIsDeleted() {
	    return _isDeleted;
	  }  
	  public boolean getIsUpdated() {
	    return _isUpdated;
	  }
	  
	  public int compareTo(Object o)
	  {
	  	return 1;
	  }
  
	public Object clone() 
	{
		try {
			ByteArrayOutputStream byteArr = new ByteArrayOutputStream();
			ObjectOutputStream objOut = new ObjectOutputStream(byteArr);
			objOut.writeObject(this);
			objOut.close();
			ByteArrayInputStream byteArrIn = new ByteArrayInputStream(byteArr.toByteArray());
			ObjectInputStream objIn = new ObjectInputStream(byteArrIn);
			Object obj = objIn.readObject();
			objIn.close();
			return obj;
			
		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}	

	public String toString() {
		
		return "";
	}

	public int hashCode() {
		int hashCode = 1;
		hashCode = 31
			* hashCode
			+ (int) (+serialVersionUID ^ (serialVersionUID >>> 32));
		hashCode = 31 * hashCode + (_isNew ? 1231 : 1237);
		hashCode = 31 * hashCode + (_isDeleted ? 1231 : 1237);
		hashCode = 31 * hashCode + (_isUpdated ? 1231 : 1237);
		return hashCode;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (o.getClass() != getClass()) {
			return false;
		}
		GenericValueObj castedObj = (GenericValueObj) o;
		return ((this._isNew == castedObj._isNew)
			&& (this._isDeleted == castedObj._isDeleted)
			&& (this._isUpdated == castedObj._isUpdated));
	}
	
	
	protected String getDocumentSourceTable() {
		return "";
	}

	
	public void convertEmptyStringToNull() {
		try {
			Class objClass = getClass();
			Field[] fields = ApplicationUtil.getAllFields(objClass);
			if(fields != null && fields.length > 0) {
				for(int i = 0; i < fields.length; i++) {
					if(fields[i] != null && fields[i].getType().getName().equals("java.lang.String")) {
						fields[i].setAccessible(true);
						Object obj = fields[i].get(this);
						if(obj != null && obj.equals("")) {
							fields[i].set(this, null);
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean getIsSelected() {
		return _isSelected;
	}

	public void setIsSelected(boolean selected) {
		_isSelected = selected;
	}
}