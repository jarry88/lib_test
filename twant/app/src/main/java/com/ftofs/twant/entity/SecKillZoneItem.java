package com.ftofs.twant.entity;

public class SecKillZoneItem {
    public int scheduleId;
    public String startTime;
    public String statusText;
    public int scheduleState;  // 状态 0-即將開場 1-已開搶 2-已結束 10-等待審核 20-審核失敗
}
