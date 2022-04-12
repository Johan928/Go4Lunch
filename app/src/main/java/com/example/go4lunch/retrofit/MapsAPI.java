package com.example.go4lunch.retrofit;


import static com.example.go4lunch.BuildConfig.MAPS_API_KEY;

import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.model.Place;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MapsAPI {
  @GET("maps/api/place/nearbysearch/json?radius=25000&types=bakery&key="+MAPS_API_KEY)
    Call<GooglePlaces> getNearBySearch(@Query("location") String location);

  @GET("maps/api/place/details/json?fields=formatted_phone_number&key="+MAPS_API_KEY)
  Call<Place> getPlaceDetails(@Query("place_id") String place_id);

}
