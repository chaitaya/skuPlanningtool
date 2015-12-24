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
	<script src="<%=contextPath %>/js/bootstrap-filestyle.js" type="text/javascript"></script>
	<title>Run forecast</title>
	
	<link href="css/bootstrap.css" rel="stylesheet" />
    <link href="css/font-awesome.css" rel="stylesheet" />
    <link href="js/plugins/morris/morris-0.4.3.min.css" rel="stylesheet" />
    <link href="css/custom-styles.css" rel="stylesheet" />
	
</head>
<body>
	<div id="wrapper">
        <div id="page-wrapper" >
          <div id="page-inner" style="background-color:#fff">
				<form:form method="post" action="uploadData.htm" name="uploadDataForm"
					commandName="model" enctype="multipart/form-data">
					<center>
						<div align="center" style="width: 53%; height: 100%; border: 5px; margin-left: 10%;margin-top:5%;background-color:#fff">
							<div style="margin-left: -125px">
								
								<div class="panel panel-info">
									<div class="panel-heading" style="">Upload Planning Cycle Data</div>
									<div class="panel-body">
										<img src="<%=contextPath%>/images/p2.png"  alt="progressBar" style="margin-top:3%;margin-bottom:3%"/>
										 <table>
											 <tr >
												<td >
														<a href="uploadData.htm">
															<input  type="button" value="Previous" class="btn btn-info"  />
														</a>
												</td>
												<td >
														<a href="homePage.htm">
															<input  type="button" value="Run forecast" class="btn btn-info"  />
														</a>
												</td>
											</tr>
											
										</table>
										</div>
									</div>
								</div>
							</div>
						</div>
					</center>
				</form:form>
			</div>
		</div>
	</div>
</body>
</html>