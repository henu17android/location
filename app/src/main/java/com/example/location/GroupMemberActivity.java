package com.example.location;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.adapter.GroupMemberAdapter;
import com.example.adapter.GroupMemberDetailsAdapter;
import com.example.bean.SignRecord;
import com.example.bean.User;
import com.example.client.ClientMessage;

import java.util.ArrayList;
import java.util.List;

public class GroupMemberActivity extends BaseActivity {


    private List<User> mUsers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);
        initToolbar();
        initData();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.group_member_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GroupMemberDetailsAdapter adapter = new GroupMemberDetailsAdapter(mUsers, this);
        recyclerView.setAdapter(adapter);
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
    public void getMessage(ClientMessage msg){}



    private void initData() {
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUserName("用户"+(i+1));
            List<SignRecord> signRecords = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                SignRecord signRecord = new SignRecord();
                signRecord.setSignTime("2019-12-1"+(i+1));
                signRecord.setSignResult("签到成功");
                signRecords.add(signRecord);
            }
            user.setSignRecord(signRecords);
            mUsers.add(user);
        }
    }
}
