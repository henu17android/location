package com.example.client;

public enum MessageType {
    REGISTER,  //注册
    LOGIN,   //登录
    CREATE_GROUP,  //创建群聊
    HEART_BEAT,  //心跳发送
    APPLY_JOIN_GROUP,  //申请加入群聊
    SEARCH_GROUP,  //查找群聊
    GET_GROUPS,  //请求服务端存储的群聊
    RECEIVE_MEMBER //群主收到接纳新成员请求

}
