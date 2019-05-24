package com.ftofs.twant.domain.refund;

import java.io.Serializable;


public class RefundReason implements Serializable {
    /**
     * 退款原因id
     */
    private int reasonId;

    /**
     * 原因内容
     */
    private String reasonInfo;

    /**
     * 属性排序
     */
    private int reasonSort;

    /**
     * 记录时间
     */
    private String updateTime;

    public int getReasonId() {
        return reasonId;
    }

    public void setReasonId(int reasonId) {
        this.reasonId = reasonId;
    }

    public String getReasonInfo() {
        return reasonInfo;
    }

    public void setReasonInfo(String reasonInfo) {
        this.reasonInfo = reasonInfo;
    }

    public int getReasonSort() {
        return reasonSort;
    }

    public void setReasonSort(int reasonSort) {
        this.reasonSort = reasonSort;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "RefundReason{" +
                "reasonId=" + reasonId +
                ", reasonInfo='" + reasonInfo + '\'' +
                ", reasonSort=" + reasonSort +
                ", updateTime=" + updateTime +
                '}';
    }
}
