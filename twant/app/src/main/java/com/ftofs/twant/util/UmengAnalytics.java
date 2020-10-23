package com.ftofs.twant.util;

import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.config.Config;
import com.umeng.analytics.MobclickAgent;


import java.util.Map;

public class UmengAnalytics {
    public static void onPageStart(String pageName) {
        if (Config.PROD) {
            MobclickAgent.onPageStart(pageName);
        }
    }

    public static void onPageEnd(String pageName) {
        if (Config.PROD) {
            MobclickAgent.onPageEnd(pageName);
        }
    }

    public static void onEventObject(String actionName, Map<String, Object> analyticsDataMap) {
        if (Config.PROD) {
            MobclickAgent.onEventObject(TwantApplication.Companion.get(), actionName, analyticsDataMap);
        }
    }
}
