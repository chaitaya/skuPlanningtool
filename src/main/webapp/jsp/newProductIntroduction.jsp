<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
   <%@page import="com.bridgei2i.common.constants.ApplicationConstants"%>
<%@page import="com.bridgei2i.common.controller.LoadApplicationCacheService"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>NPI</title>
<link href="css/bootstrap.css" rel="stylesheet" />
    <link href="css/font-awesome.css" rel="stylesheet" />
	<link href="css/custom-styles.css" rel="stylesheet" />
	<script src="js/tabcontent.js" type="text/javascript"></script>
	<link href="css/tabcontent.css" rel="stylesheet" type="text/css" />
	<script src="js/jquery-1.10.2.js"></script>
     <script src="js/jquery.metisMenu.js"></script>
     
</head>
<%
	String contextPath = request.getContextPath();
	List productLineList = (List) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.PRODUCT_LINE_LIST);
	List skuBusiness = (List) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.SKU_BUSINESS);
	List skuRegion = (List) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.SKU_REGION);
	List productHierarchy1List = (List) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.PRODUCT_HIERARCHY1_LIST);
	List productHierarchy2List = (List) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.PRODUCT_HIERARCHY2_LIST);
	List productHierarchy4List = (List) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.PRODUCT_HIERARCHY4_LIST);
	List modelList = (List) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.MODEL_LIST);
	List plClassList = (List) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.PL_CLASS_LIST);
	List plScorecardList = (List) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.PL_SCORECARD_LIST);
	List <Object[]>productManagerUserIdList = (List<Object[]>) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.PRODUCT_MANAGER_USERID_LIST);
	
%>
<script type="text/javascript">
	function initializeModalValues(npiId,region,productNumber,productDescription,business,productLine,phI,phII,phIV,plClass,plScorecard,asp,esc,productManagerId,noMoreNPI,model,modalFlag){
		
		if(modalFlag==1){
			document.getElementById("modalNPI").value="Auto Generated";
			document.getElementById("region").selectedIndex=0;
			document.getElementById("ProductDescription").value='';
			document.getElementById("business").selectedIndex=0;
			document.getElementById("ProductHierarchyI").selectedIndex=0;
			document.getElementById("ProductHierarchyII").selectedIndex=0;
			document.getElementById("ProductHierarchyIV").selectedIndex=0;
			document.getElementById("productLine").selectedIndex=0;
			document.getElementById("plClass").selectedIndex=0;
			document.getElementById("plScorecard").selectedIndex=0;
			document.getElementById("modelObj").selectedIndex =0;
			document.getElementById("modalProductNumber").disabled=true;
			document.getElementById("productManagerId").selectedIndex=0;
			document.getElementById("productNumber").value='Not Available';
			document.getElementById("ExpectedASP").value='Map Product Number';
			document.getElementById("ExpectedESC").value='Map Product Number';
			document.getElementById("ExpectedASP").disabled=true;
			document.getElementById("ExpectedESC").disabled=true;
			document.getElementById("noMoreNpi").checked=false;
			document.getElementById("noMoreNpi").disabled=true;
			document.getElementById("modalSubmit").addEventListener("click", addNewNPI);
			
		}
		else{
			
			document.getElementById("npiId").value=npiId;
			document.getElementById("region").value=region;
			document.getElementById("productNumber").value='Not Available';
			document.getElementById("modalProductNumber").value='Not Available';
			document.getElementById("modalNPI").value=npiId;
			document.getElementById("ProductDescription").value=productDescription;
			document.getElementById("business").value=business;
			document.getElementById("productLine").value=productLine;
			document.getElementById("ProductHierarchyI").value=phI;
			document.getElementById("ProductHierarchyII").value=phII;
			document.getElementById("ProductHierarchyIV").value=phIV;
			document.getElementById("plClass").value = plClass;
			document.getElementById("plScorecard").value = plScorecard;
			document.getElementById("modelObj").value=model;
			document.getElementById("productManagerId").value=productManagerId;
			document.getElementById("ExpectedASP").value=asp;
			document.getElementById("ExpectedESC").value=esc;
			if(modalFlag==2)
			{
				document.getElementById("modalProductNumber").disabled=true;
				document.getElementById("ExpectedASP").disabled=true;
				document.getElementById("ExpectedESC").disabled=true;
				document.getElementById("noMoreNpi").disabled=true;
			}
			else
			{
				/* modalProductNumber */
				document.getElementById("productNumber").value='';
				document.getElementById("modalProductNumber").value='';
				document.getElementById("modalProductNumber").removeAttribute("disabled");
				document.getElementById("productNumber").removeAttribute("disabled");
				document.getElementById("ExpectedASP").removeAttribute("disabled");
				document.getElementById("ExpectedESC").removeAttribute("disabled");
				document.getElementById("noMoreNpi").removeAttribute("disabled");
			}
			if(noMoreNPI == 1)
			{				
				document.getElementById("noMoreNpi").checked = true;
			}
			else
			{
				document.getElementById("noMoreNpi").checked = false;
			}
			if(modalFlag==2){
				document.getElementById("modalProductNumber").disabled=true;
				document.getElementById("modalSubmit").addEventListener("click", updateNPI);
			}
			else{
				
					/* document.getElementById("modalProductNumber").value = productNumber;
					document.getElementById("productNumber").value = productNumber; */
					document.getElementById("modalSubmit").addEventListener("click", mapNPI);
			}
		}
	}
	function checkProductNoMapped()
	{
		if(document.getElementById("productNumber").value == "" || document.getElementById("modalProductNumber").value == "")
		{
			document.getElementById("noMoreNpi").checked = false;
			alert("Please Enter the Product Number");
		}
	}
		function getNPI(){
			removeProgress();
			loadProgress();
			utilitiesBeanForm.action = "newProductIntroduction.htm";
			utilitiesBeanForm.submit();
		}

		function uploadNPI(){
			removeProgress();
			loadProgress();
			utilitiesBeanForm.action = "uploadNPI.htm";
			utilitiesBeanForm.submit();
		}
		
		function addNewNPI(){
			removeProgress();
			loadProgress();
			utilitiesBeanForm.action = "addNewNPI.htm";
			utilitiesBeanForm.submit();
		}
		
		function downloadNPIData(){
			removeProgress();
			utilitiesBeanForm.action = "downloadNPIData.htm";
			utilitiesBeanForm.submit();
		}
		function updateNPI(){
			if(document.getElementById("region").value!=="" 
				   && document.getElementById("ProductDescription").value!="" 
					&& document.getElementById("business").value !=""
					&& document.getElementById("productLine").value !=""	
					&& document.getElementById("ProductHierarchyII").value!=""
					&& document.getElementById("ProductHierarchyIV").value !=""
					&& document.getElementById("plClass").value!=""
					&& document.getElementById("plScorecard").value !=""
					&& document.getElementById("modelObj").value !=""
					&& document.getElementById("productManagerId").value !=""){
				utilitiesBeanForm.action="updateNPI.htm";
				utilitiesBeanForm.submit();
				
			}else{
			if(document.getElementById("region").value ==""){
				alert("Enter Region");
			}
			if(document.getElementById("ProductDescription").value ==""){
				alert("Enter ProductDescription");
			}
			if(document.getElementById("business").value == ""){
				alert("Enter business");
			}
			if(document.getElementById("productLine").value == ""){
				alert("Enter productLine");
			}	
			if(document.getElementById("ProductHierarchyII").value == ""){
				alert("Enter ProductHierarchyIV");
			}
			if(document.getElementById("ProductHierarchyIV").value == ""){
				alert("Enter ProductHierarchyIV");
			}
			if(document.getElementById("plClass").value ==""){
				alert("Enter plClass");
			} 
			if(document.getElementById("plScorecard").value ==""){
				alert("Enter plScorecard");
			}
			if(document.getElementById("modelObj").value ==""){
				alert("Enter modelObj");
			} 
			if(document.getElementById("productManagerId").value == ""){
				alert("Enter productManagerId");
				}
			
			}
			
			//utilitiesBeanForm.action="updateNPI.htm";
			//utilitiesBeanForm.submit();
		}
		
		function removeNPI(npiId){
			document.getElementById("npiId").value=npiId;
			utilitiesBeanForm.action="removeNPI.htm";
			utilitiesBeanForm.submit();
		}
		
		function mapNPI(){
			/* if( document.getElementById("modalProductNumber").value == ""){
				alert("Please Enter product Number");
			}
			
			else{
				document.getElementById("productNumber").value=document.getElementById("modalProductNumber").value;
				document.getElementById("npiId").value=document.getElementById("modalNPI").value;
				utilitiesBeanForm.action="mapSku.htm";
				utilitiesBeanForm.submit();
			} */
			if(document.getElementById("region").value!=="" 
				   && document.getElementById("ProductDescription").value!="" 
					&& document.getElementById("business").value !=""
					&& document.getElementById("productLine").value !=""	
					&& document.getElementById("ProductHierarchyII").value!=""
					&& document.getElementById("ProductHierarchyIV").value !=""
					&& document.getElementById("plClass").value!=""
					&& document.getElementById("plScorecard").value !=""
					&& document.getElementById("modelObj").value !=""
					&& document.getElementById("productManagerId").value !=""){
				document.getElementById("productNumber").value=document.getElementById("modalProductNumber").value;
				document.getElementById("npiId").value=document.getElementById("modalNPI").value;
				utilitiesBeanForm.action="mapSku.htm";
				utilitiesBeanForm.submit();
			}else{
				if(document.getElementById("region").value ==""){
					alert("Enter Region");
				}
				if(document.getElementById("ProductDescription").value ==""){
					alert("Enter ProductDescription");
				}
				if(document.getElementById("business").value == ""){
					alert("Enter business");
				}
				if(document.getElementById("productLine").value == ""){
					alert("Enter productLine");
				}	
				if(document.getElementById("ProductHierarchyII").value == ""){
					alert("Enter ProductHierarchyIV");
				}
				if(document.getElementById("ProductHierarchyIV").value == ""){
					alert("Enter ProductHierarchyIV");
				}
				if(document.getElementById("plClass").value ==""){
					alert("Enter plClass");
				} 
				if(document.getElementById("plScorecard").value ==""){
					alert("Enter plScorecard");
				}
				if(document.getElementById("modelObj").value ==""){
					alert("Enter modelObj");
				} 
				if(document.getElementById("productManagerId").value == ""){
					alert("Enter productManagerId");
					}
			}
		}
		
		function getMappedNPI(){
			utilitiesBeanForm.action="mappedNPI.htm";
			utilitiesBeanForm.submit();
		}
		
		
 	
</script>
<style>
body{padding-right:0px !important;}
textarea,select {
width :172px;
}
tr{
font-size:13px;
} 
</style>
<body>

	<form:form method="post" action="newProductIntroduction.htm" commandName="model" name="utilitiesBeanForm" id="newProductIntroduction" enctype="multipart/form-data">
	<div id="wrapper">
		<div id="page-wrapper"   style="  min-height: 500px;  padding: 15px 17px;">
			<div id="page-inner"  style="  min-height: 500px">
				 <div class="row" style="margin-top:-1%" >
                    <div class="col-md-12">
                        <div class="panel panel-default" style="height:auto;width:103%;margin-left:-1.2%">
							<div class="panel-heading" style="background-color:#2DAFCB;font-size:130%;height:52px;padding:7px 15px;color:#fff;padding-top:1.5%;margin-bottom:2% ">
								&nbsp;New Product Introduction
								<div class="pull-right" style="text-align: right;">
								</div>
							</div>
							
							<ul class="tabs">
							    <li><a href="#view1">New NPI</a></li>
							    <li><a href="#" class="" id="mappedNPI" onClick="getMappedNPI();">Mapped NPI</a></li>
							</ul>
								<div  style="margin-top:1%;margin-left:32%;float:left">
									<div class="col-md-12" style="margin-left:31%">
										Click here to download sample data
									</div> 
								</div>
								<div class="pull-right">
									<div class="col-md-12">
										<div class="col-md-6" >
											<div class="col-md-2" style="margin-top:6%">
												<a href="#" id="dowloadNpiSample"><img src="./images/excel_icon_unselected.png" style="width: 23px"  title="" height="13%" onclick="downloadNPIData();" /></a>
											</div>
											<div class="col-md-4">
												<input type="file" name="data" id="data" class="btb btn-info" style="width: 150px;margin-top:10px;" />
											</div>
										</div>
										<div class="col-md-6" >
											<input type="submit" name="submit1" class="btb btn-info"
												style="margin-top:10px;margin-left: -10px;" value="Upload" onClick="uploadNPI();" /> <input
												type="button" name="Add New" class="btb btn-info"
												onClick="initializeModalValues(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1);"
												value="Add New" data-target="#largeModal"
												data-toggle="modal" />
										</div>
									</div>
								</div>
								<div class="tabcontents" style="padding-left: 10px; padding-right: 50px;">
							<div id="view1" class="panel-body" style="width:100%">
								<div class="margin" style="  height: 520px; overflow: auto;width: 104%;">
								<c:choose>
								<c:when test ="${!empty model.npiList}">
									<table class="table table-bordered table-striped table-hover" >
										<thead id="tblheader">
											<tr >
												<th id="tblheader">NPI ID</th>
												<th id="tblheader">Product Number</th>
												<th id="tblheader" width="28%">Product Description</th>
												<th id="tblheader">Business</th>
												<th id="tblheader">PM for NPI</th>
												<th id="tblheader">No More NPI</th>
												<th id="tblheader">Edit</th>
												<th id="headerDelete">Delete</th>
												<th id="headerMap">Map NPI</th>
											</tr>
										</thead>
										<tbody>
										<c:forEach  items="${model.npiList}" var="modelObject" varStatus="outerloop">
												<tr>
													<td class="centerText">${modelObject[0]}</td>
													<td class="centerText">${modelObject[2]}</td>
													<td class="centerText" width="28%">${modelObject[3]}</td>	
													<td class="centerText">${modelObject[4]}</td>
													<td class="centerText">${modelObject[15]} ${modelObject[16]}</td>
													<td class="centerText"><c:choose><c:when test="${modelObject[14] ==1 }">Yes</c:when><c:otherwise>No</c:otherwise></c:choose></td>
													<td id="editSKu" class="centerText"><button  type="button" style="background:transparent;border:none" data-target="#largeModal" data-toggle="modal" onClick="initializeModalValues('${modelObject[0]}','${modelObject[1]}','${modelObject[2]}','${modelObject[3]}','${modelObject[4]}','${modelObject[5]}','${modelObject[6]}','${modelObject[7]}','${modelObject[8]}','${modelObject[9]}','${modelObject[10]}','${modelObject[11]}','${modelObject[12]}','${modelObject[13]}','${modelObject[14]}','${modelObject[17]}',2);"><img alt="edit" style="width:40%" src="images/edit.png" /></button></td>
													<td id="removeSku" class="centerText"><button  style="background:transparent;border:none"  onClick="removeNPI('${modelObject[0]}',2);"><img alt="delete" style="width:110%" src="images/delete.png"/></button></td> 
													<td id="mapSKu" class="centerText"><button  type="button"  style="background:transparent;border:none" data-target="#largeModal" data-toggle="modal" onClick="initializeModalValues('${modelObject[0]}','${modelObject[1]}','${modelObject[2]}','${modelObject[3]}','${modelObject[4]}','${modelObject[5]}','${modelObject[6]}','${modelObject[7]}','${modelObject[8]}','${modelObject[9]}','${modelObject[10]}','${modelObject[11]}','${modelObject[12]}','${modelObject[13]}','${modelObject[14]}','${modelObject[17]}',3);"><img alt="add" style="width:110%" src="images/add.png" /></button></td>
												</tr>	
										</c:forEach>
									 	</tbody>
									</table>
									</c:when>
									<c:otherwise>
									No Records Found
									</c:otherwise>
									</c:choose>
									<!-- <div class="pull-right">
										<div class="col-md-12">
											<div class="col-md-5">
											<input type="file"  name="data" id="data" class="btb btn-info" style="width:150px"/>
											</div>
											<div class="col-md-5">
											<input type="submit" name="submit1" class="btb btn-info" value="Upload"  onClick="uploadNPI();"/>
											<input type="button" name="Add New"class="btb btn-info"  onClick="initializeModalValues(1,1,1,1,1,1,1,1);"value="Add New" data-target="#largeModal" data-toggle="modal"/>
											</div>
										</div>
									</div> -->
									<input id="skuId"name="sku" type="hidden"/>
									<input id="commitFlagId" name="commitFlag" type="hidden"/>
									<input id="commitLevelId" name="commitLevel" type="hidden"/>
									<input id="modalId" name="modalFlag" type="hidden"/>
								</div>
							</div>
							</div>
						</div>
					</div>
				</div>
				<!-- modal start -->
				<div id="largeModal" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">
				  <div class="modal-dialog">
					 <div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">X</button>
								<h4 class="modal-title" >New Product Introduction</h4>
						</div>
						<div class="modal-body">
							<div class="row">
							  <div class="col-md-5"><label>NPI ID</label></div>
							  <div class="col-md-5"><input type="text" id="modalNPI" value="Auto Generated" style="width :100%;"  disabled="disabled"/></div>
							  <div class="col-md-2">&nbsp;</div>
							</div>
							<div class="row">
							  <div class="col-md-5"><label>Region</label></div>
							  <div class="col-md-5"><select style="width :100%;"name="region" id="region" class="required">
											  <option value="" selected>select</option>
											        <%
											       
											        for(int i=0;i<skuRegion.size();i++) {
											           String Field=skuRegion.get(i).toString();
											        %>
											        <option value="<%=Field %>"><%=Field %></option>
											        <%} %>
										</select>
								
							</div>
							<div class="col-md-2">&nbsp;</div>
							</div>
							<div class="row">
							  <div class="col-md-5"><label>Product Number</label></div>
							  <div class="col-md-5"><input id="modalProductNumber"  type="text"style="width :100%;"  value="Not Available" class="required" /></div>
							  <div class="col-md-2">&nbsp;</div>
							</div>
							<div class="row">
							  <div class="col-md-5"><label>Product Description</label></div>
							  <div class="col-md-5"><textarea id="ProductDescription" style="width :100%;"name="productDescription" class="required"></textarea> </div>
							  <!-- <input id="ProductDescription"  type="text"   > -->
							  <div class="col-md-2">&nbsp;</div>
							</div>
							<div class="row">
							  <div class="col-md-5"><label>Business</label></div>
							  <div class="col-md-5"><select  style="width :100%;"name="business" id="business" class="required">
											  <option value="" selected>select</option>
											        <%
											       
											        for(int i=0;i<skuBusiness.size();i++) {
											           String Field=skuBusiness.get(i).toString();
											        %>
											        <option value="<%=Field %>"><%=Field %></option>
											        <%} %>
										</select>
							</div>
							<div class="col-md-2">&nbsp;</div>
							</div>
							
							<div class="row">
							  <div class="col-md-5"><label>Product Line</label></div>
							  <div class="col-md-5"><select style="width :100%;"name="productLine" id="productLine" class="required">
											  <option value="" selected>select</option>
											        <%
											       
											        for(int i=0;i<productLineList.size();i++) {
											           String Field=productLineList.get(i).toString();
											        %>
											        <option value="<%=Field %>"><%=Field %></option>
											        <%} %>
										</select>
							</div>
							<div class="col-md-2">&nbsp;</div>
							</div>
							<div class="row">
							  <div class="col-md-5"><label>Product Hierarchy I</label></div>
							  <div class="col-md-5"><select style="width :100%;" id="ProductHierarchyI" name="productHierarchyI"  class="required">
											  <option value="" selected>select</option>
											        <%
											       
											        for(int i=0;i<productHierarchy1List.size();i++) {
											           String Field=productHierarchy1List.get(i).toString();
											        %>
											        <option value="<%=Field%>"><%=Field %></option>
											        <%} %>
									</select>
							</div>
							<div class="col-md-2">&nbsp;</div>
							</div>
							<div class="row">
							  <div class="col-md-5"><label>Product Hierarchy II</label></div>
							  <div class="col-md-5"><select style="width :100%;"id="ProductHierarchyII"  name="productHierarchyII" class="required">
											  <option value="" selected>select</option>
											        <%
											       
											        for(int i=0;i<productHierarchy2List.size();i++) {
											           String Field=productHierarchy2List.get(i).toString();
											        %>
											        <option value="<%=Field%>"><%=Field%></option>
											        <%} %>
									</select>
								</div>
								<div class="col-md-2">&nbsp;</div>
							</div>
							<div class="row">
							  <div class="col-md-5"><label>Product Hierarchy IV:</label></div>
							  <div class="col-md-5"><select style="width :100%;" id="ProductHierarchyIV"  name="productHierarchyIV" class="required">
											  <option value="" selected>select</option>
											        <%
											       
											        for(int i=0;i<productHierarchy4List.size();i++) {
											           String Field=productHierarchy4List.get(i).toString();
											        %>
											        <option value="<%=Field %>"><%=Field %></option>
											        <%} %>
									</select>
							</div>
							<div class="col-md-2">&nbsp;</div>
							</div>
							<div class="row">
							  <div class="col-md-5"><label>PL Class:</label></div>
							  <div class="col-md-5"><select style="width :100%;" name="plClass" id="plClass" class="required">
											  <option value="" selected>select</option>
											        <%
											       
											        for(int i=0;i<plClassList.size();i++) {
											           String Field=plClassList.get(i).toString();
											        %>
											        <option value="<%=Field %>"><%=Field %></option>
											        <%} %>
									</select >
								</div>
								<div class="col-md-2">&nbsp;</div>
							</div>
							<div class="row">
							  <div class="col-md-5"><label>PL Scorecard</label></div>
							  <div class="col-md-5"><select  style="width :100%;" name="plScorecard" id="plScorecard" class="required">
											  <option value="" selected>select</option>
											        <%
											       
											        for(int i=0;i<plScorecardList.size();i++) {
											           String Field=plScorecardList.get(i).toString();
											        %>
											        <option value="<%=Field %>"><%=Field %></option>
											        <%} %>
									</select>
								</div>
								<div class="col-md-2">&nbsp;</div>
							</div>
							<div class="row">
							  <div class="col-md-5"><label>Model</label></div>
							  <div class="col-md-5"><select style="width :100%;" name="model" id="modelObj" class="required">
											  <option value="" selected>select</option>
											        <%
											        for(int i=0;i<modelList.size();i++) {
											           String Field=modelList.get(i).toString();
											        %>
											        <option value="<%=Field %>"><%=Field %></option>
											        <%} %>
									</select>
								</div>
								<div class="col-md-2">&nbsp;</div>
							</div>
							<div class="row">
							  <div class="col-md-5"><label>Expected ASP</label></div>
							  <div class="col-md-5"><input style="width :100%;" id="ExpectedASP" autocomplete = "off" type="text"  name="expectedAsp" ></div>
							  <div class="col-md-2">&nbsp;<!-- <label  data-toggle="tooltip" data-placement="bottom"  title ="Map Product Number to Enter the ASP Value">?</label> --></div>
							</div>
							<div class="row">
							  <div class="col-md-5"><label>Expected ESC</label></div>
							  <div class="col-md-5"><input style="width :100%;" id="ExpectedESC" autocomplete = "off" type="text"  name="expectedEsc" ></div>
							  <div class="col-md-2">&nbsp;<!-- <label  data-toggle="tooltip" data-placement="bottom"  title ="Map Product Number to Enter the ESC Value">?</label> --></div>
							</div>
							<div class="row">
							  <div class="col-md-5"><label>Product Manager for NPI</label></div>
							  <div class="col-md-5"><select  style="width :100%;"  name="productManagerId" id="productManagerId"> 
							  <option value="">Choose a user...</option>
							   <%
							   Object userId = 0;
							   String firstname ="";
							   String lastName ="";
											        for(Object [] productManagerUserDetails : productManagerUserIdList) {
											        	if(productManagerUserDetails[0]!=null)
											        	{
											        		userId = productManagerUserDetails[0];
											        	}
											        	if(productManagerUserDetails[1]!=null)
											        	{
											        		firstname = (String)productManagerUserDetails[1];
											        	}
											        	if(productManagerUserDetails[2]!=null)
											        	{
											        		lastName = (String) productManagerUserDetails[2];
											        	}
											        	 %>
											        		<option value="<%=userId %>"><%=firstname+" "+lastName%></option>
								  <%}%>
          					  </select></div>
							  <div class="col-md-2">&nbsp;</div>
							</div>
							<div class="row">
							  <div class="col-md-5"><label>No More NPI</label></div>
							  <div class="col-md-5"><input  id="noMoreNpi"  type="checkbox" onclick="checkProductNoMapped();" value = "1" name="noMoreNPI" ></div>
							  <div class="col-md-2">&nbsp;</div>
							</div>
						</div>
						<div class="modal-footer">
						  <button  id="modalSubmit" class="btn btn-primary" type="button" >SAVE</button>
					    </div>
				    </div>
				  </div>												       
				 <input name="npiId" id="npiId" type="hidden"/>
				 <input name="productNumber" id="productNumber" type="hidden"/>
				 </div>
			</div>
		</div>
</form:form>
</body>
</html>