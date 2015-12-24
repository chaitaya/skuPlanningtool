
<%
	String contextPath = request.getContextPath();
String view =(String) request.getParameter("view");
%>
<%
if(view.equals("1")){
%>
    <div id="footer" style="width:100%;height:10px;margin-top:29px;">
      <div>
        <footer><p style="line-height: 0px;padding-top: 16px;">© 2015 BRIDGEi2i Analytics Solutions | <a href="privacy-policy.htm" target="_blank">Privacy Policy</a>  |  <a href="terms-of-use.htm" target="_blank">Terms of Use</a></p></footer>
      </div>
    </div>
<%}else if(view.equals("2")){%>
<div id="footer" style="width:100%;height:10px;margin-top:241px;">
      <div>
        <footer><p style="line-height: 0px;padding-top: 16px;">© 2015 BRIDGEi2i Analytics Solutions | <a href="privacy-policy.htm" target="_blank">Privacy Policy</a>  |  <a href="terms-of-use.htm" target="_blank">Terms of Use</a></p></footer>
      </div>
    </div>
<%}else if(view.equals("3")){%>
<div id="footer" style="width:100%;height:10px;margin-top:241px;">
      <div>
        <footer><p style="line-height: 0px;padding-top: 16px;">© 2015 BRIDGEi2i Analytics Solutions | <a href="privacy-policy.htm" target="_blank">Privacy Policy</a>  |  <a href="terms-of-use.htm" target="_blank">Terms of Use</a></p></footer>
      </div>
    </div>
<%}else if(view.equals("4")){%>
<div id="footer" style="width:100%;height:10px;margin-top:145px;">
      <div>
        <footer><p style="line-height: 0px;padding-top: 16px;">© 2015 BRIDGEi2i Analytics Solutions | <a href="privacy-policy.htm" target="_blank">Privacy Policy</a>  |  <a href="terms-of-use.htm" target="_blank">Terms of Use</a></p></footer>
      </div>
    </div>
<%}else if(view.equals("5")){%>
<div id="footer" style="width:100%;height:10px;margin-top:127px;">
      <div>
        <footer><p style="line-height: 0px;padding-top: 16px;">© 2015 BRIDGEi2i Analytics Solutions | <a href="privacy-policy.htm" target="_blank">Privacy Policy</a>  |  <a href="terms-of-use.htm" target="_blank">Terms of Use</a></p></footer>
      </div>
    </div>

<%} else{%>
<div id="footer" style="width:100%;height:10px;margin-top:444px;">
      <div>
        <footer><p style="line-height: 0px;padding-top: 16px;">© 2015 BRIDGEi2i Analytics Solutions | <a href="privacy-policy.htm" target="_blank">Privacy Policy</a>  |  <a href="terms-of-use.htm" target="_blank">Terms of Use</a></p></footer>
      </div>
    </div>

<%}%>