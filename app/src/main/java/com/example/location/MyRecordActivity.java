package com.example.location;

import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.adapter.MyRecordAdapter;
import com.example.bean.SignRecord;
import com.example.client.ClientMessage;
import com.example.client.MessagePostPool;
import com.example.client.MessageType;
import com.lvleo.dataloadinglayout.DataLoadingLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人签到记录页面
 */
public class MyRecordActivity extends BaseActivity {

    DataLoadingLayout dataLoadingLayout;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_record);

      dataLoadingLayout = findViewById(R.id.dataLoading_layout);
      dataLoadingLayout.setVisibility(View.VISIBLE);


        listView = (ListView)findViewById(R.id.my_record_list);

    }


    @Override
    public void getMessage(ClientMessage msg) {
        switch (msg.getMessageType()) {
            case MY_SIGN_RECORD:
                MyRecordAdapter recordAdapter = new MyRecordAdapter(MyRecordActivity.this,
                        R.layout.my_record_item,msg.getUser().getSignRecord());
                listView.setAdapter(recordAdapter);
                dataLoadingLayout.setVisibility(View.GONE);
                break;
        }
    }
}
