package com.example.location;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.client.ClientMessage;

public class GroupActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
    }

    @Override
    protected void initService() {

    }

    @Override
    public void getMessage(ClientMessage msg) {

    }
}
