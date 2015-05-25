/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1026.models;

import java.util.ArrayList;

/**
 *
 * @author Riccardo
 */
public class Conversation {
    private ArrayList<Message> messageList;
    
    public Conversation(){
        messageList = new ArrayList<Message>();
    }
    
    public void addMessage(Message msg){
        messageList.add(msg);
    }
    
    public ArrayList<Message> getMessageList(){
        return messageList;
    }
    
}
