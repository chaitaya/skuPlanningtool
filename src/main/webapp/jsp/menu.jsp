<%@ taglib uri="/WEB-INF/bridgei2i.tld" prefix="bi2itag"%>
<%
	String contextPath = request.getContextPath();
%>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.1/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/js_common.js"></script>
<script>
	$(document).ready(function() {

		$('#nav > li > a').click(function() {
			if ($(this).attr('class') != 'active') {
				$('#nav li ul').slideUp();
				$(this).next().slideToggle();
				$('#nav li a').removeClass('active');
				$(this).addClass('active');
			}
		});
	});
	

</script>
<div id="wrapper-center">
	<div id="left-menu">
		<ul id="nav">

			<bi2itag:TabNavigationTag tabName="Home">
				<bi2itag:TabNavigationTag navigationPage="home.htm" navigationController="navigation.htm" tabName="Home" />
			</bi2itag:TabNavigationTag>
			<bi2itag:TabNavigationTag navigationPage="contactUs.htm" navigationController="navigation.htm" tabName="Contact Us" />
	</div>
