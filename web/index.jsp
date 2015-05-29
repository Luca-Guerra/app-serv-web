<%@page import="java.util.Enumeration"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/style-sheets/global.css" media="screen" />
        <title>CRM Hospital</title>
    </head>
    <body>
        <div class="header">CRM HOSPITAL</div>
        <div class="form-panel">
            <h1>Accedi</h1>
            <form method="post" action="<%= request.getContextPath()%>/AccessController">
                <input type="text" name="username" placeholder="username" />
                <input type="password" name="password" placeholder="password" />
                <input class="btn" value="Accedi" type="submit" />
            </form>
            <a href="<%= request.getContextPath()%>/jsp/registration.jsp">Registrati</a>
            <% if(request.getParameter("LoginError")!=null&&request.getParameter("LoginError").equals("true")){%>
                <div class="error">Username o password errate</div>
            <%}%>
        </div>
        
    </body>
</html>