package com.example.go4lunch.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.details.DetailsViewModel;
import com.example.go4lunch.listview.ListViewViewModel;
import com.example.go4lunch.mapsView.MapsViewViewModel;
import com.example.go4lunch.repositories.LocationRepository;
import com.example.go4lunch.repositories.NearbySearchRepository;
import com.example.go4lunch.repositories.PlaceRepository;
import com.example.go4lunch.repositories.UserRepository;
import com.example.go4lunch.workmatesview.WorkmatesViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final NearbySearchRepository nearbySearchRepository;
    private final LocationRepository locationRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

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
        this.placeRepository = new PlaceRepository();
        this.locationRepository.startLocationRequest();
        this.userRepository = UserRepository.getInstance();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ListViewViewModel.class)) {
            return (T) new ListViewViewModel(locationRepository,nearbySearchRepository,userRepository);
        } else if (modelClass.isAssignableFrom(MapsViewViewModel.class)) {
            return  (T) new MapsViewViewModel(locationRepository,nearbySearchRepository,userRepository);
        } else if (modelClass.isAssignableFrom(DetailsViewModel.class)) {
            return  (T) new DetailsViewModel(placeRepository,userRepository);
        } else if (modelClass.isAssignableFrom(WorkmatesViewModel.class)) {
            return  (T) new WorkmatesViewModel(userRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");

    }
}
