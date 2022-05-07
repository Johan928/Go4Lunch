package com.example.go4lunch.mapsView;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.repositories.LocationRepository;
import com.example.go4lunch.repositories.NearbySearchRepository;
import com.example.go4lunch.repositories.UserRepository;
import com.example.go4lunch.user.User;

import java.util.List;

public class MapsViewViewModel extends ViewModel {

    private static final String TAG = "123";
    private final MediatorLiveData<MapsViewViewState> mMediator = new MediatorLiveData();
   private LiveData<List<GooglePlaces.Results>> places = new MutableLiveData<>();


    public MapsViewViewModel(LocationRepository locationRepository, NearbySearchRepository nearbySearchRepository, UserRepository userRepository) {

        LiveData<Location> location = locationRepository.getLocationLiveData();
        LiveData<List<User>> selectedRestaurantsList = userRepository.getUserList();


        mMediator.addSource(location, location1 -> {

            places = nearbySearchRepository.getNearBySearch((location1));
            mMediator.addSource(places, results -> combine(location1, results, selectedRestaurantsList.getValue()));

        });


        mMediator.addSource(selectedRestaurantsList, userList -> combine(location.getValue(), places.getValue(), userList));


    }

    private void combine(Location location, List<GooglePlaces.Results> placesList, List<User> selectedRestaurantsList) {
        if (location != null && placesList != null && selectedRestaurantsList != null) {
            mMediator.setValue(new MapsViewViewState(location, placesList, selectedRestaurantsList));
        }


    }


    public LiveData<MapsViewViewState> getMapsViewLiveData() {
        return mMediator;
    }


}
