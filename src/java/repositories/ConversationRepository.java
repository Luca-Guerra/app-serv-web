/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repositories;

import java.util.Optional;
import javax.servlet.ServletContext;
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
    
    private Message shapeMessage(Element e){
        String sender = e.getElementsByTagName("sender").item(0).getTextContent();
        String receiver = e.getElementsByTagName("receiver").item(0).getTextContent();
        String text = e.getElementsByTagName("text").item(0).getTextContent();
        String dateTime = e.getElementsByTagName("dateTime").item(0).getTextContent();
        Message msg = new Message(sender, receiver, text, dateTime);
        return msg;
    }
    
}
