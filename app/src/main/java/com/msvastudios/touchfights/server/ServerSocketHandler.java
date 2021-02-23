package com.msvastudios.touchfights.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerSocketHandler {

    public static ServerSocket serverSocket;
    public static int PORT = 8080;

    public static ArrayList<ClientHandler> players = new ArrayList<>();


    public static synchronized ServerSocket getSocket() {
        return serverSocket;
    }


    public static synchronized void setSocket(ServerSocket serverSocket){
        ServerSocketHandler.serverSocket = serverSocket;

    }
}
