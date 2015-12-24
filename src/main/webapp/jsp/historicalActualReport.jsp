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
	PageTemplate pageTemplate = (PageTemplate)pageTemplateCacheObj.get("HISTORICAL_REPORT");
	
	Map forecastingListMap = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.FORECAST_LIST_MAP, request);
	if(forecastingListMap == null){
		forecastingListMap = new HashMap();
	}
	Map eventCalendarMapObj = (Map)LoadApplicationCacheService.applicationCacheObject
			.get(ApplicationConstants.EVENT_CALENDAR_MAP);
	if(eventCalendarMapObj==null){
		eventCalendarMapObj = new HashMap();
	}
	
	Users users = (Users) ApplicationUtil.getObjectFromSession(ApplicationConstants.APPLICATION_LOGGED_IN_USER, request);
	List rolesList = users.getRolesList();
	if(rolesList==null){
		rolesList = new ArrayList();
	}
	String isFrom =(String) ApplicationUtil.getObjectFromSession("isFrom", request);
%>    
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
	planningCycleBeanForm.action="saveForecastUnitsOverride.htm";
	planningCycleBeanForm.submit();
}

function saveForecastAspOverride(){
	loadProgress();
	planningCycleBeanForm.action="saveForecastAspOverride.htm";
 	planningCycleBeanForm.submit();
}

function getTableView(index){
	var tableObj = document.getElementById("tab"+index);
	tableObj.style.display = "";
	var chartObj = document.getElementById("chart"+index);
	chartObj.style.display = "none";
	var legendObj = document.getElementById("legends");
	legendObj.style.display = "none";
}

function getChartView(index){
	var tableObj = document.getElementById("tab"+index);
	tableObj.style.display = "none";
	var chartObj = document.getElementById("chart"+index);
	chartObj.style.display = "";
	var legendObj = document.getElementById("legends");
	legendObj.style.display = "";
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
		
		#inner-row0{
			margin-top: 3%
		}
		
		.legend{
			height: 20px;
		  	width: 2%;
		 	float: left;
		}

		#legend1{
			height: 20px;
		  	width: 20px;
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
		  padding-left: 6%;
		  color: black;
	      font-size: small;
		}
		#tab0, #tab1, #tab2, #tab3, #tab4{
			height: 340px;
			overflow-y: auto;
			
		}
		
	</style>
	
	<form:form method="post" action="historicalReport.htm" commandName="model" name="planningCycleBeanForm" >
	<form:hidden id="planningCycleId" path="planningCycleId"/>
	<input type="hidden" name="isFromDashboard" id="isFromDashboard"/>
    <div id="wrapper">
       
        <div id="page-wrapper"  style=" padding-right: 30px; min-height: 500px;" >
            <div id="page-inner" style="margin-top:-1.6%;min-height: 500px;">
			 <div class="row" id="row1">
                    <div class="col-md-12">
                        <div class="panel panel-default" style="height:auto">
                        	<c:set var="pageTemplateObj"  value="<%=pageTemplate%>"/>
							<div class="panel-heading" style="background-color:#2DAFCB;font-size:130%;height:52px;padding:7px 15px;color:#fff;padding-top:1.5%  ">
								&nbsp;Actuals Report
								<div class="row" style="margin-top:2%">
									<div class="col-md-12">
										<div class="col-md-9">
											<div id="components" style="width:100%;">
											<c:if test="${model.type=='PRODUCT'}">
												<label><b>Product Num:</b></label>${model.selectedTypeValue}
												<label style="margin-left: 1%"><b>Product Description:</b></label>${model.productDescription}
												<label style="margin-left: 1%"><b>Product Hierarchy II:</b></label>${model.productHierarchyII}
												<label style="margin-left: 1%"><b>Planogram:</b></label>${model.planogram}
											</c:if>
											<c:if test="${model.type=='MODEL'}">
												<label><b>Model Name:</b></label>${model.selectedTypeValue}
											</c:if>
											<c:if test="${model.type=='CATEGORY'}">
												<label><b>Category Name:</b></label>${model.selectedTypeValue}
											</c:if>
											</div>
										</div>
										<div class="col-md-3" style="max-height:20px">
											<div id="legends" style="max-height:20px;float:right;margin-right:14%">
												<div id="legend1" class="legend"></div><div id="legendName1">Actuals</div>
											</div> 
										</div>
									</div>
							</div>
							</div>
							<div class="panel-body" style="width:100%;height:428px;overflow-y:auto">
								<c:forEach var="pageTemplatesChartsObj" items="${pageTemplateObj.pageTemplateCharts}" varStatus="outerloop">
									
			                        	<div class="panel panel-default" style="height:430px;margin-top:0" id="inner-row${outerloop.index}">
											<div class="panel-heading" style="background-color:#CFCFCF;;font-size:100%;height:40px;padding:7px 15px;color:#fff  ">
												&nbsp;${pageTemplatesChartsObj.chartTitle}
												<div class="pull-right" style="text-align: right;">
													<div class="btn-group">
														<button type="button"
															class="btn btn-default btn-xs dropdown-toggle"
															data-toggle="dropdown" style="height: 25px">
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
											</div>
											<div class="panel-body">
												 <c:if test="${model.jsonStrList != null && model.jsonStrList != '[]'  && model.jsonStrList != 'null' && model.jsonStrList != '' && isFrom.equals('historicalActual')}">
													<c:forEach var="jsonStrList" items="${model.jsonStrList}" varStatus="innerloop">
															<c:if test="${innerloop.index==outerloop.index}">
															<div id="chart${innerloop.index}"></div>
																		<script>
																		function lineChart(){
																			Morris.Line({
																						  element: 'chart${innerloop.index}',
																						  data: ${jsonStrList}, 
																						  xkey: 'w',
																						  ykeys: ['a'],
																						  labels: ['Actuals '],
																						  fillOpacity: 0.6,
																						  hideHover: 'auto',
																						  behaveLikeLine: true,
																						  resize: true,
																						  pointFillColors:['#ffffff'],
																						  pointStrokeColors: ['black'],
																						  lineColors:['gray'],
																						  parseTime: false,
																						  xLabelAngle: 80
																					});
																			}
																		lineChart();		
																		
																		</script>
															</c:if>
															
													</c:forEach> 
													<div class="table-responsive" id="tab${outerloop.index}" style="display:none;">
													<table class="table table-striped">
														<thead>
															<tr>
															<th>Period</th>
															<th>Actuals</th>
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
    
   

