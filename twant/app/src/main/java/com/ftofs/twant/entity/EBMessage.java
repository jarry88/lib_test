package com.ftofs.twant.entity;

import com.ftofs.twant.constant.EBMessageType;

import org.greenrobot.eventbus.EventBus;

/**
 * Event Bus Message
 * @author zwm
 */
public class EBMessage {
    public EBMessage(EBMessageType messageType, Object data) {
        this.messageType = messageType;
        this.data = data;
    }

    public EBMessageType messageType;
    public Object data;

    /**
     * 投遞消息
     * @param messageType
     * @param data
     */
    public static void postMessage(EBMessageType messageType, Object data) {
        EventBus.getDefault().post(new EBMessage(messageType, data));
    }
}
