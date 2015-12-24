<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.bridgei2i.common.util.ApplicationUtil"%>
<%@page import="com.bridgei2i.common.constants.ApplicationConstants"%>
<%@page
	import="com.bridgei2i.common.controller.LoadApplicationCacheService"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/bridgei2i.tld" prefix="bi2itag"%>
<%
	String contextPath = request.getContextPath();
	List masterFunctionList = (List) LoadApplicationCacheService.applicationCacheObject
			.get(ApplicationConstants.MASTER_FUNCTIONS_CACHE_KEY);
	List masterBenchMarkList = (List) LoadApplicationCacheService.applicationCacheObject
			.get(ApplicationConstants.MASTER_BENCH_MARKS_CACHE_KEY);
	List masterChartTypeList = (List) LoadApplicationCacheService.applicationCacheObject
			.get(ApplicationConstants.MASTER_CHART_TYPES_CACHE_KEY);
	List variableNamesList = (List)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.ALL_VARIABLE_NAMES_CACHE_KEY);
	Map masterFunctionsMapCache = (Map) LoadApplicationCacheService.applicationCacheObject
			.get(ApplicationConstants.MASTER_FUNCTIONS_MAP_CACHE_KEY);
	List commentList = (List)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.COMMENT_CACHE_KEY);
	List keyMeasureList = (List)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.KEY_MEASURE_CACHE_KEY);
	List weightVariableList = (List)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.WEIGHT_VARIABLE_CACHE_KEY);
	List segmentVariableNamesList  = (List)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.SEGMENT_VARIABLE_NAMES_CACHE_KEY);
	List keySegmentVariableList = new ArrayList();
	if(keyMeasureList!=null){
	keySegmentVariableList.addAll(keyMeasureList);
	}
	if(segmentVariableNamesList != null){
	keySegmentVariableList.addAll(segmentVariableNamesList);
	}
	
	
%>
<style>

  .canvas-container {
    overflow-x: auto;
    overflow-y: visible;
    position: relative;
    margin-top: 20px;
    margin-bottom: 20px;
  }
  .canvas {
    display: block;
    position: relative;
    overflow: hidden;
  }

  .canvas.hide {
    display: none;
  }

  .html-canvas > span {
    transition: text-shadow 1s ease, opacity 1s ease;
    -webkit-transition: text-shadow 1s ease, opacity 1s ease;
    -ms-transition: text-shadow 1s ease, opacity 1s ease;
  }

  .html-canvas > span:hover {
    text-shadow: 0 0 10px, 0 0 10px #fff, 0 0 10px #fff, 0 0 10px #fff;
    opacity: 0.5;
  }

  .wordHoverbox {
    pointer-events: none;
    position: absolute;
    box-shadow: 0 0 10px 10px rgba(200, 200, 200, 0.5);
    border-radius: 50px;
    cursor: pointer;
    
  }
.wordcloud-container{
	position:relative;
	float:left;
}


  </style>

<script type="text/javascript">
	function init() {
		var scrollLocationObj = document.getElementById('scrollLocation');
		document.getElementById("page-wrapper").scrollTop = scrollLocationObj.value;
	}

	function onChange() {
		var functionId = document.getElementById("functionId").value;
		workspaceForm.action = "onchangeFunction.htm";
		workspaceForm.submit();
	}
	
	function runReports() {
		workspaceForm.action = "runReports.htm";
		workspaceForm.submit();
	}
	
	function tabClick(tabName) {
		var tabNameObj = document.getElementById("tabName");
		tabNameObj.value = tabName;
	}
	
	function onChangeFilter(chartId, obj) {
		var tableName = document.getElementById("tableName" + chartId).value;

		var xAxis = document.getElementById("xAxis" + chartId).value;
		var yAxis = document.getElementById("yAxis" + chartId).value;
		var filterColumnName = document.getElementById("filterColumnName"
				+ chartId).value;
		var xAxisColumnName = document.getElementById("xAxisColumnName"
				+ chartId).value;
		var yAxisColumnName = document.getElementById("yAxisColumnName"
				+ chartId).value;
		var templateChartInfoId = document.getElementById("templateChartInfoId"
				+ chartId).value;
		var tempChartAttributes = document.getElementById("tempChartAttributes"
				+ chartId).value;
		var percentageflag=false;
		
		var percentageCheckBox = document.getElementById("percentage"+ chartId);
		if(percentageCheckBox!=null){
			percentageflag = percentageCheckBox.checked;
			
		}else{
			
		}
		var filterObj = document.getElementById("filter"+ chartId);
		var filterValue = filterObj.options[filterObj.selectedIndex].value;
		var url = "onChangeFilter.htm?templateChartInfoId is null"
				 + "&tableName=" + tableName + "&xAxis="
				+ xAxis + "&yAxis=" + yAxis + "&filterColumnName="
				+ filterColumnName + "&filterValue=" + filterValue
				+ "&xAxisColumnName=" + xAxisColumnName + "&yAxisColumnName="
				+ yAxisColumnName+"&percentageflag="+percentageflag;
		
		var xmlHttp = new XMLHttpRequest();
		if (window.XMLHttpRequest) { // Mozilla, Safari, ...
			xmlHttp = new XMLHttpRequest();
		} else if (window.ActiveXObject) { // IE
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
        
		xmlHttp.open('GET', url, true);
		xmlHttp.onreadystatechange = function() {
			if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
				var xmlStrArry = xmlHttp.responseText.split("@");
				var xmlStr =xmlStrArry[0];
				if(xmlStrArry.length>1){
					obj.setXMLData(xmlStr);	
				}else{
					tempChartAttributes = tempChartAttributes
					+ xmlStr+ "</chart>";
					obj.setXMLData(tempChartAttributes);
				}
				if(percentageflag){
					obj.setChartAttribute("numberSuffix","%");
				}
				else{
					obj.setChartAttribute("numberSuffix","");
				}
				obj.render('chartContainer' + chartId);
			}
		};
		xmlHttp.send(url);
	}

	function onChangeDistributionList(distributionListObj) {

		dashboardForm.action = " onChangeDistributionList.htm";
		dashboardForm.submit();

	}
	
	function teamExportExcel(){
		var loadingObj = document.getElementById("loading");
        loadingObj.style="display:display;position: relative;top:-350px;left: 40%";
		dashboardForm.action = "teamExportExcel.htm";
		dashboardForm.submit();
	}
	
	function onChangeMainVariable(){
		var functionId = document.getElementById("functionId").value;
		var chartTypeObj =  document.getElementById("chartTypeId");
		var count = 0;
		var mainVariableObj = document.getElementById("mainVariableId");
		for(var i=0;i<mainVariableObj.options.length;i++){
			if(mainVariableObj.options[i].selected)
				count++;
		}
		var segmentVariableObj =document.getElementById('segmentVariableId');
		
		if(mainVariableObj.value == -1 ){
    		alert("Select atleast one main variable");
    	}
        if(functionId==1 && count>1 && segmentVariableObj.options[segmentVariableObj.selectedIndex].value != -1 ){
    		alert("Cannot select segment variable ");
	     		if(segmentVariableObj.options[segmentVariableObj.selectedIndex].value !=-1){
	     			segmentVariableObj.value=-1;
	     		}
    	}
       
		if(functionId == 1){
		    if(count==1 && segmentVariableObj.options[segmentVariableObj.selectedIndex].value == -1){
			    while (chartTypeObj.firstChild) {
			    	chartTypeObj.removeChild(chartTypeObj.firstChild);
				}
			    		
			    var option = document.createElement('option');
				option.text = "Select";
				option.value = "-1";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Bar Chart";
				option.value = "Bar2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Column Chart";
				option.value = "Column2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Pie Chart";
				option.value = "Pie2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Line Chart";
				option.value = "Line";
				chartTypeObj.add(option);
		    }
		    else if(count==1 && segmentVariableObj.options[segmentVariableObj.selectedIndex].value != -1){
			    while (chartTypeObj.firstChild) {
			    	chartTypeObj.removeChild(chartTypeObj.firstChild);
				}
			    var option = document.createElement('option');
				option.text = "Select";
				option.value = "-1";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Stacked Bar Chart";
				option.value = "StackedBar2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Stacked Column Chart";
				option.value = "StackedColumn2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Clustered Bar Chart";
				option.value = "MSBar2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Clustered Column Chart";
				option.value = "MSColumn2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "100percent Stacked Bar Chart";
				option.value = "HUNDRED_PERCENT_STACKED_BAR";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "100percent Stacked Column Chart";
				option.value = "HUNDRED_PERCENT_STACKED_COLUMN";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "MultiSeries Line Chart";
				option.value = "MSLine";
				chartTypeObj.add(option);
		    }
		    else if(count>1 && segmentVariableObj.options[segmentVariableObj.selectedIndex].value == -1){
			    while (chartTypeObj.firstChild) {
			    	chartTypeObj.removeChild(chartTypeObj.firstChild);
				}
			    var option = document.createElement('option');
				option.text = "Select";
				option.value = "-1";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Stacked Bar Chart";
				option.value = "StackedBar2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Stacked Column Chart";
				option.value = "StackedColumn2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Clustered Bar Chart";
				option.value = "MSBar2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Clustered Column Chart";
				option.value = "MSColumn2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "100percent Stacked Bar Chart";
				option.value = "HUNDRED_PERCENT_STACKED_BAR";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "100percent Stacked Column Chart";
				option.value = "HUNDRED_PERCENT_STACKED_COLUMN";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "MultiSeries Line Chart";
				option.value = "MSLine";
				chartTypeObj.add(option);
		    }
		}
		if(functionId==2){
			if(document.getElementById('benchMarkId').options[document.getElementById('benchMarkId').selectedIndex].value == -1){
			if(count==1 && segmentVariableObj.options[segmentVariableObj.selectedIndex].value == -1){
				while (chartTypeObj.firstChild) {
					chartTypeObj.removeChild(chartTypeObj.firstChild);
				}
				var option = document.createElement('option');
				option.text = "Select";
				option.value = "-1";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Bar Chart";
				option.value = "Bar2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Column Chart";
				option.value = "Column2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Pie Chart";
				option.value = "Pie2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Line Chart";
				option.value = "Line";
				chartTypeObj.add(option);
			}
			else if(count>1 && segmentVariableObj.options[segmentVariableObj.selectedIndex].value == -1){
				while (chartTypeObj.firstChild) {
					chartTypeObj.removeChild(chartTypeObj.firstChild);
				}
				var option = document.createElement('option');
				option.text = "Select";
				option.value = "-1";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Bar Chart";
				option.value = "Bar2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Column Chart";
				option.value = "Column2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Pie Chart";
				option.value = "Pie2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Line Chart";
				option.value = "Line";
				chartTypeObj.add(option);
			}
			else if(count==1 && segmentVariableObj.options[segmentVariableObj.selectedIndex].value != -1){
				while (chartTypeObj.firstChild) {
					chartTypeObj.removeChild(chartTypeObj.firstChild);
				}
				var option = document.createElement('option');
				option.text = "Select";
				option.value = "-1";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Stacked Bar Chart";
				option.value = "StackedBar2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Stacked Column Chart";
				option.value = "StackedColumn2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Clustered Bar Chart";
				option.value = "MSBar2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Clustered Column Chart";
				option.value = "MSColumn2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "100percent Stacked Bar Chart";
				option.value = "HUNDRED_PERCENT_STACKED_BAR";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "100percent Stacked Column Chart";
				option.value = "HUNDRED_PERCENT_STACKED_COLUMN";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "MultiSeries Line Chart";
				option.value = "MSLine";
				chartTypeObj.add(option);
			}
			else if(count>1 && segmentVariableObj.options[segmentVariableObj.selectedIndex].value != -1){
				while (chartTypeObj.firstChild) {
					chartTypeObj.removeChild(chartTypeObj.firstChild);
				}
			    var option = document.createElement('option');
				option.text = "Select";
				option.value = "-1";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Stacked Bar Chart";
				option.value = "StackedBar2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Stacked Column Chart";
				option.value = "StackedColumn2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Clustered Bar Chart";
				option.value = "MSBar2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Clustered Column Chart";
				option.value = "MSColumn2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "100percent Stacked Bar Chart";
				option.value = "HUNDRED_PERCENT_STACKED_BAR";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "100percent Stacked Column Chart";
				option.value = "HUNDRED_PERCENT_STACKED_COLUMN";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "MultiSeries Line Chart";
				option.value = "MSLine";
				chartTypeObj.add(option);
			}
			}
			else{
				if(count>1 && segmentVariableObj.options[segmentVariableObj.selectedIndex].value != -1){
		    		while (chartTypeObj.firstChild) {
		    			chartTypeObj.removeChild(chartTypeObj.firstChild);
					}
		    		var option = document.createElement('option');
					option.text = "Select";
					option.value = "-1";
					chartTypeObj.add(option);
			
					var option = document.createElement('option');
					option.text = "Column Line Chart";
					option.value = "MSColumnLine3D";
					chartTypeObj.add(option);
				}
				else{
					while (chartTypeObj.firstChild) {
		    			chartTypeObj.removeChild(chartTypeObj.firstChild);
					}
		    		var option = document.createElement('option');
					option.text = "Select";
					option.value = "-1";
					chartTypeObj.add(option);
			
					var option = document.createElement('option');
					option.text = "Column Line Chart";
					option.value = "MSCombi2D";
					chartTypeObj.add(option);
				}
			}
		}
	}
	
	function onChangeSegmentVariable(){
		var functionId = document.getElementById("functionId").value;
		var chartTypeObj = document.getElementById("chartTypeId");
		var count = 0;
		var mainVariableObj = document.getElementById("mainVariableId");
		for(var i=0;i<mainVariableObj.options.length;i++){
			if(mainVariableObj.options[i].selected)
				count++;
		}
		var segmentVariableObj =document.getElementById('segmentVariableId');
		 if(functionId==1 && count>1 && segmentVariableObj.options[segmentVariableObj.selectedIndex].value != -1 ){
		   		alert("Cannot select segment variable ");
		   		 var segmentId =segmentVariableObj.options[segmentVariableObj.selectedIndex].value;
			     		if(segmentId !=-1){
			     			segmentVariableObj.value=-1;
			     		}
		   	    }
		if(functionId == 1){
			if(count==1 && document.getElementById('segmentVariableId').options[document.getElementById('segmentVariableId').selectedIndex].value == -1){
		    	while (chartTypeObj.firstChild) {
		    		chartTypeObj.removeChild(chartTypeObj.firstChild);
				}
		    	var option = document.createElement('option');
				option.text = "Select";
				option.value = "-1";
				chartTypeObj.add(option);
			
				var option = document.createElement('option');
				option.text = "Bar Chart";
				option.value = "Bar2D";
				chartTypeObj.add(option);
			
				var option = document.createElement('option');
				option.text = "Column Chart";
				option.value = "Column2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Pie Chart";
				option.value = "Pie2D";
				chartTypeObj.add(option);
			
				var option = document.createElement('option');
				option.text = "Line Chart";
				option.value = "Line";
				chartTypeObj.add(option);
	    	}
	    	else if(count==1 && segmentVariableObj.options[segmentVariableObj.selectedIndex].value != -1){
		    	while (chartTypeObj.firstChild) {
		    		chartTypeObj.removeChild(chartTypeObj.firstChild);
				}
			    var option = document.createElement('option');
				option.text = "Select";
				option.value = "-1";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Stacked Bar Chart";
				option.value = "StackedBar2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Stacked Column Chart";
				option.value = "StackedColumn2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Clustered Bar Chart";
				option.value = "MSBar2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Clustered Column Chart";
				option.value = "MSColumn2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "100percent Stacked Bar Chart";
				option.value = "HUNDRED_PERCENT_STACKED_BAR";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "100percent Stacked Column Chart";
				option.value = "HUNDRED_PERCENT_STACKED_COLUMN";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "MultiSeries Line Chart";
				option.value = "MSLine";
				chartTypeObj.add(option);
	  	  }
	    	else if(count>1 && segmentVariableObj.options[segmentVariableObj.selectedIndex].value == -1){
		    	while (chartTypeObj.firstChild) {
		    		chartTypeObj.removeChild(chartTypeObj.firstChild);
				}
			    var option = document.createElement('option');
				option.text = "Select";
				option.value = "-1";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Stacked Bar Chart";
				option.value = "StackedBar2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Stacked Column Chart";
				option.value = "StackedColumn2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Clustered Bar Chart";
				option.value = "MSBar2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Clustered Column Chart";
				option.value = "MSColumn2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "100percent Stacked Bar Chart";
				option.value = "HUNDRED_PERCENT_STACKED_BAR";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "100percent Stacked Column Chart";
				option.value = "HUNDRED_PERCENT_STACKED_COLUMN";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "MultiSeries Line Chart";
				option.value = "MSLine";
				chartTypeObj.add(option);
		    }
		}
		if(functionId==2){
			if(document.getElementById('benchMarkId').options[document.getElementById('benchMarkId').selectedIndex].value == -1){
			if(segmentVariableObj.options[segmentVariableObj.selectedIndex].value == -1 && count == 1){
				while (chartTypeObj.firstChild) {
					chartTypeObj.removeChild(chartTypeObj.firstChild);
				}
				var option = document.createElement('option');
				option.text = "Select";
				option.value = "-1";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Bar Chart";
				option.value = "Bar2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Column Chart";
				option.value = "Column2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Pie Chart";
				option.value = "Pie2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Line Chart";
				option.value = "Line";
				chartTypeObj.add(option);
			}
			else if(segmentVariableObj.options[segmentVariableObj.selectedIndex].value == -1 && count>1){
				while (chartTypeObj.firstChild) {
					chartTypeObj.removeChild(chartTypeObj.firstChild);
				}
				var option = document.createElement('option');
				option.text = "Select";
				option.value = "-1";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Bar Chart";
				option.value = "Bar2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Column Chart";
				option.value = "Column2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Pie Chart";
				option.value = "Pie2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Line Chart";
				option.value = "Line";
				chartTypeObj.add(option);
			}
			else if(segmentVariableObj.options[segmentVariableObj.selectedIndex].value != -1 && count==1){
				while (chartTypeObj.firstChild) {
					chartTypeObj.removeChild(chartTypeObj.firstChild);
				}
				var option = document.createElement('option');
				option.text = "Select";
				option.value = "-1";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Stacked Bar Chart";
				option.value = "StackedBar2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Stacked Column Chart";
				option.value = "StackedColumn2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Clustered Bar Chart";
				option.value = "MSBar2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Clustered Column Chart";
				option.value = "MSColumn2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "100percent Stacked Bar Chart";
				option.value = "HUNDRED_PERCENT_STACKED_BAR";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "100percent Stacked Column Chart";
				option.value = "HUNDRED_PERCENT_STACKED_COLUMN";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "MultiSeries Line Chart";
				option.value = "MSLine";
				chartTypeObj.add(option);
			}
			else if(segmentVariableObj.options[segmentVariableObj.selectedIndex].value != -1 && count>1){
				while (chartTypeObj.firstChild) {
					chartTypeObj.removeChild(chartTypeObj.firstChild);
				}
				var option = document.createElement('option');
				option.text = "Select";
				option.value = "-1";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Stacked Bar Chart";
				option.value = "StackedBar2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Stacked Column Chart";
				option.value = "StackedColumn2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Clustered Bar Chart";
				option.value = "MSBar2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "Clustered Column Chart";
				option.value = "MSColumn2D";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "100percent Stacked Bar Chart";
				option.value = "HUNDRED_PERCENT_STACKED_BAR";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "100percent Stacked Column Chart";
				option.value = "HUNDRED_PERCENT_STACKED_COLUMN";
				chartTypeObj.add(option);
				
				var option = document.createElement('option');
				option.text = "MultiSeries Line Chart";
				option.value = "MSLine";
				chartTypeObj.add(option);
			}
			}
			else{
				if(count>1 && segmentVariableObj.options[segmentVariableObj.selectedIndex].value != -1){
	    		while (chartTypeObj.firstChild) {
	    			chartTypeObj.removeChild(chartTypeObj.firstChild);
				}
	    		var option = document.createElement('option');
				option.text = "Select";
				option.value = "-1";
				chartTypeObj.add(option);
		
				var option = document.createElement('option');
				option.text = "Column Line Chart";
				option.value = "MSColumnLine3D";
				chartTypeObj.add(option);
			}
			else{
				while (chartTypeObj.firstChild) {
	    			chartTypeObj.removeChild(chartTypeObj.firstChild);
				}
	    		var option = document.createElement('option');
				option.text = "Select";
				option.value = "-1";
				chartTypeObj.add(option);
		
				var option = document.createElement('option');
				option.text = "Column Line Chart";
				option.value = "MSCombi2D";
				chartTypeObj.add(option);
			}
			}
		}
	}
	function onChangeBenchMark(index){
		var functionId = document.getElementById("functionId").value;
		var chartTypeObj = document.getElementById("chartTypeId");
		var count = 0;
		var mainVariableObj = document.getElementById("mainVariableId");
		for(var i=0;i<mainVariableObj.options.length;i++){
			if(mainVariableObj.options[i].selected)
				count++;
		}
		var segmentVariableObj =document.getElementById('segmentVariableId');
		if(functionId == 2){
			if(document.getElementById('benchMarkId').options[document.getElementById('benchMarkId').selectedIndex].value != -1){
				if(count>1 && segmentVariableObj.options[segmentVariableObj.selectedIndex].value != -1){
		    		while (chartTypeObj.firstChild) {
		    			chartTypeObj.removeChild(chartTypeObj.firstChild);
					}
		    		var option = document.createElement('option');
					option.text = "Select";
					option.value = "-1";
					chartTypeObj.add(option);
			
					var option = document.createElement('option');
					option.text = "Column Line Chart";
					option.value = "MSColumnLine3D";
					chartTypeObj.add(option);
				}
				else{
					while (chartTypeObj.firstChild) {
		    			chartTypeObj.removeChild(chartTypeObj.firstChild);
					}
		    		var option = document.createElement('option');
					option.text = "Select";
					option.value = "-1";
					chartTypeObj.add(option);
			
					var option = document.createElement('option');
					option.text = "Column Line Chart";
					option.value = "MSCombi2D";
					chartTypeObj.add(option);
				}
			}
			else{
				if(segmentVariableObj.options[segmentVariableObj.selectedIndex].value == -1 && count == 1){
					while (chartTypeObj.firstChild) {
						chartTypeObj.removeChild(chartTypeObj.firstChild);
					}
					var option = document.createElement('option');
					option.text = "Select";
					option.value = "-1";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "Bar Chart";
					option.value = "Bar2D";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "Column Chart";
					option.value = "Column2D";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "Pie Chart";
					option.value = "Pie2D";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "Line Chart";
					option.value = "Line";
					chartTypeObj.add(option);
				}
				else if(segmentVariableObj.options[segmentVariableObj.selectedIndex].value == -1 && count>1){
					while (chartTypeObj.firstChild) {
						chartTypeObj.removeChild(chartTypeObj.firstChild);
					}
					var option = document.createElement('option');
					option.text = "Select";
					option.value = "-1";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "Bar Chart";
					option.value = "Bar2D";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "Column Chart";
					option.value = "Column2D";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "Pie Chart";
					option.value = "Pie2D";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "Line Chart";
					option.value = "Line";
					chartTypeObj.add(option);
				}
				else if(segmentVariableObj.options[segmentVariableObj.selectedIndex].value != -1 && count==1){
					while (chartTypeObj.firstChild) {
						chartTypeObj.removeChild(chartTypeObj.firstChild);
					}
					var option = document.createElement('option');
					option.text = "Select";
					option.value = "-1";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "Stacked Bar Chart";
					option.value = "StackedBar2D";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "Stacked Column Chart";
					option.value = "StackedColumn2D";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "Clustered Bar Chart";
					option.value = "MSBar2D";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "Clustered Column Chart";
					option.value = "MSColumn2D";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "100percent Stacked Bar Chart";
					option.value = "HUNDRED_PERCENT_STACKED_BAR";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "100percent Stacked Column Chart";
					option.value = "HUNDRED_PERCENT_STACKED_COLUMN";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "MultiSeries Line Chart";
					option.value = "MSLine";
					chartTypeObj.add(option);
				}
				else if(segmentVariableObj.options[segmentVariableObj.selectedIndex].value != -1 && count>1){
					while (chartTypeObj.firstChild) {
						chartTypeObj.removeChild(chartTypeObj.firstChild);
					}
					var option = document.createElement('option');
					option.text = "Select";
					option.value = "-1";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "Stacked Bar Chart";
					option.value = "StackedBar2D";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "Stacked Column Chart";
					option.value = "StackedColumn2D";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "Clustered Bar Chart";
					option.value = "MSBar2D";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "Clustered Column Chart";
					option.value = "MSColumn2D";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "100percent Stacked Bar Chart";
					option.value = "HUNDRED_PERCENT_STACKED_BAR";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "100percent Stacked Column Chart";
					option.value = "HUNDRED_PERCENT_STACKED_COLUMN";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "MultiSeries Line Chart";
					option.value = "MSLine";
					chartTypeObj.add(option);
				}
			}
		}
	}
</script>
<body onLoad="init();">
	<form:form method="post" action="workspace.htm"
		commandName="model" name="workspaceForm">
		
	<div id="wrapper" style="margin-top:60px">
         <div class="navbar-default navbar-static-side" role="navigation" style="padding:15px;min-height:570px">
                <div class="sidebar-collapse">
                   <label>Function:</label><br/>
																<bi2itag:DropDownDisplayTag
																		defaultValue="Select"
																		script="onChange=\"onChange();\""
																		id="functionId"
																		selectedValue="${model.functionId}"
																		list="<%=masterFunctionList%>"
																		path="functionId" />
																
																<c:if
																		test="${model.functionId==2}">
																		<label> Bench Mark:</label><br/>
																		<bi2itag:DropDownDisplayTag defaultValue="Select"
																			script="onChange=\"onChangeBenchMark();\""
																			id="benchMarkId"
																			selectedValue="${model.benchMark}"
																			list="<%=masterBenchMarkList%>"
																			path="benchMark" />
																</c:if>
																<c:if test="${chartInfoObj.functionId==2}">
																	<form:input id="numberOfCharts" class="form-control" style="color:#888;width:200px;height:30px;" path="numberOfCharts"  />
																	</c:if>
																	<c:if
																		test="${model.functionId==1 || model.functionId==2}">
																		<label>Main Variable:</label>
																	</c:if> <c:if
																		test="${model.functionId==6 || model.functionId==7}">
																		<label>Key Measure Variable:</label>
																	</c:if>
																	<c:if
																		test="${model.functionId==8}">
																		<label>Stub Variable:</label>
																	</c:if>
																	 <c:if
																		test="${model.functionId==3 || model.functionId==4 || model.functionId==5}">
																		<label>Comment Variable:</label>
																	</c:if></br>
																	<c:if
																		test="${model.functionId==1}">
																		<bi2itag:DropDownDisplayTag    defaultValue="Select"
																			multipleValue="true"
																			height="180px" 
																			script="onChange=\"onChangeMainVariable();\""
																			id="mainVariableId"
																			selectedValue="${model.xAxis}"
																			list="<%=variableNamesList%>"
																			path="xAxisArray" />
																	</c:if>
																	<c:if
																		test="${model.functionId==2}">
																		<bi2itag:DropDownDisplayTag    defaultValue="Select"
																			multipleValue="true"
																			height="180px" 
																			script="onChange=\"onChangeMainVariable();\""
																			id="mainVariableId"
																			selectedValue="${model.xAxis}"
																			list="<%=keyMeasureList%>"
																			path="xAxisArray" />
																	</c:if> 
																	<c:if
																		test="${model.functionId==3 || model.functionId==4 || model.functionId==5}">
																		<bi2itag:DropDownDisplayTag defaultValue="Select"
																			selectedValue="${model.xAxis}"
																			list="<%=commentList%>"
																			path="xAxisArray" />
																	</c:if>
																	<c:if
																		test="${model.functionId==6 || model.functionId==7 || model.functionId==8}">
																		<bi2itag:DropDownDisplayTag defaultValue="Select"
																			selectedValue="${model.xAxis}"
																			list="<%=keyMeasureList%>"
																			path="xAxisArray" />
																	</c:if>
																	</div>
												
																<c:if
																		test="${model.functionId==1 || model.functionId==2 || model.functionId==3 || model.functionId==4 || model.functionId==5}">
																		
																		<label>Segment Variable:</label><br/>
																	</c:if> <c:if
																		test="${model.functionId==6 || model.functionId==7}">
																		
																		<label>Potential Key Variables:</label><br/>
																	</c:if>
																	<c:if
																		test="${model.functionId==8}">
																		
																		<label>Skeleton Variables:</label><br/>
																	</c:if>
																	
																<c:if test="${model.functionId==1 || model.functionId==2 || model.functionId==3 || model.functionId==4 || model.functionId==5}">
																		<bi2itag:DropDownDisplayTag defaultValue="Select"
																			selectedValue="${model.yAxis}"
																			id="segmentVariableId"
																			script="onChange=\"onChangeSegmentVariable();\""
																			list="<%=variableNamesList%>"
																			path="yAxisArray" />
																	</c:if> <c:if
																		test="${model.functionId==6 || model.functionId==7}">
																		<bi2itag:DropDownDisplayTag defaultValue="Select"
																			multipleValue="true"
																			height="180px"
																			selectedValue="${model.yAxis}"
																			list="<%=keyMeasureList%>"
																			path="yAxisArray" />
																	</c:if>
																	<c:if
																		test="${model.functionId==8}">
																		<bi2itag:DropDownDisplayTag defaultValue="Select"
																			multipleValue="true"
																			height="180px"
																			selectedValue="${model.yAxis}"
																			list="<%=keySegmentVariableList%>"
																			path="yAxisArray" />
																	</c:if>
																		<c:if test="${model.functionId==1 || model.functionId==2}">
																		<label>Chart Type:</label><br/>
																	
																		<bi2itag:DropDownDisplayTag id="chartTypeId" defaultValue="Select"
																			selectedValue="${model.chartTypeId}"
																			list="${chartTypesList}"
																			path="chartTypeId" />
																	</c:if>
																	<c:if
																		test="${model.functionId==8}">
																		<label>Weight Variables:</label><br/>
																		<bi2itag:DropDownDisplayTag defaultValue="Select"
																			selectedValue="${model.weightVariable}"
																			script="onChange=\"onChangeWeightVariable();\""
																			list="<%=weightVariableList%>"
																			path="weightVariable" />
																	</c:if>
																	<c:if
																		test="${model.functionId==1 || model.functionId==2}">
																		<label>Filter variable:</label><br/>
																	</c:if> 
																	
																	<c:if
																		test="${model.functionId==6 || model.functionId==7}">
																		<label>Segment variable:</label><br/>
																	</c:if>
																	
																	<c:if
																		test="${model.functionId==8}">
																		<input type="checkbox" name="crossTab" />
																		<label>Mean inclusive:</label>
																		<form:input id="incl" class="form-control" style="color:#888;width:200px;height:30px;" path="meanIncl"  />
																		<br/>
																		<label>Mean Exclusive:</label>
																		<form:input id="excl" class="form-control" style="color:#888;width:200px;height:30px;" path="meanExcl"  />
																		<label>Median inclusive:</label>
																		<form:input id="medianIncl" class="form-control" style="color:#888;width:200px;height:30px;" path="medianIncl"  />
																		<br/>
																		<label>Median Exclusive:</label>
																		<form:input id="medianExcl" class="form-control" style="color:#888;width:200px;height:30px;" path="medianExcl"  />
																	</c:if>
																	
																<c:if test="${model.functionId==1 || model.functionId==2 || model.functionId==6 || model.functionId==7}">
																		<bi2itag:DropDownDisplayTag defaultValue="Select"
																			selectedValue="${model.filter}"
																			list="<%=variableNamesList%>"
																			path="filter" />
																	</c:if>
																	<table width="100%" style="padding:10px">
																	<tr>
																	<td align="right" height="50px">
																		<input type="button" value="Process.." id="run" class="btn btn-primary"  onClick="runReports();">
																	</td>
																	</tr>
																	</table>
																	
                </div>
                
                 <div id="page-wrapper" style="margin-left:19%;margin-top:5px;min-height:570px">
	            <div class="row">
	                <div class="col-lg-12">
	                    <h1 class="page-header" style="margin:10px 0 10px"><font color="grey">WORKSPACE</font></h1>
	                </div>
	                <!-- /.col-lg-12 -->
	            </div>
            	<div class="row" style="margin-top:-60px;">
                <div class="col-lg-13" style="margin-top:80px;">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <i class="fa fa-bar-chart-o fa-fw"></i> <font color="grey">${model.functionName}</font>
                            <div class="pull-right">
                                <div class="btn-group">
                                    <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
                                        Actions
                                        <span class="caret"></span>
                                    </button>
                                    <ul class="dropdown-menu pull-right" role="menu">
										<li><a onClick="exportToExcel(FusionCharts('${model.singleMultiSeriesChartValueObject.chartId}'))" style="cursor:pointer;"><img src="./images/excel_icon_unselected.png" title="Export to excel" width="15" height="15"/>    
										Export Excel</a></li>
										<li><a onClick="exportPPT(FusionCharts('${model.singleMultiSeriesChartValueObject.chartId}'),'${model.singleMultiSeriesChartValueObject.chartType}','${model.singleMultiSeriesChartValueObject.caption}','${model.singleMultiSeriesChartValueObject.categoryName}','${model.singleMultiSeriesChartValueObject.chartId}')" style="cursor:pointer;"><img src="./images/ppt_icon.jpg" title="Export to ppt" width="15" height="15"/>
										Export to PPT</a></li>
									</ul>
                                </div>
                            </div>
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body" style="overflow-x: auto;">
                            <c:if
																	test="${model.functionName=='FREQUENCY' || model.functionName=='AVERAGE' || model.functionName=='KEYDRIVERANALYSIS'}">
																	
																		<div id="components">
																		
																			<bi2itag:DropDownDisplayTag defaultValue="Select"	selectedValue=""
																					id="filter${model.singleMultiSeriesChartValueObject.chartId}"
																					list="${model.singleMultiSeriesChartValueObject.filterValues}"
																					path="" script="onChange=\" onChangeFilter('${model.singleMultiSeriesChartValueObject.chartId}',FusionCharts('${model.singleMultiSeriesChartValueObject.chartId}'))\""/>
																			</div>
																		
																			<c:if test="${model.functionName=='AVERAGE'}">
																			<div id="components">					
																				<input type="checkbox" name="percentage" id="percentage${model.singleMultiSeriesChartValueObject.chartId}" onChange="onChangeFilter('${model.singleMultiSeriesChartValueObject.chartId}',FusionCharts('${model.singleMultiSeriesChartValueObject.chartId}'))" /> Display as  %
																			</div>
																			</c:if>																					

																	<div style="position:relative;width:100%;height:400px;clear:both;">
																	<bi2itag:CombinationChartTag
																		xml="${model.singleMultiSeriesChartValueObject.xml}"
																		chartType="${model.singleMultiSeriesChartValueObject.chartType}"
																		chartId="${model.singleMultiSeriesChartValueObject.chartId}"
																		height="${model.singleMultiSeriesChartValueObject.height }"
																		width="100%"
																		stack100Percent="${model.singleMultiSeriesChartValueObject.stack100Percent }"
																		tableName="${model.singleMultiSeriesChartValueObject.tableName}"
																		xAxis="${model.singleMultiSeriesChartValueObject.xAxis}"
																		yAxis="${model.singleMultiSeriesChartValueObject.yAxis }"
																		filterColumnName="${model.singleMultiSeriesChartValueObject.filterColumn}"
																		templateChartInfoId="${model.singleMultiSeriesChartValueObject.id}"
																		categoryName="${model.singleMultiSeriesChartValueObject.categoryName }"
																		xAxisColumnName="${model.singleMultiSeriesChartValueObject.xAxisColumnName}"
																		yAxisColumnName="${model.singleMultiSeriesChartValueObject.yAxisColumnName}"
																		xAxisName="${model.singleMultiSeriesChartValueObject.xAxisName}"
																		yAxisName="${model.singleMultiSeriesChartValueObject.yAxisName}"
																		caption="" 
																		xAxisMinValue="${model.singleMultiSeriesChartValueObject.xAxisMinValue}"
																		xAxisMaxValue="${model.singleMultiSeriesChartValueObject.xAxisMaxValue}"
																		yAxisMinValue="${model.singleMultiSeriesChartValueObject.yAxisMinValue}"
																		divLineAlpha="${model.singleMultiSeriesChartValueObject.divLineAlpha}"/>
																		</div>
																</c:if>
																

																<c:if
																	test="${model.functionName=='SENTIMENT'}">
																	<table width="60%" border=0px cellspacing="3px">
																		<tr>
																		<td style="valign: middle"><label><b>Segment By:</b>
																					(${model.singleMultiSeriesChartValueObject.filter})
																			</label> <bi2itag:DropDownDisplayTag defaultValue="Select"
																					selectedValue=""
																					id="filter${model.singleMultiSeriesChartValueObject.chartId}"
																					list="${model.singleMultiSeriesChartValueObject.filterValues}"
																					path="" script="onChange=\" onChangeFilter('${model.singleMultiSeriesChartValueObject.chartId}',FusionCharts('${model.singleMultiSeriesChartValueObject.chartId}'))\""/>

																			</td>
																			<td><label><b>Positive Sentiment:</b>
																					${model.singleMultiSeriesChartValueObject.positiveSentiment}%
																			</label></td>
																			<td><label><b>Negative Sentiment:</b>
																					${model.singleMultiSeriesChartValueObject.negativeSentiment}%
																			</label></td>
																		</tr>
																	</table>
																	<bi2itag:CombinationChartTag
																		xml="${model.singleMultiSeriesChartValueObject.xml}"
																		chartType="${model.singleMultiSeriesChartValueObject.chartType}"
																		chartId="${model.singleMultiSeriesChartValueObject.chartId}"
																		height="${model.singleMultiSeriesChartValueObject.height }"
																		width="${model.singleMultiSeriesChartValueObject.width }"
																		stack100Percent="${model.singleMultiSeriesChartValueObject.stack100Percent }"
																		tableName="${model.singleMultiSeriesChartValueObject.tableName}"
																		xAxis="${model.singleMultiSeriesChartValueObject.xAxis}"
																		yAxis="${model.singleMultiSeriesChartValueObject.yAxis }"
																		filterColumnName="${model.singleMultiSeriesChartValueObject.filterColumn}"
																		templateChartInfoId="${model.singleMultiSeriesChartValueObject.id}"
																		categoryName="${model.singleMultiSeriesChartValueObject.categoryName }" caption="" />
																</c:if>
																
																<c:if
																	test="${model.functionName=='BANNER TABLE'}">
																	<table width="100%" >
																	<tr>
																	<td style=width:90%;>
																	<table width="100%">
																			<tr >
																			<td>
																			<div id="count">
																		 	Count Values:
																		 	<bi2itag:BannerTableTag totalDisplayFlag="true" 
																		 	headerList="${model.singleMultiSeriesChartValueObject.bannerTableHeaderList}" 
																		 	dataList="${model.singleMultiSeriesChartValueObject.bannerTableDataList}"/>
																		 	</div>
																		   <div id="weight" >
																		   Weight Values:
																		   <bi2itag:BannerTableTag totalDisplayFlag="true"
																		    headerList="${model.singleMultiSeriesChartValueObject.bannerTableHeaderList}" 
																		    dataList="${model.singleMultiSeriesChartValueObject.bannerTableWeightList}"/>
																		   </div>
																		    <div id="percentageUnweight">
																		    Percentage Unweighted Values:
																		    <bi2itag:BannerTableTag totalDisplayFlag=""
																		     headerList="${model.singleMultiSeriesChartValueObject.bannerTableHeaderList}" 
																		     dataList="${model.singleMultiSeriesChartValueObject.bannerTablePercentageList}"/>
																		    </div>
																		    <div id="percentageWeight">
																		    Percentage Weighted Values:
																		    <bi2itag:BannerTableTag totalDisplayFlag="" 
																		    headerList="${model.singleMultiSeriesChartValueObject.bannerTableHeaderList}"
																		     dataList="${model.singleMultiSeriesChartValueObject.bannerTablePercentageWeightList}"/>
																		    </div>
																			</td>
																			</tr>
																			</table>
																	</td>
																	<td width="10%" valign="top" style="padding:20px">
																	<table>
																			<tr>
																			<td><center>
																			<div class="btn-group" id="showWeight">
																 			<button   type="button" class="btn btn-default"><img src="./images/weight.png" width="25" height="25"/></button>
																 			</div><br>
																 			<div class="btn-group" id="showWeight1">
																 			<button  type="button"  class="btn btn-default"><img src="./images/weight1.png" width="25" height="25"/></button>
																 			</div><br>
																 			<div class="btn-group" id="showPercentage">
																 			<button   type="button" class="btn btn-default"><img src="./images/percentage.png" width="25" height="25"/></button>
																 			</div><br>
																 			<div class="btn-group" id="showPercentage1">
																 			<button  type="button"  class="btn btn-default"><img src="./images/percentage1.png" width="25" height="25"/></button>
																 			</div><br>
																 			</center>
																 			</td>
																 			</tr>
																			</table>
																	</td>
																	</tr>
																	</table>
																 		
																</c:if>
																<c:if
																	test="${model.functionName=='WORDCLOUD' || model.functionName=='CATEGORICAL_ANALYSIS'}">
																	<bi2itag:DropDownDisplayTag selectedValue="" id="segmentList${model.singleMultiSeriesChartValueObject.chartId}"
																						list="${model.singleMultiSeriesChartValueObject.segmentValues}"
																						path="" script=""/>
																	<bi2itag:WordCloudTag chartTypeId="${model.singleMultiSeriesChartValueObject.chartId}"
																	width="1000" height="330" wordCloudStr="${model.singleMultiSeriesChartValueObject.wordCloudStr}"/>
																</c:if>

                        </div>
                        <!-- /.panel-body -->
                    </div>
                    </div>
                    </div>
            </div>
            </div>
    <!-- /#wrapper -->
	</form:form>
<script type="text/javascript">
$(".active").css({"display":"block"});
$(".tab-pane").css({"visibility":"hidden"});
$("body").css({"visibility":"hidden"});
$( ".active" ).fadeIn( 0 );

	$(window).bind("load", function() {
		$( ".active" ).fadeIn( 0 );
		$( ".tab-pane" ).fadeOut( 0 );
		$( ".active" ).fadeIn( 0 );
		$(".tab-pane").css({"visibility":"visible"});
		$("body").css({"visibility":"visible"});
		});
	
	$( ".nav-tabs>li" ).click(function() {
		 setTimeout(
		            function(){
		            	$(".tab-pane").css({"display":"block"});
		        		$( ".active" ).fadeOut( 0 );
		        		$( ".tab-pane" ).fadeOut( 0 );
		        		$( ".active" ).fadeIn( 0 );},
		           10);
		});
	$( document ).ready(function() {
		$("#count").show();
		$("#weight").hide();
		$("#percentageWeight").hide();
		$("#percentageUnweight").hide();
		$("#showPercentage1").hide();
		$("#showPercentage").show();
		$("#showWeight").show();
		$("#showWeight1").hide();
		$( "#showWeight" ).click(function() {
			if($( "#showWeight" ).is(':visible') && ($('#showPercentage').is(':visible'))){
			$("#count").hide();
			$("#weight").show();
			$("#percentageWeight").hide();
			$("#percentageUnweight").hide();
			$("#showPercentage1").hide();
			$("#showPercentage").show();
			$("#showWeight1").show();
			$("#showWeight").hide();
			}
			else if($( "#showPercentage" ).is(':visible') && ($('#showWeight1').is(':visible'))){
				$("#count").hide();
				$("#weight").hide();
				$("#percentageWeight").show();
				$("#percentageUnweight").hide();
				$("#showPercentage1").show();
				$("#showPercentage").hide();
				$("#showWeight1").show();
				$("#showWeight").hide();
				}
			else if($( "#showPercentage1" ).is(':visible') && ($('#showWeight').is(':visible'))){
				$("#count").hide();
				$("#weight").hide();
				$("#percentageWeight").show();
				$("#percentageUnweight").hide();
				$("#showPercentage1").show();
				$("#showPercentage").hide();
				$("#showWeight1").show();
				$("#showWeight").hide();
				}
			
		});
		$( "#showPercentage" ).click(function() {
			if($( "#showPercentage" ).is(':visible') && ($('#showWeight').is(':visible'))){
			$("#count").hide();
			$("#weight").hide();
			$("#percentageWeight").hide();
			$("#percentageUnweight").show();
			$("#showPercentage1").show();
			$("#showPercentage").hide();
			$("#showWeight1").hide();
			$("#showWeight").show();
			}
			else if($( "#showPercentage" ).is(':visible') && ($('#showWeight1').is(':visible'))){
				$("#count").hide();
				$("#weight").hide();
				$("#percentageWeight").show();
				$("#percentageUnweight").hide();
				$("#showPercentage1").show();
				$("#showPercentage").hide();
				$("#showWeight1").show();
				$("#showWeight").hide();
				}
			else if($( "#showPercentage1" ).is(':visible') && ($('#showWeight').is(':visible'))){
				$("#count").hide();
				$("#weight").hide();
				$("#percentageWeight").show();
				$("#percentageUnweight").hide();
				$("#showPercentage1").show();
				$("#showPercentage").hide();
				$("#showWeight1").show();
				$("#showWeight").hide();
				}
			
		});
		$( "#showPercentage1" ).click(function() {
			 if($( "#showPercentage1" ).is(':visible') && ($('#showWeight1').is(':visible'))){
				$("#count").hide();
				$("#weight").show();
				$("#percentageWeight").hide();
				$("#percentageUnweight").hide();
				$("#showPercentage1").hide();
				$("#showPercentage").show();
				$("#showWeight1").show();
				$("#showWeight").hide();
				}
			 else if($( "#showPercentage1" ).is(':visible') && ($('#showWeight').is(':visible'))){
					$("#count").show();
					$("#weight").hide();
					$("#percentageWeight").hide();
					$("#percentageUnweight").hide();
					$("#showPercentage1").hide();
					$("#showPercentage").show();
					$("#showWeight1").hide();
					$("#showWeight").show();
					}
		});
		
		$( "#showWeight1" ).click(function() {
			 if($( "#showPercentage1" ).is(':visible') && ($('#showWeight1').is(':visible'))){
				$("#count").hide();
				$("#weight").hide();
				$("#percentageWeight").hide();
				$("#percentageUnweight").show();
				$("#showPercentage1").show();
				$("#showPercentage").hide();
				$("#showWeight1").hide();
				$("#showWeight").show();
				}
			 else if($( "#showPercentage" ).is(':visible') && ($('#showWeight1').is(':visible'))){
					$("#count").show();
					$("#weight").hide();
					$("#percentageWeight").hide();
					$("#percentageUnweight").hide();
					$("#showPercentage1").hide();
					$("#showPercentage").show();
					$("#showWeight1").hide();
					$("#showWeight").show();
					}
		});
	});
	$('#save').click(function(){
		var count1=0;
	    $('[id=status] option:selected').each(function(){ 
			 if(($(this).val())=="-1"){
				 count1++;
			 }
		});
	    
	    if(count1!=0){
		      // invalid
		      $('#status').data("title", "Status is required");
		      $('#status').tooltip('show');
		      return false;
		}else{
	      	// submit the form here
	   		 saveTemplate();
	    }     
	});
	$('#run').click(function(){
		 if($('#incl').val()==""){
		      // invalid
		      $('#incl').data("title", "Provide mean inclusive");
		      $('#incl').tooltip('show');
		      return false;
	    }
		 else if($('#excl').val()==""){
			 // invalid
		      $('#excl').data("title", "Provide mean exclusive");
		      $('#excl').tooltip('show');
		      return false; 
		 }
		 else if($('#medianIncl').val()==""){
			 // invalid
		      $('#medianIncl').data("title", "Provide median exclusive");
		      $('#medianIncl').tooltip('show');
		      return false; 
		 }
		 else if($('#medianExcl').val()==""){
			 // invalid
		      $('#medianExcl').data("title", "Provide median exclusive");
		      $('#medianExcl').tooltip('show');
		      return false; 
		 }
		
	});
</script>