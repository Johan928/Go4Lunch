package com.example.go4lunch.listview;

import android.location.Location;

import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.user.User;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListViewViewState that = (ListViewViewState) o;
        return Objects.equals(location, that.location) && Objects.equals(places, that.places) && Objects.equals(selectedRestaurantsList, that.selectedRestaurantsList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, places, selectedRestaurantsList);
    }

    @Override
    public String toString() {
        return "ListViewViewState{" +
                "location=" + location +
                ", places=" + places +
                ", selectedRestaurantsList=" + selectedRestaurantsList +
                '}';
    }
}
