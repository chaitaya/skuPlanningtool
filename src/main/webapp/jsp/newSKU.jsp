<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.bridgei2i.common.controller.LoadApplicationCacheService"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Unassigned SKU</title>
	<link href="css/bootstrap.css" rel="stylesheet" />
    <link href="css/font-awesome.css" rel="stylesheet" />
	<link href="css/custom-styles.css" rel="stylesheet" />
	<script src="js/tabcontent.js" type="text/javascript"></script>
	<link href="css/tabcontent.css" rel="stylesheet" type="text/css" />
	<link href="/css/plugins/dataTables.bootstrap.css" rel="stylesheet">
    <script src="js/plugins/dataTables/jquery.dataTables.js"></script>
    <script src="js/plugins/dataTables/dataTables.bootstrap.js"></script>
</head>
<script type="text/javascript">
function onChangeAssignedType(){
	utilitiesBeanForm.action="newSku.htm";
	utilitiesBeanForm.submit();
}
</script>
<style>
select,textarea{
width:100%;
}
</style>
<body>
	<form:form method="post"  commandName="model" name="utilitiesBeanForm">
	<div id="wrapper">
		<div id="page-wrapper" style="  min-height: 500px;  padding: 15px 5px;">
			<div id="page-inner" style="  min-height: 500px;">
				 <div class="row">
                    <div class="col-md-12">
                        <div class="panel panel-default">
							<div class="panel-heading" style="background-color:#2DAFCB;font-size:130%;height:52px;padding:7px 15px;color:#fff;padding-top:1.5%;margin-bottom:2% ">
								&nbsp;Assign SKU
								<div class="pull-right" style="text-align: right;">
								</div>
							</div>
							
							<div id="view1" class="panel-body">
							<div class="pull-left">
									<div class="col-md-12" >
										<div class="col-md-6" style="padding-left: 0px; margin-left: -15px;" >
											<c:set var="assignedTypeObj"  value="${model.assignedType}"/>
											<form:select  path="assignedType" onchange="onChangeAssignedType()" cssStyle="width:150px" class="form-control" >
												 <c:choose>
									      			<c:when test="${assignedTypeObj== '' || assignedTypeObj== null || assignedTypeObj== 'Unassigned' }">
									      				<option value="Unassigned" selected="selected">Unassigned List</option>
									      			</c:when>
									      			<c:otherwise>
									      				<option value="Unassigned" >Unassigned List</option>
									      			</c:otherwise>
									      		</c:choose> 
												<c:choose>
									      			<c:when test="${assignedTypeObj=='Assigned' }">
									      				<option value="Assigned" selected="selected">Assigned List</option>
									      			</c:when>
									      			<c:otherwise>
									      				<option value="Assigned" >Assigned List</option>
									      			</c:otherwise>
									      		</c:choose> 
				          					 </form:select>
										</div>
										<div class="col-md-6" >
											<button  class="btn btn-info btn-xs"  type="button" data-target="#largeModal" data-toggle="modal">Assign SKU<img alt="edit" style="width:20%" src="images/edit.png" /></button>
										</div>
									</div>
								</div>
							 
							<br><br>
							<div class="table-responsive" id ="tableScroll" style="height: 450px;overflow-x: hidden;overflow-y: auto;">
									<table class="table table-striped table-bordered table-hover table-condensed newSKUMapping" id="dataTables-example">
										<thead>
											<tr >
												<th id="tblheader"><input type="checkbox" id="selectAll"></th>				
												<th>Product Number</th>
												<th width="28%">Product Description</th>
												<th >Business</th>
												<th >Category</th>
												<th >Scorecard Rollup</th>
												<th >Model</th>
												<th >User</th>
											</tr>
										</thead>
										<tbody>
										<c:forEach  items="${model.newSku}" var="modelObject" varStatus="outerloop">
												<tr class="gradeA" id ="tr${outerloop.index}">
													<td><form:checkbox path="editFlagArray" value="${outerloop.index}"/></td>
													<td id="pId">${modelObject[0]}</td>
													<td id="pdtDescription" width="28%">${fn:toUpperCase(modelObject[1])}</td>	
													<td id="business">${modelObject[2]}</td>
													<td id="region">${modelObject[3]}</td>
													<td id="model">${modelObject[4]}</td>
													<td id="scorecardRollup">${modelObject[5]}</td>
													<td id="scorecardRollup">${modelObject[6]}</td>
												</tr>	
										</c:forEach>
									 	</tbody>
									</table>
									</div>
							</div>
						</div>
					</div>
				</div>
				<!-- modal start -->
				 <div id="largeModal" class="modal fade" role="dialog" aria-hidden="true">
				  <div class="modal-dialog">
					 <div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">X</button>
								<h4 class="modal-title" >Map SKU to Product Manager</h4>
						</div>
						<div class="modal-body">
							<div class="row">
							  <div class="col-sm-5"><label>Product Manager</label></div><div class="col-sm-4">
							  <select name="productManager" id="productManagerValues"> 
							  <option value="">Choose a user...</option>
							  	<c:forEach  items="${model.productManagerList}" var="modelObject" varStatus="outerloop">
	            					<option value="${modelObject[0]}">${modelObject[1]}</option>
	          					</c:forEach>
          					  </select></div><div class="col-sm-3">&nbsp;</div>
							</div>
						</div>
						<div class="modal-footer">
						  	<button  id="modalSubmit" class="btn btn-primary"  data-dismiss="modal" >Close</button>
						  	<button  id="modalSubmit" type="button" class="btn btn-primary" onclick="assignPM();" >Ok</button>
					    </div>
				    </div>
				  </div>															       
			  </div> 
			</div>
		</div>
	</div>
</form:form>
<script>
function assignPM(){
	var checkboxes = $('input:checkbox[id!="selectAll"]:checked');
    if(checkboxes.length==0){
		swal("No SKU selected to assign.");
		return false;
    }else{
    	if(document.getElementById("productManagerValues").value!="")
    	{
    		loadProgress();
    		utilitiesBeanForm.action = "mapSKUwithPM.htm";
    		utilitiesBeanForm.submit();
    	}
    	else
    	{
    		swal("Select User");
    	}	
    }
	
}
</script>
<script>
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
</script>
</body>

</html>