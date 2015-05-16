<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <link rel="stylesheet" href="/public_webapp/styles/global.css" media="screen" />
        
        <title>CRM Hospital (Medico)</title>
        
        <script type="text/javascript">
            var lastIndex=0;
            var xmlHttp;
            var patientUsername;
            var role;
            var oldConv;
            var first = true;
            console.log("patientUsername=<%= session.getAttribute("patientUsername")%>")
            function getXml(){
                patientUsername = "<%= session.getAttribute("patientUsername")%>";
                role = "<%= session.getAttribute("role")%>";
                xmlHttp = new XMLHttpRequest();
                xmlHttp.open("POST","../ConversationService",true)
                xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                //xmlHttp.setRequestHeader("Content-length", 2);
                //xmlHttp.setRequestHeader("Connection", "close");
                xmlHttp.onreadystatechange=function(){
                    if(xmlHttp.readyState==4 && xmlHttp.status == 200){
                        xmlDoc = xmlHttp.responseXML;
                        console.log("file:");
                        console.log(xmlDoc);
                        var txt,x,i;
                        txt="";
                        x=xmlDoc.getElementsByTagName('message');
                        console.log("x="+x);
                        var divConv = document.getElementById("conversation");
                        for (i=0;i<x.length;i++)
                        {
                            if(x[i].children[0].textContent == role){
                                var divrow = document.createElement("div");
                                divrow.setAttribute("class", "row");
                                var divboxme = document.createElement("div");
                                divboxme.setAttribute("class", "box me");
                                var divtext = document.createElement("div");
                                var textNode = document.createTextNode(x[i].children[2].textContent);
                                divtext.appendChild(textNode);
                                var divTime = document.createElement("div");
                                divTime.setAttribute("class","dateTime");
                                var textTime = document.createTextNode(x[i].children[3].textContent)
                                divTime.appendChild(textTime);
                                divboxme.appendChild(divtext);
                                divboxme.appendChild(divTime);
                                divrow.appendChild(divboxme);
                                
                                if(divConv.childElementCount>0){
                                    divConv.insertBefore(divrow,divConv.children[0]);
                                }else{
                                    divConv.appendChild(divrow);
                                }
                            }else{
                                var divrow = document.createElement("div");
                                divrow.setAttribute("class", "row");
                                var divboxme = document.createElement("div");
                                divboxme.setAttribute("class", "box their");
                                var divtext = document.createElement("div");
                                var textNode = document.createTextNode(x[i].children[2].textContent);
                                divtext.appendChild(textNode);
                                var divTime = document.createElement("div");
                                divTime.setAttribute("class","dateTime");
                                var textTime = document.createTextNode(x[i].children[3].textContent)
                                divTime.appendChild(textTime);
                                divboxme.appendChild(divtext);
                                divboxme.appendChild(divTime);
                                divrow.appendChild(divboxme);
                                if(divConv.childElementCount>0){
                                    divConv.insertBefore(divrow,divConv.children[0]);
                                }else{
                                    divConv.appendChild(divrow);
                                }
                            }
                        }
                        document.body.appendChild(divConv);
                        if(first){
                            var body = document.body, html = document.documentElement;
                            // Ottengo l'altezza della pagina
                            var height = Math.max( body.scrollHeight, body.offsetHeight, html.clientHeight, html.scrollHeight, html.offsetHeight );
                            // Scrollo la pagina fino in fondo
                            window.scrollTo(0,height);
                            first=false;
                        }
                    } 
                }
                console.log("nome paziente="+patientUsername);
                var parameter = "patientUsername="+patientUsername+"&"+"index=0"
                console.log(parameter);
                xmlHttp.send("patientUsername="+patientUsername+"&"+"index="+lastIndex);
                lastIndex+=9;
                console.log("richiesta spedita")
            }
        </script>
    </head>
    <body>
        <%@ include file="/WEB-INF/jspf/menu.jspf" %>
        <div onclick="getXml()"><a>Carica Altro</a></div>
        <div id="conversation"></div>
        <div class="space-msg-box"></div>
        <div class="send-msg-box">
                <form method="post" action="/public_webapp/SendMessage">
                    <input type="text" name="message"/>
                    <input type="hidden" name="role" value="<%= session.getAttribute("role")%>"/>
                    <input type="hidden" name="patientUsername" value="<%= session.getAttribute("patientUsername")%>"/>
                    <input class="btn" value="Invia" type="submit" />
                </form>
        </div>
        
        
        <script type="text/javascript">
            window.onload = function(){
                getXml();
            };
        </script>
    </body>
</html>
