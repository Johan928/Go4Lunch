package com.example.go4lunch.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private SharedPreferences sharedPreferences;
    private static final String sharedPrefFile = "com.example.Go4lunch";
    private static final String NOTIFICATION_STATUS_KEY = "notification_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSharedPreferences();

        binding.notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> updateSharedPreferences(isChecked));
    }

    private void updateSharedPreferences(Boolean isChecked) {

        sharedPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        preferencesEditor.putBoolean(NOTIFICATION_STATUS_KEY, isChecked);
        preferencesEditor.apply();

    }

    private void getSharedPreferences() {

        sharedPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        boolean notifStatus = sharedPreferences.getBoolean(NOTIFICATION_STATUS_KEY, true);
        binding.notificationSwitch.setChecked(notifStatus);

    }
}