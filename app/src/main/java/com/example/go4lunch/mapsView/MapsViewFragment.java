package com.example.go4lunch.mapsView;

import static com.example.go4lunch.BuildConfig.MAPS_API_KEY;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
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
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentMapsViewBinding;
import com.example.go4lunch.details.DetailsActivity;
import com.example.go4lunch.factory.ViewModelFactory;
import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.user.User;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapsViewFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "123";
    private final ArrayList<GooglePlaces.Results> googlePlaces = new ArrayList<>();
    private GoogleMap googleMap;
    private Location myCurrentLocation;
    private boolean firstStart = true;
    private List<User> selectedRestaurantList;
    private static final String SELECTED_RESTAURANT_FIELD = "selectedRestaurantPlaceId";
    private ProgressBar progressBar;
    private FragmentMapsViewBinding binding;
    private Toolbar toolbar;
    private PlacesClient placesClient;


    public MapsViewFragment() {
        // Required empty public constructor
    }

    public static MapsViewFragment newInstance() {

        return new MapsViewFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Places.initialize(requireActivity().getApplicationContext(), MAPS_API_KEY);
        placesClient = Places.createClient(requireActivity().getApplicationContext());

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);
        progressBar = binding.progressBarMaps;
        ViewModelFactory vmf = ViewModelFactory.getInstance();
        MapsViewViewModel mapsViewViewModel = new ViewModelProvider(this, vmf).get(MapsViewViewModel.class);
        LiveData<MapsViewViewState> liveData = mapsViewViewModel.getMapsViewLiveData();
        liveData.observe(getViewLifecycleOwner(), mapsViewViewState -> {
            if (mapsViewViewState.getLocation() != null && mapsViewViewState.getPlaces() != null && mapsViewViewState.getSelectedRestaurantsList() != null) {
                progressBar.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                if (mapsViewViewState.getPlaces() != null && mapsViewViewState.getSelectedRestaurantsList() != null) {

                    selectedRestaurantList = mapsViewViewState.getSelectedRestaurantsList();
                    googlePlaces.clear();
                    googlePlaces.addAll(mapsViewViewState.getPlaces());
                    updatePlacesOnMap(googleMap, googlePlaces);
                }

                if (mapsViewViewState.getLocation() != null) {
                    myCurrentLocation = mapsViewViewState.getLocation();
                    if (firstStart && googleMap != null) {
                        LatLng home = new LatLng(myCurrentLocation.getLatitude(), myCurrentLocation.getLongitude());
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(home, 12));
                        firstStart = false;
                    }

                }

            }

        });

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentMapsViewBinding.inflate(inflater, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return binding.getRoot();

    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMyLocationEnabled(true);

    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    private void updatePlacesOnMap(GoogleMap googleMap, ArrayList<GooglePlaces.Results> googlePlaces) {
        LatLng point;
        if (googleMap != null) {
            googleMap.clear();

        for (GooglePlaces.Results r : googlePlaces) {
            BitmapDescriptor bitmapDescriptor = getMarkerIconFromDrawable(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.maps_poi_red, null)));
            for (User ds : selectedRestaurantList) {
                if (ds.getSelectedRestaurantPlaceId() != null && ds.getSelectedRestaurantPlaceId().equals(r.getPlace_id())) {
                    bitmapDescriptor = getMarkerIconFromDrawable(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.maps_poi_green, null)));
                }
            }

            point = new LatLng(r.getGeometry().getLocation().getLat(), r.getGeometry().getLocation().getLng());
            Marker marker;
            marker = googleMap.addMarker(new MarkerOptions()
                    .position(point)
                    .title(r.getName())
                    .icon(bitmapDescriptor));

            assert marker != null;
            marker.setTag(r.getPlace_id());

        }

        googleMap.setOnMarkerClickListener(marker -> {

            Intent intent = new Intent(getContext(), DetailsActivity.class);
            String placeId = Objects.requireNonNull(marker.getTag()).toString();
            intent.putExtra("placeId", placeId);
            requireContext().startActivity(intent);
            return false;
        });
        }
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.nav_menu, menu);
        SearchManager searchManager = (SearchManager) requireActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.nav_search).getActionView();
        searchView.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    updatePlacesOnMap(googleMap, googlePlaces);
                } else if (newText.length() < 3) {
                    googleMap.clear();
                } else {
                    initAutocomplete(newText);
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initAutocomplete(String query) {

        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(myCurrentLocation.getLatitude() - 0.02, myCurrentLocation.getLongitude() - 0.02),
                new LatLng(myCurrentLocation.getLatitude() + 0.02, myCurrentLocation.getLongitude() + 0.02));
        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setLocationBias(bounds)
                .setOrigin(new LatLng(myCurrentLocation.getLatitude(), myCurrentLocation.getLongitude()))
                .setCountries("RE", "FR")
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setQuery(query)
                .build();


        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            googleMap.clear();
            ArrayList<GooglePlaces.Results> autoCompleteResultsList = new ArrayList<>();
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                for (GooglePlaces.Results r : googlePlaces) {
                    if (r.getPlace_id().equals(prediction.getPlaceId())) {

                        autoCompleteResultsList.add(r);
                    }
                }

            }

            updatePlacesOnMap(googleMap, autoCompleteResultsList);
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                if (query.length() < 3) {
                    updatePlacesOnMap(googleMap, googlePlaces);
                }
            }
        });

    }
}