package enal1586.ju.viken_passage.controllers;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import enal1586.ju.viken_passage.R;
import enal1586.ju.viken_passage.models.HistoryModel;
import enal1586.ju.viken_passage.models.NetworkUtils;

public class ContentActivity extends AppCompatActivity {

    TextView freePassLabel;

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

        freePassLabel = findViewById(R.id.freePassLabel);

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

        DocumentReference contactListener = db.collection(USERS).document("testMail");

        contactListener.addSnapshotListener(new EventListener< DocumentSnapshot >() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(ContentActivity.this, "Something went wrong when trying to sync user data.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Date freePass = documentSnapshot.getTimestamp("freePass").toDate();
                    Date currentTime = Calendar.getInstance().getTime();

                    if (currentTime.after(freePass)) {
                        // Have not paid
                        freePassLabel.setText("You shall not pass!");
                    }
                    else {
                        // Already paid
                        updateTimeLeft(currentTime, freePass);
                    }

                    list.clear();
                    updateHistory();

                }
            }
        });
    }

    private void updateTimeLeft(Date currentTime, Date freePass) {
        // TODO REFACTOR!

        SimpleDateFormat dayPattern = new SimpleDateFormat("dd");
        String untilDay = dayPattern.format(freePass);
        String currentDay = dayPattern.format(currentTime);

        SimpleDateFormat secondPattern = new SimpleDateFormat("ss");
        String untilSecond = secondPattern.format(freePass);
        String currentSecond = secondPattern.format(currentTime);
        int seconds = Integer.valueOf(untilSecond) - Integer.valueOf(currentSecond);
        int secondsHasPassed = 0;
        if (seconds < 0) {
            secondsHasPassed = 1;
            seconds = seconds + 60;
        }
        String secondsLeft = seconds < 10 ? "0" + seconds : String.valueOf(seconds);

        SimpleDateFormat minutePattern = new SimpleDateFormat("mm");
        String untilMinute = minutePattern.format(freePass);
        String currentMinute = minutePattern.format(currentTime);
        int minutes = Integer.valueOf(untilMinute) - Integer.valueOf(currentMinute);
        int minutesHasPassed = 0;
        if (minutes < 0) {
            minutesHasPassed = 1;
            minutes = minutes + 60;
        }
        minutes -= secondsHasPassed;
        if (minutes < 0) {
            minutesHasPassed = 1;
            minutes = 59;
        }
        String minutesLeft = minutes < 10 ? ("0" + (minutes)) : String.valueOf(minutes);

        SimpleDateFormat hourPattern = new SimpleDateFormat("HH");
        String untilHour = hourPattern.format(freePass);
        String currentHour = hourPattern.format(currentTime);
        int hours = Integer.valueOf(untilHour) - Integer.valueOf(currentHour);
        if (!untilDay.equals(currentDay)) {
            int marginDifference = 1;
            hours = (24 - hours);
        }
        hours -= minutesHasPassed;
        String hoursLeft = hours < 10 ? "0" + hours : String.valueOf(hours);



        String timeLeft = hoursLeft + ":" + minutesLeft + ":" + secondsLeft;

        freePassLabel.setText(timeLeft);
    }

    private void needToPay() {
        freePassLabel.setText("You have not paid.");
    }
    
    private void updateHistory() {
        db.collection(USERS).document("testMail").collection("history")
        .orderBy("date").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (documentSnapshots.isEmpty()) {
                    return;
                } else {
                    List<DocumentSnapshot> documents = documentSnapshots.getDocuments();
                    final ArrayList<HistoryModel> historyModels;
                    CustomAdapter adapter;
                    ListView listView;
                    listView=(ListView)findViewById(R.id.listViewOfStuff);

                    historyModels= new ArrayList<>();
                    for (int i = 0; i < documents.size(); i++) {
                        DocumentSnapshot documentSnapshot = documents.get(i);
                        Map<String, Object> data = documentSnapshot.getData();
                        historyModels.add(new HistoryModel(data.get("payment").toString(), documentSnapshot.getTimestamp("date").toDate().toString()));


                    }
                    adapter= new CustomAdapter(historyModels,getApplicationContext());


                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            HistoryModel historyModel= historyModels.get(position);

                            Snackbar.make(view, historyModel.getPayment()+"\n"+historyModel.getDate(), Snackbar.LENGTH_LONG)
                                    .setAction("No action", null).show();
                        }
                    });


                        //list.add(data.get("payment").toString());


                    }
                    adapter.notifyDataSetChanged();
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
