<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/bridgei2i.tld" prefix="bi2itag"%>
<%
	String contextPath = request.getContextPath();
%>
<html>
<head>
	<script type="text/javascript">
	
	function saveContact(){
		contactForm.action="saveContact.htm";
		contactForm.submit();
	}
	
	</script>
	<style type="text/css">
	ul#horizontalmenu{
	margin:0;padding:0;
	list-style-type:none;
	width:auto;
position:relative;
display:block;
height:30px;
text-transform:uppercase;
font-size:10px;
font-weight:bold;
background:transparent url("/Bridgei2iApp/images/bgOFF.gif") repeat-x top left;
font-family:Helvetica,Arial,Verdana,sans-serif;
border-bottom:4px solid #336666;
border-top:1px solid #C0E2D4;
}

ul#horizontalmenu li{display:block;float:left;margin:0;pading:0;}

ul#horizontalmenu li a{display:block;float:left;color:#874B46;
text-decoration:none;padding:12px 20px 0 20px;height:18px;
background:transparent url("/Bridgei2iApp/images/bgDIVIDER.gif") no-repeat top right;}

ul#horizontalmenu li a:hover{
background:transparent url("/Bridgei2iApp/images/bgHOVER.gif") no-repeat top right;}

ul#horizontalmenu li a.selected,ul#horizontalmenu li a.current:hover{color:#fff;
background:transparent url("/Bridgei2iApp/images/bgON.gif") no-repeat top right;}
	</style>
</head>
<body>

<form:form method="post" action="contactUs.htm" commandName="model" name="contactForm">

<bi2itag:HorizontalTabNavigationTag root="true">
	<bi2itag:HorizontalTabNavigationTag  navigationPage="" navigationController="" tabName="Overall" logicalName="Overall" selectedTab=""/>
	<bi2itag:HorizontalTabNavigationTag  navigationPage="" navigationController="" tabName="Model 1" logicalName="Model 1" selectedTab="Model 1"/>
	<bi2itag:HorizontalTabNavigationTag  navigationPage="" navigationController="" tabName="Model 2" logicalName="Model 2" selectedTab=""/>
	<bi2itag:HorizontalTabNavigationTag  navigationPage="" navigationController="" tabName="Model 3" logicalName="Model 3" selectedTab=""/>
</bi2itag:HorizontalTabNavigationTag>
</form:form>
</body>
</html>