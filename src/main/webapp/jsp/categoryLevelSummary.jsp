<!DOCTYPE html>
<%@page import="com.bridgei2i.common.vo.Users"%>
<%@page import="java.util.List"%>
<%@page import="com.bridgei2i.common.constants.ApplicationConstants"%>
<%@page import="com.bridgei2i.common.util.ApplicationUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>Category Level Summary</title>

<!-- Bootstrap Styles-->
<link href="css/bootstrap.css" rel="stylesheet" />
<!-- FontAwesome Styles-->
<link href="css/font-awesome.css" rel="stylesheet" />
<!-- Morris Chart Styles-->
<link href="js/plugins/morris/morris-0.4.3.min.css" rel="stylesheet" />
<!-- Custom Styles-->
<link href="css/custom-styles.css" rel="stylesheet" />
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

</head>
<%
Users users = (Users) ApplicationUtil.getObjectFromSession(
		ApplicationConstants.APPLICATION_ACT_AS_USER, request);
List roles = null;
if(users!= null){
	roles = users.getRolesList();		
}

%>
<style>
.margin {
	margin: 30px;
}

td > a{
	color:#369AB2;
}

.centerHeading{
	 text-align: center;
}
.centerText{
   text-align: center;
   border:none;
   background:transparent;
   margin-top:200px
}

</style>

<script type="text/javascript">

function releaseSkuToOverride(category,releaseFlag){
	document.getElementById("categoryName").value=category;
	document.getElementById("categoryDirectorReleaseFlagId").value=releaseFlag;
	categoryLevelSummaryBeanForm.action= "releaseSkuToOverride.htm";
	categoryLevelSummaryBeanForm.submit(); 
}

function commitCategory(category,commitFlag,commitLevel){
	document.getElementById("categoryName").value=category;
	document.getElementById("commitLevel").value=commitLevel;
	document.getElementById("categoryDirectorCommitFlagId").value=commitFlag;
	categoryLevelSummaryBeanForm.action= "commitCategory.htm";
	categoryLevelSummaryBeanForm.submit(); 
}

function approveAllCategories(){
	categoryLevelSummaryBeanForm.action= "approveAllCategories.htm";
	categoryLevelSummaryBeanForm.submit(); 
}

</script>
<body>
<form:form method="post" action="categoryLevelSummary.htm" 
		commandName="model" name="categoryLevelSummaryBeanForm">
		
	<div id="wrapper">
		<div id="page-wrapper" style=" padding: 15px 18px;">
			<div id="page-inner">
				 <div class="row" style="margin-top:-1%" >
                    <div class="col-md-12">
                        <div class="panel panel-default" style="height:auto;width:103%;margin-left:-1.2%">
							<div class="panel-heading" style="background-color:#2DAFCB;font-size:130%;height:52px;padding:7px 15px;color:#fff;padding-top:1.5%  ">
								&nbsp;Summary Log
								<div class="pull-right" style="text-align: right;">
									<a href="homePage.htm" ><img src="./images/back2.png" title="Go Back" style="width: 59%; height: 30%;
  									vertical-align: -webkit-baseline-middle; margin-top: -17px;"/></a>
								</div>
							</div>
							<form:hidden id="planningCycleId" path="planningCycleId"/>
							<div class="panel-body" style="width:100%">
								<div class="margin">
									<table class="table table-bordered table-striped" >
										<thead id="tblheader">
											<tr >
												<th id="tblheader" class="centerHeading">Category</th>
												<th id="tblheader" class="centerHeading">Manager Name</th>
												<th id="tblheader" class="centerHeading">Status<br/></th>
												<%if(roles != null && !roles.contains(ApplicationConstants.ADMIN)){ %>
													<th id="tblheader" class="centerHeading">Release for Category<br/><span>Manager Override</span></th>
													<th id="tblheader" class="centerHeading">Commit</th>
												<%} %>
											</tr>
										</thead>
										 <tbody>
											<c:forEach var="modelObject" items="${model.categoryStatusList}" varStatus="outerloop">
												<tr>
													 <c:forEach var="data" items="${modelObject}" varStatus="innerloop">
															<c:choose>
																<c:when test="${innerloop.index == 4}">
																	<%if(roles != null && !roles.contains(ApplicationConstants.ADMIN)){ %>
																	 <c:choose>
																		<c:when test="${data == '1'}">
																			<td class="centerText"><button id="cmt${innerloop.index}"  style="border:none;background:transparent;margin-top:16%"  onClick="commitCategory('${category}',1,1);" value="1" ><img src="./images/commit_green.png"/></button></td>
																		</c:when>
																		<c:otherwise>
																			<td class="centerText"><button id="cmt${loop.index}" style="border:none;background:transparent;margin-top:16%"   value="0"  disabled><img src="./images/commit_grey.png"/></button></td>
																		</c:otherwise>
																	</c:choose>
																	<%} %>
																</c:when>
																<c:when test="${innerloop.index == 3}">
																	<%if(roles != null && !roles.contains(ApplicationConstants.ADMIN)){ %>
																	 	<c:choose>
																			<c:when test="${data == '1'}">
																				<td class="centerText"  style="margin-top:-2%"><button id="rel${innerloop.index}" style="border:none;background:transparent;margin-top:10%"  onClick="releaseSkuToOverride('${category}',0);" value="1" ><img width="30px" src="./images/back-blue.png"/></button></td>
																			</c:when>
																			<c:otherwise>
																				<td class="centerText"  style="margin-top:2%"><button id="rel${loop.index}" style="border:none;background:transparent"   value="0"  disabled><img width="80px" src="./images/back1.png"/></button></td>
																			</c:otherwise>
																		</c:choose>
																	<%} %>
																</c:when>
																<c:otherwise>
																		<c:if test="${innerloop.index==0}">
																			<c:set var="category" value="${data}"/>
																		</c:if>
																		<td class="centerText"><label style="margin-top: 30px">${data}</label></td>	
																</c:otherwise>
															</c:choose>
													</c:forEach>  
												</tr>
											</c:forEach> 
									 	</tbody> 
									</table>
									<input id="categoryName"name="category" type="hidden"/>
									<input id="commitLevel"name="commitLevel" type="hidden"/>
									<input id="categoryDirectorReleaseFlagId" name="categoryDirectorReleaseFlag" type="hidden"/>
									<input id="categoryDirectorCommitFlagId" name="categoryDirectorCommitFlag" type="hidden"/>
									<%if(roles != null && !roles.contains(ApplicationConstants.ADMIN)){ %>
										<c:choose>
											<c:when test="${model.categoryCommitFlag == 1}">
												<button style="float:right" class="btn btn-success" name="approve" type="submit" value="0" onclick="approveAllCategories();" >Approve</button>
											</c:when>
											<c:otherwise>
												<button style="float:right" class="btn btn-success" name="approve" type="submit" value="0" disabled>Approve</button>
											</c:otherwise>
										</c:choose>
									<%} %>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</form:form>
</body>
</html>
