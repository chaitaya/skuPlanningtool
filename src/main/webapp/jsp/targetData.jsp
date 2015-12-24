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
<script src="<%=contextPath%>/js/bootstrap-filestyle.js"
	type="text/javascript"></script>
<title>Target Data</title>
<link href="css/bootstrap.css" rel="stylesheet" />
<link href="css/font-awesome.css" rel="stylesheet" />
<link href="css/progress-bar.css" rel="stylesheet" />
<link href="css/custom-styles.css" rel="stylesheet" />

<script type="text/javascript">
function uploadData() {
	checkDataFormat();
	var inputData = document.getElementById("data").value;
	if (inputData.match(/\.csv$/i)) {
		//alert("Inside target Data");
		loadProgress();
		targetDataForm.action = "targetDataProcess.htm";
		targetDataForm.submit();
	} else {
		alert("Please select a csv file to upload");
	}

}
function downloadTargetData() {
	//loadProgress();
	targetDataForm.action = "downloadSampleData.htm";
	targetDataForm.submit();
}

function checkDataFormat() {

}

function uploadBTBData() {
	checkDataFormat();
	var inputData = document.getElementById("btbData").value;
	//alert(inputData);
	if (inputData.match(/\.csv$/i)) {
		//alert("btb ");
		loadProgress();
		targetDataForm.action = "btbDataProcess.htm";
		targetDataForm.submit();
	} else {
		alert("Please select a BTB file to upload");
	}

}
function uploadEventCalendar() {
	//alert("Inside Event cal")
	checkDataFormat();
	var inputData = document.getElementById("eventData").value;
	if (inputData.match(/\.csv$/i)) {
		loadProgress();
		targetDataForm.action = "uploadEventCalendar.htm";
		targetDataForm.submit();
	} else {
		alert("Please select Event Calendar to upload");
	}

}
function downloadBtbData() {
	//alert("Inside BTB download");
	//loadProgress();
	targetDataForm.action = "downloadBtbData.htm";
	targetDataForm.submit();
}
function downloadSampleEventCalendar() {
	//loadProgress();
	//alert("inside  event Calender download");
	targetDataForm.action = "downloadSampleEventCalendar.htm";
	targetDataForm.submit();
}
</script>
<style type="text/css">
input#data{
  min-width: 30%;
  width: 216px;
  float: left;
  height: 38px;
}
input#btbData{
  min-width: 30%;
  width: 216px;
  float: left;
  height: 38px;
}
input#eventData{
  min-width: 30%;
  width: 216px;
  float: left;
  height: 38px;
}
#msform input, #msform textarea {
  padding: 9px;
   
  }
  
</style>
</head>

<body>
	<div id="wrapper">
		<div id="page-wrapper" style="  min-height: 500px; height: 585px;">
			<div id="page-inner" style="background-color: #fff;  min-height: 500px;">
				
				<!-- multistep form -->
				<form id="msform" method="post" action="targetData.htm" name="targetDataForm" commandName="model" enctype="multipart/form-data">
					<div class="row1" style="width:100%;float:left;  height: 150px;">
					<fieldset style="  margin: 0% 1%;width:98%;">
						<h2 class="fs-title">Upload Target Data</h2>
						<!-- <label >Refresh the target data for current quarter as per specified format</label> -->
						<!-- <h3 class="fs-subtitle">Identify the latest week actuals data</h3> -->
						<div class="row">
						<div class="col-md-6">
						<input type="file" name="data" id="data" /> 
						<input type="submit" name="submit1" style="float:left; margin: 0px 7px;" class="submit action-button"
							value="Upload" onClick="uploadData();" />
							</div>
							<div class="col-md-6">
							<label style="  margin-top: 3%;">Click
							here to download sample data.&nbsp;&nbsp;&nbsp;&nbsp;</label><a href="#"><img
							src="./images/excel_icon_unselected.png" style="width: 6%"
							title="" width="13%" height="13%" onclick="downloadTargetData();" /></a>
							</div>
							</div>
						<%-- <input type="button" id="nextButton" name="${model.uploadSuccessFlag}" class="next action-button" value="Next"/> --%>
					</fieldset>
					</div>
					<div class="row2" style="width:100%;float:left;  height: 150px;">
					<fieldset style="  margin: 0% 1%;width:98%;">
						<h2 class="fs-title">Upload BTB Data</h2>
						<!-- <label >Refresh the target data for current quarter as per specified format</label> -->
						<!-- <h3 class="fs-subtitle">Identify the latest week actuals data</h3> -->
						<div class="row">
						<div class="col-md-6">
						<input type="file" name="btbData" id="btbData" /> 
						<input type="submit" name="submit1" style="float:left; margin: 0px 7px;" class="submit action-button"
							value="Upload" onClick="uploadBTBData();" />
							</div>
							<div class="col-md-6">
							<label style="  margin-top: 3%;">Click
							here to download sample data.&nbsp;&nbsp;&nbsp;&nbsp;</label><a href="#"><img
							src="./images/excel_icon_unselected.png" style="width: 6%"
							title="" width="13%" height="13%" onClick="downloadBtbData();"/></a>
							</div>
							</div>
						<%-- <input type="button" id="nextButton" name="${model.uploadSuccessFlag}" class="next action-button" value="Next"/> --%>
					</fieldset>
					</div>
					
					<div class="row3" style="width:100%;float:left;  height: 150px;">
					<fieldset style="  margin: 0% 1%;width:98%;">
						<h2 class="fs-title">Upload Event Calendar</h2>
						<!-- <label >Refresh the target data for current quarter as per specified format</label> -->
						<!-- <h3 class="fs-subtitle">Identify the latest week actuals data</h3> -->
						<div class="row">
						<div class="col-md-6">
						<input type="file" name="eventData" id="eventData" /> 
						<input type="submit" name="submit1" style="float:left; margin: 0px 7px;" class="submit action-button"
							value="Upload" onClick="uploadEventCalendar();" />
							</div>
							<div class="col-md-6">
							<label style="  margin-top: 3%;">Click
							here to download sample data.&nbsp;&nbsp;&nbsp;&nbsp;</label><a href="#"><img
							src="./images/excel_icon_unselected.png" style="width: 6%"
							title="" width="13%" height="13%" onClick="downloadSampleEventCalendar();"/></a>
							</div>
							</div>
						<%-- <input type="button" id="nextButton" name="${model.uploadSuccessFlag}" class="next action-button" value="Next"/> --%>
					</fieldset>
					</div>
				</form>
			</div>
		</div>
	</div>


	<script type="text/javascript">
		var current_fs, next_fs, previous_fs; //fieldsets

		$(".next").click(
				function() {

					current_fs = $(this).parent();
					next_fs = $(this).parent().next();
					var inputValue = $('#nextButton').attr('name');
					if (inputValue == 1) {
						$("#progressbar li").eq($("fieldset").index(next_fs))
								.addClass("active");
						$("#uploadStep").removeClass("active");

						current_fs.hide();
						next_fs.show();
					} else {
						alert("Please upload the data and continue");
					}

				});

		$(".previous").click(
				function() {

					current_fs = $(this).parent();
					previous_fs = $(this).parent().prev();

					$("#progressbar li").eq($("fieldset").index(current_fs))
							.removeClass("active");
					$("#uploadStep").addClass("active");

					current_fs.hide();
					previous_fs.show();
				});

		$(".submit").click(function() {
			return false;
		})
	</script>
</body>
</html>