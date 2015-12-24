<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
		String contextPath = request.getContextPath();
%>

<html>
<head>
<style>
	.blur{
		background-color: #000;
		}
</style>

<title>Forecasting</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=contextPath %>/css/bridgei2i.css"
	type="text/css">
<link href="<%=contextPath %>/css/bootstrap.min.css" rel="stylesheet">
<link href="<%=contextPath %>/css/styles.css" rel="stylesheet">

</head>
<body>
<nav class="navbar navbar-default navbar-fixed-top nav-background" style="height:10%;box-shadow: 1px 1px 1px #000;z-index:99">
 <a class="navbar-brand "  href="#"><img  src="<%=contextPath%>/images/Logo_hp.png" style="max-width:7%;margin-left:10%"/></a>
</nav>
 		<div class="blur"></div>
 		<div style="background-color:#6B6B6B;padding:7%;padding-left:31%">
			<div class="panel panel-default" style="margin:1px;max-width:60%;border-radius:10px;position:relative;margin-top:8%;box-shadow: 7px 7px 5px #333">
				<div class="panel-heading" style="height:9%;border-top-left-radius:10px;border-top-right-radius:10px">
				<span class="glyphicon glyphicon-lock" style="margin-top:2%;text-align:center"></span> Permission Denied</div>
				<div class="panel-body" style="text-align:center;height:22%">
					You do not have permission to access this page,<br> please refer to your system administrator.
				</div>
			</div>
		</div>
</body>
</html>
