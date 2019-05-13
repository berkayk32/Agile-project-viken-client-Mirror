package enal1586.ju.viken_passage.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import enal1586.ju.viken_passage.R;

public class LogInActivity extends AppCompatActivity {

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

<<<<<<< HEAD:vikenpassage/app/src/main/java/enal1586/ju/viken_passage/controllers/AccountActivity.java
    
=======
>>>>>>> refactorBranch:vikenpassage/app/src/main/java/enal1586/ju/viken_passage/controllers/LogInActivity.java
    public void googleLogIn(View view) {
        EditText bluetoothText = findViewById(R.id.bluetoothET);
        if (bluetoothText.getText().toString().equals("")) {
            Toast.makeText(
                    this,
                    "You need to add your bluetooth mac address.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, GoogleLogInActivity.class);
        intent.putExtra("MACADDRESS", bluetoothText.getText().toString());
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
