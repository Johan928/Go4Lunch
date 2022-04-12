package com.example.go4lunch.listview;

import static android.content.ContentValues.TAG;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.mapsView.MapsViewViewState;
import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.model.Place;
import com.example.go4lunch.repositories.LocationRepository;
import com.example.go4lunch.repositories.NearbySearchRepository;
import com.example.go4lunch.retrofit.MapsAPI;
import com.example.go4lunch.retrofit.RetrofitRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListViewViewModel extends ViewModel {

    private final MediatorLiveData<ListViewViewState> mMediator = new MediatorLiveData();
    private final LocationRepository locationRepository;
    private final NearbySearchRepository nearbySearchRepository;
    private MapsAPI mapsAPI;

    public ListViewViewModel(LocationRepository locationRepository, NearbySearchRepository nearbySearchRepository) {
        this.locationRepository = locationRepository;
        this.nearbySearchRepository = nearbySearchRepository;

        //Location myLocation = null;
        LiveData<Location> location = locationRepository.getLocationLiveData();


        mMediator.addSource(location, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                /**
                 We await location change to call the nearbysearch request
                 **/
                LiveData<List<GooglePlaces.Results>> places = nearbySearchRepository.getNearBySearch(location);
                combine(location, places.getValue());

                mMediator.addSource(places, new Observer<List<GooglePlaces.Results>>() {
                    @Override
                    public void onChanged(List<GooglePlaces.Results> results) {

                        String placeId;
                        Place place = new Place();
                        mapsAPI = RetrofitRequest.getRetrofitInstance().create(MapsAPI.class);
                        for (GooglePlaces.Results r : results) {
                            placeId = r.getPlace_id();
                            Log.d(TAG, "onRes: " + placeId);
                            mapsAPI.getPlaceDetails(placeId).enqueue(new Callback<Place>() {
                                @Override
                                public void onResponse(Call<Place> call, Response<Place> response) {
                                    if (response.isSuccessful() && response != null) {
                                        Log.d(TAG, "onResponse: OK");
                                        //  r.setTest(response.body().getResult().getFormatted_phone_number());

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
                        combine(location, results);
                    }
                });
            }
        });


    }

    private void combine(Location one, List<GooglePlaces.Results> two) {

        mMediator.setValue(new ListViewViewState(one, two));

    }

    //C'est ça qui est observé par la vue
    public LiveData<ListViewViewState> getListViewLiveData() {
        return mMediator;
    }


}

