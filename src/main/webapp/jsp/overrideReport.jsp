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
	PageTemplate pageTemplate = (PageTemplate)pageTemplateCacheObj.get("PC_DASHBOARD");
	Map selectedOverrideReportFilterValuesMapObj = (Map)ApplicationUtil.getObjectFromSession(ApplicationConstants.SELECTED_OVERRIDE_REPORT_FILTER_VALUES_MAP, request);
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
	
%>
<script type="text/javascript">
function onBackButton(){
	planningCycleBeanForm.action="planningLogDetails.htm";
	var isFromDashboardObj = document.getElementById("isFromDashboard");
	isFromDashboardObj.value = 'true';
	planningCycleBeanForm.submit();
	
}


</script>

<style>
#row1 {
	margin-right: -40px;
	margin-left: -25px;
}

#page-inner {
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
#inner-row0 {
	margin-top: 6%
}

.legend {
	height: 20px;
	width: 2%;
	float: left;
}

#legend1 {
	background: gray;
}

#legends0, #legends1 {
	margin-left: 30%;
	width: 100%;
}

#legends2, #legends3, #legends4 {
	margin-left: 40%;
	width: 100%;
}

#legend2 {
	background: #2DAFCB;
}

#legend3 {
	background: green;
}

#legend4 {
	background: orange;
}

#legendName2, #legendName3, #legendName4 {
	float: left;
	width: 13%;
	padding-left: 1%;
}

#legendName1 {
	float: left;
	width: 7%;
	padding-left: 1%;
}

#tab0, #tab1, #tab2, #tab3, #tab4 {
	height: 340px;
	overflow-y: auto;
}
</style>

<form:form method="post" action="dashboard.htm" commandName="model"
	name="planningCycleBeanForm">
	<form:hidden id="planningCycleId" path="planningCycleId" />
	<input type="hidden" name="isFromDashboard" id="isFromDashboard" />

	<div id="wrapper">
		<div id="page-wrapper" style="  padding: 15px 20px; min-height: 545px;">
			<div id="page-inner" style="margin-top:-0.6%;min-height: 545px;">
				<div class="row" style="margin-top: -1%">
					<div class="col-md-12">
						<div class="panel panel-default"
							style="height:531px; width: 103%; margin-left: -1.2%" >
							<div class="panel-heading"
								style="background-color: #2DAFCB; font-size: 130%; height: 52px; padding: 7px 15px; color: #fff; padding-top: 1.5%">
								&nbsp;<strong><b>Override Report</b></strong>

							</div>
							
							<div class="panel-body" style="width:100%;height:456px;overflow-y:auto">

								<c:forEach var="overrideReportObj"
									items="${model.overrideReportListObj}" varStatus="loop">
									<tr class="success">
										<td width="100px" style="text-align: center;">${overrideReportObj}</td>
									</tr><br>
									
																		<%-- <c:if test="${loop.index==0}">
									<tr class="success">
									<td width="100px" style="text-align: center;">${overrideReportObj}</td>
									</tr>
									<br>
									</c:if>	
									<c:if test="${loop.index==1}">
									<tr class="success">
									<td width="100px" style="text-align: center;">${overrideReportObj}</td>
									</tr>
									<br>
									</c:if>
										<c:if test="${loop.index==2}">
										<tr class="success">
									<td width="100px" style="text-align: center;">${overrideReportObj}</td>
									</tr>
									<br>
									</c:if>	 --%>
										
								</c:forEach>




							</div>
							<div class="panel-body" style="width: 100%"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

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



