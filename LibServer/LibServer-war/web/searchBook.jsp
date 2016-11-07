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
    <h3 style="padding-left: 50px">Search Book</h3><br>
    <form action="searchBook" method="POST">
        <input type="hidden" name="userName" value="<%=session.getAttribute("userName") %>"/>
        <table style="padding-left: 50px">
            <tr>
                <td align="right">Title: </td>
                <td align="left"><input type="text" name="title"/></td>
            </tr>
            <tr>
                <td align="right">ISBN: </td>
                <td align="left"><input type="text" name="isbn"/></td>
            </tr>
            <tr>
                <td align="right">Author: </td>
                <td align="left"><input type="text" name="author"/></td>
            </tr>
            <tr>
                <td align="right"></td>
                <td align="left"><input type="submit" value="Search"/></td>
            </tr>
        </table>
    </form>
    </p>
    <%
        ArrayList books = (ArrayList)request.getAttribute("data");
        String messageResponse = "";
        if (books == null) {
            messageResponse = "";
        } else if (books.toString().equalsIgnoreCase("[]")) {
            messageResponse = "No Book is Found";
        } else if (books.toString().substring(1).startsWith("!")) {
            messageResponse = books.toString().substring(2);
        } else {
            int bookCount = 0;
            for (Object o: books) {
                    bookCount++;
                    String book = (String)o;
                    StringTokenizer st = new StringTokenizer(book, "#");
                    String titleFound = st.nextToken();
                    String isbnFound = st.nextToken();
                    String bookIDFound = st.nextToken();
                    String authorFound = st.nextToken();
                    String statusFound = st.nextToken();
                    String dueDateFound = st.nextToken();
                    %>
    
    <form action="viewBook" method="POST">  
        <input type="hidden" name="userName" value="<%=session.getAttribute("userName") %>"/>
        <input type="hidden" name="bookID" value="<%= bookIDFound %>"/>
        <input type="hidden" name="bookStatus" value="<%= statusFound %>"/>
        <input type="hidden" name="dueDate" value="<%= dueDateFound %>"/>
        <table style="padding-left: 100px">
            <col width="120">
            <col width="350">
            <tr>
                <th align="left">Title      :</th>
                <td align="left"><%= titleFound %></td>
                <td><input type="submit" value="View Book"/></td>
            </tr>
            <tr>
                <th align="left">ISBN       :</th>
                <td align="left"><%= isbnFound %></td>
            </tr>
            <tr>
                <th align="left">Author     :</th>
                <td align="left"><%= authorFound.substring(0, authorFound.length()-2) %></td>
            </tr>
            <tr>
                <th align="left">Status     :</th>
                <td align="left"><%= statusFound %></td>
            </tr>
    <%
            if (statusFound.equalsIgnoreCase("Book Not Available")) {
    %>
            <tr>
                <th align="left">Due Date   :</th>
                <td align="left"><%= dueDateFound %></td>
            </tr>
    <%
            }
    %>
            <br>
        </table>
    </form>
    <%
            }
                messageResponse = bookCount + " book(s) found.";
        }
    %>
    
    <%
        String message = (String)request.getAttribute("message");
        if (message == null) {
            message = "";
        }
    %>
    <br><br>
    <h3 style="padding-left: 50px"><%= messageResponse %></h3>
    </p>
</div>

<div id="copyright" class="container">
    <p>Thank you for using ILS.<br>&copy; Untitled. All rights reserved.</p>		
</div>
</body>
</html>

