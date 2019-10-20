package com.example.location;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.Client;

import java.io.IOException;

public class IdentifyActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText user_tele;
    private EditText code;
    private Button getCode;
    private Button submit;
    private TextView numberError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify);




    }

    private void initView() {
       user_tele = (EditText)findViewById(R.id.user_tele);
       code = (EditText)findViewById(R.id.identify_number);
       getCode = (Button)findViewById(R.id.get_code);
       submit = (Button)findViewById(R.id.confirm_submit);
       numberError = (TextView)findViewById(R.id.number_error);

       getCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_code:
                thread.start();

        }
    }

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            Client client = new Client("192.168.1.174",8093);
            try {
                client.createConnection();
                client.sendMessage("socket");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("identify:","send fail");
            }
        }
    });

}
