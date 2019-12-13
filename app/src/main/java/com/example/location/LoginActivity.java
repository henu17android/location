package com.example.location;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.client.ClientMessage;
import com.example.client.MessagePostPool;
import com.example.client.MessageType;
import com.example.util.DataUtil;

public class LoginActivity extends BaseActivity implements View.OnClickListener {


    public SharedPreferences sharedPreferences;
    private Boolean isLogin;
    private EditText userName;
    private EditText password;
    private TextView phoneRegist;
    private TextView forgetPWD;
    private Button loginButton;
    private LocationApp locationApp;
    private String serverMessage;
    private static final String TAG = "LoginActivity";
    private static final int CONNECTION_SUCCESS = 1;
    private static final int CONNECTION_FAIL = 0;
    private String phoneNumber;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("SaveSetting",MODE_PRIVATE);
        isLogin = sharedPreferences.getBoolean("isLogin",false);

//        if(isLogin){
//            //登录的处理
//            Log.d("client:id", "LoginActivity "+client);
//            Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
//            String number = sharedPreferences.getString("user_phone",null);
//            DataUtil.USER_NUMBER = number;
//
//            startActivity(mainIntent);
//        }else {
            initView();
//        }

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
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;

            //TODO 登录成功后，加上 saveIsLogin(false);
            case R.id.login_button :
                phoneNumber = userName.getText().toString();
                sendLoginMessage(phoneNumber,password.getText().toString());


            default:

        }
    }


    private void sendLoginMessage(String userName,String password) {
        if (userName.length()<=0||password.length()<=0) {
            Toast.makeText(LoginActivity.this,"用户名和密码不能为空",Toast.LENGTH_SHORT).show();
        }else {
            User user = new User(userName,password);
            ClientMessage clientMessage = new ClientMessage();
            clientMessage.setUser(user);
            clientMessage.setMessageType(MessageType.LOGIN);
            String jsonString = JSON.toJSONString(clientMessage);
           //sendMessageBinder.sendMessage(jsonString);  //通过binder 发送数据
            MessagePostPool.sendMessage(clientMessage);
            Log.d(TAG, "sendLoginMessage: "+JSON.toJSONString(clientMessage));

        }
    }



    /**
     * 接收消息并做处理
     * @param msg
     */
    @Override
    public void getMessage(ClientMessage msg)  {
        if (msg.getMessageType().equals(MessageType.LOGIN)) {
            switch (msg.getStateCode()) {
                case 0:
                    Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    break;

                case -1:
                    Toast.makeText(LoginActivity.this, "账户不存在", Toast.LENGTH_SHORT).show();
                    break;

                case 1:
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    saveIsLogin(true,userName.getText().toString());  //保存登录状态
                    DataUtil.USER_NUMBER = phoneNumber;
                    startActivity(intent);
                    break;
                default:
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }


        }

        }


    private void saveIsLogin(boolean islogin,String phone){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin",islogin);
        editor.putString("user_phone",phone);
        editor.apply();
    }


}
