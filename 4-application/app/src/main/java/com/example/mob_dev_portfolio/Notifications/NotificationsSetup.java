package com.example.mob_dev_portfolio.Notifications;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

/**
 * A class for creating and setting up the notification channels to be used in the app.
 */
public class NotificationsSetup extends Application {
    public static final String CHANNEL_DATABASE_ID = "channel1";
    public static final String CHANNEL_GAME_ID = "channel2";

    @Override
    public void onCreate(){
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        //Check if build version is greater than oreo
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            //Create quiz channel for notifications relating to actions within the database
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_DATABASE_ID,
                    "Database Notifications Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            channel1.setDescription("This channel is for notifications relating to actions within the database");

            //Create quiz channel for notifications relating to actions within the quiz game
            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_GAME_ID,
                    "Quiz Game Notifications Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            channel2.setDescription("This channel is for notifications relating to the quiz game");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(channel1);
            notificationManager.createNotificationChannel(channel2);
        }
    }
}
