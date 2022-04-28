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
import android.util.Log;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
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

import java.util.ArrayList;
import java.util.List;

public class MapsViewFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "123";
    private MapsViewViewModel mapsViewViewModel;
    private ArrayList<GooglePlaces.Results> googlePlaces = new ArrayList<>();
    private GoogleMap googleMap;
    private LiveData<MapsViewViewState> liveData;
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
        Places.initialize(getActivity().getApplicationContext(), MAPS_API_KEY);
        placesClient = Places.createClient(getActivity().getApplicationContext());

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progress_bar_maps);

        ViewModelFactory vmf = ViewModelFactory.getInstance();
        mapsViewViewModel = new ViewModelProvider(this, vmf).get(MapsViewViewModel.class);
        liveData = mapsViewViewModel.getMediatorLiveData();
        liveData.observe(getViewLifecycleOwner(), new Observer<MapsViewViewState>() {
            @Override
            public void onChanged(MapsViewViewState mapsViewViewState) {
                if (mapsViewViewState.getLocation() != null && mapsViewViewState.getPlaces() != null && mapsViewViewState.getSelectedRestaurantsList() != null) {
                    progressBar.setVisibility(View.GONE);

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

            }
        });

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps_view, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
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
        googleMap.clear();
        for (GooglePlaces.Results r : googlePlaces) {
            BitmapDescriptor bitmapDescriptor = getMarkerIconFromDrawable(getResources().getDrawable(R.drawable.maps_poi_red, null));
            for (User ds : selectedRestaurantList) {
                if (ds.getSelectedRestaurantPlaceId() != null && ds.getSelectedRestaurantPlaceId().equals(r.getPlace_id())) {
                    bitmapDescriptor = getMarkerIconFromDrawable(getResources().getDrawable(R.drawable.maps_poi_green, null));
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
            String placeId = marker.getTag().toString();
            intent.putExtra("placeId", placeId);
            requireContext().startActivity(intent);
            return false;
        });
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
                new LatLng(-myCurrentLocation.getLatitude() + 0.02, myCurrentLocation.getLongitude() + 0.02));
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