package com.example.client;

public enum MessageType {
    REGISTER,  //注册
    LOGIN,   //登录
    CREATE_GROUP,  //创建群聊
    HEART_BEAT,  //心跳发送
    APPLY_JOIN_GROUP,  //申请加入群聊
    SEARCH_GROUP,  //查找群聊
    GET_GROUPS,  //请求服务端存储的群聊
    APPLY_JOIN_GROUP_RESULT, //群主处理加群请求结果
    SET_UP_SIGN, //发起签到
    GET_TO_SIGN //通知签到

}
