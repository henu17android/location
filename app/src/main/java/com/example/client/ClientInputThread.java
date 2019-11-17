package com.example.client;

import android.util.Log;

import org.json.JSONException;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * client 读操作线程类
 * @author qmn
 */
public class ClientInputThread extends Thread {

    private Socket socket;
    private DataInputStream inputStream;
    private boolean isStart = true;
    private MessageListener mMessageListener;
    private final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    private InputStreamReader reader;


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
//
//          //qmn 自测用
//            char[] buffer = new char[1024];
//            while (isStart) {
//                reader = new InputStreamReader(inputStream,"UTF-8");
//                int readSize = reader.read(buffer);
//                while (readSize!=-1) {
//                    String msg = new String(buffer,0,readSize);
//                    if (msg.charAt(msg.length()-1)=='}') {
//                        Log.d(TAG, "run: "+readSize+"    "+msg);
//                        break;
//                    }
//
//                    mMessageListener.getMessage(msg);
//
//                }
//
//            }


            //与服务器测试用
            byte[] buffer = new byte[1024];
            while (isStart) {

                int readSize = inputStream.read(buffer);
                while (readSize!=-1) {
                    String msg = new String(buffer, 0,readSize,UTF8_CHARSET);
                    if (msg.charAt(msg.length()-1)=='}') {
                        Log.d(TAG, "run: "+readSize+"    "+msg);
                        break;
                    }
                    mMessageListener.getMessage(msg);
                }

            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
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
