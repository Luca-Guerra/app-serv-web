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
public class Doctor extends Account{
    private ArrayList<String> patients;
    private ArrayList<Integer> lastVisities;
    public Doctor(String name, String surname, String username, String password) {
        super(name, surname, username, password);
        patients = new ArrayList<String>();
        lastVisities = new ArrayList<Integer>();
    }

    @Override
    public String getRole() {
        return "doctor";
    }
    
    public void addPatient(String patient, int lastVisit){
        patients.add(patient);
        lastVisities.add(lastVisit);
    }
    
    public ArrayList<String> getPatients(){
        return patients;
    }
    
    public ArrayList<Integer> getLastVisities(){
        return lastVisities;
    }
    
    public void setPatients(ArrayList<String> patients){
        this.patients=patients;
    }
    
    public void setLastVisities(ArrayList<Integer> lastVisities){
        this.lastVisities=lastVisities;
    }   
    
    public boolean hasPatient(String patient){
        for(int i = 0; i<patients.size();i++){
            if(patients.get(i).equals(patient)){
                return true;
            }
        }
        return false;
    }
}