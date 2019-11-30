package com.example.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

/**
 * 权限管理
 */
public class PermissionUtil {
    private static final String TAG = "PermissionUtil";

    public static void toPermissionSetting(Activity activity) {
        if (Build.VERSION.SDK_INT<=Build.VERSION_CODES.LOLLIPOP_MR1) {
            toSystemConfig(activity);
        }else {
            try {
                toApplicationInfo(activity);
            }catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "toPermissionSetting: "+e.getMessage());
                toSystemConfig(activity);
            }
        }
    }

    //跳到应用信息activity
    public static void toApplicationInfo(Activity activity) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        localIntent.setData(Uri.fromParts("package",activity.getPackageName(),null));
        activity.startActivity(localIntent);
    }

    //跳转系统设置管理页面
    public static void toSystemConfig(Activity activity) {
        try {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            activity.startActivity(intent);
        }catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "toSystemConfig: "+e.getMessage());
        }
    }


}
