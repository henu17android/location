package com.example.location;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import org.json.JSONObject;

import com.example.LocationApp;
import com.example.bean.User;
import com.example.client.Client;
import com.example.client.ClientMessage;
import com.example.client.ClientMessageType;
import com.example.client.MessageListener;

import org.json.JSONException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public SharedPreferences sharedPreferences;
    private Boolean isLogin;
    private EditText userName;
    private EditText password;
    private TextView phoneRegist;
    private TextView forgetPWD;
    private Button loginButton;
    private LocationApp locationApp;
    private Client client;
    private String serverMessage;
    private static final String TAG = "LoginActivity";
    private static final int CONNECTION_SUCCESS = 1;
    private static final int CONNECTION_FAIL = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("SaveSetting",MODE_PRIVATE);
        isLogin = sharedPreferences.getBoolean("isLogin",true);
        if(isLogin){
            //登录的处理
            Log.d("client:id", "LoginActivity "+client);
            initView();
            locationApp = (LocationApp)this.getApplication();
            client = locationApp.getClient();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = new Message();
                    if (client.connection()) {
                        message.what = CONNECTION_SUCCESS;
                    }else {
                        message.what = CONNECTION_FAIL;
                    }
                    handler.sendMessage(message);

                }
            }).start();
        }else {
            Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(mainIntent);
        }

    }


    private void initView(){
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
                Intent intent = new Intent(LoginActivity.this,IdentifyActivity.class);
                startActivity(intent);
                break;

            //TODO 登录成功后，加上 saveIsLogin(false);
            case R.id.login_button :
                 sendLoginMessage(userName.getText().toString(),password.getText().toString());


            default:

        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONNECTION_SUCCESS :
                    Toast.makeText(LoginActivity.this,"服务器连接成功",Toast.LENGTH_SHORT).show();
                    break;

                case CONNECTION_FAIL :
                    Toast.makeText(LoginActivity.this,"服务器连接失败",Toast.LENGTH_SHORT).show();
                    break;

                default:
            }
        }
    };

    private void sendLoginMessage(final String userName, String password) {


        if (userName.length()<=0||password.length()<=0) {
            Toast.makeText(LoginActivity.this,"用户名和密码不能为空",Toast.LENGTH_SHORT).show();
        }else {
            User user = new User(userName,password);
            ClientMessage message = new ClientMessage();
            message.setUser(user);
            message.setMessageType(ClientMessageType.LOGIN);

            ClientMessage clientMessage = new ClientMessage();
            clientMessage.setUser(user);
            clientMessage.setMessageType(ClientMessageType.LOGIN);

            client.getClientOutputThread().setMsg(JSON.toJSONString(clientMessage));
            synchronized (client.getClientOutputThread()){
                client.getClientOutputThread().notify();
            }


            Log.d(TAG, "sendLoginMessage: "+JSON.toJSONString(clientMessage));

            client.getClientInputThread().setmMessageListener(new MessageListener() {
            @Override
            public void getMessage(String msg) throws JSONException {
                Log.d(TAG, "getMessage: "+msg);
                JSONObject jsonObject = new JSONObject(msg);
                int id = jsonObject.getInt("id");
                switch (id) {
                    case -1:
                        Toast.makeText(LoginActivity.this,"账号不存在",Toast.LENGTH_SHORT).show();
                        break;

                    case 0:
                        Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                        break;

                    case 1:
                        Toast.makeText(LoginActivity.this,"密码成功",Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                        startActivity(intent);
//                        finish();

                    default:
                }
            }
        });

        }
//
    }

    private void saveIsLogin(boolean islogin){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin",islogin);
        editor.commit();
    }


}
