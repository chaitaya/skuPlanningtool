package com.bridgei2i.common.dao;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.util.StringUtils;

import com.bridgei2i.common.constants.ApplicationConstants;
import com.bridgei2i.common.constants.ApplicationErrorCodes;
import com.bridgei2i.common.controller.LoadApplicationCacheService;
import com.bridgei2i.common.exception.ApplicationException;
import com.bridgei2i.common.util.ApplicationUtil;
import com.bridgei2i.common.util.CsvReaderWriter;
import com.bridgei2i.common.util.PropertiesUtil;
import com.bridgei2i.common.vo.Data;
import com.bridgei2i.common.vo.MasterErrorCodes;
import com.bridgei2i.common.vo.PageTemplate;
import com.bridgei2i.common.vo.Roles;
import com.bridgei2i.common.vo.Users;
import com.bridgei2i.service.UtilitiesService;
import com.bridgei2i.vo.BTBData;
import com.bridgei2i.vo.Data2;
import com.bridgei2i.vo.ForecastingASP;
import com.bridgei2i.vo.ForecastingESC;
import com.bridgei2i.vo.ForecastingPM;
import com.bridgei2i.vo.ForecastingUnits;
import com.bridgei2i.vo.MasterCommitStatus;
import com.bridgei2i.vo.MasterEventCalendar;
import com.bridgei2i.vo.MasterPlanningStatus;
import com.bridgei2i.vo.MasterWorkflowStatus;
import com.bridgei2i.vo.OverrideAspLog;
import com.bridgei2i.vo.OverrideAspLogComment;
import com.bridgei2i.vo.OverrideCommit;
import com.bridgei2i.vo.OverrideUnitsLog;
import com.bridgei2i.vo.OverrideUnitsLogComment;
import com.bridgei2i.vo.PlanningCycle;
import com.bridgei2i.vo.PlanningLog;
import com.bridgei2i.vo.RawData;
import com.bridgei2i.vo.SkuList;
import com.bridgei2i.vo.SkuUserMapping;
import com.bridgei2i.vo.TargetData;
import com.bridgei2i.vo.VariableNames;
import com.bridgei2i.vo.PlanningLogUserSessions;
import com.bridgei2i.common.controller.LoadApplicationCacheService;

import edu.emory.mathcs.backport.java.util.Collections;


public class PlanningDAO  {

	@Autowired(required=true)
	private UtilitiesDAO utilitiesDAO;
	
	@Autowired(required=true)
	private ApplicationDAO applicationDAO;
	
	@Autowired(required=true)
	private UtilitiesService utilitiesService;
	
	public static Map applicationCacheObject = new HashMap();
	
	private HibernateTemplate hibernateTemplate;
	private static Logger logger = Logger.getLogger(PlanningDAO.class);
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	public final List<String> getSKUForecastingWeeks(final int year,final int week,int range) throws ApplicationException{
	logger.debug("Entered into getSKUForecastingWeeks in planningDAO");
	int baseYear = year;
	int baseWeek = 0;
	int totalWeeks = 0;
	List <String> weeksList = new ArrayList<String>();
	try{
			if(year % 4 == 0 || year % 400 == 0 )
			{
				totalWeeks = 53;
			}
			else
			{
				totalWeeks = 52;
			}
			for (int i = 0 ; i < range ; i++)
			{
				if(week+i == totalWeeks)
				{
			baseYear = year+1;
					baseWeek = -1 * (totalWeeks - ( week + ( i + 1 ) ) ) ;
				}
				else
				{
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
			logger.debug("Leaving from getSKUForecastingWeeks method in planningDAO");
			return weeksList;
			}
	@SuppressWarnings("unchecked")
	public List getAllPlanningCycle() throws ApplicationException{
		Object object = null;
		final List planningCycleList = new ArrayList();
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					
					try {
						
						String sql = "select p.startWeek, p.startYear, p.closedDate,m.statusName,p.id,w.statusName from PlanningCycle p, MasterPlanningStatus m,MasterWorkflowStatus w"
								+ " where p.planningStatusId=m.id and p.workflowStatusId=w.id and p.closedDate= (select  max(p1.closedDate) from PlanningCycle p1,MasterPlanningStatus m1 where p1.planningStatusId=m1.id and m1.logicalName='CLOSED') and m.logicalName='CLOSED'";
						Query q = session.createQuery(sql);
						List lastCycleList = q.list();
						
	                     if(lastCycleList != null){
	                     	int size =lastCycleList.size();
	                     	for(int i=0;i<size;i++){
	                     		Object[] rowList = (Object[])lastCycleList.get(i);
	                     		PlanningCycle planningCycle = new PlanningCycle();
	                     		if(rowList[0]!=null){
	                     			planningCycle.setStartWeek( rowList[0].toString());
	                     		}
	                     		if(rowList[1]!=null){
	                     			planningCycle.setStartYear(rowList[1].toString());
	                     		}
	                     		if(rowList[2]!=null){
	                     			planningCycle.setClosedDate((Date)rowList[2]);
	                     		}
	                     		if(rowList[3]!=null){
	                     			planningCycle.setStatusName(rowList[3].toString());
	                     			
	                     		}
	                     		if(rowList[4]!=null){
	                     			planningCycle.setId((Integer)rowList[4]);
	                     		}
	                     		if(rowList[5]!=null){
	                     			planningCycle.setWorkFlowStatusName(rowList[5].toString());
	                     		}
	                     		planningCycleList.add(planningCycle);
					            break;
	                     	}
	                     }
						
						
						sql = "select p.startWeek, p.startYear, p.createDate,m.statusName,p.id,m.logicalName,w.statusName from PlanningCycle p, MasterPlanningStatus m,MasterWorkflowStatus w "
								+ " where p.planningStatusId=m.id and p.workflowStatusId=w.id and p.createDate= (select  max(p1.createDate) from PlanningCycle p1,MasterPlanningStatus m1 where p1.planningStatusId = m1.id and m1.logicalName !='CLOSED') and m.logicalName !='CLOSED'";
						
						q = session.createQuery(sql);
						List activeCycleList = q.list();
						
	                     if(activeCycleList != null){
	                     	int size =activeCycleList.size();
	                     	for(int i=0;i<size;i++){
	                     		Object[] rowList = (Object[])activeCycleList.get(i);
	                     		PlanningCycle planningCycle = new PlanningCycle();
	                     		if(rowList[0]!=null){
	                     			planningCycle.setStartWeek( rowList[0].toString());
	                     		}
	                     		if(rowList[1]!=null){
	                     			planningCycle.setStartYear(rowList[1].toString());
	                     		}
	                     		if(rowList[2]!=null){
	                     			planningCycle.setClosedDate((Date)rowList[2]);
	                     		}
	                     		if(rowList[3]!=null){
	                     			planningCycle.setStatusName(rowList[3].toString());
	                     			
	                     		}
	                     		if(rowList[4]!=null){
	                     			planningCycle.setId((Integer)rowList[4]);
	                     		}
	                     		if(rowList[5]!=null){
	                     			planningCycle.setLogicalName(rowList[5].toString());
	                     		}
	                     		if(rowList[6]!=null){
	                     			planningCycle.setWorkFlowStatusName(rowList[6].toString());
	                     		}
	                     		planningCycleList.add(planningCycle);
					            break;
	                     	}
	                     }
						
						/*DetachedCriteria innerCriteria = DetachedCriteria.forClass(PlanningCycle.class, "inner")
							    .setProjection(Projections.projectionList().add(Projections.max("inner.closedDate")));

						Criteria outerCriteria= DetachedCriteria.forClass(PlanningCycle.class, "outer");
						outerCriteria.add(Subqueries.propertyIn("outer.id", innerCriteria ));
						ProjectionList proj = Projections.projectionList();
						proj = proj.add(Projections.property("planningCycle.startWeek"));
						proj = proj.add(Projections.property("planningCycle.startYear"));
						proj = proj.add(Projections.property("planningCycle.closedDate"));
						proj = proj.add(Projections.property("planningCycle.masterPlanningStatus.statusName"));
						proj = proj.add(Projections.property("planningCycle.id"));
						outerCriteria = outerCriteria.setProjection(proj);
						Criteria query  = outerCriteria.getExecutableCriteria(session);
							
					Criteria query= session.createCriteria(PlanningCycle.class, "planningCycle")
							.createCriteria("masterPlanningStatus", "masterPlanningStatus")
						.add(Restrictions.eq("planningCycle.closedDate",innerCriteria))
						.add(Restrictions.eq("masterPlanningStatus.logicalName", "CLOSED"));
						
						ProjectionList proj = Projections.projectionList();
						proj = proj.add(Projections.property("planningCycle.startWeek"));
						proj = proj.add(Projections.property("planningCycle.startYear"));
						proj = proj.add(Projections.property("planningCycle.closedDate"));
						proj = proj.add(Projections.property("masterPlanningStatus.statusName"));
						proj = proj.add(Projections.property("planningCycle.id"));
						query = query.setProjection(proj);
						
						session.createCriteria(PlanningCycle.class,"o").add(Subqueries.propertyIn("o.key",distinctQuery)).list();
						List<Object[]> list = query.list();
						
				        for(Object[] arr : list){
				        	PlanningCycle planningCycle = new PlanningCycle();
				        	planningCycle.setStartWeek((String)arr[0]);
				        	planningCycle.setStartYear((String)arr[1]);
				        	planningCycle.setClosedDate((Timestamp)arr[2]);
				        	planningCycle.setStatusName((String)arr[3]);
				        	planningCycle.setId((Integer)arr[4]);
				        	planningCycleList.add(planningCycle);
				            break;
				        }
				        
				        DetachedCriteria subQuery = DetachedCriteria.forClass(PlanningCycle.class);
				        							//.setProjection(Projections.max("createDate"));
				        proj = Projections.projectionList();
				        proj.add(Projections.max("createDate"));
				        subQuery.setProjection(proj);
				        
				     Criteria query1 = session.createCriteria(PlanningCycle.class, "planningCycle")
				        				.setProjection(Projections.max("planningCycle.createDate"));
				        List<Object[]> list1 =query1.list();
				        
				        query= session.createCriteria(PlanningCycle.class, "planningCycle")
								.createCriteria("masterPlanningStatus", "masterPlanningStatus");
								//.add(Restrictions.eq("planningCycle.createDate", list1.get(0)));
				       
								proj = Projections.projectionList();
								proj = proj.add(Projections.property("planningCycle.startWeek"));
								proj = proj.add(Projections.property("planningCycle.startYear"));
								proj = proj.add(Projections.property("planningCycle.createDate"));
								proj = proj.add(Projections.property("masterPlanningStatus.statusName"));
								proj = proj.add(Projections.property("planningCycle.id"));
								proj = proj.add(Projections.property("masterPlanningStatus.logicalName"));
								query = query.setProjection(proj);
								
								list = query.list();
								
								for(Object[] arr : list){
						        	PlanningCycle planningCycle = new PlanningCycle();
						        	planningCycle.setStartWeek((String)arr[0]);
						        	planningCycle.setStartYear((String)arr[1]);
						        	planningCycle.setClosedDate((Timestamp)arr[2]);
						        	planningCycle.setStatusName((String)arr[3]);
						        	planningCycle.setId((Integer)arr[4]);
						        	planningCycle.setLogicalName((String)arr[5]);
						        	planningCycleList.add(planningCycle);
						            break;
						        }*/
								
								
								/*List ActualsList =  q.list();
			                     System.out.println(ActualsList);*/
						//planningCycleVO= (PlanningCycleVO)query.setResultTransformer(Transformers.aliasToBean(PlanningCycleVO.class)).uniqueResult();
						
						} catch (HibernateException he) {
							he.printStackTrace();
					}
					return (Object)planningCycleList;
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "saveContact", 
					ApplicationErrorCodes.APP_EC_2, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
		return (List)planningCycleList;
	}
	//for updating saved week
	@SuppressWarnings("unchecked")
	public  void updateSavedWeek(final Integer id,final String startWeek,final String startYear) throws ApplicationException{
		try{
			 hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
						
						PlanningCycle planningCycle =(PlanningCycle) session.load(PlanningCycle.class,id);
						planningCycle.setStartWeek(startWeek);
						planningCycle.setStartYear(startYear);
					} catch (HibernateException he) {
							he.printStackTrace();
							logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
					}
				return null;
			}
			});
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "saveContact", 
					ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
	}

	//for saving the week value
	@SuppressWarnings("unchecked")
	public Integer saveActiveWeek(PlanningCycle planningCycle) throws ApplicationException{
		Integer id;
		try{
			id=(Integer) hibernateTemplate.save(planningCycle);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "saveContact", 
					ApplicationErrorCodes.APP_EC_2, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
		return id;
	}
	

	public List getMasterErrorCodes() throws ApplicationException
	{
		try{
		logger.debug("Entered into getMasterErrorCodes");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				List MasterErrorCodesList = null;
				try {
                		 String sql = "from MasterErrorCodes";
	        			 Query q = session.createQuery(sql);
	        			 MasterErrorCodesList =  (List)q.list();
        			return MasterErrorCodesList;
				} catch (HibernateException he) {
					logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
					throw he;
				}
			}
		});
		logger.debug("Leaving from getMasterErrorCodes");
	    return (List)object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	}
	
	public void saveData(final String filePath,final String dbTable,final Integer referenceId,final String referenceColumnName,final boolean deleteTableDataBeforeLoad, final int planningCycleId) throws ApplicationException{
		Object object = null;
		logger.debug("Entered into saveData");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException{
					try {
						 Connection connection = session.connection();
						 session.beginTransaction();
                    	 
						 CsvReaderWriter csvReaderWriter = new CsvReaderWriter(connection);
						 ArrayList<String[]> dataFromExcel=csvReaderWriter.readWriteCSV(filePath, dbTable, deleteTableDataBeforeLoad ,referenceId,referenceColumnName,1);
						 
						 int size=dataFromExcel.size();
						 BigDecimal temp=null;
						 
						 Map categoryMap= (Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.CATEGORY_SCORECARD_ROLLUP_MAPPING);
						 
						 for(int i=0;i<size;i++)
						 {
							 RawData rawdata=new RawData();
							 String [] nextRecord=dataFromExcel.get(i);
							 if(nextRecord!=null)
							 {
								 rawdata.setPlanningCycleId(planningCycleId);
								 rawdata.setC1(nextRecord[0]);
								 rawdata.setC2(nextRecord[1]);
								 rawdata.setC3(nextRecord[2]);
								 rawdata.setC4(nextRecord[3]);
								 rawdata.setC5(nextRecord[4]);
                            	 rawdata.setC6(nextRecord[5]);
                            	 rawdata.setC7(nextRecord[6]);
                            	 rawdata.setC8(nextRecord[7]);
                            	 rawdata.setC9(nextRecord[8]);
                            	 rawdata.setC10(nextRecord[9]);
                            	 rawdata.setC11(nextRecord[10]);
                            	 if(checkIfNull(nextRecord[11]))
                            	 {
                            		 temp=new BigDecimal(nextRecord[11]);
                            		 rawdata.setC12(temp);
                            	 }
                            	 if(checkIfNull(nextRecord[12]))
                            	 {
                            		 temp=new BigDecimal(nextRecord[12]);
                            		 rawdata.setC13(temp);
                            	 }
                            	 if(checkIfNull(nextRecord[13]))
                            	 {
                            		 rawdata.setC14(Integer.parseInt(nextRecord[13]));
                            	 }
                            	 if(checkIfNull(nextRecord[14]))
                            	 {
                            		 rawdata.setC15(Integer.parseInt(nextRecord[14]));
                            	 }
								 if(checkIfNull(nextRecord[15]))
                            	 {
									 System.out.println("--"+nextRecord[15]+"--");
                            		 temp=new BigDecimal(nextRecord[15]);
                            		 rawdata.setC16(temp);
                            	 }
								 if(checkIfNull(nextRecord[16]))
                            	 {
                            		 temp=new BigDecimal(nextRecord[16]);
                            		 rawdata.setC17(temp);
                            	 }
								 if(checkIfNull(nextRecord[17]))
                            	 {
                            		 temp=new BigDecimal(nextRecord[17]);
                            		 rawdata.setC18(temp);
                            	 }
                            	 rawdata.setC19(nextRecord[18]);
                            	 rawdata.setC20(nextRecord[19]);
                            	 if(nextRecord[29].equalsIgnoreCase("Tablet Category")&&nextRecord[20].equalsIgnoreCase("Accy")){
                            		 rawdata.setC21("Tablet Accy");
                            	 }else{
                            		 rawdata.setC21(nextRecord[20]);
                            	 }
                            	 rawdata.setC22(nextRecord[21]);
                            	 rawdata.setC23(nextRecord[22]);
                            	 rawdata.setC24(nextRecord[23]);
                            	 if(checkIfNull(nextRecord[24]))
                            	 {
                            		 temp=new BigDecimal(nextRecord[24]);
                            		 rawdata.setC25(temp);
                            	 }
                            	 rawdata.setC26(nextRecord[25]);
                            	 rawdata.setC27(nextRecord[26]);
                            	 rawdata.setC28(nextRecord[27]);
                            	 rawdata.setC29(nextRecord[28]);
                            	 if(nextRecord[29].equalsIgnoreCase("DT_Con") || nextRecord[29].equalsIgnoreCase("DT (CON)")){
                            		 rawdata.setC30(PropertiesUtil.getProperty(ApplicationConstants.DT_CLIENT_CONSUMER));
                            	 }else if(nextRecord[29].equalsIgnoreCase("DT_Biz") || nextRecord[29].equalsIgnoreCase("DT (BUS)")){
                            		 rawdata.setC30(PropertiesUtil.getProperty(ApplicationConstants.DT_CLIENT_BUSINESS));
                            	 }else if(nextRecord[29].equalsIgnoreCase("NB_Con") || nextRecord[29].equalsIgnoreCase("NB (CON)")){
                            		 rawdata.setC30(PropertiesUtil.getProperty(ApplicationConstants.NB_CLIENT_CONSUMER));
                            	 }else if(nextRecord[29].equalsIgnoreCase("NB_Biz") || nextRecord[29].equalsIgnoreCase("NB (BUS)")){
                            		 rawdata.setC30(PropertiesUtil.getProperty(ApplicationConstants.NB_CLIENT_BUSINESS));
                            	 }else if(nextRecord[29].equalsIgnoreCase("Tablet Category")){
                            		 rawdata.setC30(PropertiesUtil.getProperty(ApplicationConstants.TABLET));
                            	 } else{
                            		 rawdata.setC30(nextRecord[29]);
                            	 }
                            	 rawdata.setC31(nextRecord[30]);
                            	 rawdata.setDerivedRegion("US");
                            	 if(categoryMap.containsKey(nextRecord[29]))
                            	 {
                            		 rawdata.setDerivedCategory(categoryMap.get(nextRecord[29]).toString());
                            	 }
							 }
							 
							 session.save(rawdata);
							
						 }
						 session.getTransaction().commit();
					} catch (HibernateException he) {
						logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
						session.getTransaction().rollback();
						throw he;
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
						session.getTransaction().rollback();
						throw new HibernateException(e);
					}
					return true;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
		logger.debug("Leaving from saveData");
	}
	
	public boolean checkIfNull(String inputStr)
	{
		if(ApplicationUtil.isEmptyOrNull(inputStr) || inputStr.equalsIgnoreCase("null"))
			return false;
		else 
			return true;
	}
	
	public void saveVariableNames(final String filePath,final String dbTable,final Integer referenceId,final String referenceColumnName,final boolean deleteTableDataBeforeLoad) throws ApplicationException{
		Object object = null;
		logger.debug("Entered into Save Variable Names");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
						Connection connection = session.connection();
						session.beginTransaction();
                    	String sql="From VariableNames";
                    	Query q=session.createQuery(sql);
        				List VariableNamesList=(List)q.list();
        				int size=VariableNamesList.size();

        				if(ApplicationUtil.isEmptyOrNull(size)||size==0)
                    	 {
                    		 CsvReaderWriter csvReaderWriter = new CsvReaderWriter(connection);
                    		 String [] variableNamesString=csvReaderWriter.saveVariableNames(filePath, dbTable, deleteTableDataBeforeLoad ,referenceId,referenceColumnName);
							 
							 for(int i=1;i<=36;i++)
							 {
								 VariableNames variableNames=new VariableNames();
								 if(i==1)
								 {
									 variableNames.setVariableName("ID");
									 variableNames.setColumnName("ID");
									 variableNames.setCreatedDate(new Date());
									 variableNames.setUpdatedDate(new Date());
								 }
								 else if(i==2)
								 {
									 variableNames.setVariableName("planningCycleId");
									 variableNames.setColumnName("planningCycleId");
									 variableNames.setCreatedDate(new Date());
									 variableNames.setUpdatedDate(new Date());
								 }
								 else
								 {
									 if(i<=33){
										 variableNames.setVariableName("C"+(i-2));
										 variableNames.setColumnName(variableNamesString[i-3]);
										 if(i==33){
											 variableNames.setAggregateFormula("r.C"+(i-2));
										 }
										 
									 }else if(i==34){
										 variableNames.setVariableName("derivedRegion");
										 variableNames.setColumnName("derivedRegion");
										 variableNames.setAggregateFormula("r.derivedRegion");
									 }else if(i==35){
										 variableNames.setVariableName("derivedCategory");
										 variableNames.setColumnName("derivedCategory");
										 variableNames.setAggregateFormula("r.derivedCategory");
									 }else{
										 variableNames.setVariableName("derivedProductType");
										 variableNames.setColumnName("derivedProductType");
										 variableNames.setAggregateFormula("r.derivedProductType");
									 }
									 variableNames.setCreatedDate(new Date());
									 variableNames.setUpdatedDate(new Date());
									 if(i==3||(i>6 && i<15)||i==16||(i>20&&i<27)||(i>27&&i<33))
									 {
										 variableNames.setAggregateFormula("r.C"+(i-2));
									 }
									 else if(i==17)
									 {
										 variableNames.setAggregateFormula("SUM(r.C15)");
									 }
									 else if(i==19)
									 {
										 variableNames.setAggregateFormula("(SUM(r.C15 * r.C17) / SUM(r.C15))");
										 
									 }
									 else if(i==20)
									 {
										 variableNames.setAggregateFormula("(1-((SUM(r.C15 * r.C17) / SUM(r.C15))/(SUM(r.C15 * r.C25) / SUM(r.C15))))*100");
									 }
									 else if(i==27)
									 {
										 variableNames.setAggregateFormula("(sum(r.C15 * r.C25) / sum(r.C15))");
									 }
								 }
								 session.save(variableNames);
							 }
                    		 session.getTransaction().commit();
                    	 }
					} catch (HibernateException he) {
						logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
						logger.error("Truncate variable names table and try again");
						System.out.println("Truncate variable names table and try again");
						session.getTransaction().rollback();
						throw he;
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
						session.getTransaction().rollback();
						throw new HibernateException("Error while inserting data into table");
					}
					return true;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "saveData", 
					ApplicationErrorCodes.APP_EC_12, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
		logger.debug("Leaving from save variable Names");
	}
		
	
	public void loadAggregatedDataIntoTable(final int planningCycleId)
	{
		 try{
	        logger.debug("Entered into load aggregated data into table");
			 Object object = hibernateTemplate.execute(new HibernateCallback() {
			@SuppressWarnings("unused")
			 public Object doInHibernate(Session session)throws HibernateException, SQLException 
			 {
				session.beginTransaction();
				String sql1="SELECT ";
				String sql2="from VariableNames";
				StringBuilder sb=new StringBuilder();
				String st=null,prefix="";
				
				Query q1=session.createQuery(sql2);
				
				List VariableNamesList=(List)q1.list();
				int size=VariableNamesList.size();
						
				for(int i=1;i<size;i++)
				{
					VariableNames variableNames =  (VariableNames)VariableNamesList.get(i);
					
						st=variableNames.getAggregateFormula();
					if(!ApplicationUtil.isEmptyOrNull(st))
					{
						sb.append(prefix);
						prefix=",";
					    sb.append(st);
					}
					
				}
				
				String groupProperty1="r."+PropertiesUtil.getProperty(ApplicationConstants.ORDER_WEEK);
				String groupProperty2="r."+PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_NUMBER);
				String groupProperty3="r."+PropertiesUtil.getProperty(ApplicationConstants.BUSINESS_TYPE);
				
				
				
				sql1+=sb.toString()+" "+"FROM RawData r where r.planningCycleId="+planningCycleId+"  GROUP BY "+groupProperty1+","+groupProperty2+","+groupProperty3+" ORDER BY "+groupProperty1+"";
				
				System.out.println("properties"+sql1);
				
				Query q2 = session.createQuery(sql1);
				
				List<Object []> listResult =q2.list();

				for (Object[] inputArray : listResult) {
						
					    Data actualdata=new Data();
						int length=inputArray.length;
						String [] nextRecord=new String[length];
						for(int i=0;i<length;i++)
						{
							if(ApplicationUtil.isEmptyOrNull(inputArray[i])){
								nextRecord[i]=null;
							}
							else
							{
								try {
									nextRecord[i]=inputArray[i].toString();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						 actualdata.setPlanningCycleId(planningCycleId);
						 actualdata.setC1(nextRecord[0]);
						 actualdata.setC5(nextRecord[1]);
			           	 actualdata.setC6(nextRecord[2]);
			           	 actualdata.setC7(nextRecord[3]);
			           	 actualdata.setC8(nextRecord[4]);
			           	 actualdata.setC9(nextRecord[5]);
			           	 actualdata.setC10(nextRecord[6]);
			           	 actualdata.setC11(nextRecord[7]);
			           	 actualdata.setC12(nextRecord[8]);
			           	 actualdata.setC14(nextRecord[9]);
			           	 actualdata.setC15(nextRecord[10]);
						 actualdata.setC17(nextRecord[11]);
						 actualdata.setC18(nextRecord[12]);
			           	 actualdata.setC19(nextRecord[13]);
			           	 actualdata.setC20(nextRecord[14]);
			           	 actualdata.setC21(nextRecord[15]);
			           	 actualdata.setC22(nextRecord[16]);
			           	 actualdata.setC23(nextRecord[17]);
			           	 actualdata.setC24(nextRecord[18]);
			           	 actualdata.setC25(nextRecord[19]);
			           	 actualdata.setC26(nextRecord[20]);
			           	 actualdata.setC27(nextRecord[21]);
			           	 actualdata.setC28(nextRecord[22]);
			           	 actualdata.setC29(nextRecord[23]);
			           	 actualdata.setC30(nextRecord[24]);
			           	 actualdata.setC31(nextRecord[25]);
			           	 actualdata.setDerivedRegion(nextRecord[26]);
			           	 actualdata.setDerivedCategory(nextRecord[27]);
			           	 actualdata.setDerivedProductType(nextRecord[28]);
			           	 session.save(actualdata);
				}
			 	session.getTransaction().commit();
			 	return true;
			   }
			 });
			 }catch (Exception e) {
			              logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			              throw e;
			 }
		 logger.debug("Leaving from load Aggregated data from table");
	}
	
	public void outlierTreatment(final int planningCycleId)throws ApplicationException
	{
		Object object = null;
		logger.debug("Entered into Outlier Treatment ");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
						Connection connection = session.connection();
						 String weekProperty=PropertiesUtil.getProperty(ApplicationConstants.ORDER_WEEK);
						 String skuProperty=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_NUMBER);
						 String businessTypeProperty=PropertiesUtil.getProperty(ApplicationConstants.BUSINESS_TYPE);
						 
						 String sql ="delete from Data2";
						 Query  query=session.createQuery(sql);
						 query.executeUpdate();
						
						 Criteria weekCriteria=session.createCriteria(Data.class);
 						weekCriteria.setProjection(Projections.max(weekProperty));
						
						 String  maxWeek=weekCriteria.uniqueResult().toString();
		   				 String temp=maxWeek.substring(6, 8);
						 String minWeek=maxWeek.substring(0, 6)+String.format("%02d",(Integer.parseInt(temp)-12));
						Criteria skuCriteria=session.createCriteria(Data.class);
						skuCriteria.setProjection(Projections.distinct(Projections.property(skuProperty)));
						skuCriteria.add(Restrictions.between(weekProperty, minWeek,maxWeek));
						skuCriteria.addOrder(Order.asc(skuProperty));
						List skuList=skuCriteria.list();

						
						
						for(int i=0;i<skuList.size();i++)
						{
							Criteria businessTypeCriteria=session.createCriteria(Data.class);
							businessTypeCriteria.add(Restrictions.eq(skuProperty, 
									skuList.get(i)));
							businessTypeCriteria.add(Restrictions.between(weekProperty, minWeek,maxWeek));
							businessTypeCriteria.setProjection(Projections.distinct(Projections.property(businessTypeProperty)));
							List businessTypes=businessTypeCriteria.list();
							if(businessTypes.size()>1)
								System.out.println("BusinessTypes\t"+skuList.get(i)+"\t"+businessTypes.get(0)+"\t"+businessTypes.get(1));
							
							
							for(int j=0;j<businessTypes.size();j++){
								
							Criteria dataCriteria = session.createCriteria(Data.class);
							dataCriteria.add(Restrictions.eq(skuProperty, skuList.get(i)));
							dataCriteria.add(Restrictions.eq(businessTypeProperty, businessTypes.get(j)));
							dataCriteria.add(Restrictions.between(weekProperty, minWeek,maxWeek));
							dataCriteria.addOrder(Order.desc(weekProperty));
							List<Data> datatable =dataCriteria.list();
							int count=0;
							double sd=0,mean=0,sum=0;
							DescriptiveStatistics stats = new DescriptiveStatistics();
							
							for (Data nextRecord : datatable) {
								if(!ApplicationUtil.isEmptyOrNull(nextRecord.getC15())){
									sum +=Integer.parseInt(nextRecord.getC15());
								}
								if(!ApplicationUtil.isEmptyOrNull(nextRecord.getC15()))
								{
									stats.addValue(Double.parseDouble(nextRecord.getC15()));
									count++;
								}
								
							}
							
							if(!ApplicationUtil.isEmptyOrNull(sum)&&sum!=0)
								mean=(double)(sum/count);
							
							if(!ApplicationUtil.isEmptyOrNull(mean))
								sd=Math.sqrt(stats.getPopulationVariance())*2;
								insertDataIntoOutlierTable((mean+sd),(mean-sd),skuList.get(i).toString(),businessTypes.get(j).toString(),minWeek,maxWeek);
							
						}
					}
					} catch (HibernateException he) {
logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
						session.getTransaction().rollback();
						throw he;
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
						session.getTransaction().rollback();
						throw new HibernateException("Outlier treatment failed");
					}
					return true;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "saveData", 
					ApplicationErrorCodes.APP_EC_12, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
		logger.debug("Leaving from Outlier Treatment");
	
	}
	
	void insertDataIntoOutlierTable(final double cond1,final double cond2,final String sku,final String businessType,final String minWeek,final String maxWeek) throws ApplicationException{
		Object object = null;
		logger.debug("Entered into Insert data into Outlier Table ");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
							Connection connection = session.connection();
							session.beginTransaction();
							
							String property1=PropertiesUtil.getProperty(ApplicationConstants.ORDER_WEEK);
							String property2=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_NUMBER);
							String weekProperty=PropertiesUtil.getProperty(ApplicationConstants.ORDER_WEEK);
							String businessTypeProperty=PropertiesUtil.getProperty(ApplicationConstants.BUSINESS_TYPE);
							Criteria dataCriteria = session.createCriteria(Data.class);
							dataCriteria.add(Restrictions.eq(property2,sku));
							dataCriteria.add(Restrictions.eq(businessTypeProperty,businessType));
							dataCriteria.add(Restrictions.between(weekProperty, minWeek,maxWeek));
							dataCriteria.addOrder(Order.desc(property2));
							List<Data> datatable =dataCriteria.list();

							for (Data nextRecord : datatable) {

								Data2 actualdata2=new Data2();
								
								 actualdata2.setPlanningCycleId(nextRecord.getPlanningCycleId());
								 actualdata2.setC1(nextRecord.getC1());
								 actualdata2.setC5(nextRecord.getC5());
					           	 actualdata2.setC6(nextRecord.getC6());
					           	 actualdata2.setC7(nextRecord.getC7());
					           	 actualdata2.setC8(nextRecord.getC8());
					           	 actualdata2.setC9(nextRecord.getC9());
					           	 actualdata2.setC10(nextRecord.getC10());
					           	 actualdata2.setC11(nextRecord.getC11());
					           	 actualdata2.setC12(nextRecord.getC12());
					           	 actualdata2.setC14(nextRecord.getC14());
					           	 actualdata2.setC15(Double.toString(checkOutlierCondition(cond1,cond2,nextRecord.getC15())));
								 actualdata2.setC17(nextRecord.getC17());
								 actualdata2.setC18(nextRecord.getC18());
					           	 actualdata2.setC19(nextRecord.getC19());
					           	 actualdata2.setC20(nextRecord.getC20());
					           	 actualdata2.setC21(nextRecord.getC21());
					           	 actualdata2.setC22(nextRecord.getC22());
					           	 actualdata2.setC23(nextRecord.getC23());
					           	 actualdata2.setC24(nextRecord.getC24());
					           	 actualdata2.setC25(nextRecord.getC25());
					           	 actualdata2.setC26(nextRecord.getC26());
					           	 actualdata2.setC27(nextRecord.getC27());
					           	 actualdata2.setC28(nextRecord.getC28());
					           	 actualdata2.setC29(nextRecord.getC29());
					           	 actualdata2.setC30(nextRecord.getC30());
					           	 actualdata2.setC31(nextRecord.getC31());
					           	 actualdata2.setDerivedRegion(nextRecord.getDerivedRegion());
					           	 actualdata2.setDerivedCategory(nextRecord.getDerivedCategory());
					           	 actualdata2.setDerivedProductType(nextRecord.getDerivedProductType());
					           	 session.save(actualdata2);
							}
							session.getTransaction().commit();
                    	 
					} catch (HibernateException he) {
logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
						session.getTransaction().rollback();
						throw he;
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
						session.getTransaction().rollback();
						throw new HibernateException("Outlier treatment failed");
					}
					return true;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "saveData", 
					ApplicationErrorCodes.APP_EC_12, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
		logger.debug("Leaving from outlier Table");
	}
	
	double checkOutlierCondition(double con1,double con2,String quantity){
		
		int quant=0;
		if(!ApplicationUtil.isEmptyOrNull(quantity)){
			quant = Integer.parseInt(quantity); 
		}
		if(quant==0)
		{
			return 0;
		}
		else if(quant > con1)
		{
			return Math.round(con1);
		}
		else if(quant<con2)
		{
			return Math.round(con2);
		}
		else
		{
			return Integer.parseInt(quantity);
		}
	}
	
	public void updateSKUList(final int planningCycleId)throws ApplicationException{
		try{
			logger.debug("Entered into updateSKUList");
			Object object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
						
							 String productIdColumn=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_NUMBER);
	                		 String sql = "Insert into sku_list (productId) Select distinct r."+productIdColumn+" from raw_data r where planningCycleId="+planningCycleId+" and r."+productIdColumn+" not in (Select productId from sku_list)";
	                		 Query q = session.createSQLQuery(sql);
		        			 q.executeUpdate();
					} catch (HibernateException he) {
						throw he;
					}
					  return true;
				}
			});
			logger.debug("Leaving from updateSKUList");
		  
			}catch (Exception e) {
				logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
				ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "updateSKUList", 
						ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
				throw applicationException;
			}
	}
	
	
	public Users getUserFromUserName(final String userName) throws ApplicationException
	{
		try{
		logger.debug("Entered into getUserFromUserName");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
					Users user = null;
                		 String sql = "from Users as user where LOWER(user.userName) = ?";
	        			 Query q = session.createQuery(sql)
	        			 			.setString(0, StringUtils.trimWhitespace(userName.toLowerCase()));
	        			 user =  (Users)q.uniqueResult();
        			return user;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from getUserFromUserName");
	    return (Users)object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "getUserFromUserName", 
					ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
		
	}

	
	@SuppressWarnings("unchecked")
	public    Object getMasterPlanningStatusByStatusName(final String statusName) throws ApplicationException{
		Object object = null;
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					String masterPlanningStatus=null;
					try {
						Criteria query= session.createCriteria(MasterPlanningStatus.class)
						.add(Restrictions.eq("statusName", statusName));
						masterPlanningStatus=(String)query.uniqueResult();
				
						} catch (HibernateException he) {
							he.printStackTrace();
					}
					return masterPlanningStatus;
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
			ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "saveContact", 
					ApplicationErrorCodes.APP_EC_2, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
		return object;
	}
	public int getMasterPlanningStatusIdByName (final String logicalName) throws ApplicationException
	{
		logger.debug("Entering getMasterPlanningStatusIdByName method in planningDAO");
		
		Object object = null;
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
						Criteria criteria = session.createCriteria(MasterPlanningStatus.class);
						ProjectionList proj = Projections.projectionList();
						proj.add(Projections.property("id"));
						criteria.add(Restrictions.eq("logicalName", logicalName));
						criteria.setProjection(proj);
						return criteria.list().get(0);
						} catch (HibernateException he) {
							session.getTransaction().rollback();
	logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
					}
					return true;
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
		}
		logger.debug("Leaving from getMasterPlanningStatusIdByName method in planningDAO");
		return (int)object;
	}
	public int getMasterWorkflowStatusIdByName (final String logicalName) throws ApplicationException
	{
		logger.debug("Entering getMasterWorkflowStatusIdByName method in planningDAO");
		
		Object object = null;
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
						Criteria criteria = session.createCriteria(MasterWorkflowStatus.class);
						ProjectionList proj = Projections.projectionList();
						proj.add(Projections.property("id"));
						criteria.add(Restrictions.eq("logicalName", logicalName));
						criteria.setProjection(proj);
						return criteria.list().get(0);
						} catch (HibernateException he) {
							session.getTransaction().rollback();
							logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
					}
					return true;
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
		}
		logger.debug("Leaving from getMasterWorkflowStatusIdByName method in planningDAO");
		return (int)object;
	}

	public List getPageTemplates() throws ApplicationException
	{
		try{
		logger.debug("Entered into getPageTemplates");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				List PageTemplatesList = null;
				try {
                		 String sql = "from PageTemplate";
	        			 Query q = session.createQuery(sql);
	        			 PageTemplatesList =  (List)q.list();
        			return PageTemplatesList;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from getPageTemplates");
	    return (List)object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	}
	
	
	public List getPageTemplateFilters(final Integer userId,final String filterVariable, final String tableName,final String whereCondition,final List rolesList,final String businessValue,final Integer week,final Integer year) throws ApplicationException
	{
		try{
		logger.debug("Entered into getPageTemplateFilters");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				List dataList = null;
				try {
						String sql = null;
						String businessColumn = PropertiesUtil.getProperty(ApplicationConstants.BUSINESS_TYPE);
						String skuId=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_NUMBER);
						String categoryColumn = PropertiesUtil.getProperty(ApplicationConstants.CATEGORY);
						String eolWhereClause=""; 
						if(week!=null && year!=null){
							eolWhereClause="(skuList.eolWeek is null or ("+week+"<=skuList.eolWeek and "+year+"<=skuList.eolYear)) ";
						}else{
							eolWhereClause="1=1";
						}
						if(!ApplicationUtil.isEmptyOrNull(whereCondition)){
							String roleFilterCondition="";
							if(rolesList != null && rolesList.size()>0 && rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){
								String businessWhereClause="";
								if(!ApplicationUtil.isEmptyOrNull(businessValue)){
									businessWhereClause = " and skuUser.business = '"+businessValue+"'";
								}
								roleFilterCondition = " AND data."+skuId+" IN (select skuList.productId from SkuList as skuList where "+eolWhereClause+" and skuList.id IN "
									+ "(select skuUser.skuListId from SkuUserMapping as skuUser where skuUser.userId="+userId+businessWhereClause+")) "
											 + " AND data."+businessColumn+" IN (select distinct skuUser.business from SkuUserMapping as skuUser where skuUser.userId="+userId+") ";
							}else if (rolesList != null && rolesList.size()>0 && rolesList.contains(ApplicationConstants.CATEGORY_MANAGER)){
							roleFilterCondition = " AND data."+skuId+" IN (select skuList.productId from SkuList as skuList where "+eolWhereClause+") and data."+categoryColumn+" IN (select catList.categoryName from CategoryList as catList where catList.id IN "
									+ "(select catUser.categoryId from CategoryUserMapping as catUser where catUser.userId="+userId+")) "
											+ " AND data."+businessColumn+" IN (select distinct catUser.business from CategoryUserMapping as catUser where catUser.userId = "+userId+")";
							}
							sql = "select distinct data."+filterVariable+" from  "+tableName+" data  where data."+skuId+" IN (select skuList.productId from SkuList as skuList where "+eolWhereClause+") and 1=1 "+whereCondition+roleFilterCondition+" and data."+filterVariable+" is not null order by data."+filterVariable+" asc";	
						}else{
							sql = "select distinct d."+filterVariable+" from  "+tableName+" d where d."+skuId+" IN (select skuList.productId from SkuList as skuList where "+eolWhereClause+") and  d."+filterVariable+" is not null order by d."+filterVariable+" asc";
						}
                		 Query q = session.createQuery(sql);
	        			 dataList =  q.list();
        			return dataList;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from getPageTemplateFilters");
	    return (List)object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	}
	
	
	
	public Map getChartJsonFromDatabase(final String tableName, final String productId, final Integer planningCycleId,final String business,final List rolesList,final Integer userId,final Integer week,final Integer year,final Integer range ){
		
		try{
			logger.debug("Entered into getChartJsonFromDatabase");
            Object object = hibernateTemplate.execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                         throws HibernateException, SQLException {
            	String pattern = "###.##";
        		DecimalFormat decimalFormat = new DecimalFormat(pattern);
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
                //List exportExcelList=new ArrayList();
                Criteria criteria = session.createCriteria(ForecastingUnits.class, "forecastUnitObj");
                List forecastingUnitsList=null;
                List baseForecastAspList=null;
                List currentForecastUnitsList =new ArrayList();
                List currentForecastAspList =new ArrayList();
                List actualsWeekList =new ArrayList();
                StringBuffer actualWeeksBuffer = new StringBuffer();
                List actualWeeksList=null;
				try {
					actualWeeksList = getSKUForecastWeek(year,week,range,1);
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
                if(tableName.equalsIgnoreCase("ForecastingUnits")){
                	columName=  unitsColumn;
                	sql="From "+tableName+" p where p.productId = '"+productId+"' and  p.business = '" +business+"' and p.planningCycleId="+planningCycleId;
                	Query q=session.createQuery(sql);
                	forecastingUnitsList = (List)q.list();
                	jsonViewMapObj.put("baseForecastUnisList", forecastingUnitsList);
                	sql = "select d."+orderWeek+",d."+columName+" from Data d where d."+productIdColumn+" = '"+productId+"' and "
                			+ " d."+businessColumn+" = '" +business+"' and d."+orderWeek+" in ("+actualWeeksBuffer+")";
	    			 /*sql1 = "select forecastPeriod, forecastValue,b.overrideValue from "+tableName+" f left join com.bridgei2i.vo.OverrideUnitsLog b with f.id =b.forecastingUnitsId where planningCycleId="+planningCycleId+" and productId='"+productId+"' and "
	    					 + " business = '" +business+"'  group by forecastPeriod";*/
	    			 
	    			 
				       /* criteria.createAlias("forecastUnitObj.overrideUnit", "overrideUnit",Criteria.LEFT_JOIN);
				        Criterion rest1 =  Restrictions.eq("planningCycleId", planningCycleId);
				        Criterion rest2 =  Restrictions.eq("productId", productId);
				        Criterion rest3 =  Restrictions.eq("business", business);
			        	LogicalExpression andExp = Restrictions.and(rest1, rest2);
			        	LogicalExpression andExp1 = Restrictions.and(andExp, rest3);
				        criteria.add(andExp1);
			        
				        ProjectionList columns= Projections.projectionList();
				        columns.add(Projections.property("forecastPeriod"));
				        columns.add(Projections.property("forecastValue"));
				        columns.add(Projections.property("id"));
				        columns.add(Projections.property("overrideUnit.overrideValue"));
				        columns.add(Projections.groupProperty("forecastPeriod"));
				        criteria.setProjection(columns);*/
                	if(rolesList != null && rolesList.size()>0 && (!rolesList.contains(ApplicationConstants.PRODUCT_MANAGER))){
                		sql1 ="select forecastPeriod, round(forecastValue),a.id,round(overridevalue) from forecasting_units a left join (SELECT a.* FROM override_units_log a join "+
        				        "(select forecastingUnitsId,max(createdDate) as mdate from override_units_log where userId!="+userId +" group by forecastingUnitsId) b on "
        				        + "a.forecastingUnitsId= b.forecastingUnitsId join forecasting_units f on f.id=b.forecastingUnitsId and f.planningCycleId = "+planningCycleId+" and a.createdDate=b.mdate) b on a.id = b.forecastingUnitsId where planningCycleId = '"+planningCycleId +"' and productId = '"+productId+"'"
        				        + " and business = '" +business+"' group by forecastperiod";
                		SQLQuery query = session.createSQLQuery(sql1);
                		List forecastingValuesList = (List)query.list();
           			 	if(forecastingValuesList != null){
    	   			 		int size =forecastingValuesList.size();
    	   			 		
    	   			 		for(int i=0;i<size;i++){
    	              		   Object[] rowList = (Object[])forecastingValuesList.get(i);
    	                  		String forecastPeriod="";
    	                  		String forecastValue="";
    	                  		List forecastTable = new ArrayList();
    	                  		if(rowList[0]!=null){
    	                  			forecastPeriod = rowList[0].toString();
    	                  		}
    	                  		if(rowList[3]!=null){
                     				forecastValue = rowList[3].toString();
                     			} 
                     			if(rowList[3]==null && rowList[1]!=null){
                     				forecastValue = rowList[1].toString();
                     			}
    	              			ForecastingUnits forecastUnits= new ForecastingUnits();
    	              			int id = Integer.parseInt(rowList[2].toString());
                    			forecastUnits.setId(id);
    	              			forecastUnits.setForecastPeriod(forecastPeriod);
    	              			forecastUnits.setForecastValue(forecastValue);
    	              			currentForecastUnitsList.add(forecastUnits);
    	   			 		}
    	   			 		jsonViewMapObj.put("currentForecastUnitsList", currentForecastUnitsList);
           			 	}
                	}
                	
                	
                	
                	sql1 ="select forecastPeriod, cast(round(forecastValue) as char(30)),a.id,cast(round(overridevalue) as char(30)) from forecasting_units a left join (SELECT a.* FROM override_units_log a join "+
				        "(select forecastingUnitsId,max(createdDate) as mdate from override_units_log group by forecastingUnitsId) b on "
				        + "a.forecastingUnitsId= b.forecastingUnitsId join forecasting_units f on f.id=b.forecastingUnitsId and f.planningCycleId = "+planningCycleId+" and a.createdDate=b.mdate) b on a.id = b.forecastingUnitsId where planningCycleId = '"+planningCycleId +"' and productId = '"+productId+"'"
				        + " and business = '" +business+"' group by forecastperiod";
	    			 
                	
                }  else if (tableName.equalsIgnoreCase("ForecastingASP")){
                	columName=  aspColumn;
                	sql="From "+tableName+" p where p.productId = '"+productId+"' and  p.business = '" +business+"' and p.planningCycleId="+planningCycleId;
                	Query q=session.createQuery(sql);
                	baseForecastAspList = (List)q.list();
                	jsonViewMapObj.put("baseForecastAspList", baseForecastAspList);
                	sql = "select d."+orderWeek+",cast(round(d."+columName+",2) as string) from Data d where d."+productIdColumn+" = '"+productId+"' and "
                			+ " d."+businessColumn+" = '" +business+"' and d."+orderWeek+" in ("+actualWeeksBuffer+")";
                	if(rolesList != null && rolesList.size()>0 && ( !rolesList.contains(ApplicationConstants.PRODUCT_MANAGER) )){
                		sql1 ="select forecastPeriod, cast(round(forecastValue,2) as char(30)),a.id,overridevalue from forecasting_asp a left join (SELECT a.* FROM override_asp_log a join "+
        				        "(select forecastingAspId,max(createdDate) as mdate from override_asp_log where userId!="+userId +" group by forecastingAspId) b on "
        				        + "a.forecastingAspId= b.forecastingAspId join forecasting_asp f on f.id=b.forecastingAspId and f.planningCycleId="+planningCycleId+" and a.createdDate=b.mdate) b on a.id = b.forecastingAspId where planningCycleId = '"+planningCycleId +"' and productId = '"+productId+"'"
        				        + " and business = '" +business+"' group by forecastperiod";
                		SQLQuery query = session.createSQLQuery(sql1);
                		List forecastingValuesList = (List)query.list();
           			 	if(forecastingValuesList != null){
    	   			 		int size =forecastingValuesList.size();
    	   			 		
    	   			 		for(int i=0;i<size;i++){
    	              		   Object[] rowList = (Object[])forecastingValuesList.get(i);
    	                  		String forecastPeriod="";
    	                  		String forecastValue="";
    	                  		List forecastTable = new ArrayList();
    	                  		if(rowList[0]!=null){
    	                  			forecastPeriod = rowList[0].toString();
    	                  		}
    	                  		if(rowList[3]!=null){
                     				forecastValue = rowList[3].toString();
                     			} 
                     			if(rowList[3]==null && rowList[1]!=null){
                     				forecastValue = rowList[1].toString();
                     			}
    	              			ForecastingUnits forecastUnits= new ForecastingUnits();
    	              			int id = Integer.parseInt(rowList[2].toString());
                    			forecastUnits.setId(id);
    	              			forecastUnits.setForecastPeriod(forecastPeriod);
    	              			forecastUnits.setForecastValue(forecastValue);
    	              			currentForecastAspList.add(forecastUnits);
    	   			 		}
    	   			 		jsonViewMapObj.put("currentForecastAspList", currentForecastAspList);
           			 	}
                	}
                	sql1 ="select forecastPeriod, cast(round(forecastValue,2) as char(20)),a.id,overridevalue from forecasting_asp a left join (SELECT a.* FROM override_asp_log a join "+
    				        "(select forecastingAspId,max(createdDate) as mdate from override_asp_log group by forecastingAspId)b on "
    				        + "a.forecastingAspId= b.forecastingAspId join forecasting_asp f on f.id=b.forecastingAspId and f.planningCycleId = "+planningCycleId+" and a.createdDate=b.mdate) b on a.id = b.forecastingAspId where planningCycleId = '"+planningCycleId +"' and productId = '"+productId+"'"
    				        + " and business = '" +business+"' group by forecastperiod";
                } else if (tableName.equalsIgnoreCase("ForecastingRevenue")){
                	sql = "select d."+orderWeek+",cast(round(d."+unitsColumn+" * d."+aspColumn+",2) as string) from Data d where d."+productIdColumn+" = '"+productId+"' and "
                			+ " d."+businessColumn+" = '" +business+"' and d."+orderWeek+" in ("+actualWeeksBuffer+")";

                	/*sql1 ="select fu.forecastPeriod , fu.forecastValue * fa.forecastValue from ForecastingUnits fu, ForecastingASP fa "+
							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business =fa.business  and fa.planningCycleId = "+planningCycleId+
							" and fu.planningCycleId= "+planningCycleId+" and fu.productId = '"+productId+"' and "+
							"  fu.business = '" +business+"'";*/
                	
                	sql1 ="select fu.forecastPeriod , cast(round(fu.OriginalForecast * fa.OriginalForecast,2) as char(30)) from (SELECT f.forecastPeriod,f.productId,f.business,f.planningCycleId,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
       			 			+ " FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from override_units_log group by forecastingUnitsId) b  "
       			 			+ "on a.forecastingUnitsId= b.forecastingUnitsId join forecasting_units f on f.id=b.forecastingUnitsId and a.createdDate=b.mdate and f.planningCycleId="+planningCycleId+") b on f.id = b.forecastingUnitsId where f.planningCycleId = "+planningCycleId+"  and f.productId = '"+productId+"' and f.business = '"+business+"') fu, (SELECT f.forecastPeriod,f.productId,f.business,f.planningCycleId,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
       			 			+ " FROM forecasting_asp f left join (SELECT a.* FROM override_asp_log a join (select forecastingAspId,max(createdDate) as mdate from override_asp_log group by forecastingAspId) b  "
       			 			+ "on a.forecastingAspId= b.forecastingAspId join forecasting_asp f on f.id=b.forecastingAspId and a.createdDate=b.mdate and f.planningCycleId="+planningCycleId+") b on f.id = b.forecastingAspId where f.planningCycleId = "+planningCycleId+"  and f.productId = '"+productId+"' and f.business = '"+business+"') fa "+
							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business =fa.business  and fa.planningCycleId = "+planningCycleId+
							" and fu.planningCycleId= "+planningCycleId+" and fu.productId = '"+productId+"' and "+
							"  fu.business = '" +business+"'";
                	System.out.println("Revenue "+sql1);
                	
                } else if (tableName.equalsIgnoreCase("ForecastingESC")){
                	columName=  PropertiesUtil.getProperty("ESC");
                	
                } else {
                	columName=  PropertiesUtil.getProperty("productMargin");
                	
                }
                
            	try {
            		if(!tableName.equalsIgnoreCase("ForecastingRevenue") && !tableName.equalsIgnoreCase("ForecastingASP") && !tableName.equalsIgnoreCase("ForecastingUnits")){
            			 sql = "select d."+orderWeek+",cast(round(d."+columName+",2) as string) from Data d where d."+productIdColumn+" = '"+productId+"' and "
                			+ " d."+businessColumn+" = '" +business+"' and d."+orderWeek+" in ("+actualWeeksBuffer+")";
            			 sql1 = "select forecastPeriod, cast(round(forecastValue,2) as string),id from "+tableName+" where planningCycleId="+planningCycleId+" and productId='"+productId+"' and "
            					 + " business = '" +business+"'";
            		}
            		
            		 Query q = session.createQuery(sql);
            		 List ActualsList =  q.list();
            		 Map actualsListMap = new HashMap();
                     if(ActualsList != null){
                     	int size =ActualsList.size();
                     	/*int limit = ApplicationConstants.LIMIT;
                     	int size1= 0;
                     	if(size>limit){
                     		size1=size-limit;
                     	}*/ 
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
                     		/*forecastBuffer.append("{ w: '"+actualPeriod+"', a: "+actualValue+"}");
                     		actualTable.add(actualPeriod);
                     		actualTable.add(actualValue);
                     		actualTable.add("---");
                     		if(tableName.equalsIgnoreCase("ForecastingASP") || tableName.equalsIgnoreCase("ForecastingUnits")){
                     			actualTable.add("---");
                     			actualTable.add("---");
                     		}
                     		jsonTableList.add(actualTable);
                     		if(i<size-1){
                      			forecastBuffer.append(",");
                      		}
                      		forecastBuffer.append("\n");*/
                     	}
                     }
                     
                     try {
                     	actualsWeekList= getSKUForecastWeek(year,week,range,1);
                     	int size =actualsWeekList.size();
                     	for(int i=size-1;i>=0;i--){
                     		List actualTable= new ArrayList();
                     		String actualPeriod=(String) actualsWeekList.get(i);
                     		String actualValue="";
                     		if(actualsListMap.containsKey(actualPeriod)){
                     			
                     			actualValue= (String) actualsListMap.get(actualPeriod);
                     			if(ApplicationUtil.isEmptyOrNull(actualValue)){
                     				actualValue="0";
                     			}
                     		} else{
                     			actualValue="0";
                     		}
                     		forecastBuffer.append("{ w: '"+actualPeriod+"', a: "+actualValue+"}");
                     		actualTable.add(actualPeriod);
                     		actualTable.add(actualValue);
                     		actualTable.add("---");
                     		if(tableName.equalsIgnoreCase("ForecastingASP") || tableName.equalsIgnoreCase("ForecastingUnits")){
                     			actualTable.add("---");
                     			actualTable.add("---");
                     		}
                     		jsonTableList.add(actualTable);
                     		if(i>0){
                      			forecastBuffer.append(",");
                      		}
                      		forecastBuffer.append("\n");
                     		
                     	}
                     	
     				} catch (ApplicationException e) {
     					// TODO Auto-generated catch block
     					e.printStackTrace();
     				}
            		
                     List overrideForecastUnitsList = new ArrayList();
                     List overrideForecastAspList = new ArrayList();
                           List ForecastingValuesList =  null;
                  		 if(!tableName.equalsIgnoreCase("ForecastingUnits") && !tableName.equalsIgnoreCase("ForecastingASP") && !tableName.equalsIgnoreCase("ForecastingRevenue")){
                  			q = session.createQuery(sql1);
                  			ForecastingValuesList =  (List)q.list();
                  		 } else{
                  			//ForecastingValuesList = criteria.list();
                  			SQLQuery query = session.createSQLQuery(sql1);
                  			ForecastingValuesList =  (List)query.list();
                  		 }
                           if(ForecastingValuesList != null){
                        	   forecastBuffer.append(", \n");
                        	   int size =ForecastingValuesList.size();
                        	   for(int i=0;i<size;i++){
                        		   Object[] rowList = (Object[])ForecastingValuesList.get(i);
                            		String forecastPeriod="";
                            		String forecastValue="";
                            		List forecastTable = new ArrayList();
                            		if(rowList[0]!=null){
                            			forecastPeriod = rowList[0].toString();
                            		}
                            		/*if(rowList[1]!=null){
                            			forecastValue = rowList[1].toString();
                            		}*/
                            		
                            		if(tableName.equalsIgnoreCase("ForecastingUnits") || tableName.equalsIgnoreCase("ForecastingASP")){
                             			if(rowList[3]!=null){
                             				forecastValue = rowList[3].toString();
                             			} 
                             			if(rowList[3]==null && rowList[1]!=null){
                             				forecastValue = rowList[1].toString();
                             			}
                             			
                             		} else {
                             			if(rowList[1]!=null){
                             				forecastValue = rowList[1].toString();
                                 		}
                             			
                             		}
                            		if(ApplicationUtil.isEmptyOrNull(forecastValue)){
                            			forecastValue="0";
                            		}
                            		if(tableName.equalsIgnoreCase("ForecastingUnits")){
                            			ForecastingUnits forecastUnits= new ForecastingUnits();
                            			int id = Integer.parseInt(rowList[2].toString());
                            			forecastUnits.setId(id);
                            			forecastUnits.setForecastPeriod(forecastPeriod);
                            			forecastUnits.setForecastValue(forecastValue);
                            			overrideForecastUnitsList.add(forecastUnits);
                            		}else if(tableName.equalsIgnoreCase("ForecastingASP")){
                            			ForecastingASP forecastingASP = new ForecastingASP();
                            			int id = Integer.parseInt(rowList[2].toString());
                            			forecastingASP.setId(id);
                            			forecastingASP.setForecastPeriod(forecastPeriod);
                            			forecastingASP.setForecastValue(forecastValue);
                            			overrideForecastAspList.add(forecastingASP);
                             		}
                            		forecastTable.add(forecastPeriod);
                            		forecastTable.add("---");
                            		if(tableName.equalsIgnoreCase("ForecastingUnits")){
                            			forecastBuffer.append("{ w: '"+forecastPeriod+"', b: "+forecastValue);
                            			ForecastingUnits forecastUnits = (ForecastingUnits) forecastingUnitsList.get(i);
                            			String forecastValueStr = forecastUnits.getForecastValue();
                            			if(ApplicationUtil.isEmptyOrNull(forecastValueStr)){
                            				forecastValueStr="0";
                            				forecastBuffer.append(" , c: "+forecastValueStr);
                                			forecastTable.add(forecastValueStr);
                            			}
                            			if(!ApplicationUtil.isEmptyOrNull(forecastValueStr)){
                            				double forecastingValue=Math.round(Double.parseDouble(forecastValueStr));
                            				forecastBuffer.append(" , c: "+forecastingValue);
                                			forecastTable.add(forecastingValue);
                            			}
                            			
                            			if(rolesList != null && rolesList.size()>0 && ( !rolesList.contains(ApplicationConstants.PRODUCT_MANAGER) )){
                            				ForecastingUnits currentForecastUnits = (ForecastingUnits)currentForecastUnitsList.get(i);
                            				forecastValueStr = currentForecastUnits.getForecastValue();
                            				if(ApplicationUtil.isEmptyOrNull(forecastValueStr)){
                            					forecastValueStr="0";
                            					forecastBuffer.append(" , d: "+forecastValueStr+"}");
                                				forecastTable.add(forecastValueStr);
                            				}
                            				if(!ApplicationUtil.isEmptyOrNull(forecastValueStr)){
                                				double forecastingValue=Math.round(Double.parseDouble(forecastValueStr));
                                				forecastBuffer.append(" , d: "+forecastingValue+"}");
                                    			forecastTable.add(forecastingValue);
                                			}
                            				
                            			} else{
                            				forecastValueStr = forecastUnits.getForecastValue();
                            				if(ApplicationUtil.isEmptyOrNull(forecastValueStr)){
                            					forecastValueStr="0";
                            					forecastBuffer.append(" , d: "+forecastValueStr+"}");
                                				forecastTable.add(forecastValueStr);
                            				}
                            				if(!ApplicationUtil.isEmptyOrNull(forecastValueStr)){
                                				double forecastingValue=Math.round(Double.parseDouble(forecastValueStr));
                                				forecastBuffer.append(" , d: "+forecastingValue+"}");
                                				forecastTable.add(forecastingValue);
                                			}
                            				
                            			}
                            			
                            			
                            		} else if(tableName.equalsIgnoreCase("ForecastingASP")){
                            			forecastBuffer.append("{ w: '"+forecastPeriod+"', b: "+decimalFormat.format(Double.parseDouble(forecastValue)));
                            			ForecastingASP forecastAsp = (ForecastingASP) baseForecastAspList.get(i);
                            			String forecastValueStr = forecastAsp.getForecastValue();
                            			if(ApplicationUtil.isEmptyOrNull(forecastValueStr)){
                            				forecastValueStr="0.0";
                            			}
                            			forecastBuffer.append(" , c: "+forecastValueStr);
                            			forecastTable.add(forecastValueStr);
                            			if(rolesList != null && rolesList.size()>0 && ( !rolesList.contains(ApplicationConstants.PRODUCT_MANAGER) )){
                            				ForecastingUnits currentForecastAsp = (ForecastingUnits)currentForecastAspList.get(i);
                            				forecastValueStr = currentForecastAsp.getForecastValue();
                            				if(ApplicationUtil.isEmptyOrNull(forecastValueStr)){
                            					forecastValueStr="0.0";
                            				}
                            				forecastBuffer.append(" , d: "+forecastValueStr+"}");
                            				forecastTable.add(forecastValueStr);
                            			} else{
                            				forecastValueStr = forecastAsp.getForecastValue();
                            				if(ApplicationUtil.isEmptyOrNull(forecastValueStr)){
                            					forecastValueStr="0.0";
                            				}
                            				forecastBuffer.append(" , d: "+forecastValueStr+"}");
                            				forecastTable.add(forecastValueStr);
                            			}
                            		} else {
                            			forecastBuffer.append("{ w: '"+forecastPeriod+"', b: "+decimalFormat.format(Double.parseDouble(forecastValue))+"}");
                            		}
                            		forecastTable.add(forecastValue);
                             		jsonTableList.add(forecastTable);
                              		if(i<size-1){
                              			forecastBuffer.append(",");
                              		}
                              		forecastBuffer.append("\n");
                        	   }
                           }
                           forecastBuffer.append("] \n");
                           if(tableName.equalsIgnoreCase("ForecastingUnits")){
                        	   jsonViewMapObj.put("overrideForecastUnitsList",overrideForecastUnitsList);
                           }else if(tableName.equalsIgnoreCase("ForecastingASP")){
                         	   jsonViewMapObj.put("overrideForecastAspList",overrideForecastAspList);
                           }
                           jsonViewMapObj.put("jsonStr",forecastBuffer.toString());
                           jsonViewMapObj.put("jsonTable",jsonTableList);
                   } catch (HibernateException he) {
                           throw he;
                   }
            	 
                  return jsonViewMapObj;
            }
		     });
		     logger.debug("Leaving from getChartJsonFromDatabase");
		     return (Map) object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	}
	

	public Map getChartJsonForAggregated(final String tableName, final String productId, final Integer planningCycleId,final String selectedTypeVariable,final String selectedTypeValue,final Integer userId , final String business,final String whereClauseStr,final List rolesList,final Integer week,final Integer year,final Integer range,final String type){
		
		try{
			logger.debug("Entered into getChartJsonForModel");
            Object object = hibernateTemplate.execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                         throws HibernateException, SQLException {
            	String pattern = "###.##";
        		DecimalFormat decimalFormat = new DecimalFormat(pattern);
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
                List baseForecastAspList = new ArrayList();
                List baseForecastUnitsList = new ArrayList();
                List currentForecastUnitsList = new ArrayList();
                List currentForecastAspList =new ArrayList();
                List actualsWeekList =new ArrayList();
                StringBuffer actualWeeksBuffer = new StringBuffer();
                List actualWeeksList=null;
                Integer overrideButtonFlag=1;
				try {
					actualWeeksList = getSKUForecastWeek(year,week,range,1);
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
                String roleFilterCondition="select distinct d."+productIdColumn+" from Data d ,SkuList sl where (sl.eolWeek is null or ("+week+"<=sl.eolWeek and "+year+"<=sl.eolYear)) and  d."+selectedTypeVariable+" = '"+selectedTypeValue+"' "+whereClauseStr;
                String roleFilterConditionNativeSql="select distinct d."+productIdColumn+" from Data d ,Sku_List sl where (sl.eolWeek is null or ("+week+"<=sl.eolWeek and "+year+"<=sl.eolYear)) and d."+selectedTypeVariable+" = '"+selectedTypeValue+"' "+whereClauseStr;
    			if(rolesList != null && rolesList.size()>0 && rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){
    				roleFilterConditionNativeSql = "select distinct  sl.productId from Data d,Sku_List sl,Sku_User_Mapping um  "
       			 		+ " where (sl.eolWeek is null or ("+week+"<=sl.eolWeek and "+year+"<=sl.eolYear)) and d."+selectedTypeVariable+" = '"+selectedTypeValue+"' and um.userId="+userId+" and d."+productIdColumn+" = sl.productId and um.skuListId=sl.id and um.business=d."+businessColumn+" "+whereClauseStr;
    				roleFilterCondition="select distinct sl.productId from Data d,SkuList sl,SkuUserMapping um  "
           			 		+ " where (sl.eolWeek is null or ("+week+"<=sl.eolWeek and "+year+"<=sl.eolYear)) and d."+selectedTypeVariable+" = '"+selectedTypeValue+"' and um.userId="+userId+" and d."+productIdColumn+" = sl.productId and um.skuListId=sl.id and um.business=d."+businessColumn+" "+whereClauseStr;
    				if(tableName.equalsIgnoreCase("ForecastingUnits") && type!= null && type.equalsIgnoreCase("MODEL")){
    					String sql3="select count(distinct d."+productIdColumn+") from Data d,SkuList sl where (sl.eolWeek is null or ("+week+"<=sl.eolWeek and "+year+"<=sl.eolYear)) and d."+selectedTypeVariable+" = '"+selectedTypeValue+"' "+whereClauseStr;
    					Query q1 = session.createQuery(sql3);
    					Long totalProductCount=(Long) q1.uniqueResult();
    					sql3= "select count(distinct sl.productId) from Data d,SkuList sl,SkuUserMapping um  "
               			 		+ "where (sl.eolWeek is null or ("+week+"<=sl.eolWeek and "+year+"<=sl.eolYear)) and d."+selectedTypeVariable+" = '"+selectedTypeValue+"' and um.userId="+userId+" and d."+productIdColumn+" = sl.productId and um.skuListId=sl.id and um.business=d."+businessColumn+" "+whereClauseStr;
    					q1 = session.createQuery(sql3);
    					Long userProductCount=(Long) q1.uniqueResult();
    					if(totalProductCount.intValue()!=userProductCount.intValue()){
    						overrideButtonFlag=0;
    					}
    				}
    				
    			}else if (rolesList != null && rolesList.size()>0 && rolesList.contains(ApplicationConstants.CATEGORY_MANAGER)){
    				roleFilterConditionNativeSql = "select distinct data."+productIdColumn+" from Data data,Sku_List sl where (sl.eolWeek is null or ("+week+"<=sl.eolWeek and "+year+"<=sl.eolYear)) and data."+selectedTypeVariable+" = '"+selectedTypeValue+"' and data."+categoryColumn+" IN(select catList.categoryName from Category_List as catList where catList.id IN "
        					+ "(select catUser.categoryId from Category_User_Mapping as catUser where catUser.userId="+userId+")) AND data."+businessColumn+" IN (select distinct catUser.business from Category_User_Mapping as catUser where catUser.userId = "+userId+") "+whereClauseStr;
    				roleFilterCondition = "select distinct data."+productIdColumn+" from Data data,SkuList sl where (sl.eolWeek is null or ("+week+"<=sl.eolWeek and "+year+"<=sl.eolYear)) and data."+selectedTypeVariable+" = '"+selectedTypeValue+"' and data."+categoryColumn+" IN(select catList.categoryName from CategoryList as catList where catList.id IN "
    					+ "(select catUser.categoryId from CategoryUserMapping as catUser where catUser.userId="+userId+")) AND data."+businessColumn+" IN (select distinct catUser.business from CategoryUserMapping as catUser where catUser.userId = "+userId+") "+whereClauseStr;
    			}

                if(tableName.equalsIgnoreCase("ForecastingUnits")){
                	sql = "select d."+orderWeek+",sum(d."+unitsColumn+") from Data d where d."+selectedTypeVariable+" = '"+selectedTypeValue+"' and d."+businessColumn+" = '"+business+"' "
                			+" and d."+productIdColumn+" in ("+roleFilterCondition+")  and d."+orderWeek+" in ("+actualWeeksBuffer+") group by d."+orderWeek;
       			 	sql1 = "select t.forecastPeriod, round(sum(t.forecastValue)) from "+tableName+" t where t.productId in ("+roleFilterCondition+") and  t.planningCycleId="+planningCycleId+" and "
       			 				+ " t.business = '"+ business+"'  group by t.forecastPeriod";
       			 	Query q = session.createQuery(sql1);
       			 	List forecastingValuesList =  (List)q.list();
       			 	if(forecastingValuesList != null){
	   			 		int size =forecastingValuesList.size();
	   			 		
	   			 		for(int i=0;i<size;i++){
	              		   Object[] rowList = (Object[])forecastingValuesList.get(i);
	                  		String forecastPeriod="";
	                  		String forecastValue="";
	                  		List forecastTable = new ArrayList();
	                  		if(rowList[0]!=null){
	                  			forecastPeriod = rowList[0].toString();
	                  		}
	                  		if(rowList[1]!=null){
	                  			forecastValue = rowList[1].toString();
	                  		}
	              			ForecastingUnits forecastUnits= new ForecastingUnits();
	              			forecastUnits.setForecastPeriod(forecastPeriod);
	              			forecastUnits.setForecastValue(forecastValue);
	              			baseForecastUnitsList.add(forecastUnits);
	   			 		}
	   			 		jsonViewMapObj.put("baseForecastUnisList", baseForecastUnitsList);
       			 	}
       			 	
	       			 if(rolesList != null && rolesList.size()>0 && ( !rolesList.contains(ApplicationConstants.PRODUCT_MANAGER) )){
	             		sql1 = "select t.forecastPeriod, round(sum(t.OriginalForecast)) from (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast  "
	                			+ "FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from  "
	                			+ "override_units_log  where userId!="+userId +" group by forecastingUnitsId) b on a.forecastingUnitsId= b.forecastingUnitsId join forecasting_units f on f.id=b.forecastingUnitsId and f.planningCycleId="+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.productId in ("+roleFilterConditionNativeSql+") and f.planningCycleId="+planningCycleId+" and  f.business = '"+ business+"')  t where  t.planningCycleId="+planningCycleId+" and "
	           			 				+ " t.business = '"+ business+"'  group by t.forecastPeriod";
	             		SQLQuery query = session.createSQLQuery(sql1);
	             		forecastingValuesList = (List)query.list();
	        			 	if(forecastingValuesList != null){
	 	   			 		int size =forecastingValuesList.size();
	 	   			 		
	 	   			 		for(int i=0;i<size;i++){
	 	              		   Object[] rowList = (Object[])forecastingValuesList.get(i);
	 	                  		String forecastPeriod="";
	 	                  		String forecastValue="";
	 	                  		List forecastTable = new ArrayList();
	 	                  		if(rowList[0]!=null){
	 	                  			forecastPeriod = rowList[0].toString();
	 	                  		}
	 	                  		if(rowList[1]!=null){
	                  				forecastValue = rowList[1].toString();
	                  			} 
	 	              			ForecastingUnits forecastUnits= new ForecastingUnits();
	 	              			forecastUnits.setForecastPeriod(forecastPeriod);
	 	              			forecastUnits.setForecastValue(forecastValue);
	 	              			currentForecastUnitsList.add(forecastUnits);
	 	   			 		}
	 	   			 		jsonViewMapObj.put("currentForecastUnitsList", currentForecastUnitsList);
	        			 	}
	             	}
                	sql1 = "select t.forecastPeriod, cast(round(sum(t.OriginalForecast)) as char(30)) from (SELECT f.*,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast  "
                			+ "FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from  "
                			+ "override_units_log group by forecastingUnitsId) b on a.forecastingUnitsId= b.forecastingUnitsId join forecasting_units f on f.id=b.forecastingUnitsId and f.planningCycleId= "+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.productId in ("+roleFilterConditionNativeSql+") and  f.planningCycleId="+planningCycleId+" and " + " f.business = '"+ business+"' )  t where  t.planningCycleId="+planningCycleId+" and "
           			 				+ " t.business = '"+ business+"'  group by t.forecastPeriod";
                	System.out.println("Units"+sql1);
                	
                	
                }  else if (tableName.equalsIgnoreCase("ForecastingASP")){
                	sql = "select d."+orderWeek+",cast(round(sum(d."+unitsColumn+" * d."+aspColumn+")/sum(d."+unitsColumn+"),2) as string) from Data d where d."+selectedTypeVariable+" = '"+selectedTypeValue+"' and d."+businessColumn+" = '"+business+"' "
                			+" and d."+productIdColumn+" in ("+roleFilterCondition+")   and d."+orderWeek+" in ("+actualWeeksBuffer+") group by d."+orderWeek;
       			 	sql1 = "select fu.forecastPeriod , cast(round(sum(fu.forecastValue * fa.forecastValue)/sum(fu.forecastValue),2) as char(30)) from Forecasting_Units fu, Forecasting_ASP fa "+
							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business and fa.planningCycleId = "+planningCycleId+ " and fu.business='"+business+"' "+
							" and fu.planningCycleId= "+planningCycleId+" and fu.productId in ("+roleFilterConditionNativeSql+") group by fu.forecastPeriod";
       			 	Query q = session.createSQLQuery(sql1);
    			 	List forecastingValuesList =  (List)q.list();
    			 	if(forecastingValuesList != null){
				 		int size =forecastingValuesList.size();
		           	   	
		           	   	for(int i=0;i<size;i++){
		           		   Object[] rowList = (Object[])forecastingValuesList.get(i);
		               		String forecastPeriod="";
		               		String forecastValue="";
		               		List forecastTable = new ArrayList();
		               		if(rowList[0]!=null){
		               			forecastPeriod = rowList[0].toString();
		               		}
		               		if(rowList[1]!=null){
		               			forecastValue = rowList[1].toString();
		               		}
		           			ForecastingASP forecastAsp= new ForecastingASP();
		           			forecastAsp.setForecastPeriod(forecastPeriod);
		           			forecastAsp.setForecastValue(forecastValue);
		           			baseForecastAspList.add(forecastAsp);
		           	   	}
		           	   	jsonViewMapObj.put("baseForecastAspList", baseForecastAspList);
    			 	}
    			 	
    			 	if(rolesList != null && rolesList.size()>0 && ( !rolesList.contains(ApplicationConstants.PRODUCT_MANAGER) )){
	             		sql1 = "select fu.forecastPeriod , round(sum(fu.OriginalForecast * fa.OriginalForecast)/sum(fu.OriginalForecast),2) from (SELECT f.forecastPeriod,f.productId,f.business,f.planningCycleId,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
	       			 			+ " FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from override_units_log where userId!="+userId +" group by forecastingUnitsId) b  "
	       			 			+ "on a.forecastingUnitsId= b.forecastingUnitsId join forecasting_units f on f.id=b.forecastingUnitsId and f.planningCycleId = "+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.business='"+business+"' and f.planningCycleId= "+planningCycleId+" and f.productId in ("+roleFilterConditionNativeSql+")) fu, (SELECT f.forecastPeriod,f.productId,f.business,f.planningCycleId,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
	       			 			+ " FROM forecasting_asp f left join (SELECT a.* FROM override_asp_log a join (select forecastingAspId,max(createdDate) as mdate from override_asp_log where userId!="+userId +" group by forecastingAspId) b  "
	       			 			+ "on a.forecastingAspId= b.forecastingAspId join forecasting_asp f on f.id=b.forecastingAspId and f.planningCycleId="+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingAspId where f.business='"+business+"' and f.planningCycleId= "+planningCycleId+" and f.productId in ("+roleFilterConditionNativeSql+")) fa "+
								"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business and fa.planningCycleId = "+planningCycleId+ " and fu.business='"+business+"' "+
								" and fu.planningCycleId= "+planningCycleId+" group by fu.forecastPeriod";
	             		SQLQuery query = session.createSQLQuery(sql1);
	             		forecastingValuesList = (List)query.list();
	        			 	if(forecastingValuesList != null){
	 	   			 		int size =forecastingValuesList.size();
	 	   			 		
	 	   			 		for(int i=0;i<size;i++){
	 	              		   Object[] rowList = (Object[])forecastingValuesList.get(i);
	 	                  		String forecastPeriod="";
	 	                  		String forecastValue="";
	 	                  		List forecastTable = new ArrayList();
	 	                  		if(rowList[0]!=null){
	 	                  			forecastPeriod = rowList[0].toString();
	 	                  		}
	 	                  		if(rowList[1]!=null){
	                  				forecastValue = rowList[1].toString();
	                  			} 
	 	              			ForecastingUnits forecastUnits= new ForecastingUnits();
	 	              			forecastUnits.setForecastPeriod(forecastPeriod);
	 	              			forecastUnits.setForecastValue(forecastValue);
	 	              			currentForecastAspList.add(forecastUnits);
	 	   			 		}
	 	   			 		jsonViewMapObj.put("currentForecastAspList", currentForecastAspList);
	        			 	}
	             	}
	                	
                	sql1 = "select fu.forecastPeriod , cast(round(sum(fu.OriginalForecast * fa.OriginalForecast)/sum(fu.OriginalForecast),2) as char(30)) from (SELECT f.forecastPeriod,f.productId,f.business,f.planningCycleId,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
       			 			+ " FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from override_units_log group by forecastingUnitsId) b  "
       			 			+ "on a.forecastingUnitsId= b.forecastingUnitsId join forecasting_units f on f.id=b.forecastingUnitsId and f.planningCycleId ="+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.business='"+business+"' and f.planningCycleId= "+planningCycleId+" and f.productId in ("+roleFilterConditionNativeSql+")) fu, (SELECT f.forecastPeriod,f.productId,f.business,f.planningCycleId,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
       			 			+ " FROM forecasting_asp f left join (SELECT a.* FROM override_asp_log a join (select forecastingAspId,max(createdDate) as mdate from override_asp_log group by forecastingAspId) b  "
       			 			+ "on a.forecastingAspId= b.forecastingAspId join forecasting_asp f on f.id=b.forecastingAspId and f.planningCycleId="+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingAspId where f.business='"+business+"' and f.planningCycleId= "+planningCycleId+" and f.productId in ("+roleFilterConditionNativeSql+")) fa "+
							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business and fa.planningCycleId = "+planningCycleId+ " and fu.business='"+business+"' and fu.planningCycleId= "+planningCycleId+" group by fu.forecastPeriod";
                	System.out.println("ASP"+sql1);
                	
                } else if (tableName.equalsIgnoreCase("ForecastingRevenue")){
                	sql = "select d."+orderWeek+",cast(round(sum(d."+unitsColumn+") * (sum(d."+unitsColumn+" * d."+aspColumn+")/sum(d."+unitsColumn+")),2) as string) from Data d where d."+selectedTypeVariable+" = '"+selectedTypeValue+"' and d."+businessColumn+" = '"+business+"' "
                			+" and d."+productIdColumn+" in ("+roleFilterCondition+")   and d."+orderWeek+" in ("+actualWeeksBuffer+") group by d."+orderWeek;
                	
                	sql1 = "select fu.forecastPeriod , sum(fu.OriginalForecast * fa.OriginalForecast) from (SELECT f.forecastPeriod,f.productId,f.business,f.planningCycleId,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
       			 			+ " FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from override_units_log group by forecastingUnitsId) b  "
       			 			+ "on a.forecastingUnitsId= b.forecastingUnitsId join forecasting_units f on f.id=b.forecastingUnitsId and f.planningCycleId="+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.planningCycleId = "+planningCycleId+"  and f.productId in ("+roleFilterConditionNativeSql+") and f.business = '"+business+"') fu, (SELECT f.forecastPeriod,f.productId,f.business,f.planningCycleId,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
       			 			+ " FROM forecasting_asp f left join (SELECT a.* FROM override_asp_log a join (select forecastingAspId,max(createdDate) as mdate from override_asp_log group by forecastingAspId) b  "
       			 			+ "on a.forecastingAspId= b.forecastingAspId join forecasting_asp f on f.id=b.forecastingAspId and f.planningCycleId="+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingAspId where f.planningCycleId = "+planningCycleId+"  and f.productId in ("+roleFilterConditionNativeSql+") and f.business = '"+business+"') fa "+
							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business and fa.planningCycleId = "+planningCycleId+ " and fu.business='"+business+"' "+
							" and fu.planningCycleId= "+planningCycleId+" group by fu.forecastPeriod";
                	System.out.println("check Revenue query"+sql1);
                	

                	/*sql1 ="select fu.forecastPeriod , sum(fu.forecastValue) * (sum(fu.forecastValue * fa.forecastValue)/sum(fu.forecastValue)) from ForecastingUnits fu, ForecastingASP fa "+
							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business and fa.planningCycleId = "+planningCycleId+ " and fu.business='"+business+"' "+
							" and fu.planningCycleId= "+planningCycleId+" and fu.productId in ("+roleFilterCondition+") group by fu.forecastPeriod";*/
                	
                } else if (tableName.equalsIgnoreCase("ForecastingESC")){
                	sql = "select d."+orderWeek+",cast(round(sum(d."+escColumn+" * d."+unitsColumn+")/sum(d."+unitsColumn+"),2) as string) from Data d where d."+selectedTypeVariable+" = '"+selectedTypeValue+"' and d."+businessColumn+" = '"+business+"' "
                			+" and d."+productIdColumn+" in ("+roleFilterCondition+")   and d."+orderWeek+" in ("+actualWeeksBuffer+") group by d."+orderWeek;
                	
                	sql1 = "select fu.forecastPeriod , sum(fu.OriginalForecast * fa.forecastValue) / sum(fu.OriginalForecast) from (SELECT f.forecastPeriod,f.productId,f.business,f.planningCycleId,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
       			 			+ " FROM forecasting_units f left join (SELECT a.* FROM override_units_log a join (select forecastingUnitsId,max(createdDate) as mdate from override_units_log group by forecastingUnitsId) b  "
       			 			+ "on a.forecastingUnitsId= b.forecastingUnitsId join forecasting_units f on f.id=b.forecastingUnitsId and f.planningCycleId="+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingUnitsId where f.business='"+business+"' and f.planningCycleId= "+planningCycleId+" and f.productId in ("+roleFilterConditionNativeSql+")) fu, forecasting_esc fa "+
							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business and fa.planningCycleId = "+planningCycleId+ " and fu.business='"+business+"' and fu.planningCycleId= "+planningCycleId+" group by fu.forecastPeriod";
                	System.out.println("check ESC query"+sql1);
                	
                	/*sql1 ="select fu.forecastPeriod , sum(fe.forecastValue*fu.forecastValue)/sum(fu.forecastValue) from ForecastingUnits fu, ForecastingESC fe "+
							"where fu.forecastPeriod =fe.forecastPeriod and fu.productId =fe.productId and fu.business=fe.business and fe.planningCycleId = "+planningCycleId+ " and fu.business='"+business+"' "+
							" and fu.planningCycleId= "+planningCycleId+" and fu.productId in ("+roleFilterCondition+") group by fu.forecastPeriod";*/
                	
                	
                } else {
                	sql = "select d."+orderWeek+",cast(round((((sum(d."+aspColumn+") - sum( d."+escColumn+"))/sum(d."+aspColumn+"))*100),2) as string) from Data d where d."+selectedTypeVariable+" = '"+selectedTypeValue+"' and d."+businessColumn+" = '"+business+"' "
                			+" and d."+productIdColumn+" in ("+roleFilterCondition+")   and d."+orderWeek+" in ("+actualWeeksBuffer+") group by d."+orderWeek;
                	
                	sql1 = "select fu.forecastPeriod ,((sum(fa.OriginalForecast)-sum(fu.forecastValue))/sum(fa.OriginalForecast))*100 from forecasting_esc fu, (SELECT f.forecastPeriod,f.productId,f.business,f.planningCycleId,case when b.overrideValue is null then f.forecastValue else b.overrideValue end as OriginalForecast "
       			 			+ " FROM forecasting_asp f left join (SELECT a.* FROM override_asp_log a join (select forecastingAspId,max(createdDate) as mdate from override_asp_log group by forecastingAspId) b  "
       			 			+ "on a.forecastingAspId= b.forecastingAspId join forecasting_asp f on f.id=b.forecastingAspId and f.planningCycleId="+planningCycleId+" and a.createdDate=b.mdate) b on f.id = b.forecastingAspId where f.business='"+business+"' and f.planningCycleId= "+planningCycleId+" and f.productId in ("+roleFilterConditionNativeSql+")) fa "+
							"where fu.forecastPeriod =fa.forecastPeriod and fu.productId =fa.productId and fu.business=fa.business and fa.planningCycleId = "+planningCycleId+ " and fu.business='"+business+"' and fu.planningCycleId= "+planningCycleId+" group by fu.forecastPeriod";
                	System.out.println("check pm% query"+sql1);
                	
                	/*sql1 ="select fa.forecastPeriod , (sum(fa.forecastValue)-sum(fe.forecastValue))/sum(fa.forecastValue) from ForecastingASP fa, ForecastingESC fe "+
							"where fa.forecastPeriod =fe.forecastPeriod and fa.productId =fe.productId and fa.business=fe.business and fe.planningCycleId = "+planningCycleId+ " and fa.business='"+business+"' "+
							" and fa.planningCycleId= "+planningCycleId+" and fa.productId in ("+roleFilterCondition+") group by fa.forecastPeriod";*/
                }
                
            	try {
            		System.out.println(sql);
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
                     		/*forecastBuffer.append("{ w: '"+actualPeriod+"', a: "+actualValue+"}");
                     		actualTable.add(actualPeriod);
                     		actualTable.add(actualValue);
                     		actualTable.add("---");
                     		if(tableName.equalsIgnoreCase("ForecastingASP") || tableName.equalsIgnoreCase("ForecastingUnits")){
                     			actualTable.add("---");
                     			actualTable.add("---");
                     		}
                     		jsonTableList.add(actualTable);
                     		if(i<size-1){
                      			forecastBuffer.append(",");
                      		}
                      		forecastBuffer.append("\n");*/
                     	}
                     }
                     
                     try {
                      	actualsWeekList= getSKUForecastWeek(year,week,range,1);
                      	int size =actualsWeekList.size();
                      	for(int i=size-1;i>=0;i--){
                      		List actualTable= new ArrayList();
                      		String actualPeriod=(String) actualsWeekList.get(i);
                      		String actualValue="";
                      		if(actualsListMap.containsKey(actualPeriod)){
                      			
                      			actualValue= (String) actualsListMap.get(actualPeriod);
                      			if(ApplicationUtil.isEmptyOrNull(actualValue)){
                      				actualValue="0";
                      			}
                      		} else{
                      			actualValue="0";
                      		}
                      		forecastBuffer.append("{ w: '"+actualPeriod+"', a: "+actualValue+"}");
                      		actualTable.add(actualPeriod);
                      		actualTable.add(actualValue);
                      		actualTable.add("---");
                      		if(tableName.equalsIgnoreCase("ForecastingASP") || tableName.equalsIgnoreCase("ForecastingUnits")){
                      			actualTable.add("---");
                      			actualTable.add("---");
                      		}
                      		jsonTableList.add(actualTable);
                      		if(i>0){
                       			forecastBuffer.append(",");
                       		}
                       		forecastBuffer.append("\n");
                      		
                      	}
                      	
      				} catch (ApplicationException e) {
      					// TODO Auto-generated catch block
      					e.printStackTrace();
      				}
            		
                   /* q = session.createQuery(sql1);
                           List ForecastingValuesList =  (List)q.list();*/
                    		 /*if(!tableName.equalsIgnoreCase("ForecastingUnits") && !tableName.equalsIgnoreCase("ForecastingASP")){
                    			q = session.createQuery(sql1);
                    			ForecastingValuesList =  (List)q.list();
                    		 } else{
                    			//ForecastingValuesList = criteria.list();
                    			SQLQuery query = session.createSQLQuery(sql1);
                    			ForecastingValuesList =  (List)query.list();
                    		 }*/
                     System.out.println(sql1);
                           SQLQuery query = session.createSQLQuery(sql1);
                           List ForecastingValuesList =  (List)query.list();
                           if(ForecastingValuesList != null){
                        	   forecastBuffer.append(", \n");
                        	   int size =ForecastingValuesList.size();
                        	   List overrideForecastUnitsList = new ArrayList();
                        	   List overrideForecastAspList = new ArrayList();
                        	   for(int i=0;i<size;i++){
                        		   Object[] rowList = (Object[])ForecastingValuesList.get(i);
                            		String forecastPeriod="";
                            		String forecastValue="";
                            		List forecastTable = new ArrayList();
                            		if(rowList[0]!=null){
                            			forecastPeriod = rowList[0].toString();
                            		}
                            		if(rowList[1]!=null){
                            			forecastValue = rowList[1].toString();
                            		}
                            		forecastTable.add(forecastPeriod);
                            		forecastTable.add("---");
                            		if(ApplicationUtil.isEmptyOrNull(forecastValue)){
                            			forecastValue="0";
                            		}
                            		if(tableName.equalsIgnoreCase("ForecastingUnits")){
                            			forecastBuffer.append("{ w: '"+forecastPeriod+"', b: "+forecastValue);
                            			ForecastingUnits forecastUnits = (ForecastingUnits) baseForecastUnitsList.get(i);
                            			String forecastValueStr = forecastUnits.getForecastValue();
                            			if(ApplicationUtil.isEmptyOrNull(forecastValueStr)){
                            				forecastValueStr="0";
                            			}
                            			forecastBuffer.append(" , c: "+forecastValueStr);
                            			forecastTable.add(forecastValueStr);
                            			if(rolesList != null && rolesList.size()>0 && ( !rolesList.contains(ApplicationConstants.PRODUCT_MANAGER) )){
                            				ForecastingUnits currentForecastUnits = (ForecastingUnits)currentForecastUnitsList.get(i);
                            				forecastValueStr = currentForecastUnits.getForecastValue();
                                			if(ApplicationUtil.isEmptyOrNull(forecastValueStr)){
                                				forecastValueStr="0";
                                			}
                            				forecastBuffer.append(" , d: "+forecastValueStr+"}");
                            				forecastTable.add(forecastValueStr);
                            			} else{
                            				forecastValueStr = forecastUnits.getForecastValue();
                                			if(ApplicationUtil.isEmptyOrNull(forecastValueStr)){
                                				forecastValueStr="0";
                                			}
                            				forecastBuffer.append(" , d: "+forecastValueStr+"}");
                            				forecastTable.add(forecastValueStr);
                            			}
                            		} else if(tableName.equalsIgnoreCase("ForecastingASP")){
                            			forecastBuffer.append("{ w: '"+forecastPeriod+"', b: "+forecastValue);
                            			ForecastingASP forecastAsp = (ForecastingASP) baseForecastAspList.get(i);
                            			String forecastValueStr = forecastAsp.getForecastValue();
                            			if(ApplicationUtil.isEmptyOrNull(forecastValueStr)){
                            				forecastValueStr="0";
                            			}
                            			forecastBuffer.append(" , c: "+forecastValueStr);
                            			forecastTable.add(forecastValueStr);
                            			if(rolesList != null && rolesList.size()>0 && ( !rolesList.contains(ApplicationConstants.PRODUCT_MANAGER) )){
                            				ForecastingUnits currentForecastAsp = (ForecastingUnits)currentForecastAspList.get(i);
                            				forecastValueStr = currentForecastAsp.getForecastValue();
                                			if(ApplicationUtil.isEmptyOrNull(forecastValueStr)){
                                				forecastValueStr="0";
                                			}
                            				forecastBuffer.append(" , d: "+forecastValueStr+"}");
                            				forecastTable.add(forecastValueStr);
                            			} else{
                            				forecastValueStr = forecastAsp.getForecastValue();
                                			if(ApplicationUtil.isEmptyOrNull(forecastValueStr)){
                                				forecastValueStr="0";
                                			}
                            				forecastBuffer.append(" , d: "+forecastValueStr+"}");
                            				forecastTable.add(forecastValueStr);
                            			}
                            		} else {
                             			forecastBuffer.append("{ w: '"+forecastPeriod+"', b: "+decimalFormat.format(Double.parseDouble(forecastValue))+"}");
                            		}
                            		forecastTable.add(forecastValue);
                             		jsonTableList.add(forecastTable);
                              		if(i<size-1){
                              			forecastBuffer.append(",");
                              		}
                              		forecastBuffer.append("\n");
                              		if(tableName.equalsIgnoreCase("ForecastingUnits")){
                            			ForecastingUnits forecastUnits= new ForecastingUnits();
                            			//int id = Integer.parseInt(rowList[2].toString());
                            			//forecastUnits.setId(id);
                            			forecastUnits.setForecastPeriod(forecastPeriod);
                            			forecastUnits.setForecastValue(forecastValue);
                            			overrideForecastUnitsList.add(forecastUnits);
                            		}else if(tableName.equalsIgnoreCase("ForecastingASP")){
                            			ForecastingASP forecastingASP = new ForecastingASP();
                            			/*int id = Integer.parseInt(rowList[2].toString());
                            			forecastingASP.setId(id);*/
                            			forecastingASP.setForecastPeriod(forecastPeriod);
                            			forecastingASP.setForecastValue(forecastValue);
                            			overrideForecastAspList.add(forecastingASP);
                             		}
                        	   }
                        	   if(tableName.equalsIgnoreCase("ForecastingUnits")){
                            	   jsonViewMapObj.put("overrideForecastUnitsList",overrideForecastUnitsList);
                               }else if(tableName.equalsIgnoreCase("ForecastingASP")){
                             	   jsonViewMapObj.put("overrideForecastAspList",overrideForecastAspList);
                               }
                           }
                           forecastBuffer.append("] \n");
                           
                           jsonViewMapObj.put("jsonStr",forecastBuffer.toString());
                           jsonViewMapObj.put("jsonTable",jsonTableList);
                           jsonViewMapObj.put("overrideButtonFlag", overrideButtonFlag);
                   } catch (HibernateException he) {
                           throw he;
                   }
            	 
                  return jsonViewMapObj;
            }
		     });
		     logger.debug("Leaving from getChartJsonForModel");
		     return (Map) object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	}
	
	/**
	 * @param SKUId
	 * @return
	 * @throws ApplicationException
	 * Returns the Category to which a SKU belongs to
	 */
	public final String getSKUCategory(final String SKUId) throws ApplicationException{
		Object object = null;
		logger.debug("Entered into getSKUCategory in planningDAO");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
						String categoryColumn = PropertiesUtil.getProperty("category");
						String productNumberColumn = PropertiesUtil.getProperty("productNumber"); 
						Criteria categoryName = session.createCriteria(Data.class);
						categoryName.setProjection(Projections.distinct(Projections.property(categoryColumn)));
						categoryName.add(Restrictions.eq(productNumberColumn,SKUId));
						String category= (String) categoryName.list().get(0);
						return category;
					} catch (HibernateException he) {
logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
					}
					return true;
				}
				
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
		}
		logger.debug("Leaving from getSKUCategory method in planningDAO");
		return (String)object;
	}
	
	/**
	 * @param SKUId
	 * @return
	 * @throws ApplicationException
	 * Returns the Category to which a SKU belongs to
	 */
	public final String getSKUIdFromForecastIdUnits(final int forecastUnitsId) throws ApplicationException{
		Object object = null;
		logger.debug("Entered into getSKUIdFromForecastIdUnits in planningDAO");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
						Criteria SKUIdCriteria = session.createCriteria(ForecastingUnits.class);
						SKUIdCriteria.setProjection(Projections.distinct(Projections.property("productId")));
						SKUIdCriteria.add(Restrictions.eq("id",forecastUnitsId));
						String SKUIdValue= (String) SKUIdCriteria.list().get(0);
						return SKUIdCriteria.list().get(0);
					} catch (HibernateException he) {
logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
					}
					return true;
				}
				
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
		}
		logger.debug("Leaving from getSKUIdFromForecastIdUnits method in planningDAO");
		return (String) object;
	}
	/**
	 * 
	 * @param forecastUnitsId
	 * @return
	 * @throws ApplicationException
	 */
	public final String getSKUIdFromForecastIdASP(final int forecastUnitsId) throws ApplicationException{
		Object object = null;
		logger.debug("Entered into getSKUIdFromForecastIdUnits in planningDAO");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
						Criteria SKUIdCriteria = session.createCriteria(ForecastingASP.class);
						SKUIdCriteria.setProjection(Projections.distinct(Projections.property("productId")));
						SKUIdCriteria.add(Restrictions.eq("id",forecastUnitsId));
						String SKUIdValue= (String) SKUIdCriteria.list().get(0);
						return SKUIdCriteria.list().get(0);
					} catch (HibernateException he) {
logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
					}
					return true;
				}
				
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
		}
		logger.debug("Leaving from getSKUIdFromForecastIdASP method in planningDAO");
		return (String) object;
	}
	/**
	 * 
	 * @param year
	 * @param week
	 * @param range
	 * @return
	 * @throws ApplicationException
	 */
	public final List<String> getSKUForecastWeek(final int year,final int week,int range,int forecastType) throws ApplicationException{
		logger.debug("Entered into getSKUForecastWeek in planningDAO");
		int baseYear = 0;
		int baseWeek = 0;
		List <String> weeksList = new ArrayList<String>();
		try{
			int i = 1;		
					if(forecastType == 0){
							i=0;
							range=range-1;
						}
						for (; i <= range ; i++)
						{
							
							if(year % 4 == 0 || year % 400 == 0 )
							{
								baseWeek = 53;
							}
							else
							{
								baseWeek = 52;
							}
							if(week-i <= 0)
							{
								baseYear = year-1;
								baseWeek =baseWeek - (-1 * ( week - i ) );
							}
							else
							{
								baseYear = year;
								baseWeek = week-i;
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
		logger.debug("Leaving from getSKUForecastWeek method in planningDAO");
		return weeksList;
	}
	
	/**
	 * 
	 * @param category
	 * @param year
	 * @param week
	 * @return
	 * @throws ApplicationException
	 * Returns Seasonality Index from the Target Data Table for corresponding Category
	 */
	public final LinkedHashMap<String, LinkedHashMap> getSKUSeasonalityIndex(final String category,final int year,final int week) throws ApplicationException{
		Object object = null;
		
		logger.debug("Entered into getSKUSeasonalityIndex in planningDAO");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
						LinkedHashMap<String, LinkedHashMap> SIMap = new LinkedHashMap<String, LinkedHashMap>();
						LinkedHashMap<String,String> SIValuesMap = new LinkedHashMap<String, String>();
						Criteria categoryName = session.createCriteria(TargetData.class);
						ProjectionList seasonalityIndexProjection= Projections.projectionList();
						seasonalityIndexProjection.add(Projections.property("week"));
						seasonalityIndexProjection.add(Projections.property("seasonalityIndex"));
						seasonalityIndexProjection.add(Projections.groupProperty("biz"));
						seasonalityIndexProjection.add(Projections.groupProperty("week"));
						categoryName.add(Restrictions.eq("metric","Units"));
						categoryName.add(Restrictions.eq("gbu",category));//Matches with the category in Target Data table
						categoryName.add(Restrictions.ge("week",year+"-W"+week));//Check whether the planning cycle week is greater than equal to Forecast Week
						categoryName.setProjection(seasonalityIndexProjection);
						List <Object[]> seasonalityIndex = categoryName.list();
						for (Object[] seasonalityValues :seasonalityIndex)
						{
							if(seasonalityValues[2].toString().toLowerCase().contains("Traffic".toLowerCase()))
							{
								if(seasonalityValues[1] == null)
								{
									SIValuesMap.put(seasonalityValues[0].toString(), "1");
								}
								else
								{
									SIValuesMap.put(seasonalityValues[0].toString(), seasonalityValues[1].toString());
								}
								
								SIMap.put(seasonalityValues[2].toString(), SIValuesMap);
							}
							else if(seasonalityValues[2].toString().toLowerCase().contains("SMB".toLowerCase()))
							{
								if(seasonalityValues[1] == null)
								{
									SIValuesMap.put(seasonalityValues[0].toString(),"1");
								}
								else
								{
									SIValuesMap.put(seasonalityValues[0].toString(), seasonalityValues[1].toString());
								}
								
								SIMap.put(seasonalityValues[2].toString(), SIValuesMap);
							}
							
						}
						return SIMap;
					} catch (HibernateException he) {
logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
					}
					return true;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
		}
		logger.debug("Leaving from getSKUSeasonalityIndex method in planningDAO");
		return (LinkedHashMap<String, LinkedHashMap>) object;
	}
	
	/**
	 * @param SKUname
	 * @param totalForecastWeeks
	 * @param seasonalityIndex
	 * @param weeks
	 * @param totalWeeks
	 * @throws ApplicationException
	 * Calculates forecast values for SKU - Units
	 */
	public void baseForecast(final String SKUname,final int totalForecastWeeks,final LinkedHashMap<String, LinkedHashMap> SKUTargetSIMap,final List weeks,final int totalWeeks,final int year,final int week,final int planningCycleId,final String businessValue) throws ApplicationException{
		logger.info("Getting inside baseForecast planningDAO method");
		Object object = null;
		object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
					String orderWeekColumn = PropertiesUtil.getProperty("orderWeek");
					String productNumberColumn = PropertiesUtil.getProperty("productNumber");
					String businessTypeColumn = PropertiesUtil.getProperty("businessType");
					String quantityColumn = PropertiesUtil.getProperty("quantity");

						/*Criteria actual_data= session.createCriteria(Data.class);
						ProjectionList proj= Projections.projectionList();
						proj.add(Projections.property("planningCycleId"));
						proj.add(Projections.property(orderWeekColumn));
						proj.add(Projections.property(productNumberColumn));
						proj.add(Projections.property(businessTypeColumn));
						proj.add(Projections.property(quantityColumn));
						proj.add(Projections.groupProperty("C1"));
						proj.add(Projections.groupProperty("C9"));
						proj.add(Projections.groupProperty("C28"));
						proj.add(Projections.sum("C15"));
						actual_data.add(Restrictions.eq("planningCycleId",planningCycleId));
						actual_data.add(Restrictions.eq(productNumberColumn,SKUname));
						actual_data.setProjection(proj);
						Disjunction actual_data_condition = Restrictions.disjunction();
						actual_data_condition.add(Restrictions.in(orderWeekColumn, weeks));
						actual_data.add(actual_data_condition);
						List<Object []> data_resultset = actual_data.list();
						LinkedHashMap<String, List<Object[]>> forecastSKU = new LinkedHashMap<String, List<Object[]>>();*/
						
						Criteria outlierData= session.createCriteria(Data2.class);
						ProjectionList outlierDataProjection= Projections.projectionList();
						outlierDataProjection.add(Projections.property("planningCycleId"));
						outlierDataProjection.add(Projections.groupProperty(orderWeekColumn));
						outlierDataProjection.add(Projections.groupProperty(productNumberColumn));
						outlierDataProjection.add(Projections.groupProperty(businessTypeColumn));
						outlierDataProjection.add(Projections.sum(quantityColumn));
						outlierData.add(Restrictions.eq(businessTypeColumn,businessValue));
						outlierData.add(Restrictions.eq(productNumberColumn,SKUname));
						outlierData.setProjection(outlierDataProjection);
						Disjunction outlierDataRestriction = Restrictions.disjunction();
						outlierDataRestriction.add(Restrictions.in(orderWeekColumn, weeks));
						outlierData.add(outlierDataRestriction);
						List<Object []> outlierDataResultSet = outlierData.list();
						LinkedHashMap<String, List<Object[]>> outlierDataMap = new LinkedHashMap<String, List<Object[]>>();
						for (Object week : weeks)
							outlierDataMap.put(week.toString()+"_"+SKUname.toString(),null);
						if(!outlierDataResultSet.isEmpty())
						{
						float forecastValue=0;
						List<Object[]> mapForecastValues= new ArrayList<Object []>();
						
						String business = null;
						String productID=null;
						boolean keyExists = false;
						boolean businessFlag= true;

							/*for (Object[] result : data_resultset) 
							{
									List<Object[]> mapValues= new ArrayList<Object []>();
									Object[] value = new Object[6];
									value[0]=planningCycleId;
									value[1]=1;//ModelID
									value[2]=result[2];
									value[3]=result[3];
									value[4]=result[1];
									value[5]=result[4];
									business=result[3].toString();
									productID=result[2].toString();
									mapValues.add(value);
									forecastSKU.put(result[1].toString()+"_"+result[2].toString(), mapValues);
									forecastValue += Integer.parseInt(value[5].toString());
								//logger.info("Planning cycle:"+result[0].toString()+" F-WEEK:"+result[1].toString()+" P-ID:"+result[2].toString()+" Busi:"+result[3].toString()+" Value:"+result[4].toString());
								if(result[3].toString().toLowerCase().contains("Deal Business".toLowerCase()))
								{
									businessFlag=false;
								}
							}*/
							
							forecastValue=0;
							business = null;
							productID=null;
							keyExists = false;
							businessFlag= true;
								for (Object[] outlierResult : outlierDataResultSet) 
								{
									List<Object[]> mapValues= new ArrayList<Object []>();
									keyExists=outlierDataMap.containsKey(outlierResult[1].toString()+"_"+outlierResult[2].toString());
									
									if(keyExists && outlierDataMap.get(outlierResult[1].toString()+"_"+outlierResult[2].toString())!= null)
									{
										Object[] value = new Object[6];
										value[0]=planningCycleId;
										Object[] temp=outlierDataMap.get(outlierResult[1].toString()+"_"+outlierResult[2].toString()).get(0);
										Integer tmpValue=Integer.parseInt(temp[1].toString()) + Integer.parseInt(outlierResult[4].toString());
										value[1]=1;
										value[2]=outlierResult[2];
										value[3]=outlierResult[3];
										value[4]=outlierResult[1];
										value[5]=(Object)tmpValue;
										business=outlierResult[3].toString();
										productID=outlierResult[2].toString();
										mapValues.add(value);
										outlierDataMap.put(outlierResult[1].toString()+"_"+outlierResult[2].toString(),mapValues);
									}
									else
									{
										Object[] value = new Object[6];
										value[0]=planningCycleId;
										value[1]=1;//ModelID
										value[2]=outlierResult[2];
										value[3]=outlierResult[3];
										value[4]=outlierResult[1];
										value[5]=outlierResult[4];
										business=outlierResult[3].toString();
										productID=outlierResult[2].toString();
										mapValues.add(value);
										outlierDataMap.put(outlierResult[1].toString()+"_"+outlierResult[2].toString(), mapValues);
										forecastValue += Integer.parseInt(value[5].toString());
									}
									//logger.info("Outlier:Planning cycle:"+outlierResult[0].toString()+" F-WEEK:"+outlierResult[1].toString()+" P-ID:"+outlierResult[2].toString()+" Busi:"+outlierResult[3].toString()+" Value:"+outlierResult[4].toString());
									if(outlierResult[3].toString().toLowerCase().contains("Deal Business".toLowerCase()))
									{
										businessFlag=false;
									}
								}
							float tempFvalue=0.0f;
							int mapcount=0;
							int j=0;
							int keyiteratecount=0;
							float seasonalityIndexConstant = 0.0f;
							int cycleYear=year;
							String cycleWeek = null;								
							if(businessFlag)
							{	
								for(int i=0;i<totalForecastWeeks;i++)
								{
									if( ( week + i) - (totalWeeks) > 0 )
									{
										
										if(( week + i) - (totalWeeks) <10)
										{
											if( ( (week + i) - (totalWeeks) ) == 1)
											{
												cycleYear +=1;
											}
											cycleWeek = "0" + ( ( week + i) - (totalWeeks) );
										}
										else
										{
											cycleWeek = ((week + i) - (totalWeeks))+"";
										}
									}
									else
									{
										cycleWeek = (week + i)+"";
									}
									//This condition checks whether the forecasting week is already there in the Actual Data
									/*if(forecastSKU.containsKey(cycleYear+"-W"+cycleWeek+"_"+productID))
									{
										Object[] temp = forecastSKU.get(cycleYear+"-W"+cycleWeek+"_"+productID).get(0);
							            for(Object[] seasonalityIndexValue: seasonalityIndex)
							            {
							            	if(seasonalityIndexValue[0].equals(cycleYear+"-W"+cycleWeek))
							            	{
							            		seasonalityIndexConstant= (Float.valueOf(seasonalityIndexValue[1].toString()));
							            	}
							            }
										tempFvalue =(Integer.parseInt(temp[5].toString())*seasonalityIndexConstant);
							            j++;
							            List<Object[]> mapForecastValues2= new ArrayList<Object []>();
										Object[] addforecastValues = new Object[6];
										addforecastValues[0]=planningCycleId;
										addforecastValues[1]=1;
										addforecastValues[2]=productID;
										addforecastValues[3]=business;
										addforecastValues[4]=cycleYear+"-W"+cycleWeek;
										addforecastValues[5]=tempFvalue;
										mapForecastValues2.add(addforecastValues);
										forecastSKU.put(cycleYear+"-W"+cycleWeek+"_"+productID, mapForecastValues2);
										tempFvalue=0;
										seasonalityIndexConstant=0;
									}
									else
									{*/
										for (String keyName: outlierDataMap.keySet())
										{	
												if(keyiteratecount>=mapcount)
												{
														String key = keyName.toString();
														if(outlierDataMap.get(key) != null){
												            Object[] temp = outlierDataMap.get(key).get(0);
												            tempFvalue +=Float.valueOf(temp[5].toString());
														}else{
												            tempFvalue +=0.0;
														}
											            keyiteratecount++;	
												}
												else
												{
													keyiteratecount++;
													continue;
												}
										}

										if(SKUTargetSIMap!=null)
										{
											LinkedHashMap<String , String> siMapValues = SKUTargetSIMap.get(business);
											if(siMapValues != null)
											{
												String siValue = siMapValues.get(cycleYear+"-W"+cycleWeek);
								            	if(siValue != null)
								            	{
								            		seasonalityIndexConstant= (Float.valueOf(siValue));
								            	}else{
								            		seasonalityIndexConstant=1;
								            	}
											}
										}
										else
										{
											seasonalityIndexConstant = 1;
										}
										
										
										 if(seasonalityIndexConstant <= 0 )
                                             seasonalityIndexConstant = 1;
										List<Object[]> mapForecastValues2= new ArrayList<Object []>();
										Object[] addforecastValues = new Object[6];
										addforecastValues[0]=planningCycleId;
										addforecastValues[1]=1;
										addforecastValues[2]=productID;
										addforecastValues[3]=business;
										addforecastValues[4]=cycleYear+"-W"+cycleWeek;
										/*if(seasonalityIndexConstant <= 0 )
											seasonalityIndexConstant = 1;*/
										addforecastValues[5]=((tempFvalue/6)*seasonalityIndexConstant);
										mapForecastValues2.add(addforecastValues);
										//logger.info("WEEK:"+(week+i)+"SUM:"+tempFvalue+"Average:"+(tempFvalue/6)+"SI:"+seasonalityIndexConstant);
										
										outlierDataMap.put(cycleYear+"-W"+cycleWeek+"_"+productID, mapForecastValues2);
										session.beginTransaction();
							            ForecastingUnits obj= new ForecastingUnits();
							            
							            obj.setPlanningCycleId(planningCycleId);
							            obj.setModelId("1");
							            obj.setProductId(productID);
							            obj.setBusiness(business);
							            obj.setForecastPeriod(cycleYear+"-W"+cycleWeek);
							            obj.setForecastValue(( (tempFvalue/6)*seasonalityIndexConstant)+"");
							            session.save(obj);
							            session.getTransaction().commit();
							            //logger.info("FORECAST MAP: P-C:"+planningCycleId+"Model:"+"1"+"PDT:"+productID+"Business:"+business+"F-Week:"+cycleYear+"-W"+cycleWeek+"F-Val:"+( (tempFvalue/6)*seasonalityIndexConstant)+"");
										mapcount++;
										keyiteratecount=0;
										tempFvalue=0;
									}
							
								//}
								
								/*int i=0;
								if(!forecastSKU.isEmpty())
								{
									for (String name: forecastSKU.keySet())
									{
										if(i<6-j)
										{
											i++;
											continue;
										}
										else
										{
											try
											{
									            String key =name.toString();
									            Object[] temp=forecastSKU.get(key).get(0);
									            logger.info("FORECAST MAP: P-C:"+temp[0]+"Model:"+temp[1]+"PDT:"+temp[2]+"Business:"+temp[3]+"F-Week:"+temp[4]+"F-Val:"+temp[5]);
									            session.beginTransaction();
									            ForecastUnits obj= new ForecastUnits();
									            obj.setPlanningCycleId(Integer.parseInt(temp[0].toString()));
									            obj.setModelId(temp[1].toString());
									            obj.setProductId(temp[2].toString());
									            obj.setBusiness(temp[3].toString());
									            obj.setForecastPeriod(temp[4].toString());
									            obj.setForecastValue(temp[5].toString());
									            session.save(obj);
									            session.getTransaction().commit();
											}catch (HibernateException he){
						logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
												session.getTransaction().rollback();
											}
											finally {
									            session.close();
											}
										}
									}
								}*/
								/*int i=0;
								
									for (String name: outlierDataMap.keySet())
									{
										if(i<6)
										{
											i++;
											continue;
										}
										else
										{
											try
											{
									            String key =name.toString();
									            Object[] temp=outlierDataMap.get(key).get(0);
									            if(temp[1]!=null&&temp[2]!=null&&temp[3]!=null&&temp[4]!=null)
												{
										            logger.info("outlier MAP: P-C:"+temp[0]+"Model:"+temp[1]+"PDT:"+temp[2]+"Business:"+temp[3]+"F-Week:"+temp[4]+"F-Val:"+temp[5]);
										            session.beginTransaction();
										            ForecastUnits obj= new ForecastUnits();
										            obj.setPlanningCycleId(Integer.parseInt(temp[0].toString()));
										            obj.setModelId(temp[1].toString());
										            obj.setProductId(temp[2].toString());
										            obj.setBusiness(temp[3].toString());
										            obj.setForecastPeriod(temp[4].toString());
										            obj.setForecastValue(temp[5].toString());
										            session.save(obj);
										            session.getTransaction().commit();
												}
											}catch(HibernateException he){
						logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
												session.getTransaction().rollback();
											}
											finally {
									            session.close();
											}
										}
									}*/
							}
							else
							{
								System.out.println(productID);
								for(int i=0;i<totalForecastWeeks;i++)
								{
								    //logger.info("PlanCYcle:"+temp[0]+"Model:"+temp[1]+"PDT:"+temp[2]+"Business:"+temp[3]+"F-Week:"+temp[4]+"F-Val:"+temp[5]);
									if( ( week + i) - (totalWeeks) > 0 )
									{
										
										if(( week + i) - (totalWeeks) <10)
										{
											if( ( (week + i) - (totalWeeks) ) == 1)
											{
												cycleYear +=1;
											}
											cycleWeek = "0" + ( ( week + i) - (totalWeeks) );
										}
										else
										{
											cycleWeek = ((week + i) - (totalWeeks))+"";
										}
									}
									else
									{
										cycleWeek = (week + i)+"";
									}
								    session.beginTransaction();
								    ForecastingUnits obj= new ForecastingUnits();
								    obj.setPlanningCycleId(planningCycleId);
								    obj.setModelId("1");
								    obj.setProductId(SKUname);
								    obj.setBusiness(businessValue);
								    obj.setForecastPeriod(cycleYear+"-W"+cycleWeek);
								    obj.setForecastValue("0");
								    session.save(obj);
								    session.getTransaction().commit();
								}
							}
						}
						else
						{
							int cycleYear=year;
							String cycleWeek = null;
							for(int i=0;i<totalForecastWeeks;i++)
							{
							    //logger.info("PlanCYcle:"+temp[0]+"Model:"+temp[1]+"PDT:"+temp[2]+"Business:"+temp[3]+"F-Week:"+temp[4]+"F-Val:"+temp[5]);
								if( ( week + i) - (totalWeeks) > 0 )
								{
									
									if(( week + i) - (totalWeeks) <10)
									{
										if( ( (week + i) - (totalWeeks) ) == 1)
										{
											cycleYear +=1;
										}
										cycleWeek = "0" + ( ( week + i) - (totalWeeks) );
									}
									else
									{
										cycleWeek = ((week + i) - (totalWeeks))+"";
									}
								}
								else
								{
									cycleWeek = ((week + i)+"");
								}
							    session.beginTransaction();
							    ForecastingUnits obj= new ForecastingUnits();
							    obj.setPlanningCycleId(planningCycleId);
							    obj.setModelId("1");
							    obj.setProductId(SKUname);
							    obj.setBusiness(businessValue);
							    obj.setForecastPeriod(cycleYear+"-W"+cycleWeek);
							    obj.setForecastValue("0");
							    session.save(obj);
							    session.getTransaction().commit();
							}
						}
						} catch (HibernateException he) {
					session.getTransaction().rollback();
					logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
				}
				return true;
			}
		}); 
			
		}
	
	/**
	 * @param year
	 * @param week
	 * @param SKUName
	 * @return
	 * @throws ApplicationException
	 * Returns the forecast Units for particular SKU. 
	 * Used in forecasting ASP method in which it uses this forecasting units
	 * Forecasting units are used when the forecasting week is already in the Actual Data
	 */
	public final List<Object[]> getSKUForecastValue(final int year,final int week,final String SKUName,final int planningCycleId) throws ApplicationException{
		Object object = null;
		logger.debug("Entered into getSKUForecastValue in planningDAO");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
						Criteria SKUForecastCriteria = session.createCriteria(ForecastingUnits.class);
						ProjectionList SKUForecastProjection= Projections.projectionList();
						SKUForecastProjection.add(Projections.property("forecastPeriod"));
						SKUForecastProjection.add(Projections.property("forecastValue"));
						SKUForecastCriteria.add(Restrictions.eq("planningCycleId",planningCycleId));
						SKUForecastCriteria.add(Restrictions.eq("productId",SKUName));
						SKUForecastCriteria.add(Restrictions.ge("forecastPeriod",year+"-W"+week));
						SKUForecastCriteria.setProjection(SKUForecastProjection);
						List SKUForecastValue = SKUForecastCriteria.list();
						return SKUForecastValue;
					} catch (HibernateException he) {
logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
					}
					return true;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
		}
		logger.debug("Leaving from getSKUForecastValue method in planningDAO");
		return (List)object;
	}
	/**
	 * 
	 * @return
	 * @throws ApplicationException
	 * Returns list of categories
	 *  */
	public final List<Object[]> getSKUunderCategory(final String category,final String date,final int planningCycleId,final Map selectedFilterMapObj) throws ApplicationException{
		Object object = null;
		logger.debug("Entered into getSKUunderCategory in planningDAO");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
						String year[] = date.split("-W");
						String categoryColumn = PropertiesUtil.getProperty("category");
						String orderWeekColumn = PropertiesUtil.getProperty("orderWeek");
						String productNumberColumn = PropertiesUtil.getProperty("productNumber");
						
						Criteria SkuCategoryCriteria = session.createCriteria(Data.class);
						ProjectionList SkuCategoryProjection= Projections.projectionList();
						SkuCategoryProjection.add(Projections.distinct(Projections.property(productNumberColumn)));
						SkuCategoryCriteria.add(Restrictions.eq(categoryColumn,category));
						SkuCategoryCriteria.add(Restrictions.le(orderWeekColumn,year[0]+"-W"+year[1]));//Weeks less than Planning cycle Week
						if(selectedFilterMapObj!=null){
							Iterator entries = selectedFilterMapObj.entrySet().iterator();
								while (entries.hasNext()) {
								  Entry thisEntry = (Entry) entries.next();
								  Object key = thisEntry.getKey();
								  Object value = thisEntry.getValue();
								  SkuCategoryCriteria.add(Restrictions.eq(key.toString(),value));
							}
						  }
						SkuCategoryCriteria.setProjection(SkuCategoryProjection);
						List SKUCategory = SkuCategoryCriteria.list();
						return SKUCategory;
					} catch (HibernateException he) {
logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
					}
					return true;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
		}
		logger.debug("Leaving from getSKUunderCategory method in planningDAO");
		return (List)object;
	}
	/**
	 * @param SkuList
	 * @param week
	 * @param planningCycleId
	 * @return
	 * @throws ApplicationException
	 */
	public final List<Object[]> forecastingUnitsIdforSku(final List<Object[]> SkuList,final String week,final int planningCycleId,final String businessValue) throws ApplicationException{
		Object object = null;
		logger.debug("Entered into forecastingUnitsIdforSku in planningDAO");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				List forecastUnitsIdforSku = null;
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
						Criteria ForecastUnitsIdforSkuCriteria = session.createCriteria(ForecastingUnits.class);
						ProjectionList SkuProjection= Projections.projectionList();
						SkuProjection.add(Projections.distinct(Projections.property("id")));
						Disjunction actual_data_condition = Restrictions.disjunction();
						actual_data_condition.add(Restrictions.in("productId", SkuList));//Performs the function of 'IN' clause in MYSQL
						ForecastUnitsIdforSkuCriteria.add(Restrictions.eq("forecastPeriod",week));
						ForecastUnitsIdforSkuCriteria.add(Restrictions.eq("business",businessValue));
						ForecastUnitsIdforSkuCriteria.add(Restrictions.eq("planningCycleId",planningCycleId));
						ForecastUnitsIdforSkuCriteria.add(actual_data_condition);
						ForecastUnitsIdforSkuCriteria.setProjection(SkuProjection);
						forecastUnitsIdforSku = ForecastUnitsIdforSkuCriteria.list();
						return forecastUnitsIdforSku;
					} catch (HibernateException he) {
logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
					}
					return forecastUnitsIdforSku;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
		}
		logger.debug("Leaving from forecastingUnitsIdforSku method in planningDAO");
		return (List) object;
	}

	/**
	 * @param SkuList
	 * @param week
	 * @param planningCycleId
	 * @return
	 * @throws ApplicationException
	 */
	public final List<Object[]> forecastingAspIdforSku(final List<Object[]> SkuList,final String week,final int planningCycleId,final String businessValue) throws ApplicationException{
		Object object = null;
		logger.debug("Entered into forecastingAspIdforSku in planningDAO");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				List forecastUnitsIdforSku = null;
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
						Criteria ForecastAspIdforSkuCriteria = session.createCriteria(ForecastingASP.class);
						ProjectionList SkuProjection= Projections.projectionList();
						SkuProjection.add(Projections.distinct(Projections.property("id")));
						Disjunction actual_data_condition = Restrictions.disjunction();
						actual_data_condition.add(Restrictions.in("productId", SkuList));//Performs the function of 'IN' clause in MYSQL
						ForecastAspIdforSkuCriteria.add(Restrictions.eq("forecastPeriod",week));
						ForecastAspIdforSkuCriteria.add(Restrictions.eq("business",businessValue));
						ForecastAspIdforSkuCriteria.add(Restrictions.eq("planningCycleId",planningCycleId));
						ForecastAspIdforSkuCriteria.add(actual_data_condition);
						ForecastAspIdforSkuCriteria.setProjection(SkuProjection);
						forecastUnitsIdforSku = ForecastAspIdforSkuCriteria.list();
						return forecastUnitsIdforSku;
					} catch (HibernateException he) {
logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
					}
					return forecastUnitsIdforSku;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
		}
		logger.debug("Leaving from forecastingAspIdforSku method in planningDAO");
		return (List) object;
	}
	
	public final List<Object[]> getSKUunderModel(final String model,final String date,final int planningCycleId,final Map selectedFilterMapObj) throws ApplicationException{
		Object object = null;
		logger.debug("Entered into getSKUunderModel in PlanningDAO");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
						String modelColumn = PropertiesUtil.getProperty("model");
						String orderWeekColumn = PropertiesUtil.getProperty("orderWeek");
						String productNumberColumn = PropertiesUtil.getProperty("productNumber");
						
						String year[] = date.split("-W");
						Criteria SkuModelCriteria = session.createCriteria(Data.class);
						ProjectionList SkuModelProjection= Projections.projectionList();
						SkuModelProjection.add(Projections.distinct(Projections.property(productNumberColumn)));
						SkuModelCriteria.add(Restrictions.eq(modelColumn,model));
						SkuModelCriteria.add(Restrictions.le(orderWeekColumn,year[0]+"-W"+year[1]));//Weeks less than Planning cycle Week
						if(selectedFilterMapObj!=null){
							Iterator entries = selectedFilterMapObj.entrySet().iterator();
								while (entries.hasNext()) {
								  Entry thisEntry = (Entry) entries.next();
								  Object key = thisEntry.getKey();
								  Object value = thisEntry.getValue();
								  SkuModelCriteria.add(Restrictions.eq(key.toString(),value));
							}
						  }
						SkuModelCriteria.setProjection(SkuModelProjection);
						List SKUUnderModel = SkuModelCriteria.list();
						return SKUUnderModel;
					} catch (HibernateException he) {
logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
					}
					return true;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
		}
		logger.debug("Leaving from getSKUunderModel method in PlanningDAO");
		return (List)object;
	}
	/**
	 * @param SKUname
	 * @param totalForecastWeeks
	 * @param SKUForecastValue
	 * @param weeks
	 * @param totalWeeks
	 * @throws ApplicationException
	 * Calculates Forecast values for ASP
	 */
	public void baseForecastASP(final String SKUname,final int totalForecastWeeks,List <Object[]> SKUForecastValue,final List <Object[]> weeks,final int totalWeeks,final int year,final int week,final int planningCycleId,final String businessValue) throws ApplicationException{
		logger.info("Getting inside baseForecastASP planningDAO method");
		Object object = null;
		object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {							
					String orderWeekColumn = PropertiesUtil.getProperty("orderWeek");
					String productNumberColumn = PropertiesUtil.getProperty("productNumber");
					String businessTypeColumn = PropertiesUtil.getProperty("businessType");
					String grossDollarsColumn = PropertiesUtil.getProperty("grossDollars");
					String ASPColumn = PropertiesUtil.getProperty("ASP");
					
						Criteria actualDataASP= session.createCriteria(Data.class);
						ProjectionList ASPForecastProjection= Projections.projectionList();
						ASPForecastProjection.add(Projections.property("planningCycleId"));
						ASPForecastProjection.add(Projections.groupProperty(orderWeekColumn));
						ASPForecastProjection.add(Projections.groupProperty(productNumberColumn));
						ASPForecastProjection.add(Projections.groupProperty(businessTypeColumn));
						ASPForecastProjection.add(Projections.sum(grossDollarsColumn));
						ASPForecastProjection.add(Projections.property(ASPColumn));
						actualDataASP.add(Restrictions.isNull("derivedProductType"));
						actualDataASP.add(Restrictions.eq(businessTypeColumn,businessValue));
						actualDataASP.add(Restrictions.eq(productNumberColumn,SKUname));
						actualDataASP.setProjection(ASPForecastProjection);
						Disjunction actual_data_condition = Restrictions.disjunction();
						actual_data_condition.add(Restrictions.in(orderWeekColumn, weeks));
						actualDataASP.add(actual_data_condition);
						List<Object []> ASPResultSet = actualDataASP.list();
						LinkedHashMap<String, List<Object[]>> ASPForecast = new LinkedHashMap<String, List<Object[]>>();
						for (Object week : weeks)
							ASPForecast.put(week.toString()+"_"+SKUname.toString(),null);
						if(!ASPResultSet.isEmpty())
						{
						float forecastASPValue=0.0f;
						List<Object[]> mapForecastASPValues= new ArrayList<Object []>();
						
						String business = null;
						String productID=null;
						boolean keyExists = false;
						boolean businessFlag= true;
							for (Object[] result : ASPResultSet) 
							{
									List<Object[]> mapValues= new ArrayList<Object []>();
									Object[] value = new Object[7];
									value[0]=planningCycleId;
									value[1]=1;//ModelID
									value[2]=result[2];
									value[3]=result[3];
									value[4]=result[1];
									value[5]=result[4];
									value[6]=result[5];
									business=result[3].toString();
									productID=result[2].toString();
									mapValues.add(value);
									ASPForecast.put(result[1].toString()+"_"+result[2].toString(), mapValues);
									forecastASPValue += Float.valueOf(value[5].toString());
								//logger.info("Planning cycle:"+result[0].toString()+" F-WEEK:"+result[1].toString()+" P-ID:"+result[2].toString()+" Busi:"+result[3].toString()+" Value:"+result[4].toString());
								if(result[3].toString().toLowerCase().contains("Deal Business".toLowerCase()))
								{
									businessFlag=false;
								}
							}
							
							float tempFvalue=0.0f;
							int mapcount=0;
							int j=0;
							int keyiteratecount=0;
							float SKUForecastVal=0.0f;
							int cycleYear=year;
							String cycleWeek = null;						
							if(businessFlag)
							{	
								for(int i=0;i<totalForecastWeeks;i++)
								{
									if( ( week + i) - (totalWeeks) > 0 )
									{
										
										if(( week + i) - (totalWeeks) <10)
										{
											if( ( (week + i) - (totalWeeks) ) == 1)
											{
												cycleYear +=1;
											}
											cycleWeek = "0" + ( ( week + i) - (totalWeeks) );
										}
										else
										{
											cycleWeek = ((week + i) - (totalWeeks))+"";
										}
									}
									else
									{
										cycleWeek = (week + i)+"";
									}
									//This condition checks whether the forecasting week is already there in the Actual Data
									/*if(ASPForecast.containsKey(cycleYear+"-W"+cycleWeek+"_"+productID))
									{
									       for(Object[] ForecastValue: SKUForecastValue)
								            {
								            	if(ForecastValue[0].equals(year+"-W"+cycleWeek))
								            	{
								            		SKUForecastVal= (Float.valueOf(ForecastValue[1].toString()));
								            	}
								            }
									
										Object[] temp = ASPForecast.get(cycleYear+"-W"+cycleWeek+"_"+productID).get(0);
										tempFvalue =Float.valueOf(temp[5].toString());
							            j++;
							            List<Object[]> mapForecastValues2= new ArrayList<Object []>();
										Object[] addforecastValues = new Object[7];
										addforecastValues[0]=planningCycleId;
										addforecastValues[1]=1;
										addforecastValues[2]=productID;
										addforecastValues[3]=business;
										addforecastValues[4]=cycleYear+"-W"+cycleWeek;
										addforecastValues[5]=tempFvalue/SKUForecastVal;
										addforecastValues[6]=temp[6];
										mapForecastValues2.add(addforecastValues);
										ASPForecast.put(cycleYear+"-W"+cycleWeek+"_"+productID, mapForecastValues2);
										tempFvalue=0;
									}
									else
									{*/
										tempFvalue=0;
										for (String keyName: ASPForecast.keySet())
										{	
												if(keyiteratecount>=mapcount)
												{
														String key = keyName.toString();
														if(ASPForecast.get(key) != null){
															Object[] temp = ASPForecast.get(key).get(0);
															tempFvalue +=Float.valueOf(temp[6].toString());
														}else{
															tempFvalue +=0.0;
														}
											            keyiteratecount++;	
												}
												else
												{
													keyiteratecount++;
													continue;
												}
										}
										List<Object[]> mapForecastValues2= new ArrayList<Object []>();
										Object[] addforecastValues = new Object[7];
										addforecastValues[0]=planningCycleId;
										addforecastValues[1]=1;
										addforecastValues[2]=productID;
										addforecastValues[3]=business;
										addforecastValues[4]=cycleYear+"-W"+cycleWeek;
										addforecastValues[5]=0;
										addforecastValues[6]=(tempFvalue/6);
										mapForecastValues2.add(addforecastValues);
										ASPForecast.put(cycleYear+"-W"+cycleWeek+"_"+productID, mapForecastValues2);
								
										ForecastingASP obj= new ForecastingASP();
								        //logger.info("FORECAST ASP MAP: P-C:"+temp[0]+"Model:"+temp[1]+"PDT:"+temp[2]+"Business:"+temp[3]+"F-Week:"+temp[4]+"F-Val:"+temp[6]);
								        session.beginTransaction();
								        obj.setPlanningCycleId(planningCycleId);
								        obj.setModelId("1");
								        obj.setProductId(productID);
								        obj.setBusiness(business);
								        obj.setForecastPeriod(cycleYear+"-W"+cycleWeek);
								        obj.setForecastValue((tempFvalue/6)+"");
								        session.save(obj);
								        session.getTransaction().commit();
										mapcount++;
										keyiteratecount=0;
										tempFvalue=0;
									/*}*/
								}
							}
							else
							{
								for(int i=0;i<totalForecastWeeks;i++)
								{
									if( ( week + i) - (totalWeeks) > 0 )
									{
										if(( week + i) - (totalWeeks) <10)
										{
											if( ( (week + i) - (totalWeeks) ) == 1)
											{
												cycleYear +=1;
											}
											cycleWeek = "0" + ( ( week + i) - (totalWeeks) );
										}
										else
										{
											cycleWeek = ((week + i) - (totalWeeks))+"";
										}
									}
									else
									{
										cycleWeek = (week + i)+"";
									}
									//logger.info("PlanCYcle:"+temp[0]+"Model:"+temp[1]+"PDT:"+temp[2]+"Business:"+temp[3]+"F-Week:"+temp[4]+"F-Val:"+temp[5]);
								    session.beginTransaction();
								    ForecastingASP obj= new ForecastingASP();
								    obj.setPlanningCycleId(planningCycleId);
								    obj.setModelId("1");
								    obj.setProductId(SKUname);
								    obj.setBusiness(businessValue);
								    obj.setForecastPeriod(cycleYear+"-W"+cycleWeek);
								    obj.setForecastValue("0");
								    session.save(obj);
								    session.getTransaction().commit();
								}
							}
						}
						else
						{
							int cycleYear=year;
							String cycleWeek = null;
							for(int i=0;i<totalForecastWeeks;i++)
							{
								if( ( week + i) - (totalWeeks) > 0 )
								{
									
									if(( week + i) - (totalWeeks) <10)
									{
										if( ( (week + i) - (totalWeeks) ) == 1)
										{
											cycleYear +=1;
										}
										cycleWeek = "0" + ( ( week + i) - (totalWeeks) );
									}
									else
									{
										cycleWeek = ((week + i) - (totalWeeks))+"";
									}
								}
								else
								{
									cycleWeek = (week + i)+"";
								}
							    //logger.info("PlanCYcle:"+temp[0]+"Model:"+temp[1]+"PDT:"+temp[2]+"Business:"+temp[3]+"F-Week:"+temp[4]+"F-Val:"+temp[5]);
							    session.beginTransaction();
							    ForecastingASP obj= new ForecastingASP();
							    obj.setPlanningCycleId(planningCycleId);
							    obj.setModelId("1");
							    obj.setProductId(SKUname);
							    obj.setBusiness(businessValue);
							    obj.setForecastPeriod(cycleYear+"-W"+cycleWeek);
							    obj.setForecastValue("0");
							    session.save(obj);
							    session.getTransaction().commit();

							}
						}
						
						} catch (HibernateException he) {
					session.getTransaction().rollback();
					logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
				}
				return true;
			}
		});
		logger.info("Leaving from baseForecastASP planningDAO method");
		}
	
	/**
	 * @param SKUname
	 * @param totalForecastWeeks
	 * @param year
	 * @param week
	 * @param weeks
	 * @param totalWeeks
	 * @throws ApplicationException
	 * Forecasts ESC/Unit 
	 */
	public void baseForecastESCUnit(final String SKUname,final int totalForecastWeeks,final int year,final int week,final List <Object[]> weeks,final int totalWeeks,final int planningCycleId,final String businessValue) throws ApplicationException{
		logger.info("Getting inside baseForecastESCUnit planningDAO method");
		Object object = null;
		object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
					String orderWeekColumn = PropertiesUtil.getProperty("orderWeek");
					String productNumberColumn = PropertiesUtil.getProperty("productNumber");
					String businessTypeColumn = PropertiesUtil.getProperty("businessType");
					String ESCColumn = PropertiesUtil.getProperty("ESC");
					
						Criteria actualDataESC= session.createCriteria(Data.class);
						ProjectionList ESCForecastProjection= Projections.projectionList();
						ESCForecastProjection.add(Projections.property("planningCycleId"));
						ESCForecastProjection.add(Projections.groupProperty(orderWeekColumn));
						ESCForecastProjection.add(Projections.groupProperty(productNumberColumn));
						ESCForecastProjection.add(Projections.groupProperty(businessTypeColumn));
						ESCForecastProjection.add(Projections.sum(ESCColumn));
						actualDataESC.add(Restrictions.eq(businessTypeColumn,businessValue));
						actualDataESC.add(Restrictions.eq(productNumberColumn,SKUname));
						actualDataESC.setProjection(ESCForecastProjection);
						actualDataESC.add(Restrictions.isNull("derivedProductType"));
						Disjunction actual_data_condition = Restrictions.disjunction();
						actual_data_condition.add(Restrictions.in(orderWeekColumn, weeks));//Performs the function of 'IN' clause in MYSQL
						actualDataESC.add(actual_data_condition);
						List<Object []> ESCResultSet = actualDataESC.list();
						LinkedHashMap<String, List<Object[]>> ESCForecast = new LinkedHashMap<String, List<Object[]>>();
						for (Object week : weeks)
							ESCForecast.put(week.toString()+"_"+SKUname.toString(),null);
						float forecastESCValue=0.0f;
						List<Object[]> mapForecastASPValues= new ArrayList<Object []>();
						String business = null;
						String productID=null;
						boolean businessFlag= true;
						if(ESCResultSet != null && !ESCResultSet.isEmpty()){
							for (Object[] result : ESCResultSet) 
							{
									List<Object[]> mapValues= new ArrayList<Object []>();
									Object[] value = new Object[6];
									value[0]=planningCycleId;
									value[1]=1;//ModelID
									value[2]=result[2];
									value[3]=result[3];
									value[4]=result[1];
									value[5]=result[4];
									business=result[3].toString();
									productID=result[2].toString();
									mapValues.add(value);
									ESCForecast.put(result[1].toString()+"_"+result[2].toString(), mapValues);
									//forecastESCValue += Float.valueOf(value[5].toString());
								//logger.info("Planning cycle:"+result[0].toString()+" F-WEEK:"+result[1].toString()+" P-ID:"+result[2].toString()+" Busi:"+result[3].toString()+" Value:"+result[4].toString());
								if(result[3].toString().toLowerCase().contains("Deal Business".toLowerCase()))
								{
									businessFlag=false;
								}
							}
							float tempFvalue=0.0f;
							int mapcount=0;
							int keyiteratecount=0;
							float SKUForecastVal=0.0f;
							int cycleYear=year;
							String cycleWeek = null;
							if(businessFlag)
							{	
								for(int i=0;i<totalForecastWeeks;i++)
								{
									if( ( week + i) - (totalWeeks) > 0 )
									{
										
										if(( week + i) - (totalWeeks) <10)
										{
											if( ( (week + i) - (totalWeeks) ) == 1)
											{
												cycleYear +=1;
											}
											cycleWeek = "0" + ( ( week + i) - (totalWeeks) );
										}
										else
										{
											cycleWeek = ((week + i) - (totalWeeks))+"";
										}
									}
									else
									{
										cycleWeek = (week + i)+"";
									}
										tempFvalue=0;
										for (String keyName: ESCForecast.keySet())
										{	
												if(keyiteratecount>=mapcount)
												{
														String key = keyName.toString();
														if(ESCForecast.get(key) != null){
															Object[] temp = ESCForecast.get(key).get(0);
															if(temp[5]!=null){
															tempFvalue +=Float.valueOf(temp[5].toString());
															}
														}else{
															tempFvalue +=0.0;
														}
											            keyiteratecount++;	
												}
												else
												{
													keyiteratecount++;
													continue;
												}
										}
										List<Object[]> mapForecastValues2= new ArrayList<Object []>();
										Object[] addforecastValues = new Object[6];
										addforecastValues[0]=planningCycleId;
										addforecastValues[1]=1;
										addforecastValues[2]=productID;
										addforecastValues[3]=business;
										addforecastValues[4]=cycleYear+"-W"+cycleWeek;
										addforecastValues[5]=(tempFvalue/6);
										mapForecastValues2.add(addforecastValues);
										ESCForecast.put(cycleYear+"-W"+cycleWeek+"_"+productID, mapForecastValues2);
																					
										ForecastingESC obj= new ForecastingESC();
							            session.beginTransaction();
							            obj.setPlanningCycleId(planningCycleId);
							            obj.setModelId("1");
							            obj.setProductId(productID);
							            obj.setBusiness(business);
							            obj.setForecastPeriod(cycleYear+"-W"+cycleWeek);
							            obj.setForecastValue((tempFvalue/6)+"");
							            session.save(obj);
							            session.getTransaction().commit();
										mapcount++;
										keyiteratecount=0;
										tempFvalue=0;
								}
							}
							else
							{
								for(int i=0;i<totalForecastWeeks;i++)
								{
									if( ( week + i) - (totalWeeks) > 0 )
									{
										if(( week + i) - (totalWeeks) <10)
										{
											if( ( (week + i) - (totalWeeks) ) == 1)
											{
												cycleYear +=1;
											}
											cycleWeek = "0" + ( ( week + i) - (totalWeeks) );
										}
										else
										{
											cycleWeek = ((week + i) - (totalWeeks))+"";
										}
									}
									else
									{
										cycleWeek = (week + i)+"";
									}
								    //logger.info("PlanCYcle:"+temp[0]+"Model:"+temp[1]+"PDT:"+temp[2]+"Business:"+temp[3]+"F-Week:"+temp[4]+"F-Val:"+temp[5]);
								    session.beginTransaction();
								    ForecastingESC obj= new ForecastingESC();
								    obj.setPlanningCycleId(planningCycleId);
								    obj.setModelId("1");
								    obj.setProductId(SKUname);
								    obj.setBusiness(businessValue);
								    obj.setForecastPeriod(year+"-W"+(week+i));
								    obj.setForecastValue("0");
								    session.save(obj);
								    session.getTransaction().commit();
								}
							}
				}else{
					int cycleYear=year;
					String cycleWeek = null;
					for(int i=0;i<totalForecastWeeks;i++)
					{
						if( ( week + i) - (totalWeeks) > 0 )
						{
							if(( week + i) - (totalWeeks) <10)
							{
								if( ( (week + i) - (totalWeeks) ) == 1)
								{
									cycleYear +=1;
								}
								cycleWeek = "0" + ( ( week + i) - (totalWeeks) );
							}
							else
							{
								cycleWeek = ((week + i) - (totalWeeks))+"";
							}
						}
						else
						{
							cycleWeek = (week + i)+"";
						}
					    //logger.info("PlanCYcle:"+temp[0]+"Model:"+temp[1]+"PDT:"+temp[2]+"Business:"+temp[3]+"F-Week:"+temp[4]+"F-Val:"+temp[5]);
					    session.beginTransaction();
					    ForecastingESC obj= new ForecastingESC();
					    obj.setPlanningCycleId(planningCycleId);
					    obj.setModelId("1");
					    obj.setProductId(SKUname);
					    obj.setBusiness(businessValue);
					    obj.setForecastPeriod(year+"-W"+(week+i));
					    obj.setForecastValue("0");
					    session.save(obj);
					    session.getTransaction().commit();
					}
				}	

						} catch (HibernateException he) {
					session.getTransaction().rollback();
					logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
				}
				return true;
			}
		});
		logger.info("Leaving from baseForecastESCUnit planningDAO method");
		}
	
	/**
	 * @param SKUId
	 * @param totalForecastWeeks
	 * @param weeks
	 * @throws ApplicationException
	 * Calculates PM% for given SKU
	 */
	public void baseForecastPMPercent(final String SKUId,final int totalForecastWeeks,List <Object[]> weeks,final int planningCycleId,final String businessValue) throws ApplicationException{
		logger.info("Getting inside baseForecastPMPercent planningDAO method");
		Object object = null;
		object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
						//Fetches data from Forecast_ESC  table 
						Criteria ForecastESCData= session.createCriteria(ForecastingESC.class);
						ProjectionList ESCForecastProjection= Projections.projectionList();
						ESCForecastProjection.add(Projections.property("planningCycleId"));
						ForecastESCData.add(Restrictions.eq("planningCycleId",planningCycleId));
						ForecastESCData.add(Restrictions.eq("productId",SKUId));
						ForecastESCData.add(Restrictions.eq("business",businessValue));
						ESCForecastProjection.add(Projections.property("productId"));
						ESCForecastProjection.add(Projections.property("business"));
						ESCForecastProjection.add(Projections.property("forecastPeriod"));
						ESCForecastProjection.add(Projections.property("forecastValue"));
						ForecastESCData.setProjection(ESCForecastProjection);
						List<Object []> ESCForecastResultSet = ForecastESCData.list();
							
						//Fetches data from Forecast_ASP  table 
						Criteria ForecastASPData= session.createCriteria(ForecastingASP.class);
						ProjectionList ASPForecastProjection= Projections.projectionList();
						ASPForecastProjection.add(Projections.property("planningCycleId"));
						ForecastASPData.add(Restrictions.eq("planningCycleId",planningCycleId));
						ForecastASPData.add(Restrictions.eq("productId",SKUId));
						ForecastASPData.add(Restrictions.eq("business",businessValue));
						ASPForecastProjection.add(Projections.property("productId"));
						ASPForecastProjection.add(Projections.property("business"));
						ASPForecastProjection.add(Projections.property("forecastPeriod"));
						ASPForecastProjection.add(Projections.property("forecastValue"));
						ForecastASPData.setProjection(ASPForecastProjection);
						List<Object []> ASPForecastResultSet = ForecastASPData.list();
						
						//LinkedHashMap<Integer, List<Object[]>> PMPercentValues = new LinkedHashMap<Integer, List<Object[]>>();
						
						Iterator ESC =  ESCForecastResultSet.iterator();
						Iterator ASP =  ASPForecastResultSet.iterator();
						float PMPercent=0.0f;
						int i=0;
						while (ESC.hasNext() && ASP.hasNext())
						{
							Object[] ASPElement = (Object[]) ASP.next();
							Object[] ESCElement= (Object[]) ESC.next();
							if(ASPElement[3]!= null && ASPElement[3].equals(ESCElement[3]))
							{
								if(ASPElement[4] != null && ESCElement[4]!= null)
								{
									if((Float.valueOf(ASPElement[4].toString())) == 0 || (Float.valueOf(ESCElement[4].toString())) == 0)
									{
										PMPercent = 0;
									}
									else
									{
										PMPercent = ((Float.valueOf(ASPElement[4].toString()) - Float.valueOf(ESCElement[4].toString()))/Float.valueOf(ASPElement[4].toString())) * 100;		
									}
								}
								else
								{
									PMPercent = 0;
								}
								ForecastingPM obj= new ForecastingPM();
					            session.beginTransaction();
					            obj.setPlanningCycleId(planningCycleId);
					            obj.setModelId("1");//modelID
					            obj.setProductId(ASPElement[1].toString());
					            obj.setBusiness(ASPElement[2].toString());
					            obj.setForecastPeriod(ASPElement[3].toString());
					            obj.setForecastValue(PMPercent+"");
					            session.save(obj);
					            session.getTransaction().commit();
								i++;
							}
						}							
					} catch (HibernateException he) {
					session.getTransaction().rollback();
					logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
				}
				return true;
			}
		}); 
		logger.info("Leaving from baseForecastPMPercent planningDAO method");
		}
	
	public void updatePlanningCycleStatus(final int planningCyclId,final String logicalName) throws ApplicationException{
		Object object = null;
		logger.debug("Entered into updatePlanningCycleStatus");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
					    	try {
					    		session.beginTransaction();
					    		String sql="From PlanningCycle where id ="+planningCyclId;
					    		String sql1="From MasterPlanningStatus where logicalName ='"+logicalName+"'";
		                    	Query q=session.createQuery(sql);
		                    	Query q1=session.createQuery(sql1);
		                    	PlanningCycle planningCycle=(PlanningCycle)q.uniqueResult();
		        				MasterPlanningStatus masterPlanningStatus=(MasterPlanningStatus)q1.uniqueResult();
		        				planningCycle.setPlanningStatusId(masterPlanningStatus.getId());
		        				session.update(planningCycle);
							    session.getTransaction().commit();
							} catch (Exception e) {
								System.out.println(e);
								// TODO: handle exception
							}
					} catch (HibernateException he) {
						System.out.println(he);
						session.getTransaction().rollback();
					}
					return true;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "updatePlanningCycleStatus", 
					ApplicationErrorCodes.APP_EC_9, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
		logger.debug("Leaving from updatePlanningCycleStatus");
	}
	
	public void flushTables(final int planningCyclId) throws ApplicationException{
		Object object = null;
		logger.debug("Entered into flushTables");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
					    	try {
					    		session.beginTransaction();
					    		String sql = "delete from Data where planningCycleId ="+planningCyclId;
						   		session.createQuery(sql).executeUpdate();
						   		sql = "delete from Data2 where planningCycleId ="+planningCyclId;
						   		session.createQuery(sql).executeUpdate();
						   		sql = "delete from RawData where planningCycleId ="+planningCyclId;
						   		session.createQuery(sql).executeUpdate();
						   		sql = "delete from ForecastingUnits where planningCycleId ="+planningCyclId;
						   		session.createQuery(sql).executeUpdate();
						   		sql = "delete from ForecastingASP where planningCycleId ="+planningCyclId;
						   		session.createQuery(sql).executeUpdate();
						   		sql = "delete from ForecastingESC where planningCycleId ="+planningCyclId;
						   		session.createQuery(sql).executeUpdate();
						   		sql = "delete from ForecastingPM where planningCycleId ="+planningCyclId;
						   		session.createQuery(sql).executeUpdate();
						   		sql = "delete from ForecastingRevenue where planningCycleId ="+planningCyclId;
						   		session.createQuery(sql).executeUpdate();
						   		sql = "delete from OverrideCategoryLevelCommit where planningCycleId ="+planningCyclId;
						   		session.createQuery(sql).executeUpdate();
						   		sql = "delete from OverrideCommitCategory where planningCycleId ="+planningCyclId;
						   		session.createQuery(sql).executeUpdate();
						   		sql = "delete from OverrideCommit where planningCycleId ="+planningCyclId;
						   		session.createQuery(sql).executeUpdate();
							    session.getTransaction().commit();
							} catch (Exception e) {
								throw e;
								// TODO: handle exception
							}
					} catch (Exception e) {
						System.out.println(e);
						session.getTransaction().rollback();
						throw e;
					}
					return true;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "flushTables", 
					ApplicationErrorCodes.APP_EC_33, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
		logger.debug("Leaving from updatePlanningCycleStatus");
	}
	
	
	public void flushDuplicateRowsFromData(final int planningCyclId) throws ApplicationException{
		Object object = null;
		logger.debug("Entered into flushDuplicateRowsFromData");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
					    	try {
					    		session.beginTransaction();
					    		String sql = "delete from Data where C1 in (select distinct C1 from RawData where planningCycleId = "+planningCyclId+") and derivedProductType is null and planningCycleId != "+planningCyclId;
						   		session.createQuery(sql).executeUpdate();
						   		sql = "delete from RawData where C1 in (select distinct C1 from Data where planningCycleId = "+planningCyclId+") and derivedProductType is null and planningCycleId != "+planningCyclId;
						   		session.createQuery(sql).executeUpdate();
							    session.getTransaction().commit();
							} catch (Exception e) {
								// TODO: handle exception
							}
					} catch (HibernateException he) {
						System.out.println(he);
						session.getTransaction().rollback();
					}
					return true;
				}
			});
		}catch (Exception e) {
			logger.error(e.getMessage());
			/*ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "flushDuplicateRowsFromData", 
					ApplicationErrorCodes.APP_EC_9, ApplicationConstants.EXCEPTION, e);*/
			/*throw applicationException;*/
		}
		logger.debug("Leaving from updatePlanningCycleStatus");
	}
	
@SuppressWarnings("unchecked")
public List getPlanningLogDetails(final int userId,final int planningCycleId,final String filterCondition,final String roleFilterCondition,final List rolesList,final String businessValue,final Integer week,final Integer year,final int isFromPlanningFilter,final String skuList) throws ApplicationException{
	Object object=null;
	logger.debug("Entered into getPlanningLogDetails");
	try{
		 object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				 List planningLogList=null;

				try {
					Connection connection = session.connection();
					session.beginTransaction();
					planningLogList=new ArrayList();
					String skuId=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_NUMBER);
					String modelName=PropertiesUtil.getProperty(ApplicationConstants.MODEL);
					String categoryName=PropertiesUtil.getProperty(ApplicationConstants.CATEGORY);
					String business=PropertiesUtil.getProperty(ApplicationConstants.BUSINESS_TYPE);
					String product=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_ID);
					String id = PropertiesUtil.getProperty(ApplicationConstants.ID);
					String skuListId=PropertiesUtil.getProperty(ApplicationConstants.SKU_LIST_ID);
					String productHierarchyII=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_HIERARCHY_II);
					String productDescription = PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_DESCRIPTION);
					String planogram = PropertiesUtil.getProperty(ApplicationConstants.PLANOGRAM);
					String scorecardRollUpColumn = PropertiesUtil.getProperty(ApplicationConstants.SCORECARD_ROLL_UP_COLUMN);
					String weekColumn = PropertiesUtil.getProperty(ApplicationConstants.ORDER_WEEK);
					String productDescrCharLimit= PropertiesUtil.getProperty("productDescrCharLimit");
					String businessWhereClause="";
					if(!ApplicationUtil.isEmptyOrNull(businessValue)){
						businessWhereClause = " sm.business='"+businessValue+"' and ";
					}
					String userIdWhereClause = "";
					if(rolesList != null && rolesList.size()>0 && rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){
						userIdWhereClause=" u.login_id="+userId+" and ";
					}
					String sql="select distinct data."+skuId+",data."+categoryName+",data."+modelName+",data."+productDescription+",data."+productHierarchyII+",data."+planogram+""
							+ ",u.firstName,sm.business from Data as data,SkuList sl,SkuUserMapping sm, Users u where "+businessWhereClause+userIdWhereClause+"  (sl.eolWeek is null or ("+week+"<=sl.eolWeek and "+year+"<=sl.eolYear)) and data."+skuId+"=sl.productId and sl.id=sm.skuListId and sm.userId=u.login_id  ";
					StringBuffer stringBuffer=new StringBuffer();
					stringBuffer.append("and data."+weekColumn+"=(select max(data."+weekColumn+") from data where data."+skuId+"=sl.productId) ");
					if(isFromPlanningFilter==2){
						stringBuffer.append("and data."+skuId+" in ("+skuList+") ");
					}
					if(!ApplicationUtil.isEmptyOrNull(filterCondition)){
						if(isFromPlanningFilter!=2){
							stringBuffer.append(filterCondition);
						}
						
					}
					
					if(!ApplicationUtil.isEmptyOrNull(roleFilterCondition)){
						stringBuffer.append(roleFilterCondition);
					}
					
					stringBuffer.append("order by data."+categoryName+",data."+modelName+",data."+productDescription);
					sql=sql+stringBuffer.toString();
					System.out.println("planning DAO Query"+sql);
		            Query query=session.createQuery(sql);
		            List<Object[]> resultSet=query.list();
		            List <Object[]> productList= new ArrayList<Object[]>();
		            int i=1,count=0;
		            String model="",modelValue="",category="",categoryValue="";
		            int size=resultSet.size();
		            Map masterCommitStatusNameMap = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.MASTER_COMMIT_STATUS_NAME_MAP);
		            Map masterCommitStatusIdMap = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.MASTER_COMMIT_STATUS_ID_MAP);
		            for(Object[] record :resultSet ){
		            	PlanningLog planningLogRecord =new PlanningLog();
		            	String modelStatusStr=null;
		            	String categoryStatusStr=null;
		            	ArrayList<String> resultSet2=new  ArrayList<String>();
		            	String productId=record[0].toString();
		            	String businessValue = record[7].toString();
		            	int commitStatusId = getCommitStatusId(productId, planningCycleId,businessValue);
		            	String statusName = null;
		            	if(masterCommitStatusNameMap != null && commitStatusId!= -1){
							statusName = (String)masterCommitStatusNameMap.get(commitStatusId+"");
						}else if (commitStatusId==-1){
							String commitStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.AVAILABLE_FOR_PM);
							statusName = (String)masterCommitStatusNameMap.get(commitStatusIdStr);
						}
		            	if(rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){
	    					String pmCommitStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_PM);
	    					String cmCommitStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_CM);
	    					String cdCommitStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_CD);
	    					int pmCommitStatusId = -1;
	    					if(!ApplicationUtil.isEmptyOrNull(pmCommitStatusIdStr)){
	    						pmCommitStatusId = Integer.parseInt(pmCommitStatusIdStr);
	    					}
	    					int cmCommitStatusId = -1;
	    					if(!ApplicationUtil.isEmptyOrNull(cmCommitStatusIdStr)){
	    						cmCommitStatusId = Integer.parseInt(cmCommitStatusIdStr);
	    					}
	    					int cdCommitStatusId = -1;
	    					if(!ApplicationUtil.isEmptyOrNull(cdCommitStatusIdStr)){
	    						cdCommitStatusId = Integer.parseInt(cdCommitStatusIdStr);
	    					}
	    					if(commitStatusId==pmCommitStatusId || commitStatusId==cmCommitStatusId || commitStatusId==cdCommitStatusId){
	    						planningLogRecord.setCommitFlag(0);
	    					}
	    				}else if(rolesList.contains(ApplicationConstants.CATEGORY_MANAGER)){
	    					String committedPMStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_PM);
	    					String committedCMStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_CM);
	    					String cdCommitStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_CD);
	    					int committedPMStatusId = -1;
	    					if(!ApplicationUtil.isEmptyOrNull(committedPMStatusIdStr)){
	    						committedPMStatusId = Integer.parseInt(committedPMStatusIdStr);
	    					}
	    					int committedCMStatusId = -1;
	    					if(!ApplicationUtil.isEmptyOrNull(committedCMStatusIdStr)){
	    						committedCMStatusId = Integer.parseInt(committedCMStatusIdStr);
	    					}
	    					int cdCommitStatusId = -1;
	    					if(!ApplicationUtil.isEmptyOrNull(cdCommitStatusIdStr)){
	    						cdCommitStatusId = Integer.parseInt(cdCommitStatusIdStr);
	    					}
	    					if(commitStatusId==committedPMStatusId){
	    						planningLogRecord.setCommitFlag(1);
	    					}else if(commitStatusId==-1 || commitStatusId==committedCMStatusId || commitStatusId == cdCommitStatusId){
	    						planningLogRecord.setCommitFlag(0);
	    					}
	    				}
		            	String overrideStatus=getOverrideFlagStatus(planningCycleId,productId,businessValue);
		            	String categoryStatus=null;
		            	String whereClause="";
		            	int productManagerOverrideAtModelLevel=0;
		            	int categoryManagerOverrideAtModelLevel=0;
		            	int categoryManagerOverrideAtCategoryLevel=0;
		            	if(i==1)
		            	{
		            		if(!ApplicationUtil.isEmptyOrNull(record[1])){
		            			category=record[1].toString();
		            		}
		            		categoryValue=category;
		            		whereClause="AND data."+categoryName+" = '"+category+"'";
		            		int categoryStatusId =getModelLevelStatus(userId,whereClause,rolesList,filterCondition,planningCycleId,"CATEGORY",businessValue,week,year);
		            		categoryStatusStr = (String)masterCommitStatusNameMap.get(categoryStatusId+"");
		            		String availableCMStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.AVAILABLE_FOR_CM);
		            		int availableCMStatusId=-1;
		            		if(!ApplicationUtil.isEmptyOrNull(availableCMStatusIdStr)){
		            			availableCMStatusId = Integer.parseInt(availableCMStatusIdStr);
	    					}
		            		if(availableCMStatusId == categoryStatusId){
		            			planningLogRecord.setCommitcategoryFlag(1);
		            		}else{
		            			planningLogRecord.setCommitcategoryFlag(0);
		            		}
		            		if(!ApplicationUtil.isEmptyOrNull(filterCondition)){
		            			whereClause+=filterCondition;
		            		}
		            		categoryManagerOverrideAtCategoryLevel=getModelAndCategoryLevelOverrideFlagStatus(userId,planningCycleId,whereClause,rolesList,2,ApplicationConstants.OVERRIDE_LEVEL_CATEGORY,businessValue,week,year);
		            		if(!ApplicationUtil.isEmptyOrNull(record[2])){
		            			model=record[2].toString();
		            		}
		            		modelValue=model;
		            		whereClause="AND data."+modelName+" = '"+model+"'";
		            		int modelStatusId = getModelLevelStatus(userId,whereClause,rolesList,filterCondition,planningCycleId,"MODEL",businessValue,week,year);
		            		modelStatusStr = (String)masterCommitStatusNameMap.get(modelStatusId+"");
		            		if(rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){
		    					String pmCommitStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_PM);
		    					String cmCommitStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_CM);
		    					String cdCommitStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_CD);
		    					int cmCommitStatusId = -1;
		    					if(!ApplicationUtil.isEmptyOrNull(cmCommitStatusIdStr)){
		    						cmCommitStatusId = Integer.parseInt(cmCommitStatusIdStr);
		    					}
		    					int cdCommitStatusId = -1;
		    					if(!ApplicationUtil.isEmptyOrNull(cdCommitStatusIdStr)){
		    						cdCommitStatusId = Integer.parseInt(cdCommitStatusIdStr);
		    					}
		    					int pmCommitStatusId = -1;
		    					if(!ApplicationUtil.isEmptyOrNull(pmCommitStatusIdStr)){
		    						pmCommitStatusId = Integer.parseInt(pmCommitStatusIdStr);
		    					}
		    					if(modelStatusId==pmCommitStatusId || modelStatusId==cmCommitStatusId || modelStatusId==cdCommitStatusId){
		    						planningLogRecord.setCommitModelFlag(0);
		    					}
		    				}else if(rolesList.contains(ApplicationConstants.CATEGORY_MANAGER)){
		    					String pmStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_PM);
		    					String cmStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_CM);
		    					String cdStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_CD);
		    					String pmAvailableStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.AVAILABLE_FOR_PM);
		    					int committedByPMStatusId = -1;
		    					if(!ApplicationUtil.isEmptyOrNull(pmStatusIdStr)){
		    						committedByPMStatusId = Integer.parseInt(pmStatusIdStr);
		    					}
		    					int committedByCMStatusId = -1;
		    					if(!ApplicationUtil.isEmptyOrNull(cmStatusIdStr)){
		    						committedByCMStatusId = Integer.parseInt(cmStatusIdStr);
		    					}
		    					int committedByCDStatusId = -1;
		    					if(!ApplicationUtil.isEmptyOrNull(cdStatusIdStr)){
		    						committedByCDStatusId = Integer.parseInt(cdStatusIdStr);
		    					}
		    					int availablePMStatusId = -1;
		    					if(!ApplicationUtil.isEmptyOrNull(pmAvailableStatusIdStr)){
		    						availablePMStatusId = Integer.parseInt(pmAvailableStatusIdStr);
		    					}
		    					if(modelStatusId==committedByPMStatusId){
		    						planningLogRecord.setCommitModelFlag(1);
		    					}else if(modelStatusId==availablePMStatusId || modelStatusId == committedByCMStatusId || modelStatusId == committedByCDStatusId){
		    						planningLogRecord.setCommitModelFlag(0);
		    					}
		    				}
		            		if(!ApplicationUtil.isEmptyOrNull(filterCondition)){
		            			whereClause+=filterCondition;
		            		}
		            		productManagerOverrideAtModelLevel=getModelAndCategoryLevelOverrideFlagStatus(userId,planningCycleId,whereClause,rolesList,1,ApplicationConstants.OVERRIDE_LEVEL_MODEL,businessValue,week,year);
		            		categoryManagerOverrideAtModelLevel=getModelAndCategoryLevelOverrideFlagStatus(userId,planningCycleId,whereClause,rolesList,2,ApplicationConstants.OVERRIDE_LEVEL_MODEL,businessValue,week,year);
		            	}
		            	else
		            	{
		            		
		            		String categoryval ="";
		            		if(!ApplicationUtil.isEmptyOrNull(record[1])){
		            			categoryval=record[1].toString();
		            		}
		            		if(category.equals(categoryval))
		            		{
		            			categoryValue=" ";
		            		}
		            		else{
		            			if(!ApplicationUtil.isEmptyOrNull(record[1])){
		            				categoryValue=record[1].toString();
		            			}
			            		whereClause="AND data."+categoryName+" = '"+categoryValue+"'";
			            		int categoryStatusId =getModelLevelStatus(userId,whereClause,rolesList,filterCondition,planningCycleId,"CATEGORY",businessValue,week,year);
			            		categoryStatusStr = (String)masterCommitStatusNameMap.get(categoryStatusId+"");
			            		String availableCMStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.AVAILABLE_FOR_CM);
			            		int availableCMStatusId=-1;
			            		if(!ApplicationUtil.isEmptyOrNull(availableCMStatusIdStr)){
			            			availableCMStatusId = Integer.parseInt(availableCMStatusIdStr);
		    					}
			            		if(availableCMStatusId == categoryStatusId){
			            			planningLogRecord.setCommitcategoryFlag(1);
			            		}else{
			            			planningLogRecord.setCommitcategoryFlag(0);
			            		}
			            		if(!ApplicationUtil.isEmptyOrNull(filterCondition)){
			            			whereClause+=filterCondition;
			            		}
			            		categoryManagerOverrideAtCategoryLevel=getModelAndCategoryLevelOverrideFlagStatus(userId,planningCycleId,whereClause,rolesList,2,ApplicationConstants.OVERRIDE_LEVEL_CATEGORY,businessValue,week,year);
		            		}
		            		String modelVal ="";
		            		if(!ApplicationUtil.isEmptyOrNull(record[2])){
		            			modelVal = record[2].toString();
		            		}
		            		if(model.equalsIgnoreCase(modelVal)){
		            			modelValue=" ";
		            		}
		            		else{
		            			if(!ApplicationUtil.isEmptyOrNull(record[2])){
		            				modelValue=record[2].toString();
		            			}
		            			whereClause="AND data."+modelName+" = '"+modelValue+"'";
			            		int modelStatusId =getModelLevelStatus(userId,whereClause,rolesList,filterCondition,planningCycleId,"MODEL",businessValue,week,year);
			            		modelStatusStr = (String)masterCommitStatusNameMap.get(modelStatusId+"");
			            		if(rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){
			            			String pmCommitStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_PM);
			    					String cmCommitStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_CM);
			    					String cdCommitStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_CD);
			    					int cmCommitStatusId = -1;
			    					if(!ApplicationUtil.isEmptyOrNull(cmCommitStatusIdStr)){
			    						cmCommitStatusId = Integer.parseInt(cmCommitStatusIdStr);
			    					}
			    					int cdCommitStatusId = -1;
			    					if(!ApplicationUtil.isEmptyOrNull(cdCommitStatusIdStr)){
			    						cdCommitStatusId = Integer.parseInt(cdCommitStatusIdStr);
			    					}
			    					int pmCommitStatusId = -1;
			    					if(!ApplicationUtil.isEmptyOrNull(pmCommitStatusIdStr)){
			    						pmCommitStatusId = Integer.parseInt(pmCommitStatusIdStr);
			    					}
			    					if(modelStatusId==pmCommitStatusId || modelStatusId==cmCommitStatusId || modelStatusId==cdCommitStatusId){
			    						planningLogRecord.setCommitModelFlag(0);
			    					}
			    				}else if(rolesList.contains(ApplicationConstants.CATEGORY_MANAGER)){
			    					String pmStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_PM);
			    					String cmStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_CM);
			    					String cdStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_CD);
			    					String pmAvailableStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.AVAILABLE_FOR_PM);
			    					int committedByPMStatusId = -1;
			    					if(!ApplicationUtil.isEmptyOrNull(pmStatusIdStr)){
			    						committedByPMStatusId = Integer.parseInt(pmStatusIdStr);
			    					}
			    					int committedByCMStatusId = -1;
			    					if(!ApplicationUtil.isEmptyOrNull(cmStatusIdStr)){
			    						committedByCMStatusId = Integer.parseInt(cmStatusIdStr);
			    					}
			    					int committedByCDStatusId = -1;
			    					if(!ApplicationUtil.isEmptyOrNull(cdStatusIdStr)){
			    						committedByCDStatusId = Integer.parseInt(cdStatusIdStr);
			    					}
			    					int availablePMStatusId = -1;
			    					if(!ApplicationUtil.isEmptyOrNull(pmAvailableStatusIdStr)){
			    						availablePMStatusId = Integer.parseInt(pmAvailableStatusIdStr);
			    					}
			    					if(modelStatusId==committedByPMStatusId){
			    						planningLogRecord.setCommitModelFlag(1);
			    					}else if(modelStatusId==availablePMStatusId || modelStatusId == committedByCMStatusId || modelStatusId == committedByCDStatusId){
			    						planningLogRecord.setCommitModelFlag(0);
			    					}
			    				}
			            		if(!ApplicationUtil.isEmptyOrNull(filterCondition)){
			            			whereClause+=filterCondition;
			            		}
			            		productManagerOverrideAtModelLevel=getModelAndCategoryLevelOverrideFlagStatus(userId,planningCycleId,whereClause,rolesList,1,ApplicationConstants.OVERRIDE_LEVEL_MODEL,businessValue,week,year);
			            		categoryManagerOverrideAtModelLevel=getModelAndCategoryLevelOverrideFlagStatus(userId,planningCycleId,whereClause,rolesList,2,ApplicationConstants.OVERRIDE_LEVEL_MODEL,businessValue,week,year);
		            		}
		            	}
		            	if(!ApplicationUtil.isEmptyOrNull(record[2])){
		            		model=record[2].toString();
		            	}
		            	if(!ApplicationUtil.isEmptyOrNull(record[1])){
		            		category=record[1].toString();
		            	}
		            		
		            		i++;
		            		count++;
		            		planningLogRecord.setCategory(categoryValue);
		            		planningLogRecord.setModel(modelValue);
	        				planningLogRecord.setSku(productId);
		            		planningLogRecord.setStatus(statusName);
		            		
		            		if(overrideStatus.equals("both")){
		            			planningLogRecord.setCategoryManagerOverride("Yes");
			            		planningLogRecord.setProductManagerOverride("Yes");
		            		}
		            		else if(overrideStatus.equals(ApplicationConstants.PRODUCT_MANAGER))
		            		{
		            			planningLogRecord.setCategoryManagerOverride("No");
			            		planningLogRecord.setProductManagerOverride("Yes");
		            		}
		            		else if(overrideStatus.equals(ApplicationConstants.CATEGORY_MANAGER))
		            		{
		            			planningLogRecord.setCategoryManagerOverride("Yes");
			            		planningLogRecord.setProductManagerOverride("No");
		            		}
		            		else
		            		{
		            			planningLogRecord.setCategoryManagerOverride("No");
			            		planningLogRecord.setProductManagerOverride("No");
		            		}
		            		
							if(record[3].toString().contains("\"")){
		            			planningLogRecord.setProductDescription(record[3].toString().replaceAll("\"", "&quot;"));
		            			String temp=record[3].toString().replaceAll("\"", "&quot;");
		            			int endIndex=0;
		            			if(!ApplicationUtil.isEmptyOrNull(productDescrCharLimit)){
		            				endIndex =Integer.valueOf(productDescrCharLimit);
		    					}
		            			if(temp.length()<endIndex){
		            				endIndex=temp.length();
		            				planningLogRecord.setToolTipProductDescription(temp.substring(0, endIndex));
		            			}else{
		            				planningLogRecord.setToolTipProductDescription(temp.substring(0, endIndex)+"...");
		            			}
		            			
		            		}
		            		else{
		            			planningLogRecord.setProductDescription(record[3].toString());
		            			String temp=record[3].toString();
		            			int endIndex=0;
		            			if(!ApplicationUtil.isEmptyOrNull(productDescrCharLimit)){
		            				endIndex =Integer.valueOf(productDescrCharLimit);
		    					}
		            			if(temp.length()<endIndex){
		            				endIndex=temp.length();
		            				planningLogRecord.setToolTipProductDescription(temp.substring(0, endIndex));
		            			}else{
		            				planningLogRecord.setToolTipProductDescription(temp.substring(0, endIndex)+"...");
		            			}
		            		}
	            			planningLogRecord.setProductHierarchyII(record[4].toString());
	            			planningLogRecord.setPlanogram(record[5].toString());
            				planningLogRecord.setCategoryStatus(categoryStatusStr);
            				planningLogRecord.setModelStatus(modelStatusStr);
            				planningLogRecord.setUserName(record[6].toString());
	            			if(productManagerOverrideAtModelLevel==0){
	            				planningLogRecord.setProductManagerOverrideModelLevel("Yes");
	            			}
	            			else{
	            				planningLogRecord.setProductManagerOverrideModelLevel("No");
	            			}
		            		
	            			if(categoryManagerOverrideAtModelLevel==0){
	            				planningLogRecord.setCategoryManagerOverrideModelLevel("Yes");
	            			}
	            			else{
	            				planningLogRecord.setCategoryManagerOverrideModelLevel("No");
	            			}
	            			
	            			if(categoryManagerOverrideAtCategoryLevel==0){
	            				planningLogRecord.setCategoryManagerOverrideCategoryLevel("Yes");
	            			}
	            			else{
	            				planningLogRecord.setCategoryManagerOverrideCategoryLevel("No");
	            			}		            		          		
		            		
			            planningLogList.add(planningLogRecord);
		            }
		            session.getTransaction().commit();
				} catch (HibernateException he) {
					session.getTransaction().rollback();
					throw he;
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					session.getTransaction().rollback();
					throw new HibernateException(e);
				} 
				return planningLogList;
			}
		});
	
		logger.debug("Leaving from getUserFromUserName");
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "getUserFromUserName", 
					ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
		return (List)object;
	}

@SuppressWarnings("unchecked")
public List getForecastPeriod(final int planningCycleId) throws ApplicationException{
	
	Object object=null;
	logger.debug("Entered into getForecastPeriod");
	try{
		 object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				 List forecastPeriodList=null;

				try {
						String sql="SELECT distinct forecastPeriod FROM forecasting_units where planningCycleId="+planningCycleId+"";
						 Query query=session.createSQLQuery(sql);
						 forecastPeriodList=query.list();
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new HibernateException(e);
				} 
				return forecastPeriodList;
			}
		 });
		 logger.debug("Leaving from getForecastPeriod");
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "getForecastPeriod", 
					ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
		return (List)object;
}

@SuppressWarnings("unchecked")
public List getForecastValuesFromPlanningLog(final int userId,final int planningCycleId,final String filterCondition,final String roleFilterCondition,final List rolesList,final String businessValue,final int forecastMetric,final int week,final int year) throws ApplicationException{
	
	Object object=null;
	logger.debug("Entered into getForecastValuesFromPlanningLog");
	try{
		 object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				 List planningLogList=null;

				try {
						Connection connection = session.connection();
						session.beginTransaction();
						planningLogList=new ArrayList();
						String skuId=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_NUMBER);
						String modelName=PropertiesUtil.getProperty(ApplicationConstants.MODEL);
						String categoryName=PropertiesUtil.getProperty(ApplicationConstants.CATEGORY);
						String business=PropertiesUtil.getProperty(ApplicationConstants.BUSINESS_TYPE);
						String product=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_ID);
						String id = PropertiesUtil.getProperty(ApplicationConstants.ID);
						String skuListId=PropertiesUtil.getProperty(ApplicationConstants.SKU_LIST_ID);
						String productHierarchyII=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_HIERARCHY_II);
						String productDescription = PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_DESCRIPTION);
						String planogram = PropertiesUtil.getProperty(ApplicationConstants.PLANOGRAM);
						String scorecardRollUpColumn = PropertiesUtil.getProperty(ApplicationConstants.SCORECARD_ROLL_UP_COLUMN);
						String weekColumn = PropertiesUtil.getProperty(ApplicationConstants.ORDER_WEEK);
						String productDescrCharLimit= PropertiesUtil.getProperty("productDescrCharLimit");
						String businessWhereClause="";
						String forecastUnitSql="";
						String forecastAspSql="";
						String userIdWhereClause = "";
						StringBuffer stringBuffer=new StringBuffer();
						if(!ApplicationUtil.isEmptyOrNull(roleFilterCondition)){
							stringBuffer.append(roleFilterCondition);
						}
						
						if(!ApplicationUtil.isEmptyOrNull(filterCondition)){
							stringBuffer.append(filterCondition);
						
						}
						
						String roleBasedSkuList=stringBuffer.toString();
						Query query=null;
						if(forecastMetric == 0){
							forecastUnitSql="select CAST("+productDescription+" as char(255)),b.productId,derivedCategory,"+business+","+modelName+",CAST(b.OriginalForecast as UNSIGNED) from  data d,"
	                				+ " (select  forecastPeriod, productId, case when round(overridevalue) is null then round(forecastValue) else round(overridevalue,0) end as OriginalForecast, business from forecasting_units a left join "
	                				+ "(SELECT  a . *  FROM override_units_log a join (select forecastingUnitsId, max(createdDate) as mdate from override_units_log  where userId = "+userId+" group by forecastingUnitsId) b"
	                				+ " ON a.forecastingUnitsId = b.forecastingUnitsId join forecasting_units f ON f.id = b.forecastingUnitsId and f.planningCycleId = "+planningCycleId+" and a.createdDate = b.mdate) b"
	                				+ " ON a.id = b.forecastingUnitsId where planningCycleId = "+planningCycleId+" and productId in ("+roleBasedSkuList+") group by productId , forecastperiod, business) b where d."+skuId+" = b.productid and b.business = d."+business+" ";
							
							if(rolesList != null && rolesList.size()>0 && rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){
									forecastUnitSql+="and d."+business+" in (select distinct sm.business from Sku_List sl, Sku_User_Mapping sm, Users u where u.login_id = "+userId+" and sl.id = sm.skuListId and sm.userId = u.login_id and (sl.eolWeek is null or ("+week+"<=sl.eolWeek and "+year+"<=sl.eolYear)))";
							}
							
							
							forecastUnitSql+=" and d."+weekColumn+"=(select max(d1."+weekColumn+") from data d1 where d1."+skuId+"=b.productid) ";
							if(!ApplicationUtil.isEmptyOrNull(filterCondition)){
								forecastUnitSql+=filterCondition;
							
							}
							forecastUnitSql+=" order by "+productDescription+",b.productId,"+business+",b.forecastperiod";
					
							System.out.println("Units Forecast Query"+forecastUnitSql);
							query=session.createSQLQuery(forecastUnitSql);
						}else{
							forecastAspSql="select CAST("+productDescription+" as char(255)),b.productId,derivedCategory,"+business+","+modelName+",b.OriginalForecast from  data d,"
	                				+ " (select  forecastPeriod, productId, case when round(overridevalue) is null then cast(round(forecastValue,2) as char(30)) else round(overridevalue) end as OriginalForecast, business from forecasting_asp a left join "
	                				+ "(SELECT  a . *  FROM override_asp_log a join (select forecastingAspId, max(createdDate) as mdate from override_asp_log  where userId = "+userId+" group by forecastingAspId) b"
	                				+ " ON a.forecastingAspId = b.forecastingAspId join forecasting_asp f ON f.id = b.forecastingAspId and f.planningCycleId = "+planningCycleId+" and a.createdDate = b.mdate) b"
	                				+ " ON a.id = b.forecastingAspId where planningCycleId = "+planningCycleId+" and productId in ("+roleBasedSkuList+") group by productId , forecastperiod, business) b where d."+skuId+" = b.productid and b.business = d."+business+" ";
	                				
							
							if(rolesList != null && rolesList.size()>0 && rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){
								forecastAspSql+="and d."+business+" in (select distinct sm.business from Sku_List sl, Sku_User_Mapping sm, Users u where u.login_id = "+userId+" and sl.id = sm.skuListId and sm.userId = u.login_id and (sl.eolWeek is null or ("+week+"<=sl.eolWeek and "+year+"<=sl.eolYear)))";
						}
							
							forecastAspSql+=" and d."+weekColumn+"=(select max(d1."+weekColumn+") from data d1 where d1."+skuId+"=b.productid) ";
							if(!ApplicationUtil.isEmptyOrNull(filterCondition)){
								forecastAspSql+=filterCondition;
							
							}
							forecastAspSql+=" order by "+productDescription+",b.productId,"+business+",b.forecastperiod";
					
							System.out.println("Asp Forecast Query"+forecastAspSql);
							query=session.createSQLQuery(forecastAspSql);
						}
						
						
						
			            List<Object[]> skuUnitsForecastValues=query.list();
			            System.out.println("count"+skuUnitsForecastValues.size());
			            
			            
			            
			            String businessValue=null;
			            String skuValue=null;
			            String categoryValue=null;
			            String description=null;
			            int i=0;
			            List forecastList=new ArrayList();
			           // List ResultantList=new ArrayList();
			            ArrayList<String> forecastValuesList=new ArrayList<String>();
			            planningLogList = new ArrayList();
			            for(Object[] result: skuUnitsForecastValues ){
			            	if(i==0){
			            		businessValue=result[3].toString();
			            		skuValue=result[1].toString();
			            		categoryValue=result[2].toString();
			            		forecastValuesList.add(result[5].toString());
			            		description=result[0].toString();
			            		if(skuUnitsForecastValues.size()==1){
			            			forecastList.add(description);
			            			forecastList.add(skuValue);
				            		forecastList.add(categoryValue);
				            		forecastList.add(businessValue);
				            		forecastList.add(forecastValuesList);
				            		planningLogList.add(forecastList);
			            		}
			            		i++;
			            		continue;
			            	}
			            	if(!(result[1].equals(skuValue))||!(result[3].equals(businessValue))){
			            		forecastList.add(description);
			            		forecastList.add(skuValue);
			            		forecastList.add(categoryValue);
			            		forecastList.add(businessValue);
			            		forecastList.add(forecastValuesList);
			            		planningLogList.add(forecastList);
			            		businessValue=result[3].toString();
			            		description=result[0].toString();
			            		skuValue=result[1].toString();
			            		categoryValue=result[2].toString();
			            		forecastList=new ArrayList();
			            		forecastValuesList=new ArrayList();
			            		forecastValuesList.add(result[5].toString());
			            		if(i==skuUnitsForecastValues.size()-1){
			            			forecastList.add(description);
			            			forecastList.add(skuValue);
				            		forecastList.add(categoryValue);
				            		forecastList.add(businessValue);
				            		forecastList.add(forecastValuesList);
				            		planningLogList.add(forecastList);
			            		}
			            	}
			            	else{
			            		forecastValuesList.add(result[5].toString());
			            		if(i==skuUnitsForecastValues.size()-1){
			            			forecastList.add(description);
			            			forecastList.add(skuValue);
				            		forecastList.add(categoryValue);
				            		forecastList.add(businessValue);
				            		forecastList.add(forecastValuesList);
				            		planningLogList.add(forecastList);
			            		}
			            	}
			            	i++;
			            }
			           // planningLogList=query.list();
			           
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					session.getTransaction().rollback();
					throw new HibernateException(e);
				} 
				return planningLogList;
			}
		 });
		 logger.debug("Leaving from getForecastValuesFromPlanningLog");
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "getForecastValuesFromPlanningLog", 
					ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
		return (List)object;
}

		/*public int getModelAndCategoryLevelStatus1(final String whereClause,final List rolesList,final String filterCondition,final String roleFilterCondition,final int planningCycleId)  throws ApplicationException{
			Object object = null;
			logger.debug("Entered into getSKUCategory in planningDAO");
			try{
				object = hibernateTemplate.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						int result=0;
						try {
							session.beginTransaction();
							String productNumber = PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_NUMBER);
							String planningCycle = ApplicationConstants.PLANNING_CYCLE_ID;
							String statusSql="";
							if(rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){
								statusSql="select count(distinct data."+productNumber+") from Data data where data."+productNumber+" NOT IN (select overrideCommit.productId from OverrideCommit overrideCommit where overrideCommit.planningCycleId="+planningCycleId+") AND data.planningCycleId="+planningCycleId+"";
								if(!ApplicationUtil.isEmptyOrNull(roleFilterCondition)){
									statusSql+=roleFilterCondition;
								}
								if(!ApplicationUtil.isEmptyOrNull(filterCondition)){
									statusSql+=filterCondition;
								}
								
								if(!ApplicationUtil.isEmptyOrNull(whereClause)){
									statusSql+=whereClause;
								}
								Query statusQuery=session.createQuery(statusSql);
								String statusQueryResult=null;
								if(!ApplicationUtil.isEmptyOrNull(statusQuery.uniqueResult())){
									List ssList=statusQuery.list();
									statusQueryResult=statusQuery.uniqueResult().toString();
								}

								if(Integer.parseInt(statusQueryResult)>0){
									result=1;
								}else{
									result= 0;
								}
							}
							else{
								statusSql="select count(distinct data."+productNumber+") from Data data where data."+productNumber+" NOT IN (select overrideCategoryLevelCommit.productId from OverrideCategoryLevelCommit overrideCategoryLevelCommit where overrideCategoryLevelCommit."+planningCycle+"="+planningCycleId+") AND data."+planningCycle+"="+planningCycleId+"";
								if(!ApplicationUtil.isEmptyOrNull(roleFilterCondition)){
									statusSql+=roleFilterCondition;
								}
								if(!ApplicationUtil.isEmptyOrNull(filterCondition)){
									statusSql+=filterCondition;
								}
								
								if(!ApplicationUtil.isEmptyOrNull(whereClause)){
									statusSql+=whereClause;
								}
								Query statusQuery=session.createQuery(statusSql);
								String statusQueryResult=null;
								if(!ApplicationUtil.isEmptyOrNull(statusQuery.uniqueResult())){
									//List ssList=statusQuery.list();
									statusQueryResult=statusQuery.uniqueResult().toString();
								}
								if(Integer.parseInt(statusQueryResult)>0){
								
									statusSql="select count(distinct data."+productNumber+") from Data data where data."+productNumber+" NOT IN (select overrideCommit.productId from OverrideCommit overrideCommit where overrideCommit."+planningCycle+"="+planningCycleId+") AND data."+planningCycle+"="+planningCycleId+"";
									if(!ApplicationUtil.isEmptyOrNull(roleFilterCondition)){
										statusSql+=roleFilterCondition;
									}
									if(!ApplicationUtil.isEmptyOrNull(filterCondition)){
										statusSql+=filterCondition;
									}
									
									if(!ApplicationUtil.isEmptyOrNull(whereClause)){
										statusSql+=whereClause;
									}
									
									statusQuery=session.createQuery(statusSql);
									if(!ApplicationUtil.isEmptyOrNull(statusQuery.uniqueResult())){
										statusQueryResult=statusQuery.uniqueResult().toString();
									}
									
									if(Integer.parseInt(statusQueryResult)>0){
										result =0;
									}else{
										result=1;
									}
								}else{
									result= 0;
								}
							
							}
						} catch (HibernateException he) {
	logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
							he.printStackTrace();
						}
						return result;
					}
				});
			}catch (Exception e) {
				logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			}
			logger.debug("Leaving from getSKUCategory method in planningDAO");
			return (int)object;
		}*/
 
		public int getModelLevelStatus(final int userId,final String whereClause,final List rolesList,final String filterCondition,final int planningCycleId,final String type,final String businessValue,final Integer week,final Integer year)  throws ApplicationException{
			Object object = null;
			logger.debug("Entered into getModelLevelStatus in planningDAO");
			try{
				object = hibernateTemplate.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						int statusId=0;
						try {
							String productNumber = PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_NUMBER);
							String categoryColumn = PropertiesUtil.getProperty(ApplicationConstants.CATEGORY);
							String businessColumn = PropertiesUtil.getProperty(ApplicationConstants.BUSINESS_TYPE);
							String roleFilterCondition="";
							String filterConditionStr="";
							String businessValueStr="";
							String businessValueSUMStr="";
							if(!ApplicationUtil.isEmptyOrNull(filterCondition)){
								filterConditionStr=filterCondition;
							}
							if(!ApplicationUtil.isEmptyOrNull(businessValue)){
								businessValueStr = "and oc.business = '"+businessValue+"'";
							}
							if(!ApplicationUtil.isEmptyOrNull(businessValue)){
								businessValueSUMStr = "and sm.business = '"+businessValue+"'";
							}
							if(rolesList != null && rolesList.size()>0 && rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){
								roleFilterCondition = " AND data."+productNumber+" IN (select skuList.productId from Sku_List as skuList where  skuList.id IN "
									+ "(select skuUser.skuListId from Sku_User_Mapping as skuUser where skuUser.userId="+userId+")) "
											+ " AND data."+businessColumn+" IN (select distinct skuUser.business from sku_user_mapping as skuUser where skuUser.userId="+userId+") ";
							}else if (rolesList != null && rolesList.size()>0 && rolesList.contains(ApplicationConstants.CATEGORY_MANAGER)){
								roleFilterCondition = " AND data."+categoryColumn+" IN (select catList.categoryName from Category_List as catList where catList.id IN "
									+ "(select catUser.categoryId from Category_User_Mapping as catUser where catUser.userId="+userId+")) "
											+ " AND data."+businessColumn+" IN (select distinct catUser.business from Category_User_Mapping as catUser where catUser.userId = "+userId+")";
							}
							//String statusSql ="select distinct data."+productNumber+",data."+businessColumn+",overrideCommit.commitStatusId from Data data left join Override_Commit overrideCommit on overrideCommit.planningCycleId= "+planningCycleId+" and overrideCommit.productId = data."+productNumber+" and overrideCommit.business = data."+businessColumn+" where 1=1 ";
							String statusSql = "select distinct sl.productId,oc.business,oc.commitStatusId from sku_list sl "+
											" left join override_commit oc ON oc.productid = sl.productId and planningCycleId="+planningCycleId+" "+businessValueStr+
											" where sl.productId in (select distinct C9 from Data data,sku_user_mapping sm where (sl.eolWeek is null or ("+week+"<=sl.eolWeek and "+year+"<=sl.eolYear)) and sl.id=sm.skuListId "+
											" "+businessValueSUMStr+" "+roleFilterCondition+" "+filterConditionStr+" "+whereClause+" )";

/*								if(!ApplicationUtil.isEmptyOrNull(roleFilterCondition)){
									statusSql+=roleFilterCondition;
								}
								if(!ApplicationUtil.isEmptyOrNull(filterCondition)){
									statusSql+=filterCondition;
								}
								
								if(!ApplicationUtil.isEmptyOrNull(whereClause)){
									statusSql+=whereClause;
								}
*/								
							SQLQuery query = session.createSQLQuery(statusSql);
		                		List list = (List)query.list();
		                		boolean isPartialCommited = false;
		                		Integer statudIdObj = -1;
		                		if(list != null && list.size()>0){
		                			int size = list.size();
		                			String statudIdTemp=null;
		                			for(int i=0;i<size;i++){
		                				Object[] obj = (Object[])list.get(i);
		                				if(obj[2] != null){
		                					statudIdObj = (Integer)obj[2];
		                				}else{
		                					statudIdObj=-1;
		                				}
		                				if(statudIdTemp == null){
		                					statudIdTemp = statudIdObj.toString();
		                				}else if(!statudIdTemp.equalsIgnoreCase(statudIdObj.toString())){
		                					isPartialCommited = true;
		                				}
		                			}
		                		}
		                		
		                		if(isPartialCommited){
		                			Map masterCommitStatusIdMap = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.MASTER_COMMIT_STATUS_ID_MAP);
		                			String commitStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.PARTIALLY_COMMITTED_BY_PM);
									if(!ApplicationUtil.isEmptyOrNull(commitStatusIdStr)){
										statusId = Integer.parseInt(commitStatusIdStr);
									}
		                		}else if(statudIdObj != null && statudIdObj.toString().equalsIgnoreCase("-1")){
		                			Map masterCommitStatusIdMap = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.MASTER_COMMIT_STATUS_ID_MAP);
									String commitStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.AVAILABLE_FOR_PM);
									if(!ApplicationUtil.isEmptyOrNull(commitStatusIdStr)){
										statusId = Integer.parseInt(commitStatusIdStr);
									}
		                		}else{
		                			statusId=statudIdObj.intValue();
		                			if (type != null && type.equals("CATEGORY")){
		                				Map masterCommitStatusIdMap = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.MASTER_COMMIT_STATUS_ID_MAP);
		                				String pmStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_PM);
		                				String cmStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.AVAILABLE_FOR_CM);
		                				int committedByPMStatusId = -1;
				    					if(!ApplicationUtil.isEmptyOrNull(pmStatusIdStr)){
				    						committedByPMStatusId = Integer.parseInt(pmStatusIdStr);
				    					}
				    					if(committedByPMStatusId==statusId){
				    						if(!ApplicationUtil.isEmptyOrNull(cmStatusIdStr)){
				    							statusId =  Integer.parseInt(cmStatusIdStr);
				    						}
				    					}
		                			}
		                		}
						} catch (HibernateException he) {
							logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
							he.printStackTrace();
						}
						return statusId;
					}
				});
			}catch (Exception e) {
				logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			}
			logger.debug("Leaving from getModelLevelStatus method in planningDAO");
			return (int)object;
		}
		 /** @return
		 * @throws ApplicationException
		 * Returns list of categories
		 **/
		public final List<Object[]> getSKUCategory() throws ApplicationException{
			Object object = null;
			logger.debug("Entered into getSKUCategory in planningDAO");
			try{
				object = hibernateTemplate.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						try {
							String categoryName=PropertiesUtil.getProperty(ApplicationConstants.CATEGORY);
							Criteria SkuCategoryCriteria = session.createCriteria(Data.class);
							ProjectionList seasonalityIndexProjection= Projections.projectionList();
							seasonalityIndexProjection.add(Projections.distinct(Projections.property(categoryName)));
							SkuCategoryCriteria.setProjection(seasonalityIndexProjection);
							List SKUCategory = SkuCategoryCriteria.list();
							return SKUCategory;
						} catch (HibernateException he) {
	logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
						}
						return true;
					}
				});
			}catch (Exception e) {
				logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			}
			logger.debug("Leaving from getSKUCategory method in planningDAO");
			return (List)object;
		}
		
		/**
		 * 
		 * @param SKUwithOverrideValue
		 * @param overriddenValue
		 * @param userId
		 * @param comment
		 * @param aspFlag
		 * @param week
		 * @throws ApplicationException
		 */
		public final void overrideForecast(final List<Object[]> SKUwithOverrideValue,final String overriddenValue,final int userId,final String comment,final int aspFlag,final String week,final int planningCycleId,final String overrideLevel,final String businessValue) throws ApplicationException{
			Object object = null;
			logger.debug("Entered into overrideForecast in planningDAO");
			try{
				object = hibernateTemplate.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						try {
							float sumOfForecast = 0.0f;//sum of all Forecast value.
							int validFlag = 0;
							int diffOverriddenForecast = 0;
							List <Object[]> weightageValue = new ArrayList<Object[]>();
							TreeMap <Float, List<Integer>> SkuDuplicateMap = new TreeMap <Float, List<Integer>>();//Maintaines duplicate value.TreeMap sorts the key in ascending Order
							float weight = 0;//Weight coefficients
							float tmp = 0;
							Integer finalSKU;//used when two or more sku have same forecast value
							int roundOffForecastSum = 0;//Round Off Forecasts
								
								for(Object[] skuName : SKUwithOverrideValue)
						        {
									List <Integer> duplicateSKUList = new ArrayList<Integer>();									
									if (skuName[4] == null)//No override Value for SKU
									{
										sumOfForecast += Float.valueOf(skuName[3].toString());
										
										if(SkuDuplicateMap.containsKey(Float.valueOf(skuName[3].toString())))//Checks if key already there in Map if presents add the SKU to existing list
										{
											duplicateSKUList = SkuDuplicateMap.get(Float.valueOf(skuName[3].toString()));
											duplicateSKUList.add(Integer.parseInt(skuName[1].toString()));
											SkuDuplicateMap.put(Float.valueOf(skuName[3].toString()),duplicateSKUList);
										}
										else//creates a new entry for sku
										{
											duplicateSKUList.add(Integer.parseInt(skuName[1].toString()));
											SkuDuplicateMap.put(Float.valueOf(skuName[3].toString()),duplicateSKUList);
										}
									}
									else if(skuName[4] != null)// Override value is there
									{
										if(SkuDuplicateMap.containsKey(Float.valueOf(skuName[4].toString())))
										{
											duplicateSKUList = SkuDuplicateMap.get(Float.valueOf(skuName[4].toString()));
											duplicateSKUList.add(Integer.parseInt(skuName[1].toString()));
											SkuDuplicateMap.put(Float.valueOf(skuName[4].toString()),duplicateSKUList);
										}
										else
										{
											duplicateSKUList.add(Integer.parseInt(skuName[1].toString()));
											SkuDuplicateMap.put(Float.valueOf(skuName[4].toString()), duplicateSKUList);
										}
										sumOfForecast += Float.valueOf(skuName[4].toString());
									}
						        }
								for(Object[] skuValue : SKUwithOverrideValue)
						        {
									Object[] tmpObj = new Object[5];
									if (skuValue[4] == null)
									{
										tmpObj[0] = userId;//UserId
										tmpObj[1] = skuValue[1];//ForecastTable ID
										tmpObj[2] = skuValue[2];//Forecast Period
										tmpObj[3] = skuValue[3];//Forecast Value
										if(Float.valueOf(skuValue[3].toString()) == 0 && Float.valueOf(overriddenValue.toString()) > 0)
										{
											for(Object[] skuOverrideValueValidate : SKUwithOverrideValue)
											{
												if(Float.valueOf (skuOverrideValueValidate[3].toString()) > 0)
												{
													validFlag = 1;
												}
											}
											if(validFlag == 0)
											{
												tmpObj[4] = Integer.parseInt(overriddenValue.toString());
												roundOffForecastSum += Float.valueOf(tmpObj[4].toString());
												weightageValue.add(tmpObj);
											}
											else
											{
												weight = (Float.valueOf(skuValue[3].toString())/sumOfForecast)*100;//WeightCoefficient
												tmp = ( weight * Float.valueOf( overriddenValue.toString() ) / 100 ) ;
												tmpObj[4] = (int)tmp;//Overridern Value
												roundOffForecastSum += (int)tmp;
												weightageValue.add(tmpObj);
											}
										}
										else
										{
											weight = (Float.valueOf(skuValue[3].toString())/sumOfForecast)*100;//WeightCoefficient
											tmp = ( weight * Float.valueOf( overriddenValue.toString() ) / 100 ) ;
											tmpObj[4] = (int)tmp;//Overridern Value
											roundOffForecastSum += (int)tmp;
											weightageValue.add(tmpObj);
										}
										weight = 0;
										tmp = 0;
									}
									else if(skuValue[4] != null)
									{
										tmpObj[0] = userId;//UserId
										tmpObj[1] = skuValue[1];//ForecastTable ID
										tmpObj[2] = skuValue[2];//Forecast Period
										tmpObj[3] = skuValue[3];//Forecast Value
										if(Float.valueOf(skuValue[4].toString()) == 0 && Float.valueOf(overriddenValue.toString()) > 0)
										{
											for(Object[] skuOverrideValueValidate : SKUwithOverrideValue)
											{
												if(Float.valueOf (skuOverrideValueValidate[4].toString()) > 0)
												{
													validFlag = 1;
												}
											}
											if(validFlag == 0)
											{
												tmpObj[4] = Integer.parseInt(overriddenValue.toString());
												roundOffForecastSum += Float.valueOf(tmpObj[4].toString());
												weightageValue.add(tmpObj);
											}
											else
											{
												weight = ( Float.valueOf ( skuValue[4].toString() ) / sumOfForecast)*100;
												tmp = ( weight * Float.valueOf ( overriddenValue.toString() ) / 100 );
												tmpObj[4] = (int)tmp;//Overridern Value
												roundOffForecastSum += (int)tmp;
												weightageValue.add(tmpObj);
											}
										}
										else
										{
											weight = ( Float.valueOf ( skuValue[4].toString() ) / sumOfForecast)*100;
											tmp = ( weight * Float.valueOf ( overriddenValue.toString() ) / 100 );
											tmpObj[4] = (int)tmp;//Overridern Value
											roundOffForecastSum += (int)tmp;
											weightageValue.add(tmpObj);
										}
										
										weight = 0;
										tmp = 0;
									}
						        }
								diffOverriddenForecast = roundOffForecastSum - Integer.parseInt(overriddenValue.toString());//CM Override Delta
								int minMaxFlag = 0;
								if ( diffOverriddenForecast > 0 )
								{
									minMaxFlag = 1;
								}
									try 
									{
										int roundoffErrorForecastId ;
										if ( minMaxFlag == 0 )
										{
											 roundoffErrorForecastId = OverrideUnitsAmbiguityCheck(SkuDuplicateMap.get(SkuDuplicateMap.firstKey()),week,minMaxFlag,aspFlag,planningCycleId,businessValue);
										}
										else
										{
											 roundoffErrorForecastId = OverrideUnitsAmbiguityCheck(SkuDuplicateMap.get(SkuDuplicateMap.lastKey()),week,minMaxFlag,aspFlag,planningCycleId,businessValue);
										}
										session.beginTransaction();
										OverrideUnitsLogComment Unitsobj =  new OverrideUnitsLogComment();
										OverrideAspLogComment Aspobj =  new OverrideAspLogComment();
										if (aspFlag == 0)
										{
											Unitsobj.setComment(comment);
											session.save(Unitsobj);
										}
										else
										{
											Aspobj.setComment(comment);
											session.save(Aspobj);
										}
										for(Object[] finalOverride : weightageValue)
										{
											int overrideValue=0;//new Value after subtracting or adding the round off error
											
											
											overrideValue = Integer.valueOf(finalOverride[4].toString());
											if( Integer.valueOf(finalOverride[1]+"") == roundoffErrorForecastId)
											{
												overrideValue = Integer.valueOf(finalOverride[4].toString())-diffOverriddenForecast;
												if(overrideValue < 0)
												{
													overrideValue = 0;
												}
											}				
											if (aspFlag == 0)
											{
												OverrideUnitsLog obj2 = new OverrideUnitsLog();
												obj2.setUserId(userId);
												obj2.setForecastingUnitsId(Integer.valueOf(finalOverride[1].toString()));
												obj2.setOverrideValue(overrideValue);
												obj2.setOverrrideUnitsLogObj(Unitsobj);
												java.util.Date date= new java.util.Date();
												obj2.setCreatedDate(new Timestamp(new java.util.Date().getTime())+"");
												Unitsobj.getOverrideUnitsLogCommentObj().add(obj2);
												obj2.setOverrideLevel(overrideLevel);
												session.save(obj2);
											}
											else if(aspFlag == 1)
											{
												OverrideAspLog obj2 = new OverrideAspLog();
												obj2.setUserId(userId);
												obj2.setForecastingAspId(Integer.valueOf(finalOverride[1].toString()));
												obj2.setOverrideValue(overrideValue+"");
												obj2.setOverrideAspLogObj(Aspobj);
												java.util.Date date= new java.util.Date();
												obj2.setCreatedDate(new Timestamp(new java.util.Date().getTime())+"");
												Aspobj.getOverrideAspLogCommentObj().add(obj2);
												obj2.setOverrideLevel(overrideLevel);
												session.save(obj2);
											}
										}
										session.getTransaction().commit();
									} catch (Exception e) 
									{
										session.getTransaction().rollback();
										logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
										throw new HibernateException(e);
									}
						} catch (HibernateException he) {
							session.getTransaction().rollback();
								logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
								throw he;
						}
						return true;
					}
				});
			}catch (Exception e) {
				ApplicationException ae = ApplicationException
						.createApplicationException("PlanningDAO",
								"overrideForecast",
								ApplicationErrorCodes.APP_EC_15,
								ApplicationConstants.EXCEPTION, null);
				logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
				throw ae;
			}
			logger.debug("Leaving from overrideForecast method in planningDAO");
		}
	/**
	 * 
	 * @param SKUwithOverrideValue
	 * @param overriddenValue
	 * @param userId
	 * @param comment
	 * @param aspFlag
	 * @param week
	 * @param planningCycleId
	 * @throws ApplicationException
	 */
		public final void overrideForecastAsp(final List<Object[]> SKUwithOverrideValue,final String overriddenValue,final int userId,final String comment,final int aspFlag,final String week,final int planningCycleId,final String forecastValueOriginal,final String overrideLevel) throws ApplicationException{
			Object object = null;
			logger.debug("Entered into overrideForecast in planningDAO");
			try{
				object = hibernateTemplate.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						try {
							int sumOfForecast = 0;//sum of all Forecast value.
							int diffOverriddenForecast = 0;
							List <Object[]> weightageValue = new ArrayList<Object[]>();
							TreeMap <Integer, List<Integer>> SkuDuplicateMap = new TreeMap <Integer, List<Integer>>();//Maintaines duplicate value.TreeMap sorts the key in ascending Order
							float weight = 0.0f;//Weight coefficients
							float tmp = 0;
							Integer finalSKU;//used when two or more sku have same forecast value
							int roundOffForecastSum = 0;//Round Off Forecasts
							int validFlag = 0;
								
								/*for(Object[] skuName : SKUwithOverrideValue)
						        {									
									if (skuName[4] == null)//No override Value for SKU
									{
										sumOfForecast += Integer.parseInt(skuName[3].toString());
									}
									else if(skuName[4] != null)// Override value is there
									{
										sumOfForecast += Integer.parseInt(skuName[4].toString());
									}
						        }*/
								for(Object[] skuValue : SKUwithOverrideValue)
						        {
									Object[] tmpObj = new Object[5];
									if (skuValue[4] == null)
									{
										tmpObj[0] = userId;//UserId
										tmpObj[1] = skuValue[1];//ForecastTable ID
										tmpObj[2] = skuValue[2];//Forecast Period
										tmpObj[3] = skuValue[3];//Forecast Value
										if(Float.valueOf(skuValue[3].toString()) == 0 && Float.valueOf(overriddenValue.toString()) > 0)
										{
											for(Object[] skuOverrideValueValidate : SKUwithOverrideValue)
											{
												if(Float.valueOf (skuOverrideValueValidate[3].toString()) > 0)
												{
													validFlag = 1;
												}
											}
											if(validFlag == 0)
											{
												tmpObj[4] = Float.valueOf(overriddenValue.toString());
												weightageValue.add(tmpObj);
											}
											else
											{
												weight =  Float.valueOf( overriddenValue.toString() )/ Float.valueOf(forecastValueOriginal);//WeightCoefficient
												tmp =  weight * Float.valueOf (skuValue[3].toString());
												tmpObj[4] = tmp;//Overridern Value
												roundOffForecastSum += tmp;
												weightageValue.add(tmpObj);
											}
										}
										else
										{
											weight =  Float.valueOf( overriddenValue.toString() )/ Float.valueOf(forecastValueOriginal);//WeightCoefficient
											tmp =  weight * Float.valueOf (skuValue[3].toString());
											tmpObj[4] = tmp;//Overridern Value
											roundOffForecastSum += tmp;
											weightageValue.add(tmpObj);
										}
										
										weight = 0;
										tmp = 0;
									}
									else if(skuValue[4] != null)
									{
										tmpObj[0] = userId;//UserId
										tmpObj[1] = skuValue[1];//ForecastTable ID
										tmpObj[2] = skuValue[2];//Forecast Period
										tmpObj[3] = skuValue[3];//Forecast Value
										if(Float.valueOf(skuValue[4].toString()) == 0 && Float.valueOf(overriddenValue.toString()) > 0)
										{
											for(Object[] skuOverrideValueValidate : SKUwithOverrideValue)
											{
												if(Float.valueOf (skuOverrideValueValidate[4].toString()) > 0)
												{
													validFlag = 1;
												}
											}
											if(validFlag == 1)
											{
												weight =  Float.valueOf( overriddenValue.toString() )/ Float.valueOf(forecastValueOriginal);
												tmp = weight * Float.valueOf (skuValue[4].toString());
												tmpObj[4] = tmp;//Overridern Value
												roundOffForecastSum += tmp;
												weightageValue.add(tmpObj);
											}
											else
											{
												tmpObj[4] = Float.valueOf(overriddenValue.toString());
												weightageValue.add(tmpObj);
											}
										}
										else
										{
											weight =  Float.valueOf( overriddenValue.toString() )/ Float.valueOf(forecastValueOriginal);
											tmp = weight * Float.valueOf (skuValue[4].toString());
											tmpObj[4] = tmp;//Overridern Value
											roundOffForecastSum += tmp;
											weightageValue.add(tmpObj);
										}
										weight = 0;
										tmp = 0;
									}
						        }
										session.beginTransaction();
										OverrideUnitsLogComment Unitsobj =  new OverrideUnitsLogComment();
										OverrideAspLogComment Aspobj =  new OverrideAspLogComment();
										Aspobj.setComment(comment);
										session.save(Aspobj);
										for(Object[] finalOverride : weightageValue)
										{
											OverrideAspLog obj2 = new OverrideAspLog();
											obj2.setUserId(userId);
											obj2.setForecastingAspId(Integer.valueOf(finalOverride[1].toString()));
											obj2.setOverrideValue(finalOverride[4].toString());
											obj2.setOverrideAspLogObj(Aspobj);
											java.util.Date date= new java.util.Date();
											obj2.setCreatedDate(new Timestamp(new java.util.Date().getTime())+"");
											obj2.setOverrideLevel(overrideLevel);;
											Aspobj.getOverrideAspLogCommentObj().add(obj2);
											session.save(obj2);
										}
										session.getTransaction().commit();
									
						} catch (HibernateException he) {
							session.getTransaction().rollback();
							logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
							throw he;
						}
						return true;
					}
				});
			}catch (Exception e) {
				ApplicationException ae = ApplicationException
						.createApplicationException("PlanningDAO",
								"overrideForecast",
								ApplicationErrorCodes.APP_EC_15,
								ApplicationConstants.EXCEPTION, null);
				logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
				throw ae;
			}
			logger.debug("Leaving from overrideForecast method in planningDAO");
		}
	
/**
 * 		
 * @param duplicateSkuList
 * @param week
 * @param minMaxFlag
 * @param aspFlag
 * @return
 * @throws ApplicationException
 */
		public final Integer OverrideUnitsAmbiguityCheck(final List<Integer> duplicateSkuList,final String week,final int minMaxFlag,final int aspFlag,final int planningCycleId,final String businessValue) throws ApplicationException{
			Object object = null;
			logger.debug("Entered into OverrideUnitsAmbiguityCheck in planningDAO");
			try{
				object = hibernateTemplate.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						try {
							if( duplicateSkuList.size () > 1)
							{
								List <Float> aspList = new ArrayList <Float>();
								for(int forecastUnitsskuId : duplicateSkuList)
								{
									String pdtId = "";
									try 
									{
										if( aspFlag == 1)
										{
											pdtId = getSKUIdFromForecastIdASP(forecastUnitsskuId);	
										}
										else 
										{
											pdtId = getSKUIdFromForecastIdUnits(forecastUnitsskuId);
										}
										
										Criteria ASPCriteria = session.createCriteria(ForecastingASP.class);
										ASPCriteria.setProjection(Projections.distinct(Projections.property("forecastValue")));
										ASPCriteria.add(Restrictions.eq("productId",pdtId));
										ASPCriteria.add(Restrictions.eq("business",businessValue));
										ASPCriteria.add(Restrictions.eq("forecastPeriod",week));
										ASPCriteria.add(Restrictions.eq("planningCycleId",planningCycleId));
										
										List tmp = ASPCriteria.list();
										aspList.add(Float.valueOf(( ASPCriteria.list().get(0) ).toString()));
									} catch (ApplicationException e) 
									{
										logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
									}
								}
								if (minMaxFlag == 1)
								{
									//int b = aspList.indexOf(Collections.max(aspList));
									Integer a  = duplicateSkuList.get(aspList.indexOf(Collections.max(aspList))) ;//for debugging purpose
									return  duplicateSkuList.get(aspList.indexOf(Collections.max(aspList))) ;
								}
								else
								{
									//int b = aspList.indexOf(Collections.min(aspList));
									Integer a  = duplicateSkuList.get(aspList.indexOf(Collections.min(aspList))) ;//for debugging purpose
									return  duplicateSkuList.get(aspList.indexOf(Collections.min(aspList)));
								}
							}
							else
							{
								return duplicateSkuList.get(0);
							}
								
						} catch (HibernateException he) {
							logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
						}
						return true;
					}
				});
			}catch (Exception e) {
				logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			}
			logger.debug("Leaving from OverrideUnitsAmbiguityCheck method in planningDAO");
			return (Integer) object;
		}		
public final List<Object[]> getOverriddenValueUnits(final List<Object[]> skuList,final String overiddenWeek,final List<Object[]> ForecastUnitsIdForSkU,final int planningCycleId) throws ApplicationException{
	Object object = null;
	int sumOfForecast =0;
	int sumOfOverriddenForecast =0;
	
	logger.debug("Entered into getOverriddenValue in planningDAO");
	try{
		object = hibernateTemplate.execute(new HibernateCallback() {
			List<Object[]> forecastwithOverridden = null;
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
					Query query = session.createSQLQuery("select e.userId,foreId,forecastPeriod,forecastValue,overrideValue from (select id as foreId,productId,forecastPeriod,forecastValue from forecasting_Units d where id IN (:param))f  left join"
							+ "(select id as overrideid,overrideValue,userId,forecastingUnitsId as fid from override_units_log b inner join"
							+ "(SELECT max(id) as maxid FROM forecasting.override_units_log group by forecastingUnitsId)a on  a.maxid = b.id)e on e.fid = f.foreId").setParameterList("param", ForecastUnitsIdForSkU);
					forecastwithOverridden = query.list();
			        return forecastwithOverridden;
				} catch (HibernateException he) {
					logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
				}
				return forecastwithOverridden;
			}
		});
	}catch (Exception e) {
		logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
	}
	logger.debug("Leaving from getOverriddenValue method in planningDAO");
	return (List)object;
}

public final List<Object[]> getOverriddenValueAsp(final List<Object[]> skuList,final String overiddenWeek,final List<Object[]> ForecastAspIdForSkU,final int planningCycleId) throws ApplicationException{
	Object object = null;
	int sumOfForecast =0;
	int sumOfOverriddenForecast =0;
	
	logger.debug("Entered into getOverriddenValue in planningDAO");
	try{
		object = hibernateTemplate.execute(new HibernateCallback() {
			List<Object[]> forecastwithOverridden=null;
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
					Query query = session.createSQLQuery("select e.userId,foreId,forecastPeriod,forecastValue,overrideValue from (select id as foreId,productId,forecastPeriod,forecastValue from forecasting_asp d where id IN (:param))f  left join"
							+ "(select id as overrideid,overrideValue,userId,forecastingAspId as fid from override_asp_log b inner join"
							+ "(SELECT max(id) as maxid FROM forecasting.override_asp_log group by forecastingaspId)a on  a.maxid = b.id)e on e.fid = f.foreId").setParameterList("param", ForecastAspIdForSkU);
					forecastwithOverridden = query.list();
			        return forecastwithOverridden;
				} catch (HibernateException he) {
					logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
				}
				return forecastwithOverridden;
			}
		});
	}catch (Exception e) {
		logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
	}
	logger.debug("Leaving from getOverriddenValue method in planningDAO");
	return (List)object;
}


public int getSKUCountUnderUser (final int userId, final List<Object[]> SKUListUnderModel,final String businessValue) throws ApplicationException
{
	logger.debug("Entering getSKUCountUnderUser method in planningDAO");
	
	Object object = null;
	try{
		final int SKUCountUnderUser = 0;
		object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
					Criteria skuListCriteria = session.createCriteria(SkuList.class);
					skuListCriteria.setProjection(Projections.property("id"));
					skuListCriteria.add(Restrictions.in("productId", SKUListUnderModel));
					//List skuListId = skuListCriteria.list();
					
					Criteria SKUCountCriteria = session.createCriteria(SkuUserMapping.class);
					SKUCountCriteria.add(Restrictions.eq("userId", userId));
					SKUCountCriteria.add(Restrictions.eq("business", businessValue));
					SKUCountCriteria.add(Restrictions.in("skuListId", skuListCriteria.list()));
					SKUCountCriteria.setProjection(Projections.count("skuListId"));
					//List tmp = SKUCountCriteria.list();
					return Integer.parseInt(SKUCountCriteria.list().get(0).toString());
					} catch (HibernateException he) {
						session.getTransaction().rollback();
logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
				}
				return SKUCountUnderUser;
			}
		});
	}catch (Exception e) {
		e.printStackTrace();
		logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
	}
	logger.debug("Leaving from getSKUCountUnderUser method in planningDAO");
	return (int)object;
}

public int getCommitStatusId(final String productId,final int planningCycleId,final String business) throws ApplicationException
	{
		Object object = null;
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					int commitStatusId=-1;
					try {
							OverrideCommit overrideCommit =  getOverrideCommitObj(planningCycleId, productId,business);
							if(overrideCommit != null){
								commitStatusId =  overrideCommit.getCommitStatusId();
							}
						} catch (HibernateException he) {
	logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
							he.printStackTrace();
					} catch (ApplicationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					return commitStatusId;
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "saveContact", 
					ApplicationErrorCodes.APP_EC_2, ApplicationConstants.EXCEPTION, e);
		}
		return (int)object;
	}
	
public int getModelAndCategoryLevelOverrideFlagStatus(final int userId,final int planningCycleId, final String whereClause,final List rolesList,final int level,final String overrideLevel,final String businessValue,final Integer week,final Integer year) throws ApplicationException{
	Object object = null;
	try{
		object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				session.beginTransaction();
				int overrideFlag=0;
				int result=0;
				String skuId=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_NUMBER);
				String modelName=PropertiesUtil.getProperty(ApplicationConstants.MODEL);
				String categoryName=PropertiesUtil.getProperty(ApplicationConstants.CATEGORY);
				String business=PropertiesUtil.getProperty(ApplicationConstants.BUSINESS_TYPE);
				String product=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_ID);
				String id = PropertiesUtil.getProperty(ApplicationConstants.ID);
				String skuListId=PropertiesUtil.getProperty(ApplicationConstants.SKU_LIST_ID);
				String users=PropertiesUtil.getProperty(ApplicationConstants.USERS);
				String user=PropertiesUtil.getProperty(ApplicationConstants.USER_ID);
				String forecastingAspId=PropertiesUtil.getProperty(ApplicationConstants.FORECASTING_ASP_ID); 
				String forecastingUnitsId=PropertiesUtil.getProperty(ApplicationConstants.FORECASTING_UNITS_ID); 
				String roles = PropertiesUtil.getProperty(ApplicationConstants.ROLES); 
				try {
						String skuCount=null;
						if(rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){
							skuCount="select distinct data."+skuId+" from Data data where data."+skuId+"  IN(select skuList.productId from SkuList as skuList where (skuList.eolWeek is null or ("+week+"<=skuList.eolWeek and "+year+"<=skuList.eolYear)) and skuList.id IN "
							+ "(select skuUser.skuListId from SkuUserMapping as skuUser where skuUser.userId="+userId+"))";
						}else{
							skuCount="select distinct data."+skuId+" from Data data where data."+skuId+" IN(select sl.productId from SkuList sl where (sl.eolWeek is null or ("+week+"<=sl.eolWeek and "+year+"<=sl.eolYear))) and data."+categoryName+" IN(select catList.categoryName from CategoryList as catList where catList.id IN "
							+ "(select catUser.categoryId from CategoryUserMapping as catUser where catUser.userId="+userId+")) ";
							
						}
						
						skuCount+=whereClause;
						String sql="Select roles."+roles+" from Roles roles where (roles."+users+" IN "
								+ "(Select overrideAsp."+user+" from OverrideAspLog overrideAsp where overrideAsp.overrideLevel = '"+overrideLevel+"' and overrideAsp."+forecastingAspId+" IN "
								+ "(Select forecastingAsp."+id+" from ForecastingASP forecastingAsp where business = '"+businessValue+"' and forecastingAsp."+product+" in ("+skuCount+") AND forecastingAsp.planningCycleId = '"+planningCycleId+"')) or roles."+users+" IN"
								+"(Select overrideUnits."+user+" from OverrideUnitsLog overrideUnits where overrideUnits.overrideLevel = '"+overrideLevel+"' and overrideUnits."+forecastingUnitsId+" IN "
								+ "(Select forecastingUnits."+id+" from ForecastingUnits forecastingUnits where business = '"+businessValue+"' and forecastingUnits."+product+" in ("+skuCount+") AND forecastingUnits.planningCycleId = '"+planningCycleId+"')))";
					
						if(level==1){
							sql+="AND roles."+roles+" = 'PRODUCT_MANAGER'";
						}else{
							sql+="AND roles."+roles+" = 'CATEGORY_MANAGER'";
						}
						Query query=session.createQuery(sql);
						List recordSet = (List)query.list();
						if(recordSet != null && recordSet.size()>0){
							result=0;
						}else{
							result=1;
						}
					} catch (HibernateException he) {
						he.printStackTrace();
						session.getTransaction().rollback();
					}
				return result;
			}
		});
	}catch (Exception e) {
		e.printStackTrace();
		/*ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "saveContact", 
				ApplicationErrorCodes.APP_EC_2, ApplicationConstants.EXCEPTION, e);*/
		/*throw applicationException;*/
	}
	return (int)object;	
	
}

public String getOverrideFlagStatus(final int planningCycleId, final String productId,final String businessValue) throws ApplicationException{
	Object object = null;
	try{
		object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				int overrideFlag=0;
				String result=null;
				String skuId=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_NUMBER);
				String modelName=PropertiesUtil.getProperty(ApplicationConstants.MODEL);
				String categoryName=PropertiesUtil.getProperty(ApplicationConstants.CATEGORY);
				String business=PropertiesUtil.getProperty(ApplicationConstants.BUSINESS_TYPE);
				String product=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_ID);
				String id = PropertiesUtil.getProperty(ApplicationConstants.ID);
				String skuListId=PropertiesUtil.getProperty(ApplicationConstants.SKU_LIST_ID);
				String users=PropertiesUtil.getProperty(ApplicationConstants.USERS);
				String userId=PropertiesUtil.getProperty(ApplicationConstants.USER_ID); 
				String forecastingAspId=PropertiesUtil.getProperty(ApplicationConstants.FORECASTING_ASP_ID); 
				String forecastingUnitsId=PropertiesUtil.getProperty(ApplicationConstants.FORECASTING_UNITS_ID); 
				String roles = PropertiesUtil.getProperty(ApplicationConstants.ROLES); 
				
				try {
					Connection connection = session.connection();
					session.beginTransaction();
					String sql="Select roles."+roles+" from Roles roles where roles."+users+" IN "
							+ "(Select overrideAsp."+userId+" from OverrideAspLog overrideAsp where overrideAsp."+forecastingAspId+" IN "
							+ "(Select forecastingAsp."+id+" from ForecastingASP forecastingAsp where forecastingAsp.business = '"+businessValue+"' and forecastingAsp."+product+"='"+productId+"' AND forecastingAsp.planningCycleId = '"+planningCycleId+"')) or roles."+users+" IN"
							+"(Select overrideUnits."+userId+" from OverrideUnitsLog overrideUnits where overrideUnits."+forecastingUnitsId+" IN "
							+ "(Select forecastingUnits."+id+" from ForecastingUnits forecastingUnits where forecastingUnits.business = '"+businessValue+"' and forecastingUnits."+product+"='"+productId+"'AND forecastingUnits.planningCycleId = '"+planningCycleId+"'))";
					
					Query query=session.createQuery(sql);
					List recordSet=query.list();
					if(recordSet.contains(ApplicationConstants.CATEGORY_MANAGER) && recordSet.contains(ApplicationConstants.PRODUCT_MANAGER)){
						result="both";
					} else if(recordSet.contains(ApplicationConstants.PRODUCT_MANAGER)){
						result=ApplicationConstants.PRODUCT_MANAGER;
					} else if(recordSet.contains(ApplicationConstants.CATEGORY_MANAGER)){
						result=ApplicationConstants.CATEGORY_MANAGER;
					}else {
						result="none";
					}
					session.getTransaction().commit();
					} catch (HibernateException he) {
						he.printStackTrace();
						session.getTransaction().rollback();
					}
				return result;
			}
		});
	}catch (Exception e) {
		e.printStackTrace();
		/*ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "saveContact", 
				ApplicationErrorCodes.APP_EC_2, ApplicationConstants.EXCEPTION, e);
		throw applicationException;*/
	}
	return (String)object;	
	
}


public void saveCommitStatus(final int userId,final List selectedSKUIdList,final int planningCycleId,final List rolesList,final String commitSatusIdStr,final String business) throws ApplicationException{
	Object object = null;
	try{
		object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
					Connection connection = session.connection();
					session.beginTransaction();
						if(selectedSKUIdList != null){
							String commitSatusId = null;
							Map masterCommitStatusIdMap = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.MASTER_COMMIT_STATUS_ID_MAP);
							if(ApplicationUtil.isEmptyOrNull(commitSatusIdStr)){
								if(rolesList != null && rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){
									commitSatusId = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_PM);
								}else{
									commitSatusId = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_CM);
								}
							}else{
								commitSatusId = commitSatusIdStr;
							}
							int size = selectedSKUIdList.size();
							List tempSKUIDList = new ArrayList();
							for(int i=0;i<size;i++){
								String skuID = (String)selectedSKUIdList.get(i);
								OverrideCommit overrideCommit = getOverrideCommitObj(planningCycleId, skuID,business);
								if(overrideCommit==null && !tempSKUIDList.contains(skuID)){
									tempSKUIDList.add(skuID);
									overrideCommit=new  OverrideCommit();
									overrideCommit.setPlanningCycleId(planningCycleId);
									overrideCommit.setProductId(skuID);
									overrideCommit.setUserId(userId);
									overrideCommit.setCommitStatusId(Integer.parseInt(commitSatusId));
									overrideCommit.setBusiness(business);
									session.save(overrideCommit);
								}else if(overrideCommit != null && !tempSKUIDList.contains(skuID)){
									tempSKUIDList.add(skuID);
									overrideCommit.setUserId(userId);
									overrideCommit.setCommitStatusId(Integer.parseInt(commitSatusId));
									session.update(overrideCommit);
								}
							}
						}
						session.getTransaction().commit();
				} catch (HibernateException he) {
						session.getTransaction().rollback();
						logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
						throw he;
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					session.getTransaction().rollback();
					throw new HibernateException(e);
				}
				return true;
			}
		});
	}catch (Exception e) {
		ApplicationException ae = ApplicationException
				.createApplicationException("PlanningDAO",
						"saveCommitStatus",
						ApplicationErrorCodes.APP_EC_17,
						ApplicationConstants.EXCEPTION, null);
		logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
		throw ae;
	}
	//return (int)object;
  }

@SuppressWarnings("unchecked")
	public  void saveSKUForecastUnitsOverride(final Integer userId,final String overrideForecastUnitsComment,final List originalForecastUnitsList,final List overrideForecastUnitsList) throws ApplicationException{
		try{
			 hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
						session.beginTransaction();
						OverrideUnitsLogComment overrideUnitsLogComment =null;
						if(!ApplicationUtil.isEmptyOrNull(overrideForecastUnitsComment)){
							overrideUnitsLogComment = new OverrideUnitsLogComment();
							overrideUnitsLogComment.setComment(overrideForecastUnitsComment);
							session.save(overrideUnitsLogComment);
						}
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
									OverrideUnitsLog overrideUnitsLog = new OverrideUnitsLog();
									overrideUnitsLog.setForecastingUnitsId(forecastingUnitsOriginal.getId());
									overrideUnitsLog.setOverrideValue(Integer.parseInt(forecastValueOverriden));
									overrideUnitsLog.setUserId(userId.intValue());
									overrideUnitsLog.setOverrrideUnitsLogObj(overrideUnitsLogComment);
									overrideUnitsLog.setCreatedDate(new Timestamp(new java.util.Date().getTime())+"");
									overrideUnitsLog.setOverrideLevel(ApplicationConstants.OVERRIDE_LEVEL_PRODUCT);
									session.save(overrideUnitsLog);
								}
							}
						}
						session.getTransaction().commit();
					} catch (HibernateException he) {
							he.printStackTrace();
							session.getTransaction().rollback();
					}
				return null;
			}
			});
		}catch (Exception e) {
			e.printStackTrace();
			ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "saveSKUForecastUnitsOverride", 
					ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
	}

@SuppressWarnings("unchecked")
public  void saveSKUForecastAspOverride(final Integer userId,final String overrideForecastAspComment,final List originalForecastAspList,final List overrideForecastAspList) throws ApplicationException{
	try{
		 hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
					session.beginTransaction();
					OverrideAspLogComment overrideAspLogComment =null;
					if(!ApplicationUtil.isEmptyOrNull(overrideForecastAspComment)){
						overrideAspLogComment = new OverrideAspLogComment();
						overrideAspLogComment.setComment(overrideForecastAspComment);
						session.save(overrideAspLogComment);
					}
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
								OverrideAspLog overrideAspLog = new OverrideAspLog();
								overrideAspLog.setForecastingAspId(forecastingAspOriginal.getId());
								overrideAspLog.setOverrideValue(forecastValueOverriden);
								overrideAspLog.setUserId(userId.intValue());
								overrideAspLog.setOverrideAspLogObj(overrideAspLogComment);
								overrideAspLog.setCreatedDate(new Timestamp(new java.util.Date().getTime())+"");
								overrideAspLog.setOverrideLevel(ApplicationConstants.OVERRIDE_LEVEL_PRODUCT);
								session.save(overrideAspLog);
							}
						}
					}
					session.getTransaction().commit();
				} catch (HibernateException he) {
						he.printStackTrace();
						session.getTransaction().rollback();
				}
			return null;
		}
		});
	}catch (Exception e) {
		e.printStackTrace();
		ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "saveSKUForecastAspOverride", 
				ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
		throw applicationException;
	}
   }

public List getEolData(final List rolesList,final Integer userId) throws ApplicationException{
	try{
	final List skuDetailList = new ArrayList();
	logger.debug("Entered into EOLData");
	Object object = hibernateTemplate.execute(new HibernateCallback() {
		public Object doInHibernate(Session session)
				throws HibernateException, SQLException {
			Object object = null;
			try {
				String sql=null;
				String skuId=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_NUMBER);
				String pdesc=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_DESCRIPTION);
				String business=PropertiesUtil.getProperty(ApplicationConstants.BUSINESS_TYPE);
				String ph1=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_HIERARCHY_I);
				String ph2=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_HIERARCHY_II);
				String ph4=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_HIERARCHY_IV);
				String productDescrCharLimit =PropertiesUtil.getProperty("productDescrCharLimit");
                String weekColumn = PropertiesUtil.getProperty(ApplicationConstants.ORDER_WEEK);
				if(rolesList != null && rolesList.size()>0 && rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){
				sql = "select distinct d."+skuId+",d."+pdesc+",d."+business+",d."+ph1+",d."+ph2+",d."+ph4+",sl.eolWeek,sl.eolYear,sl.id from Data d, SkuList sl, SkuUserMapping smu where d.C9=sl.productId and sl.id=smu.skuListId and smu.userId="+userId+"";
				sql+=" and d."+weekColumn+"=(select max(d1."+weekColumn+") from Data d1 where d1."+skuId+"=sl.productId) ";
				}else{
					sql = "select distinct d."+skuId+",d."+pdesc+",d."+business+",d."+ph1+",d."+ph2+",d."+ph4+",sl.eolWeek,sl.eolYear,sl.id from Data d, SkuList sl where d.C9=sl.productId";	
					sql+=" and d."+weekColumn+"=(select max(d1."+weekColumn+") from Data d1 where d1."+skuId+"=sl.productId) ";
				}

				Query q = session.createQuery(sql);
				List sku_list = q.list();
				
	                     if(sku_list != null){
	                     	int size =sku_list.size();
	                     	SkuList skuList=null;
	                     	for(int i=0;i<size;i++){
	                     		Object[] rowList = (Object[])sku_list.get(i);
	                     		skuList = new SkuList();
	                     		if(rowList[0]!=null){
	                     			skuList.setC9(rowList[0].toString());
	                     		}
	                     		if(rowList[1]!=null){
	                     			skuList.setC11(rowList[1].toString());
	                     			String temp=rowList[1].toString();
	                     			int endIndex=0;
			            			if(!ApplicationUtil.isEmptyOrNull(productDescrCharLimit)){
			            				endIndex =Integer.valueOf(productDescrCharLimit);
			    					}
			            			if(temp.length()<endIndex){
			            				endIndex=temp.length();
			            				skuList.setToolTipProductDescription(temp.substring(0, endIndex));
			            			}else{
			            				skuList.setToolTipProductDescription(temp.substring(0, endIndex)+"...");
			            			}
	                     		}
	                     		if(rowList[2]!=null){
	                     			skuList.setC28(rowList[2].toString());
	                     		}
	                     		if(rowList[3]!=null){
	                     			skuList.setC6(rowList[3].toString());
	                     			
	                     		}
	                     		if(rowList[4]!=null){
	                     			skuList.setC7(rowList[4].toString());
	                     		}
	                     		if(rowList[5]!=null){
	                     			skuList.setC19(rowList[5].toString());
	                     		}
	                     		if(rowList[6]!=null && rowList[7]!=null)
	                     		{
	                     			skuList.setCombinedEOL("W" + rowList[6] + "-" + rowList[7]);
	                     		}
	                     		if(rowList[8]!=null){
	                     			skuList.setId(Integer.parseInt(rowList[8].toString()));
	                     		}
	                     		skuDetailList.add(skuList);
	                     		}
	                     }
			} catch (HibernateException he) {
				he.printStackTrace();
				throw he;
			}
			 return (Object)skuDetailList;
		}
	});
	logger.debug("Leaving from EOLData");
    return (List)skuDetailList;
	}catch (Exception e) {
		logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
		throw e;
	}
}
//For EOL update week data
@SuppressWarnings("unchecked")
public  void eolUpdateData( final Integer id, final String startWeek, final String startYear) throws ApplicationException{
	try{
		logger.debug("Entered into EOL Update Data");
		 hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
					SkuList skuList=(SkuList)session.load(SkuList.class, id);
					skuList.setEolWeek(startWeek);
					skuList.setEolYear(startYear);
					session.update(skuList);
				} catch (HibernateException he) {
						he.printStackTrace();
				}
				logger.debug("Leaving into EOL Update Data");
			return null;
		}
		});
	}catch (Exception e) {
		e.printStackTrace();
		ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "eolUpdateData", 
				ApplicationErrorCodes.APP_EC_20, ApplicationConstants.EXCEPTION, e);
		throw applicationException;
	}
}
public void saveTargetData(final String filePath,final String dbTable,final Integer referenceId,final String referenceColumnName,final boolean deleteTableDataBeforeLoad) throws ApplicationException{
	Object object = null;
	logger.debug("Entered into saveTargetData");
	try{
		object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException{
				try {
					 Connection connection = session.connection();
					 session.beginTransaction();
                	 
					 CsvReaderWriter csvReaderWriter = new CsvReaderWriter(connection);
					 ArrayList<String[]> dataFromExcel=csvReaderWriter.readWriteCSV(filePath, dbTable, deleteTableDataBeforeLoad ,referenceId,referenceColumnName,3);
					 String[] headerRow = csvReaderWriter.readCSVHeader(filePath, dbTable, deleteTableDataBeforeLoad ,referenceId,referenceColumnName,3);
					 int size=dataFromExcel.size();
					 BigDecimal temp=null;
					 Integer firstPrimaryId=0;
					 Integer lastPrimaryId=0;
					 for(int i=0;i<size;i++)
					 {
						 
						 String [] nextRecord=dataFromExcel.get(i);
						 if(nextRecord!=null)
						 {
							 
							// targetData.setId(Integer.parseInt(nextRecord[0]));
							
							 
							 for(int j=0;j<nextRecord.length-4;j++)
							 {
								 TargetData targetData=new TargetData();
								 targetData.setBiz(nextRecord[0]);
								 targetData.setGbu(nextRecord[1]);
								 targetData.setMetric(nextRecord[2]);
								 targetData.setTotalStoreDeals(nextRecord[3]);
								 targetData.setWeek(headerRow[4+j]);
								 if(!ApplicationUtil.isEmptyOrNull(nextRecord[4+j]) && !nextRecord[4+j].equalsIgnoreCase("#DIV/0!")){
									 System.out.println(nextRecord[4+j]);
									 targetData.setValue(new BigDecimal(nextRecord[4+j]));
								 }
								 /*Double seasonalityIndex=0.0;
								 int size1=5;
								 if(j>5){
									 size1=size1;
								 }else{
									 size1=j;
								 }
								 int tempIndex=j;
								
								 for(int k=0;k<=size1;k++){
									 String seasonalityIndexStr=nextRecord[4+tempIndex];
									 if(ApplicationUtil.isEmptyOrNull(seasonalityIndexStr)){
										 seasonalityIndexStr="0";
									 }
									 seasonalityIndex=seasonalityIndex+Double.parseDouble(seasonalityIndexStr);
									 tempIndex--;
								 }
								 seasonalityIndex=(seasonalityIndex/seasonalityIndex);
								 if(seasonalityIndex==0.0){
									 seasonalityIndex=1.0;
								 }
								targetData.setSeasonalityIndex(seasonalityIndex+""); */
								 session.save(targetData);
								 
								 if(i==0 && j==0){
									 firstPrimaryId =  targetData.getId();
								 }
								 if(i==size-1 && j==nextRecord.length-5){
									 lastPrimaryId =  targetData.getId();
								 }
								 
								 //System.out.println("Value = " + nextRecord[0] +"," + nextRecord[1] +"," +nextRecord[2] +"," + nextRecord[3] +","+ headerRow[4+j] + "," + nextRecord[4+j]);
							 }
							 
							 }
						
					 }
					 session.getTransaction().commit();
					 updateTargetDataSeasonalityIndex(firstPrimaryId,lastPrimaryId);
					 
				} catch (HibernateException he) {
					logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
					session.getTransaction().rollback();
					throw he;
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
					session.getTransaction().rollback();
					throw new HibernateException(e);
				}
				return true;
			}
		});
	}catch (Exception e) {
		logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
		ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "saveTargetData", 
				ApplicationErrorCodes.APP_EC_28, ApplicationConstants.EXCEPTION, e);
		throw applicationException;
	}
	logger.debug("Leaving from saveTargetData");
}

public OverrideCommit getOverrideCommitObj(final int planningCycleId, final String productId,final String business) throws ApplicationException
{
	try{
	logger.debug("Entered into getOverrideCommitObj");
	Object object = hibernateTemplate.execute(new HibernateCallback() {
		public Object doInHibernate(Session session)
				throws HibernateException, SQLException {
			try {
				OverrideCommit overrideCommit = null;
            		 String sql = "from OverrideCommit where planningCycleId="+planningCycleId+" and productId='"+productId+"' and business='"+business+"'";
        			 Query q = session.createQuery(sql);
        			 overrideCommit =  (OverrideCommit)q.uniqueResult();
    			return overrideCommit;
			} catch (HibernateException he) {
				throw he;
			}
		}
	});
	logger.debug("Leaving from getOverrideCommitObj");
    return (OverrideCommit)object;
	}catch (Exception e) {
		logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
		ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "getOverrideCommitObj", 
				ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
		throw applicationException;
	}
	
}

public void saveBtbData(final String filePath,final String dbTable,final Integer referenceId,final String referenceColumnName,final boolean deleteTableDataBeforeLoad) throws ApplicationException{
	Object object = null;
	logger.debug("Entered into  BTB Data");
	try{
		object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException{
				try {
					 Connection connection = session.connection();
					 session.beginTransaction();
                	 
					 CsvReaderWriter csvReaderWriter = new CsvReaderWriter(connection);
					 ArrayList<String[]> dataFromExcel=csvReaderWriter.readWriteCSV(filePath, dbTable, deleteTableDataBeforeLoad ,referenceId,referenceColumnName,3);
					 String[] headerRow = csvReaderWriter.readCSVHeader(filePath, dbTable, deleteTableDataBeforeLoad ,referenceId,referenceColumnName,3);
					 int size=dataFromExcel.size();
					 BigDecimal temp=null;
					 
					 for(int i=0;i<size;i++)
					 {
						 
						 String [] nextRecord=dataFromExcel.get(i);
						 if(nextRecord!=null)
						 {
									BTBData btbData = new BTBData();
									
									btbData.setCategory(nextRecord[0]);
									btbData.setBtbValue(nextRecord[1]);
									btbData.setTargetValue(nextRecord[2]);
                                    btbData.setBusiness(nextRecord[3]);
									session.save(btbData);
									
							 }
					}
					 session.getTransaction().commit();
				} catch (HibernateException he) {
					logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
					session.getTransaction().rollback();
					throw he;
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
					session.getTransaction().rollback();
					throw new HibernateException(e);
				}
				return true;
			}
		});
	}catch (Exception e) {
		logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
		e.printStackTrace();
		throw e;
	}
	logger.debug("Leaving from BTB Data");
}
public void uploadEventCalendar(final String filePath,final String dbTable,final Integer referenceId,final String referenceColumnName,final boolean deleteTableDataBeforeLoad) throws ApplicationException{
	Object object = null;
	logger.debug("Entered into  Event Calendar");
	try{
		object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException{
				try {
					 Connection connection = session.connection();
					 session.beginTransaction();
                	 
					 CsvReaderWriter csvReaderWriter = new CsvReaderWriter(connection);
					 ArrayList<String[]> dataFromExcel=csvReaderWriter.readWriteCSV(filePath, dbTable, deleteTableDataBeforeLoad ,referenceId,referenceColumnName,3);
					 String[] headerRow = csvReaderWriter.readCSVHeader(filePath, dbTable, deleteTableDataBeforeLoad ,referenceId,referenceColumnName,3);
					 int size=dataFromExcel.size();
					 BigDecimal temp=null;
					 for(int i=0;i<size;i++)
					 {
						 String [] nextRecord=dataFromExcel.get(i);
						 if(nextRecord!=null)
						 {
							MasterEventCalendar eventCalendar=new MasterEventCalendar();
							eventCalendar.setFiscalWeek(nextRecord[0]);
							eventCalendar.setEventName(nextRecord[1]);
							eventCalendar.setBusiness(nextRecord[2]);
							session.save(eventCalendar);
						 }
					 }
					 session.getTransaction().commit();
				} catch (HibernateException he) {
					session.getTransaction().rollback();
					throw he;
				} catch (Exception e) {
					e.printStackTrace();
					session.getTransaction().rollback();
					throw new HibernateException(e);
				}
				return true;
			}
		});
	}catch (Exception e) {
		logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
		e.printStackTrace();
		ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "uploadEventCalendar", 
				ApplicationErrorCodes.APP_EC_30, ApplicationConstants.EXCEPTION, e);
		throw applicationException;
	}
	logger.debug("Leaving from Event Calender");
}
public int getInitiatedPlanningCycleId() throws ApplicationException
{
	try{
	logger.debug("Entered into getInitiatedPlanningCycleId");
	Object object = hibernateTemplate.execute(new HibernateCallback() {
		public Object doInHibernate(Session session)
				throws HibernateException, SQLException {
			try {
				int result=0;
				OverrideCommit overrideCommit = null;
            		 String sql = "select max(planningCycle.id) from PlanningCycle planningCycle where planningCycle.planningStatusId=(select id from MasterPlanningStatus mps where mps.logicalName='UPLOAD')";
        			 Query q = session.createQuery(sql);
        			 if(!ApplicationUtil.isEmptyOrNull(q.uniqueResult())){
        				 result=Integer.parseInt(q.uniqueResult().toString());
        			 }
    			return result;
			} catch (HibernateException he) {
				throw he;
			}
		}
	});
	logger.debug("Leaving from getInitiatedPlanningCycleId");
    return (int)object;
	}catch (Exception e) {
		e.printStackTrace();
		logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
		ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "getOverrideCommitObj", 
				ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
		throw applicationException;
	}
	
}

public void baseForecast(int year,int week,int planningCycleId) throws ApplicationException {
	logger.info("Getting inside Application Service baseForecast Method");
	List<Object[]> SKUList= new ArrayList <Object[]>();
	List<Object[]> NPIList= new ArrayList <Object[]>();
	LinkedHashMap<String, LinkedHashMap> SKUTargetSIList = new LinkedHashMap<String, LinkedHashMap>();
	List<Object[]> SKUForecastValue= new ArrayList <Object[]>();
	List  SKUForecastWeek= new ArrayList <Object[]>();
	String Category;
	int totalWeeks;
	int totalForecastWeeks=Integer.valueOf(PropertiesUtil.getProperty("totalweeksToForecast"));;
	int range = Integer.valueOf(PropertiesUtil.getProperty("weeksToFetchActual"));
	try
	{
		if(year % 4 ==0 || year % 400 == 0 )
		{
			totalWeeks = 53;
		}
		else
		{
			totalWeeks = 52;
		}
		SKUForecastWeek= getSKUForecastWeek(year,week,range,1);
		Collections.sort(SKUForecastWeek);
		SKUList = (List<Object[]>)getSKUList();
		for(Object[] SKUDetails:SKUList)
		{
			totalForecastWeeks=Integer.valueOf(PropertiesUtil.getProperty("totalweeksToForecast"));
			if( ((SKUDetails[2]==null)||(SKUDetails[1]==null)) || (year < Integer.parseInt ( SKUDetails[2].toString() ) ))
			{
				Category = getSKUCategory(SKUDetails[0].toString());
				
				if(!SKUForecastWeek.isEmpty())
				{
					SKUTargetSIList =getSKUSeasonalityIndex(Category,year,week);
					baseForecast(SKUDetails[0].toString(),totalForecastWeeks,SKUTargetSIList,SKUForecastWeek,totalWeeks,year,week,planningCycleId,SKUDetails[3].toString());
					SKUForecastValue=getSKUForecastValue(year,week, SKUDetails[0].toString(),planningCycleId);
					baseForecastASP(SKUDetails[0].toString(),totalForecastWeeks,SKUForecastValue, SKUForecastWeek,totalWeeks,year,week,planningCycleId,SKUDetails[3].toString());
					baseForecastESCUnit(SKUDetails[0].toString(), totalForecastWeeks,year,week,SKUForecastWeek,totalWeeks,planningCycleId,SKUDetails[3].toString());
					baseForecastPMPercent(SKUDetails[0].toString(), totalForecastWeeks, SKUForecastWeek,planningCycleId,SKUDetails[3].toString());
				}
			}
			else if( ( ( Integer.parseInt(SKUDetails[2].toString()) == year )&& ( Integer.parseInt(SKUDetails[1].toString()) > week )))
			{
				Category = getSKUCategory(SKUDetails[0].toString());
				totalForecastWeeks = Integer.parseInt(SKUDetails[1].toString()) -  week;
				if(!SKUForecastWeek.isEmpty())
				{
					SKUTargetSIList =getSKUSeasonalityIndex(Category,year,week);
					baseForecast(SKUDetails[0].toString(),totalForecastWeeks,SKUTargetSIList,SKUForecastWeek,totalWeeks,year,week,planningCycleId,SKUDetails[3].toString());
					SKUForecastValue=getSKUForecastValue(year,week, SKUDetails[0].toString(),planningCycleId);
					baseForecastASP(SKUDetails[0].toString(),totalForecastWeeks,SKUForecastValue, SKUForecastWeek,totalWeeks,year,week,planningCycleId,SKUDetails[3].toString());
					baseForecastESCUnit(SKUDetails[0].toString(), totalForecastWeeks,year,week,SKUForecastWeek,totalWeeks,planningCycleId,SKUDetails[3].toString());
					baseForecastPMPercent(SKUDetails[0].toString(), totalForecastWeeks, SKUForecastWeek,planningCycleId,SKUDetails[3].toString());
				}
			}
			//break;
		}
		totalForecastWeeks=Integer.valueOf(PropertiesUtil.getProperty("totalweeksToForecast"));
		List<Object[]> npiList = getNpiList(planningCycleId);
		for(Object[] NPIDetails:npiList)
		{
			utilitiesDAO.forecastNewNPIUnits(NPIDetails[0].toString(),totalForecastWeeks,SKUForecastWeek,totalWeeks,year,week,planningCycleId,NPIDetails[1].toString());
			utilitiesDAO.forecastNewNPIASP(NPIDetails[0].toString(),totalForecastWeeks,SKUForecastWeek,totalWeeks,year,week,planningCycleId,NPIDetails[1].toString());
			utilitiesDAO.forecastNewNPIESC(NPIDetails[0].toString(),totalForecastWeeks,SKUForecastWeek,totalWeeks,year,week,planningCycleId,NPIDetails[1].toString());
			utilitiesDAO.forecastNewNPIPMPercent(NPIDetails[0].toString(),totalForecastWeeks,SKUForecastWeek,totalWeeks,year,week,planningCycleId,NPIDetails[1].toString());
		}
		updatePlanningCycleStatus(planningCycleId,ApplicationConstants.REVIEW_STATUS);
	}
	 catch (ApplicationException e) {
		 logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
		 ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "baseForecast", 
			ApplicationErrorCodes.APP_EC_32, ApplicationConstants.EXCEPTION, e);
	throw applicationException;
	}
	logger.info("Exiting  Application Service baseForecast Method");
}

public void automaticUpload() throws ApplicationException
{
	try{
	logger.debug("Entered into automaticUpload");
	Object object = hibernateTemplate.execute(new HibernateCallback() {
		public Object doInHibernate(Session session)
				throws HibernateException, SQLException {
		
			String localDir = PropertiesUtil.getProperty(ApplicationConstants.SCHEDULER_DATA_FOLDER);
			String hisDir = PropertiesUtil.getProperty(ApplicationConstants.SCHEDULER_BACKUP_FOLDER);
			final String FILE_PATTERN = "([^\\s]+(\\.(?i)(csv))$)";
			Pattern pattern = Pattern.compile(FILE_PATTERN);
	        Matcher matcher;
			InputStream inStream = null;
		    OutputStream outStream = null;
		    int uploadFlag=0;
		    int outlierTreatmentFlag=0;
		    int loadNPIFlag=0;
		    int planningCycleId=0;
		    try{
		        planningCycleId=getInitiatedPlanningCycleId();
		        if(planningCycleId!=0){
			    	File directory = new File(localDir);
			    	FileFilter filter = new FileFilter() {
			            @Override
			            public boolean accept(File pathname) {
			               return pathname.isFile();
			            }
			         };
			        File[] fList = directory.listFiles(filter);
					for (File file : fList){
						if (file.isFile()){
							 matcher = pattern.matcher(file.getName());
			        		 if (matcher.matches()) {
			        			 String dataFile=file.getAbsolutePath();
			        			 Files.copy(file.toPath(),(new File(hisDir + file.getName())).toPath(),StandardCopyOption.REPLACE_EXISTING);
				            	try{
				            		 flushTables(planningCycleId);
				            		 saveData(dataFile+"", "raw_data",null,null,false,planningCycleId);
				        			 saveVariableNames(dataFile+"", "variable_names",null,"variableName,columnName",false);
				 				     loadAggregatedDataIntoTable(planningCycleId);
				 				     uploadFlag=1;;
				 				     outlierTreatment(planningCycleId);
				 				     outlierTreatmentFlag=1;
				 				     loadNPIIntoDataTable(planningCycleId);
				 				     System.out.println(dataFile);
				 				     String sql="select planningCycle.startWeek,planningCycle.startYear from PlanningCycle planningCycle where planningCycle.id="+planningCycleId+"";
					            	 Query query=session.createQuery(sql);
					            	 List timeList=query.list();
					            	 uploadFlag=0;
					            	 Object [] timeArray=(Object[])timeList.get(0);
					            	 System.out.println("insert");
					            	 baseForecast(Integer.parseInt(timeArray[1].toString()),Integer.parseInt(timeArray[0].toString()),planningCycleId);
					        	     file.delete();
					        	     uploadFlag=1;
					        			 break; 
				            	}catch(Exception e){
				            		
				            		String adminEmail = PropertiesUtil.getProperty("admin_email");
				 			        String adminPassword = PropertiesUtil.getProperty("admin_password");
				 			        StringBuffer body = new StringBuffer();
				            		Properties props = new Properties();
				            		String receiversMailId = PropertiesUtil.getProperty(ApplicationConstants.EMAIL_ID);
				            		props.put("mail.smtp.auth",
				   			                       PropertiesUtil.getProperty("mail.smtp.auth"));
				            		props.put("mail.smtp.starttls.enable",
				   			                     PropertiesUtil.getProperty("mail.smtp.starttls.enable"));
				            		props.put("mail.smtp.host",
				   			                     PropertiesUtil.getProperty("mail.smtp.host"));
				            		props.put("mail.smtp.port",
				   			                     PropertiesUtil.getProperty("mail.smtp.port"));
				            		  
				            		 if(uploadFlag!=1){
				            			  body.append("Unable to load data, Check your data file format and wheather the column count matches\n ");
					            		}else if(outlierTreatmentFlag!=1){
					            			body.append("Outlier treatment failed");
					            		}else if(loadNPIFlag!=1){
					            			body.append("Error loading NPI DATA");
					            		}
					            		else{
					            			body.append("Error in running base forecast algorithm");
					            		}
			 					        try {
			 					              ApplicationUtil.sendEMail(receiversMailId,
			 					                           "HP SKU Forecasting",
			 					                            body.toString(), adminEmail, adminPassword, props,
			 					                            PropertiesUtil.getProperty("APP_EC_27"));
			 					              
			 				
			 					        } catch (Exception email) {
			 					        	logger.error(email.getMessage());
			 					        	System.out.println(email);
			 					        }
			 					        
				            	}
				            	
			            	 }
						}
					}
		        }
				
		
		}catch(Exception e){
			e.printStackTrace();
		}
			return true;
			
		}
	});
	logger.debug("Leaving from automaticUpload");
	}catch (Exception e) {
		e.printStackTrace();
		logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
		ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "getOverrideCommitObj", 
				ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
		throw applicationException;
	}
	
}

public void updateTargetDataSeasonalityIndex(final int firstId,final int lastId) throws ApplicationException{
	Object object = null;
	logger.debug("Entered into updateTargetDataSeasonalityIndex");
	try{
		object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
				    	try {
				    		session.beginTransaction();
				    		for(int k=firstId;k<=lastId;k++){
				    			String sql="From TargetData where id ="+k;
				    			Query q=session.createQuery(sql);
				    			TargetData targetData=(TargetData)q.uniqueResult();
				    			String limit =PropertiesUtil.getProperty("seasonalityLimit");
				    			String metric=targetData.getMetric();
				    			if(metric.equalsIgnoreCase("Units")){
				    				sql="select sum(a.targetval)/"+limit+" from (SELECT value as targetval FROM target_data where GBU ='"+targetData.getGbu()+"' and Biz='"+targetData.getBiz()+"' and Metric='"+targetData.getMetric()+"'  and id<"+k+"  order by id desc limit "+limit+") a";
					    			SQLQuery query = session.createSQLQuery(sql);
					    			BigDecimal seasonalitySum=(BigDecimal) query.uniqueResult();
					    			BigDecimal currentSeasonalityStr=targetData.getValue();
					    			if(currentSeasonalityStr==null || currentSeasonalityStr.doubleValue()==0){
					    				targetData.setSeasonalityIndex("1");
					    			}else{
					    				if(seasonalitySum==null || seasonalitySum.doubleValue()==0){
					    					targetData.setSeasonalityIndex("1");
					    				}else{
						    				targetData.setSeasonalityIndex(""+(currentSeasonalityStr.doubleValue()/seasonalitySum.doubleValue()));
					    				}
					    				
					    			}
					    			session.update(targetData);
				    			}
				    		}
				    		session.getTransaction().commit();
						} catch (Exception e) {
							System.out.println(e);
							// TODO: handle exception
						}
				} catch (HibernateException he) {
					System.out.println(he);
					session.getTransaction().rollback();
				}
				return true;
			}
		});
	}catch (Exception e) {
		logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
		ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "updatePlanningCycleStatus", 
				ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
		throw applicationException;
	}
	logger.debug("Leaving from updateTargetDataSeasonalityIndex");
}
public void loadNPIIntoDataTable(final int planningCycleId) throws ApplicationException,Exception{
	try{
		logger.debug("Entered into getInitiatedPlanningCycleId");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				session.beginTransaction();
				String npiSql="SELECT npi.npiId,npi.productDescription, npi.productLine, npi.hierarchy1,npi.hierarchy2,"
						+ " npi.hierarchy4, npi.business, npi.plClass,npi.plScorecard,npi.expectedASP,npi.expectedESC,npi.model from npi npi where npi.noMoreNPI = 0 or npi.noMoreNPI is null";
				
				Query q=session.createSQLQuery(npiSql);
				List npiList=q.list();
				
				 String weekColumn=PropertiesUtil.getProperty(ApplicationConstants.ORDER_WEEK);
				 
				String maxWeekSql= "select max(data."+weekColumn+") from Data data where data.planningCycleId = "+planningCycleId+"";
				q=session.createSQLQuery(maxWeekSql);
				String maxWeek =q.uniqueResult().toString();
				
				 Map categoryMap= (Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.CATEGORY_SCORECARD_ROLLUP_MAPPING);
				for(int i=0;i<npiList.size();i++){
					 Data data=new Data();
					 Object [] npiArray =(Object[]) npiList.get(i);
					 
					 if(!ApplicationUtil.isEmptyOrNull(npiArray))
						 
					 {
						 data.setPlanningCycleId(planningCycleId);
						 data.setC1(maxWeek);
						 data.setC5(npiArray[2].toString());
						 data.setC6(npiArray[3].toString());
						 data.setC7(npiArray[4].toString());
						 data.setC8(npiArray[5].toString());
						 data.setC9(npiArray[0].toString());
						 data.setC10(npiArray[7].toString());
						 data.setC11(npiArray[1].toString());
						
						 data.setC28(npiArray[6].toString());
						 if(!ApplicationUtil.isEmptyOrNull(npiArray[9])){
							 data.setC25(npiArray[9].toString());
						 }
						 if(!ApplicationUtil.isEmptyOrNull(npiArray[10])){
							 data.setC17(npiArray[10].toString());
						 }
						 data.setC21(npiArray[11].toString());
                    	 if(npiArray[8].toString().equalsIgnoreCase("DT_Con")){
                    		 data.setC30(PropertiesUtil.getProperty(ApplicationConstants.DT_CLIENT_CONSUMER));
                    	 }else if(npiArray[8].toString().equalsIgnoreCase("DT_Biz")){
                    		 data.setC30(PropertiesUtil.getProperty(ApplicationConstants.DT_CLIENT_BUSINESS));
                    	 }else if(npiArray[8].toString().equalsIgnoreCase("NB_Con")){
                    		 data.setC30(PropertiesUtil.getProperty(ApplicationConstants.NB_CLIENT_CONSUMER));
                    	 }else if(npiArray[8].toString().equalsIgnoreCase("NB_Biz")){
                    		 data.setC30(PropertiesUtil.getProperty(ApplicationConstants.NB_CLIENT_BUSINESS));
                    	 }else{
                    		 data.setC30(npiArray[8].toString());
                    	 }
                    	 data.setDerivedRegion("US");
                    	 if(categoryMap.containsKey(npiArray[8].toString()))
                    	 {
                    		 data.setDerivedCategory(categoryMap.get(npiArray[8].toString()).toString());
                    	 }
                    	 else{
                    		 System.out.println("category not mapped ");
                    	 }
                    	 data.setDerivedProductType("NPI");
					 }
					 
					 session.save(data);
					
				 }
				 session.getTransaction().commit();
			
				return true;
				
			}
		});
		logger.debug("Leaving from load NPI into data table");
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(PlanningDAO.class.toString(), "getOverrideCommitObj", 
					ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
	}

public final List<Object[]> getNpiList(final Integer planningCycleId) throws ApplicationException{
	Object object = null;
	logger.debug("Entered into getnpiList in PlanningDAO");
	try{
		object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				List distinctSKU=null;
				try {
					String productNumber = PropertiesUtil.getProperty("productNumber");
					String business = PropertiesUtil.getProperty("business");
					String orderWeek = PropertiesUtil.getProperty("orderWeek");
					String derivedProductType = PropertiesUtil.getProperty("derivedProductType");
					
					Criteria npiIdCriteria= session.createCriteria(Data.class);
					ProjectionList proj= Projections.projectionList();
					proj.add(Projections.property(productNumber));
					proj.add(Projections.property(business));
					npiIdCriteria.add(Restrictions.eq(derivedProductType,"NPI"));
					npiIdCriteria.add(Restrictions.eq("planningCycleId",planningCycleId));
					npiIdCriteria.setProjection(proj);
					distinctSKU = npiIdCriteria.list();
					/*String sql = "select sl.productId,sl.eolWeek,sl.eolYear,sm.business FROM SkuList sl, SkuUserMapping sm where sl.id=sm.skuListId";
					Query query = session.createQuery(sql);
					distinctSKU = (List)query.list();*/
				} catch (HibernateException he) {
					logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
				}
				return distinctSKU;
			}
		});
	}catch (Exception e) {
		logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
	}
	logger.debug("Leaving from getnpiList method in PlanningDAO");
	return (List)object;
}

/**
 * 
 * @return
 * @throws ApplicationException
 * Returns list of SKU's from SKU_List Table
 */
public final List<Object[]> getSKUList() throws ApplicationException{
	Object object = null;
	logger.debug("Entered into getSKUList in PlanningDAO");
	try{
		object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				List distinctSKU=null;
				try {
					String sql = "select distinct sl.productId,sl.eolWeek,sl.eolYear,sm.business FROM SkuList sl, SkuUserMapping sm where sl.id=sm.skuListId and sl.productId not like '%NPI%'";
					Query query = session.createQuery(sql);
					distinctSKU = (List)query.list();
				} catch (HibernateException he) {
					logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
				}
				return distinctSKU;
			}
		});
	}catch (Exception e) {
		logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
	}
	logger.debug("Leaving from getSKUList method in PlanningDAO");
	return (List)object;
}

public final void loadCacheData() throws ApplicationException{
	logger.debug("Entered into loadCacheData in PlanningDAO");
	try{
		List pageTemplates = getPageTemplates();
		if(pageTemplates != null){
			Map pageTemplatesCacheObject = new HashMap();
			int size = pageTemplates.size();
			for(int i=0;i<size;i++){
				PageTemplate template = (PageTemplate)pageTemplates.get(i);
				pageTemplatesCacheObject.put(template.getLogicalName(), template);
			}
			applicationCacheObject.put(ApplicationConstants.PAGE_TEMPLATES_CACHE_KEY, pageTemplatesCacheObject);
		}
		List masterErrorCodes = applicationDAO.getMasterErrorCodes();
		if(masterErrorCodes != null){
			Map errorCodesCacheObject = new HashMap();
			int size = masterErrorCodes.size();
			for(int i=0;i<size;i++){
				MasterErrorCodes errorCodes = (MasterErrorCodes)masterErrorCodes.get(i);
				errorCodesCacheObject.put(errorCodes.getErrorCode(), errorCodes.getErrorMessage());
			}
			applicationCacheObject.put(ApplicationConstants.APPLICATION_ERRORS_CACHE_KEY, errorCodesCacheObject);
		}
		
		List productLineList = applicationDAO.getProductLineList();
		applicationCacheObject.put(ApplicationConstants.PRODUCT_LINE_LIST, productLineList);
		
		List productHierarchyIList = applicationDAO.getProductHierarchyI();
		applicationCacheObject.put(ApplicationConstants.PRODUCT_HIERARCHY1_LIST, productHierarchyIList);
		
		List productHierarchyIIList = applicationDAO.getProductHierarchyII();
		applicationCacheObject.put(ApplicationConstants.PRODUCT_HIERARCHY2_LIST, productHierarchyIIList);
		
		List productHierarchyIVList = applicationDAO.getProductHierarchyIV();
		applicationCacheObject.put(ApplicationConstants.PRODUCT_HIERARCHY4_LIST, productHierarchyIVList);
		
		List modeList = applicationDAO.getModelList();
		applicationCacheObject.put(ApplicationConstants.MODEL_LIST, modeList);
		
		List plClassList = applicationDAO.getPlClass();
		applicationCacheObject.put(ApplicationConstants.PL_CLASS_LIST, plClassList);
		
		List plScorecardList = applicationDAO.getPlScorecard();
		applicationCacheObject.put(ApplicationConstants.PL_SCORECARD_LIST, plScorecardList);
		
		List<Object[]> productManagerList = applicationDAO.getProductManager();
		applicationCacheObject.put(ApplicationConstants.PRODUCT_MANAGER_LIST, productManagerList);
		
		List skuBusiness = applicationDAO.getBusiness();
		applicationCacheObject.put(ApplicationConstants.SKU_BUSINESS, skuBusiness);
			
		List skuRegion = applicationDAO.getRegion();
		applicationCacheObject.put(ApplicationConstants.SKU_REGION, skuRegion);
		
		List<Object[]> productManagerUserIdList = utilitiesService.productManagerDetails();
		applicationCacheObject.put(ApplicationConstants.PRODUCT_MANAGER_USERID_LIST, productManagerUserIdList);
		List userList = applicationDAO.getUsersList();
		List actAsUserList = new ArrayList();
		if(userList != null){
			Map userMap = new HashMap();
			int size =userList.size();
			for(int i=0;i<size;i++){
				Users users = (Users)userList.get(i);
				List roles = users.getRoles();
				List rolesNamesList = new ArrayList();
				if (roles != null) {
					int size1 = roles.size();
					for (int j = 0; j< size1; j++) {
						Roles roles2 = (Roles) roles.get(j);
						rolesNamesList.add(roles2.getRole());
					}
				}
				if(!rolesNamesList.contains(ApplicationConstants.ADMIN) 
						&& !rolesNamesList.contains(ApplicationConstants.CATEGORY_DIRECTOR)
						&& !rolesNamesList.contains(ApplicationConstants.FINANCE_DIRECTOR)){
					users.setRolesList(rolesNamesList);
					userMap.put(users.getLogin_id()+"", users);
					actAsUserList.add(users);
				}
			}
			applicationCacheObject.put(ApplicationConstants.APPLICATION_USERS_LIST, actAsUserList);
			applicationCacheObject.put(ApplicationConstants.APPLICATION_USERS_MAP, userMap);
		}
		
		List<Object[]> masterEventCalendarList = applicationDAO.getMasterEventCalendar();
		if(masterEventCalendarList !=null){
			Map eventCalendarMapObj = new HashMap();
			String weekYearSeparator=PropertiesUtil.getProperty(ApplicationConstants.WEEK_YEAR_SEPARATOR);
			int size = masterEventCalendarList.size();
			for(int i=0;i<size;i++){
				Object obj[] = masterEventCalendarList.get(i);
				String fiscalWeek = (String)obj[0];
				String business = (String)obj[1];
				String eventName = (String)obj[2];
				int endIndex=16;
				if(eventName.length()<endIndex){
					endIndex=eventName.length();
				}
				String tempEventName=eventName.substring(0, endIndex)+"....";
				String weekBusiness = fiscalWeek+business;
				String toolTip=fiscalWeek+business+"1";
				eventCalendarMapObj.put(weekBusiness, tempEventName);
				eventCalendarMapObj.put(toolTip, eventName);
			}
			applicationCacheObject.put(ApplicationConstants.EVENT_CALENDAR_MAP, eventCalendarMapObj);
		}
		
		List masterCommitStatusList = applicationDAO.getMasterCommitStatusList();
		if(masterCommitStatusList != null){
			Map masterCommitStatusIdMap = new HashMap();
			Map masterCommitStatusNameMap = new HashMap();
			int size = masterCommitStatusList.size();
			for(int i=0;i<size;i++){
				MasterCommitStatus masterCommitStatus = (MasterCommitStatus)masterCommitStatusList.get(i);
				masterCommitStatusIdMap.put(masterCommitStatus.getLogicalName(), masterCommitStatus.getId()+"");
				masterCommitStatusNameMap.put(masterCommitStatus.getId()+"", masterCommitStatus.getStatus());
			}
			applicationCacheObject.put(ApplicationConstants.MASTER_COMMIT_STATUS_ID_MAP, masterCommitStatusIdMap);
			applicationCacheObject.put(ApplicationConstants.MASTER_COMMIT_STATUS_NAME_MAP, masterCommitStatusNameMap);
		}
		

		List categoryList = applicationDAO.getCategoryFromScorecardRollup();
		if(categoryList != null){
			Map categoryScorecardRollupMap = new HashMap();
			int size = categoryList.size();
			for(int i=0;i<size;i++){
				Object [] categoryArray = (Object[]) categoryList.get(i);
				categoryScorecardRollupMap.put(categoryArray[0], categoryArray[1]);
			}
			applicationCacheObject.put(ApplicationConstants.CATEGORY_SCORECARD_ROLLUP_MAPPING, categoryScorecardRollupMap);
		}
	}catch (Exception e) {
		ApplicationException ae = ApplicationException
				.createApplicationException("PlanningDAO",
						"loadCacheData",
						ApplicationErrorCodes.APP_EC_25,
						ApplicationConstants.EXCEPTION, null);
		logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
		throw e;
	}
	LoadApplicationCacheService.applicationCacheObject.keySet().removeAll(LoadApplicationCacheService.applicationCacheObject.keySet());
	LoadApplicationCacheService.applicationCacheObject.putAll(applicationCacheObject);
	logger.debug("Leaving from loadCacheData method in PlanningDAO");
}

public void savePlanningLogUserSession(final Integer userId,final String skuList) throws ApplicationException{
	Object object = null;
	logger.debug("Entered into savePlanningLogUserSession in PlanningDAO");
	try{
		object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				List distinctSKU=null;
				try {
					session.beginTransaction();
					List<String> skuUserList = new ArrayList<String>(Arrays.asList(skuList.split(",")));
					for(int i=0;i<skuUserList.size();i++){
						PlanningLogUserSessions planningLogSession=new PlanningLogUserSessions();
						planningLogSession.setUserId(userId.toString());
						planningLogSession.setProductId(skuUserList.get(i));
						session.save(planningLogSession);
					}
					session.getTransaction().commit();
					
				} catch (HibernateException he) {
					logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
					session.getTransaction().rollback();
				}
				return true;
			}
		});
	}catch (Exception e) {
		logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
	}
	logger.debug("Leaving from savePlanningLogUserSession method in PlanningDAO");
}

public List getPlanningLogUserSession(final Integer userId) throws ApplicationException{
	Object object = null;
	logger.debug("Entered into getPlanningLogUserSession in PlanningDAO");
	try{
		
		object = hibernateTemplate.execute(new HibernateCallback() {
			List skuList=null;
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
						String sql="select productId from PlanningLogUserSessions where userId="+userId+"";
						Query query=session.createQuery(sql);
						skuList=query.list();
				} catch (HibernateException he) {
					he.printStackTrace();
					logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
				}
				return skuList;
			}
		});
	}catch (Exception e) {
		logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
	}
	logger.debug("Leaving from savePlanningLogUserSession method in PlanningDAO");
	return (List)object;
}

public void clearPlanningLogUserSession(final Integer userId) throws ApplicationException{
	Object object = null;
	logger.debug("Entered into getPlanningLogUserSession in PlanningDAO");
	try{
		
		object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
						session.beginTransaction();
						String sql="delete from PlanningLogUserSessions where userId="+userId+"";
						Query query=session.createQuery(sql);
						query.executeUpdate();
						session.getTransaction().commit();
				} catch (HibernateException he) {
					session.getTransaction().rollback();
					he.printStackTrace();
					logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
				}
				return true;
			}
		});
	}catch (Exception e) {
		logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
	}
	logger.debug("Leaving from clearPlanningLogUserSession method in PlanningDAO");
}

}
