package com.example.go4lunch.details;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.model.Place;
import com.example.go4lunch.repositories.NearbySearchRepository;
import com.example.go4lunch.repositories.PlaceRepository;
import com.example.go4lunch.repositories.UserRepository;
import com.example.go4lunch.user.User;

import java.util.List;

public class DetailsViewModel extends ViewModel {
    private static final String TAG = "111" ;
    private final MediatorLiveData<DetailsViewState> mMediator = new MediatorLiveData();
    private PlaceRepository placeRepository;
    private UserRepository userRepository;


    public DetailsViewModel(PlaceRepository placeRepository,UserRepository userRepository) {

        this.placeRepository = placeRepository;
        this.userRepository = userRepository;


    }

    private void loadData(String placeId) {


        LiveData<Place> placeLiveData = placeRepository.getPlaceLiveDetails(placeId);
        LiveData<List<User>> userList = userRepository.getUserJoiningList(placeId);


        mMediator.addSource(placeLiveData, new Observer<Place>() {
            @Override
            public void onChanged(Place place) {
                               combine(place,userList.getValue());
            }
        });

        mMediator.addSource(userList, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                combine(placeLiveData.getValue(),users);
            }
        });

    }


    private void combine(Place place,List<User> userList) {

        if (place != null && userList != null){
        mMediator.postValue(new DetailsViewState(place,userList));
        }
    }

    //C'est ça qui est observé par la vue
    public LiveData<DetailsViewState> getDetailsViewLiveData(String placeId) {
        loadData(placeId);
        return mMediator;
    }

}
