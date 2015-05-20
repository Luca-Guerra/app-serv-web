/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lib.ManageXML;

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
            String operation = request.getParameter("operation");
            switch(operation){
                case "getAgenda":
                    String day = request.getParameter("day");
                    Date date = new Date();
                    if(day.equals("today")){
                        
                    }else if(day.equals("yesterday")){
                        Calendar c = Calendar.getInstance(); 
                        c.setTime(date); 
                        c.add(Calendar.DATE, 1);
                        date = c.getTime();
                    }else{
                        Calendar c = Calendar.getInstance(); 
                        c.setTime(date); 
                        c.add(Calendar.DATE, -1);
                        date = c.getTime();
                    }
                    
                    break;
                case "register":
                    break;
                case "unregister":
                    break;
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
