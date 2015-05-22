/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

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
}
