<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="/public_webapp/styles/global.css" media="screen" />
        <title>CRM Hospital (Medico)</title>
        
        <script type="text/javascript">
            var lastIndex=0;
            var x;
            //window.onload=getXml();
            var xmlHttp;
            var patientUsername = "<%= session.getAttribute("username")%>";
            var role = "<%= session.getAttribute("role")%>";
            
            function getXml(){
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
                        var divConv = document.createElement("div");
                        divConv.setAttribute("class", "conversation");
                        for (i=(x.length-1);i>=0;i--)
                        {
                            if(x[i].children[0].textContent == role){
                                var divrow = document.createElement("div");
                                divrow.setAttribute("class", "row");
                                var divboxme = document.createElement("div");
                                divrow.setAttribute("class", "box me");
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
                                divConv.appendChild(divrow);
                            }else{
                                var divrow = document.createElement("div");
                                divrow.setAttribute("class", "row");
                                var divboxme = document.createElement("div");
                                divrow.setAttribute("class", "box their");
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
                                divConv.appendChild(divrow);
                            }
                            document.body.appendChild(divConv);
                        }
                        
                        
                        
                        
                    }
                }
                console.log("nome paziente="+patientUsername);
                var parameter = "patientUsername="+patientUsername+"&"+"index=0"
                console.log(parameter);
                xmlHttp.send("patientUsername="+patientUsername+"&"+"index="+lastIndex);
                lastIndex+=10;
                console.log("richiesta spedita")
            }
        </script>
    </head>
    <body>
        <%@ include file="/WEB-INF/jspf/menu.jspf" %>
        <div>
            user:<%= session.getAttribute("username")%>
            role:<%= session.getAttribute("role")%>
        </div>
        <input type="button" value="getXml" onclick="getXml()">
        <div class="conversation"></div>
        <form method="post" action="/public_webapp/ConversationService">
                <input type="text" name="patientUsername" placeholder="patientUsername" value="marco" />
                <input type="text" name="index" placeholder="index" value="0"/>
                <input class="btn" value="getConversation" type="submit" />
        </form>
        
        <div class="send-msg-box">
            <div>
                <textarea></textarea>
                <input type="submit" value="invia" />
            </div>
        </div>
        
        
        <script type="text/javascript">
            // Apro la pagina in fondo
            window.onload = function(){
                var body = document.body, html = document.documentElement;
                // Ottengo l'altezza della pagina
                var height = Math.max( body.scrollHeight, body.offsetHeight, html.clientHeight, html.scrollHeight, html.offsetHeight );
                // Scrollo la pagina fino in fondo
                window.scrollTo(0,height);
            };
        </script>
    </body>
</html>
