package com.ftofs.twant.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class BadgeUtil {
    /**
     * 設置華為手機角標數字
     * @param context
     * @param num
     */
    public static void setHuaweiBadgeNum(Context context, int num) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString("package", context.getPackageName()); // com.test.badge is your package name
            bundle.putString("class", Util.getLauncherClassName(context)); // com.test. badge.MainActivity is your apk main activity
            bundle.putInt("badgenumber", num);
            context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, bundle);
        } catch (Exception e) {
            SLog.info("Error");
        }
    }


    public static void setXiaoMiBadgeNum(Context context, int num) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String title = context.getString(R.string.app_name);
        String content = "您有" + num + "條未讀消息";
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(title).setContentText(content).setSmallIcon(R.mipmap.ic_launcher);

        Notification notification = builder.build();

        try {

            Field field = notification.getClass().getDeclaredField("extraNotification");

            Object extraNotification = field.get(notification);

            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);

            method.invoke(extraNotification, num);

        } catch (Exception e) {

            e.printStackTrace();

        }

        mNotificationManager.notify(0,notification);
    }


    public static void setSamsungBadgeNum(Context context, int count) {
        // 获取你当前的应用
        String launcherClassName = Util.getLauncherClassName(context);
        SLog.info("launcherClassName[%s]", launcherClassName);
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }


    public static void setSonyBadgeNum(Context context, int count) {
        String launcherClassName = Util.getLauncherClassName(context);
        if (StringUtil.isEmpty(launcherClassName)) {
            return;
        }
        try {
            //官方给出方法
            ContentValues contentValues = new ContentValues();
            contentValues.put("badge_count", count);
            contentValues.put("package_name", context.getPackageName());
            contentValues.put("activity_name", launcherClassName);
            SonyAsyncQueryHandler asyncQueryHandler = new SonyAsyncQueryHandler(context
                    .getContentResolver());
            asyncQueryHandler.startInsert(0, null, Uri.parse("content://com.sonymobile.home" +
                    ".resourceprovider/badge"), contentValues);
            return;
        } catch (Exception e) {
            try {
                //网上大部分使用方法
                Intent intent = new Intent("com.sonyericsson.home.action.UPDATE_BADGE");
                intent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", count > 0);
                intent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME",
                        launcherClassName);
                intent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", String
                        .valueOf(count));
                intent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context
                        .getPackageName());
                context.sendBroadcast(intent);
                return;
            } catch (Exception e1) {
                e1.printStackTrace();
                return;
            }
        }
    }

    static class SonyAsyncQueryHandler extends AsyncQueryHandler {
        SonyAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }
    }

    /**
     * 設置應用圖標角標
     * @param context
     * @param num
     */
    public static void setBadgeNum(Context context, int num) {
        int vendorType =Vendor.getVendorType();

        if (vendorType == Vendor.VENDOR_OTHER) {
            return;
        }

        if (vendorType == Vendor.VENDOR_SONY) {
            setSonyBadgeNum(context, num);
        } else if (vendorType == Vendor.VENDOR_HUAWEI) {
            setHuaweiBadgeNum(context, num);
        } else if (vendorType == Vendor.VENDOR_XIAOMI) {
            setXiaoMiBadgeNum(context, num);
        } else if (vendorType == Vendor.VENDOR_SAMSUNG) {
            setSamsungBadgeNum(context, num);
        }
    }
}
