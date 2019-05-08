package enal1586.ju.viken_passage.controllers;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import enal1586.ju.viken_passage.R;
import enal1586.ju.viken_passage.models.CustomAdapter;
import enal1586.ju.viken_passage.models.HistoryModel;
import enal1586.ju.viken_passage.models.NetworkUtils;

public class ContentActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    ImageView mBlueIv;
    TextView freePassLabel;
    Switch aSwitch;
    Thread timerThread = null;
    BluetoothAdapter mBlueAdapter;


    //private final String NETWORK_INTERFACE_BLUETOOTH = "wlan0";
    private final String NETWORK_INTERFACE_WIFI = "wlan0";
    private final String MAC_ADRESS = "Mac Addresses";
    private final String USERS = "Users";

    private final String TEMP_UNIQUE_EMAIL_ADRESS = "temporary@unique.email.com";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayAdapter adapter = null;
    ArrayList<String> list = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
        }
        else {
            TextView userName = findViewById(R.id.userNameTW);
            userName.setText(mAuth.getCurrentUser().getEmail());
            freePassLabel = findViewById(R.id.freePassLabel);

            list = new ArrayList<>();
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);

            ListView listView = findViewById(R.id.listViewOfStuff);
            listView.setAdapter(adapter);

            syncUser();
        }

        mBlueIv = findViewById(R.id.imageView);
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();

        aSwitch = (Switch) findViewById(R.id.switch1);//Using Swich  to enible or disable bluetooth
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {


                    if (mBlueAdapter == null){

                        Toast.makeText(getBaseContext(), "Bluetooth is not available", Toast.LENGTH_SHORT).show();

                    }
                    else if (!mBlueAdapter.isEnabled()) {
                        {
                            //enable blutooth
                            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(intent, REQUEST_ENABLE_BT);
                            // Making Your Device Discoverable

                            startActivityForResult(intent, REQUEST_DISCOVER_BT);
                            mBlueIv.setImageResource(R.drawable.ic_action_on);
                            //Making Your Device Discoverable
                            Toast.makeText(getBaseContext(), "Bluetooth On", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

                        // Making Your Device Discoverable

                        startActivityForResult(intent, REQUEST_DISCOVER_BT);
                        mBlueIv.setImageResource(R.drawable.ic_action_on);
                        Toast.makeText(getBaseContext(), "Bluetooth is already on", Toast.LENGTH_SHORT).show();
                    }

                } else {


                    if (mBlueAdapter == null){

                        Toast.makeText(getBaseContext(), "Bluetooth is not available", Toast.LENGTH_SHORT).show();
                    }
                    else if (mBlueAdapter.isEnabled()){
                        mBlueAdapter.disable();
                        mBlueIv.setImageResource(R.drawable.ic_action_off);

                        Toast.makeText(getBaseContext(), "Bluetooth Off", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        mBlueIv.setImageResource(R.drawable.ic_action_off);
                        Toast.makeText(getBaseContext(), "Bluetooth is already off", Toast.LENGTH_SHORT).show();

                    }


                    }

                }

        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            TextView userName = findViewById(R.id.userNameTW);
            userName.setText(mAuth.getCurrentUser().getEmail());
        }
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

                    stopThread();

                    if (currentTime.after(freePass)) {
                        // Have not paid
                        freePassLabel.setText("You shall not pass!");
                    }
                    else {
                        // Already paid

                        startTimerThread(freePass);
                        //updateTimeLeft(currentTime, freePass);
                    }

                    list.clear();
                    updateHistory();

                }
            }
        });
    }

    public void startTimerThread(final Date freePass) {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                Date currentTime = Calendar.getInstance().getTime();
                timerThread = Thread.currentThread();

                do {
                    try {
                        final String timeLeft = updateTimeLeft(currentTime, freePass);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                freePassLabel.setText(timeLeft);

                            }
                        });
                        Thread.sleep(1000);
                        currentTime = Calendar.getInstance().getTime();
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } while(currentTime.before(freePass) && timerThread != null && timerThread == Thread.currentThread());

                if (timerThread != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            freePassLabel.setText("You shall not pass!");
                        }
                    });
                }

            }
        });
        th.start();
    }

    private String updateTimeLeft(Date currentTime, Date freePass) {
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
            hours = (24 - hours);
        }
        hours -= minutesHasPassed;
        String hoursLeft = hours < 10 ? "0" + hours : String.valueOf(hours);



        String timeLeft = hoursLeft + ":" + minutesLeft + ":" + secondsLeft;

        return timeLeft;
    }
    
    private void updateHistory() {
        db.collection(USERS).document("testMail").collection("history")
        .orderBy("date", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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

                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void stopThread() {

        if (timerThread != null) {
            timerThread.interrupt();
            timerThread = null;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // TODO: Unsync user..!
        stopThread();

        finish();
    }

    public void logoutButtonClicked(View view){
        new AlertDialog.Builder(this)
        .setMessage("Do you really want to Logout?")
        .setPositiveButton(
            android.R.string.yes,
            new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int whichButton) {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    startActivity(getIntent());
                }
            }
        ).setNegativeButton(
            android.R.string.no,
            new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int whichButton){
                    // Do not do anything.
                }
            }
        ).show();
    }


}
