package com.bridgei2i.common.dao;


import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.Projection;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.bridgei2i.common.constants.ApplicationConstants;
import com.bridgei2i.common.constants.ApplicationErrorCodes;
import com.bridgei2i.common.controller.LoadApplicationCacheService;
import com.bridgei2i.common.exception.ApplicationException;
import com.bridgei2i.common.util.ApplicationUtil;
import com.bridgei2i.common.util.CsvReaderWriter;
import com.bridgei2i.common.util.PropertiesUtil;
import com.bridgei2i.common.vo.Users;
import com.bridgei2i.common.vo.Data;
import com.bridgei2i.vo.Data2;
import com.bridgei2i.vo.ForecastASP;
import com.bridgei2i.vo.ForecastESC;
import com.bridgei2i.vo.ForecastPMPercent;
import com.bridgei2i.vo.ForecastUnits;
import com.bridgei2i.vo.PlanningCycle;
import com.bridgei2i.vo.RawData;
import com.bridgei2i.vo.TargetData;
import com.bridgei2i.vo.VariableNames;
import com.bridgei2i.vo.MasterPlanningStatus;
import com.bridgei2i.vo.PlanningCycleVO;
import com.bridgei2i.vo.SkuList;
import com.bridgei2i.vo.SkuUserMapping;
import com.bridgei2i.vo.OverrideCommit;
import com.bridgei2i.vo.OverrideAspLog;
import com.bridgei2i.vo.PlanningLog;
import com.bridgei2i.vo.OverrideUnitsLog;
import com.bridgei2i.vo.ForecastingASP;
import com.bridgei2i.vo.ForecastingESC;
import com.bridgei2i.vo.ForecastingProductMargin;
import com.bridgei2i.vo.ForecastingRevenue;
import com.bridgei2i.vo.ForecastingUnits;
import com.bridgei2i.vo.ForecastingPM;
import com.bridgei2i.vo.CategoryList;
import com.bridgei2i.vo.CategoryUserMapping;
import com.bridgei2i.vo.OverrideCommitCategory;
import com.bridgei2i.form.PlanningLogBean;
import com.bridgei2i.form.CategoryLevelSummaryBean;

public class CategoryLevelSummaryDAO {
	@Autowired(required=true)
	private PlanningDAO planningDAO;


	private HibernateTemplate hibernateTemplate;
	private static Logger logger = Logger.getLogger(PlanningDAO.class);
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	@SuppressWarnings("unchecked")
	public Map getCategoryLevelSummary(final int planningCycleId,final int statusId,final HttpServletRequest request){
		Object object = null;
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
						Map categoryLevelSummaryMap = new HashMap();
						List categoryLevelSummaryList=null;
					try {
						Map masterCommitStatusNameMap = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.MASTER_COMMIT_STATUS_NAME_MAP);
						Map masterCommitStatusIdMap = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.MASTER_COMMIT_STATUS_ID_MAP);
						String sql="select distinct c.categoryName,u.firstName from CategoryList c, CategoryUserMapping cum, Users u where c.id = cum.categoryId and cum.userId=u.login_id";
						Query categoryListQuery=session.createQuery(sql);
						String categoryName=PropertiesUtil.getProperty(ApplicationConstants.CATEGORY);
						List resultList=categoryListQuery.list();
						if(resultList!= null){
							int approveFlagIndex=0;
							categoryLevelSummaryList = new ArrayList();
							int size = resultList.size();
							for(int i=0;i<size;i++){
								List displayList = new ArrayList();
								Object obj[] = (Object[])resultList.get(i);
								String categoryNameValue = (String)obj[0];
								String userName = (String)obj[1];
								String whereClause="AND data."+categoryName+" = '"+categoryNameValue+"'";
								PlanningCycle activePlanningCycle = (PlanningCycle)ApplicationUtil.getObjectFromSession(ApplicationConstants.ACTIVE_PLANNINGCYCLE_OBJ, request);
								String startWeek=activePlanningCycle.getStartWeek();
								Integer week=Integer.parseInt(startWeek);
								String startYear=activePlanningCycle.getStartYear();
								Integer year =Integer.parseInt(startYear);
								int categoryStatusId = planningDAO.getModelLevelStatus(-1, whereClause, null, null, planningCycleId, "CATEGORY",null,week,year);
								String categoryStatusStr = (String)masterCommitStatusNameMap.get(categoryStatusId+"");
								displayList.add(categoryNameValue);
								displayList.add(userName);
								displayList.add(categoryStatusStr);
		    					if(statusId==categoryStatusId){
		    						displayList.add(1);
		    						displayList.add(1);
		    						approveFlagIndex++;
		    					}else{
		    						displayList.add(0);
		    						displayList.add(0);
		    					}
								categoryLevelSummaryList.add(displayList);
							}
							categoryLevelSummaryMap.put("categoryLevelSummaryList", categoryLevelSummaryList);
							if(approveFlagIndex==size){
								categoryLevelSummaryMap.put("categoryLevelSummaryApprove", 1);
							}else{
								categoryLevelSummaryMap.put("categoryLevelSummaryApprove", 0);
							}
						}
					} catch (HibernateException he) {
							logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
							he.printStackTrace();
					} catch (ApplicationException e) {
						// TODO Auto-generated catch block
						logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
						e.printStackTrace();
					}
					return categoryLevelSummaryMap;
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException("categoryLevelSummaryDAO", "getCategoryLevelSummary", 
					ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
		}
		return (Map)object;
	  }

	public void setOverrideFreeze(final int planningCycleId) throws ApplicationException
	   {
		Object object = null;
		logger.debug("Entered into setOverrideFreeze in categoryLevelSummaryDAO");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
						int week = 0;
						int year = 0;
							List<String[]> planningCycleDetails= new ArrayList<String[]>();
							session.beginTransaction();
							planningCycleDetails = getPlanningCycleDetails(planningCycleId);
							for (Object[] period  : planningCycleDetails)
							{
								week = Integer.valueOf(period[0].toString());
								year = Integer.valueOf(period[1].toString());
							}
							if(year % 4 ==0 || year % 400 == 0 )
							{
								if(week == 53)
								{
									week = 1;
								}
								year ++;
							}
							else
							{
								if (week == 52)
								{
									week = 1;
								}
								week++;
							}
							Integer planningStatusId = 0;
							try {
								planningStatusId = planningDAO.getMasterPlanningStatusIdByName("Upload");
							} catch (NumberFormatException e) {
								logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
							} catch (ApplicationException e) {
								logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
							}
							Integer workflowStatusId = 0;
							try {
								workflowStatusId =  planningDAO.getMasterWorkflowStatusIdByName("Upload");
							} catch (NumberFormatException e) {
								logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
							} catch (ApplicationException e) {
								logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
							}
							
							PlanningCycle newPlanningCycle =  new PlanningCycle();
							newPlanningCycle.setStartWeek(week+"");
							newPlanningCycle.setStartYear(year+"");
							newPlanningCycle.setWorkflowStatusId(workflowStatusId);
							newPlanningCycle.setCreateDate(new Timestamp(new java.util.Date().getTime()));
							newPlanningCycle.setPlanningStatusId(planningStatusId);
							session.save(newPlanningCycle);
							session.getTransaction().commit();
							changeStatusToClosed(planningCycleId);
					} catch (HibernateException he) {
						session.getTransaction().rollback();
						logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
					} catch (ApplicationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						throw new HibernateException(e);
					}
					return true;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException ae = ApplicationException
					.createApplicationException("CategoryLevelSummaryDAO",
							"setOverrideFreeze",
							ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER,
							ApplicationConstants.EXCEPTION, null);
			throw ae;
		}
		logger.debug("Leaving from setOverrideFreeze method in categoryLevelSummaryDAO");
	   }
	
	public void changeStatusToClosed(final int planningCycleId)throws ApplicationException
	   {
		Object object = null;
		logger.debug("Entered into changeStatusToClosed in categoryLevelSummaryDAO");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
						Integer planningStatusId = 0;
						Integer workFlowStatus = 0;
						try {
							planningStatusId = planningDAO.getMasterPlanningStatusIdByName("CLOSED");
							workFlowStatus = planningDAO.getMasterWorkflowStatusIdByName("FREEZE");
						} catch (ApplicationException e) {
							logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
						}
						String hqlUpdate = "update PlanningCycle c set c.planningStatusId = :planningStatusId,c.closedDate = :closedDate,c.workflowStatusId = :workflowStatusId where c.id = :planningCycleId";
						session.createQuery( hqlUpdate )
						        .setParameter( "planningStatusId", planningStatusId )
						        .setParameter( "workflowStatusId", workFlowStatus )
						        .setParameter( "closedDate",new Timestamp(new java.util.Date().getTime()))
						        .setParameter( "planningCycleId", planningCycleId )
						        .executeUpdate();
					} catch (HibernateException he) {
						logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
					}
					return true;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException ae = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "saveContact", 
					ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
			throw ae;
		}
		logger.debug("Leaving from changeStatusToClosed method in categoryLevelSummaryDAO");
	   }
	
	public List getPlanningCycleDetails(final int planningCycleId)
	   {
		Object object = null;
		logger.debug("Entered into getPlanningCycleDetails in categoryLevelSummaryDAO");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
						Criteria criteria =  session.createCriteria(PlanningCycle.class);
						ProjectionList criteriaProjection = Projections.projectionList();
						criteriaProjection.add(Projections.property("startWeek"));
						criteriaProjection.add(Projections.property("startYear"));
						//criteria.setProjection(Projections.max("id"));
						criteria.add(Restrictions.eq("id", planningCycleId));
						criteria.setProjection(criteriaProjection);
						//List planningCycleDetails =  criteria.list();
						return criteria.list();
					} catch (HibernateException he) {
						session.getTransaction().rollback();
						logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
					}
					return true;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
		}
		logger.debug("Leaving from getPlanningCycleDetails method in categoryLevelSummaryDAO");
		return (List)object;
	   }
	
	public String getWorkflowStatus(final int planningCycleId)
	   {
		Object object = null;
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
						String sql = "SELECT m.logicalName FROM PlanningCycle p,MasterWorkflowStatus m where p.workflowStatusId=m.id and p.id="+planningCycleId;
						Query query = session.createQuery(sql);
						List workflowstatusList = query.list();
						
				        return workflowstatusList.get(0);
					} catch (HibernateException he) {
							logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
							he.printStackTrace();
					}
					return true;
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "saveContact", 
					ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
		}
		return (String)object;
	   }
	
	public void releaseSkuToOverride(final int userId,final String category,final int releaseFlag, final int planningCycleId )throws ApplicationException{
		
		Object object = null;
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
						List categoryLevelSummaryList=null;
						String skuId=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_NUMBER);
						String categoryName=PropertiesUtil.getProperty(ApplicationConstants.CATEGORY);
						String businessColumn = PropertiesUtil.getProperty(ApplicationConstants.BUSINESS_TYPE);
					try {
						String sql="select distinct d."+skuId+",d."+businessColumn+" from Data d,SkuList sl where d."+categoryName+"='"+category+"' and d."+skuId+" = sl.productId";
						Query categoryReleaseQuery=session.createQuery(sql);
						List categoryProductIdList = (List)categoryReleaseQuery.list();
						Map masterCommitStatusIdMap = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.MASTER_COMMIT_STATUS_ID_MAP);
						String commitSatusId = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_PM);
						if(categoryProductIdList != null){
							int size = categoryProductIdList.size();
							for(int i=0;i<size;i++){
								Object[] obj = (Object[])categoryProductIdList.get(i);
								String productId = (String)obj[0];
								String business = (String)obj[1];
								List productIdList = new  ArrayList();
								productIdList.add(productId);
								planningDAO.saveCommitStatus(userId, productIdList, planningCycleId, null,commitSatusId,business);
							}
						}
					} catch (HibernateException he) {
							logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
							he.printStackTrace();
							throw he;
					} catch (ApplicationException e) {
						// TODO Auto-generated catch block
						throw new HibernateException(e);
					}
					return null;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException ae = ApplicationException
					.createApplicationException("CategoryLevelSummaryDAO",
							"releaseSkuToOverride",
							ApplicationErrorCodes.APP_EC_18,
							ApplicationConstants.EXCEPTION, null);
			throw ae;
		}
	}
	
	public void changeWorkFlowStatus(final int planningCycleId) throws ApplicationException{
		Object object = null;
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
						String skuId=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_NUMBER);
						String categoryName=PropertiesUtil.getProperty(ApplicationConstants.CATEGORY);
					try {
						session.beginTransaction();
						String sql="From PlanningCycle where id ="+planningCycleId;
						Query q=session.createQuery(sql);
						Integer workflowStatusId = 0;
						try {
							workflowStatusId = planningDAO.getMasterWorkflowStatusIdByName("APPROVE");
						} catch (ApplicationException e) {
							logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
						}
						PlanningCycle planningCycle=(PlanningCycle)q.uniqueResult();
						planningCycle.setWorkflowStatusId(workflowStatusId);
						session.update(planningCycle);
						session.getTransaction().commit();
					} catch (HibernateException he) {
							logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
							session.getTransaction().rollback();
							he.printStackTrace();
					}
					return null;
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "changeWorkFlowStatus", 
					ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
		}
	}
	
	public void commitCategory(final int userId,final String category, final int planningCycleId){
		
		Object object = null;
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					String skuId=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_NUMBER);
					String categoryName=PropertiesUtil.getProperty(ApplicationConstants.CATEGORY);
					String businessColumn = PropertiesUtil.getProperty(ApplicationConstants.BUSINESS_TYPE);
				try {
					String sql="select distinct d."+skuId+",d."+businessColumn+" from Data d,SkuList sl where d."+categoryName+"='"+category+"' and d."+skuId+" = sl.productId";
					Query categoryReleaseQuery=session.createQuery(sql);
					List categoryProductIdList = (List)categoryReleaseQuery.list();
					Map masterCommitStatusIdMap = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.MASTER_COMMIT_STATUS_ID_MAP);
					String commitSatusId = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_CD);
					if(categoryProductIdList != null){
						int size = categoryProductIdList.size();
						for(int i=0;i<size;i++){
							Object[] obj = (Object[])categoryProductIdList.get(i);
							String productId = (String)obj[0];
							String business = (String)obj[1];
							List productIdList = new  ArrayList();
							productIdList.add(productId);
							planningDAO.saveCommitStatus(userId, productIdList, planningCycleId, null,commitSatusId,business);
						}
					}
				} catch (HibernateException he) {
						logger.error("Exception Occured :" + he + "  at Line no :"+he.getStackTrace()[0].getLineNumber()+"  in File: "+he.getStackTrace()[0].getFileName());
						he.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception Occured :" + e + "  at Line no :"+e.getStackTrace()[0].getLineNumber()+"  in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException applicationException = ApplicationException.createApplicationException(ApplicationDAO.class.toString(), "commitCategory", 
					ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
		}
	}
	
	
}
