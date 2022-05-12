package com.example.go4lunch.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.user.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserRepository {

    private static final String COLLECTION_NAME = "users";
    private static final String SELECTED_RESTAURANT_ID = "selectedRestaurantPlaceId";
    private static final String FAVORITES_RESTAURANTS_FIELD = "favoriteRestaurantsList";
    private static final String SELECTED_RESTAURANT_NAME = "selectedRestaurantName";
    private  static  final  String SELECTED_RESTAURANT_ADDRESS = "selectedRestaurantAddress";
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

    public String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    // Get the Collection Reference
    public CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // Get User Data from Firestore
    public Task<QuerySnapshot> getUserListInATask() {
        String uid = this.getCurrentUserId();
        if (uid != null) {
          return  this.getUsersCollection().get();
        } else {
            return null;
        }
    }


    // Create User in Firestore
    public void createUser() {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
            String username = user.getDisplayName();
            String uid = user.getUid();

            User userToCreate = new User(uid, username, urlPicture);

            Task<DocumentSnapshot> userData = getUserData();
            // If the user already exist in Firestore, we get his data and restore them
            userData.addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.contains(SELECTED_RESTAURANT_ID)) {
                    userToCreate.setSelectedRestaurantPlaceId((String) documentSnapshot.get(SELECTED_RESTAURANT_ID));
                }
                if (documentSnapshot.contains(FAVORITES_RESTAURANTS_FIELD)) {
                    userToCreate.setFavoriteRestaurantsList((ArrayList<String>) documentSnapshot.get(FAVORITES_RESTAURANTS_FIELD) );
                }
                if (documentSnapshot.contains(SELECTED_RESTAURANT_NAME)) {
                    userToCreate.setSelectedRestaurantName((String) documentSnapshot.get(SELECTED_RESTAURANT_NAME));
                }if (documentSnapshot.contains(SELECTED_RESTAURANT_ADDRESS)) {
                    userToCreate.setSelectedRestaurantAddress((String) documentSnapshot.get(SELECTED_RESTAURANT_ADDRESS));
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

    // Get User Data from Firestore
    public Task<DocumentSnapshot> getUserDataFromUid(String uid) {

        if (uid != null) {
            return this.getUsersCollection().document(uid).get();
        } else {
            return null;
        }
    }


    public LiveData<List<User>> getUserList() {
        String uid = this.getCurrentUserId();
        List<User> userList = new ArrayList<>();
        MutableLiveData<List<User>> mutableLiveData = new MutableLiveData<>();
        final CollectionReference collectionReference = getUsersCollection();
        collectionReference.addSnapshotListener((value, error) -> {
            if (error != null) {
                return;
            }
            if (value != null) {
                userList.clear();
                for (DocumentSnapshot ds : value.getDocuments()) {
                    userList.add(ds.toObject(User.class));
                }

            }
            mutableLiveData.setValue(userList);
        });
        return mutableLiveData;

    }



    public LiveData<List<User>> getUserJoiningList(String placeId) {
        String uid = this.getCurrentUserId();
        List<User> userList = new ArrayList<>();
        MutableLiveData<List<User>> mutableLiveData = new MutableLiveData<>();
        final CollectionReference collectionReference = getUsersCollection();
        collectionReference.addSnapshotListener((value, error) -> {
            if (error != null) {

                return;
            }
            if (value != null) {
                userList.clear();
                for (DocumentSnapshot ds : value.getDocuments()) {
                    if (Objects.requireNonNull(ds.toObject(User.class)).getSelectedRestaurantPlaceId() != null && Objects.requireNonNull(ds.toObject(User.class)).getSelectedRestaurantPlaceId().equals(placeId) && !Objects.requireNonNull(ds.toObject(User.class)).getUid().equals(uid)) {
                        userList.add(ds.toObject(User.class));

                    }

                }

            }
            mutableLiveData.setValue(userList);
        });
        return mutableLiveData;

    }


    public Task<Void> updateSelectedRestaurant(String placeId,String restaurantName,String restaurantAddress) {

        String uid = this.getCurrentUserId();
        if (uid != null) {
            return this.getUsersCollection().document(uid).update(SELECTED_RESTAURANT_ID, placeId,SELECTED_RESTAURANT_NAME,restaurantName,SELECTED_RESTAURANT_ADDRESS,restaurantAddress);
        } else {
            return null;
        }
    }
    public Task<Void> updateFavoritesRestaurantList(String placeId,boolean likedStatus) {

        String uid = this.getCurrentUserId();
        if (uid != null) {
            if (!likedStatus) {
                return this.getUsersCollection().document(uid).update(FAVORITES_RESTAURANTS_FIELD, FieldValue.arrayUnion(placeId));
            } else {
                return this.getUsersCollection().document(uid).update(FAVORITES_RESTAURANTS_FIELD, FieldValue.arrayRemove(placeId));
            }

        } else {
            return null;
        }
    }

}
