package enal1586.ju.viken_passage.controllers;

import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import enal1586.ju.viken_passage.R;

public class AccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    
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
}
