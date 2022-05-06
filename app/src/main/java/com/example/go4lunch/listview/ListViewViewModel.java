package com.example.go4lunch.listview;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.repositories.LocationRepository;
import com.example.go4lunch.repositories.NearbySearchRepository;
import com.example.go4lunch.repositories.UserRepository;
import com.example.go4lunch.retrofit.MapsAPI;
import com.example.go4lunch.user.User;

import java.util.List;

public class ListViewViewModel extends ViewModel {

    private static final String TAG = "999";
    private final MediatorLiveData<ListViewViewState> mMediator = new MediatorLiveData();
    private MapsAPI mapsAPI;
    private static final String SELECTED_RESTAURANT_FIELD = "selectedRestaurantPlaceId";

    public ListViewViewModel(LocationRepository locationRepository, NearbySearchRepository nearbySearchRepository, UserRepository userRepository) {

        LiveData<Location> location = locationRepository.getLocationLiveData();
        LiveData<List<User>> selectedRestaurantsList = userRepository.getUserList();
        LiveData<List<GooglePlaces.Results>> places = nearbySearchRepository.getNearBySearchLiveData();

        mMediator.addSource(location, location1 -> combine(location1, places.getValue(), selectedRestaurantsList.getValue()));

        mMediator.addSource(places, results -> combine(location.getValue(), results, selectedRestaurantsList.getValue()));


        mMediator.addSource(selectedRestaurantsList, selectedRestaurantsList1 -> combine(location.getValue(), places.getValue(), selectedRestaurantsList1));
    }

    private void combine(Location location, List<GooglePlaces.Results> googlePlaces, List<User> selectedRestaurantsList) {

        if (location != null && googlePlaces != null && selectedRestaurantsList != null) {
            mMediator.setValue(new ListViewViewState(location, googlePlaces, selectedRestaurantsList));
        }

    }

    public LiveData<ListViewViewState> getListViewLiveData() {
        return mMediator;
    }


}

