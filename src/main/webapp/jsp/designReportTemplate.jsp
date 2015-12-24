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
	List masterDistributionHierarchyList = (List) LoadApplicationCacheService.applicationCacheObject
			.get(ApplicationConstants.MASTER_DISTRIBUTION_TYPES_HIERARCHY_CACHE_KEY);
	List masterDistributionNonHierarchyList = (List) LoadApplicationCacheService.applicationCacheObject
			.get(ApplicationConstants.MASTER_DISTRIBUTION_TYPES_NON_HIERARCHY_CACHE_KEY);
	List variableNamesList = (List)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.ALL_VARIABLE_NAMES_CACHE_KEY);
	Map masterFunctionsMapCache = (Map) LoadApplicationCacheService.applicationCacheObject
			.get(ApplicationConstants.MASTER_FUNCTIONS_MAP_CACHE_KEY);
	List commentList = (List)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.COMMENT_CACHE_KEY);
	List keyMeasureList = (List)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.KEY_MEASURE_CACHE_KEY);
	List segmentVariableNamesList  = (List)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.SEGMENT_VARIABLE_NAMES_CACHE_KEY);
	List weightVariableList = (List)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.WEIGHT_VARIABLE_CACHE_KEY);
	List keySegmentVariableList = new ArrayList();
	if(keyMeasureList!=null){
	keySegmentVariableList.addAll(keyMeasureList);
	}
	if(segmentVariableNamesList != null){
	keySegmentVariableList.addAll(segmentVariableNamesList);
	}
	
%>

<script type="text/javascript">
	function init() {
		var scrollLocationObj = document.getElementById('scrollLocation');
		document.getElementById("page-wrapper").scrollTop = scrollLocationObj.value;
	}
	
	function uploadMetaData() {
		if(document.getElementById('r3').checked==false){
			document.getElementById('r3').checked=true;
		}
		
	}
	
	
	function uploadData() {
		loadProgress();
		reportTemplateForm.action = "uploadReportMetaData.htm";
		reportTemplateForm.submit();
	}
	
	function addTemplate(categoryIndexValue) {
		reportTemplateForm.action = "addTemplate.htm";
		var categoryIndexObj = document.getElementById('categoryIndex');
		categoryIndexObj.value = categoryIndexValue;
		
		reportTemplateForm.submit();
	}

	function addCategory() {
		//var returnObj = window.showModalDialog("popUpCategoryName.htm", null,
		//		"dialogWidth:300px; dialogHeight:150px; center:yes");
		var returnObj = document.getElementById("textId").value;
		if(returnObj==""){
			$("#catName").modal('show');
		}
	}
	
	var index;
	function editCategory(renameCategoryIndex) {
		index = renameCategoryIndex;
		$("#catEdit").modal('show');
	}
	
	function OK(){
		var returnObj = document.getElementById("textId").value;
		if(returnObj!=""){
			reportTemplateForm.action = "addCategory.htm";
			var categoryNameObj = document.getElementById('categoryName');
	        categoryNameObj.value = returnObj;
			reportTemplateForm.submit();
		}else{
			$('#textId').data("title", "Category Name is required");
		      $('#textId').tooltip('show');
		}
	} 
	
	function edit(){
		var val = document.getElementById("textValue").value;
		if(val!=""){
			var tabNameObj = document.getElementById('tabName');
			var renameCategoryObj = document.getElementById('renameCategory'+index);
			renameCategoryObj.value = val;
			tabNameObj.value = val;
			var catIndexObj = document.getElementById("catIndex"+index);
      	    newLabel = renameCategoryObj.value;
      	    catIndexObj.innerHTML = newLabel;
			return true;
		}
	} 
	
	function createTemplate() {
			reportTemplateForm.action = "createTemplate.htm";
			reportTemplateForm.submit();
	}
	
	function closeTemplate() {
			loadProgress();
			var Obj = document.getElementById('close');
			reportTemplateForm.action = "closeTemplate.htm";
			Obj.disabled=true;
			reportTemplateForm.submit();
	}

	function onChangeFunction(categoryIndex, templateChartInfoIndex) {
		var categoryIndexObj = document.getElementById('categoryIndex');
		categoryIndexObj.value = categoryIndex;
		var templateChartInfoIndexObj = document.getElementById('templateChartInfoIndex');
        templateChartInfoIndexObj.value = templateChartInfoIndex;		
		var scrollLocation = document.getElementById("page-wrapper").scrollTop;
		var scrollLocationObj = document.getElementById('scrollLocation');
		scrollLocationObj.value = scrollLocation;
		reportTemplateForm.action = "onChangeFunction.htm";
		reportTemplateForm.submit();
	}

	function deleteChart(categoryIndex, templateChartInfoIndex) {
		var templateChartInfoIndexObj = document
				.getElementById('templateChartInfoIndex');
		templateChartInfoIndexObj.value = templateChartInfoIndex;
		var categoryIndexObj = document.getElementById('categoryIndex');
		categoryIndexObj.value = categoryIndex;
		loadProgress();
		reportTemplateForm.action = "deleteTemplateChart.htm";
		reportTemplateForm.submit();
	}

	function onChangeReportFilter(obj, idStr) {
		var filtervalueDropDownObj = document.getElementById(idStr);
		while (filtervalueDropDownObj.firstChild) {
			filtervalueDropDownObj
					.removeChild(filtervalueDropDownObj.firstChild);
		}

		var selectedFilterId = obj.options[obj.selectedIndex].value;
		var url = "onChangeReportFilter.htm?selectedFilterId="
				+ selectedFilterId;
		var xmlHttp = new XMLHttpRequest();
		if (window.XMLHttpRequest) { // Mozilla, Safari, ...
			xmlHttp = new XMLHttpRequest();
		} else if (window.ActiveXObject) { // IE
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xmlHttp.open('POST', url, true);
		xmlHttp.onreadystatechange = function() {
			if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
				// success message
				var responseStr = xmlHttp.response;
				if (responseStr != null && responseStr != "") {
					var str = responseStr.split(",");
					var len = str.length;
					for ( var i = 0; i < len; i++) {
						var option = document.createElement('option');
						var str2 = str[i].split("@#@");
						option.value = str2[0];
						option.text =str2[1]; 
						filtervalueDropDownObj.add(option);
					}
				}
			}
		};
		xmlHttp.send(url);
	}
	
	function onChangeDistribution(obj, Str) {
		var isOverAll;
		var filtervalueDropDownObj = document.getElementById(Str);
		while (filtervalueDropDownObj.firstChild) {
			filtervalueDropDownObj
					.removeChild(filtervalueDropDownObj.firstChild);
		}
		if (document.getElementById('r1').checked) {
			isOverAll = document.getElementById('r1').value;
			}
		else if (document.getElementById('r2').checked)
			{
			isOverAll = document.getElementById('r2').value;
			}
		else {
			isOverAll = document.getElementById('r3').value;
		}
		var url = "onChangeDistribution.htm?isOverAll="
				+ isOverAll;
		
		var xmlHttp = new XMLHttpRequest();
		if (window.XMLHttpRequest) { // Mozilla, Safari, ...
			xmlHttp = new XMLHttpRequest();
		} else if (window.ActiveXObject) { // IE
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xmlHttp.open('POST', url, true);
		xmlHttp.onreadystatechange = function() {
			if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
				// success message
				var responseStr = xmlHttp.response;
				if (responseStr != null && responseStr != "") {
					var strArry = responseStr.split(",");
					var len = strArry.length;
					for ( var i = 0; i < len; i++) {
						var option = document.createElement('option');
						var strArry1= strArry[i].split("@");
						option.value = strArry1[0];
						option.text = strArry1[1];
						filtervalueDropDownObj.add(option);
					}
				}
			}
		};
		xmlHttp.send(url);
	}
	
	function inputFocus(i){
	    if(i.value=='Untitled')
	    { 
	    	i.value=""; 
	    	i.style.color="#000"; 
	    	}
	}
	
	function inputBlur(i){
	    if(i.value=="")
	    {
	    	i.value='Untitled'; 
	    	i.style.color="#888"; 
	    	}
	}
	function tabClick(tabName) {
		var tabNameObj = document.getElementById("tabName");
		tabNameObj.value = tabName;
	}
	
	function onChangeMainVariable(index){
		
		var functionId = document.getElementById("functionId"+index).value;
		var chartTypeObj =  document.getElementById("chartTypeId"+index);
		var count = 0;
		var mainVariableObj = document.getElementById("mainVariableId"+index);
		for(var i=0;i<mainVariableObj.options.length;i++){
			if(mainVariableObj.options[i].selected)
				count++;
		}
		
		var segmentVariableObj =document.getElementById('segmentVariableId'+index);
		
		/*if(mainVariableObj.value == -1 ){
    		alert("Select atleast one main variable");
    	}*/
        if(functionId==1 && count>1 && segmentVariableObj.options[segmentVariableObj.selectedIndex].value != -1 ){
    		alert("Cannot select segment variable ");
	     		if(segmentVariableObj.options[segmentVariableObj.selectedIndex].value !=-1){
	     			segmentVariableObj.value=-1;
	     		}
    	}
       
        if(functionId==2 && count>=1){
        		var chartObj =document.getElementById('numberOfCharts');
        		chartObj.disabled=true;
    	}
        
        if(functionId==2 && count==1 && segmentVariableObj.options[segmentVariableObj.selectedIndex].value != -1 && mainVariableObj.value != -1 ){
        	    
	        	var chartObj =document.getElementById('numberOfCharts');
	        	chartObj.disabled=false;
	    }
        if(functionId==2 && count>=1 && segmentVariableObj.options[segmentVariableObj.selectedIndex].value == -1 && mainVariableObj.value == -1){
        	var chartObj =document.getElementById('numberOfCharts');
        	chartObj.disabled=true;
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
				
				var option = document.createElement('option');
				option.text = "Table View";
				option.value = "TABLE_VIEW";
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
				
				var option = document.createElement('option');
				option.text = "Table View";
				option.value = "TABLE_VIEW";
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
				
				var option = document.createElement('option');
				option.text = "Table View";
				option.value = "TABLE_VIEW";
				chartTypeObj.add(option);
		    }
		}
		if(functionId==2){
			if(document.getElementById('benchMarkId'+index).options[document.getElementById('benchMarkId'+index).selectedIndex].value == -1){
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
				
				var option = document.createElement('option');
				option.text = "Table View";
				option.value = "TABLE_VIEW";
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
				
				var option = document.createElement('option');
				option.text = "Table View";
				option.value = "TABLE_VIEW";
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
				
				var option = document.createElement('option');
				option.text = "Table View";
				option.value = "TABLE_VIEW";
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
				
				var option = document.createElement('option');
				option.text = "Table View";
				option.value = "TABLE_VIEW";
				chartTypeObj.add(option);
			}
			}
			else{
				if(count==1 && segmentVariableObj.options[segmentVariableObj.selectedIndex].value == -1){
					while (chartTypeObj.firstChild) {
		    			chartTypeObj.removeChild(chartTypeObj.firstChild);
					}
		    		var option = document.createElement('option');
					option.text = "Select";
					option.value = "-1";
					chartTypeObj.add(option);
			
					var option = document.createElement('option');
					option.text = "Column Line Chart";
					option.value = "Column2D";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "Table View";
					option.value = "TABLE_VIEW";
					chartTypeObj.add(option);
				}else{
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
					
					var option = document.createElement('option');
					option.text = "Dual Y Line Chart";
					option.value = "MSCombiDY2D";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "Table View";
					option.value = "TABLE_VIEW";
					chartTypeObj.add(option);
					
				}
	    		
			}
		}
		if(functionId==5){
					
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
				option.text = "Wordcloud";
				option.value = "Wordcloud";
				chartTypeObj.add(option);
					
		}
	}
	
	function onChangeSegmentVariable(index){
		var functionId = document.getElementById("functionId"+index).value;
		var chartTypeObj = document.getElementById("chartTypeId"+index);
		var count = 0;
		var mainVariableObj = document.getElementById("mainVariableId"+index);
		for(var i=0;i<mainVariableObj.options.length;i++){
			if(mainVariableObj.options[i].selected)
				count++;
		}
		
   
		var segmentVariableObj =document.getElementById('segmentVariableId'+index);
		 if(functionId==1 && count>1 && segmentVariableObj.options[segmentVariableObj.selectedIndex].value != -1 ){
		   		alert("Cannot select segment variable ");
		   		 var segmentId =segmentVariableObj.options[segmentVariableObj.selectedIndex].value;
			     		if(segmentId !=-1){
			     			segmentVariableObj.value=-1;
			     		}
		   	    }
		 if(functionId==2 && mainVariableObj.options[mainVariableObj.selectedIndex].value == -1 
				 && segmentVariableObj.options[segmentVariableObj.selectedIndex].value != -1){
	        	var chartObj =document.getElementById('numberOfCharts');
	        	chartObj.disabled=true;
	    }
		 if(functionId==2 && count>=1 && segmentVariableObj.options[segmentVariableObj.selectedIndex].value != -1 && mainVariableObj.value != -1){
		        	var chartObj =document.getElementById('numberOfCharts');
		        	chartObj.disabled=false;
		    }
		    if(functionId==2 && count>=1 && segmentVariableObj.options[segmentVariableObj.selectedIndex].value == -1){
		    	var chartObj =document.getElementById('numberOfCharts');
		    	chartObj.disabled=true;
			}
		    
		if(functionId == 1){
			if(count==1 && document.getElementById('segmentVariableId'+index).options[document.getElementById('segmentVariableId'+index).selectedIndex].value == -1){
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
				
				var option = document.createElement('option');
				option.text = "Table View";
				option.value = "TABLE_VIEW";
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
				
				var option = document.createElement('option');
				option.text = "Table View";
				option.value = "TABLE_VIEW";
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
				
				var option = document.createElement('option');
				option.text = "Table View";
				option.value = "TABLE_VIEW";
				chartTypeObj.add(option);
		    }
		}
		if(functionId==2){
			if(document.getElementById('benchMarkId'+index).options[document.getElementById('benchMarkId'+index).selectedIndex].value == -1){
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
				
				var option = document.createElement('option');
				option.text = "Table View";
				option.value = "TABLE_VIEW";
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
				
				var option = document.createElement('option');
				option.text = "Table View";
				option.value = "TABLE_VIEW";
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
				
				var option = document.createElement('option');
				option.text = "Table View";
				option.value = "TABLE_VIEW";
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
				
				var option = document.createElement('option');
				option.text = "Table View";
				option.value = "TABLE_VIEW";
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
					option.text = "Column Line Chart";
					option.value = "Column2D";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "Table View";
					option.value = "TABLE_VIEW";
					chartTypeObj.add(option);
				} else {
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
					
					var option = document.createElement('option');
					option.text = "Dual Y Line Chart";
					option.value = "MSCombiDY2D";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "Table View";
					option.value = "TABLE_VIEW";
					chartTypeObj.add(option);
					
				}
	    		
			}
		}
		if(functionId==5){
			
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
			option.text = "Wordcloud";
			option.value = "Wordcloud";
			chartTypeObj.add(option);
			
		}
	}
	function onChangeBenchMark(index){
		var functionId = document.getElementById("functionId"+index).value;
		var chartTypeObj = document.getElementById("chartTypeId"+index);
		var count = 0;
		var mainVariableObj = document.getElementById("mainVariableId"+index);
		for(var i=0;i<mainVariableObj.options.length;i++){
			if(mainVariableObj.options[i].selected)
				count++;
		}
		var segmentVariableObj =document.getElementById('segmentVariableId'+index);
		if(functionId == 2){
			if(document.getElementById('benchMarkId'+index).options[document.getElementById('benchMarkId'+index).selectedIndex].value != -1){
				if(segmentVariableObj.options[segmentVariableObj.selectedIndex].value == -1 && count == 1){
					while (chartTypeObj.firstChild) {
		    			chartTypeObj.removeChild(chartTypeObj.firstChild);
					}
		    		var option = document.createElement('option');
					option.text = "Select";
					option.value = "-1";
					chartTypeObj.add(option);
			
					var option = document.createElement('option');
					option.text = "Column Line Chart";
					option.value = "Column2D";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "Table View";
					option.value = "TABLE_VIEW";
					chartTypeObj.add(option);
				} else{
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
					
					var option = document.createElement('option');
					option.text = "Dual Y Line Chart";
					option.value = "MSCombiDY2D";
					chartTypeObj.add(option);
					
					var option = document.createElement('option');
					option.text = "Table View";
					option.value = "TABLE_VIEW";
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
					
					var option = document.createElement('option');
					option.text = "Table View";
					option.value = "TABLE_VIEW";
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
					
					var option = document.createElement('option');
					option.text = "Table View";
					option.value = "TABLE_VIEW";
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
					
					var option = document.createElement('option');
					option.text = "Table View";
					option.value = "TABLE_VIEW";
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
					
					var option = document.createElement('option');
					option.text = "Table View";
					option.value = "TABLE_VIEW";
					chartTypeObj.add(option);
				}
			}
		}
	}
</script>
<body onLoad="init();">
	<form:form method="post" action="reportTemplate.htm"
		commandName="model" enctype="multipart/form-data" name="reportTemplateForm">
		<form:hidden path="categoryIndex" id="categoryIndex" />
		<form:hidden path="templateChartInfoIndex" id="templateChartInfoIndex" />
		<form:hidden path="categoryName" id="categoryName" />
		<form:hidden path="scrollLocation" id="scrollLocation" />
		<form:hidden path="tabName" id="tabName" />
		<!--vertical Tabs-->
		<table width=100% cellpadding="5px" style="margin-top:60px">
			<tr>
				<td align="left" width="33%">&nbsp;&nbsp;
				<input type="button" value="Add Category"
					class="btn btn-info btn-sm" onClick="addCategory();" />
				</td>
				<td align='center' width="33%">
				<form:input id="reportTitle" class="form-control" style="color:#888;width:250px;height:30px;text-align:center" path="templateChart.reportTitle" onfocus="inputFocus(this)" onblur="inputBlur(this)" />
					</td>
				<td align="left" width="33%">
				</td>
			</tr>
		</table>
		<table width=100%>
			<tr>
				<td>
					<div class="tabbable tabs-left" style="margin-left:5px;margin-top:0px;">
			<ul class="nav nav-tabs" style="width:178px;">
				<c:forEach var="catObject"
					items="${model.templateChart.templateChartCategories}"
					varStatus="loop">
					<form:hidden path="templateChart.templateChartCategories[${loop.index}].categoryName" id="renameCategory${loop.index}" />
					<c:if test="${(model.tabName ==null || model.tabName =='') && loop.index==0}">
						<li class="active" ><a data-toggle="tab" id="catIndex${loop.index}" href="#${loop.index}"  onclick="tabClick('${catObject.categoryName}');">${catObject.categoryName}  <img src="./images/Pencil.png" onClick="editCategory(${loop.index});" class="pull-right" width="15" height="15" /></a>
						</li>
					</c:if>
					<c:if test="${(model.tabName ==null || model.tabName =='') && loop.index>0}">
						<li ><a data-toggle="tab" id="catIndex${loop.index}"  href="#${loop.index}" onclick="tabClick('${catObject.categoryName}');">${catObject.categoryName} <img src="./images/Pencil.png" onClick="editCategory(${loop.index});" class="pull-right" width="15" height="15" /> </a>
						</li>
					</c:if>
					
					<c:if test="${model.tabName !=null && model.tabName != '' && model.tabName != catObject.categoryName}">
						<li  ><a data-toggle="tab" id="catIndex${loop.index}" href="#${loop.index}" onclick="tabClick('${catObject.categoryName}');">${catObject.categoryName} <img src="./images/Pencil.png" onClick="editCategory(${loop.index});" class="pull-right" width="15" height="15" /> </a>
						</li>
					</c:if>
				
					<c:if test="${model.tabName !=null && model.tabName != '' && model.tabName == catObject.categoryName}">
						<li class="active"><a data-toggle="tab" id="catIndex${loop.index}" href="#${loop.index}" onclick="tabClick('${catObject.categoryName}');">${catObject.categoryName} <img src="./images/Pencil.png" onClick="editCategory(${loop.index});"  class="pull-right" width="15" height="15" /> </a>
						</li>
					</c:if>
				</c:forEach>
			</ul>

			<div class="tab-content">
				<c:if test="${model.templateChart.templateChartCategories !=null}">
					<c:forEach var="catObject"
						items="${model.templateChart.templateChartCategories}"
						varStatus="outerLoop">
						<c:if test="${(model.tabName ==null || model.tabName =='') && outerLoop.index==0}">
							<div id="${outerLoop.index}" class="tab-pane active">
						</c:if>
						<c:if test="${(model.tabName ==null || model.tabName =='') && outerLoop.index>0}">
						<div id="${outerLoop.index}" class="tab-pane">
						</c:if>
						
						<c:if test="${model.tabName !=null && model.tabName != '' && model.tabName != catObject.categoryName}">
							<div id="${outerLoop.index}" class="tab-pane">
						</c:if>
						<c:if test="${model.tabName !=null && model.tabName != '' && model.tabName == catObject.categoryName}">
							<div id="${outerLoop.index}" class="tab-pane active">
						</c:if>
							<div id="page-wrapper" style="height: 300px; overflow-x: auto;">
								
								<div class="row">
									<div style="width:100%;margin:5px 0px;">

										<c:forEach var="chartInfoObj"
											items="${catObject.templateChartInfoList}"
											varStatus="innerLoop">
											<c:if test="${chartInfoObj.deleted==false}">
												<div class="panel panel-default">
													<div class="panel-heading" style="padding:5px;">
														<form:input path="templateChart.templateChartCategories[${outerLoop.index}].templateChartInfoList[${innerLoop.index}].chartTitle"
																		id="chartTitle${innerLoop.index}${outerLoop.index}" style="color:#888;margin-top: -6px; width: 250px; height: 20px" onfocus="inputFocus(this)" onblur="inputBlur(this)"/>
														<div class="pull-right">
															<div class="btn-group">
																<button type="button"
																	class="btn btn-default btn-xs dropdown-toggle"
																	data-toggle="dropdown">
																	Actions <span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right" role="menu">
																	<li><a href="#" onclick="deleteChart('${outerLoop.index}','${innerLoop.index}')">Delete</a></li>
																</ul>
															</div>
														</div>
													</div>
													<!-- /.panel-heading -->
													<div class="panel-body">
																<div id="components"><label>Function:</label><br/>
																<bi2itag:DropDownDisplayTag
																		defaultValue="Select"
																		script="onChange=\"onChangeFunction('${outerLoop.index}','${innerLoop.index}');\""
																		id="functionId${innerLoop.index}${outerLoop.index}"
																		selectedValue="${chartInfoObj.functionId}"
																		list="<%=masterFunctionList%>"
																		path="templateChart.templateChartCategories[${outerLoop.index}].templateChartInfoList[${innerLoop.index}].functionId" />
																
																</div>
																<c:if test="${chartInfoObj.functionId==8}">
																 <form:checkbox path="templateChart.templateChartCategories[${outerLoop.index}].templateChartInfoList[${innerLoop.index}].crossTabValue" id="crossTabValue${innerLoop.index}${outerLoop.index}" value="${innerLoop.index}"  />
																</c:if>
																
																<c:if
																		test="${chartInfoObj.functionId==8}">
																		<div id="components">																											
																		<label>Mean inclusive:</label>
																		<form:input id="incl${innerLoop.index}${outerLoop.index}" class="form-control" style="color:#888;width:200px;height:30px;" path="templateChart.templateChartCategories[${outerLoop.index}].templateChartInfoList[${innerLoop.index}].meanIncl"  />
																		<br/>
																		<label>Mean Exclusive:</label>
																		<form:input id="excl${innerLoop.index}${outerLoop.index}" class="form-control" style="color:#888;width:200px;height:30px;" path="templateChart.templateChartCategories[${outerLoop.index}].templateChartInfoList[${innerLoop.index}].meanExcl"  />
																	    <br/>
																	    <label>Median inclusive:</label>
																		<form:input id="medianIncl${innerLoop.index}${outerLoop.index}" class="form-control" style="color:#888;width:200px;height:30px;" path="templateChart.templateChartCategories[${outerLoop.index}].templateChartInfoList[${innerLoop.index}].medianIncl"  />
																		<br/>
																		<label>Median Exclusive:</label>
																		<form:input id="medianExcl${innerLoop.index}${outerLoop.index}" class="form-control" style="color:#888;width:200px;height:30px;" path="templateChart.templateChartCategories[${outerLoop.index}].templateChartInfoList[${innerLoop.index}].medianExcl"  />
																		</div>
																		
																	</c:if>
													
																<c:if
																		test="${chartInfoObj.functionId==2}">
																		<div id="components"><label> Bench Mark:</label><br/>
																		<bi2itag:DropDownDisplayTag defaultValue="Select"
																			script="onChange=\"onChangeBenchMark('${innerLoop.index}${outerLoop.index}');\""
																			id="benchMarkId${innerLoop.index}${outerLoop.index}"
																			selectedValue="${chartInfoObj.benchMark}"
																			list="<%=masterBenchMarkList%>"
																			path="templateChart.templateChartCategories[${outerLoop.index}].templateChartInfoList[${innerLoop.index}].benchMark" />
																		</div>
																</c:if>
																	<div id="components">
																	
																	<c:if
																		test="${chartInfoObj.functionId==1 || chartInfoObj.functionId==2}">
																		<label>Main Variable:</label>
																	</c:if> <c:if
																		test="${chartInfoObj.functionId==6 || chartInfoObj.functionId==7}">
																		<label>Key Measure Variable:</label>
																	</c:if>
																	<c:if
																		test="${chartInfoObj.functionId==8}">
																		<label>Stub Variable:</label>
																	</c:if>
																	 <c:if
																		test="${chartInfoObj.functionId==3 || chartInfoObj.functionId==4 || chartInfoObj.functionId==5 || chartInfoObj.functionId==9 }">
																		<label>Comment Variable:</label>
																	</c:if></br>
																	<c:if
																		test="${chartInfoObj.functionId==1}">
																		<bi2itag:DropDownDisplayTag    defaultValue="Select"
																			multipleValue="true" 
																			height="180px"
																			script="onChange=\"onChangeMainVariable('${innerLoop.index}${outerLoop.index}');\""
																			id="mainVariableId${innerLoop.index}${outerLoop.index}"
																			selectedValue="${chartInfoObj.xAxis}"
																			list="<%=variableNamesList%>"
																			path="templateChart.templateChartCategories[${outerLoop.index}].templateChartInfoList[${innerLoop.index}].xAxisArray" />
																	</c:if>
																	
																	<c:if
																		test="${chartInfoObj.functionId==2}">
																		<bi2itag:DropDownDisplayTag    defaultValue="Select"
																			multipleValue="true"
																			height="180px" 
																			script="onChange=\"onChangeMainVariable('${innerLoop.index}${outerLoop.index}');\""
																			id="mainVariableId${innerLoop.index}${outerLoop.index}"
																			selectedValue="${chartInfoObj.xAxis}"
																			list="<%=keyMeasureList%>"
																			path="templateChart.templateChartCategories[${outerLoop.index}].templateChartInfoList[${innerLoop.index}].xAxisArray" />
																	</c:if> 
																	<c:if
																		test="${chartInfoObj.functionId==3 || chartInfoObj.functionId==5 || chartInfoObj.functionId==9}">
																		<bi2itag:DropDownDisplayTag defaultValue="Select"
																			id="commentVariableId${innerLoop.index}${outerLoop.index}"
																			selectedValue="${chartInfoObj.xAxis}"
																			list="<%=commentList%>"
																			path="templateChart.templateChartCategories[${outerLoop.index}].templateChartInfoList[${innerLoop.index}].xAxisArray" />
																	</c:if>
																	<c:if
																		test="${chartInfoObj.functionId==4}">
																		<bi2itag:DropDownDisplayTag defaultValue="Select"
																			multipleValue="true"
																			height="180px" 
																			id="commentVariableId${innerLoop.index}${outerLoop.index}"
																			selectedValue="${chartInfoObj.xAxis}"
																			list="<%=commentList%>"
																			path="templateChart.templateChartCategories[${outerLoop.index}].templateChartInfoList[${innerLoop.index}].xAxisArray" />
																	</c:if>
																	<c:if
																		test="${chartInfoObj.functionId==6 || chartInfoObj.functionId==7 || chartInfoObj.functionId==8}">
																		<bi2itag:DropDownDisplayTag defaultValue="Select"
																			id="keyMeasureId${innerLoop.index}${outerLoop.index}"
																			selectedValue="${chartInfoObj.xAxis}"
																			list="<%=keyMeasureList%>"
																			path="templateChart.templateChartCategories[${outerLoop.index}].templateChartInfoList[${innerLoop.index}].xAxisArray" />
																	</c:if>
																	</div>
														
															<div id="components">
																<c:if
																		test="${chartInfoObj.functionId==1 || chartInfoObj.functionId==2 || chartInfoObj.functionId==3 || chartInfoObj.functionId==4 || chartInfoObj.functionId==5}">
																		
																		<label>Segment Variable:</label><br/>
																	</c:if> <c:if
																		test="${chartInfoObj.functionId==6 || chartInfoObj.functionId==7}">
																		
																		<label>Potential Key Variables:</label><br/>
																	</c:if>
																	<c:if
																		test="${chartInfoObj.functionId==8}">
																		
																		<label>Skeleton Variables:</label><br/>
																	</c:if>
																	<c:if test="${chartInfoObj.functionId==1 || chartInfoObj.functionId==2}">
																		<bi2itag:DropDownDisplayTag defaultValue="Select"
																			selectedValue="${chartInfoObj.yAxis}"
																			id="segmentVariableId${innerLoop.index}${outerLoop.index}"
																			script="onChange=\"onChangeSegmentVariable('${innerLoop.index}${outerLoop.index}');\""
																			list="<%=variableNamesList%>"
																			path="templateChart.templateChartCategories[${outerLoop.index}].templateChartInfoList[${innerLoop.index}].yAxisArray" />
																	</c:if>
																	<c:if test="${chartInfoObj.functionId==3 || chartInfoObj.functionId==4 || chartInfoObj.functionId==5}">
																		<bi2itag:DropDownDisplayTag defaultValue="Select"
																			selectedValue="${chartInfoObj.yAxis}"
																			id="segmentVariableId${innerLoop.index}${outerLoop.index}"
																			script="onChange=\"onChangeSegmentVariable('${innerLoop.index}${outerLoop.index}');\""
																			list="<%=segmentVariableNamesList%>"
																			path="templateChart.templateChartCategories[${outerLoop.index}].templateChartInfoList[${innerLoop.index}].yAxisArray" />
																	</c:if>
																	 <c:if
																		test="${chartInfoObj.functionId==6 || chartInfoObj.functionId==7}">
																		<bi2itag:DropDownDisplayTag defaultValue="Select"
																			multipleValue="true"
																			height="180px"
																			id="potentialKeyId${innerLoop.index}${outerLoop.index}"
																			selectedValue="${chartInfoObj.yAxis}"
																			list="<%=keyMeasureList%>"
																			path="templateChart.templateChartCategories[${outerLoop.index}].templateChartInfoList[${innerLoop.index}].yAxisArray" />
																	</c:if>
																	<c:if
																		test="${chartInfoObj.functionId==8}">
																		<bi2itag:DropDownDisplayTag defaultValue="Select"
																			multipleValue="true"
																			height="180px"
																			id="potentialKeyId${innerLoop.index}${outerLoop.index}"
																			selectedValue="${chartInfoObj.yAxis}"
																			list="<%=keySegmentVariableList%>"
																			path="templateChart.templateChartCategories[${outerLoop.index}].templateChartInfoList[${innerLoop.index}].yAxisArray" />
																	</c:if>
																	<c:if
																		test="${chartInfoObj.functionId==8}">
																		<label>Weight Variables:</label><br/>
																		<bi2itag:DropDownDisplayTag defaultValue="Select"
																			selectedValue="${chartInfoObj.weightVariable}"
																			script="onChange=\"onChangeWeightVariable();\""
																			list="<%=weightVariableList%>"
																			path="templateChart.templateChartCategories[${outerLoop.index}].templateChartInfoList[${innerLoop.index}].weightVariable" />
																	</c:if>
																	</div>
																		<c:if test="${chartInfoObj.functionId==1 || chartInfoObj.functionId==2 || chartInfoObj.functionId==5}">
																	<div id="components">
																		<label>Chart Type:</label><br/>
																	
																		<bi2itag:DropDownDisplayTag id="chartTypeId${innerLoop.index}${outerLoop.index}" defaultValue="Select"
																			selectedValue="${chartInfoObj.chartTypeId}"
																			list="${chartInfoObj.chartTypesList}"
																			path="templateChart.templateChartCategories[${outerLoop.index}].templateChartInfoList[${innerLoop.index}].chartTypeId" />
																	</div>
																	</c:if>
																	
																	<c:if
																		test="${chartInfoObj.functionId==1 || chartInfoObj.functionId==2}">
																		<label>Filter variable:</label><br/>
																	</c:if> 
																	
																	<c:if
																		test="${chartInfoObj.functionId==7}">
																		<label>Segment variable:</label><br/>
																	</c:if>
																	<c:if
																		test="${chartInfoObj.functionId==6}">
																		<label>Reference Group Variable:</label><br/>
																	</c:if>
																	
																
																	
																	<c:if test="${chartInfoObj.functionId==1 || chartInfoObj.functionId==2 || chartInfoObj.functionId==6 || chartInfoObj.functionId==7}">
																		<bi2itag:DropDownDisplayTag defaultValue="Select"
																			selectedValue="${chartInfoObj.filter}"
																			list="<%=variableNamesList%>"
																			path="templateChart.templateChartCategories[${outerLoop.index}].templateChartInfoList[${innerLoop.index}].filter" />
																	
																		<c:if test="${chartInfoObj.functionId==2}">
																			<form:input id="numberOfCharts${innerLoop.index}${outerLoop.index}" class="form-control" style="color:#888;width:200px;height:30px;" path="templateChart.templateChartCategories[${outerLoop.index}].templateChartInfoList[${innerLoop.index}].numberOfCharts"  />
																		</c:if>
																		
																	</c:if>
														
													</div>
													<!-- /.panel-body -->
												</div>
												<!-- /.panel -->
											</c:if>
										</c:forEach>
									</div>
								</div>
								<div class="row" style="margin-top:-15px">
									<div class="pull-right">
										<input type="button" Value="Add Chart"
											class="btn btn-info btn-sm"
											onClick="addTemplate('${outerLoop.index}');">
									</div>
								</div>
							</div>
						</div>
					</c:forEach>
				</c:if>
			</div>
		</div>
	</td>
	<td align="center" style="padding:5px;width:10%;">
	<div class="panel panel-info" style="width: 165px;height:150px">
					<div class="panel-heading" style="padding: 5px 5px;text-align:left">Report Filter:</div>
					<div class="panel-body">
				<table cellspacing="5px">
				<tr>
					<td><bi2itag:DropDownDisplayTag defaultValue="Select"
							script="onChange=\"onChangeReportFilter(this,'filterValues1');\""
							selectedValue="${model.templateChart.filter1}"
							classAttribute="small-width"
							list="<%=variableNamesList%>" path="templateChart.filter1" /></td>
				</tr>
				<tr>
					<td><bi2itag:DropDownDisplayTag id="filterValues1"
							multipleValue="true"
							classAttribute="small-width"
							selectedValue="${model.templateChart.filterValues1}"
							list="${model.filterValues1}"
							path="templateChart.filterValuesArray1" /></td>
				</tr>
			</table>
			</div>
			</div>
		
			<div class="panel panel-info" style="width: 165px;height:150px">
					<div class="panel-heading" style="padding: 5px 5px;;text-align:left">Report Filter:</div>
					<div class="panel-body">
			<table  cellspacing="2px">
				<tr>
					<td align="left"><bi2itag:DropDownDisplayTag
							defaultValue="Select"
							script="onChange=\"onChangeReportFilter(this,'filterValues2');\""
							selectedValue="${model.templateChart.filter2}"
							classAttribute="small-width"
							list="<%=variableNamesList%>" path="templateChart.filter2" /></td>
				</tr>
				<tr>
					<td align="left"><bi2itag:DropDownDisplayTag
							id="filterValues2" multipleValue="true"
							selectedValue="${model.templateChart.filterValues2}"
							list="${model.filterValues2}"
							classAttribute="small-width"
							path="templateChart.filterValuesArray2" /></td>
				</tr>
			</table>
			</div>
			</div>

<div class="panel panel-info" style="width: 165px;height:150px">
					<div class="panel-heading" style="padding: 5px 5px;;text-align:left">Report Filter:</div>
					<div class="panel-body">			
			<table  cellspacing="5px">
				<tr>
					<td align="left"><bi2itag:DropDownDisplayTag
							defaultValue="Select"
							script="onChange=\"onChangeReportFilter(this,'filterValues3');\""
							selectedValue="${model.templateChart.filter3}"
							classAttribute="small-width"
							list="<%=variableNamesList%>" path="templateChart.filter3" /></td>
				</tr>
				<tr>
					<td align="left"><bi2itag:DropDownDisplayTag
							id="filterValues3" multipleValue="true"
							selectedValue="${model.templateChart.filterValues3}"
							list="${model.filterValues3}"
							classAttribute="small-width"
							path="templateChart.filterValuesArray3" /></td>

				</tr>
			</table>
			</div>
			</div>
	</td>
	</tr>
	<tr>
				<td colspan="2"><center>
						<input type="button" id="save" value="Save" class="btn btn-info btn-xs"
						> <input type="button"
							value="Close" id="close" class="btn btn-info btn-xs"
							onClick="closeTemplate();">
					</center></td>
			</tr>
</table>
	
		<c:if test="${model.templateChart.templateChartCategories !=null}">
		
			<div
				style="position: absolute; left: 8px; top: 400px; width: 200px; z-index: 0">
				
				
				<div class="panel panel-info" style="width: 220px">
				
					<div class="panel-heading" style="text-align:left">Distribution List</div>
					
					<div class="panel-body">
					
					<form:radiobutton  path="templateChart.reportType" id="r1"  onchange="onChangeDistribution(this,'Hierarchy')"  value="N"/>Manager Report<br>
					 <form:radiobutton  path="templateChart.reportType" id="r2"  onchange="onChangeDistribution(this,'Hierarchy')" value="Y"/>Functional Report 
						<table width="100%" cellspacing="5px">
							<tr>
								<td><bi2itag:DropDownDisplayTag 
										multipleValue="true" id="Hierarchy"
										classAttribute="small-width"
										selectedValue="${model.selectedDistributionIdStr}"
										list="${model.distributionListObj}"
										path="selectedDistributionId" />
										</td>
										</tr>
						</table>
						<form:radiobutton  path="templateChart.reportType" id="r3" onchange="onChangeDistribution(this,'data')" Value="U" />Upload Metadata
						 <table border="0px" style="cellpadding:10px">
								<tr>
									<td>
										<div class="form-group">
											<input type="file" id="data" name="MetaData" onclick="uploadMetaData();" style="width: 200px">
										</div>
									</td>
								</tr>
								
							</table> 
								
					</div>
				</div>

			</div>
			
		</c:if>
	</form:form>
	<script type="text/javascript">

	
	$('#save').click(function(){
		 var count=0;
		 $('[id^=chartTypeId] option:selected').each(function(){ 
			 if(($(this).val())=="-1"){
				 count++;
			 }
		});
		var count1=0;
		var c=0,c1=0,c2=0,c3=0;
	    $('[id^=functionId] option:selected').each(function(){ 
			 if(($(this).val())=="-1"){
				 count1++;
			 }
			 else if((($(this).val())=="1") || (($(this).val())=="2")){
				 $('[id^=mainVariableId]').each(function(){ 
					 if(($(this).find(":selected").text()=="")){
						 c++;
					 }
				});
			 }
			 else if((($(this).val())=="3") || (($(this).val())=="4") || (($(this).val())=="5")){
				 $('[id^=commentVariableId]').each(function(){ 
					 if(($(this).val())=="-1"){
						 c1++;
					 }
				});
			 }
			 else if((($(this).val())=="6")){
				 $('[id^=keyMeasureId]').each(function(){ 
					 if(($(this).val())=="-1"){
						 c2++;
					 }
				});
				 $('[id^=potentialKeyId]').each(function(){ 
					 if(($(this).find(":selected").text()=="")){
						 c3++;
					 }
				});
			 }
		});
	    var count2=0;
	    $('[id^=chartTitle]').each(function(){ 
			 if(($(this).val())=="Untitled"){
				 count2++;
			 }
		});
	   
	    if($('#reportTitle').val()=="Untitled"){
		      // invalid
		      $('#reportTitle').data("title", "Report Title is required");
		      $('#reportTitle').tooltip('show');
		      return false;
	    }
	    /* else if($('#Hierarchy').find(":selected").text()==""){
		      // invalid
		      $('#Hierarchy').data("title", "Hierarchy is required");
		      $('#Hierarchy').tooltip('show');
		      return false;
		} */else if(count!=0){
	          $('#myModal').modal('show');
	    }else if(count1!=0){
		      $('#myModal1').modal('show');
	    }else if(count2!=0){
		      $('#myModal2').modal('show');
	    }
	   else if(c!=0){
		      $('#mainVar').modal('show');
	    }else if(c1!=0){
		      $('#commentVar').modal('show');
	    }else if(c2!=0){
		      $('#keyMeasure').modal('show');
	    }else if(c3!=0){
		      $('#potentialKey').modal('show');
	    }else{
	      	// submit the form here
	   		 createTemplate();
	    }     
	});
  </script>

<div class="modal fade"  id="myModal" style="overflow-y:auto">
  <div class="modal-dialog modal-sm" style="margin-top:160px">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title" id="myModalLabel">Validation</h4>
      </div>
      <div class="modal-body">
        One of the chart Type is not selected
      </div>
      <div class="modal-footer" style="height:15px">
        <center> <button type="button" style="margin-top:-20px" class="btn btn-info btn-xs" data-dismiss="modal">Close</button></center>
      </div>
    </div>
  </div>
</div>
<div class="modal fade"  id="myModal1" style="overflow-y:auto">
  <div class="modal-dialog modal-sm" style="margin-top:160px">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title" id="myModalLabel">Validation</h4>
      </div>
      <div class="modal-body">
        One of the function is not selected
      </div>
      <div class="modal-footer" style="height:15px">
        <center> <button type="button" style="margin-top:-20px" class="btn btn-info btn-xs" data-dismiss="modal">Close</button></center>
      </div>
    </div>
  </div>
</div>
<div class="modal fade"  id="myModal2" style="overflow-y:auto">
  <div class="modal-dialog modal-sm" style="margin-top:160px">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title" id="myModalLabel">Validation</h4>
      </div>
      <div class="modal-body">
        One of the chart title is not given
      </div>
      <div class="modal-footer" style="height:15px">
        <center> <button type="button" style="margin-top:-20px" class="btn btn-info btn-xs" data-dismiss="modal">Close</button></center>
      </div>
    </div>
  </div>
</div>
<div class="modal fade"  id="catName" style="overflow-y:auto">
  <div class="modal-dialog modal-sm" style="margin-top:160px;width:340px">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title" id="myModalLabel">Category Name</h4>
      </div>
      <div class="modal-body">
      <table><tr>
      <td><label>Enter Category Name</label></td>
      <td>&nbsp</td>
      <td><input type="text" id="textId"></td></tr></table>
      </div>
      <div class="modal-footer" style="height:15px">
        <center> 
        <button type="button" style="margin-top:-20px;width:45px" class="btn btn-info btn-xs" onClick="OK();">Ok</button>
        <button type="button" style="margin-top:-20px" class="btn btn-info btn-xs" data-dismiss="modal">Cancel</button>
        </center>
      </div>
    </div>
  </div>
</div>
<div class="modal fade"  id="mainVar" style="overflow-y:auto">
  <div class="modal-dialog modal-sm" style="margin-top:160px">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title" id="myModalLabel">Validation</h4>
      </div>
      <div class="modal-body">
        Main Variable is not selected for one of the function
      </div>
      <div class="modal-footer" style="height:15px">
        <center> <button type="button" style="margin-top:-20px" class="btn btn-info btn-xs" data-dismiss="modal">Close</button></center>
      </div>
    </div>
  </div>
</div>
<div class="modal fade"  id="commentVar" style="overflow-y:auto">
  <div class="modal-dialog modal-sm" style="margin-top:160px">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title" id="myModalLabel">Validation</h4>
      </div>
      <div class="modal-body">
        Comment Variable is not selected for one of the function
      </div>
      <div class="modal-footer" style="height:15px">
        <center> <button type="button" style="margin-top:-20px" class="btn btn-info btn-xs" data-dismiss="modal">Close</button></center>
      </div>
    </div>
  </div>
</div>
<div class="modal fade"  id="keyMeasure" style="overflow-y:auto">
  <div class="modal-dialog modal-sm" style="margin-top:160px">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title" id="myModalLabel">Validation</h4>
      </div>
      <div class="modal-body">
        Key Measure Variable is not selected for one of the function
      </div>
      <div class="modal-footer" style="height:15px">
        <center> <button type="button" style="margin-top:-20px" class="btn btn-info btn-xs" data-dismiss="modal">Close</button></center>
      </div>
    </div>
  </div>
</div>
<div class="modal fade"  id="potentialKey" style="overflow-y:auto">
  <div class="modal-dialog modal-sm" style="margin-top:160px">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title" id="myModalLabel">Validation</h4>
      </div>
      <div class="modal-body">
        Potential Key Measure is not selected for one of the function
      </div>
      <div class="modal-footer" style="height:15px">
        <center> <button type="button" style="margin-top:-20px" class="btn btn-info btn-xs" data-dismiss="modal">Close</button></center>
      </div>
    </div>
  </div>
</div>
<div  class="modal fade" id="catEdit" >
  <div class="modal-dialog modal-sm" style="margin-left:20px;width:300px">
    <div class="modal-content">
      <div class="modal-body">
      <table><tr>
      <td><label>Rename</label></td>
      <td>&nbsp</td>
      <td><input type="text" id="textValue"></td> 
      <td><img src="./images/correct.jpg" onClick="edit();" data-dismiss="modal" style=" LEFT:0px; WIDTH: 25px;  margin-TOP: -10px; HEIGHT: 25px"/></td>
      <td><img src="./images/wrong.jpg"  data-dismiss="modal" style=" LEFT:0px; WIDTH: 25px;  margin-TOP: -10px; HEIGHT: 25px"/></td>
      </tr></table>
      </div>
    </div>
  </div>
  </div>

</body>