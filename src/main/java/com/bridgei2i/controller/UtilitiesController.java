package com.bridgei2i.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.bridgei2i.common.constants.ApplicationConstants;
import com.bridgei2i.common.constants.ApplicationErrorCodes;
import com.bridgei2i.common.exception.ApplicationException;
import com.bridgei2i.common.util.ApplicationUtil;
import com.bridgei2i.common.vo.Users;
import com.bridgei2i.form.TargetDataBean;
import com.bridgei2i.form.UtilitiesBean;
import com.bridgei2i.service.PlanningService;
import com.bridgei2i.service.UtilitiesService;
import com.bridgei2i.vo.PlanningCycle;


@Controller
@SessionAttributes("utilitiesBean")
@Scope("session")
public class UtilitiesController {
	private static Logger logger = Logger.getLogger(UtilitiesController.class);
	@Autowired(required=true)
	private UtilitiesService utilitiesService;
	
	@Autowired(required=true)
	private PlanningService planningService;
	@RequestMapping(value = "/newProductIntroduction.htm")
	public ModelAndView getNewProductIntroduction(
			@ModelAttribute("UtilitiesBean") UtilitiesBean utilitiesBean,
			BindingResult result, HttpServletRequest request) throws ApplicationException {
				List<Object[]> npiList=null;
			try {
				ApplicationUtil.setObjectInSession(ApplicationConstants.ACTIVE_UTILITIES_TAB, ApplicationConstants.NPI_UTILITIES_TAB, request);
				 /*String NpiId=utilitiesService.generateNpiId();
				 utilitiesBean.setNpiId(NpiId);*/
				npiList=utilitiesService.getNpiLog(1);
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			utilitiesBean.setNpiList(npiList);
	    	logger.debug("Entering and leaving from add new NPI");
			return new ModelAndView("newProductIntroduction", "model",utilitiesBean);
	}
	
	@RequestMapping(value = "/newSku.htm")
	public ModelAndView getNewSku(
			@ModelAttribute("newBean") UtilitiesBean utilitiesBean,
			BindingResult result, HttpServletRequest request,ModelMap map) throws ApplicationException {
				List<String[]> newSku = null;
				List<Object[]> productManagerList = null;
				List<Object[]> skuDetails=null;
				Users users = (Users) ApplicationUtil.getObjectFromSession(ApplicationConstants.APPLICATION_ACT_AS_USER, request);
				List rolesList = users.getRolesList();
				Integer userId = users.getLogin_id();
			try {
				ApplicationUtil.setObjectInSession(ApplicationConstants.ACTIVE_UTILITIES_TAB, ApplicationConstants.NEW_SKU_UTILITIES_TAB, request);
				productManagerList = utilitiesService.productManagerDetails();
				//newSku = utilitiesService.getNewSku();
				String assignedType = utilitiesBean.getAssignedType();
				skuDetails = utilitiesService.getNewSkuDetails(assignedType,rolesList,userId);
			} catch (ApplicationException e) {
								
			}
			utilitiesBean.setNewSku(skuDetails);
			utilitiesBean.setProductManagerList(productManagerList);
			map.addAttribute("utilitiesBean",utilitiesBean);
	    	logger.debug("Entering and leaving from NewSku");
			return new ModelAndView("newSKU", "model",utilitiesBean);
	}
	
	@RequestMapping(value = "/mapSKUwithPM.htm")
	public ModelAndView mapSKUwithPM(
			@ModelAttribute("utilitiesBean") UtilitiesBean utilitiesBean,
			BindingResult result, HttpServletRequest request) throws ApplicationException {
			try {
				String editFlagArray[] = utilitiesBean.getEditFlagArray();
				List unAssignedSKUList = utilitiesBean.getNewSku();
				String productManagerIdStr = utilitiesBean.getProductManager();
				String assignedType = utilitiesBean.getAssignedType();
				if(!ApplicationUtil.isEmptyOrNull(assignedType) && assignedType.equalsIgnoreCase("Assigned")){
					utilitiesService.updateProductManager(unAssignedSKUList, editFlagArray, productManagerIdStr);
				}else{
					utilitiesService.mapSkuwithPM(unAssignedSKUList,editFlagArray,productManagerIdStr);
				}
				Users users = (Users) ApplicationUtil.getObjectFromSession(ApplicationConstants.APPLICATION_ACT_AS_USER, request);
				List rolesList = users.getRolesList();
				Integer userId = users.getLogin_id();
				List skuDetails = utilitiesService.getNewSkuDetails(assignedType,rolesList,userId);
				utilitiesBean.setNewSku(skuDetails);
				ApplicationException ae = ApplicationException
						.createApplicationException("UtilitiesController",
								"mapSKUwithPM",
								ApplicationErrorCodes.APP_EC_11,
								ApplicationConstants.INFORMATION, null);
			 ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, ae, request);
			} catch (ApplicationException e) {
				ApplicationUtil.setObjectInSession(
						ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e,
						request);
				
			}
			utilitiesBean.setEditFlagArray(null);
			logger.debug("Entering and leaving from mapSKUwithPM");
			return new ModelAndView("newSKU", "model",utilitiesBean);
	}
	
	@RequestMapping(value = "/uploadNPI.htm",method=RequestMethod.POST)
	public ModelAndView uploadNPI(
			@ModelAttribute("UtilitiesBean") UtilitiesBean utilitiesBean,
			BindingResult result, HttpServletRequest request) throws ApplicationException {
		logger.debug("Entering into uploadData");
		//int planningCycleId = utilitiesBean.getPlanningCycleId();
		int planningCycleId=4;
		System.out.println(planningCycleId);
			MultipartFile Data = utilitiesBean.getData();
			System.out.println(Data);
			String npi=utilitiesBean.getNpiId();
			System.out.println("npi"+npi);
			String tableName="npi";
			System.out.println("Inside NPI");
			
			if (Data != null && Data.getSize() > 0) {
				InputStream inputStream = null;
				OutputStream outputStream = null;
				ServletContext sc = request.getSession().getServletContext();
				String filePath = sc.getRealPath("/WEB-INF");
				String dataFileName = Data.getOriginalFilename();
				
				try {
					inputStream = Data.getInputStream();

					String newFilePath = filePath + "\\uploadedData";

					File file = new File(newFilePath);
					if (!file.exists()) {
						if (file.mkdir()) {
							System.out.println("Directory is created!");
						} else {
							System.out.println("Directory Already exists!");
						}
					}

					File dataFile = new File(newFilePath + "\\"
							+ dataFileName);
					if (!dataFile.exists()) {
						dataFile.createNewFile();
					}

					outputStream = new FileOutputStream(dataFile);
					int read = 0;
					byte[] bytes = new byte[(int) Data.getSize()];
					while ((read = inputStream.read(bytes)) != -1) {
						outputStream.write(bytes, 0, read);
					}
					
					
					utilitiesService.saveNPI(dataFile+"", tableName,null,null,false,planningCycleId);
					List npiList=utilitiesService.getNpiLog(1);
					utilitiesBean.setNpiList(npiList);
					ApplicationException ae = ApplicationException
							.createApplicationException("UtilitiesController",
									"uploadNPI",
									ApplicationErrorCodes.APP_EC_8,
									ApplicationConstants.INFORMATION, null);
				 ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, ae, request);
				} 
				catch(Exception e)
				{
					ApplicationUtil.setObjectInSession(
							ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e,
							request);
					e.printStackTrace();
					
				}
			}
				logger.debug("Leaving from uploaDataDetails");
				return new ModelAndView("newProductIntroduction", "model", utilitiesBean);
	}
	
	@RequestMapping(value = "/addNewNPI.htm",method=RequestMethod.POST)
	public ModelAndView getNewNPI(
			@ModelAttribute("UtilitiesBean") UtilitiesBean utilitiesBean,
			BindingResult result, HttpServletRequest request) throws ApplicationException {
			System.out.println("Inside add new npi");
			try {
				int week = 0;
				int year = 0;
				int planningCycleId = 0;
				String npiBusiness = "";
				String NpiId=utilitiesService.generateNpiId();
				npiBusiness = utilitiesBean.getBusiness();
				utilitiesBean.setNpiId(NpiId);
 				List npiList=utilitiesService.addNewNPI(utilitiesBean);
 				utilitiesBean.setNpiList(npiList);
				/*List planningCycleList  = planningService.getAllPlanningCycleValue();
				PlanningCycle planningCycle = (PlanningCycle)planningCycleList.get(1);
				week = Integer.valueOf(planningCycle.getStartWeek());
				year = Integer.valueOf(planningCycle.getStartYear());
				planningCycleId = planningCycle.getId();*/
				//utilitiesService.newNPIForecasting(NpiId,week,year,planningCycleId,npiBusiness);
 				ApplicationException ae = ApplicationException
						.createApplicationException("UtilitiesController",
								"addNewNPI",
								ApplicationErrorCodes.APP_EC_5,
								ApplicationConstants.INFORMATION, null);
			 ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, ae, request);
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationUtil.setObjectInSession(
						ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e,
						request);
			}
			
	    	logger.debug("Entering and leaving from add new NPI");
			return new ModelAndView("newProductIntroduction", "model",utilitiesBean);
	}
	
	
	@RequestMapping(value = "/removeNPI.htm")
	public ModelAndView getTest(
			@ModelAttribute("UtilitiesBean") UtilitiesBean utilitiesBean,
			BindingResult result, HttpServletRequest request) throws ApplicationException {
			try {
				String npiId=utilitiesBean.getNpiId();
				utilitiesService.removeNPI(npiId);
				System.out.println("npiId"+npiId);
				List npiList=utilitiesService.getNpiLog(1);
				utilitiesBean.setNpiList(npiList);
				ApplicationException ae = ApplicationException
						.createApplicationException("UtilitiesController",
								"removeNPI",
								ApplicationErrorCodes.APP_EC_6,
								ApplicationConstants.INFORMATION, null);
			ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, ae, request);
			} catch (ApplicationException e) {
				e.printStackTrace();
				ApplicationUtil.setObjectInSession(
						ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e,
						request);
			}
	    	logger.debug("Entering and leaving from add new NPI");
			return new ModelAndView("newProductIntroduction", "model",utilitiesBean);
	}
	
	@RequestMapping(value = "/updateNPI.htm")
	public ModelAndView updateNPI(
			@ModelAttribute("UtilitiesBean") UtilitiesBean utilitiesBean,
			BindingResult result, HttpServletRequest request) throws ApplicationException {
			try {
				System.out.println("npiID"+utilitiesBean.getNpiId());
				utilitiesService.updateNPI(utilitiesBean,1);
				List npiList=utilitiesService.getNpiLog(1);
				utilitiesBean.setNpiList(npiList);
				ApplicationException ae = ApplicationException
						.createApplicationException("UtilitiesController",
								"updateNPI",
								ApplicationErrorCodes.APP_EC_12,
								ApplicationConstants.INFORMATION, null);
			ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, ae, request);
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationUtil.setObjectInSession(
						ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e,
						request);
			}
	    	logger.debug("Entering and leaving from add new NPI");
			return new ModelAndView("newProductIntroduction", "model",utilitiesBean);
	}
	
	@RequestMapping(value = "/mapSku.htm")
	public ModelAndView mapSku(
			@ModelAttribute("UtilitiesBean") UtilitiesBean utilitiesBean,
			BindingResult result, HttpServletRequest request) throws ApplicationException {
			try {
				utilitiesService.updateNPI(utilitiesBean,1);
				utilitiesService.mapSku(utilitiesBean,1,null,0,0);
				List npiList=utilitiesService.getNpiLog(1);
				utilitiesBean.setNpiList(npiList);
				ApplicationException ae = ApplicationException
						.createApplicationException("UtilitiesController",
								"mapSKU",
								ApplicationErrorCodes.APP_EC_12,
								ApplicationConstants.INFORMATION, null);
			ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, ae, request);
			} catch (ApplicationException e) {
				e.printStackTrace();
				ApplicationUtil.setObjectInSession(
						ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e,
						request);
			}
	    	logger.debug("Entering and leaving from add new NPI");
			return new ModelAndView("newProductIntroduction", "model",utilitiesBean);
	}
	
	@RequestMapping(value = "/mappedNPI.htm")
	public ModelAndView getMappedNPI(
			@ModelAttribute("UtilitiesBean") UtilitiesBean utilitiesBean,
			BindingResult result, HttpServletRequest request) throws ApplicationException {
			List<Object[]> npiList=null;
			try {
				npiList=utilitiesService.getNpiLog(2);
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ApplicationUtil.setObjectInSession(ApplicationConstants.PREVIOUS_MAPPED_NPI_VALUE,npiList, request);
	    	utilitiesBean.setNpiList(npiList);
	    	logger.debug("Entering and leaving from add new NPI");
			return new ModelAndView("mappedNPI", "model",utilitiesBean);
	}
	
	@RequestMapping(value = "/updateMappedNPI.htm")
	public ModelAndView updateMappedNPI(
			@ModelAttribute("UtilitiesBean") UtilitiesBean utilitiesBean,
			BindingResult result, HttpServletRequest request) throws ApplicationException {
			try {
				List previousMappedNpi = (List)ApplicationUtil.getObjectFromSession(ApplicationConstants.PREVIOUS_MAPPED_NPI_VALUE, request);
				int modifiedFlag = 0;
				int productNumberModifiedFlag = 0;
				Object[] currentValues = (Object[])previousMappedNpi.get(utilitiesBean.getSelectedValues());
				if(currentValues[14] == utilitiesBean.getNoMoreNPI() && currentValues[2].toString().equalsIgnoreCase(utilitiesBean.getModalProductNumber()) && currentValues[4].toString().equalsIgnoreCase(utilitiesBean.getBusiness()) && currentValues[13].toString().equalsIgnoreCase(utilitiesBean.getProductManagerId()+""))
				{
					modifiedFlag = 0;
					utilitiesService.mapSku(utilitiesBean,2,currentValues,modifiedFlag,productNumberModifiedFlag);
					utilitiesService.updateNPI(utilitiesBean,2);
				}
				else
				{
					modifiedFlag = 1;
					if(!currentValues[2].toString().equalsIgnoreCase(utilitiesBean.getModalProductNumber()))
					{
						productNumberModifiedFlag = 1;
					}
					utilitiesService.mapSku(utilitiesBean,2,currentValues,modifiedFlag,productNumberModifiedFlag);
					utilitiesService.updateNPI(utilitiesBean,2);
				}
 				List npiList=utilitiesService.getNpiLog(2);
				ApplicationUtil.setObjectInSession(ApplicationConstants.PREVIOUS_MAPPED_NPI_VALUE,npiList, request);
				utilitiesBean.setNpiList(npiList);
				ApplicationException ae = ApplicationException
						.createApplicationException("UtilitiesController",
								"updateMappedNPI",
								ApplicationErrorCodes.APP_EC_12,
								ApplicationConstants.INFORMATION, null);
			ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, ae, request);
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				ApplicationUtil.setObjectInSession(
						ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e,
						request);
				e.printStackTrace();
			}
			
			logger.debug("Entering and leaving from updateMappedNPI");
			return new ModelAndView("mappedNPI", "model",utilitiesBean);
	}
	
	@RequestMapping(value = "/downloadNPIData.htm")
    public HttpEntity<byte[]> downloadNPIData(@ModelAttribute("UtilitiesBean") UtilitiesBean utilitiesBean,
    		HttpServletRequest request) throws ApplicationException, IOException {
    	logger.debug("Entered into downloadNPIData");
		ServletContext sc = request.getSession().getServletContext(); 
        String bridgei2iTemplateFilePathStr = sc.getRealPath("/WEB-INF");
        File destinationFolderPathFile = new File(bridgei2iTemplateFilePathStr);
        if (!destinationFolderPathFile.exists()) {
               destinationFolderPathFile.mkdir();
        }
        String csvFileName= "NPIData.csv";
        File f = new File(bridgei2iTemplateFilePathStr+File.separator+csvFileName);
        
		 HttpHeaders headers = new HttpHeaders();
         f = new File(bridgei2iTemplateFilePathStr+File.separator+csvFileName);
         String mimeType = request.getSession().getServletContext().getMimeType(csvFileName);
         headers.setContentLength((int)f.length());
         headers.set("Content-Disposition", "attachment;filename=\""+csvFileName+"\"");
         headers.setContentType(new MediaType("application", ".csv"));
         byte excelbytes[] =null;
         FileInputStream in = null;
         try {
                in = new FileInputStream(f);
                excelbytes = new byte[(int)f.length()];
                in.read(excelbytes);
         } catch (FileNotFoundException e) {
      	   logger.error(e.getMessage());
         } catch (IOException e) {
                logger.error(e.getMessage());
         }finally{
        	 if(in != null){
        		 in.close();
        	 }
         }
         logger.debug("Leaving from NPIDataDownload");
         return new HttpEntity<byte[]>(excelbytes,headers);		
	
    	
    }
}

