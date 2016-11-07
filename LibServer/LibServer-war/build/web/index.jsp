<%-- 
    Document   : ILS
    Created on : Oct 26, 2016, 2:40:59 AM
    Author     : USER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    session.setAttribute("userName", null);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>ILS: Integrated Library System</title>
<meta name="keywords" content="" />
<meta name="description" content="" />
<link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:200,300,400,600,700,900|Quicksand:400,700|Questrial" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/default.css" rel="stylesheet" type="text/css" media="all" />
<link href="${pageContext.request.contextPath}/fonts.css" rel="stylesheet" type="text/css" media="all" />

<!--[if IE 6]><link href="default_ie6.css" rel="stylesheet" type="text/css" /><![endif]-->

</head>
<body>
<div id="header-wrapper">
	<div id="header" class="container">
		<div id="logo">
			<h1><span class="icon icon-cog"></span><a href="#">Welcome to ILS</a></h1>
			<div id="menu">
				<ul>
					<li class="current_page_item"><a href="#" accesskey="1" title="">Homepage</a></li>
				</ul>
			</div>
		</div>
	</div>
</div>
<div class="wrapper">
	<div id="three-column" class="container">
		<div id="tbox1">
			<div class="title">
				<h2>Existing Member</h2>
			</div>
			<a href="ils/MemberLogin" class="button">Login</a> </div>
		<div id="tbox2">
			<div class="title">
				<h2>New User</h2>
			</div>
			<a href="ils/RegisterMember" class="button">Register Member</a> </div>
		<div id="tbox3">
			<div class="title">
				<h2></h2>
			</div>
                </div>
        </div>
</div>

<div id="copyright" class="container">
    <p>Thank you for using ILS.<br>&copy; Untitled. All rights reserved.</p>		
</div>
</body>
</html>

