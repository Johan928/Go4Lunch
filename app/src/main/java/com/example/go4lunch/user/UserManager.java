package com.example.go4lunch.user;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.go4lunch.repositories.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class UserManager {

    private static final String TAG = "900";
    private static volatile UserManager instance;
    private UserRepository userRepository;

    private UserManager() {
        userRepository = UserRepository.getInstance();
    }

    public static UserManager getInstance() {
        UserManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized (UserManager.class) {
            if (instance == null) {
                instance = new UserManager();
            }
            return instance;
        }
    }

    public FirebaseUser getCurrentUser() {
        return UserRepository.getInstance().getCurrentUser();
    }

    public Task<Void> signOut(Context context) {
        return UserRepository.getInstance().signOut(context);
    }

   /* public Task<Void> deleteUser(Context context) {
        return UserRepository.getInstance().deleteUser(context);
    }*/

    public boolean isCurrentUserLogged() {
        return (UserRepository.getInstance().getCurrentUser() != null);
    }
    public void createUser(){
         userRepository.createUser();
    }

    public Task<Void> updateSelectedRestaurant(String placeId,String restaurantName,String restaurantAddress) {
       return userRepository.updateSelectedRestaurant(placeId,restaurantName,restaurantAddress);
    }

    public Task<Void> updateFavoritesRestaurantList(String placeId,boolean likedStatus) {
        return userRepository.updateFavoritesRestaurantList(placeId,likedStatus);
    }

    public Task<DocumentSnapshot> getUserData() {
        return   userRepository.getUserData();
    }


}
