package com.example.bean;

import java.util.List;

public class Group {
    int groupId;
    String groupName;
    String introduce;
    String adminId;
    List<User> memberList; //成员名单

    public int getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getAdminId() {
        return adminId;
    }

    public List<User> getMemberList() {
        return memberList;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public void setMemberList(List<User> memberList) {
        this.memberList = memberList;
    }
}
