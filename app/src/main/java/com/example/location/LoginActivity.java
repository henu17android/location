package com.example.location;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import org.json.JSONObject;

import com.example.LocationApp;
import com.example.Service.SocketService;
import com.example.bean.User;
import com.example.client.Client;
import com.example.client.ClientMessage;
import com.example.client.ClientMessageType;
import com.example.client.MessageListener;

import org.json.JSONException;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText userName;
    private EditText password;
    private TextView phoneRegist;
    private TextView forgetPWD;
    private Button loginButton;
    private LocationApp locationApp;
    private Client client;
    private String serverMessage;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();


    }


    private void initView() {
        userName = findViewById(R.id.user_name);
        password = findViewById(R.id.user_password);
        phoneRegist = findViewById(R.id.phone_register);
        loginButton = findViewById(R.id.login_button);
        forgetPWD = findViewById(R.id.forget_pwd);

        phoneRegist.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        forgetPWD.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phone_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;

            case R.id.login_button:
                sendLoginMessage(userName.getText().toString(), password.getText().toString());


            default:

        }
    }


    private void sendLoginMessage(String userName, String password) {
        if (userName.length() <= 0 || password.length() <= 0) {
            Toast.makeText(LoginActivity.this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
        } else {
            User user = new User(userName, password);
            ClientMessage message = new ClientMessage();
            message.setUser(user);
            message.setMessageType(ClientMessageType.LOGIN);

            ClientMessage clientMessage = new ClientMessage();
            clientMessage.setUser(user);
            clientMessage.setMessageType(ClientMessageType.LOGIN);
            String jsonString = JSON.toJSONString(clientMessage);
            sendMessageBinder.sendMessage(jsonString);  //通过binder 发送数据


            Log.d(TAG, "sendLoginMessage: " + JSON.toJSONString(message));

        }
    }


    @Override
    public void initService() {
        Intent bindIntent = new Intent(LoginActivity.this, SocketService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
    }


    /**
     * 接收消息并做处理
     * @param msg
     */
    @Override
    public void getMessage(String msg)  {
        JSONObject jsonObject = null;
        String messageType = null;
        int stateCode = 2;
        Log.d(TAG, "getMessage: msg"+msg);

        try {
            jsonObject = new JSONObject(msg);
            stateCode = jsonObject.getInt("stateCode");
            messageType = jsonObject.getString("messageType");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "getMessage: " + e.getMessage());
        }

        if (messageType!=null&&messageType.endsWith("LOGIN_RESULT")) {
            switch (stateCode) {
                case 0:
                    Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    break;

                case -1:
                    Toast.makeText(LoginActivity.this, "账户不存在", Toast.LENGTH_SHORT).show();
                    break;

                case 1:
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;

                default:
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }


        }

    }
}
