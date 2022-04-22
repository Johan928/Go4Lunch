package com.example.go4lunch.details;

import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.model.Place;
import com.example.go4lunch.user.User;

import java.util.List;

public class DetailsViewState {


    private Place place;
    private List<User> userList;

    public DetailsViewState(Place place, List<User> userList) {
        this.place = place;
        this.userList = userList;
    }

    public Place getPlace() {
        return place;
    }

    public List<User> getUserList() {
        return userList;
    }


}
