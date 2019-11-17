package com.example.client;

import android.util.Log;
import android.widget.Toast;

import com.example.client.ClientInputThread;
import com.example.client.ClientOutputThread;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class Client {

//    private String ip = null;
//    private int port = 0;
//    private Socket socket = null;
//    private InputStream inputStream = null;
//    private DataOutputStream outputStream = null;
//    private static final String TAG = "Client";
//    private boolean isStartReceiveMsg;
//
//    public Client(String ip, int port) {
//        this.ip = ip;
//        this.port = port;
//    }
//
//    public void createConnection() throws IOException {
//        try {
//            socket = new Socket(ip, port);
//            socket.setSoTimeout(20000);
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void sendMessage(String sendMessage) throws IOException {
//
//        outputStream = new DataOutputStream(socket.getOutputStream());
//        outputStream.writeBytes(sendMessage);
//
////        BufferedWriter out = null;
////        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//
////            byte[] bytes = sendMessage.getBytes();
////            outputStream.write(bytes);
////            outputStream.writeUTF(sendMessage);
//
////        out.write(sendMessage);
//////        out.flush();
//////        out.close();
//
//        outputStream.flush();
//        outputStream.close();
//
//    }
//
//
//    public synchronized String getMessage () throws IOException {
//        byte[] buffer = new byte[1024];
//        inputStream = new DataInputStream(socket.getInputStream());
//
//        int len = inputStream.read(buffer);
//        String message = new String(buffer,0,len);
//
//        return message;
//
//
//    }

    public Socket socket;
    private ClientThread clientThread;

    private String ip;
    private int port;
    private static final String TAG = "ClientClass";

    public Client(String ip,int port) {
        super();
        this.ip = ip;
        this.port = port;
    }

 //与服务器端建立连接
    public boolean connection() {
        try {
            socket = new Socket(ip,port);

        } catch (UnknownHostException e) {
            e.printStackTrace();
            Log.d(TAG, "connection: "+e.getMessage());
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "connection: "+e.getMessage());
            return false;
        }

        if (socket.isConnected()) {
            System.out.println("Connected.."+socket.isConnected());

            //连接成功开启client 线程
            clientThread = new ClientThread(socket);
            clientThread.start();
        }
        return true;
    }

    public ClientInputThread getClientInputThread() {
        return clientThread.inputThread;
    }

    public ClientOutputThread getClientOutputThread() {
        return clientThread.outputThread;
    }


    /**
     * 负责连接socket和管理读写线程
     */
    public class ClientThread extends Thread {
        //app 全局只有一个client，client 线程开启后也创建了唯一的读写线程
        private ClientInputThread inputThread;
        private ClientOutputThread outputThread;

        public ClientThread (Socket socket) {
            inputThread = new ClientInputThread(socket);
            outputThread = new ClientOutputThread(socket);
        }

        //client 线程开启后，读写线程开启
        @Override
        public void run() {
            inputThread.setStart(true);
            outputThread.setStart(true);

            inputThread.start();
            outputThread.start();
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
