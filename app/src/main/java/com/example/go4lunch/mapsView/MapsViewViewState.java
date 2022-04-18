package com.example.go4lunch.mapsView;

import android.location.Location;

import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.user.User;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class MapsViewViewState {
    private Location location;
    private List<GooglePlaces.Results> places;
    private List<User> selectedRestaurantsList;

    public MapsViewViewState(Location location, List<GooglePlaces.Results> places,List<User> selectedRestaurantsList) {
        this.location = location;
        this.places = places;
        this.selectedRestaurantsList = selectedRestaurantsList;
    }
    public Location getLocation() {
        return  location;
    }
    public List<GooglePlaces.Results> getPlaces() {
        return places;
    }
    public List<User> getSelectedRestaurantsList() {return selectedRestaurantsList;}


}
