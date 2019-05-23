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
    }
}
