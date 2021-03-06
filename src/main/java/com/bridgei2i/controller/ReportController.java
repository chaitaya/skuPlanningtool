package com.bridgei2i.controller;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bridgei2i.common.constants.ApplicationConstants;
import com.bridgei2i.common.controller.LoadApplicationCacheService;
import com.bridgei2i.common.exception.ApplicationException;
import com.bridgei2i.common.util.ApplicationUtil;
import com.bridgei2i.common.util.PropertiesUtil;
import com.bridgei2i.common.vo.PageTemplate;
import com.bridgei2i.common.vo.PageTemplateCharts;
import com.bridgei2i.common.vo.PageTemplateFilters;
import com.bridgei2i.common.vo.Users;
import com.bridgei2i.form.ReportBean;
import com.bridgei2i.service.PlanningService;
import com.bridgei2i.service.ReportService;
import com.bridgei2i.vo.PlanningCycle;

@Controller
@SessionAttributes("reportBean")
@Scope("session")
public class ReportController {
	
	private static Logger logger = Logger.getLogger(ReportController.class);
	@Autowired(required=true)
	private ReportService reportService;
	
	@Autowired(required=true)
    private PlanningService planningService;
	
	
	@RequestMapping(value = "/refreshReports.htm")
	public ModelAndView refreshReports(
			@ModelAttribute("reportBean") ReportBean reportBean,
			BindingResult result, HttpServletRequest request,ModelMap map) throws ApplicationException {
		return getReports(reportBean,result,request,map);
	}

	@RequestMapping(value = "/reports.htm")
	public ModelAndView getReports(
			@ModelAttribute("newBean") ReportBean reportBean,
			BindingResult result, HttpServletRequest request,ModelMap map) throws ApplicationException {
		int planningCycleId = reportBean.getPlanningCycleId();
		String type= reportBean.getType();
		String selectedTypeValue = reportBean.getSelectedTypeValue();
		String selectedTypeVariable = reportBean.getSelectedTypeVariable();
		Map pageTemplateCacheObj = (Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.PAGE_TEMPLATES_CACHE_KEY);	
		PageTemplate pageTemplate = (PageTemplate)pageTemplateCacheObj.get("PC_DASHBOARD");
		List pageTemplateCharts = pageTemplate.getPageTemplateCharts();
		List pageTemplateFilters = pageTemplate.getPageTemplateFilters();
		String selectedFilterIndexStr = reportBean.getSelectedFilterIndex();
		Users users = (Users) ApplicationUtil.getObjectFromSession(ApplicationConstants.APPLICATION_ACT_AS_USER, request);
		List rolesList = users.getRolesList();
		Integer userId=users.getLogin_id();
		String isFromReportModule = request.getParameter("isFromReportModule");		
		String activefilterVariable="";
		String startWeek="0";
		String startYear="0";
		ApplicationUtil.removeObjectFromSession("isFromOnChangeFilter", request);
		Map closedPlanningCycleMap = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.CLOSED_PLANNING_CYCLE_LIST, request);
		if(closedPlanningCycleMap == null && !ApplicationUtil.isEmptyOrNull(isFromReportModule) && isFromReportModule.equalsIgnoreCase("true")){
			closedPlanningCycleMap = reportService.getClosedReportPlanningCycleList(false);
			ApplicationUtil.setObjectInSession(ApplicationConstants.CLOSED_PLANNING_CYCLE_LIST, closedPlanningCycleMap, request);
		}
		if(!ApplicationUtil.isEmptyOrNull(isFromReportModule) && isFromReportModule.equalsIgnoreCase("true")){
			PlanningCycle closedPlanningCycle=(PlanningCycle) closedPlanningCycleMap.get(planningCycleId);
			if(closedPlanningCycle!=null){
				startWeek=closedPlanningCycle.getStartWeek();
				startYear=closedPlanningCycle.getStartYear();
				reportBean.setPlanningCycle(closedPlanningCycle.getWeekYear());
			}
		}
		if(!isFromReportModule.equalsIgnoreCase("true")){
			PlanningCycle activePlanningCycle = (PlanningCycle)ApplicationUtil.getObjectFromSession(ApplicationConstants.ACTIVE_PLANNINGCYCLE_OBJ, request);
			startWeek=activePlanningCycle.getStartWeek();
			startYear=activePlanningCycle.getStartYear();
		}
		Integer week=Integer.parseInt(startWeek);
		Integer year =Integer.parseInt(startYear);
		
		// load defualt Report filters data
		if(ApplicationUtil.isEmptyOrNull(selectedFilterIndexStr)){
			Map reportFiltersListMapObj= new HashMap();
			Map selecteReportFilterValuesMapObj = new HashMap();
			StringBuffer filterWhereClauseSB = new StringBuffer();
			if(pageTemplateFilters!=null){
				String selectedFilterFieldNameStr=null;
				String selectedFilterVariableStr=null;
				String selectedFilterValueStr=null;
				int selectedFilterIndex=0;
				int size = pageTemplateFilters.size();
				for(int i=0;i<size;i++){
					PageTemplateFilters pageTemplateFilter = (PageTemplateFilters) pageTemplateFilters.get(i);
					String filterFieldName = pageTemplateFilter.getFilterFieldName();
					String filterLabel=pageTemplateFilter.getFilterLabel();
					String filterVariable=null;
					String filterConditionValue=null;
					if(pageTemplateFilter.getDefaultFilterValues()==1){
						selectedFilterIndex=i;
						selectedFilterFieldNameStr = filterFieldName;
						filterVariable = pageTemplateFilter.getFilterVariable();
						selectedFilterVariableStr =filterVariable; 
						String tableName = pageTemplateFilter.getTableName();
						List filtersListObj = planningService.getPageTemplateFilters(userId,filterVariable,tableName,filterWhereClauseSB.toString(),null,null,week,year);
						reportFiltersListMapObj.put(filterFieldName, filtersListObj);
						if(filtersListObj != null && filtersListObj.size()>0){
							filterConditionValue=filtersListObj.get(0).toString();
							selectedFilterValueStr =filterConditionValue; 
							selecteReportFilterValuesMapObj.put(filterFieldName, filterConditionValue);
							filterWhereClauseSB.append(" and ");
							filterWhereClauseSB.append(filterVariable+" =  ");
							filterWhereClauseSB.append("'"+filterConditionValue+"' ");
						}
					}
				}
				reportBean.setSelectedFilterIndex(selectedFilterIndex+"");
				if(selectedFilterIndex+1<size){
					PageTemplateFilters selectedPageTemplateFilter = (PageTemplateFilters)pageTemplateFilters.get(selectedFilterIndex);
					String selectedFilterVariableName = selectedPageTemplateFilter.getFilterVariable();
					String selectedFilterFieldName = selectedPageTemplateFilter.getFilterFieldName();
					String selectedFilterValue = (String)selecteReportFilterValuesMapObj.get(selectedFilterFieldName);
					PageTemplateFilters nextPageTemplateFilter = (PageTemplateFilters)pageTemplateFilters.get(selectedFilterIndex+1);
					String filterFieldName = nextPageTemplateFilter.getFilterFieldName();
					if(nextPageTemplateFilter.getDefaultFilterValues()!=1){
						 activefilterVariable = nextPageTemplateFilter.getFilterVariable();
						String tableName = nextPageTemplateFilter.getTableName();
						List filtersListObj = planningService.getPageTemplateFilters(userId,activefilterVariable,tableName,filterWhereClauseSB.toString(),null,null,week,year);
						reportFiltersListMapObj.put(filterFieldName, filtersListObj);
					}
				}
				type=selectedFilterFieldNameStr;
				selectedTypeVariable = selectedFilterVariableStr;
				selectedTypeValue = selectedFilterValueStr;
				reportBean.setType(type);
				reportBean.setSelectedTypeValue(selectedTypeValue);
				reportBean.setSelectedTypeVariable(selectedTypeVariable);
			}
			ApplicationUtil.setObjectInSession(ApplicationConstants.SELECTED_REPORT_FILTER_VALUES_MAP,selecteReportFilterValuesMapObj, request);
			ApplicationUtil.setObjectInSession(ApplicationConstants.REPORT_FILTER_LIST_VALUES_MAP, reportFiltersListMapObj, request);
		}
		// end 
		
		
		//ON CHANGE REPORT FILTERS
		
				if(!ApplicationUtil.isEmptyOrNull(selectedFilterIndexStr)){
					String[] selectedFilterIndexArray= selectedFilterIndexStr.split(",");
					int selectedFilterIndex =Integer.parseInt(selectedFilterIndexArray[0]);
					Map filtersListMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.REPORT_FILTER_LIST_VALUES_MAP, request);
					if(filtersListMapObj == null){
						filtersListMapObj = new HashMap();
					}
					Map selectedFilterValuesMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.SELECTED_REPORT_FILTER_VALUES_MAP, request);
					if(selectedFilterValuesMapObj==null){
						selectedFilterValuesMapObj = new HashMap();
					}
					List filters = pageTemplate.getFiltersList();
					String product=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_ID);
					String id = PropertiesUtil.getProperty(ApplicationConstants.ID);
					String skuListId=PropertiesUtil.getProperty(ApplicationConstants.SKU_LIST_ID);
					String roleFilterCondition="";
					/*if(rolesList != null && rolesList.size()>0 && rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){
						roleFilterCondition = " AND data."+skuId+" IN(select skuList.productId from SkuList as skuList where skuList.id IN "
							+ "(select skuUser.skuListId from SkuUserMapping as skuUser where skuUser.userId="+userId+")) ";
					}else if (rolesList != null && rolesList.size()>0 && rolesList.contains(ApplicationConstants.CATEGORY_MANAGER)){
						roleFilterCondition = " AND data."+categoryColumn+" IN(select catList.categoryName from CategoryList as catList where catList.id IN "
							+ "(select catUser.categoryId from CategoryUserMapping as catUser where catUser.userId="+userId+")) ";
					}*/
					StringBuffer filterWhereClauseSB = new StringBuffer();
					if(pageTemplateFilters!=null){
						List filtersList = new ArrayList();
						int size = pageTemplateFilters.size();
						for(int i=0;i<size;i++){
							PageTemplateFilters pageTemplateFilter = (PageTemplateFilters) pageTemplateFilters.get(i);
							String filterFieldName = pageTemplateFilter.getFilterFieldName();
							String variableName = pageTemplateFilter.getFilterVariable();
							String selectedFilterValue  = request.getParameter(filterFieldName);
							if(i==selectedFilterIndex && !ApplicationUtil.isEmptyOrNull(selectedFilterValue) && selectedFilterValue.equalsIgnoreCase("select")){
								selectedFilterIndex=selectedFilterIndex-1;
								i=i-2;
								continue;
							}
							if(i==selectedFilterIndex){
								type= filterFieldName;
								selectedTypeVariable = variableName;
								selectedTypeValue = selectedFilterValue;
								reportBean.setType(type);
								reportBean.setSelectedTypeValue(selectedTypeValue);
								reportBean.setSelectedTypeVariable(selectedTypeVariable);
							}
							if(i<=selectedFilterIndex){
								if(!ApplicationUtil.isEmptyOrNull(selectedFilterValue) && !selectedFilterValue.equalsIgnoreCase("select")){
									selectedFilterValuesMapObj.put(filterFieldName, selectedFilterValue);
									filterWhereClauseSB.append(" and ");
									filterWhereClauseSB.append(variableName+" =  ");
									filterWhereClauseSB.append("'"+selectedFilterValue+"' ");
								}
							}else{
								selectedFilterValuesMapObj.put(filterFieldName, null);
								filtersListMapObj.put(filterFieldName, null);
							}
						}
						
						if(selectedFilterIndex+1<size){
							PageTemplateFilters nextPageTemplateFilter = (PageTemplateFilters)pageTemplateFilters.get(selectedFilterIndex+1);
							String filterFieldName = nextPageTemplateFilter.getFilterFieldName();
							activefilterVariable = nextPageTemplateFilter.getFilterVariable();
							String tableName = nextPageTemplateFilter.getTableName();
							List filtersListObj = planningService.getPageTemplateFilters(userId,activefilterVariable,tableName,filterWhereClauseSB.toString(),null,null,week,year);
							filtersListMapObj.put(filterFieldName, filtersListObj);
							ApplicationUtil.setObjectInSession(ApplicationConstants.REPORT_FILTER_LIST_VALUES_MAP, filtersListMapObj, request);
						}
					}
					ApplicationUtil.setObjectInSession(ApplicationConstants.SELECTED_REPORT_FILTER_VALUES_MAP,selectedFilterValuesMapObj, request);
					ApplicationUtil.setObjectInSession("isFromOnChangeFilter", "true", request);
				}
				// End On change Report Filters
				
				
				String actualsWeekRange = PropertiesUtil.getProperty("weeksRange");
				Integer range =Integer.valueOf(actualsWeekRange);
				String business="";
				String plClass="";
				StringBuffer stringBuffer=new StringBuffer();
				if(pageTemplateFilters!=null){
					Map selectedFiltersList=(Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.SELECTED_REPORT_FILTER_VALUES_MAP, request);
					int size = pageTemplateFilters.size();
					for(int i=0;i<size;i++){
						PageTemplateFilters pageTemplateFilter = (PageTemplateFilters) pageTemplateFilters.get(i);
						String filterFieldName = pageTemplateFilter.getFilterFieldName();
						String variableName = pageTemplateFilter.getFilterVariable();
						String filterValue = (String)selectedFiltersList.get(filterFieldName);
						if(!ApplicationUtil.isEmptyOrNull(filterFieldName) && filterFieldName.equalsIgnoreCase("BUSINESS")){
							business = filterValue;
						}
						if(!ApplicationUtil.isEmptyOrNull(filterFieldName) && filterFieldName.equalsIgnoreCase("PLCLASS")){
							plClass = filterValue;
						}
						if(!ApplicationUtil.isEmptyOrNull(filterValue) && !filterValue.equalsIgnoreCase("select")){
							stringBuffer.append(" and "+variableName+"='"+filterValue+"'");
						}
					}
				}
				Map targetTableViewMapObj = new HashMap();
				Map targetTablecolorRangeMapObj = new HashMap();
				if(pageTemplateCharts!=null && planningCycleId!=0){
					int size = pageTemplateCharts.size();
					for(int i=0;i<size;i++){
						PageTemplateCharts pageTemplateChart = (PageTemplateCharts) pageTemplateCharts.get(i);
						String tableName = pageTemplateChart.getTableName();
						String selectedPeriod=reportBean.getSelectedPeriod();
						if(ApplicationUtil.isEmptyOrNull(selectedPeriod)){
							selectedPeriod="Weekly";
						}
						System.out.println(selectedPeriod);
						reportService.getReportTableView(targetTableViewMapObj,tableName,planningCycleId,activefilterVariable,business,stringBuffer.toString(),week,year,range,selectedTypeValue,selectedPeriod,targetTablecolorRangeMapObj) ;
					}
				}
				reportBean.setTargetTableMap(targetTableViewMapObj);
				reportBean.setTableColorRange((Integer) targetTablecolorRangeMapObj.get("tableColorRange"));
		String view = "report";
		ApplicationUtil.setObjectInSession("isFrom", "categoryLevelReport", request);
		ApplicationUtil.setObjectInSession("isFrom1", "planningCycleModule", request);
		if(rolesList!= null && rolesList.contains(ApplicationConstants.FINANCE_DIRECTOR)){
			view = "financeLevelreport";
		}
		if(!ApplicationUtil.isEmptyOrNull(isFromReportModule) && isFromReportModule.equalsIgnoreCase("true")){
			view = "executiveReport";
			ApplicationUtil.setObjectInSession("isFrom1", "reportModule", request);
		}
		reportBean.setBusiness(business);
		reportBean.setPlClass(plClass);
		ApplicationUtil.setObjectInSession("isFrom", "categoryLevelReport", request);
		ApplicationUtil.setObjectInSession("headerFrom", "reports", request);
		map.addAttribute(ApplicationConstants.REPORT_BEAN, reportBean);
		return new ModelAndView(view, "model",reportBean);
	}
		
	@RequestMapping(value = "/exportExecutiveSummary.htm")
	public HttpEntity<byte[]> exportExecutiveSummary(
			@ModelAttribute("reportBean") ReportBean reportBean,
			BindingResult result, HttpServletRequest request,ModelMap map) throws ApplicationException {
		byte excelbytes[] =null;
		HttpHeaders headers = new HttpHeaders();
		try {
			Map targetTableMap = reportBean.getTargetTableMap();
			Integer tableColorRange = reportBean.getTableColorRange();
			String reportType = reportBean.getReportType()+"";
			String width="";
			String numberOfActualsStr= PropertiesUtil.getProperty("numberOfActuals");
            int numberOfActuals=0;
            if(!ApplicationUtil.isEmptyOrNull(numberOfActualsStr)){
             numberOfActuals=Integer.parseInt(numberOfActualsStr);
            }
            XSSFWorkbook workbook= new XSSFWorkbook();
            XSSFSheet sheet =  workbook.createSheet("Executive Summary");
            XSSFCellStyle cs = workbook.createCellStyle();
            cs.setWrapText(true);
            CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
            headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
            CellStyle leftHeaderStyle = sheet.getWorkbook().createCellStyle();
            leftHeaderStyle.setAlignment(CellStyle.VERTICAL_CENTER);

            CellStyle aquaColorStyle = workbook.createCellStyle();
            aquaColorStyle.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
            aquaColorStyle.setFillPattern(CellStyle.ALIGN_FILL);
                
    	    CellStyle pinkColorStyle = workbook.createCellStyle();
    	    pinkColorStyle.setFillBackgroundColor(IndexedColors.PINK.getIndex());
    	    pinkColorStyle.setFillPattern(CellStyle.ALIGN_FILL);
    	    
    	    CellStyle greenColorStyle = workbook.createCellStyle();
    	    greenColorStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
    	    greenColorStyle.setFillPattern(CellStyle.ALIGN_FILL);
    	    
    	    CellStyle greyColorStyle = workbook.createCellStyle();
    	    greyColorStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    	    greyColorStyle.setFillPattern(CellStyle.ALIGN_FILL);
	        int rowIndex=0;
	        Row row = sheet.createRow(rowIndex);
	        rowIndex++;
	        Cell c = row.createCell(0);
	        c.setCellValue("Filters:");
	        
	        row = sheet.createRow(rowIndex);
	        rowIndex++;
	        
	        c = row.createCell(0);
	        c.setCellValue("Planning Cycle:");
	        c = row.createCell(1);
	        c.setCellValue(reportBean.getPlanningCycle());
	        c = row.createCell(2);
	        
	        c = row.createCell(3);
	        c.setCellValue("Business:");
	        c = row.createCell(4);
	        c.setCellValue(reportBean.getBusiness());
	        c = row.createCell(5);
	        
	        c = row.createCell(6);
	        c.setCellValue("PL Class:");
	        c = row.createCell(7);
	        c.setCellValue(reportBean.getPlClass());
	        c = row.createCell(8);
	        
	        c = row.createCell(9);
	        c.setCellValue("Category Name:");
	        c = row.createCell(10);
	        c.setCellValue(reportBean.getSelectedTypeValue());
	        c = row.createCell(11);
	        String prefix="";
			String suffix="";
			String tempStr1="";
			String tempStr2="";
	        
	        row = sheet.createRow(rowIndex);
	        rowIndex++;
			Iterator iterator = targetTableMap.entrySet().iterator();
			while (iterator.hasNext()) {
				row = sheet.createRow(rowIndex);
		        rowIndex++;
				Map.Entry entry = (Map.Entry) iterator.next();
				//out.println("<div class=\"panel-heading\" ><b>"+entry.getKey()+"</b></div>");
		        c = row.createCell(0);
		        String tableHeader=(String) entry.getKey();
		        c.setCellValue(entry.getKey().toString());
				List tableListObj = (List) entry.getValue();
				for(int i=0;i<tableListObj.size();i++){
					Map entryMap=	(Map) tableListObj.get(i);
					Iterator it = entryMap.entrySet().iterator();
					while (it.hasNext()){
						//out.println("<table class=\"table table-striped table-bordered table-hover\">");
						//out.println("<tbody>");
						Map.Entry mapEntry = (Map.Entry) it.next();
						List valueList=(List) mapEntry.getValue();
						List categoryValues=null;
						List targetValues=null;
						for(int j=0;j<valueList.size();j++){
							if(!ApplicationUtil.isEmptyOrNull(tableHeader) && (tableHeader.equalsIgnoreCase("ASP") || tableHeader.equalsIgnoreCase("ESC") || tableHeader.equalsIgnoreCase("Revenue")) ){
								prefix="$";
							}else {
								prefix="";
							}
							if(!ApplicationUtil.isEmptyOrNull(tableHeader) && tableHeader.equalsIgnoreCase("PM%")){
								suffix="%";
							}else{
								suffix="";
							}
							List value=(List) valueList.get(j);
							if(j==0){
								//out.println("<tr>");
								row = sheet.createRow(rowIndex);
								rowIndex++;
								for(int k=0;k<value.size();k++){
									c = row.createCell(k);
									if(k==0){
										//width="120px";
									}else{
										//width="";
									}
									if(k<tableColorRange+1){
										//out.println("<td style=\"min-width:"+width+";\">"+value.get(k)+"</td>");
										c.setCellValue((String)value.get(k));
										c.setCellStyle(greyColorStyle);
									} else{
										//out.println("<td style=\"background-color: #8FD8D8;min-width:"+width+";\">"+value.get(k)+"</td>");
										c.setCellValue((String)value.get(k));
										c.setCellStyle(aquaColorStyle);
									}
									
								}
								//out.println("</tr>");
								
							} else{
								//out.println("<tr>");
								row = sheet.createRow(rowIndex);
								rowIndex++;
								for(int k=0;k<value.size();k++){
									c = row.createCell(k);
									if(k==0){
										//width="120px";
										tempStr1=prefix;
										tempStr2=suffix;
										prefix="";
										suffix="";
									}else{
										//width="";
										prefix=tempStr1;
										suffix=tempStr2;
									}
									if(j==1){
										if(k==0){
											categoryValues=new ArrayList();
										}
										
										categoryValues.add(value.get(k));
									}
									if(j==2){
										if(k==0){
											targetValues=new ArrayList();
										}
										
										targetValues.add(value.get(k));
									}
									String tableCellColor="";
									if(j==3 && k!=0 && !ApplicationUtil.isEmptyOrNull(reportType) && reportType.equalsIgnoreCase("report")){
										if(!(targetValues.get(k)).equals("--") && !ApplicationUtil.isEmptyOrNull((String) categoryValues.get(k)) && !ApplicationUtil.isEmptyOrNull((String) targetValues.get(k))){
											double actualValue =Double.parseDouble((String) categoryValues.get(k));
		                                	double targetValue =Double.parseDouble((String) targetValues.get(k));
											if(actualValue<targetValue){
												c.setCellStyle(pinkColorStyle);
											}else{
												c.setCellStyle(greenColorStyle);
											}
										}
										
									}
									String cellValue=(String) value.get(k);
									if(!ApplicationUtil.isEmptyOrNull(reportType) && (reportType.equalsIgnoreCase("planogram") || ApplicationUtil.isEmptyOrNull(cellValue)) || cellValue.equalsIgnoreCase("--")){
										prefix="";
										suffix="";
									}
									if(j==valueList.size()-1){
										prefix="";
										suffix="";
									}
									c.setCellValue(prefix+(String)value.get(k)+suffix);
									//out.println("<td style=\"background-color:"+tableCellColor+";min-width:"+width+";\">"+value.get(k)+"</td>");
								}
								//out.println("</tr>");
							}
						}
						//out.println("</table>");
						//out.println("</tbody>");
					}
				
				}
				//out.println("</div>");
				//out.println("<br/>");
			}
			ServletContext sc = request.getSession().getServletContext();
			String filePath = sc.getRealPath("/WEB-INF");
			String newFilePath = filePath + "\\temp";
	        System.out.println("Path:: "+newFilePath);
	        File file = new File(newFilePath);
			if (!file.exists()) {
				file.mkdir();
			}
			String destinationPathFileStr = newFilePath+"/Executive_Summary_Report.xlsx";
			try {
	    		FileOutputStream fileOut =  new FileOutputStream(destinationPathFileStr);
				workbook.write(fileOut);
				fileOut.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			file = new File(destinationPathFileStr);
			headers.setContentLength((int)file.length());
	        headers.set("Content-Disposition", "attachment;filename=\"Executive_Summary_Report.xlsx\"");
	        headers.setContentType(new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
	        try {
	               FileInputStream in = new FileInputStream(file);
	               excelbytes = new byte[(int)file.length()];
	               in.read(excelbytes);
	        } catch (FileNotFoundException e) {
	               // TODO Auto-generated catch block
	     	   logger.error(e.getMessage());
	        } catch (IOException e) {
	               // TODO Auto-generated catch block
	               logger.error(e.getMessage());
	        }
	        
	        
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// Must return SKIP_BODY because we are not supporting a body for this
		// tag.
		return new HttpEntity<byte[]>(excelbytes,headers);
	}
	
	//Flat file export
    
    @RequestMapping(value = "/exportFlatFile.htm")
    public HttpEntity<byte[]> exportFlatFile(
                  @ModelAttribute("newBean") ReportBean reportBean,
                  BindingResult result, HttpServletRequest request,ModelMap map) throws ApplicationException {
           byte excelbytes[] =null;
           HttpHeaders headers = new HttpHeaders();
           try {
                  PlanningCycle activePlanningCycle = (PlanningCycle)ApplicationUtil.getObjectFromSession(ApplicationConstants.ACTIVE_PLANNINGCYCLE_OBJ, request);
                  int planningCycleId = activePlanningCycle.getId();
                  String startWeek=activePlanningCycle.getStartWeek();
                  Integer week=Integer.parseInt(startWeek);
                  String startYear=activePlanningCycle.getStartYear();
                  Integer year =Integer.parseInt(startYear);
                  List flatFileList=reportService.getFlatFile(planningCycleId,week,year);
                  XSSFWorkbook workbook= new XSSFWorkbook();
                  XSSFSheet sheet =  workbook.createSheet("Executive Report");
         XSSFCellStyle cs = workbook.createCellStyle();
         cs.setWrapText(true);
         CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
         headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
         CellStyle leftHeaderStyle = sheet.getWorkbook().createCellStyle();
         leftHeaderStyle.setAlignment(CellStyle.VERTICAL_CENTER);
         int size=flatFileList.size();
         Row row =  null;
         Cell  c=null;
        /* Cell  c = row.createCell(0);
           c.setCellValue("Order Week (BV)");
           
            c = row.createCell(1);
            c.setCellValue("Sales Office");
            
            c = row.createCell(2);
            c.setCellValue("Sales Centers");
            
            c = row.createCell(3);
            c.setCellValue("Agent Functional Group");
            
            c = row.createCell(4);
            c.setCellValue("Product Line");
            
            c = row.createCell(5);
            c.setCellValue("Product Hierarchy I");
            
            c = row.createCell(6);
            c.setCellValue("Product Hierarchy II");
            
            c = row.createCell(7);
            c.setCellValue("Product Hierarchy IV");
            
            c = row.createCell(8);
            c.setCellValue("Product Number");
            
            c = row.createCell(9);
            c.setCellValue("PL Class");
            
            c = row.createCell(10);
            c.setCellValue("Product Description");
            
            c = row.createCell(11);
            c.setCellValue("Gross Dollars");
            
            c = row.createCell(12);
            c.setCellValue("Less Discounts");
            
            c = row.createCell(13);
            c.setCellValue("Number of Orders");
            
            c = row.createCell(14);
            c.setCellValue("Quantity");
           
            c = row.createCell(15);
            c.setCellValue("ESC (Final)");
            
            c = row.createCell(16);
            c.setCellValue("ESC/Unit");
            
            c = row.createCell(17);
            c.setCellValue("Product Margin");
            
            c = row.createCell(18);
            c.setCellValue("Product Category");
            
            c = row.createCell(19);
            c.setCellValue("Model Roll Up");
            
            c = row.createCell(20);
            c.setCellValue("Model");
            
            c = row.createCell(21);
            c.setCellValue("Processor");
            
            c = row.createCell(22);
            c.setCellValue("Week Range");
            
            c = row.createCell(23);
            c.setCellValue("Op System");
            
            c = row.createCell(24);
            c.setCellValue("ASP");
            
            c = row.createCell(25);
            c.setCellValue("Display");
            
            c = row.createCell(26);
            c.setCellValue("Price Band");
            
            c = row.createCell(27);
            c.setCellValue("Business");
            
            c = row.createCell(28);
            c.setCellValue("PL Roll Up");
            
            c = row.createCell(29);
            c.setCellValue("Scorecard Roll up");
            
            c = row.createCell(30);
            c.setCellValue("Planogram");*/
            
         for(int i=0;i<size;i++){
           row = sheet.createRow(i);
           List rowList=(List) flatFileList.get(i);
           int rowListSize=rowList.size();
           for(int j=0;j<rowListSize;j++){
                   c = row.createCell(j);
                   c.setCellValue(rowList.get(j).toString());
           }
           
         }
                  
                  ServletContext sc = request.getSession().getServletContext();
                  String filePath = sc.getRealPath("/WEB-INF");
                  String newFilePath = filePath + "\\temp";
            System.out.println("Path:: "+newFilePath);
            File file = new File(newFilePath);
                  if (!file.exists()) {
                        file.mkdir();
                  }
                  String destinationPathFileStr = newFilePath+"/FlatFile_Summary_Report.xlsx";
                  try {
                  FileOutputStream fileOut =  new FileOutputStream(destinationPathFileStr);
                        workbook.write(fileOut);
                        fileOut.close();
                  } catch (IOException e1) {
                        // TODO Auto-generated catch block
                         e1.printStackTrace();
                  }
                  file = new File(destinationPathFileStr);
                  headers.setContentLength((int)file.length());
            headers.set("Content-Disposition", "attachment;filename=\"FlatFile_Summary_Report.xlsx\"");
            headers.setContentType(new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            try {
                   FileInputStream in = new FileInputStream(file);
                   excelbytes = new byte[(int)file.length()];
                   in.read(excelbytes);
            } catch (FileNotFoundException e) {
                   // TODO Auto-generated catch block
              logger.error(e.getMessage());
            } catch (IOException e) {
                   // TODO Auto-generated catch block
                   logger.error(e.getMessage());
            }
            
            
           } catch (Exception ex) {
                  ex.printStackTrace();
           }
           // Must return SKIP_BODY because we are not supporting a body for this
           // tag.
           return new HttpEntity<byte[]>(excelbytes,headers);
    }

	
	
//	Override Reports
	
	@RequestMapping(value = "/overrideReport.htm")
	public ModelAndView getOverrideReportDetails(
			@ModelAttribute("newBean") ReportBean reportBean,
			BindingResult result, HttpServletRequest request,ModelMap map) throws ApplicationException {
		int planningCycleId = reportBean.getPlanningCycleId();
		String type= reportBean.getType();
		String selectedTypeValue = reportBean.getSelectedTypeValue();
		String selectedTypeVariable = reportBean.getSelectedTypeVariable();
		Map pageTemplateCacheObj = (Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.PAGE_TEMPLATES_CACHE_KEY);	
		PageTemplate pageTemplate = (PageTemplate)pageTemplateCacheObj.get("PC_DASHBOARD");
		List pageTemplateCharts = pageTemplate.getPageTemplateCharts();
		List pageTemplateFilters = pageTemplate.getPageTemplateFilters();
		String selectedFilterIndexStr = reportBean.getSelectedFilterIndex();
		Map closedPlanningCycleMap = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.ALL_PLANNING_CYCLE_LIST, request);
		if(closedPlanningCycleMap == null){
			closedPlanningCycleMap = reportService.getClosedReportPlanningCycleList(true);
			ApplicationUtil.setObjectInSession(ApplicationConstants.ALL_PLANNING_CYCLE_LIST, closedPlanningCycleMap, request);
		}
		String startWeek="0";
		String startYear="0";
		PlanningCycle closedPlanningCycle=(PlanningCycle) closedPlanningCycleMap.get(planningCycleId);
		if(closedPlanningCycle!=null){
			startWeek=closedPlanningCycle.getStartWeek();
			startYear=closedPlanningCycle.getStartYear();
			reportBean.setPlanningCycle(closedPlanningCycle.getWeekYear());
		}
		Integer week=Integer.parseInt(startWeek);
		Integer year =Integer.parseInt(startYear);
		ApplicationUtil.removeObjectFromSession("isFromOnChangeFilter", request);
		String activefilterVariable="";
		// load default Report filters data
		StringBuffer filterWhereClauseSB =null;
		if(ApplicationUtil.isEmptyOrNull(selectedFilterIndexStr)){
			Map overrideReportFiltersListMapObj= new HashMap();
			Map selectedOverrideReportFilterValuesMapObj = new HashMap();
			 filterWhereClauseSB = new StringBuffer();
			if(pageTemplateFilters!=null){
				String selectedFilterFieldNameStr=null;
				String selectedFilterVariableStr=null;
				String selectedFilterValueStr=null;
				int selectedFilterIndex=0;
				int size = pageTemplateFilters.size();
				for(int i=0;i<size;i++){
					PageTemplateFilters pageTemplateFilter = (PageTemplateFilters) pageTemplateFilters.get(i);
					String filterFieldName = pageTemplateFilter.getFilterFieldName();
					String filterLabel=pageTemplateFilter.getFilterLabel();
					String filterVariable=null;
					String filterConditionValue=null;
					if(pageTemplateFilter.getDefaultFilterValues()==1){
						selectedFilterIndex=i;
						selectedFilterFieldNameStr = filterFieldName;
						filterVariable = pageTemplateFilter.getFilterVariable();
						selectedFilterVariableStr =filterVariable; 
						String tableName = pageTemplateFilter.getTableName();
						List filtersListObj = planningService.getPageTemplateFilters(-1,filterVariable,tableName,filterWhereClauseSB.toString(),null,null,week,year);
						overrideReportFiltersListMapObj.put(filterFieldName, filtersListObj);
						if(filtersListObj != null && filtersListObj.size()>0){
							filterConditionValue=filtersListObj.get(0).toString();
							selectedFilterValueStr =filterConditionValue; 
							selectedOverrideReportFilterValuesMapObj.put(filterFieldName, filterConditionValue);
							filterWhereClauseSB.append(" and ");
							filterWhereClauseSB.append(filterVariable+" =  ");
							filterWhereClauseSB.append("'"+filterConditionValue+"' ");
						}
					}
				}
				
				if(selectedFilterIndex+1<size){
					PageTemplateFilters selectedPageTemplateFilter = (PageTemplateFilters)pageTemplateFilters.get(selectedFilterIndex);
					String selectedFilterVariableName = selectedPageTemplateFilter.getFilterVariable();
					String selectedFilterFieldName = selectedPageTemplateFilter.getFilterFieldName();
					String selectedFilterValue = (String)selectedOverrideReportFilterValuesMapObj.get(selectedFilterFieldName);
					PageTemplateFilters nextPageTemplateFilter = (PageTemplateFilters)pageTemplateFilters.get(selectedFilterIndex+1);
					String filterFieldName = nextPageTemplateFilter.getFilterFieldName();
					if(nextPageTemplateFilter.getDefaultFilterValues()!=1){
						 activefilterVariable = nextPageTemplateFilter.getFilterVariable();
						String tableName = nextPageTemplateFilter.getTableName();
						List filtersListObj = planningService.getPageTemplateFilters(-1,activefilterVariable,tableName,filterWhereClauseSB.toString(),null,null,week,year);
						overrideReportFiltersListMapObj.put(filterFieldName, filtersListObj);
					}
				}
				type=selectedFilterFieldNameStr;
				selectedTypeVariable = selectedFilterVariableStr;
				selectedTypeValue = selectedFilterValueStr;
				reportBean.setType(type);
				reportBean.setSelectedTypeValue(selectedTypeValue);
				reportBean.setSelectedTypeVariable(selectedTypeVariable);
			}
			ApplicationUtil.setObjectInSession(ApplicationConstants.SELECTED_OVERRIDE_REPORT_FILTER_VALUES_MAP,selectedOverrideReportFilterValuesMapObj, request);
			ApplicationUtil.setObjectInSession(ApplicationConstants.OVERRIDE_REPORT_FILTER_LIST_VALUES_MAP, overrideReportFiltersListMapObj, request);
		}
		// end 
		
		
		//ON CHANGE REPORT FILTERS
		//StringBuffer filterWhereClauseSB =null;
				if(!ApplicationUtil.isEmptyOrNull(selectedFilterIndexStr)){
					ApplicationUtil.setObjectInSession("isFromOnChangeFilter", "true", request);
					String[] selectedFilterIndexArray= selectedFilterIndexStr.split(",");
					int selectedFilterIndex =Integer.parseInt(selectedFilterIndexArray[0]);
					Map OverrideFiltersListMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.OVERRIDE_REPORT_FILTER_LIST_VALUES_MAP, request);
					if(OverrideFiltersListMapObj == null){
						OverrideFiltersListMapObj = new HashMap();
					}
					Map selectedOverrideReportFilterValuesMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.SELECTED_OVERRIDE_REPORT_FILTER_VALUES_MAP, request);
					if(selectedOverrideReportFilterValuesMapObj==null){
						selectedOverrideReportFilterValuesMapObj = new HashMap();
					}
					List filters = pageTemplate.getFiltersList();
					String product=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_ID);
					String id = PropertiesUtil.getProperty(ApplicationConstants.ID);
					String skuListId=PropertiesUtil.getProperty(ApplicationConstants.SKU_LIST_ID);
					/*if(rolesList != null && rolesList.size()>0 && rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){
						roleFilterCondition = " AND data."+skuId+" IN(select skuList.productId from SkuList as skuList where skuList.id IN "
							+ "(select skuUser.skuListId from SkuUserMapping as skuUser where skuUser.userId="+userId+")) ";
					}else if (rolesList != null && rolesList.size()>0 && rolesList.contains(ApplicationConstants.CATEGORY_MANAGER)){
						roleFilterCondition = " AND data."+categoryColumn+" IN(select catList.categoryName from CategoryList as catList where catList.id IN "
							+ "(select catUser.categoryId from CategoryUserMapping as catUser where catUser.userId="+userId+")) ";
					}*/
					 filterWhereClauseSB = new StringBuffer();
					if(pageTemplateFilters!=null){
						List filtersList = new ArrayList();
						int size = pageTemplateFilters.size();
						for(int i=0;i<size;i++){
							PageTemplateFilters pageTemplateFilter = (PageTemplateFilters) pageTemplateFilters.get(i);
							String filterFieldName = pageTemplateFilter.getFilterFieldName();
							String variableName = pageTemplateFilter.getFilterVariable();
							String selectedFilterValue  = request.getParameter(filterFieldName);
							if(i==selectedFilterIndex && !ApplicationUtil.isEmptyOrNull(selectedFilterValue) && selectedFilterValue.equalsIgnoreCase("select")){
								selectedFilterIndex=selectedFilterIndex-1;
								i=i-2;
								continue;
							}
							if(i==selectedFilterIndex){
								type= filterFieldName;
								selectedTypeVariable = variableName;
								selectedTypeValue = selectedFilterValue;
								reportBean.setType(type);
								reportBean.setSelectedTypeValue(selectedTypeValue);
								reportBean.setSelectedTypeVariable(selectedTypeVariable);
							}
							if(i<=selectedFilterIndex){
								if(!ApplicationUtil.isEmptyOrNull(selectedFilterValue) && !selectedFilterValue.equalsIgnoreCase("select")){
									selectedOverrideReportFilterValuesMapObj.put(filterFieldName, selectedFilterValue);
									filterWhereClauseSB.append(" and ");
									filterWhereClauseSB.append(variableName+" =  ");
									filterWhereClauseSB.append("'"+selectedFilterValue+"' ");
								}
							}else{
								selectedOverrideReportFilterValuesMapObj.put(filterFieldName, null);
								OverrideFiltersListMapObj.put(filterFieldName, null);
							}
						}
						
						if(selectedFilterIndex+1<size){
							PageTemplateFilters nextPageTemplateFilter = (PageTemplateFilters)pageTemplateFilters.get(selectedFilterIndex+1);
							String filterFieldName = nextPageTemplateFilter.getFilterFieldName();
							activefilterVariable = nextPageTemplateFilter.getFilterVariable();
							String tableName = nextPageTemplateFilter.getTableName();
							List filtersListObj = planningService.getPageTemplateFilters(-1,activefilterVariable,tableName,filterWhereClauseSB.toString(),null,null,week,year);
							OverrideFiltersListMapObj.put(filterFieldName, filtersListObj);
							ApplicationUtil.setObjectInSession(ApplicationConstants.OVERRIDE_REPORT_FILTER_LIST_VALUES_MAP, OverrideFiltersListMapObj, request);
						}
					}
					ApplicationUtil.setObjectInSession(ApplicationConstants.SELECTED_OVERRIDE_REPORT_FILTER_VALUES_MAP,selectedOverrideReportFilterValuesMapObj, request);
				}
				// End On change Report Filters
				StringBuffer stringBuffer=new StringBuffer();
				Map selectedOverrideReportFilterValuesMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.SELECTED_OVERRIDE_REPORT_FILTER_VALUES_MAP, request);
				String productId=(String)selectedOverrideReportFilterValuesMapObj.get("PRODUCT");
				//String overrideLevel=
				String modelId=(String)selectedOverrideReportFilterValuesMapObj.get("MODEL");
				String categoryId=(String)selectedOverrideReportFilterValuesMapObj.get("CATEGORY");
				
				List skuLevelObj = reportService.getOverrideReport(planningCycleId ,productId,modelId,categoryId,filterWhereClauseSB.toString());
				
				reportBean.setOverrideReportListObj(skuLevelObj);
				
				String view = "overrideReport";
				map.addAttribute(ApplicationConstants.REPORT_BEAN, reportBean);
				ApplicationUtil.setObjectInSession("isFrom", "overrideReport", request);
				ApplicationUtil.setObjectInSession("headerFrom", "reports", request);
				return new ModelAndView(view, "model", reportBean);

	}
	
	@RequestMapping(value = "/accuracyReport.htm")
	public ModelAndView getAccuracyReport(
			@ModelAttribute("newBean") ReportBean reportBean,
			BindingResult result, HttpServletRequest request,ModelMap map) throws ApplicationException {
		
		Map pageTemplateCacheObj = (Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.PAGE_TEMPLATES_CACHE_KEY);	
		PageTemplate pageTemplate = (PageTemplate)pageTemplateCacheObj.get("PC_DASHBOARD");
		List pageTemplateCharts = pageTemplate.getPageTemplateCharts();
		List pageTemplateFilters = pageTemplate.getPageTemplateFilters();
		Users users = (Users) ApplicationUtil.getObjectFromSession(ApplicationConstants.APPLICATION_LOGGED_IN_USER, request);
		List rolesList = users.getRolesList();
		Integer userId=users.getLogin_id();
		String selectedFilterIndexStr = reportBean.getSelectedFilterIndex();
		String noOfWeeks=reportBean.getNoOfWeeks();
		int reportType=reportBean.getReportType();
		Map  jsonViewMapObject=null;
		String selectedFilters=reportBean.getIsFiltersSelected();
		if(reportType==0){
			reportType=1;
		}
		//functionality

		String business=null;
		ApplicationUtil.removeObjectFromSession("isFromOnChangeFilter", request);
		if(ApplicationUtil.isEmptyOrNull(selectedFilterIndexStr)){
			Map accuracyReportFiltersListMapObj= new HashMap();
			Map selectedAccuracyReportFilterValuesMapObj = new HashMap();
			StringBuffer filterWhereClauseSB = new StringBuffer();
			if(pageTemplateFilters!=null ){
				String selectedFilterFieldNameStr=null;
				String selectedFilterVariableStr=null;
				String selectedFilterValueStr=null;
				int selectedFilterIndex=0;
				int size = pageTemplateFilters.size();
				for(int i=0;i<size;i++){
					PageTemplateFilters pageTemplateFilter = (PageTemplateFilters) pageTemplateFilters.get(i);
					String filterFieldName = pageTemplateFilter.getFilterFieldName();
					String filterLabel=pageTemplateFilter.getFilterLabel();
					String filterVariable=null;
					String filterConditionValue=null;
					if(pageTemplateFilter.getDefaultFilterValues()==1){
						selectedFilterIndex=i;
						selectedFilterFieldNameStr = filterFieldName;
						filterVariable = pageTemplateFilter.getFilterVariable();
						selectedFilterVariableStr =filterVariable; 
						String tableName = pageTemplateFilter.getTableName();
						List filtersListObj = planningService.getPageTemplateFilters(-1,filterVariable,tableName,filterWhereClauseSB.toString(),null,null,null,null);
						accuracyReportFiltersListMapObj.put(filterFieldName, filtersListObj);
						if(filtersListObj != null && filtersListObj.size()>0){
							filterConditionValue=filtersListObj.get(0).toString();
							selectedFilterValueStr =filterConditionValue; 
							if(!ApplicationUtil.isEmptyOrNull(filterFieldName) && filterFieldName.equalsIgnoreCase("BUSINESS")){
								business = filterConditionValue;
							}
							selectedAccuracyReportFilterValuesMapObj.put(filterFieldName, filterConditionValue);
							filterWhereClauseSB.append(" and ");
							filterWhereClauseSB.append(filterVariable+" =  ");
							filterWhereClauseSB.append("'"+filterConditionValue+"' ");
						}
					}
				}
				reportBean.setSelectedFilterIndex(selectedFilterIndex+"");
				if(selectedFilterIndex+1<size){
					PageTemplateFilters selectedPageTemplateFilter = (PageTemplateFilters)pageTemplateFilters.get(selectedFilterIndex);
					String selectedFilterVariableName = selectedPageTemplateFilter.getFilterVariable();
					String selectedFilterFieldName = selectedPageTemplateFilter.getFilterFieldName();
					String selectedFilterValue = (String)selectedAccuracyReportFilterValuesMapObj.get(selectedFilterFieldName);
					PageTemplateFilters nextPageTemplateFilter = (PageTemplateFilters)pageTemplateFilters.get(selectedFilterIndex+1);
					String filterFieldName = nextPageTemplateFilter.getFilterFieldName();
					if(nextPageTemplateFilter.getDefaultFilterValues()!=1){
						String filterVariable = nextPageTemplateFilter.getFilterVariable();
						String tableName = nextPageTemplateFilter.getTableName();
						List filtersListObj = planningService.getPageTemplateFilters(-1,filterVariable,tableName,filterWhereClauseSB.toString(),null,null,null,null);
						accuracyReportFiltersListMapObj.put(filterFieldName, filtersListObj);
					}
				}
			}
			if(ApplicationUtil.isEmptyOrNull(noOfWeeks)){
				noOfWeeks="6";
			}
			
			try{
				jsonViewMapObject=reportService.getAccuracyReportList(noOfWeeks,filterWhereClauseSB.toString(),reportType,business);
				reportBean.setJsonStrList((List)jsonViewMapObject.get(ApplicationConstants.ACCURACY_LIST));
				reportBean.setJsonTableList((List)jsonViewMapObject.get("jsonTableList"));
				reportBean.setHistoricalJsonStrList((List)jsonViewMapObject.get(ApplicationConstants.HISTORICAL_REPORT_LIST));
			}catch(Exception e){
				e.printStackTrace();
			}
			reportBean.setNoOfWeeks(noOfWeeks);
			reportBean.setReportType(reportType);
			ApplicationUtil.setObjectInSession(ApplicationConstants.ACCURACY_LIST_MAP,jsonViewMapObject, request);
			ApplicationUtil.setObjectInSession(ApplicationConstants.SELECTED_ACCURACY_REPORT_FILTER_VALUES_MAP,selectedAccuracyReportFilterValuesMapObj, request);
			ApplicationUtil.setObjectInSession(ApplicationConstants.ACCURACY_REPORT_FILTER_LIST_VALUES_MAP, accuracyReportFiltersListMapObj, request);
		}
		//-----------
		ApplicationUtil.setObjectInSession("isFrom", "accuracyReport", request);
		ApplicationUtil.setObjectInSession("headerFrom", "reports", request);
		map.addAttribute("reportBean", reportBean);
		return new ModelAndView("accuracyReport", "model",reportBean);
	}
	
	
	@RequestMapping(value = "/accuracyReportOnFilterChange.htm")
	public ModelAndView getAccuracyReportOnchangeFilter(
			@ModelAttribute("reportBean") ReportBean reportBean,
			BindingResult result, HttpServletRequest request,ModelMap map) throws ApplicationException {
		
		try {
			ApplicationUtil.setObjectInSession("isFromOnChangeFilter", "true", request);
			String selectedFilterIndexStr = reportBean.getSelectedFilterIndex();
			String selectedFilterIndexArry[] = selectedFilterIndexStr.split(",");
			int selectedFilterIndex =Integer.parseInt(selectedFilterIndexArry[0]);
			Map accuracyReportFiltersListMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.ACCURACY_REPORT_FILTER_LIST_VALUES_MAP, request);
			if(accuracyReportFiltersListMapObj == null){
				accuracyReportFiltersListMapObj = new HashMap();
			}
			Map selectedAccuracyReportFilterValuesMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.SELECTED_ACCURACY_REPORT_FILTER_VALUES_MAP, request);
			if(selectedAccuracyReportFilterValuesMapObj==null){
				selectedAccuracyReportFilterValuesMapObj = new HashMap();
			}
			Users users = (Users) ApplicationUtil.getObjectFromSession(ApplicationConstants.APPLICATION_LOGGED_IN_USER, request);
			List rolesList = users.getRolesList();
			Integer userId=users.getLogin_id();
			Map pageTemplateCacheObj = (Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.PAGE_TEMPLATES_CACHE_KEY);	
			PageTemplate pageTemplate = (PageTemplate)pageTemplateCacheObj.get("PC_DASHBOARD");
			List pageTemplateFilters = pageTemplate.getPageTemplateFilters();
			List filters = pageTemplate.getFiltersList();
			StringBuffer filterWhereClauseSB = new StringBuffer();
			String noOfWeeks=reportBean.getNoOfWeeks();
			int reportType=reportBean.getReportType();
			if(reportType==0){
				reportType=1;
			}
			String business=null;
			if(pageTemplateFilters!=null){
				List filtersList = new ArrayList();
				int size = pageTemplateFilters.size();
				for(int i=0;i<size;i++){
					PageTemplateFilters pageTemplateFilter = (PageTemplateFilters) pageTemplateFilters.get(i);
					String filterFieldName = pageTemplateFilter.getFilterFieldName();
					String variableName = pageTemplateFilter.getFilterVariable();
					String selectedFilterValue  = request.getParameter(filterFieldName);
					if(i==selectedFilterIndex && !ApplicationUtil.isEmptyOrNull(selectedFilterValue) && selectedFilterValue.equalsIgnoreCase("select")){
						selectedFilterIndex=selectedFilterIndex-1;
					}
					if(i<=selectedFilterIndex){
						if(!ApplicationUtil.isEmptyOrNull(selectedFilterValue) && !selectedFilterValue.equalsIgnoreCase("select")){
							selectedAccuracyReportFilterValuesMapObj.put(filterFieldName, selectedFilterValue);
							filterWhereClauseSB.append(" and ");
							filterWhereClauseSB.append(variableName+" =  ");
							filterWhereClauseSB.append("'"+selectedFilterValue+"' ");
							if(!ApplicationUtil.isEmptyOrNull(filterFieldName) && filterFieldName.equalsIgnoreCase("BUSINESS")){
								business = selectedFilterValue;
							}
						}
					}else{
						selectedAccuracyReportFilterValuesMapObj.put(filterFieldName, null);
						accuracyReportFiltersListMapObj.put(filterFieldName, null);
					}
				}
				reportBean.setSelectedFilterIndex(selectedFilterIndex+"");
				
				if(selectedFilterIndex+1<size){
					PageTemplateFilters nextPageTemplateFilter = (PageTemplateFilters)pageTemplateFilters.get(selectedFilterIndex+1);
					String filterFieldName = nextPageTemplateFilter.getFilterFieldName();
					String filterVariable = nextPageTemplateFilter.getFilterVariable();
					String tableName = nextPageTemplateFilter.getTableName();
					List filtersListObj = planningService.getPageTemplateFilters(-1,filterVariable,tableName,filterWhereClauseSB.toString(),null,null,null,null);
					accuracyReportFiltersListMapObj.put(filterFieldName, filtersListObj);
					ApplicationUtil.setObjectInSession(ApplicationConstants.ACCURACY_REPORT_FILTER_LIST_VALUES_MAP, accuracyReportFiltersListMapObj, request);
				}
			}
			int planningCycleId = reportBean.getPlanningCycleId();
			Map  jsonViewMapObject=reportService.getAccuracyReportList(noOfWeeks,filterWhereClauseSB.toString(),reportType,business);
			reportBean.setJsonStrList((List)jsonViewMapObject.get(ApplicationConstants.ACCURACY_LIST));
			reportBean.setJsonTableList((List)jsonViewMapObject.get("jsonTableList"));
			reportBean.setHistoricalJsonStrList((List)jsonViewMapObject.get(ApplicationConstants.HISTORICAL_REPORT_LIST));
			reportBean.setReportType(reportType);
			reportBean.setIsFiltersSelected("yes");
			ApplicationUtil.setObjectInSession(ApplicationConstants.ACCURACY_LIST_MAP,jsonViewMapObject, request);
			ApplicationUtil.setObjectInSession(ApplicationConstants.SELECTED_ACCURACY_REPORT_FILTER_VALUES_MAP,selectedAccuracyReportFilterValuesMapObj, request);

		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		ApplicationUtil.setObjectInSession("isFrom", "accuracyReport", request);
		ApplicationUtil.setObjectInSession("headerFrom", "reports", request);
			logger.debug("Entering and leaving from accuracyReportOnChangeFilter");
			return new ModelAndView("accuracyReport", "model",reportBean);
		}
	
	//Historical Actuals
	
	@RequestMapping(value = "/historicalActualReport.htm")
	public ModelAndView getHistoricalActualReports(
			@ModelAttribute("newBean") ReportBean reportBean,
			BindingResult result, HttpServletRequest request,ModelMap map) throws ApplicationException {
		int planningCycleId = reportBean.getPlanningCycleId();
		String viewName="historicalActualReport";
		String type= reportBean.getType();
		String selectedTypeValue = reportBean.getSelectedTypeValue();
		String selectedTypeVariable = reportBean.getSelectedTypeVariable();
		String selectedPeriod=reportBean.getSelectedPeriod();
		if(ApplicationUtil.isEmptyOrNull(selectedPeriod)){
			selectedPeriod="Weekly";
		}
		ApplicationUtil.removeObjectFromSession("isFromOnChangeFilter", request);
		Map pageTemplateCacheObj = (Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.PAGE_TEMPLATES_CACHE_KEY);	
		PageTemplate pageTemplate = (PageTemplate)pageTemplateCacheObj.get("HISTORICAL_REPORT");
		List pageTemplateCharts = pageTemplate.getPageTemplateCharts();
		List pageTemplateFilters = pageTemplate.getPageTemplateFilters();
		Users users = (Users) ApplicationUtil.getObjectFromSession(ApplicationConstants.APPLICATION_LOGGED_IN_USER, request);
		List rolesList = users.getRolesList();
		Integer userId=users.getLogin_id();
		String selectedFilterIndexStr = reportBean.getSelectedFilterIndex();
		String skuId=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_NUMBER);
		String categoryColumn = PropertiesUtil.getProperty(ApplicationConstants.CATEGORY);
		String modelColumn = PropertiesUtil.getProperty(ApplicationConstants.MODEL);
		String startWeek="0";
		String startYear="0";
		Map closedPlanningCycleMap = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.CLOSED_PLANNING_CYCLE_LIST, request);
		if(closedPlanningCycleMap == null){
			closedPlanningCycleMap = reportService.getClosedReportPlanningCycleList(false);
			ApplicationUtil.setObjectInSession(ApplicationConstants.CLOSED_PLANNING_CYCLE_LIST, closedPlanningCycleMap, request);
		}
		
		
		PlanningCycle closedPlanningCycle=(PlanningCycle) closedPlanningCycleMap.get(planningCycleId);
		if(closedPlanningCycle!=null){
			startWeek=closedPlanningCycle.getStartWeek();
			startYear=closedPlanningCycle.getStartYear();
		}
		Integer week=Integer.parseInt(startWeek);
		Integer year =Integer.parseInt(startYear);
		
		// load default filters data
		if(ApplicationUtil.isEmptyOrNull(selectedFilterIndexStr)){
			Map dashboardFiltersListMapObj= new HashMap();
			Map selectedDashboardFilterValuesMapObj = new HashMap();
			StringBuffer filterWhereClauseSB = new StringBuffer();
			if(pageTemplateFilters!=null){
				String selectedFilterFieldNameStr=null;
				String selectedFilterVariableStr=null;
				String selectedFilterValueStr=null;
				int selectedFilterIndex=0;
				int size = pageTemplateFilters.size();
				for(int i=0;i<size;i++){
					PageTemplateFilters pageTemplateFilter = (PageTemplateFilters) pageTemplateFilters.get(i);
					String filterFieldName = pageTemplateFilter.getFilterFieldName();
					String filterLabel=pageTemplateFilter.getFilterLabel();
					String filterVariable=null;
					String filterConditionValue=null;
					if(pageTemplateFilter.getDefaultFilterValues()==1){
						selectedFilterIndex=i;
						selectedFilterFieldNameStr = filterFieldName;
						filterVariable = pageTemplateFilter.getFilterVariable();
						selectedFilterVariableStr =filterVariable; 
						String tableName = pageTemplateFilter.getHistoricalReportTableName();
						List filtersListObj = planningService.getPageTemplateFilters(-1,filterVariable,tableName,filterWhereClauseSB.toString(),null,null,week,year);
						dashboardFiltersListMapObj.put(filterFieldName, filtersListObj);
						if(filtersListObj != null && filtersListObj.size()>0){
							filterConditionValue=filtersListObj.get(0).toString();
							selectedFilterValueStr =filterConditionValue; 
							selectedDashboardFilterValuesMapObj.put(filterFieldName, filterConditionValue);
							filterWhereClauseSB.append(" and ");
							filterWhereClauseSB.append(filterVariable+" =  ");
							filterWhereClauseSB.append("'"+filterConditionValue+"' ");
						}
					}
				}
				reportBean.setSelectedFilterIndex(selectedFilterIndex+"");
				if(selectedFilterIndex+1<size){
					PageTemplateFilters selectedPageTemplateFilter = (PageTemplateFilters)pageTemplateFilters.get(selectedFilterIndex);
					String selectedFilterVariableName = selectedPageTemplateFilter.getFilterVariable();
					String selectedFilterFieldName = selectedPageTemplateFilter.getFilterFieldName();
					String selectedFilterValue = (String)selectedDashboardFilterValuesMapObj.get(selectedFilterFieldName);
					PageTemplateFilters nextPageTemplateFilter = (PageTemplateFilters)pageTemplateFilters.get(selectedFilterIndex+1);
					String filterFieldName = nextPageTemplateFilter.getFilterFieldName();
					if(nextPageTemplateFilter.getDefaultFilterValues()!=1){
						String filterVariable = nextPageTemplateFilter.getFilterVariable();
						String tableName = nextPageTemplateFilter.getTableName();
						List filtersListObj = planningService.getPageTemplateFilters(-1,filterVariable,tableName,filterWhereClauseSB.toString(),null,null,week,year);
						dashboardFiltersListMapObj.put(filterFieldName, filtersListObj);
					}
				}
				type=selectedFilterFieldNameStr;
				selectedTypeVariable = selectedFilterVariableStr;
				selectedTypeValue = selectedFilterValueStr;
				reportBean.setType(type);
				reportBean.setSelectedTypeValue(selectedTypeValue);
				reportBean.setSelectedTypeVariable(selectedTypeVariable);
				reportBean.setProductDescription(null);
				reportBean.setProductHierarchyII(null);
				reportBean.setPlanogram(null);
			}
			ApplicationUtil.setObjectInSession(ApplicationConstants.SELECTED_HISTORICAL_ACTUAL_REPORT_FILTER_VALUES_MAP,selectedDashboardFilterValuesMapObj, request);
			ApplicationUtil.setObjectInSession(ApplicationConstants.HISTORICAL_ACTUAL_REPORT_FILTER_LIST_VALUES_MAP, dashboardFiltersListMapObj, request);
		}
		// end 
		
		//ON CHANGE DASHBOARD FILTERS
		
		if(!ApplicationUtil.isEmptyOrNull(selectedFilterIndexStr)){
			ApplicationUtil.setObjectInSession("isFromOnChangeFilter", "true", request);
			String[] selectedFilterIndexArray= selectedFilterIndexStr.split(",");
			int selectedFilterIndex =Integer.parseInt(selectedFilterIndexArray[0]);
			Map filtersListMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.HISTORICAL_ACTUAL_REPORT_FILTER_LIST_VALUES_MAP, request);
			if(filtersListMapObj == null){
				filtersListMapObj = new HashMap();
			}
			Map selectedFilterValuesMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.SELECTED_HISTORICAL_ACTUAL_REPORT_FILTER_VALUES_MAP, request);
			if(selectedFilterValuesMapObj==null){
				selectedFilterValuesMapObj = new HashMap();
			}
			List filters = pageTemplate.getFiltersList();
			String product=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_ID);
			String id = PropertiesUtil.getProperty(ApplicationConstants.ID);
			String skuListId=PropertiesUtil.getProperty(ApplicationConstants.SKU_LIST_ID);
			StringBuffer filterWhereClauseSB = new StringBuffer();
			if(pageTemplateFilters!=null){
				List filtersList = new ArrayList();
				int size = pageTemplateFilters.size();
				String filterFieldName = null;
				String variableName = null;
				String selectedFilterValue = null;
				for(int i=0;i<size;i++){
					PageTemplateFilters pageTemplateFilter = (PageTemplateFilters) pageTemplateFilters.get(i);
					filterFieldName = pageTemplateFilter.getFilterFieldName();
					variableName = pageTemplateFilter.getFilterVariable();
					selectedFilterValue  = request.getParameter(filterFieldName);
					if(i==selectedFilterIndex && !ApplicationUtil.isEmptyOrNull(selectedFilterValue) && selectedFilterValue.equalsIgnoreCase("select")){
						selectedFilterIndex=selectedFilterIndex-1;
						i=i-2;
						continue;
					}
					
					if(i==selectedFilterIndex){
						type= filterFieldName;
						selectedTypeVariable = variableName;
						selectedTypeValue = selectedFilterValue;
						reportBean.setType(type);
						reportBean.setSelectedTypeValue(selectedTypeValue);
						reportBean.setSelectedTypeVariable(selectedTypeVariable);
					}
					if(i<=selectedFilterIndex){
						if(!ApplicationUtil.isEmptyOrNull(selectedFilterValue) && !selectedFilterValue.equalsIgnoreCase("select")){
							selectedFilterValuesMapObj.put(filterFieldName, selectedFilterValue);
							filterWhereClauseSB.append(" and ");
							filterWhereClauseSB.append(variableName+" =  ");
							filterWhereClauseSB.append("'"+selectedFilterValue+"' ");
						}
						
					}else{
						selectedFilterValuesMapObj.put(filterFieldName, null);
						filtersListMapObj.put(filterFieldName, null);
					}
				}
				
				if(selectedFilterIndex+1<size){
					PageTemplateFilters nextPageTemplateFilter = (PageTemplateFilters)pageTemplateFilters.get(selectedFilterIndex+1);
					filterFieldName = nextPageTemplateFilter.getFilterFieldName();
					String filterVariable = nextPageTemplateFilter.getFilterVariable();
					String tableName = "";
					tableName = nextPageTemplateFilter.getHistoricalReportTableName();	
					List filtersListObj = planningService.getPageTemplateFilters(-1,filterVariable,tableName,filterWhereClauseSB.toString(),null,null,week,year);
					filtersListMapObj.put(filterFieldName, filtersListObj);
					ApplicationUtil.setObjectInSession(ApplicationConstants.HISTORICAL_ACTUAL_REPORT_FILTER_LIST_VALUES_MAP, filtersListMapObj, request);
				}
			}
			ApplicationUtil.setObjectInSession(ApplicationConstants.SELECTED_HISTORICAL_ACTUAL_REPORT_FILTER_VALUES_MAP,selectedFilterValuesMapObj, request);
		}
		// END DASHBOARD FILTERS
		String business=null;
		String productId = null;
		if(!ApplicationUtil.isEmptyOrNull(type) &&  type.equalsIgnoreCase("PRODUCT")) {
			productId = selectedTypeValue;
		}
		Map forecastingUnitsListMap =new HashMap();
		List jsonViewStrList = new ArrayList();
		List jsonTableList = new ArrayList();
		Map selectedFilterMapObj = new HashMap();
		/*PlanningCycle activePlanningCycle = (PlanningCycle)ApplicationUtil.getObjectFromSession(ApplicationConstants.ACTIVE_PLANNINGCYCLE_OBJ, request);
		String startWeek=activePlanningCycle.getStartWeek();
		String startYear=activePlanningCycle.getStartYear();*/
		String actualsWeekRange = PropertiesUtil.getProperty("historicalActuals");
		Integer range =Integer.valueOf(actualsWeekRange);
		StringBuffer stringBuffer=new StringBuffer();
		if(pageTemplateFilters!=null){
			Map selectedFiltersList=(Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.SELECTED_HISTORICAL_ACTUAL_REPORT_FILTER_VALUES_MAP, request);

			int size = pageTemplateFilters.size();
			for(int i=0;i<size;i++){
				PageTemplateFilters pageTemplateFilter = (PageTemplateFilters) pageTemplateFilters.get(i);
				String filterFieldName = pageTemplateFilter.getFilterFieldName();
				String variableName = pageTemplateFilter.getFilterVariable();
				String filterValue = (String)selectedFiltersList.get(filterFieldName);
				if(!ApplicationUtil.isEmptyOrNull(filterFieldName) && filterFieldName.equalsIgnoreCase("BUSINESS")){
					business = filterValue;
				}
				if(!ApplicationUtil.isEmptyOrNull(filterValue) && !filterValue.equalsIgnoreCase("select")){
					stringBuffer.append(" and "+variableName+"='"+filterValue+"'");
				}
			}
		}
		if(pageTemplateCharts!=null && planningCycleId!=0){			
			int size = pageTemplateCharts.size();
			Map jsonViewMapObj =new HashMap();
			for(int i=0;i<size;i++){
				PageTemplateCharts pageTemplateChart = (PageTemplateCharts) pageTemplateCharts.get(i);
				String tableName = pageTemplateChart.getTableName();
				if(!ApplicationUtil.isEmptyOrNull(type) &&  type.equalsIgnoreCase("PRODUCT")) {
					jsonViewMapObj = reportService.getHistoricalActualReportChartJsonFromDatabase(tableName,selectedTypeValue,planningCycleId,business,rolesList,userId,week,year,range,selectedPeriod) ;
				}else{
					jsonViewMapObj = reportService.getHistoricalActualReportChartJsonForAggregated(tableName,selectedTypeVariable,planningCycleId,selectedTypeValue,userId,business,stringBuffer.toString(),rolesList,week,year,range,selectedPeriod) ;
				}
				jsonViewStrList.add(jsonViewMapObj.get("jsonStr"));
				System.out.println("json String  : "+jsonViewMapObj.get("jsonStr"));
				jsonTableList.add(jsonViewMapObj.get("jsonTable"));
			}
		}
		System.out.println("table list: \n    "+jsonTableList);
		reportBean.setJsonStrList(jsonViewStrList);
		reportBean.setJsonTableList(jsonTableList);
		map.addAttribute(ApplicationConstants.REPORT_BEAN, reportBean);
		ApplicationUtil.setObjectInSession("isFrom", "historicalActual", request);
		ApplicationUtil.setObjectInSession("headerFrom", "reports", request);
		return new ModelAndView(viewName, "model",reportBean);
}	
	
	@RequestMapping(value = "/refreshHistoricalReports.htm")
	public ModelAndView refreshHistoricalReports(
			@ModelAttribute("reportBean") ReportBean reportBean,
			BindingResult result, HttpServletRequest request,ModelMap map) throws ApplicationException {
		return getHistoricalActualReports(reportBean,result,request,map);
	}
	
	//planogram reports
	
	@RequestMapping(value = "/planogramReports.htm")
	public ModelAndView getPlanogramReports(
			@ModelAttribute("newBean") ReportBean reportBean,
			BindingResult result, HttpServletRequest request,ModelMap map) throws ApplicationException {
		System.out.println("in planogram reports");
		int planningCycleId = reportBean.getPlanningCycleId();
		String type= reportBean.getType();
		String selectedTypeValue = reportBean.getSelectedTypeValue();
		String selectedTypeVariable = reportBean.getSelectedTypeVariable();
		Map pageTemplateCacheObj = (Map) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.PAGE_TEMPLATES_CACHE_KEY);	
		PageTemplate pageTemplate = (PageTemplate)pageTemplateCacheObj.get("PLANOGRAM_REPORT");
		List pageTemplateCharts = pageTemplate.getPageTemplateCharts();
		List pageTemplateFilters = pageTemplate.getPageTemplateFilters();
		String selectedFilterIndexStr = reportBean.getSelectedFilterIndex();
		ApplicationUtil.removeObjectFromSession("isFromOnChangeFilter", request);
		Users users = (Users) ApplicationUtil.getObjectFromSession(ApplicationConstants.APPLICATION_ACT_AS_USER, request);
		List rolesList = users.getRolesList();
		Integer userId=users.getLogin_id();
		String activefilterVariable="";
		String startWeek="0";
		String startYear="0";
		Map closedPlanningCycleMap = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.CLOSED_PLANNING_CYCLE_LIST, request);
		if(closedPlanningCycleMap == null){
			closedPlanningCycleMap = reportService.getClosedReportPlanningCycleList(false);
			ApplicationUtil.setObjectInSession(ApplicationConstants.CLOSED_PLANNING_CYCLE_LIST, closedPlanningCycleMap, request);
		}
		PlanningCycle closedPlanningCycle=(PlanningCycle) closedPlanningCycleMap.get(planningCycleId);
		if(closedPlanningCycle!=null){
			startWeek=closedPlanningCycle.getStartWeek();
			startYear=closedPlanningCycle.getStartYear();
		}
		Integer week=Integer.parseInt(startWeek);
		Integer year =Integer.parseInt(startYear);
		// load defualt planogram Report filters data
		if(ApplicationUtil.isEmptyOrNull(selectedFilterIndexStr)){
			Map reportFiltersListMapObj= new HashMap();
			Map selecteReportFilterValuesMapObj = new HashMap();
			StringBuffer filterWhereClauseSB = new StringBuffer();
			if(pageTemplateFilters!=null){
				String selectedFilterFieldNameStr=null;
				String selectedFilterVariableStr=null;
				String selectedFilterValueStr=null;
				int selectedFilterIndex=0;
				int size = pageTemplateFilters.size();
				for(int i=0;i<size;i++){
					PageTemplateFilters pageTemplateFilter = (PageTemplateFilters) pageTemplateFilters.get(i);
					String filterFieldName = pageTemplateFilter.getFilterFieldName();
					String filterLabel=pageTemplateFilter.getFilterLabel();
					String filterVariable=null;
					String filterConditionValue=null;
					if(pageTemplateFilter.getDefaultFilterValues()==1){
						selectedFilterIndex=i;
						selectedFilterFieldNameStr = filterFieldName;
						filterVariable = pageTemplateFilter.getFilterVariable();
						selectedFilterVariableStr =filterVariable; 
						String tableName = pageTemplateFilter.getTableName();
						List filtersListObj = planningService.getPageTemplateFilters(-1,filterVariable,tableName,filterWhereClauseSB.toString(),null,null,week,year);
						reportFiltersListMapObj.put(filterFieldName, filtersListObj);
						if(filtersListObj != null && filtersListObj.size()>0){
							filterConditionValue=filtersListObj.get(0).toString();
							selectedFilterValueStr =filterConditionValue; 
							selecteReportFilterValuesMapObj.put(filterFieldName, filterConditionValue);
							filterWhereClauseSB.append(" and ");
							filterWhereClauseSB.append(filterVariable+" =  ");
							filterWhereClauseSB.append("'"+filterConditionValue+"' ");
						}
					}
				}
				reportBean.setSelectedFilterIndex(selectedFilterIndex+"");
				if(selectedFilterIndex+1<size){
					PageTemplateFilters selectedPageTemplateFilter = (PageTemplateFilters)pageTemplateFilters.get(selectedFilterIndex);
					String selectedFilterVariableName = selectedPageTemplateFilter.getFilterVariable();
					String selectedFilterFieldName = selectedPageTemplateFilter.getFilterFieldName();
					String selectedFilterValue = (String)selecteReportFilterValuesMapObj.get(selectedFilterFieldName);
					PageTemplateFilters nextPageTemplateFilter = (PageTemplateFilters)pageTemplateFilters.get(selectedFilterIndex+1);
					String filterFieldName = nextPageTemplateFilter.getFilterFieldName();
					if(nextPageTemplateFilter.getDefaultFilterValues()!=1){
						 activefilterVariable = nextPageTemplateFilter.getFilterVariable();
						String tableName = nextPageTemplateFilter.getTableName();
						List filtersListObj = planningService.getPageTemplateFilters(-1,activefilterVariable,tableName,filterWhereClauseSB.toString(),null,null,week,year);
						reportFiltersListMapObj.put(filterFieldName, filtersListObj);
					}
				}
				type=selectedFilterFieldNameStr;
				selectedTypeVariable = selectedFilterVariableStr;
				selectedTypeValue = selectedFilterValueStr;
				reportBean.setType(type);
				reportBean.setSelectedTypeValue(selectedTypeValue);
				reportBean.setSelectedTypeVariable(selectedTypeVariable);
			}
			ApplicationUtil.setObjectInSession(ApplicationConstants.SELECTED_PLANOGRAM_REPORT_FILTER_VALUES_MAP,selecteReportFilterValuesMapObj, request);
			ApplicationUtil.setObjectInSession(ApplicationConstants.PLANOGRAM_REPORT_FILTER_LIST_VALUES_MAP, reportFiltersListMapObj, request);
		}
		// end 
		
		
		//ON CHANGE REPORT FILTERS
		
				if(!ApplicationUtil.isEmptyOrNull(selectedFilterIndexStr)){
					ApplicationUtil.setObjectInSession("isFromOnChangeFilter", "true", request);
					String[] selectedFilterIndexArray= selectedFilterIndexStr.split(",");
					int selectedFilterIndex =Integer.parseInt(selectedFilterIndexArray[0]);
					Map filtersListMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.PLANOGRAM_REPORT_FILTER_LIST_VALUES_MAP, request);
					if(filtersListMapObj == null){
						filtersListMapObj = new HashMap();
					}
					Map selectedFilterValuesMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.SELECTED_PLANOGRAM_REPORT_FILTER_VALUES_MAP, request);
					if(selectedFilterValuesMapObj==null){
						selectedFilterValuesMapObj = new HashMap();
					}
					List filters = pageTemplate.getFiltersList();
					String product=PropertiesUtil.getProperty(ApplicationConstants.PRODUCT_ID);
					String id = PropertiesUtil.getProperty(ApplicationConstants.ID);
					String skuListId=PropertiesUtil.getProperty(ApplicationConstants.SKU_LIST_ID);
					String roleFilterCondition="";
					StringBuffer filterWhereClauseSB = new StringBuffer();
					if(pageTemplateFilters!=null){
						List filtersList = new ArrayList();
						int size = pageTemplateFilters.size();
						for(int i=0;i<size;i++){
							PageTemplateFilters pageTemplateFilter = (PageTemplateFilters) pageTemplateFilters.get(i);
							String filterFieldName = pageTemplateFilter.getFilterFieldName();
							String variableName = pageTemplateFilter.getFilterVariable();
							String selectedFilterValue  = request.getParameter(filterFieldName);
							if(i==selectedFilterIndex && !ApplicationUtil.isEmptyOrNull(selectedFilterValue) && selectedFilterValue.equalsIgnoreCase("select")){
								selectedFilterIndex=selectedFilterIndex-1;
								i=i-2;
								continue;
							}
							if(i==selectedFilterIndex){
								type= filterFieldName;
								selectedTypeVariable = variableName;
								selectedTypeValue = selectedFilterValue;
								reportBean.setType(type);
								reportBean.setSelectedTypeValue(selectedTypeValue);
								reportBean.setSelectedTypeVariable(selectedTypeVariable);
							}
							if(i<=selectedFilterIndex){
								if(!ApplicationUtil.isEmptyOrNull(selectedFilterValue) && !selectedFilterValue.equalsIgnoreCase("select")){
									selectedFilterValuesMapObj.put(filterFieldName, selectedFilterValue);
									filterWhereClauseSB.append(" and ");
									filterWhereClauseSB.append(variableName+" =  ");
									filterWhereClauseSB.append("'"+selectedFilterValue+"' ");
								}
							}else{
								selectedFilterValuesMapObj.put(filterFieldName, null);
								filtersListMapObj.put(filterFieldName, null);
							}
						}
						
						if(selectedFilterIndex+1<size){
							PageTemplateFilters nextPageTemplateFilter = (PageTemplateFilters)pageTemplateFilters.get(selectedFilterIndex+1);
							String filterFieldName = nextPageTemplateFilter.getFilterFieldName();
							activefilterVariable = nextPageTemplateFilter.getFilterVariable();
							String tableName = nextPageTemplateFilter.getTableName();
							List filtersListObj = planningService.getPageTemplateFilters(-1,activefilterVariable,tableName,filterWhereClauseSB.toString()+roleFilterCondition,null,null,week,year);
							filtersListMapObj.put(filterFieldName, filtersListObj);
							ApplicationUtil.setObjectInSession(ApplicationConstants.PLANOGRAM_REPORT_FILTER_LIST_VALUES_MAP, filtersListMapObj, request);
						}
					}
					ApplicationUtil.setObjectInSession(ApplicationConstants.SELECTED_PLANOGRAM_REPORT_FILTER_VALUES_MAP,selectedFilterValuesMapObj, request);
				}
				// End On change Report Filters
				
				
				/*PlanningCycle activePlanningCycle = (PlanningCycle)ApplicationUtil.getObjectFromSession(ApplicationConstants.ACTIVE_PLANNINGCYCLE_OBJ, request);
				String startWeek=activePlanningCycle.getStartWeek();
				String startYear=activePlanningCycle.getStartYear();*/
				
				String actualsWeekRange = PropertiesUtil.getProperty("planogramWeeksRange");
				Integer range =Integer.valueOf(actualsWeekRange);
				String business="";
				StringBuffer stringBuffer=new StringBuffer();
				if(pageTemplateFilters!=null){
					Map selectedFiltersList=(Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.SELECTED_PLANOGRAM_REPORT_FILTER_VALUES_MAP, request);
					int size = pageTemplateFilters.size();
					for(int i=0;i<size;i++){
						PageTemplateFilters pageTemplateFilter = (PageTemplateFilters) pageTemplateFilters.get(i);
						String filterFieldName = pageTemplateFilter.getFilterFieldName();
						String variableName = pageTemplateFilter.getFilterVariable();
						String filterValue = (String)selectedFiltersList.get(filterFieldName);
						if(!ApplicationUtil.isEmptyOrNull(filterFieldName) && filterFieldName.equalsIgnoreCase("BUSINESS")){
							business = filterValue;
						}
						if(!ApplicationUtil.isEmptyOrNull(filterValue) && !filterValue.equalsIgnoreCase("select")){
							stringBuffer.append(" and "+variableName+"='"+filterValue+"'");
						}
					}
				}
				Map planogramTableViewMapObj = new HashMap();
				Map targetTablecolorRangeMapObj = new HashMap();
				if(pageTemplateCharts!=null && planningCycleId!=0){
					int size = pageTemplateCharts.size();
					for(int i=0;i<size;i++){
						PageTemplateCharts pageTemplateChart = (PageTemplateCharts) pageTemplateCharts.get(i);
						String tableName = pageTemplateChart.getTableName();
						String selectedPeriod=reportBean.getSelectedPeriod();
						if(ApplicationUtil.isEmptyOrNull(selectedPeriod)){
							selectedPeriod="Weekly";
						}
						System.out.println(selectedPeriod);
						reportService.gePlanogramtReports(planogramTableViewMapObj,tableName,planningCycleId,stringBuffer.toString(),week,year,range,selectedPeriod,targetTablecolorRangeMapObj) ;
					}
				}
				reportBean.setTargetTableMap(planogramTableViewMapObj);
				reportBean.setTableColorRange((Integer) targetTablecolorRangeMapObj.get("tableColorRange"));
		ApplicationUtil.setObjectInSession("isFrom", "planogramReport", request);
		ApplicationUtil.setObjectInSession("headerFrom", "reports", request);
		map.addAttribute(ApplicationConstants.REPORT_BEAN, reportBean);
		return new ModelAndView("planogramReport", "model",reportBean);
	}
	
	@RequestMapping(value = "/refreshPlanogramReports.htm")
	public ModelAndView refreshPlanogramReports(
			@ModelAttribute("reportBean") ReportBean reportBean,
			BindingResult result, HttpServletRequest request,ModelMap map) throws ApplicationException {
		return getPlanogramReports(reportBean,result,request,map);
	}
}
