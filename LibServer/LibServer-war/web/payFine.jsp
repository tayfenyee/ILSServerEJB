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
			<h1><span class="icon icon-cog"></span><a href="Main">Welcome to ILS</a></h1>
			<div id="menu">
				<ul>
					<li class="current_page_item"><a href="Main" accesskey="1" title="">Homepage</a></li>
					<li><a href="UpdateProfile" accesskey="2" title="">Profile</a></li>
					<li><a href="SearchBook" accesskey="3" title="">Search</a></li>
					<li><a href="ViewCheckout" accesskey="5" title="">Checkout</a></li>
					<li><a href="ViewFine" accesskey="6" title="">Fine</a></li>
                                        <li><a href="ViewRequest" accesskey="7" title="">Request</a></li>
                                        <li><a href="../Index" accesskey="7" title="">Logout</a></li>
				</ul>
			</div>
		</div>
	</div>
</div>
<div class="wrapper">
    <p>
    <h2 style="padding-left: 50px">Hello <%=session.getAttribute("userName") %></h2><br>
    <h3 style="padding-left: 50px">Card Information:</h3><br>
    <form action="makePayment" method="POST">
        <input type="hidden" name="userName" value="<%=session.getAttribute("userName") %>"/>
        <input type="hidden" name="fineID" value="<%=(String)request.getParameter("fineID") %>"/>
        <input type="hidden" name="fineAmt" value="<%=(String)request.getParameter("fineAmt") %>"/>
        <table style="padding-left: 50px">
            <tr>
                <td align="right">Card Type: </td>
                <td align="left"><input type="text" name="cardType"/></td>
            </tr>
            <tr>
                <td align="right">Card Number: </td>
                <td align="left"><input type="text" name="cardNum"/></td>
            </tr>
            <tr>
                <td align="right">Holder Name: </td>
                <td align="left"><input type="text" name="userName" value="<%=session.getAttribute("userName") %>"/></td>
            </tr>
            <tr>
                <td align="right">Payment Value($): </td>
                <td align="left"><input type="text" name="paymentValue" value="<%= (String)request.getParameter("fineAmt") %>" readonly="true"/></td>
            </tr>
            <tr>
                <td align="right"></td>
                <td align="left"><input type="submit" value="Pay Now"/></td>
            </tr>
        </table>
    </form>
    </p>
    <%
        String message = (String)request.getAttribute("message");
        String fineAmt = (String)request.getParameter("fineAmt");
        String fineID = (String)request.getParameter("fineID");
        if (message == null) {
            message = "";
        }
    %>
    <br><br>
    <h3 style="padding-left: 50px"><%= message %></h3>
    </p>
</div>

<div id="copyright" class="container">
    <p>Thank you for using ILS.<br>&copy; Untitled. All rights reserved.</p>		
</div>
</body>
</html>

