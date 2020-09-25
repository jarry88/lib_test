//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.wzp.mvvmsmart.net.net_utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.pm.ApplicationInfo;

import com.wzq.mvvmsmart.net.net_utils.MmkvUtils;
import com.wzq.mvvmsmart.utils.KLog;
import java.lang.reflect.InvocationTargetException;

public class MetaDataUtil {
    private static final String TAG = MetaDataUtil.class.getSimpleName();
    @SuppressLint({"StaticFieldLeak"})
    private static Application sApplication;

    public MetaDataUtil() {
    }

    public static Application getApp() {
        if (sApplication == null) {
            sApplication = getApplicationByReflect();
        }

        return sApplication;
    }

    private static Application getApplicationByReflect() {
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke((Object)null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }

            return (Application)app;
        } catch (NoSuchMethodException var3) {
            var3.printStackTrace();
        } catch (IllegalAccessException var4) {
            var4.printStackTrace();
        } catch (InvocationTargetException var5) {
            var5.printStackTrace();
        } catch (ClassNotFoundException var6) {
            var6.printStackTrace();
        }

        throw new NullPointerException("u should init first");
    }

    public static String getBaseUrl() {
        String baseUrl = "";
        int serviceEnvironment = getEnvironment();
        switch(serviceEnvironment) {
            case 0:
            case 3:
                baseUrl = "https://f2.twant.com/api/";
                break;
            case 1:
                baseUrl = "http://192.168.5.28/api/";
                break;
            case 2:
                baseUrl = "https://192.168.5.29/api/";
                break;
            case 4:
                baseUrl = "https://f3.twant.com/api/";
                break;
            default:
                baseUrl = "https://f2.twant.com/api/";
        }

        KLog.INSTANCE.e("baseUrl:" + baseUrl);
        return baseUrl;
    }

    private static int getEnvironment() {
        try {
            ApplicationInfo appInfo = getApp().getPackageManager().getApplicationInfo(getApp().getPackageName(), 128);
            boolean changeEnvironmentEnable = appInfo.metaData.getBoolean("CHANGEENVIRONMENTENABLE");
            int environment = appInfo.metaData.getInt("ENVIRONMENT");
            if (changeEnvironmentEnable) {
                environment = MmkvUtils.getIntValue("env");
            }

            if (environment == 0) {
                KLog.INSTANCE.e("environment:" + environment + "--生产环境");
            } else if (environment == 1) {
                KLog.INSTANCE.e("environment:" + environment + "--28环境");
            } else if (environment == 2) {
                KLog.INSTANCE.e("environment:" + environment + "--29环境");
            } else if (environment == 3) {
                KLog.INSTANCE.e("environment:" + environment + "--生产环境");
            } else if (environment == 4) {
                KLog.INSTANCE.e("environment:" + environment + "--f3环境");
            }

            return environment;
        } catch (Exception var3) {
            KLog.INSTANCE.e("environment:2--生产环境");
            return 2;
        }
    }

    public static String getChannel() {
        try {
            ApplicationInfo appInfo = getApp().getPackageManager().getApplicationInfo(getApp().getPackageName(), 128);
            return appInfo.metaData.getString("CHANNEL_ID");
        } catch (Exception var1) {
            return null;
        }
    }
}
