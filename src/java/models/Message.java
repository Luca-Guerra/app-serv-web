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
public class Message {
    private String sender;
    private String receiver;
    private String type;
    private String text;
    private String dateTime;
    
    public Message(String sender, String receiver,String type, String text, String dateTime){
        this.sender= sender;
        this.receiver = receiver;
        this.type = type;
        this.text = text;
        this.dateTime = dateTime;
    }
    
    public void setSender(String sender){
        this.sender = sender;
    }
    
    public void setReceiver(String receiver){
        this.receiver = receiver;
    }
    
    public void setType(String type){
        this.type = type;
    }
    
    public void setText(String text){
        this.text = text;
    }
    
    public void setDateTime(String dateTime){
        this.dateTime = dateTime;
    }
    
    public String getSender(){
        return sender;
    }
    
    public String getReceiver(){
        return receiver;
    }
    
    public String getType(){
        return type;
    }
    
    public String getText(){
        return text;
    }
    
    public String getDateTime(){
        return dateTime;
    }
    
}
