package com.example.go4lunch.receivers;

import static android.content.ContentValues.TAG;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.go4lunch.R;
import com.example.go4lunch.repositories.UserRepository;
import com.example.go4lunch.ui.activities.MainActivity;
import com.example.go4lunch.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {


    private static final String CHANNEL_ID = "10";
    private static final int NOTIFICATION_ID = 0;
    private NotificationManager notificationManager;

    private SharedPreferences sharedPreferences;
    private String sharedPrefFile = "com.example.go4lunch";
    private static final String UID_KEY = "UID";
    private String userId;
    private UserRepository userRepository = UserRepository.getInstance();
    private String restaurantName;
    private String restaurantAddress;
    private String restaurantId;
    private ArrayList<User> joiningWorkmatesList = new ArrayList<>();
    private List<User> userList;


    @Override
    public void onReceive(Context context, Intent intent) {

        userId = getUidFromSharedPreferences(context);

        userRepository.getUserDataFromUid(userId).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User user = task.getResult().toObject(User.class);
                restaurantName = user.getSelectedRestaurantName();
                restaurantAddress = user.getSelectedRestaurantAddress();
                restaurantId = user.getSelectedRestaurantPlaceId();


                userRepository.getUserListInATask().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                            if (restaurantId.equals(ds.toObject(User.class).getSelectedRestaurantPlaceId()) && !userId.equals(ds.toObject(User.class).getUid())) {
                                Log.d(TAG, "onComplete: " + ds.toObject(User.class).getUsername());
                                User currentUser = ds.toObject(User.class);
                                joiningWorkmatesList.add(currentUser);
                            }
                        }
                        setUpNotification(context);
                    }

                });


            }
        });


    }

    private void setUpNotification(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        if (joiningWorkmatesList != null && joiningWorkmatesList.size() > 0) {
            for (User user : joiningWorkmatesList) {
                stringBuilder.append(user.getUsername());
            }
        } else {
            stringBuilder.append("No one else is coming");
        }

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent contentIntent = new Intent(context, MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.bowl_icon)
                .setContentTitle("Time for Lunch at " + restaurantName)
                .setContentText(context.getString(R.string.adress) + " : " +  restaurantAddress)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("\n" + context.getString(R.string.icon_workmates_text) + " : " + stringBuilder))
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private String getUidFromSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
        String Uid = sharedPreferences.getString(UID_KEY, "NOVALUE");
        return Uid;
    }


}