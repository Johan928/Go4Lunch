package com.example.go4lunch.repository;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.Models.GooglePlaces;
import com.example.go4lunch.retrofit.MapsAPI;
import com.example.go4lunch.retrofit.RetrofitRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapRepository {
    private static final String TAG = "123";

    private final MapsAPI mapsAPI;


    public MapRepository() {
        mapsAPI = RetrofitRequest.getRetrofitInstance().create(MapsAPI.class);
    }

    public LiveData<List<GooglePlaces.Results>> getNearBySearch() {
        final MutableLiveData<List<GooglePlaces.Results>> data = new MutableLiveData<>();
        mapsAPI.getNearBySearch("37.4219983,-122.084").enqueue(new Callback<GooglePlaces>() {
            @Override
            public void onResponse(Call<GooglePlaces> call, Response<GooglePlaces> response) {
                data.setValue(response.body().getResults());
               // Log.d(TAG, "onResponseD: " + response.body().getResults().size());
            }

            @Override
            public void onFailure(Call<GooglePlaces> call, Throwable t) {
                data.setValue(null);
              //  Log.d(TAG, "onFailure: ");
            }
        });

        return data;
    }
}
