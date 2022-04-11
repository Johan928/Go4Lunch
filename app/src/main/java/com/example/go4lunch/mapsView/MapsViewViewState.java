package com.example.go4lunch.mapsView;

import android.location.Location;

import com.example.go4lunch.model.GooglePlaces;

import java.util.List;

public class MapsViewViewState {
    private Location location;
    private List<GooglePlaces.Results> places;

    public MapsViewViewState(Location location, List<GooglePlaces.Results> places) {
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
