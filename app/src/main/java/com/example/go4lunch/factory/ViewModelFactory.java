package com.example.go4lunch.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.repository.LocationRepository;
import com.example.go4lunch.repository.NearbySearchRepository;
import com.example.go4lunch.viewmodels.ListViewViewModel;
import com.example.go4lunch.viewmodels.LocationViewModel;
import com.example.go4lunch.viewmodels.MapsViewViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final NearbySearchRepository nearbySearchRepository;
    private final LocationRepository locationRepository;

    private static ViewModelFactory factory;

    public static ViewModelFactory getInstance() {

        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    factory = new ViewModelFactory();
                }
            }
        }
        return factory;
    }

    private ViewModelFactory() {
        this.nearbySearchRepository = new NearbySearchRepository();
        this.locationRepository = new LocationRepository();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapsViewViewModel.class)) {
            return (T) new MapsViewViewModel(nearbySearchRepository);
        }
        if (modelClass.isAssignableFrom(ListViewViewModel.class)) {
            return (T) new ListViewViewModel(nearbySearchRepository);
        }
        if (modelClass.isAssignableFrom(LocationViewModel.class)) {
            return (T) new LocationViewModel(locationRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");

    }
}
