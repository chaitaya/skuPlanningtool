package com.bridgei2i.common.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;


import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.bridgei2i.common.controller.LoginLogoutController;

public class PropertiesUtil extends PropertyPlaceholderConfigurer {
	 private static Map propertiesMap;
	 private static Logger logger=Logger.getLogger(PropertiesUtil.class.getName());
	@SuppressWarnings("deprecation")
	@Override
	   protected void processProperties(ConfigurableListableBeanFactory beanFactory, 
	             Properties props) throws BeansException { 
	        super.processProperties(beanFactory, props); 
	        logger.info("Entering Into processPropertiesFunction");
	        propertiesMap = new HashMap<String, String>(); 
	        for (Object key : props.keySet()) { 
	            String keyStr = key.toString(); 
	            propertiesMap.put(keyStr, parseStringValue(props.getProperty(keyStr), 
	                props, new HashSet())); 
	            logger.info("Exiting From processProperties Function");
	        } 
	    } 
	  
	    public static String getProperty(String name) { 
	        return (String)propertiesMap.get(name); 
	    } 

}
