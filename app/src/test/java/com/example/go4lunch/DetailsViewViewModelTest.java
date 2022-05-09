package com.example.go4lunch;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.details.DetailsViewModel;
import com.example.go4lunch.details.DetailsViewState;
import com.example.go4lunch.model.Place;
import com.example.go4lunch.repositories.PlaceRepository;
import com.example.go4lunch.repositories.UserRepository;
import com.example.go4lunch.user.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import Utils.LiveDataTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class DetailsViewViewModelTest {
    @Rule
    public final InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    private final PlaceRepository placeRepository = Mockito.mock(PlaceRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);


    private final MutableLiveData<Place> getPlace = new MutableLiveData<>();
    private final MutableLiveData<List<User>> getUserList = new MutableLiveData<>();

    private DetailsViewModel detailsViewModel;

    @Before
    public void setup() {

        Mockito.doReturn(getPlace)
                .when(placeRepository)
                .getPlaceLiveDetails("PlaceID");
        Mockito.doReturn(getUserList)
                .when(userRepository)
                .getUserJoiningList("PlaceID");

        getPlace.setValue(getPlaceInfoIn());
        getUserList.setValue(getDefaultUsersIn());


        detailsViewModel = new DetailsViewModel(placeRepository, userRepository);
    }


    @Test
    public void nominalCase() {
        LiveDataTestUtils.observeForTesting(detailsViewModel.getDetailsViewLiveData("PlaceID"), new LiveDataTestUtils.OnObservedListener<DetailsViewState>() {
            @Override
            public void onObserved(DetailsViewState detailsViewState) {
                assertEquals(getDefaultDetailsViewState(),detailsViewState);

            }
        });

    }


    //Region IN
    public Place getPlaceInfoIn() {
        Place place =  new Place();

        Place.Result placeResult = new Place.Result();
        placeResult.setPlace_id("PlaceID");
        placeResult.setName("PlaceName");
        placeResult.setVicinity("PlaceAddress");
        place.setResult(placeResult);
        System.out.println(place.getResult().getPlace_id());
        return place;
    }

    public List<User> getDefaultUsersIn() {
        return Arrays.asList(
                new User("uid", "username", "urlpicture"),
                new User("uid2", "username2", "urlpicture2"),
                new User("uid3", "username3", "urlpicture3")
        );
    }
    //endregion

    //region OUT
    public Place getPlaceInfoOut() {
        Place place =  new Place();

        Place.Result placeResult = new Place.Result();
        placeResult.setPlace_id("PlaceID");
        placeResult.setName("PlaceName");
        placeResult.setVicinity("PlaceAddress");
        place.setResult(placeResult);
        return place;
    }

    public List<User> getDefaultUsersOut() {
        return Arrays.asList(
                new User("uid", "username", "urlpicture"),
                new User("uid2", "username2", "urlpicture2"),
                new User("uid3", "username3", "urlpicture3")
        );
    }
    public DetailsViewState getDefaultDetailsViewState() {

        return new DetailsViewState(getPlaceInfoOut(),getDefaultUsersOut());
    }
    //endregion

}
