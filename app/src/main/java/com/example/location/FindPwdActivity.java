package com.example.location;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.client.ClientMessage;

public class FindPwdActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd);
    }


    @Override
    public void getMessage(ClientMessage msg){}
}
