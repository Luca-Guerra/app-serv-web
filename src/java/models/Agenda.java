/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;

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
    
}
