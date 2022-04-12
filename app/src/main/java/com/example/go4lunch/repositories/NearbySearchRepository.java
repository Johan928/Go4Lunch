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


    public NearbySearchRepository() {

           mapsAPI = RetrofitRequest.getRetrofitInstance().create(MapsAPI.class);
    }

    //"-21.2903707,55.5057001" location.getLatitude() + "," + location.getLongitude()
    public LiveData<List<GooglePlaces.Results>> getNearBySearch(Location location) {
        final MutableLiveData<List<GooglePlaces.Results>> data = new MutableLiveData<>();

        mapsAPI.getNearBySearch(location.getLatitude() + "," + location.getLongitude()).enqueue(new Callback<GooglePlaces>() {
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
        return data;
    }
}
