/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Date;

/**
 *
 * @author Riccardo
 */
public class Appointment {
    private Date data;
    private boolean available;
    private String patient;
    private int slot;
    public Appointment(Date data, boolean available, String patient, int slot){
        this.data=data;
        this.available=available;
        this.patient=patient;
        this.slot = slot;
    }
    
    public Date getData(){
        return data;
    }
    
    public boolean isAvailable(){
        return available;
    }
    
    public String getPatient(){
        return patient;
    }
    
    public int getSlot(){
        return slot;
    }
}
