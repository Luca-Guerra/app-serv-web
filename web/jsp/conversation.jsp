<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <link rel="stylesheet" href="/public_webapp/styles/global.css" media="screen" />
        <link rel="stylesheet" href="/public_webapp/styles/conversation.css" media="screen" />
        
        <title>CRM Hospital (Medico)</title>
        
        <script type="text/javascript">
            var lastIndex=0;
            var xmlHttp;
            var patientUsername;
            var role;
            var oldConv;
            //Caso di caricamento pagina
            //Quando diventa false vuol dire che sto caricando i messaggi vecchi
            var first = true;
            console.log("patientUsername=<%= session.getAttribute("patientUsername")%>")
            function getXml(){
                patientUsername = "<%= session.getAttribute("patientUsername")%>";
                role = "<%= session.getAttribute("role")%>";
                if(role=="patient"){
                    patientUsername = "<%= session.getAttribute("username")%>";
                }else{
                    patientUsername = "<%= session.getAttribute("patientUsername")%>";
                }
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
                        //Creo il Div con tutta la conversazione
                        var divConv = document.getElementById("conversation");
                        for (i=0;i<x.length;i++)
                        {
                            console.log("type="+x[i].childNodes[1].textContent);
                            if(x[i].childNodes[0].textContent == role){
                                var divrow = document.createElement("div");
                                divrow.setAttribute("class", "row");
                                var divboxme = document.createElement("div");
                                divboxme.setAttribute("class", "box me");
                                var divtext = document.createElement("div");
                                if(x[i].childNodes[2].textContent == "text"){
                                    var textNode = document.createTextNode(x[i].childNodes[3].textContent);
                                    divtext.appendChild(textNode);
                                }else if(x[i].childNodes[2].textContent == "img"){
                                    var divimg = document.createElement("img");
                                    divimg.setAttribute('height',200);
                                    divimg.setAttribute('width',120);
                                    divimg.setAttribute('src',x[i].childNodes[3].textContent);
                                    var diva = document.createElement("a");
                                    diva.href=x[i].childNodes[3].textContent;
                                    diva.setAttribute('href',x[i].childNodes[3].textContent);
                                    diva.setAttribute('target','_blank');
                                    //diva.innerHTML = "immagine";
                                    diva.appendChild(divimg);
                                    divtext.appendChild(diva);
                                }else{
                                    var diva = document.createElement("a"); 
                                    diva.href=x[i].childNodes[3].textContent;
                                    diva.setAttribute('href',x[i].childNodes[3].textContent);
                                    diva.setAttribute('target','_blank');
                                    diva.innerHTML = "allegato";
                                    divtext.appendChild(diva);
                                }
                                var divTime = document.createElement("div");
                                divTime.setAttribute("class","dateTime");
                                var textTime = document.createTextNode(x[i].childNodes[4].textContent)
                                divTime.appendChild(textTime);
                                divboxme.appendChild(divtext);
                                divboxme.appendChild(divTime);
                                divrow.appendChild(divboxme);
                                
                                if(divConv.childElementCount>0){
                                    divConv.insertBefore(divrow,divConv.childNodes[0]);
                                }else{
                                    divConv.appendChild(divrow);
                                }
                            }else{
                                var divrow = document.createElement("div");
                                divrow.setAttribute("class", "row");
                                var divboxme = document.createElement("div");
                                divboxme.setAttribute("class", "box their");
                                var divtext = document.createElement("div");
                                if(x[i].childNodes[2].textContent == "text"){
                                    var textNode = document.createTextNode(x[i].childNodes[3].textContent);
                                    divtext.appendChild(textNode);
                                }else if(x[i].childNodes[2].textContent == "img"){
                                    var divimg = document.createElement("img");
                                    divimg.setAttribute('height',200);
                                    divimg.setAttribute('width',120);
                                    divimg.setAttribute('src',x[i].childNodes[3].textContent);
                                    var diva = document.createElement("a");
                                    diva.href=x[i].childNodes[3].textContent;
                                    diva.setAttribute('href',x[i].childNodes[3].textContent);
                                    diva.setAttribute('target','_blank');
                                    //diva.innerHTML = "immagine";
                                    diva.appendChild(divimg);
                                    divtext.appendChild(diva);
                                }else{
                                    var diva = document.createElement("a"); 
                                    diva.href=x[i].childNodes[3].textContent;
                                    diva.setAttribute('href',x[i].childNodes[3].textContent);
                                    diva.setAttribute('target','_blank');
                                    diva.innerHTML = "allegato";
                                    divtext.appendChild(diva);
                                }
                                var divTime = document.createElement("div");
                                divTime.setAttribute("class","dateTime");
                                var textTime = document.createTextNode(x[i].childNodes[4].textContent)
                                divTime.appendChild(textTime);
                                divboxme.appendChild(divtext);
                                divboxme.appendChild(divTime);
                                divrow.appendChild(divboxme);
                                if(divConv.childElementCount>0){
                                    divConv.insertBefore(divrow,divConv.childNodes[0]);
                                }else{
                                    divConv.appendChild(divrow);
                                }
                            }
                        }
                        document.body.appendChild(divConv);
                        if(first){
                            //Nel caso di primo caricamento
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
                //Mando la richiesta con il nome del paziente e l'indice che mi rimanda gli 10 ultimi messaggi
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
                    <form method="post" action="/public_webapp/UploadServlet" enctype="multipart/form-data">
                    <input type="file" name="file" id="file"/>
                    <input type="hidden" name="role" value="<%= session.getAttribute("role")%>"/>
                    <input type="hidden" name="patientUsername" value="<%= session.getAttribute("patientUsername")%>"/>
                    <input class="btn" value="Invia File" type="submit" />
                </form>
        </div>
        
        
        <script type="text/javascript">
            window.onload = function(){
                getXml();
            };
        </script>
    </body>
</html>
