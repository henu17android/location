package com.example.client;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class OutputThread implements Runnable {

    private String message;
    private DataOutputStream dataOutputStream;

    public OutputThread(DataOutputStream dataOutputStream,String message) {
        this.dataOutputStream = dataOutputStream;
        this.message = message;
    }


    @Override
    public void run() {
        try {
            dataOutputStream.write(message.getBytes());
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
