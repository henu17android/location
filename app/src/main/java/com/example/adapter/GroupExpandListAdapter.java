package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bean.Group;
import com.example.location.R;
import java.util.List;


public class GroupExpandListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String> mGroupList;
    private List<List<Group>> mChildList;

    public GroupExpandListAdapter(Context context, List<String> group, List<List<Group>> child){
        mContext = context;
        mGroupList = group;
        mChildList = child;
    }

    @Override
    public int getGroupCount(){
        return mGroupList.size();
    }

    /**
     * @param groupPosition 父List的编号
     * @return 字List的长度
     */
    @Override
    public int getChildrenCount(int groupPosition){
        return mChildList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosittion){
        return mGroupList.get(groupPosittion);
    }

    @Override
    public Object getChild(int groupPositon, int childPositon){
        return mChildList.get(groupPositon).get(childPositon);
    }

    @Override
    public long getGroupId(int groupPosition){
        return groupPosition;
    }


    @Override
    public long getChildId(int groupPositon, int childPositon){
        return childPositon;
    }

    @Override
    public boolean hasStableIds(){
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent){
        GroupViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.expandable_list_group, parent,false);
            viewHolder = new GroupViewHolder();
            viewHolder.groupImage = convertView
                    .findViewById(R.id.expandable_list_group_image);
            viewHolder.groupName = convertView
                    .findViewById(R.id.expandable_list_group_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (GroupViewHolder)convertView.getTag();
        }

        if (isExpanded){
            viewHolder.groupImage.setImageResource(R.drawable.list_image_open);
        }else {
            viewHolder.groupImage.setImageResource(R.drawable.list_image_close);
        }
        viewHolder.groupName.setText(mGroupList.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPositon, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent){
        Group group = mChildList.get(groupPositon).get(childPosition);
        ChildViewHolder viewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.expandable_list_child, parent, false);
            viewHolder = new ChildViewHolder();
            viewHolder.childImage = convertView.findViewById(R.id.expandable_list_child_image);
            viewHolder.childName = convertView.findViewById(R.id.expandable_list_child_name);
            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ChildViewHolder)convertView.getTag();
        }
        //TODO 设置图片viewHolder.childImage.set
        viewHolder.childName.setText(group.getGroupName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPositon, int childPosition){
        return true;
    }

    private class GroupViewHolder{
        private ImageView groupImage;
        private TextView groupName;
    }

    private class ChildViewHolder{

        private ImageView childImage;
        private TextView childName;
    }



}

