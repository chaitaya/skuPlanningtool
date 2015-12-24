package com.bridgei2i.common.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.util.StringUtils;

import com.bi2i.login.EncryptService;
import com.bridgei2i.common.constants.ApplicationConstants;
import com.bridgei2i.common.constants.ApplicationErrorCodes;
import com.bridgei2i.common.controller.LoadApplicationCacheService;
import com.bridgei2i.common.exception.ApplicationException;
import com.bridgei2i.common.util.ApplicationUtil;
import com.bridgei2i.common.util.CsvReaderWriter;
import com.bridgei2i.common.util.PropertiesUtil;
import com.bridgei2i.common.vo.Data;
import com.bridgei2i.common.vo.DropDownDisplayVo;
import com.bridgei2i.common.vo.Roles;
import com.bridgei2i.common.vo.Users;
import com.bridgei2i.form.WorkspaceBean;
import com.bridgei2i.vo.BannerMetaData;
import com.bridgei2i.vo.BannerMetaData2;
import com.bridgei2i.vo.Comments;
import com.bridgei2i.vo.Contact;
import com.bridgei2i.vo.MasterDistributionType;
import com.bridgei2i.vo.MasterEventCalendar;
import com.bridgei2i.vo.MasterReportStatus;
import com.bridgei2i.vo.RawData;
import com.bridgei2i.vo.TemplateChart;
import com.bridgei2i.vo.TemplateChartCategory;
import com.bridgei2i.vo.TemplateChartInfo;
import com.bridgei2i.vo.TemplateChartReportAssign;
import com.bridgei2i.vo.VariableNames;

import edu.emory.mathcs.backport.java.util.Arrays;

public class ApplicationDAO {

	private HibernateTemplate hibernateTemplate;
	private static Logger logger = Logger.getLogger(ApplicationDAO.class);
	public static int rowIndex=0;
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public Object saveContact(final Contact contact) throws ApplicationException{
		Object object = null;
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
						session.beginTransaction();
						session.save(contact);
						session.getTransaction().commit();
					} catch (HibernateException he) {
						session.getTransaction().rollback();
					}
					return true;
				}
			});
		}catch (Exception e) {
			ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "saveContact", 
					ApplicationErrorCodes.APP_EC_2, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
		return object;
	}
	
	public Object saveDesignReportTemplate(final TemplateChart templateChart,final String[] selectedDistributionIds) throws ApplicationException{
      
    	  Object object = null;
        logger.debug("Entered into saveDesignReportTemplate");
        try{
                object = hibernateTemplate.execute(new HibernateCallback() {
                        public Object doInHibernate(Session session)
                                        throws HibernateException, SQLException {
                                try {
                                        session.beginTransaction();
                                        //update the template chart
                                        if(templateChart.getId() != null && templateChart.getId().intValue()>0){
                                        	List templatechartCategoriesList  = templateChart.getTemplateChartCategories();
                                        	templateChart.setTemplateChartCategories(null);
                                        	session.update(templateChart);
                                    	if(templatechartCategoriesList != null){
                                            for(int i=0;i<templatechartCategoriesList.size();i++){
                                            	TemplateChartCategory templateChartCategory =(TemplateChartCategory) templatechartCategoriesList.get(i);
                                            	// update category
                                            	if(templateChartCategory.getId() != null && templateChartCategory.getId().intValue()>0){
                                            		List templatechartInfoList  = templateChartCategory.getTemplateChartInfoList();
                                            		templateChartCategory.setTemplateChartInfoList(null);
                                            		session.update(templateChartCategory);
                                            		List templatechartInfoList1 = new ArrayList();
                                            		if(templatechartInfoList != null){
	                                               	 for(int j=0;j<templatechartInfoList.size();j++){
	                                               		TemplateChartInfo templateChartInfo=(TemplateChartInfo) templatechartInfoList.get(j);
	                                               		if(templateChartInfo.isDeleted() && templateChartInfo.getId() != null && templateChartInfo.getId().intValue()>0){
	                                               			session.delete(templateChartInfo);
	                                               		}else if(templateChartInfo.getId() != null && templateChartInfo.getId().intValue()>0){
	                                               			session.update(templateChartInfo);
	                                               		}else if(!templateChartInfo.isDeleted()){
	                                               			templateChartInfo.setTemplateChartCategory(templateChartCategory);
	                                               			Integer functionId = templateChartInfo.getFunctionId();
	                                               			if(ApplicationUtil.isEmptyOrNull(functionId) || functionId.intValue()==-1){
	                                               				templateChartInfo.setFunctionId(null);
	                                               			}
	                                               			session.save(templateChartInfo);
	                                               		}
	                                               		if(!templateChartInfo.isDeleted()){
	                                               			templatechartInfoList1.add(templateChartInfo);
	                                               		}
	                                               	 }
                                            	}
	                                               	templateChartCategory.setTemplateChartInfoList(templatechartInfoList1);
                                            	}else{
                                            		// insert category
                                            		templateChartCategory.setTemplateChart(templateChart);
                                            		 session.save(templateChartCategory);
                                            		 List templatechartInfoList  = templateChartCategory.getTemplateChartInfoList();
                                             		templateChartCategory.setTemplateChartInfoList(null);
                                             		session.update(templateChartCategory);
                                             		List templatechartInfoList1 = new ArrayList();
                                             		if(templatechartInfoList!=null){
	 	                                               	 for(int j=0;j<templatechartInfoList.size();j++){
	 	                                               		TemplateChartInfo templateChartInfo=(TemplateChartInfo) templatechartInfoList.get(j);
	 	                                               		if(!templateChartInfo.isDeleted()){
		 	                                               		Integer functionId = templateChartInfo.getFunctionId();
		                                               			if(ApplicationUtil.isEmptyOrNull(functionId) || functionId.intValue()==-1){
		                                               				templateChartInfo.setFunctionId(null);
		                                               			}
	 	                                               			templateChartInfo.setTemplateChartCategory(templateChartCategory);
	 	                                               			session.save(templateChartInfo);
	 	                                               		templatechartInfoList1.add(templateChartInfo);
	 	                                               		}
	 	                                               	 }
                                             		}
 	                                               	templateChartCategory.setTemplateChartInfoList(templatechartInfoList1);
                                            	}
                                            }
                                        }
                                            // re-insert distribution list on template chart update
                                            if(selectedDistributionIds != null && selectedDistributionIds.length>0){
                                            	 String sql = "delete from TemplateChartReportAssign where templateChartid ="+templateChart.getId().intValue();
                        	        			 	Query q = session.createQuery(sql);
                        	        			 	q.executeUpdate();
                                    			for(int k=0;k<selectedDistributionIds.length;k++){
                                        			TemplateChartReportAssign templateChartReportAssign= new TemplateChartReportAssign();
                                        			templateChartReportAssign.setMasterDistId(new Integer(selectedDistributionIds[k]));
                                        			templateChartReportAssign.setTemplateChart(templateChart);
                                        			session.save(templateChartReportAssign);
                                    			}
                                    		} 
                                            templateChart.setTemplateChartCategories(templatechartCategoriesList);
                                        }else{
                                        	// creating new template chart with categories and chart info
                                        	List templatechartCategoriesList  = templateChart.getTemplateChartCategories();
                                        	templateChart.setTemplateChartCategories(null);
                                        	session.save(templateChart);
                                        	if(templatechartCategoriesList!=null){
	                                        	for(int i=0;i<templatechartCategoriesList.size();i++){
	                                            	TemplateChartCategory templateChartCategory =(TemplateChartCategory) templatechartCategoriesList.get(i);
	                                            	List templatechartInfoList  = templateChartCategory.getTemplateChartInfoList();
	                                        		templateChartCategory.setTemplateChartInfoList(null);
	                                        		templateChartCategory.setTemplateChart(templateChart);
	                                        		session.save(templateChartCategory);
	                                        		List templatechartInfoList1 = new ArrayList();
	                                        		if(templatechartInfoList != null){
		                                               	 for(int j=0;j<templatechartInfoList.size();j++){
		                                               		TemplateChartInfo templateChartInfo=(TemplateChartInfo) templatechartInfoList.get(j);
		                                               		if(!templateChartInfo.isDeleted()){
		                                               			templateChartInfo.setTemplateChartCategory(templateChartCategory);
		                                               			Integer functionId = templateChartInfo.getFunctionId();
		                                               			if(ApplicationUtil.isEmptyOrNull(functionId) || functionId.intValue()==-1){
		                                               				templateChartInfo.setFunctionId(null);
		                                               			}
		                                               			session.save(templateChartInfo);
		                                               			templatechartInfoList1.add(templateChartInfo);
		                                               		}
		                                               	 }
	                                        		}
	                                               	templateChartCategory.setTemplateChartInfoList(templatechartInfoList1);
	                                            }
                                        	}
                                        	if(selectedDistributionIds != null && selectedDistributionIds.length>0){
                                    			for(int k=0;k<selectedDistributionIds.length;k++){
                                        			TemplateChartReportAssign templateChartReportAssign= new TemplateChartReportAssign();
                                        			templateChartReportAssign.setMasterDistId(new Integer(selectedDistributionIds[k]));
                                        			templateChartReportAssign.setTemplateChart(templateChart);
                                        			session.save(templateChartReportAssign);
                                    			}
                                    		} 
                                        	templateChart.setTemplateChartCategories(templatechartCategoriesList);
                                        }
                                        if(templateChart.getReportType() !=null && templateChart.getReportType().equals("U")){
                                        	String sql = "delete from TemplateChartReportAssign where templateChartid ="+templateChart.getId().intValue();
                	        			 	Query q = session.createQuery(sql);
                	        			 	q.executeUpdate();
                                        }
                                        session.getTransaction().commit();
                                } catch (HibernateException he) {
                                        session.getTransaction().rollback();
                                }
                                return true;
                        }
                });
        }catch (Exception e) {
        	logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
                ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "saveDesignReportTemplate", 
                                ApplicationErrorCodes.APP_EC_21, ApplicationConstants.EXCEPTION, e);
                throw applicationException;
        }
        logger.debug("Leaving from saveDesignReportTemplate");
        return object; 
	
}

		public void deleteDesignReportTemplate(final List templateChartList) throws ApplicationException{
			Object object = null;
			logger.debug("Entered into deleteDesignReportTemplate");
			try{
				object = hibernateTemplate.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						try {
							session.beginTransaction();
							if(templateChartList != null){
								for(int i=0; i<templateChartList.size(); i++){
									TemplateChart templateChart =(TemplateChart)templateChartList.get(i); 
								    session.delete(templateChart);
								}
							}
							session.getTransaction().commit();
						} catch (HibernateException he) {
							session.getTransaction().rollback();
						}
						return true;
					}
				});
			}catch (Exception e) {
				logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
				ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "deleteDesignReportTemplate", 
						ApplicationErrorCodes.APP_EC_15, ApplicationConstants.EXCEPTION, e);
				throw applicationException;
			}
			logger.debug("Leaving from deleteDesignReportTemplate");
			
		}
		
		public void reviewRequest(final List requestReviewList) throws ApplicationException{
				Object object = null;
			
			logger.debug("Entered into reviewRequest");
			try{
				object = hibernateTemplate.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						try {
							session.beginTransaction();
							if(requestReviewList!=null){
								for(int i=0; i<requestReviewList.size(); i++){
									Comments comments = (Comments)requestReviewList.get(i);
								    session.save(comments);
								}
							}
							session.getTransaction().commit();
						} catch (HibernateException he) {
							session.getTransaction().rollback();
						}
						return true;
					}
				});
			}catch (Exception e) {
				logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
				ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "reviewRequest", 
						ApplicationErrorCodes.APP_EC_19, ApplicationConstants.EXCEPTION, e);
				throw applicationException;
			}
			logger.debug("Leaving from reviewRequest");
			
		}
		
		public void saveDashboardTemplate(final Comments comments,final TemplateChart templateChart) throws ApplicationException{
			Object object = null;
			logger.debug("Entered into saveDashboardTemplate");
			try{
				object = hibernateTemplate.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						try {
							session.beginTransaction();
								session.update(templateChart);
								session.save(comments);
							session.getTransaction().commit();
						} catch (HibernateException he) {
							session.getTransaction().rollback();
						}
						return true;
					}
				});
			}catch (Exception e) {
				logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
				ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "saveDashboardTemplate", 
						ApplicationErrorCodes.APP_EC_8, ApplicationConstants.EXCEPTION, e);
				throw applicationException;
			}
			logger.debug("Leaving from saveDashboardTemplate");
		}
	
		public final List getProductLineList() throws ApplicationException{
			Object object = null;
			logger.debug("Entered into getSKUList in ApplicationDAO");
			try{
				
				object = hibernateTemplate.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						List distinctProductLine=null;
						String productLineColumn = PropertiesUtil.getProperty("productLine");
						try {
							Criteria distinctProductLineCriteria = session.createCriteria(Data.class);
							distinctProductLineCriteria.add(Restrictions.isNotNull(productLineColumn));
							distinctProductLineCriteria.add(Restrictions.not(Restrictions.eq(productLineColumn,"")));
							ProjectionList distinctProductLineProjection= Projections.projectionList();
							distinctProductLineProjection.add(Projections.distinct(Projections.property(productLineColumn)));
							distinctProductLineCriteria.setProjection(distinctProductLineProjection);
							distinctProductLine = distinctProductLineCriteria.list();
							
						} catch (HibernateException he) {
							throw he;
						}
						return distinctProductLine;
					}
				});
			}catch (Exception e) {
				logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			}
			logger.debug("Leaving from getSKUList method in ApplicationDAO");
			return (List)object;
		}
		
		
		public final List getProductHierarchyI() throws ApplicationException{
			Object object = null;
			logger.debug("Entered into getProductHierarchyI in ApplicationDAO");
			try{
				
				object = hibernateTemplate.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						String productHierarchyI = PropertiesUtil.getProperty("productHierarchyI");
						List distinctProductHierarchyI=null;
						try {
							Criteria distinctProductHierarchyICriteria = session.createCriteria(Data.class);
							distinctProductHierarchyICriteria.add(Restrictions.isNotNull(productHierarchyI));
							distinctProductHierarchyICriteria.add(Restrictions.not(Restrictions.eq(productHierarchyI,"")));
							ProjectionList distinctProductHierarchyIProjection= Projections.projectionList();
							distinctProductHierarchyIProjection.add(Projections.distinct(Projections.property(productHierarchyI)));
							distinctProductHierarchyICriteria.setProjection(distinctProductHierarchyIProjection);
							distinctProductHierarchyI = distinctProductHierarchyICriteria.list();
							
						} catch (HibernateException he) {
							throw he;
						}
						return distinctProductHierarchyI;
					}
				});
			}catch (Exception e) {
				logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			}
			logger.debug("Leaving from getProductHierarchyI method in ApplicationDAO");
			return (List)object;
		}
		
		public final List getProductHierarchyII() throws ApplicationException{
			Object object = null;
			logger.debug("Entered into getProductHierarchyII in ApplicationDAO");
			try{
				
				object = hibernateTemplate.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						List distinctProductHierarchyII=null;
						String productHierarchyII = PropertiesUtil.getProperty("productHierarchyII");
						try {
							Criteria distinctProductHierarchyIICriteria = session.createCriteria(Data.class);
							distinctProductHierarchyIICriteria.add(Restrictions.isNotNull(productHierarchyII));
							distinctProductHierarchyIICriteria.add(Restrictions.not(Restrictions.eq(productHierarchyII,"")));
							ProjectionList distinctProductHierarchyIIProjection= Projections.projectionList();
							distinctProductHierarchyIIProjection.add(Projections.distinct(Projections.property(productHierarchyII)));
							distinctProductHierarchyIICriteria.setProjection(distinctProductHierarchyIIProjection);
							distinctProductHierarchyII = distinctProductHierarchyIICriteria.list();
						} catch (HibernateException he) {
							throw he;
						}
						return distinctProductHierarchyII;
					}
				});
			}catch (Exception e) {
				logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			}
			logger.debug("Leaving from getProductHierarchyII method in ApplicationDAO");
			return (List)object;
		}
		
		public final List getModelList() throws ApplicationException{
			Object object = null;
			logger.debug("Entered into getModelList in ApplicationDAO");
			try{
				object = hibernateTemplate.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						List modelList=null;
						try {
							String modelName=PropertiesUtil.getProperty(ApplicationConstants.MODEL);
							Criteria distinctProductHierarchyIICriteria = session.createCriteria(Data.class);
							distinctProductHierarchyIICriteria.add(Restrictions.not(Restrictions.eq(modelName,"")));
							distinctProductHierarchyIICriteria.add(Restrictions.isNotNull(modelName));
							ProjectionList distinctProductHierarchyIIProjection= Projections.projectionList();
							distinctProductHierarchyIIProjection.add(Projections.distinct(Projections.property(modelName)));
							distinctProductHierarchyIICriteria.setProjection(distinctProductHierarchyIIProjection);
							modelList = distinctProductHierarchyIICriteria.list();
							
						} catch (HibernateException he) {
							throw he;
						}
						return modelList;
					}
				});
			}catch (Exception e) {
				logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			}
			logger.debug("Leaving from getModelList method in ApplicationDAO");
			return (List)object;
		}
		
		public final List getProductHierarchyIV() throws ApplicationException{
			Object object = null;
			logger.debug("Entered into getProductHierarchyI in ApplicationDAO");
			try{
				
				object = hibernateTemplate.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						List distinctProductHierarchyIV=null;
						String productHierarchyIV = PropertiesUtil.getProperty("productHierarchyIV");
						try {
							Criteria distinctProductHierarchyIVCriteria = session.createCriteria(Data.class);
							distinctProductHierarchyIVCriteria.add(Restrictions.not(Restrictions.eq(productHierarchyIV,"")));
							distinctProductHierarchyIVCriteria.add(Restrictions.isNotNull(productHierarchyIV));
							ProjectionList distinctProductHierarchyIVProjection= Projections.projectionList();
							distinctProductHierarchyIVProjection.add(Projections.distinct(Projections.property(productHierarchyIV)));
							distinctProductHierarchyIVCriteria.setProjection(distinctProductHierarchyIVProjection);
							distinctProductHierarchyIV = distinctProductHierarchyIVCriteria.list();
							
						} catch (HibernateException he) {
							throw he;
						}
						return distinctProductHierarchyIV;
					}
				});
			}catch (Exception e) {
				logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			}
			logger.debug("Leaving from getProductHierarchyI method in ApplicationDAO");
			return (List)object;
		}
		
		public final List getPlClass() throws ApplicationException{
			Object object = null;
			logger.debug("Entered into getPlClass in ApplicationDAO");
			try{
				
				object = hibernateTemplate.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						String plclass = PropertiesUtil.getProperty("plclass");
						List distinctPlClass=null;
						try {
							Criteria distinctPlClassCriteria = session.createCriteria(Data.class);
							distinctPlClassCriteria.add(Restrictions.isNotNull(plclass));
							distinctPlClassCriteria.add(Restrictions.not(Restrictions.eq(plclass,"")));
							ProjectionList distinctPlClassProjection= Projections.projectionList();
							distinctPlClassProjection.add(Projections.distinct(Projections.property(plclass)));
							distinctPlClassCriteria.setProjection(distinctPlClassProjection);
							distinctPlClass = distinctPlClassCriteria.list();
							
						} catch (HibernateException he) {
							throw he;
						}
						return distinctPlClass;
					}
				});
			}catch (Exception e) {
				logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			}
			logger.debug("Leaving from getPlClass method in ApplicationDAO");
			return (List)object;
		}
		
		public final List getPlScorecard() throws ApplicationException{
			Object object = null;
			logger.debug("Entered into getPlScorecard in ApplicationDAO");
			try{
				
				object = hibernateTemplate.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						String scorecardRollup = PropertiesUtil.getProperty("scorecardRollup");
						List distinctPlScorecard=null;
						try {
							Criteria distinctPlScorecardCriteria = session.createCriteria(Data.class);
							distinctPlScorecardCriteria.add(Restrictions.not(Restrictions.eq(scorecardRollup,"")));
							distinctPlScorecardCriteria.add(Restrictions.isNotNull(scorecardRollup));
							ProjectionList distinctPlScorecardProjection= Projections.projectionList();
							distinctPlScorecardProjection.add(Projections.distinct(Projections.property(scorecardRollup)));
							distinctPlScorecardCriteria.setProjection(distinctPlScorecardProjection);
							distinctPlScorecard = distinctPlScorecardCriteria.list();
							
						} catch (HibernateException he) {
							throw he;
						}
						return distinctPlScorecard;
					}
				});
			}catch (Exception e) {
				logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			}
			logger.debug("Leaving from getPlClass method in ApplicationDAO");
			return (List)object;
		}
		
		public final List<Object[]> getProductManager() throws ApplicationException{
			Object object = null;
			logger.debug("Entered into getPlScorecard in ApplicationDAO");
			try{
				object = hibernateTemplate.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						List distinctProductManager=null;
						try {
							Criteria distinctProductManagerCriteria = session.createCriteria(Users.class);
							distinctProductManagerCriteria.add(Restrictions.not(Restrictions.eq("firstName","")));
							distinctProductManagerCriteria.add(Restrictions.isNotNull("userName"));
							ProjectionList distinctProductManagerProjection= Projections.projectionList();
							distinctProductManagerProjection.add(Projections.distinct(Projections.property("firstName")));
							distinctProductManagerProjection.add(Projections.property("lastName"));
							distinctProductManagerCriteria.setProjection(distinctProductManagerProjection);
							distinctProductManager = distinctProductManagerCriteria.list();
							
						} catch (HibernateException he) {
							throw he;
						}
						return distinctProductManager;
					}
				});
			}catch (Exception e) {
				logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			}
			logger.debug("Leaving from getPlClass method in ApplicationDAO");
			return (List)object;
		}
		
		public final List<Object[]> getMasterEventCalendar() throws ApplicationException{
			Object object = null;
			logger.debug("Entered into getMasterEventCalendar");
			try{
				object = hibernateTemplate.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						List masterEventCalendarList=null;
						try {
							String sql="select fiscalWeek,business, group_concat(eventName) from master_event_calender group by fiscalWeek,business";
							SQLQuery q = session.createSQLQuery(sql);
							masterEventCalendarList = (List)q.list();
						} catch (HibernateException he) {
							throw he;
						}
						return masterEventCalendarList;
					}
				});
			}catch (Exception e) {
				logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			}
			logger.debug("Leaving from getMasterEventCalendar method in ApplicationDAO");
			return (List)object;
		}
		
	public void saveVariableNames(final List variableNameList) throws ApplicationException{
		Object object = null;
		logger.debug("Entered into saveVariableNames");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
					    	try {					    		session.beginTransaction();
					    		if(variableNameList!=null){
						    		for(int i=0; i<variableNameList.size(); i++){
							    		VariableNames variableNames =(VariableNames)variableNameList.get(i); 
									    session.save(variableNames);
						    		}
					    		}
							    session.getTransaction().commit();
							} catch (Exception e) {
								// TODO: handle exception
							}
					    List variableNameObjList = getVariable();
						ApplicationUtil.loadVariableNamesCache(variableNameObjList);
					} catch (HibernateException he) {
						System.out.println(he);
					} catch (ApplicationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return true;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "saveVariableNames", 
					ApplicationErrorCodes.APP_EC_9, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
		logger.debug("Leaving from saveVariableNames");
	}
		

	public void saveData(final String filePath,final String dbTable,final Integer referenceId,final String referenceColumnName,final boolean deleteTableDataBeforeLoad, final int planningCycleId) throws ApplicationException{
		Object object = null;
		logger.debug("Entered into saveData");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
						 Connection connection = session.connection();
						 session.beginTransaction();
                    	 
						 CsvReaderWriter csvReaderWriter = new CsvReaderWriter(connection);
						 ArrayList<String[]> dataFromExcel=csvReaderWriter.readWriteCSV(filePath, dbTable, deleteTableDataBeforeLoad ,referenceId,referenceColumnName,1);
						 
						 int size=dataFromExcel.size();
						 BigDecimal temp=null;
						 
						 for(int i=0;i<size;i++)
						 {
							 RawData rawdata=new RawData();
							 String [] nextLine=dataFromExcel.get(i);
							 if(nextLine!=null)
							 {
								 rawdata.setPlanningCycleId(planningCycleId);
								 rawdata.setC1(nextLine[0]);
								 rawdata.setC2(nextLine[1]);
								 rawdata.setC3(nextLine[2]);
								 rawdata.setC4(nextLine[3]);
								 rawdata.setC5(nextLine[4]);
                            	 rawdata.setC6(nextLine[5]);
                            	 rawdata.setC7(nextLine[6]);
                            	 rawdata.setC8(nextLine[7]);
                            	 rawdata.setC9(nextLine[8]);
                            	 rawdata.setC10(nextLine[9]);
                            	 rawdata.setC11(nextLine[10]);
                            	 if(checkIfNull(nextLine[11]))
                            	 {
                            		 temp=new BigDecimal(nextLine[11]);
                            		 rawdata.setC12(temp);
                            	 }
                            	 if(checkIfNull(nextLine[12]))
                            	 {
                            		 temp=new BigDecimal(nextLine[12]);
                            		 rawdata.setC13(temp);
                            	 }
                            	 if(checkIfNull(nextLine[13]))
                            	 {
                            		 rawdata.setC14(Integer.parseInt(nextLine[13]));
                            	 }
                            	 if(checkIfNull(nextLine[14]))
                            	 {
                            		 rawdata.setC15(Integer.parseInt(nextLine[14]));
                            	 }
								 if(checkIfNull(nextLine[15]))
                            	 {
                            		 temp=new BigDecimal(nextLine[15]);
                            		 rawdata.setC16(temp);
                            	 }
								 if(checkIfNull(nextLine[16]))
                            	 {
                            		 temp=new BigDecimal(nextLine[16]);
                            		 rawdata.setC17(temp);
                            	 }
								 if(checkIfNull(nextLine[17]))
                            	 {
                            		 temp=new BigDecimal(nextLine[17]);
                            		 rawdata.setC18(temp);
                            	 }
                            	 rawdata.setC19(nextLine[18]);
                            	 rawdata.setC20(nextLine[19]);
                            	 rawdata.setC21(nextLine[20]);
                            	 rawdata.setC22(nextLine[21]);
                            	 rawdata.setC23(nextLine[22]);
                            	 rawdata.setC24(nextLine[23]);
                            	 if(checkIfNull(nextLine[24]))
                            	 {
                            		 temp=new BigDecimal(nextLine[24]);
                            		 rawdata.setC25(temp);
                            	 }
                            	 rawdata.setC26(nextLine[25]);
                            	 rawdata.setC27(nextLine[26]);
                            	 rawdata.setC28(nextLine[27]);
                            	 rawdata.setC29(nextLine[28]);
                            	 rawdata.setC30(nextLine[29]);
                            	 rawdata.setC31(nextLine[30]);
							 }
							 
							 session.save(rawdata);
							
						 }
						 session.getTransaction().commit();
					} catch (HibernateException he) {
						session.getTransaction().rollback();
						throw he;
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
						session.getTransaction().rollback();
						throw new HibernateException("Error while inserting data into table");
					}
					return true;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "saveData", 
					ApplicationErrorCodes.APP_EC_12, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
		logger.debug("Leaving from saveData");
	}
	
	public boolean checkIfNull(String inputStr)
	{
		if(ApplicationUtil.isEmptyOrNull(inputStr))
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
							 size=variableNamesString.length;
							 
							 for(int i=1;i<=size+2;i++)
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
									 variableNames.setVariableName("C"+(i-2));
									 variableNames.setColumnName(variableNamesString[i-3]);
									 variableNames.setCreatedDate(new Date());
									 variableNames.setUpdatedDate(new Date());
									 if(i==3||(i>6 && i<14)||i==16||(i>20&&i<27)||(i>27&&i<34))
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
						session.getTransaction().rollback();
						throw he;
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
						session.getTransaction().rollback();
						throw new HibernateException("Error while inserting data into table");
					}
					return true;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "saveData", 
					ApplicationErrorCodes.APP_EC_12, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
		logger.debug("Leaving from saveData");
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
				
				sql1+=sb.toString()+" "+"FROM RawData r GROUP BY r.C1,r.C9,r.C28 ORDER BY r.C1";
				Query q2 = session.createQuery(sql1);
				
				List<Object []> listResult =q2.list();

				for (Object[] inputArray : listResult) {
						
					    Data actualdata=new Data();
						int length=inputArray.length;
						String [] nextLine=new String[length];
						for(int i=0;i<length;i++)
						{
							if(ApplicationUtil.isEmptyOrNull(inputArray[i])){
								nextLine[i]=null;
							}
							else
							{
								try {
									nextLine[i]=inputArray[i].toString();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						 actualdata.setPlanningCycleId(planningCycleId);
						 actualdata.setC1(nextLine[0]);
						 actualdata.setC5(nextLine[1]);
			           	 actualdata.setC6(nextLine[2]);
			           	 actualdata.setC7(nextLine[3]);
			           	 actualdata.setC8(nextLine[4]);
			           	 actualdata.setC9(nextLine[5]);
			           	 actualdata.setC10(nextLine[6]);
			           	 actualdata.setC11(nextLine[7]);
			           	 actualdata.setC14(nextLine[8]);
			           	 actualdata.setC15(nextLine[9]);
						 actualdata.setC17(nextLine[10]);
						 actualdata.setC18(nextLine[11]);
			           	 actualdata.setC19(nextLine[12]);
			           	 actualdata.setC20(nextLine[13]);
			           	 actualdata.setC21(nextLine[14]);
			           	 actualdata.setC22(nextLine[15]);
			           	 actualdata.setC23(nextLine[16]);
			           	 actualdata.setC24(nextLine[17]);
			           	 actualdata.setC25(nextLine[18]);
			           	 actualdata.setC26(nextLine[19]);
			           	 actualdata.setC27(nextLine[20]);
			           	 actualdata.setC28(nextLine[21]);
			           	 actualdata.setC29(nextLine[22]);
			           	 actualdata.setC30(nextLine[23]);
			           	 actualdata.setC31(nextLine[24]);
			           	 session.save(actualdata);
				}
			 	session.getTransaction().commit();
			 	return true;
			              
			   }
			 });
			        
			 }catch (Exception e) {
			              logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			              throw e;
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
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "getUserFromUserName", 
					ApplicationErrorCodes.APP_EC_22, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
		
	}
	
	public MasterReportStatus getMasterReportOpenStatus(final String statusLogicalName) throws ApplicationException
	{
		try{
		logger.debug("Entered into getMasterReportOpenStatus");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
					MasterReportStatus masterReportStatus = null;
                		 String sql = "from MasterReportStatus as ms  where ms.logicalName = ?";
	        			 Query q = session.createQuery(sql)
	        			 			.setString(0, StringUtils.trimWhitespace(statusLogicalName));
	        			 masterReportStatus =  (MasterReportStatus)q.uniqueResult();
        			return masterReportStatus;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from getMasterReportOpenStatus");
	    return (MasterReportStatus)object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
		throw e;
		}
	}
	
	
	public List getRolesMailId(final String roleName) throws ApplicationException
	{
		try{
		logger.debug("Entered into getRolesMailId");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				List rolesList = null;
				try {
                		 String sql = "from Roles as r  where r.role = ?";
                		 Query q = session.createQuery(sql).setString(0, roleName);
	        			 rolesList =  (List)q.list();
        			return rolesList;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from getRolesMailId");
	    return (List)object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
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
                		 String sql = "from MasterErrorCodes where errorCode is not null";
	        			 Query q = session.createQuery(sql);
	        			 MasterErrorCodesList =  (List)q.list();
        			return MasterErrorCodesList;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from getMasterErrorCodes");
	    return (List)object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	}
	
	public List getMasterBenchMarks() throws ApplicationException
	{
		try{
		logger.debug("Entered into getMasterBenchMarks");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				List masterBenchMarkList = null;
				try {
                		 String sql = "from MasterBenchMark";
	        			 Query q = session.createQuery(sql);
	        			 masterBenchMarkList =  (List)q.list();
        			return masterBenchMarkList;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from getMasterBenchMarks");
	    return (List)object;
	}catch (Exception e) {
		logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
		throw e;
	}
	}
	
	public List getVariable() throws ApplicationException 
    {
		try{
		logger.debug("Entered into getVariable");
            Object  object = hibernateTemplate.execute(new HibernateCallback() {
                            public Object doInHibernate(Session session)
                                            throws HibernateException, SQLException {
                                    List variableList = null;
                                    try {
                                    	String sql =null;
                                    
                                    		sql = "from VariableNames";
                                    	
                                             Query q = session.createQuery(sql);
                                             variableList =  (List)q.list();
                                            return variableList;
                                    } catch (HibernateException he) {
                                            throw he;
                                    }
                            }
                    });
        logger.debug("Leaving from getVariable");
        return (List)object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
    }
	
	public List getReportMetadataList() throws ApplicationException 
    {
		try{
		logger.debug("Entered into getReportMetadata");
            Object  object = hibernateTemplate.execute(new HibernateCallback() {
                            public Object doInHibernate(Session session)
                                            throws HibernateException, SQLException {
                                List reportMetadataList = null;
                                try {
                                	String sql = "select * from reportmetadata";
                                	SQLQuery q = session.createSQLQuery(sql);
    								reportMetadataList =  (List)q.list();
                                    return reportMetadataList;
                                } catch (HibernateException he) {
                                    throw he;
                                }
                            }
                    });
        logger.debug("Leaving from getReportMetadata");
        return (List)object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
    }
    
	public List getSurveyVariables(final String type) throws ApplicationException 
    {
		try{
		logger.debug("Entered into getVariable");
            Object  object = hibernateTemplate.execute(new HibernateCallback() {
                            public Object doInHibernate(Session session)
                                            throws HibernateException, SQLException {
                                    List variableList = null;
                                    try {
                                    	String sql =null;
                                    	if(!ApplicationUtil.isEmptyOrNull(type)){
                                    		sql = "from VariableNames";
                                    	}else{
                                    		sql = "from VariableNames";
                                    	}
                                             Query q = session.createQuery(sql);
                                             variableList =  (List)q.list();
                                            return variableList;
                                    } catch (HibernateException he) {
                                            throw he;
                                    }
                            }
                    });
        logger.debug("Leaving from getVariable");
        return (List)object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
    }
	
	public List getMasterFunctions() throws ApplicationException
	{
		try{
		logger.debug("Entered into getMasterFunctions");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				List masterFunctionList = null;
				try {
                		 String sql = "from MasterFunctions";
	        			 Query q = session.createQuery(sql);
	        			 masterFunctionList =  (List)q.list();
        			return masterFunctionList;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from getMasterFunctions");
	    return (List)object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	}
	
	public List getMasterDistributionTypes(final String overALL) throws ApplicationException
	{
		try{
	
		logger.debug("Entered into getMasterDistributionTypes");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				List masterDistributionTypeList = null;
				try {
					String sql=null;
					
					if(ApplicationUtil.isEmptyOrNull(overALL)){
                		  sql = "from MasterDistributionType";
					}else{
						 sql = "from MasterDistributionType where overall='"+overALL+"'";
					}
	        			 Query q = session.createQuery(sql);
	        			 masterDistributionTypeList =  (List)q.list();
        			return masterDistributionTypeList;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from getMasterDistributionTypes");
	    return (List)object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	}
	
	public List getMasterReportStatus() throws ApplicationException
	{
		try{
		logger.debug("Entered into getMasterReportStatus");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				List masterReportStatusList = null;
				try {
                		 String sql = "from MasterReportStatus";
	        			 Query q = session.createQuery(sql);
	        			 masterReportStatusList =  (List)q.list();
	        			 if(masterReportStatusList != null){
	        				 sql = "from MasterReportStatus where logicalName in ('APPROVED','SEND_FOR_REVIEW')";
		        			 q = session.createQuery(sql);
		        			 masterReportStatusList =  (List)q.list();
	        			 }else{
	        				 sql = "from MasterReportStatus";
		        			 q = session.createQuery(sql);
		        			 masterReportStatusList =  (List)q.list();
	        			 }
        			return masterReportStatusList;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from getMasterReportStatus");
	    return (List)object;
	}catch (Exception e) {
		logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
		throw e;
	}
	}
	
	public List getMasterChartTypes() throws ApplicationException
	{
		try{
		logger.debug("Entered into getMasterChartTypes");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				List masterChartTypeList = null;
				try {
                		 String sql = "from MasterChartType";
	        			 Query q = session.createQuery(sql);
	        			 masterChartTypeList =  (List)q.list();
        			return masterChartTypeList;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from getMasterChartTypes");
	    return (List)object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	
	}
	
	
	public TemplateChart getTemplateChart(final Integer templateChartId) throws ApplicationException
    {
		try{
		logger.debug("Entered into getTemplateChart");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
					TemplateChart templateChart = null;
                		 String sql = "from TemplateChart as tc  where tc.id = ?";
	        			 Query q = session.createQuery(sql)
	        			 			.setInteger(0, templateChartId);
	        			 templateChart =  (TemplateChart)q.uniqueResult();
	        			 sql = "from MasterReportStatus where id=?";
                         q = session.createQuery(sql)
	        			 			.setLong(0, templateChart.getStatusId());
                         MasterReportStatus masterReportStatus =  (MasterReportStatus)q.uniqueResult();
                         templateChart.setStatusName(masterReportStatus.getStatusName());

                        List templateChartReportAssigns =  templateChart.getTemplateChartReportAssigns();
                        if(templateChartReportAssigns != null){
                         int len = templateChartReportAssigns.size();
                         String distIdsStr="";
                    	 	for(int j=0;j<len;j++){
                    	 		TemplateChartReportAssign templateChartReportAssign =  (TemplateChartReportAssign)templateChartReportAssigns.get(j);
                    	 		distIdsStr = distIdsStr+templateChartReportAssign.getMasterDistId();
                    	 		if(j+1<len){
                    	 			distIdsStr= distIdsStr+",";
                    	 		}
                    	 	}
                         sql = "from MasterDistributionType where id in (?)";
                         q = session.createQuery(sql)
	        			 			.setString(0, distIdsStr);
                         List masterDistributionTypeList =  (List)q.list();
                         String name="";
                         if(masterDistributionTypeList != null && masterDistributionTypeList.size()>0){
                       	  int mdSize = masterDistributionTypeList.size();
                       	  for(int k=0;k<mdSize;k++){
                       		  MasterDistributionType distributionType = (MasterDistributionType) masterDistributionTypeList.get(k);
                       		   name = name+distributionType.getTypeName();
                       		   templateChart.setIsOverall(distributionType.getOverAll());
                       		   if(k+1<mdSize){
                       			   name= name+",";
                       		   }
                       	  }
                         }else{
                        	 templateChart.setIsOverall("Y");
                         }
                         templateChart.setAssignReportNames(name);
                        }
        			return templateChart;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from getTemplateChart");
	    return (TemplateChart)object;
    }catch (Exception e) {
    	logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
		ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "getTemplateChart", 
				ApplicationErrorCodes.APP_EC_32, ApplicationConstants.EXCEPTION, e);
		throw applicationException;
	}
    }
	
	public List getReportMetaData(final Integer templateChartId) throws ApplicationException {
		try{
			logger.debug("Entered into getReportMetaData");
			Object object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
							List reportMetaDataList = null;
	                		 String sql = "from ReportMetadata as rmd  where rmd.templatechartId = ?";
		        			 Query q = session.createQuery(sql).setInteger(0, templateChartId);
		        			 reportMetaDataList =  (List)q.list();
	                        
	        			return reportMetaDataList;
					} catch (HibernateException he) {
						throw he;
					}
				}
			});
			logger.debug("Leaving from getReportMetaData");
		    return (List)object;
	    }catch (Exception e) {
	    	logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "getReportMetaData", 
					ApplicationErrorCodes.APP_EC_41, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
	    
	}
	
	public List getTemplateCharts(final Integer loginId,final String emailId,final boolean filterTemplates) throws ApplicationException
    {
            Object object = null;
            logger.debug("Entered into getTemplateCharts");
            try {
                    object = hibernateTemplate.execute(new HibernateCallback() {
                            public Object doInHibernate(Session session)
                                            throws HibernateException, SQLException {
                            	
                                    List templateChartList =null;
                                  
                                    try {
                                    	if(!filterTemplates){
                                    		String sql = "from TemplateChart";
                                              
                                              Query q = session.createQuery(sql);
                                                 templateChartList =  (List)q.list();
                                                 if(templateChartList !=null){
                                                	 int size = templateChartList.size();
                                                	 for(int i=0;i<size;i++){
                                                		 TemplateChart templateChart = (TemplateChart)templateChartList.get(i);
                                                		 sql = "from MasterReportStatus where id=?";
                                                          Query query = session.createQuery(sql)
                              	        			 			.setLong(0, templateChart.getStatusId());
                                                          MasterReportStatus masterReportStatus =  (MasterReportStatus)query.uniqueResult();
                                                          templateChart.setStatusName(masterReportStatus.getStatusName());
         
                                                         List templateChartReportAssigns =  templateChart.getTemplateChartReportAssigns();
                                                         if(templateChartReportAssigns != null){
                                                          int len = templateChartReportAssigns.size();
                                                          String distIdsStr="";
                                                     	 	for(int j=0;j<len;j++){
                                                     	 		TemplateChartReportAssign templateChartReportAssign =  (TemplateChartReportAssign)templateChartReportAssigns.get(j);
                                                     	 		distIdsStr = distIdsStr+templateChartReportAssign.getMasterDistId();
                                                     	 		if(j+1<len){
                                                     	 			distIdsStr= distIdsStr+",";
                                                     	 		}
                                                     	 	}
                                                          sql = "from MasterDistributionType where id in (?)";
                                                          query = session.createQuery(sql)
                              	        			 			.setString(0, distIdsStr);
                                                          List masterDistributionTypeList =  (List)query.list();
                                                          String name="";
                                                          if(masterDistributionTypeList != null && masterDistributionTypeList.size()>0){
                                                        	  int mdSize = masterDistributionTypeList.size();
                                                        	  for(int k=0;k<mdSize;k++){
                                                        		  MasterDistributionType distributionType = (MasterDistributionType) masterDistributionTypeList.get(k);
                                                        		   name = name+distributionType.getTypeName();
                                                        		   templateChart.setIsOverall(distributionType.getOverAll());
                                                        		   if(k+1<mdSize){
                                                        			   name= name+",";
                                                        		   }
                                                        	  }
                                                          }else{
                                                        	  templateChart.setIsOverall("Y");
                                                          }
                                                          templateChart.setAssignReportNames(name);
                                                         }
                                                          
                                                	 }
                                                 }
                                    	}else{
                                    		templateChartList = new ArrayList();
                                          String sql = "select id from TemplateChart where id in" +
                                          		"(select templateChartId from TemplateChartReportAssign  where masterDistId in" +
                                          		"(select masterDistributionTypeId from DistributionList where emailId in" +
                                          		"(select emailId from Users  where login_id="+loginId+"))) and statusId in" +
                                          		"(select id from master_report_status where logicalName ='PUBLISHED') union all select id from templatechart where  statusId in " +
                                          		"(select id from master_report_status where logicalName ='PUBLISHED') " +
                                          		"and id in (select distinct templatechartId from reportmetadata where distributionlist like '%"+emailId+"%')";
                                          
                                          SQLQuery q = session.createSQLQuery(sql);
                                             List templateChartIdList =  (List)q.list();
                                             if(templateChartIdList !=null){
                                            	 int size = templateChartIdList.size();
                                            	 for(int i=0;i<size;i++){
                                            		
                                            		 Integer templateChartId = (Integer) templateChartIdList.get(i);
                                            	
                                            		 TemplateChart templateChart=null;
													try {
														templateChart = getTemplateChart(templateChartId);
														
													} catch (ApplicationException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
                                            		 sql = "from MasterReportStatus where id=?";
                                                      Query query = session.createQuery(sql)
                          	        			 			.setLong(0, templateChart.getStatusId());
                                                      MasterReportStatus masterReportStatus =  (MasterReportStatus)query.uniqueResult();
                                                      templateChart.setStatusName(masterReportStatus.getStatusName());
     
                                                     List templateChartReportAssigns =  templateChart.getTemplateChartReportAssigns();
                                                     if(templateChartReportAssigns != null){
                                                      int len = templateChartReportAssigns.size();
                                                      String distIdsStr="";
                                                 	 	for(int j=0;j<len;j++){
                                                 	 		TemplateChartReportAssign templateChartReportAssign =  (TemplateChartReportAssign)templateChartReportAssigns.get(j);
                                                 	 		distIdsStr = distIdsStr+templateChartReportAssign.getMasterDistId();
                                                 	 		if(j+1<len){
                                                 	 			distIdsStr= distIdsStr+",";
                                                 	 		}
                                                 	 	}
                                                      sql = "from MasterDistributionType where id in (?)";
                                                      Query query1 = session.createQuery(sql)
                          	        			 			.setString(0, distIdsStr);
                                                      List masterDistributionTypeList =  (List)query1.list();
                                                      String name="";
                                                      if(masterDistributionTypeList != null && masterDistributionTypeList.size()>0){
                                                    	  int mdSize = masterDistributionTypeList.size();
                                                    	  for(int k=0;k<mdSize;k++){
                                                    		  MasterDistributionType distributionType = (MasterDistributionType) masterDistributionTypeList.get(k);
                                                    		  String isOverall = distributionType.getOverAll();
                                                    		  templateChart.setIsOverall(isOverall);
                                                    		   name = name+distributionType.getTypeName();
                                                    		   if(k+1<mdSize){
                                                    			   name= name+",";
                                                    		   }
                                                    	  }
                                                      }else{
                                                    	  templateChart.setIsOverall("Y");
                                                      }
                                                      templateChart.setAssignReportNames(name);
                                                     }
                                                     templateChartList.add(templateChart);
                                            	 }
                                             }
                                    }
                                    return templateChartList;
                                    } catch (HibernateException he) {
                                            throw he;
                                    }
                            }
                    });
            } catch (Exception e) {
            		logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
                    // TODO: handle exception
                    ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "getTemplateCharts", 
                                    ApplicationErrorCodes.APP_EC_33, ApplicationConstants.EXCEPTION, e);
                    throw applicationException;
            }
            logger.debug("Leaving from getTemplateCharts");   
        return (List)object;
    }
	
	public List getVariableNames() throws ApplicationException
    {
            Object object = null;
            logger.debug("Entered into getVariableNames");
            try {
                    object = hibernateTemplate.execute(new HibernateCallback() {
                            public Object doInHibernate(Session session)
                                            throws HibernateException, SQLException {
                                    List VariableNamesDropDownList = new ArrayList();
                                    
                                    try {
                                     String sql = "from VariableNames";
                                             Query q = session.createQuery(sql);
                                            List VariableNamesList =  (List)q.list();
                                            if(VariableNamesList != null){
                                            	int size =VariableNamesList.size();
                                            	for(int i=0;i<size;i++){
                                            		VariableNames variableNames =  (VariableNames)VariableNamesList.get(i);
                                            		String columnName= variableNames.getColumnName();
                                            		String name = variableNames.getVariableName();
                                            		DropDownDisplayVo downDisplayVo = new DropDownDisplayVo();
                                            		downDisplayVo.setDisplayName(name);
                                            		downDisplayVo.setValue(columnName);
                                            		VariableNamesDropDownList.add(downDisplayVo);
                                            	}
                                            }
                                    return VariableNamesDropDownList;
                                    } catch (HibernateException he) {
                                            throw he;
                                    }
                            }
                    });
            } catch (Exception e) {
            		logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
                    // TODO: handle exception
                    ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "getVariableNames", 
                                    ApplicationErrorCodes.APP_EC_4, ApplicationConstants.EXCEPTION, e);
            		throw e;
            }
   
            logger.debug("Leaving from getVariableNames");  
        return (List)object;
    }
	
	public TemplateChart getDashboardTemplateChart(final Integer templateChartId) 
	{
		try{
		logger.debug("Entered into getDashboardTemplateChart");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
					TemplateChart templateChart = null;
                		 String sql = "from TemplateChart  where id ="+templateChartId;
	        			 Query q = session.createQuery(sql).setInteger(0, templateChartId);
	        			 templateChart =  (TemplateChart)q.uniqueResult();
        			return templateChart;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from getDashboardTemplateChart"); 
	    return (TemplateChart)object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	
	}
	
	
	public List getWordCloudSegments(final Integer templateChartInfoId,final String empId,final Integer reportMetaDataId) 
	{
		try{
		logger.debug("Entered into getWordCloudSegments");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
					List wordCloudSegmentList = null;
					SQLQuery q = null;
					if(reportMetaDataId!=null){
							if(ApplicationUtil.isEmptyOrNull(templateChartInfoId)){
								String sql = "select distinct segmentName from WordCloud where templateChartInfoId is null and employeeId is null and segmentName is not null and reportmetadataId="+reportMetaDataId;
								q = session.createSQLQuery(sql);
								wordCloudSegmentList =  (List)q.list();
						}else{
							if(ApplicationUtil.isEmptyOrNull(empId)){
								String sql = "select distinct segmentName from WordCloud where templateChartInfoId ="+templateChartInfoId+" and employeeId is null and segmentName is not null  and reportmetadataId="+reportMetaDataId;
								q = session.createSQLQuery(sql);
								wordCloudSegmentList =  (List)q.list();
							}else{
								String sql = "select distinct segmentName from WordCloud where templateChartInfoId ="+templateChartInfoId+" and employeeId='"+empId+"' and segmentName is not null  and reportmetadataId="+reportMetaDataId;
								q = session.createSQLQuery(sql);
								wordCloudSegmentList =  (List)q.list();
							}
						}
						
					}else{
							if(ApplicationUtil.isEmptyOrNull(templateChartInfoId)){
								String sql = "select distinct segmentName from WordCloud where templateChartInfoId is null and employeeId is null and segmentName is not null";
								q = session.createSQLQuery(sql);
								wordCloudSegmentList =  (List)q.list();
						}else{
							if(ApplicationUtil.isEmptyOrNull(empId)){
								String sql = "select distinct segmentName from WordCloud where templateChartInfoId ="+templateChartInfoId+" and employeeId is null and segmentName is not null";
								q = session.createSQLQuery(sql);
								wordCloudSegmentList =  (List)q.list();
							}else{
								String sql = "select distinct segmentName from WordCloud where templateChartInfoId ="+templateChartInfoId+" and employeeId='"+empId+"' and segmentName is not null";
								q = session.createSQLQuery(sql);
								wordCloudSegmentList =  (List)q.list();
							}
						}
						
					}
					
        			return wordCloudSegmentList;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from getWordCloudSegments");
	    return (List)object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	
	}
	
	public List getWordCloud(final Integer templateChartInfoId,final String empId,final String firstSegmentName,final Integer reportMetaDataId) 
	{
		try{
		logger.debug("Entered into getWordCloud");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
					List wordCloudList = null;
					Query q = null;
					if(reportMetaDataId !=null){
						if(ApplicationUtil.isEmptyOrNull(templateChartInfoId)){
							if(!ApplicationUtil.isEmptyOrNull(firstSegmentName)){
								String sql = "from WordCloud w where templateChartInfoId is null and employeeId is null and segmentName =? and reportmetadataId=? order by w.count desc";
								q = session.createQuery(sql).setString(0, firstSegmentName).setInteger(1, reportMetaDataId);
							}else{
								String sql = "from WordCloud w where templateChartInfoId is null and employeeId is null  and reportmetadataId=? order by w.count desc";
								q = session.createQuery(sql).setInteger(0, reportMetaDataId);	
							}
							wordCloudList =  (List)q.list();
						}else{
							if(ApplicationUtil.isEmptyOrNull(empId)){
								if(!ApplicationUtil.isEmptyOrNull(firstSegmentName)){
									String sql = "from WordCloud w where templateChartInfoId =? and employeeId is null and segmentName =? and reportmetadataId=? order by w.count desc";
									q = session.createQuery(sql).setInteger(0, templateChartInfoId).setString(1, firstSegmentName).setInteger(2, reportMetaDataId);
								}else{
									String sql = "from WordCloud w where templateChartInfoId =? and employeeId is null and segmentName is null and reportmetadataId=? order by w.count desc";
									q = session.createQuery(sql).setInteger(0, templateChartInfoId).setInteger(1, reportMetaDataId);	
								}
								wordCloudList =  (List)q.list();
							}else{
								if(!ApplicationUtil.isEmptyOrNull(firstSegmentName)){
									String sql = "from WordCloud w where templateChartInfoId =? and employeeId=? and segmentName = ? and reportmetadataId=? order by w.count desc";
									q = session.createQuery(sql).setInteger(0, templateChartInfoId).setString(1, empId).setString(2,firstSegmentName).setInteger(3, reportMetaDataId);
								}else{
									String sql = "from WordCloud w where templateChartInfoId =? and employeeId=? and segmentName is null and reportmetadataId=? order by w.count desc";
									q = session.createQuery(sql).setInteger(0, templateChartInfoId).setString(1, empId).setInteger(2, reportMetaDataId);
								}
								wordCloudList =  (List)q.list();
							}
						}
						
						
					} else {

						if(ApplicationUtil.isEmptyOrNull(templateChartInfoId)){
							if(!ApplicationUtil.isEmptyOrNull(firstSegmentName)){
								String sql = "from WordCloud w where templateChartInfoId is null and employeeId is null and segmentName =? order by w.count desc";
								q = session.createQuery(sql).setString(0, firstSegmentName);
							}else{
								String sql = "from WordCloud w where templateChartInfoId is null and employeeId is null order by w.count desc";
								q = session.createQuery(sql);	
							}
							wordCloudList =  (List)q.list();
						}else{
							if(ApplicationUtil.isEmptyOrNull(empId)){
								if(!ApplicationUtil.isEmptyOrNull(firstSegmentName)){
									String sql = "from WordCloud w where templateChartInfoId =? and employeeId is null and segmentName =? order by w.count desc";
									q = session.createQuery(sql).setInteger(0, templateChartInfoId).setString(1, firstSegmentName);
								}else{
									String sql = "from WordCloud w where templateChartInfoId =? and employeeId is null and segmentName is null order by w.count desc";
									q = session.createQuery(sql).setInteger(0, templateChartInfoId);	
								}
								wordCloudList =  (List)q.list();
							}else{
								if(!ApplicationUtil.isEmptyOrNull(firstSegmentName)){
									String sql = "from WordCloud w where templateChartInfoId =? and employeeId=? and segmentName = ? order by w.count desc";
									q = session.createQuery(sql).setInteger(0, templateChartInfoId).setString(1, empId).setString(2,firstSegmentName);
								}else{
									String sql = "from WordCloud w where templateChartInfoId =? and employeeId=? and segmentName is null order by w.count desc";
									q = session.createQuery(sql).setInteger(0, templateChartInfoId).setString(1, empId);
								}
								wordCloudList =  (List)q.list();
							}
						}
						
					}
					
        			return wordCloudList;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from getWordCloud");
	    return (List)object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	
	}
	
	public List getSentimentPercentage(final Integer templateChartInfoId,final String empId,final String firstSegmentName,final Integer reportMetaDataId) 
	{
		try{
	
		logger.debug("Entered into getSentimentPercentage");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
					List sentimentPercentageList = null;
					Query q = null;
					if(reportMetaDataId!=null){
						if(ApplicationUtil.isEmptyOrNull(templateChartInfoId)){
							if(!ApplicationUtil.isEmptyOrNull(firstSegmentName)){
								String sql = "from ChartSentimentPercentage where templateChartInfoId is null and employeeId is null and segment =? and reportmetadataId=?";
								q = session.createQuery(sql).setString(0, firstSegmentName).setInteger(1, reportMetaDataId);
							}else{
								String sql = "from ChartSentimentPercentage where templateChartInfoId is null and employeeId is null and segment is null and reportmetadataId=?";
								q = session.createQuery(sql).setInteger(0, reportMetaDataId);	
							}
							sentimentPercentageList =  (List)q.list();
						}else{
							if(ApplicationUtil.isEmptyOrNull(empId)){
								if(!ApplicationUtil.isEmptyOrNull(firstSegmentName)){
									String sql = "from ChartSentimentPercentage where templateChartInfoId =? and employeeId is null and segment =? and reportmetadataId=?";
									q = session.createQuery(sql).setInteger(0, templateChartInfoId).setString(1, firstSegmentName).setInteger(2, reportMetaDataId);
								}else{
									String sql = "from ChartSentimentPercentage where templateChartInfoId =? and employeeId is null and segment is null and reportmetadataId=?";
									q = session.createQuery(sql).setInteger(0, templateChartInfoId).setInteger(1, reportMetaDataId);	
								}
								sentimentPercentageList =  (List)q.list();
							}else{
								if(!ApplicationUtil.isEmptyOrNull(firstSegmentName)){
									String sql = "from ChartSentimentPercentage where templateChartInfoId =? and employeeId=? and segment = ? and reportmetadataId=?";
									q = session.createQuery(sql).setInteger(0, templateChartInfoId).setString(1, empId).setString(2,firstSegmentName).setInteger(3, reportMetaDataId);
								}else{
									String sql = "from ChartSentimentPercentage where templateChartInfoId =? and employeeId=? and segment is null and reportmetadataId=?";
									q = session.createQuery(sql).setInteger(0, templateChartInfoId).setString(1, empId).setInteger(2, reportMetaDataId);
								}
								sentimentPercentageList =  (List)q.list();
							}
						}
						
					} else{
						if(ApplicationUtil.isEmptyOrNull(templateChartInfoId)){
							if(!ApplicationUtil.isEmptyOrNull(firstSegmentName)){
								String sql = "from ChartSentimentPercentage where templateChartInfoId is null and employeeId is null and segment =?";
								q = session.createQuery(sql).setString(0, firstSegmentName);
							}else{
								String sql = "from ChartSentimentPercentage where templateChartInfoId is null and employeeId is null and segment is null";
								q = session.createQuery(sql);	
							}
							sentimentPercentageList =  (List)q.list();
						}else{
							if(ApplicationUtil.isEmptyOrNull(empId)){
								if(!ApplicationUtil.isEmptyOrNull(firstSegmentName)){
									String sql = "from ChartSentimentPercentage where templateChartInfoId =? and employeeId is null and segment =?";
									q = session.createQuery(sql).setInteger(0, templateChartInfoId).setString(1, firstSegmentName);
								}else{
									String sql = "from ChartSentimentPercentage where templateChartInfoId =? and employeeId is null and segment is null";
									q = session.createQuery(sql).setInteger(0, templateChartInfoId);	
								}
								sentimentPercentageList =  (List)q.list();
							}else{
								if(!ApplicationUtil.isEmptyOrNull(firstSegmentName)){
									String sql = "from ChartSentimentPercentage where templateChartInfoId =? and employeeId=? and segment = ?";
									q = session.createQuery(sql).setInteger(0, templateChartInfoId).setString(1, empId).setString(2,firstSegmentName);
								}else{
									String sql = "from ChartSentimentPercentage where templateChartInfoId =? and employeeId=? and segment is null";
									q = session.createQuery(sql).setInteger(0, templateChartInfoId).setString(1, empId);
								}
								sentimentPercentageList =  (List)q.list();
							}
						}
						
					}
					
        			return sentimentPercentageList;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from getSentimentPercentage");
	    return (List)object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
		
	}
	
	public Map getChartXmlFromDatabase(final String templateChartInfoId,final String empId,final String tableName,//////////////////////
            final String categoryName, final String seriesColumnName,
            final String valuesColumnNames[],
            final boolean isFromCombinationChart,final boolean isRootDatasetRequired,final String filterName1,
            final String filterValue1,final String xAxis,final String yAxis,final String percentage,
            final boolean isFromSentiment,final Integer functionId,final Integer topBottomThreshold,
            final Integer noOfResponses,final String whereConstrain,final Integer reportMetaDataId,final String benchMark,final String colorCode,final String chartType) {
			try{
				
			logger.debug("Entered into getChartXmlFromDatabase");
			final Map variableNamesObj=(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.VARIABLE_NAMES_MAP_CACHE_KEY);
			final Map functionMapObj = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.MASTER_FUNCTIONS_MAP_CACHE_KEY);
			final Map codeBookMapObj =(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.CODEBOOK_ACTUAL_RECODE_VALUES_CACHE_KEY);
			final Map topLevelsLimitMapObj = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.TOP_LEVELS_LIMIT);
            Object object = hibernateTemplate.execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                         throws HibernateException, SQLException {
            	String[] chartColorCode = ApplicationConstants.DEFAULT_COLORS;
            	Map tableViewObj= new HashMap();
            	List tableViewList= new ArrayList();
            	int percentage_multiplier=1;
            	DecimalFormat df = new DecimalFormat("##.00");
            	String functionName= (String)functionMapObj.get(functionId);
    			if(!ApplicationUtil.isEmptyOrNull(percentage) && percentage.equals("true")){
    				percentage_multiplier=100;
    			}
                  StringBuffer categoryBuffer = new StringBuffer();
                  StringBuffer dataSetbuffer = new StringBuffer();
                  String templateChartIdStr="";
                  String sqlbenchmark="";
                  String referenceGroupValues ="";
                  int level_threshold=0;
                  int filter_threshold=0;
                  String levelThreshold = PropertiesUtil.getProperty("level_threshold");
                  if(!ApplicationUtil.isEmptyOrNull(levelThreshold)){
                	  try{
                		  level_threshold = Integer.parseInt(levelThreshold);
                	  }catch(NumberFormatException ne){
                		  logger.error(ne.getMessage());
                	  }
                  }
                  String filterThreshold = PropertiesUtil.getProperty("filter_threshold");
                  if(!ApplicationUtil.isEmptyOrNull(filterThreshold)){
                	  try{
                		  filter_threshold = Integer.parseInt(filterThreshold);
                	  }catch(NumberFormatException ne){
                		  logger.error(ne.getMessage());
                	  }
                  }
                  if(!ApplicationUtil.isEmptyOrNull(templateChartInfoId)){
                	  templateChartIdStr = "templateChartInfoId="+templateChartInfoId;
                  }else{
                	  templateChartIdStr =  "templateChartInfoId is NULL";
                  }
                  
                  if(!ApplicationUtil.isEmptyOrNull(colorCode)){
                	  chartColorCode=colorCode.split(",");
                  }
                  
                  String benchmarkstr=benchMark;
                  if(functionName.equals("AVERAGE")){
	                  if(!ApplicationUtil.isEmptyOrNull(benchmarkstr) && benchmarkstr!= null && !benchmarkstr.equals("-1") && !benchmarkstr.equals("OVER_ALL") && !benchmarkstr.equals("IMMEDIATE_SUPERVISOR") ){
	                	  if(!ApplicationUtil.isEmptyOrNull(empId)){
	                    	  sqlbenchmark="select  group_concat(distinct "+benchmarkstr+" separator ',') from data where employeeId in (select employee from hierarchy where manager='"+empId+"')";
	                      }else{
	                    	  sqlbenchmark="select  group_concat(distinct "+benchmarkstr+" separator ',') from data where 1=1 "+whereConstrain;
	                      }
	                	  ResultSet rs = session.connection().createStatement().executeQuery(sqlbenchmark);
	                	  rs = session.connection().createStatement().executeQuery(sqlbenchmark);
	                      while(rs.next()){
	    					referenceGroupValues = rs.getString(1);
	                      }
	                  }
                  }
                  try {
                	  	String empQueryStr="";
                	  	String empQueryStrWithAlias="";
                	  	 if(!ApplicationUtil.isEmptyOrNull(empId)){
                	  		empQueryStr = "and employeeId="+empId;
                	  		empQueryStrWithAlias="and t.employeeId="+empId;
                	  	 }else{
                	  		empQueryStr = "and employeeId is NULL";
                	  		empQueryStrWithAlias="and t.employeeId is NULL";
                	  	 }
                         String filterQuery1 ="";
                         if(!ApplicationUtil.isEmptyOrNull(filterName1)){
                        	 if(!ApplicationUtil.isEmptyOrNull(filterValue1)){
                        		 filterQuery1 = " and " + filterName1 + " = '" + filterValue1 + "'";
                        	 }else{
                        		 filterQuery1 = " and " + filterName1 + " is NULL";
                        	 }
                         }
                         String reportMetaDataStr="";
                         if(reportMetaDataId!=null){
                        	 reportMetaDataStr = "  and reportMetaDataId="+reportMetaDataId;
                         }
                         List categoryNameList=new ArrayList();
                         List allCategoryNamesList = new ArrayList();
                         if (functionName.equals("AVERAGE")||functionName.equals("FREQUENCY")){
                             if (valuesColumnNames != null && valuesColumnNames.length > 0) {
                            	  Double totalNValue =null;
                                  int length = valuesColumnNames.length;
                                  for (int i = 0; i < length; i++) {
                                        String valuesColumnName = valuesColumnNames[i];
                                        
                                        String tempColumnName="";
                                        if(functionName.equals("FREQUENCY")){
                                       	 tempColumnName = valuesColumnName;
                                       	 
                                        }
                                        
                                        if (functionName.equals("AVERAGE")) {
                                       	 tempColumnName= "frequency";
                                        }

                                        String s1 = "select sum(t."
                                                      + tempColumnName
                                                      + ")"
                                                      + " from "
                                                      + tableName
                                                      + " t where 1=1 "+empQueryStrWithAlias 
                                                      + " and "+templateChartIdStr
                                                      + filterQuery1+reportMetaDataStr;
                                                      
                                        SQLQuery q = session.createSQLQuery(s1);
                                        q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                                       List res = q.list();
                                       if (res != null) {
                                       	for (Iterator iter = res
                                                   .iterator(); iter
                                                   .hasNext();) {
                                       		HashMap row1 = (HashMap) iter .next();
                                       		totalNValue=  (Double) row1.get("sum(t."+ tempColumnName+")");
                                       	}
                                       }
                                    }
                                  if(totalNValue!= null && totalNValue.intValue() <=filter_threshold){
                             		 return tableViewObj;
                             	 }
                              }
                        }
                         
                         
                         if (!ApplicationUtil.isEmptyOrNull(seriesColumnName) && !seriesColumnName.equals("-1")) {
                        	 	Map categoryColumnNameBook= (Map)codeBookMapObj.get(yAxis);
                        	 	String topLevelsLimitStr = (String)topLevelsLimitMapObj.get(yAxis.toLowerCase());
                        	 	Map seriesColumnNameBook= (Map)codeBookMapObj.get(xAxis);
                                if (!isFromCombinationChart) {
                                	String whereClause="";
                                	if(topBottomThreshold!= null && topBottomThreshold.intValue()>0){
                                		whereClause=" value desc";
                                	}else if(functionName.equals("SENTIMENT")){
                                		whereClause= " id";
                                	}else if(functionName.equals("AVERAGE") && !ApplicationUtil.isEmptyOrNull(topLevelsLimitStr) && !topLevelsLimitStr.equals("-1")){
                                		whereClause=" frequency*1 desc Limit 0,"+topLevelsLimitStr;
                                	}else{
                                		whereClause= categoryName;
                                	}
                                    String sql = "SELECT distinct "
                                                     + categoryName + " FROM " + tableName
                                                     + " where 1=1 " + filterQuery1 + " " + empQueryStr +" and "+templateChartIdStr+reportMetaDataStr+" and "+categoryName+" is not null order by "+whereClause ;
                                    
                                    SQLQuery query = session.createSQLQuery(sql);
                                       query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                                       List results = query.list();
                                       categoryBuffer.append("<categories>");
                                       int index=0;
                                       int size=results.size();
                                       int thresholdVal=0;
                                       if(topBottomThreshold!= null && topBottomThreshold.intValue() > 0){
                                    	   thresholdVal=topBottomThreshold;
                                    	   if(size < topBottomThreshold *2){
                                    		   thresholdVal =size/2;
                                    	   }
                                       }
                                       if (results != null) {
                                    	   List tableList1= new ArrayList();
                                    	   String yAxisName="";
                                    	   if(!ApplicationUtil.isEmptyOrNull(yAxis) && !yAxis.equals("-1")){
                                    		   yAxisName= (String) variableNamesObj.get(yAxis.toLowerCase());
                                    	   }else{
                                    		    yAxisName="Variables";
                                    	   }
                                    	   tableList1.add(yAxisName);
                                    	  
                                    	   for (Iterator it = results.iterator(); it
                                                            .hasNext();) {
                                    		   
                                    		   
                                                     HashMap row = (HashMap) it.next();
                                                     String nVal="";
                                                     if (functionName.equals("AVERAGE")||functionName.equals("FREQUENCY")){
	                                                      if (valuesColumnNames != null && valuesColumnNames.length > 0) {
		                                                       int length = valuesColumnNames.length;
		                                                       for (int i = 0; i < length; i++) {
		                                                             String valuesColumnName = valuesColumnNames[i];
		                                                             
		                                                             String tempColumnName="";
		                                                             if(functionName.equals("FREQUENCY")){
		                                                            	 tempColumnName = "sum(t."+valuesColumnName+ ")";
		                                                            	 
		                                                             }
		                                                             
		                                                             if (functionName.equals("AVERAGE")) {
		                                                            	 
		                                                            	 tempColumnName= "group_concat(frequency  order by "+seriesColumnName+" separator ',')";
		                                                             }
		
		                                                             String s1 = "select "
		                                                                           + tempColumnName
		                                                                           + " from "
		                                                                           + tableName
		                                                                           + " t where 1=1 "+empQueryStrWithAlias 
		                                                                           + " and "+templateChartIdStr
		                                                                           + " and t."+ categoryName
		                                                                           +" = " + "'"+ (String) row.get( categoryName)+"'"
		                                                                           + filterQuery1+reportMetaDataStr;
		                                                             System.out.println(s1);
		                                                             ResultSet  rs = session.connection().createStatement().executeQuery(s1);
		                                          					 while(rs.next()){
		                                          						nVal = rs.getString(tempColumnName);
		                                          					 }
		                                                         }
	                                                       }
                                                     }
                                                     String val= (String) row.get(categoryName);
                                                     String cName=val;
                                                     allCategoryNamesList.add(cName);
                                                     
                                                     if(categoryColumnNameBook!= null){
                                                    	 String recodeValue = (String)categoryColumnNameBook.get(val);
                                                    	 if(!ApplicationUtil.isEmptyOrNull(recodeValue)){
                                                    		 val= recodeValue;
                                                    	 }
                                                     }
                                                     if(!functionName.equals("SENTIMENT")){
                                                    	 if(!ApplicationUtil.isEmptyOrNull(nVal) && !functionName.equals("AVERAGE")){
    	                                                     val+="  (N =  "+Double.valueOf(nVal).intValue()+ ")";
    	                                                     int n=Double.valueOf(nVal).intValue();
    	                                                     if(n<=level_threshold){
    	                                                    	 categoryNameList.add(cName);
    	                                                    	 //continue;
    	                                                     }
                                                         } else{
                                                        	 val+="  (N =  "+nVal+ ")";
                                                         }
                                                     }
                                                    
                                                     if(topBottomThreshold!= null && topBottomThreshold.intValue() > 0){
	                                                     if(index < thresholdVal){
	                                                    	 categoryBuffer.append("<category name='" + val + "'/>");
	                                                    	 tableList1.add(val);
	                                                     }else{
	                                                    	if(size-index <= thresholdVal){
	                                                    		 categoryBuffer.append("<category name='" + val + "'/>");
	                                                    		 tableList1.add(val);
	                                                    	}
	                                                     }
                                                     }
                                                     else{
                                                    	 categoryBuffer.append("<category name='" + val + "'/>");
                                                		 tableList1.add(val); 
                                                     }
                                                     System.out
															.println("Mice testing"+val);
                                                     index++;
                                              }
                                              tableViewList.add(tableList1);    //list1 added
                                       }
                                       categoryBuffer.append("</categories>");
                                }
                                String orderClause="";
                                if(functionName.equals("SENTIMENT")){
                                	orderClause = seriesColumnName + " desc";
                                } else{
                                	orderClause = seriesColumnName;
                                }
                                String sql = "SELECT distinct lower("
                                        + seriesColumnName + ") FROM " + tableName
                                        + " where " + seriesColumnName
                                        + " != \"\" " + filterQuery1 + " " + empQueryStr +" and "+templateChartIdStr+reportMetaDataStr+" order by "+ orderClause;
                                
                          SQLQuery query = session.createSQLQuery(sql);
                          System.out.println("required query"+sql);
                          query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                          List results = query.list();
                          if (results != null) {
                                 int index =0;
                                 if(isRootDatasetRequired){
                                        dataSetbuffer.append("<dataset>");
                                 }
                                 for (Iterator it = results.iterator(); it.hasNext();) {
                                        HashMap row = (HashMap) it.next();
                                        List tableList2 = new ArrayList();
                                        String val = (String) row.get("lower("
                                                      + seriesColumnName + ")");
                                        String tempVal=val;
                                        if (val != null) {
                                      	  if(seriesColumnNameBook != null){
                                      		  String recodeValue = (String)seriesColumnNameBook.get(val);
                                      		  if(!ApplicationUtil.isEmptyOrNull(recodeValue)){
                                      			  tempVal = recodeValue;
                                      		  }
                                      	  }
                                      	tableList2.add(tempVal);
                                               if (isFromCombinationChart) {
                                            	  
                                                      dataSetbuffer
                                                                   .append("<dataset seriesName='"
                                                                                 + tempVal.toUpperCase()
                                                                                 + "'color='"
                                                                                 + chartColorCode[index]
                                                                                 +"'>");
                                               } else {
                                            	   if(index==0 && isFromSentiment){
                                                      dataSetbuffer
                                                                   .append("<dataset seriesName='"
                                                                                 + tempVal.toUpperCase()
                                                                                 + "' color='"
                                                                                 + chartColorCode[index]
                                                                                 +"' renderas='Area'>");
                                            	   }else{
                                            		   if(topBottomThreshold!= null && topBottomThreshold.intValue()>0){
                                            			   dataSetbuffer
                                            			   .append("<dataset seriesName='"
                                                                     + tempVal.toUpperCase()
                                                                     + "'>");
                                            		   }else{
                                            			   dataSetbuffer
                                            			   .append("<datas" +
                                            			   		"et seriesName='"
                                                                     + tempVal.toUpperCase()
                                                                     + "' color='"
                                                                     + chartColorCode[index]
                                                                     +"'>");
                                            		   }
                                            	   }
                                               }

                                               if (valuesColumnNames != null
                                                            && valuesColumnNames.length > 0) {
                                                      int length = valuesColumnNames.length;
                                                      for (int i = 0; i < length; i++) {
                                                    	  String whereClause="";
                                                    	  if(topBottomThreshold!= null && topBottomThreshold.intValue()>0){
                                                      		whereClause=" b_alias desc";
                                                      	}else if(functionName.equals("SENTIMENT")){
                                                    		whereClause= " id";
                                                    	}else if(functionName.equals("AVERAGE") && !ApplicationUtil.isEmptyOrNull(topLevelsLimitStr)  && !topLevelsLimitStr.equals("-1")){
                                                    		whereClause=" frequency*1 desc Limit 0,"+topLevelsLimitStr;
                                                    	}else{
                                                      		whereClause= "t."+categoryName;
                                                      	}
                                                            String valuesColumnName = valuesColumnNames[i];
                                                            String tempColumnName="";
                                                            if(functionName.equals("FREQUENCY")){
                                                           	 tempColumnName ="";
                                                           	 
                                                            }
                                                            
                                                            if (functionName.equals("AVERAGE")) {
                                                           	 tempColumnName= " , t.frequency";
                                                            }

                                                            StringBuffer sb = new StringBuffer();
                                                            sb.append("select *  from (select t."
                                                                          + valuesColumnName
                                                                          + " b_alias ,t."
                                                                          + categoryName + tempColumnName+", t.id"
                                                                          + " from "
                                                                          + tableName
                                                                          + " t where 1=1 "+empQueryStrWithAlias 
                                                                          + " and "+templateChartIdStr
                                                                          + " and t."+ categoryName
                                                                          +" is not null" 
                                                                          + " and t. "
                                                                          + seriesColumnName
                                                                          + "='"
                                                                          + val + "' " + filterQuery1+reportMetaDataStr);
                                                            sb.append(" union ");
                                                            sb.append("select distinct 0 as "
                                                                          + valuesColumnName + ",t."
                                                                          + categoryName + tempColumnName + ", t.id  ");
                                                            sb.append("from "
                                                                          + tableName
                                                                          + " t where 1=1 "+ empQueryStrWithAlias 
                                                                          + " and "+templateChartIdStr+ " " + filterQuery1 +reportMetaDataStr+" and t."
                                                                          + categoryName
                                                                          + " not in (select distinct t."
                                                                          + categoryName + " ");
                                                            sb.append("from " + tableName
                                                                          + " t where t."
                                                                          + seriesColumnName + "='"
                                                                          + val
                                                                          + "' "+empQueryStrWithAlias
                                                                          + " and "+templateChartIdStr +" " + filterQuery1 +reportMetaDataStr + " "+")) as t group by t."+categoryName+" order by "+whereClause);

                                                            query = session.createSQLQuery(sb.toString());
                                                             query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                                                            List results1 = query.list();
                                                           
                                                            int indexVal=0;
                                                            int size1=results1.size();
                                                            int thresholdVal1=0;
                                                            if(topBottomThreshold!= null && topBottomThreshold.intValue()>0){
	                                                            thresholdVal1=topBottomThreshold;
	                                                            if(size1 < topBottomThreshold *2){
	                                                         	   thresholdVal1 =size1/2;
	                                                            }
                                                            }
                                                             if (results1 != null) {
                                                            
                                                                   for (Iterator it1 = results1
                                                                                 .iterator(); it1
                                                                                 .hasNext();) {
                                                                	   
                                                                   
                                                                          HashMap row1 = (HashMap) it1
                                                                                        .next();
                                                                          String val1 = (String) row1
                                                                                        .get("b_alias");
                                                                          String val2 =(String) row1.get(categoryName);
                                                                          int nVal = 0;
                                                                          if(!ApplicationUtil.isEmptyOrNull(functionName) && functionName.equals("AVERAGE")){
                                                                        	  nVal =Integer.parseInt((String)row1.get("frequency"));
                                                                          }
                                                                          
                                                                    	 if(topBottomThreshold!= null && topBottomThreshold.intValue()>0){
	                                                                    		  if(indexVal < thresholdVal1){
	                                                                    			  if(nVal<=level_threshold){
	                                                                                	  dataSetbuffer.append("<set value='' color='#008000' />");
	                                                                                	  tableList2.add(""); 
	                                                                            	  }else{
		                                                                    			  dataSetbuffer.append("<set value='"
		                                                                                          + Double.parseDouble(val1)*percentage_multiplier
		                                                                                          + "' color='#008000' />");
		                                                                        		  tableList2.add(val1);
	                                                                            	  }
	                                                                              }else{
	                                                                             	if(size1-indexVal <= thresholdVal1){
	                                                                             		if(nVal<=level_threshold){
		                                                                                	  dataSetbuffer.append("<set value='' color='#FF0000' />");
		                                                                                	  tableList2.add(""); 
		                                                                            	  }else{
		                                                                            		  dataSetbuffer.append("<set value='"
	                                                                                             + Double.parseDouble(val1)*percentage_multiplier
	                                                                                             + "' color='#FF0000'/>");
	                                                                             		 	tableList2.add(val1);
		                                                                            	  }
	                                                                             	}
	                                                                              }
                                                                    	 }else{
                                                                    		 if(!ApplicationUtil.isEmptyOrNull(functionName) && functionName.equals("AVERAGE")){
                                                                    			 if(nVal<=level_threshold){
	   	                                                                           	  dataSetbuffer.append("<set value='' />");
	   	                                                                           	  tableList2.add(""); 
                                                                          	  	  }else{
	                                                                       			  dataSetbuffer.append("<set value='"
	                                                                                             + Double.parseDouble(val1)*percentage_multiplier
	                                                                                             + "' />");
	                                                                           		  tableList2.add(val1);
                                                                          	      }
                                                                    		 }else{
                                                                    			 if(categoryNameList.contains(val2.toLowerCase())){
	   	                                                                           	  dataSetbuffer.append("<set value='' />");
	   	                                                                           	  tableList2.add(""); 
                                                                          	  	  }else{
	                                                                       			  dataSetbuffer.append("<set value='"
	                                                                                             + Double.parseDouble(val1)*percentage_multiplier
	                                                                                             + "' />");
	                                                                           		  tableList2.add(val1);
                                                                          	      }
                                                                    		 }
                                                                    	  }
                                                                          indexVal++;
                                                                   }
                                                            }
                                                             
                                                      }
                                               }
                                               dataSetbuffer.append("</dataset>");
                                        }
                                        index++;
                                        tableViewList.add(tableList2);
                                 }
                                 if(isRootDatasetRequired){
                                        dataSetbuffer.append("</dataset>");
                                 }
                          }
                          	
                          if(!ApplicationUtil.isEmptyOrNull(benchmarkstr) && !benchmarkstr.equals("-1") && benchmarkstr.length()>0){
                            sql = "SELECT distinct lower("
                                              + seriesColumnName + ") FROM benchmark" 
                                              + " where " + seriesColumnName
                                              + " != \"\" " + " " + empQueryStr +" and "+templateChartIdStr+reportMetaDataStr+" order by "+ seriesColumnName ;
                                 query = session.createSQLQuery(sql);
                                 System.out.println("benchmark 1st query"+sql);
                                query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                                 results = query.list();
                                if (results != null) {
                                       int index =1;
                                       if(isRootDatasetRequired){
                                              dataSetbuffer.append("<dataset>");
                                       }
                                       
                                       for (Iterator it = results.iterator(); it.hasNext();) {
                                              HashMap row = (HashMap) it.next();
                                              
                                              List tableList3 = new ArrayList();
                                              String val = (String) row.get("lower("
                                                            + seriesColumnName + ")");
                                              String tempVal=val;
                                              String legend ="";
                                              if(xAxis.contains(",") && (yAxis.equals("-1"))){
                                            	  legend ="";
                                              } else{
                                            	  legend =" RATING";
                                              }
                                              
                                              if (val != null) {
                                            	  if(seriesColumnNameBook != null){
                                            		  String recodeValue = (String)seriesColumnNameBook.get(val);
                                            		  if(!ApplicationUtil.isEmptyOrNull(recodeValue)){
                                            			  tempVal = recodeValue;
                                            		  }
                                            	  }
                                            	  tableList3.add(tempVal + legend.toLowerCase());
                                                     if (isFromCombinationChart) {
                                                            dataSetbuffer
                                                                         .append("<dataset seriesName='"
                                                                                       + tempVal.toUpperCase() +legend
                                                                                       + "' color='"
                                                                                       + chartColorCode[index]
                                                                                       +"' renderas ='Line' anchorRadius='4'  lineThickness='0' anchorBgColor='00EB00' showValues='0' >");
                                                     } else {
                                                    	 if(!ApplicationUtil.isEmptyOrNull(chartType) && chartType.equalsIgnoreCase("MSCombiDY2D")){
                                                    		 dataSetbuffer
                                                             .append("<dataset seriesName='"
                                                                           + tempVal.toUpperCase() + legend
                                                                           + "' color='"
                                                                           + chartColorCode[index]
                                                                           +"' parentYAxis='S' renderas ='Line' anchorRadius='4'  lineThickness='0' anchorBgColor='00EB00' showValues='0'>");
                                                    		 
                                                    	 } else{
                                                    		 
                                                    		 dataSetbuffer
                                                             .append("<dataset seriesName='"
                                                                           + tempVal.toUpperCase() + legend
                                                                           + "' color='"
                                                                           + chartColorCode[index]
                                                                           +"' renderas ='Line' anchorRadius='4'  lineThickness='0' anchorBgColor='00EB00' showValues='0' showValues='0'>");
                                                    		 
                                                    	 }
                                                     }

                                                     if (valuesColumnNames != null
                                                                  && valuesColumnNames.length > 0) {
                                                            int length = valuesColumnNames.length;
                                                            for (int i = 0; i < length; i++) {
                                                            	  String whereClause="";
                                                            	  if(topBottomThreshold!= null && topBottomThreshold.intValue()>0){
                                                            		  	whereClause=" b_alias desc";
                                                            	  } else{
                                                              		  	whereClause= "t."+categoryName;
                                                            	  }
                                                                  String valuesColumnName = valuesColumnNames[i];

                                                                  StringBuffer sb = new StringBuffer();
                                                                  sb.append("select *  from (select t."
                                                                                + valuesColumnName
                                                                                + " b_alias ,t."
                                                                                + categoryName
                                                                                + " from benchmark"
                                                                                
                                                                                + " t where 1=1 "+empQueryStrWithAlias 
                                                                                + " and "+templateChartIdStr+reportMetaDataStr
                                                                                + " and t. "
                                                                                + seriesColumnName
                                                                                + "='"
                                                                                + val + "' ");
                                                                  sb.append(" union ");
                                                                  sb.append("select distinct 0 as "
                                                                                + valuesColumnName + ",t."
                                                                                + categoryName + " ");
                                                                  sb.append("from "
                                                                                + tableName
                                                                                + " t where 1=1 "+ empQueryStrWithAlias 
                                                                                + " and "+templateChartIdStr+reportMetaDataStr+ " " +" and t."
                                                                                + categoryName
                                                                                + " not in (select distinct t."
                                                                                + categoryName + " ");
                                                                  sb.append("from benchmark"
                                                                                + " t where t."
                                                                                + seriesColumnName + "='"
                                                                                + val
                                                                                + "' "+empQueryStrWithAlias+reportMetaDataStr
                                                                                + " and "+templateChartIdStr +" " + " "+")) as t group by t."+categoryName+" order by "+whereClause+" ");

                                                                  query = session.createSQLQuery(sb
                                                                                .toString());
                                                                  System.out.println("benchmark 2nd query"+sb);
                                                                   query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                                                                  List results1 = query.list();
                                                                  Map benchMarkMapObj = new HashMap();
                                                                   if (results1 != null) {
                                                                         for (Iterator it1 = results1
                                                                                       .iterator(); it1
                                                                                       .hasNext();) {
                                                                         
                                                                                HashMap row1 = (HashMap) it1
                                                                                              .next();
                                                                                String val1 = (String) row1
                                                                                              .get("b_alias");
                                                                               String val2 =(String) row1.get(categoryName);
                                                                               if(!ApplicationUtil.isEmptyOrNull(val2)){
                                                                            	   benchMarkMapObj.put(val2.toLowerCase(), val1);
                                                                               }
                                                                         }
                                                                         for(int j=0;j<allCategoryNamesList.size();j++){
                                                                        	 String catName = (String)allCategoryNamesList.get(j);
                                                                        	 String valueStr = (String)benchMarkMapObj.get(catName.toLowerCase());
                                                                         	dataSetbuffer.append("<set value='"
                                                                                           + Double.parseDouble(valueStr)*percentage_multiplier
                                                                                           + "'  toolText='Effectiveness Rating, "+df.format(Double.parseDouble(valueStr)*percentage_multiplier)+"' />");
                                                                         	tableList3.add(valueStr);
                                                                         }
                                                                  }
                                                            }
                                                     }
                                                     dataSetbuffer.append("</dataset>");
                                              }
                                              index++;
                                              tableViewList.add(tableList3);
                                       }
                                       if(isRootDatasetRequired){
                                              dataSetbuffer.append("</dataset>");
                                       }
                                       
                                       
                                }
                          }
                         } else {
                                if (valuesColumnNames != null
                                              && valuesColumnNames.length > 0) {
                                       int length = valuesColumnNames.length;
                                       if (length > 1) {
                                              int index = 0;
                                              for (int i = 0; i < length; i++) {
                                                     String sql = "SELECT lower("
                                                                  + valuesColumnNames[i] + ") FROM "
                                                                  + tableName + " where 1=1 " + filterQuery1 + " " +empQueryStr+ " and "+templateChartIdStr+reportMetaDataStr ;
                                                     SQLQuery query = session
                                                                  .createSQLQuery(sql);
                                                     query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                                                     List results = query.list();
                                                     if (results != null) {
                                                            
                                                            if (isFromCombinationChart) {
                                                                  dataSetbuffer.append("<dataset seriesName='"
                                                                                + valuesColumnNames[i].toUpperCase()
                                                                                + "' color='"
                                                                                + chartColorCode[index]
                                                                                +"'>");
                                                            } else {
                                                                  dataSetbuffer.append("<dataset seriesName='"
                                                                                + valuesColumnNames[i].toUpperCase()
                                                                                + "' color='"
                                                                                + chartColorCode[index]
                                                                                +"'>");
                                                            }
                                                     
                                                            for (Iterator it = results.iterator(); it
                                                                         .hasNext();) {
                                                                  HashMap row = (HashMap) it.next();
                                                                  String val = (String) row
                                                                                .get("lower("
                                                                                              + valuesColumnNames[i]
                                                                                              + ")");
                                                                  dataSetbuffer.append("<set value='"
                                                                                + Double.parseDouble(val)*percentage_multiplier + "'/>");
                                                                   
                                                            }
                                                            dataSetbuffer.append("</dataset>");
                                                     }
                                                     index++;
                                              }
                                       } else {
                                    	   
                                    	   Map seriesColumnNameBook= (Map)codeBookMapObj.get(xAxis);////////////////////////////////////////////////////////
                                              String sql = "select " + categoryName
                                                            + ", lower(" + valuesColumnNames[0]
                                                            + ") from " + tableName + " where 1=1 " + filterQuery1 + " " +empQueryStr+" and "+templateChartIdStr + reportMetaDataStr;
                                              
                                              if(!ApplicationUtil.isEmptyOrNull(benchmarkstr) && benchmarkstr!=null && benchmarkstr.length()>0 && !benchmarkstr.equalsIgnoreCase("null"))
                                            	  sql += " union select " + categoryName
                                                          + ", lower(" + valuesColumnNames[0]
                                                          + ") from benchmark where 1=1 " + filterQuery1 + " " +empQueryStr+" and "+templateChartIdStr+reportMetaDataStr;
                                              System.out.println("required query"+sql);
                                              SQLQuery query = session.createSQLQuery(sql);
                                              query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                                              List results1 = query.list();
                                              if (results1 != null) {
                                                     int index=0;
                                                     List tableList4 = new ArrayList();
                                              	   	if((yAxis.equals("-1") && xAxis.contains(","))|| ApplicationUtil.isEmptyOrNull(yAxis)){
                                              	   		tableList4.add("Variables");
                                              	   	}else{
                                              	   		String xAxisName=(String) variableNamesObj.get(xAxis.toLowerCase());
                                              	   		tableList4.add(xAxisName);
                                              	   	}
                                                     List tableList5 = new ArrayList();
                                                     tableList5.add(functionName);
                                                     if(isFromCombinationChart){
                                                            dataSetbuffer.append("<lineSet seriesname='" + valuesColumnNames[0]+ "'  color='"
                                                                                       + chartColorCode[index]
                                                                                       +"'>");
                                                     }
                                                     for (Iterator it1 = results1.iterator(); it1
                                                                  .hasNext();) {
                                                            HashMap row1 = (HashMap) it1.next();
                                                          /* new change here  */
                                                            Double tempVal=null;
                                                            if (functionName.equals("AVERAGE")) {
                                                                    String s1 = "select sum(t.frequency"
                                                                                  + ")"
                                                                                  + " from "
                                                                                  + tableName
                                                                                  + " t where 1=1 "+empQueryStrWithAlias 
                                                                                  + " and "+templateChartIdStr
                                                                                  + " and t."+ categoryName
                                                                                  +" = " + "'"+ (String) row1.get(categoryName)+"'"
                                                                                  + filterQuery1+reportMetaDataStr;
                                                                                  
                                                                    SQLQuery q = session.createSQLQuery(s1);
                                                                    q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                                                                   List res = q.list();
                                                                   if (res != null) {
	                                                                   	for (Iterator iter = res
	                                                                               .iterator(); iter
	                                                                               .hasNext();) {
	                                                                 
	                                                                        HashMap row2 = (HashMap) iter
	                                                                                      .next();
	                                                                        tempVal=  (Double) row2.get("sum(t.frequency)");
	                                                                   	}
                                                                   }
                                                            }
                                                            String value1 = (String) row1
                                                                    .get("lower("
                                                                                  + valuesColumnNames[0]
                                                                                  + ")");
                                                            
                                                            String label = (String) row1
                                                                         .get(categoryName);
                                                            if(seriesColumnNameBook != null){
                                                            	String recodeValue = (String)seriesColumnNameBook.get(label);
                                                            	if(!ApplicationUtil.isEmptyOrNull(recodeValue)){
                                                            		label= recodeValue;
                                                            	}
                                                            }
                                                            int n=0;
                                                            if(!functionName.equals("SENTIMENT") && !functionName.equals("CATEGORICAL_ANALYSIS")){
                                                            	  if(functionName.equals("FREQUENCY")){
                                                                  	label+= " (N = "+value1+")";
                                                                  	n=Integer.parseInt(value1);
                                                                    	 
                                                                     }
                                                                  if (functionName.equals("AVERAGE") && tempVal != null) {
                                                                  	label+= " (N = "+tempVal.intValue()+")";
                                                                  	n=tempVal.intValue();
                                                                     }
                                                            }
                                                          
                                                            if(!functionName.equals("SENTIMENT") && !functionName.equals("CATEGORICAL_ANALYSIS")){
                                                            	tableList4.add(label);
                                                            }
                                                            String value = (String) row1
                                                                         .get("lower("
                                                                                       + valuesColumnNames[0]
                                                                                       + ")");
                                                            
                                                            if(n<=level_threshold && !isFromSentiment  && !functionName.equals("CATEGORICAL_ANALYSIS")){
                                                            	
                                                            	if(isFromCombinationChart){
                                                                       dataSetbuffer.append("<set value=''/>");
                                                                }else{
                                                                  		dataSetbuffer.append("<set label='"
                                                                                  + label + "' value='' color='"
                                                                                  + chartColorCode[0]
                                                                                  +"' />");
                                                                	}
                                                                 tableList5.add("");
                                                            	
                                                            	
                                                            }else{
                                                            	if(isFromSentiment){
                                                            		Double negative = (Double.parseDouble(value)/(double)noOfResponses)*100;
                                                            		Double others = 100-negative;
                                                            		dataSetbuffer.append("<set label='"
                                                                            + WordUtils.capitalize(label) + "' value='" + negative+ "' color='DF3935' />");
                                                            		dataSetbuffer.append("<set label='Others' value='" + others+ "' color='c4c4c4' />");
                                                            		tableList4.add(WordUtils.capitalize(label));
                                                            		tableList4.add("Others");
                                                            		tableList5.add(negative.toString());
                                                            		tableList5.add(others.toString());
                                                            	}else {
                                                            		if(isFromCombinationChart){
                                                                        dataSetbuffer.append("<set value='" + Double.parseDouble(value)*percentage_multiplier+ "'/>");
                                                                  }else{
                                                                  		 dataSetbuffer.append("<set label='"
                                                                                   + label + "' value='" + Double.parseDouble(value)*percentage_multiplier
                                                                                   + "' color='"
                                                                                   + chartColorCode[0]
                                                                                   +"' />");
                                                                  }
                                                                  tableList5.add(value);
                                                            	}
                                                            
                                                            }
                                                            
                                                     }
                                                     index++;
                                                     if(isFromCombinationChart){
                                                            dataSetbuffer.append("</lineSet>");
                                                     }
                                                     
                                                     tableViewList.add(tableList4);
                                                     tableViewList.add(tableList5);
                                              }
                                       }
                                }
                         }
                         categoryBuffer.append(dataSetbuffer.toString());
                         tableViewObj.put("xmlStr",categoryBuffer.toString());
                         tableViewObj.put("tableViewList", tableViewList);
                         tableViewObj.put("referenceGroupValues", referenceGroupValues);

                  } catch (HibernateException he) {
                         throw he;
                  }
                  return tableViewObj;
            }
     });
     logger.debug("Leaving from getChartXmlFromDatabase");
     return (Map) object;
     }catch (Exception e) {
    	 e.printStackTrace();
    	 logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
    	 logger.debug(e.getStackTrace());
		throw e;
	}
	
}

	public Map getPlotChartXmlFromDatabase(final String templateChartInfoId,final String empId,final String tableName,
            final String categoryName, final String seriesColumnName,
            final String valuesColumnNames[],
            final String filterName1,final String filterValue1,final String xAxis,final String yAxis,final boolean isFromFilterChange,
            final String whereConstrain,final String filterVariable,final Integer reportMetaDataId) {
		try{
			logger.debug("Entered into getPlotChartXmlFromDatabase");
			final Map variableNamesObj=(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.VARIABLE_NAMES_MAP_CACHE_KEY);
            Object object = hibernateTemplate.execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                         throws HibernateException, SQLException {
            	  Map kdaTableViewMapObj = new HashMap();
            	  List kdaTableList = new ArrayList();
                  StringBuffer rootBuffer = new StringBuffer();
                  StringBuffer categoryBuffer = new StringBuffer();
                  StringBuffer dataSetbuffer = new StringBuffer();
                  String xAxisYaxisLimits="";
                  try {
                	  String templateChartIdStr="";
                      if(!ApplicationUtil.isEmptyOrNull(templateChartInfoId)){
                    	  templateChartIdStr = "templateChartInfoId="+templateChartInfoId;
                      }else{
                    	  templateChartIdStr =  "templateChartInfoId is NULL";
                      }
                	  	 String empQueryStr="";
                	  	String empQueryStrWithAlias="";
                	  	 if(!ApplicationUtil.isEmptyOrNull(empId)){
                	  		empQueryStr = "and employeeId="+empId;
                	  		empQueryStrWithAlias="and t.employeeId="+empId;
                	  	 }else{
                	  		empQueryStr = "and employeeId is NULL";
                	  		empQueryStrWithAlias="and t.employeeId is NULL";
                	  	 }
                         String filterQuery1 ="";
                         if(!ApplicationUtil.isEmptyOrNull(filterName1)){
                        	 if(!ApplicationUtil.isEmptyOrNull(filterValue1)){
                        		 filterQuery1 = " and " + filterName1 + " = '" + filterValue1 + "'";
                        	 }else{
                        		 filterQuery1 = " and " + filterName1 + " is NULL";
                        	 }
                         }
                        String referenceGroupValues =""; 
                        String sqlQuery ="select  group_concat(distinct "+filterVariable+" separator ',') from data where 1=1 "+whereConstrain;
     					ResultSet  rs = session.connection().createStatement().executeQuery(sqlQuery);
     					while(rs.next()){
     						referenceGroupValues = rs.getString(1);
     					}
     					String reportMetaDataStr="";
     					if(reportMetaDataId!=null){
     						reportMetaDataStr = "  and  reportMetaDataId="+reportMetaDataId;
     					}
                         if (!ApplicationUtil.isEmptyOrNull(seriesColumnName) && !seriesColumnName.equals("-1")) {
							   double maxAvgPerformanceValue =0.00;      
							   double minAvgPerformanceValue =0.00;
							   double minImportanceValue = 0.00;
							   double maxImportanceValue = 0.00;
							   double totalCount=0;
                        	   double totalAvgPerformanceVal=0.0;
                        	   double totalImportanceVal=0.0;
                        	   double totalAveragePerformanceVal=0.0;
                                String sql = "SELECT distinct "+ seriesColumnName + " FROM " + tableName
                                              + " where " + seriesColumnName
                                              + " != \"\" " + filterQuery1 + " " + empQueryStr +" and "+templateChartIdStr+reportMetaDataStr;
                                SQLQuery query = session.createSQLQuery(sql);
                                query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                                List kdaList =new ArrayList();
                                kdaList.add("Variables");
                                kdaList.add("Relative Scores");
                                kdaList.add("Importance");
                                kdaTableList.add(kdaList);
                                List results = query.list();
                                if (results != null) {
                                       int index =0;
                                       for (Iterator it = results.iterator(); it.hasNext();) {
                                              HashMap row = (HashMap) it.next();
                                              List kdaList1 = new ArrayList();
                                              String val = (String) row.get(seriesColumnName);
                                              if (!ApplicationUtil.isEmptyOrNull(val)) {
                                            	  String columnName="";
                                            	  if(variableNamesObj != null){
                                            		  columnName= (String)variableNamesObj.get(val.toLowerCase());
                                            	  }
                                            	  kdaList1.add(columnName);
                                                            dataSetbuffer.append("<dataset seriesName='"
                                                                                       + columnName.toUpperCase()
                                                                                       + "' color='"
                                                                                       + ApplicationConstants.DEFAULT_COLORS[index]
                                                                                       + "' " 
                                                                                       + "anchorsides='4' " 
                                                                                       + "anchorradius='5' " 
                                                                                       + "anchorbgcolor='C6C6FF' "
                                                                                       + "anchorbordercolor='0000FF'" 
                                                                                       +">");

                                                     if (valuesColumnNames != null
                                                                  && valuesColumnNames.length > 0) {
                                                            int length = valuesColumnNames.length;
                                                            for (int i = 0; i < length; i++) {
                                                                  String valuesColumnName = valuesColumnNames[i];

                                                                  StringBuffer sb = new StringBuffer();
                                                                  sb.append("select lower(t."
                                                                                + valuesColumnName
                                                                                + "),t."
                                                                                + categoryName
                                                                                + " from "
                                                                                + tableName
                                                                                + " t where 1=1 "+empQueryStrWithAlias 
                                                                                + " and "+templateChartIdStr
                                                                                + " and t. "
                                                                                + seriesColumnName
                                                                                + "='"
                                                                                + val + "' " + filterQuery1+reportMetaDataStr);

                                                                  query = session.createSQLQuery(sb
                                                                                .toString());
                                                                   query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                                                                  List results1 = query.list();
                                                                   if (results1 != null) {
                                                                         for (Iterator it1 = results1
                                                                                       .iterator(); it1
                                                                                       .hasNext();) {
                                                                        	 
                                                                                HashMap row1 = (HashMap) it1
                                                                                              .next();
                                                                                String val2 = (String) row1.get("lower(t."
                                                                                                            + valuesColumnName
                                                                                                            + ")");
                                                                                String val1 = (String) row1.get(categoryName);
                                                                                double avgPerformanceVal = new Double(val1).doubleValue();
                                                                                avgPerformanceVal = Math.round(avgPerformanceVal*100.0)/100.0;
                                                                                
                                                                                kdaList1.add(Math.round(avgPerformanceVal*100.0)/100.0+"");
                                                                                kdaList1.add(val2);
                                                                                double importanceVal = new Double(val2).doubleValue();
                                                                                totalImportanceVal=totalImportanceVal+importanceVal;
                                                                                totalAveragePerformanceVal = totalAveragePerformanceVal+avgPerformanceVal;
                                                                                totalAvgPerformanceVal=totalAvgPerformanceVal+avgPerformanceVal;
                                                                                totalCount=totalCount+1;
                                                                                if(minImportanceValue==0.00){
                                                                                	if(!ApplicationUtil.isEmptyOrNull(val2)){
                                                                                		minImportanceValue = importanceVal;
                                                                                	}
                                                                                }else{
                                                                                	if(minImportanceValue>importanceVal){
                                                                                		minImportanceValue = importanceVal;
                                                                                	}
                                                                                }
                                                                                
                                                                                if(maxImportanceValue==0.00){
                                                                                	if(!ApplicationUtil.isEmptyOrNull(val2)){
                                                                                		maxImportanceValue = importanceVal;
                                                                                	}
                                                                                }else{
                                                                                	if(maxImportanceValue<avgPerformanceVal){
                                                                                		maxImportanceValue = importanceVal;
                                                                                	}
                                                                                }
                                                                                
                                                                                if(maxAvgPerformanceValue==0.00){
                                                                                	if(!ApplicationUtil.isEmptyOrNull(val1)){
                                                                                		maxAvgPerformanceValue = avgPerformanceVal;
                                                                                	}
                                                                                }else{
                                                                                	if(maxAvgPerformanceValue<avgPerformanceVal){
                                                                                		maxAvgPerformanceValue = avgPerformanceVal;
                                                                                	}
                                                                                }
                                                                                
                                                                                if(minAvgPerformanceValue==0.00){
                                                                                	if(!ApplicationUtil.isEmptyOrNull(val1)){
                                                                                		minAvgPerformanceValue = avgPerformanceVal;
                                                                                	}
                                                                                }else{
                                                                                	if(minAvgPerformanceValue>avgPerformanceVal){
                                                                                		minAvgPerformanceValue = avgPerformanceVal;
                                                                                	}
                                                                                }
                                                                                dataSetbuffer.append("<set x='"+ val1+ "' y='"+ val2+ "'/>");
                                                                         }
                                                                  }
                                                            }
                                                     }
                                                     dataSetbuffer.append("</dataset>");
                                              }
                                              index++;
                                              kdaTableList.add(kdaList1);
                                       }
                                }
                                categoryBuffer.append("<categories>");
                               
                                double maxAvgPerformanceVal=new Double(maxAvgPerformanceValue+(maxAvgPerformanceValue*5)/100);
                                if(maxAvgPerformanceVal>0){
                                	maxAvgPerformanceVal=Math.round(maxAvgPerformanceVal*100.0)/100.0;
                                }
                                double minAvgPerformanceVal = new Double(minAvgPerformanceValue - (minAvgPerformanceValue*5)/100);
                                minAvgPerformanceVal=Math.round(minAvgPerformanceVal*100.0)/100.0;
                                if(minAvgPerformanceVal>0){
                                	minAvgPerformanceVal=Math.round(minAvgPerformanceVal*100.0)/100.0;
                                }
                                int minImportanceVal = new Double(minImportanceValue - (minImportanceValue*5)/100).intValue();
                                if(minImportanceVal>0){
                                	minImportanceVal=Math.round(minImportanceVal);
                                }
                                int maxImportanceVal=new Double(maxImportanceValue+(maxImportanceValue*5)/100).intValue();
                                if(maxImportanceVal>0){
                                	maxImportanceVal=Math.round(maxImportanceVal);
                                }
                                double index=0.02;
                                xAxisYaxisLimits = (minAvgPerformanceVal-index)+"@"+maxAvgPerformanceVal+"@"+minImportanceVal;
                                double importanceTrendLine = totalImportanceVal/totalCount;
							    if(importanceTrendLine>0){
							    	importanceTrendLine=Math.round(importanceTrendLine);
							    }
							    double performanceTrendLine = totalAveragePerformanceVal/totalCount;
							    performanceTrendLine = Math.round(performanceTrendLine*100.0)/100.0;
                                if(isFromFilterChange){
	                                rootBuffer.append("<chart xAxisName='Relative Scores' showLegend='0' yAxisName='Importance' labelDisplay='WRAP' divLineColor='FFFFFF' divLineAlpha='0' ");
	                                rootBuffer.append("formatNumberScale='1' placeValuesInside='1' decimalPrecision='2' xAxisMaxValue='"+maxAvgPerformanceVal+"'  ");
	                                rootBuffer.append("xAxisMinValue='"+(minAvgPerformanceVal-index)+"' yAxisMinValue='"+minImportanceVal+"' yAxisMaxValue='"+maxImportanceVal+"' exportEnabled='0' exportAtClient='0'  ");
	                                rootBuffer.append("exportHandler='index.php' bgAlpha='50' showAlternateHGridColor='0' borderThickness='0' ");
	                                rootBuffer.append("plotGradientColor='' useRoundEdges='0' legendBorderAlpha='0' ");
	                                rootBuffer.append(" drawQuadrant='1' quadrantLabelTL='Primary Focus' quadrantLabelTR='Strengths' quadrantLabelBL='Secondary Focus' quadrantLabelBR='Hygiene' quadrantLineThickness='1' ");
	                                rootBuffer.append("quadrantXVal='" + performanceTrendLine + "' ");
	                                rootBuffer.append("quadrantYVal='" + importanceTrendLine + "' ");
	                                rootBuffer.append("bgcolor='FFFFFF' showplotborder='0' showshadow='0' canvasborderalpha='10'  ");
	                                rootBuffer.append("canvasbordercolor='#999999' canvasbgratio='0' chartRightMargin='15' chartTopMargin='15'  ");
	                                rootBuffer.append("canvasbgalpha='0' bordercolor='8F8F8F' borderalpha='50' bgratio='0' showborder='0'  ");
	                                rootBuffer.append("legendShadow='0' showValues='0' maxLabelWidthPercent='20' > ");
	                                rootBuffer.append("<styles>  ");
	                                rootBuffer.append("<definition> <style name='myCaptionFont' type='font' font='Segoe UI' size='18' color='666666' bold='1' underline='1' /> ");
	                                rootBuffer.append("<style name='myYAxisTitlesFont' type='font' size='12' /><style name='myXAxisTitlesFont' type='font'  size='12' /></definition>  ");
	                                rootBuffer.append("<application> <apply toObject='Caption' styles='myCaptionFont' /><apply toObject='YAxisName' styles='myYAxisTitlesFont' />  ");
	                                rootBuffer.append("<apply toObject='XAxisName' styles='myXAxisTitlesFont' /></application>  ");
	                                rootBuffer.append("</styles> ");
                                }
                                double lastCategoryIndex = 0;
                                int index1=10;
                                int lastCategoryIndex1=0;
                                
                                if(ApplicationUtil.isEmptyOrNull(referenceGroupValues) || referenceGroupValues.equals("-1")){
                                	
                                	if(!ApplicationUtil.isEmptyOrNull(filterName1)){
	                                	index1=5;
	                                	minAvgPerformanceVal=(int)minAvgPerformanceVal;
	                                	maxAvgPerformanceVal = Math.abs(minAvgPerformanceVal);
                                	}
                                	 /*for(int i=(int) minAvgPerformanceVal;i<(int)maxAvgPerformanceVal;){
                                    	 categoryBuffer.append("<category label='"+i+"' x='"+i+"'  />");
                                    		 i=i+index1;
                                    		 lastCategoryIndex1=i;
                                    }
                                    if((int)maxAvgPerformanceValue>lastCategoryIndex1-index1){
                                    	categoryBuffer.append("<category label='"+((int)maxAvgPerformanceVal)+"' x='"+((int)maxAvgPerformanceVal)+"'  />");
                                    }*/
                                    for(double i=minAvgPerformanceVal;i<maxAvgPerformanceVal;){
                                      	 categoryBuffer.append("<category label='"+Math.round(i*100.0)/100.0+"' x='"+Math.round(i*100.0)/100.0+"'  />");
                                      		 i=i+index1;
                                      		 lastCategoryIndex=i;
                                      }
                                      if(maxAvgPerformanceValue>lastCategoryIndex-index){
                                      	categoryBuffer.append("<category label='"+(maxAvgPerformanceVal)+"' x='"+(maxAvgPerformanceVal)+"'  />");
                                      }
                                }else{
                                	for(double i=minAvgPerformanceVal;i<maxAvgPerformanceVal;){
                                   	 categoryBuffer.append("<category label='"+Math.round(i*100.0)/100.0+"' x='"+Math.round(i*100.0)/100.0+"'  />");
                                   		 i=i+index;
                                   		 lastCategoryIndex=i;
                                   }
                                   if(maxAvgPerformanceValue>lastCategoryIndex-index){
                                   	categoryBuffer.append("<category label='"+(maxAvgPerformanceVal)+"' x='"+(maxAvgPerformanceVal)+"'  />");
                                   }
                                	
                                }
							    categoryBuffer.append("</categories>");
							    dataSetbuffer.append("<trendlines>");
                    		    dataSetbuffer.append("<line startvalue='"+minImportanceVal+"' endvalue='"+importanceTrendLine+"' displayvalue=' ' istrendzone='1' color='FF0000' valueonright='1'/>");
                    		    dataSetbuffer.append("<line startvalue='"+importanceTrendLine+"' endvalue='100' displayvalue=' ' istrendzone='1' color='F89900' valueonright='1'/>");
                                dataSetbuffer.append("</trendlines>");
                                dataSetbuffer.append("<vtrendlines>");
                    		    dataSetbuffer.append("<line startvalue='"+(minAvgPerformanceVal-index)+"' endvalue='"+performanceTrendLine+"' displayvalue=' ' istrendzone='1' color='009900' alpha='10' />");
							    dataSetbuffer.append("</vtrendlines>");
							    kdaTableViewMapObj.put("ImportanceTrendLine", importanceTrendLine);
		                        kdaTableViewMapObj.put("PerformanceTrendLine", performanceTrendLine);
                         } 
                         
                         categoryBuffer.append(dataSetbuffer.toString());
                         rootBuffer.append(categoryBuffer.toString());
                         if(isFromFilterChange){
                        	 rootBuffer.append("</chart>@KDA");
                         }else{
                        	 rootBuffer.append("@"+xAxisYaxisLimits);
                         }
                         kdaTableViewMapObj.put("kdaXmlStr", rootBuffer.toString());
                         kdaTableViewMapObj.put("kdaTableList", kdaTableList);
                         kdaTableViewMapObj.put("referenceGroupValues", referenceGroupValues);
                        
                  } catch (HibernateException he) {
                         throw he;
                  }
                  return kdaTableViewMapObj;
            }
     });
     logger.debug("Leaving from getPlotChartXmlFromDatabase");
     return (Map) object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
}
	
	public List getDateForSelectedVariable(final String selectedVariable) throws ApplicationException
	{
		try{
		logger.debug("Entered into getDateForSelectedVariable");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				List dataList = null;
				try {
                		 String sql = "select distinct "+selectedVariable+" from data";
	        			 SQLQuery q = session.createSQLQuery(sql);
	        			 dataList =  (List)q.list();
        			return dataList;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from getDateForSelectedVariable");
	    return (List)object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	}
	
	public List getSelectedDistributionList(final String selectedDisributionVariable) throws ApplicationException
	{
		try{
		logger.debug("Entered into getSelectedDistributionList");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				List dataList = null;
				try {
                		 String sql = "from MasterDistributionType where overAll='"+selectedDisributionVariable+"'";
                		 Query q = session.createQuery(sql);
	        			 dataList =  (List)q.list();
        			return dataList;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from getSelectedDistributionList");
	    return (List)object;
	}catch (Exception e) {
		logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
		throw e;
	}
	}
	
	public List getFilterValues(final String templateChartInfoId,final String empId,final String tableName,final String filterColumn,final Integer reportMetaDataId) 
	{
		try{
		logger.debug("Entered into getFilterValues");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				List dataList = null;
				try {
						Query q=null;
						if(reportMetaDataId!=null){
							if(!ApplicationUtil.isEmptyOrNull(empId) && !ApplicationUtil.isEmptyOrNull(templateChartInfoId)){
								String sql = "select distinct "+filterColumn+" from "+tableName+" where templateChartInfoId=? and employeeId=? and reportmetadataId=?";
								q = session.createSQLQuery(sql).setString(0, templateChartInfoId).setString(1, empId).setInteger(2, reportMetaDataId);
							}else if(!ApplicationUtil.isEmptyOrNull(templateChartInfoId)){
								String sql = "select distinct "+filterColumn+" from "+tableName+" where templateChartInfoId=? and employeeId is null and reportmetadataId=?";
								q = session.createSQLQuery(sql).setString(0, templateChartInfoId).setInteger(1, reportMetaDataId);
							}
							else
							{
								String sql = "select distinct "+filterColumn+" from "+tableName+" where templateChartInfoId is null and employeeId is null and reportmetadataId=?";
								q = session.createSQLQuery(sql).setInteger(0, reportMetaDataId);;
							}
							
						} else {
							if(!ApplicationUtil.isEmptyOrNull(empId) && !ApplicationUtil.isEmptyOrNull(templateChartInfoId)){
								String sql = "select distinct "+filterColumn+" from "+tableName+" where templateChartInfoId=? and employeeId=?";
								q = session.createSQLQuery(sql).setString(0, templateChartInfoId).setString(1, empId);
							}else if(!ApplicationUtil.isEmptyOrNull(templateChartInfoId)){
								String sql = "select distinct "+filterColumn+" from "+tableName+" where templateChartInfoId=? and employeeId is null";
								q = session.createSQLQuery(sql).setString(0, templateChartInfoId);
							}
							else
							{
								String sql = "select distinct "+filterColumn+" from "+tableName+" where templateChartInfoId is null and employeeId is null";
								q = session.createSQLQuery(sql);
							}
						}
						
						
	        			dataList =  (List)q.list();
        			return dataList;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from getFilterValues");
	    return (List)object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
		
	}
	
	public void refreshReports(final TemplateChart templateChart) throws ApplicationException
	{
		try{
			logger.debug("Entered into refreshReports");
			Object object = hibernateTemplate.execute(new HibernateCallback() {
			@SuppressWarnings("unused")
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				List dataList = null;
				Integer reportMetaDataId = templateChart.getReportMetaDataId();
				String wnsBenchmarkColumn = PropertiesUtil.getProperty(ApplicationConstants.WNS_BENCH_MARK_COLUMN_NAME);
				if(!ApplicationUtil.isEmptyOrNull(templateChart.getIsOverall())
						&& !templateChart.getIsOverall().equals("Y")){
				session.connection().setAutoCommit(true);
				Map functionMapObj = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.MASTER_FUNCTIONS_MAP_CACHE_KEY);
				Map variableNamesObj=(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.VARIABLE_NAMES_MAP_CACHE_KEY);
				List surveyVariableObjList = (List)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.SURVEY_VARIABLES_LIST);
				String minimumRespondentsStr = PropertiesUtil.getProperty(ApplicationConstants.MINIMUM_RESPONDENTS);
				int minimumRespondents = 0;
				if(!ApplicationUtil.isEmptyOrNull(minimumRespondentsStr)){
					minimumRespondents = Integer.parseInt(minimumRespondentsStr);
				}
				String employeeId="";
				String filter1=templateChart.getFilter1();
				String filter2=templateChart.getFilter2();
				String filter3=templateChart.getFilter3();
				String filterValue1=templateChart.getFilterValues1();
				String filterValue2=templateChart.getFilterValues2();
				String filterValue3=templateChart.getFilterValues3();
				
				Long createdBy = templateChart.getCreatedBy();
				Long updatedBy = templateChart.getUpdatedBy();
				Date createdDate = templateChart.getCreatedDate();
				Date updatedDate = templateChart.getUpdatedDate();
				
				String sql = "select group_concat(employeeId separator ',') from employee where emailId in (select emailId from distributionlist left join master_distribution_type on master_distribution_type.id = distributionlist.masterDistributionTypeId where overAll='N' and masterDistributionTypeId in (select masterDistId from templatechartreportassign where templateChartId = "+templateChart.getId()+"))";
				ResultSet empIdResult = session.connection().createStatement().executeQuery(sql);
				empIdResult.next();
				String tempStr=empIdResult.getString(1);
				String empIdArr[]=null;
				if(tempStr!=null && tempStr.length()>0)
					empIdArr = tempStr.split(",");
				
				System.out.println(templateChart.getId());
				//System.out.println(templateChart.toString());
 				
				String whereConstrain="";
				String whereConstrainWithAlias="";
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
					whereConstrainWithAlias=" t1."+filter1+" in ("+filterValue1+") ";
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
					whereConstrainWithAlias = (whereConstrainWithAlias.length()>0)?whereConstrainWithAlias+" and ":whereConstrainWithAlias;
					whereConstrainWithAlias+=" t1."+filter2+" in ("+filterValue2+") ";
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
					whereConstrainWithAlias = (whereConstrainWithAlias.length()>0)?whereConstrainWithAlias+" and ":whereConstrainWithAlias;
					whereConstrainWithAlias+=" t1."+filter3+" in ("+filterValue3+") ";
				}
					if(whereConstrain.length()>0)
						whereConstrain = "and "+whereConstrain;
					if(whereConstrainWithAlias.length()>0)
						whereConstrainWithAlias = "and "+whereConstrainWithAlias;
					StringBuffer columnBufferWhereClauseStr = new StringBuffer();				
				try {
					 sql = "SELECT * FROM templatechartinfo where templateChartCategoryId in (SELECT id FROM templatechartcategory where templateChartId = "+templateChart.getId()+") and functionid in (SELECT id FROM essdatabase.master_functions where logicalname in ('FREQUENCY','AVERAGE','BANNER TABLE'))";
        			 SQLQuery q = session.createSQLQuery(sql);
        			 q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        			 dataList =  (List)q.list();
        			 Iterator i = dataList.iterator();
        			 while(i.hasNext())
        			 {	HashMap map = (HashMap)i.next();
        				 String functionName= (String) functionMapObj.get((Integer)map.get("functionId"));
        				 String x_axis = (String) map.get("xAxis");
        				 String y_axis = (String) map.get("yAxis");
        				 String weight = (String)map.get("weightVariable");
        				 String filter = (String)map.get("filter");
        				 Integer incl = (Integer) map.get("meanIncl");
        				 Integer excl = (Integer) map.get("meanExcl");
        				 Integer medianIncl = (Integer) map.get("medianIncl");
        				 Integer medianExcl = (Integer) map.get("medianExcl");
        				 String crossTabValue = (String)map.get("crossTabValue");
        				 //(map.get("filter")==null)?null:(String)map.get("filter");
        				 //System.out.println(filter.length()+"- filter length");
        				 String sql1 = "";
        				 String sql2 = "";
        				 String mean = "";
        				 String mean1 = "";
        				 String stdDev = "";
        				 String median = "";
        				 String median1 = "";
        				 String stdErr = "";
        				 String sql3 = "";
        				 
        				 
        				 Integer id = (Integer)map.get("id");
        				 
    					 sql3 = "delete from chartaverage where templateChartInfoId ="+id;
    					 session.connection().createStatement().executeUpdate(sql3);
    					 sql3= "delete from chartfrequencies where templateChartInfoId ="+id;
    					 session.connection().createStatement().executeUpdate(sql3);
    					 sql3= "delete from bannertable where templateChartInfoId ="+id;
    					 session.connection().createStatement().executeUpdate(sql3);
    					 sql3= "delete from benchmark where templateChartInfoId ="+id;
    					 session.connection().createStatement().executeUpdate(sql3);
    					 
        				 ResultSet rs = null;
        				 
        				 if(ApplicationUtil.isEmptyOrNull(filter) || filter.equals("-1")){
        					 filter=null;
        				 }
        				 if(ApplicationUtil.isEmptyOrNull(y_axis) || y_axis.equals("-1")){
        					 y_axis=null;
        				 }
        				 
        				 for(int k=0;(empIdArr!=null && k<empIdArr.length);k++){
                             employeeId = empIdArr[k];
                             sql1="";
                             sql2="";
                             mean = "";
            				 mean1 = "";
            				 stdDev = "";
            				 median = "";
            				 median1 ="";
            				 stdErr="";
                             StringBuffer columnBuffer = new StringBuffer();
                             columnBufferWhereClauseStr = new StringBuffer();
                                           columnBuffer.append("select count(employeeId) from data as d where ");
                                           if(surveyVariableObjList !=null){
                                                  int col = surveyVariableObjList.size();
                                                  for(int l=0;l<col;l++){
                                                       VariableNames variableNames = (VariableNames)surveyVariableObjList.get(l);
		                                                String columnName = variableNames.getColumnName();
		                                                columnBuffer.append(" d."+columnName+ " IS NULL");
		                                                columnBufferWhereClauseStr.append(" d."+columnName+ " IS NOT NULL");
		                                                if(l+1<col){
		                                                       columnBuffer.append(" " +"and" + " ");
		                                                       columnBufferWhereClauseStr.append(" " +"or" + " ");
		                                                }
                                                  }
                                           }
                                           String colbuff="";
                                           String functionColBufferStr=columnBuffer.toString();
                                           if(columnBuffer.length()>0){
                                        	   columnBuffer.append(" and employeeId in (select employee from hierarchy where manager = "+employeeId+") "+whereConstrain);
                                                  colbuff =" - ("+columnBuffer.toString()+") ";
                                                  functionColBufferStr=" - ("+functionColBufferStr+" "+whereConstrain+") "; 
                                           }
                                          if(ApplicationUtil.isEmptyOrNull(employeeId)){
                                        	  sql="select (select count(*) from data as d where 1=1 "+whereConstrain+")" +functionColBufferStr;
                                          }else{
                                                 sql="select (select count(*) from data as d where employeeId in (select employee from hierarchy where manager = "+employeeId+")"+whereConstrain+")" +colbuff;
                                          }
                                          ResultSet rs1 = session.connection().createStatement().executeQuery(sql);
                                          rs1.next();
                                          Integer responses=rs1.getInt(1);
                                      
                                if(responses>minimumRespondents)
                                {
                                	String displayDecimal=PropertiesUtil.getProperty("displayDecimal");
                                	String roundNum ="2";
                            		if(displayDecimal.equalsIgnoreCase("true")){
                            			roundNum = "0";
                            		}
		        				 if (functionName.equals("AVERAGE")) {
			        					 if(x_axis!=null && x_axis.length()>0){
			        					 		if(x_axis.contains(",")){
			        					 			String x_axis_arr[] = x_axis.split(",");
			        					 			int j=0;
		
			        					 			sql1 = "insert into chartaverage(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,frequency) ";
			        					 			sql2 = "insert into chartaverage(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,frequency) ";
			        					 			if(y_axis!=null && y_axis.length()>0){
			        					 				for(;j<(x_axis_arr.length-1);j++){
			        					 					String variableName = (String)variableNamesObj.get(x_axis_arr[j].toLowerCase());
			        					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
			        					 						variableName = x_axis_arr[j];
			        					 					}
				        					 				sql1+="select "+id+","+employeeId+","+y_axis+",'"+variableName+"',null,round(avg("+x_axis_arr[j]+"),"+roundNum+"), count("+x_axis_arr[j]+") from data where  employeeId in (select employee from hierarchy where manager in ("+employeeId+")) and "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereConstrain+" group by "+y_axis+" union ";
				        					 				sql2+="select "+id+","+employeeId+","+y_axis+",'"+variableName+"',"+filter+",round(avg("+x_axis_arr[j]+"),"+roundNum+"), count("+x_axis_arr[j]+") from data where  employeeId in (select employee from hierarchy where manager in ("+employeeId+")) and "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null and "+filter+" is not null "+whereConstrain+" group by "+y_axis+","+filter+" union ";
				        					 				}
			        					 				String variableName = (String)variableNamesObj.get(x_axis_arr[x_axis_arr.length-1].toLowerCase());
		        					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
		        					 						variableName = x_axis_arr[x_axis_arr.length-1];
		        					 					}
			        					 				sql1+="select "+id+","+employeeId+","+y_axis+",'"+variableName+"',null,round(avg("+x_axis_arr[j]+"),"+roundNum+"), count("+x_axis_arr[j]+") from data where  employeeId in (select employee from hierarchy where manager in ("+employeeId+")) and "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereConstrain+"  group by "+y_axis;
			        					 				sql2+="select "+id+","+employeeId+","+y_axis+",'"+variableName+"',"+filter+",round(avg("+x_axis_arr[j]+"),"+roundNum+"), count("+x_axis_arr[j]+") from data where  employeeId in (select employee from hierarchy where manager in ("+employeeId+")) and "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null and "+filter+" is not null "+whereConstrain+" group by "+y_axis+","+filter;
			        					 				
			        					 			
			        					 			}else{
			        					 				for(;j<(x_axis_arr.length-1);j++){
			        					 					String variableName = (String)variableNamesObj.get(x_axis_arr[j].toLowerCase());
			        					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
			        					 						variableName = x_axis_arr[j];
			        					 					}
			        					 				sql1+="select "+id+","+employeeId+",'"+variableName+"','Average Scores',null,round(avg("+x_axis_arr[j]+"),"+roundNum+"), count("+x_axis_arr[j]+") from data where  employeeId in (select employee from hierarchy where manager in ("+employeeId+")) and "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereConstrain+" union ";
			        					 				sql2+="select "+id+","+employeeId+",'"+variableName+"','Average Scores',"+filter+",round(avg("+x_axis_arr[j]+"),"+roundNum+"), count("+x_axis_arr[j]+") from data where  employeeId in (select employee from hierarchy where manager in ("+employeeId+")) and "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereConstrain+" group by "+filter+" union ";
			        					 				}
			        					 				String variableName = (String)variableNamesObj.get(x_axis_arr[x_axis_arr.length-1].toLowerCase());
		        					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
		        					 						variableName = x_axis_arr[x_axis_arr.length-1];
		        					 					}
			        					 				sql1+="select "+id+","+employeeId+",'"+variableName+"','Average Scores',null,round(avg("+x_axis_arr[j]+"),"+roundNum+"), count("+x_axis_arr[j]+") from data where  employeeId in (select employee from hierarchy where manager in ("+employeeId+")) and "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereConstrain+" ";
			        					 				sql2+="select "+id+","+employeeId+",'"+variableName+"','Average Scores',"+filter+",round(avg("+x_axis_arr[j]+"),"+roundNum+"), count("+x_axis_arr[j]+") from data where  employeeId in (select employee from hierarchy where manager in ("+employeeId+")) and "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereConstrain+" group by "+filter+"";
			        					 			}
			        					 				
			        					 		}
			        					 		else if(y_axis!=null && y_axis.length()>0){
			        					 			String variableName = (String)variableNamesObj.get(x_axis.toLowerCase());
		    					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
		    					 						variableName = x_axis;
		    					 					}
			        					 			sql1 = "insert into chartaverage(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,frequency) select "+id+","+employeeId+","+y_axis+",'"+variableName+"',null,round(avg("+x_axis+"),"+roundNum+"), count("+x_axis+") from data where  employeeId in (select employee from hierarchy where manager in ("+employeeId+")) and "+x_axis+" != '' and "+x_axis+" is not null "+whereConstrain+" group by "+y_axis;
			        					 			sql2 = "insert into chartaverage(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,frequency) select "+id+","+employeeId+","+y_axis+",'"+variableName+"',"+filter+",round(avg("+x_axis+"),"+roundNum+"), count("+x_axis+") from data where  employeeId in (select employee from hierarchy where manager in ("+employeeId+")) and "+x_axis+" != '' and "+x_axis+" is not null "+whereConstrain+" group by "+y_axis+","+filter;
			        					 		}
			        					 		else{
			        					 			String variableName = (String)variableNamesObj.get(x_axis.toLowerCase());
		    					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
		    					 						variableName = x_axis;
		    					 					}
			        					 			whereConstrain = (whereConstrain.length()>0)?" and "+whereConstrain:"";
			        					 			sql1 = "insert into chartaverage(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,frequency) select "+id+","+employeeId+",'"+variableName+"',null,null,round(avg("+x_axis+"),"+roundNum+"), count("+x_axis+") from data where employeeId in (select employee from hierarchy where manager in ("+employeeId+")) and "+x_axis+" != '' and "+x_axis+" is not null "+whereConstrain+"";
			        					 			sql2 = "insert into chartaverage(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,frequency) select "+id+","+employeeId+",'"+variableName+"',null,"+filter+",round(avg("+x_axis+"),"+roundNum+"), count("+x_axis+") from data where employeeId in (select employee from hierarchy where manager in ("+employeeId+")) and "+x_axis+" != '' and "+x_axis+" is not null "+whereConstrain+" group by "+filter;
			        					 		}
			        					 	}
			     
			        					 	System.out.println(sql1);
			        					 	System.out.println(sql2);
			        					 			session.connection().createStatement().executeUpdate(sql1);
			        					 			if(filter!=null){
			        					 				session.connection().createStatement().executeUpdate(sql2);
			        					 			}
			        					 				
		        				 	}else if(functionName.equals("FREQUENCY")){
			        					 	if(x_axis!=null && x_axis.length()>0){
			        					 		if(x_axis.contains(",")){
			        					 			String x_axis_arr[] = x_axis.split(",");
			        					 			sql1 += "insert into chartfrequencies(templateChartInfoId,employeeId,xAxis,yAxis,filter,value) ";
			        					 			sql2 += "insert into chartfrequencies(templateChartInfoId,employeeId,xAxis,yAxis,filter,value) ";
			        					 			int length = x_axis_arr.length;
			        					 			for(int j=0;j<length;j++){
			        					 				String variableName = (String)variableNamesObj.get(x_axis_arr[j].toLowerCase());
		        					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
		        					 						variableName = x_axis_arr[j];
		        					 					}
			        					 				sql1 +=	"select "+id+","+employeeId+",'"+variableName+"',"+x_axis_arr[j]+",null,count("+x_axis_arr[j]+") from data where  employeeId in (select employee from hierarchy where manager in ("+employeeId+")) and "+x_axis_arr[j]+ " is not null "+whereConstrain+" group by "+x_axis_arr[j];
			        					 				sql2 += "select "+id+","+employeeId+",'"+variableName+"',"+x_axis_arr[j]+","+filter+",count("+x_axis_arr[j]+") from data where  employeeId in (select employee from hierarchy where manager in ("+employeeId+")) and "+x_axis_arr[j]+ " is not null "+whereConstrain+" group by "+x_axis_arr[j]+","+filter;
			        					 				for(int l=0;l<length;l++){
			        					 					if(x_axis_arr[j].equals(x_axis_arr[l])){
			        					 						continue;
			        					 					}
			        					 					sql1 += " union ";
			        					 					sql2 += " union ";
			        					 					sql1 +=	"select "+id+","+employeeId+",'"+variableName+"',"+x_axis_arr[l]+",null,0 from data where  employeeId in (select employee from hierarchy where manager in ("+employeeId+")) and "+x_axis_arr[l]+" is not null "+whereConstrain+" group by "+x_axis_arr[l];
				        					 				sql2 += "select "+id+","+employeeId+",'"+variableName+"',"+x_axis_arr[l]+","+filter+",0 from data where  employeeId in (select employee from hierarchy where manager in ("+employeeId+")) and "+x_axis_arr[l]+" is not null "+whereConstrain+" group by "+x_axis_arr[l]+","+filter;
			        					 				}
			        					 				if(j+1<length){
			        					 					sql1 += " union ";
			        					 					sql2 += " union ";
			        					 				}
			        					 			}
			        					 		}
			        					 		else if(y_axis!=null && y_axis.length()>0){ 
			        					 			sql1 = "insert into chartfrequencies(templateChartInfoId,employeeId,xAxis,yAxis,filter,value) (select "+id+","+employeeId+",t2."+y_axis+", t1."+x_axis+",null,count(1) from data t1 , data t2 where  t1.employeeId in (select employee from hierarchy where manager in ("+employeeId+")) and t1.employeeId = t2.employeeId and t1."+x_axis+" is not null and t2."+y_axis+" is not null and t1."+x_axis+" !='' and t2."+y_axis+" !=''  "+whereConstrainWithAlias+" group by t1."+x_axis+", t2."+y_axis+")";
			        					 			sql2 = "insert into chartfrequencies(templateChartInfoId,employeeId,xAxis,yAxis,filter,value) (select "+id+","+employeeId+",t2."+y_axis+", t1."+x_axis+",t1."+filter+",count(1) from data t1 , data t2 where  t1.employeeId in (select employee from hierarchy where manager in ("+employeeId+")) and t1.employeeId = t2.employeeId and t1."+x_axis+" is not null and t2."+y_axis+" is not null and t1."+x_axis+" !='' and t2."+y_axis+" !='' "+whereConstrainWithAlias+" group by t1."+x_axis+", t2."+y_axis+","+filter+")";
			        					 		}
			        					 		else{
		
			        					 			sql1 = "insert into chartfrequencies(templateChartInfoId,employeeId,xAxis,yAxis,filter,value) (select "+id+","+employeeId+","+x_axis+",null,null,count(1) from data where  employeeId in (select employee from hierarchy where manager in ("+employeeId+")) and "+x_axis+" is not null and "+x_axis+" !='' "+whereConstrain+" group by "+x_axis+")";
			        					 			sql2 = "insert into chartfrequencies(templateChartInfoId,employeeId,xAxis,yAxis,filter,value) (select "+id+","+employeeId+","+x_axis+",null,"+filter+",count(1) from data where employeeId in (select employee from hierarchy where manager in ("+employeeId+")) and "+x_axis+" is not null and "+filter+" is not null and "+x_axis+" !='' and "+filter+" !='' "+whereConstrain+" group by "+x_axis+","+filter+")";
			        					 		}
			        					 	}
			      
			        					System.out.println("Frequency---"+sql1);
			        					System.out.println("Frequency---"+sql2);
			        					    	session.connection().createStatement().executeUpdate(sql1);
			        					    	if(!ApplicationUtil.isEmptyOrNull(filter) && !filter.equals("-1")){
		        					 				session.connection().createStatement().executeUpdate(sql2);
		        					 			}
		        				 	}
		        				 	else if(functionName.equals("BANNER TABLE"))
		        				 	{
		        				 		if((x_axis!=null && x_axis.length()>0 )&& (y_axis!=null && y_axis.length()>0)){
		        				 			generateBannerTable(id,employeeId,null,x_axis, y_axis, weight, incl, excl, medianIncl, medianExcl,"Y","Y","Y","Y",null,null,null,null,crossTabValue,null,null,reportMetaDataId);
		        				 		}
		        				 	}
                        }else{
                        	logger.debug("Charts not created: "+responses+" < "+minimumRespondentsStr+" for "+employeeId);
                        }
        				 }
        			 }
				} catch (HibernateException he) {
					throw he;
				}
				/*try { 
					if(!ApplicationUtil.isEmptyOrNull(tempStr)){
						createTeamReport(templateChart.getId(),tempStr,columnBufferWhereClauseStr.toString());
					}
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				}
				
				
				try {
					HttpClient client = new HttpClient();
    				String url = null;
    				System.out.println("????\nreportmetadataid is"+reportMetaDataId);
    				
    				url="http://127.0.0.1:8081/processTextMining?templateChartId="+templateChart.getId()+"&reportMetaDataId="+reportMetaDataId+"&isOverall="+templateChart.getIsOverall()+"&wnsBenchmarkColumn="+wnsBenchmarkColumn+"&folderPath="+URLEncoder.encode(ApplicationConstants.VERBATIM_DESTINATION_FOLDER,"UTF-8");
    				System.out.println(url);
    				GetMethod method = new GetMethod(url);
    				method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
    						new DefaultHttpMethodRetryHandler(20, true));
    				int statusCode = -1;

    				try {
    					statusCode = client.executeMethod(method);
    				} catch (HttpException e) {
    					// TODO Auto-generated catch block
    					System.out.println(e);
    					logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
    				} catch (IOException e) {
    					System.out.println(e);
    					logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
    				}
    				if (statusCode == HttpStatus.SC_OK) {
    					System.out.println("success");
    					System.out.println(method.getResponseBodyAsString());
    				}
				} catch (Exception e) {
					// TODO: handle exception
					logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
				}
				return dataList;
			}
		});
			refresh_overall_reports(templateChart);
			create_benchmark_averages(templateChart);
			try {
				TemplateChart templateChartCloned = (TemplateChart)templateChart.clone();
				templateChartCloned.setTemplateChartCategories(null);
				templateChartCloned.setTemplateChartReportAssigns(null);
				templateChartCloned.setEnablePreview("Y");
				saveDesignReportTemplate(templateChartCloned,null);
				templateChart.setEnablePreview("Y");
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			}
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "refreshReports", 
					ApplicationErrorCodes.APP_EC_6, ApplicationConstants.EXCEPTION, e);
			throw applicationException;
		}
		logger.debug("Leaving from refreshReports");
	}
	
	public void createTeamReport() throws ApplicationException{
		
		try{
			logger.debug("Entered into createTeamReport");
			Object object = hibernateTemplate.execute(new HibernateCallback() {
				@SuppressWarnings("unused")
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try{
						logger.debug("Started creating the team report");
						String sqlStr = "delete from teamreport";
	              		session.connection().createStatement().executeUpdate(sqlStr);
	              		
	              		sqlStr = "delete from overallvariablesavg";
	              		session.connection().createStatement().executeUpdate(sqlStr);
	              		
	              		sqlStr = "delete from currentdata";
	              		session.connection().createStatement().executeUpdate(sqlStr);
	              		
	              		sqlStr = "delete from manager_avg_bylocation";
	              		session.connection().createStatement().executeUpdate(sqlStr);
	              		
	              		String sql1 = "select group_concat(employeeId separator ',') from employee where emailId in (select emailId from distributionlist left join master_distribution_type on master_distribution_type.id = distributionlist.masterDistributionTypeId where overAll='N' )";
	    				ResultSet empIdResult = session.connection().createStatement().executeQuery(sql1);
	    				empIdResult.next();
	    				String empIdStr=empIdResult.getString(1);
	    				String empIdArr[]=null;
	    				if(empIdStr!=null && empIdStr.length()>0)
	    					empIdArr = empIdStr.split(",");
	    				System.out.println(empIdArr.length+"============================================");
	    				StringBuffer columnBufferWhereClauseStr = new StringBuffer();
	    				// for(int k=0;(empIdArr!=null && k<empIdArr.length);k++){
                             columnBufferWhereClauseStr = new StringBuffer();
                                   sql1 = "select variableName,columnName from variablenames where surveyVariable = 'Y'";
                                   SQLQuery q = session.createSQLQuery(sql1);
                                           List variableNamesList =  (List)q.list();
                                           if(variableNamesList !=null){
                                                  int col = variableNamesList.size();
                                                  for(int l=0;l<col;l++){
                                                       Object[] rowList = (Object[])variableNamesList.get(l);
                                                String columnName = rowList[1].toString();
                                                columnBufferWhereClauseStr.append(" d."+columnName+ " IS NOT NULL");
                                                if(l+1<col){
                                                       columnBufferWhereClauseStr.append(" " +"or" + " ");
                                                }
                                                  }
                                           }
	    				 //}
						List keyMeasureWithSurveyVariableListCache = (List)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.KEY_MEASURE_WITH_SURVEY_VARIABLE_CACHE_KEY);
						String whereClauseStr="";
						String whereClause = columnBufferWhereClauseStr.toString();
						String wnsCountryColumn = PropertiesUtil.getProperty(ApplicationConstants.WNS_AVG_COUNTRY_COLUMN_NAME);
						String wnsteamReportColumn = PropertiesUtil.getProperty(ApplicationConstants.WNS_TEAM_REPORT);
						String wnsBenchmarkColumn = PropertiesUtil.getProperty(ApplicationConstants.WNS_BENCH_MARK_COLUMN_NAME);
						String managerLocationColumn =PropertiesUtil.getProperty(ApplicationConstants.MANAGER_AVG_BYLOCATION);
						
						if(!ApplicationUtil.isEmptyOrNull(empIdStr)){
							if(!ApplicationUtil.isEmptyOrNull(whereClause)){
								whereClauseStr = " and ("+whereClause+")";
							}
							String empArry[] = empIdStr.split(",");
							int len = empArry.length;
							for(int i=0;i<len;i++){
								StringBuffer empHierarchy = new StringBuffer("insert into teamreport(templatechartId,managerId,employeeId,columnName,value,totalValidRespondents,avgRespondents,orderNumber) ");
								StringBuffer directReporteesStr = new StringBuffer("insert into teamreport(templatechartId,managerId,employeeId,columnName,value,totalValidRespondents,avgRespondents,orderNumber) ");
								StringBuffer managerbuffer = new StringBuffer("insert into teamreport(templatechartId,managerId,employeeId,columnName,value,totalValidRespondents,avgRespondents,orderNumber) ");
								StringBuffer overallVariableAvgStr = new StringBuffer("insert into overallvariablesavg(columnName,empId,value,orderNumber) ");
								StringBuffer teamReportBuffer = new StringBuffer("insert into overallvariablesavg(columnName,empId,value,orderNumber) ");
								StringBuffer wnsOverallBuffer = new StringBuffer("insert into overallvariablesavg(columnName,empId,value,orderNumber) ");
								StringBuffer currentDataBuffer = new StringBuffer("insert into currentdata(variablename,variablenamesId,managerId,currentvalue,variablecount) ");
								StringBuffer mgrAvgByLocation = new StringBuffer("insert into manager_avg_bylocation(managerid,location,columnName,value,avgRespondents)  ");
								String employeeId = empArry[i];
								String wnsCountryColumnStr=wnsCountryColumn;
								if(!ApplicationUtil.isEmptyOrNull(wnsCountryColumn) && !ApplicationUtil.isEmptyOrNull(wnsBenchmarkColumn)){
									StringBuffer sb1 = new StringBuffer();
	        						sb1.append("select "+wnsCountryColumn+" val from data d where employeeId="+employeeId);
	        						ResultSet rs2 = session.connection().createStatement().executeQuery(sb1.toString());
	        						rs2.next();
                                    String value=rs2.getString(1);
									if(!ApplicationUtil.isEmptyOrNull(value) && value.equalsIgnoreCase("india")){
										wnsCountryColumnStr = wnsBenchmarkColumn;
									}
	        					}
								
								StringBuffer sb= new StringBuffer();
/*								sb.append("select group_concat(d.employeeId separator ',') as empList, aa.manager manager, count(aa.manager) respondents ");
								sb.append("from data as d, (select  group_concat(employee separator ',') as emp1, manager, e.name as name ");
								sb.append("from hierarchy, employee e where e.employeeId = manager and manager in (SELECT  employeeId FROM employee where managerId in ('"+employeeId+"')) group by manager) as aa ");
								sb.append("where find_in_set(d.employeeId, aa.emp1) "+whereClauseStr+"  group by aa.manager ");
*/								sb.append("select e.manager as manager,count(e.manager) respondents from data d,(select employee as emp1,manager,e.name as name from hierarchy, employee e where e.employeeId = manager and manager in (SELECT  employeeId FROM employee where managerId in ('"+employeeId+"'))) e where d.employeeid = e.emp1 "+whereClauseStr+" group by e.manager");
								q = session.createSQLQuery(sb.toString());
								q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
								List dataList =  (List)q.list();
								Iterator itr = dataList.iterator();
								String templateChartId = null;
								boolean flag=false;
								while(itr.hasNext())
								{
									HashMap map = (HashMap)itr.next();
			        				String managerStr= (String)map.get("manager");
			        				BigInteger respondentsStr= (BigInteger)map.get("respondents");
			        				System.out.println(" "+managerStr+" "+respondentsStr);
			        				String sql = "";
			        				int size = keyMeasureWithSurveyVariableListCache.size();
			        				for(int k=0;k<size;k++){
			        					flag=true;
			        					DropDownDisplayVo displayVo = (DropDownDisplayVo)keyMeasureWithSurveyVariableListCache.get(k);
			        					String columnName = displayVo.getValue();
			        					empHierarchy.append("select "+templateChartId+",'"+employeeId+"','"+managerStr+"','"+columnName+"',round(avg(d."+columnName+"),2),"+respondentsStr+",count(d."+columnName+"),3 from data d where d.employeeId in (select employee from hierarchy where manager = '"+managerStr+"') and d."+columnName+" is not null ");
			        					empHierarchy.append(" union all ");
			        				}
								}
								
								int size = keyMeasureWithSurveyVariableListCache.size();
		        				for(int k=0;k<size;k++){
		        					DropDownDisplayVo displayVo = (DropDownDisplayVo)keyMeasureWithSurveyVariableListCache.get(k);
		        					String columnName = displayVo.getValue();
		        					directReporteesStr.append("select "+templateChartId+",'"+employeeId+"','-1','"+columnName+"',round(avg(d."+columnName+"),2),(select count(d.employeeId) from data d, (select  e.employeeId from employee e where managerId = '"+employeeId+"') b where b.employeeid = d.employeeId "+whereClauseStr+"),count(d."+columnName+"),1 from data d,(select e.employeeId from employee e where managerId = '"+employeeId+"') b where b.employeeid = d.employeeId and d."+columnName+" is not null");
		        					directReporteesStr.append(" union all ");
		        					managerbuffer.append("select "+templateChartId+",'"+employeeId+"','"+employeeId+"','"+columnName+"',round(avg(d."+columnName+"),2),(select count(d.employeeId) from data d, (select  h.employee from hierarchy h where manager = '"+employeeId+"') b where b.employee = d.employeeId "+whereClauseStr+"),count(d."+columnName+"),0 from data d,(select  h.employee from hierarchy h where manager = '"+employeeId+"') b where b.employee = d.employeeId and d."+columnName+" is not null");
		        					managerbuffer.append(" union all ");
		        					
		        					//inserting values into currentdata table
		        					currentDataBuffer.append("select * from(select variableName,mastercategoryid from master_variable_categories where variablename =(select variableName from variablenames where columnName ='"+columnName+"')) as o ");
		        					currentDataBuffer.append("cross join "); 
		        					currentDataBuffer.append("(select '"+employeeId+"' ,round(avg("+columnName+"),2), count("+columnName+") from data where employeeId in (select employee from hierarchy where manager='"+employeeId+"') and "+columnName+" is not null) as t");
		        					currentDataBuffer.append(" union all ");
		        					
		        					if(!ApplicationUtil.isEmptyOrNull(wnsCountryColumnStr)){
		        						overallVariableAvgStr.append("select '"+columnName+"', '"+employeeId+"' ,round(avg("+columnName+"),2),1 from data where "+wnsCountryColumnStr+" in (select distinct "+wnsCountryColumnStr+" from data where employeeId in (select employee from hierarchy where manager='"+employeeId+"')) and "+columnName+" is not null");
		        						overallVariableAvgStr.append(" union all ");
		        					}
		        					
		        					if(!ApplicationUtil.isEmptyOrNull(wnsteamReportColumn)){
		        						teamReportBuffer.append("select '"+columnName+"', '"+employeeId+"' ,round(avg("+columnName+"),2),2 from data where "+wnsteamReportColumn+" in (select distinct "+wnsteamReportColumn+" from data where employeeId in (select employee from hierarchy where manager='"+employeeId+"')) and "+columnName+" is not null");
		        						teamReportBuffer.append(" union all ");
		        					}
		        					
		        					wnsOverallBuffer.append("select '"+columnName+"', '"+employeeId+"' ,round(avg("+columnName+"),2),0 from data where "+columnName+" is not null");
		        					wnsOverallBuffer.append(" union all ");
		        					
		        					if(!ApplicationUtil.isEmptyOrNull(managerLocationColumn)){
		        						mgrAvgByLocation.append("select '"+employeeId+"',"+managerLocationColumn+",'"+columnName+"',round(avg("+columnName+"),2),count("+columnName+") from data where employeeid in ( select employee from hierarchy where manager='"+employeeId+"') and "+columnName+" is not null group by "+managerLocationColumn);
		        						mgrAvgByLocation.append(" union all ");
		        					}
		        					
		        				}
		        				
		        				if(managerbuffer.length()>0){
									int index = managerbuffer.lastIndexOf("union all");
									String str=null;
									if(index!=-1){
										str = managerbuffer.replace(index, managerbuffer.length(), "").toString();
									}else{
										str= managerbuffer.toString();
									}
									System.out.println(str);
									logger.debug(str);
									session.connection().createStatement().executeUpdate(str);
								}
								if(directReporteesStr.length()>0){
									int index = directReporteesStr.lastIndexOf("union all");
									String str=null;
									if(index!=-1){
										str = directReporteesStr.replace(index, directReporteesStr.length(), "").toString();
									}else{
										str = directReporteesStr.toString();
									}
									System.out.println(str);
									logger.debug(str);
									session.connection().createStatement().executeUpdate(str);
								}
								if(empHierarchy.length()>0 && flag){
									int index = empHierarchy.lastIndexOf("union all");
									String str =null;
									if(index!= -1){
										str = empHierarchy.replace(index, empHierarchy.length(), "").toString();
									}else{
										str = empHierarchy.toString();
									}
									System.out.println(str);
									logger.debug(str);
									session.connection().createStatement().executeUpdate(str);
								}
								
								//inserting values into currentdata table
								if(currentDataBuffer.length()>0){
									int index = currentDataBuffer.lastIndexOf("union all");
									String  str=null;
									if(index!=-1){
										str = currentDataBuffer.replace(index, currentDataBuffer.length(), "").toString();
									}else{
										str = currentDataBuffer.toString();
									}
									System.out.println(str);
									logger.debug(str);
									session.connection().createStatement().executeUpdate(str);
								}
															
								if(overallVariableAvgStr.length()>0){
									int index = overallVariableAvgStr.lastIndexOf("union all");
									String str=null;
									if(index !=-1){
										str = overallVariableAvgStr.replace(index, overallVariableAvgStr.length(), "").toString();
									}else{
										str = overallVariableAvgStr.toString();
									}
									
									logger.debug(str);
									session.connection().createStatement().executeUpdate(str);
								}
								
								if(wnsOverallBuffer.length()>0){
									int index = wnsOverallBuffer.lastIndexOf("union all");
									String str=null;
									if(index !=-1){
										str = wnsOverallBuffer.replace(index, wnsOverallBuffer.length(), "").toString();
									}else{
										str = wnsOverallBuffer.toString();
									}
									
									logger.debug(str);
									session.connection().createStatement().executeUpdate(str);
								}
								
								if(teamReportBuffer.length()>0){
									int index = teamReportBuffer.lastIndexOf("union all");
									String str=null;
									if(index !=-1){
										str = teamReportBuffer.replace(index, teamReportBuffer.length(), "").toString();
									}else{
										str = teamReportBuffer.toString();
									}
									
									logger.debug(str);
									session.connection().createStatement().executeUpdate(str);
								}
								
								if(mgrAvgByLocation.length()>0){
									int index = mgrAvgByLocation.lastIndexOf("union all");
									String str=null;
									if(index!=-1){
										str = mgrAvgByLocation.replace(index, mgrAvgByLocation.length(), "").toString();
									}else{
										str= mgrAvgByLocation.toString();
									}
									System.out.println(str);
									logger.debug(str);
									session.connection().createStatement().executeUpdate(str);
								}
								
								logger.debug("Finished creating the team report for Manager = "+employeeId);
							}
						}
						logger.debug("Finished creating the team report");
					} catch (HibernateException he) {
						throw he;
					}
					return null;
				}
			});
			logger.debug("Leaving from createTeamReport");
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	}
	public void refresh_overall_reports(final TemplateChart templateChart) throws ApplicationException{
		try{
		logger.debug("Entered into refresh_overall_reports");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			@SuppressWarnings("unused")
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				List dataList = null;session.connection().setAutoCommit(true);
				Map functionMapObj = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.MASTER_FUNCTIONS_MAP_CACHE_KEY);
				Map variableNamesObj=(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.VARIABLE_NAMES_MAP_CACHE_KEY);
				String employeeId="NULL";
				String filter1=templateChart.getFilter1();
				String filter2=templateChart.getFilter2();
				String filter3=templateChart.getFilter3();
				String filterValue1=templateChart.getFilterValues1();
				String filterValue2=templateChart.getFilterValues2();
				String filterValue3=templateChart.getFilterValues3();
				Integer reportMetaDataId = templateChart.getReportMetaDataId();
				Long createdBy = templateChart.getCreatedBy();
				Long updatedBy = templateChart.getUpdatedBy();
				Date createdDate = templateChart.getCreatedDate();
				Date updatedDate = templateChart.getUpdatedDate();
				String sql="";
				if(reportMetaDataId==null){
					sql = "select count(emailId) from distributionlist left join master_distribution_type on master_distribution_type.id = distributionlist.masterDistributionTypeId where overAll='y' and masterDistributionTypeId in (select masterDistId from templatechartreportassign where templateChartId = "+templateChart.getId()+")";
					ResultSet empIdResult = session.connection().createStatement().executeQuery(sql);
					empIdResult.next();
					String count=empIdResult.getString(1);
					if(count.equals("0")){
						empIdResult.close();
						return null;
					}
				}
				
				//System.out.println(templateChart.toString());
 				
				String whereConstrain="";
				String whereConstrainWithAlias="";
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
					whereConstrainWithAlias=" t1."+filter1+" in ("+filterValue1+") ";
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
					whereConstrainWithAlias = (whereConstrainWithAlias.length()>0)?whereConstrainWithAlias+" and ":whereConstrainWithAlias;
					whereConstrainWithAlias+=" t1."+filter2+" in ("+filterValue2+") ";
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
					whereConstrainWithAlias = (whereConstrainWithAlias.length()>0)?whereConstrainWithAlias+" and ":whereConstrainWithAlias;
					whereConstrainWithAlias+=" t1."+filter3+" in ("+filterValue3+") ";
				}
					if(whereConstrain.length()>0)
						whereConstrain = "and "+whereConstrain;
					if(whereConstrainWithAlias.length()>0)
						whereConstrainWithAlias = "and "+whereConstrainWithAlias;
				
				try {
					 sql = "SELECT * FROM templatechartinfo where templateChartCategoryId in (SELECT id FROM templatechartcategory where templateChartId = "+templateChart.getId()+") and functionid in (SELECT id FROM essdatabase.master_functions where logicalname in ('FREQUENCY','AVERAGE','BANNER TABLE'))";
        			 SQLQuery q = session.createSQLQuery(sql);
        			 q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        			 dataList =  (List)q.list();
        			 Iterator i = dataList.iterator();
        			 while(i.hasNext())
        			 {	HashMap map = (HashMap)i.next();
        				 String functionName= (String) functionMapObj.get((Integer)map.get("functionId"));
        				 String x_axis = (String) map.get("xAxis");
        				 String y_axis = (String) map.get("yAxis");
        				 String weight = (String)map.get("weightVariable");
        				 String filter = (String)map.get("filter");//(map.get("filter")==null)?null:(String)map.get("filter");
        				 //System.out.println(filter.length()+"- filter length");
        				 
        				 Integer incl = (Integer) map.get("meanIncl");
        				 Integer excl = (Integer) map.get("meanExcl");
        				 Integer medianIncl = (Integer) map.get("medianIncl");
        				 Integer medianExcl = (Integer) map.get("medianExcl");
        				 String crossTabValue = (String)map.get("crossTabValue");
        				 String sql1 = "";
        				 String sql2 = "";
        				 String mean = "";
        				 String mean1 = "";
        				 String stdDev = "";
        				 String stdErr = "";
        				 String median = "";
        				 String median1 = "";
        				 
        				 Integer id = (Integer)map.get("id");
        				 if(reportMetaDataId != null){
	    					 String sql3 = "delete from chartaverage where templateChartInfoId="+id+" and reportmetadataId= "+reportMetaDataId;
	    					 session.connection().createStatement().executeUpdate(sql3);
	    					 sql3= "delete from chartfrequencies where  templateChartInfoId="+id+" and reportmetadataId= "+reportMetaDataId;
	    					 session.connection().createStatement().executeUpdate(sql3);
	    					 sql3= "delete from benchmark where  templateChartInfoId="+id+" and reportmetadataId= "+reportMetaDataId;
	    					 session.connection().createStatement().executeUpdate(sql3);
	    					 sql3= "delete from bannertable where templateChartInfoId ="+id+" and reportmetadataId= "+reportMetaDataId;
	    					 session.connection().createStatement().executeUpdate(sql3);
        				 }else{
        					 String sql3 = "delete from chartaverage where templateChartInfoId="+id;
	    					 session.connection().createStatement().executeUpdate(sql3);
	    					 sql3= "delete from chartfrequencies where  templateChartInfoId="+id;
	    					 session.connection().createStatement().executeUpdate(sql3);
	    					 sql3= "delete from benchmark where  templateChartInfoId="+id;
	    					 session.connection().createStatement().executeUpdate(sql3);
	    					 sql3= "delete from bannertable where templateChartInfoId ="+id;
	    					 session.connection().createStatement().executeUpdate(sql3);
        				 }
    					 ResultSet rs = null;
        				 
        				 if(ApplicationUtil.isEmptyOrNull(filter) || filter.equals("-1")){
        					 filter=null;
        				 }
        				 if(ApplicationUtil.isEmptyOrNull(y_axis) || y_axis.equals("-1")){
        					 y_axis=null;
        				 }
        				 String displayDecimal=PropertiesUtil.getProperty("displayDecimal");
        				 String roundNum="2";
    					if(displayDecimal.equalsIgnoreCase("true")){
    						roundNum = "0";
    					}
        				 if (functionName.equals("AVERAGE")) {
        					 if(x_axis!=null && x_axis.length()>0){
     					 		if(x_axis.contains(",")){
     					 			String x_axis_arr[] = x_axis.split(",");
     					 			int j=0;

     					 			sql1 = "insert into chartaverage(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,frequency,reportmetadataId) ";
     					 			sql2 = "insert into chartaverage(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,frequency,reportmetadataId) ";
     					 			if(y_axis!=null && y_axis.length()>0){
     					 				for(;j<(x_axis_arr.length-1);j++){
     					 					String variableName = (String)variableNamesObj.get(x_axis_arr[j].toLowerCase());
     					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
     					 						variableName = x_axis_arr[j];
     					 					}
	        					 				sql1+="select "+id+","+employeeId+","+y_axis+",'"+variableName+"',null,round(avg("+x_axis_arr[j]+"),"+roundNum+"), count("+x_axis_arr[j]+"),"+reportMetaDataId+"  from data where   "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereConstrain+" group by "+y_axis+" union ";
	        					 				sql2+="select "+id+","+employeeId+","+y_axis+",'"+variableName+"',"+filter+",round(avg("+x_axis_arr[j]+"),"+roundNum+"), count("+x_axis_arr[j]+"),"+reportMetaDataId+"  from data where   "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null and "+filter+" is not null "+whereConstrain+" group by "+y_axis+","+filter+" union ";
	        					 				}
     					 				String variableName = (String)variableNamesObj.get(x_axis_arr[x_axis_arr.length-1].toLowerCase());
 					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
 					 						variableName = x_axis_arr[x_axis_arr.length-1];
 					 					}
     					 				sql1+="select "+id+","+employeeId+","+y_axis+",'"+variableName+"',null,round(avg("+x_axis_arr[j]+"),"+roundNum+"), count("+x_axis_arr[j]+"),"+reportMetaDataId+"  from data where  "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereConstrain+"  group by "+y_axis;
     					 				sql2+="select "+id+","+employeeId+","+y_axis+",'"+variableName+"',"+filter+",round(avg("+x_axis_arr[j]+"),"+roundNum+"), count("+x_axis_arr[j]+"),"+reportMetaDataId+"  from data where "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null and "+filter+" is not null "+whereConstrain+" group by "+y_axis+","+filter;
     					 				
     					 			}else{
     					 				for(;j<(x_axis_arr.length-1);j++){
     					 					String variableName = (String)variableNamesObj.get(x_axis_arr[j].toLowerCase());
     					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
     					 						variableName = x_axis_arr[j];
     					 					}
     					 				sql1+="select "+id+","+employeeId+",'"+variableName+"','Variable',null,round(avg("+x_axis_arr[j]+"),"+roundNum+"), count("+x_axis_arr[j]+"),"+reportMetaDataId+"  from data where "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereConstrain+" union ";
     					 				sql2+="select "+id+","+employeeId+",'"+variableName+"','Variable',"+filter+",round(avg("+x_axis_arr[j]+"),"+roundNum+"), count("+x_axis_arr[j]+"),"+reportMetaDataId+"  from data where "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereConstrain+" group by "+filter+" union ";
     					 				}
     					 				String variableName = (String)variableNamesObj.get(x_axis_arr[x_axis_arr.length-1].toLowerCase());
 					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
 					 						variableName = x_axis_arr[x_axis_arr.length-1];
 					 					}
     					 				sql1+="select "+id+","+employeeId+",'"+variableName+"','Variable',null,round(avg("+x_axis_arr[j]+"),"+roundNum+"), count("+x_axis_arr[j]+"),"+reportMetaDataId+"  from data where "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereConstrain+" ";
     					 				sql2+="select "+id+","+employeeId+",'"+variableName+"','Variable',"+filter+",round(avg("+x_axis_arr[j]+"),"+roundNum+"), count("+x_axis_arr[j]+"),"+reportMetaDataId+"  from data where "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereConstrain+" group by "+filter+"";
     					 			}
     					 				
     					 				
     					 		}
     					 		else if(y_axis!=null && y_axis.length()>0){
     					 			String variableName = (String)variableNamesObj.get(x_axis.toLowerCase());
					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
					 						variableName = x_axis;
					 					}
     					 			sql1 = "insert into chartaverage(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,frequency,reportmetadataId) select "+id+","+employeeId+","+y_axis+",'"+variableName+"',null,round(avg("+x_axis+"),"+roundNum+"), count("+x_axis+"),"+reportMetaDataId+"  from data where "+x_axis+" != '' and "+x_axis+" is not null "+whereConstrain+" group by "+y_axis;
     					 			sql2 = "insert into chartaverage(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,frequency,reportmetadataId) select "+id+","+employeeId+","+y_axis+",'"+variableName+"',"+filter+",round(avg("+x_axis+"),"+roundNum+"), count("+x_axis+"),"+reportMetaDataId+"  from data where "+x_axis+" != '' and "+x_axis+" is not null "+whereConstrain+" group by "+y_axis+","+filter;
     					 		}
     					 		else{
     					 			String variableName = (String)variableNamesObj.get(x_axis.toLowerCase());
					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
					 						variableName = x_axis;
					 					}
     					 			sql1 = "insert into chartaverage(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,frequency,reportmetadataId) select "+id+","+employeeId+",'"+variableName+"',null,null,round(avg("+x_axis+"),"+roundNum+"), count("+x_axis+"),"+reportMetaDataId+"  from data where "+x_axis+" != '' and "+x_axis+" is not null "+whereConstrain+"";
     					 			sql2 = "insert into chartaverage(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,frequency,reportmetadataId) select "+id+","+employeeId+",'"+variableName+"',null,"+filter+",round(avg("+x_axis+"),"+roundNum+"), count("+x_axis+"),"+reportMetaDataId+"  from data where "+x_axis+" != '' and "+x_axis+" is not null "+whereConstrain+" group by "+filter;
     					 		}
     					 	}
  
     					 	System.out.println(sql1);
     					 	System.out.println(sql2);
     					 			session.connection().createStatement().executeUpdate(sql1);
     					 			if(filter!=null){
     					 				session.connection().createStatement().executeUpdate(sql2);
     					 			}
	        					 				
        				 	}else if(functionName.equals("FREQUENCY")){
        					 	if(x_axis!=null && x_axis.length()>0){
        					 		if(x_axis.contains(",")){
        					 			String x_axis_arr[] = x_axis.split(",");
        					 			sql1 += "insert into chartfrequencies(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,reportmetadataId) ";
        					 			sql2 += "insert into chartfrequencies(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,reportmetadataId) ";
        					 			int length = x_axis_arr.length;
        					 			for(int j=0;j<length;j++){
        					 				String variableName = (String)variableNamesObj.get(x_axis_arr[j].toLowerCase());
    					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
    					 						variableName = x_axis_arr[j];
    					 					}
        					 				sql1 +=	"select "+id+","+employeeId+",'"+variableName+"',"+x_axis_arr[j]+",null,count("+x_axis_arr[j]+"),"+reportMetaDataId+"  from data where "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereConstrain+" group by "+x_axis_arr[j];
        					 				sql2 += "select "+id+","+employeeId+",'"+variableName+"',"+x_axis_arr[j]+","+filter+",count("+x_axis_arr[j]+"),"+reportMetaDataId+"  from data where "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereConstrain+" group by "+x_axis_arr[j]+","+filter;
        					 				for(int l=0;l<length;l++){
        					 					if(x_axis_arr[j].equals(x_axis_arr[l])){
        					 						continue;
        					 					}
        					 					sql1 += " union ";
        					 					sql2 += " union ";
        					 					sql1 +=	"select "+id+","+employeeId+",'"+variableName+"',"+x_axis_arr[l]+",null,0,"+reportMetaDataId+"  from data where  "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereConstrain+" group by "+x_axis_arr[l];
	        					 				sql2 += "select "+id+","+employeeId+",'"+variableName+"',"+x_axis_arr[l]+","+filter+",0,"+reportMetaDataId+"  from data where  "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereConstrain+" group by "+x_axis_arr[l]+","+filter;
        					 				}
        					 				if(j+1<length){
        					 					sql1 += " union ";
        					 					sql2 += " union ";
        					 				}
        					 			}
        					 		}
        					 		else if(y_axis!=null && y_axis.length()>0){ 
        					 			sql1 = "insert into chartfrequencies(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,reportmetadataId) (select "+id+","+employeeId+",t2."+y_axis+", t1."+x_axis+",null,count(1),"+reportMetaDataId+"  from data t1 , data t2 where  t1.employeeId = t2.employeeId and t1."+x_axis+" != '' and t1."+x_axis+" is not null and t2."+y_axis+" is not null and t1."+x_axis+" !='' and t2."+y_axis+" !='' "+whereConstrainWithAlias+" group by t1."+x_axis+", t2."+y_axis+")";
        					 			sql2 = "insert into chartfrequencies(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,reportmetadataId) (select "+id+","+employeeId+",t2."+y_axis+", t1."+x_axis+",t1."+filter+",count(1),"+reportMetaDataId+"  from data t1 , data t2 where  t1.employeeId = t2.employeeId and t1."+x_axis+" != '' and t1."+x_axis+" is not null and t2."+y_axis+" is not null and t1."+x_axis+" !='' and t2."+y_axis+" !='' "+whereConstrainWithAlias+" group by t1."+x_axis+", t2."+y_axis+","+filter+")";
        					 		}
        					 		else{

        					 			sql1 = "insert into chartfrequencies(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,reportmetadataId) (select "+id+","+employeeId+","+x_axis+",null,null,count(1),"+reportMetaDataId+" from data where "+x_axis+" != '' and "+x_axis+" is not null and "+x_axis+" !='' "+whereConstrain+" group by "+x_axis+")";
        					 			sql2 = "insert into chartfrequencies(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,reportmetadataId) (select "+id+","+employeeId+","+x_axis+",null,"+filter+",count(1),"+reportMetaDataId+"  from data where "+x_axis+" != '' and "+x_axis+" is not null and "+filter+" is not null and "+x_axis+" !='' and "+filter+" !='' "+whereConstrain+" group by "+x_axis+","+filter+")";
        					 		}
        					 	}
      
        					 	//System.out.println(sql1);
        					System.out.println(sql2);
        					    	session.connection().createStatement().executeUpdate(sql1);
        					    	if(!ApplicationUtil.isEmptyOrNull(filter) && !filter.equals("-1")){
    					 				session.connection().createStatement().executeUpdate(sql2);
    					 			}
        				 	}
        				 	else if(functionName.equals("BANNER TABLE"))
        				 	{
        				 		if((x_axis!=null && x_axis.length()>0 )&& (y_axis!=null && y_axis.length()>0)){
        				 			generateBannerTable(id,employeeId,null,x_axis, y_axis, weight, incl, excl, medianIncl, medianExcl,"Y","Y","Y","Y",null,null,null,null,crossTabValue,null,null,reportMetaDataId);
        				 		}
        				 	}
        				 
        			 }
				} catch (HibernateException he) {
					throw he;
				}
				return dataList;
			}
		});
		logger.debug("Leaving from refresh_overall_reports");
	}catch (Exception e) {
		logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
		throw e;
	}
	}
	
	public void create_benchmark_averages(final TemplateChart templateChart) throws ApplicationException{
		try{
		logger.debug("Entered into create_benchmark_averages");
        Object object = hibernateTemplate.execute(new HibernateCallback() {
               @SuppressWarnings("unused")
               public Object doInHibernate(Session session)
                            throws HibernateException, SQLException {
                     List dataList = null;
                    String minimumRespondentsStr = PropertiesUtil.getProperty(ApplicationConstants.MINIMUM_RESPONDENTS);
     				int minimumRespondents = 0;
     				if(!ApplicationUtil.isEmptyOrNull(minimumRespondentsStr)){
     					minimumRespondents = Integer.parseInt(minimumRespondentsStr);
     				}
                     session.connection().setAutoCommit(true);
                     Map functionMapObj = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.MASTER_FUNCTIONS_MAP_CACHE_KEY);
                     Map variableNamesObj=(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.VARIABLE_NAMES_MAP_CACHE_KEY);
                     List surveyVariableObjList = (List)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.SURVEY_VARIABLES_LIST);
                     String wnsBenchmarkColumn = PropertiesUtil.getProperty(ApplicationConstants.WNS_BENCH_MARK_COLUMN_NAME);
                     String employeeId="";
                     String filter1=templateChart.getFilter1();
                     String filter2=templateChart.getFilter2();
                     String filter3=templateChart.getFilter3();
                     String filterValue1=templateChart.getFilterValues1();
                     String filterValue2=templateChart.getFilterValues2();
                     String filterValue3=templateChart.getFilterValues3();
                     Integer reportMetaDataId = templateChart.getReportMetaDataId();
                     
                     Long createdBy = templateChart.getCreatedBy();
                     Long updatedBy = templateChart.getUpdatedBy();
                     Date createdDate = templateChart.getCreatedDate();
                     Date updatedDate = templateChart.getUpdatedDate();
                     String sql = "select group_concat(employeeId separator ',') from employee where emailId in (select emailId from distributionlist left join master_distribution_type on master_distribution_type.id = distributionlist.masterDistributionTypeId where overAll='n' and masterDistributionTypeId in (select masterDistId from templatechartreportassign where templateChartId = "+templateChart.getId()+"))";
                     ResultSet empIdResult = session.connection().createStatement().executeQuery(sql);
                     empIdResult.next();
                     String tempStr=empIdResult.getString(1);
                     String empIdArr[]=null;
                     if(tempStr!=null && tempStr.length()>0)
                            empIdArr = tempStr.split(",");
                                               
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
                     
                     try {
                            sql = "SELECT * FROM templatechartinfo where templateChartCategoryId in (SELECT id FROM templatechartcategory where templateChartId = "+templateChart.getId()+") and functionid in (SELECT id FROM essdatabase.master_functions where logicalname in ('AVERAGE'))";
                      SQLQuery q = session.createSQLQuery(sql);
                      q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                      dataList =  (List)q.list();
                      Iterator i = dataList.iterator();
                      StringBuffer columnBuffer = new StringBuffer();
                      columnBuffer.append("select count(employeeId) from data as d where ");
                      if(surveyVariableObjList !=null){
                             int col = surveyVariableObjList.size();
                             for(int l=0;l<col;l++){
                                  VariableNames variableNames = (VariableNames)surveyVariableObjList.get(l);
                                   String columnName = variableNames.getColumnName();
                                   columnBuffer.append(" d."+columnName+ " IS NULL");
                                   if(l+1<col){
                                          columnBuffer.append(" " +"and" + " ");
                                   }
                             }
                      }
                      String functionalColBuff=columnBuffer.toString();
                      while(i.hasNext())
                      {     HashMap map = (HashMap)i.next();
                             String functionName= (String) functionMapObj.get((Integer)map.get("functionId"));
                             String x_axis = (String) map.get("xAxis");
                             String y_axis = (String) map.get("yAxis");
                             String filter = (String)map.get("filter");
                             String benchMark = (String)map.get("benchMark");
                             String benchMarkStr=benchMark;
                             //(map.get("filter")==null)?null:(String)map.get("filter");
                             //System.out.println(filter.length()+"- filter length");
                             Integer id = (Integer)map.get("id");
                             ResultSet rs = null;
                             
                             if(ApplicationUtil.isEmptyOrNull(filter) || filter.equals("-1")){   
                                    filter=null;
                             }
                             if(ApplicationUtil.isEmptyOrNull(y_axis) || y_axis.equals("-1")){
                                    y_axis=null;
                             }
                             if(ApplicationUtil.isEmptyOrNull(benchMark) || benchMark.equals("-1")){
                                    continue;
                             }
                             String colbuff="";
                             if(empIdArr != null && empIdArr.length>0){
                            	 for(int k=0;(empIdArr!=null && k<empIdArr.length);k++){
                                     employeeId = empIdArr[k];
                                     StringBuffer columnBufferObj  = new StringBuffer();
                                     columnBufferObj.append(columnBuffer.toString());
                                     if(columnBufferObj.length()>0){
                                    	 columnBufferObj.append(" and employeeId in (select employee from hierarchy where manager = "+employeeId+") "+whereConstrain);
                                              colbuff =" - ("+columnBufferObj.toString()+") ";
                                       }
                                     String sqlQ="select (select count(*) from data as d where employeeId in (select employee from hierarchy where manager = "+employeeId+")"+whereConstrain+")" +colbuff;
                                     ResultSet rs1 = session.connection().createStatement().executeQuery(sqlQ);
                                     rs1.next();
                                     Integer responses=rs1.getInt(1);
                                     if(responses>minimumRespondents){
                                    	benchMarkStr=benchMark;
                                    	if(!ApplicationUtil.isEmptyOrNull(wnsBenchmarkColumn)){ 
	                                    	StringBuffer sb1 = new StringBuffer();
	 		        						sb1.append("select "+benchMark+" val from data d where employeeId="+employeeId);
	 		        						ResultSet rs2 = session.connection().createStatement().executeQuery(sb1.toString());
	 		        						rs2.next();
	 	                                    String value=rs2.getString(1);
	 										if(!ApplicationUtil.isEmptyOrNull(value) && value.equalsIgnoreCase("india")){
 												benchMarkStr = wnsBenchmarkColumn;
	 										}
                                    	}
                                    	 logger.debug("Benchmarks Charts created:  for "+employeeId);
                                    	 benchmarkCalculation(employeeId,id,session,functionName,benchMarkStr,x_axis,y_axis,variableNamesObj,whereConstrain,filter,reportMetaDataId);
                                     }else{
                                    	 logger.debug("Benchmarks Charts not created: "+responses+" < "+minimumRespondentsStr+" for "+employeeId);
                                     }
                                     
                               }
                             }else{
                            	 employeeId=null;
                            	 String functionalColBuffStr=null;
                            	 if(!ApplicationUtil.isEmptyOrNull(functionalColBuff)){
                            		 functionalColBuffStr = functionalColBuff+" "+whereConstrain;
                            		 functionalColBuffStr =" - ("+functionalColBuffStr+") ";
                            	 }
                            	 String sqlQ="select (select count(*) from data as d where 1=1 "+whereConstrain+")" +functionalColBuffStr;
                                 ResultSet rs1 = session.connection().createStatement().executeQuery(sqlQ);
                                 rs1.next();
                                 Integer responses=rs1.getInt(1);
                                 if(responses>minimumRespondents){
                                	 benchmarkCalculation(employeeId,id,session,functionName,benchMark,x_axis,y_axis,variableNamesObj,whereConstrain,filter,reportMetaDataId);
                                 }
                             }
                             
                      }
                     } catch (HibernateException he) {
                            throw he;
                     }
                     logger.debug("Leaving into create_benchmark_averages");
                     return dataList;
               }							//end of doInHibernate() 
               
               private void benchmarkCalculation(String employeeId,Integer id,Session session,String functionName,String benchMark,String x_axis,String y_axis,Map variableNamesObj,String whereConstrain,String filter,Integer reportMetaDataId)
            		   														throws HibernateException, SQLException {
              	 try{
              		logger.debug("Entered into benchmarkCalculation");
              		 String sqlStr=null;
              		 filter=null;    //There is no need to group by filter for benchmark
              		 
              		 if(reportMetaDataId != null){
	              		 if(ApplicationUtil.isEmptyOrNull(employeeId)){
	              			sqlStr = "delete from benchmark where employeeId is null and templateChartInfoId = "+id+" and reportmetadataId= "+reportMetaDataId;
	              		 }else{
	              			sqlStr = "delete from benchmark where employeeId ="+employeeId+" and templateChartInfoId = "+id+" and reportmetadataId= "+reportMetaDataId;
	              		 }
              		 }else{
              			if(ApplicationUtil.isEmptyOrNull(employeeId)){
	              			sqlStr = "delete from benchmark where employeeId is null and templateChartInfoId = "+id;
	              		 }else{
	              			sqlStr = "delete from benchmark where employeeId ="+employeeId+" and templateChartInfoId = "+id;
	              		 }
              		 }
              		              		 
              		session.connection().createStatement().executeUpdate(sqlStr);
              		String sql1="";
                    String sql2="";
                    String sql3="";
                    String sql4="";
                    String whereCondition1 ="";
                    if(!ApplicationUtil.isEmptyOrNull(employeeId)){
                    	whereCondition1 = " and "+benchMark+" in (select distinct "+benchMark+" from data where employeeId in (select employee from hierarchy where manager = "+employeeId+")) ";
                    } else{
                    	whereCondition1 = " and "+benchMark+" in (select distinct "+benchMark+" from data where 1=1 "+whereConstrain+") ";
                    }
                    if (functionName.equals("AVERAGE")) {
  
                             	 if(x_axis!=null && x_axis.length()>0){
    	        					 		if(x_axis.contains(",")){
    	        					 			String x_axis_arr[] = x_axis.split(",");
    	        					 			int j=0;

    	        					 			sql1 = "insert into benchmark(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,reportmetadataId) ";
    	        					 			sql2 = "insert into benchmark(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,reportmetadataId) ";
    	        					 			sql3 += sql1;
    	        					 			sql4 += sql1;
    	        					 			if(y_axis!=null && y_axis.length()>0){
    	        					 				for(;j<(x_axis_arr.length-1);j++){
    	        					 					String variableName = (String)variableNamesObj.get(x_axis_arr[j].toLowerCase());
    	        					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
    	        					 						variableName = x_axis_arr[j];
    	        					 					}
    		        					 				sql1+="select "+id+","+employeeId+","+y_axis+",'"+variableName+"',null,round(avg("+x_axis_arr[j]+"),2),"+reportMetaDataId+" from data where "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereCondition1+" group by "+y_axis+" union ";
    		        					 				sql2+="select "+id+","+employeeId+","+y_axis+",'"+variableName+"',"+filter+",round(avg("+x_axis_arr[j]+"),2),"+reportMetaDataId+"  from data where "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereCondition1+" group by "+y_axis+","+filter+" union ";
    		        					 				// employeeId in (select employee from hierarchy where manager in (SELECT managerId FROM employee where employeeId="+employeeId+"))
    		        					 				
    		        					 				if(benchMark.equals("OVER_ALL")){
    		        					 					sql3+="select "+id+","+employeeId+","+y_axis+",'"+variableName+"',null,round(avg("+x_axis_arr[j]+"),2),"+reportMetaDataId+"  from data where "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null group by "+y_axis+" union ";
    			        					 				sql4+="select "+id+","+employeeId+","+y_axis+",'"+variableName+"',"+filter+",round(avg("+x_axis_arr[j]+"),2),"+reportMetaDataId+"  from data where "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null group by "+y_axis+","+filter+" union ";
    		        					 					
    		        					 				}else{
    		        					 					sql3+="select "+id+","+employeeId+","+y_axis+",'"+variableName+"',null,round(avg("+x_axis_arr[j]+"),2),"+reportMetaDataId+"  from data where employeeId in (select employee from hierarchy where manager in (SELECT managerId FROM employee where employeeId="+employeeId+")) and "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereConstrain+" group by "+y_axis+" union ";
    		        					 					sql4+="select "+id+","+employeeId+","+y_axis+",'"+variableName+"',"+filter+",round(avg("+x_axis_arr[j]+"),2),"+reportMetaDataId+"  from data where employeeId in (select employee from hierarchy where manager in (SELECT managerId FROM employee where employeeId="+employeeId+")) and "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereConstrain+" group by "+y_axis+","+filter+" union ";
    		        					 				}
    	        					 				}
    	        					 				String variableName = (String)variableNamesObj.get(x_axis_arr[x_axis_arr.length-1].toLowerCase());
    	     					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
    	     					 						variableName = x_axis_arr[x_axis_arr.length-1];
    	     					 					}
    	        					 				sql1+="select "+id+","+employeeId+","+y_axis+",'"+variableName+"',null,round(avg("+x_axis_arr[j]+"),2),"+reportMetaDataId+"  from data where  "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereCondition1+"  group by "+y_axis;
    	        					 				sql2+="select "+id+","+employeeId+","+y_axis+",'"+variableName+"',"+filter+",round(avg("+x_axis_arr[j]+"),2),"+reportMetaDataId+"  from data where "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereCondition1+" group by "+y_axis+","+filter;
    	        					 				
    	        					 				if(benchMark.equals("OVER_ALL")){
    	        					 					sql3+="select "+id+","+employeeId+","+y_axis+",'"+variableName+"',null,round(avg("+x_axis_arr[j]+"),2),"+reportMetaDataId+"  from data where "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null group by "+y_axis;
    		        					 				sql4+="select "+id+","+employeeId+","+y_axis+",'"+variableName+"',"+filter+",round(avg("+x_axis_arr[j]+"),2),"+reportMetaDataId+"  from data where "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null group by "+y_axis+","+filter;
    	        					 					
    	        					 				}else{
    	        					 				
    	        					 				sql3+="select "+id+","+employeeId+","+y_axis+",'"+variableName+"',null,round(avg("+x_axis_arr[j]+"),2),"+reportMetaDataId+"  from data where  employeeId in (select employee from hierarchy where manager in (SELECT managerId FROM employee where employeeId="+employeeId+")) and "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereConstrain+"  group by "+y_axis;
    	        					 				sql4+="select "+id+","+employeeId+","+y_axis+",'"+variableName+"',"+filter+",round(avg("+x_axis_arr[j]+"),2),"+reportMetaDataId+"  from data where employeeId in (select employee from hierarchy where manager in (SELECT managerId FROM employee where employeeId="+employeeId+")) and "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereConstrain+" group by "+y_axis+","+filter;
    	        					 				}
    	        					 			}else{
    	        					 				for(;j<(x_axis_arr.length-1);j++){
    	        					 					String variableName = (String)variableNamesObj.get(x_axis_arr[j].toLowerCase());
    	        					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
    	        					 						variableName = x_axis_arr[j];
    	        					 					}
    	        					 				sql1+="select "+id+","+employeeId+",'"+variableName+"','Benchmark',null,round(avg("+x_axis_arr[j]+"),2),"+reportMetaDataId+"  from data where "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereCondition1+" union ";
    	        					 				sql2+="select "+id+","+employeeId+",'"+variableName+"','Benchmark',"+filter+",round(avg("+x_axis_arr[j]+"),2),"+reportMetaDataId+"  from data where "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereCondition1+" group by "+filter+" union ";
    	        					 				
    	        					 				if(benchMark.equals("OVER_ALL")){
    	        					 					
    	        					 					sql3+="select "+id+","+employeeId+",'"+variableName+"','Benchmark',null,round(avg("+x_axis_arr[j]+"),2),"+reportMetaDataId+"  from data where "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null union ";
         					 						sql4+="select "+id+","+employeeId+",'"+variableName+"','Benchmark',"+filter+",round(avg("+x_axis_arr[j]+"),2),"+reportMetaDataId+"  from data where "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null group by "+filter+" union ";
    	        					 					
    	        					 				}else{
    	        					 						sql3+="select "+id+","+employeeId+",'"+variableName+"','Benchmark',null,round(avg("+x_axis_arr[j]+"),2),"+reportMetaDataId+"  from data  where employeeId in (select employee from hierarchy where manager in (SELECT managerId FROM employee where employeeId="+employeeId+")) and "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereConstrain+" union ";
    	        					 						sql4+="select "+id+","+employeeId+",'"+variableName+"','Benchmark',"+filter+",round(avg("+x_axis_arr[j]+"),2),"+reportMetaDataId+"  from data where employeeId in (select employee from hierarchy where manager in (SELECT managerId FROM employee where employeeId="+employeeId+")) and "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereConstrain+" group by "+filter+" union ";
    	        					 					}
    	        					 				}
    	        					 				String variableName = (String)variableNamesObj.get(x_axis_arr[x_axis_arr.length-1].toLowerCase());
		      					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
		      					 						variableName = x_axis_arr[x_axis_arr.length-1];
		      					 					}
    	        					 				sql1+="select "+id+","+employeeId+",'"+variableName+"','Benchmark',null,round(avg("+x_axis_arr[j]+"),2),"+reportMetaDataId+"  from data where "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereCondition1+" ";
    	        					 				sql2+="select "+id+","+employeeId+",'"+variableName+"','Benchmark',"+filter+",round(avg("+x_axis_arr[j]+"),2),"+reportMetaDataId+"  from data where  "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereCondition1+" group by "+filter+"";
    	        					 				
    	        					 				if(benchMark.equals("OVER_ALL")){
    		        					 				sql3+="select "+id+","+employeeId+",'"+variableName+"','Benchmark',null,round(avg("+x_axis_arr[j]+"),2),"+reportMetaDataId+"  from data where "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+" ";
    		        					 				sql4+="select "+id+","+employeeId+",'"+variableName+"','Benchmark',"+filter+",round(avg("+x_axis_arr[j]+"),2),"+reportMetaDataId+"  from data where "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null group by "+filter+"";
    	        					 				}
    	        					 				else{
    		        					 				sql3+="select "+id+","+employeeId+",'"+variableName+"','Benchmark',null,round(avg("+x_axis_arr[j]+"),2),"+reportMetaDataId+"  from data where employeeId in (select employee from hierarchy where manager in (SELECT managerId FROM employee where employeeId="+employeeId+")) and "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereConstrain+" ";
    		        					 				sql4+="select "+id+","+employeeId+",'"+variableName+"','Benchmark',"+filter+",round(avg("+x_axis_arr[j]+"),2),"+reportMetaDataId+"  from data where employeeId in (select employee from hierarchy where manager in (SELECT managerId FROM employee where employeeId="+employeeId+")) and "+x_axis_arr[j]+" != '' and "+x_axis_arr[j]+" is not null "+whereConstrain+" group by "+filter+"";
    	        					 				}
    	        					 			}
    	        					 				
    	        					 				
    	        					 		}
    	        					 		else if(y_axis!=null && y_axis.length()>0){
    	        					 			String variableName = (String)variableNamesObj.get(x_axis.toLowerCase());
    					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
    					 						variableName = x_axis;
    					 					}
    	        					 			sql1 = "insert into benchmark(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,reportmetadataId) select "+id+","+employeeId+","+y_axis+",'"+variableName+"',null,round(avg("+x_axis+"),2),"+reportMetaDataId+"  from data where "+x_axis+" != '' and "+x_axis+" is not null "+whereCondition1+" group by "+y_axis;
    	        					 			sql2 = "insert into benchmark(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,reportmetadataId) select "+id+","+employeeId+","+y_axis+",'"+variableName+"',"+filter+",round(avg("+x_axis+"),2),"+reportMetaDataId+"  from data where "+x_axis+" != '' and "+x_axis+" is not null "+whereCondition1+" group by "+y_axis+","+filter;
    	        					 			
    	        					 			if(benchMark.equals("OVER_ALL")){
    	        					 				
    	        					 				
    	        					 				sql3 = "insert into benchmark(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,reportmetadataId) select "+id+","+employeeId+","+y_axis+",'"+variableName+"',null,round(avg("+x_axis+"),2),"+reportMetaDataId+"  from data where "+x_axis+" != '' and "+x_axis+" is not null group by "+y_axis;
    		        					 			sql4 = "insert into benchmark(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,reportmetadataId) select "+id+","+employeeId+","+y_axis+",'"+variableName+"',"+filter+",round(avg("+x_axis+"),2),"+reportMetaDataId+"  from data where "+x_axis+" != '' and "+x_axis+" is not null group by "+y_axis+","+filter;
    	        					 				
    	        					 			}else{
    	        					 			sql3 = "insert into benchmark(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,reportmetadataId) select "+id+","+employeeId+","+y_axis+",'"+variableName+"',null,round(avg("+x_axis+"),2),"+reportMetaDataId+"  from data where employeeId in (select employee from hierarchy where manager in (SELECT managerId FROM employee where employeeId="+employeeId+")) and "+x_axis+" != '' and "+x_axis+" is not null "+whereConstrain+" group by "+y_axis;
    	        					 			sql4 = "insert into benchmark(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,reportmetadataId) select "+id+","+employeeId+","+y_axis+",'"+variableName+"',"+filter+",round(avg("+x_axis+"),2),"+reportMetaDataId+"  from data where employeeId in (select employee from hierarchy where manager in (SELECT managerId FROM employee where employeeId="+employeeId+")) and "+x_axis+" != '' and "+x_axis+" is not null "+whereConstrain+" group by "+y_axis+","+filter;
    	        					 			}
    	        					 		}
    	        					 		else{
    	        					 			String variableName = (String)variableNamesObj.get(x_axis.toLowerCase());
    					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
    					 						variableName = x_axis;
    					 					}
    	        					 			whereConstrain = (whereConstrain.length()>0)?whereConstrain:"";
    	        					 			sql1 = "insert into benchmark(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,reportmetadataId) select "+id+","+employeeId+",'"+variableName+"',null,null,round(avg("+x_axis+"),2),"+reportMetaDataId+"  from data where "+x_axis+" != '' and "+x_axis+" is not null "+whereCondition1+"";
    	        					 			sql2 = "insert into benchmark(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,reportmetadataId) select "+id+","+employeeId+",'"+variableName+"',null,"+filter+",round(avg("+x_axis+"),2),"+reportMetaDataId+"  from data where "+x_axis+" != '' and "+x_axis+" is not null "+whereCondition1+" group by "+filter;
    	        					 			
    	        					 			
    	        					 			if(benchMark.equals("OVER_ALL")){
    	        					 				sql3 = "insert into benchmark(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,reportmetadataId) select "+id+","+employeeId+",'"+variableName+"',null,null,round(avg("+x_axis+"),2),"+reportMetaDataId+"  from data where "+x_axis+" != '' and "+x_axis+" is not null "+"";
    		        					 			sql4 = "insert into benchmark(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,reportmetadataId) select "+id+","+employeeId+",'"+variableName+"',null,"+filter+",round(avg("+x_axis+"),2),"+reportMetaDataId+"  from data where "+x_axis+" != '' and "+x_axis+" is not null group by "+filter;
    	        					 				
    	        					 			}else
    	        					 			{
    	        					 			sql3 = "insert into benchmark(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,reportmetadataId) select "+id+","+employeeId+",'"+variableName+"',null,null,round(avg("+x_axis+"),2),"+reportMetaDataId+"  from data where employeeId in (select employee from hierarchy where manager in (SELECT managerId FROM employee where employeeId="+employeeId+")) and "+x_axis+" != '' and "+x_axis+" is not null "+whereConstrain+"";
    	        					 			sql4 = "insert into benchmark(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,reportmetadataId) select "+id+","+employeeId+",'"+variableName+"',null,"+filter+",round(avg("+x_axis+"),2),"+reportMetaDataId+"  from data where employeeId in (select employee from hierarchy where manager in (SELECT managerId FROM employee where employeeId="+employeeId+")) and "+x_axis+" != '' and "+x_axis+" is not null "+whereConstrain+" group by "+filter;
    	        					 			}
    	        					 		}
    	        					 	}
                                                       	 
                                                  
                                                  if(benchMark.equals("IMMEDIATE_SUPERVISOR")||benchMark.equals("OVER_ALL")){
                                                	  	logger.debug(sql3);
                                                        session.connection().createStatement().executeUpdate(sql3);
                                                        if(filter!=null){
                                                        	logger.debug(sql4);
                                                            session.connection().createStatement().executeUpdate(sql4);
                                                        }
                                                  }else{
                                                	  	logger.debug(sql1);
                                                        session.connection().createStatement().executeUpdate(sql1);
                                                        if(filter!=null){
                                                        	 logger.debug(sql2);
                                                             session.connection().createStatement().executeUpdate(sql2);
                                                        }
                                                  }
                                    }
              		 
              	 } catch (HibernateException he) {
                     throw he;
              }
            
            } //end of benchmarkCalculation()
        });
        logger.debug("Leaving into benchmarkCalculation");
	}catch (Exception e) {
		logger.error(e.getStackTrace());
		throw e;
	}
 }

	public List getDistributionList(final Integer templateChartId) throws ApplicationException
	{
		try{
		logger.debug("Entered into getDistributionList");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				List distributionList = null;
                try {
                 String sql = "from Employee where emailId in " +
                 		"(select emailId from Users)";
                         Query q = session.createQuery(sql);
                        distributionList =  (List)q.list();
                        
                return distributionList;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from getDistributionList");
	    return (List)object;
	    
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	}
	
	public List getReportMetaDataIdList(final String emailId) throws ApplicationException
	{
		try{
		logger.debug("Entered into getReportMetaDataIdList");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				List reportMetaDataList = null;
                try {
                		String sql ="from ReportMetadata where distributionlist like '%"+emailId+"%'"; 
                        Query q = session.createQuery(sql);
                        reportMetaDataList =  (List)q.list();
                        
                return reportMetaDataList;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from getReportMetaDataIdList");
	    return (List)object;
	    
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
	}
	
	public List getMailIdList(final String masterDistIdList) throws ApplicationException
	{
		try{
		logger.debug("Entered into getMailIdList");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				List publishReportList = null;
				try {
                		 String sql = "select e.name,d.emailId,group_concat(t.reportTitle separator ',') reportTitle from templatechart t,templatechartreportassign ta, distributionlist d,employee e where t.id = ta.templateChartId and d.masterDistributionTypeId = ta.masterDistId and e.emailId = d.emailId and ta.masterDistId in ("+masterDistIdList+") group by d.emailId";
	        			 SQLQuery q = session.createSQLQuery(sql);
	        			 publishReportList =  (List)q.list();
        			return publishReportList;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from getMailIdList");
	    return (List)object;
	}catch (Exception e) {
		logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
		throw e;
	}
	}
	
	public List getCodeBookData() throws ApplicationException
	{
		try{
		logger.debug("Entered into getCodeBookData");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				List publishReportList = null;
				try {
        		 //String sql = "SELECT  v.columnname,group_concat(c.actualValue separator \"@#@\"),group_concat(c.recodevalue separator \"@#@\") FROM codebook c, variableNames v where v.variableName = c.variablenames group by v.columnName";
						 //String sql = "SELECT c.variablenames,group_concat(c.actualValue separator '@#@'), group_concat(c.recodevalue separator '@#@') FROM codebook c group by c.variableNames";
					String sql = "SELECT c.variablenames,c.actualValue,c.recodevalue,c.displayValue FROM codebook c order by c.variablenames";
	        			 SQLQuery q = session.createSQLQuery(sql);
	        			 publishReportList =  (List)q.list();
        			return publishReportList;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from getCodeBookData");
	    return (List)object;
	}catch (Exception e) {
		logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
		throw e;
	}
	}
	

	public int getNumberofResponse(final TemplateChart templateChart,final String empId){
		logger.debug("Entered into getNumberofResponse");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
		@SuppressWarnings("unused")
		public Object doInHibernate(Session session)
				throws HibernateException, SQLException {
			session.connection().setAutoCommit(true);
			String employeeId=empId;
			String sql="";
            String filter1=templateChart.getFilter1();
            String filter2=templateChart.getFilter2();
            String filter3=templateChart.getFilter3();
            String filterValue1=templateChart.getFilterValues1();
            String filterValue2=templateChart.getFilterValues2();
            String filterValue3=templateChart.getFilterValues3();
            Long createdBy = templateChart.getCreatedBy();
            Long updatedBy = templateChart.getUpdatedBy();
            Date createdDate = templateChart.getCreatedDate();
            Date updatedDate = templateChart.getUpdatedDate();
            List surveyVariableObjList = (List)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.SURVEY_VARIABLES_LIST);
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
				if(whereConstrain.length()>0)
					whereConstrain = "and "+whereConstrain;
				
				StringBuffer columnBuffer = new StringBuffer();
			 		 columnBuffer.append("select count(employeeId) from data as d where ");
			 		 if(surveyVariableObjList !=null){
			 			 int col = surveyVariableObjList.size();
			 			 for(int k=0;k<col;k++){
			 				VariableNames variableNames = (VariableNames)surveyVariableObjList.get(k);
		           			String columnName = variableNames.getColumnName();
		           			columnBuffer.append(" d."+columnName+ " IS NULL");
		           			if(k+1<col){
		           				columnBuffer.append(" " +"and" + " ");
		           			}
			 			 }
			 		 }
			 		 String colbuff="";
			 		 String functionColBufferStr =columnBuffer.toString(); 
			 		 if(columnBuffer.length()>0){
			 			 columnBuffer.append(" and employeeId in (select employee from hierarchy where manager = "+employeeId+") "+whereConstrain);
			 			 colbuff =" - ("+columnBuffer.toString()+") ";
			 			functionColBufferStr=" - ("+functionColBufferStr+" "+whereConstrain+") ";
			 		 }
				try {
					sql = "select * from summary";
					SQLQuery q = session.createSQLQuery(sql);
					List sumList =  (List)q.list();
					int size=sumList.size();
					ArrayList summaryList =new ArrayList();
					if(sumList!=null && size>0 ){
						
						for(int l=0;l<size;l++){
							
							ArrayList summaryList1 =new ArrayList();
							Object[] sumList1 =(Object[]) sumList.get(l);
							for(int m=1;m<sumList1.length;m++){
								summaryList1.add(sumList1[m]);
							}
							summaryList.add(summaryList1);
						}
						
					} else {
						
						Integer noOfResponse =0;
						Integer totalNoResponse =0;
						if(ApplicationUtil.isEmptyOrNull(employeeId)){
							sql="select (select count(*) from data as d where 1=1 "+whereConstrain+")" +functionColBufferStr;
						}else{
							sql="select (select count(*) from data as d where employeeId in (select employee from hierarchy where manager = "+employeeId+")"+whereConstrain+")" +colbuff;
						}
			 			ResultSet rs = session.connection().createStatement().executeQuery(sql);
			 			rs.next();
			 			Integer temp=rs.getInt(1);
	                     if(temp>=0){
	                    	 noOfResponse = rs.getInt(1);
	                    	 templateChart.setNoOfResponse(rs.getInt(1));
	                     }else{
	                    	 templateChart.setNoOfResponse(0);
	                     }
			 			if(!ApplicationUtil.isEmptyOrNull(employeeId)){
			 				sql="select count(id) from data where employeeId in (select employee from hierarchy where manager = "+employeeId+") "+whereConstrain;
			 			}else{
			 				sql="select count(*) from data where 1=1 "+whereConstrain;
			 			}
			 			rs = session.connection().createStatement().executeQuery(sql);
			 			rs.next();
			 			templateChart.setTotalNoResponse(rs.getInt(1));
			 			totalNoResponse= rs.getInt(1);
			 			double percentageOfResponse=0;
			 			if(totalNoResponse!=0){
			 				percentageOfResponse = (double) ((noOfResponse / (double)totalNoResponse)*100);
			 			}
			 			
			 			DecimalFormat df = new DecimalFormat("##");
			 			//templateChart.setPercentageOfResponse((double) (Math.round(percentageOfResponse * 100.0) / 100.0));
			 			templateChart.setPercentageOfResponse(df.format(percentageOfResponse)+" %");
						
					}
					templateChart.setSummaryList(summaryList);
					
				}
				catch(Exception e){
					logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
					throw e;
				}
				return null;
			}
		});
			logger.debug("Leaving from getNumberofResponse");
			return 0;
	}

	
	public List getTeamReport(final String empId,final TemplateChart templateChart) throws ApplicationException
	{
            Object object = null;
            logger.debug("Entered into getTeamReport");
            try {
                    object = hibernateTemplate.execute(new HibernateCallback() {
                            public Object doInHibernate(Session session)
                                            throws HibernateException, SQLException {
                            	Map variableNamesObj=(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.VARIABLE_NAMES_MAP_CACHE_KEY);
                            	List teamReportList = new ArrayList();
                            	List teamReportNameHeaderList = new ArrayList();
                            	teamReportNameHeaderList.add("                ");
                            	teamReportNameHeaderList.add("WNS Overall Average");
                            	teamReportNameHeaderList.add("Reference Group Average");
                            	teamReportNameHeaderList.add("Operating Segment Average");
                            	teamReportNameHeaderList.add("Best in Class Scores");
                            	List respondentsHeaderList = new ArrayList();
                            	respondentsHeaderList.add("Respondents");
                            	respondentsHeaderList.add("");
                            	respondentsHeaderList.add("");
                            	respondentsHeaderList.add("");
                            	respondentsHeaderList.add("");
                            	teamReportList.add(teamReportNameHeaderList);
                            	teamReportList.add(respondentsHeaderList);
                            	List categoryNameList = new ArrayList();
                				try {
                					if(ApplicationUtil.isEmptyOrNull(empId)){
                						return teamReportList;
                					}
                					String sql = "select distinct t.employeeid,e.name,t.totalvalidrespondents from teamreport t left join (select employeeid,name from employee ) e on e.employeeid = t.employeeid where t.managerId='"+empId+"'  order by t.orderNumber";
                					SQLQuery q = session.createSQLQuery(sql);
                					List teamList =  (List)q.list();
                					if(teamList !=null){
                						StringBuffer sb = new StringBuffer("SELECT t.columnname, ");
	               			 			 int col = teamList.size();
	               			 			 int index=2;
	               			 			String orderNumbers="select distinct orderNumber as orderNumber from overallvariablesavg  where empId="+empId+" order by ordernumber";
	               			 		SQLQuery q1 = session.createSQLQuery(orderNumbers);
                					List orderNumberList =  (List)q1.list();
                					if(orderNumberList!=null)
                						for(int i=0;i<orderNumberList.size();i++)
                						{
                							String orderNumber=(String)(orderNumberList.get(i)+"");
                							sb.append(" max(case when ov.empId ='"+empId+"' and ov.orderNumber="+orderNumber+" then ov.value end) orderNumber"+i);
                							if(i+1<=orderNumberList.size()){
	               		           				sb.append(",");
	               		           			}
                						}
	               			 			 for(int k=0;k<col;k++){
	               			 				Object[] rowList = (Object[])teamList.get(k);
	               			 				String employeeId = (String)rowList[0];
	               		           			String empName = (String)rowList[1];
	               		           			if(ApplicationUtil.isEmptyOrNull(empName)){
	               		           				empName="Direct Reportees";
	               		           			}
	               		           			teamReportNameHeaderList.add(empName);
	               		           			int validRespondents = (int)rowList[2];
	               		           			respondentsHeaderList.add(validRespondents);
	               		           			sb.append(" max(case when employeeid="+employeeId+"  then (case when avgrespondents>4 then t.value else '-----' end)  end) val"+k);
	               		           			if(k+1<col){
	               		           				sb.append(",");
	               		           			}
	               			 			 }
	               			 			sb.append(", m.categoryname FROM teamreport t, master_categories m, variablenames v, master_variable_categories mv, overallvariablesavg ov  ");
	               			 			sb.append("where  managerId='"+empId+"' and ov.empId ='"+empId+"'  and mv.mastercategoryid = m.id and v.id = mv.id and t.columnname= v.columnname and t.columnname = ov.columnname group by t.columnname order by m.categoryname, t.columnname");
	               			 			 if(sb.length()>0 && col>0){
	               			 				q = session.createSQLQuery(sb.toString());
	               			 				System.out.println("query:"+sb.toString());
	                    					List reportList =  (List)q.list();
	                    					int size = reportList.size();
	                    					for(int k=0;k<size;k++){
	                    						List rowDataList = new ArrayList();
	                    						Object[] rowList = (Object[])reportList.get(k);
	                    						int len = rowList.length;
	                    						String columnName = (String)rowList[0];
	                    						String label = (String)variableNamesObj.get(columnName.toLowerCase());
	                    						if(ApplicationUtil.isEmptyOrNull(label)){
	                    							label = columnName;
	                    						}
	                    						rowDataList.add(label);
	                    						for(int i=1;i<len;i++){
	                    							if(i<len-1){
	                    								String val = (String) (rowList[i]+"");
		                    							rowDataList.add(val);
	                    							} else{
	                    								String categoryName = (String)rowList[i];
	                    								categoryNameList.add(categoryName);
	                    							}
	                    							
	                    						}
	                    						teamReportList.add(rowDataList);
	                    					}
	                    					teamReportList.add(categoryNameList);
	               			 			 }
                					}
	        	        			return teamReportList;
                                } catch (HibernateException he) {
                                	throw he;
                                }
                            }
                    });
            } catch (Exception e) {
            		logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
                    // TODO: handle exception
                    ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "getTeamReport", 
                                    ApplicationErrorCodes.APP_EC_5, ApplicationConstants.EXCEPTION, e);
            		throw e;
            }
            logger.debug("Leaving from getTeamReport");
        return (List)object;
    }
	
	public List getManagerByLocation(final String empId) throws ApplicationException
	{
            Object object = null;
            logger.debug("Entered into getManagerByLocation");
            try {
                    object = hibernateTemplate.execute(new HibernateCallback() {
                            public Object doInHibernate(Session session)
                                            throws HibernateException, SQLException {
                            	Map variableNamesObj=(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.VARIABLE_NAMES_MAP_CACHE_KEY);
                            	List managerByLocationList = new ArrayList();
                            	List mgrLocationHeaderList= new ArrayList();
                            	mgrLocationHeaderList.add("                ");
                            	managerByLocationList.add(mgrLocationHeaderList);
                            	
                            	List categoryNameList = new ArrayList();
                				try {
                					if(ApplicationUtil.isEmptyOrNull(empId)){
                						return managerByLocationList;
                					}
                					String sql = "select distinct location from manager_avg_bylocation where managerid='"+empId+"'";
                					SQLQuery q = session.createSQLQuery(sql);
                					List LocationList =  (List)q.list();
                					if(LocationList !=null){
                						StringBuffer sb = new StringBuffer("SELECT m.columnname, ");
	               			 			 int col = LocationList.size();
	               			 			 for(int k=0;k<col;k++){
	               			 				String location = (String)LocationList.get(k);
	               			 				mgrLocationHeaderList.add(location);
	               		           			sb.append(" max(case when location='"+location+"'  then (case when avgrespondents>4 then m.value else '-----' end)  end) val"+k);
	               		           			if(k+1<col){
	               		           				sb.append(",");
	               		           			}
	               			 			 }
	               			 			sb.append(", mc.categoryname FROM manager_avg_bylocation m join variablenames v on m.columnName=v.columnName join master_variable_categories mv on v.variableName = mv.variablename  ");
	               			 			sb.append("join master_categories mc on mc.id=mv.mastercategoryid where  managerId='"+empId+"' group by m.columnname order by mc.categoryname,m.columnname");
	               			 			 if(sb.length()>0 && col>0){
	               			 				q = session.createSQLQuery(sb.toString());
	               			 				System.out.println("query:"+sb.toString());
	                    					List reportList =  (List)q.list();
	                    					int size = reportList.size();
	                    					for(int k=0;k<size;k++){
	                    						List rowDataList = new ArrayList();
	                    						Object[] rowList = (Object[])reportList.get(k);
	                    						int len = rowList.length;
	                    						String columnName = (String)rowList[0];
	                    						String label = (String)variableNamesObj.get(columnName.toLowerCase());
	                    						if(ApplicationUtil.isEmptyOrNull(label)){
	                    							label = columnName;
	                    						}
	                    						rowDataList.add(label);
	                    						for(int i=1;i<len;i++){
	                    							if(i<len-1){
	                    								String val = (String) (rowList[i]+"");
		                    							rowDataList.add(val);
	                    							} else{
	                    								String categoryName = (String)rowList[i];
	                    								categoryNameList.add(categoryName);
	                    							}
	                    							
	                    						}
	                    						managerByLocationList.add(rowDataList);
	                    					}
	                    					managerByLocationList.add(categoryNameList);
	               			 			 }
                					}
	        	        			return managerByLocationList;
                                } catch (HibernateException he) {
                                	throw he;
                                }
                            }
                    });
            } catch (Exception e) {
            		logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
                    // TODO: handle exception
                    ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "getManagerByLocation", 
                                    ApplicationErrorCodes.APP_EC_42, ApplicationConstants.EXCEPTION, e);
            		throw e;
            }
            logger.debug("Leaving from getManagerByLocation");
        return (List)object;
    }
	public List getTrendReport(final String empId,final TemplateChart templateChart) throws ApplicationException
	{
            Object object = null;
            logger.debug("Entered into getTrendReport");
            try {
                    object = hibernateTemplate.execute(new HibernateCallback() {
                            public Object doInHibernate(Session session)
                                            throws HibernateException, SQLException {
                            	Map variableNamesObj=(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.VARIABLE_NAMES_MAP_CACHE_KEY);
                            	List trendReportListVal = new ArrayList();
                				try {
                					//String sql = "select distinct t.variablename,t.oldvalue,e.currentvalue from rawdata t left join (select managerId,currentvalue from currentdata ) e on e.managerId = t.managerId  order by t.managerId";
                					String sql = "select distinct v.id, v.categoryname,t.variablename,t.oldvalue,e.currentvalue,e.variablecount from rawdata t " +
                                            "inner join (select variablename, variablenamesid, managerId, currentvalue , variablecount from currentdata) e inner join (select id, categoryname " +
                                            "from master_categories) v ON e.managerId = t.managerId and e.variablename = t.variablename  and v.id = e.variablenamesId and t.variablenameslabelId = v.id and e.managerId = "+empId+" order by v.id";

                					SQLQuery q = session.createSQLQuery(sql);
                					List reportList =  (List)q.list();
                					List rowDataList1 = new ArrayList();
                					List headerList =  new ArrayList();
                					headerList.add("VariableName");
                					headerList.add("2012 Survey Scores");
                					headerList.add("2014 Survey Scores");
                					trendReportListVal.add(headerList);
                					if(reportList !=null){
	                    					int size = reportList.size();
	                    					for(int k=0;k<size;k++){
	                    						List rowDataList = new ArrayList();
	                    						Object[] rowList = (Object[])reportList.get(k);
	                    						int len = rowList.length;
	                    						String variableNameLabel = (String)rowList[1];
                                                String columnName = (String)rowList[2];
                                                String oldData = (String)rowList[3];
                                                String currentData = (String)rowList[4];
                                                int count = 0;
												try {
													count = (int) rowList[5];
												} catch (Exception e) {
													// TODO Auto-generated catch block
													//e.printStackTrace();
												}
                                                
                                                rowDataList1.add(variableNameLabel);
	                    						rowDataList.add(columnName);
	                    						rowDataList.add(oldData);
	                    						rowDataList.add(currentData);
	                    						rowDataList.add(count);
	                    						
	                    						trendReportListVal.add(rowDataList);
	                    					}
	                    					trendReportListVal.add(rowDataList1);
                					}
	        	        			return trendReportListVal;
                                } catch (HibernateException he) {
                                	throw he;
                                }
                            }
                    });
            } catch (Exception e) {
            		logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
                    // TODO: handle exception
                    ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "getTrendReport", 
                                    ApplicationErrorCodes.APP_EC_5, ApplicationConstants.EXCEPTION, e);
            		throw e;
            }
            logger.debug("Leaving from getTrendReport");
        return (List)object;
    }
	
	public Map getBannerTable(final String empId,final Integer templateChartInfoId,final String xAxis,final String yAxis,final String weight,
			final Integer incl,final Integer excl,final Integer medianIncl,final Integer medianExcl,final Integer reportMetaDataId) 
	{
		try{
			final Map codeBookMapObj =(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.CODEBOOK_ACTUAL_RECODE_VALUES_CACHE_KEY);
			logger.debug("Entered into getBannerTable");
			Object object = hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
					Map variableNamesObj=(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.VARIABLE_NAMES_MAP_CACHE_KEY);
					Map columnNamesObj=(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.COLUMN_NAMES_MAP_CACHE_KEY);
					Map bannerTableMapObj = new HashMap();
					StringBuffer sb = new StringBuffer();
					StringBuffer buffer = new StringBuffer();
					StringBuffer buff = new StringBuffer();
					StringBuffer buf = new StringBuffer();
					StringBuffer totalUnweight = new StringBuffer();
					StringBuffer totalWeight = new StringBuffer();
					String y_axis_arr[] = yAxis.split(",");
           		    int length = y_axis_arr.length;
					List bannerTableList = null;
					List result=null;
					List headerList = new ArrayList();
					List headerList1 = new ArrayList();
					List headerList2 = new ArrayList();
					List headerList3 = new ArrayList();
					List bannerTableDataList = new ArrayList();
					List bannerTableWeightList = new ArrayList();
					List bannerTablePercentageList = new ArrayList();
					List bannerTablePercentageWeightList = new ArrayList();
					Map seriesColumnNameBook= (Map)codeBookMapObj.get(xAxis);
					 String templateChartIdStr="";
						if(!ApplicationUtil.isEmptyOrNull(templateChartInfoId)){
		                	  templateChartIdStr = "templateChartInfoId="+templateChartInfoId;
		                  }else{
		                	  templateChartIdStr =  "templateChartInfoId is NULL";
		                  }
					String reportMetaDataIdStr="";
						if(reportMetaDataId!=null){
							reportMetaDataIdStr = "  and  reportmetadataId="+reportMetaDataId;
						}
					if(ApplicationUtil.isEmptyOrNull(empId)){
						
						String sql = "select distinct q2,qname.q,mainVariable  from bannertable b,(select distinct quesname q from bannertable)qname where quesname in (qname.q) and "+templateChartIdStr+reportMetaDataIdStr+" and employeeId is null order by qname.q ,q2";
						SQLQuery q = (SQLQuery) session.createSQLQuery(sql);
						System.out.println(sql);
						  bannerTableList =  (List)q.list();
	        			 if(bannerTableList !=null ){
                        	 int size = bannerTableList.size();
                        	 
                        	 sb.append("select q1");
                        	 totalUnweight.append("select count(t1."+xAxis+") from data t1 union ");
                        	 List yAxisList = Arrays.asList(y_axis_arr);
                        	// Collections.sort(yAxisList);
                        	 Collections.sort(yAxisList);
                        	 y_axis_arr = (String[])yAxisList.toArray();
                        	 for(int j=0;j<length;j++){
                    		     String variableName = (String)variableNamesObj.get(y_axis_arr[j].toLowerCase());
                    		     if(ApplicationUtil.isEmptyOrNull(variableName)){
                    		      variableName = y_axis_arr[j];
                    		     		}
                    		     totalUnweight.append(" select count(t1."+xAxis+") from data t1 where t1."+y_axis_arr[j]+"!=\"\"  group by t1."+y_axis_arr[j]);
                    		     if(j+1<length){
                                 totalUnweight.append(" union all ");
                    		     }
                        		 }
                        	 for(int i=0;i<size;i++){
                        		 List headerValueList = new ArrayList();
                        		 Object[] row = (Object[])bannerTableList.get(i);
                     			 String name = row[1].toString();
                     			 String value = row[0].toString();
                            	 String variableName = (String)variableNamesObj.get(name.toLowerCase());
                            	 String x_axis=row[2].toString();
                            	 String xaxis = (String)variableNamesObj.get(x_axis.toLowerCase());
                            	 
                     			headerValueList.add(variableName);
                     			headerValueList.add(value);
                     			headerValueList.add(xaxis);
                     			
                     			headerList.add(headerValueList);
                     			sb.append(",(case q1 like '%Mean%' or q1 like '%Median%' or q1 like '%Standard%' when 1 then cast(round(sum(case when q2 = '"+value+"' and quesname = '"+name+"' then count else 0 end),1) as decimal(10,1)) when 0 then cast(round(sum(case when q2 = '"+value+"' and quesname = '"+name+"' then count else 0 end)) as decimal(10,0)) end) as '"+value+i+"' ");
                        	 }
                     		sb.append(" from bannertable  where "+templateChartIdStr+reportMetaDataIdStr+" and employeeId is null  group by q1");
                     		System.out.println("Count:"+sb);
                     		System.out.println("totalunweight:"+totalUnweight);
                     		
                     		 ResultSet  rs1 = session.connection().createStatement().executeQuery(totalUnweight.toString());
                       		List totalUnweightRowList = new ArrayList();
                       		totalUnweightRowList.add("Total(actual)");
                       		while (rs1.next()) {
  									String val = rs1.getString(1);
  									totalUnweightRowList.add(val);
                       		}
                       		bannerTableDataList.add(totalUnweightRowList);
                     	
                  			ResultSet  rs = session.connection().createStatement().executeQuery(sb.toString());
							while (rs.next()) {
								 List dataRowList = new ArrayList();
								for(int i=1;i<=(headerList.size()+1);i++){
									String val = rs.getString(i);
									String originalValue = val;
									if(i==1){
										if(seriesColumnNameBook != null){
											String recodeValue = (String)seriesColumnNameBook.get(val);
	                                    	if(!ApplicationUtil.isEmptyOrNull(recodeValue)){
	                                    		val = recodeValue;
	                                    	}	
										}
									}
									 dataRowList.add(val);
									 if(i==1){
										 String str="";
										 if(originalValue.equals("Mean INCL.("+incl+")"))
										 {
											 str="select round((sum("+xAxis+") /count("+xAxis+")),1) from data where "+xAxis+"!='' ";
										 }
										 else if(originalValue.equals("Mean EXCL.("+excl+")")){
											 str ="select round(sum("+xAxis+") /count("+xAxis+"),1) from data  where 1=1 and "+xAxis+" not in ('"+excl+"') " ; 
										 }
										 else if(originalValue.equals("Standard Deviation")){
											 str="select round(stddev("+xAxis+"),1)  from data  where "+xAxis+"!='' ";
										 }
										 else if(originalValue.equals("Standard Error")){
											 str="select round((stddev("+xAxis+")/SQRT(count("+xAxis+"))),1) from data where "+xAxis+"!='' ";
										 }
										 else if(originalValue.equals("Median INCL.("+medianIncl+")")){
											 str="select round(median("+xAxis+"),1)  from data where "+xAxis+"!='' ";
										 }
										 else if(originalValue.equals("Median EXCL.("+medianExcl+")")){
											 str="select round(median("+xAxis+"),1)  from data  where 1=1 and "+xAxis+" not in ('"+medianExcl+"') ";
										 }
										 else{
										 str = "select count("+xAxis+") from data where "+xAxis+"!='' and "+xAxis+"='"+originalValue+"' group by "+xAxis+"";
										 }
										 ResultSet  rs2 = session.connection().createStatement().executeQuery(str);
				                       		if(rs2.next()){
					                       		rs2.previous();
											 	while (rs2.next()) {
					  									String val1 = rs2.getString(i);
					  									dataRowList.add(val1);
					                       		} 
				                       		}else{
				                       			dataRowList.add("0");
				                       		}
									 }
								}
								bannerTableDataList.add(dataRowList);	
							}
	        			 } 
	        			 if(bannerTableList !=null && !weight.equals("-1")){
                        	 int size = bannerTableList.size();
                        	 buffer.append("select q1");
                        	 totalWeight.append("select round(sum(t1."+weight+"),0) from data t1 where t1."+weight+"!=\"\" union");
                        	 for(int j=0;j<length;j++){
                    		     String variableName = (String)variableNamesObj.get(y_axis_arr[j].toLowerCase());
                    		     if(ApplicationUtil.isEmptyOrNull(variableName)){
                    		      variableName = y_axis_arr[j];
                    		     		}
                    		     if(!weight.equals("-1")){
                    		     totalWeight.append(" select round(sum(t1."+weight+"),0) from data t1 where t1."+y_axis_arr[j]+"!=\"\"  and  t1."+weight+"!=\"\"  group by t1."+y_axis_arr[j]);
                    		     if(j+1<length){
                    		    	 totalWeight.append(" union all ");
                    		     			}
                    		     }
                        		 }
                        	 for(int i=0;i<size;i++){
                        		 List headerValueList1 = new ArrayList();
                        		 Object[] row = (Object[])bannerTableList.get(i);
                        		 String name = row[1].toString();
                     			 String value = row[0].toString();
                            	 String variableName = (String)variableNamesObj.get(name.toLowerCase());
                            	 String x_axis=row[2].toString();
                            	 String xaxis = (String)variableNamesObj.get(x_axis.toLowerCase());
                     			
                     			headerValueList1.add(variableName);
                     			headerValueList1.add(value);
                     			headerValueList1.add(xaxis);
                     			
                     			headerList1.add(headerValueList1);
                     			buffer.append(",round(sum(case when q2='"+value+"' and quesname='"+name+"' then weight else 0 end),2) as '"+value+i+"'");
                        	 }
                        	 buffer.append(" from bannertable  where "+templateChartIdStr+reportMetaDataIdStr+" and employeeId is null  group by q1");
                     		System.out.println("Weight:"+buffer);
                     		System.out.println("totalWeight:"+totalWeight);
                     		
                    		 ResultSet  rs1 = session.connection().createStatement().executeQuery(totalWeight.toString());
                      		List totalweightRowList = new ArrayList();
                      		totalweightRowList.add("Total(weight)");
                      		while (rs1.next()) {
 									String val = rs1.getString(1);
 									totalweightRowList.add(val);
                      		}
                      		bannerTableWeightList.add(totalweightRowList);
                      		
                  			ResultSet  rs = session.connection().createStatement().executeQuery(buffer.toString());
							while (rs.next()) {
								 List dataRowList1 = new ArrayList();
								for(int i=1;i<=(headerList1.size()+1);i++){
									String val = rs.getString(i);
									String originalValue=val;
									if(i==1){
										if(seriesColumnNameBook != null){
											String recodeValue = (String)seriesColumnNameBook.get(val);
	                                    	if(!ApplicationUtil.isEmptyOrNull(recodeValue)){
	                                    		val = recodeValue;
	                                    	}	
										}
									}
									 dataRowList1.add(val);
									 if(i==1){
										 String str="";
										 if(originalValue.equals("Mean INCL.("+incl+")"))
										 {
											 str="select round((sum("+xAxis+" * "+weight+") /sum("+weight+")),1)  from data where "+xAxis+"!='' ";
										 }
										 else if(originalValue.equals("Mean EXCL.("+excl+")")){
											 str ="select round((sum("+xAxis+" * "+weight+") /sum("+weight+")),1) from data  where 1=1 and "+xAxis+" not in ('"+excl+"') " ; 
										 }
										 else if(originalValue.equals("Standard Deviation")){
											 str="select round(SQRT((sum("+weight+" *pow("+xAxis+" - ((select sum("+weight+" * "+xAxis+") from data)/(select sum("+weight+") from data)),2)))/(sum("+weight+")*((count("+weight+")-1)/count("+weight+")))),2) from data  where "+xAxis+"!='' ";
										 }
										 else if(originalValue.equals("Standard Error")){
											 str="select round((SQRT((sum("+weight+" *pow("+xAxis+" - ((select sum("+weight+" * "+xAxis+") from data)/(select sum("+weight+") from data)),2)))/(sum("+weight+")*((count("+weight+")-1)/count("+weight+"))))/SQRT(count("+xAxis+"))),2) from data  where "+xAxis+"!='' ";
										 }
										 else if(originalValue.equals("Median INCL.("+medianIncl+")")){
											 str="select round(median("+xAxis+" * "+weight+"),2)  from data  where "+xAxis+"!='' ";
										 }
										 else if(originalValue.equals("Median EXCL.("+medianExcl+")")){
											 str="select round(median("+xAxis+" * "+weight+"),2)  from data  where 1=1 and "+xAxis+" not in ('"+medianExcl+"') " ;
										 }
										 else{
										 str = "select count("+xAxis+") from data where "+xAxis+"!='' and "+xAxis+"='"+originalValue+"' group by "+xAxis+"";
										 }
										 ResultSet  rs2 = session.connection().createStatement().executeQuery(str);
				                       		if(rs2.next()){
					                       		rs2.previous();
											 	while (rs2.next()) {
					  									String val1 = rs2.getString(i);
					  									dataRowList1.add(val1);
					                       		} 
				                       		}else{
				                       			dataRowList1.add("0");
				                       		}
									 }
								}
									bannerTableWeightList.add(dataRowList1);	
							}
	        			 }
	        			 if(bannerTableList !=null ){
                        	 int size = bannerTableList.size();
                        	 buff.append("select q1");
                        	 for(int i=0;i<size;i++){
                        		 List headerValueList2 = new ArrayList();
                        		 Object[] row = (Object[])bannerTableList.get(i);
                        		 String name = row[1].toString();
                     			 String value = row[0].toString();
                            	 String variableName = (String)variableNamesObj.get(name.toLowerCase());
                            	 String x_axis=row[2].toString();
                            	 String xaxis = (String)variableNamesObj.get(x_axis.toLowerCase());
                            	 
                     			headerValueList2.add(variableName);
                     			headerValueList2.add(value);
                     			headerValueList2.add(xaxis);
                     			
                     			headerList2.add(headerValueList2);
                                 buff.append(",concat(round((sum(case when q2='"+value+"' and quesname='"+name+"' then count else 0 end)/" +
                                 		"(select sum("+name+") from data where "+name+"='"+value+"'))*100,2 ),'%') as '"+value+i+"'");
                     			
                        	 }
                     		buff.append(" from bannertable  where  "+templateChartIdStr+reportMetaDataIdStr+" and employeeId is null  group by q1");
                     		System.out.println("Percentage:"+buff);
                  			ResultSet  rs = session.connection().createStatement().executeQuery(buff.toString());
							while (rs.next()) {
								 List dataRowList2 = new ArrayList();
								for(int i=1;i<=(headerList2.size()+1);i++){
									String val = rs.getString(i);
									if(i==1){
										if(seriesColumnNameBook != null){
											String recodeValue = (String)seriesColumnNameBook.get(val);
	                                    	if(!ApplicationUtil.isEmptyOrNull(recodeValue)){
	                                    		val = recodeValue;
	                                    	}	
										}
									}
									 dataRowList2.add(val);
								}
								bannerTablePercentageList.add(dataRowList2);	
							}
	        			 } 
	        			 if(bannerTableList !=null && !weight.equals("-1")){
                        	 int size = bannerTableList.size();
                        	 buf.append("select q1");
                        	 for(int i=0;i<size;i++){
                        		 List headerValueList3 = new ArrayList();
                        		 Object[] row = (Object[])bannerTableList.get(i);
                     			 String variableName = row[1].toString();
                     			 String columnName = (String)columnNamesObj.get(variableName);
                     			 String value = row[0].toString();
                            	 String name = (String)variableNamesObj.get(variableName.toLowerCase());
                            	 String x_axis=row[2].toString();
                            	 String xaxis = (String)variableNamesObj.get(x_axis.toLowerCase());
                     			
                     			
                     			headerValueList3.add(name);
                     			headerValueList3.add(value);
                     			headerValueList3.add(xaxis);
                     			headerList3.add(headerValueList3);
                                 buf.append(",concat(round((sum(case when q2='"+value+"' and quesname='"+variableName+"' then weight else 0 end)/" +
                                 		"(select sum("+weight+") from data where "+columnName+"='"+value+"'))*100,2),'%') as '"+value+i+"'");
                     			
                        	 }
                     		buf.append(" from bannertable  where   "+templateChartIdStr+reportMetaDataIdStr+" and employeeId is null  group by q1");
                     		System.out.println("PercentageWeight:"+buf);
                  			ResultSet  rs = session.connection().createStatement().executeQuery(buf.toString());
							while (rs.next()) {
								 List dataRowList3 = new ArrayList();
								for(int i=1;i<=(headerList3.size()+1);i++){
									String val = rs.getString(i);
									if(i==1){
										if(seriesColumnNameBook != null){
											String recodeValue = (String)seriesColumnNameBook.get(val);
	                                    	if(!ApplicationUtil.isEmptyOrNull(recodeValue)){
	                                    		val = recodeValue;
	                                    	}	
										}
									}
									 dataRowList3.add(val);
								}
								bannerTablePercentageWeightList.add(dataRowList3);	
							}
							}
					}
					else{
						
						String sql = "select distinct q2,qname.q,mainVariable  from bannertable b,(select distinct quesname q from bannertable)qname where quesname in (qname.q) and "+templateChartIdStr+reportMetaDataIdStr+" and employeeId ="+empId+" order by qname.q ,q2";
						SQLQuery q = (SQLQuery) session.createSQLQuery(sql);
						  bannerTableList =  (List)q.list();
	        			 if(bannerTableList !=null ){
                        	 int size = bannerTableList.size();
                        	 
                        	 sb.append("select q1");
                        	 totalUnweight.append("select count(t1."+xAxis+") from data t1 union ");
                        	 List yAxisList = Arrays.asList(y_axis_arr);
                        	 Collections.sort(yAxisList);
                        	 y_axis_arr = (String[])yAxisList.toArray();
                        	 for(int j=0;j<length;j++){
                    		     String variableName = (String)variableNamesObj.get(y_axis_arr[j].toLowerCase());
                    		     if(ApplicationUtil.isEmptyOrNull(variableName)){
                    		      variableName = y_axis_arr[j];
                    		     		}
                    		     totalUnweight.append(" select count(t1."+xAxis+") from data t1 where t1."+y_axis_arr[j]+"!=\"\"  group by t1."+y_axis_arr[j]);
                    		     if(j+1<length){
                                 totalUnweight.append(" union all ");
                    		     }
                        		 }
                        	 for(int i=0;i<size;i++){
                        		 List headerValueList = new ArrayList();
                        		 Object[] row = (Object[])bannerTableList.get(i);
                        		 String name = row[1].toString();
                     			 String value = row[0].toString();
                            	 String variableName = (String)variableNamesObj.get(name.toLowerCase());
                            	 String x_axis=row[2].toString();
                            	 String xaxis = (String)variableNamesObj.get(x_axis.toLowerCase());
                     			
                     			headerValueList.add(variableName);
                     			headerValueList.add(value);
                     			headerValueList.add(xaxis);
                     			
                     			headerList.add(headerValueList);
                     			sb.append(",(case q1 like '%Mean%' or q1 like '%Median%' or q1 like '%Standard%' when 1 then cast(round(sum(case when q2 = '"+value+"' and quesname = '"+name+"' then count else 0 end),1) as decimal(10,1)) when 0 then cast(round(sum(case when q2 = '"+value+"' and quesname = '"+name+"' then count else 0 end)) as decimal(10,0)) end) as '"+value+i+"' ");
                        	 }
                     		sb.append(" from bannertable  where  templateChartInfoId ="+templateChartInfoId+reportMetaDataIdStr+" and employeeId ="+empId+"  group by q1");
                     		System.out.println("Count:"+sb);
                     		System.out.println("totalunweight:"+totalUnweight);
                     		
                     		 ResultSet  rs1 = session.connection().createStatement().executeQuery(totalUnweight.toString());
                       		List totalUnweightRowList = new ArrayList();
                       		totalUnweightRowList.add("Total(actual)");
                       		while (rs1.next()) {
  									String val = rs1.getString(1);
  									totalUnweightRowList.add(val);
                       		}
                       		bannerTableDataList.add(totalUnweightRowList);
                     	
                  			ResultSet  rs = session.connection().createStatement().executeQuery(sb.toString());
							while (rs.next()) {
								 List dataRowList = new ArrayList();
								for(int i=1;i<=(headerList.size()+1);i++){
									String val = rs.getString(i);
									String originalValue = val;
									if(i==1){
										if(seriesColumnNameBook != null){
											String recodeValue = (String)seriesColumnNameBook.get(val);
	                                    	if(!ApplicationUtil.isEmptyOrNull(recodeValue)){
	                                    		val = recodeValue;
	                                    	}	
										}
									}
									 dataRowList.add(val);
									 if(i==1){
										 String str="";
										 if(originalValue.equals("Mean INCL.("+incl+")"))
										 {
											 str="select round((sum("+xAxis+") /count("+xAxis+")),1) from data where "+xAxis+"!='' ";
										 }
										 else if(originalValue.equals("Mean EXCL.("+excl+")")){
											 str ="select round(sum("+xAxis+") /count("+xAxis+"),1) from data  where 1=1 and "+xAxis+" not in ('"+excl+"') " ; 
										 }
										 else if(originalValue.equals("Standard Deviation")){
											 str="select round(stddev("+xAxis+"),1)  from data  where "+xAxis+"!='' ";
										 }
										 else if(originalValue.equals("Standard Error")){
											 str="select round((stddev("+xAxis+")/SQRT(count("+xAxis+"))),1) from data where "+xAxis+"!='' ";
										 }
										 else if(originalValue.equals("Median INCL.("+medianIncl+")")){
											 str="select round(median("+xAxis+"),1)  from data where "+xAxis+"!='' ";
										 }
										 else if(originalValue.equals("Median EXCL.("+medianExcl+")")){
											 str="select round(median("+xAxis+"),1)  from data  where 1=1 and "+xAxis+" not in ('"+medianExcl+"') ";
										 }
										 else{
										 str = "select count("+xAxis+") from data where "+xAxis+"!='' and "+xAxis+"='"+originalValue+"' group by "+xAxis+"";
										 }
										 ResultSet  rs2 = session.connection().createStatement().executeQuery(str);
				                       		if(rs2.next()){
					                       		rs2.previous();
											 	while (rs2.next()) {
					  									String val1 = rs2.getString(i);
					  									dataRowList.add(val1);
					                       		} 
				                       		}else{
				                       			dataRowList.add("0");
				                       		}
									 }
								}
								bannerTableDataList.add(dataRowList);	
							}
	        			 } 
	        			 if(bannerTableList !=null && !weight.equals("-1")){
                        	 int size = bannerTableList.size();
                        	 buffer.append("select q1");
                        	 totalWeight.append("select round(sum(t1."+weight+"),0) from data t1 where t1."+weight+"!=\"\" union");
                        	 for(int j=0;j<length;j++){
                    		     String variableName = (String)variableNamesObj.get(y_axis_arr[j].toLowerCase());
                    		     if(ApplicationUtil.isEmptyOrNull(variableName)){
                    		      variableName = y_axis_arr[j];
                    		     		}
                    		     if(!weight.equals("-1")){
                    		     totalWeight.append(" select round(sum(t1."+weight+"),0) from data t1 where t1."+y_axis_arr[j]+"!=\"\"  and  t1."+weight+"!=\"\"  group by t1."+y_axis_arr[j]);
                    		     if(j+1<length){
                    		    	 totalWeight.append(" union all ");
                    		     			}
                    		     }
                        		 }
                        	 for(int i=0;i<size;i++){
                        		 List headerValueList1 = new ArrayList();
                        		 Object[] row = (Object[])bannerTableList.get(i);
                        		 String name = row[1].toString();
                     			 String value = row[0].toString();
                            	 String variableName = (String)variableNamesObj.get(name.toLowerCase());
                            	 String x_axis=row[2].toString();
                            	 String xaxis = (String)variableNamesObj.get(x_axis.toLowerCase());
                            	 
                     			headerValueList1.add(variableName);
                     			headerValueList1.add(value);
                     			headerValueList1.add(xaxis);
                     			
                     			headerList1.add(headerValueList1);
                     			buffer.append(",round(sum(case when q2='"+value+"' and quesname='"+name+"' then weight else 0 end),2) as '"+value+i+"'");
                        	 }
                        	 buffer.append(" from bannertable  where  templateChartInfoId ="+templateChartInfoId+reportMetaDataIdStr+" and employeeId ="+empId+"  group by q1");
                     		System.out.println("Weight:"+buffer);
                     		System.out.println("totalWeight:"+totalWeight);
                     		
                    		 ResultSet  rs1 = session.connection().createStatement().executeQuery(totalWeight.toString());
                      		List totalweightRowList = new ArrayList();
                      		totalweightRowList.add("Total(weight)");
                      		while (rs1.next()) {
 									String val = rs1.getString(1);
 									totalweightRowList.add(val);
                      		}
                      		bannerTableWeightList.add(totalweightRowList);
                      		
                  			ResultSet  rs = session.connection().createStatement().executeQuery(buffer.toString());
							while (rs.next()) {
								 List dataRowList1 = new ArrayList();
								for(int i=1;i<=(headerList1.size()+1);i++){
									String val = rs.getString(i);
									String originalValue=val;
									if(i==1){
										if(seriesColumnNameBook != null){
											String recodeValue = (String)seriesColumnNameBook.get(val);
	                                    	if(!ApplicationUtil.isEmptyOrNull(recodeValue)){
	                                    		val = recodeValue;
	                                    	}	
										}
									}
									 dataRowList1.add(val);
									 
									 if(i==1){
										 String str="";
										 if(originalValue.equals("Mean INCL.("+incl+")"))
										 {
											 str="select round((sum("+xAxis+" * "+weight+") /(select sum("+weight+") from data)),1)  from data where  "+xAxis+"!='' ";
										 }
										 else if(originalValue.equals("Mean EXCL.("+excl+")")){
											 str ="select round((sum("+xAxis+" * "+weight+") /(select sum("+weight+") from data)),1) from data  where 1=1 and "+xAxis+" not in ('"+excl+"') " ; 
										 }
										 else if(originalValue.equals("Standard Deviation")){
											 
											 str=" select round(SQRT((sum("+weight+" * pow("+xAxis+" - ((select sum("+weight+" * "+xAxis+") from data) / (select sum("+weight+") from data)),2))) / ((select sum("+weight+") from data) *(count("+weight+") - 1)/count("+weight+"))),1)from data where 1 = 1 and "+xAxis+" !=''";
										 }
										 else if(originalValue.equals("Standard Error")){
											 str="select round((SQRT((sum("+weight+" *pow("+xAxis+" - ((select sum("+weight+" * "+xAxis+") from data)/(select sum("+weight+") from data)),2)))/(select (sum("+weight+") from data)*((count("+weight+")-1)/count("+weight+")))))/SQRT(count("+xAxis+")),2) from data  where "+xAxis+"!='' ";
										 }
										 else if(originalValue.equals("Median INCL.("+medianIncl+")")){
											 str="select round(median("+xAxis+" * "+weight+"),2)  from data  where "+xAxis+"!='' ";
										 }
										 else if(originalValue.equals("Median EXCL.("+medianExcl+")")){
											 str="select round(median("+xAxis+" * "+weight+"),2)  from data  where 1=1 and "+xAxis+" not in ('"+medianExcl+"') " ;
										 }
										 else{
										 str = "select count("+xAxis+") from data where "+xAxis+"!='' and "+xAxis+"='"+originalValue+"' group by "+xAxis+"";
										 }
										 ResultSet  rs2 = session.connection().createStatement().executeQuery(str);
				                       		if(rs2.next()){

				                       			
				                       			rs2.previous();
											 	while (rs2.next()) {
					  									String val1 = rs2.getString(i);
					  									dataRowList1.add(val1);
					                       		} 
				                       		}else{
				                       			dataRowList1.add("0");
				                       		}
									 }
								}
									bannerTableWeightList.add(dataRowList1);	
							}
	        			 }
	        			 if(bannerTableList !=null ){
                        	 int size = bannerTableList.size();
                        	 buff.append("select q1");
                        	 for(int i=0;i<size;i++){
                        		 List headerValueList2 = new ArrayList();
                        		 Object[] row = (Object[])bannerTableList.get(i);
                        		 String name = row[1].toString();
                     			 String value = row[0].toString();
                            	 String variableName = (String)variableNamesObj.get(name.toLowerCase());
                            	 String x_axis=row[2].toString();
                            	 String xaxis = (String)variableNamesObj.get(x_axis.toLowerCase());
                            	 
                     			headerValueList2.add(variableName);
                     			headerValueList2.add(value);
                     			headerValueList2.add(xaxis);
                     			
                     			headerList2.add(headerValueList2);
                                 buff.append(",concat(round((sum(case when q2='"+value+"' and quesname='"+name+"' then count else 0 end)/" +
                                 		"(select sum("+name+") from data where "+name+"='"+value+"'))*100,2 ),'%') as '"+value+i+"'");
                     			
                        	 }
                     		buff.append(" from bannertable  where  templateChartInfoId ="+templateChartInfoId+reportMetaDataIdStr+" and employeeId ="+empId+"  group by q1");
                     		System.out.println("Percentage:"+buff);
                  			ResultSet  rs = session.connection().createStatement().executeQuery(buff.toString());
							while (rs.next()) {
								 List dataRowList2 = new ArrayList();
								for(int i=1;i<=(headerList2.size()+1);i++){
									String val = rs.getString(i);
									if(i==1){
										if(seriesColumnNameBook != null){
											String recodeValue = (String)seriesColumnNameBook.get(val);
	                                    	if(!ApplicationUtil.isEmptyOrNull(recodeValue)){
	                                    		val = recodeValue;
	                                    	}	
										}
									}
									 dataRowList2.add(val);
								}
								bannerTablePercentageList.add(dataRowList2);	
							}
	        			 } 
	        			 if(bannerTableList !=null && !weight.equals("-1")){
                        	 int size = bannerTableList.size();
                        	 buf.append("select q1");
                        	 for(int i=0;i<size;i++){
                        		 List headerValueList3 = new ArrayList();
                        		 Object[] row = (Object[])bannerTableList.get(i);
                        		 String variableName = row[1].toString();
                     			 String columnName = (String)columnNamesObj.get(variableName);
                     			 String value = row[0].toString();
                            	 String name = (String)variableNamesObj.get(variableName.toLowerCase());
                            	 String x_axis=row[2].toString();
                            	 String xaxis = (String)variableNamesObj.get(x_axis.toLowerCase());
                     			
                     			
                     			headerValueList3.add(name);
                     			headerValueList3.add(value);
                     			headerValueList3.add(xaxis);
                     			
                     			headerList3.add(headerValueList3);
                                 buf.append(",concat(round((sum(case when q2='"+value+"' and quesname='"+variableName+"' then weight else 0 end)/" +
                                 		"(select sum("+weight+") from data where "+columnName+"='"+value+"'))*100,2),'%') as '"+value+i+"'");
                     			
                        	 }
                     		buf.append(" from bannertable  where   templateChartInfoId ="+templateChartInfoId+reportMetaDataIdStr+" and employeeId ="+empId+"  group by q1");
                     		System.out.println("PercentageWeight:"+buf);
                  			ResultSet  rs = session.connection().createStatement().executeQuery(buf.toString());
							while (rs.next()) {
								 List dataRowList3 = new ArrayList();
								for(int i=1;i<=(headerList3.size()+1);i++){
									String val = rs.getString(i);
									if(i==1){
										if(seriesColumnNameBook != null){
											String recodeValue = (String)seriesColumnNameBook.get(val);
	                                    	if(!ApplicationUtil.isEmptyOrNull(recodeValue)){
	                                    		val = recodeValue;
	                                    	}	
										}
									}
									 dataRowList3.add(val);
								}
								bannerTablePercentageWeightList.add(dataRowList3);	
							}
							}
					}
					bannerTableMapObj.put("bannerTableHeaderList", headerList);
					bannerTableMapObj.put("bannerTableDataList",bannerTableDataList);
					bannerTableMapObj.put("bannerTableWeightList",bannerTableWeightList);
					bannerTableMapObj.put("bannerTablePercentageList",bannerTablePercentageList);
					bannerTableMapObj.put("bannerTablePercentageWeightList",bannerTablePercentageWeightList);
					
        			return bannerTableMapObj;
				} catch (HibernateException he) {
					throw he;
				}
			}
		});
		logger.debug("Leaving from BannerTable");
		return (Map)object;
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
		
	}
	
	public void runReports(final WorkspaceBean workspaceBean) throws ApplicationException{
		try{
		logger.debug("Entered run reports");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			@SuppressWarnings("unused")
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				session.connection().setAutoCommit(true);
				Map functionMapObj = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.MASTER_FUNCTIONS_MAP_CACHE_KEY);
				Map variableNamesObj=(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.VARIABLE_NAMES_MAP_CACHE_KEY);
				try {
        				 Integer functionId =  workspaceBean.getFunctionId();
        				 String chartType = workspaceBean.getChartTypeId();
        				 String x_axis = workspaceBean.getxAxis();
        				 String y_axis = workspaceBean.getyAxis();
        				 String weight = workspaceBean.getWeightVariable();
        				 String filter = workspaceBean.getFilter();
        				 String functionName = (String) functionMapObj.get((Integer)workspaceBean.getFunctionId());
        				 Integer meanIncl =  workspaceBean.getMeanIncl();
        				 Integer meanExcl =  workspaceBean.getMeanExcl();
        				 Integer medianIncl =  workspaceBean.getMedianIncl();
        				 Integer medianExcl =  workspaceBean.getMedianExcl();
        				 String crossTabValue = workspaceBean.getCrossTabValue();
        				 
        				 workspaceBean.setFunctionName(functionName);
        				 workspaceBean.setxAxis(x_axis);
        				 workspaceBean.setyAxis(y_axis);
        				 workspaceBean.setWeightVariable(weight);
        				 workspaceBean.setFilter(filter);
        				 workspaceBean.setChartType(chartType);
        				 workspaceBean.setMeanIncl(meanIncl);
        				 workspaceBean.setMeanExcl(meanExcl);
        				 workspaceBean.setMedianIncl(medianIncl);
        				 workspaceBean.setMedianExcl(medianExcl);
        				 workspaceBean.setCrossTabValue(crossTabValue);
        				 
        				 Integer reportMetaDataId=null;
        				 
        				 String sql1 = "";
        				 String sql2 = "";
        				 
        				 Integer id = null;
        				 String sql3 = "delete from chartaverage where templateChartInfoId is null and employeeId is null";
    					 session.connection().createStatement().executeUpdate(sql3);
    					 sql3= "delete from chartfrequencies where templateChartInfoId is null and employeeId is null";
    					 session.connection().createStatement().executeUpdate(sql3);
    					 sql3= "delete from bannertable where templateChartInfoId is null and employeeId is null and metaDataId is null";
    					 session.connection().createStatement().executeUpdate(sql3);
    					
        				 ResultSet rs = null;
        				 
        				 if(ApplicationUtil.isEmptyOrNull(filter) || filter.equals("-1")){
        					 filter=null;
        				 }
        				 if(ApplicationUtil.isEmptyOrNull(y_axis) || y_axis.equals("-1")){
        					 y_axis=null;
        				 }
        				 
        				 if (functionName.equals("AVERAGE")) {
        					 if(x_axis!=null && x_axis.length()>0){
     					 		if(x_axis.contains(",")){
     					 			String x_axis_arr[] = x_axis.split(",");
     					 			int j=0;

     					 			sql1 = "insert into chartaverage(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,frequency) ";
     					 			sql2 = "insert into chartaverage(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,frequency) ";
     					 			if(y_axis!=null && y_axis.length()>0){
     					 				for(;j<(x_axis_arr.length-1);j++){
     					 					String variableName = (String)variableNamesObj.get(x_axis_arr[j].toLowerCase());
     					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
     					 						variableName = x_axis_arr[j];
     					 					}
	        					 				sql1+="select null,null,"+y_axis+",'"+variableName+"',null,round(avg("+x_axis_arr[j]+"),2), count("+x_axis_arr[j]+") from data group by "+y_axis+" union ";
	        					 				sql2+="select null,null,"+y_axis+",'"+variableName+"',"+filter+",round(avg("+x_axis_arr[j]+"),2), count("+x_axis_arr[j]+") from data group by "+y_axis+","+filter+" union ";
	        					 				}
     					 				String variableName = (String)variableNamesObj.get(x_axis_arr[x_axis_arr.length-1].toLowerCase());
 					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
 					 						variableName = x_axis_arr[x_axis_arr.length-1];
 					 					}
     					 				sql1+="select null,null,"+y_axis+",'"+variableName+"',null,round(avg("+x_axis_arr[j]+"),2), count("+x_axis_arr[j]+") from data group by "+y_axis;
     					 				sql2+="select null,null,"+y_axis+",'"+variableName+"',"+filter+",round(avg("+x_axis_arr[j]+"),2), count("+x_axis_arr[j]+") from data group by "+y_axis+","+filter;
     					 				
     					 			}else{
     					 				for(;j<(x_axis_arr.length-1);j++){
     					 					String variableName = (String)variableNamesObj.get(x_axis_arr[j].toLowerCase());
     					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
     					 						variableName = x_axis_arr[j];
     					 					}
     					 				sql1+="select null,null,'"+variableName+"',null,null,round(avg("+x_axis_arr[j]+"),2), count("+x_axis_arr[j]+") from data union ";
     					 				sql2+="select null,null,'"+variableName+"',null,"+filter+",round(avg("+x_axis_arr[j]+"),2), count("+x_axis_arr[j]+") from data group by "+filter+" union ";
     					 				}
     					 				String variableName = (String)variableNamesObj.get(x_axis_arr[x_axis_arr.length-1].toLowerCase());
 					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
 					 						variableName = x_axis_arr[x_axis_arr.length-1];
 					 					}
     					 				sql1+="select null,null,'"+variableName+"',null,null,round(avg("+x_axis_arr[j]+"),2), count("+x_axis_arr[j]+") from data";
     					 				sql2+="select null,null,'"+variableName+"',null,"+filter+",round(avg("+x_axis_arr[j]+"),2), count("+x_axis_arr[j]+") from data group by "+filter+"";
     					 			}
     					 				
     					 				
     					 		}
     					 		else if(y_axis!=null && y_axis.length()>0){
     					 			String variableName = (String)variableNamesObj.get(x_axis.toLowerCase());
					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
					 						variableName = x_axis;
					 					}
     					 			sql1 = "insert into chartaverage(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,frequency) select null,null,"+y_axis+",'"+variableName+"',null,round(avg("+x_axis+"),2), count("+x_axis+") from data group by "+y_axis;
     					 			sql2 = "insert into chartaverage(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,frequency) select null,null,"+y_axis+",'"+variableName+"',"+filter+",round(avg("+x_axis+"),2), count("+x_axis+") from data  group by "+y_axis+","+filter;
     					 		}
     					 		else{
     					 			String variableName = (String)variableNamesObj.get(x_axis.toLowerCase());
					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
					 						variableName = x_axis;
					 					}
     					 			sql1 = "insert into chartaverage(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,frequency) select null,null,'"+variableName+"',null,null,round(avg("+x_axis+"),2), count("+x_axis+") from data  and "+x_axis+"!=\"\" ";
     					 			sql2 = "insert into chartaverage(templateChartInfoId,employeeId,xAxis,yAxis,filter,value,frequency) select null,null,'"+variableName+"',null,"+filter+",round(avg("+x_axis+"),2), count("+x_axis+") from data and "+x_axis+"!=\"\"  group by "+filter;
     					 		}
     					 	}
  
     					 	//System.out.println(sql2);
     					 			session.connection().createStatement().executeUpdate(sql1);
     					 			if(filter!=null){
     					 				session.connection().createStatement().executeUpdate(sql2);
     					 			}
	        					 				
        				 	}else if(functionName.equals("FREQUENCY")){
        					 	if(x_axis!=null && x_axis.length()>0){
        					 		if(x_axis.contains(",")){
        					 			String x_axis_arr[] = x_axis.split(",");
        					 			sql1 += "insert into chartfrequencies(templateChartInfoId,employeeId,xAxis,yAxis,filter,value) ";
        					 			sql2 += "insert into chartfrequencies(templateChartInfoId,employeeId,xAxis,yAxis,filter,value) ";
        					 			int length = x_axis_arr.length;
        					 			for(int j=0;j<length;j++){
        					 				String variableName = (String)variableNamesObj.get(x_axis_arr[j].toLowerCase());
    					 					if(ApplicationUtil.isEmptyOrNull(variableName)){
    					 						variableName = x_axis_arr[j];
    					 					}
        					 				sql1 +=	"select null,null,'"+variableName+"',"+x_axis_arr[j]+",null,count("+x_axis_arr[j]+") from data group by "+x_axis_arr[j];
        					 				sql2 += "select null,null,'"+variableName+"',"+x_axis_arr[j]+","+filter+",count("+x_axis_arr[j]+") from data  group by "+x_axis_arr[j]+","+filter;
        					 				for(int l=0;l<length;l++){
        					 					if(x_axis_arr[j].equals(x_axis_arr[l])){
        					 						continue;
        					 					}
        					 					sql1 += " union ";
        					 					sql2 += " union ";
        					 					sql1 +=	"select null,null,'"+variableName+"',"+x_axis_arr[l]+",null,0 from data group by "+x_axis_arr[l];
	        					 				sql2 += "select null,null,'"+variableName+"',"+x_axis_arr[l]+","+filter+",0 from data group by "+x_axis_arr[l]+","+filter;
        					 				}
        					 				if(j+1<length){
        					 					sql1 += " union ";
        					 					sql2 += " union ";
        					 				}
        					 			}
        					 		}
        					 		else if(y_axis!=null && y_axis.length()>0){ 
        					 			sql1 = "insert into chartfrequencies(templateChartInfoId,employeeId,xAxis,yAxis,filter,value) (select null,null,t1."+y_axis+", t1."+x_axis+",null,count(1) from data t1   group by t1."+x_axis+", t1."+y_axis+")";
        					 			sql2 = "insert into chartfrequencies(templateChartInfoId,employeeId,xAxis,yAxis,filter,value) (select null,null,t1."+y_axis+", t1."+x_axis+",t1."+filter+",count(1) from data t1   group by t1."+x_axis+", t1."+y_axis+")";
        					 		}
        					 		else{

        					 			sql1 = "insert into chartfrequencies(templateChartInfoId,employeeId,xAxis,yAxis,filter,value) (select null,null,"+x_axis+",null,null,count(1) from data where "+x_axis+"!=\"\"  group by "+x_axis+")";
        					 			sql2 = "insert into chartfrequencies(templateChartInfoId,employeeId,xAxis,yAxis,filter,value) (select null,null,"+x_axis+",null,"+filter+",count(1) from data where "+x_axis+"!=\"\"  group by "+x_axis+","+filter+")";
        					 		}
        					 	}
      
        					 	//System.out.println(sql1);
        					    	session.connection().createStatement().executeUpdate(sql1);
        					    	if(!ApplicationUtil.isEmptyOrNull(filter) && !filter.equals("-1")){
    					 				session.connection().createStatement().executeUpdate(sql2);
    					 			}
        				 	}
        				 	else if(functionName.equals("BANNER TABLE")){
        				 		generateBannerTable(null,null,id,x_axis, y_axis, weight, meanIncl, meanExcl, medianIncl, medianExcl,"Y","Y","Y","Y",null,null,null,null,crossTabValue,null,null,reportMetaDataId);	
        				 	}
        				 	
        				 	else if(functionName.equals("WORDCLOUD") 
        				 			|| functionName.equals("CATEGORICAL_ANALYSIS")
        				 			|| functionName.equals("SENTIMENT")
        				 			|| functionName.equals("KEYDRIVERANALYSIS")){
        				 		try {
        				 			if(ApplicationUtil.isEmptyOrNull(y_axis)){
        				 				y_axis="-1";
        				 			}
        				 			if(ApplicationUtil.isEmptyOrNull(filter)){
        				 				filter="-1";
        				 			}
        							HttpClient client = new HttpClient();
        		    				String url = null;
        		    				url="http://127.0.0.1:8081/processWorkspaceTextMining?logicalName="+functionName+"&xAxiscolumnName="+x_axis+"&yAxisColumnName="+y_axis+"&filterColumn="+filter+"&folderPath="+URLEncoder.encode(ApplicationConstants.VERBATIM_DESTINATION_FOLDER,"UTF-8");
        		    				GetMethod method = new GetMethod(url);
        		    				method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
        		    						new DefaultHttpMethodRetryHandler(20, true));
        		    				int statusCode = -1;

        		    				try {
        		    					statusCode = client.executeMethod(method);
        		    				} catch (HttpException e) {
        		    					// TODO Auto-generated catch block
        		    					System.out.println(e);
        		    					logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
        		    				} catch (IOException e) {
        		    					System.out.println(e);
        		    					logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
        		    				}
        		    				if (statusCode == HttpStatus.SC_OK) {
        		    					System.out.println("success");
        		    					System.out.println(method.getResponseBodyAsString());
        		    				}
        						} catch (Exception e) {
        							// TODO: handle exception
        							logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
        						}

        				 	}
        				 
				} catch (HibernateException he) {
					throw he;
				}
				return true;
			}
		});
		logger.debug("Leaving from run reports");
	}catch (Exception e) {
		logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
		throw e;
	}
	}
	
 public void generateBannerTable(final Integer templateChartInfoId,final String empId, final Integer id,final String xAxisArrayStr,
		 final String y_axis,final String weightStr,final Integer meanIncl,final Integer meanExcl,final Integer medianIncl,
		 final Integer medianExcl,final String meanVal,final String medianVal,final String std_dev,final String std_err,
		 final String type,final String xAxisRaw,final String whereClause,final String displayLevels,
		 final String crossTabValue,final String includeMainVariableZero,final String netWeight,final Integer reportMetaDataId)
 {
	 try{
         logger.debug("Entered run reports");
  Object object = hibernateTemplate.execute(new HibernateCallback() {
@SuppressWarnings("unused")
  public Object doInHibernate(Session session)throws HibernateException, SQLException 
  {
  session.connection().setAutoCommit(true);
                      
  Map variableNamesObj=(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.VARIABLE_NAMES_MAP_CACHE_KEY);
  Map columnNamesObj=(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.COLUMN_NAMES_MAP_CACHE_KEY);
  
  String sql1 = null;
  String mean = null;
  String mean1 =  null;
  String stdDev =  null;
  String stdErr =  null;
  String median =  null;
  String median1 =  null;
  String whereClauseStr =whereClause;
  String weight=weightStr;
  String netWeightArry[] = null;
  if(!ApplicationUtil.isEmptyOrNull(netWeight)){
	  netWeightArry = netWeight.split(",");
  }
  int flagValue=0;
  if(!ApplicationUtil.isEmptyOrNull(whereClauseStr)){
	  whereClauseStr = " and ("+whereClauseStr+")";  
  }else{
	  whereClauseStr="";
  }
  String displayLevelsStr="";
  if((xAxisArrayStr!=null && xAxisArrayStr.length()>0 )&& (y_axis!=null && y_axis.length()>0)){
	  String xAxisArray[] = xAxisArrayStr.split(",");
	  int len = xAxisArray.length;
	  sql1 = "insert into bannertable(templateChartInfoId,metaDataId,employeeId,quesname,mainVariable,weight,q1,q2,indx,count,mainVariableTotal,totalSeg,totalSegWeighted,numberofcount,flag,reportmetadataId) ";
	  mean = "insert into bannertable(templateChartInfoId,metaDataId,employeeId,quesname,mainVariable,weight,q1,q2,indx,count,significance,mean,standard_deviation_weighted,standard_deviation,totalSeg,totalSegWeighted,flag,numberofcount,reportmetadataId)";
	  mean1 = "insert into bannertable(templateChartInfoId,metaDataId,employeeId,quesname,mainVariable,weight,q1,q2,indx,count,significance,mean,standard_deviation_weighted,standard_deviation,totalSeg,totalSegWeighted,flag,numberofcount,reportmetadataId)";
	  stdDev = "insert into bannertable(templateChartInfoId,metaDataId,employeeId,quesname,mainVariable,weight,q1,q2,count,numberofcount,reportmetadataId)";
	  stdErr = "insert into bannertable(templateChartInfoId,metaDataId,employeeId,quesname,mainVariable,weight,q1,q2,count,numberofcount,reportmetadataId)";
	  median = "insert into bannertable(templateChartInfoId,metaDataId,employeeId,quesname,mainVariable,weight,q1,q2,count,numberofcount,reportmetadataId)";
	  median1 = "insert into bannertable(templateChartInfoId,metaDataId,employeeId,quesname,mainVariable,weight,q1,q2,count,numberofcount,reportmetadataId)";
	  String indexResetStr = "";
	  StringBuffer mysqlVariableSb = new StringBuffer("select * from ");
	  boolean flag=true;
	  int netWeightIndex=0;
	  for(int i=0;i<len;i++){
		  if(!ApplicationUtil.isEmptyOrNull(whereClauseStr)){
			  flagValue=1;
		  }
		   if(len>15){
			   	  sql1 = "insert into bannertable(templateChartInfoId,metaDataId,employeeId,quesname,mainVariable,weight,q1,q2,indx,count,mainVariableTotal,totalSeg,totalSegWeighted,numberofcount,flag,reportmetadataId) ";
				  mean = "insert into bannertable(templateChartInfoId,metaDataId,employeeId,quesname,mainVariable,weight,q1,q2,indx,count,significance,mean,standard_deviation_weighted,standard_deviation,totalSeg,totalSegWeighted,flag,numberofcount,reportmetadataId)";
				  mean1 = "insert into bannertable(templateChartInfoId,metaDataId,employeeId,quesname,mainVariable,weight,q1,q2,indx,count,significance,mean,standard_deviation_weighted,standard_deviation,totalSeg,totalSegWeighted,flag,numberofcount,reportmetadataId)";
				  stdDev = "insert into bannertable(templateChartInfoId,metaDataId,employeeId,quesname,mainVariable,weight,q1,q2,count,numberofcount,reportmetadataId)";
				  stdErr = "insert into bannertable(templateChartInfoId,metaDataId,employeeId,quesname,mainVariable,weight,q1,q2,count,numberofcount,reportmetadataId)";
				  median = "insert into bannertable(templateChartInfoId,metaDataId,employeeId,quesname,mainVariable,weight,q1,q2,count,numberofcount,reportmetadataId)";
				  median1 = "insert into bannertable(templateChartInfoId,metaDataId,employeeId,quesname,mainVariable,weight,q1,q2,count,numberofcount,reportmetadataId)";
				  mysqlVariableSb = new StringBuffer("select * from ");
		   }
		   
		   String distinctMainVariable="";
		   int count = 0;
		   String x_axis = xAxisArray[i];
		   
		   if(x_axis.startsWith("XX_NET_TB")){
			   weight=netWeightArry[netWeightIndex];
			   netWeightIndex++;
		   }else{
			   weight=weightStr;
		   }
		   
		   String includeXaxisZero=" and "+x_axis+" !=\"0\"  ";
           if(!ApplicationUtil.isEmptyOrNull(includeMainVariableZero) && includeMainVariableZero.equals("Y")){
         	  includeXaxisZero="";
           }
           if(!ApplicationUtil.isEmptyOrNull(displayLevels)){
				  displayLevelsStr = " and "+x_axis +" in ("+displayLevels+")";
			}
		    StringBuffer sbBuff= new StringBuffer();
		    sbBuff.append("select count("+x_axis+") main from data where 1 = 1 and "+x_axis+" is not null and "+x_axis+" != '' "+includeXaxisZero+" "+displayLevelsStr+"");
		    ResultSet  rs1 = session.connection().createStatement().executeQuery(sbBuff.toString());
		    while (rs1.next()) {
                count = rs1.getInt(1);
		    }
		   
           String xaxis = (String)variableNamesObj.get(x_axis.toLowerCase());
           if(ApplicationUtil.isEmptyOrNull(xaxis)){
                  xaxis = x_axis;
           }
	        String y_axis_arr[] = y_axis.split(",");
	        int length = y_axis_arr.length;
	        String xAxisStr = null;
            if(!ApplicationUtil.isEmptyOrNull(xAxisRaw)){
          	  xAxisStr = xAxisRaw;
            }else{
          	  xAxisStr=x_axis;
            }
	        for(int j=0;j<length;j++){
	        	 if(j==0){
	        		 indexResetStr = ",(select @index"+i+":=0) a"+i;
	        		 mysqlVariableSb.append(" (select @index"+i+":=0) a"+i);
	        		 mysqlVariableSb.append(", (select @meanInclIndex"+i+":=0) meanIncl"+i);
	        		 mysqlVariableSb.append(", (select @meanExclIndex"+i+":=0) meanExcl"+i);
	        	 }else{
	        		 indexResetStr="";
	        	 }
	        	 
          		 String variableName = (String)columnNamesObj.get(y_axis_arr[j]);
                  if(ApplicationUtil.isEmptyOrNull(variableName)){
                         variableName = y_axis_arr[j];
                  }
                 
                  
                  if(count==0){
                	  distinctMainVariable="(select case when alias.main is null then 1 else alias.main end as main from (select (select distinct "+x_axis+" main  from data where  1 = 1 and "+x_axis+" is not null "+includeXaxisZero+" "+displayLevelsStr+") main from dual) alias) a ,";
                  	}
                  else{
                	  distinctMainVariable="(select distinct "+x_axis+" main from data where 1=1 and "+x_axis+" is not null  "+includeXaxisZero+" "+displayLevelsStr+") a ,";
                  }
                  
                  if(ApplicationUtil.isEmptyOrNull(type) || !type.equalsIgnoreCase("SUMMARY")){
	                  if(ApplicationUtil.isEmptyOrNull(weight) || weight.equals("-1") ){
	                         //sql1 +=       "select "+templateChartInfoId+","+id+","+empId+",'"+variableName+"','"+x_axis+"',null,t1."+x_axis+",t1."+y_axis_arr[j]+",count(t1."+x_axis+") from data t1 where 1=1  and t1."+y_axis_arr[j]+"!=\"\" and t1."+x_axis+"!=\"\" group by t1."+x_axis+",t1."+y_axis_arr[j];
	                         sql1 +="select "+templateChartInfoId+" id,"+id+","+empId+" empid,'"+y_axis_arr[j]+"','"+x_axis+"',null, combi.main,combi.seg,lvl.ind,(select count("+x_axis+")" 
		                             +" from data dd where dd."+x_axis+"=combi.main and dd."+y_axis_arr[j]+"=combi.seg "+whereClauseStr+") count,null,(select count("+x_axis+")" 
		                             +" from data dd where dd."+y_axis_arr[j]+"=combi.seg "+whereClauseStr+") totalSeg,null,"+i+","+flagValue+","+reportMetaDataId 
		                             +" from (select a.main,b.seg from " +distinctMainVariable+" " 
		                             +" (select distinct "+y_axis_arr[j]+" seg from data where "+y_axis_arr[j]+" != \"\" and "+y_axis_arr[j]+" is not null ) b" 
		                             +" group by a.main,b.seg) combi, "
		                             +" (select @index"+i+":= @index"+i+"+1 ind,c.seg1 seg1 from (select distinct "+y_axis_arr[j]+" seg1 from data where "+y_axis_arr[j]+" != '' order by "+y_axis_arr[j]+") c) lvl "+indexResetStr+" where lvl.seg1=combi.seg"
		                             +" group by combi.main,combi.seg";
	                  }
	                  else
	                  {
	                         //sql1 +="select "+templateChartInfoId+","+id+","+empId+",'"+variableName+"','"+x_axis+"',round(sum(t1."+weight+"),2),t1."+x_axis+",t1."+y_axis_arr[j]+",count(t1."+x_axis+") from data t1  where 1=1  and t1."+y_axis_arr[j]+"!=\"\" and t1."+x_axis+"!=\"\" group by t1."+x_axis+",t1."+y_axis_arr[j];
	                         sql1 +="select "+templateChartInfoId+" id,"+id+","+empId+" empid,'"+y_axis_arr[j]+"','"+x_axis+"',(select sum(dd."+weight+")" 
	                         +" from data dd where dd."+x_axis+"=combi.main and dd."+y_axis_arr[j]+"=combi.seg "+whereClauseStr+") weight, combi.main,combi.seg,lvl.ind,(select count("+x_axis+")" 
	                         +" from data dd where dd."+x_axis+"=combi.main and dd."+y_axis_arr[j]+"=combi.seg "+whereClauseStr+") count,(select round(sum("+weight+")) from data where "+x_axis+"=combi.main) mainVariableTotal,null,(select sum("+weight+")" 
	                         +" from data dd where  dd."+y_axis_arr[j]+"=combi.seg "+whereClauseStr+") totalSegWeighted,"+i+","+flagValue+","+reportMetaDataId  
	                         +" from (select a.main,b.seg from " +distinctMainVariable+" " 
	                         +" (select distinct "+y_axis_arr[j]+" seg from data where "+y_axis_arr[j]+" != \"\" and "+y_axis_arr[j]+" is not null ) b" 
	                         +" group by a.main,b.seg) combi,"
	                         +" (select @index"+i+":= @index"+i+"+1 ind,c.seg1 seg1 from (select distinct "+y_axis_arr[j]+" seg1 from data where "+y_axis_arr[j]+" != '' order by "+y_axis_arr[j]+") c) lvl "+indexResetStr+" where lvl.seg1 = combi.seg"
	                         +" group by combi.main,combi.seg";
	                  }
	                  if(j+1<length){
	                	  sql1 += " union all ";
	                  }
                  }
        	}
	        
	        if(!ApplicationUtil.isEmptyOrNull(crossTabValue)){
	        if(flag){
	        	if(ApplicationUtil.isEmptyOrNull(type) || !type.equalsIgnoreCase("SUMMARY")){
	        		flag =false;
	        	}
			     if(!ApplicationUtil.isEmptyOrNull(meanVal) && meanVal.equals("Y")){
			        for(int j=0;j<length;j++){
			              String variableName = (String)columnNamesObj.get(y_axis_arr[j]);
			                      if(ApplicationUtil.isEmptyOrNull(variableName)){
			                             variableName = y_axis_arr[j];
			                      }
			                      if(ApplicationUtil.isEmptyOrNull(weight) || weight.equals("-1") ){
			                          mean +="select "+templateChartInfoId+","+id+","+empId+",'"+y_axis_arr[j]+"','"+x_axis+"',null,'Mean INCL.("+meanIncl+")',t1."+y_axis_arr[j]+",lvl.ind,(sum(t1."+xAxisStr+") /count(t1."+xAxisStr+")),null,avg(t1."+xAxisStr+"),null,stddev(t1."+xAxisStr+"),count(t1."+xAxisStr+"),sum(t1."+xAxisStr+"),"+flagValue+","+(i+1)+","+reportMetaDataId +" from data t1,(select @meanInclIndex"+i+":= @meanInclIndex"+i+"+1 ind,c.seg1 seg1 from (select distinct t2."+y_axis_arr[j]+" seg1 from data t2 where t2."+y_axis_arr[j]+" != '' order by t2."+y_axis_arr[j]+") c) lvl where t1."+y_axis_arr[j]+" = lvl.seg1 "+whereClauseStr+"  group by t1."+y_axis_arr[j];
			                      }
			                      else
			                      {
			                    	  mean +="select "+templateChartInfoId+","+id+","+empId+",'"+y_axis_arr[j]+"','"+x_axis+"',(sum(t1."+xAxisStr+" * t1."+weight+") /sum(t1."+weight+")),'Mean INCL.("+meanIncl+")',t1."+y_axis_arr[j]+",lvl.ind,(sum(t1."+xAxisStr+") /count(t1."+xAxisStr+")),null,avg(t1."+xAxisStr+"),SQRT((sum(t1."+weight+" *pow(t1."+xAxisStr+" - ((select sum(t1."+weight+" * t1."+xAxisStr+") from data t1)/(select sum(t1."+weight+") from data t1)),2)))/((select sum(t3."+weight+") from data t3)*((count(t1."+weight+")-1)/count(t1."+weight+")))),stddev(t1."+xAxisStr+"),count(t1."+xAxisStr+"),sum(t1."+weight+"),"+flagValue+",9"+(i+1)+","+reportMetaDataId +" from data t1,(select @meanInclIndex"+i+":= @meanInclIndex"+i+"+1 ind,c.seg1 seg1 from (select distinct t2."+y_axis_arr[j]+" seg1 from data t2 where t2."+y_axis_arr[j]+" != '' order by t2."+y_axis_arr[j]+") c) lvl where t1."+y_axis_arr[j]+" = lvl.seg1 "+whereClauseStr+"  group by t1."+y_axis_arr[j];      
			                      }
			                      if(j+1<length){
			                     mean += " union all ";
			              }
			        	}
			        }
	        	
			     
			     if(!ApplicationUtil.isEmptyOrNull(meanVal) && meanVal.equals("Y")){
			        for(int j=0;j<length;j++){
			              String variableName = (String)columnNamesObj.get(y_axis_arr[j]);
			                      if(ApplicationUtil.isEmptyOrNull(variableName)){
			                             variableName = y_axis_arr[j];
			                      }
			                      if(ApplicationUtil.isEmptyOrNull(weight) || weight.equals("-1") ){
			                            
			                             StringBuffer sb = new StringBuffer();
				                    	  sb.append("select ");
				                    	  sb.append(templateChartInfoId+", ");
				                    	  sb.append(id+", ");
				                    	  sb.append(empId+",");
				                    	  sb.append("'"+y_axis_arr[j]+"', ");
				                    	  sb.append("'"+x_axis+"', ");
				                    	  sb.append("null, ");
				                    	  sb.append("'Mean EXCL.("+meanExcl+")', ");
				                    	  sb.append("t1."+y_axis_arr[j]+", ");
				                    	  sb.append("lvl.ind, ");
				                    	  sb.append("(sum((case when (t1."+xAxisStr+" = "+meanExcl+" or t1."+xAxisStr+" is null) then 0 else t1."+xAxisStr+" end)) / (select count(t2."+xAxisStr+") from data t2 where t2."+xAxisStr+" != '' and t2."+xAxisStr+" not in('"+meanExcl+"') "+whereClauseStr+" and t2."+y_axis_arr[j]+" =t1."+y_axis_arr[j]+" )), ");
				                    	  sb.append("null, ");
				                    	  sb.append("(select avg(t2."+xAxisStr+") from data t2 where t2."+xAxisStr+" != '' and t2."+xAxisStr+" not in('"+meanExcl+"')"+whereClauseStr+" and t2."+y_axis_arr[j]+" =t1."+y_axis_arr[j]+") , ");
				                    	  sb.append("null, ");
				                    	  sb.append("(select stddev(t2."+xAxisStr+") from data t2 where t2."+xAxisStr+" != '' and t2."+xAxisStr+" not in('"+meanExcl+"') "+whereClauseStr+" and t2."+y_axis_arr[j]+" =t1."+y_axis_arr[j]+"), ");
				                    	  sb.append("(select count(t2."+xAxisStr+") from data t2 where t2."+xAxisStr+" != '' and t2."+xAxisStr+" not in('"+meanExcl+"') "+whereClauseStr+" and t2."+y_axis_arr[j]+" =t1."+y_axis_arr[j]+"), ");
				                    	  sb.append("null, ");
				                    	  sb.append(flagValue+","+(i+2)+","+reportMetaDataId +" ");
				                    	  sb.append("from ");
				                    	  sb.append("data t1, ");
				                    	  sb.append("(select  ");
				                    	  sb.append("@meanExclIndex0:=@meanExclIndex0 + 1 ind, c.seg1 seg1 ");
				                    	  sb.append("from ");
				                    	  sb.append("(select distinct ");
				                    	  sb.append("t2."+y_axis_arr[j]+" seg1 ");
				                    	  sb.append("from ");
				                    	  sb.append("data t2 ");
				                    	  sb.append("where ");
				                    	  sb.append("t2."+y_axis_arr[j]+" != '' ");
				                    	  sb.append("order by t2."+y_axis_arr[j]+") c) lvl ");
				                    	  sb.append("where ");
				                    	  sb.append("t1."+y_axis_arr[j]+" = lvl.seg1 ");
				                    	  sb.append(" "+whereClauseStr+" ");
				                    	  sb.append("group by t1."+y_axis_arr[j]+"  ");
				                    	  mean1 += sb.toString();	       
			                      
			                      }
			                      else
			                      {	
			                    	  // below code is for stddevi with weight excluding zero and nulls
			                    	  //mean1 +="select "+templateChartInfoId+","+id+","+empId+",'"+variableName+"','"+x_axis+"',(sum(t1."+xAxisStr+" * t1."+weight+") /sum(t1."+weight+"))," +
			                    	  //	"'Mean EXCL.("+meanExcl+")',t1."+y_axis_arr[j]+",lvl.ind,round((sum(t1."+xAxisStr+") /count(t1."+xAxisStr+")),1),null," +
			                    	  //	" avg(t1."+xAxisStr+"),SQRT((sum(t1."+weight+" *pow(t1."+xAxisStr+" - ((select sum(t1."+weight+" * t1."+xAxisStr+") from data t1)/(select sum(t1."+weight+") " +
			                    	  //	" from data t1)),2)))/(sum(t1."+weight+")*((count(t1."+weight+")-1)/count(t1."+weight+")))), stddev(t1."+xAxisStr+"),count(t1."+xAxisStr+"),sum(t1."+weight+"),"+flagValue+" from data t1, (select @meanExclIndex"+i+":= @meanExclIndex"+i+"+1 ind,c.seg1 seg1 from (select distinct t2."+y_axis_arr[j]+" seg1 from data t2 where t2."+y_axis_arr[j]+" != '' order by t2."+y_axis_arr[j]+") c) lvl where t1."+y_axis_arr[j]+" = lvl.seg1 and t1."+xAxisStr+"!=\"\" and t1."+xAxisStr+" not in ('"+meanExcl+"') "+whereClauseStr+" group by t1."+y_axis_arr[j];
			                    	  StringBuffer sb = new StringBuffer();
			                    	  sb.append("select ");
			                    	  sb.append(templateChartInfoId+", ");
			                    	  sb.append(id+", ");
			                    	  sb.append(empId+",");
			                    	  sb.append("'"+y_axis_arr[j]+"', ");
			                    	  sb.append("'"+x_axis+"', ");
			                    	  sb.append("(sum((case when (t1."+xAxisStr+" = "+meanExcl+" or t1."+xAxisStr+" is null) then 0 else t1."+xAxisStr+" end) * (case when (t1."+xAxisStr+" = "+meanExcl+" or t1."+xAxisStr+" is null) then 0 else t1."+weight+" end)) / sum((case when (t1."+xAxisStr+" = "+meanExcl+" or t1."+xAxisStr+" is null) then 0 else t1."+weight+" end))), ");
			                    	  sb.append("'Mean EXCL.("+meanExcl+")', ");
			                    	  sb.append("t1."+y_axis_arr[j]+", ");
			                    	  sb.append("lvl.ind, ");
			                    	  sb.append("(sum((case when (t1."+xAxisStr+" = "+meanExcl+" or t1."+xAxisStr+" is null) then 0 else t1."+xAxisStr+" end)) / (select count(t2."+xAxisStr+") from data t2 where t2."+xAxisStr+" != '' and t2."+xAxisStr+" not in('"+meanExcl+"') "+whereClauseStr+" and t2."+y_axis_arr[j]+" =t1."+y_axis_arr[j]+" )), ");
			                    	  sb.append("null, ");
			                    	  sb.append("(select avg(t2."+xAxisStr+") from data t2 where t2."+xAxisStr+" != '' and t2."+xAxisStr+" not in('"+meanExcl+"')"+whereClauseStr+" and t2."+y_axis_arr[j]+" =t1."+y_axis_arr[j]+") , ");
			                    	  sb.append("SQRT((sum(t1."+weight+" * pow(t1."+xAxisStr+" - ((select  ");
			                    	  sb.append("sum(t1."+weight+" * t1."+xAxisStr+") ");
			                    	  sb.append("from ");
			                    	  sb.append("data t1) / (select  ");
			                    	  sb.append("sum(t1."+weight+") ");
			                    	  sb.append("from ");
			                    	  sb.append("data t1)), ");
			                    	  sb.append("2))) / ((select sum(t3."+weight+") from data t3) * ((count(t1."+weight+") - 1) / count(t1."+weight+")))), ");
			                    	  sb.append("(select stddev(t2."+xAxisStr+") from data t2 where t2."+xAxisStr+" != '' and t2."+xAxisStr+" not in('"+meanExcl+"') "+whereClauseStr+" and t2."+y_axis_arr[j]+" =t1."+y_axis_arr[j]+"), ");
			                    	  sb.append("(select count(t2."+xAxisStr+") from data t2 where t2."+xAxisStr+" != '' and t2."+xAxisStr+" not in('"+meanExcl+"') "+whereClauseStr+" and t2."+y_axis_arr[j]+" =t1."+y_axis_arr[j]+"), ");
			                    	  sb.append("sum((case when t1."+xAxisStr+" in ('"+meanExcl+"','') then 0 else t1."+weight+" end)), ");
			                    	  sb.append(flagValue+",9"+(i+2)+","+reportMetaDataId +" ");
			                    	  sb.append("from ");
			                    	  sb.append("data t1, ");
			                    	  sb.append("(select  ");
			                    	  sb.append("@meanExclIndex0:=@meanExclIndex0 + 1 ind, c.seg1 seg1 ");
			                    	  sb.append("from ");
			                    	  sb.append("(select distinct ");
			                    	  sb.append("t2."+y_axis_arr[j]+" seg1 ");
			                    	  sb.append("from ");
			                    	  sb.append("data t2 ");
			                    	  sb.append("where ");
			                    	  sb.append("t2."+y_axis_arr[j]+" != '' ");
			                    	  sb.append("order by t2."+y_axis_arr[j]+") c) lvl ");
			                    	  sb.append("where ");
			                    	  sb.append("t1."+y_axis_arr[j]+" = lvl.seg1 ");
			                    	  sb.append(" "+whereClauseStr+" ");
			                    	  sb.append("group by t1."+y_axis_arr[j]+"  ");
			                    	  mean1 += sb.toString();	
			                      }
			                      if(j+1<length){
			                     mean1 += " union all ";
			              }
			        }
			        }
			        
			       if(!ApplicationUtil.isEmptyOrNull(std_dev) && std_dev.equals("Y")){
			    	   for(int j=0;j<length;j++){
			              String variableName = (String)columnNamesObj.get(y_axis_arr[j]);
			                      if(ApplicationUtil.isEmptyOrNull(variableName)){
			                             variableName = y_axis_arr[j];
			                      }
			                      if(ApplicationUtil.isEmptyOrNull(weight) || weight.equals("-1") ){
			                             stdDev +="select "+templateChartInfoId+","+id+","+empId+",'"+y_axis_arr[j]+"','"+x_axis+"',null,'Standard Deviation',t1."+y_axis_arr[j]+",round(stddev(case when t1."+xAxisStr+" is null then 0 else t1."+xAxisStr+" end), 1),"+(i+5)+","+reportMetaDataId +" from data t1 where  1=1 and "+y_axis_arr[j]+" != \"\" "+whereClauseStr+" group by t1."+y_axis_arr[j];
			                      }
			                      else
			                      {
			                             stdDev +="select "+templateChartInfoId+","+id+","+empId+",'"+y_axis_arr[j]+"','"+x_axis+"', round(SQRT((sum("+weight+" * pow("+xAxisStr+" - ((select sum("+weight+" * "+xAxisStr+") from data) / (select sum("+weight+") from data)),2))) / ((select sum("+weight+") from data) *(count("+weight+") - 1)/count("+weight+"))),1),'Standard Deviation',t1."+y_axis_arr[j]+",round(stddev(case when t1."+xAxisStr+" is null then 0 else t1."+xAxisStr+" end), 1),9"+(i+5)+","+reportMetaDataId +" from data t1 where  1=1  and "+y_axis_arr[j]+" != \"\" "+whereClauseStr+" group by t1."+y_axis_arr[j];   
			                      }
			                      if(j+1<length){
			                             stdDev += " union all ";
			              }
			        }
			        }
			        if(!ApplicationUtil.isEmptyOrNull(std_err) && std_err.equals("Y")){
				        for(int j=0;j<length;j++){
				              String variableName = (String)columnNamesObj.get(y_axis_arr[j]);
				                      if(ApplicationUtil.isEmptyOrNull(variableName)){
				                             variableName = y_axis_arr[j];
				                      }
				                      if(ApplicationUtil.isEmptyOrNull(weight) || weight.equals("-1") ){
				                             stdErr +="select "+templateChartInfoId+","+id+","+empId+",'"+y_axis_arr[j]+"','"+x_axis+"',null,'Standard Error',t1."+y_axis_arr[j]+",round((stddev(t1."+xAxisStr+")/SQRT(count(t1."+xAxisStr+"))),1),"+(i+6)+","+reportMetaDataId +" from data t1 where  1=1 and "+y_axis_arr[j]+" != \"\""+whereClauseStr+" group by t1."+y_axis_arr[j];
				                      }
				                      else
				                      {
				                             stdErr +="select "+templateChartInfoId+","+id+","+empId+",'"+y_axis_arr[j]+"','"+x_axis+"',round(SQRT((sum("+weight+" * pow("+xAxisStr+" - ((select sum("+weight+" * "+xAxisStr+") from data) / (select sum("+weight+") from data)),2))) / ((select sum("+weight+") from data) *(count("+weight+") - 1)/count("+weight+")))/SQRT(count("+xAxisStr+")),1),'Standard Error',t1."+y_axis_arr[j]+",round((stddev(t1."+xAxisStr+")/SQRT(count(t1."+xAxisStr+"))),1),9"+(i+6)+","+reportMetaDataId +" from data t1 where  1=1 and "+y_axis_arr[j]+" != \"\""+whereClauseStr+" group by t1."+y_axis_arr[j];   
				                      }
				                      if(j+1<length){
				                             stdErr += " union all ";
				              }
				        }
			        }
			        if(!ApplicationUtil.isEmptyOrNull(medianVal) && medianVal.equals("Y")){
				        for(int j=0;j<length;j++){
				              String variableName = (String)columnNamesObj.get(y_axis_arr[j]);
				                      if(ApplicationUtil.isEmptyOrNull(variableName)){
				                             variableName = y_axis_arr[j];
				                      }
				                      if(ApplicationUtil.isEmptyOrNull(weight) || weight.equals("-1") ){
				                           median +="select "+templateChartInfoId+","+id+","+empId+",'"+y_axis_arr[j]+"','"+x_axis+"',null,'Median INCL.("+medianIncl+")',t1."+y_axis_arr[j]+",round(median(t1."+xAxisStr+"),2) ,"+(i+3)+","+reportMetaDataId +" from data t1 where  t1."+y_axis_arr[j]+"!=\"\" "+whereClauseStr+"  group by t1."+y_axis_arr[j];
				                            
				                      }
				                      else
				                      {
				                             //median +="select "+templateChartInfoId+","+id+","+empId+",'"+y_axis_arr[j]+"','"+x_axis+"',round(median(t1."+xAxisStr+" * t1."+weight+"),2),'Median INCL.("+medianIncl+")',t1."+y_axis_arr[j]+",round(median(t1."+xAxisStr+"),2),"+(i+3)+" from data t1 where  t1."+y_axis_arr[j]+"!=\"\" "+whereClauseStr+" group by t1."+y_axis_arr[j];
				                    	  StringBuffer sb = new StringBuffer();
				                    	  sb.append(" select "+templateChartInfoId+","+id+","+empId+",'"+y_axis_arr[j]+"','"+x_axis+"',v,'Median INCL.("+medianIncl+")',h,v,9"+(i+3)+","+reportMetaDataId +" from( ");
				                    	  sb.append(" select h,v,sw,lvlw,sw/lvlw, (case when (round(cum,4)%1)=0 then 1 else (round(cum,4)%1) end) val from ( ");
				                    	  sb.append(" select h,v,sw,lvlw,sw/lvlw,(@curRank := @curRank+p) cum from ( ");
				                    	  sb.append(" select h,v,sw,lvlw,sw/lvlw p from ( ");
				                    	  sb.append(" select  ");
				                    	  sb.append(" "+y_axis_arr[j]+" h,"+xAxisStr+" as v, ");
				                    	  sb.append(" sum("+weight+") sw,(b.w) lvlw ");
				                    	  sb.append(" from ");
				                    	  sb.append(" data a,(select sum("+weight+") w, "+y_axis_arr[j]+" y from data where 1=1 "+whereClauseStr+" group by "+y_axis_arr[j]+") b,(select @curRank :=0) c where "+y_axis_arr[j]+"!='' and "+y_axis_arr[j]+" = b.y "+whereClauseStr);
				                    	  sb.append(" group by "+xAxisStr+","+y_axis_arr[j]+" ");
				                    	  sb.append(" order by "+y_axis_arr[j]+","+xAxisStr+" asc) a)b)c)b where val>=0.5 group by h ");
				                    	  median += sb.toString();  
				                      }
				                      if(j+1<length){
				                             median += " union all ";
				              }
				        }
			        }
			        if(!ApplicationUtil.isEmptyOrNull(medianVal) && medianVal.equals("Y")){
				        for(int j=0;j<length;j++){
				              String variableName = (String)columnNamesObj.get(y_axis_arr[j]);
				                      if(ApplicationUtil.isEmptyOrNull(variableName)){
				                             variableName = y_axis_arr[j];
				                      }
				                      if(ApplicationUtil.isEmptyOrNull(weight) || weight.equals("-1") ){
				                          median1 +="select "+templateChartInfoId+","+id+","+empId+",'"+y_axis_arr[j]+"','"+x_axis+"',null,'Median EXCL.("+medianExcl+")',t1."+y_axis_arr[j]+",round(median(t1."+xAxisStr+"),2) ,"+(i+4)+","+reportMetaDataId +" from data t1 where t1."+y_axis_arr[j]+"!=\"\" and t1."+xAxisStr+"!=\"\" and t1."+x_axis+" not in ('"+medianExcl+"') "+whereClauseStr+" group by t1."+y_axis_arr[j];
				                    	  
				                      }
				                      else
				                      {
				                             //median1 +="select "+templateChartInfoId+","+id+",null,'"+y_axis_arr[j]+"','"+x_axis+"',round(median(t1."+xAxisStr+" * t1."+weight+"),2),'Median EXCL.("+medianExcl+")',t1."+y_axis_arr[j]+",round(median(t1."+xAxisStr+"),2) ,"+(i+4)+" from data t1 where "+y_axis_arr[j]+" != \"\" and  t1."+xAxisStr+"!=\"\" and t1."+x_axis+" not in ('"+medianExcl+"') "+whereClauseStr+" group by t1."+y_axis_arr[j];
				                    	  StringBuffer sb = new StringBuffer();
				                    	  sb.append(" select "+templateChartInfoId+","+id+","+empId+",'"+y_axis_arr[j]+"','"+x_axis+"',v,'Median EXCL.("+medianExcl+")',h,v,9"+(i+4)+","+reportMetaDataId +" from( ");
				                    	  sb.append(" select h,v,sw,lvlw,sw/lvlw, (case when (round(cum,4)%1)=0 then 1 else (round(cum,4)%1) end) val from ( ");
				                    	  sb.append(" select h,v,sw,lvlw,sw/lvlw,(@curRank := @curRank+p) cum from ( ");
				                    	  sb.append(" select h,v,sw,lvlw,sw/lvlw p from ( ");
				                    	  sb.append(" select  ");
				                    	  sb.append(" "+y_axis_arr[j]+" h,"+xAxisStr+" as v, ");
				                    	  sb.append(" sum("+weight+") sw,(b.w) lvlw ");
				                    	  sb.append(" from ");
				                    	  sb.append(" data a,(select sum("+weight+") w, "+y_axis_arr[j]+" y from data t1 where t1."+xAxisStr+">0 "+whereClauseStr+" group by "+y_axis_arr[j]+") b,(select @curRank :=0) c where "+y_axis_arr[j]+"!='' and "+y_axis_arr[j]+" = b.y and "+xAxisStr+">0 "+whereClauseStr);
				                    	  sb.append(" group by "+xAxisStr+","+y_axis_arr[j]+" ");
				                    	  sb.append(" order by "+y_axis_arr[j]+","+xAxisStr+" asc) a)b)c)b where val>=0.5 group by h ");
				                    	  median1 +=sb.toString();
				                      }
				                      if(j+1<length){
				                             median1 += " union all ";
				              }
				        }
			        }
	        	}
	        }
	        if(len>15){
	          System.out.println("bannerTable: "+sql1);
	  	      System.out.println("bannerTablemean(incl 0):  "+mean);
	  	      System.out.println("bannerTablemean(excl 0):  "+mean1);
	  	      System.out.println("bannerTable median(incl 0):  "+median);
	  	      System.out.println("bannerTable median(excl 0): "+median1);
	  	      System.out.println("bannerTable standared deviation:  "+stdDev);
	  	      System.out.println("bannerTable standared error:  "+stdErr);
	  	      session.connection().createStatement().execute(mysqlVariableSb.toString());
	  	      System.out.println(mysqlVariableSb.toString());
	  		    if(!ApplicationUtil.isEmptyOrNull(sql1)){
	  		    	if(ApplicationUtil.isEmptyOrNull(type) || !type.equalsIgnoreCase("SUMMARY")){
	  			    	session.connection().createStatement().executeUpdate(sql1);
	  		    	}
	  		    }
	  		    
	  		    if(!ApplicationUtil.isEmptyOrNull(crossTabValue)){
	  			if(!ApplicationUtil.isEmptyOrNull(meanVal) && meanVal.equals("Y")){
	  				if(!ApplicationUtil.isEmptyOrNull(mean)){
	  					session.connection().createStatement().executeUpdate(mean);
	  				}
	  				if(!ApplicationUtil.isEmptyOrNull(mean1)){
	  					session.connection().createStatement().executeUpdate(mean1);
	  				}
	  			}
	  			
	  			if(!ApplicationUtil.isEmptyOrNull(medianVal) && medianVal.equals("Y")){
	  				if(!ApplicationUtil.isEmptyOrNull(median)){
	  					session.connection().createStatement().execute("set @curRank :=0");
	  					session.connection().createStatement().executeUpdate(median);
	  				}
	  				if(!ApplicationUtil.isEmptyOrNull(median1)){
	  					session.connection().createStatement().execute("set @curRank :=0");
	  					session.connection().createStatement().executeUpdate(median1);
	  				}
	  			}
	  			
	  			if(!ApplicationUtil.isEmptyOrNull(std_dev) && std_dev.equals("Y")){
	  				if(!ApplicationUtil.isEmptyOrNull(stdDev)){
	  					session.connection().createStatement().executeUpdate(stdDev);
	  				}
	  			}
	  			
	  			if(!ApplicationUtil.isEmptyOrNull(std_err) && std_err.equals("Y")){
	  				if(!ApplicationUtil.isEmptyOrNull(stdErr)){
	  					session.connection().createStatement().executeUpdate(stdErr);
	  				}
	  			}
	  		    }
	        }else{
		        if(i+1<len){
		        	sql1 = sql1+" union all ";
		        	 if(!ApplicationUtil.isEmptyOrNull(crossTabValue)){
		        	if(flag){
		        		mean = mean+" union all ";
		        		mean1 = mean1+" union all ";
		        		stdDev = stdDev+" union all ";
		        		stdErr = stdErr+" union all ";
		        		median = median+" union all ";
		        		median1 = median1+" union all ";
		        			}
		        	 }
		        	mysqlVariableSb.append(",");
		        }
	        }
	  	}
	  	if(len>15){
	  		//do nothing
	  	}else{
	  		System.out.println("bannerTable: "+sql1);
		      System.out.println("bannerTablemean(incl 0):  "+mean);
		      System.out.println("bannerTablemean(excl 0):  "+mean1);
		      System.out.println("bannerTable median(incl 0):  "+median);
		      System.out.println("bannerTable median(excl 0): "+median1);
		      System.out.println("bannerTable standared deviation:  "+stdDev);
		      System.out.println("bannerTable standared error:  "+stdErr);
		      session.connection().createStatement().execute(mysqlVariableSb.toString());
			    if(!ApplicationUtil.isEmptyOrNull(sql1)){
			    	if(ApplicationUtil.isEmptyOrNull(type) || !type.equalsIgnoreCase("SUMMARY")){
				    	session.connection().createStatement().executeUpdate(sql1);
			    	}
			    }
			    
			    if(!ApplicationUtil.isEmptyOrNull(crossTabValue)){
				if(!ApplicationUtil.isEmptyOrNull(meanVal) && meanVal.equals("Y")){
					if(!ApplicationUtil.isEmptyOrNull(mean)){
						session.connection().createStatement().executeUpdate(mean);
					}
					if(!ApplicationUtil.isEmptyOrNull(mean1)){
						session.connection().createStatement().executeUpdate(mean1);
					}
				}
				
				if(!ApplicationUtil.isEmptyOrNull(medianVal) && medianVal.equals("Y")){
					if(!ApplicationUtil.isEmptyOrNull(median)){
						session.connection().createStatement().execute("set @curRank :=0");
						session.connection().createStatement().executeUpdate(median);
					}
					if(!ApplicationUtil.isEmptyOrNull(median1)){
						session.connection().createStatement().execute("set @curRank :=0");
						session.connection().createStatement().executeUpdate(median1);
					}
				}
				
				if(!ApplicationUtil.isEmptyOrNull(std_dev) && std_dev.equals("Y")){
					if(!ApplicationUtil.isEmptyOrNull(stdDev)){
						session.connection().createStatement().executeUpdate(stdDev);
					}
				}
				
				if(!ApplicationUtil.isEmptyOrNull(std_err) && std_err.equals("Y")){
					if(!ApplicationUtil.isEmptyOrNull(stdErr)){
						session.connection().createStatement().executeUpdate(stdErr);
					}
				}
			   }
	  	}
  	}
  return true;
  }
  });
         
  }catch (Exception e) {
               logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
               throw e;
  }

 }


public List getBannerMetaData() throws ApplicationException
{
        Object object = null;
        logger.debug("Entered into getBannerMetaData");
        try {
                object = hibernateTemplate.execute(new HibernateCallback() {
                        public Object doInHibernate(Session session)
                                        throws HibernateException, SQLException {
                        	List BannerMetaDataList=null;
                                try {
                                	  String sql = "from BannerMetaData where type != 'SUMMARY' or type is null";
                                	  String significance="";
                                	  String updateSigVal="";
                                	  String ttest="";
                                	  String updateTtest="";
                                	  Integer reportMetaDataId =null;
                                      Query q = session.createQuery(sql);
                                      BannerMetaDataList =  (List)q.list();
                                        
                                        if(BannerMetaDataList != null){
                                        	session.connection().createStatement().executeUpdate("set sql_mode=''");
                                        	int size =BannerMetaDataList.size();
                                        	for(int i=0;i<size;i++){
                                        		BannerMetaData bannerMetaData =  (BannerMetaData)BannerMetaDataList.get(i);
                                        	    String xAxis= bannerMetaData.getxAxis();
                                        	    String yAxis= bannerMetaData.getyAxis();
                                        		String weight=bannerMetaData.getWeight();
                                        		String mean=bannerMetaData.getMean();
                                        		String median=bannerMetaData.getMedian();
                                        		String std_dev=bannerMetaData.getStd_deviation();
                                        		String std_err=bannerMetaData.getStd_error();
                                        		Integer inclExcl=bannerMetaData.getInclExcl();
                                        		Integer bannerMetaDataId = bannerMetaData.getId();
                                        		String type = bannerMetaData.getType();
                                        		String xAxisRaw = bannerMetaData.getxAxisRaw();
                                        		String whereClause = bannerMetaData.getWhereClause();
                                        		String displayLevels = bannerMetaData.getDisplayLevels();
                                        		String includeMainVariableZero = bannerMetaData.getIncludeMainVariableZero();
                                        		String netWeight= bannerMetaData.getNetWeight();
                                        		
                                        		sql= "delete from bannertable where templateChartInfoId is null and employeeId is null and metaDataId="+bannerMetaDataId;
                           					 //	session.connection().createStatement().executeUpdate(sql);
                           					 	try{
                           					 		generateBannerTable(null,null,bannerMetaDataId,xAxis, yAxis, weight, inclExcl, inclExcl, inclExcl, inclExcl,mean,median,std_dev,std_err,type,xAxisRaw,whereClause,displayLevels,null,includeMainVariableZero,netWeight,reportMetaDataId);
                           					 		logger.debug("*******************************************************************************Success in metadata id"+bannerMetaDataId);
                           					 	}
                           					 		catch(Exception e){
                           					 		System.out
															.println(e);
                           					 		logger.debug("*******************************************************************************Error in metadata id"+bannerMetaDataId);
                           					 		System.out
															.println("*******************************************************************************Error in metadata id"+bannerMetaDataId);
                           					 	}
                                        	}
                                        	StringBuffer sb = new StringBuffer();
                                        	sb.append("(select distinct ");
                                        	sb.append("    metaDataId, ");
                                        	sb.append("    mainVariable, ");
                                        	sb.append("    quesname, ");
                                        	sb.append("    q1, ");
                                        	sb.append("    q2a, ");
                                        	sb.append("    group_concat(val ");
                                        	sb.append("        order by q2b ");
                                        	sb.append("        separator '') as val ");
                                        	sb.append("from ");
                                        	sb.append("    (select  ");
                                        	sb.append("        o . *, ");
                                        	sb.append("            (case ");
                                        	sb.append("                when sig = 1 then lower(t.val) ");
                                        	sb.append("                else t.val ");
                                        	sb.append("            end) as val ");
                                        	sb.append("    from ");
                                        	sb.append("        (select  ");
                                        	sb.append("        metadataId,");
                                        	sb.append("            q1, ");
                                        	sb.append("            q2a, ");
                                        	sb.append("            q2b, ");
                                        	sb.append("            ind, ");
                                        	sb.append("            a1, ");
                                        	sb.append("            a2, ");
                                        	sb.append("            b1, ");
                                        	sb.append("            b2, ");
                                        	sb.append("            chi, ");
                                        	sb.append("            sig, ");
                                        	sb.append("            yates, ");
                                        	sb.append("            mainVariable, ");
                                        	sb.append("            quesname ");
                                        	sb.append("    from ");
                                        	sb.append("        (select  ");
                                        	sb.append("        @c11:=((a1 + a2) * (a1 + b1)) / (a1 + a2 + b1 + b2), ");
                                        	sb.append("            @c12:=((a1 + a2) * (a2 + b2)) / (a1 + a2 + b1 + b2), ");
                                        	sb.append("            @c21:=((b1 + b2) * (a1 + b1)) / (a1 + a2 + b1 + b2), ");
                                        	sb.append("            @c22:=((b1 + b2) * (a2 + b2)) / (a1 + a2 + b1 + b2), ");
                                        	sb.append("            if((@a1 < 5 or @a2 < 5 or @b1 < 5 or @b2 < 5) ");
                                        	sb.append("                and yates = true, @a1c11:=pow(abs((a1 - @c11) - 0.5), 2) / @c11, @a1c11:=pow((a1 - @c11), 2) / @c11), ");
                                        	sb.append("            if((@a1 < 5 or @a2 < 5 or @b1 < 5 or @b2 < 5) ");
                                        	sb.append("                and yates = true, @a2c12:=pow(abs((a2 - @c12) - 0.5), 2) / @c12, @a2c12:=pow((a2 - @c12), 2) / @c12), ");
                                        	sb.append("            if((@a1 < 5 or @a2 < 5 or @b1 < 5 or @b2 < 5) ");
                                        	sb.append("                and yates = true, @b1c21:=pow(abs((b1 - @c21) - 0.5), 2) / @c21, @b1c21:=pow((b1 - @c21), 2) / @c21), ");
                                        	sb.append("            if((@a1 < 5 or @a2 < 5 or @b1 < 5 or @b2 < 5) ");
                                        	sb.append("                and yates = true, @b2c22:=pow(abs((b2 - @c22) - 0.5), 2) / @c22, @b2c22:=pow((b2 - @c22), 2) / @c22), ");
                                        	sb.append("            @chi:=@a1c11 + @a2c12 + @b1c21 + @b2c22 chi, ");
                                        	sb.append("            case lev ");
                                        	sb.append("                when ");
                                        	sb.append("                    0.001 ");
                                        	sb.append("                then ");
                                        	sb.append("                    @teststat1:=12.116 ");
                                        	sb.append("                when ");
                                        	sb.append("                    0.01 ");
                                        	sb.append("                then ");
                                        	sb.append("                    @teststat1:=7.879 ");
                                        	sb.append("                when ");
                                        	sb.append("                    0.05 ");
                                        	sb.append("                then ");
                                        	sb.append("                    @teststat1:=5.024 ");
                                        	sb.append("                when ");
                                        	sb.append("                    0.10 ");
                                        	sb.append("                then ");
                                        	sb.append("                    @teststat1:= 3.841 ");
                                        	sb.append("                else @teststat:=99 ");
                                        	sb.append("            end, ");
                                        	sb.append("            case 2 * lev ");
                                        	sb.append("                when ");
                                        	sb.append("                    0.001 ");
                                        	sb.append("                then ");
                                        	sb.append("                    @teststat2:=12.116 ");
                                        	sb.append("                when ");
                                        	sb.append("                    0.01 ");
                                        	sb.append("                then ");
                                        	sb.append("                    @teststat2:=7.879 ");
                                        	sb.append("                when ");
                                        	sb.append("                    0.05 ");
                                        	sb.append("                then ");
                                        	sb.append("                    @teststat2:=5.024 ");
                                        	sb.append("                when ");
                                        	sb.append("                    0.10 ");
                                        	sb.append("                then ");
                                        	sb.append("                    @teststat2:=3.841 ");
                                        	sb.append("                else @teststat2:=99 ");
                                        	sb.append("            end, ");
                                        	sb.append("            if(@teststat1 = 99 or @teststat2 = 99, @sig:=99, if(@chi > @teststat1, @sig:=2, if(@chi > @teststat2, @sig:=1, @sig:=0))) as sig, ");
                                        	sb.append("            metadataId, ");
                                        	sb.append("            a1, ");
                                        	sb.append("            a2, ");
                                        	sb.append("            b1, ");
                                        	sb.append("            b2, ");
                                        	sb.append("            q1, ");
                                        	sb.append("            q2a, ");
                                        	sb.append("            q2b, ");
                                        	sb.append("            ind, ");
                                        	sb.append("            yates, ");
                                        	sb.append("            mainVariable, ");
                                        	sb.append("            quesname ");
                                        	sb.append("    from ");
                                        	sb.append("        (select  ");
                                        	sb.append("        a.metaDataId, ");
                                        	sb.append("            a.mainVariable, ");
                                        	sb.append("            a.quesname, ");
                                        	sb.append("            q1, ");
                                        	sb.append("            q2a, ");
                                        	sb.append("            q2b, ");
                                        	sb.append("            ind, ");
                                        	sb.append("            counta a1, ");
                                        	sb.append("            countb a2, ");
                                        	sb.append("			totala as b1, ");
                                        	sb.append("			totalb as b2, ");
                                        	sb.append("            0.05 as lev, ");
                                        	sb.append("            'true' as yates ");
                                        	sb.append("    from ");
                                        	sb.append("        (select  ");
                                        	sb.append("        a.metaDataId, ");
                                        	sb.append("            a.mainVariable, ");
                                        	sb.append("            a.quesname, ");
                                        	sb.append("            a.q1, ");
                                        	sb.append("            a.q2 as q2a, ");
                                        	sb.append("            b.q2 as q2b, ");
                                        	sb.append("            b.indx as ind, ");
                                        	sb.append("		    (case ");
                                        	sb.append("                when a.weight is null then 0 ");
                                        	sb.append("                else a.weight ");
                                        	sb.append("            end) as counta, ");
                                        	sb.append("            (case ");
                                        	sb.append("                when b.weight is null then 0 ");
                                        	sb.append("                else b.weight ");
                                        	sb.append("            end) as countb, ");
                                        	sb.append("			(a.totalSegWeighted - (case when a.weight is null then 0 else a.weight end)) as totala,");
                                        	sb.append("          (b.totalSegWeighted - (case when b.weight is null then 0 else b.weight end)) as totalb ");
                                        	sb.append("    from ");
                                        	sb.append("        bannertable a, bannertable b ");
                                        	sb.append("    where ");
                                        	sb.append("        a.metaDataId = b.metaDataId ");
                                        	sb.append("            and a.mainVariable = b.mainVariable ");
                                        	sb.append("            and a.quesname = b.quesname ");
                                        	sb.append("            and a.q1 = b.q1 ");
                                        	sb.append("            and a.q2 != b.q2 ");
                                        	sb.append("			and ((case ");
                                			sb.append("			            when a.weight is null then 0 ");
                        					sb.append("			            else a.weight ");
                							sb.append("		        end) / a.totalSegWeighted) > ((case ");
        									sb.append("		            when b.weight is null then 0 ");
								            sb.append("		            else b.weight ");
						            		sb.append("			        end) / b.totalSegWeighted)");
                                        	sb.append("            and a.q1 not like ('Mean INCL%') ");
                                        	sb.append("            and a.q1 not like ('Mean EXCL%') ");
                                        	sb.append("            and a.q1 not like ('Standard Deviation%') ");
                                        	sb.append("            and a.q1 not like ('Standard Error%') ");
                                        	sb.append("            and a.q1 not like ('Median INCL%') ");
                                        	sb.append("            and a.q1 not like ('Median EXCL%')) a ");
                                        	sb.append("    ) original, (select @c11:=0) a, (select @c12:=0) b, (select @c21:=0) c, (select @c22:=0) d, (select @chi:=0) e, (select @a1c11:=0) f, (select @a2c12:=0) g, (select @b1c21:=0) h, (select @b2c22:=0) i, (select @teststat:=0) j, (select @sig:=0) k) alias ");
                                        	sb.append("    order by a1 , a2) as o ");
                                        	sb.append("    join tempt as t ON o.ind = t.id and sig != 0 ");
                                        	sb.append("    order by o.metaDataId , o.mainVariable , o.quesname , o.q1 , o.q2a , o.q2b) as oo ");
                                        	sb.append("group by oo.metaDataId , oo.mainVariable , oo.quesname , oo.q1 , oo.q2a) ");
                                        	System.out.println(sb);
                                        	logger.debug(sb.toString());
                                        updateSigVal="update bannertable a join " +sb.toString()+" b "+
                                                    "on (a.metaDataId=b.metaDataId and a.mainvariable = b.mainVariable and a.quesname = b.quesname and a.q1 = b.q1 and a.q2 = b.q2a) "+
                                                    "set a.significance = b.val";
                                       System.out.println("significance:::"+updateSigVal);
                                       logger.debug("----------------------------------------");
                                       logger.debug(updateSigVal);
                                       logger.debug("----------------------------------------");
                                      //session.connection().createStatement().execute("select  * from  ((select @c11:=0) a, (select @c12:=0) b, (select @c21:=0) c, (select @c22:=0) d, (select @chi:=0) e, (select @a1c11:=0) f, (select @a2c12:=0) g, (select @b1c21:=0) h, (select @b2c22:=0) i, (select @teststat:=0) j, (select @sig:=0) k,(select @a1:=0) n,(select @b1:=0) o,(select @a2:=0) p,(select @b2:=0) q)");
                                       //session.connection().createStatement().executeUpdate(updateSigVal);
                                       
                                   	 
                                   	sb= new StringBuffer();
                                   	sb.append(" (select distinct ");
                                   	sb.append("     metaDataId, ");
                                   	sb.append("     mainVariable, ");
                                   	sb.append("     quesname, ");
                                   	sb.append("     q1, ");
                                   	sb.append("     q2a, ");
                                   	sb.append("     group_concat(val ");
                                   	sb.append("         order by q2b ");
                                   	sb.append("         separator '') as val ");
                                   	sb.append(" from ");
                                   	sb.append("     (select  ");
                                   	sb.append("         k . *, ");
                                   	sb.append("             (case ");
                                   	sb.append("                 when sig = 2 then m.val ");
                                   	sb.append("                 else lower(m.val) ");
                                   	sb.append("             end) as val ");
                                   	sb.append("     from ");
                                   	sb.append("         (select  ");
                                   	sb.append("         metaDataId, ");
                                   	sb.append("             mainVariable, ");
                                   	sb.append("             quesname, ");
                                   	sb.append("             ind, ");
                                   	sb.append("             m1, ");
                                   	sb.append("             d1, ");
                                   	sb.append("             m2, ");
                                   	sb.append("             d2, ");
                                   	sb.append("             n1, ");
                                   	sb.append("             n2, ");
                                   	sb.append("             q1, ");
                                   	sb.append("             q2a, ");
                                   	sb.append("             q2b, ");
                                   	sb.append("             o.df, ");
                                   	sb.append("             o.lev, ");
                                   	sb.append("             val_m, ");
                                   	sb.append("             t.value as threshold, ");
                                   	sb.append("             t1.value as threshold1, ");
                                   	sb.append("             if((val_m > t.value), 2, if((val_m > t1.value), 1, 0)) as sig ");
                                   	sb.append("     from ");
                                   	sb.append("         (select  ");
                                   	sb.append("         metadataid, ");
                                   	sb.append("             mainVariable, ");
                                   	sb.append("             quesname, ");
                                   	sb.append("             ind, ");
                                   	sb.append("             m1, ");
                                   	sb.append("             d1, ");
                                   	sb.append("             m2, ");
                                   	sb.append("             d2, ");
                                   	sb.append("             n1, ");
                                   	sb.append("             n2, ");
                                   	sb.append("             lev, ");
                                   	sb.append("             q1, ");
                                   	sb.append("             q2a, ");
                                   	sb.append("             q2b, ");
                                   	sb.append("             if(vartype = 2, (@se:=pow(((pow(d1, 2) / n1) + (pow(d2, 2) / n2)), 0.5)), null), ");
                                   	sb.append("             if(vartype = 2, (@df:=cast(pow(@se, 4) / ((pow((pow(d1, 2) / n1), 2) / (n1 - 1)) + (pow((pow(d2, 2) / n1), 2) / (n2 - 1))) ");
                                   	sb.append("                 as decimal (10 , 0 ))), null), ");
                                   	sb.append("             if(vartype = 1, (@sp:=(((n1 - 1) * pow(d1, 2)) + ((n2 - 1) * pow(d2, 2))) / (n1 + n2 - 2)), null), ");
                                   	sb.append("             if(vartype = 1, (@se:=pow(((1 / n1) + (1 / n2)) * @sp, 0.5)), null), ");
                                   	sb.append("             if(vartype = 1, (@df:=round((n1 + n2 - 2))), null), ");
                                   	sb.append("             @val_m:=abs(m1 - m2) / @se as val_m, ");
                                   	sb.append("             if(@df > 1000, 1000, @df) as df ");
                                   	sb.append("     from ");
                                   	sb.append("         (select  ");
                                   	sb.append("         a.metaDataId, ");
                                   	sb.append("             a.mainVariable, ");
                                   	sb.append("             a.quesname, ");
                                   	sb.append("             ind, ");
                                   	sb.append("             q1, ");
                                   	sb.append("             q2a, ");
                                   	sb.append("             q2b, ");
                                   	sb.append("             m1, ");
                                   	sb.append("             m2, ");
                                   	sb.append("             n1, ");
                                   	sb.append("             n2, ");
                                   	sb.append("             d1, ");
                                   	sb.append("             d2, ");
                                   	sb.append("             0.05 as lev, ");
                                   	sb.append("             1 as vartype ");
                                   	sb.append("     from ");
                                   	sb.append("         (select  ");
                                   	sb.append("         a.metaDataId, ");
                                   	sb.append("             a.mainVariable, ");
                                   	sb.append("             a.quesname, ");
                                   	sb.append("             b.indx as ind, ");
                                   	sb.append("             a.q1, ");
                                   	sb.append("             a.q2 as q2a, ");
                                   	sb.append("             b.q2 as q2b, ");
                                   	sb.append("             a.weight as m1, ");
                                   	sb.append("             b.weight as m2, ");
                                   	sb.append("             a.standard_deviation_weighted as d1, ");
                                   	sb.append("             b.standard_deviation_weighted as d2, ");
                                   	sb.append("             a.totalSegWeighted n1, ");
                                   	sb.append("             b.totalSegWeighted n2 ");
                                   	sb.append("     from ");
                                   	sb.append("         bannertable a, bannertable b ");
                                   	sb.append("     where ");
                                   	sb.append("         a.metaDataId = b.metaDataId ");
                                   	sb.append("             and a.mainVariable = b.mainVariable ");
                                   	sb.append("             and a.quesname = b.quesname ");
                                   	sb.append("             and a.q1 = b.q1 ");
                                   	sb.append("             and a.q2 != b.q2 ");
                                   	sb.append("             and a.weight > b.weight ");
                                   	sb.append("             and a.q1 like ('Mean%')) a) aa, (select @se:=0) a, (select @df:=0) b, (select @sp:=0) c, (select @val:=0) d) as o ");
                                   	sb.append("     join tinv as t ON (t.df = o.df and o.lev = t.lev and t.tails=2) ");
                                   	sb.append("     join tinv as t1 ON (t1.df = o.df and (2 * o.lev) = t1.lev and t1.tails=2) ");
                                   	sb.append("     order by n1 , n2 asc) as k ");
                                   	sb.append("    join tempt as m ON k.ind = m.id and sig != 0 ");
                                   	sb.append("     order by k.metaDataId , k.mainVariable , k.quesname , k.q1 , k.q2a , k.q2b) as l ");
                                   	sb.append(" group by l.metaDataId , l.mainVariable , l.quesname , l.q1 , l.q2a) ");
                                   	logger.debug(sb.toString());
                                   	System.out.println(sb.toString());
                                   	updateTtest="update bannertable a join" +sb.toString()+" b "+
                                                    " on (a.metaDataId=b.metaDataId and a.mainvariable = b.mainVariable and a.quesname = b.quesname and a.q1 = b.q1 and a.q2 = b.q2a) "+
                                                    " set a.significance = b.val where a.q1 like('Mean%')";
                                   	System.out.println("ttest:::"+updateTtest);
                                   //	session.connection().createStatement().execute("select  * from  ((select @se:=0) a, (select @df:=0) b, (select @sp:=0) c, (select @val:=0) d)");
                                   // session.connection().createStatement().executeUpdate(updateTtest);
                                        }
                                return BannerMetaDataList;
                                } catch (HibernateException he) {
                                        throw he;
                                }
                        }
                });
        } catch (Exception e) {
        		logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
                // TODO: handle exception
                ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "getBannerMetaData", 
                                ApplicationErrorCodes.APP_EC_6, ApplicationConstants.EXCEPTION, e);
        		throw e;
        }

        logger.debug("Leaving from getBannerMetaData");  
    return (List)object;
}

public List downloadBannertable(final String fileName,final String percentage) throws ApplicationException
{
        Object object = null;
        logger.debug("Entered into downloadBannertable");
        try {
                object = hibernateTemplate.execute(new HibernateCallback() {
                        public Object doInHibernate(Session session)
                                        throws HibernateException, SQLException {
                        	List BannerMetaDataList=null;
                                try {
                                	
                                 String sql = "from BannerMetaData";
                                      Query q = session.createQuery(sql);
                                         BannerMetaDataList =  (List)q.list();
                                         
                                         XSSFWorkbook workbook= new XSSFWorkbook();
                                         XSSFSheet sheet =  workbook.createSheet("new sheet");
                                        if(BannerMetaDataList != null){
                                        	rowIndex=0;
                                        	int size =BannerMetaDataList.size();
                                        	for(int i=0;i<size;i++){
                                        		BannerMetaData bannerMetaData =  (BannerMetaData)BannerMetaDataList.get(i);
                                        		Integer bannerMetaDataId = bannerMetaData.getId();
                                        		Integer inclExcl =bannerMetaData.getInclExcl();
                                        		String xAxis =bannerMetaData.getxAxis();
                                        		String yAxis =bannerMetaData.getyAxis();
                                        		String weight =bannerMetaData.getWeight();
                                        		String type = bannerMetaData.getType();
                                        		String title1= bannerMetaData.getTitle1();
                                        		String title2= bannerMetaData.getTitle2();
                                        		String title3= bannerMetaData.getTitle3();
                                        		String xAxisRaw=bannerMetaData.getxAxisRaw();
                                        		String whereClause=bannerMetaData.getWhereClause();
                                        		String levelOrder = bannerMetaData.getLevelOrder();
                                        		String displayLevel= bannerMetaData.getDisplayLevels();
                                        		String totalFrequencyOrder = bannerMetaData.getTotalFrequencyOrder();
                                        		String totalFrequencyOrderLevelExclude = bannerMetaData.getTotalFrequencyOrderLevelExclude();
                                        		String totalFrequencyOrderVariableExclude = bannerMetaData.getTotalFrequencyOrderVariableExclude();
                                        		String mainVariableOrder= bannerMetaData.getMainVariableOrder();
                                        		String netWeight = bannerMetaData.getNetWeight();
                                        		try {
                                        				System.out
																.println("--------------------------------------------------------rown num "+rowIndex);
                                        			 	logger.error("--------------------------------------------------------rown num "+rowIndex);
                                        			 	getBannerTableList(bannerMetaDataId,inclExcl,xAxis,yAxis,weight,sheet,type,title1,title2,title3,xAxisRaw,whereClause,levelOrder,displayLevel,percentage,totalFrequencyOrder,totalFrequencyOrderVariableExclude,totalFrequencyOrderLevelExclude,mainVariableOrder,netWeight);
                                        			 	}
                                        			catch (Exception e) {
													// TODO: handle exception
                                    				System.out
															.println(e);
												}
                                        		rowIndex = rowIndex+3;
                                        	}
                                        }
                                        FileOutputStream fileOut =  new FileOutputStream(fileName);
                                    	workbook.write(fileOut);
                                    	fileOut.close();
                                } catch (HibernateException he) {
                                        throw he;
                                } catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} 
								return BannerMetaDataList;
                        }
                });
        } catch (Exception e) {
        		logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
                // TODO: handle exception
                ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "downloadBannertable", 
                                ApplicationErrorCodes.APP_EC_37, ApplicationConstants.EXCEPTION, e);
        		throw e;
        }

        logger.debug("Leaving from downloadBannertable");  
    return (List)object;
}
public int getBannerTableList(final Integer metaDataId,final Integer inclExcl,final String xAxis,final String yAxis,final String weightStr,final XSSFSheet sheet,
		final String type,final String title1,final String title2,final String title3,final String xAxisRaw,final String whereClause,final String levelOrder,final String displayLevel,
		final String isOnlypercentage,final String totalFrequencyOrder,final String totalFrequencyOrderVariableExclude,final String totalFrequencyOrderLevelExclude,final String mainVariableOrder,final String netWeight) throws ApplicationException
{
	        Object object = null;
	        logger.debug("Entered into getBannerTableList with "+metaDataId);
	        System.out.println("Entered into getBannerTableList with "+metaDataId);
	        try {
	        	object = hibernateTemplate.execute(new HibernateCallback() {
	                public Object doInHibernate(Session session)
	                                throws HibernateException, SQLException {
	                   Map codeBookMapObj =(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.CODEBOOK_ACTUAL_RECODE_VALUES_CACHE_KEY);
	                   Map variableNamesObj=(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.VARIABLE_NAMES_MAP_CACHE_KEY);
	                   Map columnNamesObj=(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.COLUMN_NAMES_MAP_CACHE_KEY);
	                   Map bannerTableMapObj = new HashMap();
	                   String weight=weightStr;
	                   String netWeightArry[] = null;
	                   if(!ApplicationUtil.isEmptyOrNull(netWeight)){
	                 	  netWeightArry = netWeight.split(",");
	                   }
	                   Map mapXAxisNetWeightObj = new HashMap();
	                                 StringBuffer sb = new StringBuffer();
	                                 StringBuffer totalUnweight = new StringBuffer();
	                                 StringBuffer buff = new StringBuffer();
	                                 StringBuffer totalWeight = new StringBuffer();
	                                 StringBuffer stringBuff = new StringBuffer();
	                                 List bannerTableList = null;
	                                 boolean frequency=true;
	                                 boolean percentage=true;
					                 boolean significance=true;
	                                 if(!ApplicationUtil.isEmptyOrNull(isOnlypercentage)){
	                                	 percentage=true;
	                                	 frequency=false;
	                                	 significance=false;
	                                 }
	                                 
					                 String levelOrderStr="";
					                 if(!ApplicationUtil.isEmptyOrNull(levelOrder)){
					                	 levelOrderStr=levelOrder;
					                 }
					                 
					                 String totalFrequencyOrderStr="";
					                 if(ApplicationUtil.isEmptyOrNull(totalFrequencyOrder) || totalFrequencyOrder.equals("N")){
					                	 totalFrequencyOrderStr=" q1*1 ";
					                 }
					                 else{
					                	 totalFrequencyOrderStr=" mainVariableTotal *1 desc";
					                 }
					                 String totalFrequencyOrderExcludeStr="";
					                 String totalFrequencyOrderExcludeStrAlias="";
					                 String totalFrequencyOrderExcludeAlias="";
					                 
					                 if(ApplicationUtil.isEmptyOrNull(totalFrequencyOrderLevelExclude) && ApplicationUtil.isEmptyOrNull(totalFrequencyOrderVariableExclude)){
					                	 totalFrequencyOrderExcludeStr="";
					                	 totalFrequencyOrderExcludeStrAlias="";
					                	 totalFrequencyOrderExcludeAlias="";
					                 }
					                 else{
					                	 totalFrequencyOrderExcludeStr=" id not in (select id from bannertable where mainVariable in ('"+totalFrequencyOrderVariableExclude+"') and q1 in ("+totalFrequencyOrderLevelExclude+")  and metaDataId = '"+metaDataId+"') and ";
					                	 totalFrequencyOrderExcludeStrAlias=" select a.* from ( ";
					                	 totalFrequencyOrderExcludeAlias=" )a ";
					                 }
					                 
					                 String mainvariableOrderStr="";
					                 if(ApplicationUtil.isEmptyOrNull(mainVariableOrder)){
					                	 mainvariableOrderStr=" numberofcount ";
					                 }
					                 else{
					                	 mainvariableOrderStr=" -1 ";
					                 }
					                 
	                                 try {
	                                	 String mainVariableStr1=",mainVariable";
	                                	 String mainVariableStr="";
	                                	 String xAxisColumnStr="";
	                                	 String whereClauseStr = whereClause;
	                                	 int flagValue=0;
	                                	 if(!ApplicationUtil.isEmptyOrNull(whereClauseStr)){
	                                		 flagValue=1;
	                                		 if(ApplicationUtil.isEmptyOrNull(type) || (!type.equalsIgnoreCase("SUMMARY") && !type.equalsIgnoreCase("SUMMARY1"))){
	                                			 whereClauseStr = " and ("+whereClauseStr+")";
	                                		 }
	                                	 }else{
	                                		 whereClauseStr="";
	                                	 }
	                                	 String xAxisArry[] = null;
	                                	 if(xAxis!= null && xAxis.contains(",")){
	                                		 mainVariableStr="mainVariable,";
	                                		 mainVariableStr1="";
	                                		 xAxisArry = xAxis.split(",");
	                                		 int len = xAxisArry.length;
	                                		 int netWeightIndex=0;
	                                		 for(int i=0;i<len;i++){
	                                			 xAxisColumnStr = xAxisColumnStr+ "'"+xAxisArry[i].trim()+"'";
	                                			 if(!ApplicationUtil.isEmptyOrNull(netWeight) && xAxisArry[i].trim().startsWith("XX_NET_TB")){
	                                				 String netWeightStr = netWeightArry[netWeightIndex];
	                                				 netWeightIndex++;
	                                				 mapXAxisNetWeightObj.put(xAxisArry[i].trim(), netWeightStr);
	                                			 }
	                                			 if(i+1<len){
	                                				 xAxisColumnStr=xAxisColumnStr+",";
	                                			 }
	                                		 }
	                                	 }
	                                	 String sql = null;
	                                	 if(ApplicationUtil.isEmptyOrNull(type) || (!type.equalsIgnoreCase("SUMMARY") && !type.equalsIgnoreCase("SUMMARY1"))){
	                                		 sql = "select distinct q2,qname.q "+mainVariableStr1+"  from bannertable b,(select distinct quesname q from bannertable)qname where metaDataId = '"+metaDataId+"' and quesname in (qname.q) order by indx,qname.q ,q2";
	                                	 }else{
	                                		 sql = "select distinct q2,qname.q "+mainVariableStr1+"  from bannertable b,(select distinct quesname q from bannertable)qname where  quesname in (qname.q) and metaDataId <"+metaDataId+" and flag="+flagValue+"  order by indx,qname.q ,q2";
	                                	 }
	                                	 System.out.println("Header Query::"+sql);
	                                	 SQLQuery q = (SQLQuery) session.createSQLQuery(sql);
	                                	 System.out.println("Header Query executed..............");
	                                        bannerTableList =  (List)q.list();
	                                        if(bannerTableList !=null ){
	                                               Map seriesColumnNameBook= (Map)codeBookMapObj.get(xAxis.trim().toLowerCase());
	                                               int size = bannerTableList.size();
	                                   String y_axis_arr[] = yAxis.split(",");
	                                   int length = y_axis_arr.length;
	                                   
	                                   Row commonTitleRow = sheet.createRow(rowIndex);
	                                   rowIndex++;
	                                   rowIndex++;
	                                   Cell commonTitleRowCell = commonTitleRow.createCell((short)0);
	                                   commonTitleRowCell.setCellValue("INSIGNIA MARKETING RESEARCH INC.PALAU BRAND ASSESSMENT QUANTITATIVE CONSUMER SURVEY");
	                                   
	                                   if(!ApplicationUtil.isEmptyOrNull(title1)){
	                                	   Row rowTitle1 = sheet.createRow(rowIndex);
	                                	   sheet.autoSizeColumn((short) (0));
		                                   rowIndex++;
		                                   Cell title1Cell = rowTitle1.createCell((short)0);
		                                   title1Cell.setCellValue(title1.toUpperCase());
		                                   rowTitle1 = sheet.createRow(rowIndex);
		                                   rowIndex++;
	                                   }
	                                   
	                                   if(!ApplicationUtil.isEmptyOrNull(title2)){
	                                	   Row rowTitle2 = sheet.createRow(rowIndex);
	                                	   sheet.autoSizeColumn((short) (0));
		                                   rowIndex++;
		                                   Cell title2Cell = rowTitle2.createCell((short)0);
		                                   title2Cell.setCellValue(title2.toUpperCase());
		                                   rowTitle2 = sheet.createRow(rowIndex);
	                                   }
	                                   if(!ApplicationUtil.isEmptyOrNull(title3)){
	                                	   Row rowTitle3 = sheet.createRow(rowIndex);
	                                	   sheet.autoSizeColumn((short) (0));
		                                   rowIndex++;
		                                   Cell title3Cell = rowTitle3.createCell((short)0);
		                                   title3Cell.setCellValue(title3.toUpperCase());
		                                   rowIndex++;
	                                   }
	                                   	CellStyle varialbeStyle = sheet.getWorkbook().createCellStyle();
							        	varialbeStyle.setAlignment(CellStyle.ALIGN_CENTER);
							        	CellStyle dataStyle = sheet.getWorkbook().createCellStyle();
							        	dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
							        	
	                                if(ApplicationUtil.isEmptyOrNull(weight)){
	                                	int variableHeaderRowIndex = rowIndex;
	                                	int endMergeIndex=0;
		                                int startMergeIndex=0;
							        	Row variableRow = sheet.createRow(rowIndex);
		                                   rowIndex++;
		                                   Row factorHeaderRow = sheet.createRow(rowIndex);
		                                   rowIndex++;
		                                   Row charRow = sheet.createRow(rowIndex);
		                                   rowIndex++;
		                                   int mainVariable=0;
		                                   int total=1;
		                                   int dataCell=0;
		                                   int variableHeaderCell=2;
		                                   int factorHeaderCell=2;
		                                   int charHeaderCell=2;
		                                   String tempName="";
		                                   sb.append(totalFrequencyOrderExcludeStrAlias+" select mainVariable,q1 ");
		                                   char ch='B';
		                                   String tempWhereClause=whereClauseStr;
		                                   
	                                       if(!ApplicationUtil.isEmptyOrNull(type) && (type.equalsIgnoreCase("SUMMARY") || type.equalsIgnoreCase("SUMMARY1"))){
	                                    	   if(tempWhereClause.equals("")){
	                                    		   tempWhereClause="";
	                                    	   }
	                                    	   else{
	                                        	 tempWhereClause=" and ("+tempWhereClause+" )";
	                                    	   }
	                                       }
	                                       
		                                   totalUnweight.append("select count(*) from data t1 where 1=1  "+tempWhereClause);
		                                 for(int k=0;k<size;k++){
		                                 Object[] row = (Object[])bannerTableList.get(k);
		                                
		                                 String name = row[1].toString();
		                                 String value = row[0].toString();
		                                 String variableName = (String)variableNamesObj.get(name.trim().toLowerCase());
		                                 if(ApplicationUtil.isEmptyOrNull(variableName)){
		                                	 variableName = "";
		                                 }
		                                 String x_axis="";
		                                 if(xAxis!= null && xAxis.contains(",")){
		                                	 
		                                 }else{
		                                	 x_axis = row[2].toString();
		                                 }
		                                 String xaxis = (String)variableNamesObj.get(x_axis.toLowerCase());
		                                 if(ApplicationUtil.isEmptyOrNull(xaxis)){
		                                	 xaxis = x_axis;
		                                 }
		                                 
		                                 if(ApplicationUtil.isEmptyOrNull(name) || !tempName.equalsIgnoreCase(name)){
		                                         tempName=name;
		                                         totalUnweight.append(" union all ");
		                                         totalUnweight.append(" select count("+name+") from data t1 where t1."+name+"!=\"\" "+tempWhereClause+"  group by t1."+name);
		                                         if(startMergeIndex>0 && endMergeIndex>0){
		                                        	 /*CellRangeAddress region = new CellRangeAddress(variableHeaderRowIndex, variableHeaderRowIndex, startMergeIndex, endMergeIndex);
		                                     		 sheet.addMergedRegion(region);
		                                     		 RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, region, sheet, sheet.getWorkbook());
		                                     		 RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, region, sheet, sheet.getWorkbook());
		                                     		 RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, region, sheet, sheet.getWorkbook());
		                                     		 RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, region, sheet, sheet.getWorkbook());*/
		                                         }
		                                        // Cell mainVariableCell = variableRow.createCell((short)mainVariable);
		                                         //mainVariableCell.setCellValue(xaxis);
		                                         Cell totalCell = variableRow.createCell((short)total);
		                                         totalCell.setCellValue("TOTAL");
		                                         Cell cell = variableRow.createCell((short)variableHeaderCell);
		                                         startMergeIndex = variableHeaderCell;
		                                         endMergeIndex = variableHeaderCell;
		                                         cell.setCellValue(variableName.toUpperCase());
		                          		         cell.setCellStyle(varialbeStyle);
		                                 }else{
		                                	 endMergeIndex++;
		                                 }
		                                 Cell factorCell = factorHeaderRow.createCell((short)factorHeaderCell);
		                                 Map yAxisVariableCodeBookObj= (Map)codeBookMapObj.get(name.trim().toLowerCase());
		                                 String recodedValue=null;
		                                 if(yAxisVariableCodeBookObj!= null){
		                                	 recodedValue = (String)yAxisVariableCodeBookObj.get(value);
		                                 }
		                                 if(ApplicationUtil.isEmptyOrNull(recodedValue)){
		                                	 recodedValue = value;
		                                 }
		                                 factorCell.setCellValue(recodedValue.toUpperCase());
		                                 factorCell.setCellStyle(dataStyle);
		                                 Cell charCellVal = charRow.createCell((short)1);
		                                 charCellVal.setCellValue("(A)");
		                                 charCellVal.setCellStyle(dataStyle);
		                                 Cell charCell = charRow.createCell((short)charHeaderCell);
		                                 charCell.setCellValue("("+ch+")");
		                                 charCell.setCellStyle(dataStyle);
		                                 ch++;
		                                 charHeaderCell++;
		                                 factorHeaderCell++;
		                                 variableHeaderCell++;
		                                 String percentageSelectClause="";
		                                
						                 if(percentage){
						                	 percentageSelectClause =",round((sum(case when q2='"+value+"' and quesname='"+name+"' then count  end)/" +
		                                               "(select sum("+name+") from data where "+name+"='"+value+"' "+tempWhereClause+"))*100,1 ) 'percentage"+k+"'";
						                 }
						                 String significanceSelectClause ="";
						                 if(significance){
						                	 significanceSelectClause =",max(case when q2='"+value+"' and quesname='"+name+"' then significance else ''  end) as 'sign"+k+"'";
						                 }
						                 sb.append(",(case when q1 like '%Mean%' or q1 like '%Median%' or q1 like '%Standard%' then round(sum(case when q2 = '"+value+"' and quesname = '"+name+"' then count  end), 1) else round(sum(case when q2 = '"+value+"' and quesname = '"+name+"' then count  end)) end) 'count"+k+"' "+percentageSelectClause+" "+significanceSelectClause);
		                                 }
		                                 
		                                 if(startMergeIndex>0 && endMergeIndex>0){
                                        	 /*CellRangeAddress region = new CellRangeAddress(variableHeaderRowIndex, variableHeaderRowIndex, startMergeIndex, endMergeIndex);
                                     		 sheet.addMergedRegion(region);
                                     		 RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, region, sheet, sheet.getWorkbook());
                                    		 RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, region, sheet, sheet.getWorkbook());
                                    		 RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, region, sheet, sheet.getWorkbook());
                                    		 RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, region, sheet, sheet.getWorkbook());*/
                                         }
		                                 if(percentage && !significance){
	  	                                	 size = size*2;
	  	                                 	}
	      	                                 if(significance){
	      	                                	 if(percentage){
	      	                                		size = size*3;
	      	                                	 }else{
	      	                                		size = size*2;
	      	                                	 }
	      	                                 }
	      	                                 
	      	                                 if(!ApplicationUtil.isEmptyOrNull(totalFrequencyOrderLevelExclude) && !ApplicationUtil.isEmptyOrNull(totalFrequencyOrderVariableExclude) ){
	      	                                	 stringBuff.append(sb.toString());
	      	                                 }
		                                	 if(ApplicationUtil.isEmptyOrNull(type) || (!type.equalsIgnoreCase("SUMMARY") && !type.equalsIgnoreCase("SUMMARY1"))){
				                                	sb.append(" from bannertable where  "+totalFrequencyOrderExcludeStr+"  templateChartInfoId is NULL and metaDataId = '"+metaDataId+"'  group by "+mainVariableStr+" q1 order by numberofcount ,"+totalFrequencyOrderStr+" "+levelOrderStr+" "+totalFrequencyOrderExcludeAlias);
				                                	if(!ApplicationUtil.isEmptyOrNull(totalFrequencyOrderLevelExclude) && !ApplicationUtil.isEmptyOrNull(totalFrequencyOrderVariableExclude) ){
				                                		stringBuff.append(" from bannertable  where  templateChartInfoId is NULL and metaDataId = '"+metaDataId+"' and id in (select id from bannertable where mainVariable in ('"+totalFrequencyOrderVariableExclude+"') and q1 in ("+totalFrequencyOrderLevelExclude+")  and metaDataId = '"+metaDataId+"') group by "+mainVariableStr+" q1 order by numberofcount "+levelOrderStr+" "+totalFrequencyOrderExcludeAlias);
				                                		sb.append(" union all ");
					                                	sb.append(stringBuff.toString());
				                                	}
				                                }else if(type.equalsIgnoreCase("SUMMARY")){
				                                	sb.append(" from bannertable  where  templateChartInfoId is NULL and mainVariable in ("+xAxisColumnStr+") and q1 like 'Mean%' and metaDataId < "+metaDataId+"  and flag="+flagValue+" group by "+mainVariableStr+" q1 order by id");
				                                }else{
				                                	sb.append(" from bannertable  where  templateChartInfoId is NULL and mainVariable in ("+xAxisColumnStr+") and q1 = '1' and metaDataId < "+metaDataId+"  and flag="+flagValue+" group by "+mainVariableStr+" q1 order by id");
				                                }
				                                
		                                System.out.println("percentage Unweight:"+sb);
		                              
		                                   System.out.println("Total actual with out weight:"+totalUnweight);
		                                    int actual=0;
		                                    Row totalRow = sheet.createRow(rowIndex);
		                                    rowIndex++;
		                                    Cell totalactualCell = totalRow.createCell((short)actual);
		                                    totalactualCell.setCellValue("TOTAL (ACTUAL)");
		                                 ResultSet  rs1 = session.connection().createStatement().executeQuery(totalUnweight.toString());
		                                 while (rs1.next()) {
		                                        String val = rs1.getString(1);
		                                         Cell factorCell = totalRow.createCell((short)total);
		                                         factorCell.setCellValue(Double.parseDouble(val));
		                                         factorCell.setCellType(Cell.CELL_TYPE_NUMERIC);
		                                         factorCell.setCellStyle(dataStyle);
		                                         
		                                  total++;
		                                 }             
                                       logger.debug(sb.toString());
		                                 ResultSet  rs = session.connection().createStatement().executeQuery(sb.toString());
		                                 String temp="";
		                                 int rawIndex=-1;
		                                 String xAxisRawArray[] = null;
		                                 if(!ApplicationUtil.isEmptyOrNull(xAxisRaw)){
		                                	 xAxisRawArray = xAxisRaw.split(",");
		                                 }
		                                 String whereClauseStrArry[] = null;
		                                 if(!ApplicationUtil.isEmptyOrNull(whereClauseStr)){
		                               	  whereClauseStrArry = whereClauseStr.split("or");
		                                 }
		                                 
		                                while (rs.next()) {
		                                 String mainVariableColumn = rs.getString(1);
		                                 seriesColumnNameBook= (Map)codeBookMapObj.get(mainVariableColumn.trim().toLowerCase());
		                                 if(ApplicationUtil.isEmptyOrNull(temp) || !temp.equalsIgnoreCase(mainVariableColumn)){
		                                	 temp = mainVariableColumn;
		                                	 rawIndex++;
		                                 }
		                                 String factorValue = rs.getString(2);
		                                 String mainVariableLabel = (String)variableNamesObj.get(mainVariableColumn.toLowerCase());
		                                 if(ApplicationUtil.isEmptyOrNull(mainVariableLabel)){
		                                	 mainVariableLabel = mainVariableColumn;
		                                 }
		                                 dataCell=0;
		                                 Row dataRow = null;
		                                 if(frequency){
		                                	 dataRow = sheet.createRow(rowIndex);
		                                	 rowIndex++;
		                                 }
		                                 if(!frequency){
		                                	 if(factorValue.indexOf("Mean")==0 || factorValue.indexOf("Median")==0 || factorValue.indexOf("Standard")==0){
		                                		 dataRow = sheet.createRow(rowIndex);
			                                	 rowIndex++;
		                                	 }
		                                 }
		                                 Row percentageRow = null;
		                                 Row significanceRow = null;
		                                 if(percentage){
		                                	 if(factorValue.indexOf("Mean")==-1 && factorValue.indexOf("Median")==-1 && factorValue.indexOf("Standard")==-1){
		                                		 percentageRow = sheet.createRow(rowIndex);
		                                		 rowIndex++;
		                                	 }
		                                 }
		                                 
		                                 if(significance){
		                                	 if(factorValue.indexOf("Median")==-1 && factorValue.indexOf("Standard")==-1){
		                                		 significanceRow = sheet.createRow(rowIndex);
				                                 rowIndex++;
		                                	 }
		                                 }
		                                 for(int m=2;m<=(size+2);){
		                                         String val = rs.getString(m);
		                                         String percentageStrVal = null;
		                                         String sigStr =null;
		                                         if(m>2){
							                         if(percentage){
							                        	 percentageStrVal = rs.getString(m+1);
							                         }
							                         if(significance){
							                        	 if(percentage){
							                        		 sigStr = rs.getString(m+2); 
							                        	 }else{
							                        		 sigStr = rs.getString(m+1);
							                        	 }
							                         }
						                         }
		                                         String originalValue = val;
		                                         if(m==2){
		                                                if(seriesColumnNameBook != null){
		                                                       String recodeValue = (String)seriesColumnNameBook.get(val.trim());
		                                                       if(!ApplicationUtil.isEmptyOrNull(recodeValue)){
		                                                              val = recodeValue;
		                                                       }     
		                                                }
		                                                if(!ApplicationUtil.isEmptyOrNull(type) && type.equalsIgnoreCase("SUMMARY")){
		                                                	val=mainVariableLabel+" "+val;
		                                                }
		                                                if(!ApplicationUtil.isEmptyOrNull(val)){
		                                                	val = val.toUpperCase();
		                                                }
		                                                if(dataRow!= null){
		                                                	Cell dataListCell = dataRow.createCell((short)dataCell);
					                                        dataListCell.setCellValue(val);
		                                                }else if (percentageRow != null){
		                                                	Cell dataListCell = percentageRow.createCell((short)dataCell);
					                                        dataListCell.setCellValue(val);
		                                                }else if(significanceRow!= null){
		                                                	Cell dataListCell = significanceRow.createCell((short)dataCell);
					                                        dataListCell.setCellValue(val);
		                                                }
		                                                
		                                         }else{
		                                        	 Cell dataListCell =null;
		                                        	 if(dataRow!= null){
		                                        		 dataListCell =  dataRow.createCell((short)dataCell);
		                                        	 }else if(percentageRow != null){
		                                        		 dataListCell =  percentageRow.createCell((short)dataCell);
		                                        	 }else if(significanceRow!= null){
		                                        		 dataListCell =  significanceRow.createCell((short)dataCell);
		                                        	 }
                                        			 if (dataListCell != null){
			                                        	 if(!ApplicationUtil.isEmptyOrNull(val) && !val.equals("0") && !val.equals("0.0")){
			                                        		 dataListCell.setCellValue(Double.parseDouble(val));
			                                        		 dataListCell.setCellType(Cell.CELL_TYPE_NUMERIC);
			                                        		 dataListCell.setCellStyle(dataStyle);
			                                        	 }
			                                        	 else{
			                                        		 dataListCell.setCellValue("-");
			                                        		//dataListCell.setCellValue((int)0);
			                                        		//dataListCell.setCellType(Cell.CELL_TYPE_NUMERIC);
			                                        		//dataListCell.setCellStyle(dataStyle);
			                                        	 }
                                        			 }
		                                         }
		                                         
		                                         if(percentageRow != null){
		                                        	 Cell percentageRowCell = percentageRow.createCell((short)dataCell);
		                                        	 if(!ApplicationUtil.isEmptyOrNull(percentageStrVal) && !percentageStrVal.equals("0") && !percentageStrVal.equals("0.0")){
			                                        	 percentageRowCell.setCellValue(Double.parseDouble(percentageStrVal));
			                                        	 percentageRowCell.setCellType(Cell.CELL_TYPE_NUMERIC);
			                                        	 percentageRowCell.setCellStyle(dataStyle);
		                                        	 }
		                                        	 else{
		                                        		 if(m==2){
		                                        			 
		                                        		 }
		                                        		 else{
		                                        		 percentageRowCell.setCellValue("-");
		                                        		 }
		                                        	 }
		                                         }
		                                         if(significanceRow != null){
		                                        	 if(!ApplicationUtil.isEmptyOrNull(sigStr)){
		                                        		 Cell significanceRowCell = significanceRow.createCell((short)dataCell);
		                                        		 significanceRowCell.setCellValue(sigStr);
		                                        		 significanceRowCell.setCellStyle(dataStyle);
		                                        	 }
		                                         }
		                                         dataCell++;
		                                         
		                                         if(m==2){
			                                        	 String xAxisStr = null;
			                                             if(!ApplicationUtil.isEmptyOrNull(xAxisRaw)){
			                                              	 if(!ApplicationUtil.isEmptyOrNull(type) && (type.equalsIgnoreCase("SUMMARY") || type.equalsIgnoreCase("SUMMARY1")) && xAxisRawArray != null ){
			                                              		xAxisStr = xAxisRawArray[rawIndex];
			                                              		if(whereClauseStrArry !=null){
			                                              			whereClauseStr=whereClauseStrArry[rawIndex];
			                                              			whereClauseStr=" and "+whereClauseStr;
			                                              		}
			                                              		
			                                              	 }else{
			                                              		xAxisStr = xAxisRaw;
			                                              	 }
			                                             }else{
			                                            	 xAxisStr=mainVariableColumn;
			                                             }
		                                                String str1="";
			                                                if(originalValue.equals("Mean INCL.("+inclExcl+")"))
			       										 {
			       											str1="select round((sum("+xAxisStr+") /count("+xAxisStr+")),1),null  from data where 1=1 "+whereClauseStr;
			       										 }
			       										 else if(originalValue.equals("Mean EXCL.("+inclExcl+")")){
			       											str1 ="select round(sum("+xAxisStr+") /count("+xAxisStr+"),1),null from data  where "+xAxisStr+" not in ('"+inclExcl+"') "+whereClauseStr ;
			       										 }
			       										 else if(originalValue.equals("Standard Deviation")){
			       											 str1="select round(stddev("+xAxisStr+"),1),null from data where 1 = 1 "+whereClauseStr;
			       										 }
			       										 else if(originalValue.equals("Standard Error")){
			       											 str1="select  round((stddev("+xAxisStr+")/SQRT(count("+xAxisStr+"))),1),null from data  where 1=1 "+whereClauseStr;
			       										 }
			       										 else if(originalValue.equals("Median INCL.("+inclExcl+")")){
			       											str1="select round(median("+xAxisStr+"),1),null from data  where 1=1 "+whereClauseStr;
			       										 }
			       										 else if(originalValue.equals("Median EXCL.("+inclExcl+")")){
			       											str1="select round(median("+xAxisStr+"),1),null  from data  where 1=1 and "+xAxisStr+" not in ('"+inclExcl+"') " +whereClauseStr;
			       										 }
			       										 else{
			       											 //str1 = "select  round(sum("+weight+")) ,round((sum(weight)/(select sum(weight) from data where 1=1  "+whereClauseStr+")*100),1) totalPerc from data where "+mainVariableColumn+"='"+originalValue+"' "+whereClauseStr;
			       											 str1 = "select count("+xAxis+"),round((sum("+xAxisStr+")/(select sum("+xAxisStr+") from data where 1=1  "+whereClauseStr+")*100),1) totalPerc from data where "+mainVariableColumn+"='"+originalValue+"' "+whereClauseStr;
			       										 }
		                                                
		                                                System.out.println("--------------"+str1);
		                                                ResultSet  rs2 = session.connection().createStatement().executeQuery(str1);
		                                                if(rs2.next()){
		                                                       rs2.previous();
		                                                       while (rs2.next()) {
		                                                              String val1 = rs2.getString(1);
		                                                              String totalPercentage = rs2.getString(2);
		                                                              if(!frequency && !ApplicationUtil.isEmptyOrNull(totalPercentage)){
		                                                            	  val1 = totalPercentage;
		                                                              }
		                                                              Cell dataListCell = null;
		                                                              if(dataRow != null){
		                                                            	  dataListCell = dataRow.createCell((short)dataCell);
		                                                              }else if (percentageRow!= null){
		                                                            	  dataListCell = percentageRow.createCell((short)dataCell);
		                                                              }else if (significanceRow!= null){
		                                                            	  dataListCell = significanceRow.createCell((short)dataCell);
		                                                              }
		                                                              
		                                                              if(frequency && percentage){
		                                                            	  if(percentageRow != null){
			                                                            	  Cell totalPercentageCell = percentageRow.createCell((short)dataCell);
			                                                            	  if(totalPercentageCell != null){
			                                                            		  totalPercentageCell.setCellStyle(dataStyle);
								 		                                        	 if(!ApplicationUtil.isEmptyOrNull(totalPercentage)){
								 		                                        		totalPercentageCell.setCellValue(Double.parseDouble(totalPercentage));
								 		                                        		totalPercentageCell.setCellType(Cell.CELL_TYPE_NUMERIC);
								 		                                        	 }
								 		                                        	 else{
								 		                                        		totalPercentageCell.setCellValue("-");
								 		                                        		//totalPercentageCell.setCellValue(0);
								 		                                        		//totalPercentageCell.setCellType(Cell.CELL_TYPE_NUMERIC);
								 		                                        	 }
					                                                            }
		                                                            	  }
		                                                               }
		                                                            if(dataListCell != null){
		                                                            	 dataListCell.setCellStyle(dataStyle);
					 		                                        	 if(!ApplicationUtil.isEmptyOrNull(val1) && !val1.equals("0") && !val1.equals("0.0")){
					 		                                        		 dataListCell.setCellValue(Double.parseDouble(val1));
					 		                                        		 dataListCell.setCellType(Cell.CELL_TYPE_NUMERIC);
					 		                                        	 }
					 		                                        	 else{
					 		                                        		dataListCell.setCellValue("-");
					 		                                        		//dataListCell.setCellValue(0);
					 		                                        		//dataListCell.setCellType(Cell.CELL_TYPE_NUMERIC);
					 		                                        	 }
				 		                                        	  dataCell++;
		                                                            }
		                                                       } 
		                                                }else{
		                                                	if(dataRow!= null){
		                                                		Cell dataListCell = dataRow.createCell((short)dataCell);
			                                                    dataCell++;
		                                                	}else if (percentageRow!= null){
		                                                		Cell dataListCell = percentageRow.createCell((short)dataCell);
			                                                    dataCell++;
		                                                	}else if (significanceRow!= null){
		                                                		Cell dataListCell = significanceRow.createCell((short)dataCell);
			                                                    dataCell++;
		                                                	}
		                                                	
		                                                 }
		                                         }
		                                         
		                                         if(percentage && !significance &&  m>2){
						                        	 m=m+1+1;
						                         }
						                         if(significance && m>2){
						                        	 if(percentage){
						                        		 m=m+2+1;
						                        	 }else{
						                        		 m=m+1+1;
						                        	 }
						                         }
						                         if((!percentage && !significance) || m==2){
						                        	 m++;
						                         }
		                                 			}
		                                             }
	                                 }  
							        else{
							        	int variableHeaderRowIndex = rowIndex;
	                                	int endMergeIndex=0;
		                                int startMergeIndex=0;
							        	Row variableRow = sheet.createRow(rowIndex);
		                                   rowIndex++;
		                                   Row factorHeaderRow = sheet.createRow(rowIndex);
		                                   rowIndex++;
		                                   Row charRow = sheet.createRow(rowIndex);
		                                   rowIndex++;
		                                   int mainVariable=0;
		                                   int total=1;
		                                   int dataCell=0;
		                                   int variableHeaderCell=2;
		                                   int factorHeaderCell=2;
		                                   int charHeaderCell=2;
		                                   String tempName="";
		                                   sb.append(totalFrequencyOrderExcludeStrAlias+" select mainVariable,q1 ");
		                                   char ch='B';
		                                   String tempWhereClause=whereClauseStr;
		                                   
	                                       if(!ApplicationUtil.isEmptyOrNull(type) && (type.equalsIgnoreCase("SUMMARY") || type.equalsIgnoreCase("SUMMARY1"))){
	                                    	   if(tempWhereClause.equals("")){
	                                    		   tempWhereClause="";
	                                    	   }
	                                    	   else{
	                                        	 tempWhereClause=" and ("+tempWhereClause+" )";
	                                    	   }
	                                       }
	                                       
		                                   totalUnweight.append("select count(*) from data t1 where 1=1  "+tempWhereClause);
		                                   totalWeight.append("select round(sum(t1."+weight+")) from data t1 where t1."+weight+"!=\"\"  "+tempWhereClause);
		                                 for(int k=0;k<size;k++){
		                                 Object[] row = (Object[])bannerTableList.get(k);
		                                
		                                 String name = row[1].toString();
		                                 String value = row[0].toString();
		                                 String variableName = (String)variableNamesObj.get(name.trim().toLowerCase());
		                                 if(ApplicationUtil.isEmptyOrNull(variableName)){
		                                	 variableName = "";
		                                 }
		                                 String x_axis="";
		                                 if(xAxis!= null && xAxis.contains(",")){
		                                	 
		                                 }else{
		                                	 x_axis = row[2].toString();
		                                 }
		                                 String xaxis = (String)variableNamesObj.get(x_axis.toLowerCase());
		                                 if(ApplicationUtil.isEmptyOrNull(xaxis)){
		                                	 xaxis = x_axis;
		                                 }
		                                 
		                                 if(ApplicationUtil.isEmptyOrNull(name) || !tempName.equalsIgnoreCase(name)){
		                                         tempName=name;
		                                         totalUnweight.append(" union all ");
		                                         totalUnweight.append(" select count("+name+") from data t1 where t1."+name+"!=\"\" "+tempWhereClause+"  group by t1."+name);
		                                         totalWeight.append(" union all ");
		                                         totalWeight.append(" select round(sum(t1."+weight+")) from data t1 where t1."+name+"!=\"\"  and  t1."+weight+"!=\"\" "+tempWhereClause+" group by t1."+name);
		                                         if(startMergeIndex>0 && endMergeIndex>0){
		                                        	 /*CellRangeAddress region = new CellRangeAddress(variableHeaderRowIndex, variableHeaderRowIndex, startMergeIndex, endMergeIndex);
		                                     		 sheet.addMergedRegion(region);
		                                     		 RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, region, sheet, sheet.getWorkbook());
		                                     		 RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, region, sheet, sheet.getWorkbook());
		                                     		 RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, region, sheet, sheet.getWorkbook());
		                                     		 RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, region, sheet, sheet.getWorkbook());*/
		                                         }
		                                        // Cell mainVariableCell = variableRow.createCell((short)mainVariable);
		                                         //mainVariableCell.setCellValue(xaxis);
		                                         Cell totalCell = variableRow.createCell((short)total);
		                                         totalCell.setCellValue("TOTAL");
		                                         Cell cell = variableRow.createCell((short)variableHeaderCell);
		                                         startMergeIndex = variableHeaderCell;
		                                         endMergeIndex = variableHeaderCell;
		                                         cell.setCellValue(variableName.toUpperCase());
		                          		         cell.setCellStyle(varialbeStyle);
		                                 }else{
		                                	 endMergeIndex++;
		                                 }
		                                 Cell factorCell = factorHeaderRow.createCell((short)factorHeaderCell);
		                                 Map yAxisVariableCodeBookObj= (Map)codeBookMapObj.get(name.trim().toLowerCase());
		                                 String recodedValue=null;
		                                 if(yAxisVariableCodeBookObj!= null){
		                                	 recodedValue = (String)yAxisVariableCodeBookObj.get(value);
		                                 }
		                                 if(ApplicationUtil.isEmptyOrNull(recodedValue)){
		                                	 recodedValue = value;
		                                 }
		                                 factorCell.setCellValue(recodedValue.toUpperCase());
		                                 factorCell.setCellStyle(dataStyle);
		                                 Cell charCellVal = charRow.createCell((short)1);
		                                 charCellVal.setCellValue("(A)");
		                                 charCellVal.setCellStyle(dataStyle);
		                                 Cell charCell = charRow.createCell((short)charHeaderCell);
		                                 charCell.setCellValue("("+ch+")");
		                                 charCell.setCellStyle(dataStyle);
		                                 ch++;
		                                 charHeaderCell++;
		                                 factorHeaderCell++;
		                                 variableHeaderCell++;
		                                 String percentageSelectClause="";
		                                
						                 if(percentage){
						                	 percentageSelectClause =",round((sum(case when q2='"+value+"' and quesname='"+name+"' then weight  end)/" +
		                                               "(select sum("+weight+") from data where "+name+"='"+value+"' "+tempWhereClause+"))*100) 'percentage"+k+"'";
						                 }
						                 String significanceSelectClause ="";
						                 if(significance){
						                	 significanceSelectClause =",max(case when q2='"+value+"' and quesname='"+name+"' then significance else ''  end) as 'sign"+k+"'";
						                 }
						                 sb.append(",(case when q1 like '%Mean%' or q1 like '%Median%' or q1 like '%Standard%' then round(sum(case when q2 = '"+value+"' and quesname = '"+name+"' then weight  end), 1) else round(sum(case when q2 = '"+value+"' and quesname = '"+name+"' then weight  end)) end) 'count"+k+"' "+percentageSelectClause+" "+significanceSelectClause);
		                                 }
		                                 
		                                 if(startMergeIndex>0 && endMergeIndex>0){
                                        	 /*CellRangeAddress region = new CellRangeAddress(variableHeaderRowIndex, variableHeaderRowIndex, startMergeIndex, endMergeIndex);
                                     		 sheet.addMergedRegion(region);
                                     		 RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, region, sheet, sheet.getWorkbook());
                                    		 RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, region, sheet, sheet.getWorkbook());
                                    		 RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, region, sheet, sheet.getWorkbook());
                                    		 RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, region, sheet, sheet.getWorkbook());*/
                                         }
		                                 if(percentage && !significance){
	  	                                	 size = size*2;
	  	                                 	}
	      	                                 if(significance){
	      	                                	 if(percentage){
	      	                                		size = size*3;
	      	                                	 }else{
	      	                                		size = size*2;
	      	                                	 }
	      	                                 }
	      	                                 
	      	                                 if(!ApplicationUtil.isEmptyOrNull(totalFrequencyOrderLevelExclude) && !ApplicationUtil.isEmptyOrNull(totalFrequencyOrderVariableExclude) ){
	      	                                	 stringBuff.append(sb.toString());
	      	                                 }
		                                	 if(ApplicationUtil.isEmptyOrNull(type) || (!type.equalsIgnoreCase("SUMMARY") && !type.equalsIgnoreCase("SUMMARY1"))){
				                                	sb.append(" from bannertable where  "+totalFrequencyOrderExcludeStr+"  templateChartInfoId is NULL and metaDataId = '"+metaDataId+"'  group by "+mainVariableStr+" q1 order by "+mainvariableOrderStr+", "+totalFrequencyOrderStr+" "+levelOrderStr+" "+totalFrequencyOrderExcludeAlias);
				                                	if(!ApplicationUtil.isEmptyOrNull(totalFrequencyOrderLevelExclude) && !ApplicationUtil.isEmptyOrNull(totalFrequencyOrderVariableExclude)){
				                                		stringBuff.append(" from bannertable  where  templateChartInfoId is NULL and metaDataId = '"+metaDataId+"' and id in (select id from bannertable where mainVariable in ('"+totalFrequencyOrderVariableExclude+"') and q1 in ("+totalFrequencyOrderLevelExclude+")  and metaDataId = '"+metaDataId+"') group by "+mainVariableStr+" q1 order by "+mainvariableOrderStr+" "+levelOrderStr+" "+totalFrequencyOrderExcludeAlias);
				                                		sb.append(" union all ");
					                                	sb.append(stringBuff.toString());
				                                	}
				                                }else if(type.equalsIgnoreCase("SUMMARY")){
				                                	sb.append(" from bannertable  where  templateChartInfoId is NULL and mainVariable in ("+xAxisColumnStr+") and q1 like 'Mean%' and metaDataId < "+metaDataId+"  and flag="+flagValue+" group by "+mainVariableStr+" q1 order by id");
				                                }else{
				                                	sb.append(" from bannertable  where  templateChartInfoId is NULL and mainVariable in ("+xAxisColumnStr+") and q1 = '1' and metaDataId < "+metaDataId+"  and flag="+flagValue+" group by "+mainVariableStr+" q1 order by id");
				                                }
				                                
		                                System.out.println("percentage with weight:"+sb);
		                              
		                                   System.out.println("Total actual with out weight:"+totalUnweight);
		                                    int actual=0;
		                                    Row totalRow = sheet.createRow(rowIndex);
		                                    rowIndex++;
		                                    Cell totalactualCell = totalRow.createCell((short)actual);
		                                    totalactualCell.setCellValue("TOTAL (ACTUAL)");
		                                 ResultSet  rs1 = session.connection().createStatement().executeQuery(totalUnweight.toString());
		                                 while (rs1.next()) {
		                                        String val = rs1.getString(1);
		                                         Cell factorCell = totalRow.createCell((short)total);
		                                         factorCell.setCellValue(Double.parseDouble(val));
		                                         factorCell.setCellType(Cell.CELL_TYPE_NUMERIC);
		                                         factorCell.setCellStyle(dataStyle);
		                                         
		                                  total++;
		                                 }             
		                                 int totalWeightVal=1;
                                         System.out.println("Total Weight:"+totalWeight);
                                          int actual_weight=0;
                                          Row totalWeightRow = sheet.createRow(rowIndex);
                                          rowIndex++;
                                          Cell totalweightCell = totalWeightRow.createCell((short)actual_weight);
                                          totalweightCell.setCellValue("TOTAL (WEIGHTED)");
                                          rowIndex++;
                                       ResultSet  result = session.connection().createStatement().executeQuery(totalWeight.toString());
                                       while (result.next()) {
                                              String val = result.getString(1);
                                               Cell totalWeightCell = totalWeightRow.createCell((short)totalWeightVal);
                                               totalWeightCell.setCellValue(Double.parseDouble(val));
                                               totalWeightCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                               totalWeightCell.setCellStyle(dataStyle);
                                               totalWeightVal++;
                                       } 
                                       logger.debug(sb.toString());
		                                 ResultSet  rs = session.connection().createStatement().executeQuery(sb.toString());
		                                 String temp="";
		                                 int rawIndex=-1;
		                                 String xAxisRawArray[] = null;
		                                 if(!ApplicationUtil.isEmptyOrNull(xAxisRaw)){
		                                	 xAxisRawArray = xAxisRaw.split(",");
		                                 }
		                                 String whereClauseStrArry[] = null;
		                                 if(!ApplicationUtil.isEmptyOrNull(whereClauseStr)){
		                               	  whereClauseStrArry = whereClauseStr.split("or");
		                                 }
		                                 
		                                while (rs.next()) {
		                                 String mainVariableColumn = rs.getString(1);
		                                 if(ApplicationUtil.isEmptyOrNull(displayLevel)){
		                                	 seriesColumnNameBook= (Map)codeBookMapObj.get(mainVariableColumn.trim().toLowerCase());
		                                 }
		                                 else{
		                                 seriesColumnNameBook= (Map)codeBookMapObj.get(mainVariableColumn.trim().toLowerCase()+"display");
		                                 }
		                                 if(ApplicationUtil.isEmptyOrNull(temp) || !temp.equalsIgnoreCase(mainVariableColumn)){
		                                	 temp = mainVariableColumn;
		                                	 rawIndex++;
		                                 }
		                                 String factorValue = rs.getString(2);
		                                 String mainVariableLabel = (String)variableNamesObj.get(mainVariableColumn.toLowerCase());
		                                 if(ApplicationUtil.isEmptyOrNull(mainVariableLabel)){
		                                	 mainVariableLabel = mainVariableColumn;
		                                 }
		                                 dataCell=0;
		                                 Row dataRow = null;
		                                 if(frequency){
		                                	 dataRow = sheet.createRow(rowIndex);
		                                	 rowIndex++;
		                                 }
		                                 if(!frequency){
		                                	 if(factorValue.indexOf("Mean")==0 || factorValue.indexOf("Median")==0 || factorValue.indexOf("Standard")==0){
		                                		 dataRow = sheet.createRow(rowIndex);
			                                	 rowIndex++;
		                                	 }
		                                 }
		                                 Row percentageRow = null;
		                                 Row significanceRow = null;
		                                 if(percentage){
		                                	 if(factorValue.indexOf("Mean")==-1 && factorValue.indexOf("Median")==-1 && factorValue.indexOf("Standard")==-1){
		                                		 percentageRow = sheet.createRow(rowIndex);
		                                		 rowIndex++;
		                                	 }
		                                 }
		                                 
		                                 if(significance){
		                                	 if(factorValue.indexOf("Median")==-1 && factorValue.indexOf("Standard")==-1){
		                                		 significanceRow = sheet.createRow(rowIndex);
				                                 rowIndex++;
		                                	 }
		                                 }
		                                 for(int m=2;m<=(size+2);){
		                                         String val = rs.getString(m);
		                                         String percentageStrVal = null;
		                                         String sigStr =null;
		                                         if(m>2){
		                                        	 if(percentage){
							                        	 percentageStrVal = rs.getString(m+1);
							                         }
							                         if(significance){
							                        	 if(percentage){
							                        		 sigStr = rs.getString(m+2); 
							                        	 }else{
							                        		 sigStr = rs.getString(m+1);
							                        	 }
							                         }
						                         }
		                                         String originalValue = val;
		                                         if(m==2){
		                                                if(seriesColumnNameBook != null){
		                                                       String recodeValue = (String)seriesColumnNameBook.get(val.trim());
		                                                       if(!ApplicationUtil.isEmptyOrNull(recodeValue)){
		                                                              val = recodeValue;
		                                                       }     
		                                                }
		                                                if(!ApplicationUtil.isEmptyOrNull(type) && type.equalsIgnoreCase("SUMMARY")){
		                                                	val=mainVariableLabel+" "+val;
		                                                }
		                                                if(!ApplicationUtil.isEmptyOrNull(val)){
		                                                	val = val.toUpperCase();
		                                                }
		                                                if(dataRow!= null){
		                                                	Cell dataListCell = dataRow.createCell((short)dataCell);
					                                        dataListCell.setCellValue(val);
		                                                }else if (percentageRow != null){
		                                                	Cell dataListCell = percentageRow.createCell((short)dataCell);
					                                        dataListCell.setCellValue(val);
		                                                }else if(significanceRow!= null){
		                                                	Cell dataListCell = significanceRow.createCell((short)dataCell);
					                                        dataListCell.setCellValue(val);
		                                                }
		                                                
		                                         }else{
		                                        	 Cell dataListCell =null;
		                                        	 if(dataRow!= null){
		                                        		 dataListCell =  dataRow.createCell((short)dataCell);
		                                        	 }else if(percentageRow != null){
		                                        		 dataListCell =  percentageRow.createCell((short)dataCell);
		                                        	 }else if(significanceRow!= null){
		                                        		 dataListCell =  significanceRow.createCell((short)dataCell);
		                                        	 }
                                        			 if (dataListCell != null){
			                                        	 if(!ApplicationUtil.isEmptyOrNull(val) && !val.equals("0") && !val.equals("0.0")){
			                                        		 dataListCell.setCellValue(Double.parseDouble(val));
			                                        		 dataListCell.setCellType(Cell.CELL_TYPE_NUMERIC);
			                                        		 dataListCell.setCellStyle(dataStyle);
			                                        	 }
			                                        	 else{
			                                        		 dataListCell.setCellValue("-");
			                                        		//dataListCell.setCellValue((int)0);
			                                        		//dataListCell.setCellType(Cell.CELL_TYPE_NUMERIC);
			                                        		//dataListCell.setCellStyle(dataStyle);
			                                        	 }
                                        			 }
		                                         }
		                                         
		                                         if(percentageRow != null){
		                                        	 Cell percentageRowCell = null;
		                                        	 if(!ApplicationUtil.isEmptyOrNull(percentageStrVal) && !percentageStrVal.equals("0") && !percentageStrVal.equals("0.0")){
		                                        		 percentageRowCell = percentageRow.createCell((short)dataCell);
			                                        	 percentageRowCell.setCellValue(Double.parseDouble(percentageStrVal));
			                                        	 percentageRowCell.setCellType(Cell.CELL_TYPE_NUMERIC);
			                                        	 percentageRowCell.setCellStyle(dataStyle);
		                                        	 }
		                                        	 else{
		                                        		 if(m==2){
		                                        			 
		                                        		 }
		                                        		 else{
		                                        			 if(percentageRowCell==null){
		                                        				 percentageRowCell = percentageRow.createCell((short)dataCell);
		                                        			 }
		                                        			percentageRowCell.setCellValue("-");
		                                        		 }
		                                        	 }
		                                         }
		                                         
		                                         if(significanceRow != null){
		                                        	 if(!ApplicationUtil.isEmptyOrNull(sigStr)){
		                                        		 Cell significanceRowCell = significanceRow.createCell((short)dataCell);
		                                        		 significanceRowCell.setCellValue(sigStr);
		                                        		 significanceRowCell.setCellStyle(dataStyle);
		                                        	 }
		                                         }
		                                         dataCell++;
		                                         
		                                         if(m==2){
			                                        	 String xAxisStr = null;
			                                             if(!ApplicationUtil.isEmptyOrNull(xAxisRaw)){
			                                              	 if(!ApplicationUtil.isEmptyOrNull(type) && (type.equalsIgnoreCase("SUMMARY") || type.equalsIgnoreCase("SUMMARY1")) && xAxisRawArray != null ){
			                                              		xAxisStr = xAxisRawArray[rawIndex];
			                                              		if(whereClauseStrArry !=null){
			                                              			whereClauseStr=whereClauseStrArry[rawIndex];
			                                              			whereClauseStr=" and "+whereClauseStr;
			                                              		}
			                                              		
			                                              	 }else{
			                                              		xAxisStr = xAxisRaw;
			                                              	 }
			                                             }else{
			                                            	 xAxisStr=mainVariableColumn;
			                                             }
		                                                String str1="";
			                                                if(originalValue.equals("Mean INCL.("+inclExcl+")"))
			       										 {
			       											str1="select round((sum("+xAxisStr+" * "+weight+") /sum("+weight+")),1),null  from data where 1=1 "+whereClauseStr;
			       										 }
			       										 else if(originalValue.equals("Mean EXCL.("+inclExcl+")")){
			       											str1 ="select round((sum("+xAxisStr+" * "+weight+") /sum("+weight+")),1),null from data  where "+xAxisStr+" not in ('"+inclExcl+"') "+whereClauseStr ;
			       										 }
			       										 else if(originalValue.equals("Standard Deviation")){
			       											 str1="select round(SQRT((sum("+weight+" * pow("+xAxisStr+" - ((select sum("+weight+" * "+xAxisStr+") from data) / (select sum("+weight+") from data)),2))) / ((select sum("+weight+") from data) *(count("+weight+") - 1)/count("+weight+"))),1),null from data where 1 = 1 "+whereClauseStr;
			       										 }
			       										 else if(originalValue.equals("Standard Error")){
			       											 str1="select  round(SQRT((sum("+weight+" * pow("+xAxisStr+" - ((select sum("+weight+" * "+xAxisStr+") from data) / (select sum("+weight+") from data)),2))) / ((select sum("+weight+") from data) *(count("+weight+") - 1)/count("+weight+")))/SQRT(count("+xAxisStr+")),1),null from data  where 1=1 "+whereClauseStr;
			       										 }
			       										 else if(originalValue.equals("Median INCL.("+inclExcl+")")){
			       											//str="select round(median("+xAxisStr+" * "+weight+"),1),null  from data  where 1=1 "+whereClauseStr;
			       											StringBuffer sb1= new StringBuffer();
			       											sb1.append("select v,null,min(cum) from ( ");
			       											sb1.append("select *,(@curRank:=@curRank + sw) cum from (select  ");
			       											sb1.append("    "+xAxisStr+" as v, sum("+weight+"),sum("+weight+")/(select sum("+weight+") from data where 1=1 "+whereClauseStr+" ) sw ");
			       											sb1.append("from ");
			       											sb1.append("    data a,(select @curRank:=0) b ");
			       											sb1.append("where ");
			       											sb1.append("  1=1 "+whereClauseStr+" ");
			       											sb1.append("group by v ");
			       											sb1.append("order by v asc) a) b where cum>=0.5 ");
			       											str1 = sb1.toString();
			       										 }
			       										 else if(originalValue.equals("Median EXCL.("+inclExcl+")")){
			       											//str="select round(median("+xAxisStr+" * "+weight+"),1),null  from data  where 1=1 and "+xAxisStr+" not in ('"+inclExcl+"') " +whereClauseStr;
			       											StringBuffer sb1= new StringBuffer();
			       											sb1.append("select v,null,min(cum) from ( ");
			       											sb1.append("select *,(@curRank:=@curRank + sw) cum from (select  ");
			       											sb1.append("    "+xAxisStr+" as v, sum("+weight+"),sum("+weight+")/(select sum("+weight+") from data where 1=1 "+whereClauseStr+" and "+xAxisStr+">0) sw ");
			       											sb1.append("from ");
			       											sb1.append("    data a,(select @curRank:=0) b ");
			       											sb1.append("where ");
			       											sb1.append("  "+xAxisStr+">0 "+whereClauseStr+" ");
			       											sb1.append("group by v ");
			       											sb1.append("order by v asc) a) b where cum>=0.5 ");
			       											str1 = sb1.toString();
			       										 }
			       										 else{
			       											 if(!ApplicationUtil.isEmptyOrNull(mainVariableColumn) && mainVariableColumn.startsWith("XX_NET_TB")){
			       												weight = (String)mapXAxisNetWeightObj.get(mainVariableColumn.trim());
			       											 }else{
			       												weight = weightStr;
			       											 }
			       											 str1 = "select  round(sum("+weight+")) ,round((sum("+weight+"))/(select sum(weight) from data where 1=1  "+whereClauseStr+")*100) totalPerc from data where "+mainVariableColumn+"='"+originalValue+"' "+whereClauseStr;
			       											 weight = weightStr;
			       										 }
		                                                
		                                                System.out.println("--------------"+str1);
		                                                ResultSet  rs2 = session.connection().createStatement().executeQuery(str1);
		                                                if(rs2.next()){
		                                                       rs2.previous();
		                                                       while (rs2.next()) {
		                                                              String val1 = rs2.getString(1);
		                                                              String totalPercentage = rs2.getString(2);
		                                                              if(!frequency && !ApplicationUtil.isEmptyOrNull(totalPercentage)){
		                                                            	  val1 = totalPercentage;
		                                                              }
		                                                              Cell dataListCell = null;
		                                                              if(dataRow != null){
		                                                            	  dataListCell = dataRow.createCell((short)dataCell);
		                                                              }else if (percentageRow!= null){
		                                                            	  dataListCell = percentageRow.createCell((short)dataCell);
		                                                              }else if (significanceRow!= null){
		                                                            	  dataListCell = significanceRow.createCell((short)dataCell);
		                                                              }
		                                                              
		                                                              if(frequency && percentage){
		                                                            	  if(percentageRow != null){
			                                                            	  Cell totalPercentageCell = percentageRow.createCell((short)dataCell);
			                                                            	  if(totalPercentageCell != null){
			                                                            		  totalPercentageCell.setCellStyle(dataStyle);
								 		                                        	 if(!ApplicationUtil.isEmptyOrNull(totalPercentage) && !totalPercentage.equals("0") ){
								 		                                        		totalPercentageCell.setCellValue(Double.parseDouble(totalPercentage));
								 		                                        		totalPercentageCell.setCellType(Cell.CELL_TYPE_NUMERIC);
								 		                                        	 }
								 		                                        	 else{
								 		                                        		totalPercentageCell.setCellValue("-");
								 		                                        		//totalPercentageCell.setCellValue(0);
								 		                                        		//totalPercentageCell.setCellType(Cell.CELL_TYPE_NUMERIC);
								 		                                        	 }
					                                                            }
		                                                            	  }
		                                                               }
		                                                            if(dataListCell != null){
		                                                            	 dataListCell.setCellStyle(dataStyle);
					 		                                        	 if(!ApplicationUtil.isEmptyOrNull(val1) && !val1.equals("0") && !val1.equals("0.0")){
					 		                                        		 dataListCell.setCellValue(Double.parseDouble(val1));
					 		                                        		 dataListCell.setCellType(Cell.CELL_TYPE_NUMERIC);
					 		                                        	 }
					 		                                        	 else{
					 		                                        		dataListCell.setCellValue("-");
					 		                                        		//dataListCell.setCellValue(0);
					 		                                        		//dataListCell.setCellType(Cell.CELL_TYPE_NUMERIC);
					 		                                        	 }
				 		                                        	  dataCell++;
		                                                            }
		                                                       } 
		                                                }else{
		                                                	if(dataRow!= null){
		                                                		Cell dataListCell = dataRow.createCell((short)dataCell);
			                                                    dataCell++;
		                                                	}else if (percentageRow!= null){
		                                                		Cell dataListCell = percentageRow.createCell((short)dataCell);
			                                                    dataCell++;
		                                                	}else if (significanceRow!= null){
		                                                		Cell dataListCell = significanceRow.createCell((short)dataCell);
			                                                    dataCell++;
		                                                	}
		                                                	
		                                                 }
		                                         }
		                                         
		                                         if(percentage && !significance &&  m>2){
						                        	 m=m+1+1;
						                         }
						                         if(significance && m>2){
						                        	 if(percentage){
						                        		 m=m+2+1;
						                        	 }else{
						                        		 m=m+1+1;
						                        	 }
						                         }
						                         if((!percentage && !significance) || m==2){
						                        	 m++;
						                         }
		                                 			}
		                                             }
					        		}
	                            }
	                     }      
	                        catch (HibernateException he) {
	                        	he.printStackTrace();
	                          throw he;
	                        }
	                    System.out.println("----------Your excel file has been generated!-----------");
	                    return rowIndex;
	                }
	        });
	} catch (Exception e) {
		e.printStackTrace();
	             logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
	        // TODO: handle exception
	        ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "getBannerTableList", 
	                        ApplicationErrorCodes.APP_EC_37, ApplicationConstants.EXCEPTION, e);
	             throw e;
	}
      logger.debug("Leaving from getBannerTableList");
      return (int)object;  
}

public List downloadBannertable2(final String fileName) throws ApplicationException
{
        Object object = null;
        logger.debug("Entered into downloadBannertable2");
        try {
                object = hibernateTemplate.execute(new HibernateCallback() {
                        public Object doInHibernate(Session session)
                                        throws HibernateException, SQLException {
                        	List BannerMetaDataList=null;
                                try {
                                	
                                 String sql = "from BannerMetaData2";
                                      Query q = session.createQuery(sql);
                                         BannerMetaDataList =  (List)q.list();
                                         
                                         XSSFWorkbook workbook= new XSSFWorkbook();
                                         XSSFSheet sheet =  workbook.createSheet("new sheet");
                                        if(BannerMetaDataList != null){
                                        	int sheetRowIndex=0;
                                        	Row headerRow = sheet.createRow(sheetRowIndex);
                                        	sheetRowIndex++;
                                        	Cell titleCell = headerRow.createCell((short)0);
                                        	titleCell.setCellValue("Title");
                                        	titleCell = headerRow.createCell((short)1);
                                        	titleCell.setCellValue("Canadian Tire");
                                        	titleCell = headerRow.createCell((short)2);
                                        	titleCell.setCellValue("Walmart");
                                        	titleCell = headerRow.createCell((short)3);
                                        	titleCell.setCellValue("Home Depot");
                                        	titleCell = headerRow.createCell((short)4);
                                        	titleCell.setCellValue("Target");
                                        	titleCell = headerRow.createCell((short)5);
                                        	titleCell.setCellValue("COSTCO");
                                        	titleCell = headerRow.createCell((short)6);
                                        	titleCell.setCellValue("Dollar Store");
                                        	
                                        	sheetRowIndex++;
                                        	
                                        	//total
                                        	String totalActual=null;
                                        	ResultSet  rs2 = session.connection().createStatement().executeQuery("select count(*) from data");
                                    		if(rs2.next()){
                                    			totalActual = rs2.getString(1);
                                    		}
                                    		String totalWeighted=null;
                                    		ResultSet  rs3 = session.connection().createStatement().executeQuery("select round(sum(weight)) from data");
                                    		if(rs3.next()){
                                    			totalWeighted = rs3.getString(1);
                                    		}
                                    		
                                    		Row totalActualheaderRow = sheet.createRow(sheetRowIndex);
                                        	sheetRowIndex++;
                                        	Cell totalActualheaderRowCell = totalActualheaderRow.createCell((short)0);
                                        	totalActualheaderRowCell.setCellValue("TOTAL (ACTUAL)");
                                        	totalActualheaderRowCell = totalActualheaderRow.createCell((short)1);
                                        	totalActualheaderRowCell.setCellValue(Double.parseDouble(totalActual));
                                        	totalActualheaderRowCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                        	totalActualheaderRowCell = totalActualheaderRow.createCell((short)2);
                                        	totalActualheaderRowCell.setCellValue(Double.parseDouble(totalActual));
                                        	totalActualheaderRowCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                        	totalActualheaderRowCell = totalActualheaderRow.createCell((short)3);
                                        	totalActualheaderRowCell.setCellValue(Double.parseDouble(totalActual));
                                        	totalActualheaderRowCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                        	totalActualheaderRowCell = totalActualheaderRow.createCell((short)4);
                                        	totalActualheaderRowCell.setCellValue(Double.parseDouble(totalActual));
                                        	totalActualheaderRowCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                        	totalActualheaderRowCell = totalActualheaderRow.createCell((short)5);
                                        	totalActualheaderRowCell.setCellValue(Double.parseDouble(totalActual));
                                        	totalActualheaderRowCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                        	totalActualheaderRowCell = totalActualheaderRow.createCell((short)6);
                                        	totalActualheaderRowCell.setCellValue(Double.parseDouble(totalActual));
                                        	totalActualheaderRowCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                        	
                                        	Row totalWeigtedheaderRow = sheet.createRow(sheetRowIndex);
                                        	sheetRowIndex++;
                                        	Cell totalWeigtedheaderRowCell = totalWeigtedheaderRow.createCell((short)0);
                                        	totalWeigtedheaderRowCell.setCellValue("TOTAL (WEIGHTED)");
                                        	totalWeigtedheaderRowCell = totalWeigtedheaderRow.createCell((short)1);
                                        	totalWeigtedheaderRowCell.setCellValue(Double.parseDouble(totalWeighted));
                                        	totalWeigtedheaderRowCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                        	totalWeigtedheaderRowCell = totalWeigtedheaderRow.createCell((short)2);
                                        	totalWeigtedheaderRowCell.setCellValue(Double.parseDouble(totalWeighted));
                                        	totalWeigtedheaderRowCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                        	totalWeigtedheaderRowCell = totalWeigtedheaderRow.createCell((short)3);
                                        	totalWeigtedheaderRowCell.setCellValue(Double.parseDouble(totalWeighted));
                                        	totalWeigtedheaderRowCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                        	totalWeigtedheaderRowCell = totalWeigtedheaderRow.createCell((short)4);
                                        	totalWeigtedheaderRowCell.setCellValue(Double.parseDouble(totalWeighted));
                                        	totalWeigtedheaderRowCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                        	totalWeigtedheaderRowCell = totalWeigtedheaderRow.createCell((short)5);
                                        	totalWeigtedheaderRowCell.setCellValue(Double.parseDouble(totalWeighted));
                                        	totalWeigtedheaderRowCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                        	totalWeigtedheaderRowCell = totalWeigtedheaderRow.createCell((short)6);
                                        	totalWeigtedheaderRowCell.setCellValue(Double.parseDouble(totalWeighted));
                                        	totalWeigtedheaderRowCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                        	sheetRowIndex++;
                                        	int size =BannerMetaDataList.size();
                                        	for(int i=0;i<size;i++){
                                        		BannerMetaData2 bannerMetaData2 =  (BannerMetaData2)BannerMetaDataList.get(i);
                                        		Integer bannerMetaDataId = bannerMetaData2.getId();
                                        		String ctr = bannerMetaData2.getCanadiantire();
                                        		String wm = bannerMetaData2.getWalmart();
                                        		String hd = bannerMetaData2.getHomedepot();
                                        		String tg = bannerMetaData2.getTarget();
                                        		String ct = bannerMetaData2.getCostco();
                                        		String ds = bannerMetaData2.getDollarstore();
                                        		String title = bannerMetaData2.getColumnTitle();
                                        		StringBuffer sb= new StringBuffer();
                                        		StringBuffer sb1= new StringBuffer();
                                        		StringBuffer sb2= new StringBuffer();
                                        		int flag=1;
                                        		if(!ctr.contains(",")){
                                        			flag=0;
	                                        		sb.append("select (case when weight is null then '-' else round(sum(t1.weight)) end), (case when weight is null then '-' else round((sum(weight)/(select sum(weight) from data)*100),1) end) from data t1 where "+ctr+" ='1'");
	                                        		sb.append(" union all ");
	                                        		sb.append("select (case when weight is null then '-' else round(sum(t2.weight)) end),(case when weight is null then '-' else round((sum(weight)/(select sum(weight) from data)*100),1) end) from data t2 where "+wm+" ='1'");
	                                        		sb.append(" union all ");
	                                        		sb.append("select (case when weight is null then '-' else round(sum(t3.weight)) end),(case when weight is null then '-' else round((sum(weight)/(select sum(weight) from data)*100),1) end) from data t3 where "+hd+" ='1'");
	                                        		sb.append(" union all ");
	                                        		sb.append("select (case when weight is null then '-' else round(sum(t4.weight)) end),(case when weight is null then '-' else round((sum(weight)/(select sum(weight) from data)*100),1) end) from data t4 where "+tg+" ='1'");
	                                        		sb.append(" union all ");
	                                        		sb.append("select (case when weight is null then '-' else round(sum(t5.weight)) end),(case when weight is null then '-' else round((sum(weight)/(select sum(weight) from data)*100),1) end) from data t5 where "+ct+" ='1'");
	                                        		sb.append(" union all ");
	                                        		sb.append("select (case when weight is null then '-' else round(sum(t6.weight)) end),(case when weight is null then '-' else round((sum(weight)/(select sum(weight) from data)*100),1) end) from data t6 where "+ds+" ='1'");
                                        		}else{
                                        			System.out 
															.println(ctr);
                                        			// canadiantire
                                        			String ctrArry[] = ctr.split(",");
                                        			sb1.append(" (select sum(b.freq) from ( ");
                                        			for(int j=0;j<ctrArry.length;j++){
                                        				String ctrStr = ctrArry[j];
                                        				sb2.append(" select round((sum(t1.weight)/(select sum(weight) from data))*100,1) w from data t1 where "+ctrStr+" ='1'");
                                        				sb1.append(" select case when count("+ctrStr+") >0 then 1 else 0 end freq from data where "+ctrStr+"='1' ");
                                        				if(j+1<ctrArry.length){
                                        					
                                        					sb2.append(" union all ");
                                        					sb1.append(" union all ");
                                        				}
                                        			}
                                        			sb2.append(") a");
                                        			sb1.append(") b) ");
                                        			sb2.append(" union all ");
                                        			
                                        			sb.append("select round((sum(a.w)/"+sb1.toString()+"),1) from ("+sb2.toString());
                                        		
                                        			
                                        			//walmart
                                        			sb1 = new StringBuffer();
                                        			sb2 = new StringBuffer();
                                        			String wmArry[] = wm.split(",");
                                        			sb1.append(" (select sum(b.freq) from ( ");
                                        			for(int j=0;j<wmArry.length;j++){
                                        				String wmStr = wmArry[j];
                                        				sb2.append(" select round((sum(t1.weight)/(select sum(weight) from data))*100,1) w from data t1 where "+wmStr+" ='1'");
                                        				sb1.append(" select case when count("+wmStr+") >0 then 1 else 0 end freq from data where "+wmStr+"='1' ");
                                        				if(j+1<wmArry.length){
                                        					sb2.append(" union all ");
                                        					sb1.append(" union all ");
                                        				}
                                        			}
                                        			sb2.append(") a");
                                        			sb1.append(") b) ");
                                        			sb2.append(" union all ");
                                        			
                                        			sb.append("select round((sum(a.w)/"+sb1.toString()+"),1) from ("+sb2.toString());
                                        			
                                        			
                                        			//Homedepot
                                        			sb1 = new StringBuffer();
                                        			sb2 = new StringBuffer();
                                        			String hdArry[] = hd.split(",");
                                        			sb1.append(" (select sum(b.freq) from ( ");
                                        			for(int j=0;j<hdArry.length;j++){
                                        				String hdStr = hdArry[j];
                                        				sb1.append(" select case when count("+hdStr+") >0 then 1 else 0 end freq from data where "+hdStr+"='1' ");
                                        				sb2.append(" select round((sum(t1.weight)/(select sum(weight) from data))*100,1) w from data t1 where "+hdStr+" ='1'");
                                        				if(j+1<hdArry.length){
                                        					sb2.append(" union all ");
                                        					sb1.append(" union all ");
                                        				}
                                        			}
                                        			sb2.append(") a");
                                        			sb1.append(") b) ");
                                        			sb2.append(" union all ");
                                        			
                                        			sb.append("select round((sum(a.w)/"+sb1.toString()+"),1) from ("+sb2.toString());
                                        			

                                        			//Target
                                        			sb1 = new StringBuffer();
                                        			sb2 = new StringBuffer();
                                        			String tgArry[] = tg.split(",");
                                        			sb1.append(" (select sum(b.freq) from ( ");
                                        			for(int j=0;j<tgArry.length;j++){
                                        				String tgStr = tgArry[j];
                                        				sb1.append(" select case when count("+tgStr+") >0 then 1 else 0 end freq from data where "+tgStr+"='1' ");
                                        				sb2.append(" select round((sum(t1.weight)/(select sum(weight) from data))*100,1) w from data t1 where "+tgStr+" ='1'");
                                        				if(j+1<tgArry.length){
                                        					sb2.append(" union all ");
                                        					sb1.append(" union all ");

                                        				}
                                        			}
                                        			sb2.append(") a");
                                        			sb1.append(") b) ");
                                        			sb2.append(" union all ");
                                        			
                                        			sb.append("select round((sum(a.w)/"+sb1.toString()+"),1) from ("+sb2.toString());
                                        			

                                        			//Costco
                                        			sb1 = new StringBuffer();
                                        			sb2 = new StringBuffer();
                                        			String ctArry[] = ct.split(",");
                                        			sb1.append(" (select sum(b.freq) from ( ");
                                        			for(int j=0;j<ctArry.length;j++){
                                        				String ctStr = ctArry[j];
                                        				sb1.append(" select case when count("+ctStr+") >0 then 1 else 0 end freq from data where "+ctStr+"='1' ");
                                        				sb2.append(" select round((sum(t1.weight)/(select sum(weight) from data))*100,1) w from data t1 where "+ctStr+" ='1'");
                                        				if(j+1<ctArry.length){
                                        					sb2.append(" union all ");
                                        					sb1.append(" union all ");
                                        				}
                                        			}
                                        			sb2.append(") a");
                                        			sb1.append(") b) ");
                                        			sb2.append(" union all ");
                                        			
                                        			sb.append("select round((sum(a.w)/"+sb1.toString()+"),1) from ("+sb2.toString());
                                        			
                                        			//Dollarstore
                                        			sb1 = new StringBuffer();
                                        			sb2 = new StringBuffer();
                                        			String dsArry[] = ds.split(",");
                                        			sb1.append(" (select sum(b.freq) from ( ");
                                        			for(int j=0;j<dsArry.length;j++){
                                        				String dsStr = dsArry[j];
                                        				sb1.append(" select case when count("+dsStr+") >0 then 1 else 0 end freq from data where "+dsStr+"='1' ");
                                        				sb2.append(" select round((sum(t1.weight)/(select sum(weight) from data))*100,1) w from data t1 where "+dsStr+" ='1'");
                                        				if(j+1<dsArry.length){
                                        					sb2.append(" union all ");
                                        					sb1.append(" union all ");
                                        				}
                                        			}
                                        			sb2.append(") a");
                                        			sb1.append(") b) ");
                                        			
                                        			sb.append("select round((sum(a.w)/"+sb1.toString()+"),1) from ("+sb2.toString());
                                        		}
                                        		System.out
														.println(sb);
	                                        		ResultSet  rs = session.connection().createStatement().executeQuery(sb.toString());
	                                        		if(rs.next()){
	                                        			int dataCell=0;
	                                        			Row dataRow = sheet.createRow(sheetRowIndex);
	                                        			Cell dataListCell = dataRow.createCell((short)dataCell);
	                                        			dataListCell.setCellValue(title);
	                                        			dataCell++;
	                                        			sheetRowIndex++;
	                                        			Row percentRow = sheet.createRow(sheetRowIndex);
	                                        			sheetRowIndex++;
	                                        			if(flag==0){
	                                        				Row dummy = sheet.createRow(sheetRowIndex);
		                                        			sheetRowIndex++;
	                                        			}
                                                        rs.previous();
                                                        while (rs.next()) {
                                                    	   String weightedFreq = rs.getString(1);
                                                    	   String weightedPercentage = null;
                                                    	   if(flag==0){
                                                    		   weightedPercentage = rs.getString(2);
                                                    	   }
                                                           
                                                    	   dataListCell = dataRow.createCell((short)dataCell);
                                                    	   Cell percentListCell =null;
                                                    	   if(flag==0){
                                                    		   percentListCell = percentRow.createCell((short)dataCell);
                                                    	   }
		                                                   dataCell++;
		                                                   if(!ApplicationUtil.isEmptyOrNull(weightedFreq) && !weightedFreq.equals("-")){
		                                                	   dataListCell.setCellValue(Double.parseDouble(weightedFreq));
		                                                   }else if(!ApplicationUtil.isEmptyOrNull(weightedFreq) && weightedFreq.equals("-")){
		                                                	   dataListCell.setCellValue(weightedFreq);
		                                                   }
		                                                   else{
		                                                	   dataListCell.setCellValue(0);
		                                                   }
		                                                   if(!ApplicationUtil.isEmptyOrNull(weightedFreq) && !weightedFreq.equals("-")){
		                                                   dataListCell.setCellType(Cell.CELL_TYPE_NUMERIC);
		                                                   }
		                                                   if(percentListCell != null){
			                                                   if(!ApplicationUtil.isEmptyOrNull(weightedPercentage) && !weightedPercentage.equals("-")){
			                                                	   percentListCell.setCellValue(Double.parseDouble(weightedPercentage));
			                                                   }else if(!ApplicationUtil.isEmptyOrNull(weightedPercentage) && weightedPercentage.equals("-")){
			                                                	   percentListCell.setCellValue(weightedFreq);
			                                                   }
			                                                   else{
			                                                	   percentListCell.setCellValue(0.0);
			                                                   }
			                                                   if(!ApplicationUtil.isEmptyOrNull(weightedPercentage) && !weightedPercentage.equals("-")){
			                                                   percentListCell.setCellType(Cell.CELL_TYPE_NUMERIC);
		                                                   }
		                                                   }
                                                        }
	                                        		}
                                        	}
                                        }
                                        FileOutputStream fileOut =  new FileOutputStream(fileName);
                                    	workbook.write(fileOut);
                                    	fileOut.close();
                                } catch (HibernateException he) {
                                        throw he;
                                } catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} 
								return BannerMetaDataList;
                        }
                });
        } catch (Exception e) {
        		logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
                // TODO: handle exception
                ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "downloadBannertable", 
                                ApplicationErrorCodes.APP_EC_37, ApplicationConstants.EXCEPTION, e);
        		throw e;
        }

        logger.debug("Leaving from downloadBannertable2");  
    return (List)object;
}

public Map getKDAManagerReportData(final String empId,final Integer templateChartInfoId,final String filterVariable) throws ApplicationException
{
        Object object = null;
        logger.debug("Entered into getKDAManagerReportData");
        try {
                object = hibernateTemplate.execute(new HibernateCallback() {
                        public Object doInHibernate(Session session)
                                        throws HibernateException, SQLException {
                        	Map variableNamesObj=(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.VARIABLE_NAMES_MAP_CACHE_KEY);
                        	Map map = new HashMap();
                        	List highStrengthList = new ArrayList();
                        	List highNeutralList= new ArrayList();
                        	List highWeaknessList= new ArrayList();
                        	List mediumStrengthList = new ArrayList();
                        	List mediumNeutralList= new ArrayList();
                        	List mediumWeaknessList= new ArrayList();
                        	List lowStrengthList = new ArrayList();
                        	List lowNeutralList= new ArrayList();
                        	List lowWeaknessList= new ArrayList();
                        	String segmentName ="";
                        	String referenceGroupValues="";
                        	
            				try {
            					String sql = "select xaxis,yaxis,value,manageravgperfvalue,round(xaxis*1.05,2),round(xaxis*0.95,2),segmentName from chartkda where employeeid="+empId+" and templateChartInfoId="+templateChartInfoId;
            					SQLQuery q = session.createSQLQuery(sql);
            					List reportList =  (List)q.list();
            					sql ="select  group_concat(distinct "+filterVariable+" separator ',') from data where employeeId in (select employee from hierarchy where manager='"+empId+"')";
            					ResultSet  rs = session.connection().createStatement().executeQuery(sql);
            					while(rs.next()){
            						referenceGroupValues = rs.getString(1);
            					}
            					if(reportList !=null){
                    					int size = reportList.size();
                    					double values[] = new double[size];
                    					for(int k=0;k<size;k++){
                    						Object[] rowList = (Object[])reportList.get(k);
                    						String importanceValueStr = (String)rowList[2];
                    						values[k] = Double.parseDouble(importanceValueStr);
                    					}
                    					Percentile percentile = new Percentile();
                    					double topThirty = percentile.evaluate(values, 70.0);
                    					double bottomFourty = percentile.evaluate(values, 40.0);
                    					
                    					for(int k=0;k<size;k++){
                    						List rowDataList = new ArrayList();
                    						Object[] rowList = (Object[])reportList.get(k);
                    						String avgPerformanceStr = (String)rowList[0];
                    						Double avgPerformance = new Double(avgPerformanceStr);
                                            String columnName = (String)rowList[1];
                                            String importanceValueStr = (String)rowList[2];
                                            Double importanceValue = new Double(importanceValueStr);
                                            String managerAvgPerformanceStr = (String)rowList[3];
                                            Double managerAvgPerformance = new Double(managerAvgPerformanceStr);
                                            Double maxAvgPerformance = (Double)rowList[4];
                                            Double minAvgPerformance = (Double)rowList[5];
                                            segmentName = (String) rowList[6];
                                            
                                            if(variableNamesObj != null){
                                    			String label = (String)variableNamesObj.get(columnName.toLowerCase());
                                    			if(!ApplicationUtil.isEmptyOrNull(label)){
                                    				columnName = label;
                                    			}
                                    		}
                                            if(importanceValue>topThirty){
                                            	if(managerAvgPerformance>maxAvgPerformance){
                                            		highStrengthList.add(columnName);
                                            	}else if(managerAvgPerformance<minAvgPerformance){
                                                	highWeaknessList.add(columnName);
                                            	}else{
                                            		highNeutralList.add(columnName);
                                            	}
                                            }else if(importanceValue<bottomFourty){
                                            	if(managerAvgPerformance>maxAvgPerformance){
                                            		lowStrengthList.add(columnName);
                                            	}else if(managerAvgPerformance<minAvgPerformance){
                                                	lowWeaknessList.add(columnName);
                                            	}else{
                                            		lowNeutralList.add(columnName);
                                            	}
                                            }else{
                                            	if(managerAvgPerformance>maxAvgPerformance){
                                            		mediumStrengthList.add(columnName);
                                            	}else if(managerAvgPerformance<minAvgPerformance){
                                            		mediumWeaknessList.add(columnName);
                                            	}else{
                                            		mediumNeutralList.add(columnName);
                                            	}
                                            }
                    					}
            					}
            					map.put("highStrengthList", highStrengthList);
            					map.put("highNeutralList", highNeutralList);
            					map.put("highWeaknessList", highWeaknessList);
            					map.put("mediumStrengthList", mediumStrengthList);
            					map.put("mediumNeutralList", mediumNeutralList);
            					map.put("mediumWeaknessList", mediumWeaknessList);
            					map.put("lowStrengthList", lowStrengthList);
            					map.put("lowNeutralList", lowNeutralList);
            					map.put("lowWeaknessList", lowWeaknessList);
            					map.put("segmentName", segmentName);
            					map.put("referenceGroupValues", referenceGroupValues);
        	        			return map;
                            } catch (HibernateException he) {
                            	throw he;
                            }
                        }
                });
        } catch (Exception e) {
        	logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
                // TODO: handle exception
                ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "getKDAManagerReportData", 
                                ApplicationErrorCodes.APP_EC_5, ApplicationConstants.EXCEPTION, e);
        		throw e;
        }
        logger.debug("Leaving from getKDAManagerReportData");
    return (Map)object;
}

public void setDistributionUsers() throws ApplicationException
{
	try{
	logger.debug("Entered into setDistributionUsers");
	Object object = hibernateTemplate.execute(new HibernateCallback() {
		public Object doInHibernate(Session session)
				throws HibernateException, SQLException {
			try {
				
					List SurveyVariableListCache = (List)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.SURVEY_VARIABLES_LIST);
					StringBuffer columnBufferWhereClauseStr =new StringBuffer();
					if(SurveyVariableListCache !=null){
                        int col = SurveyVariableListCache.size();
                        for(int l=0;l<col;l++){
                        	VariableNames rowList = (VariableNames) SurveyVariableListCache.get(l);
	                      String columnName = (String) rowList.getColumnName();
	                      columnBufferWhereClauseStr.append(" d."+columnName+ " IS NOT NULL");
	                      if(l+1<col){
	                             columnBufferWhereClauseStr.append(" " +"or" + " ");
	                      }
                        }
					}
					String whereClauseStr="";
					String whereClause = columnBufferWhereClauseStr.toString();
					if(!ApplicationUtil.isEmptyOrNull(whereClause)){
						whereClauseStr = " and ("+whereClause+")";
					}
					String minimumRespondentsStr = PropertiesUtil.getProperty(ApplicationConstants.MINIMUM_RESPONDENTS);
					int minimumRespondents = 0;
					if(!ApplicationUtil.isEmptyOrNull(minimumRespondentsStr)){
						minimumRespondents = Integer.parseInt(minimumRespondentsStr);
					}
					
					String sql ="select count(employeeid), mgr, hh.name,hh.emailId from (select h.manager mgr, group_concat(h.employee separator ',') empids, e.name,e.emailId from hierarchy h, employee e";
					sql+=" where h.manager = e.employeeid and h.manager in (select employeeId from employee where emailId in (select distinct emailid from distributionlist where emailid not in (select emailid from users))) group by manager) hh, ";
					sql+=" data d where find_in_set(d.employeeId, hh.empids) "+whereClauseStr+" group by mgr having count(employeeid) > "+minimumRespondents;
				
            		 //String sql = "select distinct(d.emailId),e.name from distributionlist d, employee e where d.emailId not in (select u.emailId from users u ) and d.emailId=e.emailId";
            		 System.out.println(sql);
 					 SQLQuery q = session.createSQLQuery(sql);
 					 List emailIdList =  (List)q.list();
 					 
 					String adminEmail = PropertiesUtil.getProperty("admin_email");
 			        String adminPassword = PropertiesUtil.getProperty("admin_password");
 			        Properties props = new Properties();
 			        props.put("mail.smtp.auth",
 			                     PropertiesUtil.getProperty("mail.smtp.auth"));
 			        props.put("mail.smtp.starttls.enable",
 			                     PropertiesUtil.getProperty("mail.smtp.starttls.enable"));
 			        props.put("mail.smtp.host",
 			                     PropertiesUtil.getProperty("mail.smtp.host"));
 			        props.put("mail.smtp.port",
 			                     PropertiesUtil.getProperty("mail.smtp.port"));
 					if(emailIdList!=null){
 						StringBuffer sb = new StringBuffer();
 				        int len = emailIdList.size();
 						for(int i=0;i<len;i++){
 							Object[] row = (Object[])emailIdList.get(i);
 							
 							String managerId = row[1].toString();
 							String name = row[2].toString();
 							String emailId = row[3].toString();
 							 String password = ApplicationUtil.getPublishRandomKey();
 							sb.append(name);
 							sb.append(",");
 							sb.append(managerId);
 							sb.append(",");
 							sb.append(password);
 							sb.append("\n");
 						   EncryptService encryptService=EncryptService.getInstance();
 							String encryptedPassword = encryptService.encrypt(password);
 						    
 						    Users users = new Users();
 						    users.setUserName(managerId);
 						    users.setPassword(encryptedPassword);
 						    users.setFirstName(name);
 						    users.setEmailId(emailId);
 						    session.beginTransaction();
 						    session.save(users);
 						   
 						    Roles roles =new Roles();
 						    roles.setRole("MANAGER");
 						    roles.setUsers(users);
 						    session.save(roles);
 						    
 						    Roles roles1 = new Roles();
 						    roles1.setRole("ROLE_USER");
 						    roles1.setUsers(users);
						    session.save(roles1);
 						  
 						  /* String sql1 ="select emailId from users";
 						    q = session.createSQLQuery(sql1);
 		 					List emailIdList1 =  (List)q.list();
 		 					List mailIdList=new ArrayList();
 		 					
 						   if(emailIdList1.contains(emailId)){
 		 					
	 							StringBuffer body = new StringBuffer();
	 					        body.append("Dear " + " " +name+ ",\n\n" + "The following reports are available for your persual. " +"\n\n" + 
	 				            		"Please login with your credentials to view your report." + "\n\n" + 
	 				            		"URL for logging in" + "\n\n" +
	 				            		 "http://192.168.3.213:8080/EmployeeSurveySatisfaction/login.jsp" + "\n\n\n" +
	 				            		"User Name : " +emailId+ "\n\n" +
	 				            		"Password  : " +password+ "\n\n" +
	 				            		"Also, please forward this mail along with your feedback to Raghav & Arasi for testing purposes" + "\n\n" +	 				        
	 				            		"Thanks," + "\n\n" +
	 				            		"ESS Reporting Solution");
	 					        try {
	 					              ApplicationUtil.sendEMail(emailId,
	 					                           "Ess Login Credentials",
	 					                            body.toString(), adminEmail, adminPassword, props,
	 					                            PropertiesUtil.getProperty("APP_EC_27"));
	 					              
	 					              ApplicationException applicationException = ApplicationException.createApplicationException(ReportTemplateListController.class.toString(), "setDistributionUsers", 
	 					  					ApplicationErrorCodes.APP_EC_27, ApplicationConstants.INFORMATION, null);
	 				
	 					        } catch (Exception e) {
	 					        	logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
	 					        	System.out.println(e);
	 					        }
 						   }*/
 						  session.getTransaction().commit();
 						}
 						try {
							FileWriter fw = new FileWriter(new File("c:\\users.txt"));
							logger.debug("----------------------users-------------------");
							logger.debug(sb.toString());
							logger.debug("---------------------------------------------");
							fw.write(sb.toString());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
 					}
			} catch (HibernateException he) {
				session.getTransaction().rollback();
				throw he;
			}
			return null;
		}
	});
	logger.debug("Leaving from setDistributionUsers");
	}catch (Exception e) {
		logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
		throw e;
	}
}
public String getWnsCountryColumn(final String employeeId) throws ApplicationException{
	
		logger.debug("Entered into getWnsCountryColumn");
		Object object = hibernateTemplate.execute(new HibernateCallback() {
			@SuppressWarnings("unused")
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String wnsCountryColumn = PropertiesUtil.getProperty(ApplicationConstants.WNS_AVG_COUNTRY_COLUMN_NAME);
				String wnsBenchmarkColumn = PropertiesUtil.getProperty(ApplicationConstants.WNS_BENCH_MARK_COLUMN_NAME);
				String wnsCountryColumnStr=null;
				if(!ApplicationUtil.isEmptyOrNull(wnsCountryColumn) && !ApplicationUtil.isEmptyOrNull(wnsBenchmarkColumn)){
					StringBuffer sb1 = new StringBuffer();
					sb1.append("select "+wnsCountryColumn+" val from data d where employeeId="+employeeId);
					ResultSet rs2 = session.connection().createStatement().executeQuery(sb1.toString());
					rs2.next();
			        String value=rs2.getString(1);
					if(!ApplicationUtil.isEmptyOrNull(value) && value.equalsIgnoreCase("india")){
						wnsCountryColumnStr = wnsBenchmarkColumn;
					}
				}
				return wnsCountryColumnStr;
				
			}
		});
		logger.debug("Leaving from getWnsCountryColumn");
		return (String)object;
}

public Map getRawComments(final Integer templateChartInfoId) 
{
	try{
	logger.debug("Entered into getRawComments");
	Object object = hibernateTemplate.execute(new HibernateCallback() {
		public Object doInHibernate(Session session)
				throws HibernateException, SQLException {
			Map categoryCommentListMap = new HashMap();
			List categoryCommentList = new ArrayList();
        	List categoryList= new ArrayList();
        	List rawCommentsList =null;
			try {
				String sql = "select category,comments from rawcomments where templateChartInfoId = "+templateChartInfoId+" order by id ";
				ResultSet rs2 = session.connection().createStatement().executeQuery(sql);
				String tempCategory=null;
				while(rs2.next()){
					String categoryName = rs2.getString(1);
					if(ApplicationUtil.isEmptyOrNull(tempCategory) || !tempCategory.equalsIgnoreCase(categoryName)){
						rawCommentsList = new ArrayList();
						tempCategory = categoryName;
						categoryList.add(categoryName);
						categoryCommentList.add(rawCommentsList);
					}
					rawCommentsList.add(rs2.getString(2));
				}
				categoryCommentListMap.put("categoryCommentList", categoryCommentList);
				categoryCommentListMap.put("categoryList", categoryList);
    			return categoryCommentListMap;
			
			} catch (HibernateException he) {
				throw he;
			}
		}
	});
	logger.debug("Leaving from getRawComments");
    return (Map)object;
	}catch (Exception e) {
		logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
		throw e;
	}
}

public List getSegmentByLocation(final String segmentName) throws ApplicationException
{
     Object object = null;
     logger.debug("Entered into getSegmentByLocation");
     try {
             object = hibernateTemplate.execute(new HibernateCallback() {
                     public Object doInHibernate(Session session)
                                     throws HibernateException, SQLException {
                           Map variableNamesObj=(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.VARIABLE_NAMES_MAP_CACHE_KEY);
                           List segmentByLocationList = new ArrayList();
                           List segmentLocationHeaderList= new ArrayList();
                           segmentLocationHeaderList.add("                ");
                           segmentByLocationList.add(segmentLocationHeaderList);
                           
                           List categoryNameList = new ArrayList();
                                  try {
                                         if(ApplicationUtil.isEmptyOrNull(segmentName)){
                                                return segmentByLocationList;
                                         }
                                         String sql = "select distinct location from segment_avg_bylocation where segmentName='"+segmentName+"'";
                                         SQLQuery q = session.createSQLQuery(sql);
                                         List LocationList =  (List)q.list();
                                         if(LocationList !=null){
                                                StringBuffer sb = new StringBuffer("SELECT m.columnname, ");
                                                        int col = LocationList.size();
                                                        for(int k=0;k<col;k++){
                                                             String location = (String)LocationList.get(k);
                                                              segmentLocationHeaderList.add(location);
                                                      sb.append(" max(case when location='"+location+"'  then (case when avgrespondents>4 then m.value else '-----' end)  end) val"+k);
                                                      if(k+1<col){
                                                             sb.append(",");
                                                      }
                                                        }
                                                       sb.append(", mc.categoryname FROM segment_avg_bylocation m join variablenames v on m.columnName=v.columnName join master_variable_categories mv on v.variableName = mv.variablename  ");
                                                       sb.append("join master_categories mc on mc.id=mv.mastercategoryid where  segmentName='"+segmentName+"' group by m.columnname order by mc.categoryname,m.columnname");
                                                        if(sb.length()>0 && col>0){
                                                             q = session.createSQLQuery(sb.toString());
                                                              System.out.println("query:"+sb.toString());
                                                List reportList =  (List)q.list();
                                                int size = reportList.size();
                                                for(int k=0;k<size;k++){
                                                       List rowDataList = new ArrayList();
                                                       Object[] rowList = (Object[])reportList.get(k);
                                                       int len = rowList.length;
                                                       String columnName = (String)rowList[0];
                                                       String label = (String)variableNamesObj.get(columnName.toLowerCase());
                                                       if(ApplicationUtil.isEmptyOrNull(label)){
                                                              label = columnName;
                                                       }
                                                       rowDataList.add(label);
                                                       for(int i=1;i<len;i++){
                                                              if(i<len-1){
                                                                     String val = (String) (rowList[i]+"");
                                                                     rowDataList.add(val);
                                                              } else{
                                                                     String categoryName = (String)rowList[i];
                                                                     categoryNameList.add(categoryName);
                                                              }
                                                              
                                                       }
                                                       segmentByLocationList.add(rowDataList);
                                                }
                                                segmentByLocationList.add(categoryNameList);
                                                        }
                                         }
                                         return segmentByLocationList;
                         } catch (HibernateException he) {
                           throw he;
                         }
                     }
             });
     } catch (Exception e) {
              logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
             // TODO: handle exception
             ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "getSegmentByLocation", 
                             ApplicationErrorCodes.APP_EC_42, ApplicationConstants.EXCEPTION, e);
              throw e;
     }
     logger.debug("Leaving from getSegmentByLocation");
 return (List)object;
}

public List getUsersList() throws ApplicationException
{
	try{
	logger.debug("Entered into getUsersList");
	Object object = hibernateTemplate.execute(new HibernateCallback() {
		public Object doInHibernate(Session session)
				throws HibernateException, SQLException {
			try {
				List userList = null;
            		 String sql = "from Users order by firstName asc";
        			 Query q = session.createQuery(sql);
        			 userList =  (List)q.list();
    			return userList;
			} catch (HibernateException he) {
				throw he;
			}
		}
	});
	logger.debug("Leaving from getUsersList");
    return (List)object;
	}catch (Exception e) {
		logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
		ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "getUserFromUserName", 
				ApplicationErrorCodes.APP_EC_22, ApplicationConstants.EXCEPTION, e);
		throw applicationException;
	}
	
}

public List getMasterCommitStatusList() throws ApplicationException
{
	try{
	logger.debug("Entered into getMasterCommitStatusList");
	Object object = hibernateTemplate.execute(new HibernateCallback() {
		public Object doInHibernate(Session session)
				throws HibernateException, SQLException {
			List masterErrorCodesList = null;
			try {
            		 String sql = "from MasterCommitStatus";
        			 Query q = session.createQuery(sql);
        			 masterErrorCodesList =  (List)q.list();
    			return masterErrorCodesList;
			} catch (HibernateException he) {
				throw he;
			}
		}
	});
	logger.debug("Leaving from getMasterCommitStatusList");
    return (List)object;
	}catch (Exception e) {
		logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
		throw e;
	}
}

public List getCategoryFromScorecardRollup() throws ApplicationException
{
	try{
	logger.debug("Entered into getCategoryFromScorecardRollup");
	Object object = hibernateTemplate.execute(new HibernateCallback() {
		public Object doInHibernate(Session session)
				throws HibernateException, SQLException {
			List categoryFromScorecardRollupList = null;
			try {
            		 String sql = "select categoryScorecardMapping.scorecardRollup,categoryScorecardMapping.categoryName from CategoryScorecardRollupMapping categoryScorecardMapping";
        			 Query q = session.createQuery(sql);
        			 categoryFromScorecardRollupList =  (List)q.list();
    			return categoryFromScorecardRollupList;
			} catch (HibernateException he) {
				throw he;
			}
		}
	});
	logger.debug("Leaving from getMasterCommitStatusList");
    return (List)object;
	}catch (Exception e) {
		logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
		throw e;
	}
}

public final List getBusiness() throws ApplicationException{
		Object object = null;
		logger.debug("Entered into getBusiness in ApplicationDAO");
			try{
				
				object = hibernateTemplate.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						List distinctBusiness=null;
						String businessType = PropertiesUtil.getProperty("businessType");
						try {
							Criteria distinctBusinessCriteria = session.createCriteria(Data.class);
							distinctBusinessCriteria.add(Restrictions.not(Restrictions.eq(businessType,"")));
							distinctBusinessCriteria.add(Restrictions.isNotNull(businessType));
							ProjectionList distinctBusinessProjection= Projections.projectionList();
							distinctBusinessProjection.add(Projections.distinct(Projections.property(businessType)));
							distinctBusinessCriteria.setProjection(distinctBusinessProjection);
							distinctBusiness = distinctBusinessCriteria.list();
							
						} catch (HibernateException he) {
							throw he;
						}
						return distinctBusiness;
					}
			});
			}catch (Exception e) {
				logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			}
			logger.debug("Leaving from getBusiness method in ApplicationDAO");
			return (List)object;
		}
public final List getRegion() throws ApplicationException{
		Object object = null;
		logger.debug("Entered into getRegion in ApplicationDAO");
		try{
				object = hibernateTemplate.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						List distinctBusiness=null;
						try {
							String derivedRegion = PropertiesUtil.getProperty("region");
							Criteria distinctRegionCriteria = session.createCriteria(Data.class);
							distinctRegionCriteria.add(Restrictions.not(Restrictions.eq(derivedRegion,"")));
							distinctRegionCriteria.add(Restrictions.isNotNull(derivedRegion));
							ProjectionList distinctRegionProjection= Projections.projectionList();
							distinctRegionProjection.add(Projections.distinct(Projections.property(derivedRegion)));
							distinctRegionCriteria.setProjection(distinctRegionProjection);
							distinctBusiness = distinctRegionCriteria.list();
							
						} catch (HibernateException he) {
							throw he;
						}
						return distinctBusiness;
					}
				});
			}catch (Exception e) {
				logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			}
			logger.debug("Leaving from getRegion method in ApplicationDAO");
			return (List)object;
		}
}
