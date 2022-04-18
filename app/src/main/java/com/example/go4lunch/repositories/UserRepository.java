package com.example.go4lunch.repositories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.user.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private static final String COLLECTION_NAME = "users";
    private static final String SELECTED_RESTAURANT_FIELD = "selectedRestaurantPlaceId";
    private static final String FAVORITES_RESTAURANTS_FIELD = "favoriteRestaurantsList";
    private static final String TAG = "123";
    private static volatile UserRepository instance;
    private DocumentSnapshot documentSnapshot;

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        UserRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (UserRepository.class) {
            if (instance == null) {
                instance = new UserRepository();
            }
            return instance;
        }
    }
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public Task<Void> signOut(Context context) {
        return AuthUI.getInstance().signOut(context);
    }

    public Task<Void> deleteUser(Context context) {
        return AuthUI.getInstance().delete(context);
    }

    private String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    // Get the Collection Reference
    private CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // Create User in Firestore
    public void createUser() {
        FirebaseUser user = getCurrentUser();
        Log.d(TAG, "createUser: " + user.getDisplayName());
        if (user != null) {
            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
            String username = user.getDisplayName();
            String uid = user.getUid();

            User userToCreate = new User(uid, username, urlPicture);

            Task<DocumentSnapshot> userData = getUserData();
            // If the user already exist in Firestore, we get his selectedRestaurantPlaceId
            userData.addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.contains(SELECTED_RESTAURANT_FIELD)) {
                    userToCreate.setSelectedRestaurantPlaceId((String) documentSnapshot.get(SELECTED_RESTAURANT_FIELD));
                }
                if (documentSnapshot.contains(FAVORITES_RESTAURANTS_FIELD)) {
                    userToCreate.setFavoriteRestaurantsList((List<String>) documentSnapshot.get(FAVORITES_RESTAURANTS_FIELD));
                }
                this.getUsersCollection().document(uid).set(userToCreate);
            });
        }
    }


    // Get User Data from Firestore
    public Task<DocumentSnapshot> getUserData() {
        String uid = this.getCurrentUserId();
        if (uid != null) {
            return this.getUsersCollection().document(uid).get();
        } else {
            return null;
        }
    }


/*
    public LiveData<List<DocumentSnapshot>> getUserList() {
        String uid = this.getCurrentUserId();
        MutableLiveData<List<DocumentSnapshot>> mutableLiveData = new MutableLiveData<>();
        final CollectionReference collectionReference = getUsersCollection();
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(TAG, "onEvent : listen failed");
                    return;
                }
                if (value != null) {
                    mutableLiveData.setValue(value.getDocuments());
                    Log.d(TAG, "onEvent: AA" + value.getDocuments().size());
                } else {
                    Log.d(TAG, "onEvent: "+ "value is null");
                }
            }
        });
        return  mutableLiveData;

    }*/

    public LiveData<List<User>> getUserList() {
        String uid = this.getCurrentUserId();
        List<User> userList = new ArrayList<>();
        MutableLiveData<List<User>> mutableLiveData = new MutableLiveData<>();
        final CollectionReference collectionReference = getUsersCollection();
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(TAG, "onEvent : listen failed");
                    return;
                }
                if (value != null) {
                    userList.clear();
                    for (DocumentSnapshot ds : value.getDocuments()) {

                        userList.add(ds.toObject(User.class));
                        Log.d(TAG, "onEvent: USER" + ds.toObject(User.class).getUsername());


                    }
                    Log.d(TAG, "onEvent: " + value.getDocuments().size());
                } else {
                    Log.d(TAG, "onEvent: " + "value is null");
                }
                mutableLiveData.setValue(userList);
            }
        });
        return mutableLiveData;

    }


    public LiveData<List<User>> getUserJoiningList(String placeId) {
        String uid = this.getCurrentUserId();
        List<User> userList = new ArrayList<>();
        MutableLiveData<List<User>> mutableLiveData = new MutableLiveData<>();
        final CollectionReference collectionReference = getUsersCollection();
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(TAG, "onEvent : listen failed");
                    return;
                }
                if (value != null) {
                    userList.clear();
                    for (DocumentSnapshot ds : value.getDocuments()) {
                        if (ds.toObject(User.class).getSelectedRestaurantPlaceId().equals(placeId) && !ds.toObject(User.class).getUid().equals(uid)) {
                            userList.add(ds.toObject(User.class));
                            Log.d(TAG, "onEvent: USER" + ds.toObject(User.class).getUsername());
                        }

                    }
                    Log.d(TAG, "onEvent: " + value.getDocuments().size());
                } else {
                    Log.d(TAG, "onEvent: " + "value is null");
                }
                mutableLiveData.setValue(userList);
            }
        });
        return mutableLiveData;

    }

    private void doGetUserJoiningList(String placeId) {
        getUserJoiningList(placeId);
    }

    public Task<Void> updateSelectedRestaurant(String placeId) {

        String uid = this.getCurrentUserId();
        if (uid != null) {
            Log.d(TAG, "updateSelectedRestaurant: " + uid + "-" + placeId);
            return this.getUsersCollection().document(uid).update(SELECTED_RESTAURANT_FIELD, placeId);
        } else {
            return null;
        }
    }


}
