package com.ftofs.twant.domain.seckill;



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
    private String startTime = "";

    /**
     * 结束时间
     */
    private String endTime = "";

    /**
     * 状态文字
     */
    private int scheduleState = 1;

    /**
     * 倒计时时间
     */
    private String countDownTime;

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
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

    public String getCountDownTime() {
        if (countDownTime != null) {
            return countDownTime;
        }
        return startTime;
    }

    public void setCountDownTime(String countDownTime) {
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
