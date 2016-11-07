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
    <h3 style="padding-left: 50px">View Checkout</h3><br>
    <form action="viewCheckout" method="POST">
        <input type="hidden" name="userName" value="<%=session.getAttribute("userName") %>"/>
        <p style="padding-left: 50px"><input type="submit" value="View All Checkout(s)"/></p>
    </form>
    </p>
    <br>
    
    <%
        ArrayList checkouts = (ArrayList)request.getAttribute("data");
        String message = "";
        if (checkouts == null) {
            message = "";
        } else {
            String startOfStr = checkouts.toString().substring(1);
            if (startOfStr.startsWith("You")) {
                message = "You have no checkout records.";
            } else {
    %>
    
        <table style="padding-left: 50px">
            <col width="180">
            <col width="180">
            <col width="300">
            <col width="230">
            <tr>
                <th align="center"></th>
                <th align="center">ISBN</th>
                <th align="center">Title</th>
                <th align="center">Dates</th>
            </tr>
    <%
                for (Object o: checkouts) {
                    String checkout = (String)o;
                    StringTokenizer st = new StringTokenizer(checkout, "#");
                    String state = st.nextToken();
                    String checkoutID = st.nextToken();
                    String isbn = st.nextToken();
                    String title = st.nextToken();
                    String checkoutDate = st.nextToken();
                    String dueDate = st.nextToken();
                    if (state.equalsIgnoreCase("Current")) {
    %>
            <tr>
                <td align="center">Current Checkout</td>
                <td align="center"><%= isbn %></td>
                <td align="center"><%= title %></td>
                <td align="center">Checkout Date: <%= checkoutDate %><br>Due Date: <%= dueDate %></td>
            </tr>
    <%
                    } else if (state.equalsIgnoreCase("Previous")) {
    %>
            <tr>
                <td align="center">Previous Checkout</td>
                <td align="center"><%= isbn %></td>
                <td align="center"><%= title %></td>
                <td align="center">Checkout Date: <%= checkoutDate %><br>Return Date: <%= dueDate %></td>
            </tr>
    <%                    
                    } else {
    %>
            <tr>
                <td align="center">Error</td>
                <td align="center"></td>
                <td align="center"></td>
                <td align="center"></td>
            </tr>
    <%                    
                    }
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
    
    </p>
</div>

<div id="copyright" class="container">
    <p>Thank you for using ILS.<br>&copy; Untitled. All rights reserved.</p>		
</div>
</body>
</html>

