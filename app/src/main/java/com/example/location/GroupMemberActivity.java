package com.example.location;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.adapter.GroupMemberAdapter;
import com.example.adapter.GroupMemberDetailsAdapter;
import com.example.bean.SignRecord;
import com.example.bean.User;
import com.example.client.ClientMessage;
import com.example.client.MessagePostPool;
import com.example.client.MessageType;
import com.lvleo.dataloadinglayout.DataLoadingLayout;

import java.util.ArrayList;
import java.util.List;

public class GroupMemberActivity extends BaseActivity {

    private DataLoadingLayout dataLoadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);
        initToolbar();
        dataLoadingLayout = (DataLoadingLayout)findViewById(R.id.loading_member);
        dataLoadingLayout.setVisibility(View.VISIBLE);

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
    public void getMessage(ClientMessage msg){
        switch (msg.getMessageType()) {
            case MEMBER_SIGN_RECORD:
                RecyclerView recyclerView = (RecyclerView)findViewById(R.id.group_member_list);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                GroupMemberDetailsAdapter adapter = new GroupMemberDetailsAdapter(msg.getMemberList(), this);
                recyclerView.setAdapter(adapter);
                dataLoadingLayout.setVisibility(View.GONE);

        }

    }


}
