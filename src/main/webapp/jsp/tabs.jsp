<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link href="css/tabcontent.css" rel="stylesheet" type="text/css" />

<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
 <script src="js/tabcontent.js" type="text/javascript"></script>
</head>
<body>
<div>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
	<ul class="tabs">
    <li><a href="#view1">Features</a></li>
    <li><a href="#view2">How to Use</a></li>
    <li><a href="#view3">Source Code</a></li>
</ul>
<div class="tabcontents">
    <div id="view1">
        content 1
    </div>
    <div id="view2">
        content 2
    </div>
    <div id="view3">
        content 3
    </div>
</div>
</div>
</body>
</html>