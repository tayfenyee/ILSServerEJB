<%-- 
    Document   : ILS
    Created on : Oct 26, 2016, 2:40:59 AM
    Author     : USER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
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
			<h1><span class="icon icon-cog"></span><a href="Index">Welcome to ILS</a></h1>
			<div id="menu">
				<ul>
					<li class="current_page_item"><a href="Index" accesskey="1" title="">Homepage</a></li>
				</ul>
			</div>
		</div>
	</div>
</div>
<div class="wrapper">
    <p>
        <form action="register" method="POST">
        <table style="padding-left: 50px">
            <tr>
                <td align="right">Name: </td>
                <td align="left"><input type="text" name="userName"/></td>
            </tr>
            <tr>
                <td align="right">Password: </td>
                <td align="left"><input type="password" name="password"/></td>
            </tr>
            <tr>
                <td align="right">Contact Number: </td>
                <td align="left"><input type="text" name="contactNum"/></td>
            </tr>
            <tr>
                <td align="right">Email: </td>
                <td align="left"><input type="email" name="email"/></td>
            </tr>
            <tr>
                <td align="right">Address: </td>
                <td align="left"><input type="text" name="address"/></td>
            </tr>
            <tr>
                <td align="right"></td>
                <td align="left"><input type="submit" value="Register"/></td>
            </tr>
        </table>
    </form>
    </p>
    <%
        String message = (String)request.getAttribute("message");
        if (message == null) {
            message = "";
        }
    %>
    <br><br>
    <h3 style="padding-left: 50px"><%= message %></h3>
</div>

<div id="copyright" class="container">
    <p>Thank you for using ILS.<br>&copy; Untitled. All rights reserved.</p>		
</div>
</body>
</html>

