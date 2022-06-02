package ru.ruslan2570.SchoolNotificationSystem.models;

public class Session {

    public Session(String id, User user, int remainingTime){
        this.id=id;
        this.user=user;
        this.remainingTime=remainingTime;
    }

    public String id;
    public User user;
    public int remainingTime;
}
