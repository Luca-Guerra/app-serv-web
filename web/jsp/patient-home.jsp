<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="/public_webapp/styles/patient-home.css" media="screen" />
        <title>CRM Hospital</title>
    </head>
    <body>
        <%@ include file="/WEB-INF/jspf/menu.jspf" %>
        <div class="patientHome">
        <div>
            <form method="post" action="../../../public_webapp/jsp/conversation.jsp">
                <input class="btn" value="Msg" type="submit" />
            </form>
        </div>
        <div>
            <form method="post" action="../../../../public_webapp/jsp/patient-agend.jsp">
                <input class="btn" value="Agenda" type="submit" />
            </form>
        </div>
            </div>
        
            
        
    </body>
</html>
