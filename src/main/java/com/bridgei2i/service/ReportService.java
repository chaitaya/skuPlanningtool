package com.bridgei2i.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgei2i.common.exception.ApplicationException;
import com.bridgei2i.common.constants.ApplicationConstants;
import com.bridgei2i.common.constants.ApplicationErrorCodes;
import com.bridgei2i.common.dao.ReportDAO;
import com.bridgei2i.form.ReportBean;

@Service("reportService")
public class ReportService {
	
	private static Logger logger = Logger.getLogger(ReportService.class);

	@Autowired(required=true)
	private ReportDAO reportDAO;
	
	public Map getReportTableView(Map targetTableViewMapObj,String tableName,Integer planningCycleId,String filterVariable, String business,String whereClauseStr,Integer week,Integer year,Integer range, String selectedTypeValue,String selectedPeriod,Map targetTablecolorRangeMapObj) throws ApplicationException{
		
		Map jsonViewObj = reportDAO.getReportTableView(targetTableViewMapObj,tableName,planningCycleId,filterVariable,business,whereClauseStr,week,year,range,selectedTypeValue,selectedPeriod,targetTablecolorRangeMapObj);
		logger.debug("Leaving from getReportTableView");
		return jsonViewObj;
	}
	
	public Map getAccuracyReportList(final String noOfWeeks,final String filterCondition,final int reportType,String business)throws ApplicationException{
		try{
			return reportDAO.getAccuracyReportList(noOfWeeks,filterCondition,reportType,business);
		}catch(Exception e){
		e.printStackTrace();
		ApplicationException applicationException = ApplicationException.createApplicationException(ReportDAO.class.toString(), "saveCommitStatus", 
				ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
		throw applicationException;
		}
	}

	public List getOverrideReport(Integer planningCycleId,String productId,String modelId,String categoryId,String whereCondition) throws ApplicationException {
		try {
			return reportDAO.getOverrideReport(planningCycleId,productId,modelId,categoryId,whereCondition);
		} catch (ApplicationException e) {
			logger.error(e.getMessage());
			throw e;
		}

	}
	
	@Transactional
	public Map getClosedReportPlanningCycleList(boolean includeActiveCycle) {
		Map planningCycleListMap = null;
		try {
			planningCycleListMap = reportDAO.getClosedReportPlanningCycleListMap(includeActiveCycle);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
		return planningCycleListMap;
	}
	
	public Map getHistoricalActualReportChartJsonFromDatabase(String tableName,String productId, Integer planningCycleId,  String business,List rolesList,Integer userId,Integer week,Integer year,Integer range,String selectedPeriod) throws ApplicationException{
		Map jsonViewObj = reportDAO.getHistoricalActualReportChartJsonFromDatabase(tableName,productId,planningCycleId,business,rolesList,userId,week,year,range,selectedPeriod);
		logger.debug("Leaving from getHistoricalReportChartJsonFromDatabase");
		return jsonViewObj;
	}
	
	public Map getHistoricalActualReportChartJsonForAggregated(String tableName,String selectedTypeVariable,Integer planningCycleId,String selectedTypeValue,Integer userId, String business,String whereClauseStr,List rolesList,Integer week,Integer year,Integer range,String selectedPeriod) throws ApplicationException{
		Map jsonViewObj = reportDAO.getHistoricalActualReportChartJsonForAggregated(tableName,selectedTypeVariable,planningCycleId,selectedTypeValue,userId,business,whereClauseStr,rolesList,week,year,range,selectedPeriod);
		logger.debug("Leaving from getHistoricalReportChartJsonForAggregated");
		return jsonViewObj;
	}
	
	public Map gePlanogramtReports(Map planogramTableViewMapObj,String tableName,Integer planningCycleId,String whereClauseStr,Integer week,Integer year,Integer range,String selectedPeriod,Map targetTablecolorRangeMapObj) throws ApplicationException{
		
		Map jsonViewObj = reportDAO.gePlanogramtReports(planogramTableViewMapObj,tableName,planningCycleId,whereClauseStr,week,year,range,selectedPeriod,targetTablecolorRangeMapObj);
		logger.debug("Leaving from getReportTableView");
		return jsonViewObj;
	}
	
	public List getFlatFile(Integer planningCycleId,Integer week,Integer year) throws ApplicationException {
        try {
               return reportDAO.getFlatFile(planningCycleId,week,year);
        } catch (ApplicationException e) {
               logger.error(e.getMessage());
               throw e;
        }

 }

}
