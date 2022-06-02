package ru.ruslan2570.SchoolNotificationSystem.models;

public class Message {

    public int messageId;
    public String text;
    public String author;
    public String role;

    public Message(int messageId, String text, String author, String role){
        this.messageId = messageId;
        this.text = text;
        this.author = author;
        this.role = role;
    }
}
