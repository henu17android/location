package com.example;

import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void createConnection () throws IOException {
        try {
            socket = new Socket(ip,port);
            socket.setSoTimeout(20000);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket!=null) {
                socket.close();
            }
        }

    }

    public synchronized void sendMessage(String sendMessage) throws IOException {
        try {
            if (outputStream == null) {
                outputStream = new DataOutputStream(socket.getOutputStream());
            }

            byte[] bytes = sendMessage.getBytes();
            outputStream.write(bytes);
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (outputStream!=null) {
                outputStream.close();
            }
        }


    }

//    public synchronized String getMessage(int readSize) throws IOException {
//        try {
//            if (inputStream == null) {
//                inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
//            }
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            if (inputStream !=null) {
//                if (readSize!= -1 && readSize >0) {
//                    for (int i = 0;i<readSize;i++) {
//                        int read = inputStream.read();
//                        if (read == -1) {
//                            break;
//                        }else {
//                            byteArrayOutputStream.write(read);
//                        }
//            }
//
//
//
//                }
//            }
//            else {
//                while (true) {
//                    int read = inputStream.read();
//                    if (read <=0) {
//                        break;
//                    }else {
//                        byteArrayOutputStream.write(read);
//                    }
//                }
//            }
//
//        }



}
