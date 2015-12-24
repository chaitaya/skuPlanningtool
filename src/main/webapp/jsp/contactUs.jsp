<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/bridgei2i.tld" prefix="bi2itag"%>
<%
	String contextPath = request.getContextPath();
%>
<html>
<head>
	<script type="text/javascript">
	
	function saveContact1(){
		contactForm.action="saveContact.htm";
		contactForm.submit();
	}
	
	</script>
</head>
<body>

<form:form method="post" action="contactUs.htm" commandName="model" name="contactForm">
	 <table  border="0"  height="20px" class="table_headers" style="overflow: scroll;">
	<tr>              
		<th >
		<form:label path="firstname">First Name</form:label>
		</th>
		<td >
		<form:input cssClass="field" path="firstname"/>
		</td> 
	</tr>
	<tr>
		<th  ><form:label path="lastname">Last Name</form:label></th>
		<td ><form:input cssClass="field" path="lastname" /></td>
	</tr>
	<tr>
		<th  ><form:label path="lastname">Email</form:label></th>
		<td ><form:input cssClass="field" path="email" /></td>
	</tr>
	<tr>
		<th  ><form:label cssClass="field" path="lastname">Telephone</form:label></th>
		<td ><form:input cssClass="field" path="telephone"  /></td>
	</tr>
	<tr>
		<td colspan="4" align="center" >
			<bi2itag:ButtonDisplayTag clickParam = "saveContact1();"  nameButton = "Save" nameID = "buttonEdit" buttonColor ="red"/>
		</td >
	</tr>
</table>

		
</form:form>
<bi2itag:ReactGridTag list="${model.list}"/>
</body>
</html>
