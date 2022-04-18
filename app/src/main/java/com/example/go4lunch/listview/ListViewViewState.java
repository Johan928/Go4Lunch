package com.example.go4lunch.listview;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.user.User;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class ListViewViewState {
    private static final String TAG = "111";
    private Location location;
    private List<GooglePlaces.Results> places;
    private List<User> selectedRestaurantsList;

    public ListViewViewState(Location location, List<GooglePlaces.Results> places,List<User> selectedRestaurantsList) {
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
    public List<User> getSelectedRestaurantsList() { return selectedRestaurantsList;}


}
