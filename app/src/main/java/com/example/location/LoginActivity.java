package com.example.location;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText userName;
    private EditText password;
    private TextView phoneRegist;
    private TextView forgetPWD;
    private Button loginButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();


    }


    private void initView(){
        userName = findViewById(R.id.user_name);
        password = findViewById(R.id.user_password);
        phoneRegist = findViewById(R.id.phone_register);
        loginButton = findViewById(R.id.login_button);
        forgetPWD = findViewById(R.id.forget_pwd);

        phoneRegist.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phone_register:
                Intent intent = new Intent(LoginActivity.this,IdentifyActivity.class);
                startActivity(intent);
                break;


            default:

        }
    }



}
