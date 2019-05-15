package com.ftofs.twant.vo.activity;

import com.ftofs.twant.domain.activity.Activity;

/**
 * Created by cj on 2017-5-18.
 */
public class ActivityVo {
    /**
     * 活动id
     */
    private int activityId;
    /**
     * 活动状态
     */
    private int activityState ;
    /**
     * 活动类型
     */

    private int activityType ;

    /**
     * 活动数据
     */
    private  Object activityJson;


    public ActivityVo(Activity activity) {
        this.activityId = activity.getActivityId();
        this.activityType = activity.getActivityType();
        this.activityState = activity.getActivityState();
    }

    public <T> ActivityVo(Activity activity , T activityJson) {
        this.activityId = activity.getActivityId();
        this.activityType = activity.getActivityType();
        this.activityJson = activityJson ;
        this.activityState = activity.getActivityState();
    }



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

    public Object getActivityJson() {
        return activityJson;
    }

    public void setActivityJson(Object activityJson) {
        this.activityJson = activityJson;
    }

    public int getActivityState() {
        return activityState;
    }

    public void setActivityState(int activityState) {
        this.activityState = activityState;
    }

    @Override
    public String toString() {
        return "ActivityVo{" +
                "activityId=" + activityId +
                ", activityState=" + activityState +
                ", activityType=" + activityType +
                ", activityJson=" + activityJson +
                '}';
    }
}
