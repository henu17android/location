package com.example.client;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * client 读操作线程类
 * @author qmn
 */
public class ClientInputThread extends Thread {

    private Socket socket;
    private DataInputStream inputStream;
    private boolean isStart = true;
    private MessageListener mMessageListener;


    private static final String TAG = "ClientInputThread";

    public ClientInputThread(Socket socket) {
        this.socket = socket;
        try {
            //获得字节输入流
            inputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "ClientInputThread: "+e.getMessage());
        }
    }

    @Override
    public void run() {
        try{
            byte[] buffer = new byte[1024];
            while (isStart) {
                int readSize = inputStream.read(buffer);
                String msg = new String(buffer,0,readSize);
                //接口传递 获得的message
                mMessageListener.getMessage(msg);

            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "run: readSize:"+e.getMessage());
        }
    }

    public DataInputStream getInputStream() {
        return inputStream;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setInputStream(DataInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public MessageListener getMessageListener() {
        return mMessageListener;
    }

    public void setmMessageListener(MessageListener mMessageListener) {
        this.mMessageListener = mMessageListener;
    }
}
