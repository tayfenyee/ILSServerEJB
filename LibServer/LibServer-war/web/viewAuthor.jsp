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
    <h3 style="padding-left: 50px">View Author</h3><br>
    </p>
    <%
        ArrayList authorInfos = (ArrayList)request.getAttribute("data");
        String messageResponse = "";
        if (authorInfos == null) {
            messageResponse = "";
        } else if (authorInfos.toString().equalsIgnoreCase("[]")) {
            messageResponse = "No Details is Found";
        } else if (authorInfos.toString().substring(1).startsWith("!")) {
            messageResponse = authorInfos.toString().substring(2);
        } else {
            String name = "";
            String description = "";
            for (Object k: authorInfos) {
                String aut = (String)k;
                StringTokenizer st = new StringTokenizer(aut, "#");
                name = st.nextToken();
                description = st.nextToken();
            }
    %>
    <p>
        <i><h3 style="padding-left: 100px"><%= name %></h3>
            <h4 style="padding-left: 100px"><%= description %></h4></i>
    </p>
    <%
            for (Object o: authorInfos) {
                String author = (String)o;
                StringTokenizer st = new StringTokenizer(author, "#");
                String authorName = st.nextToken();
                String authorDescription = st.nextToken();
                String isbn = st.nextToken();
                String title = st.nextToken();
                String bookStatus = st.nextToken();
                String dueDate = st.nextToken();
                String authors = st.nextToken();
    %>
    
    <form action="viewAuthor" method="POST">  
        <input type="hidden" name="userName" value="<%=session.getAttribute("userName") %>"/>
        
        <table style="padding-left: 100px">
            <col width="180">
            <col width="500">
            <tr>
                <th align="left">ISBN       :</th>
                <td align="left"><%= isbn %></td>
            </tr>
            <tr>
                <th align="left">Title      :</th>
                <td align="left"><%= title %></td>
            </tr>
            <tr>
                <th align="left">Author     :</th>
                <td align="left"><%= authors.substring(0, authors.length()-1) %></td>
            </tr>
            <tr>
                <th align="left">Status     :</th>
                <td align="left"><%= bookStatus %></td>
            </tr>
    <%
            if (bookStatus.equalsIgnoreCase("Book Not Available")) {
    %>
            <tr>
                <th align="left">Due Date   :</th>
                <td align="left"><%= dueDate %></td>
            </tr>
    <%
            }
    %>
            <br>
        </table>
    </form>
    <%
            }
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

