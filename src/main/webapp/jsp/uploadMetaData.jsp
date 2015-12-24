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
	function uploadMetaData() {
		removeProgress();
		loadProgress();
		uploadMetaDataForm.action = "uploadMetaDataProcess.htm";
		uploadMetaDataForm.submit();
		removeProgress();
	}
	function generateBannerTable() {
		removeProgress();
		loadProgress();
		uploadMetaDataForm.action = "generateBannerTable.htm";
		uploadMetaDataForm.submit();
		removeProgress();
	}

	function downloadBannerTable() {
		removeProgress();
		loadProgress();
		uploadMetaDataForm.action = "downloadBannerTable.htm";
		uploadMetaDataForm.submit();
		removeProgress();
	}
	
	function downloadBannerTable2() {
		removeProgress();
		loadProgress();
		uploadMetaDataForm.action = "downloadBannerTable2.htm";
		uploadMetaDataForm.submit();
		removeProgress();
	}
	
</script>
<body>
	<form:form method="post" action="uploadMetaData.htm" name="uploadMetaDataForm"
		commandName="model" enctype="multipart/form-data">
		<center>
			<div align="center"
				style="width: 800px; height: 100px; border: 5px; margin-top: 100px; margin-left: 150px">
				<div style="margin-left: -125px">
					<div class="panel panel-info">
						<div class="panel-heading" style="text-align: left;">Upload Meta Data</div>
						<div class="panel-body">
							<table border="0px" style="cellpadding:10px">
								<tr>
									<td>Upload Meta Data&nbsp;&nbsp;</td>
									<td>
										<div class="form-group">
											<input type="file" name="metaData">
										</div>
									</td>
								</tr>
								<tr>
									<td colspan="2" align="center"><br> <input
										type="button" value="submit" class="btn btn-info"
										onClick="uploadMetaData();" />
										<input type="button" value="Generate Banner Table" class="btn btn-info" onClick="generateBannerTable();"/>
										<input type="button" value="Download Banner Table" class="btn btn-info" onClick="downloadBannerTable();"/>
										<input type="button" value="Download Banner Table 2" class="btn btn-info" onClick="downloadBannerTable2();"/>
										</td>
										<td>
										<th width="5%" style="text-align: center;">
										<input type="checkbox" name="percentage" />PERCENTAGE</th>
								</tr>
								
							</table>

						</div>
					</div>
				</div>
			</div>
		</center>
	</form:form>
</body>
</html>