package com.example.client;

import com.example.bean.Group;
import com.example.bean.User;

//客户端发送消息
public class ClientMessage {
    ClientMessageType messageType; //
    private User user;
    private Group group;

    public Group getGroup() {
        return group;
    }

    public ClientMessageType getMessageType() {
        return messageType;
    }

    public User getUser() {
        return user;
    }

    public void setMessageType(ClientMessageType messageType) {
        this.messageType = messageType;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
