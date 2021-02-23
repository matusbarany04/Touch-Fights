package com.msvastudios.touchfights.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocketHandler {

    public static Socket socket;
    public static int ID;
    public static PrintWriter writer;
    public static BufferedReader in;
    public static OutputStream os;
    public static InputStream is;


    public static synchronized Socket getSocket() {
        return socket;
    }

    public static BufferedReader getIn() {
        return in;
    }

    public static PrintWriter getWriter() {
        return writer;
    }

    public static int getID() {
        return ID;
    }

    public static synchronized void setSocket(Socket socket) throws IOException {
        ClientSocketHandler.socket = socket;
        writer = new PrintWriter(socket.getOutputStream(), true);
        os = socket.getOutputStream();
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        is = socket.getInputStream();
    }
}
