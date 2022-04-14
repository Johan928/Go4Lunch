package com.example.go4lunch.details;

import static com.example.go4lunch.BuildConfig.MAPS_API_KEY;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityDetailsBinding;
import com.example.go4lunch.factory.ViewModelFactory;
import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.model.Place;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class DetailsActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static final int CALL_PERMISSION_REQUEST_CODE = 100;
    private String placeId;
    private ActivityDetailsBinding binding;
    DetailsViewModel detailsViewModel;
    TextView textViewCall;
    Place place;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        if (getIntent() != null) {
            placeId = getIntent().getStringExtra("placeId");
        }
        detailsViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(DetailsViewModel.class);
        detailsViewModel.getDetailsViewLiveData(placeId).observe(this, detailsViewState -> {

            if (detailsViewState.getPlace().getResult() != null && detailsViewState.getPlaces() != null) {
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
                        //    url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=Aap_uEA7vb0DDYVJWEaX3O-AtYp77AaswQKSGtDaimt3gt7QCNpdjp1BkdM6acJ96xTec3tsV_ZJNL_JP-lqsVxydG3nh739RE_hepOOL05tfJh2_ranjMadb3VoBYFvF0ma6S24qZ6QJUuV6sSRrhCskSBP5C1myCzsebztMfGvm7ij3gZT&key=" + MAPS_API_KEY;
                        binding.detailsActivityImageViewRestaurantPhoto.setImageDrawable(getDrawable(R.drawable.restaurant));
                        }

                    }
                }
                binding.textViewCall.setTag(detailsViewState.getPlace().getResult().getFormatted_phone_number());
                binding.textViewWebsite.setTag(detailsViewState.getPlace().getResult().getWebsite());

            }
        });

        initListeners();
    }

    private void initListeners() {
        binding.textViewWebsite.setOnClickListener(v -> {
            if (v.getTag() != null && !v.getTag().toString().isEmpty()) {
                launchWebsite(v);
            } else {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.website_not_specified),Toast.LENGTH_SHORT).show();
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