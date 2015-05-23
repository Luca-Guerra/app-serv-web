/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

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
import com.google.gson.*;
import java.util.LinkedList;
import javax.servlet.*;
import lib.ManageXML;
import models.Appointment;
import models.Patient;
import models.UserQueuedAsync;
import org.w3c.dom.Document;
import repositories.AccountRepository;
import repositories.AgendaRepository;

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
            ManageXML mngXml= new ManageXML();
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
                    Gson gson = new Gson();
                    response.getOutputStream().print(gson.toJson(agenda.getAppointments(date)));
                    response.getOutputStream().flush();                    
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
                        gson = new Gson();
                        for (String destUser : contexts.keySet()) {
                            UserQueuedAsync value = contexts.get(destUser);
                            if (value.buffer instanceof AsyncContext) {
                                if(value.sameContext(date, doctor)){
                                    ServletOutputStream aos = ((AsyncContext) value.buffer).getResponse().getOutputStream();
                                    //mngXML.transform(aos, data);
                                    //aos.close();  
                                    aos.print(gson.toJson(agenda.getAppointments(date)));
                                    aos.flush();
                                    aos.close();
                                    ((AsyncContext) value).complete();
                                    contexts.put(destUser, new UserQueuedAsync(user, date, doctor, new LinkedList<String>()));
                                }
                            } else {
                                ((LinkedList<String>) value.buffer).addLast(gson.toJson(agenda.getAppointments(date)));
                            }
                        }             
                    }
                    break;
                case "popAgenda":
                    boolean async;
                    synchronized(this) {
                        LinkedList<String> list = (LinkedList<String>) contexts.get(user).buffer;
                        if (async=list.isEmpty()) {                        
                            AsyncContext asyncContext = request.startAsync();
                            asyncContext.setTimeout(10 * 1000);
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
                                            contexts.put(user,new UserQueuedAsync(user, (Date) session.getAttribute("date"), (String) session.getAttribute("doctor"), new LinkedList<> ()));
                                        }
                                        if (confirm) { 
                                            /*OutputStream tos = asyncContext.getResponse().getOutputStream();
                                            mngXML.transform(tos, answer);
                                            tos.close();                 */
                                            Gson gson = new Gson();
                                            response.getOutputStream().print(gson.toJson("timeout"));
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
                        os.close();  */     
                        gson = new Gson();
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
