<!DOCTYPE html>
<%@page import="java.util.HashMap"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/bridgei2i.tld" prefix="bi2itag"%>
<%@ page isELIgnored="false"%>
<%@page import="com.bridgei2i.common.util.ApplicationUtil"%>
<%@page import="com.bridgei2i.common.constants.ApplicationConstants"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.bridgei2i.common.vo.Users"%>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>SKU Level Planning Tool</title>
<link rel="shortcut icon" href="/images/bi2i.jpeg" />
<!-- Bootstrap Styles-->
<link href="css/bootstrap.css" rel="stylesheet" />
<!-- FontAwesome Styles-->
<link href="css/font-awesome.css" rel="stylesheet" />
<!-- Morris Chart Styles-->
<link href="js/plugins/morris/morris-0.4.3.min.css" rel="stylesheet" />
<!-- Custom Styles-->
<link href="css/custom-styles.css" rel="stylesheet" />
  <script src="js/jquery-1.11.0.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/plugins/metisMenu/jquery.metisMenu.js"></script>
      <script src="js/jquery-1.10.2.js"></script>
      <script src="js/jquery.metisMenu.js"></script>
        <!-- Morris Chart Js -->
     <script src="js/plugins/morris/raphael-2.1.0.min.js"></script>
    <script src="js/plugins/morris/morris.js"></script>

<%
	String contextPath = request.getContextPath();
	Users users = (Users) ApplicationUtil.getObjectFromSession(
			ApplicationConstants.APPLICATION_ACT_AS_USER, request);
	List roles = null;
	if(users!= null){
		roles = users.getRolesList();		
	}

%>

<script type="text/javascript">
	$(document).ready(function() {

		$(".btn").click(function() {

			$("#myModal").modal('show');
		});
	});
	
	
	function uploadData() {
		loadProgress();
		homeBeanForm.action = "uploadData.htm";
		homeBeanForm.submit();

	}
	
	function planningLogDetails(){
		loadProgress();
		homeBeanForm.action = "planningLogDetails.htm";
		homeBeanForm.submit();
	}
	
	function freezeOverride(){
		loadProgress();
		homeBeanForm.action = "freezePlanningCycle.htm";
		homeBeanForm.submit();
	}
	
	function getCategoryLevelSummary(){
		loadProgress();
		homeBeanForm.action= "categoryLevelSummary.htm";
		homeBeanForm.submit();
	}
	
	function forceScheduler(){
		loadProgress();
		homeBeanForm.action= "FTPscheduler.htm";
		homeBeanForm.submit();
	}
	
</script>

<style type="text/css">
.bs-example {
	margin: 20px;
}
.bootstrap-demo {
	margin: 30px;
}
.hyperlinks{
	  width: 20%;
  	  float: left;
	  margin-top: 10%;
      margin-left: 10%;
      border: 1px solid #ddd;
      text-align: -webkit-center;
}
.close{
font-size: 18px;
}
.alert{
padding: 3px;
    margin-bottom: -29px;
    height: 55px;
}
</style>

</head>
<body>
	<div id="wrapper">
		<div id="page-wrapper" style="  min-height: 500px; height: 585px;">
			<div id="page-inner" style="  min-height: 300px;">
				<div class="bootstrap-demo">
					<div class="well well-lg" id="heading"
						style="margin-left: -5%; margin-right: -5%; margin-top: -4.3%;height:54px;border-radius:4px;background-color:#2DAFCB">
						<strong> <font size="4%" color="white">Welcome to SKU Level Planning Tool</font></strong>
					</div>
				</div>
				<form action="<c:url value='/save-active-week.htm'/>" method="post" commandName="model" name="homeBeanForm">

					<input type="hidden" name="id" value="${id}" />
					<div class="bootstrap-demo">
						<table class="table table-striped">
							<thead id="tblheader">
								<tr>
									<th id="tblheader"></th>
									<th id="tblheader">Start Date</th>
									<th id="tblheader">Cycle End</th>
									<th id="tblheader">WorkFlowStatus</th>
									<th id="tblheader">PlanningStatus</th>
								</tr>
							</thead>
							<tbody>
									<c:forEach var="planningCycleListObj" items="${model.planningCycleList}" varStatus="loop" >
										<tr>
										<c:if test="${loop.index==0}">
											<td>Last Cycle</td>
										
													                    
											<td>${planningCycleListObj.startWeek}</td>
											<td>${planningCycleListObj.closedDate}</td>
											<td>${planningCycleListObj.workFlowStatusName}</td>
											<%-- <td>${model.id }</td> --%>
											<td>${planningCycleListObj.statusName}</td>
										</c:if>
										</tr>
										<!--  </hr> -->
										<tr>
											
											<c:if test="${loop.index==1}">
												<c:if test="${planningCycleListObj.logicalName!='INITIATED'}">
												<td>Active Cycle</td>
												</c:if>
												<c:if test="${planningCycleListObj.logicalName=='INITIATED'}">
												<td>
												<%if(roles != null && (roles.contains("BI2I_ADMIN"))){ %>
														<a href="#" onclick="planningLogDetails();"><b>Active Cycle</b></a>
													<%}%>
													<%if(roles != null && (roles.contains("PRODUCT_MANAGER") || roles.contains("CATEGORY_MANAGER"))){ %>
														<a href="#" onclick="planningLogDetails();"><b>Active Cycle</b></a>
													<%}%>
													<%if(roles != null && (roles.contains("CATEGORY_DIRECTOR"))){ %>
														<a href="#" onclick="getCategoryLevelSummary();"><b>Active Cycle</b></a>
													<%}%>
													<%if(roles != null && (roles.contains("FINANCE_DIRECTOR"))){ %>
														<a href="#" onclick="freezeOverride();"><b>Active Cycle</b></a>
													<%}%>
												</td>
												</c:if>
												<c:if test="${planningCycleListObj.logicalName=='UPLOAD'}">
                                                         <td><form:select path="homeBean.selectedActiveWeek" multiple="false" >
                                                    <form:options items="${model.activeWeekList}" />
                                                    </form:select>
                                                    <button type="submit" class="glyphicon glyphicon-floppy-disk"></button>
                                                    </td>
                                                   </c:if>
                                                   <c:if test="${planningCycleListObj.logicalName!='UPLOAD'}">
                                                   <td>${planningCycleListObj.cycleStartDate}
                                                    </td>
                                                   </c:if>

			               						 <td></td>
			               						 <td>${planningCycleListObj.workFlowStatusName}</td>
			
												<input id="startWeek" name="startWeek" value="${planningCycleListObj.startWeek}" type="hidden" />
												<input id="startYear" name="startYear" value="${planningCycleListObj.startYear}" type="hidden" />
												<input id="planningCycleId" name="planningCycleId" value="${planningCycleListObj.id}" type="hidden" />
												
												<c:if test="${planningCycleListObj.logicalName=='UPLOAD'}">
												<td>
												<%if(roles != null && (roles.contains("BI2I_ADMIN"))){ %>
													<button type="button" class="btn btn-primary" onclick="uploadData();">${planningCycleListObj.statusName}</button>
													<button type="button" class="btn btn-primary" onclick="forceScheduler();">Run Scheduler</button>
												<%} %>
												</td>
												</c:if>
												<c:if test="${planningCycleListObj.logicalName=='REVIEW'}">
												<%if(roles != null && (roles.contains("BI2I_ADMIN"))){ %>
														<td><button type="button" class="btn btn-primary" onclick="planningLogDetails();">${planningCycleListObj.statusName}</button></td>
												<%}%>
												</c:if>
												<c:if test="${planningCycleListObj.logicalName=='INITIATED'}">
												<td>${planningCycleListObj.statusName}
														<%-- <span class="label label-default">${planningCycleListObj.statusName}</span> --%>
													</td>
												</c:if>
											</c:if>		
										</tr>
		 							</c:forEach>
							</tbody>
						</table>
					</div>
					<%-- <c:if test="${model.logicalName=='INITIATED'}">
						<%if(roles != null && (roles.contains("BI2I_ADMIN"))){ %>
								<div class="hyperlinks"><a href="#" onclick="planningLogDetails();"><b>Planning Log</b></a></div>
								<div class="hyperlinks"><a href="#" onclick="getCategoryLevelSummary();"><b>Approve Planning Cycle</b></a></div>
								<div class="hyperlinks"><a href="#" onclick="freezeOverride();"><b>Freeze Planning Cycle</b></a></div>
						<% } %>	
					</c:if> --%>
				</form>
								<!-- <div class="modal-footer">
									<button type="button" class="btn btn-default"
										data-dismiss="modal">Close</button>
									<button type="button" class="btn btn-primary">Save
										changes</button>
								</div> -->
								<!-- For EOL -->
								<%-- <form action="<c:url value='/eolData.htm'/>" method="post" commandName="model" name="eolBeanForm">
								<div class="modal-footer">
								<button type="submit" class="btn btn-primary" onclick="eolData();">EOL
										</button>
								</div>
								</form> --%>
								<!-- For EOL -->
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- /. PAGE INNER  -->
		</div>
		<!-- /. PAGE WRAPPER  -->
	</div>
	<!-- /. WRAPPER  -->
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
</html>
