package com.example.go4lunch.listview;

import android.location.Location;

import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.user.User;

import java.util.List;

public class ListViewViewState {
    private static final String TAG = "111";
    private final Location location;
    private final List<GooglePlaces.Results> places;
    private final List<User> selectedRestaurantsList;

    public ListViewViewState(Location location, List<GooglePlaces.Results> places, List<User> selectedRestaurantsList) {
        this.location = location;
        this.places = places;
        this.selectedRestaurantsList = selectedRestaurantsList;

    }

    public Location getLocation() {
        return location;
    }
    public List<GooglePlaces.Results> getPlaces() {
        return places;
    }
    public List<User> getSelectedRestaurantsList() { return selectedRestaurantsList;}


}
