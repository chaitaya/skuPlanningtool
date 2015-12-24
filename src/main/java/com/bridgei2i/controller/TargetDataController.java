package com.bridgei2i.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
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
import com.bridgei2i.form.TargetDataBean;
import com.bridgei2i.form.UploadDataBean;
import com.bridgei2i.service.PlanningService;


@Controller
@Scope("session")

public class TargetDataController {
	private static Logger logger = Logger.getLogger(TargetDataController.class);
	@Autowired(required=true)
	private PlanningService planningService;
	
	@RequestMapping(value = "/dataUpload.htm")
	public ModelAndView getTargetDataDetails(
			@ModelAttribute("TargetDataBean") TargetDataBean targetDataBean,
			BindingResult result, HttpServletRequest request) {
			logger.debug("Entering and leaving from getTargetDataDetails");
			ApplicationUtil.setObjectInSession(ApplicationConstants.ACTIVE_UTILITIES_TAB, ApplicationConstants.TARGET_UTILITIES_TAB, request);
			ApplicationUtil.setObjectInSession("headerFrom", "utilities", request);
			return new ModelAndView("targetData", "model",targetDataBean);
			
	}
	
	
	
	@RequestMapping(value = "/targetDataProcess.htm",method=RequestMethod.POST)
	public ModelAndView uploadDataDetails(
			@ModelAttribute("TargetDataBean") TargetDataBean targetDataBean,
			BindingResult result, HttpServletRequest request) throws ApplicationException {
		logger.debug("Entering into targetData");
			MultipartFile Data = targetDataBean.getData();
			String tableName="target_data";
			
			if (Data != null && Data.getSize() > 0) {
				InputStream inputStream = null;
				OutputStream outputStream = null;
				ServletContext sc = request.getSession().getServletContext();
				String filePath = sc.getRealPath("/WEB-INF");
				String dataFileName = Data.getOriginalFilename();
				
				try {
					inputStream = Data.getInputStream();

					String newFilePath = filePath + "\\TargetData";

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
					
					
					planningService.saveTargetData(dataFile+"", tableName,null,null,false);
									   
				    System.out.println(dataFile);
				    targetDataBean.setUploadSuccessFlag(1);
				    ApplicationException applicationException = ApplicationException.createApplicationException(TargetDataController.class.toString(), "targetData", 
					ApplicationErrorCodes.APP_EC_27, ApplicationConstants.INFORMATION, null);
				} 
				catch(Exception e)
				{
					logger.error(e.getMessage());
					e.printStackTrace();
					ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e, request);
					ApplicationException applicationException = ApplicationException.createApplicationException(TargetDataController.class.toString(), "targetData", 
					ApplicationErrorCodes.APP_EC_28, ApplicationConstants.EXCEPTION, e);
					ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, applicationException, request);	
				}
			}
				logger.debug("Leaving from targetDataDetails");
				return new ModelAndView("targetData", "model", targetDataBean);
	}
	
	@RequestMapping(value = "/downloadSampleData.htm")
    public HttpEntity<byte[]> downloadTargetData(@ModelAttribute("targetDataBean") TargetDataBean targetDataBean,
    		HttpServletRequest request) throws ApplicationException, IOException {
    	logger.debug("Entered into downloadTargetData");
		ServletContext sc = request.getSession().getServletContext(); 
        String bridgei2iTemplateFilePathStr = sc.getRealPath("/WEB-INF");
        File destinationFolderPathFile = new File(bridgei2iTemplateFilePathStr);
        if (!destinationFolderPathFile.exists()) {
               destinationFolderPathFile.mkdir();
        }
        String csvFileName= "targetData.csv";
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
         logger.debug("Leaving from targetDataDownload");
         return new HttpEntity<byte[]>(excelbytes,headers);		
	
    	
    }
    
//For BTB Data
    
    @RequestMapping(value = "/btbDataProcess.htm",method=RequestMethod.POST)
	public ModelAndView uploadBTBDataDetails(
			@ModelAttribute("TargetDataBean") TargetDataBean targetDataBean,
			BindingResult result, HttpServletRequest request) throws ApplicationException {
		logger.debug("Entering into BTB Data");
			MultipartFile Data = targetDataBean.getBtbData();
			String tableName="btbdata";
			
			if (Data != null && Data.getSize() > 0) {
				InputStream inputStream = null;
				OutputStream outputStream = null;
				ServletContext sc = request.getSession().getServletContext();
				String filePath = sc.getRealPath("/WEB-INF");
				String dataFileName = Data.getOriginalFilename();
				
				try {
					inputStream = Data.getInputStream();

					String newFilePath = filePath + "\\BTBData";

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
					
					
					planningService.saveBtbData(dataFile+"", tableName,null,null,false);
									   
				    System.out.println(dataFile);
				    targetDataBean.setUploadSuccessFlag(1);
				    
				} 
				catch(Exception e)
				{
					logger.error(e.getMessage());
					e.printStackTrace();
					ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e, request);
					ApplicationException applicationException = ApplicationException.createApplicationException(TargetDataController.class.toString(), "targetData", 
					ApplicationErrorCodes.APP_EC_2, ApplicationConstants.EXCEPTION, e);
					ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, applicationException, request);	
				}
			}
			
				logger.debug("Leaving from BTB Data");
				return new ModelAndView("targetData", "model", targetDataBean);
	}
    
    //DownLoad BTB Data
    
    @RequestMapping(value = "/downloadBtbData.htm")
    public HttpEntity<byte[]> downloadBtbData(@ModelAttribute("targetDataBean") TargetDataBean targetDataBean,
    		HttpServletRequest request) throws ApplicationException, IOException {
    	logger.debug("Entered into btbDataDownload");
		ServletContext sc = request.getSession().getServletContext(); 
        String bridgei2iTemplateFilePathStr = sc.getRealPath("/WEB-INF");
        File destinationFolderPathFile = new File(bridgei2iTemplateFilePathStr);
        if (!destinationFolderPathFile.exists()) {
               destinationFolderPathFile.mkdir();
        }
        String csvFileName= "BTB_Sample.csv";
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
         logger.debug("Leaving from btbDataDownload");
         return new HttpEntity<byte[]>(excelbytes,headers);		
	
    	
    }
    

    
      //Upload Event Calendar
    @RequestMapping(value = "/uploadEventCalendar.htm",method=RequestMethod.POST)
	public ModelAndView uploadEventCalendar(
			@ModelAttribute("TargetDataBean") TargetDataBean targetDataBean,
			BindingResult result, HttpServletRequest request) throws ApplicationException {
		logger.debug("Entering into Upload Event Calender Data");
			MultipartFile Data = targetDataBean.getEventData();
			String tableName="master_event_calender";
			
			if (Data != null && Data.getSize() > 0) {
				InputStream inputStream = null;
				OutputStream outputStream = null;
				ServletContext sc = request.getSession().getServletContext();
				String filePath = sc.getRealPath("/WEB-INF");
				String dataFileName = Data.getOriginalFilename();
				
				try {
					inputStream = Data.getInputStream();

					String newFilePath = filePath + "\\MasterEventCalendar";

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
					
					
					planningService.uploadEventCalendar(dataFile+"", tableName,null,null,false);
									   
				    System.out.println(dataFile);
				    targetDataBean.setUploadSuccessFlag(1);
				    
				} 
				catch(Exception e)
				{
					logger.error(e.getMessage());
					e.printStackTrace();
					ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e, request);
					ApplicationException applicationException = ApplicationException.createApplicationException(TargetDataController.class.toString(), "targetData", 
					ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
					ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, applicationException, request);	
				}
			}
				logger.debug("Leaving from Upload Event Calender Data");
				return new ModelAndView("targetData", "model", targetDataBean);
	}

    //DownLoad Sample Event Calendar
    
    @RequestMapping(value = "/downloadSampleEventCalendar.htm")
    public HttpEntity<byte[]> downloadSampleEventCalender(@ModelAttribute("targetDataBean") TargetDataBean targetDataBean,
    		HttpServletRequest request) throws ApplicationException, IOException {
    	logger.debug("Entered into downloadTargetData");
		ServletContext sc = request.getSession().getServletContext(); 
        String bridgei2iTemplateFilePathStr = sc.getRealPath("/WEB-INF");
        File destinationFolderPathFile = new File(bridgei2iTemplateFilePathStr);
        if (!destinationFolderPathFile.exists()) {
               destinationFolderPathFile.mkdir();
        }
        String csvFileName= "Sample_Event_Calendar.csv";
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
         logger.debug("Leaving from targetDataDownload");
         return new HttpEntity<byte[]>(excelbytes,headers);		
	
    	
    }
    
	

}
