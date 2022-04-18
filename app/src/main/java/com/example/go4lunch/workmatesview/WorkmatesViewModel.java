package com.example.go4lunch.workmatesview;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.repositories.UserRepository;
import com.example.go4lunch.user.User;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesViewModel extends ViewModel {
    private UserRepository userRepository;


    public WorkmatesViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private List<User> userList = new ArrayList<>();




}
