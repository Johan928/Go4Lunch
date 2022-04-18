package com.example.go4lunch.repositories;

import static android.content.ContentValues.TAG;

import android.location.Location;
import android.util.Log;

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
    private final MutableLiveData<List<GooglePlaces.Results>> getNearBySearchLiveData = new MutableLiveData<>();

    public NearbySearchRepository() {

           mapsAPI = RetrofitRequest.getRetrofitInstance().create(MapsAPI.class);
    }

    //"-21.2903707,55.5057001" home position fot test
    public LiveData<List<GooglePlaces.Results>> getNearBySearch(Location location) {
        final MutableLiveData<List<GooglePlaces.Results>> data = new MutableLiveData<>();
        Log.d(TAG, "getNearBySearch: CALLED");
        mapsAPI.getNearBySearch(location.getLatitude() + "," + location.getLongitude()).enqueue(new Callback<GooglePlaces>() {
            @Override
            public void onResponse(@NonNull Call<GooglePlaces> call, @NonNull Response<GooglePlaces> response) {
                if (response.isSuccessful() && response.body() != null) {

                    data.setValue(response.body().getResults());
                    getNearBySearchLiveData.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(@NonNull Call<GooglePlaces> call, @NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }




    public LiveData<List<GooglePlaces.Results>> getNearBySearchLiveData() {
        return getNearBySearchLiveData;
    }
}
