package com.example.location;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.Service.SocketService;
import com.example.bean.Group;
import com.example.client.ClientMessage;
import com.example.client.MessageType;
import com.example.util.DataUtil;

import org.json.JSONException;

import java.util.Timer;
import java.util.TimerTask;


public class CreateGroupActivity extends BaseActivity {

    private EditText nameEdt;
    private Group createGroup;

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
                if (groupName.length()>0) {
                    createGroup = new Group(groupName);
                    ClientMessage clientMessage = new ClientMessage();
                    clientMessage.setMessageType(MessageType.CREATE_GROUP);
//                    createGroup.setAdminId(DataUtil.USER_NUMBER);
                    createGroup.setAdminId("123456789");
                    clientMessage.setGroup(createGroup);
                    sendMessageBinder.sendMessage(JSON.toJSONString(clientMessage));
                    Toast.makeText(CreateGroupActivity.this,"创建信息已发送",Toast.LENGTH_SHORT).show();
                }

        }
        return true;
    }

    @Override
    protected void initService() {
        Intent bindIntent = new Intent(CreateGroupActivity.this,SocketService.class);
        bindService(bindIntent,connection,BIND_AUTO_CREATE);
    }

    @Override
    public void getMessage(ClientMessage msg){
        if (msg.isSuccess()) {
            Toast.makeText(CreateGroupActivity.this,"创建群成功",Toast.LENGTH_LONG).show();
            final Intent intent = new Intent(CreateGroupActivity.this,MainActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("group_toolbar",createGroup);
            intent.putExtra("group_toolbar",createGroup);
            startActivity(intent);

            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    startActivity(intent);
                }
            };
            timer.schedule(timerTask,2);

        }
    }
}
