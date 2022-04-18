package com.example.go4lunch.mapsView;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentListViewBinding;
import com.example.go4lunch.databinding.FragmentMapsViewBinding;
import com.example.go4lunch.details.DetailsActivity;
import com.example.go4lunch.factory.ViewModelFactory;
import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.user.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MapsViewFragment extends Fragment implements OnMapReadyCallback {

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


    public MapsViewFragment() {
        // Required empty public constructor
    }

    public static MapsViewFragment newInstance() {

        return new MapsViewFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                        updatePlacesOnMap(googleMap);
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


    private void updatePlacesOnMap(GoogleMap googleMap) {
        LatLng point;
        for (GooglePlaces.Results r : googlePlaces) {
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
            for (User ds : selectedRestaurantList) {
                if (ds.getSelectedRestaurantPlaceId().equals(r.getPlace_id())) {
                    bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                }
            }
            // bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);

            point = new LatLng(r.getGeometry().getLocation().getLat(), r.getGeometry().getLocation().getLng());
            Marker marker;
            marker = googleMap.addMarker(new MarkerOptions()
                    .position(point)
                    .title(r.getName())
                    .icon(bitmapDescriptor));
            marker.setTag(r.getPlace_id());

        }

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                Intent intent = new Intent(getContext(), DetailsActivity.class);
                String placeId = marker.getTag().toString();
                intent.putExtra("placeId", placeId);
                Log.d(TAG, "onClick: " + placeId);
                requireContext().startActivity(intent);
                return false;
            }
        });
    }


}