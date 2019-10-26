package com.example.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.LocationApp;
import com.example.client.Client;

public class SocketService extends Service {
    private static final int MSG = 0x001;
    private LocationApp locationApp;
    private Client client;
    private boolean isStart = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationApp = (LocationApp)this.getApplication();
        client = locationApp.getClient();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
