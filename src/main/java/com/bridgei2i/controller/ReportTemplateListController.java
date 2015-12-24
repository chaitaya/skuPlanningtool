package com.bridgei2i.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bridgei2i.common.constants.ApplicationConstants;
import com.bridgei2i.common.constants.ApplicationErrorCodes;
import com.bridgei2i.common.controller.LoadApplicationCacheService;
import com.bridgei2i.common.dao.ApplicationDAO;
import com.bridgei2i.common.exception.ApplicationException;
import com.bridgei2i.common.util.ApplicationUtil;
import com.bridgei2i.common.util.PropertiesUtil;
import com.bridgei2i.common.vo.Roles;
import com.bridgei2i.common.vo.Users;
import com.bridgei2i.common.vo.DropDownDisplayVo;
import com.bridgei2i.form.ReportTemplateListBean;
import com.bridgei2i.service.ApplicationService;
import com.bridgei2i.vo.Comments;
import com.bridgei2i.common.vo.Data;
import com.bridgei2i.vo.DistributionList;
import com.bridgei2i.vo.Employee;
import com.bridgei2i.vo.MasterDistributionType;
import com.bridgei2i.vo.MasterReportStatus;
import com.bridgei2i.vo.ReportMetadata;
import com.bridgei2i.vo.TemplateChart;
import com.bridgei2i.vo.TemplateChartCategory;
import com.bridgei2i.vo.TemplateChartInfo;
import com.bridgei2i.vo.TemplateChartReportAssign;

@Controller
@SessionAttributes("reportTemplateListBean")
@Scope("session")

public class ReportTemplateListController {
	
	private static Logger logger = Logger.getLogger(ReportTemplateListController.class);

	@Autowired(required=true)
	private ApplicationService applicationService;
	
/*	@RequestMapping(value = "/reportTemplateList.htm")
	public ModelAndView getReportTemplateListDetails(
			HttpServletRequest request) {
		logger.debug("Entering into getReportTemplateListDetails");
		ReportTemplateListBean reportTemplateListBean = null;
		Users users = (Users)ApplicationUtil.getObjectFromSession(ApplicationConstants.APPLICATION_LOGGED_IN_USER, request);
		Integer userId = users.getLogin_id();
		String emailId = users.getEmployeeId();
		List templateChartList =null;
		try {
			List rolesList = users.getRolesList();
			boolean filterTemplates = true;
			if(rolesList != null && (rolesList.contains("BI2I_ADMIN") || rolesList.contains("HR_ADMIN") )){
				filterTemplates=false;
			}
			templateChartList = applicationService.getTemplateCharts(userId,emailId,filterTemplates);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		reportTemplateListBean = new ReportTemplateListBean();
		reportTemplateListBean.setDetailValueObjs(templateChartList);
		ApplicationUtil.setObjectInSession("reportTemplateListBean", reportTemplateListBean, request);
		logger.debug("Leaving from getReportTemplateListDetails");
		return new ModelAndView("reportTemplateList", "model", reportTemplateListBean);
	}
	
	@RequestMapping(value = "/newReportTemplate.htm")
	public ModelAndView createTemplate(
			 HttpServletRequest request) {
		logger.debug("Entering into createTemplate");
		ReportTemplateBean reportTemplateBean = null;
		try {
			List masterDistributionHierarchyList = (List) LoadApplicationCacheService.applicationCacheObject
					.get(ApplicationConstants.MASTER_DISTRIBUTION_TYPES_HIERARCHY_CACHE_KEY);
			reportTemplateBean= new ReportTemplateBean();
			reportTemplateBean.setDistributionListObj(masterDistributionHierarchyList);
			ApplicationUtil.setObjectInSession("reportTemplateBean", reportTemplateBean, request);
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		logger.debug("Leaving from createTemplate");
		return new ModelAndView("designReportTemplate", "model", reportTemplateBean);
	}
	
	@RequestMapping(value = "/deleteTemplate.htm")
	public ModelAndView deleteTemplate(
			@ModelAttribute("reportTemplateListBean") ReportTemplateListBean reportTemplateListBean,
			BindingResult result, HttpServletRequest request) {
		logger.debug("Entering into deleteTemplate");
		ArrayList reportvalueObjects = (ArrayList)reportTemplateListBean.getDetailValueObjs();
		Users users = (Users)ApplicationUtil.getObjectFromSession(ApplicationConstants.APPLICATION_LOGGED_IN_USER, request);
		Integer userId = users.getLogin_id();
		String emailId = users.getEmailId();
		String editFlagArray[] = reportTemplateListBean.getEditFlagArray();
		List deletedTemplateChartList = new ArrayList();
		if(editFlagArray != null && editFlagArray.length>0){
			for(int i=0;i<editFlagArray.length;i++){
				TemplateChart templateChart = (TemplateChart)reportvalueObjects.get(Integer.parseInt(editFlagArray[i]));
				System.out.println(templateChart);
				deletedTemplateChartList.add(templateChart);
			}
		}
		if(deletedTemplateChartList != null && deletedTemplateChartList.size()>0){
			try {
				applicationService.deleteDesignReportTemplate(deletedTemplateChartList);
				List templateChartList = applicationService.getTemplateCharts(userId,emailId,false);
				reportTemplateListBean.setDetailValueObjs(templateChartList);
				ApplicationException applicationException = ApplicationException.createApplicationException(ReportTemplateListController.class.toString(), "deleteTemplate", 
						ApplicationErrorCodes.APP_EC_14, ApplicationConstants.INFORMATION, null);
				ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, applicationException, request);
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
				ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e, request);
			}
		}
		reportTemplateListBean.setEditFlagArray(null);
		logger.debug("Leaving from deleteTemplate");
		return new ModelAndView("reportTemplateList", "model", reportTemplateListBean);
}
	
	@RequestMapping(value = "/editTemplate.htm")
	public ModelAndView editTemplate(
			@ModelAttribute("reportTemplateListBean") ReportTemplateListBean reportTemplateListBean,
			BindingResult result, HttpServletRequest request) {
		logger.debug("Entering into editTemplate");
		ReportTemplateBean reportTemplateBean = null;
		try {
			ArrayList reportvalueObjects = (ArrayList)reportTemplateListBean.getDetailValueObjs();
			TemplateChart templateChart = null;
			String editFlagArray[] = reportTemplateListBean.getEditFlagArray();
			if(editFlagArray != null && editFlagArray.length>0){
				System.out.println(editFlagArray[0]);
					templateChart = (TemplateChart)reportvalueObjects.get(Integer.parseInt(editFlagArray[0]));
					templateChart = (TemplateChart)templateChart.clone();
			}
			
			ApplicationUtil.loadTemplateChartTypes(templateChart);
			reportTemplateBean= new ReportTemplateBean();
			String reportType = templateChart.getReportType();
			if(!ApplicationUtil.isEmptyOrNull(reportType) && reportType.equals("N")){
				List masterDistributionHierarchyList = (List) LoadApplicationCacheService.applicationCacheObject
						.get(ApplicationConstants.MASTER_DISTRIBUTION_TYPES_HIERARCHY_CACHE_KEY);
				reportTemplateBean.setDistributionListObj(masterDistributionHierarchyList);
			}else{
				List masterDistributionNonHierarchyList = (List) LoadApplicationCacheService.applicationCacheObject
						.get(ApplicationConstants.MASTER_DISTRIBUTION_TYPES_NON_HIERARCHY_CACHE_KEY);
				reportTemplateBean.setDistributionListObj(masterDistributionNonHierarchyList);
			}
			reportTemplateBean.setEditFlagArray(editFlagArray);
			Map codeBookMapObj =(Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.CODEBOOK_ACTUAL_RECODE_VALUES_CACHE_KEY);
			if(templateChart != null){
				//filter1 
				String filter1Id = templateChart.getFilter1();
				
				List filterValues1 = new ArrayList();
				if(!ApplicationUtil.isEmptyOrNull(filter1Id) && !filter1Id.equals("-1")){
					Map selectedVariableCodeBookMapObj  = (Map)codeBookMapObj.get(filter1Id);
					List dataList = applicationService.getDateForSelectedVariable(filter1Id);
					if(dataList!=null){
						int len = dataList.size();
						for(int i=0;i<len;i++){
							String value =  (String)dataList.get(i);
							String recodeValue = value;
							if(selectedVariableCodeBookMapObj != null){
								recodeValue = (String)selectedVariableCodeBookMapObj.get(value);
								if(ApplicationUtil.isEmptyOrNull(recodeValue)){
									recodeValue = value;
								}
							}
							DropDownDisplayVo downDisplayVo = new DropDownDisplayVo();
							downDisplayVo.setDisplayName(recodeValue);
							downDisplayVo.setValue(value);
							filterValues1.add(downDisplayVo);
						}
					}
				}
				reportTemplateBean.setFilterValues1(filterValues1);
				
				//filter2
				String filter2Id = templateChart.getFilter2();
				List filterValues2 = new ArrayList();
				if(!ApplicationUtil.isEmptyOrNull(filter2Id) && !filter2Id.equals("-1")){
					Map selectedVariableCodeBookMapObj  = (Map)codeBookMapObj.get(filter2Id);
					List dataList = applicationService.getDateForSelectedVariable(filter2Id);
					if(dataList!=null){
						int len = dataList.size();
						for(int i=0;i<len;i++){
							String value =  (String)dataList.get(i);
							String recodeValue = value;
							if(selectedVariableCodeBookMapObj != null){
								recodeValue = (String)selectedVariableCodeBookMapObj.get(value);
								if(ApplicationUtil.isEmptyOrNull(recodeValue)){
									recodeValue = value;
								}
							}
							DropDownDisplayVo downDisplayVo = new DropDownDisplayVo();
							downDisplayVo.setDisplayName(recodeValue);
							downDisplayVo.setValue(value);
							filterValues2.add(downDisplayVo);
						}
					}
				}
				reportTemplateBean.setFilterValues2(filterValues2);
				
				//filter3
				String filter3Id = templateChart.getFilter3();
				List filterValues3 = new ArrayList();
				if(!ApplicationUtil.isEmptyOrNull(filter3Id) && !filter3Id.equals("-1")){
					Map selectedVariableCodeBookMapObj  = (Map)codeBookMapObj.get(filter3Id);
					List dataList = applicationService.getDateForSelectedVariable(filter3Id);
					if(dataList!=null){
						int len = dataList.size();
						for(int i=0;i<len;i++){
							String value =  (String)dataList.get(i);
							String recodeValue = value;
							if(selectedVariableCodeBookMapObj != null){
								recodeValue = (String)selectedVariableCodeBookMapObj.get(value);
								if(ApplicationUtil.isEmptyOrNull(recodeValue)){
									recodeValue = value;
								}
							}
							DropDownDisplayVo downDisplayVo = new DropDownDisplayVo();
							downDisplayVo.setDisplayName(recodeValue);
							downDisplayVo.setValue(value);
							filterValues3.add(downDisplayVo);
						}
					}
				}
				reportTemplateBean.setFilterValues3(filterValues3);
				
				List templateChartReportAssigns = templateChart.getTemplateChartReportAssigns();
				if(templateChartReportAssigns != null){
					String str="";
					int size = templateChartReportAssigns.size();
					for(int i=0;i<size;i++){
						TemplateChartReportAssign templateChartReportAssign = (TemplateChartReportAssign)templateChartReportAssigns.get(i);
						Integer masterDistId = templateChartReportAssign.getMasterDistId();
						
						if(masterDistId !=null){
							str=str+masterDistId.toString();
						}
						
						if(i+1<size){
							str=str+",";
						}
					}
					reportTemplateBean.setSelectedDistributionIdStr(str);
				}
			}
	
			if(templateChart.getId() != null){
				MasterReportStatus masterReportStatus =  applicationService.getMasterReportOpenStatus("OPEN");
				if(masterReportStatus != null){
					templateChart.setStatusId(masterReportStatus.getId());
				}
			}
			reportTemplateBean.setTemplateChart(templateChart);
			ApplicationUtil.setObjectInSession("reportTemplateBean", reportTemplateBean, request);
			
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		logger.debug("Leaving from editTemplate");
		return new ModelAndView("designReportTemplate", "model", reportTemplateBean);
	}
	
	@RequestMapping(value = "/copyTemplate.htm")
	public ModelAndView copyTemplate(
			@ModelAttribute("reportTemplateListBean") ReportTemplateListBean reportTemplateListBean,
			BindingResult result, HttpServletRequest request) {
		logger.debug("Entering into copyTemplate");
		try {
			ArrayList reportvalueObjects = (ArrayList)reportTemplateListBean.getDetailValueObjs();
			TemplateChart templateChart = null;
			String editFlagArray[] = reportTemplateListBean.getEditFlagArray();
			String selectedDistributionIds[] = reportTemplateListBean.getEditFlagArray();
			String reportTitle = request.getParameter("reportTitle");
			if(editFlagArray != null && editFlagArray.length>0){
				System.out.println(editFlagArray[0]);
					templateChart = (TemplateChart)reportvalueObjects.get(Integer.parseInt(editFlagArray[0]));
					templateChart = (TemplateChart)templateChart.clone();
			}
		         	templateChart.setId(null);
					templateChart.setReportTitle(reportTitle);
						
					if(templateChart.getId() == null){
						MasterReportStatus masterReportStatus =  applicationService.getMasterReportOpenStatus("OPEN");
						if(masterReportStatus != null){
							templateChart.setStatusId(masterReportStatus.getId());
						}
					}
					
		         	List templatechartCategoriesList  = templateChart.getTemplateChartCategories();
		         	if(templatechartCategoriesList!=null){
	                	for(int i=0;i<templatechartCategoriesList.size();i++){
	                    	TemplateChartCategory templateChartCategory =(TemplateChartCategory) templatechartCategoriesList.get(i);
	                    	templateChartCategory.setId(null);
	                    	
	                    	List templatechartInfoList  = templateChartCategory.getTemplateChartInfoList();
	                		if(templatechartInfoList!=null){
		                    	List templatechartInfoList1 = new ArrayList();
		                       	 for(int j=0;j<templatechartInfoList.size();j++){
		                       		TemplateChartInfo templateChartInfo=(TemplateChartInfo) templatechartInfoList.get(j);
		                       		templateChartInfo.setId(null);
		                       	 }
	                		}
	                		
	                       	List templateChartReportAssigns =  templateChart.getTemplateChartReportAssigns();
	                       	if(templateChartReportAssigns!=null){
		                        int len = templateChartReportAssigns.size();
		               	 	     for(int j=0;j<len;j++){
			               	 		TemplateChartReportAssign templateChartReportAssign =  (TemplateChartReportAssign)templateChartReportAssigns.get(j);
			               	 		templateChartReportAssign.setId(null);
		               	 		}
	                       	}
	                    }
	                	templateChart.setTemplateChartCategories(templatechartCategoriesList);
	                	applicationService.saveDesignReportTemplate(templateChart,null);
		         	}
					TemplateChart updatedTemplateChart = applicationService.getTemplateChart(templateChart.getId());
					
					List templateChartList = (List)reportTemplateListBean.getDetailValueObjs();
					if(templateChartList==null){
						templateChartList = new ArrayList();
					}
					templateChartList.add(updatedTemplateChart);
					
			ApplicationUtil.setObjectInSession("reportTemplateListBean", reportTemplateListBean, request);
			ApplicationException applicationException = ApplicationException.createApplicationException(ReportTemplateListController.class.toString(), "copyTemplate", 
					ApplicationErrorCodes.APP_EC_16, ApplicationConstants.INFORMATION, null);
			ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, applicationException, request);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			ApplicationException applicationException = ApplicationException.createApplicationException(ReportTemplateListController.class.toString(), "copyTemplate", 
					ApplicationErrorCodes.APP_EC_17, ApplicationConstants.EXCEPTION, null);
			ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, applicationException, request);
		}
		logger.debug("Leaving into copyTemplate");
		return new ModelAndView("reportTemplateList", "model", reportTemplateListBean);
	}
	
	@RequestMapping(value = "/previewTemplate.htm")
	public ModelAndView previewTemplate(
			@ModelAttribute("reportTemplateListBean") ReportTemplateListBean reportTemplateListBean,
			BindingResult result, HttpServletRequest request) throws ApplicationException {
		logger.debug("Entering into previewTemplate");
		ReportTemplateBean reportTemplateBean = null;
		TemplateChart templateChart = null;
		String empId=null;
		Users users= (Users)ApplicationUtil.getObjectFromSession(ApplicationConstants.APPLICATION_LOGGED_IN_USER,request);
		try{
			ArrayList reportvalueObjects = (ArrayList)reportTemplateListBean.getDetailValueObjs();
			String editFlagArray[] = reportTemplateListBean.getEditFlagArray();
			if(editFlagArray != null && editFlagArray.length>0){
				templateChart = (TemplateChart)reportvalueObjects.get(Integer.parseInt(editFlagArray[0]));
				String isFromPreview = reportTemplateListBean.getIsFromPreview();
				if(!ApplicationUtil.isEmptyOrNull(isFromPreview) && isFromPreview.equals("false")){
					templateChart = applicationService.getDashboardTemplateChartPreview(templateChart, users.getEmployeeId(),users.getEmployeeId());
					empId = users.getEmployeeId();
				}else{
					templateChart = applicationService.getDashboardTemplateChartPreview(templateChart,null,null);
					List list = templateChart.getDistributionList();
					if(list != null && list.size()>0){
						DropDownDisplayVo displayVo = (DropDownDisplayVo)list.get(0);
						empId = displayVo.getValue();
					}
				}
				templateChart = (TemplateChart)templateChart.clone();
				ApplicationUtil.setObjectInSession("previewTemplateObject", templateChart, request);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
			ApplicationException applicationException = ApplicationException.createApplicationException(ReportTemplateListController.class.toString(), "previewTemplate", 
					ApplicationErrorCodes.APP_EC_24, ApplicationConstants.EXCEPTION, null);
			ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, applicationException, request);
		}
		reportTemplateBean= new ReportTemplateBean();
		reportTemplateBean.setSelectedDistributionListId(empId);
		reportTemplateBean.setSelectedReportMetaDataListId(templateChart.getReportMetaDataId());
		reportTemplateBean.setTemplateChart(templateChart);
		reportTemplateListBean.setEditFlagArray(null);
		ApplicationUtil.setObjectInSession("reportTemplateBean", reportTemplateBean, request);
			logger.debug("Leaving from previewTemplate");
		return new ModelAndView("dashboard", "model", reportTemplateBean);
	}
	
	@RequestMapping(value = "/refreshReportTemplate.htm")
	public ModelAndView refreshReportTemplate(
			@ModelAttribute("reportTemplateListBean") ReportTemplateListBean reportTemplateListBean,
			BindingResult result, HttpServletRequest request) {
		logger.debug("Entering into refreshReportTemplate");
		String index=request.getParameter("index");
		ReportTemplateBean reportTemplateBean = null;
		reportTemplateBean = new ReportTemplateBean();
		try {
			ArrayList reportvalueObjects = (ArrayList)reportTemplateListBean.getDetailValueObjs();
			Users users = (Users)ApplicationUtil.getObjectFromSession(ApplicationConstants.APPLICATION_LOGGED_IN_USER, request);
			Integer userId = users.getLogin_id();
			String emailId = users.getEmailId();
			TemplateChart templateChart = null;
			String editFlagArray[] = reportTemplateListBean.getEditFlagArray();
			if(editFlagArray != null && editFlagArray.length>0){
				
				for(int i=0;i<editFlagArray.length;i++){
					System.out.println(editFlagArray[i]);
					templateChart = (TemplateChart)reportvalueObjects.get(Integer.parseInt(editFlagArray[i]));
					List templateChartReportAssigns = templateChart.getTemplateChartReportAssigns();
						if(templateChartReportAssigns==null || templateChartReportAssigns.size()==0){
							List reportMetaDataList = applicationService.getReportMetaData(templateChart.getId());
							int size = reportMetaDataList.size();
							for(int k=0;k<size;k++){
								ReportMetadata reportMetaData = (ReportMetadata) reportMetaDataList.get(k);
								templateChart.setReportMetaDataId(reportMetaData.getId());
								templateChart.setFilter1(reportMetaData.getFilterColumn1());
								templateChart.setFilterValues1(reportMetaData.getFilterValue1());
								templateChart.setFilter2(reportMetaData.getFilterColumn2());
								templateChart.setFilterValues2(reportMetaData.getFilterValue2());
								templateChart.setFilter3(reportMetaData.getFilterColumn3());
								templateChart.setFilterValues3(reportMetaData.getFilterValue3());
								templateChart.setIsOverall("Y");
								applicationService.refreshReports(templateChart);
							}
							if(templateChart.getId() != null){
								MasterReportStatus masterReportStatus =  applicationService.getMasterReportOpenStatus("OPEN");
								if(masterReportStatus != null){
									templateChart.setStatusId(masterReportStatus.getId());
									templateChart.setFilter1("-1");
									templateChart.setFilterValues1(null);
									templateChart.setFilter2("-1");
									templateChart.setFilterValues2(null);
									templateChart.setFilter3("-1");
									templateChart.setFilterValues3(null);
									applicationService.saveDesignReportTemplate(templateChart, null);
								}
							}
						} else {
							applicationService.refreshReports(templateChart);
							if(templateChart.getId() != null){
								MasterReportStatus masterReportStatus =  applicationService.getMasterReportOpenStatus("OPEN");
								if(masterReportStatus != null){
									templateChart.setStatusId(masterReportStatus.getId());
									applicationService.saveDesignReportTemplate(templateChart, null);
								}
							}
							
						}
						
				}
			}
			List templateChartList = applicationService.getTemplateCharts(userId,emailId,false);
			reportTemplateListBean = new ReportTemplateListBean();
			reportTemplateListBean.setDetailValueObjs(templateChartList);
			reportTemplateListBean.setIndex(index);
			ApplicationUtil.setObjectInSession("reportTemplateListBean", reportTemplateListBean, request);
			ApplicationException applicationException = ApplicationException.createApplicationException(ReportTemplateListController.class.toString(), "refreshReportTemplate", 
					ApplicationErrorCodes.APP_EC_7, ApplicationConstants.INFORMATION, null);
			ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, applicationException, request);
			
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e, request);
		}
		logger.debug("Leaving from refreshReportTemplate");
		return new ModelAndView("reportTemplateList", "model", reportTemplateListBean);
	}
	
	@RequestMapping(value = "/generateStandardReport.htm")
	public ModelAndView generateStandardReport(
			@ModelAttribute("reportTemplateListBean") ReportTemplateListBean reportTemplateListBean,
			BindingResult result, HttpServletRequest request) throws ApplicationException {
		logger.debug("Entering into generateStandardReport");
		ReportTemplateBean reportTemplateBean = null;
		TemplateChart templateChart = null;
		Users users= (Users)ApplicationUtil.getObjectFromSession(ApplicationConstants.APPLICATION_LOGGED_IN_USER,request);
		try{
			ArrayList reportvalueObjects = (ArrayList)reportTemplateListBean.getDetailValueObjs();
			String editFlagArray[] = reportTemplateListBean.getEditFlagArray();
			if(editFlagArray != null && editFlagArray.length>0){
				templateChart = (TemplateChart)reportvalueObjects.get(Integer.parseInt(editFlagArray[0]));
				String isOverall = templateChart.getIsOverall();
				List templateChartReportAssigns = templateChart.getTemplateChartReportAssigns();
				if(!ApplicationUtil.isEmptyOrNull(isOverall)){
					if(isOverall.equalsIgnoreCase("N")){
						List distributionList = applicationService.getDistributionList(templateChart.getId());
						if(distributionList != null && distributionList.size()>0){
							for(int i=0;i<distributionList.size();i++){
								Employee employee = (Employee)distributionList.get(i);
								String employeeName = employee.getName();
								String lastName  = employee.getLastname();
								if(!ApplicationUtil.isEmptyOrNull(lastName)){
									employeeName = employeeName+" "+lastName;
								}
								String empId = employee.getEmployeeId();
								String employeeId = employee.getEmployeeId();
								templateChart = applicationService.getDashboardTemplateChart(templateChart, empId,null,null);
								List managerByLocationList = applicationService.getManagerByLocation(empId);
								templateChart.setManagerByLocationList(managerByLocationList);
								String id = templateChart.getId().toString();
								String reportTitle = templateChart.getReportTitle();
								ServletContext sc = request.getSession().getServletContext(); 
						        String bridgei2iTemplatePptFilePathStr = sc.getRealPath("/WEB-INF");
						        String sourceFile = bridgei2iTemplatePptFilePathStr + "/bridgei2i_template.docx";
						        String destinationFileStr = bridgei2iTemplatePptFilePathStr + "/StandardReport";
						        File destinationFolderPathFile = new File(destinationFileStr);
						        if (!destinationFolderPathFile.exists()) {
						               destinationFolderPathFile.mkdir();
						        }
						        String docxFileName = "/GP"+employeeId+".docx";
						        File f = new File(destinationFileStr+File.separator+docxFileName);
						        if(!f.exists()){
						        	String destinationPathFileStr = destinationFileStr+docxFileName;
						            File destinationPathFile = new File(destinationPathFileStr);
						            if(!destinationPathFile.exists()){
						            	 InputStream is = null;
						            	    OutputStream os = null;
						            	try {
						            		 File source = new File(sourceFile);
						                    is = new FileInputStream(source);
						                    os = new FileOutputStream(destinationPathFile);
						                    byte[] buffer = new byte[1024];
						                    int length;
						                    while ((length = is.read(buffer)) > 0) {
						                        os.write(buffer, 0, length);
						                    }
						                } finally {
						                	os.flush();
						                    is.close();
						                    os.close();
						                }
						            		
						                   File templateFile = new File(sourceFile);
						                   InputStream inputStream = new FileInputStream(templateFile);
						                   byte fileContent[] = new byte[(int)templateFile.length()];
						                   inputStream.read(fileContent);
						                   FileUtils.writeByteArrayToFile(destinationPathFile, fileContent);
						            }else{
						            	destinationPathFile.delete();
						            	File templateFile = new File(sourceFile);
						                InputStream inputStream = new FileInputStream(templateFile);
						                byte fileContent[] = new byte[(int)templateFile.length()];
						                inputStream.read(fileContent);
						                FileUtils.writeByteArrayToFile(destinationPathFile, fileContent);
						            }
						    		try {
						    			applicationService.createStandardReport(templateChart,destinationPathFileStr,reportTitle,employeeName);
						    		} catch (JAXBException e) {
						    			// TODO Auto-generated catch block
						    			e.printStackTrace();
						    			 logger.error(e.getStackTrace());
						    		} catch (Docx4JException e) {
						    			// TODO Auto-generated catch block
						    			logger.error(e.getStackTrace());
						    			e.printStackTrace();
						    		}	
						        }
							}
						}
					}else{
						if(templateChartReportAssigns!= null && templateChartReportAssigns.size()>0){
							templateChart = applicationService.getDashboardTemplateChart(templateChart, null,null,null);
							String id = templateChart.getId().toString();
							String reportTitle = templateChart.getReportTitle();
							ServletContext sc = request.getSession().getServletContext();//request.getServletContext(); 
					        String bridgei2iTemplatePptFilePathStr = sc.getRealPath("/WEB-INF");
					        String sourceFile = bridgei2iTemplatePptFilePathStr + "/bridgei2i_template.docx";
					        String destinationFileStr = bridgei2iTemplatePptFilePathStr + "/StandardReport";
					        File destinationFolderPathFile = new File(destinationFileStr);
					        if (!destinationFolderPathFile.exists()) {
					               destinationFolderPathFile.mkdir();
					        }
					        String docxFileName = "/GP"+id+".docx";
					        File f = new File(destinationFileStr+File.separator+docxFileName);
					        if(!f.exists()){
					        	String destinationPathFileStr = destinationFileStr+docxFileName;
					            File destinationPathFile = new File(destinationPathFileStr);
					            if(!destinationPathFile.exists()){
					                   File templateFile = new File(sourceFile);
					                   InputStream inputStream = new FileInputStream(templateFile);
					                   byte fileContent[] = new byte[(int)templateFile.length()];
					                   inputStream.read(fileContent);
					                   FileUtils.writeByteArrayToFile(destinationPathFile, fileContent);
					            }else{
					            	destinationPathFile.delete();
					            	File templateFile = new File(sourceFile);
					                InputStream inputStream = new FileInputStream(templateFile);
					                byte fileContent[] = new byte[(int)templateFile.length()];
					                inputStream.read(fileContent);
					                FileUtils.writeByteArrayToFile(destinationPathFile, fileContent);
					            }
					    		try {
					    			applicationService.createStandardReport(templateChart,destinationPathFileStr,reportTitle,null);
					    		} catch (JAXBException e) {
					    			// TODO Auto-generated catch block
					    			e.printStackTrace();
					    			 logger.error(e.getStackTrace());
					    		} catch (Docx4JException e) {
					    			// TODO Auto-generated catch block
					    			logger.error(e.getStackTrace());
					    			e.printStackTrace();
					    		}	
					        }
						}else{
							List reportMetaDataList = applicationService.getReportMetaData(templateChart.getId());
							if(reportMetaDataList!=null){
								int size = reportMetaDataList.size();
								for(int k=0;k<size;k++){
									ReportMetadata reportMetaData = (ReportMetadata) reportMetaDataList.get(k);
									templateChart.setReportMetaDataId(reportMetaData.getId());
									templateChart.setFilter1(reportMetaData.getFilterColumn1());
									templateChart.setFilterValues1(reportMetaData.getFilterValue1());
									templateChart.setFilter2(reportMetaData.getFilterColumn2());
									templateChart.setFilterValues2(reportMetaData.getFilterValue2());
									templateChart.setFilter3(reportMetaData.getFilterColumn3());
									templateChart.setFilterValues3(reportMetaData.getFilterValue3());
									templateChart.setIsOverall("Y");
									templateChart = applicationService.getDashboardTemplateChart(templateChart, users.getEmployeeId(),users.getEmailId(),reportMetaData.getId());
                                    String operatingSegmentValue = templateChart.getFilterValues1();
                                    List segmentByLocationList = applicationService.getSegmentByLocation(operatingSegmentValue);
                                    templateChart.setSegmentByLocationList(segmentByLocationList);
                                    String distId = reportMetaData.getDistributionList();
                                    String id = templateChart.getId().toString();
                                    String reportTitle = reportMetaData.getReportTitle();
                                    ServletContext sc = request.getSession().getServletContext();
							        String bridgei2iTemplatePptFilePathStr = sc.getRealPath("/WEB-INF");
							        String sourceFile = bridgei2iTemplatePptFilePathStr + "/bridgei2i_template.docx";
							        String destinationFileStr = bridgei2iTemplatePptFilePathStr + "/StandardReport";
							        File destinationFolderPathFile = new File(destinationFileStr);
							        if (!destinationFolderPathFile.exists()) {
							               destinationFolderPathFile.mkdir();
							        }
							        String docxFileName = "/GP"+distId+"_"+reportMetaData.getReportTitle()+".docx";
							        File f = new File(destinationFileStr+File.separator+docxFileName);
							        if(!f.exists()){
							        	String destinationPathFileStr = destinationFileStr+docxFileName;
							            File destinationPathFile = new File(destinationPathFileStr);
							            if(!destinationPathFile.exists()){
							                   File templateFile = new File(sourceFile);
							                   InputStream inputStream = new FileInputStream(templateFile);
							                   byte fileContent[] = new byte[(int)templateFile.length()];
							                   inputStream.read(fileContent);
							                   FileUtils.writeByteArrayToFile(destinationPathFile, fileContent);
							            }else{
							            	destinationPathFile.delete();
							            	File templateFile = new File(sourceFile);
							                InputStream inputStream = new FileInputStream(templateFile);
							                byte fileContent[] = new byte[(int)templateFile.length()];
							                inputStream.read(fileContent);
							                FileUtils.writeByteArrayToFile(destinationPathFile, fileContent);
							            }
							    		try {
							    			applicationService.createStandardReport(templateChart,destinationPathFileStr,reportTitle,null);
							    		} catch (JAXBException e) {
							    			// TODO Auto-generated catch block
							    			e.printStackTrace();
							    			 logger.error(e.getStackTrace());
							    		} catch (Docx4JException e) {
							    			// TODO Auto-generated catch block
							    			logger.error(e.getStackTrace());
							    			e.printStackTrace();
							    		}	
							        }
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
			ApplicationException applicationException = ApplicationException.createApplicationException(ReportTemplateListController.class.toString(), "previewTemplate", 
					ApplicationErrorCodes.APP_EC_24, ApplicationConstants.EXCEPTION, null);
			ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, applicationException, request);
		}
		logger.debug("Leaving from generateStandardReport");
		return new ModelAndView("reportTemplateList", "model", reportTemplateListBean);
	}
	
	@RequestMapping(value = "/popUpComments.htm")
	public ModelAndView getpopUpCommentsDetails(
			@ModelAttribute("reportTemplateListBean") ReportTemplateListBean reportTemplateListBean,
			BindingResult result, HttpServletRequest request) {
		logger.debug("Entering and leaving from getpopUpCommentsDetails");
		System.out.println("in new popUpComments");
		return new ModelAndView("popUpComments", "model",reportTemplateListBean);
	}
	
	@RequestMapping(value = "/popUpReportTitle.htm")
	public ModelAndView getpopUpReportTitleDetails(
			@ModelAttribute("reportTemplateListBean") ReportTemplateListBean reportTemplateListBean,
			BindingResult result, HttpServletRequest request) {
		logger.debug("Entering and leaving from getpopUpReportTitleDetails");
		System.out.println("in new popUpReportTitle");
		return new ModelAndView("popUpCategoryName", "model",reportTemplateListBean);
	}
	
	@RequestMapping(value = "/reviewRequest.htm")
	public ModelAndView reviewRequestDetails(
			@ModelAttribute("reportTemplateListBean") ReportTemplateListBean reportTemplateListBean,
			BindingResult result, HttpServletRequest request) throws ApplicationException {
		logger.debug("Entering into reviewRequestDetails");
		ArrayList reportvalueObjects = (ArrayList)reportTemplateListBean.getDetailValueObjs();
		//String commentsEntered =  reportTemplateListBean.getComments();
		//reportTemplateListBean.setComments(null);
		Users users = (Users)ApplicationUtil.getObjectFromSession(ApplicationConstants.APPLICATION_LOGGED_IN_USER, request);
		Integer userId = users.getLogin_id();
		String emailId = users.getEmailId();
		String reportTitle = "";
		String editFlagArray[] = reportTemplateListBean.getEditFlagArray();
		List reviewRequestList = new ArrayList();
		if(editFlagArray != null && editFlagArray.length>0){
			for(int i=0;i<editFlagArray.length;i++){
				TemplateChart templateChart = (TemplateChart)reportvalueObjects.get(Integer.parseInt(editFlagArray[i]));
				Comments comments = new Comments();
				comments.setTemplateChart(templateChart);
				comments.setComments(commentsEntered);
	            comments.setUserId(users.getLogin_id());
				reviewRequestList.add(comments);
				reportTitle = templateChart.getReportTitle();
			}
		}
		applicationService.reviewRequest(reviewRequestList);
		MasterReportStatus masterReportStatus =  applicationService.getMasterReportOpenStatus("REVIEW");
		if(editFlagArray!=null && editFlagArray.length>0){
			for(int i=0;i<editFlagArray.length;i++){
				TemplateChart templateChart = (TemplateChart)reportvalueObjects.get(Integer.parseInt(editFlagArray[i]));
				templateChart.setStatusId(masterReportStatus.getId());
				applicationService.saveDesignReportTemplate(templateChart, null);
			}
		}
		List rolesNameList = applicationService.getRolesMailId("HR_ADMIN");
		String mailIds = "";
		String firstName = "";
		if(rolesNameList!=null){
			int len = rolesNameList.size();
			for(int i=0;i<len;i++){
				Roles roles =(Roles)rolesNameList.get(i); 
				Users user = roles.getUsers();
				firstName = firstName+user.getFirstName();
				mailIds = mailIds+user.getEmailId();
				 if(i+1<len){
					 mailIds= mailIds+",";
					 firstName= firstName+",";
      	 		}
			}
		}
		StringBuffer req = request.getRequestURL();
		String url = req.substring(0,req.length()-17);
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
        StringBuffer body = new StringBuffer();
        body.append("Dear " + " " +firstName+ ",\n\n" + "The following reports are available for reviewing. " +
        		"Kindly test the reports and update report status and review comments for the same." + "\n\n" + 
        		"Report Name :" + "" + reportTitle + "\n\n" +
        		"Kindly use the URL below for logging in." + "\n\n" +
        		 url+ "login.jsp"+ "\n\n\n" +
        		"Thanks," + "\n\n" +
        		"<EmPOWER report management system on behalf of bridgei2i Analyst>");
        try {
              ApplicationUtil.sendEMail(mailIds,
                           "Reports available for review",
                            body.toString(), adminEmail, adminPassword, props,
                            PropertiesUtil.getProperty("APP_EC_101"));
              ApplicationException applicationException = ApplicationException.createApplicationException(ReportTemplateListController.class.toString(), "reviewRequest", 
  					ApplicationErrorCodes.APP_EC_18, ApplicationConstants.INFORMATION, null);
  			ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, applicationException, request);

        } catch (Exception e) {
         //     logger.error(e.getMessage());
        	logger.error(e.getMessage());
        	ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e, request);

        }
		List templateChartList = applicationService.getTemplateCharts(userId,emailId,false);
		reportTemplateListBean = new ReportTemplateListBean();
		reportTemplateListBean.setDetailValueObjs(templateChartList);
		ApplicationUtil.setObjectInSession("reportTemplateListBean", reportTemplateListBean, request);
		logger.debug("Leaving from reviewRequestDetails");
		return new ModelAndView("reportTemplateList", "model", reportTemplateListBean);
	}
	
	@RequestMapping(value = "/publishReport.htm")
	public ModelAndView publishReport(
			@ModelAttribute("reportTemplateListBean") ReportTemplateListBean reportTemplateListBean,
			BindingResult result, HttpServletRequest request) throws ApplicationException {
		logger.debug("Entering into publishReport");
		ArrayList reportvalueObjects = (ArrayList)reportTemplateListBean.getDetailValueObjs();
		String editFlagArray[] = reportTemplateListBean.getEditFlagArray();
		List masterIdList = new ArrayList();
		Users users = (Users)ApplicationUtil.getObjectFromSession(ApplicationConstants.APPLICATION_LOGGED_IN_USER, request);
		Integer userId = users.getLogin_id();
		String emailId = users.getEmailId();
		String masterDistIdList="";
		String report = "";
		StringBuffer reportBuffer = new StringBuffer();
		if(editFlagArray != null && editFlagArray.length>0){
			for(int i=0;i<editFlagArray.length;i++){
				TemplateChart templateChart = (TemplateChart)reportvalueObjects.get(Integer.parseInt(editFlagArray[i]));
				report = templateChart.getReportTitle();
				reportBuffer.append(report);
				if(i+1<editFlagArray.length){
					reportBuffer.append(" " +","+ " ");
    			}
				List templateChartReportAssignList = templateChart.getTemplateChartReportAssigns();
				int size = templateChartReportAssignList.size();
				if(templateChartReportAssignList != null){
					for(int j=0;j<templateChartReportAssignList.size();j++){
					TemplateChartReportAssign templateChartReportAssign = (TemplateChartReportAssign) templateChartReportAssignList.get(j);
					Integer masterDistId = templateChartReportAssign.getMasterDistId();
					System.out.println(masterDistId);
					masterIdList.add(masterDistId);
					}
				}
			} 
		}
		if(masterIdList!=null){
			int size = masterIdList.size();
			for(int i=0;i<size;i++){
			masterDistIdList = masterDistIdList+masterIdList.get(i);
			if(i+1<size){
				masterDistIdList = masterDistIdList+",";
			  }
			}
		}
		System.out.println(masterDistIdList);
		MasterReportStatus masterReportStatus =  applicationService.getMasterReportOpenStatus("PUBLISHED");
		if(editFlagArray!=null && editFlagArray.length>0){
			for(int i=0;i<editFlagArray.length;i++){
				TemplateChart templateChart = (TemplateChart)reportvalueObjects.get(Integer.parseInt(editFlagArray[i]));
				templateChart.setStatusId(masterReportStatus.getId());
				applicationService.saveDesignReportTemplate(templateChart, null);
			}
		}
		
		List emailIdList = applicationService.getMailIdList(masterDistIdList);
		StringBuffer req = request.getRequestURL();
		String url = req.substring(0,req.length()-17);
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
        if(emailIdList!=null){
	        int len = emailIdList.size();
			for(int i=0;i<len;i++){
				Object[] row = (Object[])emailIdList.get(i);
				String name = row[0].toString();
				System.out.println(name);
			    String emailId = row[1].toString();
			    System.out.println(emailId);
				String reportTitle = row[2].toString();
				System.out.println(reportTitle);
				StringBuffer body = new StringBuffer();
		        body.append("Dear " + " " +name+ ",\n\n" + "The following reports are available for your persual. " +"\n\n" + 
		        		"Report Name :" + "" + reportBuffer + "\n\n" +
	            		"Please login with your credentials to view your report." + "\n\n" + 
	            		"URL for logging in" + "\n\n" +
	            		 url+"login.jsp" + "\n\n\n" +
	            		"Thanks," + "\n\n" +
	            		"ESS Reporting Solution");
		        try {
		              ApplicationUtil.sendEMail(emailId,
		                           "Your ESS Reports",
		                            body.toString(), adminEmail, adminPassword, props,
		                            PropertiesUtil.getProperty("APP_EC_101"));
		              
		              ApplicationException applicationException = ApplicationException.createApplicationException(ReportTemplateListController.class.toString(), "publishReport", 
		  					ApplicationErrorCodes.APP_EC_10, ApplicationConstants.INFORMATION, null);
		  			ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, applicationException, request);
	
		        } catch (Exception e) {
		        	logger.error(e.getMessage());
		        	ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e, request);
		        	System.out.println(e);
		        }
			}
		}
		List rolesNameList = applicationService.getRolesMailId("HR_ADMIN");
		String mailIds = "";
		String firstName = "";
		if(rolesNameList!=null){
			int length = rolesNameList.size();
			for(int i=0;i<length;i++){
				Roles roles =(Roles)rolesNameList.get(i); 
				Users user = roles.getUsers();
				firstName = firstName+user.getFirstName();
				mailIds = mailIds+user.getEmailId();
				 if(i+1<length){
					 mailIds= mailIds+",";
					 firstName= firstName+",";
	  	 		}
			}
		}
        StringBuffer body = new StringBuffer();
        body.append("Dear " + " " +firstName+ ",\n\n" + "The following reports are published. " +"\n\n" + 
        		"Report Name :" + "" + reportBuffer + "\n\n" +
        		"Please login with your credentials to view published report." + "\n\n" + 
        		"URL for logging in" + "\n\n" +
        		 url+"login.jsp" + "\n\n\n" +
        		"Thanks," + "\n\n" +
        		"ESS Reporting Solution");
        try {
              ApplicationUtil.sendEMail(mailIds,
                           "Published Reports",
                            body.toString(), adminEmail, adminPassword, props,
                            PropertiesUtil.getProperty("APP_EC_101"));
              ApplicationException applicationException = ApplicationException.createApplicationException(ReportTemplateListController.class.toString(), "publishReport", 
  					ApplicationErrorCodes.APP_EC_10, ApplicationConstants.INFORMATION, null);
  			ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, applicationException, request);

        } catch (Exception e) {
         //     logger.error(e.getMessage());
        	logger.error(e.getMessage());
        	ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e, request);

        }
        List nameList = applicationService.getRolesMailId("BI2I_ADMIN");
        String mailId = "";
		String name = "";
        if(nameList!=null){
			int leng = nameList.size();
			for(int i=0;i<leng;i++){
				Roles roles =(Roles)nameList.get(i); 
				Users user = roles.getUsers();
				name = name+user.getFirstName();
				mailId = mailId+user.getEmailId();
				 if(i+1<leng){
					 mailId= mailId+",";
					 name= name+",";
	  	 		}
			}
        }
		StringBuffer body1 = new StringBuffer();
        body1.append("Dear " + " " +name+ ",\n\n" + "The following reports are published. " +"\n\n" + 
        		"Report Name :" + "" + reportBuffer + "\n\n" +
        		"Please login with your credentials to view published report." + "\n\n" + 
        		"URL for logging in" + "\n\n" +
        		 url+"login.jsp" + "\n\n\n" +
        		"Thanks," + "\n\n" +
        		"ESS Reporting Solution");
        try {
              ApplicationUtil.sendEMail(mailId,
                           "Published Reports",
                            body1.toString(), adminEmail, adminPassword, props,
                            PropertiesUtil.getProperty("APP_EC_101"));
              ApplicationException applicationException = ApplicationException.createApplicationException(ReportTemplateListController.class.toString(), "publishReport", 
  					ApplicationErrorCodes.APP_EC_10, ApplicationConstants.INFORMATION, null);
  			ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, applicationException, request);

        } catch (Exception e) {
         //     logger.error(e.getMessage());
        	logger.error(e.getMessage());
        	ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e, request);
        }
		List templateChartList = applicationService.getTemplateCharts(userId,emailId,false);
		reportTemplateListBean = new ReportTemplateListBean();
		reportTemplateListBean.setDetailValueObjs(templateChartList);
		ApplicationUtil.setObjectInSession("reportTemplateListBean", reportTemplateListBean, request);
		logger.debug("Leaving from publishReport");
	   return new ModelAndView("reportTemplateList", "model", reportTemplateListBean);
	}
*/}
