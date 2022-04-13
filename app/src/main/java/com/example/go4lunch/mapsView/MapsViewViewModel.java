package com.example.go4lunch.mapsView;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.repositories.LocationRepository;
import com.example.go4lunch.repositories.NearbySearchRepository;

import java.util.List;

public class MapsViewViewModel extends ViewModel {

    private final MediatorLiveData<MapsViewViewState> mMediator = new MediatorLiveData();

    /** We have to wait location change to call the nearbysearch request
     **/

    public MapsViewViewModel(LocationRepository locationRepository, NearbySearchRepository nearbySearchRepository) {

        LiveData<Location> location = locationRepository.getLocationLiveData();

        mMediator.addSource(location, location1 -> {

            LiveData<List<GooglePlaces.Results>> places = nearbySearchRepository.getNearBySearch(location1);
            combine(location1, places.getValue());

            mMediator.addSource(places, results -> combine(location1, results));
        });

    }

    private void combine(Location location, List<GooglePlaces.Results> placesList) {

        mMediator.setValue(new MapsViewViewState(location, placesList));

    }

    //C'est ça qui est observé par la vue
    public LiveData<MapsViewViewState> getMediatorLiveData() {
        return mMediator;
    }


}
