package com.example;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.example.Service.SocketService;
import com.example.client.Client;

public class LocationApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("locationapp", "onCreate: ");

        Intent intent = new Intent(this,SocketService.class);
        startService(intent);

    }

}
