package com.msvastudios.touchfights.client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import static com.msvastudios.touchfights.client.ClientSocketHandler.*;

import com.msvastudios.touchfights.R;

public class Fight_Activity extends AppCompatActivity {
    boolean fighting = true;

    TextView myPointsView, enemyPointsView;
    Button attackButton;;

    boolean localhost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight);

//        Intent intent = getIntent();
//        if(intent.getIntExtra("SPECIAL", -1) == 0){
//
//        }

        myPointsView = findViewById(R.id.myServerPoints);
        enemyPointsView = findViewById(R.id.enemyServerPointsTextView);
        attackButton = findViewById(R.id.attackServerButton);


        attackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Sending message that user clicked!!");
               new Thread(new Runnable() {
                    @Override
                    public void run() {
                        writer.println("Clicked");
                    }
                }).start();

                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_anim);
                attackButton.startAnimation(anim);
            }
        });


        Thread thread = new Thread(new PointsUpdaterThread());
        thread.start();
    }

    class PointsUpdaterThread implements Runnable {


        @Override
        public void run() {
            try {
                while (fighting) {
                    Thread.sleep(10);
                    final int points;
                    final String message = in.readLine();

                //    System.out.println("[CLIENT] We got: " + message);
                    StringBuilder sb = new StringBuilder(message);

                    if (message.contains("MP") || message.contains("EP")) {
                        sb.delete(0, 2);
                        //System.out.println(sb);

                        points = Integer.valueOf(sb.toString());

                        Fight_Activity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (message.contains("MP")) myPointsView.setText("" +points);
                                else enemyPointsView.setText("" +points);
                            }
                        });

                    }
                }
            } catch (Exception e) {
            }
        }
    }
}