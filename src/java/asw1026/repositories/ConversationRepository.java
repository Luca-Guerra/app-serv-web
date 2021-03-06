/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1026.repositories;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.xml.transform.TransformerException;
import asw1026.models.Account;
import asw1026.models.Conversation;
import asw1026.models.Message;
import java.io.File;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Riccardo
 */
public class ConversationRepository extends BaseRepository {
    String schemaTxt;
    public ConversationRepository(ServletContext servletContext, String PatientUsername){
        super(servletContext, "/WEB-INF/xml/conversation/"+PatientUsername+".xml");
        schemaTxt = "xml-types/conversation.xsd";
        validateXML();
    }
        public void validateXML(){
        try
        {
            String filePath;
            filePath = context.getRealPath(fileName);
            /* DOMParser parser = new DOMParser();
             parser.setFeature("http://xml.org/sax/features/validation", true);
             parser.setProperty(
               "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", 
                          schema);
             ErrorChecker errors = new ErrorChecker();
             parser.setErrorHandler(errors);
             parser.parse(filepath);*/
            schemaTxt = context.getRealPath(schemaTxt);
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
            File xsdFile = new File(schemaTxt);
            Schema schema = sf.newSchema(xsdFile); 
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(filePath)));
            System.out.println("VA TUTTO BENE");
            
       }
       catch (Exception e) 
       {
           System.out.print("Problem parsing the file:"+e.getMessage());
       }
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
    
    public void addXMLMessage(String sender, String receiver,String type, String text, String date){
        Element message= doc.createElement("message");
        
        Element senderEl = doc.createElement("sender");
        senderEl.appendChild(doc.createTextNode(sender));
        
        Element receiverEl = doc.createElement("receiver");
        receiverEl.appendChild(doc.createTextNode(receiver));
        
        Element typeEl = doc.createElement("type");
        typeEl.appendChild(doc.createTextNode(type));
        
        Element textEl = doc.createElement("text");
        textEl.appendChild(doc.createTextNode(text));
        
        Element dateEl = doc.createElement("dateTime");
        dateEl.appendChild(doc.createTextNode(date));
        
        message.appendChild(senderEl);
        message.appendChild(receiverEl);
        message.appendChild(typeEl);
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
        String type = e.getElementsByTagName("type").item(0).getTextContent();
        String text = e.getElementsByTagName("text").item(0).getTextContent();
        String dateTime = e.getElementsByTagName("dateTime").item(0).getTextContent();
        Message msg = new Message(sender, receiver, type, text, dateTime);
        return msg;
    }
    
}
