package com.example.go4lunch.listview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.repositories.NearbySearchRepository;

import java.util.List;

public class ListViewViewModel extends ViewModel {

    private final NearbySearchRepository nearbySearchRepository;

    private LiveData<List<GooglePlaces.Results>> getNearBySearchLiveData;

    public ListViewViewModel(NearbySearchRepository nearbySearchRepository) {

        this.nearbySearchRepository = new NearbySearchRepository();

        getNearBySearchLiveData = nearbySearchRepository.getNearBySearch();


    }


    public LiveData<List<GooglePlaces.Results>> getNearBySearchLiveData() {
        return getNearBySearchLiveData;
    }



}
