<!DOCTYPE html>
<%@page import="java.io.Console"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false"%>
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
<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>SKU Planning Log</title>

      
      <link href="css/bootstrap.css" rel="stylesheet" />
    <link href="css/font-awesome.css" rel="stylesheet" />
	<link href="css/custom-styles.css" rel="stylesheet" />
	<script src="js/tabcontent.js" type="text/javascript"></script>
	<link href="css/tabcontent.css" rel="stylesheet" type="text/css" />
	<script src="js/jquery-1.10.2.js"></script>
     <script src="js/jquery.metisMenu.js"></script>
        <!-- Morris Chart Js -->
     <script src="js/plugins/morris/raphael-2.1.0.min.js"></script>
    <script src="js/plugins/morris/morris.js"></script>
    <link href="/css/plugins/dataTables.bootstrap.css" rel="stylesheet">
    <script src="js/plugins/dataTables/jquery.dataTables.js"></script>
    <script src="js/plugins/dataTables/dataTables.bootstrap.js"></script>
    <jsp:useBean id="planningLogObj" class="com.bridgei2i.form.PlanningLogBean"/>  
<%
	String  selectedSkuList = (String)ApplicationUtil.getObjectFromSession(ApplicationConstants.PLANNING_LOG_SESSION_OBJECT, request);
	int selectedSkuArrayLength=0,loopCount=0;
	if(ApplicationUtil.isEmptyOrNull(selectedSkuList)){
		selectedSkuList=" ";
	}else{
		String [] selectedSkuArray=selectedSkuList.split(",");
		//selectedSkuList+=" ";
		selectedSkuArrayLength=selectedSkuArray.length;
	}
	
	
%>

<style>
.margin {
	margin: 30px;
}

td>a {
	color: #369AB2;
}

.notif{background-color: blue;z-index: 3;display: block;width: 19px;border-radius: 100px;height: 20px;margin-top: 1;color:balck; }
.icon {
    width:30px; 
    height:30px;
    position: relative;
}
.txt{
    background:#EO3D3D; 
    font-size:13px;
    position: absolute;
    top: -5px;
    right: -8px;
    width:18px;
    border-radius:27px;
    height:17px;
    padding-left:4px;
    padding-top:0px
}
</style>

<script type="text/javascript">

function eolPopUp(id,productNumber,productDescription,business,phI,phII,phIV){
	document.getElementById("id").value =id;
	document.getElementById("ProductNumber").innerHTML =productNumber;
	document.getElementById("ProductDescription").innerHTML =productDescription;
	document.getElementById("Bussiness").innerHTML =business;
	document.getElementById("PhI").innerHTML =phI;
	document.getElementById("PhII").innerHTML =phII;
	document.getElementById("PhIV").innerHTML =phIV;
	document.getElementById("ProductNumberValue").value =id;
}

function updateEOL(){
	planningLogBeanForm.action = "updateEolData.htm";
	planningLogBeanForm.submit();
}

function submitSelectedSku(){
	removeProgress();
	loadProgress();
	planningLogBeanForm.action = "planningLogSelectedSku.htm";
	planningLogBeanForm.submit();
}

function getForecastValues(metricType){
	loadProgress();
	document.getElementById("metric").value=metricType;
	planningLogBeanForm.action= "forecastValuesFromPLanningLog.htm";
	planningLogBeanForm.submit(); 
}

function getPlanningLogDetails(){
	removeProgress();
	loadProgress();
	planningLogBeanForm.action = "planningLogDetails.htm";
	planningLogBeanForm.submit();
}

function setForecastMetric(metricType){
	removeProgress();
	loadProgress();
	document.getElementById("metric").value=metricType;
	/* alert(document.getElementById("metric").value); */
	planningLogBeanForm.action= "forecastValuesFromPLanningLog.htm";
	planningLogBeanForm.submit(); 
}

function loadPreSelectedCheckboxes(checkboxId){
	 document.getElementById(checkboxId).checked = true;
	 $("#"+checkboxId).closest('tr').css('background-color', '#E48B84');	
}

</script>
	<form:form method="post" action="planningLog.htm" commandName="model" name="planningLogBeanForm" enctype="multipart/form-data">
		<%-- <form:hidden id="planningCycleId" path="planningCycleId"/> --%>
		<!-- <input id="type" name="type" type="hidden" />
		<input id="selectedTypeValue" name="selectedTypeValue" type="hidden" />
		<input id="planogram" name="planogram" type="hidden" />
		<input id="productDescription" name="productDescription" type="hidden" />
		<input id="productHierarchyII" name="productHierarchyII" type="hidden" /> -->
 
		<div id="wrapper">
			<div id="page-wrapper" style="  min-height: 500px; height: 590px;  padding: 15px 16px;">
				<div id="page-inner" style="min-height: 500px;">
					<div class="row" style="margin-top: -1%">
						<div class="col-md-12">
							<div class="panel panel-default"
								style="height: auto; width: 103%; margin-left: -1.2%">
								<div class="panel-heading"
									style="background-color: #2DAFCB; font-size: 130%; height: 52px; padding: 7px 15px; color: #fff; padding-top: 1.5%">
									&nbsp;<strong><b>SKU List - Forecasted Values</b></strong>
									<div class="pull-right" style="text-align: right;margin-top:-0.5%;margin-right:2%">
									<!-- Total: <input name="totalSelected" id="totalSelected" readonly="readonly" size="5" type="text" /> -->
										<div class="row">
											<div class="col-sm-2">
												<div id="notifySelectedSku" class="icon"><i id="notifySelectedSkuIcon" class="glyphicon glyphicon-filter" style="width:20px;margin-top:4px" aria-hidden="true"></i><input style="background-color:#EA1F1F;color:#fff" class="txt" id="selectedSkuCount" value="0" type="text" ></input></div>
											</div>
											<div class="col-sm-2">
											<button type="button" style="background:none;border:none;outline:none;width:40px;margin-right:5%" onClick="getPlanningLogDetails()"><img src="./images/back2.png" title="Go Back" style="width: 60px;height: 60px;
			  										vertical-align: -webkit-baseline-middle; margin-top: -18px;display: -webkit-inline-box;outline:none;margin-left: 10px;outline: none;" /></button>  
											</div>
										</div>
										<div class="row" id ="hiddenSkuList" style="display:none;">
											<div style="position: absolute;display: block;background-color: #fff;border:1px solid;margin-top: -3px;font-size: 13px;z-index: 234543564787596;color:#000;border-color:#5A5A5A;font-weight:100" id="selectedSkuListDiv">
												<input type="button" name="dismiss" value="dismiss"  style="background-color:#2DAFCB;color:white;margin-left:30%;margin-right:1%;margin-top:0.2%" id="dismiss"></input>	
											</div>
											
										</div>
									</div>
								</div>
								<div class="panel-body" style="height:482px;width: 100%">
									<div id ="tableScroll" style="height: 430px;">
										<div class=" btn-group pull-left" style="margin-right:3%">
													<button type="button" id="toggle-btn-grp"
														class="btn btn-default btn-xs dropdown-toggle"
														data-toggle="dropdown" style="height: 25px">
														Forecast Metric <span class="caret"></span>
													</button>
													<ul class="dropdown-menu pull-right" role="menu" id="toggle-menu" style="min-width: 16px;">
														<li><a onClick="setForecastMetric(0);" style="cursor:pointer;" >Units</a></li>
														<li><a onClick="setForecastMetric(1);" style="cursor:pointer;" >Asp</a></li>
													</ul>
												</div>
										<div style="margin-left:74%;margin-bottom:1%">
										<!-- 													
											<select id="forecast_Metric" onChange=>
														<option onClick="setForecastMetric(0);">Units</option>
														<option onClick="setForecastMetric(1);">Asp</option>
											</select> -->
											  <input type="button" style="background-color:#2DAFCB;color:white;margin-left:30%;margin-right:1%;margin-top:0.2%" id="submitSelection" value="Submit"/>
												<input type="button" style="background-color:#2DAFCB;color:white;margin-top:0.2%" id="clearSelection" value="Clear Selection" onClick="getForecastValues(${model.forecastMetric})"/>
										</div>
									 <div style="height: 430px;overflow: auto">
										<table class="table table-striped table-bordered table-hover table-responsive" style="overflow-x:auto;width:480px" id="dataTables-example" name="dataTable">
										
											<thead>
												<tr>
													<th valign="middle"> </th>
												    <th style="min-width:140px" >Product Description</th>
													<th width="28%" valign="middle">Category</th>
													<th valign="middle">Business</th>
													<c:forEach var="forecastValues" items="${model.forecastPeriodList}" varStatus="loop">
															<th style="min-width:70px;vertical-align:middle">${forecastValues}</th>
													</c:forEach>
													
												</tr>
											</thead>
											<tbody>
													
													<c:forEach var="modelObject" items="${model.skuForecastList}" varStatus="loop">
													 <tr id="modelObject${loop.index}">
													 	<td class="headcol" style="vertical-align:middle">
																<input type="checkbox"  class="myClass" style="vertical-align:middle" value="${loop.index}" id="modelObjectCheckBox${loop.index}" />
														</td>
														<c:forEach var="modelObjectEntries" items="${modelObject}" varStatus="innerloop">
															<c:if test="${innerloop.index == 0 }">
																<td class="headcol" value="${modelObjectEntries}" style="vertical-align:middle" id="${modelObjectEntries}">
																	<label title="${modelObjectEntries}" value="innerloop">${modelObjectEntries}</label>
															</c:if>
															<c:if test="${innerloop.index == 1 }">
																 <%if(!ApplicationUtil.isEmptyOrNull(selectedSkuList)) {
																	   String skuId=	pageContext.getAttribute("modelObjectEntries").toString();
																	 
																		 if(selectedSkuList.contains(skuId)){ 
																			  System.out.println("selectedSkuList"+selectedSkuList);
																			   System.out.println("SKU Id"+skuId);
																		%>
																		<script type="text/javaScript">
																			 var checkboxId="modelObjectCheckBox"+<%=loopCount%>;
																			 loadPreSelectedCheckboxes(checkboxId);
																		</script>
																		<%	
																		 } 
																}
																loopCount++;
																%> 
																
																	<label style="font-weight:100" title="${modelObjectEntries}" id="skuId"  value="${modelObjectEntries}">${modelObjectEntries}</label>
															    </td> 
															</c:if>
															
															<c:if test="${innerloop.index == 2 || innerloop.index == 3}">
																<td class="headcol" value="${modelObjectEntries}" style="vertical-align:middle" id="${modelObjectEntries}">
																	${modelObjectEntries}
															    </td> 
															</c:if>
															<c:if test="${innerloop.index == 4 }">
																	<c:forEach var="forecastValues" items="${modelObjectEntries}" varStatus="innerloop2">
																		<td style="vertical-align:middle">
																			${forecastValues}
																		</td>
																	</c:forEach>
															</c:if>
														</c:forEach>
													</tr> 
													</c:forEach>
												
											</tbody>
										</table>
										</div>
										<input id="id" name="dataId" type="hidden" /> 
										<input id="metric" name="forecastMetric" type="hidden" /> 
										<input id="isFromPlanningLog" name="isFromPlanningLog"  type="hidden" /> 
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<input id="selectedSkuList" name="selectedSkuList" type="hidden" />
	</form:form>
<script type="text/javascript">
$( document ).ready(function() {
	
	  $("#selectedSkuCount").prop('value',<%=selectedSkuArrayLength%>);
	  if(<%=selectedSkuArrayLength%><1){
		  $("#submitSelection").prop('disabled',true);
	  }else{
		  $("#submitSelection").prop('disabled',false);
	  }
});
var skuList="<%=selectedSkuList%>";
var count=1,selectedCount=0;
$('#dataTables-example tr td input[type="checkbox"]').click(function () {
	     if ($(this).prop('checked')==true){ 
	    	 $(this).closest('tr').css('background-color', '#E48B84');	
				skuList+=',';
	    	 	skuDescription=$(this).closest("td").next().attr("value");
	    		$(".selected").remove(":contains("+skuDescription+")");
	    	 	$("#selectedSkuListDiv").append("<div class='selected'>"+count+".&nbsp;"+skuDescription+"</div>");
	    	 	
				skuList+="'"+$(this).closest("td").next().find("#skuId").text()+"'";
				count++;
				selectedCount++;
				//skuList = skuList.replace(" ",",");
	     }else{
	    	$(this).closest('tr').css('background-color', '');
	    	if(skuList .search("'"+$(this).closest("td").next().find("#skuId").text()+"'")){
	    		skuList=skuList.replace("'"+$(this).closest("td").next().find("#skuId").text()+"'", " ");
	    		/* skuList = skuList.replace(/[,]+/g," ").trim();
	    		skuList = skuList.replace(/[" "]+/g,","); */
	    		skuDescription=$(this).closest("td").next().attr("value");
	    		if(count==1){
	    			$(".selected").remove();
	    		}else{
	    			$(".selected").remove(":contains("+skuDescription+")");
	    		}
	    		
	    	}	    	
	    	selectedCount--;
	    	count--;
	    }
	     var selectedSkuCount=<%=selectedSkuArrayLength%>;
	     selectedSkuCount+=selectedCount;
	     if(selectedSkuCount<1){
	    	 $("#submitSelection").prop('disabled',true);
	     }else{
	    	 $("#submitSelection").prop('disabled',false);
	     }
	     $("#selectedSkuCount").prop('value',selectedSkuCount);
	     $("#selectedSku").prop('value',skuList);
	});
	
	
	$('#submitSelection').click(function () {
		$("#selectedSkuList").prop('value',skuList);
		submitSelectedSku();
	});
	
<%-- 	$('#notifySelectedSku').hover(function () {
		var selections="<%=selectedSkuList.toString()%>";
		selections+=skuList;
		alert(selections);
		$('#hiddenSkuList').css("display","block")
	});
	
	$('#dismiss').click(function () {
		var selections="<%=selectedSkuList.toString()%>";
		selections+=skuList;
		alert(selections);
		$('#hiddenSkuList').css("display","none");
	}); --%>
	
</script>


