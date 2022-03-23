package com.example.go4lunch.manager;

import android.content.Context;

import com.example.go4lunch.repository.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

public class UserManager {

    private static volatile UserManager instance;

    private UserManager() {
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

    public Task<Void> deleteUser(Context context) {
        return UserRepository.getInstance().deleteUser(context);
    }

    public boolean isCurrentUserLogged() {
        return (UserRepository.getInstance().getCurrentUser() != null);
    }
}
