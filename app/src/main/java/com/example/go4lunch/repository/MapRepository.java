package com.example.go4lunch.repository;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
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

    private final MapsAPI mapsAPI;


    public MapRepository() {
        Log.d(TAG, "MapRepository: ");
        mapsAPI = RetrofitRequest.getRetrofitInstance().create(MapsAPI.class);
    }

    public LiveData<List<GooglePlaces.Results>> getNearBySearch() {
        final MutableLiveData<List<GooglePlaces.Results>> data = new MutableLiveData<>();
        mapsAPI.getNearBySearch("37.4219983,-122.084").enqueue(new Callback<GooglePlaces>() {
            @Override
            public void onResponse(@NonNull Call<GooglePlaces> call, @NonNull Response<GooglePlaces> response) {
               if(response.isSuccessful() && response.body() !=null) {
                   data.setValue(response.body().getResults());
               }

               // Log.d(TAG, "onResponseD: " + response.body().getResults().size());
            }

            @Override
            public void onFailure(@NonNull Call<GooglePlaces> call, @NonNull Throwable t) {
                data.setValue(null);
              //  Log.d(TAG, "onFailure: ");
            }
        });

        return data;
    }
}
