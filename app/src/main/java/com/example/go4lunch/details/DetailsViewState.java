package com.example.go4lunch.details;

import android.location.Location;

import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.model.Place;

import java.util.List;

public class DetailsViewState {

    private List<GooglePlaces.Results> placesList;
    private Place place;

    public DetailsViewState(Place place, List<GooglePlaces.Results> placesList) {
this.place = place;
        this.placesList = placesList;
    }
public Place getPlace() {return  place;}
    public List<GooglePlaces.Results> getPlaces() {
        return placesList;
    }


}
