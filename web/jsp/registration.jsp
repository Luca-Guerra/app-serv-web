<%-- 
    Document   : registration
    Created on : 16-mag-2015, 14.50.51
    Author     : Riccardo
--%>

<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.OutputStream"%>
<%@page import="javax.xml.transform.stream.StreamResult"%>
<%@page import="java.io.File"%>
<%@page import="javax.xml.transform.dom.DOMSource"%>
<%@page import="org.w3c.dom.Element"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="lib.ManageXML"%>
<%@page import="models.Patient"%>
<%@page import="models.Doctor"%>
<%@page import="models.Account"%>
<%@page import="repositories.AccountRepository"%>
<%@page import="java.util.Enumeration"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="/public_webapp/styles/registration.css" media="screen" />
        <title>CRM Hospital</title>
    </head>
    <body>
        <%AccountRepository rep = new AccountRepository(this.getServletContext());%>
        <script>
            function generateDoctor(){
                var x = document.getElementById("selectRole");
                var div = document.getElementById("registrationDoctor");
                div.innerHTML="Seleziona dottore:";
                if(x.selectedIndex==1){
                    var select = document.createElement("select");
                    select.setAttribute("id","selDoc");
                    select.setAttribute("name","selDoc");
                    <%
                        for(int i=0;i<rep.GetAccounts().size();i++){
                            if(rep.GetAccounts().get(i).getRole().equals("doctor")){
                                Doctor doc = (Doctor)rep.GetAccounts().get(i);
                                %>
                                   var option = document.createElement("option");
                                   option.value = '<%=""+ doc.getUsername()%>';
                                   option.text = '<%= "dott. "+doc.getName()+" "+doc.getSurname() %>';
                                   select.options.add(option, <%=i%>);
                                   console.log("aggiungo opzione");
                                <%
                            }   
                        }
                    %>
                    div.appendChild(select);
                }else{
                    var sel = document.getElementById("selDoc");
                    div.innerHTML="";
                    document.removeChild(sel);
                }
            }
        </script>
        <div class="header">CRM HOSPITAL</div>
        <div class="registration">
            <% Enumeration flds = request.getParameterNames();
                if(!flds.hasMoreElements()){
                    //Primo caricamento
                %>
                    <h1>Registrazione</h1>
                    <form method="post" action="../../../public_webapp/jsp/registration.jsp">
                        Nome:<input type="text" name="name" placeholder="name" />
                        Cognome:<input type="text" name="surname" placeholder="surname" />
                        Username:<input type="text" name="username" placeholder="username" />
                        Password:<input type="password" name="password" placeholder="password" />
                        Ruolo:
                        <select id="selectRole" name="role" onchange="generateDoctor()">
                            <option value="doctor" selected>Dottore</option>
                            <option value="patient">Paziente</option>
                        </select>
                        <div id="registrationDoctor">
                            
                        </div>
                        <input class="btn" value="Registrati" type="submit" />
                    </form>
                <%
            }else{
                String name=request.getParameter("name");
                String surname=request.getParameter("surname");
                String username=request.getParameter("username");
                String password=request.getParameter("password");
                String role = request.getParameter("role");
                Account account = rep.GetAccount(username);
                if(account!=null){
                    %>
                    <div><h1>Account già presente!</h1></div>
                    <form method="post" action="../../../public_webapp/jsp/registration.jsp">
                        <input class="btn" value="Nuovo Account" type="submit" />
                    </form>
                    <%
                }else{
                    if(role.equals("patient")){
                        String personalDoctor=request.getParameter("selDoc");
                        System.out.println(name);
                        System.out.println(surname);
                        System.out.println(username);
                        System.out.println(password);
                        System.out.println(personalDoctor);
                        Patient pat = new Patient(name, surname, username, password, personalDoctor);
                        pat.setLastVisit(0);
                        ((Doctor) rep.GetAccount(personalDoctor)).addPatient(username, 0);
                        rep.GetAccounts().add(pat);
                        rep.writeAccounts(rep.GetAccounts());
                        ManageXML xml = new ManageXML();
                        Document doc = xml.newDocument();
                        Element messages = doc.createElement("messages");
                        doc.appendChild(messages);
                        
                        //String path=this.getServletContext().getContextPath()+"/WEB-INF/conversation/"+username+".xml";
                        ServletContext servletContext = getServletContext();
                        String path = servletContext.getRealPath("/WEB-INF/conversation/");
                        path+="/"+username+".xml";
                        System.out.println("PATH="+path);
                        File f = new File(path);
                        f.createNewFile();
                        OutputStream res = new FileOutputStream(path);
                        xml.transform(res, doc);
                    }else{
                        Doctor doc = new Doctor(name, surname, username, password);
                        rep.GetAccounts().add(doc);
                        rep.writeAccounts(rep.GetAccounts());
                    }
                    %>
                    <div><h1>Registrato</h1></div>
                    <form method="post" action="../../../public_webapp/jsp/access.jsp">
                        <input class="btn" value="Log In" type="submit" />
                    </form>
                    <%
                }
            }%>
        </div>
    </body>
</html>