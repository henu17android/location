package com.example.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class GroupMessage implements Serializable {

    @JSONField(name = "MessageId")
    private int messageId;
    @JSONField(name = "GroupId")
    private String groupId;
    @JSONField(name = "FromId")
    private int fromId; //发起人id,groupId对应的adminId

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }
}
