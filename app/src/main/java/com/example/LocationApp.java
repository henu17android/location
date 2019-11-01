package com.example;

import android.app.Application;
import android.util.Log;

import com.example.client.Client;

public class LocationApp extends Application {

    private Client client;
    private boolean isClientStart;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("locationapp", "onCreate: ");

        //qmn 自测用
//        client = new Client("192.168.1.177",8096);
        client = new Client("106.52.109.122",8098);
        Log.d("client:id", "onCreate: "+client);

    }


    public Client getClient() {
        return client;
    }

    public boolean isClientStart() {
        return isClientStart;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setClientStart(boolean clientStart) {
        isClientStart = clientStart;
    }
}
