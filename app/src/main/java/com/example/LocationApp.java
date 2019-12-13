package com.example;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.example.Service.SocketService;

public class LocationApp extends Application {




    @Override
    public void onCreate() {


//        client = new Client("106.52.109.122",8098);
        super.onCreate();
        Log.d("locationapp", "onCreate: ");
        //百度地图的使用
        SDKInitializer.initialize(this);
        Intent intent = new Intent(this,SocketService.class);
        startService(intent);
    }

}
