package com.example.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.LocationApp;
import com.example.client.ClientInputThread;
import com.example.client.ClientMessage;
import com.example.client.MessageListener;
import com.example.client.MessagePostPool;
import com.example.util.DataUtil;

import org.json.JSONException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author qmn
 */
public class SocketService extends Service {

    private Thread connectThread;  //连接线程
    private Socket socket;
    private Handler mHandler = new Handler();
    private TimerTask beatTask;
    private Timer timer = new Timer();
    long keepAliveDelay = 30000;
    private static final String TAG = "SocketService";
    private DataOutputStream dataOutputStream;
    private ClientInputThread clientInputThread;


    public SocketService() {

    }


    @Override
    public void onCreate() {
        super.onCreate();
        initSocket();
    }




    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return null;
    }


    private void initSocket() {
       if (socket == null && connectThread == null) {

           connectThread = new Thread(new Runnable() {
               @Override
               public void run() {
                   Log.d(TAG, "run: initSocket");
                   try {
                       socket = new Socket("192.168.1.174",8096);
                       toastMessage("已连接到服务器");
                       dataOutputStream = new DataOutputStream(socket.getOutputStream());
                       MessagePostPool.outputStream = dataOutputStream;
                       readMessage();
                   } catch (IOException e) {
                       e.printStackTrace();
                       toastMessage("连接失败，正在尝试重连");
                   }


               }
           });
           connectThread.start();
       }


    }


    //toast 弹出消息
    private void toastMessage (final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void sendHeartBeat() {
        if (timer == null) {
            timer = new Timer();
        }
        if (beatTask == null) {
            beatTask = new TimerTask() {
                @Override
                public void run() {
                    Log.d(TAG, "run: beatheart");
                    //开始发送心跳包
                /*    synchronized (client.getClientOutputThread()) {
                        client.getClientOutputThread().setMsg("{}");
                        client.getClientOutputThread().notify();
                    }

                    if (!client.getClientOutputThread().isStart()) {
                        toastMessage("连接断开，正在重连");
                        releaseSocket();
                    }*/

                }
            };

        }
        timer.schedule(beatTask,0,keepAliveDelay);
    }

    //释放资源并重新连接
    private void releaseSocket() {
        if (beatTask!=null) {
            beatTask.cancel();
            beatTask = null;
        }
        if (timer!=null) {
            timer.purge();
            timer.cancel();
            timer = null;
        }

        if (dataOutputStream!=null) {
            try {
                dataOutputStream.close();
                clientInputThread.getInputStream().close();
                MessagePostPool.outputStream = null;
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        initSocket();


    }


    //接收服务端消息并通过广播发送出去
    private void readMessage() {
        clientInputThread = new ClientInputThread(socket);
        clientInputThread.setStart(true);
        clientInputThread.start();
        clientInputThread.setmMessageListener(new MessageListener() {
            @Override
            public void getMessage(String msg) throws JSONException {
                //转为客户端消息类
                //  String jsonMsg = msg.replace("\\","");
                Log.d("message", "getMessage: "+msg);
                if (msg.length()>0) {
                    ClientMessage clientMessage = JSONObject.parseObject(msg, ClientMessage.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("message",clientMessage);
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(DataUtil.ACTION);
                    sendIntent.putExtra("object", msg);
                    sendBroadcast(sendIntent);
                }
            }
        });
    }

}
