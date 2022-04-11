package com.example.go4lunch.repositories;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.MainApplication;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationRepository {

    public static final int DEFAULT_UPDATE_INTERVAL = 50000;
    public static final int FASTEST_UPDATE_INTERVAL = 20000;
    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    private LocationCallback callback;

    @SuppressLint("MissingPermission")
    public void startLocationRequest() {
        if (callback == null) {
            callback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    Log.d(TAG, "onLocationResult: " + location.getLatitude());
                    locationMutableLiveData.setValue(location);

                }
            };
            LocationServices.getFusedLocationProviderClient(MainApplication.getApplication()).requestLocationUpdates(
                    LocationRequest.create()
                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                            .setInterval(DEFAULT_UPDATE_INTERVAL)
                            .setFastestInterval(FASTEST_UPDATE_INTERVAL)
                            .setSmallestDisplacement(50),
                    callback,
                    Looper.getMainLooper()

            );
        }
    }

    public void stopLocationRequest(){
        if(callback != null) {
            LocationServices.getFusedLocationProviderClient(MainApplication.getApplication()).removeLocationUpdates(callback);
            callback = null;

        }
    }
    public LiveData<Location> getLocationLiveData() {
        return locationMutableLiveData;
    }


}
