<!DOCTYPE html>
<%@page import="com.bridgei2i.common.util.PropertiesUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.bridgei2i.common.constants.ApplicationConstants"%>
<%@page import="com.bridgei2i.common.util.ApplicationUtil"%>
<%@page import="com.bridgei2i.common.vo.Users"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>SKU Planning Log</title>

<!-- Bootstrap Styles-->
<link href="css/bootstrap.css" rel="stylesheet" />
<!-- FontAwesome Styles-->
<link href="css/font-awesome.css" rel="stylesheet" />
<!-- Morris Chart Styles-->
<link href="js/plugins/morris/morris-0.4.3.min.css" rel="stylesheet" />
<!-- Custom Styles-->
<link href="css/custom-styles.css" rel="stylesheet" />
<script src="js/plugins/dataTables/jquery.dataTables.js"></script>
 <script src="js/plugins/dataTables/dataTables.bootstrap.js"></script>

<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

</head>
<%
	Users users = (Users) ApplicationUtil.getObjectFromSession(
			ApplicationConstants.APPLICATION_ACT_AS_USER, request);
	List roles = null;
	if(users!= null){
		roles = users.getRolesList();
		if(roles==null){
			roles= new ArrayList();
		}
	}
	String categoryColumn = PropertiesUtil.getProperty(ApplicationConstants.CATEGORY);
	String modelName=PropertiesUtil.getProperty(ApplicationConstants.MODEL);
%>

<style>
 .margin {
	/* margin: 30px;
	height: 450px;
  	overflow: auto; */
  	  margin-top: 1%;
}

td > a{
	color:#369AB2;
}

</style>

<script type="text/javascript">

function confirmCommit(){
    var checkboxes = $('input:checkbox[id!="selectAll"]:checked');
    if(checkboxes.length==0){
    	swal("No record selected to commit.");
    }else{
		swal({   title: "Override Commit",   
			text: "Are you sure you want to commit?",   
			type: "warning",   
			showCancelButton: true,   
			confirmButtonColor: "#2DAFCB",   
			confirmButtonText: "Yes, Commit it",   
			cancelButtonText: "No, cancel commit",   
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
					saveCommit();
					} else {     
						swal({   title: "Commit Cancelled",
							 text: "Commit Cancelled",   
							 showConfirmButton: false, 
							 type: "error",   
							 timer : 1000  });
					} 
				});
    }
}

function saveCommit(){
	loadProgress();
	planningLogBeanForm.action= "saveCommitStatus.htm";
	planningLogBeanForm.submit(); 
}

function onClickPlanningLog(selectedTypeVariable,type,selectedValue){
	document.getElementById("type").value=type;
	document.getElementById("selectedTypeValue").value=selectedValue;
	document.getElementById("selectedTypeVariable").value=selectedTypeVariable;
	planningLogBeanForm.action= "dashboard.htm";
	loadProgress();
	planningLogBeanForm.submit(); 
}
function onClickSku(selectedTypeVariable,type,selectedValue,planogram,prDesc,pHII){
	document.getElementById("type").value=type;
	document.getElementById("selectedTypeValue").value=selectedValue;
	document.getElementById("selectedTypeVariable").value=selectedTypeVariable;
	document.getElementById("planogram").value=planogram;
	document.getElementById("productDescription").value=prDesc;
	document.getElementById("productHierarchyII").value=pHII;
	planningLogBeanForm.action= "dashboard.htm";
	loadProgress();
	planningLogBeanForm.submit(); 
}

function getForecastValues(){
	loadProgress();
	document.getElementById("isFromPlanningLog").value="yes";
	planningLogBeanForm.action= "forecastValuesFromPLanningLog.htm";
	planningLogBeanForm.submit(); 
}

</script>
<body>
<form:form method="post" action="planningLog.htm" 
		commandName="model" name="planningLogBeanForm" 
		enctype="multipart/form-data">
		<form:hidden id="planningCycleId" path="planningCycleId"/>
		
		<input id="type" name="type" type="hidden" />
		<input id="selectedTypeValue" name="selectedTypeValue" type="hidden" />
		<input id="selectedTypeVariable" name="selectedTypeVariable" type="hidden" />
		<input id="planogram" name="planogram"  type="hidden" />
		<input id="productDescription" name="productDescription"  type="hidden" />
		<input id="productHierarchyII" name="productHierarchyII"  type="hidden" /> 
		<input id="isFromPlanningLog" name="isFromPlanningLog"  type="hidden" /> 
	<div id="wrapper">
		<div id="page-wrapper" style="  padding-left: 16px; padding-right: 25px; min-height: 500px;height: 999px;">
			<div id="page-inner" style="min-height: 500px;height: 945px;">
				 <div class="row" style="margin-top:-2.5%;" >
                    <div class="col-md-12">
                        <div class="panel panel-default" style="height:955px;width:103%;margin-left:-1.2%" >
							<div class="panel-heading" style="background-color:#2DAFCB;font-size:130%;height:52px;padding:7px 15px;color:#fff;padding-top:1.5%  ">
								&nbsp;Planning Log
								<div class="pull-right" style="text-align: right;margin-top:-0.5%">
								<%if(roles != null && !roles.contains(ApplicationConstants.ADMIN)){ %>
									<button type="button" style="background:none;border:none;width:40px;margin-left:2%;outline:none" onClick="getForecastValues()"><img src="./images/filter-pl.png" title="Filter Sku" style="width: 80%; height: 30%;
	  										vertical-align: -webkit-baseline-middle; margin-top: -17px;" /></button>  
								
								<%} %>	
							<a href="homePage.htm" ><img src="./images/back2.png" title="Go Back" style="width: 44%; height: 30%;
			  									vertical-align: -webkit-baseline-middle; margin-top: -17px;margin-right:1%"/></a>
									
								</div>
								
							</div>
							<div class="panel-body" style="position:absolute;width:100%">
							<%if(roles != null && !roles.contains(ApplicationConstants.ADMIN)){ %>
										<input type="button" name="submit1" class="btb btn-info" style= "  margin-left: 0%;" value="Commit"  onClick="confirmCommit();"/>
										<div class="margin" style="width:100%;height:831px;overflow-y:auto;padding-top:25px">
							<%}else{ %>
									<div style="height:20px"></div>
									<div class="margin" style="width:100%;height:831px;overflow-y:auto;padding-top:24px">
								<%} %>
								
									<table class="table table-bordered" >
										<thead id="tblheader" >
											<tr >
												<%if(roles != null && roles.contains(ApplicationConstants.ADMIN)){ %>
													<c:if test="${model.detailValueObjs == ' ' ||  model.detailValueObjs ==null || model.detailValueObjs=='[]' }">
														<th id="tblheader"><div style="position: absolute;text-align: -webkit-center; margin-left: -9px;width:20%;top: 44px;border-left: 1px solid #ddd;line-height: 42px;background:#5BC0DE;color:#fff;height: 48px;">Product Hierarchy</div></th>
                                                       <th id="tblheader"><div style="position: absolute;text-align: -webkit-center;width:20%;top: 44px;border-left: 1px solid #ddd;margin-left: -9px;line-height: 42px;background:#5BC0DE;color:#fff;height: 48px;">Manager Name</div></th>
                                                       <th id="tblheader"><div style="position: absolute;width: 20%;text-align: -webkit-center; margin-left: -9px;top: 44px;border-left: 1px solid #ddd;line-height: 42px;background:#5BC0DE;color:#fff;height: 48px;">Status</div></th>
                                                       <th id="tblheader"><div style="position: absolute;width: 20%;text-align: -webkit-center; margin-left: -9px;top: 44px;border-left: 1px solid #ddd;background:#5BC0DE;color:#fff; line-height: 21px;height: 48px;">Override Flag<br/><span>(Product Manager)</span></div></th>
                                                       <th id="tblheader"><div style="position: absolute;width: 20%;text-align: -webkit-center; margin-left: -9px;top: 44px;border-left: 1px solid #ddd;background:#5BC0DE;color:#fff;line-height: 21px;height: 48px;">Override Flag<br/><span>(Category Manager)</span></div></th>
													</c:if>
													<c:if test="${model.detailValueObjs != ' ' && model.detailValueObjs !=null && model.detailValueObjs !='[]' }">
                                                       <th id="tblheader"><div style="position: absolute;text-align: -webkit-center; margin-left: -9px;width:29%;top: 44px;border-left: 1px solid #ddd;line-height: 42px;background:#5BC0DE;color:#fff;height: 48px;">Product Hierarchy</div></th>
                                                       <th id="tblheader"><div style="position: absolute;text-align: -webkit-center;width:16%;top: 44px;border-left: 1px solid #ddd;margin-left: -9px;line-height: 42px;background:#5BC0DE;color:#fff;height: 48px;">Manager Name</div></th>
                                                       <th id="tblheader"><div style="position: absolute;width: 17%;text-align: -webkit-center; margin-left: -9px;top: 44px;border-left: 1px solid #ddd;line-height: 42px;background:#5BC0DE;color:#fff;height: 48px;">Status</div></th>
                                                       <th id="tblheader"><div style="position: absolute;width: 19%;text-align: -webkit-center; margin-left: -9px;top: 44px;border-left: 1px solid #ddd;background:#5BC0DE;color:#fff; line-height: 21px;height: 48px;">Override Flag<br/><span>(Product Manager)</span></div></th>
                                                       <th id="tblheader"><div style="position: absolute;width: 19.5%;text-align: -webkit-center; margin-left: -9px;top: 44px;border-left: 1px solid #ddd;background:#5BC0DE;color:#fff;line-height: 21px;height: 48px;">Override Flag<br/><span>(Category Manager)</span></div></th>
                                                    </c:if>
                                                <%}else{ %>
                                               		<c:if test="${model.detailValueObjs == ' ' ||  model.detailValueObjs ==null || model.detailValueObjs=='[]' }">
                                               			<th id="tblheader"><div style="position: absolute;text-align: -webkit-center; margin-left: -9px;width:16.5%;top: 49px;border-left: 1px solid #ddd;line-height: 42px;background:#5BC0DE;color:#fff;height: 48px;"><input type="checkbox" id="selectAll"></div></th>
                                                       <th id="tblheader"><div style="position: absolute;text-align: -webkit-center; margin-left: -9px;width:16.5%;top: 49px;border-left: 1px solid #ddd;line-height: 42px;background:#5BC0DE;color:#fff;height: 48px;">Product Hierarchy</div></th>
                                                       <th id="tblheader"><div style="position: absolute;width: 16.5%;text-align: -webkit-center; margin-left: -9px;top: 49px;border-left: 1px solid #ddd;line-height: 42px;background:#5BC0DE;color:#fff;height: 48px;">Status</div></th>
                                                       <th id="tblheader"><div style="position: absolute;width: 16.5%;text-align: -webkit-center; margin-left: -9px;top: 49px;border-left: 1px solid #ddd;background:#5BC0DE; line-height: 21px;color:#fff;height: 48px;">Override Flag<br/><span>(Product Manager)</span></div></th>
                                                       <th id="tblheader"><div style="position: absolute;width: 16.5%;text-align: -webkit-center; margin-left: -9px;top: 49px;border-left: 1px solid #ddd;background:#5BC0DE;line-height: 21px;color:#fff;height: 48px;">Override Flag<br/><span>(Category Manager)</span></div></th>
                                                       <th id="tblheader"><div style="position: absolute;width: 16.5%;text-align: -webkit-center; margin-left: -9px;top: 49px;border-left: 1px solid #ddd;background:#5BC0DE;line-height: 42px;color:#fff;height: 48px;">Commit</div></th>
                                               		
                                               		</c:if>
                                               		<c:if test="${model.detailValueObjs != ' ' && model.detailValueObjs !=null && model.detailValueObjs !='[]' }">
                                               			<th id="tblheader"><div style="position: absolute;text-align: -webkit-center; margin-left: -9px;width:5%;top: 49px;border-left: 1px solid #ddd;line-height: 42px;background:#5BC0DE;color:#fff;height: 48px;"><input type="checkbox" id="selectAll"></div></th>
                                                       <th id="tblheader"><div style="position: absolute;text-align: -webkit-center; margin-left: -9px;width:28%;top: 49px;border-left: 1px solid #ddd;line-height: 42px;background:#5BC0DE;color:#fff;height: 48px;">Product Hierarchy</div></th>
                                                       <th id="tblheader"><div style="position: absolute;width: 17%;text-align: -webkit-center; margin-left: -9px;top: 49px;border-left: 1px solid #ddd;line-height: 42px;background:#5BC0DE;color:#fff;height: 48px;">Status</div></th>
                                                       <th id="tblheader"><div style="position: absolute;width: 19%;text-align: -webkit-center; margin-left: -9px;top: 49px;border-left: 1px solid #ddd;background:#5BC0DE; line-height: 21px;color:#fff;height: 48px;">Override Flag<br/><span>(Product Manager)</span></div></th>
                                                       <th id="tblheader"><div style="position: absolute;width: 19.5%;text-align: -webkit-center; margin-left: -9px;top: 49px;border-left: 1px solid #ddd;background:#5BC0DE;line-height: 21px;color:#fff;height: 48px;">Override Flag<br/><span>(Category Manager)</span></div></th>
                                                       <th id="tblheader"><div style="position: absolute;width: 13.2%;text-align: -webkit-center; margin-left: -9px;top: 49px;border-left: 1px solid #ddd;background:#5BC0DE;line-height: 42px;color:#fff;height: 48px;">Commit</div></th>
                                               		</c:if>
                                                       
                                                <%} %>
											</tr>	
										</thead>
										<tbody>
										<%-- <h1>${model.detailValueObjs}</h1> --%>
											<c:forEach var="modelObject" items="${model.detailValueObjs}" varStatus="loop">
												<c:if test="${modelObject.category != ' ' &&  modelObject.category !=null}">
													<c:set var="categoryValue" value="${modelObject.category}" scope="page"/>
													<tr style="background-color: rgba(62, 82, 22, 0.27)">
													<%if(roles != null && !roles.contains(ApplicationConstants.ADMIN)){ %>
														    <%if(roles != null && (!roles.contains(ApplicationConstants.PRODUCT_MANAGER))){ %>
														<c:choose>
															<c:when test="${modelObject.commitcategoryFlag == '1'}">
																<td valign="middle" width="5%" style="    text-align: -webkit-center;    vertical-align: middle;">
																	<input type="checkbox" id="${categoryValue},categoryCheckBox">
																</td>
															</c:when>
															<c:otherwise>
																<td valign="middle" width="5%" style="    text-align: -webkit-center;    vertical-align: middle;">
																	<input type="checkbox" id="locked" disabled="disabled">
																</td>
															</c:otherwise>
														</c:choose>
														<%}else{%>
															<td ></td>
														<%}%>
														<%}%>
														<td width="28%"><label style="border:none;background:transparent;margin-left:5%;margin-top:5%"><a  href="#" onclick="onClickPlanningLog('<%=categoryColumn%>','CATEGORY','${modelObject.category}')">Category: ${modelObject.category}</a></label></td>
														<%if(roles != null && roles.contains(ApplicationConstants.ADMIN)){ %>
															<td width="15.4%"><label style="border:none;background:transparent;margin-left:20%;margin-top:5%">--</label></td>
														<%} %>
														<td width="16.6%"><label style="border:none;background:transparent;margin-left:20%;margin-top:5%">${modelObject.categoryStatus}</label></td>
														<td width="18.1%"><label style="border:none;background:transparent;margin-left:20%;margin-top:5%">--</label></td>
													    <td width="19.1%"><label style="border:none;background:transparent;margin-left:20%;margin-top:5%">
													    <c:if test="${modelObject.categoryManagerOverrideCategoryLevel=='Yes'}">
																<font color="red">${modelObject.categoryManagerOverrideCategoryLevel}</font>
															</c:if>
															<c:if test="${modelObject.categoryManagerOverrideCategoryLevel=='No'}">
																<font color="grey">${modelObject.categoryManagerOverrideCategoryLevel}</font>
															</c:if>
													    </label></td>
													    <%if(roles != null && !roles.contains(ApplicationConstants.ADMIN)){ %>
														    <%if(roles != null && (!roles.contains(ApplicationConstants.PRODUCT_MANAGER))){ %>
															<c:choose>
																<c:when test="${modelObject.commitcategoryFlag == '1'}">
																	<td ><button type="button" id="cat${loop.index}"  style="border:none;background:transparent"  value="1" disabled ><img src="./images/commit_green.png"/></button></td>
																</c:when>
																<c:otherwise>
																	<td ><button id="cat${loop.index}" style="border:none;background:transparent"   value="0"  disabled><img src="./images/commit_grey.png"/></button></td>
																</c:otherwise>
															</c:choose>
															<%}else{%>
																<td ></td>
															<%}%>
														<%}%>
													</tr>
												</c:if>
												<c:if test="${modelObject.model !=' ' && modelObject.model !=null}">
													<c:set var="modelValue" value="${modelObject.model}" scope="page"/>
													<tr style="background-color: #EBEBEF">
													<%if(roles != null && !roles.contains(ApplicationConstants.ADMIN)){ %>
														<c:choose>
															<c:when test="${modelObject.commitModelFlag == '1'}">
																<td valign="middle" >
																	<input type="checkbox" id="${categoryValue}_${modelValue},modelCheckBox">
																</td>
															</c:when>
															<c:otherwise>
																<td valign="middle" >
																	<input type="checkbox" id="locked" disabled="disabled">
																</td>
															</c:otherwise>
														</c:choose>
													<%} %>

														<td width="28%"><label style="border:none;background:transparent;margin-left:20%;margin-top:5%"><a href="#" onclick="onClickPlanningLog('<%=modelName%>','MODEL','${modelObject.model}')">Model: ${modelObject.model}</a></label></td>

														<%if(roles != null && roles.contains(ApplicationConstants.ADMIN)){ %>
															<td width="15.4%"><label style="border:none;background:transparent;margin-left:20%;margin-top:5%">${modelObject.userName}</label></td>
														<%}%>
														<td width="16.6%"><label style="border:none;background:transparent;margin-left:20%;margin-top:5%">${modelObject.modelStatus}</label></td>
														<td width="18.1%">
														<label style="border:none;background:transparent;margin-left:20%;margin-top:5%">
															<c:if test="${modelObject.productManagerOverrideModelLevel=='Yes'}">
																<font color="red">${modelObject.productManagerOverrideModelLevel}</font>
															</c:if>
															<c:if test="${modelObject.productManagerOverrideModelLevel=='No'}">
																<font color="grey">${modelObject.productManagerOverrideModelLevel}</font>
															</c:if>
														</label>
														</td>
														<td width="19.1%"><label style="border:none;background:transparent;margin-left:20%;margin-top:5%">
														<c:if test="${modelObject.categoryManagerOverrideModelLevel=='Yes'}">
															<font color="red">${modelObject.categoryManagerOverrideModelLevel}</font>
														</c:if>
														<c:if test="${modelObject.categoryManagerOverrideModelLevel=='No'}">
															<font color="grey">${modelObject.categoryManagerOverrideModelLevel}</font>
														</c:if>
														</label></td>
														 <%if(roles != null && !roles.contains(ApplicationConstants.ADMIN)){ %>
															<c:choose>
																<c:when test="${modelObject.commitModelFlag == '1'}">
																	<td ><button type="button"id="mdl${loop.index}"  style="border:none;background:transparent" value="1" disabled><img src="./images/commit_green.png"/></button></td>
																</c:when>
																<c:otherwise>
																	<td ><button id="mdl${loop.index}" style="border:none;background:transparent"   value="0"  disabled><img src="./images/commit_grey.png"/></button></td>
																</c:otherwise>
															</c:choose>
														<%} %>
													</tr>
												</c:if>
												<tr>
												<%if(roles != null && !roles.contains(ApplicationConstants.ADMIN)){ %>
													<c:choose>
														<c:when test="${modelObject.commitFlag == '1'}">
															<td valign="middle" >
																<form:checkbox path="editFlagArray" value="${loop.index}" id="${categoryValue}_${modelValue}" />
															</td>
														</c:when>
														<c:otherwise>
															<td valign="middle" >
																<form:checkbox path="editFlagArray" value="${loop.index}" disabled="true" id="locked"/>
															</td>
														</c:otherwise>
													</c:choose>
													<%} %>

													 <td width="28%"><label style="border:none;background:transparent;margin-left:30%;margin-top:5%" class="centerHeading"><a href="#" onclick="onClickSku('','PRODUCT','${modelObject.sku}','${modelObject.planogram}','${modelObject.productDescription}','${modelObject.productHierarchyII}')"  title="${modelObject.productDescription}">${modelObject.toolTipProductDescription}<br/>(${modelObject.sku})</a></label></td>

													 <%if(roles != null && roles.contains(ApplicationConstants.ADMIN)){ %>
													 	<td width="15.4%"><label style="border:none;background:transparent;margin-left:20%;margin-top:5%">${modelObject.userName}</label></td>
													 <%} %>
													 <td width="16.6%"><label style="border:none;background:transparent;margin-left:20%;margin-top:5%">${modelObject.status}</label></td>
													 <td width="18.1%"><label style="border:none;background:transparent;margin-left:20%;margin-top:5%">
													 <c:if test="${modelObject.productManagerOverride=='Yes'}">
															<font color="red">${modelObject.productManagerOverride}</font>
														</c:if>
														<c:if test="${modelObject.productManagerOverride=='No'}">
															<font color="grey">${modelObject.productManagerOverride}</font>
														</c:if>
													 </label></td>
													 <td width="19.1%"><label style="border:none;background:transparent;margin-left:20%;margin-top:5%">
													 <c:if test="${modelObject.categoryManagerOverride=='Yes'}">
															<font color="red">${modelObject.categoryManagerOverride}</font>
														</c:if>
														<c:if test="${modelObject.categoryManagerOverride=='No'}">
															<font color="grey">${modelObject.categoryManagerOverride}</font>
														</c:if>
													 </label></td>
													  <%if(roles != null && !roles.contains(ApplicationConstants.ADMIN)){ %>
														<c:choose>
															<c:when test="${modelObject.commitFlag == '1'}">
																<td ><button id="sbmt${loop.index}"  style="border:none;background:transparent"  value="1" disabled ><img src="./images/commit_green.png"/></button></td>
															</c:when>
															<c:otherwise>
																<td ><button id="sbmt${loop.index}" style="border:none;background:transparent"   value="0" disabled><img src="./images/commit_grey.png"/></button></td>
															</c:otherwise>
														</c:choose>
													<%} %>
												</tr>
											</c:forEach>
									 	</tbody>
									</table>
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
<script type="text/javascript">
jQuery(document).ready(function($) {
	//check all and checknone
	  $('#selectAll').click(function() {
	      if(this.checked){
		      var selectNoneButton = $('#selectNoneButton');
		      var checkboxes = $('input:checkbox[id!="locked"]');
		      checkboxes.prop('checked', true);
	      }else{
	    	  var selectNoneButton = $('#selectNoneButton');
		      var checkboxes = $('input:checkbox');
		      checkboxes.prop('checked', false);  
	      }
	  });
	
	  $("[id$=categoryCheckBox]").click(function() {
		  var idStr = this.id;
		     var idArry = idStr.split(',');
		     var categoryName = idArry[0];
		     var checkboxes =  $("input:checkbox[id*='"+categoryName+"']");
		     if(this.checked){
		    	 checkboxes.prop('checked', true);	 
		     }else{
		    	 checkboxes.prop('checked', false);
		     }
	  });
	  
	  $("[id$=modelCheckBox]").click(function() {
	     var idStr = this.id;
	     var idArry = idStr.split(',');
	     var categoryModelName = idArry[0];
	     var checkboxes =  $("input:checkbox[id='"+categoryModelName+"']");
	     if(this.checked){
	    	 checkboxes.prop('checked', true);	 
	     }else{
	    	 checkboxes.prop('checked', false);
	     }
	  });
 });
</script>