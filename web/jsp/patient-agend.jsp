<%-- 
    Document   : patient-agend
    Created on : 15-feb-2015, 16.18.06
    Author     : Luca
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="/public_webapp/styles/global.css" media="screen" />
        <title>Agend</title>
        <script type="text/javascript">
            function getDayAgend(days){
                var xmlHttp;
                xmlHttp = new XMLHttpRequest();
                xmlHttp.open("POST","../AgendaService",true)
                xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                xmlHttp.onreadystatechange=function(){
                    if(xmlHttp.readyState==4 && xmlHttp.status == 200){
                        xmlDoc = xmlHttp.responseXML;
                        console.log("file:");
                        console.log(xmlDoc);
                        document.getElementById("day").innerHTML='<%session.getAttribute("date").toString();%>'
                    }
                }
                xmlHttp.send("operation=getAgenda&day="+days);
            }
            function register(){
                
            }
        </script>
    </head>
    <body>
        <%@ include file="/WEB-INF/jspf/menu.jspf" %>
        <a onclick='getDayAgend("yesterday")'>Indietro</a>
        <h1 id="day"></h1>
        <a onclick='getDayAgend("tomorrow")'>Avanti</a>
        <div class="slots">
            <div class="slot" id="0">--</div>
            <div class="slot" id="1">--</div>
            <div class="slot" id="2">--</div>
            <div class="slot" id="3">--</div>
            <div class="slot" id="4">--</div>
            <div class="slot" id="5">--</div>
            <div class="slot" id="6">--</div>
            <div class="slot" id="7">--</div>
        </div>
        <script type="text/javascript">
            window.onload = function(){
                getDayAgend("today");
            };
        </script>
    </body>
</html>
