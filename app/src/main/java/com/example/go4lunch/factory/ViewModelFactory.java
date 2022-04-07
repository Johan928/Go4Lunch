package com.example.go4lunch.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.Models.GooglePlaces;
import com.example.go4lunch.repository.MapRepository;
import com.example.go4lunch.viewmodels.MapsViewViewModel;

import java.util.List;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final MapRepository mapRepository;

    private static ViewModelFactory factory;

    MutableLiveData<List<GooglePlaces.Results>> getNearBySearchLiveData;

    public static ViewModelFactory getInstance(Context context) {

        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    factory = new ViewModelFactory(context);
                }
            }
        }
        return factory;
    }

    private ViewModelFactory(Context context) {
        this.mapRepository = new MapRepository();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapsViewViewModel.class)) {
            return (T) new MapsViewViewModel(mapRepository, getNearBySearchLiveData);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");

    }
}
