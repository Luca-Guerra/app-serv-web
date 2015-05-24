package repositories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import lib.ManageXML;
import models.Account;
import models.Doctor;
import models.Patient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AccountRepository extends BaseRepository {
    List<Account> accounts;
    public AccountRepository(ServletContext servletContext){
        super(servletContext, "/WEB-INF/accounts.xml");
        ReadAccounts();
    }
    // Fornisce l'account richiesto
    public Account GetAccount(String username){
        // Cerco l'account richiesto
        Optional<Account> account = GetAccounts().stream().filter(a -> a.getUsername().equals(username)).findFirst();
        if(account.isPresent())
            return account.get();
        
        return null;
    }
    
    public List<Account> GetAccounts(){
        return accounts;
    }
    
    public List<Account> ReadAccounts(){
        accounts = new ArrayList<>();
        // Ottengo tutti i nodi account
        NodeList nodeList = doc.getElementsByTagName("account");
        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            Node nNode = nodeList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                // Prendo l'elemento account e lo codifico nel modello Account
                accounts.add(ShapeAccount(eElement));
            }
        }
        return accounts;
    }
    // Da forma all'elemento trovato nell'xml
    private Account ShapeAccount(Element e){
        String name        = e.getElementsByTagName("name").item(0).getTextContent();
        String surname     = e.getElementsByTagName("surname").item(0).getTextContent();
        String username    = e.getElementsByTagName("username").item(0).getTextContent();
        String password    = e.getElementsByTagName("password").item(0).getTextContent();
        String role        = e.getElementsByTagName("role").item(0).getTextContent();
        if(role.equals("patient")){
            Element doctorNode = (Element)e.getElementsByTagName("doctor").item(0);
            String doctor = doctorNode.getElementsByTagName("name").item(0).getTextContent();
            int lastVisit = Integer.parseInt(doctorNode.getElementsByTagName("lastVisit").item(0).getTextContent());
            Patient patient = new Patient(name, surname, username, password, doctor);
            patient.setLastVisit(lastVisit);
            return patient;
        }else{
            Element patientsNode = (Element)e.getElementsByTagName("patients").item(0);
            NodeList patientsNodeList = patientsNode.getElementsByTagName("patient");
            ArrayList<String> patients = new ArrayList<String>();
            ArrayList<Integer> lastVisities = new ArrayList<Integer>();
            for(int i=0;i<patientsNodeList.getLength();i++){
                Node nNode = patientsNodeList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    patients.add(eElement.getElementsByTagName("name").item(0).getTextContent());
                    lastVisities.add(Integer.parseInt(eElement.getElementsByTagName("lastVisit").item(0).getTextContent()));
                }
            }
            Doctor doctor = new Doctor(name, surname, username, password);
            doctor.setPatients(patients);
            doctor.setLastVisities(lastVisities);
            return doctor;
        }
    }
    
    public List<Doctor> getDoctors(){
        List<Doctor> doctors = new ArrayList<>();
        for(int i = 0; i<accounts.size();i++){
            if(accounts.get(i).getClass().equals(Doctor.class)){
                doctors.add((Doctor) accounts.get(i));
            }
        }        
        return doctors;
    }
    
    public List<Patient> getPatient(){
        List<Patient> patients = new ArrayList<>();
        for(int i = 0; i<accounts.size();i++){
            if(accounts.get(i).getClass().equals(Patient.class)){
                patients.add((Patient) accounts.get(i));
            }
        }        
        return patients;
    }
    
    public void writeAccounts(List<Account> accountsList) throws TransformerException, IOException{
        try {
            ManageXML xml = new ManageXML();
            Document myDoc = xml.newDocument();
            Element accountsEl = myDoc.createElement("accounts");
            
            for(int i=0;i<accountsList.size();i++){
                Element account = myDoc.createElement("account");
                Element name = myDoc.createElement("name");
                String val=accountsList.get(i).getName();
                name.setTextContent(accountsList.get(i).getName());
                account.appendChild(name);
                
                Element surname = myDoc.createElement("surname");
                val = accountsList.get(i).getSurname();
                surname.setTextContent(accountsList.get(i).getSurname());
                account.appendChild(surname);
                
                Element username = myDoc.createElement("username");
                val = accountsList.get(i).getUsername();
                username.setTextContent(accountsList.get(i).getUsername());
                account.appendChild(username);
                
                Element password = myDoc.createElement("password");
                val = accountsList.get(i).getPassword();
                password.setTextContent(accountsList.get(i).getPassword());
                account.appendChild(password);
                
                Element role = myDoc.createElement("role");
                role.setTextContent(accountsList.get(i).getRole());
                account.appendChild(role);
                Element link=null;
                if(accountsList.get(i).getRole().equals("patient")){
                    Patient pat = (Patient) accountsList.get(i);
                    link = myDoc.createElement("doctor");
                    Element nameDoc = myDoc.createElement("name");
                    String nameDocS= pat.getDoctor();
                    nameDoc.setTextContent(pat.getDoctor());
                    
                    Element lastVisit = myDoc.createElement("lastVisit");
                    String lastV = ""+pat.getLastVisit();
                    lastVisit.setTextContent(lastV);
                    link.appendChild(nameDoc);
                    link.appendChild(lastVisit);
                }else{
                    Doctor doc = (Doctor) accountsList.get(i);
                    link = myDoc.createElement("patients");
                    for(int j = 0; j<doc.getPatients().size(); j++){
                        Element patient = myDoc.createElement("patient");
                        Element namePat = myDoc.createElement("name");
                        namePat.setTextContent(doc.getPatients().get(j));
                        patient.appendChild(namePat);
                        Element lastVisit = myDoc.createElement("lastVisit");
                        String lastV = ""+doc.getLastVisities().get(j);
                        lastVisit.setTextContent(lastV);
                        patient.appendChild(lastVisit);
                        link.appendChild(patient);
                    }                    
                }
                account.appendChild(link);
                accountsEl.appendChild(account);
            }
            myDoc.appendChild(accountsEl);
            doc = myDoc;
            writeRepository();
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(AccountRepository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(AccountRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
