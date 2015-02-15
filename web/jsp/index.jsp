<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="/public_webapp/styles/global.css" media="screen" />
        <title>JSP Page</title>
    </head>
    <body>
        <% 
            if(session.getAttribute("username") == null){
                response.sendRedirect("jsp/log-in.jsp"); 
                return;
            }
        %>
        <div class="header">
            <div><a href="log-out.jsp"><img alt="Esci" src="/public_webapp/images/agend.png" /></a></div>
            <div><img alt="Agenda" src="/public_webapp/images/shutdown.png" /></div>
        </div>
        <p>Salve <% out.println(session.getAttribute("username").toString()); %></p>
    </body>
</html>
