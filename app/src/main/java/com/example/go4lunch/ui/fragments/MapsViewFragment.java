package com.example.go4lunch.ui.fragments;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.Models.GooglePlaces;
import com.example.go4lunch.factory.ViewModelFactory;
import com.example.go4lunch.R;
import com.example.go4lunch.viewmodels.MapsViewViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class MapsViewFragment extends Fragment implements OnMapReadyCallback {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location myLocation;
    private MapsViewViewModel mapsViewViewModel;
    private ViewModelFactory viewModelFactory;
    private ArrayList<GooglePlaces.Results> googlePlaces = new ArrayList<>();
    private GoogleMap googleMap;

    public MapsViewFragment() {
        // Required empty public constructor
    }

    public static MapsViewFragment newInstance() {

        return new MapsViewFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapsViewViewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance(getContext())).get(MapsViewViewModel.class);


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


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        googleMap.clear();
        setMyLocation(googleMap);
        this.googleMap = googleMap;

    }

    private void getPoi() {

        mapsViewViewModel.getNearBySearchLiveData().observe(getViewLifecycleOwner(), new Observer<List<GooglePlaces.Results>>() {
            @Override
            public void onChanged(List<GooglePlaces.Results> results) {
                googlePlaces.addAll(results);
                updatePlacesOnMap(googleMap);
            }
        });


    }

    private void updatePlacesOnMap(GoogleMap googleMap) {
        LatLng point;
        for (GooglePlaces.Results r : googlePlaces) {
            Log.d(TAG, "updatePlacesOnMap: " + r.getGeometry().getLocation().getLat() + " - " + r.getName());
            point = new LatLng(r.getGeometry().getLocation().getLat(), r.getGeometry().getLocation().getLng());
            googleMap.addMarker(new MarkerOptions()
                    .position(point)
                    .title(r.getName()));

        }
    }


    @SuppressLint("MissingPermission")
    private void setMyLocation(GoogleMap googleMap) {
        if ((ContextCompat.checkSelfPermission(this.getContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this.getContext(), ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    myLocation = task.getResult();

                    if (myLocation != null) {
                        LatLng home = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(home, 10));
                    }
                    googleMap.setMyLocationEnabled(true);
                    //Log.d(TAG, "onComplete: " + myLocation.getLatitude() + " " + myLocation.getLongitude());
                    getPoi();

                }
            });
            googleMap.setMyLocationEnabled(true);
        }



    }

}