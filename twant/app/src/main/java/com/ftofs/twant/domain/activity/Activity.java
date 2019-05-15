package com.ftofs.twant.domain.activity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Activity implements Serializable {
    private int activityId;

    /**
     * 活动状态
     * 0.关闭 1.开启
     */
    private int activityState ;

    /**
     * 活动类型
     */
    private int activityType ;

    /**
     * 活动数据
     */
    private String activityJson;

    /**
     * 修改日期
     */
    private Timestamp updateTime ;

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public String getActivityJson() {
        return activityJson;
    }

    public void setActivityJson(String activityJson) {
        this.activityJson = activityJson;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public int getActivityState() {
        return activityState;
    }

    public void setActivityState(int activityState) {
        this.activityState = activityState;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "activityId=" + activityId +
                ", activityState=" + activityState +
                ", activityType=" + activityType +
                ", activityJson='" + activityJson + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
