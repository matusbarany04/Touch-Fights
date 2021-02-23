package com.msvastudios.touchfights.client;

import android.content.Intent;
import android.os.AsyncTask;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static com.msvastudios.touchfights.client.ClientSocketHandler.ID;
import static com.msvastudios.touchfights.client.ClientSocketHandler.in;
import static com.msvastudios.touchfights.client.ClientSocketHandler.socket;
import static com.msvastudios.touchfights.client.ClientSocketHandler.writer;


public class WaitingActivityClientManager extends AsyncTask<Void, Void, Void> {

    String address;
    WaitingActivity waitingActivity;
    boolean waiting = true;

    WaitingActivityClientManager(String address, WaitingActivity waitingActivity) {
        this.address = address;
        this.waitingActivity = waitingActivity;
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        try {
            ClientSocketHandler.setSocket(new Socket(address, 8080));

            if (socket.isBound()) {

                waitingActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        waitingActivity.waiting_textView.setText("Joined");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        waitingActivity.waiting_textView.setText("Waiting for host to start...");
                    }
                });
            } else {
                waitingActivity.close();
                endSocketConnection(socket);
            }

            writer = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("[CLIENT] writer and reader were created");

            while (waiting) {
                System.out.println("[CLIENT] waiting for message");
                String serverMessage = in.readLine();
                System.out.println("[CLIENT] we got this message: " + serverMessage);
                if (serverMessage.contains("ID")) {
                    StringBuilder sb = new StringBuilder(serverMessage);
                    sb.delete(0, 2);
                    ID = Integer.valueOf(sb.toString());
                } else if (serverMessage.equals("start")) {
                    Intent newActivity = new Intent(waitingActivity.getApplicationContext(), Fight_Activity.class);
                    waitingActivity.startActivity(newActivity);
                    waiting = false;
                } else if (serverMessage.equals("cancel")) {
                    waitingActivity.close();
                    DynamicToast.makeWarning(waitingActivity.getApplicationContext(), "Host stopped the fight!").show();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean endSocketConnection(Socket socket) {
        try {
            socket.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

