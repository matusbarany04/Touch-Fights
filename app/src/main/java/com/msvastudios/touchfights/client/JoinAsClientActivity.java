package com.msvastudios.touchfights.client;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.msvastudios.touchfights.R;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JoinAsClientActivity extends AppCompatActivity {

    TextView textResponse;
    EditText editTextAddress;
    Button buttonConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_as_client);

        editTextAddress = (EditText) findViewById(R.id.address);
        buttonConnect = (Button) findViewById(R.id.connect);

        buttonConnect.setOnClickListener(buttonConnectOnClickListener);
        editTextAddress.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_ATOP);


    }

    View.OnClickListener buttonConnectOnClickListener =
            new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    String regex = "\\d{3}[.]\\d{3}[.]\\d+[.]\\d+";
                    String ipadress = editTextAddress.getText().toString().trim();
                    if (Pattern.matches(regex, ipadress)) {

                        Intent intent = new Intent(getApplicationContext(), WaitingActivity.class);
                        intent.putExtra("ipAddress",ipadress);
                        startActivity(intent);

                    } else {

                        JoinAsClientActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                editTextAddress.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
                                DynamicToast.makeWarning(getApplicationContext(), "Enter valid IP address").show();

                            }
                        });
                    }
                }
            };


    public String charReplacer(String text, char replace, char replacement) {
        char[] chars = text.toCharArray();

        for (int index = 0; index < chars.length; index++) {

            if (chars[index] == replace) {
                chars[index] = replacement;
            }

        }

        return chars.toString();
    }
}