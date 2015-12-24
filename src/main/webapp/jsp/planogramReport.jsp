<%@page import="java.util.HashMap"%>
<%@page import="com.bridgei2i.common.vo.Users"%>
<%@page import="com.bridgei2i.common.util.ApplicationUtil"%>
<%@page import="com.bridgei2i.common.constants.ApplicationConstants"%>
<%@page import="com.bridgei2i.common.vo.PageTemplate"%>
<%@page import="java.util.Map"%>
<%@page
	import="com.bridgei2i.common.controller.LoadApplicationCacheService"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/WEB-INF/bridgei2i.tld" prefix="bi2itag"%>
 <link href="css/custom-styles.css" rel="stylesheet" />
     <!-- Morris Chart Styles-->
    <link href="js/plugins/morris/morris-0.4.3.min.css" rel="stylesheet" />
    <!-- Core Scripts - Include with every page -->
     <script src="js/jquery-1.11.0.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/plugins/metisMenu/jquery.metisMenu.js"></script>
      <script src="js/jquery-1.10.2.js"></script>
      <script src="js/jquery.metisMenu.js"></script>
        <!-- Morris Chart Js -->
     <script src="js/plugins/morris/raphael-2.1.0.min.js"></script>
    <script src="js/plugins/morris/morris.js"></script>
	<style>
		.legend{
					height: 18px;
				  	width:20%;
				 	float: left;
				}

		#legend1{
			 background: #8FD8D8;
			  
		}
		
		#legendName1{
		 float: left;
		  width: 7%;
		  padding-left: 1%;
		}
	</style>	
	
	<form:form method="post" action="reports.htm" commandName="model" name="reportBeanForm" >
	<form:hidden id="planningCycleId" path="planningCycleId"/>
	<input type="hidden" name="isFromDashboard" id="isFromDashboard"/>
    <div id="wrapper">
       
        <div id="page-wrapper" style="  min-height: 550px;  padding: 15px 7px;">
            <div id="page-inner" style="  min-height: 550px;margin-top:-1.6%">
			 <div class="row" id="row1">
                    <div class="col-md-12">
                        <div class="panel panel-default" style="height:auto">
							<div class="panel-heading" style="background-color:#2DAFCB;font-size:130%;height:52px;padding:7px 15px;color:#fff;padding-top:1.5%  ">
								&nbsp;Planogram Report
								
							</div>
							<div class="panel-body">
								<div>
									<div id="legends1" style="margin-left:90%">
								 		<div id="legend1"  class="legend"></div><div id="legendName1">&nbsp;Forecast</div><br/>
									</div>
								</div>
								<bi2itag:TargetTableTag targetTableMap="${model.targetTableMap}"
								tableColorRange="${model.tableColorRange}"
								reportType="planogram"/>
							
							</div>
						</div>
                    </div>
                </div> 
                 <!-- /. ROW  -->
				</div>
             <!-- /. PAGE INNER  -->
            </div>
         <!-- /. PAGE WRAPPER  -->
        </div>
     <!-- /. WRAPPER  -->

</form:form>
    <!-- JS Scripts-->
    <!-- jQuery Js -->
     <!-- jQuery Js -->
    <script src="js/jquery-1.10.2.js"></script>
    <!-- Bootstrap Js -->
    <script src="js/bootstrap.min.js"></script>
	 
    <!-- Metis Menu Js -->
    <script src="js/jquery.metisMenu.js"></script>
    <!-- Custom Js -->
    <script src="js/custom-scripts.js"></script>
    
   

