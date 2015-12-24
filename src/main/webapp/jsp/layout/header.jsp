<%@page import="java.util.ArrayList"%>
<%@page import="com.bridgei2i.common.controller.LoadApplicationCacheService"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.bridgei2i.common.vo.Users"%>
<%@page import="com.bridgei2i.common.util.ApplicationUtil"%>
<%@page import="com.bridgei2i.common.constants.ApplicationConstants"%>
 <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="java.util.List"%>

<%
	String contextPath = request.getContextPath();
	Users users = (Users) ApplicationUtil.getObjectFromSession(
			ApplicationConstants.APPLICATION_LOGGED_IN_USER, request);
	List roles = null;
	if(users!= null){
		roles = users.getRolesList();
	}
	List usersList = (List)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.APPLICATION_USERS_LIST);
	if(usersList == null){
		usersList = new ArrayList();
	}
	String headerFrom =(String) ApplicationUtil.getObjectFromSession("headerFrom", request);

%>

<!-- Bootstrap Styles-->
    <link href="css/bootstrap.css" rel="stylesheet" />
     <!-- FontAwesome Styles-->
   <link href="css/font-awesome.css" rel="stylesheet" />
  
    
        <!-- Custom Styles-->
    <link href="css/custom-styles.css" rel="stylesheet" />


   <script src="<%=contextPath%>/js/jquery-1.10.2.js"></script>
   <script src="<%=contextPath%>/js/jquery.validate.js"></script>
   <script src="<%=contextPath%>/js/jquery.validate.min.js"></script>
   
      <!-- Bootstrap Js -->
     <script src="<%=contextPath%>/js/bootstrap.min.js"></script>
    <!-- Metis Menu Js -->
<script type="text/javascript">

	
    function logout(){
    	headerForm.action="logout.htm";
		headerForm.submit();
    }
    
    function exportFlatFile(){
    	headerForm.action="exportFlatFile.htm";
		headerForm.submit();
    }
    
    function actAsUser(){
    	var selectedUserObj = document.getElementById("selectedUser");
    	var selectedUserIdObj = document.getElementById("selectedUserId");
    	var strUserId = selectedUserObj.options[selectedUserObj.selectedIndex].value;
    	selectedUserIdObj.value =strUserId; 
    	headerForm.action="actAsUser.htm";
		headerForm.submit();
    }
    function refreshCacheData(){
    	headerForm.action="refreshCacheData.htm";
		headerForm.submit();
    }
    </script>
    <div id="wrapper">
     <nav class="navbar navbar-default top-navbar" role="navigation">
          <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".sidebar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="http://www.hp.com/" target="_blank"><img src="<%=contextPath%>/images/HP.png" width="50px"
			height="50px" style="  margin-top: -10px;" /></a>
            </div>
            <div class="title">SKU Level Planning Tool</div>
           <div class="navbar-header" id="navbar-icon">	
           <c:set var="headerFormValue"  value="<%=headerFrom%>"/>
           	<c:if test="${headerFormValue=='' || headerFormValue == null || headerFormValue != 'reports' || headerFormValue != 'reports'}">
           	</c:if>
           		<%if((headerFrom == null || ApplicationUtil.isEmptyOrNull(headerFrom)) ){%>
           			<a class="navbar-icon" id="PlanningLog" href="homePage.htm" style="color: #2DAFCB;" ><img src="<%=contextPath%>/images/cycle1.png" width="30px"
				height="30px" style="  margin-top: -2px;" />Planning</a>
				<%} %>
				<%if(headerFrom != null && headerFrom.equals("planning")){ %>
           			<a class="navbar-icon" id="PlanningLog" href="homePage.htm" style="color: #2DAFCB;" ><img src="<%=contextPath%>/images/cycle1.png" width="30px"
				height="30px" style="  margin-top: -2px;" />Planning</a>
				<%} else{%>
					<a class="navbar-icon" id="PlanningLog" href="homePage.htm" ><img src="<%=contextPath%>/images/cycle.png" width="30px"
				height="30px" style="  margin-top: -2px;" />Planning</a>
				<%} %>
	           
				<%if(headerFrom != null && headerFrom.equals("reports")){%>
					
						<ul style="list-style:none;display:inline-block;padding:0px;">
			                <li class="dropdown" >
			                    <a class="dropdown-toggle" id="Reports" data-toggle="dropdown" href="#" aria-expanded="false" style="color: #2DAFCB;text-decoration: none;">
			                   		 <img src="<%=contextPath%>/images/Report2.PNG" width="30px" height="31px" style="margin-top: -2px;" /><b>Reports</b>
										<i class="fa fa-caret-down"></i>
			                    </a>
			                    <ul class="dropdown-menu dropdown-user bullet" style="margin-top: 16px;">
			                        <li><a href="historicalActualReport.htm"><i class="fa fa-tags">&nbsp;&nbsp;</i>Actuals Report</a></li>
			                        <li><a href="accuracyReport.htm"><i class="fa fa-bullseye">&nbsp;&nbsp;</i> Accuracy Report</a></li>
			                        <li><a href="overrideReport.htm"><i class="fa fa-pencil-square-o">&nbsp;&nbsp;</i> Override Report</a></li>
			                        <li><a href="#" style="cursor:pointer;" onclick="executiveReports();"><i class="fa fa-users">&nbsp;&nbsp;</i> Executive Report</a></li>
			                        <li><a href="#" style="cursor:pointer;" onclick="getPlanogramReports();"><i class="fa fa-file-text">&nbsp;&nbsp;</i> Planogram Report</a></li>
			                    </ul>
			                </li>
		           	 	</ul>
				<%} else{ %>
				<ul  style="list-style:none;display:inline-block;padding:0px;">
	                <li class="dropdown" >
	                    <a class="dropdown-toggle" id="Reports" data-toggle="dropdown" href="#" aria-expanded="false" style="color: #000;text-decoration: none;">
								<img src="<%=contextPath%>/images/Report.PNG" width="30px" height="31px" style="margin-top: -2px;" /><b>Reports</b>
										<i class="fa fa-caret-down"></i>
	                    </a>
	                    <ul class="dropdown-menu dropdown-user bullet" style="margin-top: 16px;">
	                        <li><a href="historicalActualReport.htm"><i class="fa fa-tags">&nbsp;&nbsp;</i>Actuals Report</a></li>
	                        <li><a href="accuracyReport.htm"><i class="fa fa-bullseye">&nbsp;&nbsp;</i> Accuracy Report</a></li>
	                        <li><a href="overrideReport.htm"><i class="fa fa-pencil-square-o">&nbsp;&nbsp;</i> Override Report</a></li>
	                        <li><a style="cursor:pointer;" onclick="executiveReports();"><i class="fa fa-users">&nbsp;&nbsp;</i> Executive Report</a></li>
	                        <li><a style="cursor:pointer;" onclick="getPlanogramReports();"><i class="fa fa-file-text">&nbsp;&nbsp;</i> Planogram Report</a></li>
	                        
	                    </ul>
	                </li>
           	 	</ul>
				<%} %>
				
				<%if(headerFrom != null && headerFrom.equals("utilities")){%>
					<a class="navbar-icon" id="Utilities" href="dataUpload.htm" style="color: #2DAFCB;"><img src="<%=contextPath%>/images/Settings1.PNG" width="40px"
				height="29px" style="  margin-top: -2px;" />Utilities</a>
				<%} else{ %>
					<a class="navbar-icon" id="Utilities" href="dataUpload.htm"><img src="<%=contextPath%>/images/Settings.PNG" width="40px"
				height="29px" style="  margin-top: -2px;" />Utilities</a>
				<%} %>
			</div>
           	<form:form method="post" action="header.htm" commandName="model" name="headerForm">
           	<input type="hidden" name="selectedUserId" id="selectedUserId"/>
           	
            <ul class="nav navbar-top-links navbar-right">
               
                <!-- /.dropdown -->
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#" aria-expanded="false" style="color: #000;">
                        <img src="<%=contextPath%>/images/user-logo.png" width="18px" height="18px" />
                        <font>Welcome <%=users.getFirstName()%>&nbsp;&nbsp;</font> <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-user">
                        <li><a href="#"><i class="fa fa-user fa-fw"></i> User Profile</a>
                        </li>
                        <li><a href="#"><i class="fa fa-gear fa-fw"></i> Settings</a>
                        </li>
                        <% if(roles.contains(ApplicationConstants.ADMIN)){%>
                        <li><a href="#" data-target="#actAsUserPopup" data-toggle="modal"><i class="fa fa-user fa-fw"></i>Act As User</a>
                        </li>
                        <li><a onClick="refreshCacheData();" style="cursor:pointer;"><i class="fa fa-refresh fa-fw"></i> Refresh Cache</a>
                        </li>
                        <li><a onClick="exportFlatFile();" style="cursor:pointer;"><img src="./images/excel_icon_unselected.png" title="Export to excel" width="12" height="12" style="margin-left: 4px;margin-right: 5px;"/> Export Flat File</a>
                        </li>
                        <%} %>
                        <li><a href="HP_SKU PLANNER_FAQ.pdf" target="_blank"><i class="fa fa-question" style="padding-left: 3.1%;"></i><!-- <span class="glyphicon glyphicon-question-sign"></span> -->
                                        FAQ</a></li>
                        <li class="divider"></li>
                        <li><a onClick="logout();" style="cursor:pointer;"><i class="fa fa-sign-out fa-fw"></i> Logout</a>
                        </li>
                        
                    </ul>
                    <!-- /.dropdown-user -->
                </li>
                <!-- /.dropdown -->
                
            </ul>
            
            	</form:form>
            	
        </nav>
        <div id="actAsUserPopup" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">
				  <div class="modal-dialog modal-lg">
					 <div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">X</button>
								<h4 class="modal-title" >Act as User</h4>
						</div>
						<div class="modal-body" style="height:100px;overflow:auto">
						<div class="row">
							  <div class="col-sm-2"><label>Select User</label></div>
							<c:set var="usersListObj" value="<%=usersList%>"/>
							 <select name="selectedUser" id="selectedUser"> 
						  		<option value="">Choose a user...</option>
								<c:forEach var="usersObj" items="${usersListObj}" varStatus="innerloop1">
								<c:if test="${usersObj.lastName != null && usersObj.lastName != 'null' && usersObj.lastName != ''}">
						      		<option value="${usersObj.login_id}">${usersObj.firstName} ${usersObj.lastName}</option>
					      		</c:if>
					      		<c:if test="${usersObj.lastName == null || usersObj.lastName == 'null' || usersObj.lastName == ''}">
						      		<option value="${usersObj.login_id}">${usersObj.firstName}</option>
					      		</c:if>
			           			</c:forEach>
			           			</select>	
			           			</div>
						</div>
						<div class="modal-footer">
		  					 <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
							  <button  class="btn btn-primary" data-dismiss="modal" onclick="actAsUser();">Ok</button>
					    </div>
				    </div>
				  </div>
			  </div>
</div>

