function processNavigationClick(navigationPage,logicalName){
	var formObj = document.forms[0];
	
	var input = document.createElement("input");
	input.setAttribute("type", "hidden");
	input.setAttribute("name", "navigationPage");
	input.setAttribute("value", navigationPage);
	formObj.appendChild(input);
	
	var input = document.createElement("input");
	input.setAttribute("type", "hidden");
	input.setAttribute("name", "logicalName");
	input.setAttribute("value", logicalName);
	formObj.appendChild(input);
	
	formObj.submit();
}

function charttype(chartId, obj, width, height){
	var e = document.getElementById("item"+chartId);
	var filterValueStr = e.options[e.selectedIndex].value;
	var chartType="";
	if(filterValueStr=="HUNDRED_PERCENT_STACKED_COLUMN"){
		chartType="StackedColumn2D";
	}else if(filterValueStr=="HUNDRED_PERCENT_STACKED_BAR"){
		chartType="StackedBar2D";
	}else{
		chartType = filterValueStr;
	}
	var chartXmlStr = obj.getXMLData();
	var myChart = new FusionCharts(chartType, chartId, width, height, "0", "1" );
	myChart.setXMLData(chartXmlStr);
	if(filterValueStr=="HUNDRED_PERCENT_STACKED_COLUMN" || filterValueStr=="HUNDRED_PERCENT_STACKED_BAR" ){
		myChart.setChartAttribute("stack100Percent","1");
		myChart.setChartAttribute("animation","1");
	}
	else{
		myChart.setChartAttribute("stack100Percent","0");
		myChart.setChartAttribute("animation","1");
	}
	myChart.render('chartContainer'+chartId);	
} 

function exportExcel(obj){
    var data = obj.getDataAsCSV();
    var excelData = document.getElementById("excelData");
    excelData.value = data;
    dashboardForm.action="exportExcel.htm";
    dashboardForm.submit();
}
function wordcloudExportExcel(id){
    var data= document.getElementById("wordcloudStr"+id).value;
    var excelData = document.getElementById("excelData");
    excelData.value = data;
    dashboardForm.action="wordcloudExportExcel.htm";
    dashboardForm.submit();
}


function popUpPPTComments(obj,chartType,caption,categoryName) {
    var left = (screen.width/2)-(400/2);
    var top = (screen.height/2)-(400/2);
	    loadProgress();
	    //var returnObj = window.showModalDialog("popUpComments.htm", null,"dialogWidth:400px; dialogHeight:150px; center:yes");
		//var url = "exportToPPT.htm?chartXmlStr="+obj.getDataAsCSV()+"&chartType="+chartType+"&caption="+caption+"&categoryName="+categoryName+"&comment="+returnObj.comments+"&selectedCustomPPTName="+returnObj.selectedCustomPPTName+"&customType="+returnObj.customType;
	    var url = "exportToPPT.htm?chartXmlStr="+encodeURIComponent(obj.getDataAsCSV())+"&chartType="+chartType+"&caption="+caption+"&categoryName="+categoryName;
	    var xmlHttp = new XMLHttpRequest();
	    if (window.XMLHttpRequest) { // Mozilla, Safari, ...
	           xmlHttp = new XMLHttpRequest();
	    } else if (window.ActiveXObject) { // IE
	           xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	    }
	    xmlHttp.open('POST', url, true);
	    xmlHttp.onreadystatechange = function() {
	    if (xmlHttp.readyState==4 && xmlHttp.status==200) {
	           // success message
	    	removeProgress();
	    }
	    };
	    xmlHttp.send(url);
}

function exportToPPT(obj,chartType,caption,categoryName,chartId){
	var chart = null;
	if(document.getElementById("item"+chartId)){
		chart = document.getElementById("item"+chartId).value;
	}
	if(chart=="Select"){
		if(chartType=="StackedBar2D"){
			var stack100Percent = obj.getChartAttribute("stack100Percent");
			if(stack100Percent==1){
				popUpPPTComments(obj,"HUNDRED_PERCENT_STACKED_BAR",caption,categoryName);
			}else{
				popUpPPTComments(obj,chartType,caption,categoryName);
			}
		}
		else if(chartType=="StackedColumn2D"){
			var stack100Percent = obj.getChartAttribute("stack100Percent");
			if(stack100Percent==1){
				popUpPPTComments(obj,"HUNDRED_PERCENT_STACKED_COLUMN",caption,categoryName);
			}else{
				popUpPPTComments(obj,chartType,caption,categoryName);
			}
		}else{
			popUpPPTComments(obj,chartType,caption,categoryName);
		}
	}else{
		popUpPPTComments(obj,chartType,caption,categoryName);
	}
}


function posicionar(elemento){
    var altoDocumento = $(window).height();//alto
    var anchoDocumento = $(window).width();
    var centroXDocumento = anchoDocumento / 2;
    var centroYDocumento = altoDocumento / 2;
    $(elemento).css("position","absolute");
    $(elemento).css("top", centroYDocumento-30);
    $(elemento).css("left", centroXDocumento-30);
}

function loadProgress(){
	 var loadingObj = document.getElementById("loading");
	 posicionar(loadingObj);
	 loadingObj.right=screen.width/3+"px";
	 var img = document.createElement("IMG");
	 img.src = "./images/processing1.gif";
	 loadingObj.appendChild(img);
}

function removeProgress(){
	 var loadingObj = document.getElementById("loading");
	 while (loadingObj.firstChild) {
		 loadingObj.removeChild(loadingObj.firstChild);
	}
}

function exportToExcel(obj){
    var data = obj.getDataAsCSV();
    var excelData = document.getElementById("excelData");
    excelData.value = data;
    workspaceForm.action="exportToExcel.htm";
    workspaceForm.submit();
}


