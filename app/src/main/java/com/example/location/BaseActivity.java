//package com.example.location;
//
//import android.app.Activity;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//
//public abstract class BaseActivity extends Activity {
//    static String message;
//    private static String TAG = "BaseActivity";
//
//    private BroadcastReceiver MsgReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            message = intent.getStringExtra("message");
//            if (message != null) {
//                getMessage(message);
//                getMsg();
//                Log.d(TAG, "onReceive: "+message);
//            }else {
//                close();
//            }
//
//
//        }
//    };
//
//
//    public abstract void getMessage(String msg);
//
//    public static String getMsg() {
//        return message;
//    }
//    public void close() {
//        Intent intent = new Intent();
//        intent.setAction("com.location.receiver");
//        sendBroadcast(intent);
//        finish();a
//    }
//
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        unregisterReceiver(MsgReceiver);
//    }
//}
