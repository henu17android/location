package com.example.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.location.R;

/**
 * 通知 兼容写法
 * @author 祁好燃
 */
public class NotificationUtil extends ContextWrapper {

    private NotificationManager manager;
    public static final String id = "push";
    public static final String name = "push_channel";

    public NotificationUtil(Context base) {
        super(base);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel() {
        NotificationChannel channel = null;
            channel = new NotificationChannel(id,name,NotificationManager.IMPORTANCE_HIGH);
            getManager().createNotificationChannel(channel);

    }

    private NotificationManager getManager() {
        if (manager==null) {
            manager =  (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getChannelNotification(String title, String content){
            return new Notification.Builder(getApplicationContext(),id)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(R.drawable.user_image)
                    .setAutoCancel(true);

    }

    public NotificationCompat.Builder getNotification_25(String title,String content) {
        return new NotificationCompat.Builder(getApplicationContext()).setContentTitle(title)
                .setSmallIcon(R.drawable.user_image)
                .setAutoCancel(true);
    }

    public void sendNotification(String title,String content) {
        if (Build.VERSION.SDK_INT>=26) {
            createNotificationChannel();
            Notification notification = getChannelNotification(title, content).build();
            getManager().notify(1,notification);
        }else {
            Notification notification = getNotification_25(title,content).build();
            getManager().notify(1,notification);
        }
    }

    //判断用户通知栏权限是否开启
    public static boolean isNotificationEnabled(Context context) {
        return  NotificationManagerCompat.from(context).areNotificationsEnabled();
    }

    //引导用户打开权限
    public static void openPush(Activity activity) {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE,activity.getPackageName());
            intent.putExtra(Settings.EXTRA_CHANNEL_ID,activity.getApplicationInfo().uid);
            activity.startActivity(intent);
        }else {
           PermissionUtil.toPermissionSetting(activity);
        }
    }
}
