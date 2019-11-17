package com.example.bean;

import java.util.List;

public class Group {
    int GroupId;
    String GroupName;
    String Introduce;
    int AdminId;
    List<User> UserList; //成员名单

    public int getGroupId() {
        return GroupId;
    }

    public String getGroupName() {
        return GroupName;
    }

    public String getIntroduce() {
        return Introduce;
    }

    public int getAdminId() {
        return AdminId;
    }

    public List<User> getUserList() {
        return UserList;
    }

    public void setGroupId(int groupId) {
        GroupId = groupId;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public void setIntroduce(String introduce) {
        Introduce = introduce;
    }

    public void setAdminId(int adminId) {
        AdminId = adminId;
    }

    public void setUserList(List<User> userList) {
        UserList = userList;
    }
}
