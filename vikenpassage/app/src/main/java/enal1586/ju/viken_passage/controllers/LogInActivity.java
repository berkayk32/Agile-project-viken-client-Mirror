package enal1586.ju.viken_passage.controllers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import enal1586.ju.viken_passage.R;

public class LogInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final String SHARED_PREF_MAC = "mac";
    private static final String KEY_MAC = "key_mac";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ConstraintLayout constraintLayout = findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        findViewById(R.id.btnLogInGoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleLogIn(view);
            }
        });
    }

    public void googleLogIn(View view) {
        EditText bthMacAddress = findViewById(R.id.bluetoothET);
        String macAddress = bthMacAddress.getText().toString();
        getMacAddressFromSharedPreferences();
        if (!isValidMacAddress(macAddress)) {
            Toast.makeText(
                    this,
                    "Enter Valid Mac Address.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            //Save MacAddress To Shared Preferences
            saveMacAddressToSharedPreferences(macAddress);
        }
        Intent intent = new Intent(this, GoogleLogInActivity.class);
        intent.putExtra("MACADDRESS", getMacAddressFromSharedPreferences());
        startActivityForResult(intent, 5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5) {
            if (mAuth.getCurrentUser() != null) {
                finish();
            }
        }
    }

    //settings button
    public void SettingsButtonClicked(View view){
        Intent dialogIntent = new Intent(Settings.ACTION_DEVICE_INFO_SETTINGS);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);
    }


    @Override
    public void onBackPressed() {
        //TODO add timer and ask if user wants to exite program

    }
    private boolean isValidMacAddress(String macaddress) {
        if(macaddress.equals("")){
            return false;
        }
        String MAC_PATTERN = "((?:[a-zA-Z0-9]{2}[:-]){5}[a-zA-Z0-9]{2})";

        Pattern pattern = Pattern.compile(MAC_PATTERN);
        Matcher matcher = pattern.matcher(macaddress);
        return matcher.matches();
    }
    private String getMacAddressFromSharedPreferences(){
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_MAC, MODE_PRIVATE);
        String macAddress = sp.getString(KEY_MAC, null);

        if (macAddress != null) {
            return macAddress;
        }
        return null;
    }
    private void saveMacAddressToSharedPreferences(String macAddress){
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_MAC, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(KEY_MAC, macAddress);

        editor.apply();
    }

}
