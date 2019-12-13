package com.example.util;

import android.app.Activity;

import java.util.Stack;

public class ActivityUtil {
    private static Stack<Activity> activityStack;
    private static ActivityUtil instance;
    private ActivityUtil(){}
    public static ActivityUtil getScreenManager(){
        if(instance == null){
            instance = new ActivityUtil();
        }
        return instance;
    }

    public void popActivity(Activity activity){
        if(activity != null){
            activity.finish();
            activityStack.remove(activity);
        }
    }

    public Activity currentActivity(){
        Activity activity = null;
        if(!activityStack.empty())
            activity = activityStack.lastElement();
        return activity;
    }

    public void pushActivity(Activity activity){
        if(activityStack == null){
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    public void exit(){
        for (Activity activity: activityStack) {
            activity.finish();
        }
    }
}
