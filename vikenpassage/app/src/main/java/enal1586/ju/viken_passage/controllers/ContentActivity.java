package enal1586.ju.viken_passage.controllers;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import enal1586.ju.viken_passage.R;
import enal1586.ju.viken_passage.models.NetworkUtils;

public class ContentActivity extends AppCompatActivity {
    
    //private final String NETWORK_INTERFACE_BLUETOOTH = "wlan0";
    private final String NETWORK_INTERFACE_WIFI = "wlan0";
    private final String MAC_ADRESS = "Mac Addresses";
    private final String USERS = "Users";

    private final String TEMP_UNIQUE_EMAIL_ADRESS = "temporary@unique.email.com";
    
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    
    
    ArrayAdapter adapter = null;
    ArrayList<String> list = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
    
        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
    
        ListView listView = findViewById(R.id.listViewOfStuff);
        listView.setAdapter(adapter);


        //readData();
        //registerUser();
        syncUser();
    }
    
    private void registerUser() {
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        
        String macAddr = NetworkUtils.getMACAddress(NETWORK_INTERFACE_WIFI);
        user.put("User Name", TEMP_UNIQUE_EMAIL_ADRESS);

        // Add a new document with a generated ID
        db.collection(MAC_ADRESS).document(macAddr).set(user)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ContentActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void syncUser() {
        String macAddr = NetworkUtils.getMACAddress(NETWORK_INTERFACE_WIFI);
        DocumentReference contactListener = db.collection(USERS).document("testMail");

        contactListener.addSnapshotListener(new EventListener< DocumentSnapshot >() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(ContentActivity.this, "Something went wrong when trying to sync user data.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Map<String, Object> data = documentSnapshot.getData();
                    list.add(data.get("Balance").toString() + data.get("First Name"));
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
    
    private void readData() {
        db.collection(MAC_ADRESS).document(TEMP_UNIQUE_EMAIL_ADRESS).get()
        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot result = task.getResult();
                }
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // TODO: Unsync user..!
        finish();
    }
}
