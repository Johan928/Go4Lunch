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
    private final MutableLiveData<List<GooglePlaces.Results>> getNearBySearchLiveData = new MutableLiveData<>();

    public NearbySearchRepository() {

           mapsAPI = RetrofitRequest.getRetrofitInstance().create(MapsAPI.class);
    }

    //"-21.2903707,55.5057001" home position fot test
    public LiveData<List<GooglePlaces.Results>> getNearBySearch(Location location) {
        final MutableLiveData<List<GooglePlaces.Results>> data = new MutableLiveData<>();

        mapsAPI.getNearBySearch(location.getLatitude() + "," + location.getLongitude()).enqueue(new Callback<GooglePlaces>() {
            @Override
            public void onResponse(@NonNull Call<GooglePlaces> call, @NonNull Response<GooglePlaces> response) {
                if (response.isSuccessful() && response.body() != null) {
                    //updateDatasWithAddittionnalsInformations(response.body().getResults());
                    data.setValue(response.body().getResults());
                    getNearBySearchLiveData.setValue(response.body().getResults());
                   // Log.d(TAG, "Vicinity: " + response.body().getResults().get(0).getVicinity());
                }
            }

            @Override
            public void onFailure(@NonNull Call<GooglePlaces> call, @NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    /*private void updateDatasWithAddittionnalsInformations(List<GooglePlaces.Results> results) {
        String placeId;
        // Place place = new Place();

        for (GooglePlaces.Results r : results) {
            placeId = r.getPlace_id();
            Log.d(TAG, "onRes: " + placeId);
            mapsAPI.getPlaceDetails(placeId).enqueue(new Callback<Place>() {
                @Override
                public void onResponse(@NonNull Call<Place> call, @NonNull Response<Place> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        r.setWebsite(response.body().getResult().getWebsite());
                        r.setFormatted_address(response.body().getResult().getFormatted_address());
                        r.setFormatted_phone_number(response.body().getResult().getFormatted_phone_number());
                        r.setOpening_hours(response.body().getResult().getOpening_hours());


                    } else {
                        Log.d(TAG, "onResponse: NULL");
                    }
                }

                @Override
                public void onFailure(Call<Place> call, Throwable t) {
                    Log.d(TAG, "onResponse: failed " + t.getStackTrace());
                }
            });

        }
    }*/


    public LiveData<List<GooglePlaces.Results>> getNearBySearchLiveData() {
        return getNearBySearchLiveData;
    }
}
