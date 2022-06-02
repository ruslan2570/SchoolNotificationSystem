package ru.ruslan2570.SchoolNotificationSystem.models;

public class User {

    public User(int userId, String username, String roleName){
        this.userId = userId;
        this.username = username;
        this.roleName = roleName;
    }

    @Override
    public String toString(){
        return "Id: " + userId + " Name: " + username + " role: " + roleName;
    }

    public int userId;
    public String username;
    public String roleName;
}
