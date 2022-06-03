package ru.ruslan2570.SchoolNotificationSystem.models;

import java.util.ArrayList;
import java.util.List;

public class RoleList {

    List<Role> roles = new ArrayList<Role>();

    public void addRole(Role role){
        roles.add(role);
    }

    public List<Role> getRole(){
        return new ArrayList<>(roles);
    }

}
