<%@page import="java.util.HashMap"%>
<%@page import="com.bridgei2i.common.vo.Users"%>
<%@page import="com.bridgei2i.common.util.ApplicationUtil"%>
<%@page import="com.bridgei2i.common.constants.ApplicationConstants"%>
<%@page import="com.bridgei2i.common.vo.PageTemplate"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.bridgei2i.common.controller.LoadApplicationCacheService"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String contextPath = request.getContextPath();
	String navibar =(String) request.getParameter("navibar");
	String isFrom =(String) ApplicationUtil.getObjectFromSession("isFrom", request);
	String isFromOnChangeFilter =(String) ApplicationUtil.getObjectFromSession("isFromOnChangeFilter", request);
	String activePlanningTab =(String) ApplicationUtil.getObjectFromSession(ApplicationConstants.ACTIVE_PLANNING_TAB, request);
	String selectedUtilitiesTab = (String) ApplicationUtil.getObjectFromSession(ApplicationConstants.ACTIVE_UTILITIES_TAB, request);
	Map pageTemplateCacheObj = (Map) LoadApplicationCacheService.applicationCacheObject
			.get(ApplicationConstants.PAGE_TEMPLATES_CACHE_KEY);	
	PageTemplate pageTemplate = (PageTemplate)pageTemplateCacheObj.get("PC_DASHBOARD");
	PageTemplate pageTemplateReport = (PageTemplate)pageTemplateCacheObj.get("HISTORICAL_REPORT");
	PageTemplate planogramPageTemplate = (PageTemplate)pageTemplateCacheObj.get("PLANOGRAM_REPORT");
	Map selectedFilterValuesMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.SELECTED_FILTER_VALUES_MAP, request);
	if(selectedFilterValuesMapObj == null){
		selectedFilterValuesMapObj = new HashMap();
	}
	
	Map filtersListMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.FILTER_LIST_VALUES_MAP, request);
	if(filtersListMapObj == null){
		filtersListMapObj = new HashMap();
	}
	
	Map selectedDashboardFilterValuesMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.SELECTED_DASHBOARD_FILTER_VALUES_MAP, request);
	if(selectedDashboardFilterValuesMapObj == null){
		selectedDashboardFilterValuesMapObj = new HashMap();
	}
	
	Map dashboardFiltersListMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.DASHBOARD_FILTER_LIST_VALUES_MAP, request);
	if(dashboardFiltersListMapObj == null){
		dashboardFiltersListMapObj = new HashMap();
	}
	
	Map selectedReportFilterValuesMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.SELECTED_REPORT_FILTER_VALUES_MAP, request);
	if(selectedReportFilterValuesMapObj == null){
		selectedReportFilterValuesMapObj = new HashMap();
	}
	
	Map reportFiltersListMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.REPORT_FILTER_LIST_VALUES_MAP, request);
	if(reportFiltersListMapObj == null){
		reportFiltersListMapObj = new HashMap();
	}
	
	Map selectedPlanogramReportFilterValuesMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.SELECTED_PLANOGRAM_REPORT_FILTER_VALUES_MAP, request);
	if(selectedPlanogramReportFilterValuesMapObj == null){
		selectedPlanogramReportFilterValuesMapObj = new HashMap();
	}
	
	Map planogramReportFiltersListMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.PLANOGRAM_REPORT_FILTER_LIST_VALUES_MAP, request);
	if(planogramReportFiltersListMapObj == null){
		planogramReportFiltersListMapObj = new HashMap();
	}
	
	Map selectedHistoricalActualReportFilterValuesMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.SELECTED_HISTORICAL_ACTUAL_REPORT_FILTER_VALUES_MAP, request);
	if(selectedHistoricalActualReportFilterValuesMapObj == null){
		selectedHistoricalActualReportFilterValuesMapObj = new HashMap();
	}
	
	Map historicalActualReportFiltersListMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.HISTORICAL_ACTUAL_REPORT_FILTER_LIST_VALUES_MAP, request);
	if(historicalActualReportFiltersListMapObj == null){
		historicalActualReportFiltersListMapObj = new HashMap();
	}
	//Start:Override Report
	Map selectedOverrideReportFilterValuesMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.SELECTED_OVERRIDE_REPORT_FILTER_VALUES_MAP, request);
	if(selectedOverrideReportFilterValuesMapObj == null){
		selectedOverrideReportFilterValuesMapObj = new HashMap();
	}
	
	Map OverrideReportFiltersListMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.OVERRIDE_REPORT_FILTER_LIST_VALUES_MAP, request);
	if(OverrideReportFiltersListMapObj == null){
		OverrideReportFiltersListMapObj = new HashMap();
	}
	//End:Override Report
	
	//start: accuracy Report
	Map selectedAccuracyReportFilterValuesMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.SELECTED_ACCURACY_REPORT_FILTER_VALUES_MAP, request);
	if(selectedAccuracyReportFilterValuesMapObj == null){
		selectedAccuracyReportFilterValuesMapObj = new HashMap();
	}
	
	Map accuracyReportFiltersListMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.ACCURACY_REPORT_FILTER_LIST_VALUES_MAP, request);
	if(accuracyReportFiltersListMapObj == null){
		accuracyReportFiltersListMapObj = new HashMap();
	}
	//End:Override Report
	
	List closedPlanningCycleList = null;
	Map closedPlanningCycleListMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.CLOSED_PLANNING_CYCLE_LIST, request);
	if(closedPlanningCycleListMapObj!=null){
		closedPlanningCycleList = (List)closedPlanningCycleListMapObj.get("closedPlanningCycleList");
	}
	if(closedPlanningCycleList==null){
		closedPlanningCycleList = new ArrayList();
	}
	
	List allPlanningCycleList = null;
	Map allPlanningCycleListMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.ALL_PLANNING_CYCLE_LIST, request);
	if(allPlanningCycleListMapObj!=null){
		allPlanningCycleList = (List)allPlanningCycleListMapObj.get("closedPlanningCycleList");
	}
	if(allPlanningCycleList==null){
		allPlanningCycleList = new ArrayList();
	}
	
	Users users = (Users) ApplicationUtil.getObjectFromSession(
			ApplicationConstants.APPLICATION_ACT_AS_USER, request);
	List roles = null;
	if(users!= null){
		roles = users.getRolesList();		
	}

%>
<link href="css/custom-styles.css" rel="stylesheet" />
     <!-- Morris Chart Styles-->
    <link href="js/plugins/morris/morris-0.4.3.min.css" rel="stylesheet" />
    <!-- Core Scripts - Include with every page -->
     <script src="js/jquery-1.11.0.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/plugins/metisMenu/jquery.metisMenu.js"></script>
      <script src="js/jquery-1.10.2.js"></script>
      <script src="js/jquery.metisMenu.js"></script>
        <!-- Morris Chart Js -->
     <script src="js/plugins/morris/raphael-2.1.0.min.js"></script>
    <script src="js/plugins/morris/morris.js"></script>
     <link href="css/bootstrap-select.min.css" rel="stylesheet" />
     <script src="js/bootstrap-select.min.js"></script>
     <script src="js/plugins/jquery.min.js"></script>
<style>
   #sel0{
		width:65%;
		height: 16px;
		padding: 0px 5px;
		margin-left: 34px;
		font-size: 11px;
	}
	
	#form-grp0{
		/* margin-bottom: 10px; */
		margin-bottom: 4px;
	}
	
	#form-grp7{
	  margin-left: 50px;
	}
	.labelpadding
	{
		margin-bottom:0px;
	}
	#cel0{
		color: #B5B5B5;
		background: transparent;
		padding-left: 33px;
		}
		
	#Discard{
	  margin-left: 20px;
	}
	
	.btn-group.bootstrap-select.form-control{
		width: 70%;
  		margin-left: 34px;
  		height: 15px;
	}
	
	.btn.dropdown-toggle.btn-default{
		height: 15px;
	}
	.filter-option.pull-left{
		font-size: 11px;
		margin-top: -5px;
	}
	
	/* #openMenu {
	
	  height: 210px;
	} */
	
	/* #innerMenu{
	  height: 160px;
	} */
	
</style>
<script type="text/javascript">
function setSelectedTab(selectedTabId,disableOtherTab)
{
	document.getElementById(selectedTabId).setAttribute("class","active-menu");
	document.getElementById(disableOtherTab).setAttribute("class","#");
	document.getElementById('selectedTab').value = selectedTabId;
}


function historicalActualReportFilters()
{
	loadProgress();
	navigationForm.action= "historicalActualReport.htm";
	var selectedFilterIndexObj = document.getElementById("selectedFilterIndex");
	selectedFilterIndexObj.value = '';
	navigationForm.submit();
}
function onChangeHistoricalActualReportFilters(index)
{
	loadProgress();
	navigationForm.action="refreshHistoricalReports.htm";
	var selectedFilterIndexObj = document.getElementById("selectedFilterIndex");
	selectedFilterIndexObj.value = index;
	navigationForm.submit();
}

function onChangeHistoricalPeriodFilter(value){
	loadProgress();
	navigationForm.action="refreshHistoricalReports.htm";
	navigationForm.submit();
}
function confirmInitiate(){
	swal({   title: "Initiate",   
		text: "Are you sure you want to Initiate?",   
		type: "warning",   
		showCancelButton: true,   
		confirmButtonColor: "#2DAFCB",   
		confirmButtonText: "Yes, Initiate it",   
		cancelButtonText: "No, cancel Initiate",   
		closeOnConfirm: false,   
		closeOnCancel: false }, 
		function(isConfirm){   
			if (isConfirm) {     
				swal({   title: "Success",
					 text: "Commited ",   
					 type: "success",   
					 showConfirmButton: false, 
					 closeOnConfirm: false,
					 timer : 1000});
				initiateStatus();
				} else {     
					swal({   title: "Initiate Cancelled",
						 text: "Initiate Cancelled",   
						 showConfirmButton: false, 
						 type: "error",   
						 timer : 1000  });
				} 
			});
}
function initiateStatus(){
	navigationForm.action="initiateStatus.htm";
	navigationForm.submit();
}
function confirmDiscard(){
	swal({   title: "Discard",   
		text: "Are you sure you want to Discard?",   
		type: "warning",   
		showCancelButton: true,   
		confirmButtonColor: "#2DAFCB",   
		confirmButtonText: "Yes, Discard it",   
		cancelButtonText: "No, cancel Discard",   
		closeOnConfirm: false,   
		closeOnCancel: false }, 
		function(isConfirm){   
			if (isConfirm) {     
				swal({   title: "Success",
					 text: "Discarded",   
					 type: "success",   
					 showConfirmButton: false, 
					 closeOnConfirm: false,
					 timer : 1000});
				flushTables();
				} else {     
					swal({   title: "Discard Cancelled",
						 text: "Discard Cancelled",   
						 showConfirmButton: false, 
						 type: "error",   
						 timer : 1000  });
				} 
			});
}
function flushTables(){
	navigationForm.action="flushTables.htm";
	navigationForm.submit();
	
}
function onChangeFilter(index){
	loadProgress();
	navigationForm.action="planningLogOnFilterChange.htm";
	var selectedFilterIndexObj = document.getElementById("selectedFilterIndex");
	selectedFilterIndexObj.value = index;
	navigationForm.submit();
	
}

function onChangeDashboardFilter(index){
	loadProgress();
	navigationForm.action="refreshDashboardDetails.htm";
	var selectedFilterIndexObj = document.getElementById("selectedFilterIndex");
	selectedFilterIndexObj.value = index;
	var isFromCategoryLevelSummaryObj = document.getElementById("isFromCategoryLevelSummary");
	isFromCategoryLevelSummaryObj.value="false";
	var isFromCategoryLevelSummaryFreezeObj = document.getElementById("isFromCategoryLevelSummaryFreeze");
	isFromCategoryLevelSummaryFreezeObj.value="false";
	navigationForm.submit();
}

function onChangeCategoryLevelDashboardFilterFreeze(index){
	loadProgress();
	navigationForm.action="refreshDashboardDetails.htm";
	var selectedFilterIndexObj = document.getElementById("selectedFilterIndex");
	selectedFilterIndexObj.value = index;
	var isFromCategoryLevelSummaryObj = document.getElementById("isFromCategoryLevelSummary");
	isFromCategoryLevelSummaryObj.value="false";
	var isFromCategoryLevelSummaryFreezeObj = document.getElementById("isFromCategoryLevelSummaryFreeze");
	isFromCategoryLevelSummaryFreezeObj.value="true";
	navigationForm.submit();
}

function onChangeCategoryLevelDashboardFilter(index){
	loadProgress();
	navigationForm.action="refreshDashboardDetails.htm";
	var selectedFilterIndexObj = document.getElementById("selectedFilterIndex");
	selectedFilterIndexObj.value = index;
	var isFromCategoryLevelSummaryObj = document.getElementById("isFromCategoryLevelSummary");
	isFromCategoryLevelSummaryObj.value="true";
	var isFromCategoryLevelSummaryFreezeObj = document.getElementById("isFromCategoryLevelSummaryFreeze");
	isFromCategoryLevelSummaryFreezeObj.value="false";
	navigationForm.submit();
}

function dashboardClick(){
	loadProgress();
	navigationForm.action="dashboard.htm";
	var isFromCategoryLevelSummaryObj = document.getElementById("isFromCategoryLevelSummary");
	isFromCategoryLevelSummaryObj.value="true";
	var isFromCategoryLevelSummaryFreezeObj = document.getElementById("isFromCategoryLevelSummaryFreeze");
	isFromCategoryLevelSummaryFreezeObj.value="false";
	var selectedFilterIndexObj = document.getElementById("selectedFilterIndex");
	selectedFilterIndexObj.value = '';
	navigationForm.submit();
}

function dashboardFinanceFreezeClick(){
	loadProgress();
	navigationForm.action="dashboard.htm";
	var isFromCategoryLevelSummaryFreezeObj = document.getElementById("isFromCategoryLevelSummaryFreeze");
	isFromCategoryLevelSummaryFreezeObj.value="true";
	var isFromCategoryLevelSummaryObj = document.getElementById("isFromCategoryLevelSummary");
	isFromCategoryLevelSummaryObj.value="false";
	navigationForm.submit();
}

function getCategoryLevelSummary(){
		loadProgress();
		navigationForm.action= "categoryLevelSummary.htm";
		navigationForm.submit();
	}
	
function freezeOverride(){
	loadProgress();
	var isFromCategoryLevelSummaryFreezeObj = document.getElementById("isFromCategoryLevelSummaryFreeze");
	isFromCategoryLevelSummaryFreezeObj.value="true";
	var isFromCategoryLevelSummaryObj = document.getElementById("isFromCategoryLevelSummary");
	isFromCategoryLevelSummaryObj.value="false";
	navigationForm.action = "freezePlanningCycle.htm";
	navigationForm.submit();
}

function eolData(){
	loadProgress();
	navigationForm.action = "eolData.htm";
	navigationForm.submit();
}
function getReports(){
	loadProgress();
	navigationForm.action= "reports.htm";
	var selectedFilterIndexObj = document.getElementById("selectedFilterIndex");
	selectedFilterIndexObj.value = '';
	navigationForm.submit();
}
function getPlanogramReports(){
	loadProgress();
	navigationForm.action= "planogramReports.htm";
	var selectedFilterIndexObj = document.getElementById("selectedFilterIndex");
	selectedFilterIndexObj.value = '';
	navigationForm.submit();
}

function executiveReports(){
	loadProgress();
	var isFromReportModuleObj = document.getElementById("isFromReportModule");
	isFromReportModuleObj.value='true';
	navigationForm.action= "reports.htm";
	var selectedFilterIndexObj = document.getElementById("selectedFilterIndex");
	selectedFilterIndexObj.value = '';
	navigationForm.submit();
}

function onChangeReportFilters(index){
	loadProgress();
	navigationForm.action="refreshReports.htm";
	var selectedFilterIndexObj = document.getElementById("selectedFilterIndex");
	selectedFilterIndexObj.value = index;
	navigationForm.submit();
}
function onChangePlanogramReportFilters(index){
	loadProgress();
	navigationForm.action="refreshPlanogramReports.htm";
	var selectedFilterIndexObj = document.getElementById("selectedFilterIndex");
	selectedFilterIndexObj.value = index;
	navigationForm.submit();
}

function onChangeExecutiveReportFilters(index){
	loadProgress();
	navigationForm.action="refreshReports.htm";
	var isFromReportModuleObj = document.getElementById("isFromReportModule");
	isFromReportModuleObj.value='true';
	var selectedFilterIndexObj = document.getElementById("selectedFilterIndex");
	selectedFilterIndexObj.value = index;
	navigationForm.submit();
}

function onChangeExecutiveReportPlanningCycleFilter(obj){
	loadProgress();
	navigationForm.action="refreshReports.htm";
	var isFromReportModuleObj = document.getElementById("isFromReportModule");
	isFromReportModuleObj.value='true';
	var planningcycleIdValue = obj.options[obj.selectedIndex].value;
	var selectedFilterIndexObj = document.getElementById("planningCycleId");
	selectedFilterIndexObj.value = planningcycleIdValue;
	navigationForm.submit();
}

function onChangePeriodFilter(value){
	loadProgress();
	navigationForm.action="refreshReports.htm";
	/* var selectedPeriodObj=document.getElementById("selectedPeriod");
	selectedPeriodObj.value=value; */
	navigationForm.submit();
}
function onChangePlanogramPeriodFilter(value){
	loadProgress();
	navigationForm.action="refreshPlanogramReports.htm";
	navigationForm.submit();
}

function onChangeExecutivePeriodFilter(value){
	loadProgress();
	navigationForm.action="refreshReports.htm";
	var isFromReportModuleObj = document.getElementById("isFromReportModule");
	isFromReportModuleObj.value='true';
	/* var selectedPeriodObj=document.getElementById("selectedPeriod");
	selectedPeriodObj.value=value; */
	navigationForm.submit();
}


function onChangeOverrideReportFilters(index){
	//alert("on change ");
	loadProgress();
	navigationForm.action="overrideReport.htm";
	var selectedFilterIndexObj = document.getElementById("selectedFilterIndex");
	selectedFilterIndexObj.value = index;
	navigationForm.submit();
}
function onChangeReportPlanningCycleFilters(obj){
	//alert("on change ");
	var planningcycleIdValue = obj.options[obj.selectedIndex].value;
	var selectedFilterIndexObj = document.getElementById("planningCycleId");
	selectedFilterIndexObj.value = planningcycleIdValue;
	loadProgress();
	navigationForm.action="overrideReport.htm";
	//var selectedFilterIndexObj = document.getElementById("selectedFilterIndex");
	//selectedFilterIndexObj.value = index;
	navigationForm.submit();
}


function historicalActualReportPlanningCycleFilters(obj){
	//alert("on change ");
	var planningcycleIdValue = obj.options[obj.selectedIndex].value;
	var selectedFilterIndexObj = document.getElementById("planningCycleId");
	selectedFilterIndexObj.value = planningcycleIdValue;
	loadProgress();
	navigationForm.action="historicalActualReport.htm";
	//var selectedFilterIndexObj = document.getElementById("selectedFilterIndex");
	//selectedFilterIndexObj.value = index;
	navigationForm.submit();
}

function planogramReportPlanningCycleFilters(obj){
	//alert("on change ");
	var planningcycleIdValue = obj.options[obj.selectedIndex].value;
	var selectedFilterIndexObj = document.getElementById("planningCycleId");
	selectedFilterIndexObj.value = planningcycleIdValue;
	loadProgress();
	navigationForm.action="refreshPlanogramReports.htm";
	navigationForm.submit();
}

function onChangeAccuracyReportFilters(index,reportType){
	loadProgress();
	navigationForm.action="accuracyReportOnFilterChange.htm";
	document.getElementById("reportTypeId").value=reportType;
	var selectedFilterIndexObj = document.getElementById("selectedFilterIndex");
	selectedFilterIndexObj.value = index;
	navigationForm.submit();
}

function scroll(obj,val)
{
	if(val==1){
		obj.style.overflow='auto';	
	}else{
		obj.style.overflow='hidden';
	}
	
	
}
	function selectDefaultPlanningCycle()
	{
		document.getElementsByTagName("select").selectedIndex = "1";
	}
	
function onChangeSkuForecastValuesFilter(index){
	loadProgress();
	var selectedFilterIndexObj = document.getElementById("selectedFilterIndex");
	selectedFilterIndexObj.value = index;
	navigationForm.action="forecastValuesFromPlanningLogOnChangeFilter.htm";
	navigationForm.submit();
	
}
</script>
        <nav class="navbar-default navbar-side" role="navigation" style="z-index:6" >
            <div class="sidebar-collapse " id="myScrollBarStyle" >
   			<form:form method="post"  name="navigationForm" action="planningLogOnFilterChange.htm" commandName="model">
   			<form:hidden name="selectedFilterIndex" id="selectedFilterIndex" path="selectedFilterIndex"/>
   			<input type="hidden" name="isFromReportModule" id="isFromReportModule"/>
   			<input type="hidden" name="isFromCategoryLevelSummary" id="isFromCategoryLevelSummary"/>
   			<input type="hidden" name="closedPlanningCycleFieldName" id="closedPlanningCycleFieldName"/>
   			<form:hidden id="planningCycleId" path="planningCycleId"/>
   			<input type="hidden" id="selectedTab" name ="selectedTab"/>
   			<%-- <form:hidden id="selectedPeriod" path="selectedPeriod"/> --%>
   			<input type="hidden" name="isFromCategoryLevelSummaryFreeze" id="isFromCategoryLevelSummaryFreeze"/>
   			<input id="selectedSku" name="skuList" type="hidden" />
             <%
			if(navibar.equals("1")){
			%>
                <ul class="nav" id="main-menu">

                    <li>
                        <a class="active-menu" href="homePage.htm"><img src="<%=contextPath%>/images/cycle2.png" width="30px"
			height="32px" style="  margin-top: -2px;" /> Planning</a>
                    </li>
                </ul>
             <%
			}
			%>
			<%
			if(navibar.equals("2")){
			%>
                <ul class="nav" id="main-menu">

                    <li>
                        <a class="active-menu" href="#"><i class="fa fa-dashboard"></i> Dashboard</a>
                        <c:set var="pageTemplateObj"  value="<%=pageTemplate%>"/>
                        <c:set var="selectedDashboardFilterValuesMapObj" value="<%=selectedDashboardFilterValuesMapObj%>"/>
                        <c:set var="dashboardFiltersListMapObj" value="<%=dashboardFiltersListMapObj%>"/>
                       	<c:forEach var="pageTemplatesFiltersObj" items="${pageTemplateObj.pageTemplateFilters}" varStatus="outerloop">
                        	<li>
                            	<div class="form-group" id="form-grp0">
                                  <label for="sel0" id="cel0">${pageTemplatesFiltersObj.filterLabel}:</label>
							      <select class="form-control selectpicker" data-live-search="true" id="sel0" name="${pageTemplatesFiltersObj.filterFieldName}" onchange="onChangeDashboardFilter('${outerloop.index}')">
								      <c:if test="${outerloop.index != 0 }">
								      	<option value="select">All</option>
								      </c:if>
								      <c:forEach var="filters" items="${dashboardFiltersListMapObj[pageTemplatesFiltersObj.filterFieldName]}" varStatus="innerloop1">
								      		 <c:choose>
								      			<c:when test="${selectedDashboardFilterValuesMapObj[pageTemplatesFiltersObj.filterFieldName]==filters}">
								      				<option selected="selected" value="${filters}">${filters}</option>
								      			</c:when>
								      			<c:otherwise>
								      				<option value="${filters}">${filters}</option>
								      			</c:otherwise>
								      		</c:choose> 
							           </c:forEach>
							      </select>
							     </div>
                            </li>
                       	</c:forEach>
	                        
                           <%--  <li class="divider"></li>
                            
                            <%if(roles != null && (roles.contains("BI2I_ADMIN"))){ %>
                            <li>
	                            <div class="form-group" id="form-grp7">
		                            <input type="button" id="Initiate" value="Initiate" class="btn btn-info btn-xs"  onclick="initiateStatus()"/>
		                            <input type="button" id="Discard" value="Discard" class="btn btn-info btn-xs"  onclick="flushTables()"/>
	                            </div>
                            </li>
                       <%}%> --%>
                    </li>
                </ul>
             <%
			}
			%>
			
			<%
			if(navibar.equals("3")){
			%>
					<ul class="nav" id="main-menu">
					

                    <li>
                    <%if(activePlanningTab != null && activePlanningTab.equals("planningLog")){%>
                        <a class="active-menu" href="planningLogDetails.htm"><img src="<%=contextPath%>/images/cycle2.png" width="30px"
							height="32px" style="  margin-top: -2px;" /><strong> Planning Log</strong><span class="fa arrow"></span></a>
							
						<ul class="nav nav-second-level">
	                        <c:set var="pageTemplateObj"  value="<%=pageTemplate%>"/>
	                        <c:set var="mapKeyObj" value="<%=selectedFilterValuesMapObj%>"/>
	                        <c:set var="filtersListMapObj" value="<%=filtersListMapObj%>"/>
		                        	<c:forEach var="pageTemplatesFiltersObj" items="${pageTemplateObj.pageTemplateFilters}" varStatus="outerloop">
		                        	<li>
	                            	<div class="form-group" id="form-grp0">
	                                  <label for="sel0" id="cel0">${pageTemplatesFiltersObj.filterLabel}:</label>
								      <select class="form-control selectpicker" data-live-search="true" id="sel0" name="${pageTemplatesFiltersObj.filterFieldName}" onchange="onChangeFilter('${outerloop.index}')">
									      <c:if test="${outerloop.index != 0 }">
									      	<option value="select">All</option>
									      </c:if>
									      <c:forEach var="filters" items="${filtersListMapObj[pageTemplatesFiltersObj.filterFieldName]}" varStatus="innerloop1">
									      		 <c:choose>
									      			<c:when test="${mapKeyObj[pageTemplatesFiltersObj.filterFieldName]==filters}">
									      				<option selected="selected" value="${filters}">${filters}</option>
									      			</c:when>
									      			<c:otherwise>
									      				<option value="${filters}">${filters}</option>
									      			</c:otherwise>
									      		</c:choose> 
								           </c:forEach>
								      </select>
								     </div>
	                            </li>
	                       	</c:forEach>
	                       <li class="divider"></li>
	                       <%if(roles != null && (roles.contains("BI2I_ADMIN"))){ %>
	                            <li>
		                            <div class="form-group" id="form-grp7" style="  margin-bottom: 1px;">
			                            <input type="button" id="Initiate" value="Initiate" class="btn btn-info btn-xs"  onclick="confirmInitiate()"/>
			                            <input type="button" id="Discard" value="Discard" class="btn btn-info btn-xs"  onclick="confirmDiscard()"/>
		                            </div>
	                            </li>
	                       <%}%>
                        </ul>
					<%}else{ %>
						<a href="planningLogDetails.htm"><img src="<%=contextPath%>/images/cycle2.png" width="30px"
							height="32px" style="  margin-top: -2px;" /><strong> Planning Log</strong><span class="fa arrow"></span></a>
					<%}%>
                      
                    </li>
                    <%if(roles != null && (roles.contains("BI2I_ADMIN"))){ %>
	                     <li>
	                     <%if(activePlanningTab != null && activePlanningTab.equals("categoryReport")){%>
	                        <a class="active-menu" href="categoryLevelSummary.htm?isFromCategoryReport=true"><img src="<%=contextPath%>/images/Report1.PNG" width="30px"
								height="32px" style="  margin-top: -2px;" /><strong>Category Report</strong></a>
						<%}else{ %>
							<a href="categoryLevelSummary.htm?isFromCategoryReport=true"><img src="<%=contextPath%>/images/Report1.PNG" width="30px"
								height="32px" style="  margin-top: -2px;" /><strong>Category Report</strong></a>
						<%} %>
						</li>
					<%}%>
                </ul>				
			<%} 
			%>

			   <%
			if(navibar.equals("4")){
			%>
                <ul class="nav" id="main-menu">

                    <li>
                    	 <%if(isFrom != null && isFrom.equals("categoryLevelSummary")){%>
                        	<a class="active-menu" href="#" onclick="getCategoryLevelSummary();"><img src="<%=contextPath%>/images/summary.png" width="33px" height="31px" style="  margin-top: -2px;  margin-left: -6px;" /> Summary</a>
                        <%}else{%>
                        	<a href="#" onclick="getCategoryLevelSummary();"><img src="<%=contextPath%>/images/summary.png" width="33px" height="31px" style="  margin-top: -2px;  margin-left: -6px;" /> Summary</a>
                        <%}%>
                    </li>
                    
                    <li>
                     	<%if(isFrom != null && isFrom.equals("categoryLevelReport")){%>
                        	<a  class="active-menu" href="#" onclick="getReports();" ><img src="<%=contextPath%>/images/Report1.PNG" width="27px" height="27px" style="  margin-top: -2px;" /> Report<span class="fa arrow"></span></a>
                        <%}else{%>
                        	<a  href="#" onclick="getReports();" ><img src="<%=contextPath%>/images/Report1.PNG" width="27px" height="27px" style="  margin-top: -2px;" /> Report<span class="fa arrow"></span></a>
                        <%}%>
                        <%if(isFrom != null && isFrom.equals("categoryLevelReport")){%>
	                    	<c:set var="pageTemplateObj"  value="<%=pageTemplate%>"/>
	                        <c:set var="selectedDashboardFilterValuesMapObj" value="<%=selectedReportFilterValuesMapObj%>"/>
	                        <c:set var="dashboardFiltersListMapObj" value="<%=reportFiltersListMapObj%>"/>
	                        <!-- <ul class="nav nav-second-level"> -->
		                       	<c:forEach var="pageTemplatesFiltersObj" items="${pageTemplateObj.pageTemplateFilters}" varStatus="outerloop">
		                       	<%-- <c:out value="${fn:length(pageTemplateObj.pageTemplateFilters)}" /> --%>
		                       	<c:if test="${outerloop.index<4}">
		                        	<li>
		                            	<div class="form-group" id="form-grp0">
		                                  <label for="sel0" id="cel0">${pageTemplatesFiltersObj.filterLabel}:</label>
									      <select class="form-control selectpicker" data-live-search="true" id="sel0" name="${pageTemplatesFiltersObj.filterFieldName}" onchange="onChangeReportFilters('${outerloop.index}')">
										       <c:if test="${outerloop.index != 0 }">
										      		<option value="select">All</option>
											</c:if>										      
										      <c:forEach var="filters" items="${dashboardFiltersListMapObj[pageTemplatesFiltersObj.filterFieldName]}" varStatus="innerloop1">
										      		 <c:choose>
										      			<c:when test="${selectedDashboardFilterValuesMapObj[pageTemplatesFiltersObj.filterFieldName]==filters}">
										      				<option selected="selected" value="${filters}">${filters}</option>
										      			</c:when>
										      			<c:otherwise>
										      				<option value="${filters}">${filters}</option>
										      			</c:otherwise>
										      		</c:choose> 
									           </c:forEach>
									      </select>
									      
									     </div>
		                            </li>
		                         </c:if>
		                       	</c:forEach>
		                       	<li>
		                       		<label for="sel0" id="cel0">Period:</label>
		                       		<c:set var="selectedPeriodValue"  value="${model.selectedPeriod}"/>
							        <form:select class="form-control selectpicker" data-live-search="true" id="sel0" name="PERIOD" path="selectedPeriod" onChange="onChangePeriodFilter(this.value)">
									      <c:choose>
					      						<c:when test="${selectedPeriodValue=='Weekly'}">
							      					 <option  value="Weekly" selected="selected">Weekly</option>
							      				</c:when>
							      				<c:otherwise>
					    		  					 <option  value="Weekly">Weekly</option>
						      					</c:otherwise>
					      					</c:choose> 
					      					<c:choose>
					      						<c:when test="${selectedPeriodValue=='Monthly'}">
							      					 <option value="Monthly" selected="selected">Monthly</option>
							      				</c:when>
							      				<c:otherwise>
					    		  					 <option value="Monthly">Monthly</option>
						      					</c:otherwise>
					      					</c:choose> 
									     	<c:choose>
					      						<c:when test="${selectedPeriodValue=='Quarterly'}">
							      					  <option value="Quarterly" selected="selected">Quarterly</option>
							      				</c:when>
							      				<c:otherwise>
					    		  					  <option value="Quarterly">Quarterly</option>
						      					</c:otherwise>
					      					</c:choose>
								     </form:select>
						      	</li>
					      	<!-- </ul>onchange="onChangeReportFilters('4') -->
				      	<%} %>
                    </li>
                     <li>
                     	<%if(isFrom != null && isFrom.equals("categoryLevelDashboard")){%>
                        	<a class="active-menu" href="#" onclick="dashboardClick();"><i class="fa fa-dashboard" style="  font-size: 22px;  margin-right: -1px;  padding: 4px;"></i> Dashboard<span class="fa arrow"></span></a>
                        <%}else{%>
                        	<a  href="#" onclick="dashboardClick();"><i class="fa fa-dashboard" style="  font-size: 22px;  margin-right: -1px;  padding: 4px;"></i> Dashboard<span class="fa arrow"></span></a>
                        <%}%>
                        <%if(isFrom != null && isFrom.equals("categoryLevelDashboard")){%>
                         <c:set var="pageTemplateObj"  value="<%=pageTemplate%>"/>
                         <c:set var="selectedDashboardFilterValuesMapObj" value="<%=selectedDashboardFilterValuesMapObj%>"/>
                         <c:set var="dashboardFiltersListMapObj" value="<%=dashboardFiltersListMapObj%>"/>
                        <!--  <ul class="nav nav-second-level"> -->
                        	<c:forEach var="pageTemplatesFiltersObj" items="${pageTemplateObj.pageTemplateFilters}" varStatus="outerloop">
	                        	<li>
	                            	<div class="form-group" id="form-grp0">
	                                  <label for="sel0" id="cel0">${pageTemplatesFiltersObj.filterLabel}:</label>
								      <select class="form-control selectpicker" data-live-search="true" id="sel0" name="${pageTemplatesFiltersObj.filterFieldName}" onchange="onChangeCategoryLevelDashboardFilter('${outerloop.index}')">
									       <c:if test="${outerloop.index != 0 }">
										      <option value="select">All</option>
										   </c:if> 
									      <c:forEach var="filters" items="${dashboardFiltersListMapObj[pageTemplatesFiltersObj.filterFieldName]}" varStatus="innerloop1">
									      		 <c:choose>
									      			<c:when test="${selectedDashboardFilterValuesMapObj[pageTemplatesFiltersObj.filterFieldName]==filters}">
									      				<option selected="selected" value="${filters}">${filters}</option>
									      			</c:when>
									      			<c:otherwise>
									      				<option value="${filters}">${filters}</option>
									      			</c:otherwise>
									      		</c:choose> 
								           </c:forEach>
								      </select>
								     </div>
                            	</li>
                       		</c:forEach>
                       	 <!-- </ul> -->
                       	<%}%>
                    </li>
                </ul>
             <%
			}
			%>
			
			<%
			if(navibar.equals("5")){
			%>
                <ul class="nav" id="main-menu">

                    <li>
                    	 <%if(isFrom != null && isFrom.equals("financeManagerFreeze")){%>
                        	<a class="active-menu" href="#" onclick="freezeOverride();"><img src="<%=contextPath%>/images/summary.png" width="33px" height="31px" style="  margin-top: -2px;  margin-left: -6px;" /> Summary</a>
                        <%}else{%>
                        	<a href="#" onclick="freezeOverride();"><img src="<%=contextPath%>/images/summary.png" width="33px" height="31px" style="  margin-top: -2px;  margin-left: -6px;" /> Summary</a>
                        <%}%>
                    </li>
                    
                    <li>
                     	<%if(isFrom != null && isFrom.equals("categoryLevelReport")){%>
                        	<a  class="active-menu" href="#" onclick="getReports();" ><img src="<%=contextPath%>/images/Report1.PNG" width="27px" height="27px" style="  margin-top: -2px;" /> Report<span class="fa arrow"></span></a>
                        <%}else{%>
                        	<a  href="#" onclick="getReports();" ><img src="<%=contextPath%>/images/Report1.PNG" width="27px" height="27px" style="  margin-top: -2px;" /> Report<span class="fa arrow"></span></a>
                        <%}%>
                        <%if(isFrom != null && isFrom.equals("categoryLevelReport")){%>
	                    	<c:set var="pageTemplateObj"  value="<%=pageTemplate%>"/>
	                        <c:set var="selectedDashboardFilterValuesMapObj" value="<%=selectedReportFilterValuesMapObj%>"/>
	                        <c:set var="dashboardFiltersListMapObj" value="<%=reportFiltersListMapObj%>"/>
	                        <!-- <ul class="nav nav-second-level"> -->
		                       	<c:forEach var="pageTemplatesFiltersObj" items="${pageTemplateObj.pageTemplateFilters}" varStatus="outerloop">
		                       	<%-- <c:out value="${fn:length(pageTemplateObj.pageTemplateFilters)}" /> --%>
		                       	<c:if test="${outerloop.index<4}">
		                        	<li>
		                            	<div class="form-group" id="form-grp0">
		                                  <label for="sel0" id="cel0">${pageTemplatesFiltersObj.filterLabel}:</label>
									      <select class="form-control selectpicker" data-live-search="true" id="sel0" name="${pageTemplatesFiltersObj.filterFieldName}" onchange="onChangeReportFilters('${outerloop.index}')">
										       <c:if test="${outerloop.index != 0 }">
										      		<option value="select">All</option>
										       </c:if>
										      <c:forEach var="filters" items="${dashboardFiltersListMapObj[pageTemplatesFiltersObj.filterFieldName]}" varStatus="innerloop1">
										      		 <c:choose>
										      			<c:when test="${selectedDashboardFilterValuesMapObj[pageTemplatesFiltersObj.filterFieldName]==filters}">
										      				<option selected="selected" value="${filters}">${filters}</option>
										      			</c:when>
										      			<c:otherwise>
										      				<option value="${filters}">${filters}</option>
										      			</c:otherwise>
										      		</c:choose> 
									           </c:forEach>
									      </select>
									      
									     </div>
		                            </li>
		                         </c:if>
		                       	</c:forEach>
		                       		<li>
		                       		<label for="sel0" id="cel0">Period:</label>
		                       		<c:set var="selectedPeriodValue"  value="${model.selectedPeriod}"/>
							        <form:select class="form-control selectpicker" data-live-search="true" id="sel0" name="PERIOD" path="selectedPeriod" onChange="onChangePeriodFilter(this.value)">
									      <c:choose>
					      						<c:when test="${selectedPeriodValue=='Weekly'}">
							      					 <option  value="Weekly" selected="selected">Weekly</option>
							      				</c:when>
							      				<c:otherwise>
					    		  					 <option  value="Weekly">Weekly</option>
						      					</c:otherwise>
					      					</c:choose> 
					      					<c:choose>
					      						<c:when test="${selectedPeriodValue=='Monthly'}">
							      					 <option value="Monthly" selected="selected">Monthly</option>
							      				</c:when>
							      				<c:otherwise>
					    		  					 <option value="Monthly">Monthly</option>
						      					</c:otherwise>
					      					</c:choose> 
									     	<c:choose>
					      						<c:when test="${selectedPeriodValue=='Quarterly'}">
							      					  <option value="Quarterly" selected="selected">Quarterly</option>
							      				</c:when>
							      				<c:otherwise>
					    		  					  <option value="Quarterly">Quarterly</option>
						      					</c:otherwise>
					      					</c:choose>
								     </form:select>
						      	</li>
					      	<!-- </ul>onchange="onChangeReportFilters('4') -->
				      	<%} %>
                    </li>
                     <li>
                     	<%if(isFrom != null && isFrom.equals("categoryLevelDashboard")){%>
                        	<a class="active-menu" href="#" onclick="dashboardClick();"><i class="fa fa-dashboard" style="  font-size: 22px;  margin-right: -1px;  padding: 4px;"></i> Dashboard<span class="fa arrow"></span></a>
                        <%}else{%>
                        	<a  href="#" onclick="dashboardFinanceFreezeClick();"><i class="fa fa-dashboard" style="  font-size: 22px;  margin-right: -1px;  padding: 4px;"></i> Dashboard<span class="fa arrow"></span></a>
                        <%}%>
                        <%if(isFrom != null && isFrom.equals("categoryLevelDashboard")){%>
                         <c:set var="pageTemplateObj"  value="<%=pageTemplate%>"/>
                         <c:set var="selectedDashboardFilterValuesMapObj" value="<%=selectedDashboardFilterValuesMapObj%>"/>
                        <c:set var="dashboardFiltersListMapObj" value="<%=dashboardFiltersListMapObj%>"/>
	                        	<c:forEach var="pageTemplatesFiltersObj" items="${pageTemplateObj.pageTemplateFilters}" varStatus="outerloop">
	                        	<li>
                            	<div class="form-group" id="form-grp0">
                                  <label for="sel0" id="cel0">${pageTemplatesFiltersObj.filterLabel}:</label>
							      <select class="form-control selectpicker" data-live-search="true" id="sel0" name="${pageTemplatesFiltersObj.filterFieldName}" onchange="onChangeCategoryLevelDashboardFilterFreeze('${outerloop.index}')">
								     <c:if test="${outerloop.index != 0 }">
								      	<option value="select">All</option>
								     </c:if>
								      <c:forEach var="filters" items="${dashboardFiltersListMapObj[pageTemplatesFiltersObj.filterFieldName]}" varStatus="innerloop1">
								      		 <c:choose>
								      			<c:when test="${selectedDashboardFilterValuesMapObj[pageTemplatesFiltersObj.filterFieldName]==filters}">
								      				<option selected="selected" value="${filters}">${filters}</option>
								      			</c:when>
								      			<c:otherwise>
								      				<option value="${filters}">${filters}</option>
								      			</c:otherwise>
								      		</c:choose> 
							           </c:forEach>
							      </select>
							     </div>
                            </li>
                       	</c:forEach>
                       	<%}%>
                    </li>
                </ul>
             <%
			}
			%>
			<%
			if(navibar.equals("6")){
			%>
				<ul class="nav" id="main-menu">
					<%if(selectedUtilitiesTab != null && selectedUtilitiesTab.equals(ApplicationConstants.TARGET_UTILITIES_TAB)){%>
						 <li>
	                        <a class="active-menu" href="dataUpload.htm"><img src="<%=contextPath%>/images/target.png" width="22px"
								height="24px" style="  margin-top: -2px;" />&nbsp; Upload Data</a>
	                    </li>
                    <%}else { %>
	                     <li>
	                        <a href="dataUpload.htm"><img src="<%=contextPath%>/images/target.png" width="22px"
								height="24px" style="  margin-top: -2px;" />&nbsp; Upload Data</a>
	                    </li>
                    <%} %>
                    <%if(selectedUtilitiesTab != null && selectedUtilitiesTab.equals(ApplicationConstants.NPI_UTILITIES_TAB)){%>
	                     <li>
	                        <a class="active-menu" href="newProductIntroduction.htm"><img src="<%=contextPath%>/images/NPI.png" width="22px"
								height="24px" style="  margin-top: -2px;" />&nbsp; New Product Introduction</a>
	                    </li>
                    <%}else { %>
	                    <li>
	                        <a  href="newProductIntroduction.htm"><img src="<%=contextPath%>/images/NPI.png" width="22px"
								height="24px" style="  margin-top: -2px;" />&nbsp; New Product Introduction</a>
	                    </li>
                     <%} %>
                     <%if(selectedUtilitiesTab != null && selectedUtilitiesTab.equals(ApplicationConstants.EOL_UTILITIES_TAB)){%>
	                    <li>
	                        <a class="active-menu" href="eolData.htm"><img src="<%=contextPath%>/images/EOL.png" width="22px"
								height="24px" style="  margin-top: -2px;" />&nbsp; End of Life</a>
	                    </li>
                     <%}else { %>
                    	 <li>
	                        <a href="eolData.htm"><img src="<%=contextPath%>/images/EOL.png" width="22px"
								height="24px" style="  margin-top: -2px;" />&nbsp; End of Life</a>
	                    </li>
                      <%} %>
                      <%if(selectedUtilitiesTab != null && selectedUtilitiesTab.equals(ApplicationConstants.NEW_SKU_UTILITIES_TAB)){%>
	                    <li>
	                        <a class="active-menu" href="newSku.htm"><img src="<%=contextPath%>/images/newsku.png" width="22px"
								height="24px" style="  margin-top: -2px;" />&nbsp; Assign SKU</a>
	                    </li>
                    <%}else { %>
                    	 <li>
	                        <a  href="newSku.htm"><img src="<%=contextPath%>/images/newsku.png" width="22px"
								height="24px" style="  margin-top: -2px;" />&nbsp; Assign SKU</a>
	                    </li>
	                <%} %>
                </ul>
			<%} 
			%>
						   <%
			if(navibar.equals("12")){
			%>
			<!-- <input type ="text" id="selectedTab"/> -->
                <ul class="nav" id="main-menu">
                    <li>
                    	 <%if(isFrom != null && isFrom.equals("historicalActual")){%>
                        	<%-- <a class="active-menu" href="#" onclick="historicalActualReportFilters('');"><img src="<%=contextPath%>/images/summary.png" width="33px" height="31px" style="  margin-top: -2px;  margin-left: -6px;" /> Historical Reports<!-- </span> --></a>
                        	<%}else{%>
                        		<a href="#" onclick="historicalActualReportFilters('');"><img src="<%=contextPath%>/images/summary.png" width="33px" height="31px" style="  margin-top: -2px;  margin-left: -6px;" /> Historical Reports</span></a>
						   	<%}%>				                
				                <li>
	                        	<% if(isFrom.equals("historicalActual")){%> --%>
		                   			<a class="active-menu"  href="#" ><img src="<%=contextPath%>/images/Report1.PNG" width="27px" height="27px" style="  margin-top: -2px;" />&nbsp;Actuals Report</a>
		                   		
		                   			<li>
		                   				<c:set var="closedPlanningCycleListObj" value="<%=closedPlanningCycleList%>"/>
			                       	   <label for="sel0" id="cel0" class="labelpadding">Planning Cycle:</label>
								       <select class="form-control selectpicker" data-live-search="true" id="sel0" name="PLANNINGCYCLE" onchange="historicalActualReportPlanningCycleFilters(this)">
									      <option value="0" selected="selected">Select</option>
									      <c:forEach var="obj" items="${closedPlanningCycleListObj}" varStatus="innerloop1">
								      			<c:choose>
										      			<c:when test="${obj.id == model.planningCycleId}">
										      				<option value="${obj.id}" selected="selected">${obj.weekYear}</option>
										      			</c:when>
										      			<c:otherwise>
										      				<option value="${obj.id}">${obj.weekYear}</option>
										      			</c:otherwise>
										      		</c:choose> 
								           </c:forEach> 
								      	</select>
								     	</li>
						            <c:set var="pageTemplateObj"  value="<%=pageTemplateReport%>"/>
			                        <c:set var="selectedDashboardFilterValuesMapObj" value="<%=selectedHistoricalActualReportFilterValuesMapObj%>"/>
			                        <c:set var="historicalActualReportFiltersListMapObj" value="<%=historicalActualReportFiltersListMapObj%>"/>
				                       	<c:forEach var="pageTemplatesFiltersObjActual" items="${pageTemplateObj.pageTemplateFilters}" varStatus="outerloop">
				                       	<%-- <c:out value="${fn:length(pageTemplateObj.pageTemplateFilters)}" /> --%>
					                            	<div class="form-group labelpadding">
					                                  <label class="labelpadding" for="sel0" id="cel0">${pageTemplatesFiltersObjActual.filterLabel}:</label>
												      <select class="form-control selectpicker" data-live-search="true" id="sel0" name="${pageTemplatesFiltersObjActual.filterFieldName}" onchange="onChangeHistoricalActualReportFilters('${outerloop.index}')">
													       <c:if test="${outerloop.index != 0 }">
													      		<option value="select">All</option>	
															</c:if>										      
													      <c:forEach var="filters" items="${historicalActualReportFiltersListMapObj[pageTemplatesFiltersObjActual.filterFieldName]}" varStatus="innerloop1">
													      		 <c:choose>
													      			<c:when test="${selectedHistoricalActualReportFilterValuesMapObj[pageTemplatesFiltersObjActual.filterFieldName]==filters}">
													      				<option selected="selected" value="${filters}">${filters}</option>
													      			</c:when>
													      			<c:otherwise>
													      				<option value="${filters}">${filters}</option>
													      			</c:otherwise>
													      		</c:choose> 
												           </c:forEach>
												      </select>
												     </div>
				                       	</c:forEach>
		                       			<li>
				                       		<label class="labelpadding" for="sel0" id="cel0">Period:</label>
				                       		<c:set var="selectedPeriodValue"  value="${model.selectedPeriod}"/>
									        <form:select class="form-control selectpicker" data-live-search="true" id="sel0" name="PERIOD" path="selectedPeriod" onChange="onChangeHistoricalPeriodFilter(this.value)">
											       <c:choose>
							      						<c:when test="${selectedPeriodValue=='Weekly'}">
									      					 <option  value="Weekly" selected="selected">Weekly</option>
									      				</c:when>
									      				<c:otherwise>
							    		  					 <option  value="Weekly">Weekly</option>
								      					</c:otherwise>
							      					</c:choose> 
							      					<c:choose>
							      						<c:when test="${selectedPeriodValue=='Monthly'}">
									      					 <option value="Monthly" selected="selected">Monthly</option>
									      				</c:when>
									      				<c:otherwise>
							    		  					 <option value="Monthly">Monthly</option>
								      					</c:otherwise>
							      					</c:choose> 
											     	<c:choose>
							      						<c:when test="${selectedPeriodValue=='Quarterly'}">
									      					  <option value="Quarterly" selected="selected">Quarterly</option>
									      				</c:when>
									      				<c:otherwise>
							    		  					  <option value="Quarterly">Quarterly</option>
								      					</c:otherwise>
							      					</c:choose> 
										     </form:select>
								      	</li>
				                       </ul>
		                   			<%}else{ %>
		                   			<a  href="#" id ="historicalActualTab"onclick="historicalActualReportFilters();"><img src="<%=contextPath%>/images/Report.PNG" width="27px" height="27px" style="  margin-top: -2px;" />&nbsp; Historical Actuals Report</span></a>
		                   			<%} %>
		                   			
				                    <!-- </li> -->
					                
					                
					                <!--end  -->
						      	<!-- </ul> -->
						      	<%-- <ul class="nav nav-second-level">
	                        	<li>
	                        	<% if(isFrom.equals("historicalActual")){%>
		                   			<a class="active-menu"  href="#" onclick="setSelectedTab('historicalForecastTab','historicalForecastTab')"><i class="fa fa-dashboard" style="  font-size: 22px;  margin-right: -1px;  padding: 4px;"></i>Historical Actual</span></a>
		                   			<%}else{ %>
		                   			<a  href="#" id ="historicalActualTab" onclick="historicalActualReportFilters('');"><i class="fa fa-dashboard" style="  font-size: 22px;  margin-right: -1px;  padding: 4px;"></i>Historical Actual</span></a>
		                   			<%} %>
				                    </li>
						      	</ul> --%>
						</li>
						<!-- acuuracy report -->
                   <%}%>
                   <%
			if(navibar.equals("13")){
			%>
			<!-- <input type ="text" id="selectedTab"/> -->
                <ul class="nav" id="main-menu">
						<!-- acuuracy report -->
						 <li>
						 <% if(isFrom.equals("accuracyReport")){%>
	                        <a class="active-menu"  href="#" ><img src="<%=contextPath%>/images/Report1.PNG" width="27px" height="27px" style="  margin-top: -2px;" />&nbsp; Accuracy Report</a>
	                        <c:set var="pageTemplateObj"  value="<%=pageTemplate%>"/>
	                        <c:set var="selectedFilterValuesMapObj" value="<%=selectedAccuracyReportFilterValuesMapObj%>"/>
	                        <c:set var="filtersListMapObj" value="<%=accuracyReportFiltersListMapObj%>"/>
	                        	<c:forEach var="pageTemplatesFiltersObj" items="${pageTemplateObj.pageTemplateFilters}" varStatus="outerloop">
	                        	<c:set var="outerLoopIndex" value="${outerloop.index}"></c:set>
	                        	 <c:if test="${outerloop.index == 0 }">
									 <li style="margin-top:5%">
				                       <label for="sel0" id="cel0">Planning Cycle:</label>
				                    	<form:select id="sel0" value="${model.noOfWeeks}" class="form-control selectpicker" data-live-search="true" name="noOfWeeks" path="noOfWeeks" onChange="onChangeAccuracyReportFilters('${outerLoopIndex}','${model.reportType}')">
				                    		<form:option value="6">6 weeks</form:option>
				                    		<form:option  value="12">12 weeks</form:option>
				                    		<form:option  value="24">24 weeks</form:option>
				                    		<form:option  value="36">36 weeks</form:option>
				                 	  	</form:select>
				                    </li>
								 </c:if>
	                        	
	                        	<li>
                            	<div class="form-group" id="form-grp0">
                                  <label for="sel0" id="cel0">${pageTemplatesFiltersObj.filterLabel}:</label>
							      <select class="form-control selectpicker" data-live-search="true" id="sel0" name="${pageTemplatesFiltersObj.filterFieldName}" onchange="onChangeAccuracyReportFilters('${outerloop.index}')">
								      <c:if test="${outerloop.index != 0 }">
										    <option value="select">All</option>
									  </c:if>
								      <c:forEach var="filters" items="${filtersListMapObj[pageTemplatesFiltersObj.filterFieldName]}" varStatus="innerloop1">
								      		 <c:choose>
								      			<c:when test="${selectedFilterValuesMapObj[pageTemplatesFiltersObj.filterFieldName]==filters}">
								      				<option selected="selected" value="${filters}">${filters}</option>
								      			</c:when>
								      			<c:otherwise>
								      				<option value="${filters}">${filters}</option>
								      			</c:otherwise>
								      		</c:choose> 
							           </c:forEach>
							      </select>
							     </div>
                            </li>
                       	</c:forEach>
                       	<form:hidden id="reportTypeId" path="reportType"/>
                       	<%} else{ %>
                       	 <a  href="accuracyReport.htm"><img src="<%=contextPath%>/images/Report.PNG" width="27px" height="27px" style="  margin-top: -2px;" />&nbsp; Accuracy Report</a>
                       	<%} %>
                   </li>
			</ul>
                   <%}%>
                   <%
			if(navibar.equals("14")){
			%>
			<!-- <input type ="text" id="selectedTab"/> -->
                <ul class="nav" id="main-menu">						
						<li>
						<% if(isFrom.equals("overrideReport")){%>
                        <a class="active-menu" href="#"><img src="<%=contextPath%>/images/Report1.PNG" width="27px" height="27px" style="  margin-top: -2px;" />&nbsp; Override Report</a>
                        <c:set var="pageTemplateObj"  value="<%=pageTemplate%>"/>
                        <c:set var="selectedDashboardFilterValuesMapObj" value="<%=selectedOverrideReportFilterValuesMapObj%>"/>
                        <c:set var="dashboardFiltersListMapObj" value="<%=OverrideReportFiltersListMapObj%>"/>
                        <c:set var="allPlanningCycleListObj" value="<%=allPlanningCycleList%>"/>
		                   		
                        <li>
                       	   <label for="sel0" id="cel0">Planning Cycle:</label>
					       <select class="form-control selectpicker" data-live-search="true" id="sel0" name="PLANNINGCYCLE" onchange="onChangeReportPlanningCycleFilters(this)">
						      <option value="0" selected="selected">Select</option>
						      <c:forEach var="obj" items="${allPlanningCycleListObj}" varStatus="innerloop1">
					      			<c:choose>
							      			<c:when test="${obj.id == model.planningCycleId}">
							      				<option value="${obj.id}" selected="selected">${obj.weekYear}</option>
							      			</c:when>
							      			<c:otherwise>
							      				<option value="${obj.id}">${obj.weekYear}</option>
							      			</c:otherwise>
							      		</c:choose> 
					           </c:forEach> 
					      </select>
					     </li>
                       	<c:forEach var="pageTemplatesFiltersObj" items="${pageTemplateObj.pageTemplateFilters}" varStatus="outerloop">
		                       	<c:if test="${outerloop.index<8}">
		                       	
		                        	<li>
		                            	<div class="form-group" id="form-grp0">
		                            		                            
		                                  <label for="sel0" id="cel0">${pageTemplatesFiltersObj.filterLabel}:</label>
									      <select class="form-control selectpicker" data-live-search="true" id="sel0" name="${pageTemplatesFiltersObj.filterFieldName}" onchange="onChangeOverrideReportFilters('${outerloop.index}')">
										       <c:if test="${outerloop.index != 0 }">
										      		<option value="select">All</option>
											</c:if>	
								
						      									      
										      <c:forEach var="filters" items="${dashboardFiltersListMapObj[pageTemplatesFiltersObj.filterFieldName]}" varStatus="innerloop1">
										      		 <c:choose>
										      			<c:when test="${selectedDashboardFilterValuesMapObj[pageTemplatesFiltersObj.filterFieldName]==filters}">
										      				<option selected="selected" value="${filters}">${filters}</option>
										      			</c:when>
										      			<c:otherwise>
										      				<option value="${filters}">${filters}</option>
										      			</c:otherwise>
										      		</c:choose> 
									           </c:forEach>
									      </select>
									      
									     </div>
		                            </li>
		                         </c:if>
		                       	</c:forEach>
		                       	</ul>
		                       	<%}else{%>
		                       	<a href="overrideReport.htm"><img src="<%=contextPath%>/images/Report.PNG" width="27px" height="27px" style="  margin-top: -2px;" />&nbsp; Override Report</a>
		                       	
		                       	<%} %>
                       
                   		 </li> 
						
                   <%}%>
                   <%
			if(navibar.equals("15")){
			%>
			<!-- <input type ="text" id="selectedTab"/> -->
                <ul class="nav" id="main-menu">
                    	<!-- Executive Report -->
						
						<li>
                     	<%if(isFrom != null && isFrom.equals("categoryLevelReport")){%>
                        	<a  class="active-menu" href="#"  ><img src="<%=contextPath%>/images/Report1.PNG" width="27px" height="27px" style="  margin-top: -2px;" />Executive Report</a>
                        <%}else{%>
                        	<a  href="#" onclick="executiveReports();" ><img src="<%=contextPath%>/images/Report.PNG" width="27px" height="27px" style="  margin-top: -2px;" />&nbsp; Executive Report</a>
                        <%}%>
                        <%if(isFrom != null && isFrom.equals("categoryLevelReport")){%>
                        
	                    	<c:set var="pageTemplateObj"  value="<%=pageTemplate%>"/>
	                        <c:set var="selectedDashboardFilterValuesMapObj" value="<%=selectedReportFilterValuesMapObj%>"/>
	                        <c:set var="dashboardFiltersListMapObj" value="<%=reportFiltersListMapObj%>"/>
	                        <c:set var="closedPlanningCycleListObj" value="<%=closedPlanningCycleList%>"/>
		                   		
	                        <li>
                       	   <label for="sel0" id="cel0">Planning Cycle:</label>
					       <select class="form-control selectpicker" data-live-search="true" id="sel0" name="PLANNINGCYCLE" onchange="onChangeExecutiveReportPlanningCycleFilter(this)">
						      <option value="0" selected="selected">Select</option>
						      <c:forEach var="obj" items="${closedPlanningCycleListObj}" varStatus="innerloop1">
					      			<c:choose>
							      			<c:when test="${obj.id == model.planningCycleId}">
							      				<option value="${obj.id}" selected="selected">${obj.weekYear}</option>
							      			</c:when>
							      			<c:otherwise>
							      				<option value="${obj.id}">${obj.weekYear}</option>
							      			</c:otherwise>
							      		</c:choose> 
					           </c:forEach> 
					      </select>
					     </li>
		                       	<c:forEach var="pageTemplatesFiltersObj" items="${pageTemplateObj.pageTemplateFilters}" varStatus="outerloop">
		                       	<%-- <c:out value="${fn:length(pageTemplateObj.pageTemplateFilters)}" /> --%>
		                       	<c:if test="${outerloop.index<4}">
		                        	<li>
		                            	<div class="form-group" id="form-grp0">
		                                  <label for="sel0" id="cel0">${pageTemplatesFiltersObj.filterLabel}:</label>
									      <select class="form-control selectpicker" data-live-search="true" id="sel0" name="${pageTemplatesFiltersObj.filterFieldName}" onchange="onChangeExecutiveReportFilters('${outerloop.index}')">
										       <c:if test="${outerloop.index != 0 }">
										      		<option value="select">All</option>
											</c:if>										      
										      <c:forEach var="filters" items="${dashboardFiltersListMapObj[pageTemplatesFiltersObj.filterFieldName]}" varStatus="innerloop1">
										      		 <c:choose>
										      			<c:when test="${selectedDashboardFilterValuesMapObj[pageTemplatesFiltersObj.filterFieldName]==filters}">
										      				<option selected="selected" value="${filters}">${filters}</option>
										      			</c:when>
										      			<c:otherwise>
										      				<option value="${filters}">${filters}</option>
										      			</c:otherwise>
										      		</c:choose> 
									           </c:forEach>
									      </select>
									     </div>
		                            </li>
		                         </c:if>
		                       	</c:forEach>
		                       	<li>
		                       		<label for="sel0" id="cel0">Period:</label>
		                       		<c:set var="selectedPeriodValue"  value="${model.selectedPeriod}"/>
							        <form:select class="form-control selectpicker" data-live-search="true" id="sel0" name="PERIOD" path="selectedPeriod" onChange="onChangeExecutivePeriodFilter(this.value)">
									       <c:choose>
					      						<c:when test="${selectedPeriodValue=='Weekly'}">
							      					 <option  value="Weekly" selected="selected">Weekly</option>
							      				</c:when>
							      				<c:otherwise>
					    		  					 <option  value="Weekly">Weekly</option>
						      					</c:otherwise>
					      					</c:choose> 
					      					<c:choose>
					      						<c:when test="${selectedPeriodValue=='Monthly'}">
							      					 <option value="Monthly" selected="selected">Monthly</option>
							      				</c:when>
							      				<c:otherwise>
					    		  					 <option value="Monthly">Monthly</option>
						      					</c:otherwise>
					      					</c:choose> 
									     	<c:choose>
					      						<c:when test="${selectedPeriodValue=='Quarterly'}">
							      					  <option value="Quarterly" selected="selected">Quarterly</option>
							      				</c:when>
							      				<c:otherwise>
					    		  					  <option value="Quarterly">Quarterly</option>
						      					</c:otherwise>
					      					</c:choose> 
								     </form:select>
						      	</li>
					      	</ul><!-- onchange="onChangeReportFilters('4') -->
				      	<%} %>
                    </li>
                   <%}%>
                   <%
			if(navibar.equals("16")){
			%>
			<!-- <input type ="text" id="selectedTab"/> -->
                <ul class="nav" id="main-menu">
						<li>
                     	<%if(isFrom != null && isFrom.equals("planogramReport")){%>
                        	<a  class="active-menu" href="#"  ><img src="<%=contextPath%>/images/Report1.PNG" width="27px" height="27px" style="  margin-top: -2px;" />&nbsp; Planogram Report</a>
                        <%}else{%>
                        	<a  href="#" onclick="getPlanogramReports();" ><img src="<%=contextPath%>/images/Report.PNG" width="27px" height="27px" style="  margin-top: -2px;" />&nbsp; Planogram Report</a>
                        <%}%>
                        <%if(isFrom != null && isFrom.equals("planogramReport")){%>
	                    	<c:set var="pageTemplateObj"  value="<%=planogramPageTemplate%>"/>
	                        <c:set var="selectedDashboardFilterValuesMapObj" value="<%=selectedPlanogramReportFilterValuesMapObj%>"/>
	                        <c:set var="dashboardFiltersListMapObj" value="<%=planogramReportFiltersListMapObj%>"/>
	                        <c:set var="closedPlanningCycleListObj" value="<%=closedPlanningCycleList%>"/>
	                        <li>
								<label for="sel0" id="cel0">Planning Cycle:</label>
						       <select class="form-control selectpicker" data-live-search="true" id="sel0" name="PLANNINGCYCLE" onchange="planogramReportPlanningCycleFilters(this)">
							      <option value="0" selected="selected">Select</option>
							      <c:forEach var="obj" items="${closedPlanningCycleListObj}" varStatus="innerloop1">
						      			<c:choose>
								      			<c:when test="${obj.id == model.planningCycleId}">
								      				<option value="${obj.id}" selected="selected">${obj.weekYear}</option>
								      			</c:when>
								      			<c:otherwise>
								      				<option value="${obj.id}">${obj.weekYear}</option>
								      			</c:otherwise>
								      		</c:choose> 
						           </c:forEach> 
						      	</select>
							 </li>
		                       	<c:forEach var="pageTemplatesFiltersObj" items="${pageTemplateObj.pageTemplateFilters}" varStatus="outerloop">
		                        	<li>
		                            	<div class="form-group" id="form-grp0">
		                                  <label for="sel0" id="cel0">${pageTemplatesFiltersObj.filterLabel}:</label>
									      <select class="form-control selectpicker" data-live-search="true" id="sel0" name="${pageTemplatesFiltersObj.filterFieldName}" onchange="onChangePlanogramReportFilters('${outerloop.index}')">
										       <c:if test="${outerloop.index != 0 }">
										      		<option value="select">All</option>
											</c:if>										      
										      <c:forEach var="filters" items="${dashboardFiltersListMapObj[pageTemplatesFiltersObj.filterFieldName]}" varStatus="innerloop1">
										      		 <c:choose>
										      			<c:when test="${selectedDashboardFilterValuesMapObj[pageTemplatesFiltersObj.filterFieldName]==filters}">
										      				<option selected="selected" value="${filters}">${filters}</option>
										      			</c:when>
										      			<c:otherwise>
										      				<option value="${filters}">${filters}</option>
										      			</c:otherwise>
										      		</c:choose> 
									           </c:forEach>
									      </select>
									     </div>
		                            </li>
		                       	</c:forEach>
		                       	<li>
		                       		<label for="sel0" id="cel0">Period:</label>
		                       		<c:set var="selectedPeriodValue"  value="${model.selectedPeriod}"/>
							        <form:select class="form-control selectpicker" data-live-search="true" id="sel0" name="PERIOD" path="selectedPeriod" onChange="onChangePlanogramPeriodFilter(this.value)">
									     <c:choose>
					      						<c:when test="${selectedPeriodValue=='Weekly'}">
							      					 <option  value="Weekly" selected="selected">Weekly</option>
							      				</c:when>
							      				<c:otherwise>
					    		  					 <option  value="Weekly">Weekly</option>
						      					</c:otherwise>
					      					</c:choose> 
					      					<c:choose>
					      						<c:when test="${selectedPeriodValue=='Monthly'}">
							      					 <option value="Monthly" selected="selected">Monthly</option>
							      				</c:when>
							      				<c:otherwise>
					    		  					 <option value="Monthly">Monthly</option>
						      					</c:otherwise>
					      					</c:choose> 
									     	<c:choose>
					      						<c:when test="${selectedPeriodValue=='Quarterly'}">
							      					  <option value="Quarterly" selected="selected">Quarterly</option>
							      				</c:when>
							      				<c:otherwise>
					    		  					  <option value="Quarterly">Quarterly</option>
						      					</c:otherwise>
					      					</c:choose> 
								     </form:select>
						      	</li>
						      	</ul>
					      	<!-- </ul>onchange="onChangeReportFilters('4') -->
				      	<%} %>
                    </li>
						
						
						<!-- End of Planogram Report -->
                   <%}%>
					</li>
				</ul>
				<!-- SKU Forecasted values -->
			<%
			if(navibar.equals("17")){
			%>
					<ul class="nav" id="main-menu">
					

                    <li>
                    <%if(true){%>
                        <a class="active-menu" href="planningLogDetails.htm"><img src="<%=contextPath%>/images/cycle2.png" width="30px"
							height="32px" style="  margin-top: -2px;" /><strong> Planning Log Forecast</strong><span class="fa arrow"></span></a>
							
						<ul class="nav nav-second-level">
	                        <c:set var="pageTemplateObj"  value="<%=pageTemplate%>"/>
	                        <c:set var="mapKeyObj" value="<%=selectedFilterValuesMapObj%>"/>
	                        <c:set var="filtersListMapObj" value="<%=filtersListMapObj%>"/>
		                        	<c:forEach var="pageTemplatesFiltersObj" items="${pageTemplateObj.pageTemplateFilters}" varStatus="outerloop">
		                        	<li>
	                            	<div class="form-group" id="form-grp0">
	                                  <label for="sel0" id="cel0">${pageTemplatesFiltersObj.filterLabel}:</label>
								      <select class="form-control selectpicker" data-live-search="true" id="sel0" name="${pageTemplatesFiltersObj.filterFieldName}" onchange="onChangeSkuForecastValuesFilter('${outerloop.index}')">
									      <c:if test="${outerloop.index != 0 }">
									      	<option value="select">All</option>
									      </c:if>
									      <c:forEach var="filters" items="${filtersListMapObj[pageTemplatesFiltersObj.filterFieldName]}" varStatus="innerloop1">
									      		 <c:choose>
									      			<c:when test="${mapKeyObj[pageTemplatesFiltersObj.filterFieldName]==filters}">
									      				<option selected="selected" value="${filters}">${filters}</option>
									      			</c:when>
									      			<c:otherwise>
									      				<option value="${filters}">${filters}</option>
									      			</c:otherwise>
									      		</c:choose> 
								           </c:forEach>
								      </select>
								     </div>
	                            </li>
	                       	</c:forEach>
	                       <li class="divider"></li>
	                       <%if(roles != null && (roles.contains("BI2I_ADMIN"))){ %>
	                            <li>
		                            <div class="form-group" id="form-grp7" style="  margin-bottom: 1px;">
			                            <input type="button" id="Initiate" value="Initiate" class="btn btn-info btn-xs"  onclick="confirmInitiate()"/>
			                            <input type="button" id="Discard" value="Discard" class="btn btn-info btn-xs"  onclick="confirmDiscard()"/>
		                            </div>
	                            </li>
	                       <%}%>
                        </ul>
					<%}else{ %>
						<a href="planningLogDetails.htm"><img src="<%=contextPath%>/images/cycle2.png" width="30px"
							height="32px" style="  margin-top: -2px;" /><strong> Planning Log</strong><span class="fa arrow"></span></a>
					<%}%>
                      
                    </li>
                    <%if(roles != null && (roles.contains("BI2I_ADMIN"))){ %>
	                     <li>
	                     <%if(activePlanningTab != null && activePlanningTab.equals("categoryReport")){%>
	                        <a class="active-menu" href="categoryLevelSummary.htm?isFromCategoryReport=true"><img src="<%=contextPath%>/images/Report1.PNG" width="30px"
								height="32px" style="  margin-top: -2px;" /><strong>Category Report</strong></a>
						<%}else{ %>
							<a href="categoryLevelSummary.htm?isFromCategoryReport=true"><img src="<%=contextPath%>/images/Report1.PNG" width="30px"
								height="32px" style="  margin-top: -2px;" /><strong>Category Report</strong></a>
						<%} %>
						</li>
					<%}%>
                </ul>				
			<%} 
			%>
			
			<!--  -->
			 
			 
			</form:form>
            </div>

        </nav>
         <%
			if(navibar.equals("1") || navibar.equals("2")){
				
			%>
				<div style="margin-left: 80px; margin-top: 530px; width: 200px; position: fixed;color: #fff;z-index=4">
				Powered by: <br> <a href="http://www.bridgei2i.com"
					target="_blank"> <img
					src="<%=contextPath%>/images/bi2i-logo-png.png" width="40%"
					height="40%" /></a>
			</div>
			<%} else if(navibar.equals("12") || navibar.equals("13") || navibar.equals("14") || navibar.equals("15") || navibar.equals("16") || navibar.equals("6")){ %>
			<div style="margin-left: 80px; margin-top: 540px; width: 200px; position: fixed;color: #fff;z-index=4">
			Powered by: <br> <a href="http://www.bridgei2i.com"
				target="_blank"> <img
				src="<%=contextPath%>/images/bi2i-logo-png.png" width="40%"
				height="40%" /></a>
		</div>
			<%} else if(navibar.equals("3")){ %>
			<div style="margin-left: 80px; margin-top: 544px; width: 200px; position: fixed;color: #fff;z-index=4">
			Powered by: <br> <a href="http://www.bridgei2i.com"
				target="_blank"> <img
				src="<%=contextPath%>/images/bi2i-logo-png.png" width="40%"
				height="40%" /></a>
		</div>
			<%} else{%>
					<div style="margin-left: 80px; margin-top: 570px; width: 200px; position: fixed;color: #fff;z-index=4">
				Powered by: <br> <a href="http://www.bridgei2i.com"
					target="_blank"> <img
					src="<%=contextPath%>/images/bi2i-logo-png.png" width="40%"
					height="40%" /></a>
				</div>
			
			<%} %>
        
        <!-- /. NAV SIDE  -->