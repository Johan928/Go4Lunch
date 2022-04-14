package com.example.go4lunch.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.model.Place;
import com.example.go4lunch.repositories.NearbySearchRepository;
import com.example.go4lunch.repositories.PlaceRepository;

import java.util.List;

public class DetailsViewModel extends ViewModel {
    private final MediatorLiveData<DetailsViewState> mMediator = new MediatorLiveData();
    private NearbySearchRepository nearbySearchRepository;
    private PlaceRepository placeRepository;


    public DetailsViewModel(NearbySearchRepository nearbySearchRepository, PlaceRepository placeRepository) {
        this.nearbySearchRepository = nearbySearchRepository;
        this.placeRepository = placeRepository;


    }

    private void loadData(String placeId) {

        LiveData<List<GooglePlaces.Results>> places = nearbySearchRepository.getNearBySearchLiveData();
        Place place = new Place();
        mMediator.addSource(places, results -> {
            combine(place, results);

            LiveData<Place> placeLiveData = placeRepository.getPlaceLiveDetails(placeId);

            mMediator.addSource(placeLiveData, place1 -> combine(place1, places.getValue()));
        });

    }


    private void combine(Place place, List<GooglePlaces.Results> placesList) {

        mMediator.postValue(new DetailsViewState(place, placesList));

    }

    //C'est ça qui est observé par la vue
    public LiveData<DetailsViewState> getDetailsViewLiveData(String placeId) {
        loadData(placeId);
        return mMediator;
    }

}
