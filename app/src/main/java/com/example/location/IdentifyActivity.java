package com.example.location;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.Client;
import com.example.bean.User;
import com.example.util.JsonUtil;
import com.example.util.ValidateUtil;


import java.io.IOException;

public class IdentifyActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText user_tele;
    private EditText code;
    private Button getCode;
    private Button submit;
    private TextView numberError;
    private EditText user_pwd;
    private User user;

    private final static String TAG = "IdentifyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify);

        initView();


    }

    private void initView() {
        user_tele = (EditText) findViewById(R.id.user_tele);
        code = (EditText) findViewById(R.id.identify_number);
        getCode = (Button) findViewById(R.id.get_code);
        submit = (Button) findViewById(R.id.confirm_submit);
        numberError = (TextView) findViewById(R.id.number_error);
        user_pwd = (EditText) findViewById(R.id.user_password);

        getCode.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_code:
                break;

            case R.id.confirm_submit:
                user = new User(user_tele.getText().toString(), user_pwd.getText().toString());
                Log.d(TAG, "onClick: "+user_tele.getText().toString()+user_pwd.getText().toString());
                thread.start();
                break;

            default:
        }
    }

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            Client client = new Client("192.168.1.174", 8096);
            Log.d(TAG, "run: "+user.getPassword()+user.getPhoneNumber());
            String jsonString = JSON.toJSONString(user);
            user.setId(8);
            try {
                client.createConnection();
                client.sendMessage(jsonString);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "run: connection fail");
            }

        }
    });
}


