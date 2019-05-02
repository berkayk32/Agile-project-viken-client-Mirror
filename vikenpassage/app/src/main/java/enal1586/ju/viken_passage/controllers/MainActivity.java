package enal1586.ju.viken_passage.controllers;

import android.app.Notification;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import enal1586.ju.viken_passage.R;

public class MainActivity extends AppCompatActivity {

    private NotificationManagerCompat notificationManagerCompat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManagerCompat = NotificationManagerCompat.from(this);
    }
    
    public void btnMyAccountClicked(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }
    
    public void btnDBConnectClicked(View view) {
        Intent intent = new Intent(this, ContentActivity.class);
        startActivity(intent);
    }


    public void sendNotification(View view) {
        Notification notification = new NotificationCompat.Builder(this, Notifications.CHANNEL_USER_MONEY_WITHDRAW)
                .setSmallIcon(R.drawable.toll2)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                // Set the intent that will fire when the user taps the notification
                //.setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        notificationManagerCompat.notify(1, notification);

    }


}
