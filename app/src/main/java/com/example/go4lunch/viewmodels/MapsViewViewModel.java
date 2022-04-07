package com.example.go4lunch.viewmodels;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.Models.GooglePlaces;
import com.example.go4lunch.repository.MapRepository;

import java.util.List;

public class MapsViewViewModel extends ViewModel {

    private final MapRepository mapRepository;

    private MutableLiveData<List<GooglePlaces.Results>> getNearBySearchLiveData = new MutableLiveData<>();

    public MapsViewViewModel(MapRepository mapRepository, MutableLiveData<List<GooglePlaces.Results>> getNearBySearchLiveData) {
        this.getNearBySearchLiveData = getNearBySearchLiveData;

        this.mapRepository = new MapRepository();
        Log.d(TAG, "MapsViewViewModel: activated");

    }


    public MutableLiveData<List<GooglePlaces.Results>> getNearBySearchLiveData() {
        return getNearBySearchLiveData;
    }

public void populateLiveData() {
        getNearBySearchLiveData.setValue(mapRepository.getNearBySearch().getValue());
}

}
