package com.example.client;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.bean.Group;
import com.example.bean.GroupMessage;
import com.example.bean.GroupSignInMessage;
import com.example.bean.User;
import com.example.util.DataUtil;

import java.io.Serializable;
import java.util.List;

//客户端发送消息
public class ClientMessage implements Serializable {

    //群主发起签到
    private GroupSignInMessage groupSignInMessage;
    private GroupMessage groupMessage;
    private int messageId;  //某次签到对应的id
    private List<GroupSignInMessage> signInMessages;
    private MessageType messageType; //
    private User user;
    private Group group;
    @JSONField(name = "isSuccess")
    private boolean isSuccess;

    private String phoneNumber;
    private List<Group> createGroups;  //创建的组
    private List<Group> joinGroups;   //加入的组

    private String groupId; //传输组Id
    private int stateCode;  //错误状态码
    private List<User> memberList; //组成员
    private String to;

    public void setGroupSignInMessage(GroupSignInMessage groupSignInMessage) {
        this.groupSignInMessage = groupSignInMessage;
    }

    public void setGroupMessage(GroupMessage groupMessage) {
        this.groupMessage = groupMessage;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public void setSignInMessages(List<GroupSignInMessage> signInMessages) {
        this.signInMessages = signInMessages;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

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

    public GroupSignInMessage getGroupSignInMessage() {
        return groupSignInMessage;
    }

    public GroupMessage getGroupMessage() {
        return groupMessage;
    }

    public int getMessageId() {
        return messageId;
    }

    public List<GroupSignInMessage> getSignInMessages() {
        return signInMessages;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<Group> getCreateGroups() {
        return createGroups;
    }

    public List<Group> getJoinGroups() {
        return joinGroups;
    }

    public List<User> getMemberList() {
        return memberList;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCreateGroups(List<Group> createGroups) {
        this.createGroups = createGroups;
    }

    public void setJoinGroups(List<Group> joinGroups) {
        this.joinGroups = joinGroups;
    }

    public void setMemberList(List<User> memberList) {
        this.memberList = memberList;
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
}