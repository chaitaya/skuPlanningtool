package com.bridgei2i.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgei2i.common.constants.ApplicationConstants;
import com.bridgei2i.common.constants.ApplicationErrorCodes;
import com.bridgei2i.common.dao.ApplicationDAO;
import com.bridgei2i.common.dao.PlanningDAO;
import com.bridgei2i.common.exception.ApplicationException;
import com.bridgei2i.common.util.ApplicationUtil;
import com.bridgei2i.common.vo.Users;
import com.bridgei2i.vo.PlanningCycle;

@Service("planningService")
public class PlanningService {
	
	private static Logger logger = Logger.getLogger(PlanningService.class);

	@Autowired(required=true)
	private PlanningDAO planningDAO;
	
	public static Map applicationCacheObject = null;
	
	@Transactional
	public List getAllPlanningCycleValue(){
		List planningCycleList=null;
		
		try {
			planningCycleList=planningDAO.getAllPlanningCycle();
			
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
		return planningCycleList;
	}
	//for saving the week 
	@Transactional
	public Integer  savedWeek(PlanningCycle planningCycle){
		Integer id=null;
		try {
			id=planningDAO.saveActiveWeek(planningCycle);
			
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
		return id;
	}
	//for updating  saved week 
	@Transactional
	public Integer  updateSavedWeek(Integer id,String startWeek, String startYear )throws ApplicationException{
		try {
			planningDAO.updateSavedWeek(id,startWeek,startYear);
			
		} catch (ApplicationException e) {
			e.printStackTrace();
			throw e;
		}
		return id;
	}
	@Transactional
	public String getMasterPlanningStatusByStatusName(String statusName){
		String masterPlanningStatus=null;
		try {
			 masterPlanningStatus=(String) planningDAO.getMasterPlanningStatusByStatusName(statusName);
			
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
		return masterPlanningStatus;
	}
	
	public void saveData(String filePath, String dbTable , Integer referenceId, String referenceColumnName,boolean deleteTableDataBeforeLoad,Integer planningCycleId ) throws ApplicationException {
		try{
		planningDAO.saveData(filePath,dbTable,referenceId,referenceColumnName,deleteTableDataBeforeLoad,planningCycleId);
		}
		 catch (ApplicationException e) {
			 logger.error(e.getMessage());
				throw e;
			}
	}
	
	public void saveVariableNames(String filePath, String dbTable , Integer referenceId, String referenceColumnName,boolean deleteTableDataBeforeLoad ) throws ApplicationException {
		try{
		planningDAO.saveVariableNames(filePath,dbTable,referenceId,referenceColumnName,deleteTableDataBeforeLoad);
		}
		 catch (ApplicationException e) {
			 logger.error(e.getMessage());
				throw e;
			}
	}
	
	public void loadAggregatedDataIntoTable(Integer planningCycleId) throws ApplicationException {
		
		planningDAO.loadAggregatedDataIntoTable(planningCycleId);
		
	}
		
	public void outlierTreatment (Integer planningCycleId)throws ApplicationException {
		
		planningDAO.outlierTreatment(planningCycleId);
	}
	
	public List getPlanningLogDetails(final int userId,final int planningCycleId,final String filterCondition,final String roleFilterCondition,final List rolesList,String businessValue,Integer week,Integer year,final int planningLogType, final String skuList) throws ApplicationException{
		try{
				return planningDAO.getPlanningLogDetails(userId,planningCycleId,filterCondition,roleFilterCondition,rolesList,businessValue,week,year,planningLogType,skuList);

		}catch(Exception e){
			e.printStackTrace();
			ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "saveCommitStatus", 
					ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
	}
	
	public void saveCommitStatus(int userId,List selectedSKUIdList,int planningCycleId,List rolesList,String commitStatusId,String business) throws ApplicationException{
		try{
				planningDAO.saveCommitStatus(userId,selectedSKUIdList,planningCycleId,rolesList,commitStatusId,business);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
public List getPageTemplateFilters(Integer userId,String filterVariable,String tableName,String whereCondition,List rolesList,String businessValue,Integer week,Integer year) throws ApplicationException
{
	try{
    return planningDAO.getPageTemplateFilters(userId,filterVariable,tableName,whereCondition,rolesList,businessValue,week,year);
	}
	 catch (ApplicationException e) {
		 logger.error(e.getMessage());
			throw e;
		}
}

	public Users getUserFromUserName(String userName) throws ApplicationException{
		try{
			
			return planningDAO.getUserFromUserName(userName);
		}
		 catch (ApplicationException e) {
			 logger.error(e.getMessage());
				throw e;
			}
    }
	
	public Map getChartJsonFromDatabase(String tableName,String productId, Integer planningCycleId,  String business,List rolesList,Integer userId,Integer week,Integer year,Integer range) throws ApplicationException{
		
		Map jsonViewObj = planningDAO.getChartJsonFromDatabase(tableName,productId,planningCycleId,business,rolesList,userId,week,year,range);
		logger.debug("Leaving from getChartJsonFromDatabase");
		return jsonViewObj;
	}
	
	public Map getChartJsonForAggregated(String tableName,String productId, Integer planningCycleId,String selectedTypeVariable, String selectedTypeValue,Integer userId, String business,String whereClauseStr,List rolesList,Integer week,Integer year,Integer range,String type) throws ApplicationException{
		
		Map jsonViewObj = planningDAO.getChartJsonForAggregated(tableName,productId,planningCycleId,selectedTypeVariable,selectedTypeValue,userId,business,whereClauseStr,rolesList,week,year,range,type);
		logger.debug("Leaving from getChartJsonForModel");
		return jsonViewObj;
	}


/**
 * 
 * @param Categoryflag
 * @param loginId
 * @param param
 * @param comment
 * @param aspFlag
 * Performs Downwards Overriding for Model/Category
 * @throws ApplicationException 
 */
public void overridingForecastUnits(int categoryflag,int loginId,List<Object[]> param,String comment,int aspFlag,int planningCycleId,Map selectedFilterMapObj,List rolesList,String businessValue) throws ApplicationException{
	logger.debug("Entering into overridingForecastUnits method in PlanningService");
	List<Object[]> SKUListUnderCategory= null;
	List<Object[]> SKUListUnderModel=  null;
	List<Object[]> SKUwithOverrideValue=  null;
	List<Object[]> forecastUnitsIdForSkU=  null;
	List<Object[]> ForecastAspIdForSkU=  null;
	int totalSKUForUser = 0;
	try {
		for(Object [] overrideInputValue : param)
		{
			if(aspFlag == 0)
			{
				if( categoryflag == 1 )
				{
					SKUListUnderCategory = planningDAO.getSKUunderCategory(overrideInputValue[0].toString(),overrideInputValue[1].toString(),planningCycleId,selectedFilterMapObj);
					//if(SKUListUnderCategory.size() <= totalSKUForUser)
					//{
					forecastUnitsIdForSkU = planningDAO.forecastingUnitsIdforSku(SKUListUnderCategory,overrideInputValue[1].toString(),planningCycleId,businessValue);
						if(forecastUnitsIdForSkU.size()==0){
							Object obj[] = {"-1"};
							forecastUnitsIdForSkU.add(obj);
						}
						SKUwithOverrideValue = planningDAO.getOverriddenValueUnits(SKUListUnderCategory,overrideInputValue[1].toString(),forecastUnitsIdForSkU,planningCycleId);
						planningDAO.overrideForecast(SKUwithOverrideValue,overrideInputValue[2].toString(),loginId,comment,aspFlag,overrideInputValue[1].toString(),planningCycleId,ApplicationConstants.OVERRIDE_LEVEL_CATEGORY,businessValue);
					//}
				}
				else
				{
					SKUListUnderModel = planningDAO.getSKUunderModel(overrideInputValue[3].toString(),overrideInputValue[1].toString(),planningCycleId,selectedFilterMapObj);
					if(rolesList!= null && rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){
						totalSKUForUser = planningDAO.getSKUCountUnderUser(loginId,SKUListUnderModel,businessValue);
					}
					boolean flag = true;
					if(SKUListUnderModel.size() != totalSKUForUser){
						if(rolesList!= null && rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){
							flag=false;
						}
					}
					if(flag){
						forecastUnitsIdForSkU = planningDAO.forecastingUnitsIdforSku(SKUListUnderModel,overrideInputValue[1].toString(),planningCycleId,businessValue);
						if(forecastUnitsIdForSkU.size()==0){
							Object obj[] = {"-1"};
							forecastUnitsIdForSkU.add(obj);
						}
						SKUwithOverrideValue = planningDAO.getOverriddenValueUnits(SKUListUnderModel,overrideInputValue[1].toString(),forecastUnitsIdForSkU,planningCycleId);
						planningDAO.overrideForecast(SKUwithOverrideValue,overrideInputValue[2].toString(),loginId,comment,aspFlag,overrideInputValue[1].toString(),planningCycleId,ApplicationConstants.OVERRIDE_LEVEL_MODEL,businessValue);
					}else{
						ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "saveContact", 
								ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, null);
						throw applicationException;
					}
				}
			}
			else if (aspFlag == 1)
			{
				if( categoryflag == 1 )
				{
					SKUListUnderCategory = planningDAO.getSKUunderCategory(overrideInputValue[0].toString(),overrideInputValue[1].toString(),planningCycleId,selectedFilterMapObj);
					//if(SKUListUnderCategory.size() <= totalSKUForUser)
					//{
					ForecastAspIdForSkU = planningDAO.forecastingAspIdforSku(SKUListUnderCategory,overrideInputValue[1].toString(),planningCycleId,businessValue);
					if(ForecastAspIdForSkU.size()==0){
						Object obj[] = {"-1"};
						ForecastAspIdForSkU.add(obj);
					}
					SKUwithOverrideValue = planningDAO.getOverriddenValueAsp(SKUListUnderCategory,overrideInputValue[1].toString(),ForecastAspIdForSkU,planningCycleId);
						planningDAO.overrideForecastAsp(SKUwithOverrideValue,overrideInputValue[2].toString(),loginId,comment,aspFlag,overrideInputValue[1].toString(),planningCycleId,overrideInputValue[4].toString(),ApplicationConstants.OVERRIDE_LEVEL_CATEGORY);
					//}
				}
				else
				{
					SKUListUnderModel = planningDAO.getSKUunderModel(overrideInputValue[3].toString(),overrideInputValue[1].toString(),planningCycleId,selectedFilterMapObj);
					boolean flag = true;
					if(rolesList!= null && rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){
						totalSKUForUser = planningDAO.getSKUCountUnderUser(loginId,SKUListUnderModel,businessValue);
					}
					if(SKUListUnderModel.size() != totalSKUForUser){
						if(rolesList!= null && rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){
							flag=false;
						}
					}
					if(flag){
						ForecastAspIdForSkU = planningDAO.forecastingAspIdforSku(SKUListUnderModel,overrideInputValue[1].toString(),planningCycleId,businessValue);
						if(ForecastAspIdForSkU.size()==0){
							Object obj[] = {"-1"};
							ForecastAspIdForSkU.add(obj);
						}	
						SKUwithOverrideValue = planningDAO.getOverriddenValueAsp(SKUListUnderModel,overrideInputValue[1].toString(),ForecastAspIdForSkU,planningCycleId);
						planningDAO.overrideForecastAsp(SKUwithOverrideValue,overrideInputValue[2].toString(),loginId,comment,aspFlag,overrideInputValue[1].toString(),planningCycleId,overrideInputValue[4].toString(),ApplicationConstants.OVERRIDE_LEVEL_MODEL);
					}else{
						ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "overridingForecastUnits", 
								ApplicationErrorCodes.APP_EC_2, ApplicationConstants.EXCEPTION, null);
						throw applicationException;
					}
				}
			}
		}
	} catch (ApplicationException e) {
		logger.error(e.getMessage());
		throw e;
	}catch (Exception e) {
		logger.error(e.getMessage());
		ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "overridingForecastUnits", 
				ApplicationErrorCodes.APP_EC_2, ApplicationConstants.EXCEPTION, null);
		throw applicationException;
	}
	logger.debug("Leaving from overridingForecastUnits method in PlanningService");
}
/**
 * 
 * @param year
 * @param week
 * @param planningCycleId
 * @throws ApplicationException
 */
public void baseForecast(int year,int week,int planningCycleId) throws ApplicationException {
	try{
		planningDAO.baseForecast(year,week,planningCycleId);
 	}
	 catch (ApplicationException e) {
		 logger.error(e.getMessage());
			throw e;
		}
}
	
	public void updatePlanningCycleStatus(Integer planningCycleId, String logicalName) throws ApplicationException {
		try{
			planningDAO.updatePlanningCycleStatus(planningCycleId,logicalName);
		}
		 catch (ApplicationException e) {
			 logger.error(e.getMessage());
				throw e;
			}
	}
	
	public void flushTables(Integer planningCycleId) throws ApplicationException {
		try{
			planningDAO.flushTables(planningCycleId);
		}
		 catch (ApplicationException e) {
			 logger.error(e.getMessage());
				throw e;
			}
	}
	
	
	public void flushDuplicateRowsFromData(Integer planningCycleId) throws ApplicationException {
		try{
			planningDAO.flushDuplicateRowsFromData(planningCycleId);
		}
		 catch (ApplicationException e) {
			 logger.error(e.getMessage());
				throw e;
			}
	}
	
	public void saveSKUForecastUnitsOverride(Integer userId,String overrideForecastUnitsComment,List originalForecastUnitsList,List overrideForecastUnitsList) throws ApplicationException {
		try{
			planningDAO.saveSKUForecastUnitsOverride(userId,overrideForecastUnitsComment,originalForecastUnitsList,overrideForecastUnitsList);
		}catch (ApplicationException e) {
			 logger.error(e.getMessage());
			 throw e;
		}
	}
	
	public void saveSKUForecastAspOverride(Integer userId,String overrideForecastUnitsComment,List originalForecastUnitsList,List overrideForecastUnitsList) throws ApplicationException {
 		try{
			planningDAO.saveSKUForecastAspOverride(userId,overrideForecastUnitsComment,originalForecastUnitsList,overrideForecastUnitsList);
 		}catch (ApplicationException e) {
 			 logger.error(e.getMessage());
 			 throw e;
 		}
	}
	
	@Transactional
	public List getAllSkulist(List rolesList,Integer userId){
		List planningCycleList=null;
		
		try {
			planningCycleList=planningDAO.getEolData(rolesList,userId);
			
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
		return planningCycleList;
	}
	
	@Transactional
	public Integer  eolUpdateData(Integer id,String startWeek, String startYear )throws ApplicationException{
		try {
		planningDAO.eolUpdateData(id,startWeek,startYear);
			
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public void saveTargetData(String filePath, String dbTable , Integer referenceId, String referenceColumnName,boolean deleteTableDataBeforeLoad) throws ApplicationException {
		try{
		planningDAO.saveTargetData(filePath,dbTable,referenceId,referenceColumnName,deleteTableDataBeforeLoad);
		}
		 catch (ApplicationException e) {
			 logger.error(e.getMessage());
			 e.printStackTrace();
				throw e;
			}
	}
	
	public int getCommitStatusId(String productId,int planningCycleId,String businessValue) throws ApplicationException{
		return planningDAO.getCommitStatusId(productId,planningCycleId,businessValue);
	}
	
	/*public int getModelAndCategoryLevelStatus(final String whereClause,final List rolesList,final String filterCondition,final String roleFilterCondition,final int planningCycleId)  throws ApplicationException{
		return planningDAO.getModelAndCategoryLevelStatus(whereClause, rolesList, filterCondition, roleFilterCondition, planningCycleId);
	}*/
	
	public void saveBtbData(String filePath, String dbTable , Integer referenceId, String referenceColumnName,boolean deleteTableDataBeforeLoad) throws ApplicationException {
		try{
		planningDAO.saveBtbData(filePath,dbTable,referenceId,referenceColumnName,deleteTableDataBeforeLoad);
		}
		 catch (ApplicationException e) {
			 logger.error(e.getMessage());
			 e.printStackTrace();
				throw e;
			}
	}
	public void uploadEventCalendar(String filePath, String dbTable , Integer referenceId, String referenceColumnName,boolean deleteTableDataBeforeLoad) throws ApplicationException {
		try{
		planningDAO.uploadEventCalendar(filePath,dbTable,referenceId,referenceColumnName,deleteTableDataBeforeLoad);
		}
		 catch (ApplicationException e) {
			 logger.error(e.getMessage());
			 e.printStackTrace();
				throw e;
			}
	}

	public int getModelLevelStatus(int userId,String whereClause,List rolesList,String filterCondition, int planningCycleId,String type,String businessValue,int week,int year) throws ApplicationException{
		try {
			return planningDAO.getModelLevelStatus(userId,whereClause,rolesList,filterCondition,planningCycleId,type,businessValue,week,year);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}
	
	public int  getInitiatedPlanningCycleId(){
		int planningCycleId=0;
		try {
			planningCycleId=planningDAO.getInitiatedPlanningCycleId();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return planningCycleId;
	}
	
	
	public void automaticUpload() throws ApplicationException {
		try{
			planningDAO.automaticUpload();
		}
		 catch (ApplicationException e) {
			 logger.error(e.getMessage());
				throw e;
			}
	}
	
   public void loadNPIIntoDataTable(int planningCycleId) throws ApplicationException{
		try {
			planningDAO.loadNPIIntoDataTable(planningCycleId);
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }
   
   public void loadCacheData() throws ApplicationException{
	   try {
			planningDAO.loadCacheData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
   
   public void updateSKUList(final int planningCycleId) throws ApplicationException{
	   try {
			planningDAO.updateSKUList(planningCycleId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
   public List getForecastValuesFromPlanningLog(final int userId,final int planningCycleId,final String filterCondition,final String roleFilterCondition,final List rolesList,String businessValue,final int forecastMetric,final int week,final int year) throws ApplicationException{
		try{
			return planningDAO.getForecastValuesFromPlanningLog(userId,planningCycleId,filterCondition,roleFilterCondition,rolesList,businessValue,forecastMetric,week,year);
	}catch(Exception e){
		e.printStackTrace();
		ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "getForecastValuesFromPlanningLog", 
				ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
		throw applicationException;
		}
   }
   
   public List getForecastPeriod(final int planningCycleId) throws ApplicationException{
		try{
			return planningDAO.getForecastPeriod(planningCycleId);
	}catch(Exception e){
		e.printStackTrace();
		ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "getForecastPeriod", 
				ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
		throw applicationException;
	}
}
   
	public void savePlanningLogUserSession(final int userId, final String skuList) throws ApplicationException{
		try{
				planningDAO.savePlanningLogUserSession(userId,skuList);

		}catch(Exception e){
			e.printStackTrace();
			ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "saveCommitStatus", 
					ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
	}
	

	public List getPlanningLogUserSession(final int userId) throws ApplicationException{
		try{
				return planningDAO.getPlanningLogUserSession(userId);

		}catch(Exception e){
			e.printStackTrace();
			ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "saveCommitStatus", 
					ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
	}
	
	public void clearPlanningLogUserSession(final int userId) throws ApplicationException{
		try{
				 planningDAO.clearPlanningLogUserSession(userId);

		}catch(Exception e){
			e.printStackTrace();
			ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "clearPlanningLogUserSession", 
					ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
	}
}
