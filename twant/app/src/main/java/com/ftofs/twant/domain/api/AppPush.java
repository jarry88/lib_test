package com.ftofs.twant.domain.api;

import java.sql.Timestamp;

public class AppPush {
    /**
     * 编号
     */
    private int pushId;

    /**
     * 消息
     */
    private String message = "";

    /**
     * 标题
     */
    private String title = "";

    /**
     * 子标题
     */
    private String subTitle = "";

    /**
     * 操作类型
     */
    private String type;

    /**
     * 操作数据
     */
    private String data;

    /**
     * 创建时间
     */
    private Timestamp createTime = new Timestamp(0);

    /**
     * 编号
     */
    private int state;

    public int getPushId() {
        return pushId;
    }

    public void setPushId(int pushId) {
        this.pushId = pushId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "AppPush{" +
                "pushId=" + pushId +
                ", message='" + message + '\'' +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", type='" + type + '\'' +
                ", data='" + data + '\'' +
                ", createTime=" + createTime +
                ", state=" + state +
                '}';
    }
}

