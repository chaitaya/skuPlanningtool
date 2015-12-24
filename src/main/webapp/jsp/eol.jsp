<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false"%>

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>SKU Planning Log</title>

      
      <link href="css/bootstrap.css" rel="stylesheet" />
    <link href="css/font-awesome.css" rel="stylesheet" />
	<link href="css/custom-styles.css" rel="stylesheet" />
	<script src="js/tabcontent.js" type="text/javascript"></script>
	<link href="css/tabcontent.css" rel="stylesheet" type="text/css" />
	<script src="js/jquery-1.10.2.js"></script>
     <script src="js/jquery.metisMenu.js"></script>
        <!-- Morris Chart Js -->
     <script src="js/plugins/morris/raphael-2.1.0.min.js"></script>
    <script src="js/plugins/morris/morris.js"></script>
    <link href="/css/plugins/dataTables.bootstrap.css" rel="stylesheet">
    <script src="js/plugins/dataTables/jquery.dataTables.js"></script>
    <script src="js/plugins/dataTables/dataTables.bootstrap.js"></script>


<style>
.margin {
	margin: 30px;
}

td>a {
	color: #369AB2;
}
</style>

<script type="text/javascript">

function eolPopUp(id,productNumber,productDescription,business,phI,phII,phIV){
	document.getElementById("id").value =id;
	document.getElementById("ProductNumber").innerHTML =productNumber;
	document.getElementById("ProductDescription").innerHTML =productDescription;
	document.getElementById("Bussiness").innerHTML =business;
	document.getElementById("PhI").innerHTML =phI;
	document.getElementById("PhII").innerHTML =phII;
	document.getElementById("PhIV").innerHTML =phIV;
	document.getElementById("ProductNumberValue").value =id;
}

function updateEOL(){
	planningLogBeanForm.action = "updateEolData.htm";
	planningLogBeanForm.submit();
}
		
</script>
	<form:form method="post" action="planningLog.htm" commandName="model" name="planningLogBeanForm" enctype="multipart/form-data">
		<%-- <form:hidden id="planningCycleId" path="planningCycleId"/> --%>
		<!-- <input id="type" name="type" type="hidden" />
		<input id="selectedTypeValue" name="selectedTypeValue" type="hidden" />
		<input id="planogram" name="planogram" type="hidden" />
		<input id="productDescription" name="productDescription" type="hidden" />
		<input id="productHierarchyII" name="productHierarchyII" type="hidden" /> -->
 
		<div id="wrapper">
			<div id="page-wrapper" style="  min-height: 500px; height: 700px;  padding: 15px 16px;">
				<div id="page-inner" style="min-height: 500px;">
					<div class="row" style="margin-top: -1%">
						<div class="col-md-12">
							<div class="panel panel-default"
								style="height: auto; width: 103%; margin-left: -1.2%">
								<div class="panel-heading"
									style="background-color: #2DAFCB; font-size: 130%; height: 52px; padding: 7px 15px; color: #fff; padding-top: 1.5%">
									&nbsp;<strong><b>End of Life</b></strong>
									<!-- <div class="pull-right" style="text-align: right;">
										<a href="home.htm"><img src="./images/back2.png"
											title="Go Back"
											style="width: 59%; height: 30%; vertical-align: -webkit-baseline-middle; margin-top: -17px;" /></a>
									</div> -->
								</div>
								<div class="panel-body" style="width: 100%">
									<div class="table-responsive" id ="tableScroll" style="height: 520px;overflow-x: hidden;overflow-y: auto;">
										<!-- <div style="height: 450px;overflow: auto"> -->
										<table class="table table-striped table-bordered table-hover" id="dataTables-example">
											<thead>
												<tr>
												    <th>Product NO</th>
													<th width="28%">Product Description</th>
													<th>Business</th>
													<th>PH I</th>
													<th>PH2 II</th>
													<th>PH IV</th>
													<th>EOL</th>
													<th>Edit</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach var="modelObject" items="${model.eolList}"
													varStatus="loop">

													<tr class="gradeA">
													    <td >${modelObject.c9}</td>
														<td title="${modelObject.c11}" width="28%">${modelObject.toolTipProductDescription}</td>
														<td >${modelObject.c28}</td>
														<td >${modelObject.c6}</td>
														<td >${modelObject.c7}</td>
														<td >${modelObject.c19}</td>
														<td >${modelObject.combinedEOL}</td>
														
													<td id="editSKu"><button type="button"
															style="background: transparent; border: none"  data-target="#eolValue"
															data-toggle="modal"
															onClick="eolPopUp('${modelObject.id}','${modelObject.c9}','${modelObject.c11}','${modelObject.c28}','${modelObject.c6}','${modelObject.c7}','${modelObject.c19}');">
															<img alt="edit" style="width: 20%"
																src="images/eol_edit.png" />
														</button></td>
												</tr>
												</c:forEach>
											</tbody>
										</table>
										<!-- </div> -->
										<input id="id" name="dataId" type="hidden" /> 
										
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

 
		<!-- POP UP -->
<div id="eolValue" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">End of Life</h4>
      </div>
      <div class="modal-body">
		<div class="row">
		  <div class="col-md-4"><label >Product Number:</label>
				<form:hidden id="ProductNumberValue" path="c9"  />
		  </div>
		  <div class="col-md-7"><span id="ProductNumber"></span></div>
		  <div class="col-md-1">&nbsp;</div>
		</div>
		<div class="row">
		  <div class="col-md-4"><label>Product Description:</label></div>
		  <div class="col-md-7"><span id="ProductDescription"></span></div>
		  <div class="col-md-1">&nbsp;</div>
		</div>
		<div class="row">
		  <div class="col-md-4"><label>Business:</label></div>
		  <div class="col-md-7"><span id="Bussiness"></span></div>
		  <div class="col-md-1">&nbsp;</div>
		</div>
		<div class="row">
		  <div class="col-md-4"><label>Product Hierarchy I:</label></div>
		  <div class="col-md-7"><span id="PhI"></span></div>
		  <div class="col-md-1">&nbsp;</div>
		</div>
		<div class="row">
		  <div class="col-md-4"><label>Product Hierarchy II:</label></div>
		  <div class="col-md-7"><span id="PhII"></span></div>
		  <div class="col-md-1">&nbsp;</div>
		</div>
		<div class="row">
		  <div class="col-md-4"><label>Product Hierarchy IV:</label></div>
		  <div class="col-md-7"><span id="PhIV"></span></div>
		  <div class="col-md-1">&nbsp;</div>
		</div>
		<div class="row">
		  <div class="col-md-3"> <label>End of Life:</label></div>
		  <div class="col-md-5"><font style="color:red;font-size:18px;">*</font>Week:</label>
							 	<form:select path="eolWeek" multiple="false" >
							 	<form:option value="NONE" label="Select" />
										 <%
									      for(int i=26;i<53;i++) {
									           int Field=i;
									     %>
									  <form:option value="<%=Field %>"><%=Field %></form:option>
									        <%}
									 %>
									  
			               		 </form:select></div>
		  <div class="col-md-4"><label>&nbsp Year:</label>
									  <form:select path="eolYear" multiple="false" >
									  <form:option value="NONE" label="Select" />							  
									  <%
									      for(int i=2015;i<2018;i++) {
									           int Field=i;
									        %>
											 <form:option value="<%=Field %>"><%=Field%></form:option>			
 							        <%} %>
								</form:select></div>
		</div>
		<div class="row">
		  <div class="col-md-12"> <label style="color: blue;margin-left: 145px;font-family:sego UI; " ><font style="color:red;font-size:18px;">*</font>Please select start of week</label></div>
		</div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button  id="modalSubmit" class="btn btn-primary btn-md" data-dismiss="modal" >Save Changes</button>
      </div>
    </div>
  </div>
</div>		 <!-- pop up -->
	</form:form>
	<script type="text/javascript">
$( document ).ready(function() {
		$("#modalSubmit").attr("onclick","updateEOL()");
	
});
</script>


