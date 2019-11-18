package com.example.location;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.LocationApp;
import com.example.Service.SocketService;
import com.example.client.Client;
import com.example.bean.User;
import com.example.client.ClientMessage;
import com.example.client.ClientMessageType;


import org.json.JSONException;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText user_tele;
    private EditText code;
    private Button getCode;
    private Button submit;
    private TextView numberError;
    private EditText user_pwd;
    private EditText user_pwd_twice;
    private User user;
    private Client client;
    private LocationApp locationApp;
    private Context context;
    private boolean codeIsRight;
    private boolean codeIsSend;
    int i = 60;

    private final static String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify);

        initView();
        SMSSDK.registerEventHandler(eh);
    }

    private void initView() {
        user_tele = (EditText) findViewById(R.id.user_tele);
        code = (EditText) findViewById(R.id.identify_number);
        getCode = (Button) findViewById(R.id.get_code);
        submit = (Button) findViewById(R.id.confirm_submit);
        numberError = (TextView) findViewById(R.id.number_error);
        user_pwd = (EditText) findViewById(R.id.user_password);
        user_pwd_twice = (EditText)findViewById(R.id.user_password_twice);
        context = getApplicationContext();
        getCode.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String user_phone = user_tele.getText().toString();
        String strCode = code.getText().toString();
        switch (v.getId()) {
            case R.id.get_code:
                if(!judgePhoneNums(user_phone)){
                    return;
                }
                SMSSDK.getVerificationCode("86",user_phone);
                getCode.setClickable(false);
                getCode.setText("重新发送("+i+")");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(;i>0;i--){
                            handler.sendEmptyMessage(-9);
                            if(i<=0)
                                break;
                            try {
                                Thread.sleep(1000);
                            }catch (InterruptedException ie){
                                ie.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();
                break;

            case R.id.confirm_submit:
                SMSSDK.submitVerificationCode("86",user_phone,strCode);
                user = new User(user_tele.getText().toString(), user_pwd.getText().toString());
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

        String message = JSONObject.toJSONString(clientMessage);
        sendMessageBinder.sendMessage(message);
    }


    @Override
    protected void initService() {
        Intent bindIntent = new Intent(RegisterActivity.this,SocketService.class);
        bindService(bindIntent,connection,BIND_AUTO_CREATE);
    }



    @Override
    public void getMessage(String msg) throws JSONException {
        int id = 2;
        String messageType = null;
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(msg);
            id = jsonObject.getInt("id");
            messageType = jsonObject.getString("messageType");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "getMessage: " + e.getMessage());
        }

        if (messageType != null && messageType.endsWith("REGISTER_RESULT")) {
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


        @Override
        protected void onDestroy(){
            super.onDestroy();
            SMSSDK.unregisterEventHandler(eh);
        }


    EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == -9) {
                    getCode.setText("重新发送(" + i + ")");
                } else if (msg.what == -8) {
                    getCode.setText("获取验证码");
                    getCode.setClickable(true);
                    i = 60;
                } else {
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            Toast.makeText(context, "提交验证码成功",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "验证码错误",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            Toast.makeText(context, "正在获取验证码",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "验证码获取失败",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        };

        /**
         * 判断手机号码是否合理
         */
        private boolean judgePhoneNums (String phoneNums){
            if (isMatchLength(phoneNums, 11) && isMobileNO(phoneNums)) {
                return true;
            }
            Toast.makeText(this, "手机号输入有误！", Toast.LENGTH_SHORT).show();
            return false;
        }
        /**
         * 判断一个字符串的位数
         */
        private static boolean isMatchLength (String str,int length){
            if (str.isEmpty())
                return false;
            else
                return str.length() == length ? true : false;
        }
        /**
         * 验证手机格式
         */
        private static boolean isMobileNO (String moblieNums){
            String telRegex = "[1][0-9]\\d{9}";
            if (TextUtils.isEmpty(moblieNums))
                return false;
            else
                return moblieNums.matches(telRegex);
        }

}


