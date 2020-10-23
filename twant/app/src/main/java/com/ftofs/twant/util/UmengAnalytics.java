package com.ftofs.twant.util;

import com.ftofs.twant.config.Config;
import com.umeng.analytics.MobclickAgent;

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
}
