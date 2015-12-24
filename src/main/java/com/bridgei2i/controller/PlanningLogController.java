package com.bridgei2i.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Vector;





import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.bridgei2i.common.constants.ApplicationConstants;
import com.bridgei2i.common.constants.ApplicationErrorCodes;
import com.bridgei2i.common.controller.LoadApplicationCacheService;
import com.bridgei2i.common.exception.ApplicationException;
import com.bridgei2i.common.util.ApplicationUtil;
import com.bridgei2i.common.util.PropertiesUtil;
import com.bridgei2i.common.vo.PageTemplate;
import com.bridgei2i.common.vo.PageTemplateFilters;
import com.bridgei2i.common.vo.Users;
import com.bridgei2i.form.PlanningLogBean;
import com.bridgei2i.service.PlanningService;
import com.bridgei2i.vo.PlanningCycle;
import com.bridgei2i.vo.PlanningLog;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

@Controller
@SessionAttributes("planningLogBean")
@Scope("session")
public class PlanningLogController {
	private static Logger logger = Logger
			.getLogger(PlanningLogController.class);
	@Autowired(required = true)
	private PlanningService planningService;

	@RequestMapping(value = "/planningLog.htm")
	public ModelAndView getPlanningLog(
			@ModelAttribute("PlanningLogBean") PlanningLogBean planningLogBean,
			BindingResult result, HttpServletRequest request) {
		Integer planningCycleId = planningLogBean.getPlanningCycleId();
		System.out.println(planningCycleId);
		logger.debug("Entering and leaving from getPlanningLog");
		return new ModelAndView("planningLog", "model", planningLogBean);
	} /* closing the getUploadDataDetails method */

	@RequestMapping(value = "/planningLogDetails.htm")
	public ModelAndView getPlanningLogDetails(
			@ModelAttribute("newBean") PlanningLogBean planningLogBean,
			BindingResult result, HttpServletRequest request, ModelMap map) {
		try {
			int planningCycleId = planningLogBean.getPlanningCycleId();
			String sessionPlanningcycleId = (String) ApplicationUtil
					.getObjectFromSession(
							ApplicationConstants.PLANNING_CYCLE_ID, request);
			if (planningCycleId > 0) {
				ApplicationUtil.setObjectInSession(
						ApplicationConstants.PLANNING_CYCLE_ID, planningCycleId
								+ "", request);
			} else if (!ApplicationUtil.isEmptyOrNull(sessionPlanningcycleId)) {
				planningCycleId = Integer.parseInt(sessionPlanningcycleId);
				planningLogBean.setPlanningCycleId(planningCycleId);
			}
			String isFromDashboard = request.getParameter("isFromDashboard");
			List planningLogList = (List) ApplicationUtil.getObjectFromSession(
					ApplicationConstants.PLANNING_LOG_LIST, request);
			if (!ApplicationUtil.isEmptyOrNull(isFromDashboard)
					&& isFromDashboard.equalsIgnoreCase("true")
					&& planningLogList != null && planningLogList.size() > 0) {
				planningLogBean.setDetailValueObjs(planningLogList);
				return new ModelAndView("planningLog", "model", planningLogBean);
			}

			Users users = (Users) ApplicationUtil.getObjectFromSession(
					ApplicationConstants.APPLICATION_ACT_AS_USER, request);
			List rolesList = users.getRolesList();
			Integer userId = users.getLogin_id();
			if (!ApplicationUtil.isEmptyOrNull(ApplicationUtil
					.getObjectFromSession(
							ApplicationConstants.PLANNING_LOG_SESSION_OBJECT,
							request))) {
				ApplicationUtil.removeObjectFromSession(
						ApplicationConstants.PLANNING_LOG_SESSION_OBJECT,
						request);
			}
			PlanningCycle activePlanningCycle = (PlanningCycle) ApplicationUtil
					.getObjectFromSession(
							ApplicationConstants.ACTIVE_PLANNINGCYCLE_OBJ,
							request);
			String startWeek = activePlanningCycle.getStartWeek();
			Integer week = Integer.parseInt(startWeek);
			String startYear = activePlanningCycle.getStartYear();
			Integer year = Integer.parseInt(startYear);
			List<String> planningLogListFromSession = planningService
					.getPlanningLogUserSession(userId);
			if (!ApplicationUtil.isEmptyOrNull(planningLogListFromSession)
					&& planningLogListFromSession.size() > 0) {
				String skuListString = "";
				int count = 0;
				for (String s : planningLogListFromSession) {
					skuListString += s;
					if (count != planningLogListFromSession.size() - 1) {
						skuListString += ",";
					}
					count++;
				}
				planningLogBean.setSelectedSkuList(skuListString);
				ApplicationUtil.setObjectInSession(
						ApplicationConstants.PLANNING_LOG_SESSION_OBJECT,
						skuListString, request);
				map.addAttribute(
						ApplicationConstants.PLANNING_LOG_SESSION_BEAN,
						planningLogBean);
				return new ModelAndView("redirect:planningLogSelectedSku.htm");
			} else {
				Map filtersListMapObj = new HashMap();
				Map dashboardFiltersListMapObj = new HashMap();
				Map pageTemplateCacheObj = (Map) LoadApplicationCacheService.applicationCacheObject
						.get(ApplicationConstants.PAGE_TEMPLATES_CACHE_KEY);
				PageTemplate pageTemplate = (PageTemplate) pageTemplateCacheObj
						.get("PC_DASHBOARD");
				List pageTemplateFilters = pageTemplate
						.getPageTemplateFilters();
				Map selectedFilterValuesMapObj = new HashMap();
				Map selecteDashboardFilterValuesMapObj = new HashMap();
				String skuId = PropertiesUtil
						.getProperty(ApplicationConstants.PRODUCT_NUMBER);
				String categoryColumn = PropertiesUtil
						.getProperty(ApplicationConstants.CATEGORY);
				String businessColumn = PropertiesUtil
						.getProperty(ApplicationConstants.BUSINESS_TYPE);
				String roleFilterCondition = "";
				if (rolesList != null
						&& rolesList.size() > 0
						&& rolesList
								.contains(ApplicationConstants.PRODUCT_MANAGER)) {
					roleFilterCondition = " AND data."
							+ skuId
							+ " IN (select skuList.productId from SkuList as skuList where skuList.id IN "
							+ "(select skuUser.skuListId from SkuUserMapping as skuUser where skuUser.userId="
							+ userId
							+ ")) "
							+ " AND data."
							+ businessColumn
							+ " IN (select distinct skuUser.business from SkuUserMapping as skuUser where skuUser.userId="
							+ userId + ") ";
				} else if (rolesList != null
						&& rolesList.size() > 0
						&& rolesList
								.contains(ApplicationConstants.CATEGORY_MANAGER)) {
					roleFilterCondition = " AND data."
							+ categoryColumn
							+ " IN (select catList.categoryName from CategoryList as catList where catList.id IN "
							+ "(select catUser.categoryId from CategoryUserMapping as catUser where catUser.userId="
							+ userId
							+ ")) "
							+ " AND data."
							+ businessColumn
							+ " IN (select distinct catUser.business from CategoryUserMapping as catUser where catUser.userId = "
							+ userId + ")";
				}

				String businessValue = null;
				StringBuffer filterWhereClauseSB = new StringBuffer();
				if (pageTemplateFilters != null) {
					int selectedFilterIndex = 0;
					int size = pageTemplateFilters.size();
					for (int i = 0; i < size; i++) {
						PageTemplateFilters pageTemplateFilter = (PageTemplateFilters) pageTemplateFilters
								.get(i);
						String filterFieldName = pageTemplateFilter
								.getFilterFieldName();
						String filterLabel = pageTemplateFilter
								.getFilterLabel();
						String filterVariable = null;
						String filterConditionValue = null;
						if (pageTemplateFilter.getDefaultFilterValues() == 1) {
							selectedFilterIndex = i;
							filterVariable = pageTemplateFilter
									.getFilterVariable();
							String tableName = pageTemplateFilter
									.getTableName();
							List filtersListObj = planningService
									.getPageTemplateFilters(userId,
											filterVariable, tableName,
											filterWhereClauseSB.toString(),
											rolesList, businessValue,week,year);
							filtersListMapObj.put(filterFieldName,
									filtersListObj);
							dashboardFiltersListMapObj.put(filterFieldName,
									filtersListObj);
							if (filtersListObj != null
									&& filtersListObj.size() > 0) {
								filterConditionValue = filtersListObj.get(0)
										.toString();
								selectedFilterValuesMapObj.put(filterFieldName,
										filterConditionValue);
								selecteDashboardFilterValuesMapObj.put(
										filterFieldName, filterConditionValue);
								filterWhereClauseSB.append(" and ");
								filterWhereClauseSB.append(filterVariable
										+ " =  ");
								filterWhereClauseSB.append("'"
										+ filterConditionValue + "' ");
								if (!ApplicationUtil
										.isEmptyOrNull(filterFieldName)
										&& filterFieldName
												.equalsIgnoreCase("BUSINESS")) {
									businessValue = filterConditionValue;
								}
							}
						}
					}
					for (int i = (selectedFilterIndex + 1); i < size; i++) {
						PageTemplateFilters nextPageTemplateFilter = (PageTemplateFilters) pageTemplateFilters
								.get(i);
						String filterFieldName = nextPageTemplateFilter
								.getFilterFieldName();
						if (nextPageTemplateFilter.getDefaultFilterValues() != 1) {
							String filterVariable = nextPageTemplateFilter
									.getFilterVariable();
							String tableName = nextPageTemplateFilter
									.getTableName();
							List filtersListObj = planningService
									.getPageTemplateFilters(userId,
											filterVariable, tableName,
											filterWhereClauseSB.toString(),
											rolesList, businessValue,week,year);
							filtersListMapObj.put(filterFieldName,
									filtersListObj);
							dashboardFiltersListMapObj.put(filterFieldName,
									filtersListObj);
						}
					}
				}
				
				ApplicationUtil.setObjectInSession(
						ApplicationConstants.SELECTED_FILTER_VALUES_MAP,
						selectedFilterValuesMapObj, request);
				ApplicationUtil
						.setObjectInSession(
								ApplicationConstants.SELECTED_DASHBOARD_FILTER_VALUES_MAP,
								selecteDashboardFilterValuesMapObj, request);
				ApplicationUtil.setObjectInSession(
						ApplicationConstants.FILTER_LIST_VALUES_MAP,
						filtersListMapObj, request);
				ApplicationUtil.setObjectInSession(
						ApplicationConstants.DASHBOARD_FILTER_LIST_VALUES_MAP,
						dashboardFiltersListMapObj, request);
				planningLogList = planningService.getPlanningLogDetails(userId,
						planningCycleId, filterWhereClauseSB.toString(),
						roleFilterCondition, rolesList, businessValue, week,
						year, 1, null);
				ApplicationUtil.setObjectInSession(
						ApplicationConstants.PLANNING_LOG_LIST,
						planningLogList, request);
				ApplicationUtil.setObjectInSession(
						ApplicationConstants.ACTIVE_PLANNING_TAB,
						"planningLog", request);
				planningLogBean.setDetailValueObjs(planningLogList);
				map.addAttribute(
						ApplicationConstants.PLANNING_LOG_SESSION_BEAN,
						planningLogBean);
			}
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		map.addAttribute("planningLogBean", planningLogBean);
		logger.debug("Entering and leaving from getplanningLogDetails");
		return new ModelAndView("planningLog", "model", planningLogBean);
	}

	@RequestMapping(value = "/planningLogOnFilterChange.htm", method = RequestMethod.POST)
	public ModelAndView planningLogOnFilterChange(
			@ModelAttribute("planningLogBean") PlanningLogBean planningLogBean,
			BindingResult result, HttpServletRequest request) {
		try {
			String selectedFilterIndexStr = planningLogBean
					.getSelectedFilterIndex();
			String selectedFilterIndexArry[] = selectedFilterIndexStr
					.split(",");
			int selectedFilterIndex = Integer
					.parseInt(selectedFilterIndexArry[0]);
			Map filtersListMapObj = (Map) ApplicationUtil.getObjectFromSession(
					ApplicationConstants.FILTER_LIST_VALUES_MAP, request);
			Map dashboardFiltersListMapObj = (Map) ApplicationUtil
					.getObjectFromSession(
							ApplicationConstants.DASHBOARD_FILTER_LIST_VALUES_MAP,
							request);
			if (filtersListMapObj == null) {
				filtersListMapObj = new HashMap();
				dashboardFiltersListMapObj = new HashMap();
			}
			Map selectedFilterValuesMapObj = (Map) ApplicationUtil
					.getObjectFromSession(
							ApplicationConstants.SELECTED_FILTER_VALUES_MAP,
							request);
			Map selecteDashboardFilterValuesMapObj = (Map) ApplicationUtil
					.getObjectFromSession(
							ApplicationConstants.SELECTED_DASHBOARD_FILTER_VALUES_MAP,
							request);
			if (selectedFilterValuesMapObj == null) {
				selectedFilterValuesMapObj = new HashMap();
				selecteDashboardFilterValuesMapObj = new HashMap();
			}
			Users users = (Users) ApplicationUtil.getObjectFromSession(
					ApplicationConstants.APPLICATION_ACT_AS_USER, request);
			List rolesList = users.getRolesList();
			Integer userId = users.getLogin_id();
			PlanningCycle activePlanningCycle = (PlanningCycle) ApplicationUtil
					.getObjectFromSession(
							ApplicationConstants.ACTIVE_PLANNINGCYCLE_OBJ,
							request);
			String startWeek = activePlanningCycle.getStartWeek();
			Integer week = Integer.parseInt(startWeek);
			String startYear = activePlanningCycle.getStartYear();
			Integer year = Integer.parseInt(startYear);
			Map pageTemplateCacheObj = (Map) LoadApplicationCacheService.applicationCacheObject
					.get(ApplicationConstants.PAGE_TEMPLATES_CACHE_KEY);
			PageTemplate pageTemplate = (PageTemplate) pageTemplateCacheObj
					.get("PC_DASHBOARD");
			List pageTemplateFilters = pageTemplate.getPageTemplateFilters();
			List filters = pageTemplate.getFiltersList();
			String skuId = PropertiesUtil
					.getProperty(ApplicationConstants.PRODUCT_NUMBER);
			String product = PropertiesUtil
					.getProperty(ApplicationConstants.PRODUCT_ID);
			String id = PropertiesUtil.getProperty(ApplicationConstants.ID);
			String skuListId = PropertiesUtil
					.getProperty(ApplicationConstants.SKU_LIST_ID);
			String categoryColumn = PropertiesUtil
					.getProperty(ApplicationConstants.CATEGORY);
			String businessColumn = PropertiesUtil
					.getProperty(ApplicationConstants.BUSINESS_TYPE);
			String roleFilterCondition = "";
			if (rolesList != null && rolesList.size() > 0
					&& rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)) {
				roleFilterCondition = " AND data."
						+ skuId
						+ " IN(select skuList.productId from SkuList as skuList where skuList.id IN "
						+ "(select skuUser.skuListId from SkuUserMapping as skuUser where skuUser.userId="
						+ userId
						+ ")) "
						+ " AND data."
						+ businessColumn
						+ " IN (select distinct skuUser.business from SkuUserMapping as skuUser where skuUser.userId="
						+ userId + ") ";
			} else if (rolesList != null
					&& rolesList.size() > 0
					&& rolesList
							.contains(ApplicationConstants.CATEGORY_MANAGER)) {
				roleFilterCondition = " AND data."
						+ categoryColumn
						+ " IN(select catList.categoryName from CategoryList as catList where catList.id IN "
						+ "(select catUser.categoryId from CategoryUserMapping as catUser where catUser.userId="
						+ userId
						+ ")) "
						+ " AND data."
						+ businessColumn
						+ " IN (select distinct catUser.business from CategoryUserMapping as catUser where catUser.userId = "
						+ userId + ")";
			}
			StringBuffer filterWhereClauseSB = new StringBuffer();
			String businessValue = null;
			if (pageTemplateFilters != null) {
				List filtersList = new ArrayList();
				int size = pageTemplateFilters.size();
				for (int i = 0; i < size; i++) {
					PageTemplateFilters pageTemplateFilter = (PageTemplateFilters) pageTemplateFilters
							.get(i);
					String filterFieldName = pageTemplateFilter
							.getFilterFieldName();
					String variableName = pageTemplateFilter
							.getFilterVariable();
					String selectedFilterValue = request
							.getParameter(filterFieldName);
					/*
					 * if(i==selectedFilterIndex &&
					 * !ApplicationUtil.isEmptyOrNull(selectedFilterValue) &&
					 * selectedFilterValue.equalsIgnoreCase("select")){
					 * selectedFilterIndex=selectedFilterIndex-1; }
					 */
					if (i <= selectedFilterIndex) {
						if (!ApplicationUtil.isEmptyOrNull(selectedFilterValue)
								&& !selectedFilterValue
										.equalsIgnoreCase("select")) {
							selectedFilterValuesMapObj.put(filterFieldName,
									selectedFilterValue);
							selecteDashboardFilterValuesMapObj.put(
									filterFieldName, selectedFilterValue);
							filterWhereClauseSB.append(" and ");
							filterWhereClauseSB.append(variableName + " =  ");
							filterWhereClauseSB.append("'"
									+ selectedFilterValue + "' ");
							if (!ApplicationUtil.isEmptyOrNull(filterFieldName)
									&& filterFieldName
											.equalsIgnoreCase("BUSINESS")) {
								businessValue = selectedFilterValue;
							}
						} else {
							selectedFilterValuesMapObj.put(filterFieldName,
									null);
							selecteDashboardFilterValuesMapObj.put(
									filterFieldName, null);
							filtersListMapObj.put(filterFieldName, null);
							dashboardFiltersListMapObj.put(filterFieldName,
									null);
						}
					} else {
						selectedFilterValuesMapObj.put(filterFieldName, null);
						selecteDashboardFilterValuesMapObj.put(filterFieldName,
								null);
						filtersListMapObj.put(filterFieldName, null);
						dashboardFiltersListMapObj.put(filterFieldName, null);
					}
				}
				for (int i = 0; i < size; i++) {
					PageTemplateFilters pageTemplateFilter = (PageTemplateFilters) pageTemplateFilters
							.get(i);
					String filterFieldName = pageTemplateFilter
							.getFilterFieldName();
					String variableName = pageTemplateFilter
							.getFilterVariable();
					String selectedFilterValue = request
							.getParameter(filterFieldName);
					String tableName = pageTemplateFilter.getTableName();
					if (!ApplicationUtil.isEmptyOrNull(selectedFilterValue)
							&& selectedFilterValue.equalsIgnoreCase("select")
							|| i > selectedFilterIndex) {
						List filtersListObj = planningService
								.getPageTemplateFilters(userId, variableName,
										tableName,
										filterWhereClauseSB.toString(),
										rolesList, businessValue,week,year);
						filtersListMapObj.put(filterFieldName, filtersListObj);
						dashboardFiltersListMapObj.put(filterFieldName,
								filtersListObj);
						selectedFilterValuesMapObj.put(filterFieldName, null);
						selecteDashboardFilterValuesMapObj.put(filterFieldName,
								null);
					}
				}
			}
			int planningCycleId = planningLogBean.getPlanningCycleId();
			List planningLogList = planningService.getPlanningLogDetails(
					userId, planningCycleId, filterWhereClauseSB.toString(),
					roleFilterCondition, rolesList, businessValue, week, year,
					1, null);
			ApplicationUtil.setObjectInSession(
					ApplicationConstants.PLANNING_LOG_LIST, planningLogList,
					request);
			ApplicationUtil.setObjectInSession(
					ApplicationConstants.SELECTED_FILTER_VALUES_MAP,
					selectedFilterValuesMapObj, request);
			ApplicationUtil.setObjectInSession(
					ApplicationConstants.SELECTED_DASHBOARD_FILTER_VALUES_MAP,
					selecteDashboardFilterValuesMapObj, request);
			planningLogBean.setDetailValueObjs(planningLogList);

		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			ApplicationUtil.setObjectInSession(
					ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e,
					request);
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		logger.debug("Entering and leaving from getplanningLogDetails");
		return new ModelAndView("planningLog", "model", planningLogBean);
	}

	@RequestMapping(value = "/saveCommitStatus.htm", method = RequestMethod.POST)
	public ModelAndView getStatus(
			@ModelAttribute("planningLogBean") PlanningLogBean planningLogBean,
			BindingResult result, HttpServletRequest request) {
				try {
					System.out.println("inside status");
					Users users = (Users) ApplicationUtil.getObjectFromSession(
							ApplicationConstants.APPLICATION_ACT_AS_USER, request);
					int userId = users.getLogin_id();
					List rolesList = users.getRolesList();
					int planningCycleId = planningLogBean.getPlanningCycleId();
					System.out.println("cycle id is " + planningCycleId);
					String status = planningLogBean.getStatus();
					String editFlagArry[] = planningLogBean.getEditFlagArray();
					List planningLogObjList = (List) planningLogBean
							.getDetailValueObjs();
					List selectedSKUIdList = new ArrayList();
					if (planningLogObjList != null && editFlagArry != null) {
						int length = editFlagArry.length;
						for (int i = 0; i < length; i++) {
							PlanningLog planningLog = (PlanningLog) planningLogObjList
									.get(Integer.parseInt(editFlagArry[i]));
							selectedSKUIdList.add(planningLog.getSku());
						}
					}
				
				Map pageTemplateCacheObj = (Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.PAGE_TEMPLATES_CACHE_KEY);	
				PageTemplate pageTemplate = (PageTemplate)pageTemplateCacheObj.get("PC_DASHBOARD");
				List pageTemplateFilters = pageTemplate.getPageTemplateFilters();
				List filters = pageTemplate.getFiltersList();
				String roleFilterCondition=null;
				String skuId=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_NUMBER);
				String businessColumn = PropertiesUtil.getProperty(ApplicationConstants.BUSINESS_TYPE);
				Map selectedFiltersList=(Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.SELECTED_FILTER_VALUES_MAP, request);
				String categoryColumn = PropertiesUtil.getProperty(ApplicationConstants.CATEGORY);
				PlanningCycle activePlanningCycle = (PlanningCycle)ApplicationUtil.getObjectFromSession(ApplicationConstants.ACTIVE_PLANNINGCYCLE_OBJ, request);
				String startWeek=activePlanningCycle.getStartWeek();
				Integer week=Integer.parseInt(startWeek);
				String startYear=activePlanningCycle.getStartYear();
				Integer year =Integer.parseInt(startYear);
				if(rolesList != null && rolesList.size()>0 && rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){
					roleFilterCondition = " AND data."+skuId+" IN(select skuList.productId from SkuList as skuList where skuList.id IN "
						+ "(select skuUser.skuListId from SkuUserMapping as skuUser where skuUser.userId="+userId+")) "
								+ " AND data."+businessColumn+" IN (select distinct skuUser.business from SkuUserMapping as skuUser where skuUser.userId="+userId+") ";
				}else if (rolesList != null && rolesList.size()>0 && rolesList.contains(ApplicationConstants.CATEGORY_MANAGER)){
					roleFilterCondition = " AND data."+categoryColumn+" IN(select catList.categoryName from CategoryList as catList where  catList.id IN "
						+ "(select catUser.categoryId from CategoryUserMapping as catUser where catUser.userId="+userId+"))"
								+ " AND data."+businessColumn+" IN (select distinct catUser.business from CategoryUserMapping as catUser where catUser.userId = "+userId+") AND data."+skuId+" IN(select skuList.productId from SkuList as skuList where (skuList.eolWeek is null or ("+week+"<=skuList.eolWeek and "+year+"<=skuList.eolYear)))";
				}
			String business = null;
			StringBuffer stringBuffer = new StringBuffer();
			if (pageTemplateFilters != null) {
				if (pageTemplateFilters != null) {
					int size = pageTemplateFilters.size();
					for (int i = 0; i < size; i++) {
						PageTemplateFilters pageTemplateFilter = (PageTemplateFilters) pageTemplateFilters
								.get(i);
						String filterFieldName = pageTemplateFilter
								.getFilterFieldName();
						String variableName = pageTemplateFilter
								.getFilterVariable();
						String filterValue = (String) selectedFiltersList
								.get(filterFieldName);
						if (!ApplicationUtil.isEmptyOrNull(filterFieldName)
								&& filterFieldName.equalsIgnoreCase("BUSINESS")) {
							business = filterValue;
						}
						if (!ApplicationUtil.isEmptyOrNull(filterValue)
								&& !filterValue.equalsIgnoreCase("select")) {
							stringBuffer.append(" and " + variableName + "='"
									+ filterValue + "'");
						}
					}
				}
			}
				planningService.saveCommitStatus(userId,selectedSKUIdList,planningCycleId,rolesList,null,business);
				List planningLogList=planningService.getPlanningLogDetails(userId,planningCycleId,stringBuffer.toString(),roleFilterCondition,rolesList,business,week,year,1,null);
				planningLogBean.setDetailValueObjs(planningLogList);
				planningLogBean.setEditFlagArray(null);
				ApplicationException ae = ApplicationException
						.createApplicationException("PlanningLogController",
								"getStatus",
								ApplicationErrorCodes.APP_EC_16,
								ApplicationConstants.INFORMATION, null);
			ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, ae, request);	
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				ApplicationUtil.setObjectInSession(
						ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e,
						request);
				e.printStackTrace();
			}
		logger.debug("Entering and leaving from getStatus");
		return new ModelAndView("planningLog", "model", planningLogBean);
	}

	@RequestMapping(value = "/initiateStatus.htm")
	public ModelAndView getInitiateStatus(
			@ModelAttribute("planningLogBean") PlanningLogBean planningLogBean,
			BindingResult result, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("redirect:homePage.htm");
		int planningCycleId = planningLogBean.getPlanningCycleId();

		try {
			planningService.updatePlanningCycleStatus(planningCycleId,
					ApplicationConstants.INITIATE_STATUS);
		} catch (ApplicationException e) {
			ApplicationUtil.setObjectInSession(
					ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e,
					request);
			modelAndView = new ModelAndView("planningLog", "model",
					planningLogBean);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.debug("Entering and leaving from getInitiateStatus");
		return modelAndView;
	}

	@RequestMapping(value = "/flushTables.htm")
	public ModelAndView getFlushTables(
			@ModelAttribute("planningLogBean") PlanningLogBean planningLogBean,
			BindingResult result, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("redirect:homePage.htm");
		int planningCycleId = planningLogBean.getPlanningCycleId();

		try {
			planningService.flushTables(planningCycleId);
			planningService.updatePlanningCycleStatus(planningCycleId,
					ApplicationConstants.UPLOAD_STATUS);
			ApplicationException ae = ApplicationException
					.createApplicationException("PlanningLogController",
							"getFlushTables", ApplicationErrorCodes.APP_EC_26,
							ApplicationConstants.INFORMATION, null);
			ApplicationUtil.setObjectInSession(
					ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, ae,
					request);
		} catch (ApplicationException e) {
			modelAndView = new ModelAndView("planningLog", "model",
					planningLogBean);
			ApplicationUtil.setObjectInSession(
					ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e,
					request);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.debug("Entering and leaving from getFlushTables");
		return modelAndView;
	}

	@RequestMapping(value = "/forecastValuesFromPLanningLog.htm")
	public ModelAndView getforecastValuesFromPLanningLog(
			@ModelAttribute("newBean") PlanningLogBean planningLogBean,
			BindingResult result, HttpServletRequest request, ModelMap map) {
		try {
			String isFromDashboard = request.getParameter("isFromDashboard");
			List planningLogList = (List) ApplicationUtil.getObjectFromSession(
					ApplicationConstants.PLANNING_LOG_LIST, request);
			/*
			 * if(!ApplicationUtil.isEmptyOrNull(isFromDashboard) &&
			 * isFromDashboard.equalsIgnoreCase("true") && planningLogList!=null
			 * && planningLogList.size()>0){
			 * planningLogBean.setDetailValueObjs(planningLogList); return new
			 * ModelAndView("planningLog", "model",planningLogBean); }
			 */

			Users users = (Users) ApplicationUtil.getObjectFromSession(
					ApplicationConstants.APPLICATION_ACT_AS_USER, request);
			List rolesList = users.getRolesList();
			Integer userId = users.getLogin_id();
			Map filtersListMapObj = new HashMap();
			Map dashboardFiltersListMapObj = new HashMap();
			Map pageTemplateCacheObj = (Map) LoadApplicationCacheService.applicationCacheObject
					.get(ApplicationConstants.PAGE_TEMPLATES_CACHE_KEY);
			PageTemplate pageTemplate = (PageTemplate) pageTemplateCacheObj
					.get("PC_DASHBOARD");
			List pageTemplateFilters = pageTemplate.getPageTemplateFilters();
			Map selectedFilterValuesMapObj = new HashMap();
			Map selecteDashboardFilterValuesMapObj = new HashMap();
			String skuId = PropertiesUtil
					.getProperty(ApplicationConstants.PRODUCT_NUMBER);
			String categoryColumn = PropertiesUtil
					.getProperty(ApplicationConstants.CATEGORY);
			String businessColumn = PropertiesUtil
					.getProperty(ApplicationConstants.BUSINESS_TYPE);
			String roleFilterCondition = "";
			PlanningCycle activePlanningCycle = (PlanningCycle) ApplicationUtil
					.getObjectFromSession(
							ApplicationConstants.ACTIVE_PLANNINGCYCLE_OBJ,
							request);
			String startWeek = activePlanningCycle.getStartWeek();
			Integer week = Integer.parseInt(startWeek);
			String startYear = activePlanningCycle.getStartYear();
			int planningCycleId = activePlanningCycle.getId();
			Integer year = Integer.parseInt(startYear);
			int forecastMetric = planningLogBean.getForecastMetric();
			if (rolesList != null && rolesList.size() > 0
					&& rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)) {
				roleFilterCondition = "select distinct "
						+ skuId
						+ " from data where "
						+ skuId
						+ " in (select distinct sl.productId from Sku_List sl, Sku_User_Mapping sm, Users u where u.login_id = "
						+ userId
						+ " and sl.id = sm.skuListId and sm.userId = u.login_id and (sl.eolWeek is null or ("
						+ week + "<=sl.eolWeek and " + year + "<=sl.eolYear)))";
			} else if (rolesList != null
					&& rolesList.size() > 0
					&& rolesList
							.contains(ApplicationConstants.CATEGORY_MANAGER)) {
				roleFilterCondition = "select distinct data."
						+ skuId
						+ " from Data data,sku_list as skulist  where data."
						+ skuId
						+ "=skuList.productid and (skuList.eolWeek is null or (40 <= skuList.eolweek and 2015 <= skuList.eolYear)) and  data."
						+ categoryColumn
						+ " IN (select catList.categoryName from Category_List as catList where catList.id IN "
						+ "(select catUser.categoryId from Category_User_Mapping as catUser where catUser.userId="
						+ userId
						+ ")) AND data."
						+ businessColumn
						+ " IN (select distinct catUser.business from Category_User_Mapping as catUser where catUser.userId = "
						+ userId + ")";
			}

			StringBuffer filterWhereClauseFromPlanningLogSB = new StringBuffer();

			Map<String, String> selectedFiltersMap = (Map) ApplicationUtil
					.getObjectFromSession(
							ApplicationConstants.SELECTED_FILTER_VALUES_MAP,
							request);
			System.out.println("Map Object");
			Map pageTemplateFiltersMap = new HashMap<String, String>();
			for (int i = 0; i < pageTemplateFilters.size(); i++) {
				PageTemplateFilters pageTemplateFilter = (PageTemplateFilters) pageTemplateFilters
						.get(i);
				pageTemplateFiltersMap.put(
						pageTemplateFilter.getFilterFieldName(),
						pageTemplateFilter.getFilterVariable());

			}
			for (Map.Entry<String, String> entry : selectedFiltersMap
					.entrySet()) {
				if (!ApplicationUtil.isEmptyOrNull(entry.getValue())) {
					filterWhereClauseFromPlanningLogSB.append(" and ");
					filterWhereClauseFromPlanningLogSB
							.append(pageTemplateFiltersMap.get(entry.getKey())
									+ " =  ");
					filterWhereClauseFromPlanningLogSB.append("'"
							+ entry.getValue() + "' ");
				}

			}

			// filter change

			if (ApplicationUtil.isEmptyOrNull(forecastMetric)) {
				forecastMetric = 0;
			}
			if (!ApplicationUtil.isEmptyOrNull(ApplicationUtil
					.getObjectFromSession(
							ApplicationConstants.PLANNING_LOG_SESSION_OBJECT,
							request))) {
				ApplicationUtil.removeObjectFromSession(
						ApplicationConstants.PLANNING_LOG_SESSION_OBJECT,
						request);
			}
			System.out.println(filterWhereClauseFromPlanningLogSB.toString());
			planningLogList = planningService.getForecastValuesFromPlanningLog(
					userId, planningCycleId,
					filterWhereClauseFromPlanningLogSB.toString(),
					roleFilterCondition, rolesList, null, forecastMetric, week,
					year);
			ApplicationUtil.setObjectInSession(
					ApplicationConstants.PLANNING_LOG_LIST, planningLogList,
					request);
			ApplicationUtil.setObjectInSession(
					ApplicationConstants.ACTIVE_PLANNING_TAB, "planningLog",
					request);
			planningLogBean.setSkuForecastList(planningLogList);
			planningLogBean.setForecastPeriodList(planningService
					.getForecastPeriod(planningCycleId));
			// planningLogBean.setDetailValueObjs(planningLogList);

		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			ApplicationUtil.setObjectInSession(
					ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e,
					request);
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		logger.debug("Entering and leaving from getforecastValuesFromPLanningLog");
		return new ModelAndView("skuForecastValues", "model", planningLogBean);
	}

	@RequestMapping(value = "/forecastValuesFromPlanningLogOnChangeFilter.htm", method = RequestMethod.POST)
	public ModelAndView forecastValuesFromPlanningLogOnChangeFilter(
			@ModelAttribute("planningLogBean") PlanningLogBean planningLogBean,
			BindingResult result, HttpServletRequest request) {
		try {
			String selectedFilterIndexStr = planningLogBean
					.getSelectedFilterIndex();
			String selectedFilterIndexArry[] = selectedFilterIndexStr
					.split(",");
			int selectedFilterIndex = Integer
					.parseInt(selectedFilterIndexArry[0]);
			Map filtersListMapObj = (Map) ApplicationUtil.getObjectFromSession(
					ApplicationConstants.FILTER_LIST_VALUES_MAP, request);
			Map dashboardFiltersListMapObj = (Map) ApplicationUtil
					.getObjectFromSession(
							ApplicationConstants.DASHBOARD_FILTER_LIST_VALUES_MAP,
							request);
			if (filtersListMapObj == null) {
				filtersListMapObj = new HashMap();
				dashboardFiltersListMapObj = new HashMap();
			}
			Map selectedFilterValuesMapObj = (Map) ApplicationUtil
					.getObjectFromSession(
							ApplicationConstants.SELECTED_FILTER_VALUES_MAP,
							request);
			Map selecteDashboardFilterValuesMapObj = (Map) ApplicationUtil
					.getObjectFromSession(
							ApplicationConstants.SELECTED_DASHBOARD_FILTER_VALUES_MAP,
							request);
			if (selectedFilterValuesMapObj == null) {
				selectedFilterValuesMapObj = new HashMap();
				selecteDashboardFilterValuesMapObj = new HashMap();
			}
			Users users = (Users) ApplicationUtil.getObjectFromSession(
					ApplicationConstants.APPLICATION_ACT_AS_USER, request);
			List rolesList = users.getRolesList();
			Integer userId = users.getLogin_id();
			Map pageTemplateCacheObj = (Map) LoadApplicationCacheService.applicationCacheObject
					.get(ApplicationConstants.PAGE_TEMPLATES_CACHE_KEY);
			PageTemplate pageTemplate = (PageTemplate) pageTemplateCacheObj
					.get("PC_DASHBOARD");
			List pageTemplateFilters = pageTemplate.getPageTemplateFilters();
			List filters = pageTemplate.getFiltersList();
			String skuId = PropertiesUtil
					.getProperty(ApplicationConstants.PRODUCT_NUMBER);
			String product = PropertiesUtil
					.getProperty(ApplicationConstants.PRODUCT_ID);
			String id = PropertiesUtil.getProperty(ApplicationConstants.ID);
			String skuListId = PropertiesUtil
					.getProperty(ApplicationConstants.SKU_LIST_ID);
			String categoryColumn = PropertiesUtil
					.getProperty(ApplicationConstants.CATEGORY);
			String businessColumn = PropertiesUtil
					.getProperty(ApplicationConstants.BUSINESS_TYPE);
			String roleFilterCondition = "";
			PlanningCycle activePlanningCycle = (PlanningCycle) ApplicationUtil
					.getObjectFromSession(
							ApplicationConstants.ACTIVE_PLANNINGCYCLE_OBJ,
							request);
			String startWeek = activePlanningCycle.getStartWeek();
			Integer week = Integer.parseInt(startWeek);
			String startYear = activePlanningCycle.getStartYear();
			Integer year = Integer.parseInt(startYear);
			int forecastMetric = planningLogBean.getForecastMetric();

			if (rolesList != null && rolesList.size() > 0
					&& rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)) {
				roleFilterCondition = "select distinct "
						+ skuId
						+ " from data where "
						+ skuId
						+ " in (select distinct sl.productId from Sku_List sl, Sku_User_Mapping sm, Users u where u.login_id = "
						+ userId
						+ " and sl.id = sm.skuListId and sm.userId = u.login_id and (sl.eolWeek is null or ("
						+ week + "<=sl.eolWeek and " + year + "<=sl.eolYear)))";
			} else if (rolesList != null
					&& rolesList.size() > 0
					&& rolesList
							.contains(ApplicationConstants.CATEGORY_MANAGER)) {
				roleFilterCondition = "select distinct data."
						+ skuId
						+ " from Data data,sku_list as skulist  where data."
						+ skuId
						+ "=skuList.productid and (skuList.eolWeek is null or (40 <= skuList.eolweek and 2015 <= skuList.eolYear)) and  data."
						+ categoryColumn
						+ " IN (select catList.categoryName from Category_List as catList where catList.id IN "
						+ "(select catUser.categoryId from Category_User_Mapping as catUser where catUser.userId="
						+ userId
						+ ")) AND data."
						+ businessColumn
						+ " IN (select distinct catUser.business from Category_User_Mapping as catUser where catUser.userId = "
						+ userId + ")";
			}
			StringBuffer filterWhereClauseSB = new StringBuffer();
			String businessValue = null;
			if (pageTemplateFilters != null) {
				List filtersList = new ArrayList();
				int size = pageTemplateFilters.size();
				for (int i = 0; i < size; i++) {
					PageTemplateFilters pageTemplateFilter = (PageTemplateFilters) pageTemplateFilters
							.get(i);
					String filterFieldName = pageTemplateFilter
							.getFilterFieldName();
					String variableName = pageTemplateFilter
							.getFilterVariable();
					String selectedFilterValue = request
							.getParameter(filterFieldName);
					/*
					 * if(i==selectedFilterIndex &&
					 * !ApplicationUtil.isEmptyOrNull(selectedFilterValue) &&
					 * selectedFilterValue.equalsIgnoreCase("select")){
					 * selectedFilterIndex=selectedFilterIndex-1; }
					 */
					if (i <= selectedFilterIndex) {
						if (!ApplicationUtil.isEmptyOrNull(selectedFilterValue)
								&& !selectedFilterValue
										.equalsIgnoreCase("select")) {
							selectedFilterValuesMapObj.put(filterFieldName,
									selectedFilterValue);
							selecteDashboardFilterValuesMapObj.put(
									filterFieldName, selectedFilterValue);
							filterWhereClauseSB.append(" and ");
							filterWhereClauseSB.append(variableName + " =  ");
							filterWhereClauseSB.append("'"
									+ selectedFilterValue + "' ");
							if (!ApplicationUtil.isEmptyOrNull(filterFieldName)
									&& filterFieldName
											.equalsIgnoreCase("BUSINESS")) {
								businessValue = selectedFilterValue;
							}
						} else {
							selectedFilterValuesMapObj.put(filterFieldName,
									null);
							selecteDashboardFilterValuesMapObj.put(
									filterFieldName, null);
							filtersListMapObj.put(filterFieldName, null);
							dashboardFiltersListMapObj.put(filterFieldName,
									null);
						}
					} else {
						selectedFilterValuesMapObj.put(filterFieldName, null);
						selecteDashboardFilterValuesMapObj.put(filterFieldName,
								null);
						filtersListMapObj.put(filterFieldName, null);
						dashboardFiltersListMapObj.put(filterFieldName, null);
					}
				}
				for (int i = 0; i < size; i++) {
					PageTemplateFilters pageTemplateFilter = (PageTemplateFilters) pageTemplateFilters
							.get(i);
					String filterFieldName = pageTemplateFilter
							.getFilterFieldName();
					String variableName = pageTemplateFilter
							.getFilterVariable();
					String selectedFilterValue = request
							.getParameter(filterFieldName);
					String tableName = pageTemplateFilter.getTableName();
					if (!ApplicationUtil.isEmptyOrNull(selectedFilterValue)
							&& selectedFilterValue.equalsIgnoreCase("select")
							|| i > selectedFilterIndex) {
						List filtersListObj = planningService
								.getPageTemplateFilters(userId, variableName,
										tableName,
										filterWhereClauseSB.toString(),
										rolesList, businessValue,week,year);
						filtersListMapObj.put(filterFieldName, filtersListObj);
						dashboardFiltersListMapObj.put(filterFieldName,
								filtersListObj);
						selectedFilterValuesMapObj.put(filterFieldName, null);
						selecteDashboardFilterValuesMapObj.put(filterFieldName,
								null);
					}
				}
			}
			int planningCycleId = activePlanningCycle.getId();
			List planningLogList = planningService
					.getForecastValuesFromPlanningLog(userId, planningCycleId,
							filterWhereClauseSB.toString(),
							roleFilterCondition, rolesList, businessValue,
							forecastMetric, week, year);
			ApplicationUtil.setObjectInSession(
					ApplicationConstants.PLANNING_LOG_LIST, planningLogList,
					request);
			ApplicationUtil.setObjectInSession(
					ApplicationConstants.SELECTED_FILTER_VALUES_MAP,
					selectedFilterValuesMapObj, request);
			ApplicationUtil.setObjectInSession(
					ApplicationConstants.SELECTED_DASHBOARD_FILTER_VALUES_MAP,
					selecteDashboardFilterValuesMapObj, request);
			planningLogBean.setDetailValueObjs(planningLogList);
			planningLogBean.setSkuForecastList(planningLogList);
			planningLogBean.setForecastPeriodList(planningService
					.getForecastPeriod(planningCycleId));
			if (ApplicationUtil.isEmptyOrNull(ApplicationUtil
					.getObjectFromSession(
							ApplicationConstants.PLANNING_LOG_SESSION_OBJECT,
							request))) {
				String SkuListFromSession = planningLogBean.getSkuList()
						.replaceAll(",", " ").replaceAll("\\s+", " ").trim()
						.replaceAll(" ", ",");
				ApplicationUtil.setObjectInSession(
						ApplicationConstants.PLANNING_LOG_SESSION_OBJECT,
						SkuListFromSession, request);
			} else {
				if (!ApplicationUtil
						.isEmptyOrNull(planningLogBean.getSkuList())) {
					/*
					 * ApplicationUtil.setObjectInSession(ApplicationConstants.
					 * PLANNING_LOG_SESSION_OBJECT
					 * ,ApplicationUtil.getObjectFromSession
					 * (ApplicationConstants.PLANNING_LOG_SESSION_OBJECT,
					 * request)+","+ planningLogBean.getSkuList(), request);
					 */
					String SkuListFromSession = planningLogBean.getSkuList()
							.replaceAll(",", " ").replaceAll("\\s+", " ")
							.trim().replaceAll(" ", ",");
					ApplicationUtil.setObjectInSession(
							ApplicationConstants.PLANNING_LOG_SESSION_OBJECT,
							SkuListFromSession, request);
				}

			}
			System.out.println("SKU LIst\t"
					+ ApplicationUtil.getObjectFromSession(
							ApplicationConstants.PLANNING_LOG_SESSION_OBJECT,
							request));
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			ApplicationUtil.setObjectInSession(
					ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e,
					request);
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		logger.debug("Entering and leaving from getplanningLogDetails");
		return new ModelAndView("skuForecastValues", "model", planningLogBean);
	}

	@RequestMapping(value = "/planningLogSelectedSku.htm")
	public ModelAndView getPlanningLogSelectedSku(
			@ModelAttribute("planningLogBean") PlanningLogBean planningLogBean,
			BindingResult result, HttpServletRequest request, ModelMap map) {
		List planningLogList = null;
		try {
			Integer planningCycleId = planningLogBean.getPlanningCycleId();
			Users users = (Users) ApplicationUtil.getObjectFromSession(
					ApplicationConstants.APPLICATION_ACT_AS_USER, request);
			List rolesList = users.getRolesList();
			Integer userId = users.getLogin_id();
			String skuId = PropertiesUtil
					.getProperty(ApplicationConstants.PRODUCT_NUMBER);
			String product = PropertiesUtil
					.getProperty(ApplicationConstants.PRODUCT_ID);
			String id = PropertiesUtil.getProperty(ApplicationConstants.ID);
			String skuListId = PropertiesUtil
					.getProperty(ApplicationConstants.SKU_LIST_ID);
			String categoryColumn = PropertiesUtil
					.getProperty(ApplicationConstants.CATEGORY);
			String businessColumn = PropertiesUtil
					.getProperty(ApplicationConstants.BUSINESS_TYPE);
			String roleFilterCondition = "";
			String skuList = "";

			if (ApplicationUtil.isEmptyOrNull(ApplicationUtil
					.getObjectFromSession(
							ApplicationConstants.PLANNING_LOG_SESSION_OBJECT,
							request))) {
				String SkuListFromSession = planningLogBean
						.getSelectedSkuList().replaceAll(",", " ")
						.replaceAll("\\s+", " ").trim().replaceAll(" ", ",");
				ApplicationUtil.setObjectInSession(
						ApplicationConstants.PLANNING_LOG_SESSION_OBJECT,
						SkuListFromSession, request);
				skuList = (String) ApplicationUtil.getObjectFromSession(
						ApplicationConstants.PLANNING_LOG_SESSION_OBJECT,
						request);
			} else {
				if (!ApplicationUtil.isEmptyOrNull(planningLogBean
						.getSelectedSkuList())) {
					/*
					 * ApplicationUtil.setObjectInSession(ApplicationConstants.
					 * PLANNING_LOG_SESSION_OBJECT
					 * ,ApplicationUtil.getObjectFromSession
					 * (ApplicationConstants.PLANNING_LOG_SESSION_OBJECT,
					 * request)+","+ planningLogBean.getSkuList(), request);
					 */
					String SkuListFromSession = planningLogBean
							.getSelectedSkuList().replaceAll(",", " ")
							.replaceAll("\\s+", " ").trim()
							.replaceAll(" ", ",");
					ApplicationUtil.setObjectInSession(
							ApplicationConstants.PLANNING_LOG_SESSION_OBJECT,
							SkuListFromSession, request);
					skuList = (String) ApplicationUtil.getObjectFromSession(
							ApplicationConstants.PLANNING_LOG_SESSION_OBJECT,
							request);
				}
			}

			System.out.println("SKU List selected\t" + skuList);
			if (rolesList != null && rolesList.size() > 0
					&& rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)) {
				roleFilterCondition = " AND data."
						+ skuId
						+ " IN(select skuList.productId from SkuList as skuList where skuList.id IN "
						+ "(select skuUser.skuListId from SkuUserMapping as skuUser where skuUser.userId="
						+ userId
						+ ")) "
						+ " AND data."
						+ businessColumn
						+ " IN (select distinct skuUser.business from SkuUserMapping as skuUser where skuUser.userId="
						+ userId + ") ";
			} else if (rolesList != null
					&& rolesList.size() > 0
					&& rolesList
							.contains(ApplicationConstants.CATEGORY_MANAGER)) {
				roleFilterCondition = " AND data."
						+ categoryColumn
						+ " IN(select catList.categoryName from CategoryList as catList where catList.id IN "
						+ "(select catUser.categoryId from CategoryUserMapping as catUser where catUser.userId="
						+ userId
						+ ")) "
						+ " AND data."
						+ businessColumn
						+ " IN (select distinct catUser.business from CategoryUserMapping as catUser where catUser.userId = "
						+ userId + ")";
			}
			PlanningCycle activePlanningCycle = (PlanningCycle) ApplicationUtil
					.getObjectFromSession(
							ApplicationConstants.ACTIVE_PLANNINGCYCLE_OBJ,
							request);
			String startWeek = activePlanningCycle.getStartWeek();
			Integer week = Integer.parseInt(startWeek);
			String startYear = activePlanningCycle.getStartYear();
			Integer year = Integer.parseInt(startYear);
			planningLogList = planningService.getPlanningLogDetails(userId,
					planningCycleId, null, roleFilterCondition, rolesList,
					null, week, year, 2, skuList);
			List<String> planningLogListFromSession = planningService
					.getPlanningLogUserSession(userId);
			if (ApplicationUtil.isEmptyOrNull(planningLogListFromSession)
					|| planningLogListFromSession.size() == 0) {
				planningService.savePlanningLogUserSession(userId, skuList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationUtil.setObjectInSession(
					ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e,
					request);
		}
		planningLogBean.setDetailValueObjs(planningLogList);
		logger.debug("Entering and leaving from getPlanningLog");
		return new ModelAndView("filteredPlanningLog", "model", planningLogBean);
	} /* closing the getUploadDataDetails method */

	@RequestMapping(value = "/clearPlanningLogUserSession.htm")
	public ModelAndView clearUserSession(
			@ModelAttribute("planningLogBean") PlanningLogBean planningLogBean,
			BindingResult result, HttpServletRequest request) {
		int planningCycleId = planningLogBean.getPlanningCycleId();

		try {

			Users users = (Users) ApplicationUtil.getObjectFromSession(
					ApplicationConstants.APPLICATION_ACT_AS_USER, request);
			Integer userId = users.getLogin_id();
			if (!ApplicationUtil.isEmptyOrNull(ApplicationUtil
					.getObjectFromSession(
							ApplicationConstants.PLANNING_LOG_SESSION_OBJECT,
							request))) {
				ApplicationUtil.removeObjectFromSession(
						ApplicationConstants.PLANNING_LOG_SESSION_OBJECT,
						request);
			}
			planningService.clearPlanningLogUserSession(userId);
		} catch (ApplicationException e) {
			ApplicationUtil.setObjectInSession(
					ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e,
					request);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.debug("Entering and leaving from clearUserSession");
		return new ModelAndView("redirect:planningLogDetails.htm");
	}
}
