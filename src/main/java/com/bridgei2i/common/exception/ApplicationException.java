package com.bridgei2i.common.exception;

import java.util.Map;

import org.apache.log4j.Logger;

import com.bridgei2i.common.constants.ApplicationConstants;
import com.bridgei2i.common.constants.ApplicationErrorCodes;
import com.bridgei2i.common.controller.LoadApplicationCacheService;

public class ApplicationException extends Exception {

	protected static Logger logger = Logger.getLogger(ApplicationException.class);
	private static final long serialVersionUID = 1L;
	private int exceptionNumber = ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER;
	private String exceptionMessage="";
	private String exceptionLocation;
	private Exception rootCause;
	private int exceptionType =ApplicationConstants.EXCEPTION;
	private ApplicationException(){	
	}
	
	public static ApplicationException createApplicationException(String className,String methodName,int exceptionNumber,int type,Exception rootCause) {
		//logger.error("Creating Application Exception");
		ApplicationException applicationException = new ApplicationException();
		applicationException.setExceptionNumber(exceptionNumber);
		//logger.error("Application Exception: "+exceptionNumber);
		
		Map errorCodesCacheObject = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.APPLICATION_ERRORS_CACHE_KEY);
		String 	exceptionMsg = (String)errorCodesCacheObject.get(exceptionNumber+"");
		applicationException.setExceptionMessage(exceptionMsg);
		
		String exceptionLocation = exceptionLocationMsg(className,methodName);
		applicationException.setExceptionLocation(exceptionLocation);
		//logger.error(exceptionLocation);
		applicationException.setExceptionType(type);
		if(rootCause != null){
			applicationException.setRootCause(rootCause);
			//logger.error("Application Exception Root Cause: "+rootCause.getMessage());
		}
		return applicationException;
	}
	
	public int getExceptionNumber() {
		return exceptionNumber;
	}

	public void setExceptionNumber(int exceptionNumber) {
		this.exceptionNumber = exceptionNumber;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public String getExceptionLocation() {
		return exceptionLocation;
	}

	public void setExceptionLocation(String exceptionLocation) {
		this.exceptionLocation = exceptionLocation;
	}
	
	
	public Exception getRootCause() {
		return rootCause;
	}

	public void setRootCause(Exception rootCause) {
		this.rootCause = rootCause;
	}

	
	public int getExceptionType() {
		return exceptionType;
	}

	public void setExceptionType(int exceptionType) {
		this.exceptionType = exceptionType;
	}

	public static final String exceptionLocationMsg(String className,String methodName)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[Application Exception Code Location - ");
		sb.append(" In Class - ");
		sb.append(className);
		sb.append(" | In Method Name - ");
		sb.append(methodName);
		sb.append(" ]");
		return sb.toString();
	}
}