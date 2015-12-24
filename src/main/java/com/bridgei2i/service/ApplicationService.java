package com.bridgei2i.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVReader;

import com.bridgei2i.common.constants.ApplicationConstants;
import com.bridgei2i.common.constants.ApplicationErrorCodes;
import com.bridgei2i.common.controller.LoadApplicationCacheService;
import com.bridgei2i.common.dao.ApplicationDAO;
import com.bridgei2i.common.exception.ApplicationException;
import com.bridgei2i.common.util.ApplicationUtil;
import com.bridgei2i.common.util.PropertiesUtil;
import com.bridgei2i.common.vo.DropDownDisplayVo;
import com.bridgei2i.common.vo.SingleMultiSeriesChartValueObject;
import com.bridgei2i.common.vo.Users;
import com.bridgei2i.form.WorkspaceBean;
import com.bridgei2i.vo.BannerTable;
import com.bridgei2i.vo.ChartFrequencies;
import com.bridgei2i.vo.ChartSentimentPercentage;
import com.bridgei2i.vo.Comments;
import com.bridgei2i.vo.Employee;
import com.bridgei2i.vo.MasterReportStatus;
import com.bridgei2i.vo.ReportMetadata;
import com.bridgei2i.vo.TemplateChart;
import com.bridgei2i.vo.TemplateChartCategory;
import com.bridgei2i.vo.TemplateChartInfo;
import com.bridgei2i.vo.WordCloud;

import edu.emory.mathcs.backport.java.util.Arrays;
import flex.messaging.io.ObjectProxy;

@Service("applicationService")

public class ApplicationService{

	private static Logger logger = Logger.getLogger(ApplicationService.class);

	@Autowired(required=true)
	private ApplicationDAO applicationDAO;
	
	public ApplicationService() {
	}

	public List getTemplateCharts(Integer loginId,String emailId,boolean filterTemplates) throws ApplicationException {
		try {
			return applicationDAO.getTemplateCharts(loginId,emailId,filterTemplates);
		} catch (ApplicationException e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	}
	
	public TemplateChart getTemplateChart(Integer templateChartId) throws ApplicationException {
		try {
			return applicationDAO.getTemplateChart(templateChartId);
		} catch (ApplicationException e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	}

	public List getReportMetaData(Integer templateChartId) throws ApplicationException {
		try {
			return applicationDAO.getReportMetaData(templateChartId);
		} catch (ApplicationException e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	}
	
	
	public void saveData(String filePath, String dbTable , Integer referenceId, String referenceColumnName,boolean deleteTableDataBeforeLoad,Integer planningCycleId ) throws ApplicationException {
		try{
		applicationDAO.saveData(filePath,dbTable,referenceId,referenceColumnName,deleteTableDataBeforeLoad,planningCycleId);
		}
		 catch (ApplicationException e) {
			 logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
				throw e;
			}
	}
	public void saveVariableNames(String filePath, String dbTable , Integer referenceId, String referenceColumnName,boolean deleteTableDataBeforeLoad ) throws ApplicationException {
		try{
		applicationDAO.saveVariableNames(filePath,dbTable,referenceId,referenceColumnName,deleteTableDataBeforeLoad);
		}
		 catch (ApplicationException e) {
			 logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
				throw e;
			}
	}
	
	
	
	
	
	public void loadAggregatedDataIntoTable(Integer planningCycleId) throws ApplicationException {
		applicationDAO.loadAggregatedDataIntoTable(planningCycleId);
	}
		
	public void saveVariableNames(List variableNameList) throws ApplicationException {
		try{
		applicationDAO.saveVariableNames(variableNameList);
		}
		 catch (ApplicationException e) {
			 logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
				throw e;
			}
	}
	
	public void saveDesignReportTemplate(TemplateChart templateChart,String[] selectedDistributionIds) throws ApplicationException {
        try {
                applicationDAO.saveDesignReportTemplate(templateChart,selectedDistributionIds);
        } catch (ApplicationException e) {
        	logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
                throw e;
        }
	}
	
	public void deleteDesignReportTemplate(List templateChartList) throws ApplicationException {
        try {
                applicationDAO.deleteDesignReportTemplate(templateChartList);
        } catch (ApplicationException e) {
        	logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
                throw e;
        }
	}
	
	public List getVariableNames() throws ApplicationException{
		try {
            return applicationDAO.getVariableNames();
		} catch (ApplicationException e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
            throw e;
		}
	}
	
	public void refreshReports(TemplateChart templateChart) throws ApplicationException{
		try{
		applicationDAO.refreshReports(templateChart);
		}
		 catch (ApplicationException e) {
			 logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
				throw e;
			}
	}
	
	public void runReports(WorkspaceBean workspaceBean) throws ApplicationException{
		try{
		applicationDAO.runReports(workspaceBean);
		}
		 catch (ApplicationException e) {
			 logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
				throw e;
			}
	}
	
	public void reviewRequest(List requestReviewList) throws ApplicationException {
		try{
        applicationDAO.reviewRequest(requestReviewList);
		}
		 catch (ApplicationException e) {
			 logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
				throw e;
			}
	}
	
	public void saveDashboardTemplate(Comments comments, TemplateChart templateChart) throws ApplicationException {
		try{
        applicationDAO.saveDashboardTemplate(comments,templateChart);
		}
		 catch (ApplicationException e) {
			 logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
				throw e;
			}
	}
	
	public MasterReportStatus getMasterReportOpenStatus(String statusLogicalName) throws ApplicationException{
		try{
            return applicationDAO.getMasterReportOpenStatus(statusLogicalName);
		}
		 catch (ApplicationException e) {
			 logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
				throw e;
			}
	}
	
	public Users getUserFromUserName(String userName) throws ApplicationException{
		try{
        return applicationDAO.getUserFromUserName(userName);
		}
		 catch (ApplicationException e) {
			 logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
				throw e;
			}
    }
	
	public List getRolesMailId(String rolesNameList) throws ApplicationException{
		try{
		 return applicationDAO.getRolesMailId(rolesNameList);
		}
		 catch (ApplicationException e) {
			 logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
				throw e;
			}
	}
	
	public List getDistributionList(Integer templateChartId) throws ApplicationException {
		return applicationDAO.getDistributionList(templateChartId);
	}
	
	public TemplateChart getDashboardTemplateChartPreview(TemplateChart templateChart,String empId ,String emailId) throws ApplicationException {
		try{
		logger.debug("Entered into getDashboardTemplateChartPreview");
		List distributionListDropDownVos = new ArrayList();
		String employeeId="";
		Integer reportMetaDataId =null;
		String isOverall = templateChart.getIsOverall();
		
		if(isOverall.equalsIgnoreCase("N")){
			if(ApplicationUtil.isEmptyOrNull(empId)){
				List distributionList = applicationDAO.getDistributionList(templateChart.getId());
				if(distributionList != null){
					int len = distributionList.size();
					for(int k=0;k<len;k++){
						Employee employee = (Employee)distributionList.get(k);
						if(k==0)
						{
							employeeId=employee.getEmployeeId();
						}
						DropDownDisplayVo dropDownDisplayVo = new DropDownDisplayVo();
						dropDownDisplayVo.setDisplayName(employee.getName());
						dropDownDisplayVo.setValue(employee.getEmployeeId());
						distributionListDropDownVos.add(dropDownDisplayVo);
					}
					templateChart.setDistributionList(distributionListDropDownVos);
				}
			}else{
				employeeId = empId;
			}
			
		}else{
			List templateChartReportAssigns = templateChart.getTemplateChartReportAssigns();
			Map reportMetaDataMapObj = new HashMap();
	        // templateChart.getTemplateChartReportAssigns() is null and overall == y
	        if((templateChartReportAssigns==null || templateChartReportAssigns.size()==0) && isOverall.equalsIgnoreCase("Y")){
	        	List reportMetaDataIdListDropDownVos = new ArrayList();
	        	List reportMetaDataIdList = null;
	        	if(!ApplicationUtil.isEmptyOrNull(emailId)){
	        		reportMetaDataIdList = applicationDAO.getReportMetaDataIdList(emailId);
	        	}else{
	        		reportMetaDataIdList = applicationDAO.getReportMetaData(templateChart.getId());
	        	}
	        	
	        	if(reportMetaDataIdList != null){
					int len = reportMetaDataIdList.size();
					for(int k=0;k<len;k++){
						ReportMetadata reportMetaData = (ReportMetadata)reportMetaDataIdList.get(k);
						if(k==0){
							reportMetaDataId = reportMetaData.getId();
							templateChart.setReportMetaDataId(reportMetaData.getId());
							templateChart.setFilter1(reportMetaData.getFilterColumn1());
							templateChart.setFilterValues1(reportMetaData.getFilterValue1());
							templateChart.setFilter2(reportMetaData.getFilterColumn2());
							templateChart.setFilterValues2(reportMetaData.getFilterValue2());
							templateChart.setFilter3(reportMetaData.getFilterColumn3());
							templateChart.setFilterValues3(reportMetaData.getFilterValue3());
							templateChart.setIsOverall("Y");
							templateChart.setId(reportMetaData.getTemplatechartId());
						}
						DropDownDisplayVo dropDownDisplayVo = new DropDownDisplayVo();
						dropDownDisplayVo.setDisplayName(reportMetaData.getReportTitle());
						dropDownDisplayVo.setValue(reportMetaData.getId()+"");
						reportMetaDataIdListDropDownVos.add(dropDownDisplayVo);
						reportMetaDataMapObj.put(reportMetaData.getId().toString(), reportMetaData);
					}
				}
	        	templateChart.setReportMetaDataId(reportMetaDataId);
	        	templateChart.setReportMetaDataIdList(reportMetaDataIdListDropDownVos);
	        	templateChart.setReportMetaDataMapObj(reportMetaDataMapObj);
	        }
		}
		templateChart = getDashboardTemplateChart(templateChart,employeeId,emailId,reportMetaDataId);
		logger.debug("Leaving from getDashboardTemplateChartPreview");
		return templateChart;
		}
		 catch (ApplicationException e) {
			 logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
				throw e;
			}
	}
	
	public TemplateChart getDashboardTemplateChart(TemplateChart templateChart, String empId, String emailId,Integer reportMetaDataId) throws ApplicationException {
			logger.debug("Entered into getDashboardTemplateChart");
			Map functionMapObj = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.MASTER_FUNCTIONS_MAP_CACHE_KEY);
			Map variableNamesObj=(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.VARIABLE_NAMES_MAP_CACHE_KEY);
			Map notesMapObj=(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.NOTES_MAP_CACHE_KEY);
			Map topLevelsLimitMapObj = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.TOP_LEVELS_LIMIT);
			Map benchMarkObj=(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.	MASTER_BENCH_MARKS_MAP_CACHE_KEY);
			Map codeBookMapObj =(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.CODEBOOK_ACTUAL_RECODE_VALUES_CACHE_KEY);
		    String isOverall = templateChart.getIsOverall();
			List templatechartCategoriesList  = templateChart.getTemplateChartCategories();
			String filter1=templateChart.getFilter1();
	        String filter2=templateChart.getFilter2();
	        String filter3=templateChart.getFilter3();
	        String filterValue1=templateChart.getFilterValues1();
	        String filterValue2=templateChart.getFilterValues2();
	        String filterValue3=templateChart.getFilterValues3();
	        String whereConstrain="";
			if (!ApplicationUtil.isEmptyOrNull(filter1) && !filter1.equals("-1") && !ApplicationUtil.isEmptyOrNull(filterValue1)){
				String str[] = filterValue1.split(",");
				int len = str.length;
				String str1="";
				for(int i=0;i<len;i++){
					if(!ApplicationUtil.isEmptyOrNull(str[i])){
						str1 = str1+"'"+str[i]+"'";
					}
					if(i+1<len){
						str1 = str1+",";
					}
				}
				filterValue1=str1;
				whereConstrain =" "+filter1+" in ("+filterValue1+") ";
			}
			if (!ApplicationUtil.isEmptyOrNull(filter2) && !filter2.equals("-1") && !ApplicationUtil.isEmptyOrNull(filterValue2)){
				String str[] = filterValue2.split(",");
				int len = str.length;
				String str1="";
				for(int i=0;i<len;i++){
					if(!ApplicationUtil.isEmptyOrNull(str[i])){
						str1 = str1+"'"+str[i]+"'";
					}
					if(i+1<len){
						str1 = str1+",";
					}
				}
				filterValue2=str1;
				whereConstrain = (whereConstrain.length()>0)?whereConstrain+" and ":whereConstrain;
				whereConstrain+=" "+filter2+" in ("+filterValue2+") ";
			}
			if (!ApplicationUtil.isEmptyOrNull(filter3) && !filter3.equals("-1") && !ApplicationUtil.isEmptyOrNull(filterValue3)){
				String str[] = filterValue3.split(",");
				int len = str.length;
				String str1="";
				for(int i=0;i<len;i++){
					if(!ApplicationUtil.isEmptyOrNull(str[i])){
						str1 = str1+"'"+str[i]+"'";
					}
					if(i+1<len){
						str1 = str1+",";
					}
				}
				filterValue3=str1;
				whereConstrain = (whereConstrain.length()>0)?whereConstrain+" and ":whereConstrain;
				whereConstrain+=" "+filter3+" in ("+filterValue3+") ";
			}
			if(whereConstrain.length()>0){
				whereConstrain = "and "+whereConstrain;
			}
			if(!ApplicationUtil.isEmptyOrNull(isOverall)
					&& isOverall.equals("Y")){
				empId=null;
			}
			createSummary(templateChart,empId);
			  for(int i=0;i<templatechartCategoriesList.size();i++){
            	TemplateChartCategory templateChartCategory =(TemplateChartCategory) templatechartCategoriesList.get(i);
            		
            		List templatechartInfoList  = templateChartCategory.getTemplateChartInfoList();
                	 for(int j=0;j<templatechartInfoList.size();j++){
                		TemplateChartInfo templateChartInfo=(TemplateChartInfo) templatechartInfoList.get(j);
                		String chartTitle = templateChartInfo.getChartTitle();
                		String chartTypeName = templateChartInfo.getChartTypeId();
                		String filterVariable = templateChartInfo.getFilter();
                		String xAxis = templateChartInfo.getxAxis();
                		String yAxis = templateChartInfo.getyAxis();
                		String weight= templateChartInfo.getWeightVariable();
                		Integer incl= templateChartInfo.getMeanIncl();
                		Integer excl= templateChartInfo.getMeanExcl();
                		Integer medianIncl= templateChartInfo.getMedianIncl();
                		Integer medianExcl= templateChartInfo.getMedianExcl();
                		Integer topBottomThreshold= templateChartInfo.getNumberOfCharts();
                		Integer funtionId = templateChartInfo.getFunctionId();
                		String myNote= templateChartInfo.getMyNote();
                		String mainVariable="";
                		String benchMarkId=templateChartInfo.getBenchMark();
                		String colorCode=templateChartInfo.getColorCode();
                		if(!ApplicationUtil.isEmptyOrNull(empId)){
                			String wnsCountryColumn = applicationDAO.getWnsCountryColumn(empId);
                			if(!ApplicationUtil.isEmptyOrNull(wnsCountryColumn)){
                				benchMarkId=wnsCountryColumn;
                			}
                		}
                		 String splitByVariable="";
                		 String note="";
                		if(xAxis!= null){
                			String s[] = xAxis.split(",");
                			int len = s.length;
                			for(int k=0;k<len;k++){
                				String id = s[k];
                				mainVariable =mainVariable+ (String)variableNamesObj.get(id.toLowerCase());
                				if(len==1){
	                				note= (String)notesMapObj.get(id);
	                				templateChartInfo.setMyNote(note);
                				}
                				if(k+1<len){
                					mainVariable= mainVariable+",";
                     	 		}
                			}
                		}
                		if(yAxis!= null){
                			String s[] = yAxis.split(",");
                			int len = s.length;
                			for(int k=0;k<len;k++){
                				String id = s[k];
                				String topLevelsLimit = (String)topLevelsLimitMapObj.get(id.toLowerCase());
                				if(!ApplicationUtil.isEmptyOrNull(topLevelsLimit) && ApplicationUtil.isEmptyOrNull(topBottomThreshold)){
                					note= (String)notesMapObj.get(id);
                					templateChartInfo.setMyNote(note);
                				}
                				splitByVariable =splitByVariable+ (String)variableNamesObj.get(id.toLowerCase());
                				
                				if(k+1<len){
                					splitByVariable= splitByVariable+",";
                     	 		}
                			}
                		}
                		String benchMark = "";
                		if(!ApplicationUtil.isEmptyOrNull(benchMarkId) && !benchMarkId.equals("OVER_ALL") && !benchMarkId.equals("IMMEDIATE_SUPERVISOR")){
                			benchMark = (String)variableNamesObj.get(benchMarkId.toLowerCase());
                		}else {
                			benchMark = (String)benchMarkObj.get(benchMarkId);
                		}
                		
                		String functionName= (String)functionMapObj.get(funtionId);
                		
                		if(!ApplicationUtil.isEmptyOrNull(empId) && functionName.equals("KEYDRIVERANALYSIS")){
                			String wnsCountryColumn = applicationDAO.getWnsCountryColumn(empId);
                			if(!ApplicationUtil.isEmptyOrNull(wnsCountryColumn)){
                				filterVariable=wnsCountryColumn;
                			}
                		}
                		String filter = "";
                		if(!ApplicationUtil.isEmptyOrNull(filterVariable)){
                			filter = (String)variableNamesObj.get(filterVariable.toLowerCase());
                		}
                		String tableName="";
                		String xAxisColumn="";
                		String yAxisColumn="";
                		String filterColumn="";
                		String chartType="";
                		String xAxisName="";
                		String yAxisName="";
                		String stack100Percent=null;
                		if(!ApplicationUtil.isEmptyOrNull(chartTypeName) && chartTypeName.equals("HUNDRED_PERCENT_STACKED_COLUMN")){
            				stack100Percent="1";
            				chartType="StackedColumn2D";
                		}else if(!ApplicationUtil.isEmptyOrNull(chartTypeName) && chartTypeName.equals("HUNDRED_PERCENT_STACKED_BAR")){
            				chartType="StackedBar2D";
            				stack100Percent="1";
                		}else{
                			chartType = chartTypeName;
                		}

                		if(functionName != null && functionName.equals("KEYDRIVERANALYSIS")){
                			chartType="Scatter";
                		}
                		
                		String[] valueColName= {"value"};

                		if(functionName != null && functionName.equals("AVERAGE")){
                			
                			tableName="chartaverage";
                			xAxisColumn="xAxis";
                			if(!ApplicationUtil.isEmptyOrNull(yAxis) && !yAxis.equals("-1")){
                				yAxisColumn="yAxis";
                				xAxisName=splitByVariable;
                			}
                			yAxisName="Average Rating";
                			
                			if(!ApplicationUtil.isEmptyOrNull(benchMarkId) && !benchMarkId.equals("-1")){
                				if(!ApplicationUtil.isEmptyOrNull(xAxis) && xAxis.contains(",")){
                                    yAxisColumn="yAxis";
                                }
                			}
                			if(!ApplicationUtil.isEmptyOrNull(filterVariable) && !filterVariable.equals("-1")){
                				filterColumn="filter";
                			}
                		}
                		else if(functionName != null && functionName.equals("FREQUENCY"))
                		{
                			tableName="chartfrequencies";
                			xAxisColumn="xAxis";
                			if(!ApplicationUtil.isEmptyOrNull(yAxis) && !yAxis.equals("-1")){
                				yAxisColumn="yAxis";
                				xAxisName=splitByVariable;
                				yAxisName=mainVariable;
                			}else if(!ApplicationUtil.isEmptyOrNull(xAxis) && xAxis.contains(",")){
                				yAxisColumn="yAxis";
                				yAxisName="Frequency";
                			}else{
                				yAxisName="Frequency";
                				xAxisName=mainVariable;
                			}
                			if(!ApplicationUtil.isEmptyOrNull(filterVariable) && !filterVariable.equals("-1")){
                				filterColumn="filter";
                			}
                		}
                		else if(functionName != null && functionName.equals("KEYDRIVERANALYSIS"))
                		{
                			tableName="chartkda";
                			xAxisColumn="xAxis";
                			if(!ApplicationUtil.isEmptyOrNull(yAxis) && !yAxis.equals("-1")){
                				yAxisColumn="yAxis";
                			}
                			if(!ApplicationUtil.isEmptyOrNull(filterVariable) && !filterVariable.equals("-1")){
                				filterColumn="filter";
                			}
                			yAxisName="Importance";
                			xAxisName="Relative Scores";
                		}
                		else if(functionName != null && functionName.equals("KAA"))
                		{
                			tableName="chartkaa";
                			xAxisColumn="xAxis";
                			if(!ApplicationUtil.isEmptyOrNull(yAxis) && !yAxis.equals("-1")){
                				yAxisColumn="yAxis";
                			}
                			if(!ApplicationUtil.isEmptyOrNull(filterVariable) && !filterVariable.equals("-1")){
                				filterColumn="filter";
                			}
                		}
                		else if(functionName != null && functionName.equals("VERBATIM")|| functionName.equals("THEMECLOUD"))
                		{
                			tableName="verbatim";
                		}else if(functionName != null && functionName.equals("SENTIMENT")){
                			tableName="chartsentiment";
                			xAxisColumn="name";
                			//yAxisColumn="series";
                			chartType="Pie2D";
                			if(!ApplicationUtil.isEmptyOrNull(yAxis) && !yAxis.equals("-1")){
                				filterColumn="segment";
                				filterVariable = yAxis;
                			}
                			
                		} else if(functionName != null && functionName.equals("CATEGORICAL_ANALYSIS") && !ApplicationUtil.isEmptyOrNull(chartType) && !chartType.equals("Wordcloud")){
                			tableName="wordcloud";
                			xAxisColumn="word";
                			if(!ApplicationUtil.isEmptyOrNull(yAxis) && !yAxis.equals("-1")){
                				filterColumn="segmentName";
                				filterVariable = yAxis;
                			}
                			String[] valueColName1= {"count"};
                			valueColName=valueColName1;
                			
                		}
                		
                		List filterValuesDropDownVos = null;
                		List filterValues =null;
                		if (!ApplicationUtil.isEmptyOrNull(filterColumn)){
                			Map filterColumnCodeBookMapObj = (Map)codeBookMapObj.get(filterVariable);
                			filterValuesDropDownVos = new ArrayList();
                			System.out.println(templateChartInfo.getId()+"----->"+empId);
                			filterValues = applicationDAO.getFilterValues(templateChartInfo.getId()+"",empId,tableName, filterColumn,reportMetaDataId);
                			if(filterValues != null){
                				int len = filterValues.size();
                				for(int k=0;k<len;k++){
                					String str = (String)filterValues.get(k);
                					if(!ApplicationUtil.isEmptyOrNull(str)){
	                					DropDownDisplayVo dropDownDisplayVo = new DropDownDisplayVo();
	                					String recodeVal = str;
	                					if(filterColumnCodeBookMapObj != null){
	                						recodeVal = (String)filterColumnCodeBookMapObj.get(str);
	                						if(ApplicationUtil.isEmptyOrNull(recodeVal)){
	                							recodeVal = str;
	                						}
	                						
	                					}
	                					
	                					dropDownDisplayVo.setDisplayName(recodeVal);
	                					dropDownDisplayVo.setValue(str);
	                					filterValuesDropDownVos.add(dropDownDisplayVo);
                					}
                				}
                			}
						}
                		String str=null;
                		Map tableViewMapObj;
                		Map kdaTableViewMapObj;
                		SingleMultiSeriesChartValueObject singleMultiSeriesChartValueObject = new SingleMultiSeriesChartValueObject();
                		singleMultiSeriesChartValueObject.setFilterColumn(filterColumn);
                		singleMultiSeriesChartValueObject.setFilterValues(filterValuesDropDownVos);
                		singleMultiSeriesChartValueObject.setTemplateChartInfoId(templateChartInfo.getId()+"");
                		singleMultiSeriesChartValueObject.setMainVariable(mainVariable);
                		singleMultiSeriesChartValueObject.setSplitByVariable(splitByVariable);
                		singleMultiSeriesChartValueObject.setFilter(filter);
                		singleMultiSeriesChartValueObject.setBenchMark(benchMark);
                		singleMultiSeriesChartValueObject.setFunctionName(functionName);
                		singleMultiSeriesChartValueObject.setChartType(chartType);
                		singleMultiSeriesChartValueObject.setxAxisName(xAxisName);
                		singleMultiSeriesChartValueObject.setyAxisName(yAxisName);
                		if(!ApplicationUtil.isEmptyOrNull(benchMarkId) && !benchMarkId.equals("-1")){
                			singleMultiSeriesChartValueObject.setBenchMarkVariable(benchMarkId);
                		}
                		if(reportMetaDataId != null){
                			singleMultiSeriesChartValueObject.setReportMetaDataId(reportMetaDataId.toString());
                		}else{
                			singleMultiSeriesChartValueObject.setReportMetaDataId(null);
                		}
                		
                		if(functionName != null && functionName.equals("AVERAGE")){
                			singleMultiSeriesChartValueObject.setyAxisMaxValue("5");
                		}
                		if(functionName != null && (functionName.equals("FREQUENCY") || functionName.equals("AVERAGE"))){
                			
                			//changing the code here
                			tableViewMapObj=applicationDAO.getChartXmlFromDatabase(templateChartInfo.getId()+"",empId, tableName, xAxisColumn,yAxisColumn,valueColName,false,false,filterColumn,null,xAxis,yAxis,"false",false,funtionId,topBottomThreshold,null,whereConstrain,reportMetaDataId,benchMarkId,colorCode,chartType);
                			singleMultiSeriesChartValueObject.setCategoryName(templateChartCategory.getCategoryName());
                			List tableViewList = (List) tableViewMapObj.get("tableViewList");
                			String referenceGroupValues = (String) tableViewMapObj.get("referenceGroupValues");
                			StringBuffer tableStr= new StringBuffer();
                			try {
                				tableStr.append("<table class=\"table table-striped table-bordered table-hover\" id=\"dataTables-example\">");
                				tableStr.append("<tbody>");	
                				if(!ApplicationUtil.isEmptyOrNull(yAxis) && !yAxis.equals("-1")){
                					if(tableViewList!=null && tableViewList.size()>0){
    									List trList = new ArrayList();
    									for(int j1=0;j1<tableViewList.size();j1++) {
    										List rowDataList = (List)tableViewList.get(j1);
    										if(rowDataList != null && rowDataList.size()>0){
    											int categoryListSize = rowDataList.size();
    											for(int k=0;k<categoryListSize;k++){
    												if(j1==0){
    														String headerStr1 ="";
    														headerStr1="<tr class=\"odd gradeX\"><td style=\"text-align: left;\">"+rowDataList.get(k)+"</td>";
    														trList.add(headerStr1);
    													
    												}else{
    													String dataRow = (String)trList.get(k);
    													if(ApplicationUtil.isEmptyOrNull(dataRow)){
    														dataRow="";
    													}
    													dataRow +="<td style=\"text-align: left;\">"+rowDataList.get(k)+"</td>";
    													trList.set(k,dataRow);
    												}
    												
    											}
    										}
    									}
    									if(trList.size()>0){
    										int size = trList.size();
    										for(int k=0;k<size;k++){
    											String trStr = (String)trList.get(k);
    											tableStr.append(trStr);
    											tableStr.append("</tr>");
    										}
    									}
    									tableStr.append("</tbody>");
    									tableStr.append("</table>");
    									}
    									else{
    										tableStr.append("<table style=\"position: absolute;right: 50%; top:50%;\"");
    										tableStr.append("<tr>");
    										tableStr.append("<td>");
    										tableStr.append("No data to display");
    										tableStr.append("</td>");
    										tableStr.append("</tr>");
    										tableStr.append("</table>");
    									    }
                					
                				}else {
                					if(tableViewList!=null && tableViewList.size()>0){
                						List trList = new ArrayList();
                						for(int l=0;l<tableViewList.size();l++) {
                							List rowDataList = (List)tableViewList.get(l);
                							if(rowDataList != null && rowDataList.size()>0){
                								int categoryListSize = rowDataList.size();
                								for(int k=0;k<categoryListSize;k++){
                									if(l==0){
                											String headerStr1 ="";
                											headerStr1="<tr class=\"odd gradeX\"><td style=\"text-align: left;\">"+rowDataList.get(k)+"</td>";
                											trList.add(headerStr1);
                										
                									}else{
                										String dataRow = (String)trList.get(k);
                										if(ApplicationUtil.isEmptyOrNull(dataRow)){
                											dataRow="";
                										}
                										dataRow +="<td style=\"text-align: left;\">"+rowDataList.get(k)+"</td>";
                										trList.set(k,dataRow);
                									}
                									
                								}
                							}
                						}
                						if(trList.size()>0){
                							int size = trList.size();
                							for(int k=0;k<size;k++){
                								String trStr = (String)trList.get(k);
                								tableStr.append(trStr);
                								tableStr.append("</tr>");
                							}
                						}
                						tableStr.append("</tbody>");
                						tableStr.append("</table>");
                							}
                							else{
                								tableStr.append("<table style=\"position: absolute;right: 50%; top:50%;\"");
                								tableStr.append("<tr>");
                								tableStr.append("<td>");
                								tableStr.append("No data to display");
                								tableStr.append("</td>");
                								tableStr.append("</tr>");
                								tableStr.append("</table>");
                							    }
                				}
							} catch (Exception e) {
								// TODO: handle exception
							}
                				
								
                			singleMultiSeriesChartValueObject.setTableView(tableStr.toString());
                			singleMultiSeriesChartValueObject.setTableViewList(tableViewList);
							singleMultiSeriesChartValueObject.setXml((String) (tableViewMapObj.get("xmlStr")));
                    		singleMultiSeriesChartValueObject.setTableName(tableName);
                    		singleMultiSeriesChartValueObject.setxAxis(xAxisColumn);
                    		singleMultiSeriesChartValueObject.setyAxis(yAxisColumn);
                    		singleMultiSeriesChartValueObject.setxAxisColumnName(xAxis);
                    		singleMultiSeriesChartValueObject.setyAxisColumnName(yAxis);
                    		singleMultiSeriesChartValueObject.setWidth("900");
                    		singleMultiSeriesChartValueObject.setHeight("375");
                    		singleMultiSeriesChartValueObject.setDivLineAlpha("10");
                    		singleMultiSeriesChartValueObject.setFunctionId(funtionId);
                    		if(!ApplicationUtil.isEmptyOrNull(stack100Percent)){
                    			singleMultiSeriesChartValueObject.setStack100Percent(stack100Percent);
                    		}
                    		singleMultiSeriesChartValueObject.setCategoryField(xAxisColumn);
                    		singleMultiSeriesChartValueObject.setReferenceGroupValues(referenceGroupValues);
                		}else if(functionName != null && (functionName.equals("WORDCLOUD") || functionName.equals("CATEGORICAL_ANALYSIS"))){
                			
                			if(!ApplicationUtil.isEmptyOrNull(chartType) && !chartType.equals("Wordcloud") ){
                				tableViewMapObj=applicationDAO.getChartXmlFromDatabase(templateChartInfo.getId()+"",empId, tableName, xAxisColumn,yAxisColumn,valueColName,false,false,filterColumn,null,xAxis,yAxis,"false",false,funtionId,null,null,whereConstrain,reportMetaDataId,null,null,chartType);
                    			List tableViewList = (List) tableViewMapObj.get("tableViewList");
                    			singleMultiSeriesChartValueObject.setTableName(tableName);
                        		singleMultiSeriesChartValueObject.setxAxis(xAxisColumn);
                        		singleMultiSeriesChartValueObject.setyAxis(yAxisColumn);
                        		singleMultiSeriesChartValueObject.setxAxisColumnName(xAxis);
                        		singleMultiSeriesChartValueObject.setyAxisColumnName(yAxis);
                        		singleMultiSeriesChartValueObject.setFilter(splitByVariable);
                    			singleMultiSeriesChartValueObject.setXml((String)(tableViewMapObj.get("xmlStr")));
                    			singleMultiSeriesChartValueObject.setTableViewList(tableViewList);
                        		singleMultiSeriesChartValueObject.setWidth("900");
                        		singleMultiSeriesChartValueObject.setHeight("375");
                        		singleMultiSeriesChartValueObject.setFunctionId(funtionId);
                				
                			} else{
                				List segmentDropDownList = new ArrayList();
                    			List wordCloudList =  applicationDAO.getWordCloud(templateChartInfo.getId(), empId,null,reportMetaDataId);
                				if(wordCloudList != null && wordCloudList.size()>0){
                					StringBuffer buffer = new StringBuffer();
                					for(int ii=0;ii<wordCloudList.size();ii++){
            							WordCloud  wordCloud = (WordCloud) wordCloudList.get(ii);
            							buffer.append(wordCloud.getNormalizedCount()+"#$#"+wordCloud.getCount()+"#$#"+wordCloud.getWord()+"#@#");
            						}
                					DropDownDisplayVo displayVo = new DropDownDisplayVo();
            						displayVo.setDisplayName("Select");
            						displayVo.setValue(buffer.toString());
            						segmentDropDownList.add(displayVo);
                					singleMultiSeriesChartValueObject.setWordCloudStr(buffer.toString());
                					singleMultiSeriesChartValueObject.setWordCloudList(wordCloudList);
                    			}
                    			if(!ApplicationUtil.isEmptyOrNull(yAxis) 
                    					&& !yAxis.equals("-1")){
                    						List wordCloudSegmentList =  applicationDAO.getWordCloudSegments(templateChartInfo.getId(), empId,reportMetaDataId);
    		                				if(wordCloudSegmentList != null && wordCloudSegmentList.size()>0){
    		                					
    		                					for(int k=0;k<wordCloudSegmentList.size();k++){
    		                						String segmentName = (String)wordCloudSegmentList.get(k);
    		                						
    		                						DropDownDisplayVo displayVo = new DropDownDisplayVo();
    		                						displayVo.setDisplayName(segmentName);
    		                						 wordCloudList =  applicationDAO.getWordCloud(templateChartInfo.getId(), empId,segmentName,reportMetaDataId);
    			                					StringBuffer buffer = new StringBuffer();
    			                					if(wordCloudList != null){
    			                						for(int ii=0;ii<wordCloudList.size();ii++){
    			                							WordCloud  wordCloud = (WordCloud) wordCloudList.get(ii);
    			                							buffer.append(wordCloud.getNormalizedCount()+"#$#"+wordCloud.getCount()+"#$#"+wordCloud.getWord()+"#@#");
    			                						}
    			                					}
    		                						displayVo.setValue(buffer.toString());
    		                						segmentDropDownList.add(displayVo);
    		                					}
    		                					
    		                    				singleMultiSeriesChartValueObject.setSegmentValues(segmentDropDownList);
    		                    			}
                    			}
                				
                			}
                			
                		}else if(functionName != null && functionName.equals("RAW COMMENTS")){
                			Map rawCommentsList =  applicationDAO.getRawComments(templateChartInfo.getId());
                			List categoryCommentList =(List)rawCommentsList.get("categoryCommentList");
                			List categoryList =(List)rawCommentsList.get("categoryList");
                			singleMultiSeriesChartValueObject.setCategoryCommentList(categoryCommentList);
                			singleMultiSeriesChartValueObject.setCategoryList(categoryList);
                				
                		}else if(functionName != null && (functionName.equals("SENTIMENT"))){
                			List sentimentPercentageList =  applicationDAO.getSentimentPercentage(templateChartInfo.getId(), empId,null,reportMetaDataId);
                			if(sentimentPercentageList != null && sentimentPercentageList.size()>0){
                				ChartSentimentPercentage chartSentimentPercentage = (ChartSentimentPercentage)sentimentPercentageList.get(0);
                				singleMultiSeriesChartValueObject.setPositiveSentiment(chartSentimentPercentage.getPositive());
                				singleMultiSeriesChartValueObject.setNegativeSentiment(chartSentimentPercentage.getNegative());
                			}
                			
                			//changing code here
                			Integer noOfResponse = templateChart.getNoOfResponse();
                			tableViewMapObj=applicationDAO.getChartXmlFromDatabase(templateChartInfo.getId()+"",empId, tableName, xAxisColumn,yAxisColumn,valueColName,false,false,filterColumn,null,xAxis,yAxis,"false",true,funtionId,null,noOfResponse,whereConstrain,reportMetaDataId,null,null,chartType);
                			List tableViewList = (List) tableViewMapObj.get("tableViewList");
                			singleMultiSeriesChartValueObject.setTableName(tableName);
                    		singleMultiSeriesChartValueObject.setxAxis(xAxisColumn);
                    		singleMultiSeriesChartValueObject.setyAxis(yAxisColumn);
                    		singleMultiSeriesChartValueObject.setxAxisColumnName(xAxis);
                    		singleMultiSeriesChartValueObject.setyAxisColumnName(yAxis);
                    		singleMultiSeriesChartValueObject.setFilter(splitByVariable);
                			singleMultiSeriesChartValueObject.setXml((String)(tableViewMapObj.get("xmlStr")));
                			singleMultiSeriesChartValueObject.setTableViewList(tableViewList);
                    		singleMultiSeriesChartValueObject.setWidth("900");
                    		singleMultiSeriesChartValueObject.setHeight("375");
                    		singleMultiSeriesChartValueObject.setFunctionId(funtionId);
                		}else if (functionName != null && (functionName.equals("KEYDRIVERANALYSIS"))){
                			String referenceGroupValues ="";
                				if(isOverall.equals("Y")){
	                				List kdaTableList;
	                				double performanceTrendLine;
	                        		double importanceTrendLine;
	                				StringBuffer kdaTableStr= new StringBuffer();
	                				if(filterValues != null && filterValues.size()>0){
	                					kdaTableViewMapObj=applicationDAO.getPlotChartXmlFromDatabase(templateChartInfo.getId()+"",empId, tableName, xAxisColumn,yAxisColumn,valueColName,filterColumn,(String)filterValues.get(0),xAxis,yAxis,false,whereConstrain,filterVariable,reportMetaDataId);
	                					str= (String) kdaTableViewMapObj.get("kdaXmlStr");
	                					importanceTrendLine= (double) kdaTableViewMapObj.get("ImportanceTrendLine");
	                					performanceTrendLine= (double) kdaTableViewMapObj.get("PerformanceTrendLine");
	                					referenceGroupValues= (String) kdaTableViewMapObj.get("referenceGroupValues");
	                					
	                					kdaTableList = (List) kdaTableViewMapObj.get("kdaTableList");
	                					kdaTableStr.append("<table class=\"table table-striped table-bordered table-hover\" id=\"dataTables-example\">");
	                					kdaTableStr.append("<tbody>");	
	                					if(kdaTableList!=null && kdaTableList.size()>1){
	                				    	List trList = new ArrayList();
	                				    	for(int j1=0;j1<kdaTableList.size();j1++) {
	                				    		List rowDataList = (List) kdaTableList.get(j1);
	                				    		if(rowDataList != null && rowDataList.size()>0){
	                				    			int categoryListSize = rowDataList.size();
	                									if(j1==0){
	                										String headerStr="<tr class=\"odd gradeX\">";
	                										for(int k=0;k<categoryListSize;k++){
	                											headerStr+="<th style=\"text-align: center;\">"+rowDataList.get(k)+"</th>";
	                																		
	                										}
	                										headerStr+="</tr>";
	                										trList.add(headerStr);
	
	                									}else {
	                										String dataStr="<tr class=\"odd gradeX\">";
	                										for(int k=0;k<categoryListSize;k++){
	                											dataStr+="<td>"+rowDataList.get(k)+"</td>";
	                																		
	                										}
	                										dataStr+="</tr>";
	                										trList.add(dataStr);
	                									}
	                								}
	
	                				    	   }	if(trList.size()>0){
	                				    	 
	                				    					int size = trList.size();
	                				    					for(int k=0;k<size;k++){
	                				    						String trStr = (String)trList.get(k);
	                				    						kdaTableStr.append(trStr);
	                				    					}
	                				    			}
	                				    	   kdaTableStr.append("</tbody>");
	                				    	   kdaTableStr.append("</table>");
	                						}else {
	                							kdaTableStr.append("<table style=\"position: absolute;right: 50%; top:50%;\"");
	                							kdaTableStr.append("<tr>");
	                							kdaTableStr.append("<td>");
	                							kdaTableStr.append("No data to display");
	                							kdaTableStr.append("</td>");
	                							kdaTableStr.append("</tr>");
	            								kdaTableStr.append("</table>");
	                						}
	                				}else{
	                					kdaTableViewMapObj=applicationDAO.getPlotChartXmlFromDatabase(templateChartInfo.getId()+"",empId, tableName, xAxisColumn,yAxisColumn,valueColName,filterColumn,null,xAxis,yAxis,false,whereConstrain,filterVariable,reportMetaDataId);
	                					str= (String) kdaTableViewMapObj.get("kdaXmlStr");
	                					importanceTrendLine= (double) kdaTableViewMapObj.get("ImportanceTrendLine");
	                					performanceTrendLine= (double) kdaTableViewMapObj.get("PerformanceTrendLine");
	                					referenceGroupValues= (String) kdaTableViewMapObj.get("referenceGroupValues");
	                					
	                					kdaTableList = (List) kdaTableViewMapObj.get("kdaTableList");
	                					kdaTableStr.append("<table class=\"table table-striped table-bordered table-hover\" id=\"dataTables-example\">");
	                					kdaTableStr.append("<tbody>");	
	                					if(kdaTableList!=null && kdaTableList.size()>1){
	                				    	List trList = new ArrayList();
	                				    	for(int j1=0;j1<kdaTableList.size();j1++) {
	                				    		List rowDataList = (List) kdaTableList.get(j1);
	                				    		if(rowDataList != null && rowDataList.size()>0){
	                				    			int categoryListSize = rowDataList.size();
	                									if(j1==0){
	                										String headerStr="<tr class=\"odd gradeX\">";
	                										for(int k=0;k<categoryListSize;k++){
	                											headerStr+="<th style=\"text-align: center;\">"+rowDataList.get(k)+"</th>";
	                																		
	                										}
	                										headerStr+="</tr>";
	                										trList.add(headerStr);
	
	                									}else {
	                										String dataStr="<tr class=\"odd gradeX\">";
	                										for(int k=0;k<categoryListSize;k++){
	                											dataStr+="<td>"+rowDataList.get(k)+"</td>";
	                																		
	                										}
	                										dataStr+="</tr>";
	                										trList.add(dataStr);
	                									}
	                								}
	
	                				    	   }	if(trList.size()>0){
	                				    	 
	                				    					int size = trList.size();
	                				    					for(int k=0;k<size;k++){
	                				    						String trStr = (String)trList.get(k);
	                				    						kdaTableStr.append(trStr);
	                				    					}
	                				    			}
	                				    	   kdaTableStr.append("</tbody>");
	                				    	   kdaTableStr.append("</table>");
	                						}else {
	                							kdaTableStr.append("<table style=\"position: absolute;right: 50%; top:50%;\"");
	                							kdaTableStr.append("<tr>");
	                							kdaTableStr.append("<td>");
	                							kdaTableStr.append("No data to display");
	                							kdaTableStr.append("</td>");
	                							kdaTableStr.append("</tr>");
	            								kdaTableStr.append("</table>");
	                						}
	                				}
	                				if(str != null){
	                					String strArry[] = str.split("@");
	                					singleMultiSeriesChartValueObject.setXml(strArry[0]);
	                					singleMultiSeriesChartValueObject.setTableView(kdaTableStr.toString());
	                					singleMultiSeriesChartValueObject.setKdaTableList(kdaTableList);
	                					singleMultiSeriesChartValueObject.setxAxisMinValue(strArry[1]);
	                					singleMultiSeriesChartValueObject.setxAxisMaxValue(strArry[2]);
	                					singleMultiSeriesChartValueObject.setyAxisMinValue(strArry[3]);
	                					singleMultiSeriesChartValueObject.setPerformanceTrendLine(performanceTrendLine);
	                					singleMultiSeriesChartValueObject.setImportanceTrendLine(importanceTrendLine);
	                					singleMultiSeriesChartValueObject.setReferenceGroupValues(referenceGroupValues);
	                				}
	                    			singleMultiSeriesChartValueObject.setCategoryName(templateChartCategory.getCategoryName());
	                        		singleMultiSeriesChartValueObject.setTableName(tableName);
	                        		singleMultiSeriesChartValueObject.setKdaTableList(kdaTableList);
	                        		singleMultiSeriesChartValueObject.setxAxis(xAxisColumn);
	                        		singleMultiSeriesChartValueObject.setyAxis(yAxisColumn);
	                        		singleMultiSeriesChartValueObject.setxAxisColumnName(xAxis);
	                        		singleMultiSeriesChartValueObject.setyAxisColumnName(yAxis);
	                        		singleMultiSeriesChartValueObject.setDivLineAlpha("0");
	                        		singleMultiSeriesChartValueObject.setWidth("900");
	                        		singleMultiSeriesChartValueObject.setHeight("375");
                				}else{
                					
                					Map map = applicationDAO.getKDAManagerReportData(empId,templateChartInfo.getId(),filterVariable);
                					
                					List highStrengthList = (List)map.get("highStrengthList");
                					List highNeutralList= (List)map.get("highNeutralList");
                					List highWeaknessList =(List)map.get("highWeaknessList");
                					List mediumStrengthList = (List)map.get("mediumStrengthList");
                					List mediumNeutralList= (List)map.get("mediumNeutralList");
                					List mediumWeaknessList =(List)map.get("mediumWeaknessList");
                					List lowStrengthList = (List)map.get("lowStrengthList");
                					List lowNeutralList= (List)map.get("lowNeutralList");
                					List lowWeaknessList =(List)map.get("lowWeaknessList");
                					String segmentName = (String) map.get("segmentName");
                					referenceGroupValues = (String) map.get("referenceGroupValues");
                					
                					singleMultiSeriesChartValueObject.setHighStrengthList(highStrengthList);
                					singleMultiSeriesChartValueObject.setHighNeutralList(highNeutralList);
                					singleMultiSeriesChartValueObject.setHighWeaknessList(highWeaknessList);
                					
                					singleMultiSeriesChartValueObject.setMediumStrengthList(mediumStrengthList);
                					singleMultiSeriesChartValueObject.setMediumNeutralList(mediumNeutralList);
                					singleMultiSeriesChartValueObject.setMediumWeaknessList(mediumWeaknessList);
                					
                					singleMultiSeriesChartValueObject.setLowStrengthList(lowStrengthList);
                					singleMultiSeriesChartValueObject.setLowNeutralList(lowNeutralList);
                					singleMultiSeriesChartValueObject.setLowWeaknessList(lowWeaknessList);
                					singleMultiSeriesChartValueObject.setSegmentName(segmentName);
                					singleMultiSeriesChartValueObject.setReferenceGroupValues(referenceGroupValues);
                					
                				}
                		}
                		
                		else if (functionName != null && (functionName.equals("BANNER TABLE"))){
                			System.out.println("Banner Table");
                			if(empId != null){
	                				Map bannerTableMapObj = applicationDAO.getBannerTable(empId, templateChartInfo.getId(),xAxis,yAxis,weight,incl,excl,medianIncl,medianExcl,reportMetaDataId);
		           					List bannerTableHeaderList =(List)bannerTableMapObj.get("bannerTableHeaderList");
		           					List bannerTableDataList =(List)bannerTableMapObj.get("bannerTableDataList");
		           					List bannerTableWeightList =(List)bannerTableMapObj.get("bannerTableWeightList");
		           					List bannerTablePercentageList =(List)bannerTableMapObj.get("bannerTablePercentageList");
		           					List bannerTablePercentageWeightList =(List)bannerTableMapObj.get("bannerTablePercentageWeightList");
		           					singleMultiSeriesChartValueObject.setBannerTableHeaderList(bannerTableHeaderList);
		           					singleMultiSeriesChartValueObject.setBannerTableDataList(bannerTableDataList);
		           					singleMultiSeriesChartValueObject.setBannerTableWeightList(bannerTableWeightList);
		           					singleMultiSeriesChartValueObject.setBannerTablePercentageList(bannerTablePercentageList);
		           					singleMultiSeriesChartValueObject.setBannerTablePercentageWeightList(bannerTablePercentageWeightList);
                			}
                			else{
                    				Map bannerTableMapObj = applicationDAO.getBannerTable(empId, templateChartInfo.getId(),xAxis,yAxis,weight,incl,excl,medianIncl,medianExcl,reportMetaDataId);
    	           					List bannerTableHeaderList =(List)bannerTableMapObj.get("bannerTableHeaderList");
    	           					List bannerTableDataList =(List)bannerTableMapObj.get("bannerTableDataList");
    	           					List bannerTableWeightList =(List)bannerTableMapObj.get("bannerTableWeightList");
    	           					List bannerTablePercentageList =(List)bannerTableMapObj.get("bannerTablePercentageList");
    	           					List bannerTablePercentageWeightList =(List)bannerTableMapObj.get("bannerTablePercentageWeightList");
    	           					singleMultiSeriesChartValueObject.setBannerTableHeaderList(bannerTableHeaderList);
    	           					singleMultiSeriesChartValueObject.setBannerTableDataList(bannerTableDataList);
    	           					singleMultiSeriesChartValueObject.setBannerTableWeightList(bannerTableWeightList);
    	           					singleMultiSeriesChartValueObject.setBannerTablePercentageList(bannerTablePercentageList);
    	           					singleMultiSeriesChartValueObject.setBannerTablePercentageWeightList(bannerTablePercentageWeightList);
                			}
                			
            		}
                		templateChartInfo.setSingleMultiSeriesChartValueObject(singleMultiSeriesChartValueObject);
                	 }
			  }
			  
			  try {
				  if(empId != null){
					 List teamReportList = applicationDAO.getTeamReport(empId,templateChart);
					 templateChart.setTeamReport(teamReportList);
				  }
			  }catch (Exception e) {
				 e.printStackTrace();
					ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "getTemplateChart", 
							ApplicationErrorCodes.APP_EC_35, ApplicationConstants.EXCEPTION, e);
					throw applicationException;
				}
			  
			  try {
				  if(empId != null){
					  List trendReportList = applicationDAO.getTrendReport(empId,templateChart);
					 templateChart.setTrendReportList(trendReportList);
				  }
			  }catch (Exception e) {
				 e.printStackTrace();
					ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "getTemplateChart", 
							ApplicationErrorCodes.APP_EC_35, ApplicationConstants.EXCEPTION, e);
					throw applicationException;
				}
			  logger.debug("Leaving from getDashboardTemplateChart");
		return templateChart;
	}
	
	public List getDateForSelectedVariable(String selectedVariableId) throws ApplicationException
	{
		try{
	    return applicationDAO.getDateForSelectedVariable(selectedVariableId);
		}
		 catch (ApplicationException e) {
			 logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
				throw e;
			}
	}
	
	public List getSelectedDistributionList(String selectedId) throws ApplicationException
	{
		try{
	    return applicationDAO.getSelectedDistributionList(selectedId);
		}
		 catch (ApplicationException e) {
			 logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
				throw e;
			}
	}
	
	
	public Map getChartXmlFromDatabase(String templateChartInfoId,String empId,String tableName,String xAxisColumn,String yAxisColumn,String[] valueColName
			,boolean isFromCombinationChart,boolean isRootDatasetRequired, String filterColumnName, 
			String filterValue,String xAxisColumnName,String yAxisColumnName,String percentage,
			boolean isFromSentiment,Integer funtionId,Integer topBottomThreshold,Integer noOfResponse,
			String whereConstrain,Integer reportMetaDataId,String benchMarkVariableStr,String colorCode,String chartType) throws ApplicationException{
		Map tableViewMapObj=applicationDAO.getChartXmlFromDatabase(templateChartInfoId,empId, tableName, xAxisColumn,yAxisColumn,valueColName,isFromCombinationChart,isRootDatasetRequired,filterColumnName,filterValue,xAxisColumnName,yAxisColumnName,percentage,isFromSentiment,funtionId, topBottomThreshold,noOfResponse,whereConstrain,reportMetaDataId,benchMarkVariableStr,colorCode,chartType);
		logger.debug("Leaving from getChartXmlFromDatabase");
		return tableViewMapObj;
	}
	
	public Map getPlotChartXmlFromDatabase(String templateChartInfoId,String empId,String tableName,String xAxisColumn,String yAxisColumn,String[] valueColName
			, String filterColumnName, String filterValue,String xAxisColumnName,String yAxisColumnName,String whereConstrain,String filterVariable,Integer reportMetaDataId) throws ApplicationException{
		logger.debug("Entered into getPlotChartXmlFromDatabase");
		
		Map kdaTableViewMapObj=applicationDAO.getPlotChartXmlFromDatabase(templateChartInfoId,empId, tableName, xAxisColumn,yAxisColumn,valueColName,filterColumnName,filterValue,xAxisColumnName,yAxisColumnName,true,whereConstrain,filterVariable,reportMetaDataId);
		//String str=(String)kdaTableViewMapObj.get("kdaXmlStr");
		logger.debug("Leaving from getPlotChartXmlFromDatabase");
		return kdaTableViewMapObj;
	}
	
	public List getMailIdList(String masterDistIdList) throws ApplicationException {
		try{
		 return applicationDAO.getMailIdList(masterDistIdList);
		}
		 catch (ApplicationException e) {
			 logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
				throw e;
			}
	}
	public void createSummary(TemplateChart templateChart,String empId) throws ApplicationException{
		System.out.println("--------------------------------createSummary--------------------------");
		logger.debug("Entered into createSummary");
		Map codebookValuesMap = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.CODEBOOK_ACTUAL_RECODE_VALUES_CACHE_KEY);
		Map variableNamesMap = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.VARIABLE_NAMES_MAP_CACHE_KEY);
		String filter1 = templateChart.getFilter1();
		String[] filter1Values = null;
		String filter1VariableName = (String)variableNamesMap.get(filter1.toLowerCase());
		Map filter1VariableCodebook = (Map)codebookValuesMap.get(filter1);
			String temp = templateChart.getFilterValues1();
			if(temp!=null && temp.length()>0){
				filter1Values= temp.split(",");	
			}
			 
			if(filter1VariableCodebook!=null){
			  for(int i=0;(filter1Values!=null && i<filter1Values.length);i++){	
					String recoded = (String)filter1VariableCodebook.get(filter1Values[i]);
					if(recoded!=null)
					{
						filter1Values[i]=recoded;
					}
			  }
			
			 
		}
		

		String filter2 = templateChart.getFilter2();
		String[] filter2Values = null;
		String filter2VariableName = (String)variableNamesMap.get(filter2.toLowerCase());
		Map filter2VariableCodebook = (Map)codebookValuesMap.get(filter2);
		temp = templateChart.getFilterValues2();
		if(temp!=null && temp.length()>0){
			filter2Values= temp.split(",");	
		}
		
		if(filter2VariableCodebook!=null){
			  for(int i=0;i<filter2Values.length;i++){
					String recoded = (String)filter2VariableCodebook.get(filter2Values[i]);
					if(recoded!=null)
					{
						filter2Values[i]=recoded;
					}
			  }
		}
	
	
	
		String filter3 = templateChart.getFilter3();
		String[] filter3Values = null;
		String filter3VariableName = (String)variableNamesMap.get(filter3.toLowerCase());
		Map filter3VariableCodebook = (Map)codebookValuesMap.get(filter3);
		temp = templateChart.getFilterValues3();
		if(temp!=null && temp.length()>0){
			filter3Values= temp.split(",");	
		}
		if(filter3VariableCodebook!=null){
			  for(int i=0;i<filter3Values.length;i++){
					String recoded = (String)filter3VariableCodebook.get(filter3Values[i]);
					if(recoded!=null)
					{
						filter3Values[i]=recoded;
					}
			  }
		}

		templateChart.setCodeBookappliedFilter1Variable(filter1VariableName);
		templateChart.setCodeBookappliedFilter2Variable(filter2VariableName);
		templateChart.setCodeBookappliedFilter3Variable(filter3VariableName);
		templateChart.setCodeBookappliedFilter1Value((filter1Values!=null)?Arrays.toString(filter1Values):"");
		templateChart.setCodeBookappliedFilter2Value((filter2Values!=null)?Arrays.toString(filter2Values):"");
		templateChart.setCodeBookappliedFilter3Value((filter3Values!=null)?Arrays.toString(filter3Values):"");
		
		System.out.println(templateChart.toString());
		applicationDAO.getNumberofResponse(templateChart, empId);
		logger.debug("Leaving from createSummary");
		System.out.println("--------------------------------end createSummary--------------------------");
}
	
	public WorkspaceBean getDashboardRunReports(WorkspaceBean workspaceBean, String empId) throws ApplicationException {
		logger.debug("Entered into getDashboardTemplateChart");
		Map functionMapObj = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.MASTER_FUNCTIONS_MAP_CACHE_KEY);
		Map variableNamesObj=(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.VARIABLE_NAMES_MAP_CACHE_KEY);
		Map benchMarkObj=(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.	MASTER_BENCH_MARKS_MAP_CACHE_KEY);
		Map codeBookMapObj =(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.CODEBOOK_ACTUAL_RECODE_VALUES_CACHE_KEY);
	    
					empId=null;
					String chartTitle= workspaceBean.getChartTitle();
            		String chartTypeName = workspaceBean.getChartTypeId();
            		String filterVariable = workspaceBean.getFilter();
            		String xAxis = workspaceBean.getxAxis();
            		String yAxis = workspaceBean.getyAxis();
            		String weight= workspaceBean.getWeightVariable();
            		String benchMarkId = workspaceBean.getBenchMark();
            		Integer funtionId = workspaceBean.getFunctionId();
            		Integer meanIncl = workspaceBean.getMeanIncl();
            		Integer meanExcl = workspaceBean.getMeanExcl();
            		Integer medianIncl = workspaceBean.getMedianIncl();
            		Integer medianExcl = workspaceBean.getMedianExcl();
            		Integer topBottomThreshold = workspaceBean.getNumberOfCharts();
            		String colorCode = workspaceBean.getColorCode();
            		
            		workspaceBean.setChartTitle(chartTitle);
            		workspaceBean.setChartType(chartTypeName);
            		workspaceBean.setFilter(filterVariable);
            		workspaceBean.setxAxis(xAxis);
            		workspaceBean.setyAxis(yAxis);
            		workspaceBean.setWeightVariable(weight);
            		workspaceBean.setBenchMark(benchMarkId);
            		workspaceBean.setMeanIncl(meanIncl);
            		workspaceBean.setMeanExcl(meanExcl);
            		workspaceBean.setMedianIncl(medianIncl);
            		workspaceBean.setMedianExcl(medianExcl);
            		
            		 String mainVariable="";
            		 String splitByVariable="";
            		if(xAxis!= null){
            			String s[] = xAxis.split(",");
            			int len = s.length;
            			for(int k=0;k<len;k++){
            				String id = s[k];
            				mainVariable =mainVariable+ (String)variableNamesObj.get(id.toLowerCase());
            				if(k+1<len){
            					mainVariable= mainVariable+",";
                 	 		}
            			}
            		}
            		if(yAxis!= null){
            			String s[] = yAxis.split(",");
            			int len = s.length;
            			for(int k=0;k<len;k++){
            				String id = s[k];
            				splitByVariable =splitByVariable+ (String)variableNamesObj.get(id.toLowerCase());
            				if(k+1<len){
            					splitByVariable= splitByVariable+",";
                 	 		}
            			}
            		}
            		String benchMark = (String)benchMarkObj.get(benchMarkId);
            		String filter = (String)variableNamesObj.get(filterVariable);
            		
            		String functionName= (String)functionMapObj.get(funtionId);
            		String tableName="";
            		String xAxisColumn="";
            		String yAxisColumn="";
            		String filterColumn="";
            		String chartType="";
            		String xAxisName="";
            		String yAxisName="";
            		String whereConstrain="";
            		String stack100Percent=null;
            		Integer reportMetaDataId=null;
            		if(!ApplicationUtil.isEmptyOrNull(chartTypeName) && chartTypeName.equals("HUNDRED_PERCENT_STACKED_COLUMN")){
        				stack100Percent="1";
        				chartType="StackedColumn2D";
            		}else if(!ApplicationUtil.isEmptyOrNull(chartTypeName) && chartTypeName.equals("HUNDRED_PERCENT_STACKED_BAR")){
        				chartType="StackedBar2D";
        				stack100Percent="1";
            		}else{
            			chartType = chartTypeName;
            		}

            		if(functionName != null && functionName.equals("KEYDRIVERANALYSIS")){
            			chartType="Scatter";
            		}

            		if(functionName != null && functionName.equals("AVERAGE")){
            			
            			tableName="chartaverage";
            			xAxisColumn="xAxis";
            			if(!ApplicationUtil.isEmptyOrNull(yAxis) && !yAxis.equals("-1")){
            				yAxisColumn="yAxis";
            				yAxisName=splitByVariable;
            			}else{
            				yAxisName="Average";
            			}
            			if(!ApplicationUtil.isEmptyOrNull(benchMarkId) && !benchMarkId.equals("-1")){
            				if(!ApplicationUtil.isEmptyOrNull(xAxis) && xAxis.contains(",")){
            					yAxisColumn="yAxis";
            				}
            			}
            			if(!ApplicationUtil.isEmptyOrNull(filterVariable) && !filterVariable.equals("-1")){
            				filterColumn="filter";
            			}
            		}
            		else if(functionName != null && functionName.equals("FREQUENCY"))
            		{
            			tableName="chartfrequencies";
            			xAxisColumn="xAxis";
            			if(!ApplicationUtil.isEmptyOrNull(yAxis) && !yAxis.equals("-1")){
            				yAxisColumn="yAxis";
            				xAxisName=splitByVariable;
            				yAxisName=mainVariable;
            			}else if(!ApplicationUtil.isEmptyOrNull(xAxis) && xAxis.contains(",")){
            				yAxisColumn="yAxis";
            				yAxisName="Frequency";
            			}else{
            				yAxisName="Frequency";
            				xAxisName=mainVariable;
            			}
            			if(!ApplicationUtil.isEmptyOrNull(filterVariable) && !filterVariable.equals("-1")){
            				filterColumn="filter";
            			}
            		}
            		else if(functionName != null && functionName.equals("KEYDRIVERANALYSIS"))
            		{
            			tableName="chartkda";
            			xAxisColumn="xAxis";
            			if(!ApplicationUtil.isEmptyOrNull(yAxis) && !yAxis.equals("-1")){
            				yAxisColumn="yAxis";
            			}
            			if(!ApplicationUtil.isEmptyOrNull(filterVariable) && !filterVariable.equals("-1")){
            				filterColumn="filter";
            			}
            			yAxisName="Importance";
            			xAxisName="Relative Scores";
            		}
            		else if(functionName != null && functionName.equals("KAA"))
            		{
            			tableName="chartkaa";
            			xAxisColumn="xAxis";
            			if(!ApplicationUtil.isEmptyOrNull(yAxis) && !yAxis.equals("-1")){
            				yAxisColumn="yAxis";
            			}
            			if(!ApplicationUtil.isEmptyOrNull(filterVariable) && !filterVariable.equals("-1")){
            				filterColumn="filter";
            			}
            		}
            		else if(functionName != null && functionName.equals("VERBATIM")|| functionName.equals("THEMECLOUD"))
            		{
            			tableName="verbatim";
            		}else if(functionName != null && functionName.equals("SENTIMENT")){
            			tableName="chartsentiment";
            			xAxisColumn="name";
            			yAxisColumn="series";
            			chartType="MSCombi2D";
            			if(!ApplicationUtil.isEmptyOrNull(yAxis) && !yAxis.equals("-1")){
            				filterColumn="segment";
            				filterVariable = yAxis;
            			}
            			
            		}
            		String valueColName[]= {"value"};
            		List filterValuesDropDownVos = null;
            		List filterValues =null;
            		if (!ApplicationUtil.isEmptyOrNull(filterColumn)){
            			Map filterColumnCodeBookMapObj = (Map)codeBookMapObj.get(filterVariable);
            			filterValuesDropDownVos = new ArrayList();
            			filterValues = applicationDAO.getFilterValues(null,empId,tableName, filterColumn,reportMetaDataId);
            			
            			workspaceBean.setFilterValues(filterValues);
            			
            			if(filterValues != null){
            				int len = filterValues.size();
            				for(int k=0;k<len;k++){
            					String str = (String)filterValues.get(k);
            					if(!ApplicationUtil.isEmptyOrNull(str)){
                					DropDownDisplayVo dropDownDisplayVo = new DropDownDisplayVo();
                					String recodeVal = str;
                					if(filterColumnCodeBookMapObj != null){
                						recodeVal = (String)filterColumnCodeBookMapObj.get(str);
                						if(ApplicationUtil.isEmptyOrNull(recodeVal)){
                							recodeVal = str;
                						}
                						
                					}
                					dropDownDisplayVo.setDisplayName(recodeVal);
                					dropDownDisplayVo.setValue(str);
                					filterValuesDropDownVos.add(dropDownDisplayVo);
            					}
            				}
            			}
					}
            		String str=null;
            		
            		Map tableViewMapObj;
            		Map kdaTableViewMapObj;
            		SingleMultiSeriesChartValueObject singleMultiSeriesChartValueObject = new SingleMultiSeriesChartValueObject();
            		singleMultiSeriesChartValueObject.setFilterColumn(filterColumn);
            		singleMultiSeriesChartValueObject.setFilterValues(filterValuesDropDownVos);
            		singleMultiSeriesChartValueObject.setTemplateChartInfoId(null);
            		singleMultiSeriesChartValueObject.setMainVariable(mainVariable);
            		singleMultiSeriesChartValueObject.setSplitByVariable(splitByVariable);
            		singleMultiSeriesChartValueObject.setFilter(filter);
            		singleMultiSeriesChartValueObject.setBenchMark(benchMark);
            		singleMultiSeriesChartValueObject.setFunctionName(functionName);
            		singleMultiSeriesChartValueObject.setChartType(chartType);
            		singleMultiSeriesChartValueObject.setxAxisName(xAxisName);
            		singleMultiSeriesChartValueObject.setyAxisName(yAxisName);
            		if(functionName != null && (functionName.equals("FREQUENCY") || functionName.equals("AVERAGE"))){
            			tableViewMapObj=applicationDAO.getChartXmlFromDatabase(null,empId, tableName, xAxisColumn,yAxisColumn,valueColName,false,false,filterColumn,null,xAxis,yAxis,"false",false,funtionId,topBottomThreshold,null,whereConstrain,reportMetaDataId,benchMarkId,colorCode,chartType);
            			singleMultiSeriesChartValueObject.setCategoryName(workspaceBean.getCategoryName());
                		singleMultiSeriesChartValueObject.setXml((String)tableViewMapObj.get("xmlStr"));
                		singleMultiSeriesChartValueObject.setTableName(tableName);
                		singleMultiSeriesChartValueObject.setxAxis(xAxisColumn);
                		singleMultiSeriesChartValueObject.setyAxis(yAxisColumn);
                		singleMultiSeriesChartValueObject.setxAxisColumnName(xAxis);
                		singleMultiSeriesChartValueObject.setyAxisColumnName(yAxis);
                		singleMultiSeriesChartValueObject.setWidth("900");
                		singleMultiSeriesChartValueObject.setHeight("375");
                		if(!ApplicationUtil.isEmptyOrNull(stack100Percent)){
                			singleMultiSeriesChartValueObject.setStack100Percent(stack100Percent);
                		}
                		singleMultiSeriesChartValueObject.setCategoryField(xAxisColumn);
            		}else if(functionName != null && (functionName.equals("WORDCLOUD") || functionName.equals("CATEGORICAL_ANALYSIS"))){
            			if(!ApplicationUtil.isEmptyOrNull(yAxis) 
            					&& !yAxis.equals("-1")){
            						List wordCloudSegmentList =  applicationDAO.getWordCloudSegments(null, null,reportMetaDataId);
            						String firstSegmentName="";
	                				if(wordCloudSegmentList != null && wordCloudSegmentList.size()>0){
	                					List segmentDropDownList = new ArrayList();
	                					for(int k=0;k<wordCloudSegmentList.size();k++){
	                						String segmentName = (String)wordCloudSegmentList.get(k);
	                						if(k==0){
	                							firstSegmentName = segmentName;
	                						}
	                						DropDownDisplayVo displayVo = new DropDownDisplayVo();
	                						displayVo.setDisplayName(segmentName);
	                						List wordCloudList =  applicationDAO.getWordCloud(null, null,segmentName,reportMetaDataId);
		                					StringBuffer buffer = new StringBuffer();
		                					if(wordCloudList != null){
		                						for(int ii=0;ii<wordCloudList.size();ii++){
		                							WordCloud  wordCloud = (WordCloud) wordCloudList.get(ii);
		                							buffer.append(wordCloud.getNormalizedCount()+"#$#"+wordCloud.getCount()+"#$#"+wordCloud.getWord()+"#@#");
		                						}
		                					}
	                						displayVo.setValue(buffer.toString());
	                						segmentDropDownList.add(displayVo);
	                					}
	                					List wordCloudList =  applicationDAO.getWordCloud(null, null,firstSegmentName,reportMetaDataId);
	                					StringBuffer buffer = new StringBuffer();
	                					if(wordCloudList != null){
	                						for(int ii=0;ii<wordCloudList.size();ii++){
	                							WordCloud  wordCloud = (WordCloud) wordCloudList.get(ii);
	                							buffer.append(wordCloud.getNormalizedCount()+"#$#"+wordCloud.getCount()+"#$#"+wordCloud.getWord()+"#@#");
	                						}
	                					}
	                			        singleMultiSeriesChartValueObject.setWordCloudStr(buffer.toString());
	                    				singleMultiSeriesChartValueObject.setSegmentName(firstSegmentName);
	                    				singleMultiSeriesChartValueObject.setSegmentValues(segmentDropDownList);
	                    			}
            			}else{
            				List wordCloudList =  applicationDAO.getWordCloud(null, empId,null,reportMetaDataId);
            				if(wordCloudList != null && wordCloudList.size()>0){
            					StringBuffer buffer = new StringBuffer();
            					for(int ii=0;ii<wordCloudList.size();ii++){
        							WordCloud  wordCloud = (WordCloud) wordCloudList.get(ii);
        							buffer.append(wordCloud.getNormalizedCount()+"#$#"+wordCloud.getCount()+"#$#"+wordCloud.getWord()+"#@#");
        						}
            					singleMultiSeriesChartValueObject.setWordCloudStr(buffer.toString());
                			}
            			}
            		}else if(functionName != null && (functionName.equals("SENTIMENT"))){
            			List sentimentPercentageList =  applicationDAO.getSentimentPercentage(null, empId,null,reportMetaDataId);
            			if(sentimentPercentageList != null && sentimentPercentageList.size()>0){
            				ChartSentimentPercentage chartSentimentPercentage = (ChartSentimentPercentage)sentimentPercentageList.get(0);
            				singleMultiSeriesChartValueObject.setPositiveSentiment(chartSentimentPercentage.getPositive());
            				singleMultiSeriesChartValueObject.setNegativeSentiment(chartSentimentPercentage.getNegative());
            			}
            			tableViewMapObj=applicationDAO.getChartXmlFromDatabase(null,empId, tableName, xAxisColumn,yAxisColumn,valueColName,false,false,filterColumn,null,xAxis,yAxis,"false",true,funtionId,null,null,whereConstrain,reportMetaDataId,null,null,chartType);
            			singleMultiSeriesChartValueObject.setTableName(tableName);
                		singleMultiSeriesChartValueObject.setxAxis(xAxisColumn);
                		singleMultiSeriesChartValueObject.setyAxis(yAxisColumn);
                		singleMultiSeriesChartValueObject.setxAxisColumnName(xAxis);
                		singleMultiSeriesChartValueObject.setyAxisColumnName(yAxis);
                		singleMultiSeriesChartValueObject.setFilter(splitByVariable);
            			singleMultiSeriesChartValueObject.setXml((String)tableViewMapObj.get("xmlStr"));
                		singleMultiSeriesChartValueObject.setWidth("900");
                		singleMultiSeriesChartValueObject.setHeight("375");
            		}else if (functionName != null && (functionName.equals("KEYDRIVERANALYSIS"))){
            				if(filterValues != null && filterValues.size()>0){
            					kdaTableViewMapObj=applicationDAO.getPlotChartXmlFromDatabase(null,empId, tableName, xAxisColumn,yAxisColumn,valueColName,filterColumn,(String)filterValues.get(0),xAxis,yAxis,false,whereConstrain,filterVariable,reportMetaDataId);
            					str= (String) kdaTableViewMapObj.get("kdaXmlStr");
            				}else{
            					kdaTableViewMapObj=applicationDAO.getPlotChartXmlFromDatabase(null,empId, tableName, xAxisColumn,yAxisColumn,valueColName,filterColumn,null,xAxis,yAxis,false,whereConstrain,filterVariable,reportMetaDataId);
            					str= (String) kdaTableViewMapObj.get("kdaXmlStr");
            					
            				}
            				if(str != null){
            					String strArry[] = str.split("@");
            					singleMultiSeriesChartValueObject.setXml(strArry[0]);
            					singleMultiSeriesChartValueObject.setxAxisMinValue(strArry[1]);
            					singleMultiSeriesChartValueObject.setxAxisMaxValue(strArry[2]);
            					singleMultiSeriesChartValueObject.setyAxisMinValue(strArry[3]);
            				}
                			singleMultiSeriesChartValueObject.setCategoryName(workspaceBean.getCategoryName());
                    		singleMultiSeriesChartValueObject.setTableName(tableName);
                    		singleMultiSeriesChartValueObject.setxAxis(xAxisColumn);
                    		singleMultiSeriesChartValueObject.setyAxis(yAxisColumn);
                    		singleMultiSeriesChartValueObject.setxAxisColumnName(xAxis);
                    		singleMultiSeriesChartValueObject.setyAxisColumnName(yAxis);
                    		singleMultiSeriesChartValueObject.setDivLineAlpha("0");
                    		singleMultiSeriesChartValueObject.setWidth("900");
                    		singleMultiSeriesChartValueObject.setHeight("375");
            		}
            		else if (functionName != null && (functionName.equals("BANNER TABLE"))){
            			System.out.println("Banner Table");
            			if(empId != null){
                				Map bannerTableMapObj = applicationDAO.getBannerTable(empId, null,xAxis,yAxis,weight,meanIncl,meanExcl,medianIncl,medianExcl,reportMetaDataId);
	           					List bannerTableHeaderList =(List)bannerTableMapObj.get("bannerTableHeaderList");
	           					List bannerTableDataList =(List)bannerTableMapObj.get("bannerTableDataList");
	           					List bannerTableWeightList =(List)bannerTableMapObj.get("bannerTableWeightList");
	           					List bannerTablePercentageList =(List)bannerTableMapObj.get("bannerTablePercentageList");
	           					List bannerTablePercentageWeightList =(List)bannerTableMapObj.get("bannerTablePercentageWeightList");
	           					singleMultiSeriesChartValueObject.setBannerTableHeaderList(bannerTableHeaderList);
	           					singleMultiSeriesChartValueObject.setBannerTableDataList(bannerTableDataList);
	           					singleMultiSeriesChartValueObject.setBannerTableWeightList(bannerTableWeightList);
	           					singleMultiSeriesChartValueObject.setBannerTablePercentageList(bannerTablePercentageList);
	           					singleMultiSeriesChartValueObject.setBannerTablePercentageWeightList(bannerTablePercentageWeightList);
            			}
            			else{
                				Map bannerTableMapObj = applicationDAO.getBannerTable(empId, null,xAxis,yAxis,weight,meanIncl,meanExcl,medianIncl,medianExcl,reportMetaDataId);
	           					List bannerTableHeaderList =(List)bannerTableMapObj.get("bannerTableHeaderList");
	           					List bannerTableDataList =(List)bannerTableMapObj.get("bannerTableDataList");
	           					List bannerTableWeightList =(List)bannerTableMapObj.get("bannerTableWeightList");
	           					List bannerTablePercentageList =(List)bannerTableMapObj.get("bannerTablePercentageList");
	           					List bannerTablePercentageWeightList =(List)bannerTableMapObj.get("bannerTablePercentageWeightList");
	           					singleMultiSeriesChartValueObject.setBannerTableHeaderList(bannerTableHeaderList);
	           					singleMultiSeriesChartValueObject.setBannerTableDataList(bannerTableDataList);
	           					singleMultiSeriesChartValueObject.setBannerTableWeightList(bannerTableWeightList);
	           					singleMultiSeriesChartValueObject.setBannerTablePercentageList(bannerTablePercentageList);
	           					singleMultiSeriesChartValueObject.setBannerTablePercentageWeightList(bannerTablePercentageWeightList);
            			}
        		}
            		workspaceBean.setSingleMultiSeriesChartValueObject(singleMultiSeriesChartValueObject);
					
          return workspaceBean;
		
}
	
	public void setDistributionUsers()
			throws ApplicationException{
		applicationDAO.setDistributionUsers();
	}

	public void getBannerMetaData()
			throws ApplicationException{
		applicationDAO.getBannerMetaData();
	}

	public List downloadBannerTable(String filename,String percentage) throws ApplicationException {
		try{
		 return applicationDAO.downloadBannertable(filename,percentage);
		}
		 catch (ApplicationException e) {
			 logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
				throw e;
			}
	}
	
	public List downloadBannerTable2(String filename) throws ApplicationException {
		try{
		 return applicationDAO.downloadBannertable2(filename);
		}
		 catch (ApplicationException e) {
			 logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
				throw e;
			}
	}
	
	public void createStandardReport(TemplateChart templateChart,String destinationPathFileStr,String reportTitle,String managerName) throws ApplicationException, IOException, JAXBException, Docx4JException {
		
	}
		
	public List getManagerByLocation(String empId) throws ApplicationException {
		try{
		 return applicationDAO.getManagerByLocation(empId);
		}catch (ApplicationException e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	}
	
	public List getSegmentByLocation(String segmentName) throws ApplicationException {
        try{
         return applicationDAO.getSegmentByLocation(segmentName);
        }catch (ApplicationException e) {
                logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
                throw e;
        }
}
}