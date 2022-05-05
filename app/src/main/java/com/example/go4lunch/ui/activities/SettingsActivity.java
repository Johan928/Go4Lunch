package com.example.go4lunch.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.databinding.ActivitySettingsBinding;
import com.example.go4lunch.user.UserManager;

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

        binding.notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSharedPreferences(isChecked);
            }
        });
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