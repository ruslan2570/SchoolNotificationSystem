package ru.ruslan2570.SchoolNotificationSystem;

import java.util.LinkedList;
import java.util.List;

public class UserList {

    List<User> users = new LinkedList<User>();

    public void addUser(User user){
        users.add(user);
    }

    public List<User> getUsers(){
        return new LinkedList<>(users);
    }


}
