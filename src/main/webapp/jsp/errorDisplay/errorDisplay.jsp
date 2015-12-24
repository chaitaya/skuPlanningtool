<%@ page import="com.bridgei2i.common.exception.ApplicationException"%>
<%@ page import="com.bridgei2i.common.constants.ApplicationConstants"%>
<%@ page import="com.bridgei2i.common.util.ApplicationUtil"%>

<%
ApplicationException applicationException = (ApplicationException)ApplicationUtil.getObjectFromSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY,request);
ApplicationUtil.removeObjectFromSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY,request);
	if(applicationException != null){
%>

<%
if(applicationException.getExceptionType()==3) {%>
<div class="alert alert-success">
    		<a href="#" class="close" data-dismiss="alert">&times;</a>
    		<strong>Success!</strong> <%= applicationException.getExceptionMessage() %>    		
</div>
<%}else if(applicationException.getExceptionType()==1){%>
<div class="alert alert-danger">
    		<a href="#" class="close" data-dismiss="alert">&times;</a>
    		<strong>Alert!</strong> <%=applicationException.getExceptionMessage() %>
</div>
<%}else{%>
<div class="alert alert-error">
    		
    		<strong>OOPS!</strong> <%=applicationException.getExceptionMessage() %>
</div>
<%}
}
	
%>
<div id="ajaxmessage">
<a href="#" class="close" id="closevalue" data-dismiss="alert"></a>
    		<div id="ajaxerrormessage"></div>
</div>
<div id="fileuploaderror">
<a href="#" class="filecloseerror" id="closevaluefile" ></a>
    		<div id="fileerrormessage"></div>
</div>
