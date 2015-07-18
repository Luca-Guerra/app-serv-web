/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1026.repositories;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.chart.PieChart.Data;
import javax.servlet.ServletContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import asw1026.ManageXML;
import asw1026.models.Agenda;
import asw1026.models.Appointment;
import java.io.File;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Riccardo
 */
public class AgendaRepository extends BaseRepository{
    private Agenda agenda;
    String schemaTxt;
    public AgendaRepository(ServletContext context, String fileName) {
        super(context, "/WEB-INF/xml/agenda/"+fileName+".xml");
        agenda = new Agenda();
        schemaTxt="xml-types/agenda.xsd";
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
    public ArrayList<Appointment> getAppointments(){
        NodeList nodeList = doc.getElementsByTagName("appointment");
        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            Node nNode = nodeList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                // Prendo l'elemento account e lo codifico nel modello Account
                agenda.addAppointment(ShapeAppointments(eElement));
            }
        }
        return agenda.getAppointments();
    }
    
    public Appointment ShapeAppointments(Element e){
        try {
            String dateString = e.getElementsByTagName("date").item(0).getTextContent();
            String availableString     = e.getElementsByTagName("available").item(0).getTextContent();
            String patient    = e.getElementsByTagName("patient").item(0).getTextContent();
            int slot = Integer.parseInt(e.getElementsByTagName("slot").item(0).getTextContent());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        String dateS = sdf.format(app.getData());
        date.appendChild(doc.createTextNode(dateS));
        
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
        doc.getElementsByTagName("agenda").item(0).appendChild(appointment);
        try {
            writeRepository();
        } catch (TransformerException ex) {
            Logger.getLogger(ConversationRepository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConversationRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<Appointment> getAppointments(Date date){
        ArrayList<Appointment> appointments= new ArrayList<Appointment>();
        for(int i = 0; i<8; i++){
            Appointment ap = new Appointment();
            ap.setData(date);
            ap.setAvailable(true);
            ap.setPatient("");
            ap.setSlot(i);
            appointments.add(ap);
        }
        Calendar c = Calendar.getInstance(); 
        c.setTime(date); 
        getAppointments();
        for(int i=0;i<agenda.getAppointments().size();i++){
            Date dateEl = agenda.getAppointments().get(i).getData();
            Calendar cEl = Calendar.getInstance(); 
            cEl.setTime(dateEl); 
            if((c.get(Calendar.DAY_OF_YEAR)==cEl.get(Calendar.DAY_OF_YEAR))&&(c.get(Calendar.YEAR)==cEl.get(Calendar.YEAR))){
                appointments.set(agenda.getAppointments().get(i).getSlot(),agenda.getAppointments().get(i));
            }
        }
        return appointments;
    }
    
    public void deleteAppointment(Appointment app) throws IOException{
        try {
            ManageXML xml = new ManageXML();
            Document myDoc = xml.newDocument();
            doc = myDoc;
            Element agendaEl = myDoc.createElement("agenda");
            myDoc.appendChild(agendaEl);
            agenda.deleteAppointment(app);
            writeRepository();
            for(int i=0;i<agenda.getAppointments().size();i++){
                addAppointment(agenda.getAppointments().get(i));
            }
        } catch (TransformerException ex) {
            Logger.getLogger(AgendaRepository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(AgendaRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean hasAppointment(Appointment app){
        return agenda.hasAppointment(app);
    }
    
    public Appointment getAppointment(Appointment app){
        return agenda.getAppointment(app);
    }
    
}
