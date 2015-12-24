<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
 <link href="css/custom-styles.css" rel="stylesheet" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Accuracy Report</title>
 <link href="css/custom-styles.css" rel="stylesheet" />
     <!-- Morris Chart Styles-->
    <link href="js/plugins/morris/morris-0.4.3.min.css" rel="stylesheet" />
    <!-- Core Scripts - Include with every page -->
     <script src="js/jquery-1.11.0.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/plugins/metisMenu/jquery.metisMenu.js"></script>
      <script src="js/jquery-1.10.2.js"></script>
      <script src="js/jquery.metisMenu.js"></script>
</head>
<%
	Map pageTemplateCacheObj = (Map) LoadApplicationCacheService.applicationCacheObject
	.get(ApplicationConstants.PAGE_TEMPLATES_CACHE_KEY);	
	PageTemplate pageTemplate = (PageTemplate)pageTemplateCacheObj.get("PC_DASHBOARD");

	Map accuracyListMap = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.ACCURACY_LIST_MAP, request);
	if(accuracyListMap == null){
		accuracyListMap = new HashMap();
	}
	
%>
<style>
.legend{
			height: 20px;
		  	width: 1.5%;
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
	      color: black;
	      font-size: small;
		  padding-left: 0.5%;
		}
		#legendName1{
		 float: left;
		  width: 7%;
		  padding-left: 0.5%;
		}
		/* svg{
		  width: 948px;
		} */
</style>
<script>
function setReportType(reportType){
	loadProgress();
	document.getElementById("reportTypeId").value=reportType;
	navigationForm.action="accuracyReportOnFilterChange.htm";
	navigationForm.submit();
	
	
}

function onBackButton(){
	reportBeanForm.action="homePage.htm";
	reportBeanForm.submit();
	
}

function getTableView(index,reportType){
	if(reportType==1){
		var tableObj = document.getElementById("tab"+index);
		tableObj.style.display = "";
		var chartObj = document.getElementById("chart"+index);
		chartObj.style.display = "none";
		var legendObj = document.getElementById("legends1");
		legendObj.style.display = "none";
		/* chartObj = document.getElementById("accuracyReportLegend"+index);
		chartObj.style.display = "none"; */
	}else{
		var tableObj = document.getElementById("tab2"+index);
		tableObj.style.display = "";
		var chartObj = document.getElementById("chart2"+index);
		chartObj.style.display = "none";
		var legendObj = document.getElementById("legends2");
		legendObj.style.display = "none";
		/* chartObj = document.getElementById("historicalReportLegend"+index);
		chartObj.style.display = "none";
 */	}
	
}

function getChartView(index,reportType){
	
	if(reportType==1){
		var tableObj = document.getElementById("tab"+index);
		tableObj.style.display = "none";
		var chartObj = document.getElementById("chart"+index);
		chartObj.style.display = "";
		var legendObj = document.getElementById("legends1");
		legendObj.style.display = "";
		/* tableObj = document.getElementById("accuracyReportLegend"+index);
		tableObj.style.display = ""; */
	}
	else{
		var tableObj = document.getElementById("tab2"+index);
		tableObj.style.display = "none";
		var chartObj = document.getElementById("chart2"+index);
		chartObj.style.display = "";
		var legendObj = document.getElementById("legends2");
		legendObj.style.display = "";
		/* tableObj = document.getElementById("historicalReportLegend"+index);
		tableObj.style.display = ""; */
	}
	
}
$(document).ready(function () {
	    function exportTableToCSV($table, filename) {
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
	    	exportTableToCSV.apply(this, [$(address), 'exportAccuracyReport.csv']);
	        
	    }); 
	    
	     $(".exportHistorical").on('click', function (event) {
	    	var inputID = $(this).attr('id');
	    	var address='#tab2'+inputID+'>table';
	    	exportTableToCSV.apply(this, [$(address), 'exportHistoricalReport.csv']);
	        
	    }); 
	    	});

</script>

<body>
<form:form method="post" action="accuracyReport.htm" commandName="model" name="reportBeanForm" >
<div id="wrapper" style="overflow-x:hidden;overflow-y:hidden">
     <div id="page-wrapper" style="  padding: 15px 16px;min-height:200px">
       <div id="page-inner" style="margin-top:-1.6%;min-height: 550px;">
       		<div class="panel panel-default" style="height:520px;width:103%;margin-left:-1.2%">
	       		<div  class="panel-heading" style="background-color:#2DAFCB;font-size:130%;height:52px;padding:7px 15px;color:#fff;padding-top:1.5%  ">
					<c:if test="${model.reportType == 1 }">
						&nbsp;<label id="accuracy-report">Accuracy Report</label>
					</c:if>
					<c:if test="${model.reportType == 2 }">
						&nbsp;<label id="historical-report">Historical Report</label>
					</c:if>
						  <label id="historical-report" style="display:none">Historical Report</label>
						<div class="pull-right btn-group">
													<button type="button" id="toggle-btn-grp"
														class="btn btn-default btn-xs dropdown-toggle"
														data-toggle="dropdown" style="height: 25px">
														Report Type <span class="caret"></span>
													</button>
													<ul class="dropdown-menu pull-right" role="menu" id="toggle-menu" style="min-width: 16px;">
														<li><a onClick="setReportType(1);" style="cursor:pointer;" >Accuracy Report</a></li>
														<li><a onClick="setReportType(2);" style="cursor:pointer;" >Historical Report</a></li>
													</ul>
												</div>
					<div class="row" style="margin-top:1.5%">
						<div class="col-md-12" style="height:25px">
							<div class="col-md-4">
							
							</div>
							<div class="col-md-8">
								<c:if test="${model.reportType == 1}">
									<div id="legends1" style="width:230%;margin-bottom:2%;margin-left:35%">
								 		<div id="legend2"  class="legend"></div><div id="legendName2" >Base Forecast Error Rate</div>&nbsp;
								 		<div id="legend4"  class="legend" ></div><div id="legendName4">Frozen Forecast Error Rate</div>
									</div>
								</c:if>
								<c:if test="${model.reportType == 2}">
									<div id="legends2" style="width:200%;margin-bottom:2%;margin-left:48%">
								 		<div id="legend2"  class="legend"></div><div id="legendName2">Historical Actuals</div>&nbsp;
								 		<div id="legend4"  class="legend" ></div><div id="legendName4">Historical Forecast</div>
									</div>
								</c:if>
									
							</div>
						</div>
					</div>
				</div>
				<div class="panel-body" style="width:100%;height:455px;overflow-y:auto">
				<c:set var="pageTemplateObj"  value="<%=pageTemplate%>"/>
					<c:if test="${model.reportType == 1}">
									<c:if test="${model.jsonStrList != null && model.jsonStrList != '[]' && model.jsonStrList != 'null' && model.jsonStrList != ''}">
										<c:forEach var="jsonStrList" items="${model.jsonStrList}" varStatus="outerloop">
										 
											<div class="panel panel-default" style="height:430px;" id="inner-row${outerloop.index}">
												<div class="panel-heading" id="panel-head${outerloop.index}" style="background-color:#C1C1C8;font-size:100%;height:40px;padding:7px 15px;color:#fff  ">
												<c:if test="${outerloop.index == 0}">
															&nbsp;Actuals vs Forecasted Units
												</c:if> 
												<c:if test="${outerloop.index == 1}">
															&nbsp;Actuals vs Forecasted ASP 
												</c:if>
												<c:if test="${outerloop.index == 2}">
															&nbsp;Actuals vs Forecasted Revenue 
												</c:if>
												<c:if test="${outerloop.index == 3}">
															&nbsp;Actuals vs Forecasted ESC 
												</c:if>
												<c:if test="${outerloop.index == 4}">
															&nbsp;Actuals vs Forecasted PM Percentage 
												</c:if>
										
															<div class="pull-right btn-group">
																<button type="button" id="btn-grp${outerloop.index}"
																	class="btn btn-default btn-xs dropdown-toggle"
																	data-toggle="dropdown" style="height: 25px">
																	Actions <span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right" role="menu" id="menu${outerloop.index}" style="min-width: 16px;">
																	<li><a id='${outerloop.index}' href="#" class="export"><img src="./images/excel_icon_unselected.png" title="Export to excel" width="20" height="20"/>    
																	</a></li>
																	<li><a onClick="getTableView('${outerloop.index}',1);" style="cursor:pointer;" ><i class="fa fa-table" style="font-size: 22px" title="Table View"></i></a></li>
																	<li><a onClick="getChartView('${outerloop.index}',1);" style="cursor:pointer;" ><i class="fa fa-bar-chart-o" style="font-size: 19px" title="Chart View"></i></a></li>
																</ul>
															</div>
												</div>
											<!-- -------------CHART VIEW------------- -->
												<div class="panel-body" >	
														<div id="chart${outerloop.index}" ></div>
														<c:if test="${outerloop.index < 2 }">
															<script>
																function lineChart(){
																	Morris.Line({
																				  element: 'chart${outerloop.index}',
																				  data: ${jsonStrList}, 
																				  xkey: 'w',
																				  ykeys: ['a', 'b'],
																				  labels: ['Frozen Forecast Error Rate','Base Forecast Error Rate'],
																				  fillOpacity: 0.6,
																				  hideHover: 'auto',
																				  behaveLikeLine: true,
																				  resize: true,
																				  pointFillColors:['#ffffff'],
																				  pointStrokeColors: ['black'],
																				  lineColors:['orange','#2DAFCB'],
																				  parseTime: false,
																				 
																				  xLabelAngle: 80
																	  
																			});
																		
																	}
																		
																	
																lineChart();		
																
															</script>
														</c:if>
														<c:if test="${outerloop.index >= 2 }">
															<script>
																function lineChart(){
																	Morris.Line({
																				  element: 'chart${outerloop.index}',
																				  data: ${jsonStrList}, 
																				  xkey: 'w',
																				  ykeys: ['a'],
																				  labels: ['Error Rate'],
																				  fillOpacity: 0.6,
																				  hideHover: 'auto',
																				  behaveLikeLine: true,
																				  resize: true,
																				  pointFillColors:['#ffffff'],
																				  pointStrokeColors: ['black'],
																				  lineColors:['#2DAFCB'],
																				  parseTime: false,
																				  
																				  xLabelAngle: 80
																	  
																			});
																		
																	}
																		
																	
																lineChart();		
																
															</script>
														</c:if>
													<!-- </div> -->	
			
													<!-- --------Table View------- -->	
														<div class="table-responsive  table-bordered" id="tab${outerloop.index}" style="height:320px;overflow:auto;display:none;">
																<table class="table table-responsive table-striped ">
																	<thead>
																		<tr>
																			<th style="color:#2DAFCB">Period</th>
																			<th style="color:#2DAFCB">Actuals</th>
																			<th style="color:#2DAFCB">Forecasted Values</th>
																			<th style="color:#2DAFCB">Error Rate</th>
																		</tr>
																	</thead>
																	<tbody style="min-height:300px;max-height:300px;overflow:auto">
																		<c:forEach var="jsonTableList2" items="${model.jsonTableList}" varStatus="innerloop1">
																				<c:if test="${innerloop1.index==outerloop.index}">
																					<c:forEach var="tableList2" items="${jsonTableList2}" varStatus="innerloop2">
																						<tr>
																						<c:forEach var="actualTableList2" items="${tableList2}" varStatus="innerloop3">
																										<td>${actualTableList2}</td>
																						</c:forEach>
																						</tr>
																					</c:forEach>
																				</c:if>
																		</c:forEach>
																	</tbody>
																</table>
														</div> 
														<%-- <div id="accuracyReportLegend${outerloop.index}">
															
															 	
															 	<c:if test="${outerloop.index <2}">
																 	<div id="legends${outerloop.index}" style="margin-left: 40%;">
																 		<div id="legend2"  class="legend"></div><div id="legendName2">Base Forecast</div>&nbsp;
																 		<div id="legend4"  class="legend" ></div><div id="legendName4">Frozen Forecast</div>
																	</div>
																</c:if>
																<c:if test="${outerloop.index >1}">
																	<div id="legends${outerloop.index}" style="margin-left: 50%;">
																 		<div id="legend2"  class="legend" ></div><div id="legendName2">Base Forecast</div>&nbsp;
																	</div>
																</c:if>
															
														</div> --%>
													</div>	
											</div>
										</c:forEach>
								 </c:if> 
							</c:if>
							
							<!-- historical report -->
							<c:if test="${model.reportType != 1 }">
							
								<c:if test="${model.historicalJsonStrList != null && model.historicalJsonStrList != '[]' && model.historicalJsonStrList != 'null' && model.historicalJsonStrList != ''}">
										<c:forEach var="jsonStrList" items="${model.historicalJsonStrList}" varStatus="outerloop">
										 
											<div class="panel panel-default" style="height:430px;" id="inner-row${outerloop.index}">
												<div class="panel-heading" id="panel-head${outerloop.index}" style="background-color:#C1C1C8;font-size:100%;height:40px;padding:7px 15px;color:#fff  ">
												<c:if test="${outerloop.index == 0}">
															&nbsp;Actuals vs Forecasted Units
												</c:if> 
												<c:if test="${outerloop.index == 1}">
															&nbsp;Actuals vs Forecasted ASP 
												</c:if>
												<c:if test="${outerloop.index == 2}">
															&nbsp;Actuals vs Forecasted Revenue 
												</c:if>
												<c:if test="${outerloop.index == 3}">
															&nbsp;Actuals vs Forecasted ESC 
												</c:if>
												<c:if test="${outerloop.index == 4}">
															&nbsp;Actuals vs Forecasted PM Percentage 
												</c:if>
										
															<div class="pull-right btn-group">
																<button type="button" id="btn-grp${outerloop.index}"
																	class="btn btn-default btn-xs dropdown-toggle"
																	data-toggle="dropdown" style="height: 25px">
																	Actions <span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right" role="menu" id="menu${outerloop.index}" style="min-width: 16px;">
																	<li><a id='${outerloop.index}' href="#" class="exportHistorical"><img src="./images/excel_icon_unselected.png" title="Export to excel" width="20" height="20"/>    
															</a></li>
																	<li><a onClick="getTableView('${outerloop.index}',2);" style="cursor:pointer;" ><i class="fa fa-table" style="font-size: 22px" title="Table View"></i></a></li>
																	<li><a onClick="getChartView('${outerloop.index}',2);" style="cursor:pointer;" ><i class="fa fa-bar-chart-o" style="font-size: 19px" title="Chart View"></i></a></li>
																</ul>
															</div>
												</div>
											<!-- -------------CHART VIEW------------- -->
												<div class="panel-body">	
														<div id="chart2${outerloop.index}" ></div>
															<script>
																function lineChart(){
																	Morris.Line({
																				  element: 'chart2${outerloop.index}',
																				  data: ${jsonStrList}, 
																				  xkey: 'w',
																				  ykeys: ['a', 'b'],
																				  labels: ['Historical Actuals','Historical Forecast'],
																				  fillOpacity: 0.6,
																				  hideHover: 'auto',
																				  behaveLikeLine: true,
																				  resize: true,
																				  pointFillColors:['#ffffff'],
																				  pointStrokeColors: ['black'],
																				  lineColors:['#2DAFCB','orange'],
																				  parseTime: false,
																				 
																				  xLabelAngle: 80
																	  
																			});
																		
																	}
																		
																	
																lineChart();		
																
															</script>
													<!-- </div> -->	
			
													<!-- --------Table View------- -->	
														<div class="table-responsive  table-bordered" id="tab2${outerloop.index}" style="height:320px;overflow:auto;display:none;">
																<table class="table table-responsive table-striped " CELL-SPACING=10>
																	<thead>
																	<col width="500">
  																	<col width="400">
  																	<col width="300">
																		<tr>
																			<th style="color:#2DAFCB">Period</th>
																			<th style="color:#2DAFCB;margin-left:150px">Actuals</th>
																			<th style="color:#2DAFCB;margin-left:150px">Forecasted Values</th>
																		</tr>
																	</thead>
																	<tbody style="min-height:300px;max-height:300px;overflow:auto">
																		<c:forEach var="jsonTableList" items="${model.jsonTableList}" varStatus="innerloop1">
																				<c:if test="${innerloop1.index==outerloop.index}">
																					<c:forEach var="tableList" items="${jsonTableList}" varStatus="innerloop2">
																						<tr>
																						<c:forEach var="actualTableList" items="${tableList}" varStatus="innerloop3">
																								<c:if test="${innerloop3.index==0}">
																										<td>${actualTableList}</td>
																								</c:if>
																								<c:if test="${innerloop3.index>0}">
																										<td>${actualTableList}</td>
																								</c:if>
																						</c:forEach>
																						</tr>
																					</c:forEach>
																				</c:if>
																		</c:forEach>
																	</tbody>
																</table>
														</div> 
														<%-- <div id="historicalReportLegend${outerloop.index}">
															<div id="legends0" style="margin-left: 40%;">
															 	<div id="legend2"  class="legend"></div><div id="legendName2">Base Forecast</div>&nbsp;
															 	<div id="legend4"  class="legend" ></div><div id="legendName4">Frozen Forecast</div>
															</div>
														</div> --%>
													</div>	
											</div>
										</c:forEach>
								 </c:if> 
							
							
							</c:if>
							
							
				</div>
			</div>
       </div>
     </div>
 </div>
<form:hidden id="planningCycleId" path="planningCycleId"/>
<form:hidden id="selectedFiltersId" path="isFiltersSelected" value="${model.isFiltersSelected}"/>

</form:form>

</body>
</html>
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
