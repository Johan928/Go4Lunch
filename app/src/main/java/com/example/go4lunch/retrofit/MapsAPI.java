package com.example.go4lunch.retrofit;


import android.location.Location;

import com.example.go4lunch.Models.GooglePlaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MapsAPI {
  @GET("maps/api/place/nearbysearch/json?radius=25000&types=restaurant&name=harbour&key=AIzaSyAO7A6BB1RAP7Ksmq4iJeL41X5JvtoGoyQ")
    Call<GooglePlaces> getNearBySearch(@Query("location") String location);


}
