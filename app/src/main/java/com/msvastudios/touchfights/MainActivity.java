package com.msvastudios.touchfights;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.msvastudios.touchfights.client.JoinAsClientActivity;
import com.msvastudios.touchfights.server.StartServerActivity;

public class MainActivity extends Activity {

    Button hostButton;
    Button joinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        hostButton = findViewById(R.id.hostButon);
        hostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StartServerActivity.class);
                startActivity(intent);
            }
        });


        joinButton = findViewById(R.id.joinButton);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentClient = new Intent(getApplicationContext(), JoinAsClientActivity.class);
                startActivity(intentClient);
            }
        });

    }
}



