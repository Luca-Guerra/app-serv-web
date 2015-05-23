<%-- 
    Document   : agend
    Created on : 15-feb-2015, 16.18.06
    Author     : Luca
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="/public_webapp/styles/global.css" media="screen" />
        <link rel="stylesheet" href="/public_webapp/styles/agend.css" media="screen" />
        <title>Agend</title>
        <script type="text/javascript">
            function getDayAgend(days){
                var xmlHttp;
                var dateGlobal;
                var username = '<%= session.getAttribute("username")%>';
                var role = '<%= session.getAttribute("role")%>';
                var day = days;
                console.log("RICHIESTA");
                xmlHttp = new XMLHttpRequest();
                xmlHttp.open("POST","../AgendaService",true)
                xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                xmlHttp.onreadystatechange=function(){
                    if(xmlHttp.readyState==4 && xmlHttp.status == 200){
                        console.log("RISPOSTA");
                        xmlDoc = xmlHttp;
                        console.log("file:");
                        console.log(xmlHttp.responseText);
                        var myArr = JSON.parse(xmlHttp.responseText);
                        var date = new Date(myArr[0].data);
                        dateGlobal = date;
                        document.getElementById("day").innerHTML = date.getDate()+"/"+(date.getMonth()+1)+"/"+date.getFullYear();
                        for(var j = 0; j<myArr.length; j++){
                            var text = "slot "+(myArr[j].slot+1);
                            console.log("available="+myArr[j].available);
                            console.log("user="+myArr[j].patient);
                            if(myArr[j].available==true&&myArr[j].patient==""){
                                text+=" libero";
                            }else if(myArr[j].available==false){
                                text+=" disabilitato";
                            }else{
                                if(role=="doctor"){
                                    text+=" occupato da "+myArr[j].patient;
                                }else{
                                    if(username == myArr[j].patient){
                                        text+=" occupato da me";
                                    }else{
                                        text+=" occupato";
                                    }
                                }
                            }
                            document.getElementById(""+j).innerHTML= text;
                            popAgenda();
                        }
                        //document.getElementById("day").innerHTML='<%session.getAttribute("date").toString();%>'
                    }
                }
                xmlHttp.send("operation=getAgenda&day="+days);
            }
            function register(slot){
                var xmlHttp;
                var dateGlobal;
                console.log("RICHIESTA Register");
                xmlHttp = new XMLHttpRequest();
                xmlHttp.open("POST","../AgendaService",true)
                xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                xmlHttp.onreadystatechange=function(){
                    if(xmlHttp.readyState==4 && xmlHttp.status == 200){
                        var jspcall = "../../../public_webapp/jsp/agend.jsp";
                        window.location.href = jspcall;
                    }
                }
                xmlHttp.send("operation=register&slot="+slot);
            }
            function popAgenda(){
                var xmlHttp;
                var dateGlobal;
                var username = '<%= session.getAttribute("username")%>';
                var role = '<%= session.getAttribute("role")%>';
                console.log("RICHIESTA");
                xmlHttp = new XMLHttpRequest();
                xmlHttp.open("POST","../AgendaService",true)
                xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                xmlHttp.onreadystatechange=function(){
                    if(xmlHttp.readyState==4 && xmlHttp.status == 200){
                        console.log("RISPOSTA");
                        xmlDoc = xmlHttp;
                        console.log("file:");
                        console.log(xmlHttp.responseText);
                        if(xmlDoc == "timeout"){
                            popAgenda();
                        }else{
                            var myArr = JSON.parse(xmlHttp.responseText);
                            var date = new Date(myArr[0].data);
                            dateGlobal = date;
                            document.getElementById("day").innerHTML = date.getDate()+"/"+(date.getMonth()+1)+"/"+date.getFullYear();
                            for(var j = 0; j<myArr.length; j++){
                                var text = "slot "+(myArr[j].slot+1);
                                console.log("available="+myArr[j].available);
                                console.log("user="+myArr[j].patient);
                                if(myArr[j].available==true&&myArr[j].patient==""){
                                    text+=" libero";
                                }else if(myArr[j].available==false){
                                    text+=" disabilitato";
                                }else{
                                    if(role=="doctor"){
                                        text+=" occupato da "+myArr[j].patient;
                                    }else{
                                        if(username == myArr[j].patient){
                                            text+=" occupato da me";
                                        }else{
                                            text+=" occupato";
                                        }
                                    }
                                }
                                document.getElementById(""+j).innerHTML= text;
                                popAgenda();
                            }
                        }
            }
                }
            }
        </script>
    </head>
    <body>
        <%@ include file="/WEB-INF/jspf/menu.jspf" %>
        <a onclick='getDayAgend("yesterday")'>Indietro</a>
        <h1 id="day"></h1>
        <a onclick='getDayAgend("tomorrow")'>Avanti</a>
        <div class="slots">
            <div class="slot" id="0" onclick="register(0)">--</div>
            <div class="slot" id="1" onclick="register(1)">--</div>
            <div class="slot" id="2" onclick="register(2)">--</div>
            <div class="slot" id="3" onclick="register(3)">--</div>
            <div class="slot" id="4" onclick="register(4)">--</div>
            <div class="slot" id="5" onclick="register(5)">--</div>
            <div class="slot" id="6" onclick="register(6)">--</div>
            <div class="slot" id="7" onclick="register(7)">--</div>
        </div>
        <script type="text/javascript">
            window.onload = function(){
                getDayAgend("today");
            };
        </script>
    </body>
</html>
