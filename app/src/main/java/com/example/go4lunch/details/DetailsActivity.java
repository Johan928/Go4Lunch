package com.example.go4lunch.details;

import static com.example.go4lunch.BuildConfig.MAPS_API_KEY;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
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
import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.model.Place;
import com.example.go4lunch.user.User;
import com.example.go4lunch.user.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class DetailsActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static final int CALL_PERMISSION_REQUEST_CODE = 100;
    private static final String TAG = "200";
    private String placeId;
    private ActivityDetailsBinding binding;
    private DetailsViewModel detailsViewModel;
    private TextView textViewCall;
    private Place place;
    private String phoneNumber;
    private UserManager userManager = UserManager.getInstance();
    private static final String SELECTED_RESTAURANT_FIELD = "selectedRestaurantPlaceId";
    private String currentSelectedPlace;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private DetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        if (getIntent() != null) {
            placeId = getIntent().getStringExtra("placeId");
        }
        //get recorded user selected place
        getUserSelectedRestaurant();

        detailsViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(DetailsViewModel.class);
        detailsViewModel.getDetailsViewLiveData(placeId).observe(this, detailsViewState -> {

            if (detailsViewState.getPlace().getResult() != null && detailsViewState.getPlaces() != null && detailsViewState.getUserList() != null) {
                for (GooglePlaces.Results gp : detailsViewState.getPlaces()) { //Traitement des données places
                    if (gp.getPlace_id().equals(placeId)) {
                        binding.detailsActivityTextviewName.setText(gp.getName());
                        binding.detailsActivityTexviewAddress.setText(gp.getVicinity());
                        String url;
                        if (gp.getPhotos() != null) {
                            url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=" + gp.getPhotos().get(0).getPhoto_reference() + "&key=" + MAPS_API_KEY;
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
                            //binding.detailsActivityImageViewRestaurantPhoto.setImageDrawable(getDrawable(R.drawable.restaurant));

                        }

                    }
                }
                binding.textViewCall.setTag(detailsViewState.getPlace().getResult().getFormatted_phone_number());
                binding.textViewWebsite.setTag(detailsViewState.getPlace().getResult().getWebsite());
                binding.detailsActivityFabSelectRestaurant.setTag(placeId);


                initRecyclerView(detailsViewState.getUserList());
            }
        });

        initListeners();

    }

    private void initRecyclerView(List<User> userList) {
        recyclerView = binding.recyclerViewDetails;
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
      /*  if (adapter != null) {
            adapter.submitList(userList);
        } else {*/
            adapter = new DetailsAdapter(getApplicationContext(),userList);
            recyclerView.setAdapter(adapter);
        /*}*/

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
        binding.detailsActivityFabSelectRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentSelectedPlace == null || !currentSelectedPlace.equals(placeId)) {
                    userManager.updateSelectedRestaurant(v.getTag().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(v.getContext(), "Choice Recorded !", Toast.LENGTH_SHORT).show();
                                    getUserSelectedRestaurant();

                                }
                            })

                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(v.getContext(), "Unable to record choice", Toast.LENGTH_SHORT).show();

                                }
                            });
                } else {
                    Toast.makeText(v.getContext(), "You have already chosen this restaurant !", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void getUserSelectedRestaurant() {
        Log.d(TAG, "getSelectedRestaurant: ");
        userManager.getUserData().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().getData().get(SELECTED_RESTAURANT_FIELD) != null) {
                    Log.d(TAG, "onComplete: " + task.getResult().getData().get(SELECTED_RESTAURANT_FIELD));
                    currentSelectedPlace = task.getResult().getData().get(SELECTED_RESTAURANT_FIELD).toString();
                    Log.d(TAG, "onComplete: " + currentSelectedPlace + "-" + placeId);
                    if (currentSelectedPlace.equals(placeId)) {
                        binding.detailsActivityFabSelectRestaurant.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_check_circle_24, null));
                        binding.detailsActivityFabSelectRestaurant.getDrawable().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP); //deprecated
                    }
                }

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
        // String phoneNumber = v.getTag().toString();

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
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
}