package com.example.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bean.SignRecord;
import com.example.bean.User;
import com.example.location.R;
import com.example.util.TimeTransform;
import com.hgdendi.expandablerecycleradapter.BaseExpandableRecyclerViewAdapter;

import java.util.List;

public class GroupMemberDetailsAdapter extends BaseExpandableRecyclerViewAdapter<User,SignRecord,GroupMemberDetailsAdapter.GroupVH, GroupMemberDetailsAdapter.ChildVH> {

    private List<User> mUsers;

    private Context mContext;
    public GroupMemberDetailsAdapter(List<User> users,Context context) {
        mUsers = users;
        mContext = context;
    }

    @Override
    public int getGroupCount() {
        return mUsers.size();
    }

    @Override
    public User getGroupItem(int groupIndex) {
        return mUsers.get(groupIndex);
    }

    @Override
    public GroupVH onCreateGroupViewHolder(ViewGroup parent, int groupViewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.group_member_user_list_item, parent, false);
        return new GroupVH(view);
    }

    @Override
    public void onBindGroupViewHolder(GroupVH holder, User groupBean, boolean isExpand) {
        holder.nameTv.setText(groupBean.getUserName());
        if (groupBean.isExpandable()) {
            holder.foldIv.setVisibility(View.VISIBLE);
            holder.foldIv.setImageResource(isExpand ?  R.drawable.ic_arrow_drop_up_grey_24dp : R.drawable.ic_arrow_drop_down_grey_24dp);
        } else {
            holder.foldIv.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public ChildVH onCreateChildViewHolder(ViewGroup parent, int childViewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.group_member_user_sign_record_item, parent, false);
        return new ChildVH(view);
    }

    @Override
    public void onBindChildViewHolder(ChildVH holder, User groupBean, SignRecord signRecord) {
        if (signRecord.isSignResult()){
            holder.recordResult.setText("签到成功");
        }else {
            holder.recordResult.setText("签到失败");
        }

        holder.recordTime.setText(TimeTransform.stampToTime(signRecord.getSignTime()));
    }

    static class GroupVH extends BaseExpandableRecyclerViewAdapter.BaseGroupViewHolder {
        ImageView foldIv;//下拉箭头
        ImageView avatar;//头像
        TextView nameTv;//用户名

        GroupVH(View itemView) {
            super(itemView);
            foldIv = (ImageView) itemView.findViewById(R.id.fold_arrow_view);
            nameTv = (TextView) itemView.findViewById(R.id.user_name);
            avatar = (ImageView) itemView.findViewById(R.id.user_avatar);
            avatar.setImageResource(R.drawable.ic_person_black_24dp);//头像暂时用图标代替
        }

        @Override
        protected void onExpandStatusChanged(RecyclerView.Adapter relatedAdapter, boolean isExpanding) {
            foldIv.setImageResource(isExpanding ? R.drawable.ic_arrow_drop_up_grey_24dp : R.drawable.ic_arrow_drop_down_grey_24dp);
        }
    }

    static class ChildVH extends RecyclerView.ViewHolder {
        TextView recordTime;
        TextView recordResult;

        ChildVH(View itemView) {
            super(itemView);
            recordTime = (TextView) itemView.findViewById(R.id.sign_record_time);
            recordResult = (TextView) itemView.findViewById(R.id.sign_record_result);
        }
    }


}
