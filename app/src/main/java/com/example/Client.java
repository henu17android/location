package com.example;

import android.util.Log;
import android.widget.Toast;

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

public class Client {

    private String ip = null;
    private int port = 0;
    private Socket socket = null;
    private DataInputStream inputStream = null;
    private DataOutputStream outputStream = null;
    private static final String TAG = "Client";
    private boolean isStartReceiveMsg;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void createConnection() throws IOException {
        try {
            socket = new Socket(ip, port);
            socket.setSoTimeout(20000);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String sendMessage) throws IOException {

//        outputStream = new DataOutputStream(socket.getOutputStream());
//        outputStream.writeBytes(sendMessage);


        BufferedWriter out = null;
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

//            byte[] bytes = sendMessage.getBytes();
//            outputStream.write(bytes);
//            outputStream.writeUTF(sendMessage);

        out.write(sendMessage);
        Log.d(TAG, "sendMessage: " + sendMessage + out.toString());
        out.flush();

        out.close();
    }


    public synchronized String getMessage () throws IOException {
        isStartReceiveMsg = true;
        try {
            if (isStartReceiveMsg) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                if (reader.ready()) {
                    reader.readLine();
                    String text = "";
                    while ((text = reader.readLine())!=null) {
                        return text;
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

       return null;
    }


}
