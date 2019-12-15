package com.example.location;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.bean.Group;
import com.example.bean.SignRecord;
import com.example.client.ClientMessage;
import com.example.client.MessagePostPool;
import com.example.client.MessageType;
import com.example.fragment.CreatedFragment;
import com.example.fragment.JoinFragment;

import com.example.client.ClientMessage;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends BaseActivity implements CreatedFragment.startSignClickListener {
/**
 * 群详情页面
 */

    private boolean isCreate;
    private String groupName;
    private int groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Toolbar toolbar = (Toolbar)findViewById(R.id.group_tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);

        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        isCreate = getIntent().getBooleanExtra("isCreate",false);
        groupName = getIntent().getStringExtra("group_name");
        groupId = getIntent().getIntExtra("group_id",-1);
        actionBar.setTitle(groupName);

    }


     private void replaceFragment(Fragment fragment) {
         FragmentManager fragmentManager = getSupportFragmentManager();
         FragmentTransaction transaction = fragmentManager.beginTransaction();
         transaction.replace(R.id.group_layout,fragment);
         transaction.commit();
     }


    @Override
    public void getMessage(ClientMessage msg) {

    }

    //根据群类型显示不同的toolbar图标
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_toolbar,menu);
        MenuItem memberItem = menu.findItem(R.id.group_member);
        MenuItem meItem = menu.findItem(R.id.group_me);
        if (isCreate) {
            memberItem.setVisible(true);
            meItem.setVisible(false);
//            Bundle bundle = new Bundle();
//            bundle.putString("group_id",String.valueOf(groupId)); //将群id// 传递给fragment
            CreatedFragment fragment = new CreatedFragment();
           // fragment.setArguments(bundle);
            replaceFragment(fragment);
        }else {
            memberItem.setVisible(false);
            meItem.setVisible(true);
            replaceFragment(new JoinFragment());
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.group_member:
                ClientMessage clientMessage = new ClientMessage();
                clientMessage.setMessageType(MessageType.MEMBER_SIGN_RECORD);
                clientMessage.setGroupId(groupId);
                MessagePostPool.sendMessage(clientMessage);

                Intent intent = new Intent(GroupActivity.this,GroupMemberActivity.class);
                startActivity(intent);
                break;

            case R.id.group_me:
                ClientMessage clientMessage1 = new ClientMessage();
                clientMessage1.setMessageType(MessageType.MY_SIGN_RECORD);
                clientMessage1.setGroupId(groupId);
                MessagePostPool.sendMessage(clientMessage1);

                Intent intent1 = new Intent(GroupActivity.this,MyRecordActivity.class);
                startActivity(intent1);
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void startSign(Boolean isClick) {
        if (isClick) {
            Intent intent = new Intent(GroupActivity.this,SetSignInActivity.class);
            intent.putExtra("group_id",groupId);
            startActivity(intent);
        }
    }
}
