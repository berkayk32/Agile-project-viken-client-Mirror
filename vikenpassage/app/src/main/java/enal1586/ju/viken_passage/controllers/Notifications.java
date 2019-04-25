package enal1586.ju.viken_passage.controllers;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Notifications extends Application {

    public final static String CHANNEL_USER_MONEY_WITHDRAW = "withdrawn money notification";

    @Override
    public void onCreate() {
        super.onCreate();

        startNotification();
    }

    private void startNotification() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_USER_MONEY_WITHDRAW,
                    "A string name",
                    importance);

            channel.setDescription("Channel description");

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
