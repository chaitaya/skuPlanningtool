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
	List plClassList = (List) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.PL_CLASS_LIST);
	List plScorecardList = (List) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.PL_SCORECARD_LIST);
	List modelList = (List) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.MODEL_LIST);
	List <Object[]>productManagerUserIdList = (List<Object[]>) LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.PRODUCT_MANAGER_USERID_LIST);
%>
<script type="text/javascript">
function checkProductNoMapped()
{
	if(document.getElementById("productNumber").value == "")
	{
		document.getElementById("noMoreNpi").checked = false;
		alert("Enter the Product Number");
		
	}
}
	function initializeModalValues(npiId,region,productNumber,productDescription,business,productLine,phI,phII,phIV,plClass,plScorecard,asp,esc,productManagerId,noMoreNPI,selectedRow,model,modalFlag){
		document.getElementById("selectedValues").value=selectedRow;
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
			document.getElementById("modalProductNumber").value='Not Available';
			document.getElementById("ExpectedASP").value='Map Product Number';
			document.getElementById("ExpectedESC").value='Map Product Number';
			document.getElementById("ExpectedASP").disabled=true;
			document.getElementById("ExpectedESC").disabled=true;
			document.getElementById("noMoreNpi").checked=false;
			document.getElementById("noMoreNpi").disabled=true;
		}
		else{
			document.getElementById("npiId").value=npiId;
			document.getElementById("region").value=region;
			document.getElementById("modalProductNumber").value= productNumber;
			document.getElementById("productNumber").value= productNumber;
			document.getElementById("modalNPI").value=npiId;
			document.getElementById("ProductDescription").value=productDescription;
			document.getElementById("business").value=business;
			document.getElementById("productLine").value=productLine;
			document.getElementById("modelObj").value =model;
			document.getElementById("ProductHierarchyI").value=phI;
			document.getElementById("ProductHierarchyII").value=phII;
			document.getElementById("ProductHierarchyIV").value=phIV;
			document.getElementById("plClass").value = plClass;
			document.getElementById("plScorecard").value = plScorecard;
			document.getElementById("productManagerId").value=productManagerId;
			if(modalFlag==2)
			{
				document.getElementById("ExpectedASP").disabled=true;
				document.getElementById("ExpectedESC").disabled=true;
				document.getElementById("noMoreNpi").disabled=true;
			}
			else
			{
				document.getElementById("productNumber").value=productNumber;
				document.getElementById("ExpectedASP").removeAttribute("disabled");
				document.getElementById("ExpectedESC").removeAttribute("disabled");
				document.getElementById("noMoreNpi").removeAttribute("disabled");
			}
			document.getElementById("ExpectedASP").value=asp;
			document.getElementById("ExpectedESC").value=esc;
			if(noMoreNPI == 1)
			{				
				document.getElementById("noMoreNpi").checked = true;
			}
			else
			{
				document.getElementById("noMoreNpi").checked = false;
			}
			if(modalFlag != 1){
				document.getElementById("selectedValues").value = selectedRow;
				document.getElementById("modalSubmit").addEventListener("click", updateNPI);
			}
		}
	}
	
	function updateNPI(){
		/* document.getElementById("modalProductNumber").value = document.getElementById("productNumber").value;
		if(document.getElementById("productNumber").value == "")
		{
			document.getElementById("noMoreNpi").checked = false;
			alert("Please Enter the Product Number");
		}
		else
		{
			utilitiesBeanForm.action="updateMappedNPI.htm";
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
		
	}
	function getNPI(){
		removeProgress();
		loadProgress();
		utilitiesBeanForm.action = "newProductIntroduction.htm";
		utilitiesBeanForm.submit();
	}
		
</script>
<style>
tr{
font-size:13px;
}
body{padding-right:0px !important;}
</style>
<body>

	<form:form method="post" action="mappedNPI.htm" commandName="model" name="utilitiesBeanForm" id="newProductIntroduction" enctype="multipart/form-data">
	<div id="wrapper">
		<div id="page-wrapper" style="padding: 15px 17px; min-height: 500px; ">
			<div id="page-inner" style="  min-height: 500px">
				 <div class="row" style="margin-top:-1%" >
                    <div class="col-md-12">
                        <div class="panel panel-default" style="height:auto;width:103%;margin-left:-1.2%">
							<div class="panel-heading" style="background-color:#2DAFCB;font-size:130%;height:52px;padding:7px 15px;color:#fff;padding-top:1.5%;margin-bottom:2% ">
								&nbsp;NPI
							</div>
							
							<ul class="tabs">
							    <li class=""><a href="#" onClick="getNPI();">New NPI</a></li>
							    <li id="mappedNPI" class="selected"><a href="#view1" class="active" >Mapped NPI</a></li>
							</ul>
							<div class="tabcontents" style="padding-left: 15px; padding-right: 50px;">
							<div id="view1" class="panel-body" style="width:100%">
								<div class="margin" style="  height: 520px; overflow: auto;width: 104%;">
								<c:choose>
								<c:when test ="${!empty model.npiList}">
									<table class="table table-bordered table-striped table-hover" >
										<thead id="tblheader">
											<tr >
												<th id="tblheader">NPI ID</th>
												<th id="tblheader">Product Number</th>
												<th id="tblheader">Product Description</th>
												<th id="tblheader">Business</th>
												<th id="tblheader">Product Manager for NPI</th>
												<th id="tblheader">No More NPI</th>
												<th id="tblheader">Edit</th>
											</tr>
										</thead>
										<tbody >
										<c:forEach  items="${model.npiList}" var="modelObject" varStatus="outerloop">
												<tr>
													<td class="centerText">${modelObject[0]}</td>
													<td class="centerText">${modelObject[2]}</td>
													<td class="centerText">${modelObject[3]}</td>	
													<td class="centerText">${modelObject[4]}</td>
													<td class="centerText">${modelObject[15]} ${modelObject[16]}</td>
													<td class="centerText"><c:choose><c:when test="${modelObject[14] ==1 }">Yes</c:when><c:otherwise>No</c:otherwise></c:choose></td>
													<td id="editSKu" class="centerText"><button  type="button" style="background:transparent;border:none" data-target="#largeModal" data-toggle="modal" onClick="initializeModalValues('${modelObject[0]}','${modelObject[1]}','${modelObject[2]}','${modelObject[3]}','${modelObject[4]}','${modelObject[5]}','${modelObject[6]}','${modelObject[7]}','${modelObject[8]}','${modelObject[9]}','${modelObject[10]}','${modelObject[11]}','${modelObject[12]}','${modelObject[13]}','${modelObject[14]}',${outerloop.index},'${modelObject[17]}',3);"><img alt="edit" style="width:40%" src="images/edit.png"/></button></td>
												</tr>	
										</c:forEach>
									 	</tbody>
									</table>
									</c:when>
									<c:otherwise>
									No Records Found
									</c:otherwise>
									</c:choose>
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
							  <div class="col-md-5"><select style="width :100%;"name="region" id="region"  class="required">
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
							  <div class="col-md-5"><input id="productNumber"  name="productNumber" type="text"style="width :100%;"  class="required"  /></div>
							  <div class="col-md-2">&nbsp;</div>
							</div>
							<div class="row">
							  <div class="col-md-5"><label>Product Description</label></div>
							  <div class="col-md-5"><textarea id="ProductDescription" style="width :100%;"name="productDescription"  class="required"></textarea> </div>
							  <!-- <input id="ProductDescription"  type="text"   > -->
							  <div class="col-md-2">&nbsp;</div>
							</div>
							<div class="row">
							  <div class="col-md-5"><label>Business</label></div>
							  <div class="col-md-5"><select style="width :100%;"name="business" id="business"  class="required">
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
							  <div class="col-md-5"><select style="width :100%;"name="productLine" id="productLine"  class="required">
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
							  <div class="col-md-5"><select style="width :100%;"id="ProductHierarchyII"  name="productHierarchyII"  class="required">
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
							  <div class="col-md-5"><select style="width :100%;" id="ProductHierarchyIV"  name="productHierarchyIV"  class="required">
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
							  <div class="col-md-5"><select style="width :100%;" name="plClass" id="plClass"  class="required">
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
							  <div class="col-md-5"><select style="width :100%;" name="plScorecard" id="plScorecard"  class="required">
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
							  <div class="col-md-5"><select style="width :100%;" name="model" id="modelObj"  class="required">
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
							  <div class="col-md-5"><input style="width :100%;" id="ExpectedASP" autocomplete = "off" type="text"  name="expectedAsp"  class="required"></div>
							  <div class="col-md-2">&nbsp;<!-- <label  data-toggle="tooltip" data-placement="bottom"  title ="Map Product Number to Enter the ASP Value">?</label> --></div>
							</div>
							<div class="row">
							  <div class="col-md-5"><label>Expected ESC</label></div>
							  <div class="col-md-5"><input style="width :100%;" id="ExpectedESC" autocomplete = "off" type="text"  name="expectedEsc"  class="required"></div>
							  <div class="col-md-2">&nbsp;<!-- <label  data-toggle="tooltip" data-placement="bottom"  title ="Map Product Number to Enter the ESC Value">?</label> --></div>
							</div>
							<div class="row">
							  <div class="col-md-5"><label>Product Manager for NPI</label></div>
							  <div class="col-md-5"><select style="width :100%;"  name="productManagerId" id="productManagerId"> 
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
											        		<option value="<%=userId %>"><%=firstname+" "+lastName%></option><!-- 	+"("+productManagerUserDetails[3]+")" -->
								  <%}%>
          					  </select></div>
							  <div class="col-md-2">&nbsp;</div>
							</div>
							<div class="row">
							  <div class="col-md-5"><label>No More NPI</label></div>
							  <div class="col-md-5"><input  id="noMoreNpi"  type="checkbox"  value = "1" onclick="checkProductNoMapped();" name="noMoreNPI" ></div>
							  <div class="col-md-2">&nbsp;</div>
							</div>
							
						</div>
						<div class="modal-footer">
						  <button  id="modalSubmit" class="btn btn-primary" type="button">SAVE</button>
					    </div>
				    </div>
				  </div>															       
			  </div>
			</div>
			<input name="npiId" id="npiId" type="hidden"/>
			<input name="modalProductNumber" id="modalProductNumber" type="hidden"/>
			<input name="selectedValues" id="selectedValues" type="hidden"/>
		</div>
	</div>
</form:form>
</body>
</html>