package com.example;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.example.Service.SocketService;
import com.example.client.Client;
import com.example.util.ActivityUtil;

public class LocationApp extends Application {




    private ActivityUtil activityUtil;
    @Override
    public void onCreate() {


//        client = new Client("106.52.109.122",8098);
        super.onCreate();
        activityUtil = ActivityUtil.getScreenManager();

        //百度地图的使用
        SDKInitializer.initialize(this);
        Intent intent = new Intent(this,SocketService.class);
        if(Build.VERSION.SDK_INT>26){
            startForegroundService(intent);
        }else{
            startService(intent);
        }
    }

    public ActivityUtil getActivityUtil(){
        return activityUtil;
    }
    public void setActivityUtil(ActivityUtil activityUtil){
        this.activityUtil = activityUtil;
    }

}
