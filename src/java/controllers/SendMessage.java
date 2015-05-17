/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.TransformerException;
import models.Account;
import models.Conversation;
import models.Doctor;
import models.Message;
import models.Patient;
import repositories.AccountRepository;
import repositories.ConversationRepository;

/**
 *
 * @author Riccardo
 */
@WebServlet(name = "SendMessage", urlPatterns = {"/SendMessage"})
public class SendMessage extends HttpServlet{
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Istanzio il repository
            System.out.println("RICHIESTA EFFETTUATA SEND MESSAGE");
            String sendRole = request.getParameter("role");
            String message = request.getParameter("message");
            String user=request.getParameter("patientUsername");
            System.out.println("role="+sendRole+" message="+message+" user="+user);
            String receiverRole="";
            AccountRepository repAcc = new AccountRepository(this.getServletContext());
            HttpSession session = request.getSession();
            List<Patient> pats=repAcc.getPatient();
            List<Doctor> docs=repAcc.getDoctors();
            if(sendRole.equals("patient")){
                receiverRole="doctor";
                String doctorUser="";
                for(int i=0; i<repAcc.getDoctors().size();i++){
                    if(((Doctor)repAcc.getDoctors().get(i)).hasPatient(user)){
                        doctorUser = ((Doctor)repAcc.getDoctors().get(i)).getUsername();
                    }
                }
                for(int i=0;i<((Doctor)repAcc.GetAccount(doctorUser)).getPatients().size();i++){
                    if(((Doctor)repAcc.GetAccount(doctorUser)).getPatients().get(i).equals(user)){
                        ((Doctor)repAcc.GetAccount(doctorUser)).getLastVisities().set(i, ((Doctor)repAcc.GetAccount(doctorUser)).getLastVisities().get(i)+1);
                    }
                }
                
            }else{
                receiverRole="patient";
                ((Patient)repAcc.GetAccount(user)).setLastVisit(((Patient)repAcc.GetAccount(user)).getLastVisit()+1);
            }
            List<Account> acc = repAcc.GetAccounts();
            repAcc.writeAccounts(repAcc.GetAccounts());
            // Setto il forward di default
            String forward = "jsp/conversation.jsp";
            ConversationRepository rep = new ConversationRepository(getServletContext(),user);
            Conversation conv = rep.GetConversation();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            rep.addXMLMessage(sendRole, receiverRole, message, date.toString());
            System.out.println("data="+date);
            response.sendRedirect(request.getContextPath() +"/"+ forward);
        } catch (TransformerException ex) {
            Logger.getLogger(SendMessage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
