package ru.ruslan2570.SchoolNotificationSystem.models;

import java.util.List;
import java.util.ArrayList;

public class MessageList {
    List<Message> messages = new ArrayList<Message>();

    public void addMessage(Message msg){
        messages.add(msg);
    }

    public List<Message> getMessages(){
        return new ArrayList<>(messages);
    }
}
