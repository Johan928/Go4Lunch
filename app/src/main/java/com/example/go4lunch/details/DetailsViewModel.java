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
    private NearbySearchRepository nearbySearchRepository;
    private PlaceRepository placeRepository;
    private UserRepository userRepository;


    public DetailsViewModel(NearbySearchRepository nearbySearchRepository, PlaceRepository placeRepository,UserRepository userRepository) {
        this.nearbySearchRepository = nearbySearchRepository;
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;


    }

    private void loadData(String placeId) {

        LiveData<List<GooglePlaces.Results>> gPlaces = nearbySearchRepository.getNearBySearchLiveData();
      //  Place place = new Place();
        LiveData<Place> placeLiveData = placeRepository.getPlaceLiveDetails(placeId);
        LiveData<List<User>> userList = userRepository.getUserJoiningList(placeId);

        mMediator.addSource(gPlaces, new Observer<List<GooglePlaces.Results>>() {
            @Override
            public void onChanged(List<GooglePlaces.Results> results) {
                Log.d(TAG, "onChanged: GPLACES" );
                combine(placeLiveData.getValue(), results,userList.getValue());

            }
        });



        mMediator.addSource(placeLiveData, new Observer<Place>() {
            @Override
            public void onChanged(Place place) {
                Log.d(TAG, "onChanged: place" );
                combine(place, gPlaces.getValue(),userList.getValue());
            }
        });

        mMediator.addSource(userList, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                Log.d(TAG, "onChanged: userlist " + users.size()  );
                combine(placeLiveData.getValue(),gPlaces.getValue(),users);
            }
        });

    }


    private void combine(Place place, List<GooglePlaces.Results> placesList,List<User> userList) {

        if (place != null && placesList != null && userList != null){
        mMediator.postValue(new DetailsViewState(place, placesList,userList));
        }
    }

    //C'est ça qui est observé par la vue
    public LiveData<DetailsViewState> getDetailsViewLiveData(String placeId) {
        loadData(placeId);
        return mMediator;
    }

}
