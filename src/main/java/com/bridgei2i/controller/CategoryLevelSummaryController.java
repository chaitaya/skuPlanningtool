package com.bridgei2i.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.HibernateException;
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
import com.bridgei2i.common.controller.LoadApplicationCacheService;
import com.bridgei2i.common.exception.ApplicationException;
import com.bridgei2i.common.util.ApplicationUtil;
import com.bridgei2i.common.util.PropertiesUtil;
import com.bridgei2i.common.vo.PageTemplate;
import com.bridgei2i.common.vo.PageTemplateCharts;
import com.bridgei2i.common.vo.PageTemplateFilters;
import com.bridgei2i.common.vo.SingleMultiSeriesChartValueObject;
import com.bridgei2i.common.vo.Users;
import com.bridgei2i.form.CategoryLevelSummaryBean;
import com.bridgei2i.form.ReportTemplateBean;
import com.bridgei2i.service.CategoryLevelSummaryService;
import com.bridgei2i.service.ReportService;
import com.bridgei2i.vo.PlanningCycle;
import com.bridgei2i.vo.TemplateChart;
import com.bridgei2i.vo.TemplateChartCategory;
import com.bridgei2i.vo.TemplateChartInfo;

@Controller
@SessionAttributes("categoryLevelSummaryBean")
@Scope("session")
public class CategoryLevelSummaryController {
	
	private static Logger logger = Logger.getLogger(CategoryLevelSummaryService.class);
	public static int rowIndex=0;
	
	@Autowired(required=true)
	private CategoryLevelSummaryService categoryLevelSummaryService;
	
	@Autowired(required=true)
	private ReportService reportService;

	@RequestMapping(value = "/categoryLevelSummary.htm")
	public ModelAndView getCategoryLevelSummary(
			@ModelAttribute("CategoryLevelSummaryBean") CategoryLevelSummaryBean categoryLevelSummaryBean,
			BindingResult result, HttpServletRequest request,ModelMap map)  {
			
			int planningCycleId=categoryLevelSummaryBean.getPlanningCycleId();
			String sessionPlanningcycleId = (String)ApplicationUtil.getObjectFromSession(ApplicationConstants.PLANNING_CYCLE_ID, request);
			if(planningCycleId>0){
				ApplicationUtil.setObjectInSession(ApplicationConstants.PLANNING_CYCLE_ID, planningCycleId+"", request);
			}else if(!ApplicationUtil.isEmptyOrNull(sessionPlanningcycleId)){
				planningCycleId = Integer.parseInt(sessionPlanningcycleId);
				categoryLevelSummaryBean.setPlanningCycleId(planningCycleId);
			}
			try {
				Map masterCommitStatusIdMap = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.MASTER_COMMIT_STATUS_ID_MAP);
				String cmStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_CM);
				int cmStatusId = -1;
				if(!ApplicationUtil.isEmptyOrNull(cmStatusIdStr)){
					cmStatusId = Integer.parseInt(cmStatusIdStr);
				}
				Map categoryLogListMap = categoryLevelSummaryService.getCategoryLevelSummary(planningCycleId,cmStatusId,request);
				List categoryLogList = (List)categoryLogListMap.get("categoryLevelSummaryList");
				int categoryCommitResult = (int)categoryLogListMap.get("categoryLevelSummaryApprove");
				String workFlowStatus = categoryLevelSummaryService.getWorkflowStatus(planningCycleId);
				categoryLevelSummaryBean.setCategoryStatusList(categoryLogList);
				categoryLevelSummaryBean.setCategoryCommitFlag(categoryCommitResult);
				categoryLevelSummaryBean.setWorkFlowStatus(workFlowStatus);
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ApplicationUtil.setObjectInSession("isFrom", "categoryLevelSummary", request);
			map.addAttribute("categoryLevelSummaryBean", categoryLevelSummaryBean);
			String isFromCategoryReport = request.getParameter("isFromCategoryReport");
			String view = "categoryLevelSummary";
			if(!ApplicationUtil.isEmptyOrNull(isFromCategoryReport) && isFromCategoryReport.equalsIgnoreCase("true")){
				view = "categoryReport";
				ApplicationUtil.setObjectInSession(ApplicationConstants.ACTIVE_PLANNING_TAB, "categoryReport", request);
			}
	    	logger.debug("Entering and leaving from getcategoryLevelSummary");
			return new ModelAndView(view, "model",categoryLevelSummaryBean);
	}
	
	@RequestMapping(value = "/freezePlanningCycle.htm")
	public ModelAndView financeManagerFreeze(
			@ModelAttribute("CategoryLevelSummaryBean") CategoryLevelSummaryBean categoryLevelSummaryBean,
			BindingResult result, HttpServletRequest request)  {
			int planningCycleId=categoryLevelSummaryBean.getPlanningCycleId();
			try {
				Map masterCommitStatusIdMap = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.MASTER_COMMIT_STATUS_ID_MAP);
				String statusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_CD);
				int statusId = -1;
				if(!ApplicationUtil.isEmptyOrNull(statusIdStr)){
					statusId = Integer.parseInt(statusIdStr);
				}
				Map categoryLogListMap = categoryLevelSummaryService.getCategoryLevelSummary(planningCycleId,statusId,request);
				List categoryLogList = (List)categoryLogListMap.get("categoryLevelSummaryList");
				int categoryCommitResult = (int)categoryLogListMap.get("categoryLevelSummaryApprove");
				String workFlowStatus = categoryLevelSummaryService.getWorkflowStatus(planningCycleId);
				categoryLevelSummaryBean.setCategoryStatusList(categoryLogList);
				categoryLevelSummaryBean.setCategoryCommitFlag(categoryCommitResult);
				categoryLevelSummaryBean.setWorkFlowStatus(workFlowStatus);
				ApplicationUtil.removeObjectFromSession(ApplicationConstants.CLOSED_PLANNING_CYCLE_LIST, request);
			} catch (ApplicationException e) {
				ApplicationUtil.setObjectInSession(
						ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e,
						request);
				e.printStackTrace();
			}
			ApplicationUtil.setObjectInSession("isFrom", "financeManagerFreeze", request);
	    	logger.debug("Entering and leaving from financeManagerFreeze");
			return new ModelAndView("financeManagerFreeze", "model",categoryLevelSummaryBean);
	}

	@RequestMapping(value = "/overrideFreeze.htm")
	public String overrideFreeze(
			@ModelAttribute("CategoryLevelSummaryBean") CategoryLevelSummaryBean categoryLevelSummaryBean,
			BindingResult result, HttpServletRequest request)  {
			int planningCycleId = categoryLevelSummaryBean.getPlanningCycleId();
			try {
				categoryLevelSummaryService.setOverrideFreeze(planningCycleId);
			} catch (Exception e) {
				ApplicationUtil.setObjectInSession(
						ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e,
						request);
				logger.info(e.getMessage());
				return "redirect:" + "freezePlanningCycle";
				//return new ModelAndView("financeManagerFreeze", "model",categoryLevelSummaryBean);
			}
			ApplicationUtil.setObjectInSession("isFrom", "financeManagerFreeze", request);
	    	logger.debug("Entering and leaving from financeManagerFreeze");
	    	return "redirect:" + "homePage.htm";
	}
	
	@RequestMapping(value = "/releaseSkuToOverride.htm",method=RequestMethod.POST)
	public ModelAndView getReleaseSkuToOverride(
			@ModelAttribute("CategoryLevelSummaryBean") CategoryLevelSummaryBean categoryLevelSummaryBean,
			BindingResult result, HttpServletRequest request)  {
			
			int planningCycleId=categoryLevelSummaryBean.getPlanningCycleId();
			String category=categoryLevelSummaryBean.getCategory();
			int releaseFlag=categoryLevelSummaryBean.getCategoryDirectorReleaseFlag();
			try {
				Users users = (Users) ApplicationUtil.getObjectFromSession(ApplicationConstants.APPLICATION_ACT_AS_USER, request);
				Integer userId=users.getLogin_id();
				categoryLevelSummaryService.releaseSkuToOverride(userId.intValue(),category,releaseFlag,planningCycleId);
				Map masterCommitStatusIdMap = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.MASTER_COMMIT_STATUS_ID_MAP);
				String statusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_CM);
				int statusId = -1;
				if(!ApplicationUtil.isEmptyOrNull(statusIdStr)){
					statusId = Integer.parseInt(statusIdStr);
				}
				Map categoryLogListMap = categoryLevelSummaryService.getCategoryLevelSummary(planningCycleId,statusId,request);
				List categoryLogList = (List)categoryLogListMap.get("categoryLevelSummaryList");
				int categoryCommitResult = (int)categoryLogListMap.get("categoryLevelSummaryApprove");
				categoryLevelSummaryBean.setCategoryStatusList(categoryLogList);
				categoryLevelSummaryBean.setCategoryCommitFlag(categoryCommitResult);
			} catch (ApplicationException e) {
				ApplicationUtil.setObjectInSession(
						ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e,
						request);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ApplicationUtil.setObjectInSession("isFrom", "categoryLevelSummary", request);
	    	logger.debug("Entering and leaving from releaseSkuToOverride");
			return new ModelAndView("categoryLevelSummary", "model",categoryLevelSummaryBean);
	}
	
	@RequestMapping(value = "/commitCategory.htm",method=RequestMethod.POST)
	public ModelAndView getCommitCategory(
			@ModelAttribute("categoryLevelSummaryBean") CategoryLevelSummaryBean categoryLevelSummaryBean,
			BindingResult result, HttpServletRequest request)  {
			int planningCycleId=categoryLevelSummaryBean.getPlanningCycleId();
			String category=categoryLevelSummaryBean.getCategory();
			int commitFlag=categoryLevelSummaryBean.getCategoryDirectorCommitFlag();
			int commitLevel=categoryLevelSummaryBean.getCommitLevel();
			Users users = (Users) ApplicationUtil.getObjectFromSession(ApplicationConstants.APPLICATION_ACT_AS_USER, request);
			Integer userId=users.getLogin_id();
			try {
				categoryLevelSummaryService.commitCategory(userId,category,planningCycleId);
				Map masterCommitStatusIdMap = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.MASTER_COMMIT_STATUS_ID_MAP);
				String cmStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_CM);
				int cmStatusId = -1;
				if(!ApplicationUtil.isEmptyOrNull(cmStatusIdStr)){
					cmStatusId = Integer.parseInt(cmStatusIdStr);
				}
				Map categoryLogListMap = categoryLevelSummaryService.getCategoryLevelSummary(planningCycleId,cmStatusId,request);
				List categoryLogList = (List)categoryLogListMap.get("categoryLevelSummaryList");
				int categoryCommitResult = (int)categoryLogListMap.get("categoryLevelSummaryApprove");
				String workFlowStatus = categoryLevelSummaryService.getWorkflowStatus(planningCycleId);
				categoryLevelSummaryBean.setCategoryStatusList(categoryLogList);
				categoryLevelSummaryBean.setCategoryCommitFlag(categoryCommitResult);
				categoryLevelSummaryBean.setWorkFlowStatus(workFlowStatus);
			} catch (ApplicationException e) {
				ApplicationUtil.setObjectInSession(
						ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e,
						request);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ApplicationUtil.setObjectInSession("isFrom", "categoryLevelSummary", request);
	    	logger.debug("Entering and leaving from commit Category");
			return new ModelAndView("categoryLevelSummary", "model",categoryLevelSummaryBean);
			
	}
	
	@RequestMapping(value = "/approveAllCategories.htm",method=RequestMethod.POST)
	public ModelAndView approveAllCategories(
			@ModelAttribute("categoryLevelSummaryBean") CategoryLevelSummaryBean categoryLevelSummaryBean,
			BindingResult result, HttpServletRequest request)  {
			int planningCycleId=categoryLevelSummaryBean.getPlanningCycleId();
			List categoresList = categoryLevelSummaryBean.getCategoryStatusList();
			Users users = (Users) ApplicationUtil.getObjectFromSession(ApplicationConstants.APPLICATION_ACT_AS_USER, request);
			Integer userId=users.getLogin_id();
			try {
				if(categoresList != null){
					int size = categoresList.size();
					for(int i=0;i<size;i++){
						List categoryListObj = (List)categoresList.get(i);
						String category = (String)categoryListObj.get(0);
						categoryLevelSummaryService.commitCategory(userId,category,planningCycleId);
					}
				}
				categoryLevelSummaryService.changeWorkFlowStatus(planningCycleId);
				Map masterCommitStatusIdMap = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.MASTER_COMMIT_STATUS_ID_MAP);
				String cmStatusIdStr = (String)masterCommitStatusIdMap.get(ApplicationConstants.COMMITTED_BY_CM);
				int cmStatusId = -1;
				if(!ApplicationUtil.isEmptyOrNull(cmStatusIdStr)){
					cmStatusId = Integer.parseInt(cmStatusIdStr);
				}
				Map categoryLogListMap = categoryLevelSummaryService.getCategoryLevelSummary(planningCycleId,cmStatusId,request);
				List categoryLogList = (List)categoryLogListMap.get("categoryLevelSummaryList");
				int categoryCommitResult = (int)categoryLogListMap.get("categoryLevelSummaryApprove");
				String workFlowStatus = categoryLevelSummaryService.getWorkflowStatus(planningCycleId);
				categoryLevelSummaryBean.setCategoryStatusList(categoryLogList);
				categoryLevelSummaryBean.setCategoryCommitFlag(categoryCommitResult);
				categoryLevelSummaryBean.setWorkFlowStatus(workFlowStatus);
				ApplicationUtil.removeObjectFromSession(ApplicationConstants.CLOSED_PLANNING_CYCLE_LIST, request);
			} catch (ApplicationException e) {
				ApplicationUtil.setObjectInSession(
						ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e,
						request);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ApplicationUtil.setObjectInSession("isFrom", "categoryLevelSummary", request);
	    	logger.debug("Entering and leaving from approveAllCategories");
			return new ModelAndView("categoryLevelSummary", "model",categoryLevelSummaryBean);
			
	}
	
	@RequestMapping(value = "/flatFileExportExcel.htm")
public HttpEntity<byte[]> flatFileExportExcel(CategoryLevelSummaryBean categoryLevelSummaryBean,HttpServletRequest request) throws ApplicationException {
		
		int planningCycleId=categoryLevelSummaryBean.getPlanningCycleId();
		String filePath = PropertiesUtil.getProperty(ApplicationConstants.SCHEDULER_DATA_FOLDER);
        System.out.println("Path:: "+filePath);
        File file = new File(filePath);
		if (!file.exists()) {
			file.mkdir();
		}
		String destinationPathFileStr = filePath+"flatFile.xlsx";
		
		List templateChartCategories = null;
        
		XSSFWorkbook workbook= new XSSFWorkbook();
        XSSFSheet sheet =  workbook.createSheet("new sheet");
        XSSFCellStyle cs = workbook.createCellStyle();
        cs.setWrapText(true);
        CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        CellStyle leftHeaderStyle = sheet.getWorkbook().createCellStyle();
        leftHeaderStyle.setAlignment(CellStyle.VERTICAL_CENTER);
        
        Map pageTemplateCacheObj = (Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.PAGE_TEMPLATES_CACHE_KEY);	
		PageTemplate pageTemplate = (PageTemplate)pageTemplateCacheObj.get("PC_DASHBOARD");
		List pageTemplateCharts = pageTemplate.getPageTemplateCharts();
		PlanningCycle activePlanningCycle = (PlanningCycle)ApplicationUtil.getObjectFromSession(ApplicationConstants.ACTIVE_PLANNINGCYCLE_OBJ, request);
		String startWeek=activePlanningCycle.getStartWeek();
		Integer week=Integer.parseInt(startWeek);
		String startYear=activePlanningCycle.getStartYear();
		Integer year =Integer.parseInt(startYear);
		String actualsWeekRange = PropertiesUtil.getProperty("weeksRange");
		Integer range =Integer.valueOf(actualsWeekRange);
		Map targetTableViewMapObj = new HashMap();
		Map targetTablecolorRangeMapObj = new HashMap();
		int size1 = pageTemplateCharts.size();
		for(int i=0;i<size1;i++){
			PageTemplateCharts pageTemplateChart = (PageTemplateCharts) pageTemplateCharts.get(i);
			String tableName = pageTemplateChart.getTableName();
			String selectedPeriod="";
			if(ApplicationUtil.isEmptyOrNull(selectedPeriod)){
				selectedPeriod="Weekly";
			}
			System.out.println(selectedPeriod);
			//reportService.getReportTableView(targetTableViewMapObj,tableName,planningCycleId,activefilterVariable,business,stringBuffer.toString(),week,year,range,selectedTypeValue,selectedPeriod,targetTablecolorRangeMapObj) ;
		}
        
		if(templateChartCategories != null ){
			List templateChartInfoList = null;
			
			if(templateChartInfoList != null){
				
				TemplateChartInfo templateChartInfo = null;
				rowIndex=0;
				int cellNum =0;
				SingleMultiSeriesChartValueObject singleMultiSeriesChartValueObject = templateChartInfo.getSingleMultiSeriesChartValueObject();
				
				List highStrengthList = singleMultiSeriesChartValueObject.getHighStrengthList();
				List highNeutralList= singleMultiSeriesChartValueObject.getHighNeutralList();
				List highWeaknessList= singleMultiSeriesChartValueObject.getHighWeaknessList();
					
				List mediumStrengthList= singleMultiSeriesChartValueObject.getMediumStrengthList();
				List mediumNeutralList= singleMultiSeriesChartValueObject.getMediumNeutralList();
				List mediumWeaknessList= singleMultiSeriesChartValueObject.getMediumWeaknessList();
				
				List lowStrengthList= singleMultiSeriesChartValueObject.getLowStrengthList();
				List lowNeutralList= singleMultiSeriesChartValueObject.getLowNeutralList();
				List lowWeaknessList= singleMultiSeriesChartValueObject.getLowWeaknessList();
				
				Row headerRow = sheet.createRow(rowIndex);
				Cell headerCell1 = headerRow.createCell((short)(++cellNum));
				headerCell1.setCellValue("Below Par");
				headerCell1.setCellStyle(headerStyle);
				Cell headerCell2 = headerRow.createCell((short)(++cellNum));
				headerCell2.setCellValue("At Par");
				headerCell2.setCellStyle(headerStyle);
				Cell headerCell3 = headerRow.createCell((short)(++cellNum));
				headerCell3.setCellValue("Above Par");
				headerCell3.setCellStyle(headerStyle);
				rowIndex++;
				
				Row dataRow1 = sheet.createRow(rowIndex);
				
				cellNum =0;
				sheet.autoSizeColumn(cellNum);
				Cell dataCell1 = dataRow1.createCell((short)(cellNum++));
				dataCell1.setCellValue("High");
				dataCell1.setCellStyle(headerStyle);
				
				Cell dataCell2 = dataRow1.createCell((short)(cellNum++));
				if(highWeaknessList != null){
					
					int size = highWeaknessList.size();
					String highWeaknessStr ="";
					for(int i=0;i<size;i++){
						highWeaknessStr += highWeaknessList.get(i) +"\n";
					}
					
					dataCell2.setCellValue(highWeaknessStr);
				}
				dataCell2.setCellStyle(cs);
				sheet.autoSizeColumn((short)cellNum-1);
				
				
				Cell dataCell3 = dataRow1.createCell((short)(cellNum++));
				if(highNeutralList != null){
					
					int size = highNeutralList.size();
					String highNeutralStr ="";
					for(int i=0;i<size;i++){
						highNeutralStr += highNeutralList.get(i) +"\n";
					}
					
					dataCell3.setCellValue(highNeutralStr);
				}
				dataCell3.setCellStyle(cs);
				sheet.autoSizeColumn((short)cellNum-1);
				
				
				Cell dataCell4 = dataRow1.createCell((short)(cellNum));
				if(highStrengthList != null){
					
					int size = highStrengthList.size();
					String highStrengthStr ="";
					for(int i=0;i<size;i++){
						highStrengthStr += highStrengthList.get(i) +"\n";
					}
					
					dataCell4.setCellValue(highStrengthStr);
				}
				dataCell4.setCellStyle(cs);
				dataRow1.setHeightInPoints((5*sheet.getDefaultRowHeightInPoints()));
				sheet.autoSizeColumn((short)cellNum);
				rowIndex++;
				
				Row dataRow2 = sheet.createRow(rowIndex);
				cellNum =0;
				Cell data2Cell1 = dataRow2.createCell((short)(cellNum++));
				data2Cell1.setCellValue("Medium");
				data2Cell1.setCellStyle(headerStyle);
				Cell data2Cell2 = dataRow2.createCell((short)(cellNum++));
				if(mediumWeaknessList != null){
					
					int size = mediumWeaknessList.size();
					String mediumWeaknessStr ="";
					for(int i=0;i<size;i++){
						mediumWeaknessStr += mediumWeaknessList.get(i) +"\n";
					}
					
					data2Cell2.setCellValue(mediumWeaknessStr);
				}
				data2Cell2.setCellStyle(cs);
				sheet.autoSizeColumn((short)cellNum-1);
				
				Cell data2Cell3 = dataRow2.createCell((short)(cellNum++));
				if(mediumNeutralList != null){
					
					int size = mediumNeutralList.size();
					String mediumNeutralStr ="";
					for(int i=0;i<size;i++){
						mediumNeutralStr += mediumNeutralList.get(i) +"\n";
					}
					
					data2Cell3.setCellValue(mediumNeutralStr);
				}
				data2Cell3.setCellStyle(cs);
				sheet.autoSizeColumn((short)cellNum-1);
				
				
				Cell data2Cell4 = dataRow2.createCell((short)(cellNum));
				if(mediumStrengthList != null){
					
					int size = mediumStrengthList.size();
					String mediumStrengthStr ="";
					for(int i=0;i<size;i++){
						mediumStrengthStr += mediumStrengthList.get(i) +"\n";
					}
					
					data2Cell4.setCellValue(mediumStrengthStr);
				}
				data2Cell4.setCellStyle(cs);
				dataRow2.setHeightInPoints((5*sheet.getDefaultRowHeightInPoints()));
				sheet.autoSizeColumn((short)cellNum);
				rowIndex++;
				
				Row dataRow3 = sheet.createRow(rowIndex);
				cellNum =0;
				Cell data3Cell1 = dataRow3.createCell((short)(cellNum++));
				data3Cell1.setCellValue("Low");
				data3Cell1.setCellStyle(headerStyle);
				Cell data3Cell2 = dataRow3.createCell((short)(cellNum++));
				if(lowWeaknessList != null){
					
					int size = lowWeaknessList.size();
					String lowWeaknessStr ="";
					for(int i=0;i<size;i++){
						lowWeaknessStr += lowWeaknessList.get(i) +"\n";
					}
					
					data3Cell2.setCellValue(lowWeaknessStr);
				}
				data3Cell2.setCellStyle(cs);
				sheet.autoSizeColumn((short)cellNum-1);
				
				
				Cell data3Cell3 = dataRow3.createCell((short)(cellNum++));
				if(lowNeutralList != null){
					
					int size = lowNeutralList.size();
					String lowNeutralStr ="";
					for(int i=0;i<size;i++){
						lowNeutralStr += lowNeutralList.get(i) +"\n";
					}
					
					data3Cell3.setCellValue(lowNeutralStr);
				}
				data3Cell3.setCellStyle(cs);
				sheet.autoSizeColumn((short)cellNum-1);
				
				
				Cell data3Cell4 = dataRow3.createCell((short)(cellNum));
				if(lowStrengthList != null){
					
					int size = lowStrengthList.size();
					String lowStrengthStr ="";
					for(int i=0;i<size;i++){
						lowStrengthStr += lowStrengthList.get(i) +"\n";
					}
					
					data3Cell4.setCellValue(lowStrengthStr);
				}
				data3Cell4.setCellStyle(cs);
				dataRow3.setHeightInPoints((5*sheet.getDefaultRowHeightInPoints()));
				sheet.autoSizeColumn((short)cellNum);
				
			}
			
		}
		
    	try {
    		FileOutputStream fileOut =  new FileOutputStream(destinationPathFileStr);
			workbook.write(fileOut);
			fileOut.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
	    file = new File(destinationPathFileStr);
		HttpHeaders headers = new HttpHeaders();
        headers.setContentLength((int)file.length());
        headers.set("Content-Disposition", "attachment;filename=\"kda_excel.xlsx\"");
        headers.setContentType(new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        byte excelbytes[] =null;
        try {
               FileInputStream in = new FileInputStream(file);
               excelbytes = new byte[(int)file.length()];
               in.read(excelbytes);
        } catch (FileNotFoundException e) {
        	ApplicationUtil.setObjectInSession(
					ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e,
					request);
               // TODO Auto-generated catch block
     	   logger.error(e.getMessage());
        } catch (IOException e) {
               // TODO Auto-generated catch block
               logger.error(e.getMessage());
        }
		return new HttpEntity<byte[]>(excelbytes,headers);
	}


	
}
