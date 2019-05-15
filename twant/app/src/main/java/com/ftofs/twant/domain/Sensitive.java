package com.ftofs.twant.domain;

import java.io.Serializable;
import java.sql.Timestamp;

public class Sensitive implements Serializable {
    /**
     * 编号
     */
    private int sensitiveId;

    /**
     * 分词
     */
    private String sensitiveWord;

    /**
     * 是否使用:0不使用，1使用
     */
    private int isUse;

    /**
     * 添加时间
     */
    private Timestamp addTime;

    public int getSensitiveId() {
        return sensitiveId;
    }

    public void setSensitiveId(int sensitiveId) {
        this.sensitiveId = sensitiveId;
    }

    public String getSensitiveWord() {
        return sensitiveWord;
    }

    public void setSensitiveWord(String sensitiveWord) {
        this.sensitiveWord = sensitiveWord;
    }

    public int getIsUse() {
        return isUse;
    }

    public void setIsUse(int isUse) {
        this.isUse = isUse;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    @Override
    public String toString() {
        return "Sensitive{" +
                "sensitiveId=" + sensitiveId +
                ", sensitiveWord='" + sensitiveWord + '\'' +
                ", isUse=" + isUse +
                ", addTime=" + addTime +
                '}';
    }
}

