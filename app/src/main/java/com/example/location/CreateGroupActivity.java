package com.example.location;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.example.LocationApp;
import com.example.Service.SocketService;
import com.example.bean.Group;
import com.example.client.Client;
import com.example.client.ClientMessage;
import com.example.client.ClientMessageType;
import com.example.client.MessageListener;

import org.json.JSONException;
import org.json.JSONObject;


public class CreateGroupActivity extends BaseActivity {

    private EditText nameEdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        nameEdt = (EditText)findViewById(R.id.group_name);
        Toolbar toolbar = (Toolbar)findViewById(R.id.create_group_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_group_toobar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_group_finish :
                String groupName = nameEdt.getText().toString();
                sendMessage(groupName);




        }
        return true;
    }

    private void sendMessage(String groupName) {
        Group group = new Group();
        group.setGroupName(groupName);
        group.setAdminId(33);
        ClientMessage clientMessage = new ClientMessage();
        clientMessage.setMessageType(ClientMessageType.CREATE_GROUP);
        clientMessage.getUser().setId(33);


    }


    @Override
    protected void initService() {
        Intent bindIntent = new Intent(CreateGroupActivity.this,SocketService.class);
        bindService(bindIntent,connection,BIND_AUTO_CREATE);
    }

    @Override
    public void getMessage(String msg) throws JSONException {

    }
}
