package com.example;

import android.app.Application;

import com.example.client.Client;

public class LocationApp extends Application {

     private String ip = "192.168.137.1";
     private int port = 8096;
     private Client client;       //全局定义client
     private boolean isClientStart;

     @Override
     public void onCreate() {
          super.onCreate();
          client = new Client(ip,port);

     }


     public Client getClient() {
          return client;
     }

     public void setClient(Client client) {
          this.client = client;
     }

     public boolean isClientStart() {
          return isClientStart;
     }

     public void setClientStart(boolean clientStart) {
          isClientStart = clientStart;
     }
}
