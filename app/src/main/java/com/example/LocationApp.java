package com.example;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.example.Service.SocketService;
import com.example.client.Client;

public class LocationApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("locationapp", "onCreate: ");
        //百度地图的使用
        SDKInitializer.initialize(this);
        Intent intent = new Intent(this,SocketService.class);
        startService(intent);
    }

}
