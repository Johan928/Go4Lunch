package com.example.go4lunch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.Models.GooglePlaces;
import com.example.go4lunch.repository.NearbySearchRepository;

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
