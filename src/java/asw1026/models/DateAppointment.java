/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1026.models;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Riccardo
 */
@XmlRootElement(name="agenda")
public class DateAppointment {
    ArrayList<Appointment> app;
    
    public ArrayList<Appointment> getAppointment(){
        return app;
    }
    
    public void setAppointment(ArrayList<Appointment> app){
        this.app = app;
    }
    
}
