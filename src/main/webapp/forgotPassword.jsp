<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String contextPath = request.getContextPath();
	String errorMsg = request.getParameter("error");
	if (errorMsg == null) {
		errorMsg = "";
	}
%>

<html>
<head>
<style>
.blur {
	background-color: #000;
}
</style>

<title>Forecasting</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=contextPath%>/css/bridgei2i.css"
	type="text/css">
<link href="<%=contextPath%>/css/bootstrap.min.css" rel="stylesheet">
<link href="<%=contextPath%>/css/styles.css" rel="stylesheet">
<link href="<%=contextPath %>/css/sweetalert.css" rel="stylesheet">
<script src="<%=contextPath%>/js/sweetalert.min.js"></script>

<script type="text/javascript">

function actionlistener() {
	var y=document.getElementById("emailId").value;
	var atpos=y.indexOf("@");
	var dotpos=y.lastIndexOf(".");

	if (atpos<1 || dotpos<atpos+2 || dotpos+2>=y.length)
	  {
	  //alert("Not a valid e-mail address");
	  swal({   title: "Invalid Email Address",
			 text: "Please Enter Valid Email Address",   
			 type: "warning",   
			 confirmButtonColor: "#2DAFCB",   
			 confirmButtonText: "Ok",   
			 closeOnConfirm: false });
	  return false;
	  }
	//var val=document.getElementById("button").value;
	homeBeanForm.submit();
}


function enableError()
{
	if("<%=errorMsg%>" != "") {
			document.all.exception.style.visibility = "visible";
		}
	}

	function closeError() {
		document.all.exception.style.visibility = "hidden";
	}

	function init() {
		enableError();
	}
	function goHome() {
		homeBeanForm.action= "homePage.htm";
		homeBeanForm.submit();
	}
</script>
</head>
<body>
	<nav class="navbar navbar-default navbar-fixed-top nav-background"
		style="height: 10%; box-shadow: 1px 1px 1px #000; z-index: 99">
		<a class="navbar-brand " href="#"><img
			src="<%=contextPath%>/images/Logo_hp.png"
			style="max-width: 7%; margin-left: 10%" /></a>
	</nav>
	<div class="blur"></div>
	<div style="background-color: #6B6B6B; padding: 7%; padding-left: 31%">
		<div class="panel panel-default"
			style="margin: 1px; max-width: 60%; border-radius: 25px; position: relative; margin-top: 8%; box-shadow: 7px 7px 5px #333">
			<div class="panel-heading"
				style="height: 9%; border-top-left-radius: 25px; border-top-right-radius: 25px">
				<span class="glyphicon glyphicon-lock"
					style="margin-top: 2%; text-align: center"></span> Login to
				Forecasting
			</div>
			<div class="panel-body" style="text-align: center;">
				<form method="post" action="forgotPassword.htm" commandName="model"
					name="homeBeanForm">
					<div class="form-group">
						<div class="col-md-12" style="margin-top: 3%">
							<table border="0px" class="table">
								<tr>
									<td>Enter your registered mail id*</td>
									<td><input type="text" class="input" name="emailId"
										id="emailId" required="required"/></td>
										</tr>
										</table>
										<%
						String str=(String)request.getSession().getAttribute("Message");
						System.out.println("STR :"+str);
						
						if(str!=null){
					%>
                               <table>
									<tr>
										<td
											style="font-size: medium; color: Green; text-align: center;">${Message}
											<br>
											<h5>
												<a href="login.jsp">Click here To Login</a>
											</h5>
										</td>

									</tr>
									<%} %>

								<!-- </tr> -->
								</table>

								<%
									if (request.getAttribute("errorMessage") != null) {
								%>

								<tr>
									<td
										style="font-size: medium; color: Green; text-align: center;">${errorMessage}
										<br>
										<h5>
											<a href="login.jsp">Click here To Login</a>
										</h5>
									</td>

								</tr>
								<%
									}
								%>
								<div class="col-md-6" style="margin-left:73%">
								<table>
								<tr>
									<td />
									<!-- <td style="text-align: right;"><input type="button"
										class="button" value="Change" id="forgotpassword"
										name="button" onclick="return actionlistener();" /></td> -->
										
										<td style="text-align: right;">
											<input type="button" class="btn btn-default btn-sm" value="Cancel" id="goBack" name="button" onclick="goHome();" />
											<input class ="btn btn-primary btn-sm" type="submit" class="button" value="Reset" id="forgotpassword" name="button" onclick="actionlistener();" />
										</td>
								</tr>
								</table>
								</div>
							
						</div>
					</div>
			</div>
			</form>
		</div>
	</div>


	<nav class="navbar navbar-default navbar-fixed-bottom nav-background"
		style="height: 10%; box-shadow: 5px 5px 5px #888888";>

		<div style="text-align: center; margin-top: 1%">

			<a style="margin-left: 7%" href="privacy-policy.htm" target="_blank">Privacy
				policy</a><font color="#333"> | email: </font><a
				href="mailto:apps@bridgei2i.com">apps@bridgei2i.com</a> <a
				href="http://www.bridgei2i.com" target="_blank">www.bridgei2i.com</a>
			<img alt="bi2i-apps"
				src="<%=contextPath%>/images/bi2i-logo-trans.png"
				style="width: 6%; float: right; margin-right: 2.5%" /> <br />

		</div>
	</nav>

</body>

</html>
