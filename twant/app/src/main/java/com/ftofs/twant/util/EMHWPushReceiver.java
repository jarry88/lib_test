package com.ftofs.twant.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gzp.lib_common.utils.SLog;

public class EMHWPushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        super.onReceive(context,intent);
        SLog.info("EMHWPushReceiver::onReceive");
    }
}
