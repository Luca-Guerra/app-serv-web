/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repositories;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.xml.transform.TransformerException;
import models.Agenda;
import models.Appointment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Riccardo
 */
public class AgendaRepository extends BaseRepository{
    private Agenda agenda;
    public AgendaRepository(ServletContext context, String fileName) {
        super(context, fileName);
    }
    
    public Agenda getAgenda(){
        agenda = new Agenda();
        NodeList nodeList = doc.getElementsByTagName("appointment");
        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            Node nNode = nodeList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                // Prendo l'elemento account e lo codifico nel modello Account
                agenda.addAppointment(ShapeAppointments(eElement));
            }
        }
        return agenda;
    }
    
    public Appointment ShapeAppointments(Element e){
        try {
            String dateString = e.getElementsByTagName("date").item(0).getTextContent();
            String availableString     = e.getElementsByTagName("available").item(0).getTextContent();
            String patient    = e.getElementsByTagName("patient").item(0).getTextContent();
            int slot = Integer.parseInt(e.getElementsByTagName("slot").item(0).getTextContent());
            SimpleDateFormat sdf = new SimpleDateFormat();
            Date date = sdf.parse(dateString);
            boolean available=false;
            if(availableString.equals("true")){
                available=true;
            }
            Appointment app = new Appointment(date, available, patient, slot);
            return app;
        } catch (ParseException ex) {
            Logger.getLogger(AgendaRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void addAppointment(Appointment app){
        Element appointment= doc.createElement("appointment");
        
        Element date = doc.createElement("date");
        date.appendChild(doc.createTextNode(app.getData().toString()));
        
        Element available = doc.createElement("available");
        available.appendChild(doc.createTextNode(String.valueOf(app.isAvailable())));
        
        Element patient = doc.createElement("patient");
        patient.appendChild(doc.createTextNode(app.getPatient()));
        
        Element slot = doc.createElement("slot");
        slot.appendChild(doc.createTextNode(String.valueOf(app.getSlot())));
        
        appointment.appendChild(date);
        appointment.appendChild(available);
        appointment.appendChild(patient);
        appointment.appendChild(slot);
        doc.getElementsByTagName("appointment").item(0).appendChild(appointment);
        try {
            writeRepository();
        } catch (TransformerException ex) {
            Logger.getLogger(ConversationRepository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConversationRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
