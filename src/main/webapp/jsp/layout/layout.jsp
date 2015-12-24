<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
    <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
            "http://www.w3.org/TR/html4/strict.dtd">
<%
	String contextPath = request.getContextPath();
String view =(String) request.getParameter("view");
%>
<html>
<script type="text/javascript">
</script>
<head>
<link rel="shortcut icon" href="<%=contextPath %>/images/bi2i.jpeg" />
<link rel="stylesheet" href="css/bridgei2i.css" type="text/css">
<link href="<%=contextPath %>/css/bootstrap.min.css" rel="stylesheet">
<link href="<%=contextPath %>/font-awesome/css/font-awesome.css" rel="stylesheet">
<link href="<%=contextPath %>/css/tabs.css" rel="stylesheet">
<link href="<%=contextPath %>/css/custom-styles.css" rel="stylesheet" />
<link href="<%=contextPath %>/css/mylayout.css" rel="stylesheet">
 <link href="<%=contextPath %>/css/plugins/metisMenu/metisMenu.css" rel="stylesheet">
<link href="<%=contextPath %>/css/plugins/dataTables.bootstrap.css" rel="stylesheet">
<link href="<%=contextPath %>/css/sweetalert.css" rel="stylesheet">
<script src="<%=contextPath%>/js/js_common.js" type="text/javascript"></script>
<script src="<%=contextPath%>/FusionCharts/fusioncharts.js"	type="text/javascript"></script>
 <script type="text/javascript" src="<%=contextPath%>/FusionCharts/themes/fusioncharts.theme.fint.js"></script>
<link href="css/bootstrap.css" rel="stylesheet" />
    <!-- FontAwesome Styles-->
    <link href="css/font-awesome.css" rel="stylesheet" />
    <!-- Custom Styles-->
    <link href="css/custom-styles.css" rel="stylesheet" />
     <!-- Morris Chart Styles-->
    <link href="<%=contextPath%>/js/plugins/morris/morris-0.4.3.min.css" rel="stylesheet" />
    <!-- Core Scripts - Include with every page -->
     <script src="<%=contextPath%>/js/jquery-1.11.0.js"></script>
    <script src="<%=contextPath%>/js/bootstrap.min.js"></script>
    <script src="<%=contextPath%>/js/plugins/metisMenu/jquery.metisMenu.js"></script>
      <script src="<%=contextPath%>/js/jquery-1.10.2.js"></script>
      <script src="<%=contextPath%>/js/jquery.metisMenu.js"></script>
        <!-- Morris Chart Js -->
     <script src="<%=contextPath%>/js/plugins/morris/raphael-2.1.0.min.js"></script>
    <script src="<%=contextPath%>/js/plugins/morris/morris.js"></script>
    <script src="<%=contextPath%>/js/sweetalert.min.js"></script>


    <!-- Page-Level Demo Scripts - Buttons - Use for reference -->
 <!-- DataTables JavaScript -->
    <script src="<%=contextPath%>/js/plugins/dataTables/jquery.dataTables.js"></script>
    <script src="<%=contextPath%>/js/plugins/dataTables/dataTables.bootstrap.js"></script>
    
     <!-- Page-Level Demo Scripts - Tables - Use for reference -->
    <script>
    $(document).ready(function() {
        $('#dataTables-example').dataTable();
    }); 
    </script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><tiles:insertAttribute name="title" ignore="true" /></title>
</head>
<body>


<%
if(view.equals("1")){
%>
		<div id="wrapper2">
			<tiles:insertAttribute name="header" />
			<tiles:insertAttribute name="menu" />
				<div>
					<div >
						<tiles:insertAttribute name="overviewmenu" />
						<div id="page-wrapper2" >
						<tiles:insertAttribute name="error" />
						</div>
						<tiles:insertAttribute name="body" />
					</div>
				</div>
			</div>
			<tiles:insertAttribute name="footer" />
			
			<%
}else if(view.equals("2")){
%>
<div id="wrapper2">
			<tiles:insertAttribute name="header" />
				<div>
					<div style="height: 599px;">
						<tiles:insertAttribute name="navigation" />
						<div id="page-wrapper2" >
							<tiles:insertAttribute name="error" />
						</div>
						<tiles:insertAttribute name="body"/>
						<div id="loading"  style="position:relative;" >
							<img src="./images/loading4.gif" width="64" height="64" border="0" style="display: none" >
						</div>
				</div>
			</div>
			</div>
			<tiles:insertAttribute name="footer" />
<%
}
else if(view.equals("3")){
%>
	<div id="wrapper">
		<tiles:insertAttribute name="body" />
	</div>
<%
}
%>


</body>
</html>