package com.example.go4lunch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.Models.GooglePlaces;
import com.example.go4lunch.repository.MapRepository;

import java.util.List;

public class MapsViewViewModel extends ViewModel {

    private MapRepository mapRepository;

    private LiveData<List<GooglePlaces.Results>> getNearBySearchLiveData;

    public MapsViewViewModel(MapRepository mapRepository) {

        this.mapRepository = new MapRepository();
        this.getNearBySearchLiveData = mapRepository.getNearBySearch();

    }


    public LiveData<List<GooglePlaces.Results>> getNearBySearchLiveData() {
        return getNearBySearchLiveData;
    }



}
