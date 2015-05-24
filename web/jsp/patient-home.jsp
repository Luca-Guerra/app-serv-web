<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="/public_webapp/styles/global.css" media="screen" />
        <title>CRM Hospital</title>
    </head>
    <body>
        <%@ include file="/WEB-INF/jspf/menu.jspf" %>
        <% 
            AccountRepository rep = new AccountRepository(this.getServletContext());
            String patientUser = (String) session.getAttribute("username");
            Patient pat = (Patient) rep.GetAccount(patientUser);
            int lastVisit = pat.getLastVisit();
                %>
        <div id="dashboard">
            <div class="alert" onclick="location.href='/public_webapp/jsp/conversation.jsp'">
                <div>Messaggi</div>
                 <div class="new-msg">
                    <div><%= lastVisit%></div>
                 </div>
            </div>
        </div>
    </body>
</html>
