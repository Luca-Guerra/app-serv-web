<%@page import="models.Patient"%>
<%@page import="models.Doctor"%>
<%@page import="repositories.AccountRepository"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="/public_webapp/styles/global.css" media="screen" />
        <title>CRM Hospital (Medico)</title>
    </head>
    <body>
        <%@ include file="/WEB-INF/jspf/menu.jspf" %>
        <script type="text/javascript">
            function goToConv(user){
                xmlHttp = new XMLHttpRequest();
                xmlHttp.open("POST","/public_webapp/SetSession",true);
                xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                xmlHttp.onreadystatechange=function(){
                    if(xmlHttp.readyState==4 && xmlHttp.status == 200){
                        var jspcall = "/public_webapp/jsp/conversation.jsp";
                        window.location.href = jspcall;
                    }
                }
                var parameter = "attribute=patientUsername&"+"value="+user;
                xmlHttp.send(parameter);
            }
        </script>
        <div id="dashboard">
            <div class="conversation">
        <% 
            AccountRepository rep = new AccountRepository(this.getServletContext());
            String doctorUser = (String) session.getAttribute("username");
            Doctor doc = (Doctor) rep.GetAccount(doctorUser);
            for(int i=0;i<doc.getPatients().size();i++){
                Patient pat = (Patient)rep.GetAccount(doc.getPatients().get(i));
                String patName=pat.getName()+" "+pat.getSurname();
                String user=pat.getUsername();
                int lastVisit=doc.getLastVisities().get(i);
                %>
                <div class="alert" onclick="goToConv('<%=user%>');">
                    <div><%= patName%></div>
                    <div class="new-msg">
                        <div><%= lastVisit%></div>
                    </div>
                </div>
                <%
            }
        %>
            </div>
            <div class="agend" onclick="location.href='/public_webapp/jsp/agend.jsp'">
                <div class="alert" >
                    <div>Agenda</div>
                </div>
            </div>
        </div>
    </body>
</html>