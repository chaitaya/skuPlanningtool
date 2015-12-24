<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/bridgei2i.tld" prefix="bi2itag"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.bridgei2i.common.util.ApplicationUtil"%>
<%@page import="com.bridgei2i.common.constants.ApplicationConstants"%>
<%@page import="com.bridgei2i.common.vo.Users"%>
<%
	String contextPath = request.getContextPath();
	Users users = (Users) ApplicationUtil.getObjectFromSession(
			ApplicationConstants.APPLICATION_LOGGED_IN_USER, request);
	List roles = users.getRolesList();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>

<link rel="stylesheet" href="<%=contextPath%>/css/bridgei2i.css"
	type="text/css">
<script type="text/javascript">
	var check = 0;
	function publishReport() {
		var publishReport = document.getElementById("Publish Report");
		var obj = document.getElementsByName("editFlagArray");
		var isChecked = false;
		for ( var i = 0; i < obj.length; i++) {
			if (obj[i].checked) {
				isChecked = true;
				var statusVal = document.getElementById("statusNames" + i).value;
				if (statusVal == "Published") {
					alert("Already Published");
					break;
				}
				if (statusVal != "Approved") {
					alert("Select only approved report to publish");
					break;
				} else {
					loadProgress();
					reportTemplateListForm.action = "publishReport.htm";
					publishReport.disabled = true;
					reportTemplateListForm.submit();
				}
			}
		}
		if (!isChecked)
			alert("Please select the template report");
	}
	
	function preview(indexValue) {
		removeProgress();
		indexValue.onclick = function(event) {
			e.preventDefault();
		}
		loadProgress();
		reportTemplateListForm.action = "previewTemplate.htm";
		reportTemplateListForm.submit();
	}

	function newReportTemplate() {
		removeProgress();
		var newTemplate = document.getElementById("New Template");
		loadProgress();
		reportTemplateListForm.action = "newReportTemplate.htm";
		newTemplate.disabled = true;
		reportTemplateListForm.submit();
	}

	function deleteTemplate() {
		removeProgress();
		var obj = document.getElementsByName("editFlagArray");
		var deleteTemplate = document.getElementById("Delete Template");
		var isChecked = false;
		var count = 0;
		for ( var i = 0; i < obj.length; i++) {
			if (obj[i].checked) {
				isChecked = true;
				count++;
			}
		}
		if (isChecked == true) {
			var returnObj = confirm("Are you sure you want to delete?");
			if (returnObj) {
				loadProgress();
				reportTemplateListForm.action = "deleteTemplate.htm";
				deleteTemplate.disabled = true;
				reportTemplateListForm.submit();
			}

		}
		if (!isChecked)
			alert("Please select the template report");
	}

	function editTemplate() {
		removeProgress();
		var obj = document.getElementsByName("editFlagArray");
		var editTemplate = document.getElementById("Edit Template");
		var isChecked = false;
		var count = 0;
		for ( var i = 0; i < obj.length; i++) {
			if (obj[i].checked) {
				isChecked = true;
				count++;
			}
		}
		if (count > 1) {
			alert("Please select only one template report");
		} else if (isChecked == true) {
			loadProgress();
			reportTemplateListForm.action = "editTemplate.htm";
			editTemplate.disabled = true;
			reportTemplateListForm.submit();
		}
		if (!isChecked)
			alert("Please select the template report");
	}

	function copyTemplate() {
		removeProgress();
		var obj = document.getElementsByName("editFlagArray");
		var isChecked = false;
		var count = 0;
		for ( var i = 0; i < obj.length; i++) {
			if (obj[i].checked) {
				isChecked = true;
				count++;
			}
		}
		if (count > 1) {
			alert("Please select only one template report");
		} else if (isChecked == true) {
			//var returnObj = window.showModalDialog("popUpReportTitle.htm?isFromReportTitle=true", null,
			//"dialogWidth:300px; dialogHeight:150px; center:yes");
			var returnObj = document.getElementById("textId").value;
			if (returnObj == "") {
				$("#copyCategory").modal('show');
			}
		}
		if (!isChecked) {
			alert("Please select the template report.");
		}
	}

	function OK() {
		var obj = document.getElementsByName("editFlagArray");
		var returnObj = document.getElementById("textId").value;
		if (returnObj != "") {
			var categoryNameObj = document.getElementById('reportTitle');
			categoryNameObj.value = returnObj;
			var calc = 0;
			for ( var j = 0; j < obj.length; j++) {
				var reportName = document.getElementById("reportTitle" + j).value;
				if (reportName == categoryNameObj.value) {
					calc++;
				}
			}
			if (calc == 1) {
				alert("Name already exists");
				document.getElementById("textId").value = "";
			}
			if (calc == 0) {
				removeProgress();
				loadProgress();
				reportTemplateListForm.action = "copyTemplate.htm";
				var copyTemplate = document.getElementById("Copy Template");
				copyTemplate.disabled = true;
				reportTemplateListForm.submit();
			}
		} else {
			$('#textId').data("title", "Please enter report title");
			$('#textId').tooltip('show');
		}
	}

	function generateStandardReport() {
		var obj = document.getElementsByName("editFlagArray");
		var isChecked = false;
		for ( var i = 0; i < obj.length; i++) {
			if (obj[i].checked) {
				isChecked = true;
				var statusVal = document.getElementById("statusNames" + i).value;
				if (statusVal != "Approved") {
					alert("Select only approved report to Generate Standard Reports");
					break;
				} else {
					loadProgress();
					var Obj = document.getElementById('Generate Standard Reports');
					Obj.disabled = true;
					loadProgress();
					reportTemplateListForm.action = "generateStandardReport.htm";
					reportTemplateListForm.submit();
				}
			}
		}
		if (!isChecked)
			alert("Please select the template report.");
	}
	
	function refreshReport() {
		var obj = document.getElementsByName("editFlagArray");
		var isChecked = false;
		for ( var i = 0; i < obj.length; i++) {
			if (obj[i].checked) {
				isChecked = true;
				var statusVal = document.getElementById("statusNames" + i).value;
				
				if (statusVal != "Approved") {
					alert("Select only approved report to Generate Reports");
					break;
				} else {
					loadProgress();
					var Obj = document.getElementById('Generate Reports');
					Obj.disabled = true;
					
					reportTemplateListForm.action = "refreshReportTemplate.htm?index="
							+ check;
					reportTemplateListForm.submit();
				}
			}
		}
		if (!isChecked)
			alert("Please select the template report.");
	}

	function generateReport() {
		var obj = document.getElementsByName("editFlagArray");
		var isChecked = false;
		for ( var i = 0; i < obj.length; i++) {
			if (obj[i].checked) {
				isChecked = true;
				var statusVal = document.getElementById("statusNames" + i).value;
					loadProgress();
					var Obj = document.getElementById('Generate Reports');
					Obj.disabled = true;
					
					reportTemplateListForm.action = "refreshReportTemplate.htm?index="
							+ check;
					reportTemplateListForm.submit();
			}
		}
		if (!isChecked)
			alert("Please select the template report.");
	}
	
	function reviewRequest() {
		removeProgress();
		var Obj = document.getElementById('review');
		var checkIndexObj = document.getElementsByName("editFlagArray");
		var isChecked = false;
		var count = 0;
		for ( var i = 0; i < checkIndexObj.length; i++) {
			if (checkIndexObj[i].checked) {
				isChecked = true;
				count++;
			}
		}
		if (count > 1) {
			alert("Please select only one template report");
			return;
		} else if (isChecked) {
			for ( var i = 0; i < checkIndexObj.length; i++) {
				if (checkIndexObj[i].checked) {
					isChecked = true;
					var statusVal = document.getElementById("statusNames" + i).value;
					var val = document.getElementById("previewId[" + i + "]").value;
					if (statusVal == "Review") {
						alert("The status is already Reviewed");
						break;
					}
					if (statusVal == "Open") {
							$("#generate").modal('show');
					}
					
					if (val == "Preview" && statusVal !="Open") {
						alert("Please generate the report");
					} else {
						/* var returnObj = window.showModalDialog("popUpComments.htm", null,
						"dialogWidth:350px; dialogHeight:150px; center:yes");
						var commentObj=document.getElementById('comments');
						commentObj.value=returnObj.comments;
						loadProgress();
						reportTemplateListForm.action = "reviewRequest.htm";
						Obj.disabled = true;
						reportTemplateListForm.submit(); */ 
					}
				}
			}
		}
		if (!isChecked) {
			alert("Please select the template report.");
			return;
		}
	}
	
	function loadProgressBar() {
		removeProgress();
		loadProgress();
	}
	
	function popUpReview() {
		var returnObj = window.showModalDialog("popUpComments.htm", null,
		"dialogWidth:350px; dialogHeight:150px; center:yes");
		var commentObj=document.getElementById('comments');
		commentObj.value=returnObj.comments;
		loadProgress();
		reportTemplateListForm.action = "reviewRequest.htm";
		reportTemplateListForm.submit(); 
	}
	
	function onChangeCheckBox(index) {
		check = index;
	}

	function checkfocus() {
		var i = document.getElementById("checkedIndex").value;
		document.getElementById("editFlagArray" + i).focus();
		document.getElementById("editFlagArray" + i).blur();
	}
</script>

</head>
<body onLoad="checkfocus();">
	<form:form method="post" action="reportTemplateList.htm"
		commandName="model" name="reportTemplateListForm">
		<form:hidden path="comments" id="comments" />
		<form:hidden path="index" id="checkedIndex" />
		<form:hidden path="reportTitle" id="reportTitle" />

<div class="tabbable tabs-left" style="padding: 0px 5px;">
		<div style="margin-left:15px;margin-top:532px;width:200px;position: fixed;">
							Powered
								by: <br>
								<a href="http://www.bridgei2i.com" target="_blank">
								 <img
								src="<%=contextPath%>/images/bi2i-logo-trans.png" width="50%"
								height="10%" /></a>
					</div>
		<div class="tab-content"
			style="position: relative; width: 85%; float: right;">
			<div id="summary" class="tab-pane active">

				<div id="page-wrapper"	style="height: 648px; overflow-x: auto; margin-left: 2px;margin-right: -18px;min-height: 658px;border: 1.9px solid #ddd;">
					<div class="row" style="margin-top: 60px;">
					<div style="padding-left: 15px"><h1 style="padding-bottom: 5px;border-bottom: 1px solid #eee;color:#A9A9A9 ">List of Reports</h1></div>
						<div class="pull-right" style="padding-right: 12px">
							<%
								if (roles != null && roles.contains("BI2I_ADMIN")) {
							%>
							<input type="button" id="Publish Report" value="Publish Report"
								class="btn btn-primary" onClick="publishReport();"> <input
								type="button" id="New Template" value="New Template"
								class="btn btn-primary" onClick="newReportTemplate();">
							<input type="button" id="Edit Template" name="edit"
								value="Edit Template" class="btn btn-primary"
								onClick="editTemplate();"> <input type="button"
								id="Delete Template" name="del" value="Delete Template"
								class="btn btn-primary" onClick="deleteTemplate();"> <input
								type="button" id="Copy Template" name="copy"
								value="Copy Template" class="btn btn-primary"
								onClick="copyTemplate();">
							<%
								}
							%>
						</div>


					</div>
					<div class="row" style="margin-top: 3px">
						<div class="col-lg-10" style="width: 100%">
							<div class="panel panel-default" style="height: 350px">
								<div class="panel-heading" style="height:40px"> </div>
								<!-- /.panel-heading -->
								<div class="panel-body" style="height: 300px; overflow-x: auto;">
									<div class="table-responsive">

										<table class="table table-striped table-bordered table-hover"
											id="dataTables-example">
											<thead style="text-align: center">
												<tr style="text-align: center">
													<th width="5%" style="text-align: center;"><input
														type="checkbox"></th>
													<th width="20%" style="text-align: center;">NAME</th>
													<th width="20%" style="text-align: center;">STATUS</th>
													<th width="25%" style="text-align: center;">DISTRIBUTION
														NAME</th>
													<%
														if (roles != null
																	&& (roles.contains("BI2I_ADMIN") || roles
																			.contains("HR_ADMIN"))) {
													%>
													<th width="15%" style="text-align: center;">PREVIEW</th>
													<%
														}
													%>
												</tr>
											</thead>
											<tbody>
												<c:forEach var="modelObject"
													items="${model.detailValueObjs}" varStatus="loop">
													<c:if test="${modelObject.deleted==false}">
														<tr class="odd gradeX">
															<%
																if (roles != null && roles.contains("MANAGER")
																					|| roles.contains("BI2I_ADMIN")) {
															%>
															<td width="5%" style="text-align: center;"><form:checkbox
																	path="editFlagArray" value="${loop.index}"
																	onChange="onChangeCheckBox('${loop.index}');" /></td>
															<td width="20%" style="text-align: left;">
																<%
																	if (roles != null && roles.contains("MANAGER")) {
																%> <a
																href="previewTemplate.htm?isFromPreview=false&editFlagArray=${loop.index}" onclick="loadProgressBar(); ">${modelObject.reportTitle}</a>
																<%
																	} else {
																%> ${modelObject.reportTitle} <input
																type="hidden" id="reportTitle${loop.index}"
																value="${modelObject.reportTitle}" /> <%
 	}
 %>
															</td>
															<td width="20%" id="statusName" style="text-align: left;">
																${modelObject.statusName} <input type="hidden"
																id="statusNames${loop.index}"
																value="${modelObject.statusName}" />
															</td>
															<td width="25%" style="text-align: left;">${modelObject.assignReportNames}</td>
															<%
																}
															%>

															<%
																if (roles != null && (roles.contains("HR_ADMIN"))) {
															%>
															<c:if test="${(modelObject.statusName=='Review')}">
																<td width="5%" style="text-align: center;"><form:checkbox
																		path="editFlagArray" value="${loop.index}" /></td>
																<td width="20%" style="text-align: left;">
																	<%
																		if (roles != null && roles.contains("MANAGER")) {
																	%> <a
																	href="previewTemplate.htm?isFromPreview=false&editFlagArray=${loop.index}" onclick="loadProgressBar();">${modelObject.reportTitle}</a>
																	<%
																		} else {
																	%> ${modelObject.reportTitle} <%
 	}
 %>
																</td>
																<td width="20%" id="statusName"
																	style="text-align: left;">
																	${modelObject.statusName} <input type="hidden"
																	id="statusNames${loop.index}"
																	value="${modelObject.statusName}" />
																</td>
																<td width="25%" style="text-align: left;">${modelObject.assignReportNames}</td>
															</c:if>
															<%
																}
															%>


															<%
																if (roles != null && (roles.contains("BI2I_ADMIN"))) {
															%>
															<td width="15%" style="text-align: center;"><c:if
																	test="${modelObject.enablePreview=='Y'}">
																	<a
																		href="previewTemplate.htm?isFromPreview=true&editFlagArray=${loop.index}"
																		id="preview" onclick="preview(this);">Preview</a>
																	<input type="hidden" id="previewId[${loop.index}]"
																		value="" />
																</c:if> <c:if test="${modelObject.enablePreview=='N'}">
                                                                  Preview
                                                                  <input
																		type="hidden" id="previewId[${loop.index}]"
																		value="Preview" />
																</c:if></td>
															<%
																}
															%>
															<%
																if (roles != null && (roles.contains("HR_ADMIN"))) {
															%>
															<c:if test="${(modelObject.statusName=='Review')}">
																<td width="15%" style="text-align: center;"><c:if
																		test="${modelObject.enablePreview=='Y'}">
																		<a
																			href="previewTemplate.htm?isFromPreview=true&editFlagArray=${loop.index}"
																			id="preview" onclick="preview()">Preview</a>
																	</c:if> <c:if test="${modelObject.enablePreview=='N'}">
                                                                  Preview
                                                                  </c:if></td>
															</c:if>
															<%
																}
															%>
														</tr>
													</c:if>
												</c:forEach>
												</tr>
										</table>
									</div>
								</div>
								<!-- /.panel-body -->
							</div>
							<!-- /.panel -->
						</div>
					</div>
					<div class="pull-right">
						<%
							if (roles != null && roles.contains("BI2I_ADMIN")) {
						%>
						<input type="button" id="Generate Standard Reports"
							value="Generate Standard Reports" class="btn btn-primary"
							onClick="generateStandardReport();">
						<input type="button" id="Generate Reports"
							value="Generate Reports" class="btn btn-primary"
							onClick="refreshReport();"> <input type="button"
							id="review" value="Request for review" class="btn btn-primary"
							onClick="reviewRequest();">
						<%
							}
						%>

					</div>
				</div>
			</div>


		</div>
		</div>
	</form:form>
	<div class="modal fade" id="copyCategory" style="overflow-y: auto">
		<div class="modal-dialog modal-sm"
			style="margin-top: 160px; width: 320px">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title" id="myModalLabel">Report Title</h4>
				</div>
				<div class="modal-body">
					<table>
						<tr>
							<td><label>Enter Report Title</label></td>
							<td>&nbsp</td>
							<td><input type="text" id="textId"></td>
						</tr>
					</table>
				</div>
				<div class="modal-footer" style="height: 15px">
					<center>
						<button type="button" style="margin-top: -20px; width: 45px"
							class="btn btn-info btn-xs" onClick="OK();">Ok</button>
						<button type="button" style="margin-top: -20px"
							class="btn btn-info btn-xs" data-dismiss="modal">Cancel</button>
					</center>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade"  id="generate" style="overflow-y:auto">
  <div class="modal-dialog modal-sm" style="margin-top:160px;width:340px">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title" id="myModalLabel">STEP 1</h4>
      </div>
      <div class="modal-body">
       <input
								type="button" id="refresh reports" name="refresh reports"
								value="Generate reports" class="btn btn-primary"
								onClick="generateReport();">
      </div>
      <div class="modal-footer" >
        <input
								type="button" id="next" name="next"
								value="Next>" class="btn btn-primary"
								onClick="popUpReview();">
      </div>
    </div>
  </div>
</div>

</body>
</html>