/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repositories;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.xml.transform.TransformerException;
import models.Account;
import models.Conversation;
import models.Message;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Riccardo
 */
public class ConversationRepository extends BaseRepository {
    
    public ConversationRepository(ServletContext servletContext, String PatientUsername){
        super(servletContext, "WEB-INF/conversation/"+PatientUsername+".xml");
    }
    public Conversation GetConversation(){
        // Cerco l'account richiesto
        Conversation conv = new Conversation();
        NodeList nodeList = doc.getElementsByTagName("message");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node nNode = nodeList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                // Prendo l'elemento account e lo codifico nel modello Account
                conv.addMessage(shapeMessage(eElement));
            }
            
        }
        return conv;
    }
    
    public void addXMLMessage(String sender, String receiver, String text, String date){
        Element message= doc.createElement("message");
        Element senderEl = doc.createElement("sender");
        senderEl.appendChild(doc.createTextNode(sender));
        Element receiverEl = doc.createElement("receiver");
        receiverEl.appendChild(doc.createTextNode(receiver));
        Element textEl = doc.createElement("text");
        textEl.appendChild(doc.createTextNode(text));
        Element dateEl = doc.createElement("dateTime");
        dateEl.appendChild(doc.createTextNode(date));
        message.appendChild(senderEl);
        message.appendChild(receiverEl);
        message.appendChild(textEl);
        message.appendChild(dateEl);
        doc.getElementsByTagName("messages").item(0).appendChild(message);
        try {
            writeRepository();
        } catch (TransformerException ex) {
            Logger.getLogger(ConversationRepository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConversationRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Message shapeMessage(Element e){
        String sender = e.getElementsByTagName("sender").item(0).getTextContent();
        String receiver = e.getElementsByTagName("receiver").item(0).getTextContent();
        String text = e.getElementsByTagName("text").item(0).getTextContent();
        String dateTime = e.getElementsByTagName("dateTime").item(0).getTextContent();
        Message msg = new Message(sender, receiver, text, dateTime);
        return msg;
    }
    
}
