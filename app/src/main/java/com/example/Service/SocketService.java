package com.example.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.LocationApp;
import com.example.client.Client;
import com.example.client.ClientInputThread;
import com.example.client.MessageListener;

import org.json.JSONException;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 */
public class SocketService extends Service {

    private Client client;
    private Thread connectThread;  //连接线程
    private Handler mHandler = new Handler();
    private TimerTask beatTask;
    private Timer timer = new Timer();
    long keepAliveDelay = 30000;
    private static final String TAG = "SocketService";
    private SocketService.SendMessageBinder sendMessageBinder = new SendMessageBinder();

    public SocketService() {

    }


    @Override
    public void onCreate() {
        super.onCreate();
        //qmn 自测用
        client = new Client("192.168.1.174",8096);
//        client = new Client("106.52.109.122",8098);
        Log.d("client:id", "onCreate: "+client);
        initSocket();
    }




    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return sendMessageBinder;
    }


    private void initSocket() {
       if (client.socket == null && connectThread == null) {

           connectThread = new Thread(new Runnable() {
               @Override
               public void run() {
                   Log.d(TAG, "run: initSocket");
                  if (client.connection()) {
                      toastMessage("已连接到服务器");
                      sendMessageBinder.readMessage();
//                      sendHeartBeat();
                  }else {
                      toastMessage("连接失败,正在重连");
//                      releaseSocket();
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
                    synchronized (client.getClientOutputThread()) {
                        client.getClientOutputThread().setMsg("{}");
                        client.getClientOutputThread().notify();
                    }

                    if (!client.getClientOutputThread().isStart()) {
                        toastMessage("连接断开，正在重连");
                        releaseSocket();
                    }

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
        if (client.getClientOutputThread().outputStream!=null) {
            try {
                client.getClientOutputThread().outputStream.close();
                client.getSocket().close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "releaseSocket: "+e.getMessage());
            }
        }
        initSocket();


    }

   public class SendMessageBinder extends Binder {

        //发送信息到服务端
        public void sendMessage(String message) {
            synchronized (client.getClientOutputThread()) {
                client.getClientOutputThread().setMsg(message);
                client.getClientOutputThread().notify();
            }

        }

        //接收消息并传递到BaseActivity的广播接收器
        private void readMessage() {
            client.getClientInputThread().setmMessageListener(new MessageListener() {
                @Override
                public void getMessage(String msg) throws JSONException {
                    Intent intent = new Intent();
                    intent.setAction("com.location.serverMessage");
                    intent.putExtra("ServerMessage",msg);
                    sendBroadcast(intent);
                }
            });
        }

    }

}
