package com.example.go4lunch.workmatesview;

import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.model.Place;
import com.example.go4lunch.repositories.UserRepository;
import com.example.go4lunch.user.User;

import java.util.List;

public class WorkmatesViewState {

    private List<User> userList;
    public WorkmatesViewState(List<User> userList) {

        this.userList = userList;
    }


    public List<User> getUserList(){return userList;}
}
