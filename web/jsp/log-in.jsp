<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="/public_webapp/styles/log-in.css" media="screen" />
        <title>CRM Hospital</title>
    </head>
    <body>
        <% 
            String username = request.getParameter("username"); 
            String password = request.getParameter("password"); 
            if(username != null && password != null && username.equals("luca") && password.equals("123456")) { 
                session.setAttribute("username",username); 
                response.sendRedirect("index.jsp");
                return;
            }
        %>
        <div class="header">CRM HOSPITAL</div>
        <div class="log-in">
            <h1>Accedi</h1>
            <form method="post" action="/public_webapp/jsp/log-in.jsp">
                <input type="text" name="username" placeholder="username" />
                <input type="password" name="password" placeholder="password" />
                <input class="btn" value="Accedi" type="submit" />
            </form>
        </div>
    </body>
</html>