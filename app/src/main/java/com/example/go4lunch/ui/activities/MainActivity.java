package com.example.go4lunch.ui.activities;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityMainBinding;
import com.example.go4lunch.details.DetailsActivity;
import com.example.go4lunch.listview.ListViewFragment;
import com.example.go4lunch.mapsView.MapsViewFragment;
import com.example.go4lunch.user.UserManager;
import com.example.go4lunch.workmatesview.WorkmatesFragment;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static final int RC_SIGN_IN = 123;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String FRAGMENT_MAPS_TAG = "FRAGMENT_MAP_TAG";
    private static final String FRAGMENT_LIST_TAG = "FRAGMENT_LIST_TAG";
    private static final String FRAGMENT_WORKMATES_TAG = "FRAGMENT_WORKMATES_TAG";
    private ActivityMainBinding binding;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActivityResultLauncher<Intent> signInActivityResultLauncher;
    private final UserManager userManager = UserManager.getInstance();
    final int yourLunch = R.id.activity_main_drawer_your_lunch;
    final int settings = R.id.activity_main_drawer_settings;
    final int logOut = R.id.activity_main_drawer_logout;
    final int pageMapView = R.id.page_mapview;
    final int pageListView = R.id.page_listview;
    final int pageWorkmates = R.id.page_workmates;
    private static final String SELECTED_RESTAURANT_ID = "selectedRestaurantPlaceId";
    private static final String CHANNEL_ID = "10";


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            outState.putString("CURRENT_FRAGMENT", fragment.getTag());
        }

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        switch (savedInstanceState.getString("CURRENT_FRAGMENT")) {
            case "FRAGMENT_MAP_TAG" :
                try {
                    configureMapViewFragment();
                    break;
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            case "FRAGMENT_LIST_TAG" :
                try {
                    configureListViewFragment();
                    showBottomNavigationBarAndHideLoginButton();
                    break;
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            case "FRAGMENT_WORKMATES_TAG" :
                try {
                    configureWorkmatesFragment();
                    showBottomNavigationBarAndHideLoginButton();
                    break;
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
        }


    }
    private void showBottomNavigationBarAndHideLoginButton() {
        if (UserManager.getInstance().isCurrentUserLogged()) {
            binding.bottomNavigation.setVisibility(View.VISIBLE);
            binding.logInButton.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onPause() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {

            Log.d(TAG, "onPause: " + fragment.getTag());
        }

        super.onPause();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.logInButton.setVisibility(View.GONE);


        signInActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    IdpResponse response = IdpResponse.fromResultIntent(result.getData());
                    if (result.getResultCode() == Activity.RESULT_OK) {


                        showSnackBar(getString(R.string.connection_succeed));
                        userManager.createUser();

                        updateUiWithUserData();
                        try {
                            if (userManager.isCurrentUserLogged()) {
                                binding.logInButton.setVisibility(View.GONE);
                                binding.bottomNavigation.setVisibility(View.VISIBLE);
                                binding.activityMainDrawerLayout.setVisibility(View.VISIBLE);
                                configureMapViewFragment();
                            }
                        } catch (IllegalAccessException | InstantiationException e) {
                            e.printStackTrace();
                        }


                    } else {
                        // ERRORS
                        if (response == null) {
                            showSnackBar(getString(R.string.error_authentication_canceled));
                            List<Fragment> fragments = getSupportFragmentManager().getFragments();
                            for (Fragment fragment : fragments) {
                                if (fragment != null) {
                                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                                }
                            }
                            binding.bottomNavigation.setVisibility(View.GONE);
                            binding.logInButton.setVisibility(View.VISIBLE);

                            binding.activityMainDrawerLayout.setVisibility(View.GONE);

                        } else if (response.getError() != null) {
                            if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                                showSnackBar(getString(R.string.error_no_internet));
                            } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                                showSnackBar(getString(R.string.error_unknown_error));
                            }

                        }
                        if (!userManager.isCurrentUserLogged()) {

                            showSnackBar(getString(R.string.login_obligation));
                            binding.bottomNavigation.setVisibility(View.GONE);
                            binding.logInButton.setVisibility(View.VISIBLE);

                            binding.activityMainDrawerLayout.setVisibility(View.GONE);
                        }
                    }
                });

        configureActivity();
        initListeners();
        if (!UserManager.getInstance().isCurrentUserLogged()) {
            binding.bottomNavigation.setVisibility(View.GONE);
            binding.logInButton.setVisibility(View.VISIBLE);
            startSignInActivity();
        } else {
            updateUiWithUserData();
            binding.bottomNavigation.setVisibility(View.VISIBLE);
            binding.logInButton.setVisibility(View.GONE);
            try {
                configureMapViewFragment();
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }


    }



    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private void initListeners() {
        binding.logInButton.setOnClickListener(v -> startSignInActivity());
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void configureActivity() {
        configureBottomNavigationView();
        configureToolBar();
        configureDrawerLayout();
        configureNavigationView();
    }

    private void startSignInActivity() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build(),
        new AuthUI.IdpConfig.EmailBuilder().build());

        // Launch the activity
        signInActivityResultLauncher.launch(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.Theme_Go4LunchBottomNavigation)
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.bowl_icon)
                        .build());


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            try {
                configureMapViewFragment();
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }

    }


    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(binding.mainLayout, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }


    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {

        return super.onCreateView(parent, name, context, attrs);

    }

    private void configureToolBar() {
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.hungry);
        }
    }

    private void configureDrawerLayout() {
        this.drawerLayout = binding.activityMainDrawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureNavigationView() {
        this.navigationView = binding.activityMainNavView;

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case yourLunch:

                    userManager.getUserData().addOnCompleteListener(task -> {
                        if (task.getResult().get(SELECTED_RESTAURANT_ID) != null && !Objects.requireNonNull(task.getResult().get(SELECTED_RESTAURANT_ID)).toString().isEmpty()) {
                            Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                            intent.putExtra("placeId", Objects.requireNonNull(task.getResult().get(SELECTED_RESTAURANT_ID)).toString());
                            startActivity(intent);
                        } else {
                            showSnackBar(getString(R.string.not_chosen_yet));
                        }

                    });

                    break;
                case settings:
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                    break;
                case logOut:
                    userManager.signOut(this);
                    startSignInActivity();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

    }

    private void updateUiWithUserData() {

        if (userManager.isCurrentUserLogged()) {
            FirebaseUser user = userManager.getCurrentUser();
            View header = navigationView.getHeaderView(0);

            if (user.getPhotoUrl() != null) {
                setProfilePicture(user.getPhotoUrl(), header);
            }
            setTextUserData(user, header);
        }
    }

    private void setProfilePicture(Uri profilePictureUrl, View view) {
        Glide.with(this)
                .load(profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into((ImageView) view.findViewById(R.id.avatar));
    }

    private void setTextUserData(FirebaseUser user, View view) {
        String email = TextUtils.isEmpty(user.getEmail()) ?
                getString(R.string.info_no_email_found) : user.getEmail();
        String username = TextUtils.isEmpty(user.getDisplayName()) ?
                getString(R.string.info_no_username_found) : user.getDisplayName();
        TextView username_Textview = view.findViewById(R.id.textview_username);
        username_Textview.setText(username);
        TextView usermail_Textview = view.findViewById(R.id.textview_user_mail);
        usermail_Textview.setText(email);
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
            Log.d(TAG, "onBackPressed:opened");
        } else {
            super.onBackPressed();
        }
    }

    private void configureBottomNavigationView() {
        BottomNavigationView bottomNavigationView = binding.bottomNavigation;
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case pageMapView:
                    try {
                        configureMapViewFragment();
                        toolbar.setTitle(R.string.hungry);
                    } catch (IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }
                    return true;
                case pageListView:

                    try {
                        configureListViewFragment();
                        toolbar.setTitle(R.string.hungry);
                    } catch (IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }
                    return true;
                case pageWorkmates:
                    try {
                        configureWorkmatesFragment();
                        toolbar.setTitle(R.string.available_workmates);
                    } catch (IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }
                    return true;
                default:
                    return true;
            }

        });
    }

    private void configureMapViewFragment() throws IllegalAccessException, InstantiationException {
        //Check for permissions before going further
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, MapsViewFragment.class.newInstance(), FRAGMENT_MAPS_TAG)
                    .setReorderingAllowed(true)
                    .commit();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_rationale_message), LOCATION_PERMISSION_REQUEST_CODE, perms);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        try {

            configureMapViewFragment();

        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    private void configureListViewFragment() throws IllegalAccessException, InstantiationException {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ListViewFragment.class.newInstance(), FRAGMENT_LIST_TAG)
                .setReorderingAllowed(true)
                .commit();
    }

    private void configureWorkmatesFragment() throws IllegalAccessException, InstantiationException {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, WorkmatesFragment.class.newInstance(), FRAGMENT_WORKMATES_TAG)
                .setReorderingAllowed(true)
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


}