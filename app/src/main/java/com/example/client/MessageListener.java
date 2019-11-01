package com.example.client;

import org.json.JSONException;

public interface MessageListener {

    public void getMessage(String msg) throws JSONException;
}
