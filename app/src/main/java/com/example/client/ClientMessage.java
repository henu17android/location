package com.example.client;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.bean.Group;
import com.example.bean.User;
import com.example.util.DataUtil;

import java.io.Serializable;
import java.util.List;

//客户端发送消息
public class ClientMessage implements Serializable {
    MessageType messageType; //
    private User user;
    private Group group;
    @JSONField(name = "isSuccess")
    private boolean isSuccess;

    private String phoneNumber;
    private List<Group> create_groups;  //创建的组
    private List<Group> join_groups;   //加入的组

    private String groupId; //传输组Id
    private int stateCode;  //错误状态码
    private List<User> memberList; //组成员

    public ClientMessage() {
        this.phoneNumber = DataUtil.USER_NUMBER;  //构造函数中默认添加当前用户的号码
    }


    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public String getUserId() {
        return phoneNumber;
    }



    public String getGroupId() {
        return groupId;
    }

    public void setUserId(String userId) {
        this.phoneNumber = userId;
    }

    public List<Group> getCreate_groups() {
        return create_groups;
    }

    public List<Group> getJoin_groups() {
        return join_groups;
    }

    public void setCreate_groups(List<Group> create_groups) {
        this.create_groups = create_groups;
    }

    public void setJoin_groups(List<Group> join_groups) {
        this.join_groups = join_groups;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Group getGroup() {
        return group;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public User getUser() {
        return user;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    @Override
    public String toString() {
        return "ClientMessage{"+
                "messageType='"+messageType+'\''+
                ",user="+user+'\''+
                ",group"+group+'\''+"}";
    }
}
