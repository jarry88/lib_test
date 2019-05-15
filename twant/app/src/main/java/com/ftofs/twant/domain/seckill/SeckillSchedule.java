package com.ftofs.twant.domain.seckill;

import java.sql.Timestamp;

public class SeckillSchedule {
    /**
     * 秒杀排期编号
     */
    private int scheduleId;

    /**
     * 排期名称
     */
    private String scheduleName;

    /**
     * 开始时间
     */
    private Timestamp startTime = new Timestamp(0);

    /**
     * 结束时间
     */
    private Timestamp endTime = new Timestamp(0);

    /**
     * 状态文字
     */
    private int scheduleState = 1;

    /**
     * 倒计时时间
     */
    private Timestamp countDownTime;

    /**
     * 状态文字
     */
    private String scheduleStateText = "";

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public int getScheduleState() {
        return scheduleState;
    }

    public void setScheduleState(int scheduleState) {
        this.scheduleState = scheduleState;
    }

    public String getScheduleStateText() {
       return scheduleStateText;
    }

    public void setScheduleStateText(String scheduleStateText) {
        this.scheduleStateText = scheduleStateText;
    }

    public Timestamp getCountDownTime() {
        if (countDownTime != null) {
            return countDownTime;
        }
        return startTime;
    }

    public void setCountDownTime(Timestamp countDownTime) {
        this.countDownTime = countDownTime;
    }

    @Override
    public String toString() {
        return "SeckillSchedule{" +
                "scheduleId=" + scheduleId +
                ", scheduleName='" + scheduleName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", scheduleState=" + scheduleState +
                ", countDownTime=" + countDownTime +
                ", scheduleStateText='" + scheduleStateText + '\'' +
                '}';
    }
}
