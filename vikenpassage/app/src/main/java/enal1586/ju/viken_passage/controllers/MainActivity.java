package enal1586.ju.viken_passage.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import enal1586.ju.viken_passage.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    public void btnMyAccountClicked(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }
    
    public void btnDBConnectClicked(View view) {
        Intent intent = new Intent(this, ContentActivity.class);
        startActivity(intent);
    }
    
}
