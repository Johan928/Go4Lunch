package com.example.go4lunch.listview;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.repositories.LocationRepository;
import com.example.go4lunch.repositories.NearbySearchRepository;
import com.example.go4lunch.retrofit.MapsAPI;

import java.util.List;

public class ListViewViewModel extends ViewModel {

    private final MediatorLiveData<ListViewViewState> mMediator = new MediatorLiveData();
    private final LocationRepository locationRepository;
    private final NearbySearchRepository nearbySearchRepository;
    private MapsAPI mapsAPI;

    public ListViewViewModel(LocationRepository locationRepository, NearbySearchRepository nearbySearchRepository) {
        this.locationRepository = locationRepository;
        this.nearbySearchRepository = nearbySearchRepository;

        //Location myLocation = null;
        LiveData<Location> location = locationRepository.getLocationLiveData();


        mMediator.addSource(location, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                /**
                 We await location change to call the nearbysearch request
                 **/
                LiveData<List<GooglePlaces.Results>> places = nearbySearchRepository.getNearBySearchLiveData();
                combine(location, places.getValue());

                mMediator.addSource(places, new Observer<List<GooglePlaces.Results>>() {
                    @Override
                    public void onChanged(List<GooglePlaces.Results> results) {
                        combine(location, results);
                    }
                });
            }
        });


    }

    private void combine(Location one, List<GooglePlaces.Results> two) {

        mMediator.postValue(new ListViewViewState(one, two));

    }

    //C'est ça qui est observé par la vue
    public LiveData<ListViewViewState> getListViewLiveData() {
        return mMediator;
    }


}

