package enal1586.ju.viken_passage.controllers;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import enal1586.ju.viken_passage.R;

public class AccountActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        findViewById(R.id.btnLogInGoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleLogIn(view);
            }
        });
    }
    
    public void googleLogIn(View view) {
        Intent intent = new Intent(this, GoogleLogInActivity.class);
        startActivity(intent);
    }
    
    public void btnSignOutClicked(View view) {
        Intent intent = new Intent(this, GoogleLogInActivity.class);
        intent.putExtra(GoogleLogInActivity.LOGIN_ATTEMPT, false);
        startActivity(intent);
    }
    //log in button
    public void loginButtonClicked(View view){
        Intent viken = new Intent(AccountActivity.this, ContentActivity.class);
        startActivity(viken);
    }
//settings button
    public void SettingsButtonClicked(View view){
        Intent dialogIntent = new Intent(Settings.ACTION_DEVICE_INFO_SETTINGS);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);
    }

}
