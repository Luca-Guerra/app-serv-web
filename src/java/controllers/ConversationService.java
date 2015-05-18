/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import lib.ManageXML;
import models.Account;
import models.Conversation;
import models.Doctor;
import models.Message;
import models.Patient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import repositories.AccountRepository;
import repositories.ConversationRepository;

/**
 *
 * @author Riccardo
 */
@WebServlet(name = "ConversationService", urlPatterns = {"/ConversationService"})
public class ConversationService extends HttpServlet{
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
                // Istanzio il repository
        try {
            System.out.println("RICHIESTA EFFETTUATA CONVERSATION SERVICE");
            String patientUser = request.getParameter("patientUsername");
            String textIndex = request.getParameter("index");
            System.out.println("PatientUsername="+patientUser);
            System.out.println("index="+textIndex);
            HttpSession session = request.getSession();
            String user = (String)session.getAttribute("username");
            System.out.println("servlet context="+getServletContext().getContextPath());
            AccountRepository accounts = new AccountRepository(getServletContext());
            System.out.println("user="+user);
            Account userAccount = accounts.GetAccount(user);
            if(userAccount.getRole().equals("patient")){
                ((Patient)accounts.GetAccount(user)).setLastVisit(0);
            }else{
                for(int i = 0; i<((Doctor)accounts.GetAccount(user)).getPatients().size();i++){
                    if(((Doctor)accounts.GetAccount(user)).getPatients().get(i).equals(patientUser)){
                        ((Doctor)accounts.GetAccount(user)).getLastVisities().set(i, 0);
                    }
                }
            }
            accounts.writeAccounts(accounts.GetAccounts());
            // Setto il forward di default
            ConversationRepository rep = new ConversationRepository(getServletContext(),patientUser);
            Conversation conv = rep.GetConversation();
            int index = Integer.parseInt(textIndex);
            ManageXML mngXml= new ManageXML();
            Document answer=mngXml.newDocument();
            Element msgs = answer.createElement("messages");
            answer.appendChild(msgs);
            for(int i=(conv.getMessageList().size()-index-1);i>(conv.getMessageList().size()-index)-10;i--){
                System.out.println("i="+i);
                System.out.println("limite="+(conv.getMessageList().size()-index-10));
                if(i<0){
                    break;
                }
                Element msg = answer.createElement("message");
                Message indexMessage = conv.getMessageList().get(i);
                //sender
                Element sender = answer.createElement("sender");
                sender.setTextContent(indexMessage.getSender());
                msg.appendChild(sender);
                //receiver
                Element receiver = answer.createElement("receiver");
                receiver.setTextContent(indexMessage.getReceiver());
                msg.appendChild(receiver);
                //text
                Element text = answer.createElement("text");
                text.setTextContent(indexMessage.getText());
                msg.appendChild(text);
                System.out.println("TESTO="+indexMessage.getText());
                //date
                Element dateTime = answer.createElement("dateTime");
                dateTime.setTextContent(indexMessage.getDateTime());
                msg.appendChild(dateTime);
                msgs.appendChild(msg);
            }
            System.out.println(answer);
            ServletContext servletContext = getServletContext();
            OutputStream os = response.getOutputStream();
            mngXml.transform(os, answer);
            os.close();
            
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(ConversationService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ConversationService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(ConversationService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        /*
        String forward = "";
        RequestDispatcher dispatcher = request.getRequestDispatcher(forward);  
        dispatcher.forward(request, response);
        */
    }


}
