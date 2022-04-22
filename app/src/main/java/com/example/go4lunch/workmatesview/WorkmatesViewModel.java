package com.example.go4lunch.workmatesview;

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
import com.example.go4lunch.retrofit.MapsAPI;
import com.example.go4lunch.user.User;
import com.example.go4lunch.user.UserManager;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesViewModel extends ViewModel {
    private UserRepository userRepository;
    private NearbySearchRepository nearbySearchRepository;
    private MediatorLiveData<WorkmatesViewState> mMediator = new MediatorLiveData();

    private final static String TAG = "666";



    public WorkmatesViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;

        LiveData<List<User>> userList = userRepository.getUserList();

        mMediator.addSource(userList, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> userList) {

                 combine(userList);

            }
        });



    }

    private void combine(List<User> userList) {
        if (userList != null) {
            mMediator.setValue(new WorkmatesViewState(userList));
        }

    }

    public LiveData<WorkmatesViewState> getWorkmatesViewState() {
        return mMediator;
    }


}
