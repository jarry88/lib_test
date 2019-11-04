package com.ftofs.twant.util;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;

public class BadgeUtil {
    /**
     * 設置華為手機角標數字
     * @param context
     * @param num
     */
    public void setHuaweiBadgeNum(Context context, int num) {
        try {
            String packageName = context.getResources().getString(R.string.app_name);
            Bundle bundle = new Bundle();
            bundle.putString("package", packageName); // com.test.badge is your package name
            bundle.putString("class", packageName + ".activity.SplashActivity"); // com.test. badge.MainActivity is your apk main activity
            bundle.putInt("badgenumber", num);
            context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, bundle);
        } catch (Exception e) {
            SLog.info("Error");
        }
    }
}
