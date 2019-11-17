package com.example.location;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.Service.SocketService;
import com.example.bean.User;
import com.example.client.ClientMessage;
import com.example.client.ClientMessageType;
import com.example.client.MessageListener;


import org.json.JSONException;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText user_tele;
    private EditText code;
    private Button getCode;
    private Button submit;
    private TextView numberError;
    private EditText user_pwd;
    private User user;



    private final static String TAG = "RegisterActivity";

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
                //TODO: 发送验证码的方法
                break;

            case R.id.confirm_submit:
                user = new User();
                Log.d(TAG, "onClick: "+user_tele.getText().toString()+user_pwd.getText().toString());
                user.setPhoneNumber(user_tele.getText().toString());
                user.setPassword(user_pwd.getText().toString());
                sendRegister(user);

                break;

            default:
        }
    }

    private void sendRegister(User user) {
        //定义发送消息
        ClientMessage clientMessage  = new ClientMessage();
        clientMessage.setUser(user);
        clientMessage.setMessageType(ClientMessageType.REGISTER);

        String message = JSON.toJSONString(clientMessage);
        sendMessageBinder.sendMessage(message);  //binder 向服务器发送消息

    }


    @Override
    protected void initService() {
        Intent bindIntent = new Intent(RegisterActivity.this,SocketService.class);
        bindService(bindIntent,connection,BIND_AUTO_CREATE);
    }

    @Override
    public void getMessage(String msg)  {
        Log.d(TAG, "getMessage: ServerMessage"+msg);
        int id = 2;
        String messageType = null;
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(msg);
            id = jsonObject.getInt("id");
            messageType = jsonObject.getString("messageType");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "getMessage: "+e.getMessage());
        }

        if (messageType!=null&&messageType.endsWith("REGISTER_RESULT")) {
            switch (id) {
                case 0:
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    break;

                case -1:
                    Toast.makeText(RegisterActivity.this, "账户已存在", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;

            }
        }

    }
}


