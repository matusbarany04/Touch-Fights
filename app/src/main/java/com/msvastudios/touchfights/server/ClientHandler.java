package com.msvastudios.touchfights.server;

import androidx.annotation.Nullable;

import com.msvastudios.touchfights.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static com.msvastudios.touchfights.server.ServerFightActivity.serverPoints;

public class ClientHandler implements Runnable {

    int points = 100;
    private int ID;
    private Socket client;
    private PrintWriter writer;
    private BufferedReader in;


    public ClientHandler(@Nullable Socket client,int ID) throws IOException {
        this.client = client;
        this.ID = ID;
        writer = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    }


    @Override
    public void run() {
        try {
            while (true) {
                System.out.println("ClientHandler is running");
                String line = in.readLine();
                System.out.println("We got message : " + line);
                if(line.equals("Clicked")){
                    points++;
                    serverPoints--;
                    System.out.println("[SERVER] Client with id: " + ID + " Clicked with " + points + " points");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ClientHandler stopped running");
        }
    }

    public int getID() {
        return ID;
    }

    public int getPoints() {
        return points;
    }

    public void writeToClient(String message){
        writer.println(message);
    }
}


