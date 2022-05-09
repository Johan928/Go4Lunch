package com.example.go4lunch.details;

import com.example.go4lunch.model.Place;
import com.example.go4lunch.user.User;

import java.util.List;
import java.util.Objects;

public class DetailsViewState {


    private final Place place;
    private final List<User> userList;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetailsViewState that = (DetailsViewState) o;
        return Objects.equals(this.place.getResult().getPlace_id(), that.place.getResult().getPlace_id()) && Objects.equals(userList, that.userList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(place, userList);
    }

    @Override
    public String toString() {
        return "DetailsViewState{" +
                "place=" + place +
                ", userList=" + userList +
                '}';
    }
}
