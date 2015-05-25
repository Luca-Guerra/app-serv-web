/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1026.models;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Fabio
 */
public class UserQueuedAsync {
    public String user;
    public Date date;
    public String doctor;
    public Object buffer;
    public UserQueuedAsync(String user, Date date, String doctor, Object buffer){
        this.user = user;
        this.date = date;
        this.doctor = doctor;
        this.buffer = buffer;
    }
    
    public boolean sameContext(Date myDate, String myDoctor){
        Calendar d1 = Calendar.getInstance();
        Calendar d2 = Calendar.getInstance();
        d1.setTime(date);
        d2.setTime(myDate);
        if((d1.get(Calendar.DAY_OF_YEAR)==d2.get(Calendar.DAY_OF_YEAR))&&(d1.get(Calendar.YEAR)==d2.get(Calendar.YEAR))){
            if(doctor.equals(myDoctor)){
                return true;
            }
        }
        return false;
    }
    
}
