<!DOCTYPE html>
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

<style>
.margin {
	margin:0px 30px 30px 30px;
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

	function flatFileExportExcel() {
		loadProgress();
		categoryLevelSummaryBeanForm.action = "flatFileExportExcel.htm";
		categoryLevelSummaryBeanForm.submit();
	}
</script>

<body>
<form:form method="post" action="overrideFreeze.htm" commandName="model" name="categoryLevelSummaryBeanForm">
	<div id="wrapper">
		<div id="page-wrapper" style="  padding: 15px 18px;">
			<div id="page-inner">
				 <div class="row" style="margin-top:-1%" >
                    <div class="col-md-12">
                        <div class="panel panel-default" style="height:auto;width:103%;margin-left:-1.2%">
							<div class="panel-heading" style="background-color:#2DAFCB;font-size:130%;height:52px;padding:7px 15px;color:#fff;padding-top:1.5%  ">
								&nbsp;Freeze Planning Cycle
								<div class="pull-right" style="text-align: right;">
									<a href="homePage.htm" ><img src="./images/back2.png" title="Go Back" style="width: 59%; height: 30%;
  									vertical-align: -webkit-baseline-middle; margin-top: -17px;"/></a>
								</div>
							</div>
							<div class="panel-body" style="width:100%">
								<div class="margin">
								<img src="./images/commit_green.png"/> <font size="3;">- Committed</font> 
								<img style = "padding-left:40px;" src="./images/commit_grey.png"/><font size="3;">- Not Committed</font>
								<input id="planningCycleId" name="planningCycleId" value="${model.planningCycleId}" type="hidden" />
								<c:choose>
								
										<c:when test="${model.workFlowStatus == 'APPROVE'}">
											<button style="float:right;margin-bottom:20px" class="btn btn-success btn-sm"  type="submit"  >Freeze Planning Cycle</button>
										</c:when>
										<c:otherwise>
											<button style="float:right;margin-bottom:20px" class="disabled btn btn-danger btn-sm" type="submit" >Freeze Planning Cycle</button>
										</c:otherwise>
									</c:choose>
									<table class="table table-bordered table-striped" style ="padding-top:10px;">
										<thead id="tblheader">
											<tr>
												<th id="tblheader" class="centerHeading">Category</th>
												<th id="tblheader" class="centerHeading">User</th>
												<th id="tblheader" class="centerHeading">Commit Status</th>
											</tr>
										</thead>
										 <tbody>
											<c:forEach var="modelObject" items="${model.categoryStatusList}" varStatus="outerloop">
												<tr>
													 <c:forEach var="data" items="${modelObject}" varStatus="innerloop">
															<c:choose>
																<c:when test="${innerloop.index == 4}">
																	 <c:choose>
																		<c:when test="${data == '1'}">
																			<td class="centerText"><img src="./images/commit_green.png"/></td>
																		</c:when>
																		<c:otherwise>
																			<td class="centerText"><img src="./images/commit_grey.png"/></td>
																		</c:otherwise>
																	</c:choose>
																</c:when>
																<c:when test="${innerloop.index == 2}">
																</c:when>
																<c:when test="${innerloop.index == 3}">
																</c:when>
																<c:otherwise>
																		<td class="centerText"><label style="margin: 15px">${data}</label></td>	
																</c:otherwise>
															</c:choose>
													</c:forEach>  
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
