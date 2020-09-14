package com.ftofs.twant.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.entity.EBMessage;
import com.gzp.lib_common.utils.SLog;

import org.greenrobot.eventbus.EventBus;

public class MsgNotifyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String huanxin_id = intent.getStringExtra(Constant.MSG_NOTIFY_HXID);
//        ChatUser chatUser = LitePal.findUser(huanxin_id);
        SLog.info("發送跳轉信息");
        EventBus.getDefault().post(new EBMessage(EBMessageType.MESSAGE_NOTIFICATION_INTENT,huanxin_id));
    }
}