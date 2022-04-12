package com.example.go4lunch.listview;

import android.location.Location;

import com.example.go4lunch.model.GooglePlaces;

import java.util.List;

public class ListViewViewState {
    private Location location;
    private List<GooglePlaces.Results> places;

    public ListViewViewState(Location location, List<GooglePlaces.Results> places) {
        this.location = location;
        this.places = places;
    }
    public Location getLocation() {
        return  location;
    }
    public List<GooglePlaces.Results> getPlaces() {
        return places;
    }


}
