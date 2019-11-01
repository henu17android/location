package com.example.client;

import com.example.bean.User;

public class ClientMessage {
    ClientMessageType messageType;
    private User user;

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
}
