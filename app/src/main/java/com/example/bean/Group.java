package com.example.bean;

public class Group {
    int groupId;
    String groupName;
    String introduce;
    int adminId;

    public int getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getIntroduce() {
        return introduce;
    }

    public int getAdminId() {
        return adminId;
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

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }
}
