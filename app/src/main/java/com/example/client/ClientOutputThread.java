package com.example.client;

import android.util.Log;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * client 写线程类
 * @author qmn
 */

/*
public class ClientOutputThread extends Thread {

    private Socket socket;
    public DataOutputStream outputStream;
    private boolean isStart = true;
    private String msg;
    private static final String TAG = "ClientOutputThread";


    public ClientOutputThread (Socket socket) {
        this.socket = socket;
        try {
            outputStream = new DataOutputStream(socket.getOutputStream());
            Log.d(TAG, "ClientOutputThread: ");
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
                    outputStream.write(msg.getBytes());
                    outputStream.flush();
//                    Log.d(TAG, "run: 发送成功");
//                    streamWriter = new OutputStreamWriter(outputStream, "UTF-8");
//                    StringBuffer sBuilder = new StringBuffer();
//                    sBuilder.append(msg);
//                    streamWriter.write(sBuilder.toString());
//                    streamWriter.flush();
                    Log.d("message", "send: "+msg);
                    synchronized (this) {
                        wait();   //发送后线程等待
                    }
                }
            }
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "run: writeBytes:"+e.getMessage());
            isStart = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d(TAG, "run: "+e.getMessage());
            isStart = false;
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public OutputStream getOutputStream() {
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

    public void setMsg(String msg) {
        this.msg = msg;
    }
}*/
