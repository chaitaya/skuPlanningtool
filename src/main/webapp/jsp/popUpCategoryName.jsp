<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	String contextPath = request.getContextPath();
String isFromReportTitle = (String) request.getParameter("isFromReportTitle");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="<%=contextPath%>/css/bridgei2i.css"
	type="text/css">
<title>PopUp</title>
</head>
<script type="text/javascript">
function createCategory()
{
	var categoryNameObj = document.getElementById('categoryName');
	
	var returnObj= new Object();
	returnObj.categoryName=categoryNameObj.value;
	if(returnObj.categoryName==null ||returnObj.categoryName=="")
		{
		alert("Name cannot be empty ");
		}
	else
		{
		window.returnValue=returnObj;
		window.close();
		}
	
	}
	
function cancel()
{
	
		window.close();
		
	}
</script>
<body>

	<center>
	<br>
		<table>
			<tr>
				<td>
				<%if(isFromReportTitle!=null ) {%>
	           	<label> Enter Report Title</label>
	           	
		        <%}else {%>
		
					<label> Enter Category Name</label>
					<%} %>
					
				</td>
								<td><input type="text" id="categoryName"></td>
			</tr>
			<tr>
			<td><br></td>
			</tr>
 <tr>
 <td colspan='2' align='center'>
						<input type="button" value="Ok" class="smallbtn" onClick="createCategory(); " >
						 <input type="button" value="Cancel" class="smallbtn" onClick="cancel();">

				</td>

			</tr>
			
 
 
		</table>

 </center>
</body>
</html>