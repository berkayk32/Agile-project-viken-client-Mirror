package enal1586.ju.viken_passage.controllers;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import static android.support.constraint.Constraints.TAG;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        super.onMessageReceived(remoteMessage);

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
}
