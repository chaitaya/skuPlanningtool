package com.bridgei2i.controller;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bridgei2i.common.constants.ApplicationConstants;
import com.bridgei2i.common.constants.ApplicationErrorCodes;
import com.bridgei2i.common.controller.LoadApplicationCacheService;
import com.bridgei2i.common.exception.ApplicationException;
import com.bridgei2i.common.util.ApplicationUtil;
import com.bridgei2i.common.util.PropertiesUtil;
import com.bridgei2i.common.vo.PageTemplate;
import com.bridgei2i.common.vo.PageTemplateCharts;
import com.bridgei2i.common.vo.PageTemplateFilters;
import com.bridgei2i.common.vo.Users;
import com.bridgei2i.form.PlanningCycleBean;
import com.bridgei2i.service.PlanningService;
import com.bridgei2i.vo.ForecastingASP;
import com.bridgei2i.vo.ForecastingUnits;
import com.bridgei2i.vo.PlanningCycle;

@Controller
@SessionAttributes("planningCycleBean")
@Scope("session")
public class DashboardController {
	@Autowired(required=true)
    private PlanningService planningService;
	
	@RequestMapping(value = "/refreshDashboardDetails.htm")
	public ModelAndView refreshDashboardDetails(
			@ModelAttribute("planningCycleBean") PlanningCycleBean planningCycleBean,
			BindingResult result, HttpServletRequest request,ModelMap map) throws ApplicationException {
		return getDashboardDetails(planningCycleBean,result,request,map);
	}
	
	@RequestMapping(value = "/dashboard.htm")
	public ModelAndView getDashboardDetails(
			@ModelAttribute("newBean") PlanningCycleBean planningCycleBean,
			BindingResult result, HttpServletRequest request,ModelMap map) throws ApplicationException {
            
		int planningCycleId = planningCycleBean.getPlanningCycleId();
		int isFromSaveOverride = planningCycleBean.getIsFromSaveOverride();
		planningCycleBean.setIsFromSaveOverride(0);
		String type= planningCycleBean.getType();
		String selectedTypeValue = planningCycleBean.getSelectedTypeValue();
		String selectedTypeVariable = planningCycleBean.getSelectedTypeVariable();
		Map pageTemplateCacheObj = (Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.PAGE_TEMPLATES_CACHE_KEY);	
		PageTemplate pageTemplate = (PageTemplate)pageTemplateCacheObj.get("PC_DASHBOARD");
		List pageTemplateCharts = pageTemplate.getPageTemplateCharts();
		List pageTemplateFilters = pageTemplate.getPageTemplateFilters();
		Users users = (Users) ApplicationUtil.getObjectFromSession(ApplicationConstants.APPLICATION_ACT_AS_USER, request);
		List rolesList = users.getRolesList();
		Integer userId=users.getLogin_id();
		String selectedFilterIndexStr = planningCycleBean.getSelectedFilterIndex();
		String skuId=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_NUMBER);
		String categoryColumn = PropertiesUtil.getProperty(ApplicationConstants.CATEGORY);
		String modelColumn = PropertiesUtil.getProperty(ApplicationConstants.MODEL);
		String isFromCategoryLevelSummary = planningCycleBean.getIsFromCategoryLevelSummary();
		String isFromCategoryLevelSummaryFreeze = planningCycleBean.getIsFromCategoryLevelSummaryFreeze();
		String viewName="dashboard";
		if(!ApplicationUtil.isEmptyOrNull(isFromCategoryLevelSummary) && isFromCategoryLevelSummary.equalsIgnoreCase("true")){
			viewName = "categoryLevelDashboard";
			ApplicationUtil.setObjectInSession("isFrom", "categoryLevelDashboard", request);
		}
		if(!ApplicationUtil.isEmptyOrNull(isFromCategoryLevelSummaryFreeze) && isFromCategoryLevelSummaryFreeze.equalsIgnoreCase("true")){
			viewName = "categoryLevelFreezeDashboard";
			ApplicationUtil.setObjectInSession("isFrom", "categoryLevelDashboard", request);
		}
		// load defualt filters data
		if(ApplicationUtil.isEmptyOrNull(selectedFilterIndexStr) 
				&& ((!ApplicationUtil.isEmptyOrNull(isFromCategoryLevelSummary) 
						&& isFromCategoryLevelSummary.equalsIgnoreCase("true")) || (!ApplicationUtil.isEmptyOrNull(isFromCategoryLevelSummaryFreeze) 
						&& isFromCategoryLevelSummaryFreeze.equalsIgnoreCase("true")))){
			Map dashboardFiltersListMapObj= new HashMap();
			Map selecteDashboardFilterValuesMapObj = new HashMap();
			StringBuffer filterWhereClauseSB = new StringBuffer();
			if(pageTemplateFilters!=null){
				String selectedFilterFieldNameStr=null;
				String selectedFilterVariableStr=null;
				String selectedFilterValueStr=null;
				int selectedFilterIndex=0;
				int size = pageTemplateCharts.size();
				for(int i=0;i<size;i++){
					PageTemplateFilters pageTemplateFilter = (PageTemplateFilters) pageTemplateFilters.get(i);
					String filterFieldName = pageTemplateFilter.getFilterFieldName();
					String filterLabel=pageTemplateFilter.getFilterLabel();
					String filterVariable=null;
					String filterConditionValue=null;
					if(pageTemplateFilter.getDefaultFilterValues()==1){
						selectedFilterIndex=i;
						selectedFilterFieldNameStr = filterFieldName;
						filterVariable = pageTemplateFilter.getFilterVariable();
						selectedFilterVariableStr =filterVariable; 
						String tableName = pageTemplateFilter.getTableName();
						PlanningCycle activePlanningCycle = (PlanningCycle)ApplicationUtil.getObjectFromSession(ApplicationConstants.ACTIVE_PLANNINGCYCLE_OBJ, request);
						String startWeek=activePlanningCycle.getStartWeek();
						Integer week=Integer.parseInt(startWeek);
						String startYear=activePlanningCycle.getStartYear();
						Integer year =Integer.parseInt(startYear);
						List filtersListObj = planningService.getPageTemplateFilters(userId,filterVariable,tableName,filterWhereClauseSB.toString(),null,null,week,year);
						dashboardFiltersListMapObj.put(filterFieldName, filtersListObj);
						if(filtersListObj != null && filtersListObj.size()>0){
							filterConditionValue=filtersListObj.get(0).toString();
							selectedFilterValueStr =filterConditionValue; 
							selecteDashboardFilterValuesMapObj.put(filterFieldName, filterConditionValue);
							filterWhereClauseSB.append(" and ");
							filterWhereClauseSB.append(filterVariable+" =  ");
							filterWhereClauseSB.append("'"+filterConditionValue+"' ");
						}
					}
				}
				
				if(selectedFilterIndex+1<size){
					PageTemplateFilters selectedPageTemplateFilter = (PageTemplateFilters)pageTemplateFilters.get(selectedFilterIndex);
					String selectedFilterVariableName = selectedPageTemplateFilter.getFilterVariable();
					String selectedFilterFieldName = selectedPageTemplateFilter.getFilterFieldName();
					String selectedFilterValue = (String)selecteDashboardFilterValuesMapObj.get(selectedFilterFieldName);
					PageTemplateFilters nextPageTemplateFilter = (PageTemplateFilters)pageTemplateFilters.get(selectedFilterIndex+1);
					String filterFieldName = nextPageTemplateFilter.getFilterFieldName();
					if(nextPageTemplateFilter.getDefaultFilterValues()!=1){
						String filterVariable = nextPageTemplateFilter.getFilterVariable();
						String tableName = nextPageTemplateFilter.getTableName();
						PlanningCycle activePlanningCycle = (PlanningCycle)ApplicationUtil.getObjectFromSession(ApplicationConstants.ACTIVE_PLANNINGCYCLE_OBJ, request);
						String stWeek=activePlanningCycle.getStartWeek();
						Integer week=Integer.parseInt(stWeek);
						String stYear=activePlanningCycle.getStartYear();
						Integer year =Integer.parseInt(stYear);
						List filtersListObj = planningService.getPageTemplateFilters(userId,filterVariable,tableName,filterWhereClauseSB.toString(),null,null,week,year);
						dashboardFiltersListMapObj.put(filterFieldName, filtersListObj);
					}
				}
				type=selectedFilterFieldNameStr;
				selectedTypeVariable = selectedFilterVariableStr;
				selectedTypeValue = selectedFilterValueStr;
				planningCycleBean.setType(type);
				planningCycleBean.setSelectedTypeValue(selectedTypeValue);
				planningCycleBean.setSelectedTypeVariable(selectedTypeVariable);
				planningCycleBean.setProductDescription(null);
				planningCycleBean.setProductHierarchyII(null);
				planningCycleBean.setPlanogram(null);
			}
			ApplicationUtil.setObjectInSession(ApplicationConstants.SELECTED_DASHBOARD_FILTER_VALUES_MAP,selecteDashboardFilterValuesMapObj, request);
			ApplicationUtil.setObjectInSession(ApplicationConstants.DASHBOARD_FILTER_LIST_VALUES_MAP, dashboardFiltersListMapObj, request);
		}
		// end 
		
		//ON CHANGE DASHBOARD FILTERS
		
		if(!ApplicationUtil.isEmptyOrNull(selectedFilterIndexStr)){
			String[] selectedFilterIndexArray= selectedFilterIndexStr.split(",");
			int selectedFilterIndex =Integer.parseInt(selectedFilterIndexArray[0]);
			Map filtersListMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.DASHBOARD_FILTER_LIST_VALUES_MAP, request);
			if(filtersListMapObj == null){
				filtersListMapObj = new HashMap();
			}
			Map selectedFilterValuesMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.SELECTED_DASHBOARD_FILTER_VALUES_MAP, request);
			if(selectedFilterValuesMapObj==null){
				selectedFilterValuesMapObj = new HashMap();
			}
			List filters = pageTemplate.getFiltersList();
			String product=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_ID);
			String id = PropertiesUtil.getProperty(ApplicationConstants.ID);
			String skuListId=PropertiesUtil.getProperty(ApplicationConstants.SKU_LIST_ID);
			String businessColumn = PropertiesUtil.getProperty(ApplicationConstants.BUSINESS_TYPE);
			String roleFilterCondition="";
			if(rolesList != null && rolesList.size()>0 && rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){
				roleFilterCondition = " AND data."+skuId+" IN(select skuList.productId from SkuList as skuList where skuList.id IN "
					+ "(select skuUser.skuListId from SkuUserMapping as skuUser where skuUser.userId="+userId+")) "
							+ " AND data."+businessColumn+" IN (select distinct skuUser.business from SkuUserMapping as skuUser where skuUser.userId="+userId+") ";
			}else if (rolesList != null && rolesList.size()>0 && rolesList.contains(ApplicationConstants.CATEGORY_MANAGER)){
				roleFilterCondition = " AND data."+categoryColumn+" IN(select catList.categoryName from CategoryList as catList where catList.id IN "
					+ "(select catUser.categoryId from CategoryUserMapping as catUser where catUser.userId="+userId+")) "
							+ " AND data."+businessColumn+" IN (select distinct catUser.business from CategoryUserMapping as catUser where catUser.userId = "+userId+")";
			}
			StringBuffer filterWhereClauseSB = new StringBuffer();
			String businessValue="";
			if(pageTemplateFilters!=null){
				List filtersList = new ArrayList();
				int size = pageTemplateFilters.size();
				for(int i=0;i<size;i++){
					PageTemplateFilters pageTemplateFilter = (PageTemplateFilters) pageTemplateFilters.get(i);
					String filterFieldName = pageTemplateFilter.getFilterFieldName();
					String variableName = pageTemplateFilter.getFilterVariable();
					String selectedFilterValue  = request.getParameter(filterFieldName);
					if(i==selectedFilterIndex && !ApplicationUtil.isEmptyOrNull(selectedFilterValue) && selectedFilterValue.equalsIgnoreCase("select")){
						selectedFilterIndex=selectedFilterIndex-1;
						i=i-2;
						continue;
					}
					if(i==selectedFilterIndex){
						type= filterFieldName;
						selectedTypeVariable = variableName;
						selectedTypeValue = selectedFilterValue;
						planningCycleBean.setType(type);
						planningCycleBean.setSelectedTypeValue(selectedTypeValue);
						planningCycleBean.setSelectedTypeVariable(selectedTypeVariable);
						planningCycleBean.setProductDescription(null);
						planningCycleBean.setProductHierarchyII(null);
						planningCycleBean.setPlanogram(null);
						if(!ApplicationUtil.isEmptyOrNull(filterFieldName) && filterFieldName.equalsIgnoreCase("BUSINESS")){
							businessValue = selectedFilterValue;
						}
					}
					if(i<=selectedFilterIndex){
						if(!ApplicationUtil.isEmptyOrNull(selectedFilterValue) && !selectedFilterValue.equalsIgnoreCase("select")){
							selectedFilterValuesMapObj.put(filterFieldName, selectedFilterValue);
							filterWhereClauseSB.append(" and ");
							filterWhereClauseSB.append(variableName+" =  ");
							filterWhereClauseSB.append("'"+selectedFilterValue+"' ");
							if(!ApplicationUtil.isEmptyOrNull(filterFieldName) && filterFieldName.equalsIgnoreCase("BUSINESS")){
								businessValue = selectedFilterValue;
							}
						}
					}else{
						selectedFilterValuesMapObj.put(filterFieldName, null);
						filtersListMapObj.put(filterFieldName, null);
					}
				}
				
				if(selectedFilterIndex+1<size){
					PageTemplateFilters nextPageTemplateFilter = (PageTemplateFilters)pageTemplateFilters.get(selectedFilterIndex+1);
					String filterFieldName = nextPageTemplateFilter.getFilterFieldName();
					String filterVariable = nextPageTemplateFilter.getFilterVariable();
					String tableName = nextPageTemplateFilter.getTableName();
					PlanningCycle activePlanningCycle = (PlanningCycle)ApplicationUtil.getObjectFromSession(ApplicationConstants.ACTIVE_PLANNINGCYCLE_OBJ, request);
					String stWeek=activePlanningCycle.getStartWeek();
					Integer week=Integer.parseInt(stWeek);
					String stYear=activePlanningCycle.getStartYear();
					Integer year =Integer.parseInt(stYear);
					List filtersListObj = planningService.getPageTemplateFilters(userId,filterVariable,tableName,filterWhereClauseSB.toString(),rolesList,businessValue,week,year);
					filtersListMapObj.put(filterFieldName, filtersListObj);
					ApplicationUtil.setObjectInSession(ApplicationConstants.DASHBOARD_FILTER_LIST_VALUES_MAP, filtersListMapObj, request);
				}
			}
			ApplicationUtil.setObjectInSession(ApplicationConstants.SELECTED_DASHBOARD_FILTER_VALUES_MAP,selectedFilterValuesMapObj, request);
		}else{
			if(isFromSaveOverride==0){
				HashMap filtersListMapObj = (HashMap)ApplicationUtil.getObjectFromSession(ApplicationConstants.FILTER_LIST_VALUES_MAP, request);
				HashMap selectedFilterValuesMapObj = (HashMap)ApplicationUtil.getObjectFromSession(ApplicationConstants.SELECTED_FILTER_VALUES_MAP, request);
				if(filtersListMapObj!= null){
					ApplicationUtil.setObjectInSession(ApplicationConstants.DASHBOARD_FILTER_LIST_VALUES_MAP, (Map)filtersListMapObj.clone(), request);
				}
				if(selectedFilterValuesMapObj != null){
					ApplicationUtil.setObjectInSession(ApplicationConstants.SELECTED_DASHBOARD_FILTER_VALUES_MAP, (Map)selectedFilterValuesMapObj.clone(), request);
				}
			}
		}
		// END DASHBOARD FILTERS
		
		String business=null;
		String productId =selectedTypeValue;
		Map forecastingUnitsListMap =new HashMap();
		List jsonViewStrList = new ArrayList();
		List jsonTableList = new ArrayList();
		List exportExcelList=new ArrayList();
		Map selectedFilterMapObj = new HashMap();
		PlanningCycle activePlanningCycle = (PlanningCycle)ApplicationUtil.getObjectFromSession(ApplicationConstants.ACTIVE_PLANNINGCYCLE_OBJ, request);
		String startWeek=activePlanningCycle.getStartWeek();
		Integer week=Integer.parseInt(startWeek);
		String startYear=activePlanningCycle.getStartYear();
		Integer year =Integer.parseInt(startYear);
		String actualsWeekRange = PropertiesUtil.getProperty("actualsWeekRange");
		Integer range =Integer.valueOf(actualsWeekRange);
		StringBuffer stringBuffer=new StringBuffer();
		if(pageTemplateFilters!=null){
			Map selectedFiltersList=(Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.SELECTED_DASHBOARD_FILTER_VALUES_MAP, request);
			int size = pageTemplateFilters.size();
			for(int i=0;i<size;i++){
				PageTemplateFilters pageTemplateFilter = (PageTemplateFilters) pageTemplateFilters.get(i);
				String filterFieldName = pageTemplateFilter.getFilterFieldName();
				String variableName = pageTemplateFilter.getFilterVariable();
				String filterValue = (String)selectedFiltersList.get(filterFieldName);
				if(!ApplicationUtil.isEmptyOrNull(filterFieldName) && filterFieldName.equalsIgnoreCase("BUSINESS")){
					business = filterValue;
				}
				if(!ApplicationUtil.isEmptyOrNull(filterValue) && !filterValue.equalsIgnoreCase("select")){
					stringBuffer.append(" and "+variableName+"='"+filterValue+"'");
				}
			}
		}
		int commitStatusId=-1;
		int overrideFlag = 0;
		Map masterCommitStatusIdMap = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.MASTER_COMMIT_STATUS_ID_MAP);
		if(pageTemplateCharts!=null){
			if(!ApplicationUtil.isEmptyOrNull(type) &&  type.equalsIgnoreCase("PRODUCT")) {
				commitStatusId =planningService.getCommitStatusId(selectedTypeValue, planningCycleId,business);
				if(rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){
					String cacheCommitStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.AVAILABLE_FOR_PM);
					int cacheCommitStatusId = -1;
					if(!ApplicationUtil.isEmptyOrNull(cacheCommitStatusIdStr)){
						cacheCommitStatusId = Integer.parseInt(cacheCommitStatusIdStr);
					}
					if(commitStatusId==-1 || commitStatusId==cacheCommitStatusId){
						overrideFlag = 1;
					}
				}else if(rolesList.contains(ApplicationConstants.CATEGORY_MANAGER)){
					String cacheCommitStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_PM);
					int cacheCommitStatusId = -1;
					if(!ApplicationUtil.isEmptyOrNull(cacheCommitStatusIdStr)){
						cacheCommitStatusId = Integer.parseInt(cacheCommitStatusIdStr);
					}
					if(commitStatusId==cacheCommitStatusId){
						overrideFlag = 1;
					}
				}
				planningCycleBean.setEnableOverride(overrideFlag);
			}else if(!ApplicationUtil.isEmptyOrNull(type) &&  (type.equalsIgnoreCase("CATEGORY") || type.equalsIgnoreCase("MODEL"))) {
				String whereClause="AND data."+selectedTypeVariable+" = '"+selectedTypeValue+"'";
				String businessColumn = PropertiesUtil.getProperty(ApplicationConstants.BUSINESS_TYPE);
				if(type.equalsIgnoreCase("CATEGORY")){
					commitStatusId =planningService.getModelLevelStatus(userId,whereClause,rolesList,stringBuffer.toString(),planningCycleId,"CATEGORY",business,week,year);
					String cacheCommitStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.AVAILABLE_FOR_CM);
					int cacheCommitStatusId = -1;
					if(!ApplicationUtil.isEmptyOrNull(cacheCommitStatusIdStr)){
						cacheCommitStatusId = Integer.parseInt(cacheCommitStatusIdStr);
					}
					if(commitStatusId==cacheCommitStatusId){
						overrideFlag=1;
					}
				}else{
					commitStatusId =planningService.getModelLevelStatus(userId,whereClause,rolesList,stringBuffer.toString(),planningCycleId,"MODEL",business,week,year);
					String cacheCommitStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.AVAILABLE_FOR_PM);
					int cacheCommitStatusId = -1;
					if(!ApplicationUtil.isEmptyOrNull(cacheCommitStatusIdStr)){
						cacheCommitStatusId = Integer.parseInt(cacheCommitStatusIdStr);
					}
					if(commitStatusId==-1 || commitStatusId==cacheCommitStatusId){
						overrideFlag=1;
					}
				}
				planningCycleBean.setEnableOverride(overrideFlag);
			}
			
			int size = pageTemplateCharts.size();
			Map jsonViewMapObj =new HashMap();
			for(int i=0;i<size;i++){
				PageTemplateCharts pageTemplateChart = (PageTemplateCharts) pageTemplateCharts.get(i);
				String tableName = pageTemplateChart.getTableName();
				
				/*if(!ApplicationUtil.isEmptyOrNull(type) &&  type.equalsIgnoreCase("CATEGORY")){
					jsonViewMapObj = planningService.getChartJsonForCategory(tableName,productId,planningCycleId,selectedTypeValue,userId,business,stringBuffer.toString(),rolesList,week,year,range) ;
				} 
				
				if(!ApplicationUtil.isEmptyOrNull(type) &&  type.equalsIgnoreCase("MODEL")){
					jsonViewMapObj = planningService.getChartJsonForModel(tableName,productId,planningCycleId,selectedTypeValue,userId,business,stringBuffer.toString(),rolesList,week,year,range) ;
				} 

				if(!ApplicationUtil.isEmptyOrNull(type) &&  type.equalsIgnoreCase("PRODUCT")) {
					jsonViewMapObj = planningService.getChartJsonFromDatabase(tableName,selectedTypeValue,planningCycleId,business,rolesList,userId,week,year,range) ;
				*/ 

				if(!ApplicationUtil.isEmptyOrNull(type) &&  type.equalsIgnoreCase("PRODUCT")) {
					jsonViewMapObj = planningService.getChartJsonFromDatabase(tableName,selectedTypeValue,planningCycleId,business,rolesList,userId,week,year,range) ;
				}else{
					jsonViewMapObj = planningService.getChartJsonForAggregated(tableName,productId,planningCycleId,selectedTypeVariable,selectedTypeValue,userId,business,stringBuffer.toString(),rolesList,week,year,range,type) ;
					
					if(!ApplicationUtil.isEmptyOrNull(tableName) && tableName.equalsIgnoreCase("ForecastingUnits")){
						planningCycleBean.setOverrideButtonFlag((int) jsonViewMapObj.get("overrideButtonFlag"));
					}
				}
				jsonViewStrList.add(jsonViewMapObj.get("jsonStr"));
				System.out.println("json String  : "+jsonViewMapObj.get("jsonStr"));
				jsonTableList.add(jsonViewMapObj.get("jsonTable"));
				
				if(!ApplicationUtil.isEmptyOrNull(tableName) && tableName.equalsIgnoreCase("ForecastingUnits") && overrideFlag==1){
					List baseForecastUnisList = (List)jsonViewMapObj.get("baseForecastUnisList");
					ArrayList overridenForecastUnitsList = (ArrayList)jsonViewMapObj.get("overrideForecastUnitsList");
					forecastingUnitsListMap.put("baseForecastUnitsList", baseForecastUnisList);
					forecastingUnitsListMap.put("overrideForecastUnitsList", overridenForecastUnitsList);
					if(rolesList != null && rolesList.size()>0 && !rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){
						List currentForecastUnitsList = (List)jsonViewMapObj.get("currentForecastUnitsList");
						forecastingUnitsListMap.put("currentForecastUnitsList", currentForecastUnitsList);
					}
					List overrideForecastUnitsList = null;
					if(overridenForecastUnitsList != null){
						overrideForecastUnitsList= new ArrayList();
						int listSize = overridenForecastUnitsList.size();
						for(int j=0;j<listSize;j++){
							ForecastingUnits forecastingUnits = (ForecastingUnits)overridenForecastUnitsList.get(j);
							try {
								overrideForecastUnitsList.add(forecastingUnits.clone());
							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					ApplicationUtil.setObjectInSession(ApplicationConstants.FORECAST_UNITS_LIST_COPY, overrideForecastUnitsList, request);
					planningCycleBean.setOverrideForecastUnitsList(overridenForecastUnitsList);
					ApplicationUtil.setObjectInSession(ApplicationConstants.FORECAST_LIST_MAP, forecastingUnitsListMap, request);
								
					
				}else if(!ApplicationUtil.isEmptyOrNull(tableName) && tableName.equalsIgnoreCase("ForecastingASP") && overrideFlag==1){
					List baseForecastAspList = (List)jsonViewMapObj.get("baseForecastAspList");
					ArrayList overridenForecastAspList = (ArrayList)jsonViewMapObj.get("overrideForecastAspList");
					forecastingUnitsListMap.put("baseForecastAspList", baseForecastAspList);
					forecastingUnitsListMap.put("overrideForecastAspList", overridenForecastAspList);
					if(rolesList != null && rolesList.size()>0 && !rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){
						List currentForecastAspList = (List)jsonViewMapObj.get("currentForecastAspList");
						forecastingUnitsListMap.put("currentForecastAspList", currentForecastAspList);
					}
					List overrideForecastAspList = null;
					if(overridenForecastAspList != null){
						overrideForecastAspList= new ArrayList();
						int listSize = overridenForecastAspList.size();
							for(int j=0;j<listSize;j++){
								ForecastingASP forecastingAsp = (ForecastingASP)overridenForecastAspList.get(j);
								try {
									overrideForecastAspList.add(forecastingAsp.clone());
								} catch (CloneNotSupportedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
					}
					ApplicationUtil.setObjectInSession(ApplicationConstants.FORECAST_ASP_LIST_COPY, overrideForecastAspList, request);
					planningCycleBean.setOverrideForecastASPList(overridenForecastAspList);
					ApplicationUtil.setObjectInSession(ApplicationConstants.FORECAST_LIST_MAP, forecastingUnitsListMap, request);
	 			}
			}
		}
		System.out.println("table list: \n    "+jsonTableList);
		planningCycleBean.setJsonStrList(jsonViewStrList);
		planningCycleBean.setJsonTableList(jsonTableList);
		planningCycleBean.setBusiness(business);
		//planningCycleBean.setJsonExortExcelList(jsonTableList);
		map.addAttribute(ApplicationConstants.PLANNING_CYCLE_BEAN, planningCycleBean);
		return new ModelAndView(viewName, "model",planningCycleBean);
}

	@ModelAttribute("planningCycleBean")
	@RequestMapping(value = "/saveForecastUnitsOverride.htm")
	public ModelAndView saveForecastUnitsOverride(
				@ModelAttribute("planningCycleBean") PlanningCycleBean planningCycleBean,
				BindingResult result, HttpServletRequest request) throws ApplicationException {
		System.out.println(planningCycleBean.hashCode());
		int planningCycleId = planningCycleBean.getPlanningCycleId();
		String type= planningCycleBean.getType();
		String selectedTypeValue = planningCycleBean.getSelectedTypeValue();
		List originalForecastUnitsList = (List)ApplicationUtil.getObjectFromSession(ApplicationConstants.FORECAST_UNITS_LIST_COPY, request);
		List overrideForecastUnitsList = planningCycleBean.getOverrideForecastUnitsList();
		String overrideForecastUnitsComment = planningCycleBean.getOverrideForecastUnitsComment();
		if(overrideForecastUnitsComment==null){
			overrideForecastUnitsComment="";
		}
		Users users = (Users)ApplicationUtil.getObjectFromSession(ApplicationConstants.APPLICATION_ACT_AS_USER, request);
		List rolesList = users.getRolesList();
		if(!ApplicationUtil.isEmptyOrNull(type) &&  type.equalsIgnoreCase("PRODUCT")) {
			planningService.saveSKUForecastUnitsOverride(users.getLogin_id(), overrideForecastUnitsComment,originalForecastUnitsList, overrideForecastUnitsList);
		}else if(!ApplicationUtil.isEmptyOrNull(type) &&  (type.equalsIgnoreCase("MODEL") || type.equalsIgnoreCase("CATEGORY"))){
			try {
				int categoryflag=1;//Flag to indicate whether overriding is for Category / Model
				String selectedCategoryValue=null;
				String selectedModelValue=null;
				if(!ApplicationUtil.isEmptyOrNull(type) &&  type.equalsIgnoreCase("MODEL")){
					categoryflag=2;
					selectedModelValue = selectedTypeValue;
				}else{
					selectedCategoryValue=selectedTypeValue;
				}
				int aspFlag = 0;//Flag to indicate whether to calculate override for asp/Units. aspFlag = 0 will calculate for Units
				List<Object[]> param= new ArrayList <Object[]>();//Parameter that I receive from UI
				if(originalForecastUnitsList!= null){
					int size = originalForecastUnitsList.size();
					for(int i=0;i<size;i++){
						ForecastingUnits forecastingUnitsOriginal = (ForecastingUnits) originalForecastUnitsList.get(i);
						ForecastingUnits forecastingUnitsOverriden = (ForecastingUnits) overrideForecastUnitsList.get(i);
						String forecastValueOriginal =  forecastingUnitsOriginal.getForecastValue();
						String forecastValueOverriden =  forecastingUnitsOverriden.getForecastValue();
						String forecastValueOverridenStrArry[] = forecastValueOverriden.split(",");
						forecastValueOverriden = forecastValueOverridenStrArry[0];
						if(!ApplicationUtil.isEmptyOrNull(forecastValueOriginal) && !forecastValueOriginal.equalsIgnoreCase(forecastValueOverriden)){
							Object[] value = new Object[4];	 
							value[0]=selectedCategoryValue;//Category
							value[1]=forecastingUnitsOriginal.getForecastPeriod();//OverriddenWeek
							value[2]=forecastValueOverriden;//Overridden Value
							value[3]=selectedModelValue;//Model Name
							param.add(value);
						}
					}
				}
				Map pageTemplateCacheObj = (Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.PAGE_TEMPLATES_CACHE_KEY);	
				PageTemplate pageTemplate = (PageTemplate)pageTemplateCacheObj.get("PC_DASHBOARD");
				List pageTemplateFilters = pageTemplate.getPageTemplateFilters();
				String businessValue = null;
				Map selectedFilterMapObj = new HashMap();
				if(pageTemplateFilters!=null){
					Map selectedFiltersList=(Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.SELECTED_DASHBOARD_FILTER_VALUES_MAP, request);
					int size = pageTemplateFilters.size();
					for(int i=0;i<size;i++){
						PageTemplateFilters pageTemplateFilter = (PageTemplateFilters) pageTemplateFilters.get(i);
						String filterFieldName = pageTemplateFilter.getFilterFieldName();
						String variableName = pageTemplateFilter.getFilterVariable();
						String filterValue = (String)selectedFiltersList.get(filterFieldName);
						if(!ApplicationUtil.isEmptyOrNull(filterFieldName) && filterFieldName.equalsIgnoreCase("BUSINESS")){
							businessValue = filterValue;
						}
						if(!ApplicationUtil.isEmptyOrNull(filterValue) && !filterValue.equalsIgnoreCase("select")){
							selectedFilterMapObj.put(variableName, filterValue);
						}
						
					}
				}
				planningService.overridingForecastUnits(categoryflag,users.getLogin_id(),param,overrideForecastUnitsComment,aspFlag,planningCycleId,selectedFilterMapObj,rolesList,businessValue);
				
			} catch (ApplicationException e) {
				// TODO: handle exception
				ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e, request);
			}
		}
		planningCycleBean.setSelectedFilterIndex(null);
		planningCycleBean.setIsFromSaveOverride(1);
		ApplicationUtil.removeObjectFromSession(ApplicationConstants.PLANNING_LOG_LIST, request);
		return new ModelAndView("redirect:refreshDashboardDetails.htm");
		
	}
	
	@RequestMapping(value = "/saveForecastAspOverride.htm")
		public ModelAndView saveForecastAspOverride(
					@ModelAttribute("planningCycleBean") PlanningCycleBean planningCycleBean,
					BindingResult result, HttpServletRequest request) throws ApplicationException {
			int planningCycleId = planningCycleBean.getPlanningCycleId();
			String type= planningCycleBean.getType();
			String selectedTypeValue = planningCycleBean.getSelectedTypeValue();
			List originalForecastAspList = (List)ApplicationUtil.getObjectFromSession(ApplicationConstants.FORECAST_ASP_LIST_COPY, request);
			List overrideForecastAspList = planningCycleBean.getOverrideForecastASPList();
			String overrideForecastAspComment = planningCycleBean.getOverrideForecastASPComment();
			if(overrideForecastAspComment==null){
				overrideForecastAspComment="";
			}
			Users users = (Users)ApplicationUtil.getObjectFromSession(ApplicationConstants.APPLICATION_ACT_AS_USER, request);
			List rolesList = users.getRolesList();
			if(!ApplicationUtil.isEmptyOrNull(type) &&  type.equalsIgnoreCase("PRODUCT")) {
				planningService.saveSKUForecastAspOverride(users.getLogin_id(), overrideForecastAspComment,originalForecastAspList, overrideForecastAspList);
			}else if(!ApplicationUtil.isEmptyOrNull(type) &&  (type.equalsIgnoreCase("MODEL") || type.equalsIgnoreCase("CATEGORY"))){
				try {
					int categoryflag=1;//Flag to indicate whether overriding is for Category / Model
					String selectedCategoryValue=null;
					String selectedModelValue=null;
					if(!ApplicationUtil.isEmptyOrNull(type) &&  type.equalsIgnoreCase("MODEL")){
						categoryflag=2;
						selectedModelValue = selectedTypeValue;
					}else{
						selectedCategoryValue=selectedTypeValue;
					}
					int aspFlag = 1;//Flag to indicate whether to calculate override for asp/Units. aspFlag = 0 will calculate for Units
					List<Object[]> param= new ArrayList <Object[]>();//Parameter that I receive from UI
					if(originalForecastAspList!= null){
						int size = originalForecastAspList.size();
						for(int i=0;i<size;i++){
							ForecastingASP forecastingAspOriginal = (ForecastingASP) originalForecastAspList.get(i);
							ForecastingASP forecastingAspOverriden = (ForecastingASP) overrideForecastAspList.get(i);
							String forecastValueOriginal =  forecastingAspOriginal.getForecastValue();
							String forecastValueOverriden =  forecastingAspOverriden.getForecastValue();
							String forecastValueOverridenStrArry[] = forecastValueOverriden.split(",");
							forecastValueOverriden = forecastValueOverridenStrArry[0];
							if(!ApplicationUtil.isEmptyOrNull(forecastValueOriginal) && !forecastValueOriginal.equalsIgnoreCase(forecastValueOverriden)){
								Object[] value = new Object[5];	 
								value[0]=selectedCategoryValue;//Category
								value[1]=forecastingAspOriginal.getForecastPeriod();//OverriddenWeek
								value[2]=forecastValueOverriden;//Overridden Value
								value[3]=selectedModelValue;//Model Name
								value[4]=forecastValueOriginal;//original forecast value
								param.add(value);
							}
						}
					}
					Map pageTemplateCacheObj = (Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.PAGE_TEMPLATES_CACHE_KEY);	
					PageTemplate pageTemplate = (PageTemplate)pageTemplateCacheObj.get("PC_DASHBOARD");
					List pageTemplateFilters = pageTemplate.getPageTemplateFilters();
					Map selectedFilterMapObj = new HashMap();
					String businessValue = null;
					if(pageTemplateFilters!=null){
						Map selectedFiltersList=(Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.SELECTED_DASHBOARD_FILTER_VALUES_MAP, request);
						int size = pageTemplateFilters.size();
						for(int i=0;i<size;i++){
							PageTemplateFilters pageTemplateFilter = (PageTemplateFilters) pageTemplateFilters.get(i);
							String filterFieldName = pageTemplateFilter.getFilterFieldName();
							String variableName = pageTemplateFilter.getFilterVariable();
							String filterValue = (String)selectedFiltersList.get(filterFieldName);
							if(!ApplicationUtil.isEmptyOrNull(filterFieldName) && filterFieldName.equalsIgnoreCase("BUSINESS")){
								businessValue = filterValue;
							}
							if(!ApplicationUtil.isEmptyOrNull(filterValue) && !filterValue.equalsIgnoreCase("select")){
								selectedFilterMapObj.put(variableName, filterValue);
							}
							
						}
					}
					planningService.overridingForecastUnits(categoryflag,users.getLogin_id(),param,overrideForecastAspComment,aspFlag,planningCycleId,selectedFilterMapObj,rolesList,businessValue);
				} catch (Exception e) {
					// TODO: handle exception
					ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e, request);
				}
			}
			planningCycleBean.setSelectedFilterIndex(null);
			planningCycleBean.setIsFromSaveOverride(1);
			ApplicationUtil.removeObjectFromSession(ApplicationConstants.PLANNING_LOG_LIST, request);
			ApplicationException ae = ApplicationException
					.createApplicationException("DashboardController",
							"saveForecastAspOverride",
							ApplicationErrorCodes.APP_EC_14,
							ApplicationConstants.INFORMATION, null);
		ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, ae, request);
			return new ModelAndView("redirect:refreshDashboardDetails.htm");
	 }
	
	  @RequestMapping(value = "/logout.htm",method=RequestMethod.POST)
	 	public String getLogoutPage(HttpServletRequest request) {
			ServletContext sc = request.getSession().getServletContext(); 
	    	String bridgei2iTemplatePptFilePathStr = sc.getRealPath("/WEB-INF");
	    	String destinationFileStr = bridgei2iTemplatePptFilePathStr + "/CustomReport";
	    	String destinationPathFileStr = destinationFileStr+"/custom_ppt.pptx";
	    	File file = new File(destinationPathFileStr);	 
			if(file.delete()){
				System.out.println( "PPT File is deleted!");
			}
					return "redirect:logout";
		}
}
