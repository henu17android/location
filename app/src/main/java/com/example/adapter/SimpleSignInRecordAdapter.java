package com.example.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bean.GroupSignInMessage;
import com.example.location.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SimpleSignInRecordAdapter extends RecyclerView.Adapter<SimpleSignInRecordAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    List<GroupSignInMessage> mGroupSignData;

    public SimpleSignInRecordAdapter(Context context, List<GroupSignInMessage> groupSignData){
        mContext = context;
        mGroupSignData = groupSignData;
    }

    @Override
    public SimpleSignInRecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        ViewHolder holder = null;
        mInflater = LayoutInflater.from(mContext);
        holder = new ViewHolder(mInflater.inflate(R.layout.layout_sign_record_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(SimpleSignInRecordAdapter.ViewHolder holder, int position){
        GroupSignInMessage groupSignInMessage = mGroupSignData.get(position);
        holder.mTvName.setText(groupSignInMessage.getReceiverId());
        String time = stampToTime(groupSignInMessage.getStartTime());
        holder.mTvTime.setText(time);
        if(groupSignInMessage.getResult() == 1){
            holder.mImgResult.setImageResource(R.drawable.ic_success);
        }else if(groupSignInMessage.getResult() == 0){
            holder.mImgResult.setImageResource(R.drawable.ic_fail);
        }
    }

    @Override
    public int getItemCount(){
        return mGroupSignData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImgResult;
        TextView mTvTime;
        TextView mTvName;
        public ViewHolder(View itemView) {
            super(itemView);
            mImgResult = itemView.findViewById(R.id.img_result);
            mTvTime = itemView.findViewById(R.id.tv_time);
            mTvName = itemView.findViewById(R.id.tv_name);
        }
    }
    String stampToTime(long time){
        SimpleDateFormat dateString = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat timeString = new SimpleDateFormat("HH:mm");
        String times = timeString.format(new Date(time));
        String dates = dateString.format(new Date(time));
        Date date;
        int myDate = 0;
        String week = null;
        try{
            date = dateString.parse(dates);
            Calendar cd = Calendar.getInstance();
            cd.setTime(date);
            myDate = cd.get(Calendar.DAY_OF_WEEK);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (myDate == 1) {
            week = "星期日";
        } else if (myDate == 2) {
            week = "星期一";
        } else if (myDate == 3) {
            week = "星期二";
        } else if (myDate == 4) {
            week = "星期三";
        } else if (myDate == 5) {
            week = "星期四";
        } else if (myDate == 6) {
            week = "星期五";
        } else if (myDate == 7) {
            week = "星期六";
        }
        String result = dates + " " + week + "   " + times;
        return result;
    }
}