package com.example.go4lunch.workmatesview;

import com.example.go4lunch.user.User;

import java.util.List;

public class WorkmatesViewState {

    private final List<User> userList;
    public WorkmatesViewState(List<User> userList) {

        this.userList = userList;
    }


    public List<User> getUserList(){return userList;}
}
