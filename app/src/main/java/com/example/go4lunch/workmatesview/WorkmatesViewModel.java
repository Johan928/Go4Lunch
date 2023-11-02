package com.example.go4lunch.workmatesview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.repositories.NearbySearchRepository;
import com.example.go4lunch.repositories.UserRepository;
import com.example.go4lunch.user.User;

import java.util.List;

public class WorkmatesViewModel extends ViewModel {
    private NearbySearchRepository nearbySearchRepository;
    private final MediatorLiveData<WorkmatesViewState> mMediator = new MediatorLiveData<>();


    public WorkmatesViewModel(UserRepository userRepository) {

        LiveData<List<User>> userList = userRepository.getUserList();

        mMediator.addSource(userList,
                this::combine);



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
