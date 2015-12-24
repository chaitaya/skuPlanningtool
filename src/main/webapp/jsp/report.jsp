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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/WEB-INF/bridgei2i.tld" prefix="bi2itag"%>
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
<script type="text/javascript" language="javascript">
window.scrollTo((document.body.offsetWidth-document.documentElement.offsetWidth)/2, (document.body.offsetHeight-document.documentElement.offsetHeight)/2);

</script>
<style>
.legend{
			height: 18px;
		  	width: 4%;
		 	float: left;
		}

		#legend1{
			 background: #ddd;
			  
		}
		#legend2{
			 background: #8FD8D8;
		}
		#legends1{
			  margin-left: 30%;
 			  width: 100%;
		}
		#legends2{
			  margin-left: 40%;
 			  width: 100%;
		}
		#legendName2{
		
		  float: left;
		  width: 13%;
		  padding-left: 1%;
		  font-size:small;
		  font-color:black
		}
		#legendName1{
		 float: left;
		  width: 7%;
		  padding-left: 1%;
		  font-size:small;
		  font-color:black
		}
		/* svg{
		  width: 948px;
		} */
</style>
<% String isFrom1 =(String) ApplicationUtil.getObjectFromSession("isFrom1", request); 
String isFrom =(String) ApplicationUtil.getObjectFromSession("isFrom", request);

%>

	
	<form:form method="post" action="reports.htm" commandName="model" name="reportBeanForm" >
	<form:hidden id="planningCycleId" path="planningCycleId"/>
	<input type="hidden" name="isFromDashboard" id="isFromDashboard"/>
    <div id="wrapper">
       
        <div id="page-wrapper"  style="  padding: 18px 7px;min-height:525px">
            <div id="page-inner"  style="margin-top:-1.6%;min-height: 525px;">
			 <div class="row" id="row1" style="padding-left:8px">
                    <div class="col-md-12">
                        <div class="panel panel-default" style="height:515px;width:102%;margin-left:-1.2%;background-color:#fff">
							<div class="panel-heading" style="background-color:#2DAFCB;font-size:130%;height:52px;padding:7px 15px;color:#fff;padding-top:1.5%  ">
								&nbsp;Executive Report
								<div class="pull-right" style="text-align: right;">
									<div class="btn-group">
										<button type="button"
											class="btn btn-default btn-xs dropdown-toggle"
											data-toggle="dropdown" style="height: 25px">
											Actions <span class="caret"></span>
										</button>
										<ul class="dropdown-menu pull-right" role="menu" style="min-width: 16px;">
											<li><a id='${outerloop.index}' href="exportExecutiveSummary.htm" class="export"><img src="./images/excel_icon_unselected.png" title="Export to excel" width="20" height="20"/>    
											</a></li>
										</ul>
									</div>
								</div>
								
								<div class="row" style="margin-top:1.1%;overflow-x:hidden;background-color:#fff">
								<div class="col-md-12" style="color:black;background-color:#fff">
									<div class="col-md-9">
										<%if(isFrom != null && isFrom.equals("categoryLevelReport")){%>
											<div id="components" style="width:100%;">
												<c:if test="${model.type=='BUSINESS'}">
													<%if(isFrom1 != null && isFrom1.equals("reportModule")){%>
													<label style="font-size:15px;color:black"><b>Planning Cycle:</b></label><label style="font-size:15px;font-weight:400">&nbsp; ${model.planningCycle}</label>
													<%} %>
													<label style="font-size:15px;font-color:black;margin-left: 1%"><b>Business:</b></label><label style="font-size:15px;font-weight:400">&nbsp; ${model.selectedTypeValue}</label>
													<%-- 
													<label style="margin-left: 1%"><b>Planogram:</b></label>${model.planogram} --%>
												</c:if>
												<c:if test="${model.type=='PLCLASS'}">
													<%if(isFrom1 != null && isFrom1.equals("reportModule")){%>
													<label style="font-size:15px;font-color:black"><b>Planning Cycle:</b></label><label style="font-size:15px;font-weight:400">&nbsp; ${model.planningCycle}</label>
													<%} %>
													<label style="font-size:15px;font-color:black;margin-left: 1%"><b>Business:</b></label> <label style="font-size:15px;font-weight:400">&nbsp; ${model.business}</label>
													<label style="font-size:15px;font-color:black;margin-left: 1%"><b>PL Class:</b></label><label style="font-size:15px;font-weight:400">&nbsp; ${model.selectedTypeValue}</label>
												</c:if>
												<c:if test="${model.type=='CATEGORY'}">
													<%if(isFrom1 != null && isFrom1.equals("reportModule")){%>
													<label  style="font-size:15px;font-color:black"><b>Planning Cycle:</b></label><label style="font-size:15px;font-weight:400">&nbsp; ${model.planningCycle}</label>
													<%} %>
													<label style="margin-left: 1%;font-size:15px;font-color:black"><b>Business:</b></label><label style="font-size:15px;font-weight:400">&nbsp; ${model.business}</label>
													<label style="margin-left: 1%;font-size:15px;font-color:black"><b>PL Class:</b></label ><label style="font-size:15px;font-weight:400">&nbsp; ${model.plClass}</label>
													<label  style="margin-left: 1%;font-size:15px;font-color:black"><b>Category Name:</b></label><label style="font-size:15px;font-weight:400">&nbsp; ${model.selectedTypeValue}</label> 
												</c:if>
											</div>
										<%} %>
									</div>
									<div class="col-md-3" >
											<div id="legends1" style="width:240%;margin-bottom:2%;margin-left:25%">
										 		<div id="legend1"  class="legend"></div><div id="legendName1">Actuals</div>&nbsp;
										 		<div id="legend2" style="margin-left:5%" class="legend" ></div><div id="legendName2" >Forecast</div>
											</div>
									</div>
								</div>
							</div>
							</div>
							<div class="panel-body" style="overflow-x:hidden;height:455px;overflow-y:auto">
								<bi2itag:TargetTableTag targetTableMap="${model.targetTableMap}"
								tableColorRange="${model.tableColorRange}"
								reportType="report"/>
							
								<%-- <c:forEach var="entry" items="${model.targetTableMap}" varStatus="outerloop">
									<div class="panel-heading">
		                            	${entry.key}
		                        	</div>
								
									<div class="table-responsive">
										
                                       <c:forEach var="tableListObj" items="${entry.value}" varStatus="innerloop1">
                                       				<c:forEach var="entryMap" items="${tableListObj}">
                                       					<table class="table table-striped table-bordered table-hover">
                                    						<tbody>
																<c:forEach var="valueList" items="${entryMap.value}" varStatus="innerloop2">
																	<c:if test="${innerloop2.index==0}">
																	<tr>
																		<c:forEach var="value" items="${valueList}" varStatus="innerloop3">
																			<c:if test="${innerloop3.index<7}">
																			<td>${value}</td>
																			</c:if>
																			<c:if test="${innerloop3.index>=7}">
																			<td style="background-color: #8FD8D8;">${value}</td>
																			</c:if>
																			
																		</c:forEach>
																	</tr>
																	</c:if>
																	<c:if test="${innerloop2.index!=0}">
																	<tr>
																		<c:forEach var="value" items="${valueList}" varStatus="innerloop3">
																			<td>${value}</td>
																			
																		</c:forEach>
																	</tr>
																	</c:if>
																	</c:forEach>
																</tbody>
															</table>
													</c:forEach>
										</c:forEach>
		                            	</div>
		                            <br/>
								</c:forEach> --%>
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
    
   

