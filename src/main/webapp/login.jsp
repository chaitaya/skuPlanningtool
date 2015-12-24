<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
		String contextPath = request.getContextPath();
		String errorMsg = request.getParameter("error");
		if(errorMsg==null){
			errorMsg="";
		}		
%>

<html>
<head>
<style>
	.blur{
		background-color: #000;
		}
</style>

<title>Forecasting</title>
<link rel="shortcut icon" href="<%=contextPath %>/images/bi2i.jpeg" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=contextPath %>/css/bridgei2i.css"
	type="text/css">
<link href="<%=contextPath %>/css/bootstrap.min.css" rel="stylesheet">
<link href="<%=contextPath %>/css/styles.css" rel="stylesheet">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<script language="JavaScript">

function enableError()
{
	if("<%=errorMsg%>"!="")
	{
		document.all.exception.style.visibility="visible";
	}
}

function closeError()
{
	document.all.exception.style.visibility="hidden";
}

function init(){
enableError();
}

</script>	
</head>
<body>
<nav class="navbar navbar-default navbar-fixed-top nav-background" style="height:10%;box-shadow: 1px 1px 1px #000;z-index:99">
 <a class="navbar-brand "  href="#"><img  src="<%=contextPath%>/images/Logo_hp.png" style="max-width:7%;margin-left:10%"/></a>
</nav>
 		<div class="blur"></div>
 		<div style="background-color:#6B6B6B;padding:7%;padding-left:31%">
							<div class="panel panel-default" style="margin:1px;max-width:60%;border-radius:25px;position:relative;margin-top:8%;box-shadow: 7px 7px 5px #333">
								<div class="panel-heading" style="height:9%;border-top-left-radius:25px;border-top-right-radius:25px">
								<span class="glyphicon glyphicon-lock" style="margin-top:2%;text-align:center"></span> Login to SKU Level Planning Tool</div>
								<div class="panel-body" style="text-align:center;height:42%">
								 <form class="form-horizontal" name='f' role="form" action="<c:url value='j_spring_security_check' />" method='POST'>
										<div class="form-group">
										<div class="col-md-12"  style="margin-top:3%">
											<label for="inputEmail3"   style="color:black;align:center">
												Email</label><br/>
												<input style="width:60%;margin-left:22%" type="text" name="j_username" class="form-control" id="inputEmail3" placeholder="Email" value="" required>
												<input type="hidden" name="sessionid" value="<%=request.getSession().getId()%>">
											</div>
										</div>
										<div class="form-group" style="margin-bottom:5%">
											<div class="col-md-12">
											<label for="inputPassword3"  style="color:black;align:center">
												Password</label><br/>
												<input  style="width:60%;margin-left:22%" type="password" name="j_password" class="form-control" id="inputPassword3" placeholder="Password" value="" required>
											</div>
										</div>
										<hr/>
										<div class="form-group last">
										<div class="row">
											<div class="col-md-7">
												<a href="forgotPassword.jsp">Forgot Password</a>&nbsp;|&nbsp;
											   	<a href="changePassword.jsp">Change Password</a><br/>
											</div>
											<div class="col-md-5">
													<button type="reset" class="btn btn-default btn-sm">
														Reset</button>	&nbsp;
														<button type="submit" class="btn btn-primary btn-sm">
														Sign in</button>
											</div>
										</div>
											
										</div>
									</form>
								</div>
							</div>
		</div>

<nav class="navbar navbar-default navbar-fixed-bottom nav-background" style="height:10%;box-shadow: 5px 5px 5px #888888" ;>
	
	<div  style="text-align:center;margin-top:1%">
		
		<a style=" margin-left:7%" href="privacy-policy.htm"  target="_blank">Privacy policy</a><font color="#333" > | email: </font><a href="mailto:apps@bridgei2i.com">apps@bridgei2i.com</a>
		<a href="http://www.bridgei2i.com" target="_blank">www.bridgei2i.com</a>
		<img alt="bi2i-apps" src="<%=contextPath %>/images/bi2i-logo-trans.png"  style="width:6%;float:right;margin-right:2.5%"/>
		<br/>
		
	</div>
</nav>
 
</body>

</html>
