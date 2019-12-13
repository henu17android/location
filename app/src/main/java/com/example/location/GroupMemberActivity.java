package com.example.location;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.adapter.GroupMemberAdapter;
import com.example.bean.User;

import java.util.ArrayList;
import java.util.List;

public class GroupMemberActivity extends AppCompatActivity {
public class GroupMemberActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);
        initToolbar();

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.group_member_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<User> users = new ArrayList<>();
        for (int i =0;i<10;i++) {
            User user = new User();
            user.setUserName("name"+i);
            users.add(user);
        }


        GroupMemberAdapter groupMemberAdapter = new GroupMemberAdapter(GroupMemberActivity.this,users);
        recyclerView.setAdapter(groupMemberAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            }
    }



    @Override
    public void initService() { }
    @Override
    public void getMessage(ClientMessage msg){}
}
