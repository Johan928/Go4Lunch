package com.example.go4lunch.receivers;

import static android.content.ContentValues.TAG;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.go4lunch.R;
import com.example.go4lunch.repositories.UserRepository;
import com.example.go4lunch.ui.activities.MainActivity;
import com.example.go4lunch.user.User;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AlarmReceiver extends BroadcastReceiver {


    private static final String CHANNEL_ID = "10";
    private static final int NOTIFICATION_ID = 0;

    private String userId;
    private final UserRepository userRepository = UserRepository.getInstance();
    private String restaurantName;
    private String restaurantAddress;
    private String restaurantId;
    private final ArrayList<User> joiningWorkmatesList = new ArrayList<>();
    private List<User> userList;
    private static final String sharedPrefFile = "com.example.Go4lunch";
    private static final String UID_KEY = "UID";

    @Override
    public void onReceive(Context context, Intent intent) {

        userId = getUidFromSharedPreferences(context);
        if (!userId.equals("NOVALUE")) {
            userRepository.getUserDataFromUid(userId).addOnCompleteListener(task -> {
                User user = task.getResult().toObject(User.class);
                assert user != null;
                restaurantName = user.getSelectedRestaurantName();
                Log.d(TAG, "updateSh: " + userId);
                Log.d(TAG, "updateSh: " + restaurantName);
                restaurantAddress = user.getSelectedRestaurantAddress();
                restaurantId = user.getSelectedRestaurantPlaceId();
                Log.d(TAG, "updateSh: " + restaurantId);

                userRepository.getUserListInATask().addOnCompleteListener(task1 -> {
                    for (DocumentSnapshot ds : task1.getResult().getDocuments()) {
                        if (restaurantId.equals(Objects.requireNonNull(ds.toObject(User.class)).getSelectedRestaurantPlaceId()) && !userId.equals(Objects.requireNonNull(ds.toObject(User.class)).getUid())) {
                            Log.d(TAG, "onComplete: " + Objects.requireNonNull(ds.toObject(User.class)).getUsername());
                            User currentUser = ds.toObject(User.class);
                            joiningWorkmatesList.add(currentUser);
                        }
                    }
                    setUpNotification(context);
                });


            });
        }


    }

    private void setUpNotification(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        if (joiningWorkmatesList.size() > 0) {
            for (User user : joiningWorkmatesList) {
                stringBuilder.append(user.getUsername()).append("-");
            }
        } else {
            stringBuilder.append(context.getString(R.string.no_one_else_is_coming));
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent contentIntent = new Intent(context, MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.bowl_icon)
                .setContentTitle(context.getString(R.string.time_for_lunch) + restaurantName)
                .setContentText(context.getString(R.string.address) + " : " +  restaurantAddress)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("\n" + context.getString(R.string.icon_workmates_text) + " : " + stringBuilder))
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private String getUidFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
        return sharedPreferences.getString(UID_KEY, "NOVALUE");
    }


}