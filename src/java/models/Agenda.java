/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Riccardo
 */
public class Agenda {
    public ArrayList<Appointment> appointments;
    public Agenda(){
        appointments = new ArrayList<Appointment>();
    }
    
    public ArrayList<Appointment> getAppointments(){
        return appointments;
    }
    
    public void addAppointment(Appointment app){
        appointments.add(app);
    }
    
    public boolean hasAppointment(Appointment app){
        for(int i=0; i<appointments.size();i++){
            Date date1 = app.getData();
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date1);
            
            Date date2 = appointments.get(i).getData();
            Calendar c2 = Calendar.getInstance();
            c2.setTime(date2);
            
            if((c1.get(Calendar.DAY_OF_YEAR)==c2.get(Calendar.DAY_OF_YEAR))&&(c1.get(Calendar.YEAR)==c2.get(Calendar.YEAR))){
                if(appointments.get(i).getSlot()==app.getSlot()){
                    return true;
                }
            }
        }
        return false;
    }
    
    public Appointment getAppointment(Appointment app){
        for(int i=0; i<appointments.size();i++){
            Date date1 = app.getData();
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date1);
            
            Date date2 = appointments.get(i).getData();
            Calendar c2 = Calendar.getInstance();
            c2.setTime(date2);
            
            if((c1.get(Calendar.DAY_OF_YEAR)==c2.get(Calendar.DAY_OF_YEAR))&&(c1.get(Calendar.YEAR)==c2.get(Calendar.YEAR))){
                if(appointments.get(i).getSlot()==app.getSlot()){
                    return appointments.get(i);
                }
            }
        }
        return null;
    }
    
    public void deleteAppointment(Appointment app){
        for(int i=0; i<appointments.size();i++){
            Date date1 = app.getData();
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date1);
            
            Date date2 = appointments.get(i).getData();
            Calendar c2 = Calendar.getInstance();
            c2.setTime(date2);
            
            if((c1.get(Calendar.DAY_OF_YEAR)==c2.get(Calendar.DAY_OF_YEAR))&&(c1.get(Calendar.YEAR)==c2.get(Calendar.YEAR))){
                if(appointments.get(i).getSlot()==app.getSlot()){
                    appointments.remove(i);
                }
            }
        }
    }
}
