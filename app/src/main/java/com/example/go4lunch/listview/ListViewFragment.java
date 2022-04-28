package com.example.go4lunch.listview;

import static com.example.go4lunch.BuildConfig.MAPS_API_KEY;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.Adapter.ListViewAdapter;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentListViewBinding;
import com.example.go4lunch.factory.ViewModelFactory;
import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.user.User;
import com.example.go4lunch.user.UserManager;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListViewFragment extends Fragment {

    private static final String TAG = ListViewFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayoutManager layoutManager;
    private final ArrayList<GooglePlaces.Results> googlePLacesList = new ArrayList<>();
    private ListViewViewModel listViewViewModel;
    private ListViewAdapter adapter;
    private FragmentListViewBinding binding;
    private LiveData<ListViewViewState> livedata;
    private final UserManager userManager = UserManager.getInstance();
    private List<User> selectedRestaurantList = new ArrayList<>();
    private LatLng myPosition;
    private PlacesClient placesClient;


    public ListViewFragment() {
        // Required empty public constructor
    }

    public static ListViewFragment newInstance() {
        return new ListViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Places.initialize(getActivity().getApplicationContext(), MAPS_API_KEY);
        placesClient = Places.createClient(getActivity().getApplicationContext());

    }

    private void init() {

        progressBar = binding.progressBar;
        recyclerView = binding.recyclerView;
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        ViewModelFactory vm = ViewModelFactory.getInstance();
        listViewViewModel = new ViewModelProvider(this, vm).get(ListViewViewModel.class);
        listViewViewModel.getListViewLiveData().observe(getViewLifecycleOwner(), listViewViewState -> {


            if (listViewViewState.getPlaces() != null && listViewViewState.getLocation() != null && listViewViewState.getSelectedRestaurantsList() != null) {
                selectedRestaurantList = listViewViewState.getSelectedRestaurantsList();
                myPosition = new LatLng(listViewViewState.getLocation().getLatitude(), listViewViewState.getLocation().getLongitude());
                progressBar.setVisibility(View.GONE);
                googlePLacesList.clear();
                googlePLacesList.addAll(listViewViewState.getPlaces());
                adapter = new ListViewAdapter(ListViewFragment.this.getContext(), googlePLacesList, myPosition, selectedRestaurantList);
                recyclerView.setAdapter(adapter);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.nav_menu, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.nav_search).getActionView();
        searchView.setBackgroundColor(getResources().getColor(R.color.white));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() == 0) {
                    adapter = new ListViewAdapter(getContext(), googlePLacesList, myPosition, selectedRestaurantList);
                } else if (newText.length() < 3) {
                    adapter = new ListViewAdapter(getContext(), new ArrayList<>(), myPosition, selectedRestaurantList);
                } else {
                    initAutocomplete(newText);
                }
                recyclerView.setAdapter(adapter);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initAutocomplete(String query) {
        // ArrayList<GooglePlaces.Results> filteredList = new ArrayList<>();
        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(myPosition.latitude - 0.02, myPosition.longitude - 0.02),
                new LatLng(myPosition.latitude + 0.02, myPosition.longitude + 0.02));
        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setLocationBias(bounds)
                .setOrigin(new LatLng(myPosition.latitude, myPosition.longitude))
                .setCountries("RE", "FR")
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setQuery(query)
                .build();


        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {

            ArrayList<GooglePlaces.Results> filteredList = new ArrayList<>();
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                for (GooglePlaces.Results r : googlePLacesList) {
                    if (r.getPlace_id().equals(prediction.getPlaceId())) {

                        filteredList.add(r);
                    }
                }

            }

            adapter = new ListViewAdapter(getContext(), filteredList, myPosition, selectedRestaurantList);
            recyclerView.setAdapter(adapter);
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;

            }
        });

    }
}