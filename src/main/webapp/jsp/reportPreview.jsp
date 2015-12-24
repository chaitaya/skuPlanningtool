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

<title>Report Preview-Bridgei2i Analyst</title>
</head>
<script type="text/javascript" language="javascript">
	function show() {
		var ele = document.getElementById("showDiv");
		if (ele.style.display == "block") {
			// ele.style.display = "none";
		} else {
			ele.style.display = "block";
		}
	}
	function hide() {
		var ele = document.getElementById("showDiv");
		if (ele.style.display == "block") {
			 ele.style.display = "none";
		} 
	}
</script>
<body>
	<center>
		<div style="width: 1345px;height:200px;margin-top:0px;margin-left:5px">
			<div style="height:25px;border:0px solid lightgray;">Chart
				Title</div>

			<div style="float:left;width:206px;border:1px solid lightgray">
				<table width="100%">
					<tr>
						<td width="50"
							style="text-align:top;border:0px solid lightgray">
							<input type="submit" value="Satisfaction Score" style="width:100%;height:25px" onclick="return show();"class="xlgebtn" /></td>
					</tr>
				</table>
				<table width="100%">
					<tr>
						<td
							style="width:10px;text-align:top;border:0px solid lightgray">
							<input type="submit" value="Employee feedback" style="width:100%;height:25px" class="xlgebtn" />
						</td>
					</tr>
				</table>
				<table width="100%">
					<tr>
						<td style="width:10px;text-align:top;border:0px solid lightgray">
							<input type="submit" value="Sentiment"style="width:100%;height:25px" class="xlgebtn" />
						</td>
					</tr>
				</table>
				<div align="center" style="border:0px solid lightgray;height:175px;width:100%">
				</div>
				<table width="100%">
					<tr>
						<td style="width:10px;text-align:top;border:0px solid lightgray">
							<input type="submit" value="Team" style="width:100%;height:25px" class="xlgebtn" />
						</td>
					</tr>
				</table>
				<div align="center" style="border:0px solid lightgray;height:180px;width:100%">
					<table width="100%">
						<tr>
							<td width="50" style="text-align:top;border:0px solid lightgray">
							<label>Report Status: </label>
							</td>
						</tr>
					</table>
					<table width="100%">
						<tr>
							<td width="100"style="text-align: top; border: 0px solid lightgray">
							<select>
									<option>Open</option>

							</select>
							</td>
							<td>
							<input type="submit" value="Update" class="smallbtn" />
							</td>
						</tr>
					</table>
					<div align="center" style="border:0px solid lightgray;height:5px;width:100%">
					</div>
					<table width="100%">
						<tr>
							<td width="25" style="text-align:top; border:0px solid lightgray">
							<label>Comments:</label>
							</td>
						</tr>
						<tr>
							<td>
							<textarea rows="5" cols="21">
                                 </textarea>
                           </td>
						</tr>
					</table>
				</div>
			</div>
			<div style="border:1px solid lightgray;height:480px;width:1135px;float:left;overflow:scroll;overflow-x:hidden;overflow-y:scroll;">
				<div style="margin-top: 0px;margin-left:-850px">
					<table width=250 height="10" border=0px>
						<tr>
							<td><label>Distribution list </label></td>
							<td><select>
									<option>Karnan</option>
							</select></td>
						</tr>
					</table>
				</div>
				<div id="showDiv" style="border:solid lightgray 1px;display:none;width:950px;height:420px;margin-top:20px;margin-left:0px">
                          <div style="border:solid lightgray 1px;width:950px;height:35px;margin-top:0px;margin-left:0px">
				                  <table width=950 height="10" border=0px>
						        <tr>
							     <td>
									<label>Distribution Of Satisfaction</label>
								</td>
								<td align="right"> <img src="<%=contextPath %>/images/close.png" onclick="return hide();"/>
								</td>
								</tr>
								</table>
				          </div>
                                  <table align="left" style="margin-top:10px;" width=180 height="10" border=0px>
						        <tr>
							           <td>
									<label>X axis:</label>
								      </td>
								<td > <label>Y axis:</label>
								</td>
								</tr>
								<tr>
							          <td>
									<label>Filter:</label>
									<select>
									<option>Gender</option>
									<option>Male</option>
									<option>Female</option>
                              		</select> 
							        </td>
								</tr>
								</table>
		    	</div>
			</div>
		</div>
	</center>
</body>
</html>