package com.example.go4lunch.repositories;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.model.Place;
import com.example.go4lunch.retrofit.MapsAPI;
import com.example.go4lunch.retrofit.RetrofitRequest;
import com.example.go4lunch.user.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceRepository {
    MapsAPI mapsAPI = RetrofitRequest.getRetrofitInstance().create(MapsAPI.class);
    MutableLiveData<Place> placeMutableLiveData = new MutableLiveData<>();
    private int userCounter;

    public PlaceRepository() {
        mapsAPI = RetrofitRequest.getRetrofitInstance().create(MapsAPI.class);
    }

    private LiveData<Place> getPlaceDetails(String place_id) {
        final MutableLiveData<Place> data = new MutableLiveData<>();
        mapsAPI.getPlaceDetails(place_id).enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {
                if (response.isSuccessful() && response.body() !=null) {
                    data.setValue(response.body());

                }
            }

            @Override
            public void onFailure(Call<Place> call, Throwable t) {
                Log.d(TAG, "PlaceFailure: " + t.getMessage());
            }
        });


        return data;
    }

    public LiveData<Place> getPlaceLiveDetails(String placeId) {
        return getPlaceDetails(placeId);
    }

}
