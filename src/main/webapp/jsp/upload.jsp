<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="<%=contextPath%>/css/bridgei2i.css"
	type="text/css">
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<title>Upload</title>

</head>
<script type="text/javascript">
	function uploadData() {
		loadProgress();
		uploadDataForm.action = "uploadData.htm";
		uploadDataForm.submit();
	}
	
	function teamReportTaskExecutor() {
		loadProgress();
		uploadDataForm.action = "teamReportTaskExecutor.htm";
		uploadDataForm.submit();
	}
	
	function setupDistributionUsers() {
		loadProgress();
		uploadDataForm.action = "setupDistributionUsers.htm";
		uploadDataForm.submit();
	}
	
</script>
<body>
	<form:form method="post" action="uploadData.htm" name="uploadDataForm"
		commandName="model" enctype="multipart/form-data">
		<center>
			<div align="center"
				style="width: 800px; height: 100px; border: 5px; margin-top: 100px; margin-left: 150px">
				<div style="margin-left: -125px">
					<div class="panel panel-info">
						<div class="panel-heading" style="text-align: left;">Upload Data</div>
						<div class="panel-body">
							<table border="0px" style="cellpadding:10px">
								<tr>
									<td>Upload Survey Data&nbsp;&nbsp;</td>
									<td>
										<div class="form-group">
											<input type="file" name="surveyData">
										</div>
									</td>
								</tr>
								<tr>
									<td>Upload code book&nbsp;&nbsp;</td>
									<td>
										<div class="form-group">
											<input name="codeBookData" id="codeBookData" type="file">
										</div>
									</td>
								</tr>
								<tr>
									<td colspan="2" align="center"><br> <input
										type="button" value="submit" class="btn btn-info"
										onClick="uploadData();" /></td>

								</tr>
							</table>

						</div>
						
					</div>
					<input type="button" value="Generate Team Report" class="btn btn-info"	onClick="teamReportTaskExecutor();" />
					<input type="button" id="Setup Users" value="Setup Distribution Users" class="btn btn-info"	onClick="setupDistributionUsers();" />
				</div>
			</div>
		</center>
	</form:form>
</body>
</html>