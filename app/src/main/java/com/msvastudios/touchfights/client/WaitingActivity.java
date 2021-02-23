package com.msvastudios.touchfights.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.msvastudios.touchfights.R;

public class WaitingActivity extends AppCompatActivity {

    String ipadress;
    TextView waiting_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        waiting_textView = findViewById(R.id.waiting_textView);

        Intent intent = getIntent();
        ipadress = intent.getStringExtra("ipAddress");

        startConnection(ipadress);


    }


    void startConnection(String ipadress) {
        WaitingActivityClientManager waitingActivityClientManager = new WaitingActivityClientManager(ipadress,this);
        waitingActivityClientManager.execute();
    }

    void close(){
        finish();
    }
}