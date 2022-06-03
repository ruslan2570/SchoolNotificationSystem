package ru.ruslan2570.SchoolNotificationSystem.models;

import java.util.ArrayList;
import java.util.List;

public class UserList {

    List<User> users = new ArrayList<User>();

    public void addUser(User user){
        users.add(user);
    }

    public List<User> getUsers(){
        return new ArrayList<>(users);
    }


}
