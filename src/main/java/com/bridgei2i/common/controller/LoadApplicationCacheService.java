package com.bridgei2i.common.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.bridgei2i.common.constants.ApplicationConstants;
import com.bridgei2i.common.dao.ApplicationDAO;
import com.bridgei2i.common.dao.PlanningDAO;
import com.bridgei2i.common.exception.ApplicationException;
import com.bridgei2i.common.util.PropertiesUtil;
import com.bridgei2i.common.vo.MasterErrorCodes;
import com.bridgei2i.common.vo.PageTemplate;
import com.bridgei2i.common.vo.Roles;
import com.bridgei2i.common.vo.Users;
import com.bridgei2i.service.PlanningService;
import com.bridgei2i.service.UtilitiesService;
import com.bridgei2i.vo.MasterCommitStatus;
public class LoadApplicationCacheService {
	
	@Autowired(required=true)
	private ApplicationDAO applicationDAO;
	
	@Autowired(required=true)
	private PlanningDAO planningDAO;
	
	@Autowired(required=true)
	private UtilitiesService utilitiesService;
	
	public static Map applicationCacheObject = new HashMap();
	public LoadApplicationCacheService() {
	}
	
	@SuppressWarnings("rawtypes")
	public void init() throws ApplicationException {
		int mb = 1024 * 1024;
		// Getting the runtime reference from system
		Runtime runtime = Runtime.getRuntime();
		System.out.println("##### Heap utilization statistics [MB] #####");
		// Print used memory
		System.out.println("Used Memory:"
				+ (runtime.totalMemory() - runtime.freeMemory()) / mb);
		// Print free memory
		System.out.println("Free Memory:" + runtime.freeMemory() / mb);
		// Print total available memory
		System.out.println("Total Memory:" + runtime.totalMemory() / mb);
		// Print Maximum available memory
		System.out.println("Max Memory:" + runtime.maxMemory() / mb);
		
		planningDAO.loadCacheData();
	}
}