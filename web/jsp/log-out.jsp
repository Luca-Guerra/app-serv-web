<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <% 
            session.removeAttribute("username"); 
            session.removeAttribute("password"); 
            session.invalidate(); 
        %> 
        <h1>Logout realizzato con successo!.</h1>
        <a href="/public_webapp/index.html">Log-in</a>
    </body>
</html>
