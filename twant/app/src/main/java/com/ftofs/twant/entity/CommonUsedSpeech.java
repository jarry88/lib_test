package com.ftofs.twant.entity;

/**
 * 常用語、常用版式的數據結構
 * @author zwm
 */
public class CommonUsedSpeech {
    /**
     * 數據類型: 常用語
     */
    public static final int DATA_TYPE_SPEECH = 1;

    /**
     * 數據類型：常用版式
     */
    public static final int DATA_TYPE_TEMPLATE = 2;


    public CommonUsedSpeech(int dataType, String content) {
        this.dataType = dataType;
        this.content = content;
    }

    public int dataType;
    public String content;
}
