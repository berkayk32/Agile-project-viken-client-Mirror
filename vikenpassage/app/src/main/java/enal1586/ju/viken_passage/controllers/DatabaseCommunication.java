package enal1586.ju.viken_passage.controllers;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import enal1586.ju.viken_passage.R;
import enal1586.ju.viken_passage.models.NetworkUtils;

public class DatabaseCommunication extends AppCompatActivity {
    
    private final String NETWORK_INTERFACE_WIFI = "wlan0";
    //private final String NETWORK_INTERFACE_BLUETOOTH = "wlan0";
    
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_communication);
        
        write();
    }
    
    private void write() {
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        
        String macAddr = NetworkUtils.getMACAddress(NETWORK_INTERFACE_WIFI);
        user.put("address", macAddr);
        
        // Add a new document with a generated ID
        db.collection("Mac Addresses").add(user)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(DatabaseCommunication.this, "Success!", Toast.LENGTH_SHORT).show();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DatabaseCommunication.this, "Error adding document", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void readData() {
        db.collection("users").get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getId();
                        Map<String, Object> data = document.getData();
                        
                        
                    }
                } else {
                    Toast.makeText(DatabaseCommunication.this,
                            "Error getting documents.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    
}
