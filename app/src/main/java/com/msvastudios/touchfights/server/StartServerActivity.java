package com.msvastudios.touchfights.server;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.msvastudios.touchfights.server.ServerSocketHandler.PORT;
import static com.msvastudios.touchfights.server.ServerSocketHandler.players;
import static com.msvastudios.touchfights.server.ServerSocketHandler.serverSocket;

import androidx.appcompat.app.AppCompatActivity;

import com.msvastudios.touchfights.R;
import com.msvastudios.touchfights.client.Fight_Activity;

public class StartServerActivity extends AppCompatActivity {

    boolean waiting = true;

    TextView infoip;
    TextView joined_players_view;
    Button start;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_server);

        infoip = findViewById(R.id.info_ip);
        joined_players_view = findViewById(R.id.joined_players_textview);
        start = findViewById(R.id.start_fight);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        players.get(players.size() - 1).writeToClient("start");
                    }
                });
                thread.start();


                Intent intent = new Intent(getApplicationContext(), ServerFightActivity.class);
                startActivity(intent);
            }
        });
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();

        infoip.setText(getIpAddress());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "SiteLocalAddress: "
                                + inetAddress.getHostAddress() + "\n";
                    }

                }

            }

        } catch (SocketException e) {
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }

    private class SocketServerThread implements Runnable {


        @Override
        public void run() {
            try {
                new Thread(new PlayerCounter()).start();

                serverSocket = new ServerSocket(PORT);

                while (waiting) {
                    //wait till client comes
                    Socket socket = serverSocket.accept();
                    System.out.println("[SERVER] Client was accepted");

                    //adds client to array list
                    ClientHandler clientHandler = new ClientHandler(socket, players.size());
                    players.add(clientHandler);
                    new Thread(clientHandler).start();

                    players.get(players.size() - 1).writeToClient("ID" + players.get(players.size() - 1).getID());
                    break;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public class PlayerCounter implements Runnable {

        @Override
        public void run() {
            while (waiting) {
                StartServerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        joined_players_view.setText(players.size() + 1 + "/2");
                    }
                });
                try {
                    Thread.sleep(1000);
                    if (players.size() + 1 == 2) {
                        waiting = false;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}