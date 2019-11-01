//package com.example.Service;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//import android.util.Log;
//import android.webkit.ClientCertRequest;
//
//import com.example.LocationApp;
//import com.example.client.Client;
//import com.example.client.ClientInputThread;
//import com.example.client.MessageListener;
//
//public class GetMsgService extends Service {
//
//    private static final int MSG = 0x001;
//    private LocationApp locationApp;
//    private Client client;
//    private boolean isStart = false;
//    private static final String TAG = "GetMsgService";
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        locationApp = (LocationApp)this.getApplication();
//        client = locationApp.getClient();
//    }
//
//    @Override
//    public int onStartCommand(final Intent intent, int flags, int startId) {
//        isStart = client.connection();
//        locationApp.setClientStart(isStart);
//        Log.d(TAG, "onStartCommand: Service  "+ isStart);
//
//        if (isStart) {
//            ClientInputThread inputThread = client.getClientInputThread();
//            inputThread.setmMessageListener(new MessageListener() {
//                @Override
//                public void getMessage(String msg) {
//                    Intent sendMsg = new Intent();
//                    sendMsg.setAction("com.location.receiver");
//                    sendBroadcast(sendMsg);
//                }
//            });
//        }
//
//        return super.onStartCommand(intent, flags, startId);
//
//
//
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        client.getClientInputThread().setStart(false);
//        client.getClientOutputThread().setStart(false);
//    }
//}
