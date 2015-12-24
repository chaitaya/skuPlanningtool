package com.bridgei2i.common.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.util.StringUtils;

import com.bi2i.login.EncryptService;
import com.bridgei2i.common.constants.ApplicationConstants;
import com.bridgei2i.common.constants.ApplicationErrorCodes;
import com.bridgei2i.common.exception.ApplicationException;
import com.bridgei2i.common.util.ApplicationUtil;
import com.bridgei2i.common.util.CsvReaderWriter;
import com.bridgei2i.common.util.PropertiesUtil;
import com.bridgei2i.common.vo.Roles;
import com.bridgei2i.common.vo.Users;
import com.bridgei2i.form.UtilitiesBean;
import com.bridgei2i.vo.MasterPlanningStatus;
import com.bridgei2i.vo.MasterWorkflowStatus;
import com.bridgei2i.vo.ForecastingASP;
import com.bridgei2i.vo.ForecastingESC;
import com.bridgei2i.vo.ForecastingPM;
import com.bridgei2i.vo.ForecastingUnits;
import com.bridgei2i.vo.NPI;
import com.bridgei2i.vo.SkuList;
import com.bridgei2i.vo.SkuUserMapping;
import com.mysql.jdbc.Statement;

public class UtilitiesDAO {

	private HibernateTemplate hibernateTemplate;
	private static Logger logger = Logger.getLogger(UtilitiesDAO.class);
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	
	public void saveNPI(final String filePath,final String dbTable,final Integer referenceId,final String referenceColumnName,final boolean deleteTableDataBeforeLoad, final int planningCycleId) throws ApplicationException{
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
						 ArrayList<String[]> dataFromExcel=csvReaderWriter.readWriteCSV(filePath, dbTable, deleteTableDataBeforeLoad ,referenceId,referenceColumnName,2);
						 if(dataFromExcel != null){
							 int size=dataFromExcel.size();
							 BigDecimal temp=null;
							 for(int i=0;i<size;i++)
							 {
								 NPI npi=new NPI();
								 String npiId=generateNpiId();
								 String [] nextRecord=dataFromExcel.get(i);
								 if(nextRecord!=null)
								 {
									npi.setNpiId(npiId);
									npi.setRegion(nextRecord[0]);
									if(!ApplicationUtil.isEmptyOrNull(nextRecord[1])){
										npi.setProductNumber(nextRecord[1]);
									}
									npi.setProductDescription(nextRecord[2]);
									npi.setBusiness(nextRecord[3]);
									npi.setProductLine(nextRecord[4]);
									npi.setHierarchy1(nextRecord[5]);
									npi.setHierarchy2(nextRecord[6]);
									npi.setHierarchy4(nextRecord[7]);
									npi.setPlClass(nextRecord[8]);
									npi.setPlScorecard(nextRecord[9]);
									npi.setModel(nextRecord[10]);
									if(!ApplicationUtil.isEmptyOrNull(nextRecord[11])){
										npi.setExpectedAsp(new BigDecimal(nextRecord[11]));
									}
									if(!ApplicationUtil.isEmptyOrNull(nextRecord[12])){
										npi.setExpectedEsc(new BigDecimal(nextRecord[12]));
									}
								 }
								 session.save(npi);
							 }
						 }
						 session.getTransaction().commit();
					} catch (HibernateException he) {
						session.getTransaction().rollback();
						throw he;
					} catch (Exception e) {
						session.getTransaction().rollback();
						logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
						throw new HibernateException(e);
					}
					return true;
				}
			});
		}catch (Exception e) {
			ApplicationException ae = ApplicationException
					.createApplicationException("UtilitiesDAO",
							"saveNPI",
							ApplicationErrorCodes.APP_EC_9,
							ApplicationConstants.EXCEPTION, null);
			throw ae;
		}
		logger.debug("Leaving from saveNPI");
	}

	
	
	public List addNewNPI(final UtilitiesBean utilitiesBean) throws ApplicationException{
		Object object = null;
		logger.debug("Entered into addNewNPI");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException{
					try {
						 Connection connection = session.connection();
						 session.beginTransaction();

						 NPI productDetails=new NPI();
						 productDetails.setNpiId(utilitiesBean.getNpiId());
						 productDetails.setRegion(utilitiesBean.getRegion());
						 productDetails.setProductNumber(utilitiesBean.getProductNumber());
						 productDetails.setProductDescription(utilitiesBean.getProductDescription());
						 productDetails.setProductLine(utilitiesBean.getProductLine());
						 productDetails.setHierarchy1(utilitiesBean.getProductHierarchyI());
						 productDetails.setHierarchy2(utilitiesBean.getProductHierarchyII());
						 productDetails.setHierarchy4(utilitiesBean.getProductHierarchyIV());
						 productDetails.setPlClass(utilitiesBean.getPlClass());
						 productDetails.setPlScorecard(utilitiesBean.getPlScorecard());
						 productDetails.setModel(utilitiesBean.getModel());
						 productDetails.setProductManagerId(utilitiesBean.getProductManagerId());
						 productDetails.setCreatedDate(utilitiesBean.getCreatedDate());
						 productDetails.setUpdateDate(utilitiesBean.getUpdatedDate());
						 if(!ApplicationUtil.isEmptyOrNull(utilitiesBean.getNoMoreNPI())){
							 productDetails.setNoMoreNPI(utilitiesBean.getNoMoreNPI()+"");
						 }
						 productDetails.setBusiness(utilitiesBean.getBusiness());
						 if(utilitiesBean.getExpectedAsp() == null)
 						 {
 							 productDetails.setExpectedAsp(new BigDecimal(0));
 						 }
 						 else
 						 {
 							 productDetails.setExpectedAsp(new BigDecimal(utilitiesBean.getExpectedAsp()));
 						 }
 						 if(utilitiesBean.getExpectedEsc() == null)
 						 {
 						 productDetails.setExpectedEsc(new BigDecimal(0));
 						 }
 						 else
 						 {
 							 productDetails.setExpectedEsc(new BigDecimal(utilitiesBean.getExpectedEsc()));	 
 						 }
 						 session.save(productDetails);
 						 if(utilitiesBean.getProductManagerId()!= null && utilitiesBean.getProductManagerId()>0){
	 						 SkuList skuList = new SkuList();
	 						 skuList.setProductId(utilitiesBean.getNpiId());
							 session.save(skuList);
							 SkuUserMapping skuUserMappingObj = new SkuUserMapping();
	 						 skuUserMappingObj.setSkuListId(skuList.getId());
	 						 skuUserMappingObj.setUserId(utilitiesBean.getProductManagerId());
	 						 skuUserMappingObj.setBusiness(utilitiesBean.getBusiness());
	 						 session.save(skuUserMappingObj);
 						 }
						 session.getTransaction().commit();
					} catch (HibernateException he) {
						session.getTransaction().rollback();
						throw he;
					} catch (Exception e) {
						logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
						session.getTransaction().rollback();
						throw e;
					}
					return true;
				}
			});
		}catch (Exception e) {
			ApplicationException ae = ApplicationException.createApplicationException("UtilitiesDAO","saveNPI", ApplicationErrorCodes.APP_EC_4,ApplicationConstants.EXCEPTION,e);
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			throw ae;
		}
		List npiList=getNpiLog(1);
		logger.debug("Leaving from saveNPI");
		return npiList;
	}

	public void mapProductManager(final List unAssignedSKUList,final String editFlagArray[],final String productManagerIdStr) throws ApplicationException{
		Object object = null;
		logger.debug("Entered into mapProductManager");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException{
					try {
						if(unAssignedSKUList!= null && editFlagArray!= null){
							session.beginTransaction();
							Integer productManagerId =Integer.valueOf(productManagerIdStr);
							int length = editFlagArray.length;
							for(int i=0;i<length;i++){
								Object[] objArry = (Object[])unAssignedSKUList.get(Integer.parseInt(editFlagArray[i]));
								String productNumber = (String)objArry[0];
								String business =  (String)objArry[2];
								Criteria criteria = session.createCriteria(SkuList.class);
								criteria.setProjection(Projections.property("id"));
								criteria.add(Restrictions.eq("productId", productNumber));
								Integer skuListId = Integer.valueOf(criteria.list().get(0).toString());
								SkuUserMapping obj = new SkuUserMapping();
								obj.setSkuListId(skuListId);
								obj.setUserId(productManagerId);
								obj.setBusiness(business);
								session.save(obj);
							}
						}
						session.getTransaction().commit();
					} catch (Exception e) {
						session.getTransaction().rollback();
						throw e;
					}
					return true;
				}
			});
		}catch (Exception e) {
			ApplicationException ae = ApplicationException.createApplicationException("UtilitiesDAO","mapProductManager", ApplicationErrorCodes.APP_EC_10,ApplicationConstants.EXCEPTION,e);
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			throw ae;
		}
		logger.debug("Leaving from mapProductManager");
	}
	
	public void updateProductManager(final List unAssignedSKUList,final String editFlagArray[],final String productManagerIdStr) throws ApplicationException{
		Object object = null;
		logger.debug("Entered into updateProductManager");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException{
					try {
						if(unAssignedSKUList!= null && editFlagArray!= null){
							session.beginTransaction();
							Integer productManagerId =Integer.valueOf(productManagerIdStr);
							int length = editFlagArray.length;
							for(int i=0;i<length;i++){
								Object[] objArry = (Object[])unAssignedSKUList.get(Integer.parseInt(editFlagArray[i]));
								String productNumber = (String)objArry[0];
								Integer skuMappedId = (Integer)objArry[7];
								String sql2="from SkuUserMapping where id="+skuMappedId.toString();
								Query q1=session.createQuery(sql2);
								SkuUserMapping obj =(SkuUserMapping)q1.uniqueResult();
								obj.setUserId(productManagerId);
								session.update(obj);
							}
						}
						session.getTransaction().commit();
					} catch (Exception e) {
						session.getTransaction().rollback();
						throw e;
					}
					return true;
				}
			});
		}catch (Exception e) {
			ApplicationException ae = ApplicationException.createApplicationException("UtilitiesDAO","updateProductManager", ApplicationErrorCodes.APP_EC_10,ApplicationConstants.EXCEPTION,e);
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			throw ae;
		}
		logger.debug("Leaving from updateProductManager");
	}

	public List<Object[]> getUnassignedSku() throws ApplicationException{
		logger.debug("Entered into getUnassignedSku ");
		Object object = null;
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
						String skuListId = PropertiesUtil.getProperty("skuUserMappingskuListId");
						String productId = PropertiesUtil.getProperty("skuListTableproductId");
						String skuListTableId = PropertiesUtil.getProperty("skuListTableId");
						
						Criteria skuUserMappingCriteria = session.createCriteria(SkuUserMapping.class);
						ProjectionList proj = Projections.projectionList();
						proj.add(Projections.property(skuListId));
						skuUserMappingCriteria.setProjection(proj);
						List skuListIdd = skuUserMappingCriteria.list();
						
						Criteria skuListCriteria = session.createCriteria(SkuList.class);
						ProjectionList skuListProj = Projections.projectionList();
						skuListProj.add(Projections.property(productId));
						skuListCriteria.add(Restrictions.not(Restrictions.in(skuListTableId, skuUserMappingCriteria.list())));
						skuListCriteria.setProjection(skuListProj);
						List skuListIdList = skuListCriteria.list();
						return skuListCriteria.list();
						} catch (HibernateException he) {
							session.getTransaction().rollback();
					}
					return true;
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
		}
		logger.debug("Leaving from getUnassignedSku");
		return (List)object;
}

	public List<Object[]> getProductManagerDetails() throws ApplicationException{
		logger.debug("Entered into getProductManagerDetails ");
		Object object = null;
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					try {
						String role = PropertiesUtil.getProperty("role");
						String rolesTableLoginId = PropertiesUtil.getProperty("rolesTableLoginId");
						String firstName = PropertiesUtil.getProperty("firstName");
						String userId = PropertiesUtil.getProperty("userTableLoginId");
						
						Criteria criteria = session.createCriteria(Roles.class);
						criteria.setProjection(Projections.distinct(Projections.property(rolesTableLoginId)));
						criteria.add(Restrictions.eq(role, "PRODUCT_MANAGER"));
						
						Criteria criteria2 = session.createCriteria(Users.class);
						ProjectionList proj = Projections.projectionList();
						proj.add(Projections.property(userId));
						proj.add(Projections.property(firstName));
						proj.add(Projections.property("lastName"));
						proj.add(Projections.property("userName"));
						criteria2.setProjection(proj);
						criteria2.add(Restrictions.in(userId, criteria.list()));
						List a = criteria2.list();
						return criteria2.list();

						} catch (HibernateException he) {
							session.getTransaction().rollback();
					}
					return true;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
		}
		logger.debug("Leaving from getProductManagerDetails");
		return (List)object;
}
	
	public List<Object[]> getUnassignedSkuDetails(final String assignedType,final List rolesList,final Integer userId) throws ApplicationException{		
		logger.debug("Entered into getUnassignedSkuDetails ");
		Object object = null;
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					List unAssignedSkuList = null;
					try {
						String skuListId = PropertiesUtil.getProperty("skuListId");
						String description = PropertiesUtil.getProperty("productDescription");
						String business = PropertiesUtil.getProperty("business");
						String region = PropertiesUtil.getProperty("region");
						String model = PropertiesUtil.getProperty("model");
						String categoryColumn = PropertiesUtil.getProperty(ApplicationConstants.CATEGORY);
						String scorecardRollup = PropertiesUtil.getProperty("scorecardRollup");
						String weekColumn = PropertiesUtil.getProperty(ApplicationConstants.ORDER_WEEK);
						String sql = null;
						ResultSet sqlResultSet=null;
						if(ApplicationUtil.isEmptyOrNull(assignedType) || assignedType.equalsIgnoreCase("Unassigned")){
							unAssignedSkuList = new ArrayList();
							sql = "select distinct "+skuListId+","+description+","+business+","+categoryColumn+","+scorecardRollup+","+model+",'--' from data d join sku_list sl on d."+skuListId+"= sl.productId left join sku_user_mapping sum on sl.id= sum.skuListId and d."+business+" = sum.business"
									+ " where sum.skuListId is null and "+weekColumn+" in (select max("+weekColumn+") from data where "+skuListId+" = d."+skuListId+")";
							Connection con=session.connection();
							sqlResultSet = con.createStatement().executeQuery(sql);
							while (sqlResultSet.next()) {   
								String [] tempResultArray=new String[7];
								tempResultArray[0]=sqlResultSet.getString(1);
								tempResultArray[1]=sqlResultSet.getString(2);
								tempResultArray[2]=sqlResultSet.getString(3);
								tempResultArray[3]=sqlResultSet.getString(4);
								tempResultArray[4]=sqlResultSet.getString(5);
								tempResultArray[5]=sqlResultSet.getString(6);
								tempResultArray[6]=sqlResultSet.getString(7);
								unAssignedSkuList.add(tempResultArray);
							}
							
						}else{
							if(rolesList != null && rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){
								sql = "select distinct d."+skuListId+",d."+description+",sm.business,d."+categoryColumn+",d."+scorecardRollup+",d."+model+",u.firstName,sm.id from Data d,SkuList sl,SkuUserMapping sm,Users u where d."+skuListId+"=sl.productId and sl.id=sm.skuListId and u.login_id = sm.userId and sm.userId="+userId.toString()+""
										+ " and d."+weekColumn+"=(select max(d1."+weekColumn+") from Data d1 where d1."+skuListId+"=sl.productId) ";
							}else{
								sql = "select distinct d."+skuListId+",d."+description+",sm.business,d."+categoryColumn+",d."+scorecardRollup+",d."+model+",u.firstName,sm.id from Data d,SkuList sl,SkuUserMapping sm,Users u where d."+skuListId+"=sl.productId and sl.id=sm.skuListId and u.login_id = sm.userId "
										+ "and d."+weekColumn+"=(select max(d1."+weekColumn+") from Data d1 where d1."+skuListId+"=sl.productId) ";
							}
							Query q = session.createQuery(sql);
							unAssignedSkuList = q.list();
						}
						
						
						return unAssignedSkuList;
					} catch (HibernateException he) {
							he.printStackTrace();
							session.getTransaction().rollback();
					}
					return unAssignedSkuList;
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
		}
		logger.debug("Leaving from getUnassignedSkuDetails");
		return (List <Object[]>)object;


		
	}
	public List<Object[]> getNpiLog(final int logType) throws ApplicationException{
		Object object = null;
		logger.debug("Entered into NPI Log");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException{
					List<Object[]> npiList=null;
					try {
						String sql=null;
						if(logType==1){
							sql = "SELECT npiId,region,productNumber,productDescription,business,productLine,hierarchy1,hierarchy2,hierarchy4,plClass,plScorecard,expectedASP,expectedESC,productManagerId,noMoreNPI,firstname,lastname,model FROM npi a left join"
									+ "(select firstname,lastname,login_id from users)b "
									+ "on a.productManagerId = b.login_Id where a.productNumber='Not Available'";
						}
						else
						{
							sql = "SELECT npiId,region,productNumber,productDescription,business,productLine,hierarchy1,hierarchy2,hierarchy4,plClass,plScorecard,expectedASP,expectedESC,productManagerId,noMoreNPI,firstname,lastname,model FROM npi a left join"
									+ "(select firstname,lastname,login_id from users)b "
									+ "on a.productManagerId = b.login_Id where a.productNumber !='Not Available'";
						}
						Query npiListQuery=session.createSQLQuery(sql);
						npiList = npiListQuery.list();
					} catch (HibernateException he) {
						throw he;
					} catch (Exception e) {
						e.printStackTrace();
						throw new HibernateException(e);
					}
					return npiList;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
		logger.debug("Leaving from saveNPI");
		return (List<Object[]>)object;
	}
	
	public String generateNpiId() throws ApplicationException{
		Object object = null;
		logger.debug("Entered into remove NPI");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException{
					 String NpiId=null;
					try {
						
						 Criteria npiIdCriteria=session.createCriteria(NPI.class);
						 npiIdCriteria.setProjection(Projections.max("npiId"));
						 String npiId=null;
						 if(!ApplicationUtil.isEmptyOrNull(npiIdCriteria.uniqueResult())){
							 npiId=npiIdCriteria.uniqueResult().toString();
						 }
	 					 if(ApplicationUtil.isEmptyOrNull(npiId)){
	 						 NpiId="NPI001";
	 					 }
	 					 else{
	 						 String temp=npiId.substring(4);
							 NpiId=npiId.substring(0, 3)+String.format("%03d",(Integer.parseInt(temp)+1));
	 					 }
					} catch (HibernateException he) {
						throw he;
					} catch (Exception e) {
						e.printStackTrace();
						throw new HibernateException(e);
					}
					return NpiId;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			throw e;
		}
		
		logger.debug("Leaving from removeNPI");
		return (String)object;
	}
	
	public void removeNPI(final String npiId) throws ApplicationException{
		Object object = null;
		logger.debug("Entered into remove NPI");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException{
					try 
					{
						String skuListIdQuery="select id from sku_List where productId ='"+npiId+"'";
						Query skuListIdResult=session.createSQLQuery(skuListIdQuery);
						Object a = skuListIdResult.uniqueResult();
						if(skuListIdResult.uniqueResult() != null)
						{
							session.createSQLQuery("delete from sku_user_mapping  where skuListId = '"+skuListIdResult.uniqueResult()+"'").executeUpdate();
							session.createSQLQuery("delete from sku_List  where productId = '"+npiId+"'").executeUpdate();
						}
						String sql="delete from NPI  where npiId='"+npiId+"'";
						Query npiListQuery=session.createSQLQuery(sql);
						npiListQuery.executeUpdate();
					} catch (HibernateException he) {
						throw he;
					} catch (Exception e) {
						throw e;
					}
					return true;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException ae = ApplicationException
					.createApplicationException("UtilitiesDAO",
							"removeNPI",
							ApplicationErrorCodes.APP_EC_7,
							ApplicationConstants.EXCEPTION, null);
			throw ae;
		}
		logger.debug("Leaving from removeNPI");
	}
	
	public void updateNPI(final UtilitiesBean utilitiesBean,final int updateType) throws ApplicationException{
		Object object = null;
		logger.debug("Entered into Update NPI");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException{
					try {
						session.beginTransaction();
						 NPI productDetails=new NPI();
						 String sql="select npi.id from NPI npi where npi.npiId='"+utilitiesBean.getNpiId()+"'";
						 Query query = session.createQuery(sql);
						 int id = (int)query.uniqueResult();
						 productDetails.setId(id);
						 productDetails.setNpiId(utilitiesBean.getNpiId());
						 productDetails.setRegion(utilitiesBean.getRegion());
						 if(updateType==1){
							 productDetails.setProductNumber(utilitiesBean.getProductNumber());
						 }else{
							 productDetails.setProductNumber(utilitiesBean.getModalProductNumber());
						 }
						 productDetails.setProductDescription(utilitiesBean.getProductDescription());
						 productDetails.setProductLine(utilitiesBean.getProductLine());
						 productDetails.setHierarchy1(utilitiesBean.getProductHierarchyI());
						 productDetails.setHierarchy2(utilitiesBean.getProductHierarchyII());
						 productDetails.setHierarchy4(utilitiesBean.getProductHierarchyIV());
						 productDetails.setPlClass(utilitiesBean.getPlClass());
						 productDetails.setPlScorecard(utilitiesBean.getPlScorecard());
						 productDetails.setProductManagerId(utilitiesBean.getProductManagerId());
						 productDetails.setBusiness(utilitiesBean.getBusiness());
						 productDetails.setModel(utilitiesBean.getModel());
						 if(!ApplicationUtil.isEmptyOrNull(utilitiesBean.getNoMoreNPI())){
							 productDetails.setNoMoreNPI(utilitiesBean.getNoMoreNPI()+"");
						 }
						 if(!ApplicationUtil.isEmptyOrNull(utilitiesBean.getExpectedAsp())){
							 productDetails.setExpectedAsp(new BigDecimal(utilitiesBean.getExpectedAsp()));
						 }
						 if(!ApplicationUtil.isEmptyOrNull(utilitiesBean.getExpectedEsc())){
							 productDetails.setExpectedEsc(new BigDecimal(utilitiesBean.getExpectedEsc()));
						 }
 						 String skuUserMappingQueryString = "";
 						 
 						 if(updateType==1){
	 						 skuUserMappingQueryString = "select skuList.id from SkuList skuList where skuList.productId='"+utilitiesBean.getNpiId()+"'";
	 						 Query skuUserMapping = session.createQuery(skuUserMappingQueryString);
	 						 Object skuTableIdObj = skuUserMapping.uniqueResult();
	 						 if(skuTableIdObj != null){
	 							 int skuTableId = (int)skuTableIdObj;
	 							 String skuUserMappingUpdate ="update Sku_User_Mapping set business='"+utilitiesBean.getBusiness()+"',userId ='"+utilitiesBean.getProductManagerId()+"' where skuListId = '"+skuTableId+"'";
		 						 Query skuUserMappingUpdateQuery = session.createSQLQuery(skuUserMappingUpdate);
		 						 skuUserMappingUpdateQuery.executeUpdate();
	 						 }else{
	 							 SkuList skuList = new SkuList();
		 						 skuList.setProductId(utilitiesBean.getNpiId());
								 session.save(skuList);
								 SkuUserMapping skuUserMappingObj = new SkuUserMapping();
		 						 skuUserMappingObj.setSkuListId(skuList.getId());
		 						 skuUserMappingObj.setUserId(utilitiesBean.getProductManagerId());
		 						 skuUserMappingObj.setBusiness(utilitiesBean.getBusiness());
		 						 session.save(skuUserMappingObj); 
	 						 }
 						 }
						 session.update(productDetails);
						 session.getTransaction().commit();
					} catch (HibernateException he) {
						throw he;
					} catch (Exception e) {
						e.printStackTrace();

						throw e;
					}
					return true;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException ae = ApplicationException
					.createApplicationException("UtilitiesDAO",
							"updateNPI",
							ApplicationErrorCodes.APP_EC_13,
							ApplicationConstants.EXCEPTION, null);
			throw e;
		}
		logger.debug("Leaving from Update Npi");
	}
	
	public void mapSku(final UtilitiesBean utilitiesBean,final int mappingType,final Object[] previousMappedNpi,final int modifiedFlag,final int productNumberModifiedFlag) throws ApplicationException{
		Object object = null;
		logger.debug("Entered into map SKU");
		try{
			object = hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException{
					try {
						session.beginTransaction();
						String productId = utilitiesBean.getProductNumber();
						String updatedProductId = utilitiesBean.getModalProductNumber();
						SkuList skuList = new SkuList();
						SkuUserMapping skuUserMapping = new SkuUserMapping();
						if(mappingType == 1)
						{
							String sql = "select skuList.id from SkuList skuList where skuList.productId='"+utilitiesBean.getNpiId()+"'";
							 Query query = session.createQuery(sql);
							 if(query.uniqueResult() != null)
							 {
								 int id = (int)query.uniqueResult();
								 String q ="INSERT INTO sku_user_mapping (skuListId, userId, business) VALUES ('"+id+"','"+utilitiesBean.getProductManagerId()+"','"+utilitiesBean.getBusiness()+"') "
								 + "ON DUPLICATE KEY UPDATE skuListId = '"+id+"', userId = '"+utilitiesBean.getProductManagerId()+"', business = '"+utilitiesBean.getBusiness()+"'";
								 Query modify = session.createSQLQuery(q);
								 modify.executeUpdate();
							 }
						//currentValues[2] == utilitiesBean.getModalProductNumber() && currentValues[4] == utilitiesBean.getBusiness() && currentValues[13] == utilitiesBean.getProductManagerId()
						}
						else if(mappingType == 2  && utilitiesBean.getNoMoreNPI()== null)
						{
							if(productNumberModifiedFlag == 0)
 							{
								 String sql = "select skuList.id from SkuList skuList where skuList.productId='"+utilitiesBean.getModalProductNumber()+"'";
								 Query query = session.createQuery(sql);
								 if(query.uniqueResult() != null)
								 {
									 int id = (int)query.uniqueResult();
									 session.createSQLQuery("delete from sku_user_mapping where userId ='"+utilitiesBean.getProductManagerId()+"'and business='"+utilitiesBean.getBusiness()+"'and skuListId ='"+id+"'").executeUpdate();
									 try
									 {
										 session.createSQLQuery("delete from sku_List where id ='"+id+"'").executeUpdate();
									 }catch(Exception e)
									 {
										 
									 }
								 }
							}
							else 
							 {
								 if(productNumberModifiedFlag == 1)
								 {
									 String sqlOriginalSKu = "select skuList.id from SkuList skuList where skuList.productId='"+previousMappedNpi[2].toString()+"'";
									 Query queryOriginal = session.createQuery(sqlOriginalSKu);
									 if(queryOriginal.uniqueResult() != null)
									 {
										 int id = (int)queryOriginal.uniqueResult();
										 session.createSQLQuery("delete from sku_user_mapping where userId ='"+utilitiesBean.getProductManagerId()+"'and business='"+utilitiesBean.getBusiness()+"'and skuListId ='"+id+"'").executeUpdate();
										 try
										 {
											 session.createSQLQuery("delete from sku_List where id ='"+id+"'").executeUpdate();
										 }catch(Exception e)
										 {
											 
										 }
									 }
								 }
							 }
							 
							 if(modifiedFlag == 1)
							 {
								 int id = (int) session.createQuery("select skuList.id from SkuList skuList where skuList.productId='"+utilitiesBean.getNpiId()+"'").uniqueResult();
								 String q ="INSERT INTO sku_user_mapping (skuListId, userId, business) VALUES ('"+id+"','"+utilitiesBean.getProductManagerId()+"','"+utilitiesBean.getBusiness()+"') "
									 + "ON DUPLICATE KEY UPDATE skuListId = '"+id+"', userId = '"+utilitiesBean.getProductManagerId()+"', business = '"+utilitiesBean.getBusiness()+"'";
								 Query modify = session.createSQLQuery(q);
								 modify.executeUpdate();
							 }
						}
						else if(mappingType == 2 && utilitiesBean.getNoMoreNPI()!= null &&  utilitiesBean.getNoMoreNPI() == 1)
						{
							if(!ApplicationUtil.isEmptyOrNull(productId))
							{
								if(modifiedFlag == 0)
							 {
									 String sql = "select skuList.id from SkuList skuList where skuList.productId='"+productId+"'";
									 Query query = session.createQuery(sql);
								 if(query.uniqueResult() != null)
									 {
										 int id = (int)query.uniqueResult();
										 skuList.setId(id);
										 skuList.setProductId(utilitiesBean.getModalProductNumber());
										 skuUserMapping.setSkuListId(skuList.getId());
										 skuUserMapping.setUserId(utilitiesBean.getProductManagerId());
										 skuUserMapping.setBusiness(utilitiesBean.getBusiness());
										 session.update(skuList);
										 session.save(skuUserMapping);
									 }
									 else if(query.uniqueResult() == null)
									 {
										 if(productNumberModifiedFlag == 1)
										 {
											 String sqlOriginalSKu = "select skuList.id from SkuList skuList where skuList.productId='"+previousMappedNpi[2].toString()+"'";
											 Query queryOriginal = session.createQuery(sqlOriginalSKu);
											 if(queryOriginal.uniqueResult() != null)
											 {
												 int id = (int)queryOriginal.uniqueResult();
												 session.createSQLQuery("delete from sku_user_mapping where userId ='"+utilitiesBean.getProductManagerId()+"'and business='"+utilitiesBean.getBusiness()+"'and skuListId ='"+id+"'").executeUpdate();
												 try
												 {
													 session.createSQLQuery("delete from sku_List where id ='"+id+"'").executeUpdate();
												 }catch(Exception e)
												 {
												 }
											 }
											 skuList.setProductId(utilitiesBean.getModalProductNumber());
											 session.save(skuList);
											 skuUserMapping.setSkuListId(skuList.getId());
											 skuUserMapping.setUserId(utilitiesBean.getProductManagerId());
											 skuUserMapping.setBusiness(utilitiesBean.getBusiness());
											 session.save(skuUserMapping);
										 }
									 }
								 }
								else
								{ 
									if(productNumberModifiedFlag == 1)
									{
										String sql = "select skuList.id from SkuList skuList where skuList.productId='"+previousMappedNpi[2].toString()+"'";
										 Query query = session.createQuery(sql);
										 if(query.uniqueResult() != null)
										 {
											 int id = (int)query.uniqueResult();
											 session.createSQLQuery("delete from sku_user_mapping where userId ='"+utilitiesBean.getProductManagerId()+"'and business='"+utilitiesBean.getBusiness()+"'and skuListId ='"+id+"'").executeUpdate();
											 try
											 {
												 session.createSQLQuery("delete from sku_List where id ='"+id+"'").executeUpdate();
											 }catch(Exception e)
											 {
											 }
										 }
										 skuList.setProductId(utilitiesBean.getModalProductNumber());
										 session.save(skuList);
										 skuUserMapping.setSkuListId(skuList.getId());
										 skuUserMapping.setUserId(utilitiesBean.getProductManagerId());
										 skuUserMapping.setBusiness(utilitiesBean.getBusiness());
										 session.save(skuUserMapping);
									}
									else
									{
										 skuList.setProductId(utilitiesBean.getModalProductNumber());
										 session.save(skuList);
										 skuUserMapping.setSkuListId(skuList.getId());
										 skuUserMapping.setUserId(utilitiesBean.getProductManagerId());
										 skuUserMapping.setBusiness(utilitiesBean.getBusiness());
										 session.save(skuUserMapping);
									}
								}
							}
						}
						session.getTransaction().commit();
					} catch (HibernateException he) {
						session.getTransaction().rollback();
						throw he;
					} catch (Exception e) {
						session.getTransaction().rollback();
						throw e;
					}
					return true;
				}
			});
		}catch (Exception e) {
			logger.error("Exception Occured :" + e + " at Line no :"+e.getStackTrace()[0].getLineNumber()+" in File: "+e.getStackTrace()[0].getFileName());
			ApplicationException ae = ApplicationException
					.createApplicationException("UtilitiesDAO",
							"mapSku",
							ApplicationErrorCodes.APP_EC_13,
							ApplicationConstants.EXCEPTION, null);
			throw e;
		}
		logger.debug("Leaving from mapSku");
	}
	
	 public Users getUserObjectByEmail(final String emailId)
	    {
	    	logger.debug("Entered into getUserObjectByEmail(..)");
	    	Object object = hibernateTemplate.execute(
			          new HibernateCallback<Object>()
			          {
			                public Object doInHibernate(Session session) throws HibernateException, SQLException
			                {
			                	Users user = null;
			                	try
			                	{
			                		  String sql = "from Users as u where u.emailId = ?";
	                  Query q = session.createQuery(sql).setString(0, StringUtils.trimWhitespace(emailId));
	                  user = (Users)q.uniqueResult();
			                	}
			                	catch(HibernateException he)
			                	{
			                		logger.error("Exception Occured :" + he + " at Line no :"+he.getStackTrace()[0].getLineNumber()+" in File: "+he.getStackTrace()[0].getFileName());
			                		throw he;
			                	}
			        			return user;
			                }
			          }
				);
		      return (Users)object;    
	    }
	 
	 public Object updateUser(final Users user) throws Exception {
			logger.debug("Entered into updateUser(..)");
			Object object = hibernateTemplate
					.execute(new HibernateCallback<Object>() {
						@Override
						public Object doInHibernate(Session session)
								throws HibernateException, SQLException {
							EncryptService epts = EncryptService.getInstance();
							String newPassword = epts.encrypt(user.getPassword());
							user.setPassword(newPassword);
							try {
								session.beginTransaction();
								session.update(user);
								session.getTransaction().commit();

								return true;
							} catch (HibernateException he) {
								logger.error("Exception Occured :" + he + " at Line no :"+he.getStackTrace()[0].getLineNumber()+" in File: "+he.getStackTrace()[0].getFileName());
								throw he;
							}
						}
					});

			return object;
		}
	
	
	public void forecastNewNPIUnits(final String npiId,final int totalForecastWeeks,final List  npiForecastWeek,final int totalWeeks,final int year,final int week,final int planningCycleId ,final String npiBusiness) throws ApplicationException{
				logger.info("Getting inside forecastNewNPIUnits utilitiesDAO method");
				Object object = null;
				object = hibernateTemplate.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						try {
							List<String> forecastingUnitsId = null;	
							Query forecastingUnitsIdQuery = session.createSQLQuery("SELECT max(id) as maxid FROM forecasting_Units where forecastPeriod in(:param) group by forecastPeriod").setParameterList("param",npiForecastWeek);
								forecastingUnitsId = forecastingUnitsIdQuery.list();
								if(!forecastingUnitsId.isEmpty())
							{
									List<Object[]> forecastwithOverridden = null;
									Query query = session.createSQLQuery("select productId,forecastPeriod,business,forecastValue,overrideValue from (select id as foreId,productId,business,forecastPeriod,forecastValue from forecasting_Units d where id IN (:param))f  left join"
											+ "(select id as overrideid,overrideValue,userId,forecastingUnitsId as fid from override_units_log b inner join"
											+ "(SELECT max(id) as maxid FROM forecasting.override_units_log group by forecastingUnitsId)a on  a.maxid = b.id)e on e.fid = f.foreId").setParameterList("param", forecastingUnitsId);
									forecastwithOverridden = query.list();
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
												cycleWeek = (week + i) + "";
											}
											for (Object[] result : forecastwithOverridden)
											{
												session.beginTransaction();
									            ForecastingUnits obj= new ForecastingUnits();
									            obj.setPlanningCycleId(planningCycleId);
									            obj.setModelId("1");
									            obj.setProductId(npiId);
									            obj.setBusiness(npiBusiness);
									            obj.setForecastPeriod(cycleYear+"-W"+cycleWeek);
									            if(result[4]!= null){
									            	obj.setForecastValue( result[4] + "" );	
									            }else{
									            	obj.setForecastValue("0");
									            }
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
										    session.beginTransaction();
										    ForecastingUnits obj= new ForecastingUnits();
										    obj.setPlanningCycleId(planningCycleId);
										    obj.setModelId("1");
										    obj.setProductId(npiId);
										    obj.setBusiness(npiBusiness);
										    obj.setForecastPeriod(cycleYear+"-W"+(cycleWeek));
										    obj.setForecastValue("0");
										    session.save(obj);
										    session.getTransaction().commit();
										}
								}
								} catch (HibernateException he) {
							session.getTransaction().rollback();
							logger.error("Exception Occured :" + he + " at Line no :"+he.getStackTrace()[0].getLineNumber()+" in File: "+he.getStackTrace()[0].getFileName());
						}
						return true;
					}
				}); 
				logger.info(" Leaving from forecastNewNPIUnits utilitiesDAO method");
			}
			
			public void forecastNewNPIASP(final String npiId,final int totalForecastWeeks,final List  npiForecastWeek,final int totalWeeks,final int year,final int week,final int planningCycleId, final String npiBusiness) throws ApplicationException{
				logger.info("Getting inside forecastNewNPIASP utilitiesDAO method");
				Object object = null;
				object = hibernateTemplate.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						try {
								int cycleYear = year;
								String cycleWeek = null;
								session.beginTransaction();								
								Criteria npiASPValue= session.createCriteria(NPI.class);
								ProjectionList npiASPValueProj= Projections.projectionList();
								npiASPValueProj.add(Projections.property("expectedAsp"));
								npiASPValue.add(Restrictions.eq("npiId",npiId));
								npiASPValue.setProjection(npiASPValueProj);
								List  ASPValue = npiASPValue.list();
								if(ASPValue != null && !ASPValue.isEmpty())
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
													cycleWeek = (week + i) + "";
												}
												ForecastingASP obj= new ForecastingASP();
									            obj.setPlanningCycleId(planningCycleId);
									            obj.setModelId("1");
									            obj.setProductId(npiId);
									            obj.setBusiness(npiBusiness);
									            obj.setForecastPeriod(cycleYear+"-W"+cycleWeek);
									            BigDecimal aspValueDecimal = (BigDecimal)ASPValue.get(0);
									            if(aspValueDecimal != null){
									            	obj.setForecastValue(aspValueDecimal.toString());
									            }else{
									            	obj.setForecastValue("0");
									            }
									            session.save(obj);
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
										    session.beginTransaction();
										    ForecastingASP obj= new ForecastingASP();
										    obj.setPlanningCycleId(planningCycleId);
										    obj.setModelId("1");
										    obj.setProductId(npiId);
										    obj.setBusiness(npiBusiness);
										    obj.setForecastPeriod(cycleYear+"-W"+(cycleWeek));
										    obj.setForecastValue("0");
										    session.save(obj);
										    session.getTransaction().commit();
										}
					
								}
								session.getTransaction().commit();
							} catch (HibernateException he) {
							session.getTransaction().rollback();
							logger.error("Exception Occured :" + he + " at Line no :"+he.getStackTrace()[0].getLineNumber()+" in File: "+he.getStackTrace()[0].getFileName());
						}
						return true;
					}
				}); 
				logger.info("Leaving from forecastNewNPIASP utilitiesDAO method");
			}
			public void forecastNewNPIESC(final String npiId,final int totalForecastWeeks,final List  npiForecastWeek,final int totalWeeks,final int year,final int week,final int planningCycleId, final String npiBusiness) throws ApplicationException{
				logger.info("Getting inside forecastNewNPIESC utilitiesDAO method");
				Object object = null;
				object = hibernateTemplate.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						try {
								int cycleYear = year;
								String cycleWeek = null;
								session.beginTransaction();								
								Criteria npiESCValue= session.createCriteria(NPI.class);
								ProjectionList npiESCValueProj= Projections.projectionList();
								npiESCValueProj.add(Projections.property("expectedEsc"));
								npiESCValue.add(Restrictions.eq("npiId",npiId));
								npiESCValue.setProjection(npiESCValueProj);
								List ESCValue = npiESCValue.list();		
								if(ESCValue!=null && !ESCValue.isEmpty())
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
												cycleWeek = (week + i) + "";
												}
												ForecastingESC obj= new ForecastingESC();
									            obj.setPlanningCycleId(planningCycleId);
									            obj.setModelId("1");
									            obj.setProductId(npiId);
									            obj.setBusiness(npiBusiness);
									            obj.setForecastPeriod(cycleYear+"-W"+cycleWeek);
									            BigDecimal escValueDecimal = (BigDecimal)ESCValue.get(0);
									            if(escValueDecimal != null){
									            	obj.setForecastValue(escValueDecimal.toString());
									            }else{
									            	obj.setForecastValue("0");
									            }
									            session.save(obj);
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
										    session.beginTransaction();
										    ForecastingESC obj= new ForecastingESC();
										    obj.setPlanningCycleId(planningCycleId);
										    obj.setModelId("1");
										    obj.setProductId(npiId);
										    obj.setBusiness(npiBusiness);
										    obj.setForecastPeriod(cycleYear+"-W"+(cycleWeek));
										    obj.setForecastValue("0");
										    session.save(obj);
										    session.getTransaction().commit();
										}
								}
								session.getTransaction().commit();
							} catch (HibernateException he) {
							session.getTransaction().rollback();
							logger.error("Exception Occured :" + he + " at Line no :"+he.getStackTrace()[0].getLineNumber()+" in File: "+he.getStackTrace()[0].getFileName());
						}
						return true;
					}
				}); 
				logger.info("Leaving from forecastNewNPIASP utilitiesDAO method");
			}
				public void forecastNewNPIPMPercent(final String npiId,final int totalForecastWeeks,final List  npiForecastWeek,final int totalWeeks,final int year,final int week,final int planningCycleId, final String npiBusiness) throws ApplicationException{
				logger.info("Getting inside forecastNewNPIPMPercent utilitiesDAO method");
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
								ForecastESCData.add(Restrictions.eq("productId",npiId));
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
								ForecastASPData.add(Restrictions.eq("productId",npiId));
								ASPForecastProjection.add(Projections.property("productId"));
								ASPForecastProjection.add(Projections.property("business"));
								ASPForecastProjection.add(Projections.property("forecastPeriod"));
								ASPForecastProjection.add(Projections.property("forecastValue"));
								ForecastASPData.setProjection(ASPForecastProjection);
								List<Object []> ASPForecastResultSet = ForecastASPData.list();
								
								//LinkedHashMap<Integer, List<Object[]>> PMPercentValues = new LinkedHashMap<Integer, List<Object[]>>();
								session.beginTransaction();
								Iterator ESC =  ESCForecastResultSet.iterator();
								Iterator ASP =  ASPForecastResultSet.iterator();
								float PMPercent=0.0f;
								int i=0;
								while (ESC.hasNext() && ASP.hasNext())
								{
									Object[] ASPElement = (Object[]) ASP.next();
									Object[] ESCElement= (Object[]) ESC.next();
									if(ASPElement[3].equals(ESCElement[3]))
									{
										Float aspValue=0.0f;
										if(ASPElement[4] != null){
											aspValue = Float.valueOf(ASPElement[4].toString());
										}
										Float escValue=0.0f;
										if(ASPElement[4] != null){
											escValue = Float.valueOf(ESCElement[4].toString());
										}
										if(aspValue>0){
											PMPercent = ((aspValue - escValue)/aspValue) * 100;
										}
										ForecastingPM obj= new ForecastingPM();
							            obj.setPlanningCycleId(planningCycleId);
							            obj.setModelId("1");//modelID
							            obj.setProductId(ASPElement[1].toString());
							            obj.setBusiness(ASPElement[2].toString());
							            obj.setForecastPeriod(ASPElement[3].toString());
							            obj.setForecastValue(PMPercent+"");
							            session.save(obj);
										i++;
									}
								}
								session.getTransaction().commit();
							} catch (HibernateException he) {
							session.getTransaction().rollback();
							logger.error("Exception Occured :" + he + " at Line no :"+he.getStackTrace()[0].getLineNumber()+" in File: "+he.getStackTrace()[0].getFileName());
						}
						return true;
					}
				}); 
				logger.info("Leaving from forecastNewNPIPMPercent utilitiesDAO method");
				}
			
}


