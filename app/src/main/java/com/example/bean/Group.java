package com.example.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

public class Group implements Serializable {
    private int Id;
    @JSONField(name = "GroupId")
    private String groupId;
    @JSONField(name = "GroupName")
    private String groupName;
    @JSONField(name = "Introduce")
    private String introduce;
    @JSONField(name = "AdminId")
    private String adminId;

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public Group() {

    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getGroupId() {
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



    public void setGroupId(String groupId) {
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





}
