<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	String contextPath = request.getContextPath();
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
function createComments()
{
	var commentObj = document.getElementById('comments');
	var returnObj= new Object();
	returnObj.comments=commentObj.value;
	if(returnObj.comments==null ||returnObj.comments=="")
		{
		alert("Enter your comments");
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
					<label> Enter Comments</label>
				</td>
								<td><textarea rows="" cols="25" id="comments"></textarea></td>
			</tr>
			<tr>
			<td><br></td>
			</tr>
 <tr>
 <td colspan='2' align='center'>
						<input type="button" value="Ok" class="smallbtn" onClick="createComments(); " >
						 <input type="button" value="Cancel" class="smallbtn" onClick="cancel();">
				</td>
			</tr>
		</table>
 </center>
</body>
</html>