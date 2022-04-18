package com.example.go4lunch.details;

import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.model.Place;
import com.example.go4lunch.user.User;

import java.util.List;

public class DetailsViewState {

    private List<GooglePlaces.Results> placesList;
    private Place place;
    private List<User> userList;

    public DetailsViewState(Place place, List<GooglePlaces.Results> placesList, List<User> userList) {
        this.place = place;
        this.placesList = placesList;
        this.userList = userList;
    }

    public Place getPlace() {
        return place;
    }

    public List<GooglePlaces.Results> getPlaces() {
        return placesList;
    }

    public List<User> getUserList() {
        return userList;
    }


}
