package com.ftofs.twant.entity;

import es.dmoral.toasty.Toasty;

/**
 * 顯示Toast的數據
 * @author zwm
 */
public class ToastData {
    public static final int TYPE_SUCCESS = 1;
    public static final int TYPE_ERROR = 2;
    public static final int TYPE_INFO = 3;


    public ToastData(int type, String text) {
        this(type, Toasty.LENGTH_SHORT, text);
    }

    public ToastData(int type, int duration, String text) {
        this.type = type;
        this.duration = duration;
        this.text = text;
    }

    /**
     * 哪種類型  SUCCESS,ERROR,INFO等
     */
    public int type;
    /**
     * 顯示時長類型
     */
    public int duration;
    /**
     * 消息文本
     */
    public String text;
}
