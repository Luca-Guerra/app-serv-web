/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1026.controllers;

import asw1026.AsyncAdapter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import javax.servlet.*;
import asw1026.ManageXML;
import asw1026.models.Agenda;
import asw1026.models.Appointment;
import asw1026.models.DateAppointment;
import asw1026.models.Patient;
import asw1026.models.UserQueuedAsync;
import org.w3c.dom.Document;
import asw1026.repositories.AccountRepository;
import asw1026.repositories.AgendaRepository;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamWriter;
import org.codehaus.jettison.mapped.MappedNamespaceConvention;
import org.codehaus.jettison.mapped.MappedXMLStreamWriter;

/**
 *
 * @author Riccardo
 */
@WebServlet(urlPatterns = {"/AgendaService"}, asyncSupported = true)
public class AgendaService extends HttpServlet{
    
    private HashMap<String, UserQueuedAsync> contexts = new HashMap<String, UserQueuedAsync>();
    String answer = null;
    OutputStream os;
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            DateAppointment dp = new DateAppointment();
            JAXBContext jc = JAXBContext.newInstance(DateAppointment.class);
            Marshaller m = jc.createMarshaller();
            MappedNamespaceConvention mnc = new MappedNamespaceConvention();
            XMLStreamWriter xmlStreamWriter = new MappedXMLStreamWriter(mnc, response.getWriter());
            HttpSession session = request.getSession();
            String role = (String) session.getAttribute("role");
            String user = (String) session.getAttribute("username");
            AccountRepository acc = new AccountRepository(this.getServletContext());
            String doctor="";
            if(role.equals("patient")){
                Patient p = (Patient) acc.GetAccount(user);
                doctor = p.getDoctor();
            }else{
                doctor = user;
            }
            session.setAttribute("doctor", doctor);
            AgendaRepository agenda = new AgendaRepository(this.getServletContext(),doctor);
            agenda.getAppointments();
            String operation = request.getParameter("operation");
            Date date = (Date) session.getAttribute("date");
            switch(operation){
                case "getAgenda":
                    String day = request.getParameter("day");                    
                    if(day.equals("today")){                
                        response.setContentType("application/json");
                        Calendar c = Calendar.getInstance(); 
                        c.setTime(date); 
                        
                    }else if(day.equals("yesterday")){
                        Calendar c = Calendar.getInstance(); 
                        c.setTime(date); 
                        c.add(Calendar.DATE, -1);
                        date = c.getTime();
                    }else{
                        Calendar c = Calendar.getInstance(); 
                        c.setTime(date); 
                        c.add(Calendar.DATE, 1);
                        date = c.getTime();
                    }
                    synchronized (this) {
                        contexts.put(user, new UserQueuedAsync(user, date, doctor, new LinkedList<String>()));
                    }
                    session.setAttribute("date", date);
                    response.setContentType("application/json");

                    //response.getOutputStream().print(gson.toJson(agenda.getAppointments(date)));
                    dp.setAppointment(agenda.getAppointments(date));
                    m.marshal(dp, xmlStreamWriter);
                    xmlStreamWriter.flush();
                    xmlStreamWriter.close();
                    //response.getOutputStream().flush();                    
                    break;
                case "register":
                    int slot = Integer.parseInt(request.getParameter("slot"));
                    Appointment p;
                    if(role.equals("patient")){
                         p = new Appointment(date, true, user, slot);
                    }else{
                        p = new Appointment(date, false, "", slot);
                    }
                    if(role.equals("doctor")){
                        if(agenda.hasAppointment(p)){
                            agenda.deleteAppointment(p);
                        }else{
                            agenda.addAppointment(p);
                        }
                    }else{
                        if(agenda.getAppointment(p)==null){
                            agenda.addAppointment(p);
                        }else{
                            if(user.equals(agenda.getAppointment(p).getPatient())){
                                agenda.deleteAppointment(p);
                            }
                        }
                    }
                    synchronized(this) {
                        for (String destUser : contexts.keySet()) {
                            UserQueuedAsync value = contexts.get(destUser);
                            System.out.println("Scorro i contesti");
                            if (value.buffer instanceof AsyncContext) {
                                if(value.sameContext(date, doctor)){
                                    System.out.println("Stesso Contesto!");
                                    PrintWriter aos = ((AsyncContext) value.buffer).getResponse().getWriter();
                                    //mngXML.transform(aos, data);
                                    //aos.close();  
                                    //aos.print(gson.toJson(agenda.getAppointments(date)));
                                    dp.setAppointment(agenda.getAppointments(date));
                                    m.marshal(dp, new MappedXMLStreamWriter(mnc, aos));
                                    xmlStreamWriter.flush();
                                    xmlStreamWriter.close();
                                    //aos.flush();
                                    //aos.close();
                                    ((AsyncContext) value.buffer).complete();
                                    contexts.put(destUser, new UserQueuedAsync(user, date, doctor, new LinkedList<String>()));
                                }
                            } else {
                                //((LinkedList<String>) value.buffer).addLast(gson.toJson(agenda.getAppointments(date)));
                                System.out.println("Ci entro qui?");
                                StringWriter stringw = new StringWriter();
                                XMLStreamWriter sw = new MappedXMLStreamWriter(mnc, stringw);
                                dp.setAppointment(agenda.getAppointments(date));
                                m.marshal(dp, sw);
                                sw.flush();
                                sw.close();
                                System.out.println("BUFFER VALUE COMET="+stringw.toString());
                                ((LinkedList<String>) value.buffer).addLast(stringw.toString());
                            }
                        }             
                    }
                    break;
                case "popAgenda":
                    boolean async;
                    System.out.println("POPAGENDA!");
                    synchronized(this) {
                        System.out.println("CLASSE="+contexts.get(user).buffer.getClass().getName());
                        LinkedList<String> list = (LinkedList<String>) contexts.get(user).buffer;
                        if (async=list.isEmpty()) {                        
                            AsyncContext asyncContext = request.startAsync();
                            asyncContext.setTimeout(30 * 1000);
                            asyncContext.addListener(new AsyncAdapter(){
                                @Override
                                public void onTimeout(AsyncEvent e) {
                                    try {
                                    AsyncContext asyncContext = e.getAsyncContext();
                                    String user = (String) ((HttpServletRequest) asyncContext.getRequest()).getSession().getAttribute("username");
                                    System.out.println("timeout event launched for: "+ user);
                                    /*ManageXML mngXML = new ManageXML();
                                    Document answer = mngXML.newDocument();
                                    answer.appendChild(answer.crea1teElement("timeout"));*/
                                    boolean confirm;
                                    synchronized(AgendaService.this) {
                                        if (confirm = (contexts.get(user).buffer instanceof AsyncContext))
                                            contexts.put(user,new UserQueuedAsync(user, (Date) session.getAttribute("date"), (String) session.getAttribute("doctor"), new LinkedList<String> ()));
                                        }
                                        if (confirm) { 
                                            /*OutputStream tos = asyncContext.getResponse().getOutputStream();
                                            mngXML.transform(tos, answer);
                                            tos.close();                 */
                                            response.getOutputStream().print("timeout");
                                            response.getOutputStream().flush();
                                            asyncContext.complete(); 
                                        }
                                } catch (Exception ex) { System.out.println(ex); }
                                }
                            });
                            contexts.put(user,new UserQueuedAsync(user, date, doctor, asyncContext));
                        } else{
                            answer=list.removeFirst();
                        }
                    }
                    if (!async) {
                        /*os = response.getOutputStream();
                        mngXML.transform(os, answer);
                        os.close();*/
                        response.getOutputStream().print(answer);
                        response.getOutputStream().flush();
                    }
                    break;
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
