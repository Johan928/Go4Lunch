package com.example.go4lunch;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.go4lunch.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);
        configureBottomNavigationView();
        configureToolBar();
        configureDrawerLayout();
        configureNavigationView();

        try {
            configureMapViewFragment();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
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
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

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
                .replace(R.id.fragment_container, MapViewFragment.class.newInstance(), null)
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