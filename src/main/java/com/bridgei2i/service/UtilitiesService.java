package com.bridgei2i.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgei2i.common.exception.ApplicationException;
import com.bridgei2i.common.util.ApplicationUtil;
import com.bridgei2i.common.util.PropertiesUtil;
import com.bridgei2i.common.vo.Users;
import com.bridgei2i.common.constants.ApplicationConstants;
import com.bridgei2i.common.controller.LoadApplicationCacheService;
import com.bridgei2i.common.dao.PlanningDAO;
import com.bridgei2i.common.dao.UtilitiesDAO;
import com.bridgei2i.common.exception.ApplicationException;
import com.bridgei2i.common.util.PropertiesUtil;
import com.bridgei2i.form.UtilitiesBean;

@Service("utilitiesService")
public class UtilitiesService {
	
	private static Logger logger = Logger.getLogger(UtilitiesService.class);

	@Autowired(required=true)
	private UtilitiesDAO utilitiesDAO;
	
	@Autowired(required=true)
	private PlanningDAO planningDAO;
	
	public List<Object[]> getNpiLog(int logType) throws ApplicationException {
		return utilitiesDAO.getNpiLog(logType);
	}

	
	public void saveNPI(String filePath, String dbTable , Integer referenceId, String referenceColumnName,boolean deleteTableDataBeforeLoad,Integer planningCycleId ) throws ApplicationException {
		try{
		utilitiesDAO.saveNPI(filePath,dbTable,referenceId,referenceColumnName,deleteTableDataBeforeLoad,planningCycleId);
		}
		 catch (ApplicationException e) {
				throw e;
			}
	}

	
	public String generateNpiId( ) throws ApplicationException {
		try{
		return utilitiesDAO.generateNpiId();
		}
		 catch (ApplicationException e) {
				throw e;
			}
	}
	public List addNewNPI(UtilitiesBean  utilitiesBean ) throws ApplicationException {
		try{
		return utilitiesDAO.addNewNPI(utilitiesBean);
		}
		 catch (ApplicationException e) {
				throw e;
			}
	}
	
	public void mapSkuwithPM(List unAssignedSKUList,String editFlagArray[],String productManagerIdStr) throws ApplicationException {
		try{
		utilitiesDAO.mapProductManager(unAssignedSKUList,editFlagArray,productManagerIdStr);
		}
		 catch (ApplicationException e) {
				throw e;
			}
	}
	
	public void updateProductManager(List unAssignedSKUList,String editFlagArray[],String productManagerIdStr) throws ApplicationException {
		try{
		utilitiesDAO.updateProductManager(unAssignedSKUList,editFlagArray,productManagerIdStr);
		}
		 catch (ApplicationException e) {
				throw e;
			}
	}
	
	public List getNewSku() throws ApplicationException {
		try{
			//List testing = utilitiesDAO.getUnassignedSku();
			return utilitiesDAO.getUnassignedSku();
		}
		 catch (ApplicationException e) {
				throw e;
			}
	}
	public List productManagerDetails() throws ApplicationException {
		try{
			//List testing = utilitiesDAO.getUnassignedSku();
			return utilitiesDAO.getProductManagerDetails();
		}
		 catch (ApplicationException e) {
				throw e;
			}
	}
	
	public List getNewSkuDetails(String assignedType,List rolesList,Integer userId) throws ApplicationException {
		try{
			//List testing = utilitiesDAO.getUnassignedSkuDetails(unassignedSkuList);
			return utilitiesDAO.getUnassignedSkuDetails(assignedType,rolesList,userId);
		}
		 catch (ApplicationException e) {
				throw e;
			}
	}
	public void removeNPI(String  npiId ) throws ApplicationException {
		try{
		utilitiesDAO.removeNPI(npiId);
		}
		 catch (ApplicationException e) {
				throw e;
			}
	}
	
	public void updateNPI(UtilitiesBean  utilitiesBean,int updateType) throws ApplicationException {
		try{
		utilitiesDAO.updateNPI(utilitiesBean,updateType);
		}
		 catch (ApplicationException e) {
				throw e;
			}
	}
	
	public void mapSku(final UtilitiesBean  utilitiesBean,final int mappingType,final Object[] previousMappedNpi , final int modifiedFlag,final int productNumberModifiedFlag) throws ApplicationException {
		try{
			utilitiesDAO.mapSku(utilitiesBean,mappingType,previousMappedNpi,modifiedFlag,productNumberModifiedFlag);
		}
		 catch (ApplicationException e) {
				throw e;
		}
	}
	
	public Users getUserByEmail(String emailId) {

		return utilitiesDAO.getUserObjectByEmail(emailId);
		
	}
	
	public void createPassword(Users user)  {
		String newPassWd = ApplicationUtil.getUniqueKey(8);
			
		try {
			user.setPassword(newPassWd);
			utilitiesDAO.updateUser(user);	
			StringBuffer subject = new StringBuffer();
			subject.append("Welcome to SKU Planner - Password Reset Notification");
			StringBuffer sb = new StringBuffer();

			sb.append((new StringBuilder("Hi")).append(",\n\n").toString());
			sb.append("In response to your request, your SKU Planner password has been reset \n\n\n");
			sb.append("Your account details\n\n\n\n\tUsername : "
					+ user.getUserName()
					+ "\n\tNew Password : "
					+ newPassWd
					+ "\n\n\n\n");
			sb.append("\n\nFor technical support, please contact apps@bridgei2i.com");
			sb.append("\n\nPlease note that this password is case-sensitive, replaces all previous SKU Plannner passwords and must be entered exactly as shown. To avoid mis-keying, we recommend you copy and paste the password onto the Login page.\n\n\nThanks & Regards,"
					+ "\n\nSKU Planning Support Team \nBridgei2i Analytics Solutions Private Limited"
					+ "\n\nFollow us on: https://www.facebook.com/BRIDGEi2i"
					+ "\n\nFollow us on twitter @BRIDGEi2i");

			
				System.out.println("Mail :"+ sb.toString());
				Properties props = new Properties();
				props.put("mail.smtp.auth",
						PropertiesUtil.getProperty("mail.smtp.auth"));
				props.put("mail.smtp.starttls.enable",
						PropertiesUtil.getProperty("mail.smtp.starttls.enable"));
				props.put("mail.smtp.host",
						PropertiesUtil.getProperty("mail.smtp.host"));
				props.put("mail.smtp.port",
						PropertiesUtil.getProperty("mail.smtp.port"));

				String adminEmail = PropertiesUtil.getProperty("admin_email");
				String adminPassword = PropertiesUtil
						.getProperty("admin_password");
				ApplicationUtil.sendEMail(user.getEmailId(),
						subject.toString(), sb.toString(), adminEmail,
						adminPassword, props,
						PropertiesUtil.getProperty("APP_EC_101"));
				user.setPassword(newPassWd);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	
	public void updatePassword(Users user) {
		try{
		utilitiesDAO.updateUser(user);

		Properties props = new Properties();
		props.put("mail.smtp.auth",
				PropertiesUtil.getProperty("mail.smtp.auth"));
		props.put("mail.smtp.starttls.enable",
				PropertiesUtil.getProperty("mail.smtp.starttls.enable"));
		props.put("mail.smtp.host",
				PropertiesUtil.getProperty("mail.smtp.host"));
		props.put("mail.smtp.port",
				PropertiesUtil.getProperty("mail.smtp.port"));

		String adminEmail = PropertiesUtil.getProperty("admin_email");
		String adminPassword = PropertiesUtil.getProperty("admin_password");
		StringBuffer sb = new StringBuffer();
		sb.append((new StringBuilder("Dear ")).append(user.getFirstName())
				.append(",\n\n").toString());

		sb.append("In response to your request, Your SKU Planner password has been changed successfully.\n\n");
		sb.append("Thanks and Regards,\n\n");
		sb.append("SKU Planning Support Team\n");
		sb.append("apps@bridgei2i.com\n\n");
		sb.append("Follow us on: https://www.facebook.com/BRIDGEi2i");

			ApplicationUtil.sendEMail(user.getEmailId(),
					"Your  Password Changed", sb.toString(), adminEmail,
					adminPassword, props,
					PropertiesUtil.getProperty("APP_EC_101"));
		} catch (Exception e) {
		}
	}
	
	
}
