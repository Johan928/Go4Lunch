package com.example.go4lunch.mapsView;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.factory.ViewModelFactory;
import com.example.go4lunch.model.GooglePlaces;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsViewFragment extends Fragment implements OnMapReadyCallback {

    private MapsViewViewModel mapsViewViewModel;
    private ArrayList<GooglePlaces.Results> googlePlaces = new ArrayList<>();
    private GoogleMap googleMap;
    private LiveData<MapsViewViewState> liveData;
    private Location myCurrentLocation;
    private boolean firstStart = true;


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

        ViewModelFactory vmf = ViewModelFactory.getInstance();
        mapsViewViewModel = new ViewModelProvider(this, vmf).get(MapsViewViewModel.class);
        liveData = mapsViewViewModel.getMediatorLiveData();
        liveData.observe(getViewLifecycleOwner(), new Observer<MapsViewViewState>() {
            @Override
            public void onChanged(MapsViewViewState mapsViewViewState) {
                if (mapsViewViewState != null) {
                    if (mapsViewViewState.getPlaces() != null) {
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
            Log.d(TAG, "updatePlacesOnMap: " + r.getGeometry().getLocation().getLat() + " - " + r.getName());
            point = new LatLng(r.getGeometry().getLocation().getLat(), r.getGeometry().getLocation().getLng());
            Marker marker;
            marker = googleMap.addMarker(new MarkerOptions()
                    .position(point)
                    .title(r.getName())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        }
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Log.d(TAG, "onMarkerClick: " + marker.getTitle().toString());
                return false;
            }
        });
    }


}