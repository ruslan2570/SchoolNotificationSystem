package ru.ruslan2570.SchoolNotificationSystem.models;

import java.util.List;
import java.util.LinkedList;

public class MessageList {
    List<Message> messages = new LinkedList<Message>();

    public void addMessage(Message msg){
        messages.add(msg);
    }

    public List<Message> getMessages(){
        return new LinkedList<>(messages);
    }
}
