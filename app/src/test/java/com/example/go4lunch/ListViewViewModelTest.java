package com.example.go4lunch;

import static org.junit.Assert.assertEquals;

import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.listview.ListViewViewModel;
import com.example.go4lunch.listview.ListViewViewState;
import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.repositories.LocationRepository;
import com.example.go4lunch.repositories.NearbySearchRepository;
import com.example.go4lunch.repositories.UserRepository;
import com.example.go4lunch.user.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils.LiveDataTestUtils;

public class ListViewViewModelTest {
    @Rule
    public final InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    private final LocationRepository locationRepository = Mockito.mock(LocationRepository.class);
    private final NearbySearchRepository nearbySearchRepository = Mockito.mock(NearbySearchRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final Location location = Mockito.mock(Location.class);

    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<GooglePlaces.Results>> places = new MutableLiveData<>();
    private final MutableLiveData<List<User>> selectedRestaurantsList = new MutableLiveData<>();

    private ListViewViewModel listViewViewModel;

    @Before
    public void setup() {
        Mockito.doReturn(-21.2903707).when(location).getLatitude();
        Mockito.doReturn(55.5057001).when(location).getLongitude();
        Mockito.doReturn(locationMutableLiveData)
                .when(locationRepository)
                .getLocationLiveData();
        Mockito.doReturn(places)
                .when(nearbySearchRepository)
                .getNearBySearchLiveData();
        Mockito.doReturn(selectedRestaurantsList)
                .when(userRepository)
                .getUserList();

        locationMutableLiveData.setValue(location);
        selectedRestaurantsList.setValue(getDefaultUsersIn());
        places.setValue(getDefaultRestaurantListIn());

        listViewViewModel = new ListViewViewModel(locationRepository, nearbySearchRepository, userRepository);
    }


    @Test
    public void nominalCase() {
        // WHEN
        LiveDataTestUtils.observeForTesting(listViewViewModel.getListViewLiveData(), listViewViewState -> {
            // THEN
            assertEquals(ListViewViewModelTest.this.getDefaultListViewViewState(), listViewViewState);

        });
    }


    //Region IN
    public List<GooglePlaces.Results> getDefaultRestaurantListIn() {
        GooglePlaces gp = new GooglePlaces();
        ArrayList<GooglePlaces.Results> resultsArrayList = new ArrayList<>();
        GooglePlaces.Results r1 = new GooglePlaces.Results();
        r1.setName("RESTO_O1");
        r1.setPlace_id("RESTO1_ID");
        r1.setVicinity("RESTO1_ADDRESS");
        GooglePlaces.Results r2 = new GooglePlaces.Results();
        r2.setName("RESTO2");
        r2.setPlace_id("RESTO2_ID");
        r2.setVicinity("RESTO2_ADDRESS");
        GooglePlaces.Results r3 = new GooglePlaces.Results();
        r3.setName("RESTO3");
        r3.setPlace_id("RESTO3_ID");
        r3.setVicinity("RESTO3_ADDRESS");

        resultsArrayList.add(r1);
        resultsArrayList.add(r2);
        resultsArrayList.add(r3);

        return resultsArrayList;
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
    public List<GooglePlaces.Results> getDefaultRestaurantListOut() {
        GooglePlaces gp = new GooglePlaces();
        ArrayList<GooglePlaces.Results> resultsArrayList = new ArrayList<>();
        GooglePlaces.Results r1 = new GooglePlaces.Results();
        r1.setName("RESTO_O1");
        r1.setPlace_id("RESTO1_ID");
        r1.setVicinity("RESTO1_ADDRESS");
        GooglePlaces.Results r2 = new GooglePlaces.Results();
        r2.setName("RESTO2");
        r2.setPlace_id("RESTO2_ID");
        r2.setVicinity("RESTO2_ADDRESS");
        GooglePlaces.Results r3 = new GooglePlaces.Results();
        r3.setName("RESTO3");
        r3.setPlace_id("RESTO3_ID");
        r3.setVicinity("RESTO3_ADDRESS");

        resultsArrayList.add(r1);
        resultsArrayList.add(r2);
        resultsArrayList.add(r3);

        return resultsArrayList;
    }

    public List<User> getDefaultUsersOut() {
        return Arrays.asList(
                new User("uid", "username", "urlpicture"),
                new User("uid2", "username2", "urlpicture2"),
                new User("uid3", "username3", "urlpicture3")
        );
    }
    public ListViewViewState getDefaultListViewViewState() {

        return new ListViewViewState(location,getDefaultRestaurantListOut(),getDefaultUsersOut());
    }
    //endregion

}
