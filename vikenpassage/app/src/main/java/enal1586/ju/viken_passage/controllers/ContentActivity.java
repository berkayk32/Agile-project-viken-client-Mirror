package enal1586.ju.viken_passage.controllers;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import enal1586.ju.viken_passage.R;
import enal1586.ju.viken_passage.models.CustomAdapter;
import enal1586.ju.viken_passage.models.HistoryModel;

public class ContentActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    private static final int LOGIN_INTENT = 2;

    ImageView mBlueIv;
    Switch aSwitch;
    Thread timerThread = null;
    BluetoothAdapter mBlueAdapter;

    TextView expiryDateLabel;
    private final String USERS = "Users";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayAdapter adapter = null;
    ArrayList<String> list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        // Code for bluetooth detection via broadcast
        IntentFilter filter1 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver1, filter1);
        // Code for bluetooth detection via broadcast

        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, LogInActivity.class);
            startActivityForResult(intent, LOGIN_INTENT);
        }
        else {
            initiateLogin();
        }

        mBlueIv = findViewById(R.id.imageView);
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();

        aSwitch = findViewById(R.id.bluetoothSwitch);//Using Swich  to enible or disable bluetooth
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mBlueAdapter == null){
                        aSwitch.setChecked(false);
                        Toast.makeText(getBaseContext(), "Bluetooth is not available", Toast.LENGTH_SHORT).show();
                    }
                    else if (!mBlueAdapter.isEnabled()) {
                        //enable blutooth
                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        // Making Your Device Discoverable

                        startActivityForResult(intent, REQUEST_DISCOVER_BT);
                        mBlueIv.setImageResource(R.drawable.ic_action_on);

                        String addr = mBlueAdapter.getAddress();
                        Toast.makeText(ContentActivity.this, "The address is: " + addr, Toast.LENGTH_SHORT).show();

                        //Making Your Device Discoverable
                        Toast.makeText(getBaseContext(), "Bluetooth On", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(intent, REQUEST_ENABLE_BT);

                        // Making Your Device Discoverable

                        startActivityForResult(intent, REQUEST_DISCOVER_BT);
                        mBlueIv.setImageResource(R.drawable.ic_action_on);
                        Toast.makeText(getBaseContext(), "Bluetooth is already on", Toast.LENGTH_SHORT).show();
                    }

                } else {


                    if (mBlueAdapter == null){
                        aSwitch.setChecked(false);
                        Toast.makeText(getBaseContext(), "Bluetooth is not available...", Toast.LENGTH_SHORT).show();
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

    private void initiateLogin() {
        TextView userName = findViewById(R.id.userNameTW);
        String email = mAuth.getCurrentUser().getEmail();
        String userNameText = email.substring(0,email.indexOf("@"));

        userName.setText(userNameText);

        expiryDateLabel = findViewById(R.id.freePassLabel);

        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);

        ListView listView = findViewById(R.id.listViewOfStuff);
        listView.setAdapter(adapter);

        syncUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(this, "Inne!", Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch(state) {
                    case BluetoothAdapter.STATE_OFF:
                        mBlueIv.setImageResource(R.drawable.ic_action_off);
                        aSwitch.setChecked(false);
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_ON:
                        mBlueIv.setImageResource(R.drawable.ic_action_on);
                        aSwitch.setChecked(true);
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                }

            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            TextView userName = findViewById(R.id.userNameTW);
            String email = mAuth.getCurrentUser().getEmail();
            String userNameText = email.substring(0,email.indexOf("@"));

            userName.setText(userNameText);
        }
    }

    private void syncUser() {

        final DocumentReference contactListener = db.collection(USERS).document(mAuth.getCurrentUser().getEmail());

        contactListener.addSnapshotListener(new EventListener< DocumentSnapshot >() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(ContentActivity.this, "Something went wrong when trying to sync user data.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists() && documentSnapshot.getTimestamp("expiryDate") != null) {
                    Date freePass = documentSnapshot.getTimestamp("expiryDate").toDate();
                    Date currentTime = Calendar.getInstance().getTime();

                    stopThread();

                    if (currentTime.after(freePass)) {
                        // Have not paid
                        expiryDateLabel.setText("You shall not pass!");
                    }
                    else {
                        // Already paid
                        startTimerThread(freePass);
                    }

                    list.clear();
                    updateUserHistory();

                }
            }
        });
    }

    @TargetApi(26)
    public void startTimerThread(final Date expiryDate) {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                Date currentTime = Calendar.getInstance().getTime();
                timerThread = Thread.currentThread();
                do {
                    try {
                        LocalDateTime localDateTimeCurrent = LocalDateTime.ofInstant(currentTime.toInstant(), ZoneId.systemDefault());
                        LocalDateTime localDateTimeExpiryDate = LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault());
                        final String timeLeft = updateTimeLeft(localDateTimeCurrent, localDateTimeExpiryDate);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                expiryDateLabel.setText(timeLeft);

                            }
                        });
                        Thread.sleep(1000);
                        currentTime = Calendar.getInstance().getTime();
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } while (currentTime.before(expiryDate) && timerThread != null && timerThread == Thread.currentThread());

                if (timerThread != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            expiryDateLabel.setText("You shall not pass!");
                        }
                    });
                }

            }
        });
        th.start();
    }

    @TargetApi(26)
    private String updateTimeLeft(LocalDateTime currentTime, LocalDateTime freePass) {
        long durationInSeconds = Duration.between(currentTime, freePass).getSeconds();
        String timeLeftString = String.format("%02d:%02d:%02d", TimeUnit.SECONDS.toHours(durationInSeconds),
                TimeUnit.SECONDS.toMinutes(durationInSeconds) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.SECONDS.toSeconds(durationInSeconds) % TimeUnit.MINUTES.toSeconds(1));

        if (TimeUnit.SECONDS.toHours(durationInSeconds) > 23) {
            return freePass.toString();
        }

        return timeLeftString;
    }

    private void updateUserHistory() {
        db.collection(USERS).document(mAuth.getCurrentUser().getEmail()).collection("history")
                .orderBy("date", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {

                if (documentSnapshots.isEmpty()) {
                    return;
                } else {
                    List<DocumentSnapshot> documents = documentSnapshots.getDocuments();
                    final ArrayList<HistoryModel> historyModels;
                    CustomAdapter customHistoryAdapter;
                    ListView listView;
                    listView = (ListView) findViewById(R.id.listViewOfStuff);

                    historyModels = new ArrayList<>();

                    int totalAmount = 0;
                    for (int i = 0; i < documents.size(); i++) {
                        DocumentSnapshot documentSnapshot = documents.get(i);
                        Map<String, Object> data = documentSnapshot.getData();
                        totalAmount += Integer.valueOf(data.get("payment").toString());
                        historyModels.add(new HistoryModel(documentSnapshot.getTimestamp("date").toDate(), data.get("payment").toString(),documentSnapshot.getGeoPoint("position")));
                    }

                    TextView viewById = findViewById(R.id.textView4);
                    viewById.setText(totalAmount + " kr");

                    customHistoryAdapter = new CustomAdapter(historyModels, getApplicationContext());
                    listView.setAdapter(customHistoryAdapter);


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
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                FirebaseAuth.getInstance().signOut();
                                finish();
                                startActivity(getIntent());
                            }
                        }
                ).setNegativeButton(
                android.R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do not do anything.
                    }
                }
        ).show();
    }

public void GPSButtonClicked(View view){
    Intent gps = new Intent(this, GPS.class);
    startActivity(gps);

}
}
