package com.example.go4lunch.details;

import static com.example.go4lunch.BuildConfig.MAPS_API_KEY;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.Adapter.DetailsAdapter;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityDetailsBinding;
import com.example.go4lunch.factory.ViewModelFactory;
import com.example.go4lunch.receivers.AlarmReceiver;
import com.example.go4lunch.user.User;
import com.example.go4lunch.user.UserManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class DetailsActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static final int CALL_PERMISSION_REQUEST_CODE = 100;
    private static final String TAG = "200";
    private String placeId;
    private ActivityDetailsBinding binding;
    private TextView textViewCall;
    private String currentPlaceName;
    private String currentPlaceVicinity;
    private String phoneNumber;
    private final UserManager userManager = UserManager.getInstance();
    private static final String SELECTED_RESTAURANT_ID = "selectedRestaurantPlaceId";
    private static final String FAVORITES_RESTAURANTS_FIELD = "favoriteRestaurantsList";
    private String currentSelectedPlace;
    private static final String CHANNEL_ID = "10";
    private static final int NOTIFICATION_ID = 0;
    private SharedPreferences sharedPreferences;
    private static final String sharedPrefFile = "com.example.Go4lunch";
    private static final String UID_KEY = "UID";
    private static final String NOTIFICATION_STATUS_KEY = "notification_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        if (getIntent() != null) {
            placeId = getIntent().getStringExtra("placeId");
        }
        //get logged user selected place
        getUserSelectedRestaurant();
        //get logged user favorites list
        getUserFavoriteList();

        DetailsViewModel detailsViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(DetailsViewModel.class);
        detailsViewModel.getDetailsViewLiveData(placeId).observe(this, detailsViewState -> {

            if (detailsViewState.getPlace().getResult() != null && detailsViewState.getUserList() != null) {

                String url;
                if (detailsViewState.getPlace().getResult().getPhotos() != null) {
                    url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=" + detailsViewState.getPlace().getResult().getPhotos().get(0).getPhoto_reference() + "&key=" + MAPS_API_KEY;
                    Glide.with(view)
                            .load(url)
                            .apply(RequestOptions.centerInsideTransform())
                            .into((binding.detailsActivityImageViewRestaurantPhoto));
                } else {
                    //On affiche une image par défaut
                    Glide.with(view)
                            .load(R.drawable.restaurant)
                            .apply(RequestOptions.centerInsideTransform())
                            .into((binding.detailsActivityImageViewRestaurantPhoto));

                }

                binding.detailsActivityTextviewName.setText(detailsViewState.getPlace().getResult().getName());
                binding.detailsActivityTexviewAddress.setText(detailsViewState.getPlace().getResult().getVicinity());
                binding.textViewCall.setTag(detailsViewState.getPlace().getResult().getFormatted_phone_number());
                binding.textViewWebsite.setTag(detailsViewState.getPlace().getResult().getWebsite());
                binding.textViewLike.setTag(placeId);
                binding.detailsActivityFabSelectRestaurant.setTag(placeId);

                currentPlaceName = detailsViewState.getPlace().getResult().getName();
                currentPlaceVicinity = detailsViewState.getPlace().getResult().getVicinity();

                initRecyclerView(detailsViewState.getUserList());
            }
        });

        initListeners();

    }

    private void getUserFavoriteList() {
        List<String> favoritesList = new ArrayList<>();
     userManager.getUserData().addOnSuccessListener(documentSnapshot -> {
    User user = documentSnapshot.toObject(User.class);
         assert user != null;
         if (user.getFavoriteRestaurantsList() != null && user.getFavoriteRestaurantsList().size() > 0 ) {
        for (String s : user.getFavoriteRestaurantsList()) {
            favoritesList.add(s);
        }
    }

    if (favoritesList.contains(placeId)) {
        binding.imageviewLikedStar.setVisibility(View.VISIBLE);
        binding.textViewLike.setText(R.string.dislike);
    } else {
        binding.imageviewLikedStar.setVisibility(View.GONE);
        binding.textViewLike.setText(R.string.like);
    }
     });
    }

    private void initRecyclerView(List<User> userList) {
        RecyclerView recyclerView = binding.recyclerViewDetails;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        DetailsAdapter adapter = new DetailsAdapter(getApplicationContext(), userList);
        recyclerView.setAdapter(adapter);


    }

    private void addOrRemoveFavoriteRestaurant(String placeId,boolean likedStatus) {
        userManager.updateFavoritesRestaurantList(placeId,likedStatus).addOnSuccessListener(unused -> {
            if (!likedStatus) {
                Toast.makeText(getApplicationContext(),getString(R.string.added_to_favorites),Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),getString(R.string.removed_from_favorites),Toast.LENGTH_SHORT).show();
            }
            getUserFavoriteList();
        });

    }

    private void initListeners() {
        binding.textViewWebsite.setOnClickListener(v -> {
            if (v.getTag() != null && !v.getTag().toString().isEmpty()) {
                launchWebsite(v);
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.website_not_specified), Toast.LENGTH_SHORT).show();
            }

        });
        binding.textViewCall.setOnClickListener(v -> {
            phoneNumber = v.getTag().toString();
            String[] perms = {Manifest.permission.CALL_PHONE};
            if (EasyPermissions.hasPermissions(DetailsActivity.this, perms)) {
                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    callNow();
                }
            } else {
                EasyPermissions.requestPermissions(DetailsActivity.this, getString(R.string.permission_rationale_call), CALL_PERMISSION_REQUEST_CODE, perms);
            }
        });
        binding.textViewLike.setOnClickListener(v -> {
            boolean likedStatus = binding.imageviewLikedStar.getVisibility() != View.GONE;
            addOrRemoveFavoriteRestaurant(v.getTag().toString(),likedStatus);
        });
        binding.detailsActivityFabSelectRestaurant.setOnClickListener(v -> {

            if (currentSelectedPlace == null || !currentSelectedPlace.equals(placeId)) {
                userManager.updateSelectedRestaurant(v.getTag().toString(), currentPlaceName, currentPlaceVicinity)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(v.getContext(), getString(R.string.choice_recorded), Toast.LENGTH_SHORT).show();
                            getUserSelectedRestaurant();
                            boolean status = getNotificationActivationStatus();
                            if (status) {
                                registerAlarm();
                                Toast.makeText(v.getContext(), getString(R.string.alarm_activated), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(v.getContext(), getString(R.string.unable_to_record_choice), Toast.LENGTH_SHORT).show());
            } else {
                userManager.updateSelectedRestaurant(null, null, null)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(v.getContext(),getString(R.string.choice_canceled), Toast.LENGTH_SHORT).show();
                            currentSelectedPlace = null;
                            getUserSelectedRestaurant();
                            cancelAlarm();
                        })
                        .addOnFailureListener(e -> Toast.makeText(v.getContext(), getString(R.string.unable_to_cancel_choice), Toast.LENGTH_SHORT).show());
            }


        });
        binding.detailsActivityFabBack.setOnClickListener(v -> finish());
    }

    private boolean getNotificationActivationStatus() {
        sharedPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        return sharedPreferences.getBoolean(NOTIFICATION_STATUS_KEY, true);
    }

    private void getUserSelectedRestaurant() {
        userManager.getUserData().addOnCompleteListener(task -> {
            if (Objects.requireNonNull(task.getResult().getData()).get(SELECTED_RESTAURANT_ID) != null) {

                currentSelectedPlace = Objects.requireNonNull(task.getResult().getData().get(SELECTED_RESTAURANT_ID)).toString();

                if (currentSelectedPlace.equals(placeId)) {
                    binding.detailsActivityFabSelectRestaurant.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_check_circle_24, null));
               binding.detailsActivityFabSelectRestaurant.setColorFilter(R.color.green);
                }
            } else {
                binding.detailsActivityFabSelectRestaurant.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_arrow_circle_up_24, null));
            }

        });
    }

    private void launchWebsite(View v) {
        String url = v.getTag().toString();
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, DetailsActivity.this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        callNow();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            callNow();
        }
    }

    @SuppressLint("MissingPermission")
    private void callNow() {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(getString(R.string.tel) + phoneNumber));
        try {
            this.startActivity(callIntent);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.call_error),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);

    }

    private void registerAlarm() {

        Intent notifyIntent = new Intent(this, AlarmReceiver.class);

        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
       final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            Calendar calendar = Calendar.getInstance();
            long currentTime = calendar.getTimeInMillis();

            calendar.set(Calendar.HOUR_OF_DAY, 12);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            // if the restaurant is chosen after 12:00 then the alarm is recorded for the next day
            if (currentTime > calendar.getTimeInMillis()) {
                calendar.add(Calendar.HOUR, 24);
            }

            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), notifyPendingIntent);
            updateSharedPreferences();

        }
    }

    private void cancelAlarm() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        Intent notifyIntent = new Intent(this, AlarmReceiver.class);
        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(notifyPendingIntent);

        clearSharedPreferences();
    }

    private void updateSharedPreferences() {
        if (UserManager.getInstance().getCurrentUser() != null) {
            sharedPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
            SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
            String Uid = UserManager.getInstance().getCurrentUser().getUid();
            preferencesEditor.putString(UID_KEY, Uid);
            preferencesEditor.apply();
        }
    }

    private void clearSharedPreferences() {
        sharedPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        preferencesEditor.putString(UID_KEY, "NOVALUE");
        preferencesEditor.apply();
    }

}