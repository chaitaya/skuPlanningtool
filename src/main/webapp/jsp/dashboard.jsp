<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.bridgei2i.common.vo.Users"%>
<%@page import="com.bridgei2i.common.util.ApplicationUtil"%>
<%@page import="com.bridgei2i.common.constants.ApplicationConstants"%>
<%@page import="com.bridgei2i.common.vo.PageTemplate"%>
<%@page import="java.util.Map"%>
<%@page
	import="com.bridgei2i.common.controller.LoadApplicationCacheService"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
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
    

<%
	Map pageTemplateCacheObj = (Map) LoadApplicationCacheService.applicationCacheObject
			.get(ApplicationConstants.PAGE_TEMPLATES_CACHE_KEY);	
	PageTemplate pageTemplate = (PageTemplate)pageTemplateCacheObj.get("PC_DASHBOARD");
	
	Map forecastingListMap = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.FORECAST_LIST_MAP, request);
	if(forecastingListMap == null){
		forecastingListMap = new HashMap();
	}
	Map eventCalendarMapObj = (Map)LoadApplicationCacheService.applicationCacheObject
			.get(ApplicationConstants.EVENT_CALENDAR_MAP);
	if(eventCalendarMapObj==null){
		eventCalendarMapObj = new HashMap();
	}
	
	Users users = (Users) ApplicationUtil.getObjectFromSession(ApplicationConstants.APPLICATION_ACT_AS_USER, request);
	List rolesList = users.getRolesList();
	if(rolesList==null){
		rolesList = new ArrayList();
	}
	
%>    
<style>
.float_center {
  float: right;

  position: relative;
  left: -50%; /* or right 50% */
  text-align: left;
}
</style>

<script type="text/javascript">	
function onBackButton(){
	planningCycleBeanForm.action="planningLogDetails.htm";
	var isFromDashboardObj = document.getElementById("isFromDashboard");
	isFromDashboardObj.value = 'true';
	planningCycleBeanForm.submit();
	
}

function overrideForecastingUnits() {
		$("#editForecastUnits").modal('show');
}

function overrideForecastingAsp() {
	$("#editForecastAsp").modal('show');
}

function saveForecastUnitsOverride(){
	loadProgress();
	document.getElementById("unitsOverrideButton").disabled = true;
	planningCycleBeanForm.action="saveForecastUnitsOverride.htm";
	planningCycleBeanForm.submit();
}

function saveForecastAspOverride(){
	loadProgress();
	document.getElementById("aspOverrideButton").disabled = true;
	planningCycleBeanForm.action="saveForecastAspOverride.htm";
 	planningCycleBeanForm.submit();
}

function getTableView(index){
	var tableObj = document.getElementById("tab"+index);
	tableObj.style.display = "";
	/* document.getElementById("combinationTag"+id).style.height= "0px";
	document.getElementById("combinationTag"+id).style.width= "0%"; */
	var chartObj = document.getElementById("chart"+index);
	chartObj.style.display = "none";
}

function getChartView(index){
	var tableObj = document.getElementById("tab"+index);
	tableObj.style.display = "none";
/* 	document.getElementById("combinationTag"+id).style.height= "400px";
	document.getElementById("combinationTag"+id).style.width= "100%"; */
	var chartObj = document.getElementById("chart"+index);
	chartObj.style.display = "";
	
}

 
$(document).ready(function () {
 //alert("inside export1");
    function exportTableToCSV($table, filename) {
    	//alert($table );
    	//alert("export2");
        var $rows = ($table).find('tr:has(th)'),
        

            tmpColDelim = String.fromCharCode(11), 
            tmpRowDelim = String.fromCharCode(0), 

            colDelim = '","',
            rowDelim = '"\r\n"',

            csv = '"' + $rows.map(function (i, row) {
                var $row = $(row),
                    $cols = $row.find('th');

                return $cols.map(function (j, col) {
                    var $col = $(col),
                        text = $col.text();

                    return text.replace(/"/g, '""'); // escape double quotes

                }).get().join(tmpColDelim);

            }).get().join(tmpRowDelim)
                .split(tmpRowDelim).join(rowDelim)
                .split(tmpColDelim).join(colDelim) + rowDelim+
                  ($table.find('tr:has(td)')).map(function (i, row) {
                    var $row = $(row),
                        $cols = $row.find('td');

                    return $cols.map(function (j, col) {
                        var $col = $(col),
                            text = $col.text();

                        return text.replace(/"/g, '""'); // escape double quotes

                    }).get().join(tmpColDelim);

                }).get().join(tmpRowDelim)
                    .split(tmpRowDelim).join(rowDelim)
                    .split(tmpColDelim).join(colDelim) + '"',

                
            csvData = 'data:application/csv;charset=utf-8,' + encodeURIComponent(csv);

        $(this)
            .attr({
            'download': filename,
                'href': csvData,
                'target': '_blank'
        });
    }

    $(".export").on('click', function (event) {
    	var inputID = $(this).attr('id');
    	var address='#tab'+inputID+'>table';
        //alert("Export3");
    	exportTableToCSV.apply(this, [$(address), 'exportExcel.csv']);
        
        
    });
});

  

</script>

  <style>
		#row1{
		  margin-right: -40px;
		  margin-left: -25px;
		}
		
		#page-inner{
		margin-top: 0px;
		}
		/* div#footer{
		margin-top: 97px;
		}
		#wrapper{
		height:1174px;
		}
		#page-wrapper{
		height:1220px;
		} */
		
		#inner-row0{
			margin-top: 6%
		}
		
		.legend{
			height: 20px;
		  	width: 2%;
		 	float: left;
		}

		#legend1{
			 background: gray;
			  
		}
		#legends0,#legends1{
			  margin-left: 30%;
 			  width: 100%;
		}
		#legends2,#legends3,#legends4{
			  margin-left: 40%;
 			  width: 100%;
		}
	
		#legend2{
			 background: #2DAFCB;
		}
		#legend3{
			 background: green;
		}
		#legend4{
			 background: orange;
		}
		#legendName2,#legendName3,#legendName4{
		
		  float: left;
		  width: 13%;
		  padding-left: 1%;
		}
		#legendName1{
		 float: left;
		  width: 7%;
		  padding-left: 1%;
		}
		#tab0, #tab1, #tab2, #tab3, #tab4{
			height: 340px;
			overflow-y: auto;
			
		}
		#actions{
		height: 22px ;
		}
		
	</style>
	
	<form:form method="post" action="dashboard.htm" commandName="model" name="planningCycleBeanForm" >
	<form:hidden id="planningCycleId" path="planningCycleId"/>
	<input type="hidden" name="isFromDashboard" id="isFromDashboard"/>
    <div id="wrapper">
       
        <div id="page-wrapper"  style="  padding-left: 17px; padding-right: 25px; min-height: 500px;overflow-x:hidden">
            <div id="page-inner" style="min-height: 500px;overflow-x:fixed">
			 <div class="row" id="row1" style="margin-top:-2.5%;">
                    <div class="col-md-12">
                        <div class="panel panel-default" style="height:520px;width:100%;">
                        	<c:set var="pageTemplateObj"  value="<%=pageTemplate%>"/>
							<div class="panel-heading" style="background-color:#2DAFCB;font-size:130%;height:52px;padding:7px 15px;color:#fff;padding-top:1.5%  ">
								&nbsp;${pageTemplateObj.pageTitle}
								<div class="pull-right" style="text-align: right;">
									<a href="#" onclick="onBackButton();" ><img src="./images/back2.png" title="Go Back" style="width: 59%; height: 30%;
  									vertical-align: -webkit-baseline-middle; margin-top: -17px;"/></a>
								</div>
								<div id="components" style="width:100%;font-size:small;color:black">
									<c:if test="${model.type=='PRODUCT'}">
										<label><b>Product Num:</b></label><label style="font-size:15px;font-weight:400">&nbsp;${model.selectedTypeValue}</label>
										<label style="margin-left: 1%"><b>&nbsp;Product Description:</b></label><label style="font-size:15px;font-weight:400">&nbsp;${model.productDescription}</label>
										<label style="margin-left: 1%"><b>&nbsp;Product Hierarchy II:</b></label><label style="font-size:15px;font-weight:400">&nbsp;${model.productHierarchyII}</label>
										<label style="margin-left: 1%"><b>&nbsp;Planogram:</b></label><label style="font-size:15px;font-weight:400">&nbsp${model.planogram}</label>
									</c:if>
									<c:if test="${model.type=='MODEL'}">
										<label><b>Model Name:</b></label>${model.selectedTypeValue}
									</c:if>
									<c:if test="${model.type=='CATEGORY'}">
										<label><b>Category Name:</b></label>${model.selectedTypeValue}
									</c:if>
								</div>
							</div>
							<div class="panel-body" style="width:100%;height:456px;overflow-y:auto">
								<c:forEach var="pageTemplatesChartsObj" items="${pageTemplateObj.pageTemplateCharts}" varStatus="outerloop">
									
			                        	<div class="panel panel-default" style="height:430px;margin-top:0px" id="inner-row${outerloop.index}">
											<div class="panel-heading" style="background-color:#CFCFCF;;font-size:100%;height:40px;padding:7px 15px;color:#fff  ">
												&nbsp;${pageTemplatesChartsObj.chartTitle}
												<div class="pull-right" style="text-align: right;">
												<% if(!rolesList.contains(ApplicationConstants.ADMIN) && !rolesList.contains(ApplicationConstants.CATEGORY_DIRECTOR) && !rolesList.contains(ApplicationConstants.FINANCE_DIRECTOR)){%>
													<c:if test="${model.type=='CATEGORY'}">
														<% if(!rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){%>
															<c:if test="${pageTemplatesChartsObj.tableName=='ForecastingUnits'}">
																<c:if test="${model.enableOverride==0}">
																	<img src="./images/Pencil.png"  title="" width="13%" height="13%"/>
																</c:if>
															<c:if test="${model.enableOverride==1}">
																<img src="./images/edit.png"  title="Override Forecast Units" width="13%" height="13%" onclick="overrideForecastingUnits();"/>
															</c:if>
														</c:if>
														<c:if test="${pageTemplatesChartsObj.tableName=='ForecastingASP'}">
															<c:if test="${model.enableOverride==0}">
																<img src="./images/Pencil.png" title="" width="13%" height="13%"/>
															</c:if>
															<c:if test="${model.enableOverride==1}">
																<img src="./images/edit.png" title="Override Forecast Units" width="13%" height="13%" onclick="overrideForecastingAsp();"/>
															</c:if>
														</c:if>		
														<%} %>
													</c:if>
														<c:if test="${model.type!='CATEGORY'}">
															<c:if test="${pageTemplatesChartsObj.tableName=='ForecastingUnits'}">
																<c:if test="${model.enableOverride==0}">
																	<img src="./images/Pencil.png"  title="" width="13%" height="13%"/>
																</c:if>
																<c:if test="${model.overrideButtonFlag==0}">
																	<img src="./images/Pencil.png"  title="" width="13%" height="13%"/>
																</c:if>
																<c:if test="${model.enableOverride==1 && model.overrideButtonFlag==1}">
																	<img src="./images/edit.png"  title="Override Forecast Units" width="13%" height="13%" onclick="overrideForecastingUnits();"/>
																</c:if>
															</c:if>
															<c:if test="${pageTemplatesChartsObj.tableName=='ForecastingASP'}">
																<c:if test="${model.enableOverride==0}">
																	<img src="./images/Pencil.png" title="" width="13%" height="13%"/>
																</c:if>
																<c:if test="${model.overrideButtonFlag==0}">
																	<img src="./images/Pencil.png" title="" width="13%" height="13%"/>
																</c:if>
																<c:if test="${model.enableOverride==1 && model.overrideButtonFlag==1}">
																	<img src="./images/edit.png" title="Override Forecast Units" width="13%" height="13%" onclick="overrideForecastingAsp();"/>
																</c:if>
															</c:if>		
														</c:if>
												<%}%>
													<div class="btn-group">
														<button type="button"
															class="btn btn-default btn-xs dropdown-toggle"
															data-toggle="dropdown" id= "actions">
															Actions <span class="caret"></span>
														</button>
														<ul class="dropdown-menu pull-right" role="menu" style="min-width: 16px;">
															 <li><a id='${outerloop.index}' href="#" class="export"><img src="./images/excel_icon_unselected.png" title="Export to excel" width="20" height="20"/>    
															</a></li>
															<li><a onClick="getTableView('${outerloop.index}');" style="cursor:pointer;" ><i class="fa fa-table" style="font-size: 22px" title="Table View"></i></a></li>
															<li><a onClick="getChartView('${outerloop.index}');" style="cursor:pointer;" ><i class="fa fa-bar-chart-o" style="font-size: 19px" title="Chart View"></i></a></li>
														</ul>
													</div>
												</div>

<c:if test="${pageTemplatesChartsObj.tableName=='ForecastingUnits'}">
<div id="editForecastUnits" class="modal fade" >
<div class="modal-dialog" style="width: 1000px;height:700px;margin-right:65px;margin-top:50px;overflow:auto; ">
    <div class="modal-content">
        <div class="modal-header">
           <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h4 class="modal-title" style="background: 6868" ><font color="black" >Edit Forecast Units:</font></h4>
</div>
<div class="modal-body" style="height:450px;width:100%;">
<!-- <font color="black"><b>BaseForecast:</b></font>
 --><div style="overflow-x: auto;  width: 100%;">
 <font color="black"><b>BaseForecast:</b></font>
<div class="table-responsive" id="table1"> 
<table class="table">
<tbody>
<tr class="success">
<c:forEach var="forecastUnitsObj" items="${forecastingListMap['baseForecastUnitsList']}">
	 	<td width="100px" style="  text-align: center;  background-color: #2DAFCB;">${forecastUnitsObj.forecastPeriod}</td>
</c:forEach>
</tr>
<tr class="info">
<c:forEach var="forecastUnitsObj" items="${forecastingListMap['baseForecastUnitsList']}">
	 	<td width="100px" style="  text-align: center;  background-color: #dff0d8;  color: black;"><fmt:formatNumber type="number" maxFractionDigits="0" value="${forecastUnitsObj.forecastValue}"/></td>
</c:forEach>
</tr>
</tbody>
</table>
</div>
<% if(!rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){%> 
<font color="black"><b>Current Forecast:</b></font>
<div class="table-responsive" id="table2"> 
<table class="table">
<tbody>
<tr class="success">
<c:forEach var="forecastUnitsObj" items="${forecastingListMap['currentForecastUnitsList']}">
	 	<td width="100px" style="  text-align: center;  background-color: #2DAFCB;">${forecastUnitsObj.forecastPeriod}</td>
</c:forEach>
</tr>
<tr class="info">
<c:forEach var="forecastUnitsObj" items="${forecastingListMap['currentForecastUnitsList']}">
	 	<td width="100px" style="  text-align: center;   background-color: #dff0d8;  color: black;"><fmt:formatNumber type="number" maxFractionDigits="0" value="${forecastUnitsObj.forecastValue}"/></td>
</c:forEach>
</tr>
</tbody>
</table>
</div>
<c:set var="eventCalendarMapObj"  value="<%=eventCalendarMapObj%>"/>
<font color="black"><b>Override Forecast:</b><div id="errMsg" style="color: red;text-decoration: none" class="float_center"></div></font>
<div class="table-responsive" id="table3"> 
<table class="table">
<tbody>
<tr class="success">
<c:forEach var="overrideForecastUnitsList" items="${model.overrideForecastUnitsList}">
	 	<td width="100px" style="text-align: center; background-color: #2DAFCB;">
	 		${overrideForecastUnitsList.forecastPeriod}<br/>
	 		<c:set var="eventCalendarMapKey"  value="${overrideForecastUnitsList.forecastPeriod}${model.business}"/>
	 		<c:set var="eventCalendarToolTipKey"  value="${overrideForecastUnitsList.forecastPeriod}${model.business}1"/>
	 		<font size="1px" Style="color: #fff;"><span title="${eventCalendarMapObj[eventCalendarToolTipKey]}">${eventCalendarMapObj[eventCalendarMapKey]}</span></font>
	 	</td>
</c:forEach>
</tr>
<tr class="info">
<c:forEach var="overrideForecastUnitsList" items="${model.overrideForecastUnitsList}" varStatus="loop">
	 	<td style="text-align: center;  background-color: #dff0d8;"><form:input id="overrideForecastUnits${loop.index}" class="validate" path="overrideForecastUnitsList[${loop.index}].forecastValue"   style="width: 64%;text-align: center;  color: black;"/></td>
</c:forEach>
</tr>
</tbody>
</table>
</div>
<%} else {%>

<font color="black"><b>Current Forecast:</b></font>
<div class="table-responsive" id="table2"> 
<table class="table">
<tbody>
<tr class="success">
<c:forEach var="forecastUnitsObj" items="${forecastingListMap['baseForecastUnitsList']}">
	 	<td width="100px" style="  text-align: center; background-color: #2DAFCB;">${forecastUnitsObj.forecastPeriod}</td>
</c:forEach>
</tr>
<tr class="info">
<c:forEach var="forecastUnitsObj" items="${forecastingListMap['baseForecastUnitsList']}">
	 	<td width="100px" style="  text-align: center;   background-color: #dff0d8;  color: black;"><fmt:formatNumber type="number" maxFractionDigits="0" value="${forecastUnitsObj.forecastValue}"/></td>
</c:forEach>
</tr>
</tbody>
</table>
</div>
<c:set var="eventCalendarMapObj"  value="<%=eventCalendarMapObj%>"/>
<font color="black"><b>Override Forecast:</b><div id="errMsg" style="color: red;text-decoration: none" class="float_center"></div></font>
<div class="table-responsive" id="table3"> 
<table class="table">
<tbody>
<tr class="success">
<c:forEach var="overrideForecastUnitsList" items="${model.overrideForecastUnitsList}">
	 	<td width="100px" style="text-align: center; background-color: #2DAFCB;">
	 		${overrideForecastUnitsList.forecastPeriod}<br/>
	 		<c:set var="eventCalendarMapKey"  value="${overrideForecastUnitsList.forecastPeriod}${model.business}"/>
	 		<c:set var="eventCalendarToolTipKey"  value="${overrideForecastUnitsList.forecastPeriod}${model.business}1"/>
	 		<font size="1px" Style="color: #fff;"><span title="${eventCalendarMapObj[eventCalendarToolTipKey]}">${eventCalendarMapObj[eventCalendarMapKey]}</span></font>
	 	</td>
</c:forEach>
</tr>
<tr class="info">
<c:forEach var="overrideForecastUnitsList" items="${model.overrideForecastUnitsList}" varStatus="loop">
	 	<td style="text-align: center;   background-color: #dff0d8;"><form:input class="validate" id="overrideForecastUnits${loop.index}" path="overrideForecastUnitsList[${loop.index}].forecastValue"   style="width: 64%;text-align: center;  color: black;"/></td>
</c:forEach>
</tr>
</tbody>
</table>
</div>

<%} %>
</div>
<div style="float:left;  height:30px; margin-top:3px;  width: 100%;">
	<div style="float:left;text-align: center;  margin-top: 0.7%;"><font color="black" style="text-align: center;  vertical-align: -webkit-baseline-middle;"><b>Comments:</b></font></div>
	<form:textarea path="overrideForecastUnitsComment" style="color:black;width: 56%;"/> 
</div> 
</div>

<div class="modal-footer">
<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
<button type="button" id="unitsOverrideButton" onclick="saveForecastUnitsOverride();" class="btn btn-primary">Override</button>
</div>
</div>
</div>
</div>
</c:if>

<c:if test="${pageTemplatesChartsObj.tableName=='ForecastingASP'}">												
<div id="editForecastAsp" class="modal fade" >
<div class="modal-dialog" style="width: 1000px;height:700px;margin-right:65px;margin-top:50px;overflow:auto; ">
    <div class="modal-content">
        <div class="modal-header">
           <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h4 class="modal-title" style="background: 6868" ><font color="black" >Edit Forecast ASP:</font></h4>
</div>
<div class="modal-body" style="height:450px;width:100%;">
<!-- <font color="black"><b>BaseForecast:</b></font>
 --><div style="overflow-x: auto;  width: 100%;">
 <font color="black"><b>BaseForecast:</b></font>
<div class="table-responsive" id="table1"> 
<table class="table">
<tbody>
<tr class="success">
<c:forEach var="forecastAspObj" items="${forecastingListMap['baseForecastAspList']}">
	 	<td width="100px" style="  text-align: center; background-color: #2DAFCB;">${forecastAspObj.forecastPeriod}</td>
</c:forEach>
</tr>
<tr class="info">
<c:forEach var="forecastAspObj" items="${forecastingListMap['baseForecastAspList']}">
	 	<td width="100px" style="  text-align: center;  background-color: #dff0d8;  color: black;"><fmt:formatNumber type="number" maxFractionDigits="2" value="${forecastAspObj.forecastValue}"/></td>
</c:forEach>
</tr>
</tbody>
</table>
</div>
<% if(!rolesList.contains(ApplicationConstants.PRODUCT_MANAGER)){%> 
<font color="black"><b>Current Forecast:</b></font>
<div class="table-responsive" id="table2"> 
<table class="table">
<tbody>
<tr class="success">
<c:forEach var="forecastAspObj" items="${forecastingListMap['currentForecastAspList']}">
	 	<td width="100px" style="  text-align: center; background-color: #2DAFCB;">${forecastAspObj.forecastPeriod}</td>
</c:forEach>
</tr>
<tr class="info">
<c:forEach var="forecastAspObj" items="${forecastingListMap['currentForecastAspList']}">
	 	<td width="100px" style="  text-align: center;  background-color: #dff0d8;  color: black;"><fmt:formatNumber type="number" maxFractionDigits="2" value="${forecastAspObj.forecastValue}"/></td>
</c:forEach>
</tr>
</tbody>
</table>
</div>
<c:set var="eventCalendarMapObj"  value="<%=eventCalendarMapObj%>"/>
<font color="black"><b>Override Forecast:</b><div id="errMsg" style="color: red;text-decoration: none" class="float_center"></div></font>
<div class="table-responsive" id="table3"> 
<table class="table">
<tbody>
<tr class="success">
<c:forEach var="overrideForecastAspList" items="${model.overrideForecastASPList}">
	 	<td width="100px" style="text-align: center; background-color: #2DAFCB;">${overrideForecastAspList.forecastPeriod}<br/>
	 	<c:set var="eventCalendarMapKey"  value="${overrideForecastAspList.forecastPeriod}${model.business}"/>
	 	<c:set var="eventCalendarToolTipKey"  value="${overrideForecastAspList.forecastPeriod}${model.business}1"/>
	 		<font size="1px" Style="color: #fff;"><span title="${eventCalendarMapObj[eventCalendarToolTipKey]}">${eventCalendarMapObj[eventCalendarMapKey]}</span></font>
	 	</td>
</c:forEach>
</tr>
<tr class="info">
<c:forEach var="overrideForecastASPList" items="${model.overrideForecastASPList}" varStatus="loop">
	 	<td style="text-align: center;  background-color: #dff0d8; "><form:input id="overrideForecastASP${loop.index}" class="validate" path="overrideForecastASPList[${loop.index}].forecastValue"   style="width: 64%;text-align: center;  color: black;"/></td>
</c:forEach>
</tr>
</tbody>
</table>
</div>
<%} else { %>
<font color="black"><b>Current Forecast:</b></font>
<div class="table-responsive" id="table2"> 
<table class="table">
<tbody>
<tr class="success">
<c:forEach var="forecastAspObj" items="${forecastingListMap['baseForecastAspList']}">
	 	<td width="100px" style="  text-align: center; background-color: #2DAFCB;">${forecastAspObj.forecastPeriod}</td>
</c:forEach>
</tr>
<tr class="info">
<c:forEach var="forecastAspObj" items="${forecastingListMap['baseForecastAspList']}">
	 	<td width="100px" style="  text-align: center;  background-color: #dff0d8;  color: black;"><fmt:formatNumber type="number" maxFractionDigits="2" value="${forecastAspObj.forecastValue}"/></td>
</c:forEach>
</tr>
</tbody>
</table>
</div>
<c:set var="eventCalendarMapObj"  value="<%=eventCalendarMapObj%>"/>
<font color="black"><b>Override Forecast:</b><div id="errMsg" style="color: red;text-decoration: none" class="float_center"></div></font>
<div class="table-responsive" id="table3"> 
<table class="table">
<tbody>
<tr class="success">
<c:forEach var="overrideForecastAspList" items="${model.overrideForecastASPList}">
	 	<td width="100px" style="text-align: center; background-color: #2DAFCB;">${overrideForecastAspList.forecastPeriod}<br/>
	 	<c:set var="eventCalendarMapKey"  value="${overrideForecastAspList.forecastPeriod}${model.business}"/>
	 	<c:set var="eventCalendarToolTipKey"  value="${overrideForecastAspList.forecastPeriod}${model.business}1"/>
	 		<font size="1px" Style="color: #fff;"><span title="${eventCalendarMapObj[eventCalendarToolTipKey]}">${eventCalendarMapObj[eventCalendarMapKey]}</span></font>
	 	</td>
</c:forEach>
</tr>
<tr class="info">
<c:forEach var="overrideForecastASPList" items="${model.overrideForecastASPList}" varStatus="loop">
	 	<td style="text-align: center;    background-color: #dff0d8;"><form:input class="validate"  id="overrideForecastASP${loop.index}" path="overrideForecastASPList[${loop.index}].forecastValue"   style="width: 64%;text-align: center;  color: black;" required="true"/></td>
</c:forEach>
</tr>
</tbody>
</table>
</div>
<%} %>
</div>
<div style="float:left;  height:30px; margin-top:3px;  width: 100%;">
	<div style="float:left;text-align: center;  margin-top: 0.7%;"><font color="black" style="text-align: center;  vertical-align: -webkit-baseline-middle;"><b>Comments:</b></font></div>
	<form:textarea path="overrideForecastASPComment" style="color:black;width: 56%;"/> 
</div> 
</div>

<div class="modal-footer">
<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
<button type="button" id="aspOverrideButton" onclick="saveForecastAspOverride();" class="btn btn-primary">Override</button>
</div>
</div>
</div>
</div>
</c:if>												

												
											</div>
											<div class="panel-body">
												<c:if test="${model.jsonStrList != null && model.jsonStrList != 'null' && model.jsonStrList != ''}">
													<c:forEach var="jsonStrList" items="${model.jsonStrList}" varStatus="innerloop">
															<c:if test="${innerloop.index==outerloop.index}">
															<div id="chart${innerloop.index}"></div>
																<c:if test="${outerloop.index==0 || outerloop.index==1}">
																		<script>
																		function lineChart(){
																			Morris.Line({
																						  element: 'chart${innerloop.index}',
																						  data: ${jsonStrList}, 
																						  xkey: 'w',
																						  ykeys: ['a', 'c','d','b'],
																						  labels: ['Actuals ', 'Base Forecast','Current Forecast','Override Forecast'],
																						  fillOpacity: 0.6,
																						  hideHover: 'auto',
																						  behaveLikeLine: true,
																						  resize: true,
																						  pointFillColors:['#ffffff'],
																						  pointStrokeColors: ['black'],
																						  lineColors:['gray','#2DAFCB','Green','Orange'],
																						  parseTime: false,
																						 
																						  xLabelAngle: 80
																			  
																					});
																				
																			}
																				
																			
																		lineChart();		
																		
																		</script>
																</c:if>
																<c:if test="${outerloop.index !=0 && outerloop.index!=1}">
																		<script>
																		function lineChart(){
																			Morris.Line({
																						  element: 'chart${innerloop.index}',
																						  data: ${jsonStrList}, 
																						  xkey: 'w',
																						  ykeys: ['a', 'b'],
																						  labels: ['Actuals ', 'Forecast'],
																						  fillOpacity: 0.6,
																						  hideHover: 'auto',
																						  behaveLikeLine: true,
																						  resize: true,
																						  pointFillColors:['#ffffff'],
																						  pointStrokeColors: ['black'],
																						  lineColors:['gray','#2DAFCB'],
																						  parseTime: false,
																						 
																						  xLabelAngle: 80
																			  
																					});
																				
																			}
																				
																			
																		lineChart();		
																		
																		</script>
																</c:if>
																	
															</c:if>
															
													</c:forEach> 
													<div class="table-responsive" id="tab${outerloop.index}" style="display:none;">
													<table class="table table-striped">
														<thead>
															<tr>
															<th>Period</th>
															<c:if test="${outerloop.index==0 || outerloop.index==1}">
																<th>Actuals</th>
																<th>Base Forecast</th>
																<th>Current Forecast</th>
																<th>Override Forecast</th>
															</c:if>
															<c:if test="${outerloop.index !=0 && outerloop.index!=1}">
																<th>Actuals</th>
																<th>Forecast</th>
															</c:if>
															</tr>
														</thead>
														<tbody>
															<c:forEach var="jsonTableList" items="${model.jsonTableList}" varStatus="innerloop1">
																<c:if test="${innerloop1.index==outerloop.index}">
																	
																		<c:forEach var="tableList" items="${jsonTableList}" varStatus="innerloop2">
																			<tr>
																			<c:forEach var="actualTableList" items="${tableList}" varStatus="innerloop3">
																				<td>${actualTableList}</td>
																			</c:forEach>
																			</tr>
																		</c:forEach>
																</c:if>
															</c:forEach>
														</tbody>
													</table>
													</div>
													<c:if test="${outerloop.index==0 || outerloop.index==1}">
														<div id="legends${outerloop.index}">
														 	<div id="legend1" class="legend"></div><div id="legendName1">Actuals</div>
														 	<div id="legend2" class="legend"></div><div id="legendName2">Base Forecast</div>
														 	<div id="legend3" class="legend"></div><div id="legendName3">Current Forecast</div>
														 	<div id="legend4" class="legend"></div><div id="legendName4">Override Forecast</div>
														</div>
													</c:if>
													<c:if test="${outerloop.index !=0 && outerloop.index!=1}">
														<div id="legends${outerloop.index}">
														 	<div id="legend1" class="legend"></div><div id="legendName1">Actuals</div>
														 	<div id="legend2" class="legend"></div><div id="legendName2">Forecast</div>
														</div>
													</c:if>
												 </c:if> 
												 
											</div>
												
										</div>
		                        	
		                        	</c:forEach>
							
							</div>
								
								
						</div>
                    </div>
                </div> 
                 <!-- /. ROW  -->
				</div>
             <!-- /. PAGE INNER  -->
            </div>
         <!-- /. PAGE WRAPPER  -->
        </div>
     <!-- /. WRAPPER  -->

</form:form>
<script type="text/javascript">
$(document).ready(function () {
	     var elementId;
	 $(".validate").focusin(function (e) {
   	      elementId =$(this).attr("id");
    	  
   	      if($('#'+elementId).val() == '0'){
    		  $('#'+elementId).val("");
    	  }
	      
   	      $(".validate").keypress(function (e) {
   	    	if(elementId.indexOf("overrideForecastUnits") != -1){
   	    		if (e.which != 8 && e.which != 0 && e.which == 46 &&(e.which < 48 || e.which > 57)) {
   	    			 $("#errMsg").html("Only numbers are allowed").show().fadeOut(3000);
	               return false;
   		   		 }else{
	   		   		if (e.which != 8 && e.which != 0 && e.which != 46 &&(e.which < 48 || e.which > 57)) {
		   		   		 $("#errMsg").html("Only numbers are allowed").show().fadeOut(3000);
			              return false;
	   		   		 }
   		   		 }
   		}
	    	
	     });
	 });
	  
	  $(".validate").focusout(function (e) {
    	    if ($('#'+elementId).val().length == 0) {
    	    	$('#'+elementId).val('0');
    	    }
	   });
	  
	  });
</script>
    <!-- JS Scripts-->
    <!-- jQuery Js -->
     <!-- jQuery Js -->
    <script src="js/jquery-1.10.2.js"></script>
    <!-- Bootstrap Js -->
    <script src="js/bootstrap.min.js"></script>
	 
    <!-- Metis Menu Js -->
    <script src="js/jquery.metisMenu.js"></script>
    <!-- Custom Js -->
    <script src="js/custom-scripts.js"></script>
    
   

