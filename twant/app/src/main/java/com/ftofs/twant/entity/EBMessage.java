package com.ftofs.twant.entity;

import org.greenrobot.eventbus.EventBus;

/**
 * Event Bus Message
 * @author zwm
 */
public class EBMessage {
    /**
     * 添加購物車完成的消息
     */
    public static final int MESSAGE_TYPE_ADD_CART = 1;

    public EBMessage(int messageType, String data) {
        this.messageType = messageType;
        this.data = data;
    }

    public int messageType;
    public String data;

    /**
     * 投遞消息
     * @param messageType
     * @param data
     */
    public static void postMessage(int messageType, String data) {
        EventBus.getDefault().post(new EBMessage(messageType, data));
    }
}
