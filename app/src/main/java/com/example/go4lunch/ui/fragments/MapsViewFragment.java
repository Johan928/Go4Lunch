package com.example.go4lunch.ui.fragments;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.go4lunch.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapsViewFragment extends Fragment implements OnMapReadyCallback {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location myLocation;




    public MapsViewFragment() {
        // Required empty public constructor
    }

    public static MapsViewFragment newInstance() {

        return new MapsViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps_view, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null){
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        setMyLocation(googleMap);
    }


    @SuppressLint("MissingPermission")
    private void setMyLocation(GoogleMap googleMap) {
        if ((ContextCompat.checkSelfPermission(this.getContext(),ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)  &&
                (ContextCompat.checkSelfPermission(this.getContext(),ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
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
                }
            });
            googleMap.setMyLocationEnabled(true);
        }



    }

}