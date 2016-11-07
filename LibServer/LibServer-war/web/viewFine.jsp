<%-- 
    Document   : ILS
    Created on : Oct 26, 2016, 2:40:59 AM
    Author     : USER
--%>

<%@page import="java.util.StringTokenizer"%>
<%@page import="java.util.ArrayList"%>
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
<style type="text/css">
    tr, td, th {border: 1px solid black;}
</style>
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
    <h3 style="padding-left: 50px">View Fine</h3><br>
    <form action="viewFine" method="POST">
        <input type="hidden" name="userName" value="<%=session.getAttribute("userName") %>"/>
        <p style="padding-left: 50px"><input type="submit" value="View All Fine(s)"/></p>
    </form>
    </p>
    <br>
    
    <%
        String paymentStatus = (String)request.getAttribute("paymentMsg");
        if (paymentStatus == null) {
            paymentStatus = "";
        }
        
        ArrayList fines = (ArrayList)request.getAttribute("data");
        String message = "";
        if (fines == null) {
            message = "";
        } else {
            String startOfStr = fines.toString().substring(1);
            if (startOfStr.startsWith("You")) {
                message = "You have no fine records.";
            } else if (startOfStr.startsWith("Error")) {
                message = "Error! Record(s) not found.";
            } else {
    %>
    
        <table style="padding-left: 50px">
            <col width="180">
            <col width="180">
            <col width="250">
            <col width="180">
            <tr>
                <th align="center">Fine Date</th>
                <th align="center">Fine Amount ($)</th>
                <th align="center">Payment Date</th>
                <th align="center">Pay Now</th>
            </tr>
    <%
                for (Object o: fines) {
                    String fine = (String)o;
                    StringTokenizer st = new StringTokenizer(fine, "#");
                    String fineID = st.nextToken();
                    String fineDate = st.nextToken();
                    String fineAmt = st.nextToken();
                    String paymentDate = st.nextToken();
    %>
            <tr>
                <td align="center"><%= fineDate %></td>
                <td align="center"><%= fineAmt %></td>
                <td align="center"><%= paymentDate %></td>
    <%
                    if (paymentDate.equalsIgnoreCase("Payment Due")) {
    %>
                <form action="payFine" method="POST">
                <input type="hidden" name="userName" value="<%=session.getAttribute("userName") %>"/>
                <input type="hidden" name="fineID" value="<%= fineID %>"/>
                <input type="hidden" name="fineAmt" value="<%= fineAmt %>"/>
                <td align="center"><input type="submit" value="Pay"/></td>
                </form>
    <%
                    } else {
    %>
                <td align="center">PAID</td>
    <%
                    }
    %>
            </tr>
    <%
                }
    %>
    </table>
    <%
            }
        }
    %>
    
    </p>
    <br><br>
    <h3 style="padding-left: 50px"><%= message %></h3><br>
    <h3 style="padding-left: 50px"><%= paymentStatus %></h3><br>
    
    </p>
</div>

<div id="copyright" class="container">
    <p>Thank you for using ILS.<br>&copy; Untitled. All rights reserved.</p>		
</div>
</body>
</html>

