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
	<title>Upload Data</title>
	<link href="css/bootstrap.css" rel="stylesheet" />
    <link href="css/font-awesome.css" rel="stylesheet" />
	<link href="css/progress-bar.css" rel="stylesheet" />
	<link href="css/custom-styles.css" rel="stylesheet" />

</head>
<script type="text/javascript">
	function uploadData() {
		checkDataFormat();
		var inputData=document.getElementById("data").value;
		if(inputData.match(/\.csv$/i))
		{
			document.getElementById("submit1").disabled = true;
			loadProgress();
			uploadDataForm.action = "uploadDataProcess.htm";
			uploadDataForm.submit();
		}
		else
		{
			swal({   title: "File Format Error",
				 text: "Please upload a CSV file",   
				 type: "warning",   
				 confirmButtonColor: "#2DAFCB",   
				 confirmButtonText: "Ok",   
				 closeOnConfirm: false });
		}
			
	}
	function runForecast() {
		loadProgress();
		document.getElementById("runForecast1").disabled = true;
		uploadDataForm.action = "runForecast.htm";
		uploadDataForm.submit();
	}
	
	function checkDataFormat(){
		
	}
</script>
<body>
<div id="wrapper">
        <div id="page-wrapper" style="  min-height: 500px; height: 585px;" >
          <div id="page-inner" style="background-color:#fff; min-height: 500px;">
			<!-- multistep form -->
			<form id="msform" method="post" action="uploadData.htm" name="uploadDataForm"
					commandName="model" enctype="multipart/form-data">
				<input id="startWeek" name="startWeek" value="${model.startWeek}" type="hidden" />
				<input id="startYear" name="startYear" value="${model.startYear}" type="hidden" />
				<input id="planningCycleId" name="planningCycleId" value="${model.planningCycleId}" type="hidden" />
				<!-- progressbar -->
				<ul id="progressbar">
					<li id="uploadStep" class="active">Upload Data</li>
					<li id="forecastStep" style="margin-left:20%">Run Forecast</li>
				</ul>
				<fieldset>
					<h2 class="fs-title">Upload Planning Cycle Data</h2>
					<h3 class="fs-subtitle">Identify the latest week actuals data</h3>
					<input type="file"  name="data" id="data"/>
					<input type="submit" id="submit1" name="submit1" class="submit action-button" value="Submit"  onClick="uploadData();"/>
					<input type="button" id="nextButton" name="1" class="next action-button" value="Next"/>
				</fieldset>
				<fieldset>
					<h2 class="fs-title" style="margin-top:5%">Run Forecast</h2>
					<h3 class="fs-subtitle" style="margin-top:3%">The current model being used is 6 months average to forecast ASP and Unit values</h3>
					<input type="button" name="previous" class="previous action-button" value="Previous" style="margin-top:5%"/>
					<input type="submit" id="runForecast1" name="submit2" class="submit action-button" value="Run Forecast" onClick= "runForecast();" />
				</fieldset>
			</form>
		</div>
	</div>
</div>

<script type="text/javascript">
var current_fs, next_fs, previous_fs; //fieldsets

$(".next").click(function(){
	
	current_fs = $(this).parent();
	next_fs = $(this).parent().next();
	var inputValue= $('#nextButton').attr('name');
	if(inputValue == 1){
		$("#progressbar li").eq($("fieldset").index(next_fs)).addClass("active");
			$("#uploadStep").removeClass("active");
			
			current_fs.hide();
			next_fs.show(); 
		}
		else{
			swal({   title: "Data Not Uploaded",
					 text: "Please upload data and continue",   
					 type: "warning",   
					 confirmButtonColor: "#2DAFCB",   
					 confirmButtonText: "Ok",   
					 closeOnConfirm: false });
		} 
});

$(".previous").click(function(){
	
	current_fs = $(this).parent();
	previous_fs = $(this).parent().prev();
	
	$("#progressbar li").eq($("fieldset").index(current_fs)).removeClass("active");
	$("#uploadStep").addClass("active");
	
	current_fs.hide();
	previous_fs.show(); 
});

$(".submit").click(function(){
	return false;
})

</script>
	
</body>
</html>