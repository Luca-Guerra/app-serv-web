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
            <div class="alert" onclick="location.href='../../../public_webapp/jsp/conversation.jsp'">
                <!--<form method="post" action="../../../public_webapp/jsp/conversation.jsp">
                    <input class="btn" value="Msg" type="submit" />
                </form>-->
                <div>Messaggi</div>
                 <div class="new-msg">
                    <div><%= lastVisit%></div>
                 </div>
            </div>
                <!--<form method="post" action="../../../../public_webapp/jsp/agend.jsp">
                    <input class="btn" value="Agenda" type="submit" />
                </form>-->
                <div class="alert" onclick="location.href='../../../public_webapp/jsp/agend.jsp'">
                    <div>Agenda</div>
                </div>
        </div>
        
            
        
    </body>
</html>
