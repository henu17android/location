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

import com.example.client.ClientMessage;
import com.example.fragment.CreatedFragment;
import com.example.fragment.JoinFragment;

import com.example.client.ClientMessage;

public class GroupActivity extends BaseActivity {
/**
 * 群详情页面
 */
public class GroupActivity extends AppCompatActivity {

    private boolean isCreate;
    private String groupName;
    private String groupId;

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
        groupId = getIntent().getStringExtra("group_id");

        toolbar.setTitle(groupName);
        Log.d("GroupActivity", "onCreate: "+isCreate);


    }


     private void replaceFragment(Fragment fragment) {
         FragmentManager fragmentManager = getSupportFragmentManager();
         FragmentTransaction transaction = fragmentManager.beginTransaction();
         transaction.replace(R.id.group_layout,fragment);
         transaction.commit();
     }


    @Override
    public void getMessage(ClientMessage msg) {

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_toolbar,menu);
        MenuItem memberItem = menu.findItem(R.id.group_member);
        if (isCreate) {
            memberItem.setVisible(true);
            Bundle bundle = new Bundle();
            bundle.putString("group_id",groupId); //将群id// 传递给fragment
            CreatedFragment fragment = new CreatedFragment();
            fragment.setArguments(bundle);
            replaceFragment(fragment);
        }else {
            memberItem.setVisible(false);
            replaceFragment(new JoinFragment());
        }
        return true;
    }
}
