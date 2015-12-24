package com.bridgei2i.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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
import com.bridgei2i.common.controller.AutomaticUploadScheduler;
import com.bridgei2i.common.exception.ApplicationException;
import com.bridgei2i.common.util.ApplicationUtil;
import com.bridgei2i.common.vo.Users;
import com.bridgei2i.form.UploadDataBean;
import com.bridgei2i.service.PlanningService;


@Controller
@SessionAttributes("uploadDataBean")
@Scope("session")
public class UploadDataController {
	private static Logger logger = Logger.getLogger(UploadDataController.class);
	@Autowired(required=true)
	private PlanningService planningService;

	
	@RequestMapping(value = "/uploadData.htm",method=RequestMethod.POST)
	public ModelAndView getUploadDataDetails(
			@ModelAttribute("UploadDataBean") UploadDataBean uploadDataBean,
			BindingResult result, HttpServletRequest request) {
			logger.debug("Entering and leaving from getUploadDataDetails");
			Integer planningCycleId = uploadDataBean.getPlanningCycleId();
			System.out.println(planningCycleId);
			uploadDataBean.setUploadSuccessFlag(0);
			return new ModelAndView("uploadData", "model",uploadDataBean);
			
	} /* closing the getUploadDataDetails method*/
	
	@RequestMapping(value = "/onclick.htm")
	public ModelAndView getOnClickDetails(
			@ModelAttribute("UploadDataBean") UploadDataBean uploadDataBean,
			BindingResult result, HttpServletRequest request) {
			return new ModelAndView("uploadData", "model",uploadDataBean);
}

	@RequestMapping(value = "/overrideFun.htm")
	public void overridefun(HttpServletRequest request) {
		
		logger.debug("Entered into overridefun method");
		int Categoryflag=1;//Flag to indicate whether overriding is for Category / Model
		int aspFlag = 1;//Flag to indicate whether to calculate override for asp/Units. aspFlag = 0 will calculate for Units
		String comment = "Test Comment";// Comment that Model or Category manager entered
		List<Object[]> param= new ArrayList <Object[]>();//Parameter that I receive from UI
		int planningCycleId = 14;
		Object[] value = new Object[4];	 
		value[0]="Monitor-LCD";//Category
		value[1]="2015-W24";//OverriddenWeek
		value[2]="1000";//Overridden Value
		value[3]="Flash Drive";//Model Name
		
		Object[] value2 = new Object[4];
		value2[0]="Accy";
		value2[1]="2015-W24";
		value2[2]="2000";
		value2[3]="Flash Drive";
		
		param.add(value);
		param.add(value2);
		
		Users users = (Users) ApplicationUtil.getObjectFromSession(ApplicationConstants.APPLICATION_LOGGED_IN_USER, request);
		//planningService.overridingForecastUnits(Categoryflag,users.getLogin_id(),param,comment,aspFlag,planningCycleId,null,null,null);
		logger.debug("Leaving from overridefun method");
		
}
	
	@RequestMapping(value = "/runForecast.htm",method=RequestMethod.POST)
	public ModelAndView homePage(
			@ModelAttribute("UploadDataBean") UploadDataBean uploadDataBean,
			BindingResult result, HttpServletRequest request) {
		logger.debug("Entered into run forecast page");
		ModelAndView modelAndView = new ModelAndView("redirect:homePage.htm");
		int planningCycleId = 0;
		int year =0;
		int week =0;
		Integer pcId =uploadDataBean.getPlanningCycleId();
		String startYear =uploadDataBean.getStartYear();
		String startWeek = uploadDataBean.getStartWeek();
		if(!ApplicationUtil.isEmptyOrNull(pcId)){
		planningCycleId = pcId.intValue();
		}
		
		if(!ApplicationUtil.isEmptyOrNull(startYear)){
			year =Integer.parseInt(startYear);
		}
		if(!ApplicationUtil.isEmptyOrNull(startWeek)){
			week = Integer.parseInt(startWeek);
		}
		
		try {
			planningService.baseForecast(year,week,planningCycleId);
		} catch (ApplicationException e) {
			modelAndView = new ModelAndView("uploadData", "model",uploadDataBean);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.debug("Leaving from run forecast page");
		return modelAndView;
	}
	
	
	@RequestMapping(value = "/uploadDataProcess.htm",method=RequestMethod.POST)
	public ModelAndView uploadDataDetails(
			@ModelAttribute("UploadDataBean") UploadDataBean uploadDataBean,
			BindingResult result, HttpServletRequest request) throws ApplicationException {
		logger.debug("Entering into uploadData");
		Integer planningCycleId = uploadDataBean.getPlanningCycleId();
		System.out.println(planningCycleId);
			MultipartFile Data = uploadDataBean.getData();
			
			String tableName="raw_data";
		    
			
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
					
					planningService.flushTables(planningCycleId);
					planningService.saveData(dataFile+"", tableName,null,null,false,planningCycleId);
					tableName="variablenames";
				    planningService.saveVariableNames(dataFile+"", tableName,null,"variableName,columnName",false);
				    planningService.loadAggregatedDataIntoTable(planningCycleId);
				    planningService.flushDuplicateRowsFromData(planningCycleId);
				    planningService.outlierTreatment(planningCycleId);
				    planningService.loadNPIIntoDataTable(planningCycleId);
				    System.out.println("loadNPISuccess");
				    planningService.updateSKUList(planningCycleId);
				    System.out.println(dataFile);
				    uploadDataBean.setUploadSuccessFlag(1);
				    
				} 
				catch(Exception e)
				{
					logger.error(e.getMessage());
					ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e, request);
					ApplicationException applicationException = ApplicationException.createApplicationException(UploadDataController.class.toString(), "uploadData", 
					ApplicationErrorCodes.APP_DEFAULT_EXCEPTION_NUMBER, ApplicationConstants.EXCEPTION, e);
					ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, applicationException, request);	
				}
			}
			
				logger.debug("Leaving from uploaDataDetails");
				return new ModelAndView("uploadData", "model", uploadDataBean);
	}
	
	//--------automatic upload
			/*@RequestMapping(value = "/FTPscheduler.htm")
			public ModelAndView FTPscheduler(
					@ModelAttribute("PlanningLogBean") PlanningLogBean planningLogBean,
					BindingResult result, HttpServletRequest request)
					throws ApplicationException {

				 String SFTPHOST = "175.41.131.113";
			        int    SFTPPORT = 22;
			        String SFTPUSER = "hpsku";
			        String SFTPPASS = "Sku&&123hp";
			        String SFTPWORKINGDIR = "/home/suresh/ftp/virtual/hpsku/";
			        final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(csv))$)";
			        Session     session     = null;
			        Channel     channel     = null;
			        ChannelSftp channelSftp = null;
			         
			        try{
			            JSch jsch = new JSch();
			            session = jsch.getSession(SFTPUSER,SFTPHOST,SFTPPORT);
			            session.setPassword(SFTPPASS);
			            java.util.Properties config = new java.util.Properties();
			            config.put("StrictHostKeyChecking", "no");
			            session.setConfig(config);
			            session.connect();
			            channel = session.openChannel("sftp");
			            channel.connect();
			            channelSftp = (ChannelSftp)channel;
			            channelSftp.cd(SFTPWORKINGDIR);
			            Vector filelist = channelSftp.ls(SFTPWORKINGDIR);
			            String remotePath=channelSftp.realpath(SFTPWORKINGDIR);
			            Pattern pattern = Pattern.compile(IMAGE_PATTERN);
			            Matcher matcher;
			            for(int i=0; i<filelist.size();i++){
			            	 ChannelSftp.LsEntry le=(ChannelSftp.LsEntry) filelist.get(i);
				             
				             if (le.getAttrs().isDir()) {
				            	 continue;
				               }
				            else {
				            	 if (le.getFilename().startsWith(".")) {
				            	        continue;
				            	  }
				            	 else{
				            		 matcher = pattern.matcher(le.getFilename());
				            		 if (matcher.matches()) {
				            			 String dataFile=remotePath+"/"+le.getFilename();
					            	       System.out.println(dataFile);
					            	       planningService.saveData(dataFile+"", "raw_data",null,null,false,4);
					            	  }
				            		
				            	 }
				               }
			            }
			             
			        }catch(Exception ex){
			            ex.printStackTrace();
			        }
				return new ModelAndView("planningLog", "model",planningLogBean);
			}*/
	
			/*	@RequestMapping(value = "/SFTPscheduler.htm")
			public ModelAndView FTPscheduler(
					@ModelAttribute("UploadDataBean") PlanningLogBean planningLogBean,
					BindingResult result, HttpServletRequest request)
					throws ApplicationException {

				 String SFTPHOST = "175.41.131.113";
			        int    SFTPPORT = 22;
			        String SFTPUSER = "hpsku";
			        String SFTPPASS = "Sku&&123hp";
			        String SFTPWORKINGDIR = "/home/suresh/ftp/virtual/hpsku/";
			        final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(csv))$)";
			        Session     session     = null;
			        Channel     channel     = null;
			        ChannelSftp channelSftp = null;
			         
			        try{
			            JSch jsch = new JSch();
			            session = jsch.getSession(SFTPUSER,SFTPHOST,SFTPPORT);
			            session.setPassword(SFTPPASS);
			            java.util.Properties config = new java.util.Properties();
			            config.put("StrictHostKeyChecking", "no");
			            session.setConfig(config);
			            session.connect();
			            channel = session.openChannel("sftp");
			            channel.connect();
			            channelSftp = (ChannelSftp)channel;
			            channelSftp.cd(SFTPWORKINGDIR);
			            Vector filelist = channelSftp.ls(SFTPWORKINGDIR);
			            String remotePath=channelSftp.realpath(SFTPWORKINGDIR);
			            Pattern pattern = Pattern.compile(IMAGE_PATTERN);
			            Matcher matcher;
			            for(int i=0; i<filelist.size();i++){
			            	 ChannelSftp.LsEntry le=(ChannelSftp.LsEntry) filelist.get(i);
				             
				             if (le.getAttrs().isDir()) {
				            	 continue;
				               }
				            else {
				            	 if (le.getFilename().startsWith(".")) {
				            	        continue;
				            	  }
				            	 else{
				            		 matcher = pattern.matcher(le.getFilename());
				            		 if (matcher.matches()) {
				            			 String dataFile=remotePath+"/"+le.getFilename();
					            	       System.out.println(dataFile);
					            	       planningService.saveData(dataFile+"", "raw_data",null,null,false,4);
					            	  }
				            		
				            	 }
				               }
			            }
			             
			        }catch(Exception ex){
			            ex.printStackTrace();
			        }
				return new ModelAndView("planningLog", "model",planningLogBean);
			}
	*/
			@RequestMapping(value = "/FTPscheduler.htm")
			public ModelAndView FTPscheduler(
					@ModelAttribute("UploadDataBean") UploadDataBean uploadDataBean,
					BindingResult result, HttpServletRequest request)throws ApplicationException {
					if(AutomaticUploadScheduler.isAutoUploadInProgress==0){
						planningService.automaticUpload();
					}
					return new ModelAndView("redirect:homePage.htm");
			}

}
