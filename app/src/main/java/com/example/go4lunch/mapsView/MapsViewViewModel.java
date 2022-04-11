package com.example.go4lunch.mapsView;

import static android.content.ContentValues.TAG;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.repositories.LocationRepository;
import com.example.go4lunch.repositories.NearbySearchRepository;

import java.util.List;

public class MapsViewViewModel extends ViewModel {

    private final MediatorLiveData<MapsViewViewState> mMediator = new MediatorLiveData();
    private final LocationRepository locationRepository;
    private final NearbySearchRepository nearbySearchRepository;


    public MapsViewViewModel(LocationRepository locationRepository, NearbySearchRepository nearbySearchRepository) {
        this.locationRepository = locationRepository;
        this.nearbySearchRepository = nearbySearchRepository;

        LiveData<List<GooglePlaces.Results>> places = nearbySearchRepository.getNearBySearch();
        LiveData<Location> location = locationRepository.getLocationLiveData();


        mMediator.addSource(location, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                Log.d(TAG, "onChanged Location: " + location.getLatitude());
                combine(location, places.getValue());
            }
        });

        mMediator.addSource(places, new Observer<List<GooglePlaces.Results>>() {
            @Override
            public void onChanged(List<GooglePlaces.Results> results) {
                Log.d(TAG, "onChanged addSource: " + results.size());
                combine(location.getValue(), results);
            }
        });
    }

    private void combine(Location one, List<GooglePlaces.Results> two) {

        mMediator.setValue(new MapsViewViewState(one, two));

    }

    //C'est ça qui est observé par la vue
    public LiveData<MapsViewViewState> getMediatorLiveData() {
        return mMediator;
    }


}
