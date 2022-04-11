package com.example.go4lunch.repositories;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.retrofit.MapsAPI;
import com.example.go4lunch.retrofit.RetrofitRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearbySearchRepository {

    private final MapsAPI mapsAPI;

    private LocationRepository locationRepository;
    private Location location;

    public NearbySearchRepository() {

        this.locationRepository = new LocationRepository();
        this.locationRepository.startLocationRequest();
        this.location = locationRepository.getLocationLiveData().getValue();
        mapsAPI = RetrofitRequest.getRetrofitInstance().create(MapsAPI.class);
    }

    //"-21.2903707,55.5057001" location.getLatitude() + "," + location.getLongitude()
    public LiveData<List<GooglePlaces.Results>> getNearBySearch() {
        final MutableLiveData<List<GooglePlaces.Results>> data = new MutableLiveData<>();
        this.location = locationRepository.getLocationLiveData().getValue();
        mapsAPI.getNearBySearch("-21.2903707,55.5057001").enqueue(new Callback<GooglePlaces>() {
            @Override
            public void onResponse(@NonNull Call<GooglePlaces> call, @NonNull Response<GooglePlaces> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(@NonNull Call<GooglePlaces> call, @NonNull Throwable t) {
                data.setValue(null);
            }
        });
        locationRepository.stopLocationRequest();
        return data;
    }
}
