package com.example.client;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.LocationApp;

import java.io.DataOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 发送消息的线程池维护类
 */
public class MessagePostPool {


     public static DataOutputStream outputStream;


    //根据系统资源进行设置线程池的大小
    private static final int NCPU = Runtime.getRuntime().availableProcessors();

    private static final ExecutorService executor = new ThreadPoolExecutor(NCPU,NCPU*2,
            10L,TimeUnit.SECONDS,
            new LinkedBlockingDeque<Runnable>(20),
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static void sendMessage(ClientMessage clientMessage) {
        String msg = JSON.toJSONString(clientMessage);
        Log.d("sendMessage", "sendMessage: "+msg);
        OutputThread thread = new OutputThread(outputStream,msg);
        executor.execute(thread);
    }

}
