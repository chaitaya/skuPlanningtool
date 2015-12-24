package com.bridgei2i.common.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.util.StringUtils;

import com.bridgei2i.common.constants.ApplicationConstants;
import com.bridgei2i.common.constants.ApplicationErrorCodes;
import com.bridgei2i.common.exception.ApplicationException;
import com.bridgei2i.common.util.ApplicationUtil;
import com.bridgei2i.common.util.CsvReaderWriter;
import com.bridgei2i.common.util.PropertiesUtil;
import com.bridgei2i.common.vo.PageTemplate;
import com.bridgei2i.common.vo.Users;
import com.bridgei2i.vo.BTBData;
import com.bridgei2i.vo.ForecastingASP;
import com.bridgei2i.vo.ForecastingUnits;
import com.bridgei2i.vo.PlanningCycle;
import com.bridgei2i.vo.VariableNames;
public class ReportDAO {
	
	private HibernateTemplate hibernateTemplate;
	private static Logger logger = Logger.getLogger(UtilitiesDAO.class);
	
	@Autowired(required=true)
	private PlanningDAO planningDAO;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	public Map getReportTableView(final Map targetTableViewMapObj,final String tableName,final Integer planningCycleId,final String nextFilterVariable,final String business,final String whereClauseStr,final Integer week,final Integer year,final Integer range,final String selectedFilterTypeValue,final String selectedPeriod,final Map targetTablecolorRangeMapObj){
		try{
			logger.debug("Entered into getReportTableView");
            Object object = hibernateTemplate.execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                         throws HibernateException, SQLException {
            	
            	 String sql = "";
            	 List targetTableList = new ArrayList();
            	 String tableHeader="";
            	 String totalSelectedTypeValues ="";
            	 int switchForTotalSelectedTypeValues=0;
            	 String plClassColumn=PropertiesUtil.getProperty(ApplicationConstants.PLCLASS);
            	 String categoryColumn = PropertiesUtil.getProperty(ApplicationConstants.CATEGORY);
            	 String businessTypeCriteria=" ";
            	 if(tableName.equalsIgnoreCase("ForecastingUnits")){
          			tableHeader="Units";
            	 }  else if (tableName.equalsIgnoreCase("ForecastingASP")){
            		 tableHeader="ASP";
            	 } else if (tableName.equalsIgnoreCase("ForecastingRevenue")){
                 	tableHeader="Revenue";
            	 } else if (tableName.equalsIgnoreCase("ForecastingESC")){
                 	tableHeader="ESC";
            	 }else {
                 	tableHeader="PM%";
            	 }
            	if(!ApplicationUtil.isEmptyOrNull(business)){
            		businessTypeCriteria="and business='"+business+"'";
            	}
            	 sql="from BTBData where 1=1 "+businessTypeCriteria+" ";
            	 Query q = session.createQuery(sql);
            	 Map btbDataMapObject = new HashMap();
            	 List btbList= (List) q.list();
            	  if(btbList != null){
                   	int size =btbList.size();
                    	for(int i=0;i<size;i++){
                    		BTBData btbData = (BTBData)btbList.get(i);
                    		btbDataMapObject.put(btbData.getCategory(), btbData);
                    	}
                   }
            	  
            	 
            	 sql="select distinct "+nextFilterVariable+" from Data where 1=1 "+whereClauseStr;
            	 q = session.createQuery(sql);
                 List distinctTargetTableList =  q.list();
                 if(distinctTargetTableList != null){
                 	int size1 =distinctTargetTableList.size();
                 	int firstIndex = 0;
                 	double plClassLevelBtb =0;
                 	double plClassLevelBtbTarget=0.0;
                 	String btbValue="";
              		String targetValue="";
                  	for(int i=0;i<size1;i++){
                  		if(i==0){
                  			 firstIndex=0;
                  		} else{
                  			 firstIndex=1;
                  		}
                  		
                  		switchForTotalSelectedTypeValues=1;
                  		String selectedTypeValue = "'"+(String)distinctTargetTableList.get(i)+"'";
                  		totalSelectedTypeValues = totalSelectedTypeValues+selectedTypeValue;
                  		if(i<size1-1){
                  			totalSelectedTypeValues=totalSelectedTypeValues+",";
                  		}
                  		if(btbDataMapObject.containsKey((String)distinctTargetTableList.get(i)) && (tableName.equalsIgnoreCase("ForecastingASP") || tableName.equalsIgnoreCase("ForecastingRevenue"))){
                  			BTBData btbData= (BTBData) btbDataMapObject.get((String)distinctTargetTableList.get(i));
                  			btbValue=btbData.getBtbValue(); 
                  			targetValue=btbData.getTargetValue();
                  			double btb =Double.parseDouble(btbValue);
                  			plClassLevelBtb=plClassLevelBtb+btb;
                  			double btbTarget =Double.parseDouble(targetValue);
                  			plClassLevelBtbTarget=plClassLevelBtbTarget+btbTarget;
                  			switchForTotalSelectedTypeValues=3;
                 		}
                  		if(plClassColumn.equalsIgnoreCase(nextFilterVariable) && (tableName.equalsIgnoreCase("ForecastingASP") || tableName.equalsIgnoreCase("ForecastingRevenue"))){
	                   		 sql="SELECT sum(btbValue),sum(targetValue) FROM BTBData where 1=1 "+businessTypeCriteria+" and category in (SELECT distinct "+categoryColumn+" FROM Data where "+plClassColumn+"="+selectedTypeValue+")" ;
	                       	 q = session.createQuery(sql);
	                       	 btbList= (List) q.list();
	                       	 if(btbList != null){
	                               	int btbSize =btbList.size();
	                                	for(int k=0;k<btbSize;k++){
	                                		Object[] rowList = (Object[])btbList.get(k);
	                                		if(rowList[0]!=null){
	                                			btbValue=rowList[0].toString();
	                                		}
	                                		if(rowList[1]!=null){
	                                			targetValue=rowList[1].toString();
	                                		}
	                                	}
	                               }
	                       	 if(!ApplicationUtil.isEmptyOrNull(btbValue)){
	                       		double btb =Double.parseDouble(btbValue);
		              			plClassLevelBtb=plClassLevelBtb+btb;
	                       	 }
	                       	if(!ApplicationUtil.isEmptyOrNull(targetValue)){
	                       		double btbTarget =Double.parseDouble(targetValue);
		              			plClassLevelBtbTarget=plClassLevelBtbTarget+btbTarget;
	                       	}
	              			switchForTotalSelectedTypeValues=3;
                  		}
                  		getReportTableData(targetTableViewMapObj,targetTableList,tableName,planningCycleId,nextFilterVariable,selectedTypeValue,business,whereClauseStr,week,year,range,firstIndex,switchForTotalSelectedTypeValues,btbValue,targetValue,selectedPeriod,targetTablecolorRangeMapObj);
                  		if(i==size1-1 && plClassLevelBtbTarget!=0.0 && plClassLevelBtb!=0.0){
                  			btbValue=plClassLevelBtb+"";
                  			targetValue=""+plClassLevelBtbTarget;
                  			
                  		} else{
                  			btbValue=""; 
                      		targetValue="";
                  		}
                  		
                  	}
                  	if(btbDataMapObject.containsKey(selectedFilterTypeValue) && (tableName.equalsIgnoreCase("ForecastingASP") || tableName.equalsIgnoreCase("ForecastingRevenue"))){
                  		switchForTotalSelectedTypeValues=2;
                  		BTBData btbData= (BTBData) btbDataMapObject.get(selectedFilterTypeValue);
              			btbValue=btbData.getBtbValue(); 
              			targetValue=btbData.getTargetValue();
              			getReportTableData(targetTableViewMapObj,targetTableList,tableName,planningCycleId,nextFilterVariable,totalSelectedTypeValues,business,whereClauseStr,week,year,range,firstIndex,switchForTotalSelectedTypeValues,btbValue,targetValue,selectedPeriod,targetTablecolorRangeMapObj);
             		}
                  	if(size1>1 || switchForTotalSelectedTypeValues==2){
                  		switchForTotalSelectedTypeValues=0;
                      	getReportTableData(targetTableViewMapObj,targetTableList,tableName,planningCycleId,nextFilterVariable,totalSelectedTypeValues,business,whereClauseStr,week,year,range,firstIndex,switchForTotalSelectedTypeValues,btbValue,targetValue,selectedPeriod,targetTablecolorRangeMapObj);
                  	}
                  	targetTableViewMapObj.put(tableHeader, targetTableList);
                  
                 }
            	
            	 return targetTableViewMapObj;
            }
		     });
		     logger.debug("Leaving from getReportTableView");
		     return (Map) object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	}

	
	public Map getReportTableData(final Map targetTableViewMapObj,final List targetTableList ,final String tableName,final Integer planningCycleId,final String nextFilterVariable,final String selectedTypeValue,final String business,final String whereClauseStr,final Integer week,final Integer year,
			final Integer range,final Integer firstIndex,final Integer switchForTotalSelectedTypeValues,final String btbValue,final String targetValue,final String selectedPeriod,final Map targetTablecolorRangeMapObj){
		try{
			logger.debug("Entered into getReportTableData");
           
			Object object = hibernateTemplate.execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                         throws HibernateException, SQLException {
                String sql = "";
                String sql1="";
                String orderWeek = PropertiesUtil.getProperty("orderWeek");
                String productIdColumn =PropertiesUtil.getProperty("skuListId");
                String unitsColumn = PropertiesUtil.getProperty("quantity");
                String aspColumn = PropertiesUtil.getProperty("ASP");
                String escColumn =PropertiesUtil.getProperty("ESC");
                String pmColumn =PropertiesUtil.getProperty("productMargin");
                String businessColumn= PropertiesUtil.getProperty("business");
                String numberOfForecastsStr= PropertiesUtil.getProperty("numberOfForecasts");
                int numberOfForecasts=0;
                if(!ApplicationUtil.isEmptyOrNull(numberOfForecastsStr)){
                	numberOfForecasts=Integer.parseInt(numberOfForecastsStr);
                }
                String numberOfActualsStr= PropertiesUtil.getProperty("numberOfActuals");
                int numberOfActuals=0;
                if(!ApplicationUtil.isEmptyOrNull(numberOfActualsStr)){
                	numberOfActuals=Integer.parseInt(numberOfActualsStr);
                }
                int tableColorRange=0;
                String categoryColumn = PropertiesUtil.getProperty(ApplicationConstants.CATEGORY);
                System.out.println("filter variable is "+nextFilterVariable);
                StringBuffer weeksBuffer = new StringBuffer();
                StringBuffer actualWeeksBuffer = new StringBuffer();
                StringBuffer allWeeksBuffer=new StringBuffer();
                StringBuffer forecastWeeksBuffer = new StringBuffer();
            	try {
                     		List dataList = new ArrayList();      
                     		Map catSubcategoryDataMap = new HashMap();
                     		Object[] rowList=null;
                     		List weekList= new ArrayList();
                     		weekList.add(" ");
                     		List monthlyOrQuarterlyList = new ArrayList();
                     		List monthlyOrQuarterlyForecastsList= new ArrayList();
                     		List monthlyOrQuarterlyActualsList =new ArrayList();
                     		monthlyOrQuarterlyForecastsList.add("");
                     		monthlyOrQuarterlyActualsList.add("");
                     		List valuesList= new ArrayList();
                     		List percentageOfTargetsList= new ArrayList();
                     		if(switchForTotalSelectedTypeValues==1 || switchForTotalSelectedTypeValues==3){
                     			if(!ApplicationUtil.isEmptyOrNull(btbValue)){
                     				valuesList.add(selectedTypeValue+" Wt BTB");
                     			} else{
                     				valuesList.add(selectedTypeValue);
                     			}
                     			
                     		} else if(switchForTotalSelectedTypeValues==0) {
                     			if(!ApplicationUtil.isEmptyOrNull(btbValue)){
                     				valuesList.add("Total Wt BTB");
                     			} else{
                     				valuesList.add("Total");
                     			}
                     			//valuesList.add("Total");
                     		} else{
                     			valuesList.add("BTB");
                     		}
                     		percentageOfTargetsList.add("% to Target");
                     		List actualWeeksList= planningDAO.getSKUForecastWeek(year,week,range,1);
                     		int actualsSize=actualWeeksList.size();
                     		for(int t=0;t<actualsSize;t++){
                     			actualWeeksBuffer.append("'"+actualWeeksList.get(t)+"'");
                     			allWeeksBuffer.append("'"+actualWeeksList.get(t)+"'");
                            	if(t<actualsSize-1){
                            		actualWeeksBuffer.append(",");
                            	}
                            	allWeeksBuffer.append(",");
                     		}
                     		List forecastWeeksList= getForecastWeeks(year,week,range);
                     		int forecastsSize=forecastWeeksList.size();
                     		for(int t=0;t<forecastsSize;t++){
                     			forecastWeeksBuffer.append("'"+forecastWeeksList.get(t)+"'");
                     			allWeeksBuffer.append("'"+forecastWeeksList.get(t)+"'");
                            	if(t<forecastsSize-1){
                            		forecastWeeksBuffer.append(",");
                            		allWeeksBuffer.append(",");
                            	}
                     		}
                     		String filterCondition="select distinct d."+productIdColumn+" from Data d where d."+nextFilterVariable+" in ( "+selectedTypeValue+") "+whereClauseStr;
                     		String filterConditionNativeSql="select distinct d."+productIdColumn+" from Data d where d."+nextFilterVariable+" in ( "+selectedTypeValue+") "+whereClauseStr;
                     		String monthlyOrQuarterly="";
                     		String businessTypeCriteria=" ";
                     		String businessTypeCriteria2=" ";
                     		if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && selectedPeriod.equalsIgnoreCase("Monthly")){
                     			monthlyOrQuarterly="calendarMonth";
                     		}
                     		if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && selectedPeriod.equalsIgnoreCase("Quarterly")){
                     			monthlyOrQuarterly="fiscalQuarter";
                     		}
                     		
                     		if(!ApplicationUtil.isEmptyOrNull(business)){
                     			businessTypeCriteria="and d."+businessColumn+" = '"+business+"'";
                     			businessTypeCriteria2="and f.business = '"+business+"'";
                     		}
                     		if(tableName.equalsIgnoreCase("ForecastingUnits")){
                     			if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                     				monthlyOrQuarterlyList.add("");
                     				sql="select mfc."+monthlyOrQuarterly+",cast(round(sum(totalData.val)) as char(30)),year from ( ";
                     				
                     				sql =sql+ "select d."+orderWeek+" week,sum(d."+unitsColumn+") val from Data d where d."+nextFilterVariable+" in ( "+selectedTypeValue+") "+businessTypeCriteria+" "
                                			+" and d."+productIdColumn+" in ("+filterCondition+")  and d."+orderWeek+" in ("+actualWeeksBuffer+") group by d."+orderWeek;
                       			 	
                                	sql=sql+ " union all ";
                                	sql = sql + "select t.forecastPeriod week, round(sum(t.OriginalForecast)) val from (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast  "
                                			+ "FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from  "
                                			+ "override_units_log group by forecastingUnitsId) b on a.forecastingUnitsId= b.forecastingUnitsId join forecasting_units f on f.id=b.forecastingUnitsId and f.planningCycleId ="+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.productId in ("+filterConditionNativeSql+") and  f.planningCycleId="+planningCycleId+"  "
                           			 				+ ""+businessTypeCriteria2+" and  f.forecastPeriod in ("+forecastWeeksBuffer+"))  t  group by forecastPeriod";
                                	sql=sql+ " ) totalData ,master_fiscal_calender mfc where totalData.week=mfc.fiscalWeek group by mfc."+monthlyOrQuarterly+",mfc.year order by mfc.id ";
                                	System.out.println("Units"+sql);
                     				
                     			}else {
                     				sql = "select d."+orderWeek+",sum(d."+unitsColumn+") from Data d where d."+nextFilterVariable+" in ( "+selectedTypeValue+") "+businessTypeCriteria+" "
                                			+" and d."+productIdColumn+" in ("+filterCondition+") and d."+orderWeek+" in ("+actualWeeksBuffer+") group by d."+orderWeek;
                       			 	
                     				sql1 = "select t.forecastPeriod, cast(round(sum(t.OriginalForecast)) as char(30)) from (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast  "
                                			+ "FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from  "
                                			+ "override_units_log group by forecastingUnitsId) b on a.forecastingUnitsId= b.forecastingUnitsId join forecasting_units f on f.id=b.forecastingUnitsId and f.planningCycleId ="+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.productId in ("+filterConditionNativeSql+") and  f.planningCycleId="+planningCycleId+" "+businessTypeCriteria2+" and f.forecastPeriod in ("+forecastWeeksBuffer+"))  t  group by t.forecastPeriod";
                     				
                     			}
                            	
                            }  else if (tableName.equalsIgnoreCase("ForecastingASP")){
                            	if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                            		monthlyOrQuarterlyList.add("");
                            		sql="select mfc."+monthlyOrQuarterly+",round(sum(totalData.quantity * totalData.asp)/sum(totalData.quantity),2),year from ( ";
                            		
                            		sql = sql+"select d."+orderWeek+" week,d."+unitsColumn+" quantity, d."+aspColumn+" asp from Data d  where d."+nextFilterVariable+" in ( "+selectedTypeValue+") "+businessTypeCriteria+" "
                                			+" and d."+productIdColumn+" in ("+filterCondition+")  and d."+orderWeek+" in ("+actualWeeksBuffer+")";
                                	
                            		sql=sql+ " union all ";
                            		
                                	sql = sql+"select fu.forecastPeriod week , fu.OriginalForecast quantity, fa.OriginalForecast asp from (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
                       			 			+ " FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from override_units_log group by forecastingUnitsId) b  "
                       			 			+ "on a.forecastingUnitsId= b.forecastingUnitsId join forecasting_units f on f.id=b.forecastingUnitsId and f.planningCycleId = "+planningCycleId+ " and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.planningCycleId = "+planningCycleId+ " "+businessTypeCriteria2+" and f.productId in ("+filterConditionNativeSql+") and f.forecastPeriod in ("+forecastWeeksBuffer+")) fu, (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
                       			 			+ " FROM forecasting_asp f left join (SELECT a.* FROM override_asp_log a join (select forecastingAspId,max(createdDate) as mdate from override_asp_log group by forecastingAspId) b  "
                       			 			+ "on a.forecastingAspId= b.forecastingAspId join forecasting_asp f on f.id=b.forecastingAspId and f.planningCycleId = "+planningCycleId+ " and a.createdDate=b.mdate) b on f.id = b.forecastingAspId where f.planningCycleId = "+planningCycleId+ "  "+businessTypeCriteria2+" and f.planningCycleId= "+planningCycleId+" and f.productId in ("+filterConditionNativeSql+") and  f.forecastPeriod in ("+forecastWeeksBuffer+")) fa  "+
                							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business and fa.planningCycleId = "+planningCycleId+ 
                							" and fu.planningCycleId= "+planningCycleId;
                                	
                                	sql=sql+ " ) totalData ,master_fiscal_calender mfc where totalData.week=mfc.fiscalWeek group by mfc."+monthlyOrQuarterly+",mfc.year order by mfc.id ";
                                	System.out.println("ASP"+sql);
                            	} else{
                            		sql = "select d."+orderWeek+",round(sum(d."+unitsColumn+" * d."+aspColumn+")/sum(d."+unitsColumn+"),2) from Data d where d."+nextFilterVariable+" in ( "+selectedTypeValue+") "+businessTypeCriteria+" "
                                			+" and d."+productIdColumn+" in ("+filterCondition+") and d."+orderWeek+" in ("+actualWeeksBuffer+") group by d."+orderWeek;
                	                	
                            		sql1 = "select fu.forecastPeriod , round(sum(fu.OriginalForecast * fa.OriginalForecast)/sum(fu.OriginalForecast),2) from (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
                       			 			+ " FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from override_units_log group by forecastingUnitsId) b  "
                       			 			+ "on a.forecastingUnitsId= b.forecastingUnitsId join forecasting_units f on f.id=b.forecastingUnitsId and f.planningCycleId = "+planningCycleId+ " and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.planningCycleId = "+planningCycleId+ " "+businessTypeCriteria2+" and f.productId in ("+filterConditionNativeSql+") and f.forecastPeriod in ("+forecastWeeksBuffer+")) fu, (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
                       			 			+ " FROM forecasting_asp f left join (SELECT a.* FROM override_asp_log a join (select forecastingAspId,max(createdDate) as mdate from override_asp_log group by forecastingAspId) b  "
                       			 			+ "on a.forecastingAspId= b.forecastingAspId join forecasting_asp f on f.id=b.forecastingAspId and f.planningCycleId = "+planningCycleId+ " and a.createdDate=b.mdate) b on f.id = b.forecastingAspId where f.planningCycleId = "+planningCycleId+ " "+businessTypeCriteria2+" and f.planningCycleId= "+planningCycleId+" and f.productId in ("+filterConditionNativeSql+") and f.forecastPeriod in ("+forecastWeeksBuffer+")) fa "+
                							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business and fa.planningCycleId = "+planningCycleId+ " and fu.planningCycleId= "+planningCycleId+" group by fu.forecastPeriod";
                            	}
                            	
                            	
                            } else if (tableName.equalsIgnoreCase("ForecastingRevenue")){
                            	if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                            		monthlyOrQuarterlyList.add("");
                            		sql="select mfc."+monthlyOrQuarterly+",round(sum(totalData.quantity * totalData.asp),2),year from ( ";
                            		
                            		sql =sql+ "select d."+orderWeek+" week,d."+unitsColumn+" quantity, d."+aspColumn+" asp from Data d where d."+nextFilterVariable+" in ( "+selectedTypeValue+") "+businessTypeCriteria+" "
                                			+" and d."+productIdColumn+" in ("+filterCondition+")  and d."+orderWeek+" in ("+actualWeeksBuffer+")";
                            		sql=sql+ " union all ";
                            		
                                	sql = sql+"select fu.forecastPeriod week , fu.OriginalForecast quantity, fa.OriginalForecast asp from (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
                       			 			+ " FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from override_units_log group by forecastingUnitsId) b  "
                       			 			+ "on a.forecastingUnitsId= b.forecastingUnitsId join forecasting_units f on f.id=b.forecastingUnitsId and f.planningCycleId = "+planningCycleId+ " and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.planningCycleId = "+planningCycleId+ " "+businessTypeCriteria2+" and f.productId in ("+filterConditionNativeSql+") and f.forecastPeriod in ("+forecastWeeksBuffer+")) fu, (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
                       			 			+ " FROM forecasting_asp f left join (SELECT a.* FROM override_asp_log a join (select forecastingAspId,max(createdDate) as mdate from override_asp_log group by forecastingAspId) b  "
                       			 			+ "on a.forecastingAspId= b.forecastingAspId join forecasting_asp f on f.id=b.forecastingAspId and f.planningCycleId = "+planningCycleId+ " and a.createdDate=b.mdate) b on f.id = b.forecastingAspId where f.planningCycleId = "+planningCycleId+ " "+businessTypeCriteria2+" and f.productId in ("+filterConditionNativeSql+") and f.forecastPeriod in ("+forecastWeeksBuffer+")) fa  "+
                							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business and fa.planningCycleId = "+planningCycleId+ " and fu.planningCycleId= "+planningCycleId;
                                	sql=sql+ " ) totalData ,master_fiscal_calender mfc where totalData.week=mfc.fiscalWeek group by mfc."+monthlyOrQuarterly+",mfc.year order by mfc.id ";
                                	System.out.println("check Revenue query"+sql);
                            		
                            	} else{
                            		sql = "select d."+orderWeek+",round(sum(d."+unitsColumn+") * (sum(d."+unitsColumn+" * d."+aspColumn+")/sum(d."+unitsColumn+")),2) from Data d where d."+nextFilterVariable+" in ( "+selectedTypeValue+") "+businessTypeCriteria+" "
                                			+" and d."+productIdColumn+" in ("+filterCondition+")  and d."+orderWeek+" in ("+actualWeeksBuffer+") group by d."+orderWeek;
                                	sql1 = "select fu.forecastPeriod , round(sum(fu.OriginalForecast * fa.OriginalForecast),2) from (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
                       			 			+ " FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from override_units_log group by forecastingUnitsId) b  "
                       			 			+ "on a.forecastingUnitsId= b.forecastingUnitsId join forecasting_units f on f.id=b.forecastingUnitsId and f.planningCycleId = "+planningCycleId+ " and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.planningCycleId = "+planningCycleId+ " "+businessTypeCriteria2+" and f.productId in ("+filterConditionNativeSql+") and f.forecastPeriod in ("+forecastWeeksBuffer+")) fu, (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
                       			 			+ " FROM forecasting_asp f left join (SELECT a.* FROM override_asp_log a join (select forecastingAspId,max(createdDate) as mdate from override_asp_log group by forecastingAspId) b  "
                       			 			+ "on a.forecastingAspId= b.forecastingAspId join forecasting_asp f on f.id=b.forecastingAspId and f.planningCycleId = "+planningCycleId+ " and a.createdDate=b.mdate) b on f.id = b.forecastingAspId where f.planningCycleId = "+planningCycleId+ " "+businessTypeCriteria2+" and f.productId in ("+filterConditionNativeSql+") and f.forecastPeriod in ("+forecastWeeksBuffer+")) fa "+
                							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business and fa.planningCycleId = "+planningCycleId+ " and fu.planningCycleId= "+planningCycleId+" group by fu.forecastPeriod";
                            		
                            	}
                            	
                            	
                            } else if (tableName.equalsIgnoreCase("ForecastingESC")){
                            	if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                            		monthlyOrQuarterlyList.add("");
                            		sql="select mfc."+monthlyOrQuarterly+",round(sum(totalData.esc * totalData.quantity)/sum(totalData.quantity),2),year from ( ";
                            		
                            		sql = sql+ "select d."+orderWeek+" week,d."+unitsColumn+" quantity, d."+escColumn+" esc  from Data d where d."+nextFilterVariable+" in ( "+selectedTypeValue+") "+businessTypeCriteria+" "
                                			+" and d."+productIdColumn+" in ("+filterCondition+") and d."+orderWeek+" in ("+actualWeeksBuffer+")";
                            		sql=sql+ " union all ";
                            		
                                	sql =sql+ "select fu.forecastPeriod week, fu.OriginalForecast quantity, fa.forecastValue esc from (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
                       			 			+ " FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from override_units_log group by forecastingUnitsId) b  "
                       			 			+ "on a.forecastingUnitsId= b.forecastingUnitsId join forecasting_units f on f.id=b.forecastingUnitsId and f.planningCycleId = "+planningCycleId+ " and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.planningCycleId = "+planningCycleId+ " "+businessTypeCriteria2+" and f.productId in ("+filterConditionNativeSql+") and f.forecastPeriod in ("+forecastWeeksBuffer+")) fu, forecasting_esc fa "+
                							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business and fa.planningCycleId = "+planningCycleId+ "  and fu.planningCycleId= "+planningCycleId;
                                	sql=sql+ " ) totalData ,master_fiscal_calender mfc where totalData.week=mfc.fiscalWeek group by mfc."+monthlyOrQuarterly+",mfc.year order by mfc.id ";
                                	System.out.println("check ESC query"+sql);
                            		
                            	} else{
                            		sql = "select d."+orderWeek+",round(sum(d."+escColumn+" * d."+unitsColumn+")/sum(d."+unitsColumn+"),2) from Data d where d."+nextFilterVariable+" in ( "+selectedTypeValue+") "+businessTypeCriteria+" "
                                			+" and d."+productIdColumn+" in ("+filterCondition+")  and d."+orderWeek+" in ("+actualWeeksBuffer+") group by d."+orderWeek;
                                	
                                	sql1 = "select fu.forecastPeriod , round(sum(fu.OriginalForecast * fa.forecastValue) / sum(fu.OriginalForecast),2) from (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
                       			 			+ " FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from override_units_log group by forecastingUnitsId) b  "
                       			 			+ "on a.forecastingUnitsId= b.forecastingUnitsId join forecasting_units f on f.id=b.forecastingUnitsId and f.planningCycleId = "+planningCycleId+ " and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.planningCycleId = "+planningCycleId+ " "+businessTypeCriteria2+" and f.productId in ("+filterConditionNativeSql+") and f.forecastPeriod in ("+forecastWeeksBuffer+")) fu, forecasting_esc fa "+
                							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business and fa.planningCycleId = "+planningCycleId+ " and fu.planningCycleId= "+planningCycleId+" group by fu.forecastPeriod";
                            	}
                            	
                            	
                            	
                            } else {
                            	if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                            		
                            		monthlyOrQuarterlyList.add("");
                            		
                            		sql="select mfc."+monthlyOrQuarterly+",round(((sum(totalData.asp) - sum(totalData.esc))/sum(totalData.asp))*100,2),year from ( ";
                            		
                            		sql = sql + "select d."+orderWeek+" week,d."+aspColumn+" asp,d."+escColumn+" esc from Data d  where d."+nextFilterVariable+" in ( "+selectedTypeValue+") "+businessTypeCriteria+" "
                                			+" and d."+productIdColumn+" in ("+filterCondition+")  and d."+orderWeek+" in ("+actualWeeksBuffer+")";
                            		sql=sql+ " union all ";
                                	
                            		sql =sql+ "select fu.forecastPeriod week,fa.OriginalForecast asp,fu.forecastValue esc from forecasting_esc fu, (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
                       			 			+ " FROM forecasting_asp f left join (SELECT a.* FROM override_asp_log a join (select forecastingAspId,max(createdDate) as mdate from override_asp_log group by forecastingAspId) b  "
                       			 			+ "on a.forecastingAspId= b.forecastingAspId join forecasting_asp f on f.id=b.forecastingAspId and f.planningCycleId = "+planningCycleId+ " and a.createdDate=b.mdate) b on f.id = b.forecastingAspId where f.planningCycleId = "+planningCycleId+ " "+businessTypeCriteria2+" and f.productId in ("+filterConditionNativeSql+") and f.forecastPeriod in ("+forecastWeeksBuffer+")) fa "+
                							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business and fa.planningCycleId = "+planningCycleId+ " and fu.planningCycleId= "+planningCycleId;
                            		sql=sql+ " ) totalData ,master_fiscal_calender mfc where totalData.week=mfc.fiscalWeek group by mfc."+monthlyOrQuarterly+",mfc.year order by mfc.id ";
                            		System.out.println("check pm% query"+sql);
                            	} else{
                            		sql = "select d."+orderWeek+",round(((sum(d."+aspColumn+") - sum( d."+escColumn+"))/sum(d."+aspColumn+"))*100,2) from Data d where d."+nextFilterVariable+" in ( "+selectedTypeValue+") "+businessTypeCriteria+" "
                                			+" and d."+productIdColumn+" in ("+filterCondition+")  and d."+orderWeek+" in ("+actualWeeksBuffer+")  group by d."+orderWeek;
                                	
                                	sql1 = "select fu.forecastPeriod ,round(((sum(fa.OriginalForecast)-sum(fu.forecastValue))/sum(fa.OriginalForecast))*100,2) from forecasting_esc fu, (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
                       			 			+ " FROM forecasting_asp f left join (SELECT a.* FROM override_asp_log a join (select forecastingAspId,max(createdDate) as mdate from override_asp_log group by forecastingAspId) b  "
                       			 			+ "on a.forecastingAspId= b.forecastingAspId join forecasting_asp f on f.id=b.forecastingAspId and f.planningCycleId = "+planningCycleId+ " and a.createdDate=b.mdate) b on f.id = b.forecastingAspId where f.planningCycleId = "+planningCycleId+ " "+businessTypeCriteria2+" and f.productId in ("+filterConditionNativeSql+") and f.forecastPeriod in ("+forecastWeeksBuffer+")) fa "+
                							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business and fa.planningCycleId = "+planningCycleId+ " and fu.planningCycleId= "+planningCycleId+" group by fu.forecastPeriod";
                            		
                            	}
                            	
                            }
                     		List ActualsList=null;
                     		if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && selectedPeriod.equalsIgnoreCase("Weekly")){
                     			Query q = session.createQuery(sql);
                                ActualsList =  q.list();
                     		}
                     		
                     		
                            if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                            	SQLQuery q = session.createSQLQuery(sql);
                            	ActualsList =  q.list();
                            }
                            Map actualsListMap = new HashMap();
                            if(ActualsList != null){
                            	int size =ActualsList.size();
                            	int limit = numberOfActuals;
                            	/*int count= 0;
                            	if(size>limit){
                            		count=size-limit;
                            	}*/ 
                            	for(int j=0;j<size;j++){
                            		rowList = (Object[])ActualsList.get(j);
                            		String actualPeriod="";
                            		String actualValue="";
                            		if(rowList[0]!=null){
                            			actualPeriod = rowList[0].toString();
                            		}
                            		if(rowList[1]!=null){
                            			actualValue = rowList[1].toString();
                            		}
                            		if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && selectedPeriod.equalsIgnoreCase("Monthly")){
                            			String year="";
                            			if(rowList[2]!=null){
                            				year = rowList[2].toString();
                                		}
                            			actualsListMap.put(actualPeriod+"-"+year, actualValue);
                                    }else{
                                    	actualsListMap.put(actualPeriod, actualValue);
                                    }
                            		
                            		/*if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                            			monthlyOrQuarterlyList.add(actualPeriod);    
                             			if(!ApplicationUtil.isEmptyOrNull(btbValue)){
                             				if(switchForTotalSelectedTypeValues==0 || switchForTotalSelectedTypeValues==3){
                             					double actual=Double.parseDouble(actualValue);
                                            	double btb =Double.parseDouble(btbValue);
                             					valuesList.add(actual+btb+"");
                             					
                             				}else{
                             					valuesList.add(btbValue);
                             				}
                             				
                             			}else{
                             				valuesList.add(actualValue);
                             			}
                            			
                            		}*/
                            		
                            	}
                            }
                            if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                            	List monthlyQuarterlyList = getMonthlyQuarterlyNamesWithYear(actualWeeksBuffer.toString(),selectedPeriod);
                            	int listSize1=monthlyQuarterlyList.size();
                            	tableColorRange=listSize1;
                            	if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && selectedPeriod.equalsIgnoreCase("Monthly")){
                            		monthlyQuarterlyList=getMonthlyQuarterlyNamesWithYear(allWeeksBuffer.toString(),selectedPeriod);
                            	}else{
                            		monthlyQuarterlyList=getMonthlyQuarterlyNames(allWeeksBuffer.toString(),selectedPeriod);
                            	}
                            	listSize1=monthlyQuarterlyList.size();
                            	for(int l=0;l<listSize1;l++){
                            		
                            		String monthQuarter=(String) monthlyQuarterlyList.get(l);
                             		String actualValue="";
                             		if(actualsListMap.containsKey(monthQuarter)){
                             			actualValue= (String) actualsListMap.get(monthQuarter);
                             		} else{
                             			actualValue="0";
                             		}
                             		monthlyOrQuarterlyList.add(monthQuarter);   
                             		monthlyOrQuarterlyActualsList.add(monthQuarter);
                         			if(!ApplicationUtil.isEmptyOrNull(btbValue)){
                         				if(switchForTotalSelectedTypeValues==0 || switchForTotalSelectedTypeValues==3){
                         					double actual=Double.parseDouble(actualValue);
                                        	double btb =Double.parseDouble(btbValue);
                         					valuesList.add(actual+btb+"");
                         					
                         				}else{
                         					valuesList.add(btbValue);
                         				}
                         				
                         			}else{
                         				valuesList.add(actualValue);
                         			}
                            		
                            	}
                            }
                            
                            if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Monthly") && !selectedPeriod.equalsIgnoreCase("Quarterly")){
                            	try {
                                 	List actualsWeekList= planningDAO.getSKUForecastWeek(year,week,range,1);
                                 	System.out.println("actuals "+actualsWeekList);
                                 	int size =actualsWeekList.size();
                                 	tableColorRange=size;
                                 	for(int k=size-1;k>=0;k--){
                                 		
                                 		String actualPeriod=(String) actualsWeekList.get(k);
                                 		String actualValue="";
                                 		if(actualsListMap.containsKey(actualPeriod)){
                                 			
                                 			actualValue= (String) actualsListMap.get(actualPeriod);
                                 			
                                     		
                                     		//percentageOfTargetsList.add("    ");
                                 		} else{
                                 			actualValue="0";
                                 		}
                                 		weekList.add(actualPeriod);   
                             			if(!ApplicationUtil.isEmptyOrNull(btbValue)){
                             				if(switchForTotalSelectedTypeValues==0 || switchForTotalSelectedTypeValues==3){
                             					double actual=Double.parseDouble(actualValue);
                                            	double btb =Double.parseDouble(btbValue);
                             					valuesList.add(actual+btb+"");
                             					
                             				}else{
                             					valuesList.add(btbValue);
                             				}
                             				
                             			}else{
                             				valuesList.add(actualValue);
                             			}
                                 		
                                 		
                                 	}
                                 	
                 				} catch (ApplicationException e) {
                 					// TODO Auto-generated catch block
                 					logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
                 				}
                            }
                            
                            
                            
                            if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && selectedPeriod.equalsIgnoreCase("Weekly")){
                            	SQLQuery query = session.createSQLQuery(sql1);
                                List ForecastingValuesList =  (List)query.list();
                                if(ForecastingValuesList != null){
                             	   int size =ForecastingValuesList.size();
                             	   List overrideForecastUnitsList = new ArrayList();
                             	   List overrideForecastAspList = new ArrayList();
                             	   for(int l=0;l<size;l++){
                             		  rowList = (Object[])ForecastingValuesList.get(l);
                                 		String forecastPeriod="";
                                 		String forecastValue="";
                                 		List forecastTable = new ArrayList();
                                 		if(rowList[0]!=null){
                                 			forecastPeriod = rowList[0].toString();
                                 		}
                                 		if(rowList[1]!=null){
                                 			forecastValue = rowList[1].toString();
                                 		}
                                 		weekList.add(forecastPeriod);
                                 		if(!ApplicationUtil.isEmptyOrNull(btbValue)){
                             				if(switchForTotalSelectedTypeValues==0 || switchForTotalSelectedTypeValues==3){
                             					double forecast=Double.parseDouble(forecastValue);
                                            	double btb =Double.parseDouble(btbValue);
                             					valuesList.add(forecast+btb+"");
                             					
                             				}else{
                             					valuesList.add(btbValue);
                             				}
                             				
                             			}else{
                             				valuesList.add(forecastValue);
                             			}
                                 		
                                 		//percentageOfTargetsList.add("    ");
                             	   }
                                }
                            	
                            }
                            
                            if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                            	
                            	dataList.add(monthlyOrQuarterlyList);
                     		}else{
                     			dataList.add(weekList);
                     			
                     		}
                            /*if(firstIndex==0 && switchForTotalSelectedTypeValues!=0){
                            	dataList.add(weekList);
                            }*/
                            dataList.add(valuesList);
                            int weekListSize=weekList.size();
                            List targetData= new ArrayList();
                            if(selectedPeriod.equalsIgnoreCase("Weekly")){
                            	 for(int m=0;m<weekListSize;m++){
                                 	if(m!=0){
                                 		weeksBuffer.append("'"+weekList.get(m)+"'");
                                     	if(m<weekListSize-1){
                                     		weeksBuffer.append(",");
                                     	}
                                 	}
                                 	if(!ApplicationUtil.isEmptyOrNull(targetValue) && switchForTotalSelectedTypeValues==2){
                                 		if(m==0){
                                 			targetData.add("Target");
                                 		}else{
                                 			targetData.add(targetValue);
                                 		}
                                 		
                                 	}
                                 	
                                 }
                            }
                           
                            if(!ApplicationUtil.isEmptyOrNull(targetValue) && switchForTotalSelectedTypeValues==2 && !ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                            	int monthlyListSize=monthlyOrQuarterlyList.size();
                            	for(int m=0;m<monthlyListSize;m++){
                            		if(m==0){
                            			targetData.add("Target");
                            		}else{
                            			targetData.add(targetValue);
                            		}
                            	}
                            }
                            if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly") && switchForTotalSelectedTypeValues!=2){
                            	targetData.add("Target");
                            	List targetData1 = getTargetTableData(tableName,selectedTypeValue, business, allWeeksBuffer.toString(),weekList,nextFilterVariable,whereClauseStr,targetValue,selectedPeriod,monthlyOrQuarterlyList );
                            	targetData.addAll(targetData1);
                            	/*List targetData2 = getTargetTableData(tableName,selectedTypeValue, business, forecastWeeksBuffer.toString(),weekList,nextFilterVariable,whereClauseStr,targetValue,selectedPeriod,monthlyOrQuarterlyForecastsList );
                            	targetData.addAll(targetData2);*/
                            }
                            if(switchForTotalSelectedTypeValues!=2 && selectedPeriod.equalsIgnoreCase("Weekly")){
                            	targetData.add("Target");
                            	List targetData1 = getTargetTableData(tableName,selectedTypeValue, business, weeksBuffer.toString(),weekList,nextFilterVariable,whereClauseStr,targetValue,selectedPeriod,monthlyOrQuarterlyList );
                            	targetData.addAll(targetData1);
                            }
                            dataList.add(targetData);
                            int listSize=valuesList.size();
                            int listSize1=targetData.size();
                            int SizeOfList=0;
                            if(listSize==listSize1){
                            	SizeOfList=listSize;
                            } else if(listSize<listSize1){
                            	SizeOfList=listSize;
                            }
                            if(listSize>listSize1){
                            	SizeOfList=listSize1;
                            }
                            
                            for(int i=1;i<SizeOfList;i++){
                            	String actualValueStr=(String) valuesList.get(i);
                            	String targetValueStr=(String) targetData.get(i);
                            	if(!ApplicationUtil.isEmptyOrNull(targetValueStr) && !targetValueStr.equalsIgnoreCase("--") && !ApplicationUtil.isEmptyOrNull(actualValueStr) ){
                            		double actualValue =Double.parseDouble(actualValueStr);
                                	double targetValue =Double.parseDouble(targetValueStr);
                                	if(targetValue!=0){
                                		double percentageValue=((actualValue/(double)targetValue)*100);
                                		percentageOfTargetsList.add(Math.round(percentageValue)+"%");
                                	}else{
                                		percentageOfTargetsList.add("--");
                                	}
                            	}else{
                            		percentageOfTargetsList.add("--");
                            	}
                            	
                            }
                            dataList.add(percentageOfTargetsList);
                            if(switchForTotalSelectedTypeValues!=0){
                            	catSubcategoryDataMap.put(selectedTypeValue, dataList);
                            } else{
                            	catSubcategoryDataMap.put("total", dataList);
                            }
                            
                            targetTableList.add(catSubcategoryDataMap);
                            targetTablecolorRangeMapObj.put("tableColorRange", tableColorRange);
                    
                   } catch (HibernateException he) {
                           throw he;
                   } catch (ApplicationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	 
                  return targetTableViewMapObj;
            }
		     });
		     logger.debug("Leaving from getReportTableData");
		     return (Map) object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	}
	

	public List getTargetTableData(final String tableName,final String selectedTypeValue,final String business,final String whereClauseStr,final List rawWeekList ,final String nextFilterVariable,final String filterWhereClauseStr,final String btbTargetValue,final String selectedPeriod,final List monthlyOrQuarterlyList){
		try{
			logger.debug("Entered into getTargetTableData");
            Object object = hibernateTemplate.execute(new HibernateCallback() {
            List targetTableList = new ArrayList();
            public Object doInHibernate(Session session)
                         throws HibernateException, SQLException {
                String sql = "";
            	try {
                     		Object[] rowList=null;
                     		//targetTableList.add("Target");
                     		String scoreCardRollupColumn =PropertiesUtil.getProperty("scoreCardRollup");
                     		String monthlyOrQuarterly="";
                     		if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && selectedPeriod.equalsIgnoreCase("Monthly")){
                     			monthlyOrQuarterly="calendarMonth";
                     		}
                     		if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && selectedPeriod.equalsIgnoreCase("Quarterly")){
                     			monthlyOrQuarterly="fiscalQuarter";
                     		}
                     		String filterQuery ="";
                     		if(scoreCardRollupColumn!= null && scoreCardRollupColumn.equalsIgnoreCase(nextFilterVariable)){
                     			filterQuery ="select distinct "+scoreCardRollupColumn+"  from data where "+nextFilterVariable+" in ( "+selectedTypeValue+") "+filterWhereClauseStr;
                     		}else{
                     			filterQuery =selectedTypeValue;
                     		}
                     		
                     		String businessTypeCriteria = " ";
                     		if(!ApplicationUtil.isEmptyOrNull(business)){
                     			businessTypeCriteria="and Biz= '"+business+"'";
                     		}
                     		
                     		if(tableName.equalsIgnoreCase("ForecastingUnits")){
                     			if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                     				sql="select mfc."+monthlyOrQuarterly+", cast(round(sum(t.value)) as char(30)),mfc.year from target_data t,master_fiscal_calender mfc where 1=1 "+businessTypeCriteria+" and GBU in ( "+filterQuery+") and Metric ='Units' and week in ( "+whereClauseStr+") and t.week=mfc.fiscalWeek group by mfc."+monthlyOrQuarterly+" order by mfc.id";
               	       			 
                                	System.out.println("Units"+sql);
                     			} else{
                     				sql="select t.week, cast(round(t.value) as char(30)) from target_data t where 1=1 "+businessTypeCriteria+"  and GBU in ( "+filterQuery+") and Metric ='Units' and week in ( "+whereClauseStr+") group by t.week";
               	       			 
                                	System.out.println("Units"+sql);
                     			}
                   			 	
                            	
                            	
                            }  else if (tableName.equalsIgnoreCase("ForecastingASP")){
                            	
                            	if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                            		
                            		sql="select mfc."+monthlyOrQuarterly+", round(sum(t.value),2),mfc.year from target_data t,master_fiscal_calender mfc where 1=1 "+businessTypeCriteria+"  and GBU in ( "+filterQuery+") and Metric ='ASP' and week in ( "+whereClauseStr+") and t.week=mfc.fiscalWeek group by mfc."+monthlyOrQuarterly+" order by mfc.id";
                            		
                            		/*sql="select mfc."+monthlyOrQuarterly+", round(sum(fb.value * fa.value)/sum(fa.value),2) from (select t.* from target_data t where Biz= '"+business+"' and GBU in ( "+filterQuery+") "
                	                		+ "and Metric ='Units' and week in ( "+whereClauseStr+") group by GBU,week) fa,(select  t.* from target_data t where Biz= '"+business+"' and GBU in "
                	                		+ "( "+filterQuery+") and Metric ='ASP' and week in ( "+whereClauseStr+") group by GBU,week) fb,master_fiscal_calender mfc where fa.week =fb.week and fa.Biz=fb.Biz and fa.week in "
                	                		+ "( "+whereClauseStr+") and fa.GBU =fb.GBU and fa.Biz = '"+business+"' and fa.GBU in ( "+filterQuery+") and fa.week=mfc.fiscalWeek group by mfc."+monthlyOrQuarterly+" order by mfc.id";*/
                	                
                                	System.out.println("ASP"+sql);
                            		
                            	} else{
                            		sql="select t.week, round(t.value,2) from target_data t where 1=1 "+businessTypeCriteria+"  and GBU in ( "+filterQuery+") and Metric ='ASP' and week in ( "+whereClauseStr+") group by t.week";
                            		
                            		/*sql="select fa.week, round(sum(fb.value * fa.value)/sum(fa.value),2) from (select t.* from target_data t where Biz= '"+business+"' and GBU in ( "+filterQuery+") "
                	                		+ "and Metric ='Units' and week in ( "+whereClauseStr+") group by GBU,week) fa,(select  t.* from target_data t where Biz= '"+business+"' and GBU in "
                	                		+ "( "+filterQuery+") and Metric ='ASP' and week in ( "+whereClauseStr+") group by GBU,week) fb where fa.week =fb.week and fa.Biz=fb.Biz and fa.week in "
                	                		+ "( "+whereClauseStr+") and fa.GBU =fb.GBU and fa.Biz = '"+business+"' and fa.GBU in ( "+filterQuery+") group by fa.week";*/
                	                
                                	System.out.println("ASP"+sql);
                            		
                            	}
            	                
                            	
                            } else if (tableName.equalsIgnoreCase("ForecastingRevenue")){
                            	if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                            		
                            		sql="select mfc."+monthlyOrQuarterly+", round(sum(t.value),2),mfc.year from target_data t,master_fiscal_calender mfc where 1=1 "+businessTypeCriteria+"  and GBU in ( "+filterQuery+") and Metric ='Order $' and week in ( "+whereClauseStr+") and t.week=mfc.fiscalWeek group by mfc."+monthlyOrQuarterly+" order by mfc.id";
                            		
                            		/*sql="select mfc."+monthlyOrQuarterly+", round(sum(fb.value * fa.value),2) from (select t.* from target_data t where Biz= '"+business+"' and GBU in ( "+filterQuery+") "
                	                		+ "and Metric ='Units' and week in ( "+whereClauseStr+") group by GBU,week) fa,(select  t.* from target_data t where Biz= '"+business+"' and GBU in "
                	                		+ "( "+filterQuery+") and Metric ='ASP' and week in ( "+whereClauseStr+") group by GBU,week) fb,master_fiscal_calender mfc where fa.week =fb.week and fa.Biz=fb.Biz and fa.week in "
                	                		+ "( "+whereClauseStr+") and fa.GBU =fb.GBU and fa.Biz = '"+business+"' and fa.GBU in ( "+filterQuery+") and fa.week=mfc.fiscalWeek group by mfc."+monthlyOrQuarterly+" order by mfc.id";*/
                                	
                                	System.out.println("check Revenue query"+sql);
                            		
                            	} else{
                            		sql="select t.week, round(t.value,2) from target_data t where 1=1 "+businessTypeCriteria+"  and GBU in ( "+filterQuery+") and Metric ='Order $' and week in ( "+whereClauseStr+") group by t.week";
                            		
                            		/*sql="select fa.week, round(sum(fb.value * fa.value),2) from (select t.* from target_data t where Biz= '"+business+"' and GBU in ( "+filterQuery+") "
                	                		+ "and Metric ='Units' and week in ( "+whereClauseStr+") group by GBU,week) fa,(select  t.* from target_data t where Biz= '"+business+"' and GBU in "
                	                		+ "( "+filterQuery+") and Metric ='ASP' and week in ( "+whereClauseStr+") group by GBU,week) fb where fa.week =fb.week and fa.Biz=fb.Biz and fa.week in "
                	                		+ "( "+whereClauseStr+") and fa.GBU =fb.GBU and fa.Biz = '"+business+"' and fa.GBU in ( "+filterQuery+") group by fa.week";*/
                                	
                                	System.out.println("check Revenue query"+sql);
                            		
                            	}
                            	
                            	
                            } else if (tableName.equalsIgnoreCase("ForecastingESC")){
                            	if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                            		
                            		sql="select mfc."+monthlyOrQuarterly+", round(sum(t.value),2),mfc.year from target_data t,master_fiscal_calender mfc where 1=1 "+businessTypeCriteria+"  and GBU in ( "+filterQuery+") and Metric ='ESC' and week in ( "+whereClauseStr+") and t.week=mfc.fiscalWeek group by mfc."+monthlyOrQuarterly+" order by mfc.id";
                            		
                            		/*sql="select mfc."+monthlyOrQuarterly+", round(sum(fb.value * fa.value) / sum(fa.value),2) from (select t.* from target_data t where Biz= '"+business+"' and GBU in ( "+filterQuery+") "
                	                		+ "and Metric ='Units' and week in ( "+whereClauseStr+") group by GBU,week) fa,(select  t.* from target_data t where Biz= '"+business+"' and GBU in "
                	                		+ "( "+filterQuery+") and Metric ='ESC' and week in ( "+whereClauseStr+") group by GBU,week) fb,master_fiscal_calender mfc where fa.week =fb.week and fa.Biz=fb.Biz and fa.week in "
                	                		+ "( "+whereClauseStr+") and fa.GBU =fb.GBU and fa.Biz = '"+business+"' and fa.GBU in ( "+filterQuery+") and fa.week=mfc.fiscalWeek group by mfc."+monthlyOrQuarterly+" order by mfc.id";*/
                                	
                                	System.out.println("check ESC query"+sql);
                            		
                            	} else{
                            		
                            		sql="select t.week, round(t.value,2) from target_data t where 1=1 "+businessTypeCriteria+"  and GBU in ( "+filterQuery+") and Metric ='ESC' and week in ( "+whereClauseStr+") group by t.week";
                            		
                            		/*sql="select fa.week, round(sum(fb.value * fa.value) / sum(fa.value),2) from (select t.* from target_data t where Biz= '"+business+"' and GBU in ( "+filterQuery+") "
                	                		+ "and Metric ='Units' and week in ( "+whereClauseStr+") group by GBU,week) fa,(select  t.* from target_data t where Biz= '"+business+"' and GBU in "
                	                		+ "( "+filterQuery+") and Metric ='ESC' and week in ( "+whereClauseStr+") group by GBU,week) fb where fa.week =fb.week and fa.Biz=fb.Biz and fa.week in "
                	                		+ "( "+whereClauseStr+") and fa.GBU =fb.GBU and fa.Biz = '"+business+"' and fa.GBU in ( "+filterQuery+") group by fa.week";*/
                                	
                                	System.out.println("check ESC query"+sql);
                            		
                            	}
                            	
                            	
                            	
                            } else {
                            	if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                            		
                            		sql="select mfc."+monthlyOrQuarterly+", round(sum(t.value),2),mfc.year from target_data t,master_fiscal_calender mfc where 1=1 "+businessTypeCriteria+"  and GBU in ( "+filterQuery+") and Metric ='PM%' and week in ( "+whereClauseStr+") and t.week=mfc.fiscalWeek group by mfc."+monthlyOrQuarterly+" order by mfc.id";
                            		
                            		/*sql="select mfc."+monthlyOrQuarterly+", round(((sum(fa.value)-sum(fb.value))/sum(fa.value))*100,2) from (select t.* from target_data t where Biz= '"+business+"' and GBU in ( "+filterQuery+") "
                	                		+ "and Metric ='ASP' and week in ( "+whereClauseStr+") group by GBU,week) fa,(select  t.* from target_data t where Biz= '"+business+"' and GBU in "
                	                		+ "( "+filterQuery+") and Metric ='ESC' and week in ( "+whereClauseStr+") group by GBU,week) fb,master_fiscal_calender mfc where fa.week =fb.week and fa.Biz=fb.Biz and fa.week in "
                	                		+ "( "+whereClauseStr+") and fa.GBU =fb.GBU and fa.Biz = '"+business+"' and fa.GBU in ( "+filterQuery+") and fa.week=mfc.fiscalWeek group by mfc."+monthlyOrQuarterly+" order by mfc.id";*/
                                	
                                	System.out.println("check pm% query"+sql);
                            		
                            	} else{
                            		sql="select t.week, round(t.value,2) from target_data t where 1=1 "+businessTypeCriteria+"  and GBU in ( "+filterQuery+") and Metric ='PM%' and week in ( "+whereClauseStr+") group by t.week";
                            		
                            		/*sql="select fa.week, round(((sum(fa.value)-sum(fb.value))/sum(fa.value))*100,2) from (select t.* from target_data t where Biz= '"+business+"' and GBU in ( "+filterQuery+") "
                	                		+ "and Metric ='ASP' and week in ( "+whereClauseStr+") group by GBU,week) fa,(select  t.* from target_data t where Biz= '"+business+"' and GBU in "
                	                		+ "( "+filterQuery+") and Metric ='ESC' and week in ( "+whereClauseStr+") group by GBU,week) fb where fa.week =fb.week and fa.Biz=fb.Biz and fa.week in "
                	                		+ "( "+whereClauseStr+") and fa.GBU =fb.GBU and fa.Biz = '"+business+"' and fa.GBU in ( "+filterQuery+") group by fa.week";*/
                                	
                                	System.out.println("check pm% query"+sql);
                            		
                            	}
                            	
                            	
                            }
                     		
                     		SQLQuery query = session.createSQLQuery(sql);
                            List targetsList =  (List)query.list();
                            Map targetListMap = new HashMap();
                            if(targetsList != null && targetsList.size()>0){
                            	int size =targetsList.size();
                            	for(int j=0;j<size;j++){
                            		rowList = (Object[])targetsList.get(j);
                            		String actualPeriod="";
                            		String targetValue="0";
                            		if(rowList[0]!=null){
                            			actualPeriod = rowList[0].toString();
                            		}
                            		if(rowList[1]!=null){
                            			targetValue = rowList[1].toString();
                            		} 
                            		if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && selectedPeriod.equalsIgnoreCase("Monthly")){
                            			String year="";
                            			if(rowList[2]!=null){
                            				year = rowList[2].toString();
                                		}
                            			targetListMap.put(actualPeriod+"-"+year, targetValue);
                                    }else{
                                    	targetListMap.put(actualPeriod, targetValue);
                                    }
                            		
                            		/*if(!ApplicationUtil.isEmptyOrNull(btbTargetValue)){
                            			double target=Double.parseDouble(targetValue);
                                    	double btbTarget =Double.parseDouble(btbTargetValue);
                                    	targetTableList.add(target+btbTarget+"");
                         			}else{
                         				targetTableList.add(targetValue);
                         			}*/
                            		
                            	}
                            	
                            	 if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                                 	int listSize1=monthlyOrQuarterlyList.size();
                                 	for(int l=1;l<listSize1;l++){
                                 		
                                 		String monthQuarter=(String) monthlyOrQuarterlyList.get(l);
                                  		String targetValue="";
                                  		if(targetListMap.containsKey(monthQuarter)){
                                  			targetValue= (String) targetListMap.get(monthQuarter);
                                  		} else{
                                  			targetValue="0";
                                  		}
                                  		
                                  		if(!ApplicationUtil.isEmptyOrNull(btbTargetValue)){
                                 			double target=Double.parseDouble(targetValue);
                                         	double btbTarget =Double.parseDouble(btbTargetValue);
                                         	targetTableList.add(target+btbTarget+"");
                              			}else{
                              				targetTableList.add(targetValue);
                              			}
                                  		
                                 		
                                 	}
                                 }else{
                                	 int size1= rawWeekList.size();
                                 	for(int i=1;i<size1;i++){
                                 		String actualPeriod=(String) rawWeekList.get(i);
                                  		String targetValue="";
                                  		if(targetListMap.containsKey(actualPeriod)){
                                  			
                                  			targetValue= (String) targetListMap.get(actualPeriod);
                                  			if(ApplicationUtil.isEmptyOrNull(targetValue)){
                                  				targetValue="0";
                                  			}
                                  		} else{
                                  			targetValue="0";
                                  		}
                                  		if(!ApplicationUtil.isEmptyOrNull(btbTargetValue)){
                                 			double target=Double.parseDouble(targetValue);
                                         	double btbTarget =Double.parseDouble(btbTargetValue);
                                         	targetTableList.add(target+btbTarget+"");
                              			}else{
                              				targetTableList.add(targetValue);
                              			}
                                  		
                                 	}
                                	 
                                 }
                            	
                            	
                            	
                            } else{
                            	int size=0;
                            	if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                            		size= monthlyOrQuarterlyList.size();
                            	}else{
                            		size= rawWeekList.size();
                            	}
                            	
                            	for(int j=1;j<size;j++){
                            		targetTableList.add("--");
                            	}
                            	
                            }
                            
                    
                   } catch (HibernateException he) {
                           throw he;
                   }
            	 
                  return targetTableList;
            }
		     });
		     logger.debug("Leaving from getTargetTableData");
		     return (List) object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public Map getAccuracyReportList(final String noOfWeeks,final String filterCondition,final int reportType,final String businessValue){
		Object object = null;
		logger.debug("Entered into getAccuracyReportList");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException{
						Map jsonViewMapObject=new HashMap();
						List JsonAccuracyList=null;
					try {
						 Connection connection = session.connection();
						 String orderWeek = PropertiesUtil.getProperty("orderWeek");
						 String unitsColumn = PropertiesUtil.getProperty("quantity");
						 String productNumber=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_NUMBER);
						 String aspColumn=PropertiesUtil.getProperty(ApplicationConstants.ASP);
						 String escColumn =PropertiesUtil.getProperty("ESC");
			             String pmColumn =PropertiesUtil.getProperty("productMargin");
						 DecimalFormat numberFormat = new DecimalFormat("#.00");
						 String weekSql="select planningCycle.startWeek,planningCycle.startYear from PlanningCycle planningCycle where planningCycle.planningStatusId = (select masterPlanningStatus.id from MasterPlanningStatus masterPlanningStatus where masterPlanningStatus.logicalName='INITIATED') and planningCycle.workflowStatusId = (select masterWorkflowStatus.id from MasterWorkflowStatus masterWorkflowStatus where masterWorkflowStatus.logicalName='APPROVE')";
						 Query maxWeekQuery=session.createQuery(weekSql);
						 List maxWeekList=maxWeekQuery.list();
						 if(maxWeekList==null || maxWeekList.size()==0){
                              weekSql="select max(planningCycle.startWeek),max(planningCycle.startYear) from PlanningCycle planningCycle where planningCycle.planningStatusId = (select masterPlanningStatus.id from MasterPlanningStatus masterPlanningStatus where masterPlanningStatus.logicalName='CLOSED'))";
                              maxWeekQuery=session.createQuery(weekSql);
                              maxWeekList=maxWeekQuery.list();
                         }
						 
						 Object [] weekArray=(Object[]) maxWeekList.get(0);
						 String maxWeek=weekArray[1]+"-W"+String.format("%02d",(Integer.parseInt(weekArray[0].toString())));
						// List weekList=getForecastWeek(Integer.parseInt(weekArray[1].toString()),Integer.parseInt(weekArray[0].toString()),Integer.parseInt(noOfWeeks));
						 List weekList=planningDAO.getSKUForecastWeek(Integer.parseInt(weekArray[1].toString()), Integer.parseInt(weekArray[0].toString()), Integer.parseInt(noOfWeeks),0);
						 String minWeek=weekList.get(weekList.size()-1).toString();
		       			 List jsonTable= new ArrayList();
		       			 Map actualsMap=new HashMap();
		       			 Map unitsForecastMap=new HashMap();
		       			 Map aspForecastMap=new HashMap();
		       			 Map unitsForecastWithoutOverridenMap=new HashMap();
		       			 Map aspForecastWithoutOverridenMap=new HashMap();
		       			 Map escForecastMap=new HashMap();
		       			 Map pmPercentageForecastMap=new HashMap();
		       			 String businessTypeCriteria="";
		       			 
		       			 String sql = "select d."+orderWeek+", round(sum(d."+unitsColumn+")), round(sum(d."+unitsColumn+" * d."+aspColumn+")/sum(d."+unitsColumn+"),2), "
		       			 				+ "round(sum(d."+unitsColumn+") * (sum(d."+unitsColumn+" * d."+aspColumn+")/sum(d."+unitsColumn+")),2), round(sum(d."+escColumn+" * d."+unitsColumn+")/sum(d."+unitsColumn+"),2), round((sum(d."+aspColumn+") - sum( d."+escColumn+"))/sum(d."+aspColumn+")*100,2)"
		       			 				+ " from data d where 1=1 "+filterCondition+" AND d."+orderWeek+" BETWEEN '"+minWeek+"' AND '"+maxWeek+"' group by d."+orderWeek+" order by d."+orderWeek+"";
	       				 Query q=session.createSQLQuery(sql);
	       				 List availableActualsList=q.list();
	       				 List actualsList=new ArrayList();

	       				 for(int j=0;j<availableActualsList.size();j++){
	       					 	Object [] availableActualsArray = (Object[]) availableActualsList.get(j);
	       					 	actualsMap.put(availableActualsArray[0], availableActualsArray);
	       				 }
		       			 
	       				 if(!ApplicationUtil.isEmptyOrNull(businessValue)){
	       					businessTypeCriteria="and f.business='"+businessValue+"'";
	       				 }
	       				Query unitsForecastQuery = session.createSQLQuery("select t.forecastPeriod,round(sum(t.OriginalForecast)) from (SELECT f.forecastPeriod,f.productId,f.business,f.planningCycleId,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast  "
	                			+ "	FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from  "
	                			+ "	override_units_log group by forecastingUnitsId) b on a.forecastingUnitsId= b.forecastingUnitsId and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.productId in (select distinct d."+productNumber+" from Data d where 1=1 "+filterCondition+") and f.forecastPeriod BETWEEN '"+minWeek+"' AND '"+maxWeek+"' "+businessTypeCriteria+")  t "
	                			+ "	group by t.forecastPeriod");
		       				
	       				List unitsForecastList=unitsForecastQuery.list();
	       				
	       				if(!ApplicationUtil.isEmptyOrNull(businessValue)){
	       					businessTypeCriteria="and t.business='"+businessValue+"'";
	       				 }
	       				
	       				Query unitsForecastWithoutOverriden=session.createSQLQuery("select t.forecastPeriod,round(sum(t.forecastValue)) from forecasting_units t where t.productId in (select distinct d."+productNumber+" from Data d where 1=1 "+filterCondition+") and "
	                					+ "t.forecastPeriod BETWEEN '"+minWeek+"' AND '"+maxWeek+"' "+businessTypeCriteria+" group by t.forecastPeriod");
	       				
	       				List unitsForecastListWithoutOvrriden=unitsForecastWithoutOverriden.list();
	       				
	       				for(int k=0;k<unitsForecastList.size();k++){
       					 	Object [] availableunitsForecastArray = (Object[]) unitsForecastList.get(k);
       					 	unitsForecastMap.put(availableunitsForecastArray[0], availableunitsForecastArray);
       					 	
       					 	Object [] unitsForecastWithoutOverridenArray=(Object[]) unitsForecastListWithoutOvrriden.get(k);
       					 	unitsForecastWithoutOverridenMap.put(unitsForecastWithoutOverridenArray[0], unitsForecastWithoutOverridenArray);
       				 }
	       				
	       				if(!ApplicationUtil.isEmptyOrNull(businessValue)){
	       					businessTypeCriteria="and f.business='"+businessValue+"'";
	       				 }
	       				
	       				Query aspForecastquery = session.createSQLQuery("select fu.forecastPeriod ,case when sum(fu.OriginalForecast * fa.OriginalForecast)/sum(fu.OriginalForecast) is null then 0 else round(sum(fu.OriginalForecast * fa.OriginalForecast)/sum(fu.OriginalForecast),2) end as forecastResult  from (SELECT f.forecastPeriod,f.productId,f.business,f.planningCycleId,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
	       			 			+ " FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from override_units_log group by forecastingUnitsId) b  "
	       			 			+ "on a.forecastingUnitsId= b.forecastingUnitsId and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.productId in (select distinct d."+productNumber+" from Data d where 1=1 "+filterCondition+") and f.forecastPeriod BETWEEN '"+minWeek+"' AND '"+maxWeek+"' "+businessTypeCriteria+") fu, (SELECT f.forecastPeriod,f.productId,f.business,f.planningCycleId,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
	       			 			+ " FROM forecasting_asp f left join (SELECT a.* FROM override_asp_log a join (select forecastingAspId,max(createdDate) as mdate from override_asp_log group by forecastingAspId) b  "
	       			 			+ "on a.forecastingAspId= b.forecastingAspId and a.createdDate=b.mdate) b on f.id = b.forecastingAspId where f.productId in (select distinct d."+productNumber+" from Data d where 1=1 "+filterCondition+") and f.forecastPeriod BETWEEN '"+minWeek+"' AND '"+maxWeek+"' "+businessTypeCriteria+") fa where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business "
								+" group by fu.forecastPeriod");
						
		       			List aspForecastList = aspForecastquery.list();	 
		       			
		       			if(!ApplicationUtil.isEmptyOrNull(businessValue)){
		       				businessTypeCriteria="and fu.business='"+businessValue+"'";
	       				 }
		       			
		       			Query aspForecastWithoutOverriden=session.createSQLQuery("select fu.forecastPeriod ,case when sum(fu.forecastValue * fa.forecastValue)/sum(fu.forecastValue) is null then 0 else round(sum(fu.forecastValue * fa.forecastValue)/sum(fu.forecastValue),2) end as forecastResult"
		       					+ " from Forecasting_Units fu, Forecasting_ASP fa "+
								"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business "+
								"  and fu.productId in (select distinct d."+productNumber+" from Data d where 1=1 "+filterCondition+") and fu.forecastPeriod BETWEEN '"+minWeek+"' AND '"+maxWeek+"' "+businessTypeCriteria+" group by fu.forecastPeriod");
		       					
		       					/*
		       					session.createSQLQuery("select t.forecastPeriod,sum(t.forecastValue) from forecasting_asp t where t.productId in (select distinct d."+productNumber+" from Data d where 1=1 "+filterCondition+") and "
            					+ "t.forecastPeriod BETWEEN '"+minWeek+"' AND '"+maxWeek+"' AND t.planningCycleId = (select max(forecast.planningCycleId) from Forecasting_asp forecast)  group by t.forecastPeriod");*/
   				
		       			List aspForecastListWithoutOvrriden=aspForecastWithoutOverriden.list();
		       			
		       			for(int k=0;k<aspForecastList.size();k++){
       					 	Object [] availableaspForecastArray = (Object[]) aspForecastList.get(k);
       					 	aspForecastMap.put(availableaspForecastArray[0], availableaspForecastArray);
       					 	

       					 	Object [] aspForecastWithoutOverridenArray=(Object[]) aspForecastListWithoutOvrriden.get(k);
       					 	aspForecastWithoutOverridenMap.put(aspForecastWithoutOverridenArray[0], aspForecastWithoutOverridenArray);
       				 }
		       			if(!ApplicationUtil.isEmptyOrNull(businessValue)){
		       				businessTypeCriteria="and f.business='"+businessValue+"'";
	       				 }
		       			
		       		Query revenueForecastQuery=session.createSQLQuery("select fu.forecastPeriod ,round(sum(fu.OriginalForecast * fa.OriginalForecast),2) from (SELECT f.forecastPeriod,f.productId,f.business,f.planningCycleId,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
       			 			+ " FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from override_units_log group by forecastingUnitsId) b  "
       			 			+ "on a.forecastingUnitsId= b.forecastingUnitsId and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.productId in (select distinct d."+productNumber+" from Data d where 1=1 "+filterCondition+") and f.forecastPeriod BETWEEN '"+minWeek+"' AND '"+maxWeek+"' "+businessTypeCriteria+") fu, (SELECT f.forecastPeriod,f.productId,f.business,f.planningCycleId,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
       			 			+ " FROM forecasting_asp f left join (SELECT a.* FROM override_asp_log a join (select forecastingAspId,max(createdDate) as mdate from override_asp_log group by forecastingAspId) b  "
       			 			+ "on a.forecastingAspId= b.forecastingAspId and a.createdDate=b.mdate) b on f.id = b.forecastingAspId where f.productId in (select distinct d."+productNumber+" from Data d where 1=1 "+filterCondition+") and f.forecastPeriod BETWEEN '"+minWeek+"' AND '"+maxWeek+"' "+businessTypeCriteria+") fa "+
							" where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business   "+
							" group by fu.forecastPeriod");
		       		
			       		List revenueForecastList=revenueForecastQuery.list();
			       		Map revenueForecastMap=new HashMap();
			       		for(int k=0;k<revenueForecastList.size();k++){
       					 	Object [] availableRevenueArray = (Object[]) revenueForecastList.get(k);
       					 	revenueForecastMap.put(availableRevenueArray[0], availableRevenueArray);

       				 }
			       		if(!ApplicationUtil.isEmptyOrNull(businessValue)){
			       			businessTypeCriteria="and f.business='"+businessValue+"'";
	       				 }
			       		
			       	Query EscForecastQuery =session.createSQLQuery("select fu.forecastPeriod ,case when sum(fa.forecastValue * fu.OriginalForecast)/sum(fu.OriginalForecast) is null then 0 else round(sum(fa.forecastValue * fu.OriginalForecast)/sum(fu.OriginalForecast),2) end as forecastResult"
			       			+ " from (SELECT  f.forecastPeriod,f.productId,f.business,f.planningCycleId,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
       			 			+ " FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from override_units_log group by forecastingUnitsId) b  "
       			 			+ "on a.forecastingUnitsId= b.forecastingUnitsId and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.productId in (select distinct d."+productNumber+" from Data d where 1=1 "+filterCondition+") and f.forecastPeriod BETWEEN '"+minWeek+"' AND '"+maxWeek+"' "+businessTypeCriteria+") fu, forecasting_esc fa "+
							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business  "+
							" group by fu.forecastPeriod");
			       		
		       			
			       	List escForecastList=EscForecastQuery.list();
		       		for(int l=0;l<escForecastList.size();l++){
   					 	Object [] availableEscArray = (Object[]) escForecastList.get(l);
   					 	escForecastMap.put(availableEscArray[0], availableEscArray);

   				 }
		       		if(!ApplicationUtil.isEmptyOrNull(businessValue)){
		       			businessTypeCriteria="and f.business='"+businessValue+"'";
       				 }
		       		
		       		Query pmPercentForecastQuery = session.createSQLQuery("select fu.forecastPeriod ,case when ((sum(fa.OriginalForecast)-sum(fu.forecastValue))/sum(fa.OriginalForecast))*100 is null then 0 else round(((sum(fa.OriginalForecast)-sum(fu.forecastValue))/sum(fa.OriginalForecast))*100,2) end as forecastResult from forecasting_esc fu, (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
       			 			+ " FROM forecasting_asp f left join (SELECT a.* FROM override_asp_log a join (select forecastingAspId,max(createdDate) as mdate from override_asp_log group by forecastingAspId) b  "
       			 			+ "on a.forecastingAspId= b.forecastingAspId and a.createdDate=b.mdate) b on f.id = b.forecastingAspId where f.productId in (select distinct d."+productNumber+" from Data d where 1=1 "+filterCondition+") and f.forecastPeriod BETWEEN '"+minWeek+"' AND '"+maxWeek+"' "+businessTypeCriteria+") fa "+
							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business "+
							" group by fu.forecastPeriod");
		       		
	       			
		       	List pmPercentageForecastList=pmPercentForecastQuery.list();
	       		for(int l=0;l<pmPercentageForecastList.size();l++){
					 	Object [] availablePmPercentageArray = (Object[]) pmPercentageForecastList.get(l);
					 	pmPercentageForecastMap.put(availablePmPercentageArray[0], availablePmPercentageArray);

				 }
			       	
			       	
			       		List jsonUnitsTable=new ArrayList();
		       			List jsonAspTable=new ArrayList();
		       			List jsonRevenueTable=new ArrayList();
		       			List jsonEscTable=new ArrayList();
		       			List jsonPmPercentageTable=new ArrayList();
		       			
		       			StringBuffer jsonUnitsStringBuffer=null;
		       			StringBuffer jsonAspStringBuffer=null;
		       			StringBuffer jsonRevenueStringBuffer=null;
		       			StringBuffer jsonEscStringBuffer=null;
		       			StringBuffer jsonPmPercentageStringBuffer=null;
		       			StringBuffer jsonHistoricalUnitsStringBuffer=null;
		       			StringBuffer jsonHistoricalAspStringBuffer=null;
		       			StringBuffer jsonHistoricalRevenueStringBuffer=null;
		       			StringBuffer jsonHistoricalEscStringBuffer=null;
		       			StringBuffer jsonHistoricalPmPercentageStringBuffer=null;
		       			if(reportType==1){
		       				 jsonUnitsStringBuffer=new StringBuffer();
			       			 jsonAspStringBuffer=new StringBuffer();
			       			 jsonRevenueStringBuffer=new StringBuffer();
			       			 jsonEscStringBuffer=new StringBuffer();
			       			 jsonPmPercentageStringBuffer=new StringBuffer();
			       			jsonUnitsStringBuffer.append("[\n");
			       			jsonAspStringBuffer.append("[\n");
			       			jsonRevenueStringBuffer.append("[\n");
			       			jsonEscStringBuffer.append("[\n");
			       			jsonPmPercentageStringBuffer.append("[\n");
		       			}
		       			else{
		       				 jsonHistoricalUnitsStringBuffer=new StringBuffer();
			       			 jsonHistoricalAspStringBuffer=new StringBuffer();
			       			 jsonHistoricalRevenueStringBuffer=new StringBuffer();
			       			 jsonHistoricalEscStringBuffer=new StringBuffer();
			       			 jsonHistoricalPmPercentageStringBuffer=new StringBuffer();
			       			jsonHistoricalUnitsStringBuffer.append("[\n");
			       			jsonHistoricalAspStringBuffer.append("[\n");
			       			jsonHistoricalRevenueStringBuffer.append("[\n");
			       			jsonHistoricalEscStringBuffer.append("[\n");
			       			jsonHistoricalPmPercentageStringBuffer.append("[\n");
		       			}
		       			
		       			for(int i=0;i<weekList.size();i++){
		       				 
		       				 List jsonUnitsTableConstructor=new ArrayList();
		       				 List jsonAspTableConstructor=new ArrayList();
		       				 List jsonRevenueTableConstructor=new ArrayList();
		       				 List jsonEscTableConstructor=new ArrayList();
		       				 List jsonPmPercentageTableConstructor=new ArrayList();
		       				 
		       				 String unitsAccuracy=null;
		       				 String aspAccuracy=null;
		       				 String revenueAccuracy=null;
		       				 String escAccuracy=null;
		       				 String pmPercentageAccuracy=null;
		       				 String unitsAccuracyWithoutOverriden=null;
		       				 String aspAccuracyWithoutOverriden=null;
		       				 
		       				 
		       				 jsonUnitsTableConstructor.add(weekList.get(weekList.size()-1-i));
		       				 jsonAspTableConstructor.add(weekList.get(weekList.size()-1-i));
		       				 jsonRevenueTableConstructor.add(weekList.get(weekList.size()-1-i));
		       				 jsonEscTableConstructor.add(weekList.get(weekList.size()-1-i));
		       				 jsonPmPercentageTableConstructor.add(weekList.get(weekList.size()-1-i));
		       				 
		       				 if(actualsMap.containsKey(weekList.get(weekList.size()-1-i))){
		       					actualsList.add((Object[]) actualsMap.get(weekList.get(weekList.size()-1-i)));
		       					Object [] actualsArray=(Object[]) actualsMap.get(weekList.get(weekList.size()-1-i));
		       					if(unitsForecastMap.containsKey(weekList.get(weekList.size()-1-i))){
				       				 Object [] unitsForecastArray = (Object[]) unitsForecastMap.get(weekList.get(weekList.size()-1-i));
				       				 Object [] aspForecastArray = (Object[]) aspForecastMap.get(weekList.get(weekList.size()-1-i));
				       				 Object [] unitsForecastWithoutOverridenArray = (Object[]) unitsForecastWithoutOverridenMap.get(weekList.get(weekList.size()-1-i));
				       				 Object [] aspForecastWithoutOverridenArray = (Object[]) aspForecastWithoutOverridenMap.get(weekList.get(weekList.size()-1-i));
				       				 Object [] revenueForecastArray=(Object[]) revenueForecastMap.get(weekList.get(weekList.size()-1-i));
				       				 Object [] escForecastArray=(Object[]) escForecastMap.get(weekList.get(weekList.size()-1-i));
				       				 Object [] pmPercentageForecastArray=(Object[]) pmPercentageForecastMap.get(weekList.get(weekList.size()-1-i));
				       				if(reportType==1){
				       				 unitsAccuracy=numberFormat.format((Math.abs((Double.parseDouble(actualsArray[1].toString()))-(Double.parseDouble(unitsForecastArray[1].toString())))/ Double.parseDouble(actualsArray[1].toString())) *100);
				       				 aspAccuracy=numberFormat.format((Math.abs((Double.parseDouble(actualsArray[2].toString()))-(Double.parseDouble(aspForecastArray[1].toString())))/ Double.parseDouble(actualsArray[2].toString()))*100);
					       			 unitsAccuracyWithoutOverriden=numberFormat.format((Math.abs((Double.parseDouble(actualsArray[1].toString()))-(Double.parseDouble(unitsForecastWithoutOverridenArray[1].toString())))/ Double.parseDouble(actualsArray[1].toString())) *100);
					       			 aspAccuracyWithoutOverriden=numberFormat.format((Math.abs((Double.parseDouble(actualsArray[2].toString()))-(Double.parseDouble(aspForecastWithoutOverridenArray[1].toString())))/ Double.parseDouble(actualsArray[2].toString()))*100);
					       			 revenueAccuracy=numberFormat.format((Math.abs((Double.parseDouble(actualsArray[3].toString()))-(Double.parseDouble(revenueForecastArray[1].toString())))/ Double.parseDouble(actualsArray[3].toString())) *100);
					       			 escAccuracy=numberFormat.format((Math.abs((Double.parseDouble(actualsArray[4].toString()))-(Double.parseDouble(escForecastArray[1].toString())))/ Double.parseDouble(actualsArray[4].toString())) *100);
					       			 pmPercentageAccuracy=numberFormat.format((Math.abs((Double.parseDouble(actualsArray[5].toString()))-(Double.parseDouble(pmPercentageForecastArray[1].toString())))/ Double.parseDouble(actualsArray[5].toString())) *100);
					       			 jsonUnitsStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: "+unitsAccuracy+", b:"+unitsAccuracyWithoutOverriden+"}");
				       				 jsonAspStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: "+aspAccuracy+", b:"+aspAccuracyWithoutOverriden+"}");
				       				 jsonRevenueStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: "+revenueAccuracy+"}");
				       				 jsonEscStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: "+escAccuracy+"}");
				       				 jsonPmPercentageStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: "+pmPercentageAccuracy+"}");
				       				}
				       				else{
				       				 jsonHistoricalUnitsStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: "+actualsArray[1]+", b:"+unitsForecastArray[1]+"}");
				       				 jsonHistoricalAspStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: "+actualsArray[2]+", b:"+aspForecastArray[1]+"}");
				       				 jsonHistoricalRevenueStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: "+actualsArray[3]+", b:"+revenueForecastArray[1]+"}");
				       				 jsonHistoricalEscStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: "+actualsArray[4]+", b:"+escForecastArray[1]+"}");
				       				 jsonHistoricalPmPercentageStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: "+actualsArray[5]+", b:"+pmPercentageForecastArray[1]+"}");
				       				}
				       				 jsonUnitsTableConstructor.add(actualsArray[1]);
				       				 jsonAspTableConstructor.add(actualsArray[2]);
				       				 jsonRevenueTableConstructor.add(actualsArray[3]);
				       				 jsonEscTableConstructor.add(actualsArray[4]);
				       				 jsonPmPercentageTableConstructor.add(actualsArray[5]);
				       				 
					       			 jsonUnitsTableConstructor.add(Math.round(Double.parseDouble(unitsForecastArray[1].toString())));
				       				 jsonAspTableConstructor.add(aspForecastArray[1]);
				       				 jsonRevenueTableConstructor.add(revenueForecastArray[1]);
				       				 jsonEscTableConstructor.add(escForecastArray[1]);
				       				 jsonPmPercentageTableConstructor.add(pmPercentageForecastArray[1]);
				       				 
				       				 if(reportType==1){
					       				 jsonUnitsTableConstructor.add(unitsAccuracy);
					       				 jsonAspTableConstructor.add(aspAccuracy);
					       				 jsonRevenueTableConstructor.add(revenueAccuracy);
					       				 jsonEscTableConstructor.add(escAccuracy);
					       				 jsonPmPercentageTableConstructor.add(pmPercentageAccuracy);
				       				 }
				       				 
								}else{
									
									jsonUnitsTableConstructor.add(actualsArray[1]);
				       				jsonAspTableConstructor.add(actualsArray[2]);
				       				jsonRevenueTableConstructor.add(actualsArray[3]);
				       				jsonEscTableConstructor.add(actualsArray[4]);
				       				jsonPmPercentageTableConstructor.add(actualsArray[5]);
				       				if(reportType==1){
					       				jsonUnitsStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: '0', b: '0' }");
					       				jsonAspStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: '0', b: '0'  }");
					       			    jsonRevenueStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: '0' }");
					       			    jsonEscStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: '0'}");
					       			    jsonPmPercentageStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: '0' }");
				       				}
				       				else{
				       					jsonHistoricalUnitsStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: "+actualsArray[1]+", b: '0' }");
					       				jsonHistoricalAspStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: "+actualsArray[2]+", b: '0'  }");
					       			    jsonHistoricalRevenueStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: "+actualsArray[3]+", b: '0' }");
					       			    jsonHistoricalEscStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: "+actualsArray[4]+", b: '0'  }");
					       			    jsonHistoricalPmPercentageStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: "+actualsArray[5]+", b: '0' }");
				       				}
				       			    jsonUnitsTableConstructor.add("-");
				       			    jsonAspTableConstructor.add("-");
						       		jsonRevenueTableConstructor.add("-");
						       		jsonEscTableConstructor.add("-");
						       		jsonPmPercentageTableConstructor.add("-");
						       		if(reportType==1){
						       			jsonUnitsTableConstructor.add(0);
					       				jsonAspTableConstructor.add(0);
					       				jsonRevenueTableConstructor.add(0);
					       				jsonEscTableConstructor.add(0);
							       		jsonPmPercentageTableConstructor.add(0);
						       		}
								}
		       				 }
		       				else{
		       					actualsList.add("-");
		       					jsonUnitsTableConstructor.add("-");
			       				jsonAspTableConstructor.add("-");
			       				jsonRevenueTableConstructor.add("-");
			       				jsonEscTableConstructor.add("-");
					       		jsonPmPercentageTableConstructor.add("-");
					       		
					       		if(reportType==1){
					       			jsonUnitsStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: '0', b: '0'}");
				       				jsonAspStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: '0', b: '0'}");
				       			    jsonRevenueStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: '0' }");
				       			    jsonEscStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: '0'}");
				       			    jsonPmPercentageStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: '0'}");
					       		}
			       			    if(unitsForecastMap.containsKey(weekList.get(weekList.size()-1-i))){
				       				 Object [] unitsForecastArray = (Object[]) unitsForecastMap.get(weekList.get(weekList.size()-1-i));
				       				 Object [] aspForecastArray = (Object[]) aspForecastMap.get(weekList.get(weekList.size()-1-i));
				       				 Object [] revenueForecastArray=(Object[]) revenueForecastMap.get(weekList.get(weekList.size()-1-i));
				       				 Object [] escForecastArray=(Object[]) escForecastMap.get(weekList.get(weekList.size()-1-i));
				       				 Object [] pmPercentageForecastArray=(Object[]) pmPercentageForecastMap.get(weekList.get(weekList.size()-1-i));
				       				 jsonUnitsTableConstructor.add(Math.round(Double.parseDouble(unitsForecastArray[1].toString())));
				       				 jsonAspTableConstructor.add(aspForecastArray[1]);
				       				 jsonRevenueTableConstructor.add(revenueForecastArray[1]);
				       				 jsonEscTableConstructor.add(escForecastArray[1]);
						       		 jsonPmPercentageTableConstructor.add(pmPercentageForecastArray[1]);
				       				 if(reportType!=1){
				       					 jsonHistoricalUnitsStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: '0', b: "+unitsForecastArray[1]+"}");
					       				 jsonHistoricalAspStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: '0', b: "+aspForecastArray[1]+"}");
					       			     jsonHistoricalRevenueStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: '0', b: "+revenueForecastArray[1]+" }");
					       			     jsonHistoricalEscStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: '0', b: "+escForecastArray[1]+"}");
					       			     jsonHistoricalPmPercentageStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: '0', b: "+pmPercentageForecastArray[1]+" }");
				       				 }
						       		
				       				
								}
								else{
									 jsonUnitsTableConstructor.add("-");
					       			 jsonAspTableConstructor.add("-");
					       			 jsonRevenueTableConstructor.add("-");
					       			 jsonEscTableConstructor.add("-");
					       			 jsonPmPercentageTableConstructor.add("-");
					       			if(reportType!=1){
					       				 jsonHistoricalUnitsStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: '0', b: '0'}");
					       				 jsonHistoricalAspStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: '0', b: '0'}");
					       			     jsonHistoricalRevenueStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: '0', b: '0' }");
					       			     jsonHistoricalEscStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: '0', b:'0' }");
					       			     jsonHistoricalPmPercentageStringBuffer.append("{ w: '"+weekList.get(weekList.size()-1-i).toString()+"', a: '0', b:'0' }");
					       			}
					       			 
								}
			       			 if(reportType==1){
				       				jsonUnitsTableConstructor.add(0);
				       				jsonAspTableConstructor.add(0);
				       				jsonRevenueTableConstructor.add(0);
				       				jsonEscTableConstructor.add(0);
					       			jsonPmPercentageTableConstructor.add(0);
			       			   }
		       				 }
		       				 
		       				if(i!=weekList.size()-1){
		       					if(reportType==1){
		       						jsonUnitsStringBuffer.append(",");
			       					jsonAspStringBuffer.append(",");
			       					jsonRevenueStringBuffer.append(",");
			       					jsonEscStringBuffer.append(",");
			       					jsonPmPercentageStringBuffer.append(",");
		       					}else{
		       						jsonHistoricalUnitsStringBuffer.append(",");
			       					jsonHistoricalAspStringBuffer.append(",");
			       					jsonHistoricalRevenueStringBuffer.append(",");
			       					jsonHistoricalEscStringBuffer.append(",");
			       					jsonHistoricalPmPercentageStringBuffer.append(",");
		       					}
		       					
		       				}
		       				 //--------------------JSON Table--------------//
		       				
		       				
		       				 jsonUnitsTable.add(jsonUnitsTableConstructor);
		       				 jsonAspTable.add(jsonAspTableConstructor);
		       				 jsonRevenueTable.add(jsonRevenueTableConstructor);
		       				 jsonEscTable.add(jsonEscTableConstructor);
		       				 jsonPmPercentageTable.add(jsonPmPercentageTableConstructor);
		       				 
		       			 }
		       			if(reportType==1){
		       				jsonUnitsStringBuffer.append("]\n");
			       			jsonAspStringBuffer.append("]\n");
			       			jsonRevenueStringBuffer.append("]\n");
			       			jsonEscStringBuffer.append("]\n");
	       					jsonPmPercentageStringBuffer.append("]\n");
		       			}else{
		       				jsonHistoricalUnitsStringBuffer.append("]\n");
			       			jsonHistoricalAspStringBuffer.append("]\n");
			       			jsonHistoricalRevenueStringBuffer.append("]\n");
			       			jsonHistoricalEscStringBuffer.append("]\n");
	       					jsonHistoricalPmPercentageStringBuffer.append("]\n");
		       			}
		       			
		       			List JsonHistoricalReportList=null;
		       			
		       			if(reportType==1){
		       				JsonAccuracyList=new ArrayList();
		       				JsonAccuracyList.add(jsonUnitsStringBuffer);
			       			JsonAccuracyList.add(jsonAspStringBuffer);
			       			JsonAccuracyList.add(jsonRevenueStringBuffer);
			       			JsonAccuracyList.add(jsonEscStringBuffer);
			       			JsonAccuracyList.add(jsonPmPercentageStringBuffer);
		       			}else{
		       				JsonHistoricalReportList=new ArrayList();
		       				JsonHistoricalReportList.add(jsonHistoricalUnitsStringBuffer);
			       			JsonHistoricalReportList.add(jsonHistoricalAspStringBuffer);
			       			JsonHistoricalReportList.add(jsonHistoricalRevenueStringBuffer);
			       			JsonHistoricalReportList.add(jsonHistoricalEscStringBuffer);
			       			JsonHistoricalReportList.add(jsonHistoricalPmPercentageStringBuffer);
		       			}
		       			jsonTable.add(jsonUnitsTable);
		       			jsonTable.add(jsonAspTable);
		       			jsonTable.add(jsonRevenueTable);
		       			jsonTable.add(jsonEscTable);
		       			jsonTable.add(jsonPmPercentageTable);
		       			
		       			jsonViewMapObject.put(ApplicationConstants.ACTUALS_LIST, actualsList);
		       			jsonViewMapObject.put(ApplicationConstants.FORECASTED_ASP_LIST, aspForecastList);
		       			jsonViewMapObject.put(ApplicationConstants.FORECASTED_UNITS_LIST, unitsForecastList);
		       			jsonViewMapObject.put(ApplicationConstants.FORECASTED_REVENUE_LIST, revenueForecastList);
		       			jsonViewMapObject.put(ApplicationConstants.ACCURACY_LIST, JsonAccuracyList);
		       			jsonViewMapObject.put(ApplicationConstants.HISTORICAL_REPORT_LIST, JsonHistoricalReportList);
		       			jsonViewMapObject.put("jsonTableList", jsonTable);
		       			
					} catch (HibernateException he) {
						logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
						logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
						throw he;
					} catch (Exception e) {
						logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
						logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
						throw new HibernateException(e);
					}
					
					return jsonViewMapObject;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
		logger.debug("Leaving from getAccuracyReportList");
		return (Map)object;
	}

	@SuppressWarnings("unchecked")
	public List getOverrideReport(final Integer planningCycleId,final String productId,final String modelId,final String categoryId,final String whereCondition) throws ApplicationException{
		try{
			logger.debug("Entered into getOverrideReport");
            Object object = hibernateTemplate.execute(new HibernateCallback() {
           StringBuffer forecastProductBuffer = new StringBuffer();
           StringBuffer forecastModelBuffer = new StringBuffer();
            StringBuffer forecastCategoryBuffer = new StringBuffer();
            public Object doInHibernate(Session session)
                         throws HibernateException, SQLException {
        		final List overrideReportProductList = new ArrayList();
        		final List overrideReportModelList = new ArrayList();
        		final List overrideReportCategoryList = new ArrayList();
        		Map<String,String> forecastingUnitsMap=new HashMap();
        		String categoryColumn = PropertiesUtil.getProperty(ApplicationConstants.CATEGORY);
        		String modelColumn = PropertiesUtil.getProperty(ApplicationConstants.MODEL);
        		String skuColumn=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_NUMBER);
                String sql = "";
                String sql1 = "";
                String sql2 = "";
            	try {
						Object[] rowList = null;
						//For SKU Level
						if(productId!=null){

						sql = "select   u.username,u.firstName,f.forecastPeriod,f.productId,lc.comment "
								+ " from  users U "
								+ "	LEFT JOIN override_units_log O on O.userId=U.login_id "
								+ "LEFT JOIN forecasting_units F on O.forecastingUnitsId=F.id "
								+ " left join override_units_log_comments lc on lc.id=o.overrideUnitsLogCommentId "
								+ " where  f.productId='"
								+ productId
								+ "' and f.planningCycleId="+planningCycleId
								+ " and O.overrideLevel= 'PRODUCT'"
								+"	group by forecastPeriod";
						
						
    					SQLQuery q =session.createSQLQuery(sql) ;
    					
   	        			 List dataList =  q.list();
   	        			 if(dataList != null){
   		                     	int size =dataList.size();
   		                     	String commentString=null;
   		                     	for(int i=0;i<size;i++){
   		                     		Object temp[]=(Object[]) dataList.get(i);
   		                     		String userName=(String) temp[1];
   		                     		String forecastPeriod=(String)temp[2];
   		                     		String productId=(String)temp[3];
   		                     		String comment=(String)temp[4];
   		                     		if(forecastingUnitsMap.containsKey(userName)){
   		                     			String value=(String)forecastingUnitsMap.get(userName);	
   		                     			if(!value.equalsIgnoreCase(forecastPeriod)){
   		                     				String newValue=forecastPeriod+","+value;
   		                     				forecastingUnitsMap.put(userName,newValue);
   		                     			}
   		                     		}else{
   		                     			forecastingUnitsMap.put(userName,forecastPeriod);
   		                     		}
   		                     	
								String temp1=forecastProductBuffer.toString();
									if(!temp1.contains(userName)){
										forecastProductBuffer.append("<br><font color='green'><b>SKU:</b></font> " + productId + " <b>updated by</b> " + userName+ " <b>for Week</b> " + forecastPeriod);
										if(i==size-1){
											commentString=comment;
		   		                     	}
									}else{
										//if(!temp1.contains(s))
										forecastProductBuffer.append(","+forecastPeriod);
										if(ApplicationUtil.isEmptyOrNull(commentString)){
											commentString+=comment;
										}
										else{
											if(!commentString.equalsIgnoreCase(comment))
												commentString+=","+comment;
										}
									}
									if(i==size-1){
										forecastProductBuffer.append("<br><font color='blue'><b>Comment:</b></font>"+comment);
									}
							}
   		                     overrideReportProductList.add(forecastProductBuffer);
						}
						

						sql1 = "select   u.username,u.firstName,f.forecastPeriod,f.productId,lc.comment "
								+ " from  users U "
								+ "	LEFT JOIN override_units_log O on O.userId=U.login_id "
								+ "	LEFT JOIN forecasting_units F on O.forecastingUnitsId=F.id "
								+ " left join override_units_log_comments lc on lc.id=o.overrideUnitsLogCommentId "
								+ " where  f.productId='" + productId + "' and f.planningCycleId="+planningCycleId+" "
								+ " and O.overrideLevel = 'MODEL'"
								+"	group by forecastPeriod";
						
						
    					SQLQuery q1 =session.createSQLQuery(sql1) ;
    					
   	        			 List dataList1 =  q1.list();
   	        			 if(dataList1 != null){
   		                     	int size =dataList1.size();
   		                        String modelCommentString=null;
   		                     	for(int i=0;i<size;i++){
   		                     		Object temp[]=(Object[]) dataList1.get(i);
   		                     		String userName=(String) temp[1];
   		                     		String forecastPeriod=(String)temp[2];
   		                     		String productId=(String)temp[3];
   		                     		String comment=(String)temp[4];
   		                     		if(forecastingUnitsMap.containsKey(userName)){
   		                     			String value=(String)forecastingUnitsMap.get(userName);	
   		                     			if(!value.equalsIgnoreCase(forecastPeriod)){
   		                     				String newValue=forecastPeriod+","+value;
   		                     				forecastingUnitsMap.put(userName,newValue);
   		                     			}
   		                     		}else{
   		                     			forecastingUnitsMap.put(userName,forecastPeriod);
   		                     		}
   		                     		
   		                     	String modelBuffer=forecastModelBuffer.toString();
   		                     	if(!(modelBuffer.contains(userName))){
   		                     		forecastModelBuffer.append("<br><font color='green'><b>Model:</b></font> " + modelId + " <b>updated by</b> " + userName+ " <b>for Week</b> " + forecastPeriod);
	   		                     	if(i==size-1){
	   		                     		modelCommentString=comment;
	   		                     	}
   		                     	}else{
									forecastModelBuffer.append(","+forecastPeriod);
									if(ApplicationUtil.isEmptyOrNull(modelCommentString)){
										modelCommentString+=comment;
									}
									else{
										if(!modelCommentString.equalsIgnoreCase(comment))
											modelCommentString+=","+comment;
									}
								}
   		                     	if(i==size-1){
   		                     		forecastModelBuffer.append("<br><font color='blue'><b>Comment:</b></font> "+comment);
								}
   		                     }
   		                     	
   		                     overrideReportProductList.add(forecastModelBuffer);
                            }
						
						sql2 = "select   u.username,u.firstName,f.forecastPeriod,f.productId,lc.comment "
								+ " from  users U "
								+ "	LEFT JOIN override_units_log O on O.userId=U.login_id "
								+ " LEFT JOIN forecasting_units F on O.forecastingUnitsId=F.id "
								+ " left join override_units_log_comments lc on lc.id=o.overrideUnitsLogCommentId "
								+ " where  f.productId='" + productId + "' and f.planningCycleId="+planningCycleId
								+ " and O.overrideLevel = 'CATEGORY'"
								+"	group by forecastPeriod";
                     		
                     		
    					SQLQuery q2 =session.createSQLQuery(sql2) ;
    					
   	        			 List dataList2 =  q2.list();
   	        			 if(dataList2 != null){
   	        				 //Map forecastingUnitsMap=new HashMap();
   		                     	int size =dataList2.size();
   		                     	String categoryCommentString=null;
   		                     	for(int i=0;i<size;i++){
   		                     		Object temp[]=(Object[]) dataList2.get(i);
   		                     		String userName=(String) temp[1];
   		                     		String forecastPeriod=(String)temp[2];
   		                     		String productId=(String)temp[3];
   		                     		String comment=(String)temp[4];
   		                     		if(forecastingUnitsMap.containsKey(userName)){
   		                     			String value=(String)forecastingUnitsMap.get(userName);	
   		                     			if(!value.equalsIgnoreCase(forecastPeriod)){
   		                     				String newValue=forecastPeriod+","+value;
   		                     				forecastingUnitsMap.put(userName,newValue);
   		                     			}
   		                     		}else{
   		                     			forecastingUnitsMap.put(userName,forecastPeriod);
   		                     		}
   		                     		String categoryBuffer=forecastCategoryBuffer.toString();
								if (!categoryBuffer.contains(userName)) {
									forecastCategoryBuffer.append("<br><font color='green'><b>Category:</b></font> "
											+ categoryId + " <b>updated by</b> " + userName
											+ " <b>for Week</b> " + forecastPeriod);
									if(i==size-1){
										categoryCommentString=comment;
	   		                     	}
								} else {
									forecastCategoryBuffer.append(","
											+ forecastPeriod);
									if(ApplicationUtil.isEmptyOrNull(categoryCommentString)){
										categoryCommentString+=comment;
									}
									else{
										if(!categoryCommentString.equalsIgnoreCase(comment))
											categoryCommentString+=","+comment;
									}

								}
								if(i==size-1){
									forecastCategoryBuffer.append("<br><font color='blue'><b>Comment:</b></font> " +comment);
								}

   		                     	}
   		                     overrideReportProductList.add(forecastCategoryBuffer);
   	        			 }
            	}
				    //For Model		
						if(productId==null && modelId!=null){
							String s="select "+skuColumn+" from data "+ ""
									+ " where "+modelColumn+"= '"+ modelId + "' "+ whereCondition +" ";
							sql="select  u.username,u.firstName,f.forecastPeriod,f.productId,lc.comment	"
							+" from  users U "
							+" LEFT JOIN override_units_log O on O.userId=U.login_id	"
                            +" LEFT JOIN forecasting_units F on O.forecastingUnitsId=F.id	"
							+" left join override_units_log_comments lc on lc.id=o.overrideUnitsLogCommentId	"
							+" where  f.productId in ("+s+")  and f.planningCycleId="+planningCycleId
							+" and O.overrideLevel = 'MODEL'	"
							+" group by forecastPeriod";
							
							
							SQLQuery q =session.createSQLQuery(sql) ;
	    					
	   	        			 List dataList =  q.list();
	   	        			 if(dataList != null){
	   		                     	int size =dataList.size();
	   		                     	String modelCommentString=null;
	   		                     	for(int i=0;i<size;i++){
	   		                     		Object temp[]=(Object[]) dataList.get(i);
	   		                     		String userName=(String) temp[1];
	   		                     		String forecastPeriod=(String)temp[2];
	   		                     		String productId=(String)temp[3];
	   		                     		String comment=(String)temp[4];
	   		                     		if(forecastingUnitsMap.containsKey(userName)){
	   		                     			String value=(String)forecastingUnitsMap.get(userName);	
	   		                     			if(!value.equalsIgnoreCase(forecastPeriod)){
	   		                     				String newValue=forecastPeriod+","+value;
	   		                     				forecastingUnitsMap.put(userName,newValue);
	   		                     			}
	   		                     		}else{
	   		                     			forecastingUnitsMap.put(userName,forecastPeriod);
	   		                     		}
	   		                     		
	   		                     		String modelBuffer=forecastModelBuffer.toString();
	   		                     	if(!modelBuffer.contains(userName)){
	   		                     		forecastModelBuffer.append("<br><font color='green'><b>Model:</b></font> " + modelId + " <b>updated by</b> " + userName+ " <b>for Week</b> " + forecastPeriod);
		   		                     	if(i==size-1){
		   		                     		modelCommentString=comment;
		   		                     	}
	   		                     	}else{
										
										forecastModelBuffer.append(","+forecastPeriod);
										if(ApplicationUtil.isEmptyOrNull(modelCommentString)){
											modelCommentString+=comment;
										}
										else{
											if(!modelCommentString.equalsIgnoreCase(comment))
												modelCommentString+=","+comment;
										}
										
									}
	   		                     	if(i==size-1){
	   		                     		forecastModelBuffer.append("<br><font color='blue'><b>Comment:</b></font> " +comment);
	   		                     	}
	   		                     		
	   		                     	}
	   		                     	
	   		                     overrideReportProductList.add(forecastModelBuffer);
	                            }
							
							
							sql1="select   u.username,u.firstName,f.forecastPeriod,f.productId,lc.comment"
									+"	from  users U	"
									+"	LEFT JOIN override_units_log O on O.userId=U.login_id	"
		                            +"	LEFT JOIN forecasting_units F on O.forecastingUnitsId=F.id	"
									+"	left join override_units_log_comments lc on lc.id=o.overrideUnitsLogCommentId	"
									+"	where  f.productId in  ("+ s +") and f.planningCycleId="+planningCycleId
									+"	and O.overrideLevel = 'CATEGORY'	"
									+"	group by forecastPeriod";
							
							SQLQuery q1 =session.createSQLQuery(sql1) ;
	    					
	   	        			 List dataList1 =  q1.list();
	   	        			 if(dataList1 != null){
	   		                     	int size =dataList1.size();
	   		                     	String categoryCommentString=null;
	   		                     	for(int i=0;i<size;i++){
	   		                     		Object temp[]=(Object[]) dataList1.get(i);
	   		                     		String userName=(String) temp[1];
	   		                     		String forecastPeriod=(String)temp[2];
	   		                     		String productId=(String)temp[3];
	   		                     		String comment=(String)temp[4];
	   		                     		if(forecastingUnitsMap.containsKey(userName)){
	   		                     			String value=(String)forecastingUnitsMap.get(userName);	
	   		                     			if(!value.equalsIgnoreCase(forecastPeriod)){
	   		                     				String newValue=forecastPeriod+","+value;
	   		                     				forecastingUnitsMap.put(userName,newValue);
	   		                     			}
	   		                     		}else{
	   		                     			forecastingUnitsMap.put(userName,forecastPeriod);
	   		                     		}
	   		                     		
	   		                     		String categoryBuffer=forecastCategoryBuffer.toString();
	   		                     	if(!(categoryBuffer.contains(userName))){
	   		                     		forecastCategoryBuffer.append("<br><font color='green'><b>Category:</b></font> " + categoryId + " <b>updated by</b> " + userName+ " for Week " + forecastPeriod);
	   		                     	if(i==size-1){
	   		                     		categoryCommentString=comment;
	   		                     	}
	   		                     	}else{
										forecastCategoryBuffer.append(","+forecastPeriod);
										if(ApplicationUtil.isEmptyOrNull(categoryCommentString)){
											categoryCommentString+=comment;
										}
										else{
											if(!categoryCommentString.equalsIgnoreCase(comment))
												categoryCommentString+=","+comment;
										}
									}
	   		                     	if(i==size-1){
	   		                     		forecastCategoryBuffer.append("<br><font color='blue'><b>Comment:</b></font> "+categoryCommentString);
	   		                     	}
	   		                     	}
	   		                     	
	   		                     overrideReportProductList.add(forecastCategoryBuffer);
	                            }
						
						
						}
						
						
						//For Category
						
						if(modelId==null && productId==null && categoryId!=null){
							String s="select "+skuColumn+" from data "+ ""
									+ " where "+categoryColumn+"= '"+ categoryId + "'  "+ whereCondition + " ";
							sql="select   u.username,u.firstName,f.forecastPeriod,f.productId,lc.comment "
									+"	from  users U	"
									+"	LEFT JOIN override_units_log O on O.userId=U.login_id	"
		                            +"	LEFT JOIN forecasting_units F on O.forecastingUnitsId=F.id	"
									+"	left join override_units_log_comments lc on lc.id=o.overrideUnitsLogCommentId	"
									+"	where  f.productId in ("+ s +")	 and f.planningCycleId="+planningCycleId
									+"	and O.overrideLevel = 'CATEGORY'	"
									+"	group by forecastPeriod";
							
							SQLQuery q =session.createSQLQuery(sql) ;
	    					
	   	        			 List dataList =  q.list();
	   	        			 if(dataList != null){
	   		                     	int size =dataList.size();
	   		                     	String categoryCommentString=null;
	   		                     	for(int i=0;i<size;i++){
	   		                     		Object temp[]=(Object[]) dataList.get(i);
	   		                     		String userName=(String) temp[1];
	   		                     		String forecastPeriod=(String)temp[2];
	   		                     		String productId=(String)temp[3];
	   		                     		String comment=(String)temp[4];
	   		                     		if(forecastingUnitsMap.containsKey(userName)){
	   		                     			String value=(String)forecastingUnitsMap.get(userName);	
	   		                     			if(!value.equalsIgnoreCase(forecastPeriod)){
	   		                     				String newValue=forecastPeriod+","+value;
	   		                     				forecastingUnitsMap.put(userName,newValue);
	   		                     			}
	   		                     		}else{
	   		                     			forecastingUnitsMap.put(userName,forecastPeriod);
	   		                     		}
	   		                     		String categoryBuffer=forecastCategoryBuffer.toString();
	   		                     		
	   		                     	if(!categoryBuffer.contains(userName)){
	   		                     		forecastCategoryBuffer.append("<br><font color='green'><b>Category:</b></font> " + categoryId + " <b>updated by</b> " + userName+ " <b>for Week</b> " + forecastPeriod);
		   		                     	if(i==size-1){
		   		                     		categoryCommentString=comment;
		   		                     	}
	   		                     	}else{
										forecastCategoryBuffer.append(","+forecastPeriod);
										if(ApplicationUtil.isEmptyOrNull(categoryCommentString)){
											categoryCommentString+=comment;
										}
										else{
											if(!categoryCommentString.equalsIgnoreCase(comment))
												categoryCommentString+=","+comment;
										}
										
									}
	   		                     	if(i==size-1){
	   		                     		forecastCategoryBuffer.append("<br><font color='blue'><b>Comment:</b></font> "+comment);
	   		                     	}
	   		                     	}
	   		                     	
	   		                     overrideReportProductList.add(forecastCategoryBuffer);
	                            }
						}
                   } catch (HibernateException he) {
                	   logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
                   }
            	 
                  return overrideReportProductList;
            }
		     });
		     logger.debug("Leaving from getOverrideReport");
		     return (List) object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Map getClosedReportPlanningCycleListMap(final boolean includeActiveCycle) throws ApplicationException{
		Object object = null;
		final List planningCycleList = new ArrayList();
		final Map closedReportPlanningCycleListMap=new HashMap();
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
						String sql = null;
						if(includeActiveCycle){
							sql="select  p1.id,p1.startWeek,p1.startYear from Planning_Cycle p1 order by p1.id desc";
						}else{
							sql="select id,startWeek,startYear from (select  p1.id,p1.startWeek,p1.startYear from Planning_Cycle p1,Master_Planning_Status m1 where p1.planningStatusId=m1.id and m1.logicalName='CLOSED'"
									+ " union all select  p1.id,p1.startWeek,p1.startYear from Planning_Cycle p1,Master_Planning_Status m1, Master_Workflow_Status mws where p1.workflowstatusid=mws.id and mws.logicalname='APPROVE' and p1.planningStatusId=m1.id and m1.logicalName='INITIATED') cycle order by cycle.id desc";
						}
						SQLQuery query = session.createSQLQuery(sql);
						List closedCycleList = query.list();
	                     if(closedCycleList != null){
	                     	int size =closedCycleList.size();
	                     	for(int i=0;i<size;i++){
	                     		Object[] rowList = (Object[])closedCycleList.get(i);
	                     		PlanningCycle planningCycle = new PlanningCycle();
	                     		planningCycle.setId((int)rowList[0]);
	                     		String week = (String)rowList[1];
	                     		String year = (String)rowList[2];
	                     		String weekYear = year+"-W"+week;
	                     		planningCycle.setStartWeek(week);
	                     		planningCycle.setStartYear(year);
	                     		planningCycle.setWeekYear(weekYear);
	                     		planningCycleList.add(planningCycle);
	                     		closedReportPlanningCycleListMap.put((int)rowList[0], planningCycle);
	                     	}
	                     	closedReportPlanningCycleListMap.put("closedPlanningCycleList", planningCycleList);
	                     }
						} catch (HibernateException he) {
							logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
					}
					return (Object)closedReportPlanningCycleListMap;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "saveContact", 
					ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
		return (Map)closedReportPlanningCycleListMap;
	}
	
	public final List<String> getForecastWeeks(final int year,final int week,int range) throws ApplicationException{
		logger.debug("Entered into getForecastWeeks in planningDAO");
		int baseYear = 0;
		int baseWeek = 0;
		List <String> weeksList = new ArrayList<String>();
		try{
			int i = 0;		
						for (; i < range ; i++)
						{
							
							if(year % 4 == 0 || year % 400 == 0 )
							{
								baseWeek = 53;
							}
							else
							{
								baseWeek = 52;
							}
							if(week+i > baseWeek)
							{
								baseYear = year+1;
								baseWeek =(1 * ( week + i ) ) - baseWeek ;
							}
							else
							{
								baseYear = year;
								baseWeek = week+i;
							}
							if(baseWeek < 10)	
							{
								weeksList.add(baseYear+"-W0"+baseWeek);
							}
							else
							{
								weeksList.add(baseYear+"-W"+baseWeek);
							}
						}
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
		}
		logger.debug("Leaving from getForecastWeeks method in planningDAO");
		return weeksList;
	}
	
	
	public List getMonthlyQuarterlyNames(final String weeksListStr,final String selectedPeriod) throws ApplicationException
	{
		try{
		logger.debug("Entered into getMonthlyQuarterlyNames");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
						String monthlyOrQuarterly="";
				 		if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && selectedPeriod.equalsIgnoreCase("Monthly")){
				 			monthlyOrQuarterly="calendarMonth";
				 		}
				 		if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && selectedPeriod.equalsIgnoreCase("Quarterly")){
				 			monthlyOrQuarterly="fiscalQuarter";
				 		}
						List  monthOrQuarterList = new ArrayList();
						String sql ="select distinct "+monthlyOrQuarterly+" from MasterFiscalCalendar where fiscalWeek in ("+weeksListStr+") order by id";
		        		Query q = session.createQuery(sql);
		        		List queryList= q.list();
		        		if(queryList!=null){
		        			int size=queryList.size();
		        			for(int i=0;i<size;i++){
		        				monthOrQuarterList.add((String)queryList.get(i));
		        			}
		        			
		        		}
		        			 
	        			return monthOrQuarterList;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from getMonthlyQuarterlyNames");
	    return (List)object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "getUserFromUserName", 
					ApplicationErrorCodes.APP_EC_22, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
	}
	
	public List getMonthlyQuarterlyNamesWithYear(final String weeksListStr,final String selectedPeriod) throws ApplicationException
	{
		try{
		logger.debug("Entered into getMonthlyQuarterlyNamesWithYear");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
						String monthlyOrQuarterly="";
				 		if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && selectedPeriod.equalsIgnoreCase("Monthly")){
				 			monthlyOrQuarterly="calendarMonth";
				 		}
				 		if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && selectedPeriod.equalsIgnoreCase("Quarterly")){
				 			monthlyOrQuarterly="fiscalQuarter";
				 		}
						List  monthOrQuarterList = new ArrayList();
						String sql ="select distinct "+monthlyOrQuarterly+",year from MasterFiscalCalendar where fiscalWeek in ("+weeksListStr+") order by id";
		        		Query q = session.createQuery(sql);
		        		List queryList= q.list();
		        		if(queryList!=null){
		        			int size=queryList.size();
		        			for(int i=0;i<size;i++){
		        				Object[] rowList = (Object[])queryList.get(i);
		        				monthOrQuarterList.add(rowList[0].toString()+"-"+rowList[1].toString());
		        			}
		        			
		        		}
		        			 
	        			return monthOrQuarterList;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from getMonthlyQuarterlyNamesWithYear");
	    return (List)object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "getUserFromUserName", 
					ApplicationErrorCodes.APP_EC_22, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
	}
		
	public Map getHistoricalActualReportChartJsonForAggregated(final String tableName,final String selectedTypeVariable,final int planningCycelId,final String selectedTypeValue,final Integer userId , final String business,final String whereClauseStr,final List rolesList,final Integer week,final Integer year,final Integer range,final String selectedPeriod){
		try{
			logger.debug("Entered into getHistoricalActualReportChartJsonForAggregated");
	        Object object = hibernateTemplate.execute(new HibernateCallback() {
	        public Object doInHibernate(Session session)
	                     throws HibernateException, SQLException {
	        	Map jsonViewMapObj = new HashMap();
	            StringBuffer forecastBuffer = new StringBuffer();
	            forecastBuffer.append("[ \n");
	            String sql = "";
	            String sql1="";
	            String orderWeek = PropertiesUtil.getProperty("orderWeek");
	            String productIdColumn =PropertiesUtil.getProperty("skuListId");
	            String unitsColumn = PropertiesUtil.getProperty("quantity");
	            String aspColumn = PropertiesUtil.getProperty("ASP");
	            String escColumn =PropertiesUtil.getProperty("ESC");
	            String pmColumn =PropertiesUtil.getProperty("productMargin");
	            String businessColumn= PropertiesUtil.getProperty("business");
	            String categoryColumn = PropertiesUtil.getProperty(ApplicationConstants.CATEGORY);
	            List jsonTableList = new ArrayList();
	            StringBuffer actualWeeksBuffer = new StringBuffer();
	            List actualWeeksList=null;
	            String businessTypeCriteria=" ";
				try {
					actualWeeksList = planningDAO.getSKUForecastWeek(year,week,range,1);
					int actualsSize=actualWeeksList.size();
	         		for(int t=0;t<actualsSize;t++){
	         			actualWeeksBuffer.append("'"+actualWeeksList.get(t)+"'");
	                	if(t<actualsSize-1){
	                		actualWeeksBuffer.append(",");
	                	}
	         		}
				} catch (ApplicationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String monthlyOrQuarterly="";
         		if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && selectedPeriod.equalsIgnoreCase("Monthly")){
         			monthlyOrQuarterly="calendarMonth";
         		}
         		if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && selectedPeriod.equalsIgnoreCase("Quarterly")){
         			monthlyOrQuarterly="fiscalQuarter";
         		}
         		
         		
         		 if(!ApplicationUtil.isEmptyOrNull(business)){
    					businessTypeCriteria="and d."+businessColumn+" = '"+business+"'";
    				 }
         		 
	            if(tableName.equalsIgnoreCase("ForecastingUnits")){
					if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly") ){
						sql = "select mfc."+monthlyOrQuarterly+",sum(d."+unitsColumn+") from RawData d,MasterFiscalCalendar mfc where d."+selectedTypeVariable+" = '"+selectedTypeValue+"' "+businessTypeCriteria+" "
	                			+"  and d."+orderWeek+"=mfc.fiscalWeek  and d."+orderWeek+" in ("+actualWeeksBuffer+") "+whereClauseStr+" group by mfc."+monthlyOrQuarterly+" order by mfc.id";
						
					}else{
						sql = "select d."+orderWeek+",sum(d."+unitsColumn+") from RawData d where d."+selectedTypeVariable+" = '"+selectedTypeValue+"' "+businessTypeCriteria+"  and d."+orderWeek+" in ("+actualWeeksBuffer+")  "
	                			+" and d."+orderWeek+" in ("+actualWeeksBuffer+") "+whereClauseStr+" group by d."+orderWeek;
						
					}
                	
                	
                	
                }  else if (tableName.equalsIgnoreCase("ForecastingASP")){
                	if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly") ){
                		sql = "select mfc."+monthlyOrQuarterly+",cast(round(sum(d."+unitsColumn+" * d."+aspColumn+")/sum(d."+unitsColumn+"),2) as string) from RawData d,MasterFiscalCalendar mfc where d."+selectedTypeVariable+" = '"+selectedTypeValue+"' "+businessTypeCriteria+" "
                    			+" and d."+orderWeek+"=mfc.fiscalWeek  and d."+orderWeek+" in ("+actualWeeksBuffer+") "+whereClauseStr+" group by mfc."+monthlyOrQuarterly+" order by mfc.id";
						
					}else{
						sql = "select d."+orderWeek+",cast(round(sum(d."+unitsColumn+" * d."+aspColumn+")/sum(d."+unitsColumn+"),2) as string) from RawData d where d."+selectedTypeVariable+" = '"+selectedTypeValue+"' "+businessTypeCriteria+"  and d."+orderWeek+" in ("+actualWeeksBuffer+") "
	                			+" and d."+orderWeek+" in ("+actualWeeksBuffer+") "+whereClauseStr+"  group by d."+orderWeek;
						
					}
                	
                	
                } else if (tableName.equalsIgnoreCase("ForecastingRevenue")){
                	if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly") ){
                		sql = "select mfc."+monthlyOrQuarterly+",cast(round(sum(d."+unitsColumn+") * (sum(d."+unitsColumn+" * d."+aspColumn+")/sum(d."+unitsColumn+")),2) as string) from RawData d,MasterFiscalCalendar mfc where d."+selectedTypeVariable+" = '"+selectedTypeValue+"' "+businessTypeCriteria+" "
                    			+" and d."+orderWeek+"=mfc.fiscalWeek  and d."+orderWeek+" in ("+actualWeeksBuffer+") "+whereClauseStr+" group by mfc."+monthlyOrQuarterly+" order by mfc.id";
						
					}else{
						sql = "select d."+orderWeek+",cast(round(sum(d."+unitsColumn+") * (sum(d."+unitsColumn+" * d."+aspColumn+")/sum(d."+unitsColumn+")),2) as string) from RawData d where d."+selectedTypeVariable+" = '"+selectedTypeValue+"' "+businessTypeCriteria+"  and d."+orderWeek+" in ("+actualWeeksBuffer+") "
	                			+" and d."+orderWeek+" in ("+actualWeeksBuffer+") "+whereClauseStr+" group by d."+orderWeek;
						
					}
                	
                	
                } else if (tableName.equalsIgnoreCase("ForecastingESC")){
                	if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly") ){
                		sql = "select mfc."+monthlyOrQuarterly+",cast(round(sum(d."+escColumn+" * d."+unitsColumn+")/sum(d."+unitsColumn+"),2) as string) from RawData d,MasterFiscalCalendar mfc where d."+selectedTypeVariable+" = '"+selectedTypeValue+"' "+businessTypeCriteria+" "
                    			+" and d."+orderWeek+"=mfc.fiscalWeek  and d."+orderWeek+" in ("+actualWeeksBuffer+") "+whereClauseStr+" group by mfc."+monthlyOrQuarterly+" order by mfc.id";
						
					}else{
						sql = "select d."+orderWeek+",cast(round(sum(d."+escColumn+" * d."+unitsColumn+")/sum(d."+unitsColumn+"),2) as string) from RawData d where d."+selectedTypeVariable+" = '"+selectedTypeValue+"' "+businessTypeCriteria+" and d."+orderWeek+" in ("+actualWeeksBuffer+") "
	                			+" and d."+orderWeek+" in ("+actualWeeksBuffer+") "+whereClauseStr+" group by d."+orderWeek;
						
					}
                	
                } else {
                	if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly") ){
                		sql = "select mfc."+monthlyOrQuarterly+",cast(round(((sum(d."+aspColumn+") - sum( d."+escColumn+"))/sum(d."+aspColumn+"))*100,2) as string) from RawData d,MasterFiscalCalendar mfc where d."+selectedTypeVariable+" = '"+selectedTypeValue+"' "+businessTypeCriteria+" "
                    			+" and d."+orderWeek+"=mfc.fiscalWeek  and d."+orderWeek+" in ("+actualWeeksBuffer+") "+whereClauseStr+" group by mfc."+monthlyOrQuarterly+" order by mfc.id";
					}else{
						sql = "select d."+orderWeek+",cast(round(((sum(d."+aspColumn+") - sum( d."+escColumn+"))/sum(d."+aspColumn+"))*100,2) as string) from RawData d where d."+selectedTypeVariable+" = '"+selectedTypeValue+"' "+businessTypeCriteria+" and d."+orderWeek+" in ("+actualWeeksBuffer+") "
	                			+" and d."+orderWeek+" in ("+actualWeeksBuffer+") "+whereClauseStr+" group by d."+orderWeek;
					}
                	
                	
                }
                
				try {
	        		
	        		 Query q = session.createQuery(sql);
	        		 List ActualsList =  q.list();
	        		 Map actualsListMap = new HashMap();
	                 if(ActualsList != null){
	                 	int size =ActualsList.size();
	                 	for(int i=0;i<size;i++){
	                 		Object[] rowList = (Object[])ActualsList.get(i);
	                 		String actualPeriod="";
	                 		String actualValue="";
	                 		List actualTable= new ArrayList();
	                 		if(rowList[0]!=null){
	                 			actualPeriod = rowList[0].toString();
	                 		}
	                 		if(rowList[1]!=null){
	                 			actualValue = rowList[1].toString();
	                 		}
	                 		actualsListMap.put(actualPeriod, actualValue);
	                 	}
	                 }
	                 
	                 if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                    	List monthlyQuarterlyList = getMonthlyQuarterlyNames(actualWeeksBuffer.toString(),selectedPeriod);
                    	int listSize1=monthlyQuarterlyList.size();
                    	for(int l=0;l<listSize1;l++){
                    		
                    		List actualTable= new ArrayList();
	                 		String actualPeriod=(String) monthlyQuarterlyList.get(l);
	                 		String actualValue="";
	                 		if(actualsListMap.containsKey(actualPeriod)){
	                 			
	                 			actualValue= (String) actualsListMap.get(actualPeriod);
	                 		} else{
	                 			actualValue="0";
	                 		}
	                 		forecastBuffer.append("{ w: '"+actualPeriod+"', a: "+actualValue+"}");
	                 		actualTable.add(actualPeriod);
	                 		actualTable.add(actualValue);
	                 		jsonTableList.add(actualTable);
	                 		if(l<listSize1-1){
	                  			forecastBuffer.append(",");
	                  		}
	                  		forecastBuffer.append("\n");
                    		
                    	}
                    }
                    
                    if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Monthly") && !selectedPeriod.equalsIgnoreCase("Quarterly")){
                    	int size =actualWeeksList.size();;
                    		for(int k=size-1;k>=0;k--){
								List actualTable= new ArrayList();
		                 		String actualPeriod=(String) actualWeeksList.get(k);
		                 		String actualValue="";
		                 		if(actualsListMap.containsKey(actualPeriod)){
		                 			
		                 			actualValue= (String) actualsListMap.get(actualPeriod);
		                 		} else{
		                 			actualValue="0";
		                 		}
		                 		forecastBuffer.append("{ w: '"+actualPeriod+"', a: "+actualValue+"}");
		                 		actualTable.add(actualPeriod);
		                 		actualTable.add(actualValue);
		                 		jsonTableList.add(actualTable);
		                 		if(k>0){
                         			forecastBuffer.append(",");
                         		}
		                  		forecastBuffer.append("\n");
								
							}
                    }
                    forecastBuffer.append("] \n");
                	jsonViewMapObj.put("jsonStr",forecastBuffer.toString());
                    jsonViewMapObj.put("jsonTable",jsonTableList);
	                 
	               } catch (HibernateException he) {
	                       throw he;
	               } catch (ApplicationException e) {
					// TODO Auto-generated catch block
					logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
				}
	        	 
	              return jsonViewMapObj;
	        }
		     });
		     logger.debug("Leaving from getHistoricalActualReportChartJsonForAggregated");
		     return (Map) object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	}
	public Map getHistoricalActualReportChartJsonFromDatabase(final String tableName, final String productId, final int planningCycleId,final String business,final List rolesList,final int userId,final int week,final int year,final int range,final String selectedPeriod ){
		try{
			logger.debug("Entered into getHistoricalActualReportChartJsonFromDatabase");
	        Object object = hibernateTemplate.execute(new HibernateCallback() {
	        public Object doInHibernate(Session session)
	                     throws HibernateException, SQLException {
	        	Map jsonViewMapObj = new HashMap();
	            StringBuffer forecastBuffer = new StringBuffer();
	            forecastBuffer.append("[ \n");
	            String sql = "";
	            String sql1="";
	            String columName = "";
	            String orderWeek = PropertiesUtil.getProperty("orderWeek");
	            String productIdColumn =PropertiesUtil.getProperty("skuListId");
	            String unitsColumn = PropertiesUtil.getProperty("quantity");
	            String aspColumn = PropertiesUtil.getProperty("ASP");
	            String businessColumn= PropertiesUtil.getProperty("business");
	            List jsonTableList = new ArrayList();
	            Criteria criteria = session.createCriteria(ForecastingUnits.class, "forecastUnitObj");
	            List forecastingUnitsList=null;
	            List baseForecastAspList=null;
	            List currentForecastUnitsList =new ArrayList();
	            List currentForecastAspList =new ArrayList();
	            StringBuffer actualWeeksBuffer = new StringBuffer();
	            List actualWeeksList=null;
				try {
					actualWeeksList = planningDAO.getSKUForecastWeek(year,week,range,1);
					int actualsSize=actualWeeksList.size();
	         		for(int t=0;t<actualsSize;t++){
	         			actualWeeksBuffer.append("'"+actualWeeksList.get(t)+"'");
	                	if(t<actualsSize-1){
	                		actualWeeksBuffer.append(",");
	                	}
	         		}
				} catch (ApplicationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String monthlyOrQuarterly="";
         		if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && selectedPeriod.equalsIgnoreCase("Monthly")){
         			monthlyOrQuarterly="calendarMonth";
         		}
         		if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && selectedPeriod.equalsIgnoreCase("Quarterly")){
         			monthlyOrQuarterly="fiscalQuarter";
         		}
         		
	           if (tableName.equalsIgnoreCase("ForecastingRevenue")){
	        	   if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly") ){
	        		   sql = "select mfc."+monthlyOrQuarterly+",cast(round(d."+unitsColumn+" * d."+aspColumn+",2) as string) from RawData d,MasterFiscalCalendar mfc where d."+productIdColumn+" = '"+productId+"'  and "
		            			+ " d."+businessColumn+" = '" +business+"' and d."+orderWeek+"=mfc.fiscalWeek  and d."+orderWeek+" in ("+actualWeeksBuffer+") group by mfc."+monthlyOrQuarterly+" order by mfc.id";
	        		   
	        	   } else{
	        		   sql = "select d."+orderWeek+",cast(round(d."+unitsColumn+" * d."+aspColumn+",2) as string) from RawData d where d."+productIdColumn+" = '"+productId+"' and "
		            			+ " d."+businessColumn+" = '" +business+"' and d."+orderWeek+" in ("+actualWeeksBuffer+") group by d."+orderWeek;
	        		   
	        	   }
	            	
	            	System.out.println("Revenue "+sql1);
	            } else if (tableName.equalsIgnoreCase("ForecastingUnits")){
	            	columName=  PropertiesUtil.getProperty("quantity");
	            }else if (tableName.equalsIgnoreCase("ForecastingESC")){
	            	columName=  PropertiesUtil.getProperty("ESC");
	            } 
	            else {
	            	columName=  PropertiesUtil.getProperty("productMargin");
	            }
	           if(!tableName.equalsIgnoreCase("ForecastingRevenue") ){
      			 if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly") ){
      				 sql = "select mfc."+monthlyOrQuarterly+",cast(round(d."+columName+",2) as string) from RawData d,MasterFiscalCalendar mfc where d."+productIdColumn+" = '"+productId+"' and "
   	            			+ " d."+businessColumn+" = '" +business+"' and "
   	            			+ " d."+orderWeek+"=mfc.fiscalWeek  and d."+orderWeek+" in ("+actualWeeksBuffer+") group by mfc."+monthlyOrQuarterly+" order by mfc.id";
      				 
      			 }else{
      				 sql = "select d."+orderWeek+",cast(round(d."+columName+",2) as string) from RawData d where d."+productIdColumn+" = '"+productId+"' and "
   	            			+ " d."+businessColumn+" = '" +business+"' and "
   	            			+ " d."+orderWeek+" in ("+actualWeeksBuffer+") group by d."+orderWeek+"";
      				 
      			 }
      			 
	           }
	        	try {
	        		
	        		 Query q = session.createQuery(sql);
	        		 List ActualsList =  q.list();
	        		 Map actualsListMap = new HashMap();
	                 if(ActualsList != null){
	                 	int size =ActualsList.size();
	                 	for(int i=0;i<size;i++){
	                 		Object[] rowList = (Object[])ActualsList.get(i);
	                 		String actualPeriod="";
	                 		String actualValue="";
	                 		List actualTable= new ArrayList();
	                 		if(rowList[0]!=null){
	                 			actualPeriod = rowList[0].toString();
	                 		}
	                 		if(rowList[1]!=null){
	                 			actualValue = rowList[1].toString();
	                 		}
	                 		actualsListMap.put(actualPeriod, actualValue);
	                 	}
	                 }
	                 
	                 if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                     	List monthlyQuarterlyList = getMonthlyQuarterlyNames(actualWeeksBuffer.toString(),selectedPeriod);
                     	int listSize1=monthlyQuarterlyList.size();
                     	for(int l=0;l<listSize1;l++){
                     		
                     		List actualTable= new ArrayList();
	                 		String actualPeriod=(String) monthlyQuarterlyList.get(l);
	                 		String actualValue="";
	                 		if(actualsListMap.containsKey(actualPeriod)){
	                 			
	                 			actualValue= (String) actualsListMap.get(actualPeriod);
	                 		} else{
	                 			actualValue="0";
	                 		}
	                 		forecastBuffer.append("{ w: '"+actualPeriod+"', a: "+actualValue+"}");
	                 		actualTable.add(actualPeriod);
	                 		actualTable.add(actualValue);
	                 		jsonTableList.add(actualTable);
	                 		if(l<listSize1-1){
	                  			forecastBuffer.append(",");
	                  		}
	                  		forecastBuffer.append("\n");
                     		
                     	}
                     }
                     
                     if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Monthly") && !selectedPeriod.equalsIgnoreCase("Quarterly")){
                     	int size =actualWeeksList.size();;
                     	for(int k=size-1;k>=0;k--){
								
								List actualTable= new ArrayList();
		                 		String actualPeriod=(String) actualWeeksList.get(k);
		                 		String actualValue="";
		                 		if(actualsListMap.containsKey(actualPeriod)){
		                 			
		                 			actualValue= (String) actualsListMap.get(actualPeriod);
		                 		} else{
		                 			actualValue="0";
		                 		}
		                 		forecastBuffer.append("{ w: '"+actualPeriod+"', a: "+actualValue+"}");
		                 		actualTable.add(actualPeriod);
		                 		actualTable.add(actualValue);
		                 		jsonTableList.add(actualTable);
		                 		if(k>0){
                          			forecastBuffer.append(",");
                          		}
		                  		forecastBuffer.append("\n");
								
							}
                     }
                     forecastBuffer.append("] \n");
                 	jsonViewMapObj.put("jsonStr",forecastBuffer.toString());
                     jsonViewMapObj.put("jsonTable",jsonTableList);
	                 
	               } catch (HibernateException he) {
	                       throw he;
	               } catch (ApplicationException e) {
					// TODO Auto-generated catch block
					logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
				}
	              return jsonViewMapObj;
	        }
		     });
		     logger.debug("Leaving from getHistoricalActualReportChartJsonFromDatabase");
		     return (Map) object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	}
	
	public Map gePlanogramtReports(final Map planogramTableViewMapObj,final String tableName,final Integer planningCycleId,final String whereClauseStr,final Integer week,final Integer year,final Integer range,final String selectedPeriod,final Map targetTablecolorRangeMapObj){
		try{
			logger.debug("Entered into gePlanogramtReports");
            Object object = hibernateTemplate.execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                         throws HibernateException, SQLException {
            	
            	 String sql = "";
            	 List targetTableList = new ArrayList();
            	 String tableHeader="";
            	 if(tableName.equalsIgnoreCase("ForecastingUnits")){
          			tableHeader="Units";
            	 }  else if (tableName.equalsIgnoreCase("ForecastingASP")){
            		 tableHeader="ASP";
            	 } else if (tableName.equalsIgnoreCase("ForecastingRevenue")){
                 	tableHeader="Revenue";
            	 } else if (tableName.equalsIgnoreCase("ForecastingESC")){
                 	tableHeader="ESC";
            	 }else {
                 	tableHeader="PM%";
            	 }
            	 String categoryColumn = PropertiesUtil.getProperty(ApplicationConstants.CATEGORY);
            	 String planogramColumn= PropertiesUtil.getProperty("planogram");
            	 String businessColumn= PropertiesUtil.getProperty("business");
            	 String uniqueCategoryName="";
            	
            	
            	 sql="SELECT distinct "+planogramColumn+" ,"+categoryColumn+","+businessColumn+" FROM Data where "+categoryColumn+" in ('DT Category','NB Category')  "+whereClauseStr+"  group by "+categoryColumn+","+planogramColumn+","+businessColumn;
            	 Query q = session.createQuery(sql);
                 List planogramTableList =  q.list();
                 if(planogramTableList != null){
                 	int size1 =planogramTableList.size();
                 	List dataList=null;
                  	for(int i=0;i<size1;i++){
                  		 int switchToNextCategory=0;
                  		Object[] rowList = (Object[])planogramTableList.get(i);
                  		String planogram="";
                  		String category="";
                  		String business="";
                  		String nextCategory="";
                  		if(rowList[0]!=null){
                  			planogram=rowList[0].toString();
                  		}
						if(rowList[1]!=null){
							category=rowList[1].toString();
							
                  		}
						if(rowList[2]!=null){
							business=rowList[2].toString();	
						}
						if(i==0){
							uniqueCategoryName=category;
							dataList = new ArrayList();
							switchToNextCategory=1;
						}
						if(i>0 && !uniqueCategoryName.equals(category)){
							switchToNextCategory=1;
							uniqueCategoryName=category;
							dataList = new ArrayList();
						}
						if(i<size1-1){
							rowList = (Object[])planogramTableList.get(i+1);
							if(rowList[1]!=null){
								nextCategory=rowList[1].toString();
								
	                  		}
						}
                  		
                  		getPlanogramReportTableData(planogramTableViewMapObj,targetTableList,tableName,planningCycleId,business,category,planogram,whereClauseStr,week,year,range,selectedPeriod,targetTablecolorRangeMapObj,dataList,switchToNextCategory,nextCategory);
                  	}
                  	planogramTableViewMapObj.put(tableHeader, targetTableList);
                 }
                 
                
            	
            	 return planogramTableViewMapObj;
            }
		     });
		     logger.debug("Leaving from getPlanogramReports");
		     return (Map) object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	}
	
	public Map getPlanogramReportTableData(final Map planogramTableViewMapObj,final List targetTableList ,final String tableName,final Integer planningCycleId,final String business,final String category,final String planogram,final String whereClauseStr,final Integer week,final Integer year,
			final Integer range,final String selectedPeriod,final Map targetTablecolorRangeMapObj,final List dataList,final Integer switchToNextCategory,final String nextCategory){
		try{
			logger.debug("Entered into getReportTableData");
           
			Object object = hibernateTemplate.execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                         throws HibernateException, SQLException {
                String sql = "";
                String sql1="";
                String orderWeek = PropertiesUtil.getProperty("orderWeek");
                String productIdColumn =PropertiesUtil.getProperty("skuListId");
                String unitsColumn = PropertiesUtil.getProperty("quantity");
                String aspColumn = PropertiesUtil.getProperty("ASP");
                String escColumn =PropertiesUtil.getProperty("ESC");
                String pmColumn =PropertiesUtil.getProperty("productMargin");
                String businessColumn= PropertiesUtil.getProperty("business");
                String planogramColumn= PropertiesUtil.getProperty("planogram");
                String numberOfForecastsStr= PropertiesUtil.getProperty("numberOfForecasts");
                int numberOfForecasts=0;
                if(!ApplicationUtil.isEmptyOrNull(numberOfForecastsStr)){
                	numberOfForecasts=Integer.parseInt(numberOfForecastsStr);
                }
                String numberOfActualsStr= PropertiesUtil.getProperty("numberOfActuals");
                int numberOfActuals=0;
                if(!ApplicationUtil.isEmptyOrNull(numberOfActualsStr)){
                	numberOfActuals=Integer.parseInt(numberOfActualsStr);
                }
                int tableColorRange=0;
                String categoryColumn = PropertiesUtil.getProperty(ApplicationConstants.CATEGORY);
                StringBuffer weeksBuffer = new StringBuffer();
                StringBuffer actualWeeksBuffer = new StringBuffer();
                StringBuffer forecastWeeksBuffer = new StringBuffer();
            	try {
                     		//List dataList = new ArrayList();      
                     		Map catSubcategoryDataMap = new HashMap();
                     		Map actualsListMap = new HashMap();
                     		Object[] rowList=null;
                     		List weekList= new ArrayList();
                     		weekList.add(business);
                     		List monthlyOrQuarterlyList = new ArrayList();
                     		List valuesList= new ArrayList();
                     		List percentageOfTargetsList= new ArrayList();
                     		if(category.equalsIgnoreCase("DT Category")){
                     			valuesList.add("DT "+planogram);
                     		}
                     		if(category.equalsIgnoreCase("NB Category")){
                     			valuesList.add("NB "+planogram);
                     		}
                     		
                     		percentageOfTargetsList.add("% to Target");
                     		List forecastWeeksList= getForecastWeeks(year,week,range);
                     		int forecastsSize=forecastWeeksList.size();
                     		String businessTypeCriteria=" ";
                     		for(int t=0;t<forecastsSize;t++){
                     			forecastWeeksBuffer.append("'"+forecastWeeksList.get(t)+"'");
                            	if(t<forecastsSize-1){
                            		forecastWeeksBuffer.append(",");
                            	}
                     		}
                     		String filterConditionNativeSql="select distinct d."+productIdColumn+" from Data d where  "+businessColumn+"='"+business+"' and "+categoryColumn+"='"+category+"' and "+planogramColumn+"=\""+planogram+"\" "+whereClauseStr;
                     		String monthlyOrQuarterly="";
                     		if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && selectedPeriod.equalsIgnoreCase("Monthly")){
                     			monthlyOrQuarterly="calendarMonth";
                     		}
                     		if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && selectedPeriod.equalsIgnoreCase("Quarterly")){
                     			monthlyOrQuarterly="fiscalQuarter";
                     		}
                     		if(tableName.equalsIgnoreCase("ForecastingUnits")){
                     			if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                     				monthlyOrQuarterlyList.add(business);
                	       			 
                                	sql1 = "select mfc."+monthlyOrQuarterly+", cast(round(sum(t.OriginalForecast)) as char(30)) from (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast  "
                                			+ "FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from  "
                                			+ "override_units_log group by forecastingUnitsId) b on a.forecastingUnitsId= b.forecastingUnitsId join forecasting_units f on f.id=b.forecastingUnitsId and f.planningCycleId="+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.productId in ("+filterConditionNativeSql+") and  f.planningCycleId="+planningCycleId+" and f.business = '"+ business+"' and f.forecastPeriod in ("+forecastWeeksBuffer+"))  t ,master_fiscal_calender mfc where t.forecastPeriod=mfc.fiscalWeek group by mfc."+monthlyOrQuarterly+"  order by mfc.id";
                                	System.out.println("Units"+sql1);
                     				
                     			}else {
                	       			 
                                	sql1 = "select t.forecastPeriod, cast(round(sum(t.OriginalForecast)) as char(30)) from (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast  "
                                			+ "FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from  "
                                			+ "override_units_log group by forecastingUnitsId) b on a.forecastingUnitsId= b.forecastingUnitsId join forecasting_units f on f.id=b.forecastingUnitsId and f.planningCycleId="+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.productId in ("+filterConditionNativeSql+") and  f.planningCycleId="+planningCycleId+" and  f.business = '"+ business+"' and f.forecastPeriod in ("+forecastWeeksBuffer+"))  t   group by t.forecastPeriod";
                     				
                     			}
                            	
                            }  else if (tableName.equalsIgnoreCase("ForecastingASP")){
                            	if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                            		monthlyOrQuarterlyList.add(business);
                	                	
                                	sql1 = "select mfc."+monthlyOrQuarterly+" , round(sum(fu.OriginalForecast * fa.OriginalForecast)/sum(fu.OriginalForecast),2) from (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
                       			 			+ " FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from override_units_log group by forecastingUnitsId) b  "
                       			 			+ "on a.forecastingUnitsId= b.forecastingUnitsId join forecasting_units f on f.id=b.forecastingUnitsId and f.planningCycleId="+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.planningCycleId = "+planningCycleId+ " and f.business='"+business+"' and f.planningCycleId= "+planningCycleId+" and f.productId in ("+filterConditionNativeSql+") and f.forecastPeriod in ("+forecastWeeksBuffer+")) fu, (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
                       			 			+ " FROM forecasting_asp f left join (SELECT a.* FROM override_asp_log a join (select forecastingAspId,max(createdDate) as mdate from override_asp_log group by forecastingAspId) b  "
                       			 			+ "on a.forecastingAspId= b.forecastingAspId join forecasting_asp f on f.id=b.forecastingAspId and f.planningCycleId="+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingAspId where f.planningCycleId = "+planningCycleId+ " and f.business='"+business+"' and f.productId in ("+filterConditionNativeSql+") and f.forecastPeriod in ("+forecastWeeksBuffer+")) fa ,master_fiscal_calender mfc "+
                							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business and fa.planningCycleId = "+planningCycleId+ " and fu.planningCycleId= "+planningCycleId+" and  fu.forecastPeriod=mfc.fiscalWeek and fa.forecastPeriod=mfc.fiscalWeek group by mfc."+monthlyOrQuarterly+" order by mfc.id";
                                	System.out.println("ASP"+sql1);
                            	} else{
                                	sql1 = "select fu.forecastPeriod , round(sum(fu.OriginalForecast * fa.OriginalForecast)/sum(fu.OriginalForecast),2) from (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
                       			 			+ " FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from override_units_log group by forecastingUnitsId) b  "
                       			 			+ "on a.forecastingUnitsId= b.forecastingUnitsId join forecasting_units f on f.id=b.forecastingUnitsId and f.planningCycleId="+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.planningCycleId = "+planningCycleId+ " and f.business='"+business+"' and f.productId in ("+filterConditionNativeSql+") and f.forecastPeriod in ("+forecastWeeksBuffer+")) fu, (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
                       			 			+ " FROM forecasting_asp f left join (SELECT a.* FROM override_asp_log a join (select forecastingAspId,max(createdDate) as mdate from override_asp_log group by forecastingAspId) b  "
                       			 			+ "on a.forecastingAspId= b.forecastingAspId join forecasting_asp f on f.id=b.forecastingAspId and f.planningCycleId="+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingAspId where f.planningCycleId = "+planningCycleId+ " and f.business='"+business+"' and f.productId in ("+filterConditionNativeSql+") and f.forecastPeriod in ("+forecastWeeksBuffer+")) fa "+
                							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business and fa.planningCycleId = "+planningCycleId+ " and fu.planningCycleId= "+planningCycleId+" group by fu.forecastPeriod";
                            	}
                            	
                            	
                            } else if (tableName.equalsIgnoreCase("ForecastingRevenue")){
                            	if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                            		
                            		monthlyOrQuarterlyList.add(business);
                                	sql1 = "select mfc."+monthlyOrQuarterly+" , round(sum(fu.OriginalForecast * fa.OriginalForecast),2) from (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
                       			 			+ " FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from override_units_log group by forecastingUnitsId) b  "
                       			 			+ "on a.forecastingUnitsId= b.forecastingUnitsId join forecasting_units f on f.id=b.forecastingUnitsId and f.planningCycleId="+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.planningCycleId = "+planningCycleId+ " and f.business='"+business+"' and f.productId in ("+filterConditionNativeSql+") and f.forecastPeriod in ("+forecastWeeksBuffer+")) fu, (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
                       			 			+ " FROM forecasting_asp f left join (SELECT a.* FROM override_asp_log a join (select forecastingAspId,max(createdDate) as mdate from override_asp_log group by forecastingAspId) b  "
                       			 			+ "on a.forecastingAspId= b.forecastingAspId join forecasting_asp f on f.id=b.forecastingAspId and f.planningCycleId="+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingAspId where f.planningCycleId = "+planningCycleId+ " and f.business='"+business+"' and f.productId in ("+filterConditionNativeSql+") and f.forecastPeriod in ("+forecastWeeksBuffer+")) fa ,master_fiscal_calender mfc "+
                							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business and fa.planningCycleId = "+planningCycleId+ " and fu.planningCycleId= "+planningCycleId+" and fu.forecastPeriod=mfc.fiscalWeek and fa.forecastPeriod=mfc.fiscalWeek group by mfc."+monthlyOrQuarterly+" order by mfc.id";
                                	System.out.println("check Revenue query"+sql1);
                            		
                            	} else{
                                	sql1 = "select fu.forecastPeriod , round(sum(fu.OriginalForecast * fa.OriginalForecast),2) from (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
                       			 			+ " FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from override_units_log group by forecastingUnitsId) b  "
                       			 			+ "on a.forecastingUnitsId= b.forecastingUnitsId join forecasting_units f on f.id=b.forecastingUnitsId and f.planningCycleId="+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.planningCycleId = "+planningCycleId+ " and f.business='"+business+"' and f.productId in ("+filterConditionNativeSql+") and f.forecastPeriod in ("+forecastWeeksBuffer+")) fu, (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
                       			 			+ " FROM forecasting_asp f left join (SELECT a.* FROM override_asp_log a join (select forecastingAspId,max(createdDate) as mdate from override_asp_log group by forecastingAspId) b  "
                       			 			+ "on a.forecastingAspId= b.forecastingAspId join forecasting_asp f on f.id=b.forecastingAspId and f.planningCycleId="+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingAspId where f.planningCycleId = "+planningCycleId+ " and f.business='"+business+"' and f.productId in ("+filterConditionNativeSql+") and f.forecastPeriod in ("+forecastWeeksBuffer+")) fa "+
                							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business and fa.planningCycleId = "+planningCycleId+ " and fu.planningCycleId= "+planningCycleId+" group by fu.forecastPeriod";
                            		
                            	}
                            	
                            	
                            } else if (tableName.equalsIgnoreCase("ForecastingESC")){
                            	if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                            		monthlyOrQuarterlyList.add(business);
                                	sql1 = "select mfc."+monthlyOrQuarterly+" , round(sum(fu.OriginalForecast * fa.forecastValue) / sum(fu.OriginalForecast),2) from (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
                       			 			+ " FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from override_units_log group by forecastingUnitsId) b  "
                       			 			+ "on a.forecastingUnitsId= b.forecastingUnitsId join forecasting_units f on f.id=b.forecastingUnitsId and f.planningCycleId="+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.planningCycleId = "+planningCycleId+ " and f.business='"+business+"' and f.productId in ("+filterConditionNativeSql+") and f.forecastPeriod in ("+forecastWeeksBuffer+")) fu, forecasting_esc fa ,master_fiscal_calender mfc "+
                							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business and fa.planningCycleId = "+planningCycleId+ " and fu.planningCycleId= "+planningCycleId+" and fu.forecastPeriod=mfc.fiscalWeek group by mfc."+monthlyOrQuarterly+" order by mfc.id";
                                	System.out.println("check ESC query"+sql1);
                            		
                            	} else{
                                	sql1 = "select fu.forecastPeriod , round(sum(fu.OriginalForecast * fa.forecastValue) / sum(fu.OriginalForecast),2) from (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
                       			 			+ " FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from override_units_log group by forecastingUnitsId) b  "
                       			 			+ "on a.forecastingUnitsId= b.forecastingUnitsId join forecasting_units f on f.id=b.forecastingUnitsId and f.planningCycleId="+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.planningCycleId = "+planningCycleId+ " and f.business='"+business+"' and f.productId in ("+filterConditionNativeSql+") and f.forecastPeriod in ("+forecastWeeksBuffer+")) fu, forecasting_esc fa "+
                							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business and fa.planningCycleId = "+planningCycleId+ " and fu.planningCycleId= "+planningCycleId+" group by fu.forecastPeriod";
                            	}
                            	
                            	
                            	
                            } else {
                            	if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                            		monthlyOrQuarterlyList.add(business);
                            		
                                	sql1 = "select mfc."+monthlyOrQuarterly+" ,round(((sum(fa.OriginalForecast)-sum(fu.forecastValue))/sum(fa.OriginalForecast))*100,2) from forecasting_esc fu, (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
                       			 			+ " FROM forecasting_asp f left join (SELECT a.* FROM override_asp_log a join (select forecastingAspId,max(createdDate) as mdate from override_asp_log group by forecastingAspId) b  "
                       			 			+ "on a.forecastingAspId= b.forecastingAspId join forecasting_asp f on f.id=b.forecastingAspId and f.planningCycleId="+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingAspId where f.planningCycleId = "+planningCycleId+ " and f.business='"+business+"' and f.productId in ("+filterConditionNativeSql+") and f.forecastPeriod in ("+forecastWeeksBuffer+")) fa ,master_fiscal_calender mfc "+
                							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business and fa.planningCycleId = "+planningCycleId+ " and fu.planningCycleId= "+planningCycleId+" and fu.forecastPeriod=mfc.fiscalWeek group by mfc."+monthlyOrQuarterly+" order by mfc.id";
                                	System.out.println("check pm% query"+sql1);
                            	} else{
                                	sql1 = "select fu.forecastPeriod ,round(((sum(fa.OriginalForecast)-sum(fu.forecastValue))/sum(fa.OriginalForecast))*100,2) from forecasting_esc fu, (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
                       			 			+ " FROM forecasting_asp f left join (SELECT a.* FROM override_asp_log a join (select forecastingAspId,max(createdDate) as mdate from override_asp_log group by forecastingAspId) b  "
                       			 			+ "on a.forecastingAspId= b.forecastingAspId join forecasting_asp f on f.id=b.forecastingAspId and f.planningCycleId="+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingAspId where f.planningCycleId = "+planningCycleId+ " and f.business='"+business+"' and f.productId in ("+filterConditionNativeSql+") and f.forecastPeriod in ("+forecastWeeksBuffer+")) fa "+
                							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business and fa.planningCycleId = "+planningCycleId+ " and fu.planningCycleId= "+planningCycleId+" group by fu.forecastPeriod";
                            		
                            	}
                            	
                            }
                     		
                            
                            
                            SQLQuery query = session.createSQLQuery(sql1);
                            List ForecastingValuesList =  (List)query.list();
                            if(ForecastingValuesList != null){
                         	   int size =ForecastingValuesList.size();
                         	   for(int l=0;l<size;l++){
                         		  rowList = (Object[])ForecastingValuesList.get(l);
                             		String forecastPeriod="";
                             		String forecastValue="";
                             		List forecastTable = new ArrayList();
                             		if(rowList[0]!=null){
                             			forecastPeriod = rowList[0].toString();
                             		}
                             		if(rowList[1]!=null){
                             			forecastValue = rowList[1].toString();
                             		}
                             		actualsListMap.put(forecastPeriod, forecastValue);
                         	   }
                            }
                            if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                            	List monthlyQuarterlyList = getMonthlyQuarterlyNames(forecastWeeksBuffer.toString(),selectedPeriod);
                            	int listSize1=monthlyQuarterlyList.size();
                            	tableColorRange=listSize1;
                            	for(int l=0;l<listSize1;l++){
                            		
                            		String monthQuarter=(String) monthlyQuarterlyList.get(l);
                             		String actualValue="";
                             		if(actualsListMap.containsKey(monthQuarter)){
                             			actualValue= (String) actualsListMap.get(monthQuarter);
                             		} else{
                             			actualValue="0";
                             		}
                             		monthlyOrQuarterlyList.add(monthQuarter);   
                             		if(tableName.equalsIgnoreCase("ForecastingUnits") ){
                             			valuesList.add(actualValue);
                             		}
                             		if(tableName.equalsIgnoreCase("ForecastingASP") || tableName.equalsIgnoreCase("ForecastingESC") || tableName.equalsIgnoreCase("ForecastingRevenue")){
                             			valuesList.add("$"+actualValue);
                             		}
                             		if(tableName.equalsIgnoreCase("ForecastingPM")){
                             			valuesList.add(actualValue+"%");
                             		}
                            		
                            	}
                            }
                            
                            if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Monthly") && !selectedPeriod.equalsIgnoreCase("Quarterly")){
                            	int size =forecastsSize;
								tableColorRange=size;
								for(int k=0;k<size;k++){
									
									String actualPeriod=(String) forecastWeeksList.get(k);
									String actualValue="";
									if(actualsListMap.containsKey(actualPeriod)){
										actualValue= (String) actualsListMap.get(actualPeriod);
									} else{
										actualValue="0";
									}
									weekList.add(actualPeriod);   
									if(tableName.equalsIgnoreCase("ForecastingUnits")){
                             			valuesList.add(actualValue);
                             		}
                             		if(tableName.equalsIgnoreCase("ForecastingASP") || tableName.equalsIgnoreCase("ForecastingESC")  || tableName.equalsIgnoreCase("ForecastingRevenue")){
                             			valuesList.add("$"+actualValue);
                             		}
                             		if(tableName.equalsIgnoreCase("ForecastingPM")){
                             			valuesList.add(actualValue+"%");
                             		}
									
								}
                            }
                            if(switchToNextCategory==1){
                            	if(!ApplicationUtil.isEmptyOrNull(selectedPeriod) && !selectedPeriod.equalsIgnoreCase("Weekly")){
                                	
                                	dataList.add(monthlyOrQuarterlyList);
                         		}else{
                         			dataList.add(weekList);
                         			
                         		}
                            	
                            }
                            
                            dataList.add(valuesList);
                            if(!nextCategory.equalsIgnoreCase(category)){
                            	catSubcategoryDataMap.put(planogram, dataList);
                                targetTableList.add(catSubcategoryDataMap);
                                targetTablecolorRangeMapObj.put("tableColorRange", tableColorRange);
                            }
                            
                    
                   } catch (HibernateException he) {
                           throw he;
                   } catch (ApplicationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	 
                  return planogramTableViewMapObj;
            }
		     });
		     logger.debug("Leaving from getReportTableData");
		     return (Map) object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	}
	
	public List getFlatFile(final Integer planningCycleId,final Integer week,final Integer year) throws ApplicationException
    {
           try{
           logger.debug("Entered into getFlatFile");
           Object object = hibernateTemplate.execute(new HibernateCallback() {
                  public Object doInHibernate(Session session)
                               throws HibernateException, SQLException {
                        try {
                                      List  flatFileList = new ArrayList();
                                      StringBuffer forecastWeeksBuffer = new StringBuffer();
                                      StringBuffer selectCluaseBuffer = new StringBuffer();
                                      String productIdColumn =PropertiesUtil.getProperty("skuListId");
                                      String businessColumn= PropertiesUtil.getProperty("business");
                                      String weeksRangeStr =PropertiesUtil.getProperty("flatFileForecastWeeksRange");
                                      Integer range =Integer.valueOf(weeksRangeStr);
                                      String whereClauseStr="SELECT  distinct "+productIdColumn+" FROM data where planningCycleId="+planningCycleId+" and "+productIdColumn+" in (select productId from sku_list where ("+week+"<=eolweek and "+year+"<=eolyear) or eolweek is null )";
                                      String businessQuery="select distinct "+businessColumn+" FROM data";
                                      String sql="from VariableNames";
                                      Query q=session.createQuery(sql);
                                      
                                      List VariableNamesList=(List)q.list();
                                      int size1=VariableNamesList.size();
                                      List columnNamesList=new ArrayList();           
                                      for(int i=2;i<size1-3;i++)
                                      {
                                             VariableNames variableNames =  (VariableNames)VariableNamesList.get(i);
                                             columnNamesList.add(variableNames.getColumnName());
                                             String vName=variableNames.getVariableName();
                                             if(vName.equalsIgnoreCase("C1")){
                                                    selectCluaseBuffer.append("un.forecastPeriod");
                                             } else if(vName.equalsIgnoreCase("C11")){
                                                    selectCluaseBuffer.append("cast("+vName+" as char (500))");
                                                    
                                             }else if(vName.equalsIgnoreCase("C15")){
                                                    selectCluaseBuffer.append("Quantity");
                                                    
                                             }else if(vName.equalsIgnoreCase("C25")){
                                                    selectCluaseBuffer.append("OriginalForecast");
                                                    
                                             }else{
                                                    selectCluaseBuffer.append(""+vName+"");
                                             }
                                             
                        if(i<size1-4){
                               selectCluaseBuffer.append(",");
                        }
                                      }
                                      flatFileList.add(columnNamesList);
                                      List forecastWeeksList=null;
                                      try {
                                             forecastWeeksList = getForecastWeeks(year,week,range);
                                      } catch (ApplicationException e) {
                                             // TODO Auto-generated catch block
                                             e.printStackTrace();
                                      }
                        int forecastsSize=forecastWeeksList.size();
                        String businessTypeCriteria=" ";
                        for(int t=0;t<forecastsSize;t++){
                               forecastWeeksBuffer.append("'"+forecastWeeksList.get(t)+"'");
                        if(t<forecastsSize-1){
                               forecastWeeksBuffer.append(",");
                        }
                        }
                        sql="SELECT  "+selectCluaseBuffer.toString()+" FROM data d  ,"
                                      + "(select forecastPeriod, (case when b.overrideValue is null then cast(round(a.forecastValue) as char (30)) else cast(round(b.overrideValue) as char (30)) end)  Quantity,productId,business from  forecasting_units a "
                                      + " left join  (SELECT a.* FROM override_units_log a join (select forecastingUnitsId, max(createdDate) as mdate from override_units_log group by forecastingUnitsId) b ON a.forecastingUnitsId = b.forecastingUnitsId join forecasting_units f ON f.id = b.forecastingUnitsId "
                                      + " and f.planningCycleId ="+planningCycleId+"  and a.createdDate = b.mdate) b ON a.id = b.forecastingUnitsId where planningCycleId ="+planningCycleId+" and productId in( "+whereClauseStr+") "
                                      + " and business in ("+businessQuery+") and forecastperiod in ("+forecastWeeksBuffer+") group by forecastperiod,productId,business) un ,(select  forecastPeriod, (case when b.overrideValue is null then cast(round(a.forecastValue,2) as char (30)) else cast(round(b.overrideValue,2) as char (30)) end) as OriginalForecast,productId,business "
                                      + " from forecasting_asp a left join (SELECT a.* FROM override_asp_log a join (select forecastingAspId, max(createdDate) as mdate from override_asp_log group by forecastingAspId) b ON a.forecastingAspId = b.forecastingAspId join forecasting_asp f ON f.id = b.forecastingAspId "
                                      + " and f.planningCycleId ="+planningCycleId+" and a.createdDate = b.mdate) b ON a.id = b.forecastingAspId where planningCycleId ="+planningCycleId+" and productId in("+whereClauseStr+") and business in ("+businessQuery+") and forecastperiod in ("+forecastWeeksBuffer+") "
                                      + " group by forecastperiod,productId,business) ASP where d.planningCycleId="+planningCycleId+" and d."+productIdColumn+" =un.productId and d."+businessColumn+"=un.business and d."+productIdColumn+" =ASP.productId and d."+businessColumn+"=ASP.business and un.forecastPeriod=ASP.forecastPeriod order by un.forecastPeriod";
                        SQLQuery query = session.createSQLQuery(sql);
                        List forecastingValuesList = (List)query.list();
                        if(forecastingValuesList != null){
                                       int size =forecastingValuesList.size();
                                       
                                       for(int i=0;i<size;i++){
                                  Object[] rowList = (Object[])forecastingValuesList.get(i);
                                  List forecastRowsList=new ArrayList();
                               int rowSize=rowList.length;
                               for(int j=0;j<rowSize;j++){
                                      if(rowList[j]!=null){
                                             forecastRowsList.add(rowList[j].toString());
                                      }else{
                                             forecastRowsList.add("");
                                      }
                                      
                               }
                               flatFileList.add(forecastRowsList);
                                       }
                               }
                                       
                               return flatFileList;
                        } catch (HibernateException he) {
                               throw he;
                        }
                  }
           });
           logger.debug("Leaving from getFlatFile");
        return (List)object;
           }catch (Exception e) {
                  logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
                  ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "getUserFromUserName", 
                               ApplicationErrorCodes.APP_EC_43, ApplicationConstants.EXCEPTION, e);
                  throw applicationException;
           }
    }


	     
}
