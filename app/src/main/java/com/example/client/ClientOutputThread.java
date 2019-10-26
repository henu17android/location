package com.example.client;

import android.util.Log;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * client 写线程类
 * @author qmn
 */
public class ClientOutputThread extends Thread {

    private Socket socket;
    private DataOutputStream outputStream;
    private boolean isStart = true;
    private String msg;
    private static final String TAG = "ClientOutputThread";

    public ClientOutputThread (Socket socket) {
        this.socket = socket;
        try {
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "ClientOutputThread: "+e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            while (isStart) {
                if (msg != null) {
                    outputStream.writeBytes(msg);
                    outputStream.flush();
                    synchronized (this){
                        wait();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "run: writeBytes:"+e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d(TAG, "run:synchronized :"+e.getMessage());
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public DataOutputStream getOutputStream() {
        return outputStream;
    }

    public boolean isStart() {
        return isStart;
    }

    public String getMsg() {
        return msg;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setOutputStream(DataOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setStart(boolean start) {
        isStart = start;
    }
}
