package com.wjc.parttime.util;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by yhui on 2017/4/6 0006.
 */

public class VersionMessageUtils {

    /**
     * 获取版本号
     * @return
     */
    public static int getVersionCode(Context context){
        PackageManager manager =context.getPackageManager();//获取包管理器
        try {
            //通过当前的包名获取包的信息
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),0);//获取包对象信息
            return  info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取版本名
     * @return
     */
    public static String getVersionName(Context context){
        PackageManager manager = context.getPackageManager();
        try {
            //第二个参数代表额外的信息，例如获取当前应用中的所有的Activity
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES
            );
            ActivityInfo[] activities = packageInfo.activities;
            showActivities(activities);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static void showActivities(ActivityInfo[] activities){
        for(ActivityInfo activity : activities) {
            LogUtil.i("activity=========", activity.name);
        }
    }




}
