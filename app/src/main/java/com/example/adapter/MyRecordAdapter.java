package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bean.SignRecord;
import com.example.location.R;
import com.example.util.TimeTransform;

import java.util.List;

public class MyRecordAdapter extends ArrayAdapter<SignRecord> {

    private List<SignRecord> signRecordList;
    private int resourceId;

    public MyRecordAdapter(Context context, int resource,List<SignRecord> records) {
        super(context, resource);
        this.signRecordList =records;
        this.resourceId = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SignRecord signRecord = signRecordList.get(position);
        View view;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        }else {
            view = convertView;
        }

        TextView signTime = view.findViewById(R.id.sign_time);
        TextView signResult = view.findViewById(R.id.sign_result);
        signTime.setText(TimeTransform.stampToTime(signRecord.getSignTime()));
        if (signRecord.isSignResult()) {
            signResult.setText("签到成功");
        }else {
            signResult.setText("签到失败");
        }

        return view;
    }

    @Override
    public int getCount() {
        return signRecordList.size();
    }


}
