package com.example.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bean.User;
import com.example.location.R;

import java.util.List;

public class GroupMemberAdapter extends RecyclerView.Adapter<GroupMemberAdapter.ViewHolder> {

    private List<User> memberList;
    private Context context;



    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.group_member_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        User user = memberList.get(position);
        viewHolder.userName.setText(user.getUserName());
    }

    public GroupMemberAdapter(Context context,List<User> memberList) {
        this.memberList = memberList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

       ImageView imageIcon;
       TextView userName;

       ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageIcon = (ImageView)itemView.findViewById(R.id.member_icon);
            userName = (TextView)itemView.findViewById(R.id.member_name);
       }
    }
}
