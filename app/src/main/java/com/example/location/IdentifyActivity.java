package com.example.location;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.LocationApp;
import com.example.client.Client;
import com.example.bean.User;
import com.example.client.ClientMessage;
import com.example.client.ClientMessageType;
import com.example.client.ClientOutputThread;
import com.example.client.MessageListener;


import org.json.JSONException;

import java.io.IOException;

public class IdentifyActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText user_tele;
    private EditText code;
    private Button getCode;
    private Button submit;
    private TextView numberError;
    private EditText user_pwd;
    private User user;
    private Client client;
    private LocationApp locationApp;


    private final static String TAG = "IdentifyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify);
        locationApp = (LocationApp)this.getApplication();
        client = locationApp.getClient();
        Log.d("client:id", "IdentifyActivity"+client);
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
                user = new User("123", "123");
                Log.d(TAG, "onClick: "+user_tele.getText().toString()+user_pwd.getText().toString());
                sendRegister(user);

                break;

            default:
        }
    }

    private void sendRegister(User user) {
        ClientMessage clientMessage  = new ClientMessage();
        clientMessage.setUser(user);
        clientMessage.setMessageType(ClientMessageType.REGISTER);

        final ClientOutputThread out = client.getClientOutputThread();
        out.setMsg(JSON.toJSONString(clientMessage));
        synchronized (out) {
            out.notify();
        }





        client.getClientInputThread().setmMessageListener(new MessageListener() {
            @Override
            public void getMessage(String msg) throws JSONException {
                Log.d(TAG, "getMessage: "+msg);
            }
        });

    }


}


