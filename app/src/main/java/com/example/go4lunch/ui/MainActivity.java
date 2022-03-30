package com.example.go4lunch.ui;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityMainBinding;
import com.example.go4lunch.manager.UserManager;
import com.example.go4lunch.ui.fragments.ListViewFragment;
import com.example.go4lunch.ui.fragments.MapsViewFragment;
import com.example.go4lunch.ui.fragments.WorkmatesFragment;
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

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private ActivityMainBinding activityMainBinding;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private UserManager userManager = UserManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);
        configureActivity();
        startSignInActivity();


        try {
            configureMapViewFragment();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
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
                new AuthUI.IdpConfig.FacebookBuilder().build());

        // Launch the activity
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.Theme_Go4LunchBottomNavigation)
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.bowl_icon)
                        .build(),
                RC_SIGN_IN);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    private void showSnackBar(String message) {
        Snackbar.make(activityMainBinding.mainLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            // SUCCESS
            if (resultCode == RESULT_OK) {
                showSnackBar(getString(R.string.connection_succeed));
                updateUiWithUserData();
            } else {
                // ERRORS
                if (response == null) {
                    showSnackBar(getString(R.string.error_authentication_canceled));
                } else if (response.getError() != null) {
                    if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                        showSnackBar(getString(R.string.error_no_internet));
                    } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                        showSnackBar(getString(R.string.error_unknown_error));
                    }

                }
                if (!userManager.isCurrentUserLogged()) {
                    startSignInActivity();
                }
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    private void configureToolBar() {
        toolbar = activityMainBinding.toolbar;
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.hungry);
        }
    }

    private void configureDrawerLayout() {
        this.drawerLayout = activityMainBinding.activityMainDrawerLayout;
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureNavigationView() {
        this.navigationView = activityMainBinding.activityMainNavView;
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.activity_main_drawer_your_lunch:
                    Toast.makeText(getApplicationContext(), "TEST " + drawerLayout.getDrawingTime(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.activity_main_drawer_settings:
                    Toast.makeText(getApplicationContext(), "SETTINGS", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.activity_main_drawer_logout:
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
                Log.d(TAG, "updateUiWithUserData: " + user.getPhotoUrl().toString());
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
        TextView username_Textview = (TextView) view.findViewById(R.id.textview_username);
        username_Textview.setText(username);
        TextView usermail_Textview = (TextView) view.findViewById(R.id.textview_user_mail);
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
        bottomNavigationView = activityMainBinding.bottomNavigation;
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.page_mapview:
                    try {
                        configureMapViewFragment();
                        toolbar.setTitle(R.string.hungry);
                    } catch (IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }
                    return true;
                case R.id.page_listview:
                    try {
                        configureListViewFragment();
                        toolbar.setTitle(R.string.hungry);
                    } catch (IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }
                    return true;
                case R.id.page_workmates:
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
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,MapsViewFragment.class.newInstance(),null)
                .setReorderingAllowed(true)
                .commit();


    }

    private void configureListViewFragment() throws IllegalAccessException, InstantiationException {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ListViewFragment.class.newInstance(), null)
                .setReorderingAllowed(true)
                .commit();
    }

    private void configureWorkmatesFragment() throws IllegalAccessException, InstantiationException {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, WorkmatesFragment.class.newInstance(), null)
                .setReorderingAllowed(true)
                .commit();
    }

}