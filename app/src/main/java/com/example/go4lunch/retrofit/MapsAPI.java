package com.example.go4lunch.retrofit;


import static com.example.go4lunch.BuildConfig.MAPS_API_KEY;

import android.location.Location;

import com.example.go4lunch.Models.GooglePlaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MapsAPI {
  @GET("maps/api/place/nearbysearch/json?radius=25000&types=restaurant&name=harbour&key="+MAPS_API_KEY)
    Call<GooglePlaces> getNearBySearch(@Query("location") String location);


}
