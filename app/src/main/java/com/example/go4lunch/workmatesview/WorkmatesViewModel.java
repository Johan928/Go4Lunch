package com.example.go4lunch.workmatesview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.repositories.NearbySearchRepository;
import com.example.go4lunch.repositories.UserRepository;
import com.example.go4lunch.user.User;

import java.util.List;

public class WorkmatesViewModel extends ViewModel {
    private final UserRepository userRepository;
    private NearbySearchRepository nearbySearchRepository;
    private MediatorLiveData<WorkmatesViewState> mMediator = new MediatorLiveData();

    private final static String TAG = "666";



    public WorkmatesViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;

        LiveData<List<User>> userList = userRepository.getUserList();

        mMediator.addSource(userList,
                userList1 -> combine(userList1));



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
