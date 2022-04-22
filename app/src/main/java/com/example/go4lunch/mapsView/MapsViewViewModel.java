package com.example.go4lunch.mapsView;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
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
    private final NearbySearchRepository nearbySearchRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private LiveData<List<GooglePlaces.Results>> places = new MutableLiveData<>();

    /**
     * We have to wait location change to call the nearbysearch request
     **/

    public MapsViewViewModel(LocationRepository locationRepository, NearbySearchRepository nearbySearchRepository, UserRepository userRepository) {
        this.nearbySearchRepository = nearbySearchRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;

        LiveData<Location> location = locationRepository.getLocationLiveData();
        LiveData<List<User>> selectedRestaurantsList = userRepository.getUserList();
        // LiveData<List<GooglePlaces.Results>> places = nearbySearchRepository.getNearBySearch((location.getValue()));


        mMediator.addSource(location, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {

                places = nearbySearchRepository.getNearBySearch((location));
                mMediator.addSource(places, new Observer<List<GooglePlaces.Results>>() {
                    @Override
                    public void onChanged(List<GooglePlaces.Results> results) {
                        combine(location, results, selectedRestaurantsList.getValue());
                    }
                });

            }
        });


        mMediator.addSource(selectedRestaurantsList, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> userList) {

                combine(location.getValue(), places.getValue(), userList);
            }
        });


    }

    private void combine(Location location, List<GooglePlaces.Results> placesList, List<User> selectedRestaurantsList) {
        if (location != null && placesList != null && selectedRestaurantsList != null) {
            Log.d(TAG, "combine: OK");
            mMediator.setValue(new MapsViewViewState(location, placesList, selectedRestaurantsList));
        }


    }

    //C'est ça qui est observé par la vue
    public LiveData<MapsViewViewState> getMediatorLiveData() {
        return mMediator;
    }


}
