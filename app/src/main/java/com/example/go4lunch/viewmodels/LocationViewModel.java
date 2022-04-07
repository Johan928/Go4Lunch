package com.example.go4lunch.viewmodels;

import static android.content.ContentValues.TAG;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.Models.GooglePlaces;
import com.example.go4lunch.repository.LocationRepository;

public class LocationViewModel extends ViewModel {

    LocationRepository locationRepository ;
    LiveData<Location> locationLiveData;

    public LocationViewModel(LocationRepository locationRepository) {
        this.locationRepository = new LocationRepository();
        this.locationLiveData = locationRepository.getLocationLiveData();
    }
    public LiveData<Location> getLocationLiveData() {
        //Log.d(TAG, "getLocationLiveData: " + locationRepository.getLocationLiveData().getValue().getLatitude());
        return locationLiveData;
    }
    public void startLocationRequest() {
        locationRepository.startLocationRequest();
    }

}
