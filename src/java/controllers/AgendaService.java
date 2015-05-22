/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import javax.servlet.RequestDispatcher;
import lib.ManageXML;
import models.Appointment;
import models.Patient;
import net.sf.json.JSONObject;
import repositories.AccountRepository;
import repositories.AgendaRepository;

/**
 *
 * @author Riccardo
 */
@WebServlet(urlPatterns = {"/AgendaService"}, asyncSupported = true)
public class AgendaService extends HttpServlet{
    
    private HashMap<String, Object> contexts = new HashMap<String, Object>();
    
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
            AgendaRepository agenda = new AgendaRepository(this.getServletContext(),doctor);
            String operation = request.getParameter("operation");
            Date date = (Date) session.getAttribute("date");
            switch(operation){
                case "getAgenda":
                    String day = request.getParameter("day");
                    
                    if(day.equals("today")){
                        ObjectMapper mapper= new ObjectMapper();
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
                    agenda.addAppointment(p);
                    break;
                case "unregister":
                    break;
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
