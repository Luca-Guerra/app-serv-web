<%-- 
    Document   : home
    Created on : 14-feb-2015, 21.37.52
    Author     : Luca
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>
        <%
            String a=session.getAttribute("username").toString();
            out.println("Benvenuto  "+a);
        %>
        </h1>
        <a href="log-out.jsp">Logout</a>
    </body>
</html>
