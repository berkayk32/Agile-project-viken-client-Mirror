package enal1586.ju.viken_passage.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import enal1586.ju.viken_passage.R;

public class AccountActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        
        findViewById(R.id.btnLogInGoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleLogIn(view);
            }
        });
        
    }
    
    public void googleLogIn(View view) {
        Toast.makeText(this, "Clicked Google login!", Toast.LENGTH_SHORT).show();
    }
    
}
