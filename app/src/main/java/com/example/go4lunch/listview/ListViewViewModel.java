package com.example.go4lunch.listview;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.repositories.LocationRepository;
import com.example.go4lunch.repositories.NearbySearchRepository;
import com.example.go4lunch.repositories.UserRepository;
import com.example.go4lunch.retrofit.MapsAPI;
import com.example.go4lunch.user.User;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class ListViewViewModel extends ViewModel {

    private static final String TAG = "999";
    private final MediatorLiveData<ListViewViewState> mMediator = new MediatorLiveData();
    private final LocationRepository locationRepository;
    private final NearbySearchRepository nearbySearchRepository;
    private final UserRepository userRepository;
    private MapsAPI mapsAPI;
    private static final String SELECTED_RESTAURANT_FIELD = "selectedRestaurantPlaceId";

    public ListViewViewModel(LocationRepository locationRepository, NearbySearchRepository nearbySearchRepository, UserRepository userRepository) {
        this.locationRepository = locationRepository;
        this.nearbySearchRepository = nearbySearchRepository;
        this.userRepository = userRepository;

        LiveData<Location> location = locationRepository.getLocationLiveData();
        LiveData<List<User>> selectedRestaurantsList = userRepository.getUserList();
        LiveData<List<GooglePlaces.Results>> places = nearbySearchRepository.getNearBySearchLiveData();

        mMediator.addSource(location, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                combine(location, places.getValue(), selectedRestaurantsList.getValue());
            }
        });

        mMediator.addSource(places, new Observer<List<GooglePlaces.Results>>() {
            @Override
            public void onChanged(List<GooglePlaces.Results> results) {
                combine(location.getValue(), results, selectedRestaurantsList.getValue());
            }
        });


        mMediator.addSource(selectedRestaurantsList, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> selectedRestaurantsList) {
                combine(location.getValue(), places.getValue(), selectedRestaurantsList);
            }
        });
    }

    private void combine(Location location, List<GooglePlaces.Results> googlePlaces, List<User> selectedRestaurantsList) {

        if (location != null && googlePlaces != null && selectedRestaurantsList != null) {
            mMediator.postValue(new ListViewViewState(location, googlePlaces, selectedRestaurantsList));
        }

    }


    //observé par la vue
    public LiveData<ListViewViewState> getListViewLiveData() {
        return mMediator;
    }


}

