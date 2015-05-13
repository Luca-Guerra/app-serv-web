/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
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
import models.Message;
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
    private ConversationRepository rep;
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
            System.out.println("RICHIESTA EFFETTUATA");
            String user = request.getParameter("patientUsername");
            String textIndex = request.getParameter("index");
            System.out.println("PatientUsername="+user);
            System.out.println("index="+textIndex);
            HttpSession session = request.getSession();
            rep = new ConversationRepository(getServletContext(),user);

            // Setto il forward di default
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
                //date
                Element dateTime = answer.createElement("dateTime");
                dateTime.setTextContent(indexMessage.getDateTime());
                msg.appendChild(dateTime);

                msgs.appendChild(msg);
            }
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
