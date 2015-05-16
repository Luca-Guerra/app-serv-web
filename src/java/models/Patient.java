/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Riccardo
 */
public class Patient extends Account{
    private String doctor;
    private int lastVisit;
    public Patient(String name, String surname, String username, String password, String doctor) {
        super(name, surname, username, password);
        this.doctor=doctor;
    }

    @Override
    public String getRole() {
        return "patient";
    }
    
    public void setDoctor(String doctor){
        this.doctor=doctor;
    }
    
    public String getDoctor(){
        return doctor;
    }
    
    public void setLastVisit(int i){
        lastVisit=i;
    }
    
    public int getLastVisit(){
        return lastVisit;
    }
    
}