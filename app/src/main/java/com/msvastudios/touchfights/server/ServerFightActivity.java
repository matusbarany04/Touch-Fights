package com.msvastudios.touchfights.server;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.msvastudios.touchfights.R;

import static com.msvastudios.touchfights.server.ServerSocketHandler.players;

public class ServerFightActivity extends AppCompatActivity {
    private static final int MAX_POINTS = 100;
    static int serverPoints = MAX_POINTS;

    boolean fighting = true;

    TextView myServerPoints, enemyServerPoints;
    Button attackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_fight);

        attackButton = findViewById(R.id.attackServerButton);
        myServerPoints = findViewById(R.id.myServerPoints);
        enemyServerPoints = findViewById(R.id.enemyServerPointsTextView);


        new Thread(new NumberUpdater()).start();


        attackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverPoints++;
                players.get(0).points--;
            }
        });
    }


    class NumberUpdater implements Runnable {

        int myLastNumberOfPoints;
        int enemyLastNumberOfPoints;

        @Override
        public void run() {
            while (fighting) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (enemyLastNumberOfPoints != players.get(0).getPoints() || myLastNumberOfPoints != serverPoints) {
                    players.get(0).writeToClient("MP" + players.get(0).getPoints());
                    players.get(0).writeToClient("EP" + serverPoints);

                    enemyLastNumberOfPoints = players.get(0).getPoints();
                    myLastNumberOfPoints = serverPoints;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            enemyServerPoints.setText("" + players.get(0).getPoints());
                            myServerPoints.setText("" + serverPoints);
                        }
                    });
                }
            }
        }

    }


}